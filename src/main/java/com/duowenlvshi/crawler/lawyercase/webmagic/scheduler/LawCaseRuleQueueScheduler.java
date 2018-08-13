package com.duowenlvshi.crawler.lawyercase.webmagic.scheduler;

import com.duowenlvshi.crawler.lawyercase.webmagic.scheduler.component.FreedomDuplicateRemover;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.scheduler.QueueScheduler;

/**
 * TODO
 *
 * @Author: wangchun
 * @Date: 2018/8/10
 * @Version: 1.0
 * @see FreedomDuplicateRemover
 */
@Slf4j
public class LawCaseRuleQueueScheduler extends QueueScheduler {

    public LawCaseRuleQueueScheduler() {
        super.setDuplicateRemover(new FreedomDuplicateRemover());
    }
}
