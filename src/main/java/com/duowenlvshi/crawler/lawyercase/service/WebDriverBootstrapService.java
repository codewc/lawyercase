package com.duowenlvshi.crawler.lawyercase.service;

import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
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

    /**
     * 初始化调度任务.若存在当天的调度任务，将返回己存的调度任务
     *
     * @param refereeingDay 裁判日期
     * @return 获取的调取任务
     */
    TaskSchedule initTaskSchedule(String refereeingDay);

}
