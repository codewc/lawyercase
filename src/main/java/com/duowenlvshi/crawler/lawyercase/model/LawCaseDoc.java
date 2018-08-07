package com.duowenlvshi.crawler.lawyercase.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: wangchun
 * @Date: 2018/8/7 16:17
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
     * 文档创建时间
     */
    private Date crateDate;

    /**
     * 文档更新时间
     */
    private Date updateDate;
}
