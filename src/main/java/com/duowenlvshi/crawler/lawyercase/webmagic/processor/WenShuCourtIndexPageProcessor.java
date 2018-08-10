package com.duowenlvshi.crawler.lawyercase.webmagic.processor;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import com.duowenlvshi.crawler.lawyercase.webmagic.downloader.selenium.SeleniumDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

/**
 * @Auther: wangchun
 * @Date: 2018/8/10 10:29
 */
@Slf4j
public class WenShuCourtIndexPageProcessor implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setRetrySleepTime(10);

    @Autowired
    private LawCaseSearchRuleService lawCaseSearchRuleService;

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    @Override
    public void process(Page page) {
        System.out.println(page.getHtml());
        String refereeingDay = page.getResultItems().get("refereeingDay");

//        for (LawCaseSearchRule rule : ruleList) {
//            try {
//                lawCaseSearchRuleService.proceedLawCaseSearchRule(webDriver, rule);
//                Context context = rootContextFactory.createContext(webDriver, rule);
//                lawCaseDocTaskSchemaAction.action(context);
//            } catch (Exception e) {
//                log.error("Exception ->", e);
//            }
//        }
        // 设置重新爬取的地址
        List<Request> targetRequests = page.getTargetRequests();


    }

    @Override
    public Site getSite() {
        return this.site;
    }

    public static void main(String[] args) {

        Spider.create(new WenShuCourtIndexPageProcessor())
                //从"http://wenshu.court.gov.cn/"开始抓
                .addUrl("http://wenshu.court.gov.cn/")
                //开启5个线程抓取
                .thread(5)
                .setDownloader(new SeleniumDownloader().setSleepTime(2))
                //启动爬虫
                .run();
    }
}
