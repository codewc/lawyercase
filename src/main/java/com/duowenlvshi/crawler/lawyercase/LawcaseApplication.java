package com.duowenlvshi.crawler.lawyercase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LawcaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawcaseApplication.class, args);
    }
}
