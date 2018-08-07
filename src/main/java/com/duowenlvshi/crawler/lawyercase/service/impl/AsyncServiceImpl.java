package com.duowenlvshi.crawler.lawyercase.service.impl;

import com.duowenlvshi.crawler.lawyercase.service.AsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author wangchun
 * @see AsyncService
 * @since 2018/8/7 15:40
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async("crawlerExecutor")
    public void executeAsync() {
        System.out.println(Thread.currentThread().toString() + "11111111111111111");
    }
}
