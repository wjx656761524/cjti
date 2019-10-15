package com.honghailt.cjtj.repository;


import com.honghailt.cjtj.domain.Crowd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface CrowdRepository  extends JpaRepository<Crowd, Long> {

    public List<Crowd> findByCampaignIdAndAdgroupId(Long campaignId, Long adgroupId);

    public List<Crowd> findByCampaignId(Long campaignId);

}
