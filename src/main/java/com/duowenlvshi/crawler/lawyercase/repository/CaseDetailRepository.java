package com.duowenlvshi.crawler.lawyercase.repository;

import com.duowenlvshi.crawler.lawyercase.model.CaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangchun
 * @since 2018/10/16 15:35
 */
@Repository
public interface CaseDetailRepository extends JpaRepository<CaseDetail, String> {
    String CASE_DETAIL_STATE_00 = "00";//未开始处理
    String CASE_DETAIL_STATE_01 = "01";//处理中
    String CASE_DETAIL_STATE_03 = "03";//异常
    String CASE_DETAIL_STATE_07 = "07";//文档不存在
    String CASE_DETAIL_STATE_08 = "08";//remind错误
    String CASE_DETAIL_STATE_09 = "09";//爬取失败
    String CASE_DETAIL_STATE_10 = "10";//爬取成功

    @Query(nativeQuery = true, value = "SELECT \n" +
            "detail_id,\n" +
            "remarks,\n" +
            "create_date,\n" +
            "doc_id,\n" +
            "update_date,\n" +
            "schema_day,\n" +
            "rule_id,\n" +
            "state,\n" +
            "doc_level,\n" +
            "doc_num,\n" +
            "doc_source,\n" +
            "doc_reason,\n" +
            "doc_type,\n" +
            "doc_judge_date,\n" +
            "doc_title,\n" +
            "doc_court,\n" +
            "doc_javascript\n" +
            "FROM \n" +
            "case_detail where state='00' order by create_date limit 100")
    List<CaseDetail> fetchBatchData();
}
