package com.duowenlvshi.crawler.lawyercase.service;

import com.duowenlvshi.crawler.lawyercase.config.CrawlerThreadPoolConfig;

/**
 * 异步任务组件
 *
 * @Auther: wangchun
 * @Date: 2018/8/7 15:39
 */
public interface AsyncService {

    /**
     * 执行异步任务
     * @see CrawlerThreadPoolConfig#crawlerExecutor()
     */
    void executeAsync();
}
