package com.duowenlvshi.crawler.lawyercase.webmagic.pipeline.belong;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * 判别是否存储
 *
 * @Author: wangchun
 * @Date: 2018/8/11
 * @Version: 1.0
 */
public interface Condition {

    boolean belong(ResultItems resultItems, Task task);
}
