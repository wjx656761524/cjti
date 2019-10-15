package com.honghailt.cjtj.service.dto;

import java.time.Instant;

/**
 * @Auther: wangzhuang
 * @Date: 2019/5/24 14:33
 * @Description:
 */
public class UpdateCampaignDto {

    /**
     * 店铺账号昵称
     */
    public String nick;


    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * 计划名称
     */
    private String campaignName;

    /**
     * 日预算金额，单位分
     */
    private Long dayBudget;

    /**
     * 投放开始时间
     */
    private String beginTime;

    /**
     * 投放结束时间
     */
    private String endTime;

    /**
     * 推广计划ID
     */
    public Long campaignId;

    public  String status;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
