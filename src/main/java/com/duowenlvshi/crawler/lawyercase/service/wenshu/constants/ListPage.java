package com.duowenlvshi.crawler.lawyercase.service.wenshu.constants;

/**
 * 文书列表页中使用到的常量值
 * <br>http://wenshu.court.gov.cn/list/list/?sorttype=1</>
 *
 * @author wangchun
 * @since 2018/8/8 15:18
 */
public interface ListPage {

    /**
     * <!--左侧树-->
     */
    interface TreeKey {
        /**
         * 匹配的key属性
         * @see #MATCH_RULE
         * @see #LEVEL_CASE
         * @see #COURT_HIERARCHY
         * @see #COURT_AREA
         * @see #REFEREE_YEAR
         * @see #TRIAL_PROCEDURE
         * @see #DOC_CATEGORY
         */
        String MATCH_RULE = "key";
        String KEY_WORD = "关键词";
        String LEVEL_CASE = "一级案由";
        String COURT_HIERARCHY = "法院层级";
        String COURT_AREA = "法院地域";
        String REFEREE_YEAR = "裁判年份";
        String TRIAL_PROCEDURE = "审判程序";
        String DOC_CATEGORY = "文书类型";
    }
}
