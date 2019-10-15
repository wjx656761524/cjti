package com.honghailt.cjtj.repository;

import com.honghailt.cjtj.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findCampaignsByNick(String nick);

    Campaign findCampaignByCampaignId(Long campaignId);

}
