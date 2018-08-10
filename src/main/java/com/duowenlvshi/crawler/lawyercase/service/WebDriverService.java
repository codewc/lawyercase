package com.duowenlvshi.crawler.lawyercase.service;

import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * 页面爬虫业务组件
 *
 * @Auther: wangchun
 * @Date: 2018/8/7 10:05
 */
public interface WebDriverService {

    /**
     * 浏览器执行
     *
     * @param webDriver 浏览器
     * @param bizData   业务参数
     * @return
     */
    WebDriver doService(WebDriver webDriver, Map<String, Object> bizData);


    /**
     * 初始化调度任务.若存在当天的调度任务，将返回己存的调度任务
     *
     * @param refereeingDay 裁判日期
     * @return 获取的调取任务
     */
    TaskSchedule initTaskSchedule(String refereeingDay);

//    /**
//     * 解析并设置
//     *
//     * @param webDriver 浏览器
//     * @param rules     爬取规则
//     * @return
//     */
//    WebDriver proceedMatchRule(WebDriver webDriver, List<MatchRule> rules);
}
