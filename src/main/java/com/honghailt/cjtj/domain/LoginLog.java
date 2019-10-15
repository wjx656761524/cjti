package com.honghailt.cjtj.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * 登录日志
 */
@Document(collection = "cjtj_login_log")
public class LoginLog {

    @Id
    private String id;

    /*登录人*/
    private String login;
    /*登录的nick*/
    private String nick;
    /*登录时间*/
    private Instant time;
    /*登录来源*/
    private LoginEndpoint endpoint;
    /*登录ip*/
    private String ip;
    /*登录使用的sessionkey*/
    private String sessionkey;

    public LoginLog() {
    }

    public LoginLog(String login, String nick, Instant time, LoginEndpoint endpoint, String ip, String sessionkey) {
        this.login = login;
        this.nick = nick;
        this.time = time;
        this.endpoint = endpoint;
        this.ip = ip;
        this.sessionkey = sessionkey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public LoginEndpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(LoginEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSessionkey() {
        return sessionkey;
    }

    public void setSessionkey(String sessionkey) {
        this.sessionkey = sessionkey;
    }
}
