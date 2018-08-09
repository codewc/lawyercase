package com.duowenlvshi.crawler.lawyercase.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 爬取案例文档内容
 *
 * @author wangchun
 * @since 2018/8/7 16:17
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LawCaseDoc implements Serializable {
    /**
     * 文档id
     */
    @Id
    private String docId;

    /**
     * 文档来源地址
     */
    private String sourceUrl;

    /**
     * 文档内容
     */
    private String context;

    /**
     * 裁判日期：2018-08-08
     */
    private String refereeingDay;

    /**
     * 应用爬取ip
     */
    private String ip;

    /**
     * 文档创建时间
     */
    private Date crateDate;

    /**
     * 文档更新时间
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
