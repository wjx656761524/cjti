package com.honghailt.cjtj.domain;


import java.io.Serializable;



public class CrowdStatus implements Serializable {

    /**
     * 计划id
     */
    private Long campaignId;

    /**
     * 计划名称
     */
    private String campaignName;

    /**
     * 单元id
     */
    private Long adgroupId;

    /**
     * 单元名称
     */
    private String adgroupName;

    /**
     * 人群Id
     */
    private Long crowdId;

    /**
     * 人群名称
     */
    private String crowdName;

    /**
     * 出价
     */
    private Long price;


    /**
     * 人群状态
     */
    private String status;


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

    public Long getAdgroupId() {
        return adgroupId;
    }

    public void setAdgroupId(Long adgroupId) {
        this.adgroupId = adgroupId;
    }

    public String getAdgroupName() {
        return adgroupName;
    }

    public void setAdgroupName(String adgroupName) {
        this.adgroupName = adgroupName;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CrowdStatus{" +
            "campaignId=" + campaignId +
            ", campaignName='" + campaignName + '\'' +
            ", adgroupId=" + adgroupId +
            ", adgroupName='" + adgroupName + '\'' +
            ", crowdId=" + crowdId +
            ", crowdName='" + crowdName + '\'' +
            ", price=" + price +
            ", status='" + status + '\'' +
            '}';
    }
}
