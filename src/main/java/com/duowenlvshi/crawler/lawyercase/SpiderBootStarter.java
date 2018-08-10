package com.duowenlvshi.crawler.lawyercase;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import com.duowenlvshi.crawler.lawyercase.webmagic.downloader.selenium.SeleniumDownloader;
import com.duowenlvshi.crawler.lawyercase.webmagic.processor.WenShuCourtIndexPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;

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
    private WebDriverService webDriverService;

    public void testSeleniumDownloader(String refereeingDay) {
        TaskSchedule taskSchedule = webDriverService.initTaskSchedule(refereeingDay);
        List<MatchRule> rules = RuleMatchUtils.buildDefaultMathRules(taskSchedule);
        List<LawCaseSearchRule> searchRuleList = lawCaseSearchRuleService.initLawCaseSearchRule(rules);
        //从"http://wenshu.court.gov.cn/"开始抓
        Request request = new Request("http://wenshu.court.gov.cn/");
        request.putExtra("searchRuleList", searchRuleList);
        Spider.create(new WenShuCourtIndexPageProcessor()).addRequest(request)

                //开启5个线程抓取
                .thread(5)
                .setDownloader(seleniumDownloader.setSleepTime(2))
                //启动爬虫
                .run();
    }
}
