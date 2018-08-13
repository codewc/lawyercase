package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

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
            return page.isDownloadSuccess();
        } catch (Exception e) {
            log.error("------↓↓↓------notice------↓↓↓------", e);
        }
        return ret;
    }
}
