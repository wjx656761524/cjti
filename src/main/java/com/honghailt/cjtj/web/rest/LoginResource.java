package com.honghailt.cjtj.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.honghailt.cjtj.security.AuthoritiesConstants;
import com.honghailt.cjtj.service.TaobaoOAuthService;
import com.honghailt.cjtj.taobao.TaobaoClientProperties;
import com.honghailt.cjtj.taobao.domain.AccessTokenResponse;
import com.taobao.api.internal.util.WebUtils;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginResource {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JHipsterProperties jHipsterProperties;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaobaoClientProperties taobaoClientProperties;
    @Autowired
    private TaobaoOAuthService taobaoOAuthService;

    /**
     * 卖家中心登录入口
     *
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping("/cjtjLogin")
    public String taobao(@RequestParam String code, HttpServletRequest request) throws Exception {
        log.info("用户登录,code：{}", code);
        String token = tryToken(code);
        log.info("用户登录成功：{}", token);
        AccessTokenResponse accessToken = objectMapper.readValue(token, AccessTokenResponse.class);
        //将nick和sub nick 解码
        accessToken.setTaobao_user_nick(WebUtils.decode(accessToken.getTaobao_user_nick()));
        accessToken.setSub_taobao_user_nick(WebUtils.decode(accessToken.getSub_taobao_user_nick()));
        UserDetails userDetails = taobaoOAuthService.loginWithAccessToken(accessToken, request, false);
        SecurityContextHolder.getContext()
            .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, Lists.newArrayList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))));

        return "redirect:/#/";
    }

    private String tryToken(String code) {
        Map<String, String> params = Maps.newHashMap();
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        params.put("client_id", taobaoClientProperties.getAppKey());
        params.put("client_secret", taobaoClientProperties.getAppSecret());
        params.put("redirect_uri", "cjgj.honghailt.com");
        for (int i = 0; i < 10; i++) {
            try {
                String token = WebUtils.doPost("https://oauth.taobao.com/token", params, 500, 1000);
                return token;
            } catch (Exception e) {
                log.error("获取token异常，准备重试", e);
            }
        }

        throw new RuntimeException("无法正常获取token");
    }
}
