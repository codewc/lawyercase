package com.duowenlvshi.crawler.lawyercase.bean;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wangchun
 * @Date: 2018/8/8
 * @Version: 1.0
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRule {

    /**
     * 任务id
     */
    private String taskId;


    /**
     * 搜索
     */
    private String key;

    /**
     * 搜索条件集合
     */
    private List<String> value = new ArrayList<>();

}
