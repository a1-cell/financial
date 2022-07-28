package com.bw.borrow.controller;

import com.bw.borrow.service.BorrowService;
import com.bw.borrow.utils.Filetomutportfile;
import com.bw.borrow.utils.ImageWatermarkUtils;
import com.bw.borrow.utils.OSSUploadUtil;

import com.xxl.job.core.handler.annotation.XxlJob;
import io.renren.common.borrow.Borrow;
import io.renren.common.borrow.Rule;
import io.renren.common.result.Result;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.UUID;

@RequestMapping("/v1")
@RestController
@CrossOrigin
public class BorrowController extends Thread{
    @Autowired
    BorrowService borrowService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run() {
        for (int i=0;i<10;i++){
            System.out.println(i);
        }
    }


    //xxl-job自动投标
    @XxlJob("rule")
    public void test(){
        System.out.println("成功!111111");
        //查询所有自动投标
        List<Rule> list=borrowService.getRuleList();
    }


    //开启自动投标添加规则
    @PostMapping("/addrule")
    public Result addrule(@RequestBody Rule rule){
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
        String name="最可爱最漂亮的女孩";
        ImageWatermarkUtils.markImageByText(name,url+filenameimg,null,null);
        //Thread.sleep(3000);
        String substring = filenameimg.substring(0, filenameimg.lastIndexOf("."));
        substring=substring+"_water.png";
        File file1 = new File(url + substring);

       // MultipartFile multipartFile = getFile(file1);
        String upload = OSSUploadUtil.upload(file);

        return upload;
    }
    //File转MultipartFile
   /* private MultipartFile getFile(File file) throws Exception {
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

    */


}
