package com.honghailt.cjtj.web.rest;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.Campaign;
import com.honghailt.cjtj.domain.CampaignReport;
import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.CampaignService;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangzhuang
 * @Date: 2019/6/12 17:45
 * @Description:
 */
@RestController
@RequestMapping("/api/report")
public class ReportResource {


    @Autowired
    CampaignService campaignService;


    /**
     * 获取全部计划信息
     * @param dateRange
     * @param syn
     * @return
     */
    @GetMapping("/getCampaignReport")
    public Map getCampaignReport(DateRange dateRange,
                                          @RequestParam(defaultValue = "false") Boolean syn) {

        Map map = new HashMap();
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<Campaign> campaigns = campaignService.getCampaigns(details, syn);
        List<CampaignReport> allCampaignReport = Lists.newArrayList();
        //所有计划所有日期的报表
        if (!CollectionUtils.isEmpty(campaigns)) {
            for (Campaign campaign : campaigns) {
                        List<CampaignReport> campaignReports = campaignService.getCampaignReport(details, campaign.getCampaignId(), dateRange, syn);
                        if (!CollectionUtils.isEmpty(campaignReports)) {
                            allCampaignReport.addAll(campaignReports);
                }
            }
        }
            List<StatusAndReportVM> results = campaignService.buildStatusAndReportResults(campaigns, allCampaignReport, dateRange);
            CampaignReport report = ReportUtils.mergeReport(allCampaignReport);
        map.put("k1",report);
        map.put("k2",results);

            return map;
        }
}
