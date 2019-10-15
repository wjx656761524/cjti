package com.honghailt.cjtj.api;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.CjtjApp;
import com.honghailt.cjtj.api.campaign.CampaignAPI;
import com.honghailt.cjtj.domain.Campaign;
import com.honghailt.cjtj.service.CampaignService;
import com.taobao.api.response.FeedflowItemCampaignAddResponse;
import com.taobao.api.response.FeedflowItemCampaignModifyResponse;
import com.taobao.api.response.FeedflowItemCampaignPageResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CjtjApp.class)
@ActiveProfiles({"dev"})
public class CampaignAPITest {

    @Autowired
    private CampaignAPI campaignAPI;
    @Autowired
    private CampaignService campaignService;

    @Test
    public void findCampaignListTest() {
        List<String> status = Lists.newArrayList();
        status.add("delete");
        // status.add("start");
        // status.add("erminate");
        campaignAPI.selectCampaignPage(null,null,null,null,status,"6202a057631d0ZZb62fe8aee7562f11df1f6e0854d9f5662200657116219");
    }

    @Test
    public void updateCampaignTest() {

        FeedflowItemCampaignModifyResponse pause = campaignAPI.modifyCampaign(null, 551812301L, "pause",
                                                                                null, null, null, null, null,
                                                                                null, null, null,
                                                                                "6200e2676638fbd8eecf0be62041af4359ZZfdfa90932652200657116219");
        System.out.println(pause);
    }

    @Test
    public void updateModifybind(){
        FeedflowItemCampaignModifyResponse softTest9 = campaignAPI.modifyCampaign("超级推荐_商品推广_SoftTest1", 551812301L, null,
            null, null, null, null, null,
            null, null, null,
            "6200e2676638fbd8eecf0be62041af4359ZZfdfa90932652200657116219");

    }

    @Test
    public void updateModifybd(){
        FeedflowItemCampaignModifyResponse softTest9 = campaignAPI.modifyCampaign(null, 551812301L, null,
            null, null, null, null, null,
            1000000L, null, null,
            "6200e2676638fbd8eecf0be62041af4359ZZfdfa90932652200657116219");

    }

    @Test
    public void getCampaignReport() {
        campaignAPI.campaignRptdailylist("2019-05-12", 582153855l, "2019-06-10", "6202a057631d0ZZb62fe8aee7562f11df1f6e0854d9f5662200657116219");
    }


}
