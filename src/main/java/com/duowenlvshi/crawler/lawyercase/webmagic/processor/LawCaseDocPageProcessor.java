package com.duowenlvshi.crawler.lawyercase.webmagic.processor;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.model.LawCaseSearchRule;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.util.RuleMatchUtils;
import com.duowenlvshi.crawler.lawyercase.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author wangchun
 * @since 2018/8/14 10:51
 */
@Slf4j
@Component("lawCaseDocPageProcessor")
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LawCaseDocPageProcessor implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setRetrySleepTime(10);

    @Autowired
    private LawCaseDocRepository lawCaseDocRepository;

    @Override
    public void process(Page page) {
        Request request = page.getRequest();
        LawCaseSearchRule searchRule = (LawCaseSearchRule) request.getExtra("rule");
        page.putField("ruleId", searchRule.getRuleId());
        String sourceUrl = request.getUrl();
        String docId = RuleMatchUtils.analyzeDocId(sourceUrl);
        page.putField("docId", docId);
        page.putField("sourceUrl", sourceUrl);
        page.putField("refereeingDay", searchRule.getRefereeingDay());
        LawCaseDoc lawCaseDoc = lawCaseDocRepository.findLawCaseDocBySourceUrl(sourceUrl);
        if (lawCaseDoc != null) {
            log.info("doc已经存在-> {};", lawCaseDoc.getDocId());
            page.setSkip(true);// 跳过解析入库
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
