package com.duowenlvshi.crawler.lawyercase.webmagic.processor;

import com.duowenlvshi.crawler.lawyercase.model.CaseDetail;
import com.duowenlvshi.crawler.lawyercase.repository.CaseDetailRepository;
import com.duowenlvshi.crawler.lawyercase.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 文档内容正文下载
 *
 * @author wangchun
 * @since 2018/10/16 10:24
 */
@Slf4j
@Component("docContextPageProcessor")
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DocContextPageProcessor implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setRetrySleepTime(10).setTimeOut(15000);

    @Autowired
    private CaseDetailRepository caseDetailRepository;

    @Override
    public void process(Page page) {
        String javaScript = page.getRawText();
        page.putField("javaScript", javaScript);
        log.info(javaScript);
        if (!success(javaScript)) {
            log.warn("未有有效的正文-> {};", javaScript);
            page.setSkip(true);// 跳过解析入库
        }
        CaseDetail caseDetail = caseDetailRepository.fetchOne();
        if (caseDetail != null) {
            page.addTargetRequest(RequestUtils.convertRequest(caseDetail.getDocId()).putExtra("detail", caseDetail));

        }
    }

    private boolean success(String javaScript) {
        boolean ret = false;
        if (StringUtils.isEmpty(javaScript)) {
            return ret;
        }
        if (javaScript.contains("JSON.stringify") && !javaScript.contains("此篇文书不存在!")) {
            ret = true;
        }
        return ret;
    }

    @Override
    public Site getSite() {
        return site;
    }
}
