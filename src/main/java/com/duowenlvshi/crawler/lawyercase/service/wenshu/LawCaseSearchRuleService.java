package com.duowenlvshi.crawler.lawyercase.service.wenshu;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * 匹配规则业务组件
 *
 * @Auther: wangchun
 * @Date: 2018/8/9 11:02
 */
public interface LawCaseSearchRuleService {

    /**
     * 初始化案例规则
     *
     * @param rules 规则
     * @return
     */
    List<LawCaseSearchRule> initLawCaseSearchRule(List<MatchRule> rules);

    /**
     * @param taskId
     * @param stateCondition
     * @return
     */
    List<LawCaseSearchRule> queryLawCaseSearchRuleList(String taskId, String... stateCondition);

    /**
     * 设置规则
     *
     * @param driver 浏览器
     * @param rule   规则
     * @return 已经设置好规则的浏览器
     */
    WebDriver proceedLawCaseSearchRule(WebDriver driver, LawCaseSearchRule rule);
}
