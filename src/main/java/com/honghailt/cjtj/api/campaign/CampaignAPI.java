package com.honghailt.cjtj.api.campaign;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.Campaign;
import com.honghailt.cjtj.domain.CampaignReport;
import com.honghailt.cjtj.taobao.CompositeTaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Api(value="CampaignAPI",tags={"CampaignAPI接口"})
@Service
public class CampaignAPI {

    @Autowired
    private CompositeTaobaoClient taobaoClient;


    @ApiOperation( value = "信息流增加推广计划",notes = "信息流增加推广计划" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignName", value = "计划名称", required = true, dataType = "String"),
        @ApiImplicitParam(name = "discount", value = "折扣率", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String"),
        @ApiImplicitParam(name = "launchForever", value = "是否永远生效", required = false, dataType = "Boolean"),
        @ApiImplicitParam(name = "beginTime", value = "开始时间", required = false, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String"),
        @ApiImplicitParam(name = "code", value = "地址code", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "name", value = "地址名称", required = false, dataType = "String"),
        @ApiImplicitParam(name = "dayBudget", value = "每日预算，单位为分", required = true, dataType = "Long"),
    })
    public FeedflowItemCampaignAddResponse addCampaign(String campaignName, Long dayBudget,Long discount,String time,boolean launchForever,String beginTime,String endTime,Long code,String name, String sessionKey) throws ParseException {

        FeedflowItemCampaignAddRequest req = new FeedflowItemCampaignAddRequest();
        FeedflowItemCampaignAddRequest.CampaignDto campaignDto = new FeedflowItemCampaignAddRequest.CampaignDto();
        campaignDto.setCampaignName(campaignName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<FeedflowItemCampaignAddRequest.LaunchPeriodDTO> launchPeriodDTOS = new ArrayList<FeedflowItemCampaignAddRequest.LaunchPeriodDTO>();
        FeedflowItemCampaignAddRequest.LaunchPeriodDTO launchPeriodDTO = new FeedflowItemCampaignAddRequest.LaunchPeriodDTO();
        List<FeedflowItemCampaignAddRequest.TimeSpanDto> timeSpanDtos = new ArrayList<FeedflowItemCampaignAddRequest.TimeSpanDto>();
        FeedflowItemCampaignAddRequest.TimeSpanDto timeSpanDto = new FeedflowItemCampaignAddRequest.TimeSpanDto();
//        if(null != discount)timeSpanDto.setDiscount(discount);
//        if(null != time)timeSpanDto.setTime(time);
//        timeSpanDtos.add(timeSpanDto);
//        launchPeriodDTO.setTimeSpanList(timeSpanDtos);
//        launchPeriodDTOS.add(launchPeriodDTO);
//        campaignDto.setLaunchPeriodList(launchPeriodDTOS);
        FeedflowItemCampaignAddRequest.LaunchTimeDto launchTimeDto = new FeedflowItemCampaignAddRequest.LaunchTimeDto();
        launchTimeDto.setLaunchForever(launchForever);
        if(null != beginTime)launchTimeDto.setBeginTime(sdf.parse(beginTime));
        if(null != endTime)launchTimeDto.setEndTime(sdf.parse(endTime));
        campaignDto.setLaunchTime(launchTimeDto);
        List<FeedflowItemCampaignAddRequest.LaunchAreaDTO> launchAreaDTOS = new ArrayList<FeedflowItemCampaignAddRequest.LaunchAreaDTO>();
        FeedflowItemCampaignAddRequest.LaunchAreaDTO launchAreaDTO = new FeedflowItemCampaignAddRequest.LaunchAreaDTO();
        //if(null != code)launchAreaDTO.setCode(code);
        //if(null != name)launchAreaDTO.setName(name);
        //launchAreaDTOS.add(launchAreaDTO);
        //campaignDto.setLaunchAreaList(launchAreaDTOS);
        campaignDto.setDayBudget(dayBudget);
        req.setCampaign(campaignDto);
        FeedflowItemCampaignAddResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }


    @ApiOperation( value = "获取当日投放日预算总额",notes = "获取当日投放日预算总额" )
    public FeedflowItemCampaignDaybudgetResponse findCampaignDaybudget(String sessionKey){
        FeedflowItemCampaignDaybudgetRequest req = new FeedflowItemCampaignDaybudgetRequest();
        FeedflowItemCampaignDaybudgetResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }



    @ApiOperation( value = "信息流修改计划",notes = "信息流修改计划" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignName", value = "计划名称", required = false, dataType = "String"),
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "String"),
        @ApiImplicitParam(name = "launchForever", value = "是否永远生效", required = false, dataType = "Boolean"),
        @ApiImplicitParam(name = "beginTime", value = "开始时间", required = false, dataType = "String"),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String"),
        @ApiImplicitParam(name = "code", value = "地址code", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "name", value = "地址名称", required = false, dataType = "String"),
        @ApiImplicitParam(name = "dayBudget", value = "每日预算，单位为分", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "time", value = "时间", required = false, dataType = "String"),
        @ApiImplicitParam(name = "discount", value = "折扣率", required = false, dataType = "Long"),
    })
    public FeedflowItemCampaignModifyResponse modifyCampaign(String campaignName,Long campaignId,String status,Boolean launchForever,String beginTime,String endTime,
                                                              Long code,String name,Long dayBudget,String time,Long discount, String sessionKey){
        FeedflowItemCampaignModifyRequest req = new FeedflowItemCampaignModifyRequest();
        FeedflowItemCampaignModifyRequest.CampaignDto campaignDto = new FeedflowItemCampaignModifyRequest.CampaignDto();
        campaignDto.setCampaignId(campaignId);
        // 修改计划名称
        if (!StringUtils.isEmpty(campaignName)) {
            campaignDto.setCampaignName(campaignName);
        }
        // 修改计划状态
        if (!StringUtils.isEmpty(status)) {
            campaignDto.setStatus(status);
        }
        // 投放时间
        if (!StringUtils.isEmpty(beginTime)&&!StringUtils.isEmpty(endTime)) {
            FeedflowItemCampaignModifyRequest.LaunchTimeDto launchTimeDto = new FeedflowItemCampaignModifyRequest.LaunchTimeDto();
            launchTimeDto.setLaunchForever(launchForever);
            if(beginTime != null && "".equals(beginTime)){
                launchTimeDto.setBeginTime(StringUtils.parseDateTime(beginTime));// 开始时间

            }
            if(endTime != null && "".equals(endTime)){
                launchTimeDto.setEndTime(StringUtils.parseDateTime(endTime)); // 结束时间

            }

            campaignDto.setLaunchTime(launchTimeDto);
        }
        // 投放地域
        if (code !=null && !StringUtils.isEmpty(name)) {
            List<FeedflowItemCampaignModifyRequest.LaunchAreaDto> launchAreaDtos = new ArrayList<FeedflowItemCampaignModifyRequest.LaunchAreaDto>();
            FeedflowItemCampaignModifyRequest.LaunchAreaDto launchAreaDto = new FeedflowItemCampaignModifyRequest.LaunchAreaDto();
            launchAreaDto.setCode(code); // 地址code
            launchAreaDto.setName(name); // 地址名称
            launchAreaDtos.add(launchAreaDto);
            campaignDto.setLaunchAreaList(launchAreaDtos);
        }
        // 修改日限额
        if (dayBudget!=null) {
            campaignDto.setDayBudget(dayBudget);
        }
        // 折价范围
        if (!StringUtils.isEmpty(time) && discount != null) {
            List<FeedflowItemCampaignModifyRequest.TimeSpanDto> timeSpanDtos = new ArrayList<FeedflowItemCampaignModifyRequest.TimeSpanDto>();
            FeedflowItemCampaignModifyRequest.TimeSpanDto timeSpanDto = new FeedflowItemCampaignModifyRequest.TimeSpanDto();
            timeSpanDto.setTime(time); //时间
            timeSpanDto.setDiscount(discount); // 折扣率
            timeSpanDtos.add(timeSpanDto);
            FeedflowItemCampaignModifyRequest.LaunchPeriodDto launchPeriodDto = new FeedflowItemCampaignModifyRequest.LaunchPeriodDto();
            launchPeriodDto.setTimeSpanList(timeSpanDtos);
            List<FeedflowItemCampaignModifyRequest.LaunchPeriodDto> launchPeriodDtos = new ArrayList<FeedflowItemCampaignModifyRequest.LaunchPeriodDto>();
            launchPeriodDtos.add(launchPeriodDto);
            campaignDto.setLaunchPeriodList(launchPeriodDtos);
        }
        req.setCampaign(campaignDto);
        FeedflowItemCampaignModifyResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    @ApiOperation( value = "删除计划",notes = "删除计划" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
    })
    public FeedflowItemCampaignDeleteResponse deleteOneCampaign(Long campaignId,String sessionKey){
        FeedflowItemCampaignDeleteRequest req = new FeedflowItemCampaignDeleteRequest();
        req.setCampaignId(campaignId);
        FeedflowItemCampaignDeleteResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    @ApiOperation( value = "通过计划id查询计划",notes = "通过计划id查询计划" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
    })
    public FeedflowItemCampaignGetResponse selectOneCampaign(Long campaignId,String sessionKey){
        FeedflowItemCampaignGetRequest req = new FeedflowItemCampaignGetRequest();
        req.setCampaginId(campaignId);
        FeedflowItemCampaignGetResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    @ApiOperation( value = "批量查询计划列表",notes = "批量查询计划列表" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "campaignName", value = "计划名称", required = false, dataType = "String"),
        @ApiImplicitParam(name = "offset", value = "起始位置", required = false, dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "每页大小", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "statusList", value = "状态列表", required = false, dataType = "List<String>"),
    })
    public List<Campaign> selectCampaignPage (Long campaignId, String campaignName, Long offset, Long pageSize, List<String> statusList, String sessionKey){
        List<Campaign> campaignList = Lists.newArrayList();
        FeedflowItemCampaignPageRequest req = new FeedflowItemCampaignPageRequest();
        FeedflowItemCampaignPageRequest.CampaignQueryDto campaignQueryDto = new FeedflowItemCampaignPageRequest.CampaignQueryDto();
        if (campaignId != null) campaignQueryDto.setCampaignId(campaignId);
        if (!StringUtils.isEmpty(campaignName)) campaignQueryDto.setCampaignName(campaignName);
        if (offset != null) campaignQueryDto.setOffset(offset);
        if (pageSize != null) campaignQueryDto.setPageSize(pageSize);
        if (!CollectionUtils.isEmpty(statusList)) campaignQueryDto.setStatusList(statusList);
        req.setCampaignQuery(campaignQueryDto);
        FeedflowItemCampaignPageResponse rsp = taobaoClient.execute(req, sessionKey);
        FeedflowItemCampaignPageResponse.ResultDto result = rsp.getResult();
        List<FeedflowItemCampaignPageResponse.CampaignDTo> campaignDTos = result.getResults();
        if (!CollectionUtils.isEmpty(campaignDTos)) {
            for (FeedflowItemCampaignPageResponse.CampaignDTo campaignDTo : campaignDTos) {
                Campaign campaign = new Campaign();
                campaign.setCampaignId(campaignDTo.getCampaignId());
                campaign.setCampaignName(campaignDTo.getCampaignName());
                campaign.setDayBudget(campaignDTo.getDayBudget());
                campaign.setStatus(campaignDTo.getStatus());
                FeedflowItemCampaignPageResponse.LaunchTimeDto launchTimeDto = campaignDTo.getLaunchTime();
                campaign.setBeginTime(launchTimeDto.getBeginTime().toInstant());
                campaign.setEndTime(launchTimeDto.getEndTime().toInstant());
                campaign.setLaunchForever(launchTimeDto.getLaunchForever());
                campaignList.add(campaign);
            }
        }
        return campaignList;
    }

    @ApiOperation( value = "推广计划分日数据查询",notes = "推广计划分日数据查询" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "查询开始日期", required = false, dataType = "String"),
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "end_time", value = "查询结束日期", required = false, dataType = "String"),
    })
    public List<CampaignReport> campaignRptdailylist(String startTime, Long campaignId, String endTime, String sessionKey){
        List<CampaignReport> campaignReportList = Lists.newArrayList();
        FeedflowItemCampaignRptdailylistRequest req = new FeedflowItemCampaignRptdailylistRequest();
        FeedflowItemCampaignRptdailylistRequest.RptQueryDto rptQueryDto = new FeedflowItemCampaignRptdailylistRequest.RptQueryDto();
        rptQueryDto.setStartTime(startTime);
        rptQueryDto.setCampaignId(campaignId);
        rptQueryDto.setEndTime(endTime);
        req.setRptQueryDTO(rptQueryDto);
        FeedflowItemCampaignRptdailylistResponse rsp = taobaoClient.execute(req, sessionKey);
        if (rsp!=null) {
            FeedflowItemCampaignRptdailylistResponse.ResultDto resultDto = rsp.getResult();
            if (resultDto != null) {
                List<FeedflowItemCampaignRptdailylistResponse.RptResultDto> reportList = resultDto.getRptList();
                if (!CollectionUtils.isEmpty(reportList)) {
                    for (FeedflowItemCampaignRptdailylistResponse.RptResultDto report : reportList) {
                        CampaignReport campaignReport = new CampaignReport();
                        campaignReport.setCampaignId(campaignId);
                        campaignReport.setAdPv(report.getAdPv());
                        campaignReport.setAlipayInshopAmt(this.stringToLong(report.getAlipayInshopAmt()));
                        campaignReport.setAlipayInShopNum(report.getAlipayInShopNum());
                        campaignReport.setCartNum(report.getCartNum());
                        campaignReport.setCharge(this.stringToLong(report.getCharge()));
                        campaignReport.setClick(report.getClick());
                        campaignReport.setCvr(this.stringToDouble(report.getCvr()));
                        campaignReport.setEcpc(this.stringToLong(report.getEcpc()));
                        campaignReport.setEcpm(this.stringToLong(report.getEcpm()));
                        campaignReport.setInshopItemColNum(report.getInshopItemColNum());
                        campaignReport.setRoi(this.stringToDouble(report.getRoi()));
                        campaignReport.setLogDate(com.honghailt.cjtj.utils.StringUtils.formatDateTime(report.getLogDate(), "yyyy-MM-dd"));
                        campaignReportList.add(campaignReport);
                    }
                }
            }
        }
        return campaignReportList;
    }

    @ApiOperation( value = "超级推荐【商品推广】计划分时报表查询 ",notes = "超级推荐【商品推广】计划分时报表查询 " )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "endHourId", value = "结束小时", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "logDate", value = "查询日期", required = true, dataType = "String"),
        @ApiImplicitParam(name = "startHourId", value = "开始小时", required = true, dataType = "Long"),
    })
    public FeedflowItemCampaignRpthourlistResponse campaignRpthourlist(Long campaignId,Long endHourId, String logDate,Long startHourId,String sessionKey){
        FeedflowItemCampaignRpthourlistRequest req = new FeedflowItemCampaignRpthourlistRequest();
        FeedflowItemCampaignRpthourlistRequest.RptQueryDto rptQueryDto = new  FeedflowItemCampaignRpthourlistRequest.RptQueryDto();
        rptQueryDto.setCampaignId(campaignId);
        rptQueryDto.setEndHourId(endHourId);
        rptQueryDto.setLogDate(logDate);
        rptQueryDto.setStartHourId(startHourId);
        req.setRptQuery(rptQueryDto);
        FeedflowItemCampaignRpthourlistResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    private Double  stringToLong(String param) {
        Double l = 0.0;
        if (org.springframework.util.StringUtils.isEmpty(param)) {
            l = 0.0;
        }else {
            l = Double.parseDouble(param);
        }
        return l;
    }

    private Double  stringToDouble(String param) {
        Double d = 0d;
        if (org.springframework.util.StringUtils.isEmpty(param)) {
            d = 0d;
        }else {
            d = Double.parseDouble(param);
        }
        return d;
    }


    @ApiOperation( value = "批量查询计划列表",notes = "批量查询计划列表" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "campaignName", value = "计划名称", required = false, dataType = "String"),
        @ApiImplicitParam(name = "offset", value = "起始位置", required = false, dataType = "Integer"),
        @ApiImplicitParam(name = "pageSize", value = "每页大小", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "statusList", value = "状态列表", required = false, dataType = "List<String>"),
    })
    public FeedflowItemCampaignPageResponse campaignPage (Long campaignId, String campaignName, Long offset, Long pageSize, List<String> statusList, String sessionKey){
        FeedflowItemCampaignPageRequest req = new FeedflowItemCampaignPageRequest();
        FeedflowItemCampaignPageRequest.CampaignQueryDto campaignQueryDto = new FeedflowItemCampaignPageRequest.CampaignQueryDto();
        if(null != campaignId)campaignQueryDto.setCampaignId(campaignId);
        if(null != campaignName)campaignQueryDto.setCampaignName(campaignName);
        if(null != offset)campaignQueryDto.setOffset(offset);
        if(null != pageSize) campaignQueryDto.setPageSize(pageSize);
        if(null != statusList) campaignQueryDto.setStatusList(statusList);
        req.setCampaignQuery(campaignQueryDto);
        FeedflowItemCampaignPageResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

}
