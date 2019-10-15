package com.honghailt.cjtj.service.dto;

import com.honghailt.cjtj.domain.enumeration.OperationSource;
import com.honghailt.cjtj.domain.enumeration.OperationType;

public class UpdateCampaignOperateStatusDTO {

    /**
     * 店铺账号昵称
     */
    private String nick;

    /**
     * 推广计划ID
     */
    private Long campaignId;

    /**
     * 推广计划名称
     */
    private String campaignName;

    /**
     * 老的操作状态
     */
    private Integer oldOperationStatus;

    /**
     * 新的操作状态
     */
    private Integer newOperationStatus;

    /**
     * 操作源
     */
    private OperationSource operationSource;

    /**
     * 操作类型
     */
    private OperationType operationType;

    /**
     * 操作结果
     */
    private String operationReason;

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

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Integer getOldOperationStatus() {
        return oldOperationStatus;
    }

    public void setOldOperationStatus(Integer oldOperationStatus) {
        this.oldOperationStatus = oldOperationStatus;
    }

    public Integer getNewOperationStatus() {
        return newOperationStatus;
    }

    public void setNewOperationStatus(Integer newOperationStatus) {
        this.newOperationStatus = newOperationStatus;
    }

    public OperationSource getOperationSource() {
        return operationSource;
    }

    public void setOperationSource(OperationSource operationSource) {
        this.operationSource = operationSource;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getOperationReason() {
        return operationReason;
    }

    public void setOperationReason(String operationReason) {
        this.operationReason = operationReason;
    }
}
