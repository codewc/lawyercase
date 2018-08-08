package com.duowenlvshi.crawler.lawyercase.bean;

import lombok.*;

import java.util.*;

/**
 * 规则匹配结果信息
 *
 * @author wangchun
 * @version 1.0
 * @since 2018/8/8
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleMatchResult {

    /**
     * 匹配的人数
     */
    private int matchNum;

    /**
     * 搜索条件
     */
    private List<MatchRule> rules = new ArrayList<>();

}
