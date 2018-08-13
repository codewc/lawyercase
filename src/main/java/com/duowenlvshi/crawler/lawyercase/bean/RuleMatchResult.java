package com.duowenlvshi.crawler.lawyercase.bean;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
     * 规则id
     */
    private String ruleId;

    /**
     * 匹配的人数
     */
    private int matchTotalNum;

    /**
     * 当前结果所属页数
     */
    private int currentPage;

    /**
     * 文档url
     */
    private Set<String> docUrls = new HashSet<>();

}
