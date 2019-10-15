package com.honghailt.cjtj.web.rest;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.*;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.CrowdService;
import com.honghailt.cjtj.service.GroupService;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import com.taobao.api.response.FeedflowItemCrowdRpthourlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
@RequestMapping("/api/crowd")
public class CrowdResource {

    @Autowired
    private CrowdService srv;

    @Autowired
    private GroupService groupService;

    /**
     * ,
     * 定向分日数据查询
     */
    @GetMapping("/getCrowdRptdailylist")
    public Map getCrowdRptdailylist(DateRange dateRange, Long campaignId, Long adgroupId) {
//        Date getStartTime=Date.from(dateRange.getStartTime());
//        Date getEndTime=Date.from(dateRange.getEndTime());
//        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
//        String startTime=formatter.format(getStartTime);
//        String endTime=formatter.format(getEndTime);
        Map map = new HashMap();
        List<StatusAndReportVM> list = srv.getCrowdRptdailylist(dateRange.getStartTime().toString(), campaignId, adgroupId, dateRange.getEndTime().toString());
        map.put("list", list);

        return map;
    }
/*
    *//**
     * 定向合并数据查询
     *
     * @param campaignId
     * @param adgroupId
     * @param dateRange
     * @retur
     */
    @GetMapping("/getCrowdlist")
    public Map getCrowdlist(DateRange dateRange, Long campaignId, Long adgroupId){

        Map map = new HashMap();

        List<AbstractReport> crowdReports = Lists.newArrayList();
        List<StatusAndReportVM> reportVMS = srv.getCrowdRptdailylist(dateRange.getStartTime().toString(), campaignId, adgroupId, dateRange.getEndTime().toString());
        for (StatusAndReportVM statusAndReportVM : reportVMS){
            AbstractReport ar = new AbstractReport();
            ar.setAdPv(statusAndReportVM.getReport().getAdPv());
            ar.setAlipayInshopAmt(statusAndReportVM.getReport().getAlipayInshopAmt());
            ar.setAlipayInShopNum(statusAndReportVM.getReport().getAlipayInShopNum());
            ar.setCartNum(statusAndReportVM.getReport().getCartNum());
            ar.setCharge(statusAndReportVM.getReport().getCharge());
            ar.setClick(statusAndReportVM.getReport().getClick());
            ar.setCvr(statusAndReportVM.getReport().getCvr());
            ar.setEcpc(statusAndReportVM.getReport().getEcpc());
            ar.setInshopItemColNum(statusAndReportVM.getReport().getInshopItemColNum());
            ar.setRoi(statusAndReportVM.getReport().getRoi());
            ar.setLogDate(statusAndReportVM.getReport().getLogDate());
            ar.setCtr(statusAndReportVM.getReport().getCtr());
            crowdReports.add(ar);
        }
        AbstractReport abstractReport = ReportUtils.mergeReport(crowdReports);
        StatusAndReportVM statusAndReportVM1 = new StatusAndReportVM(null,abstractReport,null);


        map.put("abstractReport", abstractReport);

        return map;

    }


    /**
     * 单品单元下，新增定向人群
     */
    @PostMapping("/crowdAdd")
    public String crowdAdd(@RequestBody AddCrowd addCrowd) {
        String result = srv.crowdAdd(addCrowd);
        return result;
    }

    /**
     * 获取所有计划和所有单元
     */
    @GetMapping("/getCampaignGroup")
    public StatusAndReportVM getCampaignGroup() {
        StatusAndReportVM statusAndReportVM = srv.getCampaignGroup();
        return statusAndReportVM;
    }

    /**
     * 获取定向类型下标签列表
     */
    @GetMapping("/getOptionPage")
    public StatusAndReportVM getOptionPage(Long targetId, String targetType, String itemIds) {
        StatusAndReportVM statusAndReportVM = srv.optionPage(targetId, targetType, itemIds);
        return statusAndReportVM;
    }

    /**
     * 单元下获取定向类型下标签列表
     */
    @GetMapping("/getUnitOptionPage")
    public StatusAndReportVM getUnitOptionPage(Long targetId, String targetType, List<Long> longList) {
        StatusAndReportVM statusAndReportVM = srv.getUnitOptionPage(targetId, targetType, longList);
        return statusAndReportVM;
    }


    /**
     * 获取单元下所有宝贝
     */
    @GetMapping("/getItemPage")
    public StatusAndReportVM getItemPage(Long campaignId) {
        StatusAndReportVM statusAndReportVM = srv.getItemPage(campaignId);
        return statusAndReportVM;
    }


    /**
     * 覆盖单元下同类型定向人群
     */
    @PostMapping("/crowdModify")
    public String crowdModify(HttpServletRequest request, Long targetId, Long labelId, String labelValue, String targetType, String optionValue, Long price, String crowdDesc, Long crowdId, Long adgroupId) {
        User user = (User) request.getSession().getAttribute("user");
        String result = srv.crowdModify(targetId, labelId, labelValue, targetType, optionValue, price, crowdDesc, crowdId, adgroupId, user.getSessionkey());
        return result;
    }

    /**
     * 删除单品人群
     */
    @GetMapping("/crowdDelete")
    public String crowdDelete(Long crowdId, Long adgroupId) {
        String result = srv.crowdDelete(crowdId, adgroupId);
        return result;
    }


    /**
     * 修改人群出价或状态
     */
    @GetMapping("/crowdModifybind")
    public String crowdModifybind(Long price, String status, Long crowdId, Long adgroupId) {
        String result = srv.crowdModifybind(price, status, crowdId, adgroupId);
        return result;
    }

    /**
     * 超级推荐【商品推广】定向分时报表查询
     */
    @PostMapping("/crowdRpthourlist")
    public List<FeedflowItemCrowdRpthourlistResponse.RptResultDto> crowdRpthourlist(HttpServletRequest request, Long campaignId, Long endHourId, Long adgroupId, Long crowdId, String logDate, Long startHourId) {
        User user = (User) request.getSession().getAttribute("user");
        List<FeedflowItemCrowdRpthourlistResponse.RptResultDto> list = srv.crowdRpthourlist(campaignId, endHourId, adgroupId, crowdId, logDate, startHourId, user.getSessionkey());
        return list;
    }

}
