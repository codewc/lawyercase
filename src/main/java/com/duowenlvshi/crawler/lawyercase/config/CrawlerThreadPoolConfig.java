package com.duowenlvshi.crawler.lawyercase.config;

import com.duowenlvshi.crawler.lawyercase.config.executor.VisibleThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 爬虫资源线程池
 *
 * @Auther: wangchun
 * @Date: 2018/8/7 15:29
 */
@Configuration
@EnableAsync
public class CrawlerThreadPoolConfig {

    private int corePoolSize = 5;//线程池维护线程的最少数量

    private int maxPoolSize = 10;//线程池维护线程的最大数量

    private int queueCapacity = 500; //缓存队列

    private int keepAlive = 60;//允许的空闲时间

    @Bean
    public Executor crawlerExecutor() {
        ThreadPoolTaskExecutor executor = new VisibleThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("crawlerExecutor-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); //对拒绝task的处理策略
        executor.setKeepAliveSeconds(keepAlive);
        executor.initialize();
        return executor;
    }
}
