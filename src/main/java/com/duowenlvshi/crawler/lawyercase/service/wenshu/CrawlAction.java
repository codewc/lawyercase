package com.duowenlvshi.crawler.lawyercase.service.wenshu;

/**
 * 爬取逻辑组件
 *
 * @Auther: wangchun
 * @Date: 2018/8/9 17:34
 */
public interface CrawlAction {

    /**
     * 执行爬取
     *
     * @param context 环境容器变量
     */
    boolean action(Context context);


}
