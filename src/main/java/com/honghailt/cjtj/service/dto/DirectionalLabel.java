package com.honghailt.cjtj.service.dto;

import java.util.List;

public class DirectionalLabel {

    private Long labelId;  // 标签id
    private Long targetId; // 定向id
    private String targetType; // 定向类型
    private String labelValue; // 标签值
    private List<options> options;

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public List<com.honghailt.cjtj.service.dto.options> getOptions() {
        return options;
    }

    public void setOptions(List<com.honghailt.cjtj.service.dto.options> options) {
        this.options = options;
    }
}
