package com.duowenlvshi.crawler.lawyercase.service.impl;

import com.duowenlvshi.crawler.lawyercase.exception.CommonErrorHandler;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.service.AsyncService;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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

    @Override
    public void test() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\wangchun\\IdeaProjects\\git\\lawyercase\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        ((ChromeDriver) driver).setErrorHandler(new CommonErrorHandler(false));
        driver.get("http://wenshu.court.gov.cn/");
        try {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            WebElement head_maxsearch_btn = driver.findElement(By.xpath("//*[@id=\"head_maxsearch_btn\"]"));
            head_maxsearch_btn.click();
            Thread.sleep(1000);
            WebElement beginTimeCPRQ = driver.findElement(By.xpath("//*[@id=\"beginTimeCPRQ\"]"));
            beginTimeCPRQ.clear();
            beginTimeCPRQ.sendKeys("2018-08-06 ");
            WebElement endTimeCPRQ = driver.findElement(By.xpath("//*[@id=\"endTimeCPRQ\"]"));
            endTimeCPRQ.clear();
            endTimeCPRQ.sendKeys(" 2018-08-06");

            WebElement searchBtn = driver.findElement(By.className("head_search_btn"));
            searchBtn.click();
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
            //获取当前页面句柄
            String current_handle = driver.getWindowHandle();
            WebElement pageInfo = driver.findElement(By.xpath("//*[@id=\"pageNumber\"]"));
            String total = pageInfo.getAttribute("total");
            log.info("查询到的总页数 ->{} ", total);
            List<WebElement> webElements = ((ChromeDriver) driver).findElements(By.ByXPath.xpath("//*[@id=\"resultList\"]/div/table/tbody/tr[1]/td/div/a[2]"));
            for (WebElement element : webElements) {
                String href = element.getAttribute("href");
                if (StringUtils.isNotBlank(href) && href.contains("/content/content")) {
                    //driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
                    log.info(href);
                    //element.click();
                }
            }

            Set<String> all_handles = driver.getWindowHandles();
            //循环判断，把当前句柄从所有句柄中移除，剩下的就是你想要的新窗口
            Iterator<String> it = all_handles.iterator();
            String handle = null;
            WebDriver newWindow = null;
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
