package com.duowenlvshi.crawler.lawyercase.webmagic.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.SpiderListener;

/**
 * @author wangchun
 * @since 2018/10/16 17:05
 */
@Service
@Slf4j
public class DocContextSpiderListener implements SpiderListener {


    @Override
    public void onSuccess(Request request) {
        log.info("success=>{}", request.getUrl());
    }

    @Override
    public void onError(Request request) {
        log.info("error=>{}", request.getUrl());
    }
}
