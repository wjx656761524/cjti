package com.honghailt.cjtj.api.Item;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.api.APIPage;
import com.honghailt.cjtj.api.convert.ItemConverter;
import com.honghailt.cjtj.domain.Item;
import com.honghailt.cjtj.domain.ItemDetail;
import com.honghailt.cjtj.domain.ListPage;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.taobao.CompositeTaobaoClient;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.ItemsSellerListGetRequest;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.ItemsSellerListGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 16:53 2019/5/24
 * @Modified By
 */
@Service
public class ItemAPI {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CompositeTaobaoClient taobaoClient;

    /**获取当前会话用户出售中的商品列表
     * taobao.items.onsale.get
     * @param details
     * @param pageNo
     * @return
     */
    public APIPage<Item> getOnSaleItems(TaobaoUserDetails details, Long pageNo) {
        if (pageNo > 500) {
            throw new IllegalArgumentException("淘宝禁止查询10万之外的数据");
        }
        ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
        req.setFields("cid,num_iid,pic_url,seller_cids,modified,title,num,sold_quantity,price");
        req.setOrderBy("modified:desc");
        req.setPageNo(pageNo);
        req.setPageSize(200L);
        ItemsOnsaleGetResponse rep = taobaoClient.execute(req, details.getPrimarySessionkey());

        List<com.taobao.api.domain.Item> apiItems = rep.getItems();

        List<Item> items = null;
        if (apiItems != null) {
            //接口有时候不报错但是会返回空，宝贝多的店铺到后面那些页就有问题了
            items = apiItems.stream().map((item) -> ItemConverter.convert(item, details.getNick())).collect(Collectors.toList());
        } else {
            log.warn("在线销售宝贝，店铺 [{}] 第 [{}] 页返回空", details.getNick(), pageNo);
        }

        APIPage<Item> result = new APIPage<>(items, pageNo, rep.getTotalResults());

        return result;
    }


    /**
     * 获取宝贝详情
     * taobao.items.seller.list.get
     * @param userDetails
     * @param numIds 宝贝id列表
     * @return
     */
    public List<ItemDetail> getItemDetails(TaobaoUserDetails userDetails, List<Long> numIds) {
        ListPage<Long> idPage = new ListPage<>(numIds, 20);
        List<ItemsSellerListGetRequest> allRequests = Lists.newArrayList();
        while (idPage.hasNext()) {
            List<Long> page = idPage.next();
            ItemsSellerListGetRequest req = new ItemsSellerListGetRequest();
            req.setFields("cid,num_iid,pic_url,seller_cids,modified,title,num,sold_quantity,price,props,sell_point,approve_status,item_img,props_name");
            req.setNumIids(StringUtils.join(page, ","));
            allRequests.add(req);
        }
        List<ItemsSellerListGetResponse> allResponse = taobaoClient.batchRequest(allRequests, userDetails.getSessionKey());
        List<ItemDetail> result = Lists.newArrayList();
        for (ItemsSellerListGetResponse rep : allResponse) {
            if (rep.getItems() != null) {
                List<com.taobao.api.domain.Item> apiItems = rep.getItems();
                for (com.taobao.api.domain.Item apiItem : apiItems) {
                    ItemDetail itemDetail = ItemConverter.convertToItemDetail(apiItem, userDetails.getNick());
                    result.add(itemDetail);
                }
            }
        }
        return result;
    }
}
