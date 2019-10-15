package com.honghailt.cjtj.utils;

import com.honghailt.cjtj.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SpringMVCUtil {

    /**
     * 获取当前请求
     *
     * @return
     */
    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request;
        }
        return null;
    }

    /**
     * 获取当前请求ip
     *
     * @return
     */
    public static String getCurrentIp() {
        return getRequestIp(getCurrentHttpRequest());
    }

    // 获得请求的ip地址
    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    // 获取当前登录入口
    public static String getCurrentLoginPoint() {
        HttpServletRequest request = getCurrentHttpRequest();
        String val = request.getParameter(Constants.LOGIN_TYPE_FORM_ATTR_NAME);
        return StringUtils.isBlank(val) ? "proxy" : val;
    }

    /**
     * 是否千牛请求
     *
     * @param request
     * @return
     */
    public static boolean isQNRequest(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        return agent != null && agent.toLowerCase().contains("qianniu");
    }

    /**
     * 是否移动端请求
     *
     * @param request
     * @return
     */
    public static boolean isMobileRequest(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        //TODO 未测试移动千牛请求头是啥样
        return agent != null && agent.toLowerCase().contains("mobile");
    }
}
