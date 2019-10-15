package com.honghailt.cjtj.service.mapper;

import com.honghailt.cjtj.CjtjApp;
import com.honghailt.cjtj.domain.LogCampaign;
import com.honghailt.cjtj.service.LogCampaignService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: wangzhuang
 * @Date: 2019/5/27 14:43
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CjtjApp.class)
@ActiveProfiles({"dev"})
public class LogCampaignServicetest {

    @Autowired
    private LogCampaignService logCampaignService;

    @Test
    public void saveCam(){
        LogCampaign logCampaign = new LogCampaign();
        logCampaign.setId("5552222111");
        logCampaign.setStatus("aaaaa");
        logCampaignService.save(logCampaign);

    }

}
