package com.duowenlvshi.crawler.lawyercase.webmagic.downloader.selenium;

import com.duowenlvshi.crawler.lawyercase.service.wenshu.DownloadService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * 使用Selenium调用浏览器进行渲染。目前仅支持chrome。<br>
 * 需要下载Selenium driver支持。<br>
 *
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午1:37 <br>
 */
@Slf4j
@Component
public class SeleniumDownloader implements Downloader, Closeable {

    protected volatile WebDriverPool webDriverPool;

    protected int sleepTime = 0;

    protected int poolSize = 1;

    @Autowired
    private DownloadService indexLayoutDownloadService;

    /**
     * 新建
     *
     * @param chromeDriverPath chromeDriverPath
     */
    public SeleniumDownloader(String chromeDriverPath) {
        System.getProperties().setProperty("webdriver.chrome.driver",
                chromeDriverPath);
    }

    public SeleniumDownloader() {
        this(SeleniumDownloader.class.getClassLoader().getResource("chromedriver.exe").getPath());
    }

    /**
     * set sleep time to wait until load success
     *
     * @param sleepTime sleepTime
     * @return this
     */
    public SeleniumDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

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
        /*
         * TODO You can add mouse event or other processes
         *
         * @author: bob.li.0718@gmail.com
         */
//        ChromeOptions options = new ChromeOptions();
//        options.setProxy();
        Page page = new Page();
        page.setRequest(request);
        page.setUrl(new PlainText(request.getUrl()));
        indexLayoutDownloadService.doService(webDriver, page);
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        page.setRawText(content);
        webDriverPool.returnToPool(webDriver);
        return page;
    }

    protected void checkInit() {
        if (webDriverPool == null) {
            synchronized (this) {
                webDriverPool = new WebDriverPool(poolSize);
            }
        }
    }

    @Override
    public void setThread(int thread) {
        this.poolSize = thread;
    }

    @Override
    public void close() throws IOException {
        if (webDriverPool != null) {
            webDriverPool.closeAll();
        }
    }
}
