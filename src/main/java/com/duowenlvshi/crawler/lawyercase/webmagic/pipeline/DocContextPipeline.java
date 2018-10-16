package com.duowenlvshi.crawler.lawyercase.webmagic.pipeline;

import com.duowenlvshi.crawler.lawyercase.model.CaseDetail;
import com.duowenlvshi.crawler.lawyercase.repository.CaseDetailRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Date;

/**
 * @author wangchun
 * @since 2018/10/16 12:04
 */
@Slf4j
@Component("docContextPipeline")
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocContextPipeline implements Pipeline {

    @Autowired
    private CaseDetailRepository caseDetailRepository;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String javaScript = resultItems.get("javaScript");
        CaseDetail caseDetail = (CaseDetail) resultItems.getRequest().getExtra("detail");
        caseDetail.setDocJavascript(javaScript);
        caseDetail.setUpdateDate(new Date());
        caseDetail.setState(matchState(javaScript));
        caseDetailRepository.saveAndFlush(caseDetail);
    }


    public static String matchState(String javaScript) {
        String match = CaseDetailRepository.CASE_DETAIL_STATE_09;
        if (StringUtils.isEmpty(javaScript)) {
            return match;
        }
        if (javaScript.contains("JSON.stringify")) {
            match = CaseDetailRepository.CASE_DETAIL_STATE_10;
        }
        if (javaScript.contains("此篇文书不存在!")) {
            match = CaseDetailRepository.CASE_DETAIL_STATE_07;
        }
        return match;
    }
}
