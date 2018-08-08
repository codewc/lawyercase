package com.duowenlvshi.crawler.lawyercase.service.impl;

import com.duowenlvshi.crawler.lawyercase.exception.CommonErrorHandler;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.model.constant.TaskScheduleHelper;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.repository.TaskScheduleRepository;
import com.duowenlvshi.crawler.lawyercase.service.AsyncService;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.TaskScheduleService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.WenShuCrawlerStrategy;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.impl.DocCategory;
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

    /**
     * 空格
     */
    private static final String BLANK_SPACE = " ";

    @Override
    public void test() {
        try {
            String refereeingDay = "2018-08-06";
            TaskSchedule taskSchedule = taskScheduleService.getTaskSchedule(refereeingDay);
            WebDriver driver = init(taskSchedule);
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
                if (current_handle == handle) {
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
//        WebDriver driver = init(refereeingDay);
//        WebElement webElement = driver.findElement(By.ByXPath.xpath("//*[@id=\"content\"]"));
//        WenShuCrawlerStrategy strategy = new DocCategory();
//        int ret = strategy.calculateNegativeScore(webElement);
        return taskScheduleService.getTaskSchedule(refereeingDay);
    }


    private WebDriver init(TaskSchedule schedule) {
        String refereeingDay = schedule.getRefereeingDay();
        System.setProperty("webdriver.chrome.driver", "E:\\wangchun\\Intelli_workspace\\crawler\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        ((ChromeDriver) driver).setErrorHandler(new CommonErrorHandler(false));
        driver.get("http://wenshu.court.gov.cn/");
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
                schedule.setCaseTotalNum(new Integer(total));
                schedule.setState(TaskScheduleHelper.STATE_10);
                schedule.setUpdateDate(new Date());
                taskScheduleRepository.save(schedule);
            }
            log.info("查询到的总页数 ->{} ", total);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("test发生错误", e);
        }
        return driver;
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
