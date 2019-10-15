package com.honghailt.cjtj.security;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.domain.User;
import com.honghailt.cjtj.service.UserService;
import com.honghailt.cjtj.utils.SpringMVCUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    @Autowired
    private UserService userService;
//    @Autowired
//    private UserAPI userAPI;
//    @Autowired
//    private ProxyAccountService proxyAccountService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String loginPoint = SpringMVCUtil.getCurrentLoginPoint();
        if ("qck".equals(loginPoint)) {
            return findQuickLoginUser(login);
        } else if ("aem".equals(loginPoint)) {
            return findAEMLoginUser(login);
        } else if ("proxy".equals(loginPoint)) {
            return findProxyLoginUser(login);
        } else {
            throw new IllegalArgumentException("非法请求,username:" + login + ",loginPoint:" + loginPoint);
        }
    }

    private UserDetails findQuickLoginUser(String username) {
        User user = userService.findOneByNick(username);
        // user = userService.updateUserVersionAndExpireDate(user);
        // List<String> authAccounts = getAuthAccounts(user.getNick(), user.getSessionkey());
        List<String> authAccounts = null;
        TaobaoUserDetails details = new TaobaoUserDetails(user.getNick(), username, authAccounts, user.getSessionkey(), user.getSessionkey());
        details.setSellerLogin(false);
        details.setOperateAccount(true);
        details.setPassword("^(^aabc*(c@@%!f@^d~a)^a!#$(b*c!~");
        return details;
    }

    private UserDetails findAEMLoginUser(String username) {
//        List<String> proxyAccounts = proxyAccountService.findAEMAccount(username);
//        TaobaoUserDetails details = new TaobaoUserDetails(null, username, proxyAccounts, null, null);
//        details.setSellerLogin(false);
//        details.setOperateAccount(false);
//        details.setPassword("cde(e#e%(ab^e@d!^cbe!a~ee!)fe#a#");
//        return details;
        return null;
    }

    private UserDetails findProxyLoginUser(String username) {
//        String requestPassword = SpringMVCUtil.getCurrentHttpRequest().getParameter("j_password");
//        String encodePwd = MD5Encrypt.encrypt(requestPassword);
//        List<String> proxyAccounts = proxyAccountService.getAccountProxyAccounts(username, encodePwd);
//        if(CollectionUtils.isEmpty(proxyAccounts)){
//            return null;
//        }else {
//            TaobaoUserDetails details = new TaobaoUserDetails(null, username, proxyAccounts, null, null);
//            details.setSellerLogin(false);
//            details.setOperateAccount(false);
//            details.setPassword(encodePwd);
//            return details;
//        }
        return null;
    }

    /*获取授权列表*/
//    private List<String> getAuthAccounts(String nick, String sessionkey) {
//        List<String> accounts = Lists.newArrayList();
//        accounts.add(nick);
//        try {
//            List<String> authAccounts = userAPI.getAuthAccounts(sessionkey);
//            if (!CollectionUtils.isEmpty(authAccounts)) {
//                accounts.addAll(authAccounts);
//            }
//        } catch (Exception e) {
//            log.error("获取授权账户列表异常", e);
//        }
//        return accounts;
//    }
}
