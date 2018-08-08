package com.duowenlvshi.crawler.lawyercase.util;

import com.duowenlvshi.crawler.lawyercase.bean.MatchRule;
import org.apache.commons.lang3.StringUtils;

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

    private RuleMatchUtils() {
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
    public static List<MatchRule> buildDefaultMathRules(String refereeingDay) {
        List<MatchRule> matchRuleList = new ArrayList<>();
        matchRuleList.add(buildRuleMatch(RULE_TYPE_LEVEL_CASE,
                new String[]{"刑事案件", "民事案件", "行政案件", "赔偿案件", "执行案件"}));
        matchRuleList.add(buildRuleMatch(RULE_TYPE_DOC_CATEGORY,
                new String[]{"判决书", "裁定书", "调解书", "决定书", "通知书", "批复", "答复", "函", "令", "其他"}));
        if (StringUtils.isNotBlank(refereeingDay)) {
            matchRuleList.add(buildRuleMatch(RULE_TYPE_REFEREEING_DAY, new String[]{refereeingDay, refereeingDay}));
        }
        return matchRuleList;
    }

    private static MatchRule buildRuleMatch(String key, String[] values) {
        MatchRule rule = new MatchRule();
        List<String> list = new ArrayList<>();
        for (String value : values) {
            list.add(value);
        }
        rule.setValue(list);
        rule.setKey(key);
        return rule;
    }
}


