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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.UUID;
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



    //???????????????????????????
    @GetMapping("/getnum/{id}")
    public Integer getUnm(@PathVariable Integer id){
        Integer num=borrowService.getNum(id);
        return num;
    }
    //??????????????????
    @GetMapping("/resh/{radio}/{id}")
    @Transactional
    public Result resh(@PathVariable Integer radio,@PathVariable Integer id) throws InterruptedException {
        //?????????????????????
        Rechruld rechruld=borrowService.getRechruldById(radio);
        //????????????

        RLock lock = redissonClient.getLock("lock"+id);
        boolean b = lock.tryLock(10, 5, TimeUnit.SECONDS);
        if(b){
            try {
                borrowService.updateRechruldNum(id,rechruld.getNum());
            }finally {
                lock.unlock();
            }
            return new Result(true,"????????????!","");
        }else{
            return new Result(true,"??????????????????????????????!","");
        }
    }

    //???????????????????????????????????????es
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
        return new Result(true,"??????ES??????!","");
    }
    //??????ES
    @PostMapping("/getEs")
    public Result getEs(@RequestBody BackList blackList) throws Exception {
        //????????????????????????
        Integer num = borrowService.getNum(blackList.getId());
        if(num<=0){
            return new Result(false,"?????????????????????????????????????????????!","");
        }
        //??????????????????
        borrowService.updateNum(blackList.getId());
        //??????????????????
        SearchRequest searchRequest = new SearchRequest("bank");
        searchRequest.types("blacklist");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if(blackList.getUsername()!=null && blackList.getUsername()!=""){
            //??????????????????
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(blackList.getUsername(), "username");
            searchSourceBuilder.query(multiMatchQueryBuilder);
        }
        //??????????????????
        searchRequest.source(searchSourceBuilder);
        //????????????
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
        return new Result(true,"????????????!",list);
    }
    //xxl-job????????????
    @XxlJob("rule")
    @GetMapping("/ab")
    public void test(){
        //System.out.println("??????!111111");
        //????????????????????????
        List<Rule> list=borrowService.getRuleList();
        //feign???????????????
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
                   // System.out.println("??????????????????!");
                    System.out.println(a+"---"+product.getProductRate()+"------"+b);
                    count++;
                }
            }
            System.out.println(rule.getName()+"???????????????????????????:"+count);
        }
    }



    //??????????????????????????????
    @PostMapping("/addrule")
    public Result addrule(@RequestBody Rule rule){
        int count=borrowService.checkName(rule.getName());
        if(count>=1){
            return new Result(false,"?????????","");
        }
        rule.setStatue(1);
        borrowService.addrule(rule);
        return new Result(true,"??????","");
    }
    //??????????????????
    @GetMapping("/getrule")
    public Result getrule(String name){
        Rule rule=borrowService.getrule(name);
        return new Result(true,"????????????",rule);
    }
    //??????????????????
    @GetMapping("/norul")
    public Result norul(String name){
        Rule rule=borrowService.checkStatue(name);
        if(rule.getStatue()==0){
            return new Result(false,"?????????","");
        }
        borrowService.norul(name);
        return new Result(true,"????????????","");
    }

    //??????????????????
    @GetMapping("/getlist")
    public Result getlist(){
        List<Borrow> list=borrowService.getlist();
        return new Result(true,"????????????!",list);
    }

    //??????
    @PostMapping("/add")
    public Result addBorrow(@RequestBody Borrow borrow){

        BorrowController borrowController = new BorrowController();
        borrowController.run();
        //????????????id
        borrow.setUserid(3);
        borrow.setBorrowRen("mlove");
        borrow.setTid(0);
        borrow.setTtid(0);
        borrowService.add(borrow);
        return new Result(true,"????????????!","");
    }

    @PostMapping("/upload2")
    public String upload2(MultipartFile file) throws IOException {
        String upload = OSSUploadUtil.upload(file);
        return upload;
    }
    //????????????
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
                // 2????????????????????????
                // 1K???????????????
                byte[] bs = new byte[1024];
                // ????????????????????????
                int len;
                // ???????????????????????????????????????
                File tempFile = new File(url);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                os = new FileOutputStream(tempFile.getPath() + File.separator + filenameimg);
                // ????????????
                while ((len = inputStream.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // ???????????????????????????
                try {
                    os.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        //???????????????
        String name="???????????????????????????,?????????.";
        ImageWatermarkUtils.markImageByText(name,url+filenameimg,null,null);
        //Thread.sleep(3000);
        String substring = filenameimg.substring(0, filenameimg.lastIndexOf("."));
        substring=substring+"_water.png";
        File file1 = new File(url + substring);

       // MultipartFile multipartFile = getFile(file1);
        String upload = OSSUploadUtil.upload(file);

        return upload;
    }
    //?????????????????????

    /**
     * ???????????????????????????????????????
     * @return
     */
    @GetMapping("/list")
    public Result getBorrowList() throws JsonProcessingException {
        List<Product> list=borrowService.getList();
        return new Result(true,"????????????",list);
    }

    @PostMapping("/tou")
    public Result tou(@RequestBody Product product) throws InterruptedException {

        return borrowService.tou(product);
    }
    //File???MultipartFile
   /* private MultipartFile getFile(File file) throws Exception {
        FileItem fileItem = new DiskFileItem("copyfile.txt", Files.probeContentType(file.toPath()),false,file.getName(),(int)file.length(),file.getParentFile());
        byte[] buffer = new byte[1024];
        int n;
        try (InputStream inputStream = new FileInputStream(file); OutputStream os = fileItem.getOutputStream()){
            while ( (n = inputStream.read(buffer,0,1024)) != -1){
                os.write(buffer,0,n);
            }
            //????????????IOUtils.copy(inputStream,os);
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            System.out.println(multipartFile.getName());
            return multipartFile;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;

    }

    */


}
