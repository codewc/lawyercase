package com.duowenlvshi.crawler.lawyercase.repository;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther: wangchun
 * @Date: 2018/8/7 16:31
 */
public interface LawCaseDocRepository extends MongoRepository<LawCaseDoc, String> {

    /**
     * @param sourceUrl
     * @return
     */
    LawCaseDoc findLawCaseDocBySourceUrl(String sourceUrl);
}
