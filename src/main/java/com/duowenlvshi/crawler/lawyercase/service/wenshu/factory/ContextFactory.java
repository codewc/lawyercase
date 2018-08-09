package com.duowenlvshi.crawler.lawyercase.service.wenshu.factory;

import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import org.openqa.selenium.WebDriver;

/**
 * 上下文环境变量工厂
 *
 * @Auther: wangchun
 * @Date: 2018/8/9 17:54
 */
public interface ContextFactory {

    /**
     * 创建上下文环境变量
     *
     * @return
     */
    Context createContext();

    /**
     * 创建上下文环境变量
     *
     * @param webDriver 浏览器对象
     * @return
     */
    Context createContext(WebDriver webDriver);
}
