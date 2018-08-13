package com.duowenlvshi.crawler.lawyercase;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import com.duowenlvshi.crawler.lawyercase.webmagic.downloader.selenium.SeleniumDownloader;
import com.duowenlvshi.crawler.lawyercase.webmagic.pipeline.WenShuCourtIndexPipeline;
import com.duowenlvshi.crawler.lawyercase.webmagic.processor.WenShuCourtIndexPageProcessor;
import com.duowenlvshi.crawler.lawyercase.webmagic.scheduler.LawCaseRuleQueueScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.util.List;

/**
 * @Auther: wangchun
 * @Date: 2018/8/10 14:02
 */
@Service
public class SpiderBootStarter {

    @Autowired
    private SeleniumDownloader seleniumDownloader;

    @Autowired
    private LawCaseSearchRuleService lawCaseSearchRuleService;

    @Autowired
    private WebDriverBootstrapService webDriverBootstrapService;

    @Autowired
    private WenShuCourtIndexPipeline wenShuCourtIndexPipeline;

    public void testSeleniumDownloader(String refereeingDay) {
        // 初始化日度调度计划
        TaskSchedule taskSchedule = webDriverBootstrapService.initTaskSchedule(refereeingDay);
        // 初始化计划相应规则
        List<MatchRule> rules = RuleMatchUtils.buildDefaultMathRules(taskSchedule);
        List<LawCaseSearchRule> searchRuleList = lawCaseSearchRuleService.initLawCaseSearchRule(rules);
        // 包装请求参数
        List<Request> requests = RuleMatchUtils.warp(searchRuleList);
        //从"http://wenshu.court.gov.cn/"开始抓
        Spider.create(new WenShuCourtIndexPageProcessor()).
                setScheduler(new LawCaseRuleQueueScheduler()).
                addRequest(requests.toArray(new Request[requests.size()]))
                .thread(2)// 开启5个线程抓取
                .setDownloader(seleniumDownloader.setSleepTime(2))
                //.addPipeline(new FilePipeline())
                .addPipeline(wenShuCourtIndexPipeline)
                .run();// 启动爬虫
    }
}
