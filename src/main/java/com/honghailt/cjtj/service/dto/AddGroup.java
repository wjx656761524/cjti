package com.honghailt.cjtj.service.dto;

import com.honghailt.cjtj.domain.Location;

import java.util.List;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 13:50 2019/5/28
 * @Modified By
 */
public class AddGroup {
    private Long  groupId; //单元id

    private String groupName;//单元名称

    private Long campaignId;//  计划id

    private String campaignName;//  计划id

    private Long itemId;//商品id

    private String itemName;//商品名称

    private Long adzoneId;//广告位

    private String adzoneName;//广告名称

    // private Boolean open;  //是否打开

    private Long scopePercent;//溢价

    private Long strategy;//策略

    private String status;//状态  PAUSE("投放暂停"), START("投放开始"), TERMINATE("投放停止")

    /* 操作的店铺nick */
    private String nick;
    /**
     *  人群id
     */
    private Long crowdId;

    private List<Location> locations;

    private List<CrowdList> directionalUnit; // 定向

    /**
     * 人群名称
     */

    private String crowdName;

    /**
     * 人群描述
     */

    private String crowdDesc;

    /**
     *  人群出价，单位：分
     */

    private  Long price;




    /**
     * 定向id
     */

    private Long targetId;

    /**
     * 定向类型
     */

    private String targetType;

    /**
     *
     * 标签值
     */
    private String ptionValue;

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

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
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

    public Long getCrowdId() {
        return crowdId;
    }

    public void setCrowdId(Long crowdId) {
        this.crowdId = crowdId;
    }

    public String getCrowdName() {
        return crowdName;
    }

    public void setCrowdName(String crowdName) {
        this.crowdName = crowdName;
    }

    public String getCrowdDesc() {
        return crowdDesc;
    }

    public void setCrowdDesc(String crowdDesc) {
        this.crowdDesc = crowdDesc;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<CrowdList> getDirectionalUnit() {
        return directionalUnit;
    }

    public void setDirectionalUnit(List<CrowdList> directionalUnit) {
        this.directionalUnit = directionalUnit;
    }

    public String getPtionValue() {
        return ptionValue;
    }

    public void setPtionValue(String ptionValue) {
        this.ptionValue = ptionValue;
    }
}
