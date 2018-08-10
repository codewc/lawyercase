package com.duowenlvshi.crawler.lawyercase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

@SpringBootApplication
@RestController
@EnableAsync
public class LawCaseSpiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawCaseSpiderApplication.class, args);
    }

}
