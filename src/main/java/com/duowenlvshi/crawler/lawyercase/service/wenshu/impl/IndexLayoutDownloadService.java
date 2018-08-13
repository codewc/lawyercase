package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.Context;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.DownloadService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.factory.ContextFactory;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;

import java.util.List;

/**
 * TODO
 *
 * @Author: wangchun
 * @Date: 2018/8/10
 * @Version: 1.0
 * @see DownloadService
 */
@Service("indexLayoutDownloadService")
@Slf4j
public class IndexLayoutDownloadService implements DownloadService {

    @Autowired
    private LawCaseSearchRuleService lawCaseSearchRuleService;

    @Autowired
    private LawCaseDocTaskSchemaAction lawCaseDocTaskSchemaAction;

    @Autowired
    private ContextFactory rootContextFactory;

    @Override
    public Context doService(WebDriver webDriver, Page page) {
        LawCaseSearchRule rule = (LawCaseSearchRule) page.getRequest().getExtras().get("rule");
        Context context = rootContextFactory.createContext(webDriver, rule).setPage(page);
        try {
            lawCaseSearchRuleService.proceedLawCaseSearchRule(webDriver, rule);
            lawCaseDocTaskSchemaAction.action(context);
        } catch (Exception e) {
            log.error("Exception ->", e);
        }
        return context;
    }
}
