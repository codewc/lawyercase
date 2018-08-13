package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.model.constant.LawCaseSearchRuleHelper;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseSearchRuleRepository;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.LawCaseSearchRuleService;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wangchun
 * @see LawCaseSearchRuleService
 * @since 2018/8/9 11:03
 */
@Service
@Slf4j
public class LawCaseSearchRuleServiceImpl implements LawCaseSearchRuleService {

    private static final String BLANK_SPACE = " ";

    @Autowired
    private LawCaseSearchRuleRepository lawCaseSearchRuleRepository;

    @Override
    public List<LawCaseSearchRule> initLawCaseSearchRule(List<MatchRule> rules) {

        List<LawCaseSearchRule> lawCaseSearchRules = new ArrayList<>();
        if (rules.size() != 3) {
            throw new RuntimeException("规则检验和以前版本不一致，请对这里更新改造的匹配逻辑。");
        }
        Object[] level_case_arr = new Object[]{};
        Object[] doc_category_arr = new Object[]{};
        Object[] refereeing_day_arr = new Object[]{};
        String taskId = "";
        for (MatchRule rule : rules) {
            String key = rule.getKey();
            if (RuleMatchUtils.RULE_TYPE_LEVEL_CASE.equals(key)) {
                level_case_arr = rule.getValue().toArray();
            }
            if (RuleMatchUtils.RULE_TYPE_DOC_CATEGORY.equals(key)) {
                doc_category_arr = rule.getValue().toArray();
            }
            if (RuleMatchUtils.RULE_TYPE_REFEREEING_DAY.equals(key)) {
                refereeing_day_arr = rule.getValue().toArray();
            }
            if (StringUtils.isEmpty(taskId)) {
                taskId = rule.getTaskId();
            }
        }
        List<LawCaseSearchRule> ruleList = lawCaseSearchRuleRepository.findLawCaseSearchRulesByRefereeingDay((String) refereeing_day_arr[0]);
        if (ruleList != null && ruleList.size() > 0) {
            log.info("LawCaseSearchRule：ruleList数据库已经存在,打印开始：");
            for (LawCaseSearchRule caseSearchRule : ruleList) {
                log.info("明细数据： ->{}", caseSearchRule);
            }
            log.info("LawCaseSearchRule：ruleList数据库已经存在,打印完毕。->总共{}条", ruleList.size());
            return ruleList;
        }

        int doc_category_arr_length = doc_category_arr.length;
        int level_case_arr_length = level_case_arr.length;
        int index_1 = 0;
        int index_2 = 0;
        for (int i = 0; i < doc_category_arr_length * level_case_arr_length; i++) {
            Map<String, Object> serachMap = new HashMap<>();
            serachMap.put(RuleMatchUtils.RULE_TYPE_DOC_CATEGORY, doc_category_arr[index_1].toString());
            serachMap.put(RuleMatchUtils.RULE_TYPE_LEVEL_CASE, level_case_arr[index_2].toString());
            String refereeingDay = refereeing_day_arr[0].toString();
            serachMap.put(RuleMatchUtils.RULE_TYPE_REFEREEING_DAY, refereeingDay);
            JSONObject mapJson = new JSONObject(serachMap);
            LawCaseSearchRule lawCaseSearchRule = LawCaseSearchRule.builder().
                    refereeingDay(refereeingDay).taskId(taskId).
                    state(LawCaseSearchRuleHelper.STATE_00).
                    createDate(new Date()).
                    updateDate(new Date()).
                    conditionMapJson(mapJson.toString()).
                    build();
            lawCaseSearchRuleRepository.save(lawCaseSearchRule);
            log.info("初始化lawCaseSearchRule -> {}", lawCaseSearchRule);
            ++index_2;
            if (index_2 > level_case_arr_length - 1) {
                ++index_1;
                index_2 = 0;
            }
        }
        return lawCaseSearchRules;
    }

    @Override
    public WebDriver proceedLawCaseSearchRule(WebDriver driver, LawCaseSearchRule rule) {
        if (!WebDriverBootstrapService.WEBSITE_WENSHU.equals(driver.getCurrentUrl())) {
            driver.manage().deleteAllCookies();
            driver.navigate().back();
        }
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS).implicitlyWait(30, TimeUnit.SECONDS);
        WebElement head_maxsearch_btn = driver.findElement(By.xpath("//*[@id=\"head_maxsearch_btn\"]"));
        head_maxsearch_btn.click();
        String conditionMapJson = rule.getConditionMapJson();
        log.info(conditionMapJson);
        Map<String, String> conditionMap = JSON.parseObject(conditionMapJson, Map.class);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        if (conditionMap.containsKey(RuleMatchUtils.RULE_TYPE_REFEREEING_DAY)) {
            WebElement beginTimeCPRQ = wait.until(new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    return driver.findElement(By.xpath("//*[@id=\"beginTimeCPRQ\"]"));
                }
            });
            WebElement endTimeCPRQ = driver.findElement(By.xpath("//*[@id=\"endTimeCPRQ\"]"));
            try {
                beginTimeCPRQ.clear();
                endTimeCPRQ.clear();
            } catch (Exception e) {
                log.error("beginTimeCPRQ.clear()", e);
            }
            beginTimeCPRQ.sendKeys(conditionMap.get(RuleMatchUtils.RULE_TYPE_REFEREEING_DAY) + BLANK_SPACE);// 设置查询开始时间
            endTimeCPRQ.sendKeys(BLANK_SPACE + conditionMap.get(RuleMatchUtils.RULE_TYPE_REFEREEING_DAY));// 设置结束开始时间
        }

        if (conditionMap.containsKey(RuleMatchUtils.RULE_TYPE_DOC_CATEGORY)) {
            WebElement docButton = driver.findElement(By.xpath("//*[@id=\"advanceSearchContent\"]/div[9]/div[2]/div/table/tbody/tr/td[2]/input"));
            docButton.click();
            String value = conditionMap.get(RuleMatchUtils.RULE_TYPE_DOC_CATEGORY);
            List<WebElement> options = driver.findElements(By.xpath("//*[@id=\"advanceSearchContent\"]/div[9]/div[2]/div/div/ul/li"));
            for (WebElement element : options) {
                String text = element.getText();
                if (value.equals(text)) {
                    element.click();
                    break;
                }
            }
        }

        if (conditionMap.containsKey(RuleMatchUtils.RULE_TYPE_LEVEL_CASE)) {
            WebElement docButton = driver.findElement(By.xpath("//*[@id=\"advanceSearchContent\"]/div[7]/div[2]/div/table/tbody/tr/td[2]/input"));
            docButton.click();
            String value = conditionMap.get(RuleMatchUtils.RULE_TYPE_LEVEL_CASE);
            List<WebElement> options = driver.findElements(By.xpath("//*[@id=\"advanceSearchContent\"]/div[7]/div[2]/div/div/ul/li"));
            for (WebElement element : options) {
                String text = element.getText();
                if (value.equals(text)) {
                    element.click();
                    break;
                }
            }
        }
        return driver;
    }
}
