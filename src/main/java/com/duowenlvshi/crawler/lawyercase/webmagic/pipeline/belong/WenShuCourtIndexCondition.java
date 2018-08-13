package com.duowenlvshi.crawler.lawyercase.webmagic.pipeline.belong;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * 首页处理判断
 *
 * @Author: wangchun
 * @Date: 2018/8/11
 * @Version: 1.0
 */
@Component("wenShuCourtIndexCondition")
public class WenShuCourtIndexCondition implements Condition {

    @Override
    public boolean belong(ResultItems resultItems, Task task) {
        return true;
    }
}
