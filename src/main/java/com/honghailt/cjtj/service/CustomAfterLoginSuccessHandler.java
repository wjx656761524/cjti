package com.honghailt.cjtj.service;

import com.honghailt.cjtj.domain.LoginEndpoint;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.utils.SpringMVCUtil;
import io.github.jhipster.security.AjaxAuthenticationSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义认证成功处理器
 */
public class CustomAfterLoginSuccessHandler extends AjaxAuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    /**
     * 认证成功后的处理
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication      认证信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        TaobaoUserDetails userDetails = (TaobaoUserDetails) authentication.getPrincipal();
        String ip = SpringMVCUtil.getCurrentIp();
        String loginPoint = SpringMVCUtil.getCurrentLoginPoint();
        String loginPlatform = SpringMVCUtil.isMobileRequest(httpServletRequest) ? "mobile" : "pc";
        String loginClient = SpringMVCUtil.isQNRequest(httpServletRequest) ? "qianniu" : "browser";
        LoginEndpoint loginEndpoint = new LoginEndpoint(loginPoint, loginPlatform, loginClient);
        log.info("用户登录,{},{}", userDetails, loginEndpoint);
        userService.afterLogin(userDetails, loginEndpoint, ip);
        super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
    }
}
