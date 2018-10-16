package com.duowenlvshi.crawler.lawyercase;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.model.constant.LawCaseSearchRuleHelper;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import com.duowenlvshi.crawler.lawyercase.webmagic.downloader.selenium.LawCaseDocDownloader;
import com.duowenlvshi.crawler.lawyercase.webmagic.pipeline.LawCaseDocPipeline;
import com.duowenlvshi.crawler.lawyercase.webmagic.processor.LawCaseDocPageProcessor;
import com.duowenlvshi.crawler.lawyercase.webmagic.scheduler.LawCaseRuleQueueScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

import java.util.List;

/**
 * @author wangchun
 * @since 2018/8/14 11:53
 */
@Service
public class SpiderLawCaseDocStarter {

    @Autowired
    private LawCaseDocPageProcessor lawCaseDocPageProcessor;

    @Autowired
    private LawCaseDocDownloader lawCaseDocDownloader;

    @Autowired
    private LawCaseDocPipeline lawCaseDocPipeline;

    @Autowired
    private LawCaseSearchRuleService lawCaseSearchRuleService;

    @Autowired
    private WebDriverBootstrapService webDriverBootstrapService;

    public void executeLawCaseDocDownloader(String refereeingDay) {
        // 初始化日度调度计划11
        TaskSchedule taskSchedule = webDriverBootstrapService.initTaskSchedule(refereeingDay);
        List<LawCaseSearchRule> searchRuleList = lawCaseSearchRuleService.queryLawCaseSearchRuleList(taskSchedule.getTaskId(), LawCaseSearchRuleHelper.STATE_10);
        List<Request> requests = RuleMatchUtils.transform(searchRuleList);
        Spider.create(lawCaseDocPageProcessor)
                .setScheduler(new LawCaseRuleQueueScheduler())
                .addRequest(requests.toArray(new Request[requests.size()]))
                .setDownloader(lawCaseDocDownloader.setSleepTime(10000))
                .addPipeline(lawCaseDocPipeline)
                .run();
    }
}
