package com.aurora.util.qiniuYunOss.config;

import com.aurora.util.qiniuYunOss.OssConfig;
import com.aurora.util.qiniuYunOss.other.AppHttpCodeEnum;
import com.aurora.util.qiniuYunOss.other.SystemException;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class QiniuYunOssService {

    /**
     * 上传头像
     * 判断文件类型或者文件大小
     * 如果判断通过上传文件到OSS
     */
    public String uploadImg(MultipartFile img) {
        //判断文件类型或者文件大小
        String originalFilename = img.getOriginalFilename();
        //获取原始文件名 进行判断
        assert originalFilename != null;
        if (!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);

        }
        //* 如果判断通过上传文件到OSS
        String filePath = PathUtils.generateFilePath(originalFilename);
        return uploadOss(img, filePath);
    }


    private String uploadOss(MultipartFile imgFile, String filePath) {
        //构造一个带指定 Region 对象的配置类 自动获取存储地址 这个这次是华东-浙江2
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        //String key = "2022/sg.png";
        try {
            //拿到前端传过来的文件 赋值给InputStream对象
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(OssConfig.accessKey, OssConfig.secretKey);
            String upToken = auth.uploadToken(OssConfig.bucket);
            try {
                Response response = uploadManager.put(inputStream, filePath, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println("照片路径吧 putRet.key " + putRet.key);
                System.out.println("照片哈希值吧 putRet.hash " + putRet.hash);
                String imgUrl = OssConfig.realmName + filePath;
                System.out.println("imgUrl: " + imgUrl);
                return imgUrl;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
            System.err.println("upload is not normal");
        }
        return "upload is not normal";
    }
}

