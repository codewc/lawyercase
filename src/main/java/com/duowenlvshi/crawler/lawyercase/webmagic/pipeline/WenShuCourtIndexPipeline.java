package com.duowenlvshi.crawler.lawyercase.webmagic.pipeline;

import com.duowenlvshi.crawler.lawyercase.webmagic.pipeline.belong.WenShuCourtIndexCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * TODO
 *
 * @Author: wangchun
 * @Date: 2018/8/11
 * @Version: 1.0
 */
@Slf4j
@Component("wenShuCourtIndexPipeline")
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WenShuCourtIndexPipeline implements Pipeline {

    @Autowired
    private WenShuCourtIndexCondition wenShuCourtIndexCondition;

    @Override
    public void process(ResultItems resultItems, Task task) {
        log.info("WenShuCourtIndexPipeline save taskId->{}", task.getUUID());
        if (wenShuCourtIndexCondition.belong(resultItems, task)) {

        }
    }
}
