package com.honghailt.cjtj.service.dto;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 13:41 2019/5/30
 * @Modified By
 */
public class AddCampaignDTO {

    private String campaignName;
    private String startTime;
    private String endTime;
    private Long dayBudget;
    private Boolean launchForever;

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getDayBudget() {
        return dayBudget;
    }

    public void setDayBudget(Long dayBudget) {
        this.dayBudget = dayBudget;
    }

    public Boolean getLaunchForever() {
        return launchForever;
    }

    public void setLaunchForever(Boolean launchForever) {
        this.launchForever = launchForever;
    }
}
