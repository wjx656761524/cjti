package com.honghailt.cjtj.web.rest;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.Item;
import com.honghailt.cjtj.domain.ItemDetail;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.ItemService;
import com.honghailt.cjtj.service.dto.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 12:00 2019/5/24
 * @Modified By
 */
@RestController
@RequestMapping("/api/item")
public class ItemResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ItemService itemService;

    /**
     * 获取商品的信息详情
     *
     * @param itemId
     * @param syn
     * @return
     */
    @GetMapping("/getItemDetailsByItemId")
    public ItemDetail getItemDetailsByItemId(@RequestParam Long itemId, @RequestParam(defaultValue = "true") Boolean syn) {
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        ItemDetail item = new ItemDetail();
        item = itemService.findOneItemDetail( itemId, details, syn);
        return item;

    }

    /**
     * 获取指定计划下除所有单元绑定宝贝外的所有宝贝
     * @param campaignId
     * @param syn
     * @return
     */

    @GetMapping("/getItemDetailsByCampaignId")
    public List<ItemDetail> getItemDetailsByCampaignId(@RequestParam Long campaignId, @RequestParam(defaultValue = "true") Boolean syn) {
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<ItemDetail> items = Lists.newArrayList();
        items = itemService.getItemDetailsByCampaignId(campaignId, details, syn);
        return items;


    }
}
