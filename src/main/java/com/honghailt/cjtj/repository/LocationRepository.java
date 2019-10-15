package com.honghailt.cjtj.repository;

import com.honghailt.cjtj.domain.GroupStatus;
import com.honghailt.cjtj.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 17:36 2019/5/17
 * @Modified By
 */
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {
    List<Location> findLocationByGroupId(Long groupId);
    List<Location>findByCampaignId(Long campaignId);

}
