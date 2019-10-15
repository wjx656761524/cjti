package com.honghailt.cjtj.config;

import com.honghailt.cjtj.domain.DateRange;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class DateRangeHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(DateRange.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String startTimeStr = nativeWebRequest.getParameter("startTime");
        String endTimeStr = nativeWebRequest.getParameter("endTime");

        if ("null".equals(startTimeStr) || "null".equals(endTimeStr) || StringUtils.isBlank(startTimeStr) || StringUtils.isBlank(endTimeStr)) {
            return DateRange.getDefaultDateRange();
        }

        return new DateRange(startTimeStr, endTimeStr);
    }
}
