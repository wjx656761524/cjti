package com.honghailt.cjtj.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Column;
import javax.persistence.Id;
import java.time.Instant;

/**
 * @Author: wangzhuang
 * @Date: 2019/5/27 13:50
 * @Description: 推广计划日志
 */
@Document(collection = "cjtj_campaign_log")
public class LogCampaign {

    /**
     * 店铺名称
     */
    private String nick;

    /**
     * 计划id
     */
    @Id
    @Column(name = "campaign_id")
    private String id;

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
     *  pause("投放暂停"),start("投放开始"),erminate("投放停止"),abnormal(投放异常"),wait("投放等待中"),delete("删除")
     */
    @Column(name = "status")
    private String status;

    /**
     * 投放结束时间
     */
    @Column(name = "end_time")
    private Instant endTime;

    /**
     * 投放开始时间
     */
    @Column(name = "begin_time")
    private Instant beginTime;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Instant beginTime) {
        this.beginTime = beginTime;
    }
}
