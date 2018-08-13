package com.duowenlvshi.crawler.lawyercase.webmagic.pipeline;

import com.duowenlvshi.crawler.lawyercase.bean.RuleMatchResult;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.model.constant.LawCaseSearchRuleHelper;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseSearchRuleRepository;
import com.duowenlvshi.crawler.lawyercase.webmagic.pipeline.belong.WenShuCourtIndexCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;
import java.util.Map;

/**
 * 案例条件
 *
 * @Author: wangchun
 * @Date: 2018/8/11
 * @Version: 1.0
 * @see LawCaseSearchRule
 */
@Slf4j
@Component("wenShuCourtIndexPipeline")
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LawCaseSearchRulePipeline implements Pipeline {

    @Autowired
    private LawCaseSearchRuleRepository ruleRepository;

    @Autowired
    private WenShuCourtIndexCondition wenShuCourtIndexCondition;

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.info("LawCaseSearchRulePipeline save taskId->{}", task.getUUID());
        if (wenShuCourtIndexCondition.belong(resultItems, task)) {
            Map<String, Object> fields = resultItems.getAll();
            RuleMatchResult matchResult = (RuleMatchResult) fields.get("matchResult");
            LawCaseSearchRule searchRule = ruleRepository.findLawCaseSearchRulesByRuleId(matchResult.getRuleId());
            searchRule.setCreateDate(new Date());
            searchRule.setRuleId(matchResult.getRuleId());
            searchRule.setBatchCaseNum(matchResult.getMatchTotalNum());
            searchRule.getWaitDocIdList().addAll(matchResult.getDocUrls());// 增量增加文档url
            searchRule.setState(LawCaseSearchRuleHelper.STATE_10);
            ruleRepository.save(searchRule);// 更新规则
        }
    }
}
