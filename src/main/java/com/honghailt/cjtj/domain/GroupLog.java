package com.honghailt.cjtj.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.honghailt.cjtj.domain.enumeration.OperationSource;
import com.honghailt.cjtj.domain.enumeration.OperationType;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 15:35 2019/5/17
 * @Modified By
 */
@Document(collection = "cjtj_group_log")
public class GroupLog extends AbstractLog {

    /**
     * 老的操作状态
     */
    private String oldStatus;

    /**
     * 新的操作状态
     */
    private String newStatus;

    /**
     * 老的单元名称
     */
    private String oldGroupName;

    /**
     * 新的单元名称
     */
    private String newGroupName;

    /**
     * 操作源
     */
    @Enumerated(EnumType.STRING)
    private OperationSource operationSource;

    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    /**
     * 操作结果
     */
    private String operationReason;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Instant operatonTime;

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

    public Instant getOperatonTime() {
        return operatonTime;
    }

    public void setOperatonTime(Instant operatonTime) {
        this.operatonTime = operatonTime;
    }
}
