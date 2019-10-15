package com.honghailt.cjtj.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cjtj_crowd")
@DynamicUpdate
@DynamicInsert
public class Crowd implements Serializable {

    /**
     *  人群id
     */
    @Id
    @Column(name = "crowd_id")
    private Long crowdId;

    /**
     * 人群名称
     */
    @Column(name = "crowd_name")
    private String crowdName;

    /**
     * 人群描述
     */
    @Column(name = "crowd_desc")
    private String crowdDesc;

    /**
     *  人群出价，单位：分
     */
    @Column(name = "price")
    private Long price;

    /**
     * 计划id
     */
    @Column(name = "campaign_id")
    private Long campaignId;

    /**
     * 计划名称
     */
    @Column(name = "campaign_name")
    private String campaignName;

    /**
     * 单元id
     */
    @Column(name = "adgroup_id")
    private Long adgroupId;

    /**
     * 单元名称
     */
    @Column(name = "adgroup_name")
    private String adgroupName;


    /**
     * 人群状态  ----PAUSE("投放暂停"),START("投放开始"),ERMINATE("投放停止"),ABNORMAL(投放异常"),WAIT("投放等待中"),DELETE("删除")
     */
    @Column(name = "status")
    private String status;


    /**
     * 定向id
     */
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 定向类型
     */
    @Column(name = "target_type")
    private String targetType;


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

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getAdgroupId() {
        return adgroupId;
    }

    public void setAdgroupId(Long adgroupId) {
        this.adgroupId = adgroupId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getAdgroupName() {
        return adgroupName;
    }

    public void setAdgroupName(String adgroupName) {
        this.adgroupName = adgroupName;
    }

    @Override
    public String toString() {
        return "Crowd{" +
            "crowdId=" + crowdId +
            ", crowdName='" + crowdName + '\'' +
            ", crowdDesc='" + crowdDesc + '\'' +
            ", price=" + price +
            ", campaignId=" + campaignId +
            ", campaignName='" + campaignName + '\'' +
            ", adgroupId=" + adgroupId +
            ", adgroupName='" + adgroupName + '\'' +
            ", status='" + status + '\'' +
            ", targetId=" + targetId +
            ", targetType='" + targetType + '\'' +
            '}';
    }
}
