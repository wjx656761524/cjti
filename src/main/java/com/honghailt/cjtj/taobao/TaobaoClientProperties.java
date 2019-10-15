package com.honghailt.cjtj.taobao;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * taobao client properties
 */
@ConfigurationProperties(prefix = "taobao.client")
public class TaobaoClientProperties {

    /*app key*/
    private String appKey;
    /*app secret*/
    private String appSecret;
    /*server url api请求url*/
    private String serverUrl;
    /*batch server url 批量api请求url*/
    private String batchServerUrl;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getBatchServerUrl() {
        return batchServerUrl;
    }

    public void setBatchServerUrl(String batchServerUrl) {
        this.batchServerUrl = batchServerUrl;
    }
}
