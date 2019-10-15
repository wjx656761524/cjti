package com.honghailt.cjtj.config.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.honghailt.cjtj.config.Constants.MONGO_TEMPLATE_CJTJ;

/**
 * 智投mongo配置
 */
@ConfigurationProperties(prefix = "mongo.cjtj")
@Configuration
public class CjtjMongoConfiguration extends AbstractMongoConfiguration {

    @Primary
    @Bean("cjtjMongoClient")
    public MongoClient mongoClient() {
        return getMongoClient();
    }

    @Primary
    @Bean("cjtjMongoDatabase")
    public MongoDatabase mongoDatabase() {
        return getMongoDatabase();
    }

    @Bean(MONGO_TEMPLATE_CJTJ)
    @Override
    @Primary
    public MongoTemplate getMongoTemplate() throws Exception {
        return createNew(mongoDbFactory());
    }
}
