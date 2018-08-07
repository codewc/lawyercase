package com.duowenlvshi.crawler.lawyercase.exception;

import org.openqa.selenium.remote.ErrorCodes;
import org.openqa.selenium.remote.Response;

/**
 * @Auther: wangchun
 * @Date: 2018/8/7 11:38
 */
public class CommonErrorHandler extends org.openqa.selenium.remote.ErrorHandler {

    public CommonErrorHandler(boolean includeServerErrors) {
        super(includeServerErrors);
    }

    public CommonErrorHandler(ErrorCodes codes, boolean includeServerErrors) {
        super(codes, includeServerErrors);
    }

    @Override
    public boolean isIncludeServerErrors() {
        return super.isIncludeServerErrors();
    }

    @Override
    public void setIncludeServerErrors(boolean includeServerErrors) {
        super.setIncludeServerErrors(includeServerErrors);
    }

    @Override
    public Response throwIfResponseFailed(Response response, long duration) throws RuntimeException {
        return super.throwIfResponseFailed(response, duration);
    }
}
