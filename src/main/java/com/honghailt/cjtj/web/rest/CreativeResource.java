package com.honghailt.cjtj.web.rest;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.api.creative.CreativeAPI;
import com.honghailt.cjtj.domain.*;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.CreativeService;
import com.honghailt.cjtj.service.UserService;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.OperationResultVM;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 14:44 2019/5/20
 * @Modified By
 */
@RestController
@RequestMapping("/api/creative")
public class CreativeResource {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService userService;

    @Autowired
    private CreativeService creativeService;
    @Autowired
    private CreativeAPI creativeAPI;

    /**
     * 获取创意状态和报表信息
     * @param campaignId
     * @param dateRange
     * @return
     */
    @GetMapping("/getAll")
    public List<StatusAndReportVM> getCreativesBygroupId(@RequestParam Long campaignId ,@RequestParam Long groupId , DateRange dateRange,@RequestParam Boolean syn){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<Creative> creatives = creativeService.findCreatives(details,campaignId,groupId, syn);
        List<StatusAndReportVM> results = Lists.newArrayList();
        for(Creative creative: creatives) {
            List<CreativeReport> creativeReports = creativeService.findCreativeReport(details,campaignId,creative.getGroupId(),creative.getCreativeId(),dateRange,syn);
            StatusAndReportVM vm = new StatusAndReportVM(creative, ReportUtils.mergeReport(creativeReports),  creativeReports);
            results.add(vm);
        }
        return results;
    }
    /**
     * 获取创意状态和报表信息
     * @param campaignId
     * @param dateRange
     * @return
     */
    @GetMapping("/getCreativesReport")
    public Map getCreativesReport(@RequestParam Long campaignId ,@RequestParam Long groupId , DateRange dateRange,@RequestParam Boolean syn){
        List<CreativeReport>  creativeReportlists =new ArrayList();
        CreativeReport allCreativeReport=new  CreativeReport();
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<Creative> creatives = creativeService.findCreatives(details,campaignId,groupId, syn);
        List<StatusAndReportVM> results = Lists.newArrayList();
        for(Creative creative: creatives) {
            List<CreativeReport> creativeReports = creativeService.findCreativeReport(details,creative.getCampaignId(),creative.getGroupId(),creative.getCreativeId(),dateRange,syn);
            for(CreativeReport creativeReport:creativeReports){
                creativeReportlists .add(creativeReport);
            }
            StatusAndReportVM vm = new StatusAndReportVM(creative, ReportUtils.mergeReport(creativeReports),  creativeReports);
            results.add(vm);
        }
        allCreativeReport=ReportUtils.mergeReport(creativeReportlists);
        Map resultMap =new HashMap<>();
        resultMap.put("allCreativeReport", allCreativeReport);
        resultMap.put("results",results);
        return  resultMap;
    }

   /* public Map getCreativesReport(@RequestParam Long campaignId ,@RequestParam Long groupId , DateRange dateRange,@RequestParam Boolean syn){
        List<CreativeReport>  creativeReportlists = Lists.newArrayList();
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<Creative> creatives = creativeService.findCreatives(details,campaignId,groupId, syn);
       *//* List<StatusAndReportVM> results = Lists.newArrayList();
        for(Creative creative: creatives) {
            List<CreativeReport> creativeReports = creativeService.findCreativeReport(details,campaignId,groupId,creative.getCreativeId(),dateRange,syn);
            for(CreativeReport creativeReport:creativeReports){
                creativeReportlists .add(creativeReport);
            }
            StatusAndReportVM vm = new StatusAndReportVM(creative, ReportUtils.mergeReport(creativeReports),  creativeReports);
            results.add(vm);
        }*//*
        //所有计划所有日期的报表
        if (!CollectionUtils.isEmpty(creatives)) {
            for (Creative creative : creatives) {
                List<CreativeReport> creativeReports = creativeService.findCreativeReport(details,campaignId,groupId,creative.getCreativeId(),dateRange,syn);
                if (!CollectionUtils.isEmpty(creativeReports)) {
                    creativeReportlists.addAll(creativeReports);
                }
            }
        }
        List<StatusAndReportVM> results = creativeService.buildStatusAndReportResults(creatives,creativeReportlists,dateRange);
        CreativeReport report = ReportUtils.mergeReport(creativeReportlists);
        Map resultMap =new HashMap<>();
        resultMap.put("report", report);
        resultMap.put("results",results);
        return  resultMap;
    }*/

    /**
     * 删除创意
     * @param creatives
     * @return
     */
    @PostMapping ("/delCreative")
   public OperationResultVM delCreative(@RequestBody List<Creative> creatives) {
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        return creativeService.batchDelCreatives(details, creatives);

    }

    /**
     * 添加创意
     * @param creatives
     * @return
     */
        @PostMapping ("/updateCreative")
        public OperationResultVM updateCreative(@RequestBody List<Creative> creatives){
            TaobaoUserDetails details = SecurityUtils.getCurrentUser();
            return creativeService.batchUpdateCreatives(details,creatives);
}





}
