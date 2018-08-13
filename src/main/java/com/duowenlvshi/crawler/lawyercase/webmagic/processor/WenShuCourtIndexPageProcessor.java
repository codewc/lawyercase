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
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

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
        WebDriverWait wait = new WebDriverWait(driver, 60);
        List<WebElement> webElements = wait.until(webDriver -> driver.findElements(By.xpath("//*[@id=\"resultList\"]/div/table/tbody/tr[1]/td/div/a[2]")));
        if (driver.getCurrentUrl().indexOf("VisitRemind.html") > 0) {
            page.setDownloadSuccess(false);//默认设置下载失败
            String errorMsg = String.format("严重错误->:IP地址被禁，请换IP地址！errorUrl->%s", driver.getCurrentUrl());
            throw new IllegalStateException(errorMsg);
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error("", e);
        }

        int matchTotalNum = -1;
        try {
            WebElement pageInfo = wait.until(webDriver -> (webDriver.findElement(By.xpath("//*[@id=\"pageNumber\"]"))));
            matchTotalNum = Integer.valueOf(pageInfo.getAttribute("total"));
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            log.info("matchTotalNum ->{}", matchTotalNum);
        }
        try {
            if (matchTotalNum == -1) {
                WebElement emptyElement = wait.until(webDriver -> (webDriver.findElement(By.xpath("//*[@id=\"resultList\"]"))));
                String text = emptyElement.getText();
                if (StringUtils.contains(text, "无符合条件的数据")) {
                    matchTotalNum = 0;
                }
            }
        } catch (Exception e) {
            log.error("",e);
        }


        boolean state = false;// 是否获取成功
        RuleMatchResult matchResult = new RuleMatchResult();
        Set<String> docUrlSet = matchResult.getDocUrls();
        LawCaseSearchRule searchRule = (LawCaseSearchRule) page.getRequest().getExtras().get("rule");
        matchResult.setRuleId(searchRule.getRuleId());
        matchResult.setTaskId(searchRule.getTaskId());
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
        matchResult.setCurrentPage(1);
        matchResult.setCurrentPage(docUrlSet.size());
        matchResult.setMatchTotalNum(matchTotalNum);
        page.putField("matchResult", matchResult);
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
