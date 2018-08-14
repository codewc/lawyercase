package com.duowenlvshi.crawler.lawyercase.webmagic.pipeline;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseSearchRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.Set;

/**
 * @author wangchun
 * @since 2018/8/14 11:06
 */
@Slf4j
@Component("lawCaseDocPipeline")
public class LawCaseDocPipeline implements Pipeline {

    @Autowired
    private LawCaseDocRepository lawCaseDocRepository;

    @Autowired
    private LawCaseSearchRuleRepository searchRuleRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.info("LawCaseSearchRulePipeline save taskId->{}", task.getUUID());
        String docHtmlContext = resultItems.get("docHtmlContext");
        String ruleId = resultItems.get("ruleId");
        String docId = resultItems.get("docId");
        String sourceUrl = resultItems.get("sourceUrl");
        String refereeingDay = resultItems.get("refereeingDay");
        try {
            updateLawCaseSearchRule(ruleId, docId);
            saveLawCaseDoc(docId, sourceUrl, refereeingDay, docHtmlContext);
        } catch (Exception e) {
            log.error("发生了错误", e);
        }
    }

    private void updateLawCaseSearchRule(String ruleId, String docId) {
        LawCaseSearchRule searchRule = searchRuleRepository.findLawCaseSearchRuleByRuleId(ruleId);
        if (searchRule != null) {
            Set<String> waitDocIdList = searchRule.getWaitDocIdList();
            Set<String> dealDocIdList = searchRule.getDealDocIdList();
            // change docId location
            waitDocIdList.remove(docId);
            waitDocIdList.remove("http://wenshu.court.gov.cn/content/content?DocID=" + docId);
            dealDocIdList.add(docId);
            searchRuleRepository.save(searchRule);
        }
    }

    private void saveLawCaseDoc(String docId, String sourceUrl, String refereeingDay, String context) {
        LawCaseDoc caseDoc = new LawCaseDoc();
        caseDoc.setDocId(docId);
        caseDoc.setSourceUrl(sourceUrl);
        caseDoc.setCrateDate(new Date());
        caseDoc.setUpdateDate(new Date());
        caseDoc.setRefereeingDay(refereeingDay);
        caseDoc.setContext(context);
        caseDoc.setErrorCode("0");
        caseDoc.setErrorMsg("处理成功！");
        lawCaseDocRepository.save(caseDoc);
    }
}
