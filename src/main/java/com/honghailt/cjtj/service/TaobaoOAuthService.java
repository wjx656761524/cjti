package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.LoginEndpoint;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.domain.User;
import com.honghailt.cjtj.taobao.domain.AccessTokenResponse;
import com.honghailt.cjtj.utils.SpringMVCUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 淘宝认证服务
 */
@Service
public class TaobaoOAuthService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    //private UserAPI userAPI;

    /**
     * 通过accesstoken（淘宝code参数转换得到）登录
     *
     * @param accessToken
     * @return
     */
    public TaobaoUserDetails loginWithAccessToken(AccessTokenResponse accessToken, HttpServletRequest request, boolean isQnLogin) {

        User user = userService.findOneByNick(accessToken.getTaobao_user_nick());
        if (user == null) {
            user = userService.createUserByAccessToken(accessToken,isQnLogin);
        } else {
            user = userService.updateUserByAccessToken(user, accessToken,isQnLogin);
        }

        String login = StringUtils.isNotEmpty(accessToken.getSub_taobao_user_nick())
            ? accessToken.getSub_taobao_user_nick()
            : accessToken.getTaobao_user_nick();

        List<String> authAccounts = Lists.newArrayList(accessToken.getTaobao_user_nick());

//        try {
//            List<String> apiAuthAccounts = userAPI.getAuthAccounts(accessToken.getAccess_token());
//            authAccounts.addAll(apiAuthAccounts);
//        } catch (Exception e) {
//            log.error("获取账户授权列表失败", e);
//        }

        TaobaoUserDetails details = new TaobaoUserDetails(accessToken.getTaobao_user_nick(), login, authAccounts, accessToken.getAccess_token(), user.getSessionkey());

        String loginPlatform = SpringMVCUtil.isMobileRequest(request) ? "mobile" : "pc";
        String loginClient = SpringMVCUtil.isQNRequest(request) ? "qianniu" : "browser";
        LoginEndpoint loginEndpoint = new LoginEndpoint("seller", loginPlatform, loginClient);
        String ip = SpringMVCUtil.getCurrentIp();
        userService.afterLogin(details, loginEndpoint, ip);

        return details;
    }
}
