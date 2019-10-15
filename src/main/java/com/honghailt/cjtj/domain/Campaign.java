package com.honghailt.cjtj.domain;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "cjtj_campaign")
@DynamicUpdate
@DynamicInsert
@Where(clause = "deleted=0")
public class Campaign {

    /**
     * 店铺名称
     */
    private String nick;

    /**
     * 计划id
     */
    @Id
    @Column(name = "campaign_id")
    private Long campaignId;

    /**
     * 计划名称
     */
    @Column(name = "campaign_name")
    private String campaignName;

    /**
     * 日预算金额，单位分
     */
    @Column(name = "day_budget")
    private Long dayBudget;

    /**
     * 投放开始时间
     */
    @Column(name = "begin_time")
    private Instant beginTime;

    /**
     * 投放结束时间
     */
    @Column(name = "end_time")
    private Instant endTime;

    /**
     * 是否永久投放  true 永久投放   false 标准投放
     */
    @Column(name = "launch_forever")
    private Boolean launchForever;

    /**
     *  pause("投放暂停"),start("投放开始"),erminate("投放停止"),abnormal(投放异常"),wait("投放等待中"),delete("删除")
     */
    @Column(name = "status")
    private String status;

    private Boolean deleted;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Boolean getLaunchForever() {
        return launchForever;
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

    public Long getDayBudget() {
        return dayBudget;
    }

    public void setDayBudget(Long dayBudget) {
        this.dayBudget = dayBudget;
    }

    public Instant getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Instant beginTime) {
        this.beginTime = beginTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Boolean isLaunchForever() {
        return launchForever;
    }

    public void setLaunchForever(Boolean launchForever) {
        this.launchForever = launchForever;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
