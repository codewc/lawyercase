package com.duowenlvshi.crawler.lawyercase.util;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.service.WebDriverBootstrapService;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * 匹配规则工具类
 *
 * @Author: wangchun
 * @Date: 2018/8/8
 * @Version: 1.0
 */
public class RuleMatchUtils {

    public RuleMatchUtils() {
    }

    /**
     * 案件类型
     */
    public static final String RULE_TYPE_LEVEL_CASE = "LEVEL_CASE";
    /**
     * 文书类型
     */
    public static final String RULE_TYPE_DOC_CATEGORY = "DOC_CATEGORY";
    /**
     * 裁判日期
     */
    public static final String RULE_TYPE_REFEREEING_DAY = "REFEREEING_DAY";

    /**
     * 构建默认得查询规则
     *
     * @return 爬取的规则字典
     */
    public static List<MatchRule> buildDefaultMathRules(TaskSchedule taskSchedule) {
        String refereeingDay = taskSchedule.getRefereeingDay();
        String taskId = taskSchedule.getTaskId();
        List<MatchRule> matchRuleList = new ArrayList<>();
        matchRuleList.add(buildRuleMatch(RULE_TYPE_LEVEL_CASE,
                new String[]{"刑事案件", "民事案件", "行政案件", "赔偿案件", "执行案件"}, taskId));
        matchRuleList.add(buildRuleMatch(RULE_TYPE_DOC_CATEGORY,
                new String[]{"判决书", "裁定书", "调解书", "决定书", "通知书", "批复", "答复", "函", "令", "其他"}, taskId));
        if (StringUtils.isNotBlank(refereeingDay)) {
            matchRuleList.add(buildRuleMatch(RULE_TYPE_REFEREEING_DAY, new String[]{refereeingDay, refereeingDay}, taskId));
        }
        return matchRuleList;
    }

    /**
     * 根据网页地址解析docId
     *
     * @param docUrl
     * @return
     */
    public static String analyzeDocId(String docUrl) {
        String ret = "";
        if (StringUtils.isNotBlank(docUrl)) {
            String[] docs = docUrl.split("DocID=");
            if (docs.length == 2) {
                ret = docs[1];
            }
        }
        return ret;
    }

    /**
     * 爬取规则
     *
     * @param key    规则类型
     * @param values 规则命中值
     * @return
     */
    private static MatchRule buildRuleMatch(String key, String[] values, String taskId) {
        MatchRule rule = new MatchRule();
        List<String> list = new ArrayList<>();
        for (String value : values) {
            list.add(value);
        }
        rule.setTaskId(taskId);
        rule.setValue(list);
        rule.setKey(key);
        return rule;
    }

    /**
     * @param ruleList
     * @return
     */
    public static List<Request> warp(List<LawCaseSearchRule> ruleList) {
        List<Request> list = new ArrayList<>();
        for (LawCaseSearchRule rule : ruleList) {
            Request request = new Request();
            request.setUrl(WebDriverBootstrapService.WEBSITE_WENSHU);
            request.putExtra("rule", rule);
            list.add(request);
        }
        return list;
    }

}


