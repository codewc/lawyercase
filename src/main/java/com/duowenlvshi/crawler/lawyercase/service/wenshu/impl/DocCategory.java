package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

import com.duowenlvshi.crawler.lawyercase.service.wenshu.WenShuCrawlerStrategy;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.constants.ListPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author wangchun
 * @see WenShuCrawlerStrategy
 * @since 2018/8/8 15:06
 */
@Slf4j
public class DocCategory implements WenShuCrawlerStrategy {

    @Override
    public int calculateNegativeScore(WebElement element) {
        int ret = WenShuCrawlerStrategy.FAIL_SCORE;
        if (element == null) {
            return ret;
        }

        try {
            List<WebElement> webElementList = element.findElements(By.xpath("//*[@id=\"tree\"]/div"));
            for (WebElement webElement : webElementList) {
                String key = webElement.getAttribute(ListPage.TreeKey.MATCH_RULE);
                if (ListPage.TreeKey.DOC_CATEGORY.equals(key)) {// 匹配成功，爬虫进行抓取action
                    ret = proceedWebElementScore(webElement);
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("calculateNegativeScore方法调用{}发生了错误->,{}", element, e);
        }
        return ret;
    }

    /**
     * 计算案例匹配分数
     *
     * @return
     */
    private int proceedWebElementScore(WebElement treeElement) {
        int ret = WenShuCrawlerStrategy.FAIL_SCORE;
        if (treeElement == null) {
            return ret;
        }
        try {
            List<WebElement> textWebElement = treeElement.findElements(By.xpath("//*[@id=\"tree\"]/div[8]/div[2]/ul/li"));//FIXME: 这里需要根号的规则优化
            int sum = 0;
            for (WebElement element : textWebElement) {
                String text = element.getText();
                if (StringUtils.isNotBlank(text)) {//e.g: 判决书(12)
                    int left = text.indexOf("(");
                    int length = text.length();
                    String val = text.trim().substring(left + 1, length - 1);//e.g: 12
                    sum += new Integer(val);
                }
            }
            ret = sum;
        } catch (Exception e) {
            log.warn("proceedWebElementScore方法调用:{}发生了错误->,{}", treeElement, e);
        }
        return ret;
    }
}
