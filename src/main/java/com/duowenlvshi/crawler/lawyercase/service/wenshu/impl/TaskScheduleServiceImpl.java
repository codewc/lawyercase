package com.duowenlvshi.crawler.lawyercase.service.wenshu.impl;

import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import com.duowenlvshi.crawler.lawyercase.model.constant.TaskScheduleHelper;
import com.duowenlvshi.crawler.lawyercase.repository.TaskScheduleRepository;
import com.duowenlvshi.crawler.lawyercase.service.wenshu.TaskScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Auther: wangchun
 * @Date: 2018/8/8 18:42
 * @see TaskScheduleService
 */
@Service
@Slf4j
public class TaskScheduleServiceImpl implements TaskScheduleService {


    @Autowired
    private TaskScheduleRepository taskScheduleRepository;

    @Override
    public TaskSchedule getTaskSchedule(String refereeingDay) {
        TaskSchedule schedule = taskScheduleRepository.findFirstByRefereeingDay(refereeingDay);
        if (schedule != null) {
            return schedule;
        }
        schedule = TaskSchedule.builder().
                state(TaskScheduleHelper.STATE_00).
                refereeingDay(refereeingDay).
                createDate(new Date()).
                updateDate(new Date()).build();
        taskScheduleRepository.save(schedule);
        return schedule;
    }
}
