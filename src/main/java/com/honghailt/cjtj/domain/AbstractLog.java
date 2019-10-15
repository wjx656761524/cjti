package com.honghailt.cjtj.domain;

public abstract class AbstractLog {
    /**
     * 店铺账号昵称
     */
    private String nick;

    /**
     * 推广计划ID
     */
    private Long campaignId;

    /**
     * 推广单元ID
     */
    private Long groupId;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
