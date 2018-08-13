package com.duowenlvshi.crawler.lawyercase.service.impl;

import com.duowenlvshi.crawler.lawyercase.exception.CommonErrorHandler;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.model.constant.TaskScheduleHelper;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.repository.TaskScheduleRepository;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.TaskScheduleService;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import com.duowenlvshi.crawler.lawyercase.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wangchun
 * @version 1.0
 * @see WebDriverBootstrapService
 * @since 2018/8/8
 */
@Service
@Slf4j
public class WebDriverBootstrapServiceImpl implements WebDriverBootstrapService {

    @Autowired
    private TaskScheduleService taskScheduleService;

    @Autowired
    private TaskScheduleRepository taskScheduleRepository;

    @Autowired
    private WebDriverBootstrapService webDriverBootstrapService;

    private static final String BLANK_SPACE = " ";//空格

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

    @Override
    public TaskSchedule initTaskSchedule(String refereeingDay) {
        TaskSchedule taskSchedule = taskScheduleService.getTaskSchedule(refereeingDay);
        if (StringUtils.isBlank(refereeingDay) || taskSchedule.getCaseTotalNum() > 0) {
            return taskSchedule;
        }
        // 统计任务表需要爬取案例条数
        initCaseTotalNum(refereeingDay, taskSchedule);
        return taskSchedule;
    }


    protected void initCaseTotalNum(String refereeingDay, TaskSchedule taskSchedule) {
        WebDriver driver = webDriverBootstrapService.initWebDriver("http://wenshu.court.gov.cn/");
        try {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            WebElement head_maxsearch_btn = driver.findElement(By.xpath("//*[@id=\"head_maxsearch_btn\"]"));
            head_maxsearch_btn.click();
            WebElement beginTimeCPRQ = driver.findElement(By.xpath("//*[@id=\"beginTimeCPRQ\"]"));
            beginTimeCPRQ.clear();
            beginTimeCPRQ.sendKeys(refereeingDay + BLANK_SPACE);// 设置查询开始时间
            WebElement endTimeCPRQ = driver.findElement(By.xpath("//*[@id=\"endTimeCPRQ\"]"));
            endTimeCPRQ.clear();
            endTimeCPRQ.sendKeys(BLANK_SPACE + refereeingDay);// 设置结束开始时间
            WebElement searchBtn = driver.findElement(By.className("head_search_btn"));
            searchBtn.click();
            WebDriverWait wait = new WebDriverWait(driver, 10);
            // 获取到的总页数
            WebElement pageInfo = wait.until(webDriver -> (webDriver.findElement(By.xpath("//*[@id=\"pageNumber\"]"))));
            String total = pageInfo.getAttribute("total");
            if (StringUtils.isNotBlank(total)) {
                taskSchedule.setCaseTotalNum(new Integer(total));
                taskSchedule.setState(TaskScheduleHelper.STATE_10);
                taskSchedule.setUpdateDate(new Date());
                taskScheduleRepository.save(taskSchedule);
            }
            log.info("查询到的总页数 ->{} ", total);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("initTaskSchedule发生错误-> {}", e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
