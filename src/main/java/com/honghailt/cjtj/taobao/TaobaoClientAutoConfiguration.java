package com.honghailt.cjtj.taobao;

import com.taobao.api.ApiException;
import com.taobao.api.BatchTaobaoClient;
import com.taobao.api.ClusterTaobaoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * taobao client auto configuration
 */
@Configuration
@EnableConfigurationProperties(TaobaoClientProperties.class)//指定类的配置
public class TaobaoClientAutoConfiguration {

    @Autowired
    private TaobaoClientProperties properties;

    @Bean
    @ConditionalOnMissingBean(ClusterTaobaoClient.class)
    public ClusterTaobaoClient taobaoClient() throws ApiException {
        return new ClusterTaobaoClient(properties.getServerUrl(), properties.getAppKey(), properties.getAppSecret());
    }

    @Bean
    @ConditionalOnMissingBean(BatchTaobaoClient.class)
    public BatchTaobaoClient batchTaobaoClient() throws ApiException {
        return new BatchTaobaoClient(properties.getBatchServerUrl(), properties.getAppKey(), properties.getAppSecret());
    }

    @Bean
    @ConditionalOnMissingBean(CompositeTaobaoClient.class)
    public CompositeTaobaoClient compositeTaobaoClient(ClusterTaobaoClient taobaoClient, BatchTaobaoClient batchTaobaoClient) {
        return new CompositeTaobaoClient(taobaoClient, batchTaobaoClient);
    }

}
