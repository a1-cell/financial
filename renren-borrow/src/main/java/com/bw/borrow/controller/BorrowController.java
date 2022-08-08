package com.bw.borrow.controller;

import com.bw.borrow.config.ElasticsearchConfig;
import com.bw.borrow.service.BorrowService;
import com.bw.borrow.utils.Filetomutportfile;
import com.bw.borrow.utils.ImageWatermarkUtils;
import com.bw.borrow.utils.OSSUploadUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.renren.common.borrow.Borrow;
import io.renren.common.entity.Rechruld;
import io.renren.common.es.BackList;
import io.renren.common.product.Product;
import io.renren.common.borrow.Rule;
import io.renren.common.result.Result;
import io.swagger.models.auth.In;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequestMapping("/v1")
@RestController
@CrossOrigin
public class BorrowController extends Thread{
    @Autowired
    BorrowService borrowService;
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    RedissonClient redissonClient;

    @Override
    public void run() {
        for (int i=0;i<10;i++){
            System.out.println(i);
        }
    }



    //黑名单查询剩余次数
    @GetMapping("/getnum/{id}")
    public Integer getUnm(@PathVariable Integer id){
        Integer num=borrowService.getNum(id);
        return num;
    }
    //购买查询次数
    @GetMapping("/resh/{radio}/{id}")
    @Transactional
    public Result resh(@PathVariable Integer radio,@PathVariable Integer id) throws InterruptedException {
        //查询是哪种规格
        Rechruld rechruld=borrowService.getRechruldById(radio);
        //调用支付

        RLock lock = redissonClient.getLock("lock"+id);
        boolean b = lock.tryLock(10, 5, TimeUnit.SECONDS);
        if(b){
            try {
                borrowService.updateRechruldNum(id,rechruld.getNum());
            }finally {
                lock.unlock();
            }
            return new Result(true,"购买成功!","");
        }else{
            return new Result(true,"购买失败，请重新购买!","");
        }
    }

