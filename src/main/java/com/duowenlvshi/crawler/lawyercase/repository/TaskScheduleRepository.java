package com.duowenlvshi.crawler.lawyercase.repository;

import com.duowenlvshi.crawler.lawyercase.model.TaskSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author wangchun
 * @see com.duowenlvshi.crawler.lawyercase.model.TaskSchedule
 * @since 2018/8/8 18:47
 */
public interface TaskScheduleRepository extends MongoRepository<TaskSchedule, String> {

    TaskSchedule findFirstByRefereeingDay(String refereeingDay);
}


