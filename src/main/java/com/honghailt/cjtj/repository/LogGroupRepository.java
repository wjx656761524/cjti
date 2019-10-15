package com.honghailt.cjtj.repository;

import com.honghailt.cjtj.domain.GroupLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.List;

import static com.honghailt.cjtj.config.Constants.MONGO_TEMPLATE_CJTJ;

@Component
public class LogGroupRepository {
    @Autowired
    @Qualifier(MONGO_TEMPLATE_CJTJ)
    private MongoTemplate mongoTemplate;

    /**
     * 保存或更新数据
     * @param logList
     */
    public void save(List<GroupLog> logList) {
        if (CollectionUtils.isEmpty(logList)){
            return;
        }
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, GroupLog.class);
        bulkOperations.insert(logList);
        bulkOperations.execute();
    }

    public Page<GroupLog> find(String nick, Long campaignId, Long adgroupId, Instant startTime, Instant endTime, Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        query.with(new Sort(Sort.Direction.DESC, "operatonTime"));
        query.addCriteria(Criteria.where("nick").is(nick));
        if(campaignId!=null && campaignId!=0)
            query.addCriteria(Criteria.where("campaignId").is(campaignId));
        if(adgroupId!=null && adgroupId!=0)
            query.addCriteria(Criteria.where("adgroupId").is(adgroupId));
        if(startTime!=null && endTime!=null){
            Criteria operatonTime = Criteria.where("operatonTime").gte(startTime).lte(endTime);
            query.addCriteria(operatonTime);
        }
        List<GroupLog> list = mongoTemplate.find(query, GroupLog.class);
        return PageableExecutionUtils.getPage(list, pageable,
            () -> mongoTemplate.count(query, GroupLog.class));
    }

}
