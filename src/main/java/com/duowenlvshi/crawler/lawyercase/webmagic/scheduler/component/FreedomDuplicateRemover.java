package com.duowenlvshi.crawler.lawyercase.webmagic.scheduler.component;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

/**
 * 自定义url不去重限制。由业务层面processor去控制
 *
 * @Author: wangchun
 * @Date: 2018/8/10
 * @Version: 1.0
 */
public class FreedomDuplicateRemover extends HashSetDuplicateRemover implements DuplicateRemover {

    // all request will be process
    @Override
    public boolean isDuplicate(Request request, Task task) {
        return false;
    }
}
