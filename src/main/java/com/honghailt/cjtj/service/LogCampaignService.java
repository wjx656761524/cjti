package com.honghailt.cjtj.service;

import com.honghailt.cjtj.domain.LogCampaign;
import com.honghailt.cjtj.repository.LogCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: wangzhuang
 * @Date: 2019/5/27 14:01
 * @Description: 推广计划日志
 */
@Service
@Transactional
public class LogCampaignService {

    @Autowired
    private LogCampaignRepository logCampaignRepository;

    public void save(LogCampaign logCampaign){
        logCampaignRepository.save(logCampaign);
    }
}
