package com.bw.borrow.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: zyk
 * @Date: 2022/06/09/10:56
 * @Description:
 */
public class OSSUploadUtil {
    private static String endpoint = "oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "LTAI5t8ekt1gSKcdmb4kZLSj";
    private static String accessKeySecret = "mXQmx8JTwT4t7ZkDvZx3wOKYlTYjAt";

    private static String bucketName = "pinxixi1910a";


    public static String upload(MultipartFile file) throws IOException {
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String name = UUID.randomUUID().toString();
        //name=name.substring(0,10);
        String fileName = file.getOriginalFilename();
        String extName = fileName.substring(fileName.lastIndexOf("."));
        String objectName = name + extName;
        try {
            client.putObject(bucketName,objectName,file.getInputStream());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            client.shutdown();
        }
        return bucketName+"."+endpoint+"/"+objectName;
    }

    public static String pvw(String objectName) throws IOException {
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String obj = "";
        try {
            OSSObject object = client.getObject(new GetObjectRequest(bucketName, objectName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                obj += line+"\t\n";
            }
            reader.close();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            client.shutdown();
        }
        return obj;
    }
    public static void download(String objectName) throws IOException {
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            OSSObject object = client.getObject(new GetObjectRequest(bucketName, objectName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
            //FileOutputStream fos=new FileOutputStream(new File("C:/Users/Administrator/Desktop/界面原型/pages/pages/banner4.html"));
            //objectName=objectName.substring(objectName.lastIndexOf("/"));
            File file = new File("");
            String canonicalPath = file.getCanonicalPath();
           // FileOutputStream fos = new FileOutputStream("D:\\IDEAProject\\education-page\\pages" + objectName);
             FileOutputStream fos=new FileOutputStream(new File(canonicalPath+"\\renren-borrow\\src\\main\\resources\\public\\"+objectName));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw=new BufferedWriter(osw);
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                System.out.println(line);
                bw.write(line+"\t\n");
            }
            bw.close();
            osw.close();
            fos.close();
            reader.close();
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            client.shutdown();
        }
    }


}
