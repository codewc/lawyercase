package com.duowenlvshi.crawler.lawyercase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableAsync
public class LawCaseSpiderApplication {

    @Autowired
    SpiderBootStarter starter;

    public static void main(String[] args) {
        SpringApplication.run(LawCaseSpiderApplication.class, args);
    }

    @RequestMapping("/execute")
    public void execute(String refereeingDay) {
        starter.testSeleniumDownloader("2018-08-09");
    }

}
