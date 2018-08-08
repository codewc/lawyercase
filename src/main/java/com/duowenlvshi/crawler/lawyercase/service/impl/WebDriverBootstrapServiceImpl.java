package com.duowenlvshi.crawler.lawyercase.service.impl;

import com.duowenlvshi.crawler.lawyercase.exception.CommonErrorHandler;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

/**
 * @author wangchun
 * @version 1.0
 * @see WebDriverBootstrapService
 * @since 2018/8/8
 */
@Service
@Slf4j
public class WebDriverBootstrapServiceImpl implements WebDriverBootstrapService {

    @Override
    public WebDriver initWebDriver() {
        String driverPath = this.getClass().getClassLoader().getResource("chromedriver.exe").getPath();
        log.info("chromedriver.exe->driverPath={}", driverPath);
        System.setProperty("webdriver.chrome.driver", driverPath);
        WebDriver driver = new ChromeDriver();
        ((ChromeDriver) driver).setErrorHandler(new CommonErrorHandler(false));
        driver.get("http://wenshu.court.gov.cn/");
        return driver;
    }
}
