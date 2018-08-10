package com.duowenlvshi.crawler.lawyercase.service.wenshu;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import org.openqa.selenium.WebDriver;

/**
 * 环境变量容器
 *
 * @Auther: wangchun
 * @Date: 2018/8/9 17:41
 */
public interface Context {

    /**
     * 有参构造器
     *
     * @param webDriver 浏览器对象
     * @return
     * @see #getWebDriver()
     */
    boolean setContext(WebDriver webDriver);

    /**
     * @return
     */
    LawCaseSearchRule getLawCaseSearchRule();

    /**
     * 设置的抓取规则
     *
     * @param searchRule 定义好的抓取规则
     * @return this
     */
    Context setLawCaseSearchRule(LawCaseSearchRule searchRule);

    /**
     * 获取当前上下文中的浏览器
     *
     * @return
     */
    WebDriver getWebDriver();

    /**
     * 关闭当前环境
     */
    void close();
}
