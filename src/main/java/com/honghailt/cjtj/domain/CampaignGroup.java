package com.honghailt.cjtj.domain;

import java.util.List;

public class CampaignGroup {

    private Campaign campaign;


    private List<GroupStatus> groupStatusList;


    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public List<GroupStatus> getGroupStatusList() {
        return groupStatusList;
    }

    public void setGroupStatusList(List<GroupStatus> groupStatusList) {
        this.groupStatusList = groupStatusList;
    }
}
