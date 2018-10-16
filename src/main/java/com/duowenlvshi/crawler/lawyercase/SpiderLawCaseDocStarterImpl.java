package com.duowenlvshi.crawler.lawyercase;

import com.duowenlvshi.crawler.lawyercase.model.CaseDetail;
import com.duowenlvshi.crawler.lawyercase.repository.CaseDetailRepository;
import com.duowenlvshi.crawler.lawyercase.util.RequestUtils;
import com.duowenlvshi.crawler.lawyercase.webmagic.listener.DocContextSpiderListener;
import com.duowenlvshi.crawler.lawyercase.webmagic.pipeline.DocContextPipeline;
import com.duowenlvshi.crawler.lawyercase.webmagic.processor.DocContextPageProcessor;
import com.duowenlvshi.crawler.lawyercase.webmagic.scheduler.LawCaseRuleQueueScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangchun
 * @since 2018/8/14 11:53
 */
@Service("spiderLawCaseDocStarter")
@Slf4j
public class SpiderLawCaseDocStarterImpl implements SpiderLawCaseDocStarter {

    @Autowired
    private DocContextPageProcessor docContextPageProcessor;

    @Autowired
    private CaseDetailRepository caseDetailRepository;

    @Autowired
    private DocContextPipeline docContextPipeline;

    public void executeLawCaseDocDownloader(String refereeingDay) {
        CaseDetail detail = caseDetailRepository.fetchOne();
        Request request = null;
        if (detail != null) {
            String docId = detail.getDocId();
            log.info(docId);
            request = RequestUtils.convertRequest(docId);
            request.putExtra("detail", detail);
        }
        Downloader downloader = new HttpClientDownloader();
        downloader.setThread(1);

        List<SpiderListener> spiderListeners = new ArrayList<>();
        spiderListeners.add(new DocContextSpiderListener());

        Spider.create(docContextPageProcessor)
                .setScheduler(new LawCaseRuleQueueScheduler())
                .addRequest(request)
                .setDownloader(downloader)
                .addPipeline(docContextPipeline)
                .setSpiderListeners(spiderListeners)
                .run();
    }

}
