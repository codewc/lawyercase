package com.duowenlvshi.crawler.lawyercase.service.impl;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.model.constant.TaskScheduleHelper;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.repository.TaskScheduleRepository;
import com.duowenlvshi.crawler.lawyercase.service.AsyncService;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.TaskScheduleService;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wangchun
 * @Date: 2018/8/7 10:05
 */
@Slf4j
@Service
public class WebDriverServiceImpl implements WebDriverService {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private LawCaseDocRepository repository;

    @Autowired
    private TaskScheduleService taskScheduleService;

    @Autowired
    private TaskScheduleRepository taskScheduleRepository;

    @Autowired
    private WebDriverBootstrapService webDriverBootstrapService;

    /**
     * 空格
     */
    private static final String BLANK_SPACE = " ";

    @Override
    public void test() {
        try {
            String refereeingDay = "2018-08-06";
            TaskSchedule taskSchedule = initTaskSchedule(refereeingDay);
            WebDriver driver = initWebDriver();
            driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS).implicitlyWait(30, TimeUnit.SECONDS);
            List<MatchRule> rules = RuleMatchUtils.buildDefaultMathRules(refereeingDay);
            WebElement head_maxsearch_btn = driver.findElement(By.xpath("//*[@id=\"head_maxsearch_btn\"]"));
            head_maxsearch_btn.click();
            proceedMatchRule(driver, rules);
            WebElement searchBtn = driver.findElement(By.className("head_search_btn"));
            searchBtn.click();
            List<WebElement> webElements = driver.findElements(By.ByXPath.xpath("//*[@id=\"resultList\"]/div/table/tbody/tr[1]/td/div/a[2]"));
            for (WebElement element : webElements) {
                String href = element.getAttribute("href");
                if (StringUtils.isNotBlank(href) && href.contains("/content/content")) {
                    //driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
                    log.info(href);
                    element.click();
                }
            }
            Set<String> all_handles = driver.getWindowHandles();
            //循环判断，把当前句柄从所有句柄中移除，剩下的就是你想要的新窗口
            Iterator<String> it = all_handles.iterator();
            String handle = null;
            WebDriver newWindow = null;
            //获取当前页面句柄
            String current_handle = driver.getWindowHandle();
            while (it.hasNext()) {
                handle = it.next();
                if (current_handle.equals(handle)) {
                    continue;
                }
                //跳入新窗口,并获得新窗口的driver - newWindow
                newWindow = driver.switchTo().window(handle);
                try {
                    WebElement contextElement = newWindow.findElement(By.xpath("//*[contains(@class, 'div_doc_container')]"));
                    dbSaveDocWebElement(contextElement, newWindow.getCurrentUrl());
                } catch (Exception e) {
                    log.error("发生了错误…………", e);
                }
                newWindow.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("test发生错误", e);
        }
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

    @Override
    public WebDriver proceedMatchRule(WebDriver driver, List<MatchRule> rules) {
        for (MatchRule rule : rules) {
            String key = rule.getKey();
            List<String> values = rule.getValue();
            if (RuleMatchUtils.RULE_TYPE_REFEREEING_DAY.equals(key)) {
                WebElement beginTimeCPRQ = driver.findElement(By.xpath("//*[@id=\"beginTimeCPRQ\"]"));
                beginTimeCPRQ.clear();
                beginTimeCPRQ.sendKeys(values.get(0) + BLANK_SPACE);// 设置查询开始时间
                WebElement endTimeCPRQ = driver.findElement(By.xpath("//*[@id=\"endTimeCPRQ\"]"));
                endTimeCPRQ.clear();
                endTimeCPRQ.sendKeys(BLANK_SPACE + values.get(1));// 设置结束开始时间
                continue;
            }

            if (RuleMatchUtils.RULE_TYPE_DOC_CATEGORY.equals(key)) {
                WebElement docButton = driver.findElement(By.xpath("//*[@id=\"advanceSearchContent\"]/div[9]/div[2]/div/table/tbody/tr/td[2]/input"));
                docButton.click();
                String value = rule.getValue().get(0);//FIXME:
                //List<WebElement> options = driver.findElements(By.xpath("//*[@id=\"adsearch_WSLX\"]/option"));
                List<WebElement> options = driver.findElements(By.xpath("//*[@id=\"advanceSearchContent\"]/div[9]/div[2]/div/div/ul/li"));
                for (WebElement element : options) {
                    String text = element.getText();
                    log.info(text);
                    if (value.equals(text)) {
                        log.info("OK -> " + text);
                        element.click();
                        continue;
                    }
                }
                continue;
            }

            if (RuleMatchUtils.RULE_TYPE_LEVEL_CASE.equals(key)) {
                WebElement docButton = driver.findElement(By.xpath("//*[@id=\"advanceSearchContent\"]/div[7]/div[2]/div/table/tbody/tr/td[2]/input"));
                docButton.click();
                String value = rule.getValue().get(0);//FIXME:
                List<WebElement> options = driver.findElements(By.xpath("//*[@id=\"advanceSearchContent\"]/div[7]/div[2]/div/div/ul/li"));
                for (WebElement element : options) {
                    String text = element.getText();
                    log.info(text);
                    if (value.equals(text)) {
                        element.click();
                        continue;
                    }
                }
                continue;
            }
        }
        return driver;
    }

    private void initCaseTotalNum(String refereeingDay, TaskSchedule taskSchedule) {
        WebDriver driver = initWebDriver();
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
            WebElement pageInfo = driver.findElement(By.xpath("//*[@id=\"pageNumber\"]"));
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
                driver.close();
            }
        }
    }


    /**
     * 初始化浏览器
     *
     * @return
     */
    private WebDriver initWebDriver() {
        return webDriverBootstrapService.initWebDriver();
    }


    private void dbSaveDocWebElement(WebElement docwebElement, String sourceUrl) {
        if (docwebElement == null) {
            return;
        }
        String context = docwebElement.getText();
        LawCaseDoc doc = LawCaseDoc.builder().context(context).crateDate(new Date()).updateDate(new Date()).build();
        log.info("save doc -> {}", doc.toString());
        repository.insert(doc);
    }
}
