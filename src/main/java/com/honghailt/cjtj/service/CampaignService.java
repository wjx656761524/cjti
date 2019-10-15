package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.honghailt.cjtj.api.campaign.CampaignAPI;
import com.honghailt.cjtj.domain.Campaign;
import com.honghailt.cjtj.domain.CampaignReport;
import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.repository.CampaignRepository;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.dto.UpdateCampaignDto;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import com.taobao.api.response.FeedflowItemCampaignAddResponse;
import com.taobao.api.response.FeedflowItemCampaignModifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignAPI campaignAPI;

    /**
     * 获取店铺下的计划列表
     *
     * @param userDetails
     * @param syn
     * @return
     */
    public List<Campaign> getCampaigns(TaobaoUserDetails userDetails, boolean syn) {
        // 本地数据库中的推广计划列表
        List<Campaign> localCampaigns = campaignRepository.findCampaignsByNick(userDetails.getNick());
        if (syn) {
            // 接口中获取的计划列表
            List<Campaign> apiCampaigns = this.synCampaigns(userDetails);
            // 本地数据库中的列表转Map
            Map<Long, Campaign> localCampaignMap = localCampaigns.stream().collect(Collectors.toMap((campaign) -> campaign.getCampaignId(), Function.identity()));
            if (!CollectionUtils.isEmpty(apiCampaigns)) {
                List<Campaign> updateList = Lists.newArrayList();
                for (Campaign apiCampaign : apiCampaigns) {
                    Campaign localCampaign = localCampaignMap.get(apiCampaign.getCampaignId());
                    if (localCampaign == null) {
                        localCampaign = apiCampaign;
                        localCampaign.setNick(userDetails.getNick());
                        localCampaign.setDeleted(false);
                    } else {
                        localCampaign.setStatus(apiCampaign.getStatus());
                        localCampaign.setEndTime(apiCampaign.getEndTime());
                        localCampaign.setCampaignName(apiCampaign.getCampaignName());
                        localCampaign.setDayBudget(apiCampaign.getDayBudget());
                        localCampaign.setBeginTime(apiCampaign.getBeginTime());
                        localCampaign.setLaunchForever(apiCampaign.getLaunchForever());
                        localCampaign.setDeleted(false);
                        localCampaignMap.remove(apiCampaign.getCampaignId());
                    }
                    updateList.add(localCampaign);
                }
                // 删除的
                if (!CollectionUtils.isEmpty(localCampaignMap)) {
                    Set<Long> keys = localCampaignMap.keySet();
                    for (Long key : keys) {
                        Campaign deleteStatus = localCampaignMap.get(key);
                        deleteStatus.setDeleted(true);
                        campaignRepository.save(deleteStatus);
                    }
                }
                //更新
                campaignRepository.saveAll(updateList);

            }
            localCampaigns = campaignRepository.findCampaignsByNick(userDetails.getNick());
        }
        return localCampaigns;
    }

    /**
     * 获取计划下的报表数据
     *
     * @param userDetails
     * @param campaignId
     * @param dateRange
     * @param syn
     * @return
     */
    public List<CampaignReport> getCampaignReport(TaobaoUserDetails userDetails, Long campaignId, DateRange dateRange, boolean syn) {
        List<CampaignReport> campaignReports = Lists.newArrayList();
        if (syn) campaignReports = this.synCampaignReports(userDetails, campaignId, dateRange);
        return campaignReports;
    }

    /**
     * 同步店铺下的计划列表
     *
     * @param userDetails
     * @return
     */
    public List<Campaign> synCampaigns(TaobaoUserDetails userDetails) {
        List<Campaign> campaigns = Lists.newArrayList();
        campaigns = campaignAPI.selectCampaignPage(null, null, null, null, null, userDetails.getSessionKey());
        return campaigns;
    }


    /**
     * 同步计划下的报表数据
     *
     * @param userDetails
     * @param campaignId
     * @param dateRange
     * @return
     */
    public List<CampaignReport> synCampaignReports(TaobaoUserDetails userDetails, Long campaignId, DateRange dateRange) {
        List<CampaignReport> campaignReports = Lists.newArrayList();
        campaignReports = campaignAPI.campaignRptdailylist(dateRange.getStartTimeStr(), campaignId, dateRange.getEndTimeStr(), userDetails.getSessionKey());
        return campaignReports;
    }

    /**
     * 组装StatusAndReportVM对象
     *
     * @param campaigns
     * @param campaignReports
     * @param dateRange
     * @return
     */
    public List<StatusAndReportVM> buildStatusAndReportResults(List<Campaign> campaigns, List<CampaignReport> campaignReports, DateRange dateRange) {

        Map<Long, List<CampaignReport>> reportMap = Maps.newHashMap();

        // 每个计划对应的所有计划的报表
        if (!CollectionUtils.isEmpty(campaignReports)) {
            reportMap = campaignReports.stream().collect(groupingBy((report) -> report.getCampaignId(), Collectors.toList()));
        }
        List<StatusAndReportVM> vms = Lists.newArrayList();
        for (Campaign campaign : campaigns) {
            List<CampaignReport> reports = reportMap.get(campaign.getCampaignId());
            CampaignReport mergedReport = new CampaignReport();
            List<CampaignReport> everyDayReports = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(reports)) {
                // 根据日期范围过滤掉日期范围之外的报表
                List<CampaignReport> needMergeReports = ReportUtils.filterReportsByDateRange(reports, dateRange);
                mergedReport = ReportUtils.mergeReport(needMergeReports);

                everyDayReports = ReportUtils.groupByMergeReport(reports, new Function<CampaignReport, String>() {
                    @Override
                    public String apply(CampaignReport campaignReport) {
                        return campaignReport.getCampaignId().toString() + campaignReport.getLogDate();
                    }
                });
            }
            ReportUtils.sortByDate(everyDayReports);
            StatusAndReportVM vm = new StatusAndReportVM(campaign, mergedReport, everyDayReports);
            vms.add(vm);
        }
        return vms;
    }

    /**
     * 修改推广计划状态
     *
     * @param details
     * @param
     * @return
     */
    public Boolean updateStatusByCampaign(TaobaoUserDetails details, UpdateCampaignDto updateCampaignDto){

        Boolean tag =false;

        try {
            FeedflowItemCampaignModifyResponse cmr = campaignAPI.modifyCampaign(null,updateCampaignDto.getCampaignId(),
                                                                            updateCampaignDto.getStatus(),false,
                                                                null,null,null,null,null,
                                                                                    null,null,details.getSessionKey());
            tag = cmr.isSuccess();
            if(tag){
                Campaign one = campaignRepository.getOne(updateCampaignDto.getCampaignId());
                if (null!= one){
                    one.setStatus(updateCampaignDto.getStatus());
                    campaignRepository.save(one);
                }
            }
        } catch (Exception e) {
            tag = false;
        }

        return tag;

    }

    /**
     *  修改计划信息
     */
    public Boolean updateModifybind(TaobaoUserDetails details, UpdateCampaignDto updateCampaignDto){

        Boolean tag = false;
        try {
            FeedflowItemCampaignModifyResponse cmr = campaignAPI.modifyCampaign(updateCampaignDto.getCampaignName(),updateCampaignDto.getCampaignId(),
                null,false,
                null,null,null,null,null,
                null,null,details.getSessionKey());
            tag = cmr.isSuccess();
            if(tag){
                Campaign one = campaignRepository.getOne(updateCampaignDto.getCampaignId());
                if (null!= one){
                    one.setCampaignName(updateCampaignDto.getCampaignName());
                    campaignRepository.save(one);
                }
            }
        } catch (Exception e) {
            tag = false;
        }
        return tag;

    }

    /**
     * 修改计划日预算
     * @param details
     * @param updateCampaignDto
     * @return
     */
    public Boolean updateModifybd(TaobaoUserDetails details, UpdateCampaignDto updateCampaignDto){

        Boolean tag = false;
        FeedflowItemCampaignModifyResponse cmr = campaignAPI.modifyCampaign(null,updateCampaignDto.getCampaignId(),
            null,false,
            null,null,null,null,updateCampaignDto.getDayBudget(),
            null,null,details.getSessionKey());
        tag = cmr.isSuccess();
        if(tag){
            Campaign one = campaignRepository.getOne(updateCampaignDto.getCampaignId());
            if (null!= one){
                one.setDayBudget(updateCampaignDto.getDayBudget());
                campaignRepository.save(one);
            }
        }
        return tag;
    }

    public Long addCampaingn(Map<String,Object> map) throws ParseException {
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        FeedflowItemCampaignAddResponse result =  campaignAPI.addCampaign(map.get("campaignName").toString(),Long.parseLong(map.get("dayBudget").toString()),null,null,(boolean)map.get("launchForever"),map.get("startTime").toString(),map.get("endTime").toString(),null,null,details.getSessionKey());
        return result.getResult().getResult();
    }

    /**
     * 批量修改状态
     * @param details
     * @param updateCampaignDto
     * @return
     */
    public Boolean updateCam(TaobaoUserDetails details, UpdateCampaignDto updateCampaignDto){
        Boolean tag = false;

        try {
            String[] idArray = updateCampaignDto.getIds().split(",");
            for (String idStr : idArray) {
                Integer id = Integer.parseInt(idStr);
                if (id!=null) {
                    for (int i = 0; i < id; i++) {
                        FeedflowItemCampaignModifyResponse cmr = campaignAPI.modifyCampaign(
                            null, updateCampaignDto.getCampaignId(),
                            updateCampaignDto.getStatus(), false,
                            null, null, null, null, null,
                            null, null, details.getSessionKey());
                        tag = cmr.isSuccess();
                        if (tag) {
                            Campaign one = campaignRepository.getOne(updateCampaignDto.getCampaignId());
                            if (null != one) {
                                one.setDayBudget(updateCampaignDto.getDayBudget());
                                campaignRepository.save(one);
                            }
                            return false;
                        }
                    }
                }
                return false;
            }
        } catch (Exception e) {
            tag = false;
        }
        return tag;
    }


    /**
     * 批量修改日预算
     * @param details
     * @param updateCampaignDto
     * @return
     */
    public Boolean updatePai(TaobaoUserDetails details, UpdateCampaignDto updateCampaignDto){
        Boolean tag = false;

        try {
            String[] idArray = updateCampaignDto.getIds().split(",");
            for (String idStr : idArray) {
                Integer id = Integer.parseInt(idStr);
                if (id!=null){
                    for (int i = 0; i < id; i++) {
                        FeedflowItemCampaignModifyResponse cmr = campaignAPI.modifyCampaign(
                            null, updateCampaignDto.getCampaignId(),
                            null, false,
                            null, null, null, null, updateCampaignDto.getDayBudget(),
                            null, null, details.getSessionKey());
                        tag = cmr.isSuccess();
                        if (tag) {
                            Campaign one = campaignRepository.getOne(updateCampaignDto.getCampaignId());
                            if (null != one) {
                                one.setDayBudget(updateCampaignDto.getDayBudget());
                                campaignRepository.save(one);
                            }
                            return false;
                        }
                    }

                }
                return true;
            }

        }catch (Exception e) {
            tag = false;
        }
        return tag;
    }


}

