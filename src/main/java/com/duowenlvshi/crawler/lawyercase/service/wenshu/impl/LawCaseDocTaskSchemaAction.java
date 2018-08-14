package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

import com.duowenlvshi.crawler.lawyercase.bean.RuleMatchResult;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.CrawlAction;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import com.duowenlvshi.crawler.lawyercase.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author wangchun
 * @see CrawlAction
 * @since 2018/8/9 18:21
 */
@Service("lawCaseDocTaskSchemaAction")
@Slf4j
public class LawCaseDocTaskSchemaAction implements CrawlAction {

    @Autowired
    private LawCaseDocRepository repository;

//    @Override
//    public boolean action(Context context) {
//        Set<String> all_handles = driver.getWindowHandles();
    //循环判断，把当前句柄从所有句柄中移除，剩下的就是你想要的新窗口
//        Iterator<String> it = all_handles.iterator();
//        String handle = null;
//        WebDriver newWindow = null;
    //获取当前页面句柄
//        String current_handle = driver.getWindowHandle();
//        while (it.hasNext()) {
//            handle = it.next();
//            if (current_handle.equals(handle)) {
//                continue;
//            }
//            //跳入新窗口,并获得新窗口的driver - newWindow
//            newWindow = driver.switchTo().window(handle);
//            try {
//                WebElement contextElement = newWindow.findElement(By.xpath("//*[contains(@class, 'div_doc_container')]"));
//                dbSaveDocWebElement(contextElement, newWindow.getCurrentUrl());
//            } catch (Exception e) {
//                log.error("发生了错误…………", e);
//            }
//            newWindow.close();
//        }
//    }

    private void dbSaveDocWebElement(WebElement docWebElement, String sourceUrl) {
        if (docWebElement == null) {
            return;
        }
        String context = docWebElement.getText();
        LawCaseDoc doc = LawCaseDoc.builder().context(context).crateDate(new Date()).updateDate(new Date()).build();
        doc.setIp(WebUtils.getHostAddress());
        doc.setSourceUrl(sourceUrl);
        doc.setDocId(RuleMatchUtils.analyzeDocId(sourceUrl));
        log.info("save doc -> {}", doc.toString());
        repository.save(doc);
    }


    @Override
    public boolean action(Context context) {
        boolean ret = false;
        try {
            WebDriver driver = context.getWebDriver();
            Page page = context.getPage();
            WebElement webElement = driver.findElement(By.xpath("//*[@id=\"list_btnmaxsearch\"]"));
            webElement.click();
            page.putField("webDriver", driver);
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
            LawCaseSearchRule searchRule = (LawCaseSearchRule) page.getRequest().getExtra("rule");
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

            return page.isDownloadSuccess();
        } catch (Exception e) {
            log.error("------↓↓↓------notice------↓↓↓------", e);
        }
        return ret;
    }
}
