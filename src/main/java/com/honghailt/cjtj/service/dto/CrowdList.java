package com.honghailt.cjtj.service.dto;

public class CrowdList {

    private Long price; // 出价
    private String crowdName; //人群名称
    private String crowdDesc; // 人群描述
    private String optionName;  // 选项名称
    private DirectionalLabel directionalLabel;


    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
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

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public DirectionalLabel getDirectionalLabel() {
        return directionalLabel;
    }

    public void setDirectionalLabel(DirectionalLabel directionalLabel) {
        this.directionalLabel = directionalLabel;
    }
}
