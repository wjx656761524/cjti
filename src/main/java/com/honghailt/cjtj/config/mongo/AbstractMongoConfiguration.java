package com.honghailt.cjtj.config.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * mongo 配置抽象类
 */
public abstract class AbstractMongoConfiguration {
    //Mongo DB Properties
    private String mongoUrl;
    private String database;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public String getMongoUrl() {
        return mongoUrl;
    }

    public void setMongoUrl(String mongoUrl) {
        this.mongoUrl = mongoUrl;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    protected MongoClient getMongoClient(){
        if(mongoClient == null){
            MongoClientOptions.Builder options = MongoClientOptions.builder();
            options.socketKeepAlive(true);
            options.socketTimeout(900000);
            mongoClient = new MongoClient(new MongoClientURI(mongoUrl,options));
        }
        return mongoClient;
    }

    protected MongoDatabase getMongoDatabase(){
        if(mongoDatabase == null){
            mongoDatabase = getMongoClient().getDatabase(database);
        }
        return mongoDatabase;
    }

    /*
     * Method that creates MongoDbFactory
     * Common to both of the MongoDb connections
     */
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(getMongoClient(), database);
    }

    /*
     * Factory method to create the MongoTemplate
     */
    abstract public MongoTemplate getMongoTemplate() throws Exception;

    protected MongoTemplate createNew(MongoDbFactory dbFactory){
        //remove _class
        MappingMongoConverter converter =
                new MappingMongoConverter(new DefaultDbRefResolver(dbFactory), new MongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(dbFactory,converter);
    }
}
