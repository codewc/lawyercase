package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import us.codecraft.webmagic.Page;

/**
 * 根上下文
 *
 * @Auther: wangchun
 * @Date: 2018/8/9 17:49
 */
@Slf4j
public class RootContext implements Context {

    private WebDriver webDriver;

    private Page page;

    private LawCaseSearchRule lawCaseSearchRule;

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
    public LawCaseSearchRule getLawCaseSearchRule() {
        return lawCaseSearchRule;
    }

    @Override
    public Context setLawCaseSearchRule(LawCaseSearchRule searchRule) {
        this.lawCaseSearchRule = searchRule;
        return this;
    }

    @Override
    public Context setPage(Page page) {
        this.page = page;
        return this;
    }

    @Override
    public Page getPage() {
        return page;
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
