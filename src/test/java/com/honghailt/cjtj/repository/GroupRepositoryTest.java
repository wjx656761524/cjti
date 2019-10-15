package com.honghailt.cjtj.repository;

import com.honghailt.cjtj.CjtjApp;
import com.honghailt.cjtj.api.campaign.CampaignAPI;
import com.honghailt.cjtj.domain.Campaign;
import com.honghailt.cjtj.domain.GroupStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CjtjApp.class)
@ActiveProfiles({"dev"})
public class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void findCampaignListTest() {
        GroupStatus groupStatus = groupRepository.getOne(151787051l);
        System.out.println("");
    }

}
