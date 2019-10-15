package com.honghailt.cjtj.service.dto;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 13:59 2019/5/17
 * @Modified By
 */
public class DelGroup {

        private String sick;

        private Long campaignId;

        private Long groupId;

        private String operationReason;

    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
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

    public String getOperationReason() {
        return operationReason;
    }

    public void setOperationReason(String operationReason) {
        this.operationReason = operationReason;
    }
}