    //定时任务查询黑名单库添加到es
    @PostMapping("/sync")
    public Result blacklist(){
        List<BackList> list=borrowService.blacklist();
        for (BackList backList : list) {
            IndexRequest indexRequest = new IndexRequest("bank", "blacklist");
            indexRequest.id(String.valueOf(backList.getId()));
            Map map=new HashMap();
            map.put("userid",backList.getUserid());
            map.put("money",backList.getMoney());
            map.put("username",backList.getUsername());
            map.put("seenum",backList.getSeenum());
            map.put("overduenum",backList.getOverduenum());
            indexRequest.source(map);
            try {
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Result(true,"添加ES成功!","");
    }
    //查询ES
    @PostMapping("/getEs")
    public Result getEs(@RequestBody BackList blackList) throws Exception {
        //判断查询剩余次数
        Integer num = borrowService.getNum(blackList.getId());
        if(num<=0){
            return new Result(false,"您的查询次数已用完，请及时充值!","");
        }
        //修改查询次数
        borrowService.updateNum(blackList.getId());
        //创建查询请求
        SearchRequest searchRequest = new SearchRequest("bank");
        searchRequest.types("blacklist");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if(blackList.getUsername()!=null && blackList.getUsername()!=""){
            //添加搜索条件
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(blackList.getUsername(), "username");
            searchSourceBuilder.query(multiMatchQueryBuilder);
        }
        //加入查询请求
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        TotalHits totalHits = hits.getTotalHits();
        long value = totalHits.value;
        System.out.println(value);
        SearchHit[] data = hits.getHits();
        List list=new ArrayList();
        for (SearchHit obj : data) {
            int id = obj.docId();
            Map<String, Object> map = obj.getSourceAsMap();
            map.put("id",id);
            list.add(map);
        }
        return new Result(true,"查询成功!",list);
    }
    //xxl-job自动投标
    @XxlJob("rule")
    @GetMapping("/ab")
    public void test(){
        //System.out.println("成功!111111");
        //查询所有自动投标
        List<Rule> list=borrowService.getRuleList();
        //feign调用查询标
        //Result productList = productClient.getProductList();
        List<Product> list1=borrowService.getProductList();
        for (Rule rule : list) {
            BigDecimal ratemin = rule.getRatemin();
            BigDecimal ratemax = rule.getRatemax();
            BigDecimal a = ratemin.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal b = ratemax.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            int count=0;
            for (Product product : list1) {
                if(a.compareTo(product.getProductRate())<0 && b.compareTo(product.getProductRate())>0){
                   // System.out.println("自动投标成功!");
                    System.out.println(a+"---"+product.getProductRate()+"------"+b);
                    count++;
                }
            }
            System.out.println(rule.getName()+"投标完成，投标次数:"+count);
        }
    }



    //开启自动投标添加规则
    @PostMapping("/addrule")
    public Result addrule(@RequestBody Rule rule){
        int count=borrowService.checkName(rule.getName());
        if(count>=1){
            return new Result(false,"已存在","");
        }
        rule.setStatue(1);
        borrowService.addrule(rule);
        return new Result(true,"成功","");
    }
    //查看自动投标
    @GetMapping("/getrule")
    public Result getrule(String name){
        Rule rule=borrowService.getrule(name);
        return new Result(true,"查询成功",rule);
    }
    //关闭自动投标
    @GetMapping("/norul")
    public Result norul(String name){
        Rule rule=borrowService.checkStatue(name);
        if(rule.getStatue()==0){
            return new Result(false,"已关闭","");
        }
        borrowService.norul(name);
        return new Result(true,"关闭成功","");
    }

    //借款查询所有
    @GetMapping("/getlist")
    public Result getlist(){
        List<Borrow> list=borrowService.getlist();
        return new Result(true,"查询成功!",list);
    }

    //借款
    @PostMapping("/add")
    public Result addBorrow(@RequestBody Borrow borrow){

        BorrowController borrowController = new BorrowController();
        borrowController.run();
        //设置用户id0
        borrow.setUserid(3);
        borrow.setBorrowRen("mlove");
        borrow.setTid(0);
        borrow.setTtid(0);
        borrowService.add(borrow);
        return new Result(true,"借款成功!","");
    }

    @PostMapping("/upload2")
    public String upload2(MultipartFile file) throws IOException {
        String upload = OSSUploadUtil.upload(file);
        return upload;
    }
    //上传图片
    @PostMapping("/upload")
    public String upload(MultipartFile file) throws Exception {
        String nameimg = UUID.randomUUID().toString();
        nameimg=nameimg.substring(0,10);
        String filenameimg = file.getOriginalFilename();
        String extName = filenameimg.substring(filenameimg.lastIndexOf("."));
        filenameimg = nameimg + extName;
        File fileimg = new File("");
        String canonicalPath = fileimg.getCanonicalPath();
        String url=canonicalPath+"\\renren-borrow\\src\\main\\resources\\public\\";
            OutputStream os = null;
            InputStream inputStream = null;
            String fileName = null;
            try {
                inputStream = file.getInputStream();
                fileName = file.getOriginalFilename();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // 2、保存到临时文件
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流保存到本地文件
                File tempFile = new File(url);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                os = new FileOutputStream(tempFile.getPath() + File.separator + filenameimg);
                // 开始读取
                while ((len = inputStream.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 完毕，关闭所有链接
                try {
                    os.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        //图片加水印
        String name="最可爱最漂亮的女孩,我爱你.";
        ImageWatermarkUtils.markImageByText(name,url+filenameimg,null,null);
        //Thread.sleep(3000);
        String substring = filenameimg.substring(0, filenameimg.lastIndexOf("."));
        substring=substring+"_water.png";
        File file1 = new File(url + substring);

       // MultipartFile multipartFile = getFile(file1);
        String upload = OSSUploadUtil.upload(file);

        return upload;
    }
    //上传阿里云测试

    /**
     * 查询所有审核通过的借款申请
     * @return
     */
    @GetMapping("/list")
    public Result getBorrowList() throws JsonProcessingException {
        List<Product> list=borrowService.getList();
        return new Result(true,"查询成功",list);
    }

    @PostMapping("/tou")
    public Result tou(@RequestBody Product product) throws InterruptedException {

        return borrowService.tou(product);
    }
    //File转MultipartFile
    private MultipartFile getFile(File file) throws Exception {
        FileItem fileItem = new DiskFileItem("copyfile.txt", Files.probeContentType(file.toPath()),false,file.getName(),(int)file.length(),file.getParentFile());
        byte[] buffer = new byte[1024];
        int n;
        try (InputStream inputStream = new FileInputStream(file); OutputStream os = fileItem.getOutputStream()){
            while ( (n = inputStream.read(buffer,0,1024)) != -1){
                os.write(buffer,0,n);
            }
            //也可以用IOUtils.copy(inputStream,os);
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            System.out.println(multipartFile.getName());
            return multipartFile;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }




}
