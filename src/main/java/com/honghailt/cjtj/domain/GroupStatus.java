package com.honghailt.cjtj.domain;


import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * Created with IDEA
 * author:wujinxian
 * Date:2019/5/11
 * Time:15:20
 */
@Entity
@Table(name = "cjtj_group_status")
@DynamicInsert
@DynamicUpdate
@Where(clause = "deleted=0")
@Proxy(lazy = false)
public class GroupStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long  groupId; //单元id

    private String groupName;//单元名称


    private Long campaignId;//  计划id
    private String campaignName;//  计划id


    private Long itemId;//商品id
    private String itemName;//商品名称
    private String picUrl;//商品图片


   // private Boolean open;  //是否打开

    private Long scopePercent;//溢价

    private Long strategy;//策略

    private String status;//状态  PAUSE("投放暂停"), START("投放开始"), TERMINATE("投放停止")
    /* 操作的店铺nick */
    private String nick;


    private Integer deleted = 0;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getScopePercent() {
        return scopePercent;
    }

    public void setScopePercent(Long scopePercent) {
        this.scopePercent = scopePercent;
    }

    public Long getStrategy() {
        return strategy;
    }

    public void setStrategy(Long strategy) {
        this.strategy = strategy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
