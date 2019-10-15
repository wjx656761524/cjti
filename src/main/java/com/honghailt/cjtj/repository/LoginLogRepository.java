package com.honghailt.cjtj.repository;

import com.honghailt.cjtj.domain.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static com.honghailt.cjtj.config.Constants.MONGO_TEMPLATE_CJTJ;

@Component
public class LoginLogRepository {

    @Autowired
    @Qualifier(MONGO_TEMPLATE_CJTJ)
    private MongoTemplate logMongoTemplate;

    public void save(LoginLog loginLog) {
        if (loginLog != null) {
            logMongoTemplate.save(loginLog);
        }
    }

    public Page<LoginLog> find(String nick, Instant startTime, Instant endTime, Pageable pageable) {
        Query query = new Query();
        query.with(pageable);
        query.with(new Sort(Sort.Direction.DESC, "_id"));
        query.addCriteria(Criteria.where("nick").is(nick));
        if (endTime != null) {
            query.addCriteria(Criteria.where("time").gte(startTime));
        }
        if (startTime != null) {
            query.addCriteria(Criteria.where("time").lte(endTime));
        }
        List<LoginLog> list = logMongoTemplate.find(query, LoginLog.class);
        return PageableExecutionUtils.getPage(
            list,
            pageable,
            () -> logMongoTemplate.count(query, LoginLog.class));
    }

}
