package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

/**
 * 根上下文
 *
 * @Auther: wangchun
 * @Date: 2018/8/9 17:49
 */
@Slf4j
public class RootContext implements Context {

    private WebDriver webDriver;

    public RootContext(WebDriver webDriver) {
        this.setContext(webDriver);
    }

    @Override
    public boolean setContext(WebDriver webDriver) {
        boolean ret = false;
        if (webDriver != null && this.webDriver == null) {
            this.webDriver = webDriver;
            ret = true;
        }
        return ret;
    }

    @Override
    public WebDriver getWebDriver() {
        return this.webDriver;
    }

    @Override
    public void close() {
        if (webDriver != null) {
            try {
                webDriver.quit();
            } catch (Exception e) {
                log.warn("close释放资源发生错误： ->{}", e);
            }

        }
    }
}
