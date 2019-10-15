package com.honghailt.cjtj.taobao.domain;

public class AccessTokenResponse {

    private String access_token;
    private String taobao_user_nick;
    private String taobao_user_id;
    private String sub_taobao_user_nick;
    private String sub_taobao_user_id;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
    private Long re_expires_in;
    private Long r1_expires_in;
    private Long r2_expires_in;
    private Long w1_expires_in;
    private Long w2_expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getTaobao_user_nick() {
        return taobao_user_nick;
    }

    public void setTaobao_user_nick(String taobao_user_nick) {
        this.taobao_user_nick = taobao_user_nick;
    }

    public String getTaobao_user_id() {
        return taobao_user_id;
    }

    public void setTaobao_user_id(String taobao_user_id) {
        this.taobao_user_id = taobao_user_id;
    }

    public String getSub_taobao_user_nick() {
        return sub_taobao_user_nick;
    }

    public void setSub_taobao_user_nick(String sub_taobao_user_nick) {
        this.sub_taobao_user_nick = sub_taobao_user_nick;
    }

    public String getSub_taobao_user_id() {
        return sub_taobao_user_id;
    }

    public void setSub_taobao_user_id(String sub_taobao_user_id) {
        this.sub_taobao_user_id = sub_taobao_user_id;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Long getRe_expires_in() {
        return re_expires_in;
    }

    public void setRe_expires_in(Long re_expires_in) {
        this.re_expires_in = re_expires_in;
    }

    public Long getR1_expires_in() {
        return r1_expires_in;
    }

    public void setR1_expires_in(Long r1_expires_in) {
        this.r1_expires_in = r1_expires_in;
    }

    public Long getR2_expires_in() {
        return r2_expires_in;
    }

    public void setR2_expires_in(Long r2_expires_in) {
        this.r2_expires_in = r2_expires_in;
    }

    public Long getW1_expires_in() {
        return w1_expires_in;
    }

    public void setW1_expires_in(Long w1_expires_in) {
        this.w1_expires_in = w1_expires_in;
    }

    public Long getW2_expires_in() {
        return w2_expires_in;
    }

    public void setW2_expires_in(Long w2_expires_in) {
        this.w2_expires_in = w2_expires_in;
    }

    @Override
    public String toString() {
        return "AccessTokenResponse{" +
            "access_token='" + access_token + '\'' +
            ", taobao_user_nick='" + taobao_user_nick + '\'' +
            ", taobao_user_id='" + taobao_user_id + '\'' +
            ", sub_taobao_user_nick='" + sub_taobao_user_nick + '\'' +
            ", sub_taobao_user_id='" + sub_taobao_user_id + '\'' +
            ", token_type='" + token_type + '\'' +
            ", expires_in=" + expires_in +
            ", refresh_token='" + refresh_token + '\'' +
            ", re_expires_in=" + re_expires_in +
            ", r1_expires_in=" + r1_expires_in +
            ", r2_expires_in=" + r2_expires_in +
            ", w1_expires_in=" + w1_expires_in +
            ", w2_expires_in=" + w2_expires_in +
            '}';
    }
}
