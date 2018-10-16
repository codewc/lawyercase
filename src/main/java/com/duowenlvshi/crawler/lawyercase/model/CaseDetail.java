package com.duowenlvshi.crawler.lawyercase.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author wangchun
 * @since 2018/10/16 14:05
 */
@Data
@Entity
@Table(name = "case_detail")
@ToString
public class CaseDetail implements Serializable {

    @Id
    @Column(name = "doc_id")
    private String docId;

    @Column(name = "detail_id")
    private String detailId;

    @Column
    private String remarks;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "schema_day")
    private String schemaDay;

    @Column(name = "rule_id")
    private String ruleId;

    @Column
    private String state;

    @Column(name = "doc_level")
    private String docLevel;

    @Column(name = "doc_num")
    private String doc_num;

    @Column(name = "doc_source", columnDefinition = "TEXT")
    private String docSource;

    @Column(name = "doc_reason")
    private String docReason;

    @Column(name = "doc_type")
    private String docType;

    @Column(name = "doc_judge_date")
    private String docJudgeDate;

    @Column(name = "doc_title")
    private String docTitle;

    @Column(name = "doc_court")
    private String docCourt;

    @Column(name = "doc_javascript", columnDefinition = "TEXT")
    private String docJavascript;
}
