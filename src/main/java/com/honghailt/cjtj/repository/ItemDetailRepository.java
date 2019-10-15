package com.honghailt.cjtj.repository;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.ItemDetail;
import com.honghailt.cjtj.domain.enumeration.ApproveStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

import static com.honghailt.cjtj.config.Constants.MONGO_TEMPLATE_CJTJ;

@Component
public class ItemDetailRepository {

    @Autowired
    @Qualifier(MONGO_TEMPLATE_CJTJ)
    private MongoTemplate cjtjMongoTemplate;

    /**
     * 通过宝贝id获取单个宝贝详情
     *
     * @param numId 宝贝id
     * @return
     */
    public ItemDetail findOne(Long numId) {
        return cjtjMongoTemplate.findOne(Query.query(Criteria.where("_id").is(numId)), ItemDetail.class);
    }

    /**
     * 通过宝贝id列表获取宝贝详情
     *
     * @param ids 宝贝id列表
     * @return
     */
    public List<ItemDetail> findByIds(Collection<Long> ids) {
        List<ItemDetail> result = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(ids)) {
            Query query = Query.query(Criteria.where("_id").in(ids));
            result = cjtjMongoTemplate.find(query, ItemDetail.class);
        }
        return result;
    }

    /**
     * 保存宝贝详情
     *
     * @param itemDetails
     */
    public void save(List<ItemDetail> itemDetails) {
        if (CollectionUtils.isEmpty(itemDetails)) {
            return;
        }
        BulkOperations bulk = cjtjMongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ItemDetail.class);
        for (ItemDetail item : itemDetails) {
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
                .set("num", item.getNum())
                .set("props", item.getProps())
                .set("sellPoint", item.getSellPoint())
                .set("approveStatus", item.getApproveStatus())
                .set("itemImgs",item.getItemImgs());
            bulk.upsert(query, update);
        }
        bulk.execute();
    }

    /**
     * 删除宝贝详情
     *
     * @param itemIds
     */
    public void delete(Collection<Long> itemIds) {
        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }
        Query query = Query.query(Criteria.where("_id").in(itemIds));
        Update update = Update.update("approveStatus", ApproveStatus.deleted);
        cjtjMongoTemplate.updateMulti(query, update, ItemDetail.class);
    }

}
