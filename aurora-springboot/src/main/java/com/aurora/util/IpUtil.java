package com.aurora.util;

import com.aurora.constant.CommonConstant;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 计算ip工具类
 */
@Slf4j
@Component
public class IpUtil {

    /**数据搜寻者*/
    private static DbSearcher searcher;

    /**方法*/
    private static Method method;

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Real-IP");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("x-forwarded-for");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("getIpAddress exception:", e);
                }
                assert inet != null;
                ipAddress = inet.getHostAddress();
            }
        }
        return StringUtils.substringBefore(ipAddress, ",");
    }

    @PostConstruct
    private void initIp2regionResource() throws Exception {
        System.err.println("想使用这个ip的方法");
    }

    public static String getIpSource(String ipAddress) {
        System.out.println("得到id的省份吧 不知道谁调用了这个方法");
//        if (ipAddress == null || !Util.isIpAddress(ipAddress)) {
//            log.error("Error: Invalid ip address");
//            return "";
//        }
//        try {
//            DataBlock dataBlock = (DataBlock) method.invoke(searcher, ipAddress);
//            String ipInfo = dataBlock.getRegion();
//            if (!StringUtils.isEmpty(ipInfo)) {
//                ipInfo = ipInfo.replace("|0", "");
//                ipInfo = ipInfo.replace("0|", "");
//                return ipInfo;
//            }
//        } catch (Exception e) {
//            log.error("getCityInfo exception:", e);
//        }
        return "";
    }


    /**
     * 获取用户代理
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

}
