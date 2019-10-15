package com.honghailt.cjtj.repository;


import com.honghailt.cjtj.domain.Creative;
import com.honghailt.cjtj.domain.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreativeRepository extends JpaRepository<Creative, Long> {

    List<Creative> findCreativeByGroupId (Long groupId);

    List<Creative>findCreativeByCampaignId(Long campaignId);


}
