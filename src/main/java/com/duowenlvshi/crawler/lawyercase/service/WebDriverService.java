package com.duowenlvshi.crawler.lawyercase.service;

import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;

/**
 * 页面爬虫业务组件
 *
 * @Auther: wangchun
 * @Date: 2018/8/7 10:05
 */
public interface WebDriverService {

    void test();


    /**
     * 初始化调度任务
     *
     * @param refereeingDay 裁判日期
     * @return 获取的调取任务
     */
    TaskSchedule initTaskSchedule(String refereeingDay);
}
