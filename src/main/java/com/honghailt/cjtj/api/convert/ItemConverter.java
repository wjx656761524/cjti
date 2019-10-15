package com.honghailt.cjtj.api.convert;

import com.honghailt.cjtj.domain.Item;
import com.honghailt.cjtj.domain.ItemDetail;
import com.honghailt.cjtj.domain.enumeration.ApproveStatus;

public class ItemConverter {

    public static Item convert(com.taobao.api.domain.Item apiItem, String nick){
        Item result = new Item();
        setProperties(result,apiItem,nick);
        return result;
    }

    public static ItemDetail convertToItemDetail(com.taobao.api.domain.Item apiItem, String nick){
        ItemDetail result = new ItemDetail();
        setProperties(result,apiItem,nick);
        result.setProps(apiItem.getProps());
        result.setSellPoint(apiItem.getSellPoint());
        result.setApproveStatus(ApproveStatus.valueOf(apiItem.getApproveStatus()));
        result.setItemImgs(apiItem.getItemImgs());
        result.setPropsName(apiItem.getPropsName());
        return result;
    }

    private static void setProperties(Item result,com.taobao.api.domain.Item apiItem,String nick){
        result.setId(apiItem.getNumIid());
        result.setTitle(apiItem.getTitle());
        result.setCid(apiItem.getCid());
        result.setSellerCids(apiItem.getSellerCids());
        result.setPrice(apiItem.getPrice());
        result.setNick(nick);
        result.setNum(apiItem.getNum());
        result.setModified(apiItem.getModified().toInstant());
        result.setPicUrl(apiItem.getPicUrl());
        result.setSoldQuantity(apiItem.getSoldQuantity());
    }
}
