package com.duowenlvshi.crawler.lawyercase.repository;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Auther: wangchun
 * @Date: 2018/8/9 12:12
 */
public interface LawCaseSearchRuleRepository extends MongoRepository<LawCaseSearchRule, String> {

    /**
     * @param refereeingDay 裁判日期
     * @return
     */
    List<LawCaseSearchRule> findLawCaseSearchRulesByRefereeingDay(String refereeingDay);

    /**
     * @param ruleId
     * @return
     */
    LawCaseSearchRule findLawCaseSearchRuleByRuleId(String ruleId);

    /**
     * @param taskId
     * @return
     */
    List<LawCaseSearchRule> findLawCaseSearchRulesByTaskId(String taskId);
}
