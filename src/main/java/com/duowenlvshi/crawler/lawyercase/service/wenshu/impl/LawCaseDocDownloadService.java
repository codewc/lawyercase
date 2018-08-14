package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.DownloadService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.factory.ContextFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;

/**
 * @author wangchun
 * @since 2018/8/14 10:15
 */
@Service("lawCaseDocDownloadService")
@Slf4j
public class LawCaseDocDownloadService implements DownloadService {

    @Autowired
    private ContextFactory rootContextFactory;

    @Override
    public Context doService(WebDriver webDriver, Page page) {
        Context context = rootContextFactory.createContext(webDriver).setPage(page);
        String url = page.getRequest().getUrl();
        webDriver.get(url);
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id=\"Content\"]"),"发布日期"));
        WebElement docElement = webDriver.findElement(By.xpath("//*[@id=\"Content\"]"));
        String docHtmlContext = docElement.getAttribute("innerHTML");// 获取doc节点html源码
        page.putField("docHtmlContext", docHtmlContext);
        log.info(docHtmlContext);
        return context;
    }
}
