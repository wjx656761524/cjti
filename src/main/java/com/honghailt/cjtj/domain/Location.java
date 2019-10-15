package com.honghailt.cjtj.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 17:09 2019/5/17
 * @Modified By
 */
@Entity
@Table(name = "cjtj_location")
@DynamicInsert
@DynamicUpdate
@Where(clause = "deleted=0")
@Proxy(lazy = false)
public class Location implements Serializable {


    /**
     * 广告位id
     */@Id
    private Long adzoneId;
    /**
     * 广告位名称
     */
    private String adzoneName;



    /**
     * 计划ID
     */
    private Long campaignId;
    /**
     * 计划名称
     */
    private String campaignName;

    /**
     * 单元ID
     */
    private Long groupId;
    /**
     * 单元名称
     */
    private String groupName;
    /**
     * 店铺名称
     */
    private String sick;
    /**
     * 单元状态
     */
    private String statues;
    /**
     * 溢价
     */
    private Long discount;
    private Integer deleted = 0;

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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

    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
    }

    public String getStatues() {
        return statues;
    }

    public void setStatues(String statues) {
        this.statues = statues;
    }


    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

}
