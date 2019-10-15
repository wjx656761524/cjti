package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.honghailt.cjtj.api.APIPage;
import com.honghailt.cjtj.api.Item.ItemAPI;
import com.honghailt.cjtj.api.adgroup.AdGroupAPI;
import com.honghailt.cjtj.domain.GroupStatus;
import com.honghailt.cjtj.domain.Item;
import com.honghailt.cjtj.domain.ItemDetail;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.repository.ItemDetailRepository;
import com.honghailt.cjtj.repository.ItemRepository;
import com.taobao.api.domain.ItemImg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 宝贝 service
 */
@Service
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemAPI itemAPI;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private AdGroupAPI adGroupAPI;
    @Autowired
    private ItemDetailRepository itemDetailRepository;

    /**
     * 通过宝贝id获取单个在线销售宝贝信息
     *
     * @param itemId
     * @return
     */
    public Item findOneOnSale(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    /**
     * 通过宝贝id列表获取在线销售宝贝信息
     *
     * @param itemIds
     * @return
     */
    public List<Item> findOnSaleByIds(List<Long> itemIds) {
        return itemRepository.findByIds(itemIds);
    }


    /**
     * 同步在线销售宝贝，这里不返回，因为有可能列表很大导致内存不足
     *
     * @param details
     */
    public void synOnSaleItem(TaobaoUserDetails details) {
        log.info("店铺 [{}] 开始同步在线销售宝贝", details.getNick());
        APIPage<Item> itemPage = itemAPI.getOnSaleItems(details, 1L);
        log.info("店铺 [{}] 获取到在线销售宝贝数量 ： {}", details.getNick(), itemPage.getTotal());
        itemRepository.deleteByNick(details.getNick());
        itemRepository.save(itemPage.getContent());
        if (itemPage.getTotal() > 200) {
            //如果多于一页，则处理其他页
            processOtherPageOnSale(details, itemPage.getTotal());
        }
        log.info("店铺 [{}] 成功同步在线销售宝贝", details.getNick());
    }

    private void processOtherPageOnSale(TaobaoUserDetails details, Long total) {
        Long pageCount = total % 200 == 0 ? total / 200 : total / 200 + 1;
        for (Long page = 2L; page <= pageCount && page <= 500; page++) {
            synAndSaveItem(details, page);
        }
    }

    /**
     * @param details
     * @param pageNo
     * @return
     */
    private void synAndSaveItem(TaobaoUserDetails details, Long pageNo) {
        try {
            APIPage<Item> itemPage = itemAPI.getOnSaleItems(details, pageNo);
            itemRepository.save(itemPage.getContent());
        } catch (Exception e) {
            log.error("同步店铺 [" + details.getNick() + "] 第 " + pageNo + " 页异常", e);
        }
    }

    /**
     * 获取单个宝贝详情
     *
     * @param itemId
     * @param userDetails
     * @param synIfDBNotExist 是否在数据库中没有的时候从接口同步
     * @return
     */
    public ItemDetail findOneItemDetail(Long itemId, TaobaoUserDetails userDetails, boolean synIfDBNotExist) {
        ItemDetail item = itemDetailRepository.findOne(itemId);
        if (item == null && synIfDBNotExist) {
            item = forceOneItemDetail(itemId, userDetails);
        }
        return item;
    }

    /**
     * 强制从接口同步单个宝贝
     *
     * @param itemId
     * @param userDetails
     * @return
     */
    public ItemDetail forceOneItemDetail(Long itemId, TaobaoUserDetails userDetails) {
        List<ItemDetail> apiResult = synItemDetail(Lists.newArrayList(itemId), userDetails);
        ItemDetail item = null;
        if (apiResult != null && apiResult.size() == 1) {
            item = apiResult.get(0);
        }
        return item;
    }

    /**
     * 根据宝贝id列表同步宝贝详情
     *
     * @param numIds      宝贝id列表
     * @param userDetails
     * @return
     */
    public List<ItemDetail> synItemDetail(List<Long> numIds, TaobaoUserDetails userDetails) {
        if (CollectionUtils.isEmpty(numIds)) {
            return Lists.newArrayList();
        }

        List<ItemDetail> items = itemAPI.getItemDetails(userDetails, numIds);
        itemDetailRepository.save(items);

        Map<Long, ItemDetail> apiResultMap = items.stream().collect(Collectors.toMap(ItemDetail::getId, Function.identity()));

        //接口不返回的就是删除了
        List<Long> deletedItemIds = Lists.newArrayList();
        for (Long numId : numIds) {
            if (!apiResultMap.containsKey(numId)) {
                deletedItemIds.add(numId);
            }
        }
        itemDetailRepository.delete(deletedItemIds);

        return items;
    }

    /**
     * 获取指定计划下除所有单元绑定宝贝外的所有宝贝
     *
     * @param campaignId
     * @param userDetails
     * @param syn
     * @return
     */
    public List<ItemDetail> getItemDetailsByCampaignId(Long campaignId, TaobaoUserDetails userDetails, Boolean syn) {
        if (campaignId == null) {
            return null;
        }
        List<Item> ItemDetails = Lists.newArrayList();
        List<Long> campaigns = Lists.newArrayList();
        List<Long> ItemIds = Lists.newArrayList();
        campaigns.add(campaignId);
        //查询指定计划下的单元
       List<GroupStatus> units=adGroupAPI.pageGroupByAPI(userDetails.getSessionKey(),null, campaigns,null,null,null,null);
         if(null!=units) {
             for (GroupStatus unit : units) {
                 Long ItemId = unit.getItemId();
                 ItemIds.add(ItemId);
             }
         }
        List<ItemDetail> itemDetails = Lists.newArrayList();
        List<Item> items = itemRepository.queryItemsByCampaignOutSize(userDetails.getNick(), ItemIds);
        if (null != items)
            for (Item item : items) {
                ItemDetail itemDetail = this.findOneItemDetail(item.getId(), userDetails, true);
                ItemDetail itemDetailNew=  limitItemImgs(itemDetail);
                itemDetails.add(itemDetailNew);
            }
        return itemDetails;

    }

    /**
     * 取前三张图片
     */
    public ItemDetail limitItemImgs(ItemDetail itemDetail) {
        List<ItemImg> itemImgs = itemDetail.getItemImgs();
        if (null == itemImgs) return null;
        int size = itemImgs.size();
        List<ItemImg> list = Lists.newArrayList();
        if (size >= 3) {
            for (int i = 0; i < 3; i++) {
                list.add(itemImgs.get(i));
            }
            itemDetail.setItemImgs(list);
            return itemDetail;
        }else {
            return itemDetail;

        }



    }
}
