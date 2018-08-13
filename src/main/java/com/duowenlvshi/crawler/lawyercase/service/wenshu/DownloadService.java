package com.duowenlvshi.crawler.lawyercase.service.wenshu;

import org.openqa.selenium.WebDriver;
import us.codecraft.webmagic.Page;

/**
 * TODO
 *
 * @Author: wangchun
 * @Date: 2018/8/10
 * @Version: 1.0
 */
public interface DownloadService {

    /**
     * 浏览器执行
     *
     * @param webDriver 浏览器
     * @param page      业务参数
     * @return
     */
    Context doService(WebDriver webDriver, Page page);

}
