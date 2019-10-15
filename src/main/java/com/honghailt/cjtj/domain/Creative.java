package com.honghailt.cjtj.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cjtj_creative")
@DynamicUpdate
@DynamicInsert
@Proxy(lazy = false)
@Where(clause = "deleted=0")
public class Creative {

    /**
     * 店铺名称
     */
    @Column(name = "nick")
    private String nick;
    /**
     * 创意id
     */
    @Id
    @Column(name = "creative_id")
    private Long creativeId;

    /**
     * 图片地址
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 创意文案
     */
    @Column(name = "title")
    private String title;

    /**
     * 审核状态，W待审核，P审核通过，R审核拒绝
     */
    @Column(name = "audit_status")
    private String auditStatus;

    /**
     * 审核拒绝原因
     */
    @Column(name = "audit_reason")
    private String auditReason;

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
    @Column(name = "group_id")
    private Long groupId;
    /**
     * 单元名称
     */
    @Column(name = "group_name")
    private String groupName;

    /**
     * 创意名称
     */
    @Column(name = "creative_name")
    private String creativeName;
    @Column(name = "deleted")
    private Integer deleted = 0;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Long getCreativeId() {
        return creativeId;
    }

    public void setCreativeId(Long creativeId) {
        this.creativeId = creativeId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
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

    public String getCreativeName() {
        return creativeName;
    }

    public void setCreativeName(String creativeName) {
        this.creativeName = creativeName;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
