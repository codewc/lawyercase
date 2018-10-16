package com.duowenlvshi.crawler.lawyercase.controller;

import com.duowenlvshi.crawler.lawyercase.SpiderLawCaseDocStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangchun
 * @since 2018/10/16 17:41
 */
@RestController("/spider")
public class SpiderController {

    @Autowired
    private SpiderLawCaseDocStarter spiderLawCaseDocStarter;

    @RequestMapping("/execute")
    public void execute() {
        spiderLawCaseDocStarter.executeLawCaseDocDownloader("");
    }

}
