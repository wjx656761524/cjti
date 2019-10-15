package com.honghailt.cjtj.service;

import com.honghailt.cjtj.api.adgroup.AdGroupAPI;
import com.honghailt.cjtj.api.campaign.CampaignAPI;
import com.honghailt.cjtj.api.crowd.CrowdAPI;
import com.honghailt.cjtj.domain.Campaign;
import com.honghailt.cjtj.domain.GroupStatus;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.repository.CampaignRepository;
import com.honghailt.cjtj.repository.GroupRepository;
import com.honghailt.cjtj.security.SecurityUtils;
import com.taobao.api.response.FeedflowItemCampaignPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *  同步计划、单元、定向、接口数据到本地数据库
 */
@Service
public class SynItfData {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CampaignAPI campaignAPI;

    @Autowired
    private AdGroupAPI adGroupAPI;

    @Autowired
    private CrowdAPI crowdAPI;


    public void synCampaign(){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        FeedflowItemCampaignPageResponse rsp = campaignAPI.campaignPage(null,null,null,null,null,details.getSessionKey());
        for (FeedflowItemCampaignPageResponse.CampaignDTo campaignDTo: rsp.getResult().getResults()) {
            Campaign campaign = new Campaign();
            campaign.setCampaignId(campaignDTo.getCampaignId());
            campaign.setCampaignName(campaignDTo.getCampaignName());
            campaign.setDayBudget(campaignDTo.getDayBudget());
            campaign.setStatus(campaignDTo.getStatus());
            campaign.setBeginTime(campaignDTo.getLaunchTime().getBeginTime().toInstant());
            campaign.setEndTime(campaignDTo.getLaunchTime().getEndTime().toInstant());
            campaign.setLaunchForever(campaignDTo.getLaunchTime().getLaunchForever());
            campaignRepository.save(campaign);
        }
    }

    public List<GroupStatus>  synGroup(){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<GroupStatus> Lists = adGroupAPI.pageGroupByAPI(details.getSessionKey(),null,null,null,null,null,null);
        for(GroupStatus groupStatus: Lists){
            groupRepository.save(groupStatus);
        }
        return Lists;
    }

    public void synCroed(List<GroupStatus> groupStatuses){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        for (GroupStatus groupStatus: groupStatuses) {
            crowdAPI.crowdPage(null,groupStatus.getGroupId(),null,null,null,null,details.getSessionKey());
        }
    }
}
