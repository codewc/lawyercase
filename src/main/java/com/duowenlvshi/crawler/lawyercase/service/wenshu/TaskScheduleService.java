package com.duowenlvshi.crawler.lawyercase.service.wenshu;

import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;

/**
 * 调度任务组件
 *
 * @Auther: wangchun
 * @Date: 2018/8/8 18:42
 */
public interface TaskScheduleService {

    /**
     * 按裁判日期分类获取调度任务
     *
     * @param refereeingDay 裁判日期
     * @return
     */
    TaskSchedule getTaskSchedule(String refereeingDay);
}
