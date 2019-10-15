package com.honghailt.cjtj.domain;

import java.io.Serializable;
import java.util.List;

public class AddCrowd implements Serializable {
    private Long campaignId;
    private Long adgroupId;
    private Long targetId;
    private String targetType;
    private Long labelId;
    private String labelValue;
    private Long price;
    private String optionName;
    private boolean checked;
    private String crowdName;
    private String crowdDesc;

    private List<String> optionValue;

    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }

    public Long getAdgroupId() {
        return adgroupId;
    }

    public void setAdgroupId(Long adgroupId) {
        this.adgroupId = adgroupId;
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

    public Long getLabelId() {
        return labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<String> getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(List<String> optionValue) {
        this.optionValue = optionValue;
    }

    public String getCrowdName() {
        return crowdName;
    }

    public void setCrowdName(String crowdName) {
        this.crowdName = crowdName;
    }

    public String getCrowdDesc() {
        return crowdDesc;
    }

    public void setCrowdDesc(String crowdDesc) {
        this.crowdDesc = crowdDesc;
    }
}
