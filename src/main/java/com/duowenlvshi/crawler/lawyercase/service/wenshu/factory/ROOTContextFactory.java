package com.duowenlvshi.crawler.lawyercase.service.wenshu.factory;

import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.impl.RootContext;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wangchun
 * @Date: 2018/8/9 17:56
 */
@Service("rootContextFactory")
public class ROOTContextFactory implements ContextFactory {

    @Autowired
    private WebDriverBootstrapService webDriverBootstrapService;

    @Override
    public Context createContext() {
        WebDriver driver = webDriverBootstrapService.initWebDriver(WebDriverBootstrapService.WEBSITE_WENSHU);
        return createContext(driver);
    }

    @Override
    public Context createContext(WebDriver webDriver) {
        RootContext context = new RootContext(webDriver);
        return context;
    }
}
