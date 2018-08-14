package com.duowenlvshi.crawler.lawyercase.webmagic.downloader.selenium;

import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.factory.ContextFactory;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.impl.LawCaseDocDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.util.Map;

/**
 * 文档详情下载
 *
 * @author wangchun
 * @since 2018/8/14 10:09
 */
@Slf4j
@Component
public class LawCaseDocDownloader extends SeleniumDownloader implements Downloader, Closeable {

    @Autowired
    private LawCaseDocDownloadService lawCaseDocDownloadService;

    @Override
    public Page download(Request request, Task task) {
        checkInit();
        WebDriver webDriver;
        try {
            webDriver = webDriverPool.get();
        } catch (InterruptedException e) {
            log.warn("interrupted", e);
            return null;
        }
        log.info("taskUUID ->:{}; downloading page url ->:{}", task.getUUID(), request.getUrl());
        webDriver.get(request.getUrl());
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebDriver.Options manage = webDriver.manage();
        Site site = task.getSite();
        if (site.getCookies() != null) {
            for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
                Cookie cookie = new Cookie(cookieEntry.getKey(), cookieEntry.getValue());
                manage.addCookie(cookie);
            }
        }
        Page page = new Page();
        page.setRequest(request);
        page.setUrl(new PlainText(request.getUrl()));
        lawCaseDocDownloadService.doService(webDriver, page);
        page.isDownloadSuccess();
        webDriverPool.returnToPool(webDriver);
        return page;
    }
}
