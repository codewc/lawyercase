package com.duowenlvshi.crawler.lawyercase.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * @Auther: wangchun
 * @Date: 2018/8/9 10:25
 */
@Slf4j
public class WebUtils {

    private WebUtils() {

    }

    public static String getHostAddress() {
        String ret = "who";
        try {
            InetAddress address = InetAddress.getLocalHost();
            ret = address.getHostAddress().toString(); //获取本机ip
        } catch (Exception e) {
            log.error("WebUtils getHostAddress发生了错误：-> {}", e);
        }
        return ret;
    }
}
