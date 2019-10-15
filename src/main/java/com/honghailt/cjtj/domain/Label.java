package com.honghailt.cjtj.domain;

import java.io.Serializable;
import java.util.List;

public class Label implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long labelId;

    private Long targetId;

    private String targetType;

    private String labelName;

    private String labelDesc;

    private String labelValue;

    private List<options> optionsList;

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

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelDesc() {
        return labelDesc;
    }

    public void setLabelDesc(String labelDesc) {
        this.labelDesc = labelDesc;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public List<options> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<options> optionsList) {
        this.optionsList = optionsList;
    }
}
