package com.duowenlvshi.crawler.lawyercase.service.impl;

import com.duowenlvshi.crawler.lawyercase.exception.CommonErrorHandler;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import com.duowenlvshi.crawler.lawyercase.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    public WebDriver initWebDriver(String webSite) {

        String driverPath = this.getClass().getClassLoader().getResource("chromedriver.exe").getPath();
        log.info("chromedriver.exe->driverPath={}", driverPath);
        System.setProperty("webdriver.chrome.driver", driverPath);
        WebDriver driver = new ChromeDriver();
        ((ChromeDriver) driver).setErrorHandler(new CommonErrorHandler(false));
        driver.get(webSite);
        return driver;
    }

}
