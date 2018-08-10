package com.duowenlvshi.crawler.lawyercase.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.*;

/**
 * 案例条件
 *
 * @Auther: wangchun
 * @Date: 2018/8/8 17:57
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCaseSearchRule implements Serializable {

    /**
     * 规则id
     */
    @Id
    private String ruleId;

    /**
     * @see TaskSchedule#taskId
     */
    private String taskId;

    /**
     * @see TaskSchedule#refereeingDay
     */
    private String refereeingDay;

    /**
     * 筛选条件
     */
    private String conditionMapJson;

    /**
     * 匹配数量
     */
    private int batchCaseNum;

    /**
     * 批次状态
     */
    private String state;

    /**
     * 爬取成功法律案例文档id
     */
    private Set<String> dealDocIdList = new HashSet<>();

    /**
     * 待爬取法律案例文档id
     */
    private Set<String> waitDocIdList = new HashSet<>();

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;
}
