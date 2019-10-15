package com.honghailt.cjtj.repository;


import com.honghailt.cjtj.domain.GroupStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 13:53 2019/5/13
 * @Modified By
 */
public interface GroupRepository extends JpaRepository<GroupStatus, Long>, JpaSpecificationExecutor<GroupStatus> {
    List<GroupStatus> findByCampaignId(Long campaignId);
    GroupStatus  findByGroupId(Long croupId);
}
