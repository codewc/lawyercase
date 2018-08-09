package com.duowenlvshi.crawler.lawyercase.service;

import org.openqa.selenium.WebDriver;

/**
 * 浏览器层业务组件
 *
 * @Author: wangchun
 * @Date: 2018/8/8
 * @Version: 1.0
 */
public interface WebDriverBootstrapService {

    /**
     * 文书
     */
    String WEBSITE_WENSHU = "http://wenshu.court.gov.cn/";
    /**
     * 初始化浏览器
     *
     * @param webSite 网页
     * @return 浏览器
     */
    WebDriver initWebDriver(String webSite);

}
