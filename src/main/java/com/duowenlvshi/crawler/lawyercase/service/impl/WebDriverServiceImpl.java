package com.duowenlvshi.crawler.lawyercase.service.impl;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.model.constant.TaskScheduleHelper;
import com.duowenlvshi.crawler.lawyercase.repository.TaskScheduleRepository;
import com.duowenlvshi.crawler.lawyercase.service.AsyncService;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.CrawlAction;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.TaskScheduleService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.factory.ContextFactory;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.impl.RootContext;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wangchun
 * @Date: 2018/8/7 10:05
 */
@Slf4j
@Service
public class WebDriverServiceImpl implements WebDriverService {

    @Autowired
    private TaskScheduleService taskScheduleService;

    @Autowired
    private TaskScheduleRepository taskScheduleRepository;

    @Autowired
    private WebDriverBootstrapService webDriverBootstrapService;

    @Autowired
    private LawCaseSearchRuleService lawCaseSearchRuleService;

    @Autowired
    private CrawlAction lawCaseDocTaskSchemaAction;

    @Autowired
    private ContextFactory rootContextFactory;

    /**
     * 空格
     */
    private static final String BLANK_SPACE = " ";

    public void test(String url, String refereeingDay) {
//        try {
//            TaskSchedule taskSchedule = initTaskSchedule(refereeingDay);
//            List<MatchRule> rules = RuleMatchUtils.buildDefaultMathRules(refereeingDay);
//            List<LawCaseSearchRule> ruleList = lawCaseSearchRuleService.initLawCaseSearchRule(rules);
//            for (LawCaseSearchRule rule : ruleList) {
//                WebDriver driver = webDriverBootstrapService.initWebDriver("http://wenshu.court.gov.cn/");
//                try {
//                    driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS).implicitlyWait(30, TimeUnit.SECONDS);
//                    WebElement head_maxsearch_btn = driver.findElement(By.xpath("//*[@id=\"head_maxsearch_btn\"]"));
//                    head_maxsearch_btn.click();
//                    lawCaseSearchRuleService.proceedLawCaseSearchRule(driver, rule);
//                    Context context = rootContextFactory.createContext(driver);
//                    lawCaseDocTaskSchemaAction.action(context);
//                } catch (Exception e) {
//                    log.error("Exception ->", e);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("test发生错误", e);
//        }
    }

    @Override
    public WebDriver doService(WebDriver webDriver, Map<String, Object> bizData) {
        //WebDriver driverTemp = webDriverBootstrapService.initWebDriver(WebDriverBootstrapService.WEBSITE_WENSHU);
        try {
            List<LawCaseSearchRule> ruleList = (List) bizData.get("ruleList");
            for (LawCaseSearchRule rule : ruleList) {
                try {
                    lawCaseSearchRuleService.proceedLawCaseSearchRule(webDriver, rule);
                    Context context = rootContextFactory.createContext(webDriver, rule);
                    lawCaseDocTaskSchemaAction.action(context);
                } catch (Exception e) {
                    log.error("Exception ->", e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("test发生错误", e);
        } finally {
//            if (driverTemp != null) {
//                driverTemp.quit();
//            }
        }
        return webDriver;
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

//    @Override
//    public List<LawCaseSearchRule> proceedMatchRule(WebDriver driver, List<MatchRule> rules) {
//        List<LawCaseSearchRule> lawCaseSearchRules = lawCaseSearchRuleService.initLawCaseSearchRule(rules);
//        return lawCaseSearchRules;
//    }

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
                driver.quit();
            }
        }
    }

}
