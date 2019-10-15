package com.honghailt.cjtj.domain;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.security.AuthoritiesConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TaobaoUserDetails implements UserDetails {

    /* 操作的店铺nick */
    private String nick;

    /* 登录人 */
    private String login;

    /* 授权的账户列表 */
    private List<String> authAccounts;

    /* 登录时的sessionkey，通常使用这个属性就行 */
    private String sessionKey;

    /* 店铺主账户的sessionkey，尽量不要使用这个，有风险 */
    private String primarySessionkey;

    /*是否卖家中心登录，默认true卖家中心登录*/
    private boolean isSellerLogin = true;

    /*是否已经可以操作账号，代理登录的应该先选择要操作的账号*/
    private boolean operateAccount = true;

    private String password;

    public TaobaoUserDetails(String nick, String login, List<String> authAccounts, String sessionKey, String primarySessionkey) {
//        if (isBlank(nick) || isBlank(login) || isBlank(sessionKey) || isBlank(primarySessionkey)) {
//            throw new IllegalArgumentException("基础信息不能为空");
//        }
        if (isBlank(login)) {
            throw new IllegalArgumentException("基础信息不能为空");
        }
        this.nick = nick;
        this.login = login;
        this.authAccounts = authAccounts;
        this.sessionKey = sessionKey;
        this.primarySessionkey = primarySessionkey;
    }

    private List<GrantedAuthority> authorities = Lists.newArrayList(new SimpleGrantedAuthority(AuthoritiesConstants.USER));

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return login;
    }

    public List<String> getAuthAccounts() {
        return authAccounts;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getPrimarySessionkey() {
        return primarySessionkey;
    }

    public void setPrimarySessionkey(String primarySessionkey) {
        this.primarySessionkey = primarySessionkey;
    }

    public boolean isOperateAccount() {
        return operateAccount;
    }

    public void setOperateAccount(boolean operateAccount) {
        this.operateAccount = operateAccount;
    }

    public boolean isSellerLogin() {
        return isSellerLogin;
    }

    public void setSellerLogin(boolean sellerLogin) {
        isSellerLogin = sellerLogin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "TaobaoUserDetails{" +
            "nick='" + nick + '\'' +
            ", login='" + login + '\'' +
            ", authAccounts=" + authAccounts +
            ", isSellerLogin=" + isSellerLogin +
            ", operateAccount=" + operateAccount +
            '}';
    }
}
