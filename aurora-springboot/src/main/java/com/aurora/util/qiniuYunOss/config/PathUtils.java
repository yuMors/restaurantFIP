package com.aurora.util.qiniuYunOss.config;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * 文件名字生成工具类
 * 生成照片的名字 按照一定的规则如果没有指定的话
 */
public class PathUtils {

    @NotNull
    public static String generateFilePath(@NotNull String fileName) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");

        String dateName = LocalDateTime.now().format(dateTimeFormatter);
        //根据日期生成路径   2022/1/15/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/");
        String imgPath = simpleDateFormat.format(new Date());
        //uuid作为文件名
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").trim();
        //String uuidNew = dataName+uuid.substring(8);
        String uuidNews = dateName + "YH" + "-" + uuid.substring(19);
        //后缀和文件后缀一致 拿到名字
        int index = fileName.lastIndexOf(".");
        // test.jpg -> .jpg
        String fileType = fileName.substring(index);
        return imgPath + uuidNews + fileType;
        //return new StringBuilder().append(datePath).append(uuid).append(fileType).toString();
    }
}



