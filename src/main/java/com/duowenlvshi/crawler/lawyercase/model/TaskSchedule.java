package com.duowenlvshi.crawler.lawyercase.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调度任务表
 *
 * @Auther: wangchun
 * @Date: 2018/8/8 17:43
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskSchedule implements Serializable {

    /**
     * 调度任务id
     */
    @Id
    private String taskId;

    /**
     * @see LawCaseSearchRule
     */
    private List<LawCaseSearchRule> searchRules = new ArrayList<>();

    /**
     * 主批次状态
     */
    private String state;

    /**
     * 裁判日期：2018-08-08
     */
    private String refereeingDay;

    /**
     * 案例条数
     */
    private int caseTotalNum;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新时间
     */
    private Date updateDate;

}
