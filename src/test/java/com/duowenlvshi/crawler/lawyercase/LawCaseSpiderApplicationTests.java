package com.duowenlvshi.crawler.lawyercase;

import com.duowenlvshi.crawler.lawyercase.model.LawCaseDoc;
import com.duowenlvshi.crawler.lawyercase.repository.LawCaseDocRepository;
import com.duowenlvshi.crawler.lawyercase.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LawCaseSpiderApplicationTests {
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private LawCaseDocRepository repository;

    @Autowired
    private SpiderBootStarter spiderBootStarter;

    @Autowired
    private SpiderLawCaseDocStarter spiderLawCaseDocStarter;

    @Test
    public void contextLoads() {
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
        asyncService.executeAsync();
    }

    @Test
    public void test2() {
        LawCaseDoc caseDoc = new LawCaseDoc();
        caseDoc.setDocId("test" + System.currentTimeMillis());
        caseDoc.setContext("Hello world!");
        repository.insert(caseDoc);
        List<LawCaseDoc> docs = repository.findAll();
        for (LawCaseDoc doc : docs) {
            System.out.println(doc.getDocId());
        }
    }

    @Test
    public void testSeleniumDownloader() {
        spiderBootStarter.testSeleniumDownloader("2018-08-06");
    }

    @Test
    public void testLawCaseDocDownloader() {
        spiderLawCaseDocStarter.executeLawCaseDocDownloader("2018-08-06");
    }

}
