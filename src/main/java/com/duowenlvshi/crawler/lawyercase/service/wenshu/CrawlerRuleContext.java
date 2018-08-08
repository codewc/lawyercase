package com.duowenlvshi.crawler.lawyercase.service.wenshu;

public class CrawlerRuleContext {

    private WenShuCrawlerStrategy strategy;

    public CrawlerRuleContext(WenShuCrawlerStrategy strategy) {
        this.strategy = strategy;
    }

    public int excute() {
        return this.strategy.calculateNegativeScore();
    }
}
