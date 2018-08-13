package com.duowenlvshi.crawler.lawyercase.webmagic.processor;

import com.duowenlvshi.crawler.lawyercase.bean.RuleMatchResult;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.webmagic.downloader.selenium.SeleniumDownloader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        ResultItems items = page.getResultItems();

//        for (LawCaseSearchRule rule : ruleList) {
//            try {
//                lawCaseSearchRuleService.proceedLawCaseSearchRule(webDriver, rule);
//                Context context = rootContextFactory.createContext(webDriver, rule);
//                lawCaseDocTaskSchemaAction.action(context);
//            } catch (Exception e) {
//                log.error("Exception ->", e);
//            }
//        }
        WebDriver driver = items.get("webDriver");

        WebDriverWait wait = new WebDriverWait(driver, 20);
        List<WebElement> webElements = wait.until(webDriver -> driver.findElements(By.xpath("//*[@id=\"resultList\"]/div/table/tbody/tr[1]/td/div/a[2]")));
        if (driver.getCurrentUrl().indexOf("VisitRemind.html") > 0) {
            page.setDownloadSuccess(false);//默认设置下载失败
            String errorMsg = String.format("严重错误->:IP地址被禁，请换IP地址！errorUrl->%s", driver.getCurrentUrl());
            throw new IllegalStateException(errorMsg);
        }
        boolean state = false;// 是否获取成功
        RuleMatchResult result = new RuleMatchResult();
        Set<String> docUrlSet = result.getDocUrls();
        String ruleId = items.get("ruleId");
        result.setRuleId(ruleId);
        for (WebElement element : webElements) {
            String href = element.getAttribute("href");
            if (StringUtils.isNotBlank(href) && href.contains("/content/content")) {
                state = true;
                //driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
                log.info(href);
                docUrlSet.add(href);
                //element.click();
            }
        }
        result.setCurrentPage(1);
        result.setMatchTotalNum(docUrlSet.size());
        page.setDownloadSuccess(state);
        if (!page.isDownloadSuccess()) {
            page.setSkip(true);
        }
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