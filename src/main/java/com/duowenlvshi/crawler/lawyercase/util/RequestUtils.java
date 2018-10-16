package com.duowenlvshi.crawler.lawyercase.util;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求工具
 *
 * @author wangchun
 * @since 2018/10/16 10:44
 */
public class RequestUtils {
    //    payload = {"DocID": doc_id, }
    //    headers = {'Accept': 'text/javascript, application/javascript, */*',
    //            'Accept-Encoding': 'gzip, deflate',
    //            'Accept-Language': 'zh-CN,zh;q=0.9',
    //            'Proxy - Connection': 'keep - alive',
    //            "Referer": "http://wenshu.court.gov.cn/content/content?DocID={}&KeyWord=".format(doc_id),
    //            'Host': 'wenshu.court.gov.cn',
    //            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36',
    //            'X-Requested-With': 'XMLHttpRequest',
    //    }
    /**
     * 文档正文下载请求地址
     */
    public static final String DOC_CONTEXT_REQUEST_TEMPLATE = "http://wenshu.court.gov.cn/CreateContentJS/CreateContentJS.aspx?DocID=%s";

    /**
     * 包装请求报文
     *
     * @param request
     * @param docId
     * @return
     */
    public static Request convertRequest(String docId) {
        Request request = new Request();
        request.addHeader("Accept", "text/javascript, application/javascript, */*");
        request.addHeader("Accept-Encoding", "gzip, deflate");
        request.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
        request.addHeader("Proxy - Connection", "keep - alive");
        request.addHeader("Referer", String.format("http://wenshu.court.gov.cn/content/content?DocID=%s&KeyWord=", docId));
        request.addHeader("Host", "wenshu.court.gov.cn");
        request.setUrl(String.format(DOC_CONTEXT_REQUEST_TEMPLATE, docId));
        Map<String, Object> params = new HashMap<>();
        params.put("DocID", docId);
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.form(params, "UTF-8"));
        return request;
    }

}
