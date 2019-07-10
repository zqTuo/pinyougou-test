package com.pinyougou.upload.controller;


import com.pinyougou.common.util.FastDFSClient;
import entity.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    /*发送上传路径的请求： 、uploadFile
    * 参数 文本对象本身
    * 返回值  页面需要图片的路径回显 result 工具类
    * */

    @RequestMapping("/uploadFile")
//    支持跨域  只有这两个的跨域请求上传图片才可以被允许
    @CrossOrigin(origins = {"http://localhost:9102","http://localhost:9101"},allowCredentials ="true" )
    public Result upload(@RequestParam(value = "file") MultipartFile file)   {
//
        try {
            //1.获取原上传文件的扩展名
            String originalFilename = file.getOriginalFilename();// 123.jpg
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            //2.获取原文件的字节数组
            byte[] bytes =file.getBytes();
            //3.调用fastdfsclient的api 上传图片
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //   group1/M00/00/06/wKgZhV0V4biAItLpAANdC6JX9KA166.jpg
            String path = fastDFSClient.uploadFile(bytes, extName);
            String realPath = "http://192.168.25.133/"+path;
            //4.返回result 带有图片的路径
            return new Result(true,realPath);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
