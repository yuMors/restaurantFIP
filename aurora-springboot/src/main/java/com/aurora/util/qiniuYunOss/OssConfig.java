package com.aurora.util.qiniuYunOss;


/**
 * 对象存储常量配置
 */
public class OssConfig {
    /**
     * 这是域名的一个样例 mors.cc
     * 这是域名的一个样例 rs81ljuv5.hb-bkt.clouddn.com
     * <p>
     * 实际效果：http://mors.cc/2023/03/28/bg.jpg
     * 实际效果：http://rs81ljuv5.hb-bkt.clouddn.com/2023/03/28/bg.jpg
     * 域名 realmName
     */
    public static final String realmName = "http://mors.cc/";

    public static final String accessKey = "eSFNm0_nhg4_pcTf0dYcsEVWiS7-aqowg5h9vg7K";

    public static final String secretKey = "HHCfafB2a1UQ3WYZ_d3CxbSZNT-PlVN7IbYmu9oB";
    /**
     * 存储空间名
     */
    public static final String bucket = "auroraye";
    //String accessKey = YHOSSConstants.YHAccessKey;
    //String myUrl = "http://rs81ljuv5.hb-bkt.clouddn.com";
//    String myUrl = "http://mors.cc/";
//    String myUrl = OssConfig.realmName;
//    @Value("${upload.oss.accessKeyId}")
//    String accessKey = OssConfig.accessKey;

    //mors-cc-idvllpm.qiniudns.com
    //String secretKey = YHOSSConstants.YHSecfretKey;
//    @Value("${upload.oss.accessKeySecret}")
//    String secretKey = OssConfig.secretKey;

    //String bucket = YHOSSConstants.YHBucket; //
    //bucket – 空间名
//    String bucket = "auroraye";
//    String bucket = OssConfig.bucket;


}
