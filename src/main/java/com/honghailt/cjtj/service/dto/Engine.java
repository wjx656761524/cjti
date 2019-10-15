package com.honghailt.cjtj.service.dto;

import com.honghailt.cjtj.domain.GroupStatus;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 9:01 2019/5/23
 * @Modified By
 */
public class Engine {

    public String nick; //店铺名称
    public Long campaignId;//计划id
    public String campaignName;//计划名称
    public Long discount;//溢价
    public Long groupId;//单元id
    public String groupName; //单元名称
    public String groupStatus;//单元状态
    public Long adzoneId;//广告id
    public String adzoneName; //广告名称
    public Long itemId;//创意id
    public String itemName; //创意名称
    public GroupStatus group ;//单元对象


    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

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

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
    }

    public Long getAdzoneId() {
        return adzoneId;
    }

    public void setAdzoneId(Long adzoneId) {
        this.adzoneId = adzoneId;
    }

    public String getAdzoneName() {
        return adzoneName;
    }

    public void setAdzoneName(String adzoneName) {
        this.adzoneName = adzoneName;
    }

    public GroupStatus getGroup() {
        return group;
    }

    public void setGroup(GroupStatus group) {
        this.group = group;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
