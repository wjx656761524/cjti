package com.honghailt.cjtj.service.dto;

import com.honghailt.cjtj.domain.enumeration.OperationSource;
import com.honghailt.cjtj.domain.enumeration.OperationType;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 13:38 2019/5/19
 * @Modified By
 */
public class UpdateGroupDto {
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

    /**
     * 修改前的推广状态
     */
    private String oldStatus;

    /**
     * 修改后的推广状态
     */
    private String newStatus;

    /**
     * 修改前的单元名称
     */
    private String oldGroupName;

    /**
     * 修改后的单元名称
     */
    private String newGroupName;

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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getOldGroupName() {
        return oldGroupName;
    }

    public void setOldGroupName(String oldGroupName) {
        this.oldGroupName = oldGroupName;
    }

    public String getNewGroupName() {
        return newGroupName;
    }

    public void setNewGroupName(String newGroupName) {
        this.newGroupName = newGroupName;
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
