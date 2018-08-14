package com.duowenlvshi.crawler.lawyercase.webmagic.processor;

import com.duowenlvshi.crawler.lawyercase.bean.RuleMatchResult;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.webmagic.downloader.selenium.SeleniumDownloader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Set;

/**
 * @Auther: wangchun
 * @Date: 2018/8/10 10:29
 */
@Slf4j
@Component()
public class WenShuCourtIndexPageProcessor implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(5000).setRetrySleepTime(10);

    @Autowired
    private LawCaseSearchRuleService lawCaseSearchRuleService;

    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    @Override
    public void process(Page page) {

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
