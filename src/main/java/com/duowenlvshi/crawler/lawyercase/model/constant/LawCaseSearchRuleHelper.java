package com.duowenlvshi.crawler.lawyercase.model.constant;

/**
 * @Auther: wangchun
 * @Date: 2018/8/8 18:27
 * @see com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc
 */
public abstract class LawCaseSearchRuleHelper {
    /**
     * 初始状态
     */
    public static final String STATE_00 = "00";
    /**
     * 爬取任务在处理队列中
     */
    public static final String STATE_10 = "10";
    /**
     * 爬取暂停。继续爬取，求修改{@link #STATE_10}然后继续爬取
     */
    public static final String STATE_15 = "15";
    /**
     * 爬取错误发生中断，请确认爬取规则。修改{@link #STATE_10}然后继续爬取
     */
    public static final String STATE_19 = "19";
    /**
     * 爬取成功
     */
    public static final String STATE_20 = "20";
    /**
     * 规则置为无效
     */
    public static final String STATE_99 = "99";

}
