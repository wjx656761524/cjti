package com.honghailt.cjtj.domain;

public class LoginEndpoint {
    //入口：卖家中心、proxy、quick、aem
    private String point;
    //平台：pc、移动
    private String platform;
    //端：浏览器、千牛
    private String client;

    public LoginEndpoint() {
    }

    public LoginEndpoint(String point, String platform, String client) {
        this.point = point;
        this.platform = platform;
        this.client = client;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "LoginEndpoint{" +
            "point='" + point + '\'' +
            ", platform='" + platform + '\'' +
            ", client='" + client + '\'' +
            '}';
    }
}
