package com.honghailt.cjtj.web.rest;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.Campaign;
import com.honghailt.cjtj.domain.CampaignReport;
import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.CampaignService;
import com.honghailt.cjtj.service.dto.UpdateCampaignDto;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/campaign")
public class CampaignResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CampaignService campaignService;

    /**
     * 获取计店铺下所有计划数据（状态、合并报表、历史报表）
     * @param dateRange
     * @param syn
     * @return
     */
    @GetMapping("/getAll")
    public List<StatusAndReportVM> getAll(DateRange dateRange,
                                          @RequestParam(defaultValue = "false") Boolean syn) {

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
        return results;
    }

    @PostMapping("/addCampaign")
    public Long addCampaign(@RequestBody Map<String,Object> map) throws ParseException {

        return campaignService.addCampaingn(map);
    }


    /**
     * 修改计划状态
     * @param
     * @return
     */
    @PostMapping("/status")
    public Boolean updateCampaignStatus(@RequestBody UpdateCampaignDto updateCampaignDto) {
        try {
            TaobaoUserDetails details = SecurityUtils.getCurrentUser();
            campaignService.updateStatusByCampaign(details, updateCampaignDto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 修改计划信息
     * @param updateCampaignDto
     * @return
     */
    @PostMapping("/updateModifybind")
    public Boolean updateModifybind(@RequestBody UpdateCampaignDto updateCampaignDto) {
        try {
            TaobaoUserDetails details = SecurityUtils.getCurrentUser();
            campaignService.updateModifybind(details, updateCampaignDto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 修改计划日预算
     * @param updateCampaignDto
     * @return
     */
    @PostMapping("/updateModify")
    public Boolean updateModify(@RequestBody UpdateCampaignDto updateCampaignDto) {
        try {
            TaobaoUserDetails details = SecurityUtils.getCurrentUser();
            campaignService.updateModifybd(details, updateCampaignDto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * 修改计划日预算
     * @param updateCampaignDto
     * @return
     */
    @PostMapping("/updatePai")
    public Boolean updatePai(@RequestBody UpdateCampaignDto updateCampaignDto) {
        try {
            TaobaoUserDetails details = SecurityUtils.getCurrentUser();
            campaignService.updatePai(details, updateCampaignDto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 批量修改状态
     * @param updateCampaignDto
     * @return
     */
    @PostMapping("updateCam")
    public Boolean updateCam(@RequestBody UpdateCampaignDto updateCampaignDto) {
        try {
            TaobaoUserDetails details = SecurityUtils.getCurrentUser();
            campaignService.updateCam(details, updateCampaignDto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
