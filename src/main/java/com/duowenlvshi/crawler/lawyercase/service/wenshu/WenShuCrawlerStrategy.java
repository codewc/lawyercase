package com.duowenlvshi.crawler.lawyercase.service.wenshu;

/**
 * 裁判文书案例抓取策略
 *
 * @Auther: wangchun
 * @Date: 2018/8/8 14:39
 */
public interface WenShuCrawlerStrategy {

    /**
     * 负面分数为0
     */
    int BEST_SCORE = 0;

    /**
     * 由于其它原因，不能抓取
     */
    int FAIL_SCORE = -1;

    /**
     * 计算负面的分数
     */
    int calculateNegativeScore();

}
