package com.honghailt.cjtj.repository;

import com.honghailt.cjtj.domain.LogCampaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import static com.honghailt.cjtj.config.Constants.MONGO_TEMPLATE_CJTJ;

/**
 * @Author: wangzhuang
 * @Date: 2019/5/27 14:08
 * @Description: 推广计划日志
 */
@Component
public class LogCampaignRepository {

    @Autowired
    @Qualifier(MONGO_TEMPLATE_CJTJ)
    private MongoTemplate mongoTemplate;

    public void save(LogCampaign logCampaign){
            if (logCampaign!=null){
                    mongoTemplate.save(logCampaign);
            }
    }
}
