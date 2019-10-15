package com.honghailt.cjtj.domain;

/**
 * 计划报表数据对象
 */
public class CampaignReport extends AbstractReport {

    private Long campaignId;

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }
}
