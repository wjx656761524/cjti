package com.honghailt.cjtj.repository;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.honghailt.cjtj.config.Constants.MONGO_TEMPLATE_CJTJ;

@Component
public class ItemRepository {

    @Autowired
    @Qualifier(MONGO_TEMPLATE_CJTJ)
    private MongoTemplate cjtjMongoTemplate;

    /**
     * 获取单个在线销售宝贝
     *
     * @param itemId
     * @return
     */
    public Item findOne(Long itemId) {
        return cjtjMongoTemplate.findOne(Query.query(Criteria.where("_id").is(itemId)), Item.class);
    }

    /**
     * 通过宝贝id列表获取在线销售宝贝
     *
     * @param ids
     * @return
     */
    public List<Item> findByIds(List<Long> ids) {
        List<Item> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(ids)) {
            Query query = Query.query(Criteria.where("_id").in(ids));
            result = cjtjMongoTemplate.find(query, Item.class);
        }
        return result;
    }

    public List<Long> specialFindShopItemIds(String nick, Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        Query query = Query.query(Criteria.where("_id").in(ids).and("nick").is(nick));
        query.fields().include("_id");
        return cjtjMongoTemplate.find(query, Long.class, cjtjMongoTemplate.getCollectionName(Item.class));
    }

    /**
     * 通过nick删除在线销售宝贝
     *
     * @param nick
     */
    public void deleteByNick(String nick) {
        Query query = Query.query(Criteria.where("nick").is(nick));
        cjtjMongoTemplate.remove(query, Item.class);
    }

    public void save(List<Item> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        BulkOperations bulk = cjtjMongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Item.class);
        for (Item item : items) {
            //保存的时候检查是否已有这个宝贝，因为从接口取的数据有可能不同页之间有重复的
            Query query = Query.query(Criteria.where("_id").is(item.getId()));
            Update update = Update.update("_id", item.getId())
                .set("nick", item.getNick())
                .set("title", item.getTitle())
                .set("cid", item.getCid())
                .set("picUrl", item.getPicUrl())
                .set("sellerCids", item.getSellerCids())
                .set("soldQuantity", item.getSoldQuantity())
                .set("modified", item.getModified())
                .set("price", item.getPrice())
                .set("num", item.getNum());
            bulk.upsert(query, update);
        }
        bulk.execute();
    }

    public List<Item> queryItemsByCampaignOutSize(String nick, List<Long> numids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("nick").is(nick));
        if (numids != null && numids.size() > 0)
            query.addCriteria(Criteria.where("_id").nin(numids));
        List<Item> content = new ArrayList<>();
        content = cjtjMongoTemplate.find(query, Item.class);
        return content;
    }
}
