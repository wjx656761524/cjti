package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.honghailt.cjtj.api.crowd.CrowdAPI;
import com.honghailt.cjtj.domain.*;
import com.honghailt.cjtj.repository.CampaignRepository;
import com.honghailt.cjtj.repository.CrowdRepository;
import com.honghailt.cjtj.repository.GroupRepository;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.dto.DirectionalLabel;
import com.honghailt.cjtj.service.dto.options;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import com.taobao.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CrowdService {

    @Autowired
    private CrowdAPI crowdAPI;

    @Autowired
    private CrowdRepository repository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private SynItfData synItfData;



    /**
     * 定向列表数据查询
     */
    public List<StatusAndReportVM> getCrowdRptdailylist(String startTime, Long campaignId, Long adgroupId, String endTime){
        // 同步推广计划，单元
       // synItfData.synCampaign();
        List<GroupStatus> groupStatuss = synItfData.synGroup();
        synItfData.synCroed(groupStatuss);
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<StatusAndReportVM> statusAndReportVMList = Lists.newArrayList();
        List<Crowd> crowdList = Lists.newArrayList();
        if(null ==adgroupId || "".equals(adgroupId)){
            if(null !=campaignId && !"".equals(adgroupId)){
                List<GroupStatus> groupStatusList = groupRepository.findByCampaignId(campaignId);
                for (GroupStatus groupStatus : groupStatusList) {
                    crowdPage(null,groupStatus.getGroupId(),groupStatus.getGroupName(),groupStatus.getCampaignName(),null,null,null,null,details.getSessionKey());
                }
                crowdList = repository.findByCampaignId(campaignId);
                statusAndReportVMList =  MergeCrowdReport(crowdList,startTime,endTime,details);
            }else{
                List<GroupStatus> groupStatusList = groupRepository.findAll();
                for (GroupStatus groupStatus : groupStatusList) {
                    crowdPage(null,groupStatus.getGroupId(),groupStatus.getGroupName(),groupStatus.getCampaignName(),null,null,null,null,details.getSessionKey());
                }
                crowdList = repository.findAll();
                statusAndReportVMList =  MergeCrowdReport(crowdList,startTime,endTime,details);
            }

        }else{
            Optional<GroupStatus> groupStatus = groupRepository.findById(adgroupId);
            crowdPage(null,adgroupId,groupStatus.get().getGroupName(),groupStatus.get().getCampaignName(),null,null,null,null,details.getSessionKey());
            crowdList  = repository.findByCampaignIdAndAdgroupId(campaignId,adgroupId);
            statusAndReportVMList = MergeCrowdReport(crowdList,startTime,endTime,details);
        }
        return statusAndReportVMList;
    }

    /**
     *  分页查询单品单元下人群列表
     */
    public List<FeedflowItemCrowdPageResponse.CrowdDto> crowdPage(List<String> targetTypeList, Long adgroupId,String groupName,String campaignName, Long pageSize, Long crowdId, List<String> statusList, Long offset, String sessionKey){
        FeedflowItemCrowdPageResponse rsp = crowdAPI.crowdPage(targetTypeList,adgroupId,pageSize,crowdId,statusList,offset,sessionKey);
        List<FeedflowItemCrowdPageResponse.CrowdDto> crowdDtos = rsp.getResult().getCrowds();
        for(int i=0;i<crowdDtos.size() ;i++){
            Crowd cwd = new Crowd();
            cwd.setCrowdId(crowdDtos.get(i).getCrowdId());
            cwd.setCrowdName(crowdDtos.get(i).getCrowdName());
            cwd.setCrowdDesc(crowdDtos.get(i).getCrowdDesc());
            cwd.setPrice(crowdDtos.get(i).getPrice());
            cwd.setCampaignId(crowdDtos.get(i).getCampaignId());
            cwd.setCampaignName(campaignName);
            cwd.setAdgroupId(crowdDtos.get(i).getAdgroupId());
            cwd.setAdgroupName(groupName);
            cwd.setStatus(crowdDtos.get(i).getStatus());
            cwd.setTargetId(crowdDtos.get(i).getTargetLabel().getTargetId());
            cwd.setTargetType(crowdDtos.get(i).getTargetLabel().getTargetType());
            repository.save(cwd);
        }
        return crowdDtos;
    }

    /**
     *  单品单元下，新增定向人群
     */
    public String crowdAdd(AddCrowd addCrowd){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        FeedflowItemCrowdAddResponse rsp = crowdAPI.crowdAdd(addCrowd,details.getSessionKey());
        return rsp.getResult().getMessage();
    }

    // 获取定向类型中的标签中的属性值
    public StatusAndReportVM optionPage(Long targetId,String targetType,String itemIds) {
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<DirectionalLabel> directionalLabels = Lists.newArrayList();
        String str[] = itemIds.split(",");
        for(String itemId: str){
            List<Long> longs = Lists.newArrayList();
            longs.add(Long.parseLong(itemId));
            FeedflowItemOptionPageResponse rsp2  =   crowdAPI.optionPage(null,null,targetId,null,targetType,longs,details.getSessionKey());
            for(FeedflowItemOptionPageResponse.LabelDto labelDto :rsp2.getResult().getLabels()){
                DirectionalLabel directionalLabel = new DirectionalLabel();
                directionalLabel.setLabelId(labelDto.getLabelId());
                directionalLabel.setLabelValue(labelDto.getLabelValue());
                directionalLabel.setTargetId(labelDto.getTargetId());
                directionalLabel.setTargetType(labelDto.getTargetType());
                List<options> options = Lists.newArrayList();
                if(null != labelDto.getOptions()){
                    for(FeedflowItemOptionPageResponse.OptionDto optionDto :labelDto.getOptions()){
                        options options1 = new options();
                        options1.setOptionName(optionDto.getOptionName());
                        options1.setOptionValue(optionDto.getOptionValue());
                        options.add(options1);
                    }
                    directionalLabel.setOptions(options);
                }
                directionalLabels.add(directionalLabel);
            }
        }
        if(directionalLabels.size() > 1){
            for(int i=1;i<directionalLabels.size();i++){
                for(options options1: directionalLabels.get(i).getOptions()){
                    int p = 0;
                    for(options options  :directionalLabels.get(0).getOptions()){
                        if(options1.getOptionValue().equalsIgnoreCase(options.getOptionValue())){
                            p = p+1;
                        }
                    }
                    if(p == 0){
                        directionalLabels.get(0).getOptions().add(options1);
                    }
                }
            }
        }
        StatusAndReportVM statusAndReportVM = new StatusAndReportVM(directionalLabels,null,null);
        return  statusAndReportVM;
    }

    //  单元下获取定向类型中的标签中的属性值
    public StatusAndReportVM getUnitOptionPage(Long targetId,String targetType,List<Long> longList) {
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        FeedflowItemOptionPageResponse rsp2  =   crowdAPI.optionPage(null,null,targetId,null,targetType,longList,details.getSessionKey());
        StatusAndReportVM statusAndReportVM = new StatusAndReportVM(rsp2.getResult().getLabels(),null,null);
        return  statusAndReportVM;
    }

    // 获取宝贝id
    public StatusAndReportVM  getItemPage(Long campaignId){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        FeedflowItemItemPageResponse rsp1 =  crowdAPI.itemPage(campaignId,null,null,null,null,details.getSessionKey());
        StatusAndReportVM statusAndReportVM = new StatusAndReportVM(rsp1.getResult().getItemList(), null,null);
        return statusAndReportVM;
    }



    public StatusAndReportVM getCampaignGroup(){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<CampaignGroup> campaignGroupList = Lists.newArrayList();
        List<Campaign> campaignList =  campaignRepository.findAll();
        for (int i=0;i<campaignList.size();i++){
            CampaignGroup campaignGroup = new CampaignGroup();
            Campaign campaign = new Campaign();
            campaign.setCampaignId(campaignList.get(i).getCampaignId());
            campaign.setCampaignName(campaignList.get(i).getCampaignName());
            campaignGroup.setCampaign(campaign);
            List<GroupStatus> groupStatusList = groupRepository.findByCampaignId(campaignList.get(i).getCampaignId());
            campaignGroup.setGroupStatusList(groupStatusList);
            campaignGroupList.add(campaignGroup);
        }
        StatusAndReportVM statusAndReportVM = new StatusAndReportVM(campaignGroupList,null,null);
        return statusAndReportVM;
    }



    /**
     * 覆盖单元下同类型定向人群
     */

    public String crowdModify(Long targetId,Long labelId,String labelValue,String targetType,String optionValue,Long price,String crowdDesc ,Long crowdId,Long adgroupId,String sessionKey){
        FeedflowItemCrowdModifyResponse rsp = crowdAPI.crowdModify(targetId,labelId,labelValue,targetType,optionValue,price,crowdDesc,crowdId,adgroupId,sessionKey);
        return rsp.getResult().getMessage();
    }

    /**
     *  删除单品人群
     */
    public String crowdDelete(Long crowdId,Long adgroupId){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        FeedflowItemCrowdDeleteResponse rsp = crowdAPI.crowdDelete(crowdId,adgroupId,details.getSessionKey());
        repository.deleteById(crowdId);
        return rsp.getResult().getMessage();
    }

    /**
     *  修改人群出价或状态
     */
    public String crowdModifybind(Long price,String status,Long crowdId,Long adgroupId){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        FeedflowItemCrowdModifybindResponse rsp = crowdAPI.crowdModifybind(price,status,crowdId,adgroupId,details.getSessionKey());
        return rsp.getResult().getMessage();
    }


    /**
     *  超级推荐【商品推广】定向分时报表查询
     */
    public List<FeedflowItemCrowdRpthourlistResponse.RptResultDto> crowdRpthourlist(Long campaignId,Long endHourId,Long adgroupId,Long crowdId,String logDate,Long startHourId, String sessionKey){
        FeedflowItemCrowdRpthourlistResponse rsp = crowdAPI.crowdRpthourlist(campaignId,endHourId,adgroupId,crowdId,logDate,startHourId,sessionKey);
        return rsp.getResult().getRptList();
    }


    /**
     * 合并报表数据
     */
    public List<StatusAndReportVM> MergeCrowdReport(List<Crowd> crowdList,String startTime,String endTime,TaobaoUserDetails details){
        List<StatusAndReportVM> statusAndReportVMList = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Crowd crowd:crowdList) {
            List<AbstractReport> abstractReportList = Lists.newArrayList();
            CrowdStatus crowdStatus = new CrowdStatus();
            // 定向分日数据查询
            FeedflowItemCrowdRptdailylistResponse rsp = crowdAPI.crowdRptdailylist(startTime,crowd.getCampaignId(),crowd.getAdgroupId(),crowd.getCrowdId(),endTime,details.getSessionKey());
            if(null == rsp.getResult().getRptList()){
                crowdStatus.setAdgroupId(crowd.getAdgroupId());
                crowdStatus.setAdgroupName(crowd.getAdgroupName());
                crowdStatus.setCampaignId(crowd.getCampaignId());
                crowdStatus.setCampaignName(crowd.getCampaignName());
                crowdStatus.setCrowdId(crowd.getCrowdId());
                crowdStatus.setCrowdName(crowd.getCrowdName());
                crowdStatus.setPrice(crowd.getPrice());
                crowdStatus.setStatus(crowd.getStatus());
                AbstractReport abstractReport = new AbstractReport();
                abstractReport.setAdPv(0L);
                abstractReport.setAlipayInshopAmt(0.0);
                abstractReport.setAlipayInShopNum(0L);
                abstractReport.setCartNum(0L);
                abstractReport.setCharge(0.0);
                abstractReport.setClick(0L);
                abstractReport.setCvr(0.0);
                abstractReport.setEcpc(0.0);
                abstractReport.setEcpm(0.0);
                abstractReport.setInshopItemColNum(0L);
                abstractReport.setLogDate("");
                abstractReport.setRoi(0.0);
                abstractReportList.add(abstractReport);
            }else{
                crowdStatus.setAdgroupId(crowd.getAdgroupId());
                crowdStatus.setAdgroupName(crowd.getAdgroupName());
                crowdStatus.setCampaignId(crowd.getCampaignId());
                crowdStatus.setCampaignName(crowd.getCampaignName());
                crowdStatus.setCrowdId(crowd.getCrowdId());
                crowdStatus.setCrowdName(crowd.getCrowdName());
                crowdStatus.setPrice(crowd.getPrice());
                for (FeedflowItemCrowdRptdailylistResponse.RptResultDto rptResultDto: rsp.getResult().getRptList()) {
                    AbstractReport abstractReport = new AbstractReport();
                    abstractReport.setAdPv(rptResultDto.getAdPv() == null ? 0L: rptResultDto.getAdPv());
                    abstractReport.setAlipayInshopAmt(Double.parseDouble(rptResultDto.getAlipayInshopAmt() == null ?"0.0":rptResultDto.getAlipayInshopAmt()));
                    abstractReport.setAlipayInShopNum(rptResultDto.getAlipayInShopNum() == null ?0L:rptResultDto.getAlipayInShopNum());
                    abstractReport.setCartNum(rptResultDto.getCartNum() == null ? 0L: rptResultDto.getCartNum());
                    abstractReport.setCharge(Double.parseDouble(rptResultDto.getCharge() == null?"0.0": rptResultDto.getCharge()));
                    abstractReport.setClick(rptResultDto.getClick() == null?0:rptResultDto.getClick());
                    abstractReport.setCvr(Double.parseDouble(rptResultDto.getCvr()==null? "0.0":rptResultDto.getCvr()));
                    abstractReport.setEcpc(Double.parseDouble(rptResultDto.getEcpc() == null?"0.0": rptResultDto.getEcpc()));
                    abstractReport.setEcpm(Double.parseDouble(rptResultDto.getEcpm() == null?"0.0": rptResultDto.getEcpm()));
                    abstractReport.setInshopItemColNum(rptResultDto.getInshopItemColNum() == null?0L:rptResultDto.getInshopItemColNum());
                    abstractReport.setLogDate(sdf.format(rptResultDto.getLogDate()));
                    abstractReport.setRoi(Double.parseDouble(rptResultDto.getRoi() == null?"0.0":rptResultDto.getRoi()));
                    abstractReportList.add(abstractReport);
                }
            }
            AbstractReport abstractReport = ReportUtils.mergeReport(abstractReportList);
            StatusAndReportVM statusAndReportVM = new StatusAndReportVM(crowdStatus,abstractReport,null);
            statusAndReportVMList.add(statusAndReportVM);
        }
        return statusAndReportVMList;
    }



    /**
     * 定向列表数据查询
     */
    public List<StatusAndReportVM> getCrowdlist(String startTime, Long campaignId, Long adgroupId, String endTime){
        // 同步推广计划，单元
        // synItfData.synCampaign();
        List<GroupStatus> groupStatuss = synItfData.synGroup();
        synItfData.synCroed(groupStatuss);
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<StatusAndReportVM> statusAndReportVMList = Lists.newArrayList();
        List<Crowd> crowdList = Lists.newArrayList();
        if(null ==adgroupId || "".equals(adgroupId)){
            if(null !=campaignId && !"".equals(adgroupId)){
                crowdList = repository.findByCampaignId(campaignId);
                statusAndReportVMList =  getCrowdlistReport(crowdList,startTime,endTime,details);
            }else{
                crowdList = repository.findAll();
                statusAndReportVMList =  getCrowdlistReport(crowdList,startTime,endTime,details);
            }

        }else{
            crowdList  = repository.findByCampaignIdAndAdgroupId(campaignId,adgroupId);
            statusAndReportVMList = getCrowdlistReport(crowdList,startTime,endTime,details);
        }
        return statusAndReportVMList;
    }


    /**
     *  合并报表数据
     */
    public List<StatusAndReportVM> getCrowdlistReport(List<Crowd> crowdList,String startTime,String endTime,TaobaoUserDetails details){
        List<StatusAndReportVM> statusAndReportVMList = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Crowd crowd:crowdList) {
            List<AbstractReport> abstractReportList = Lists.newArrayList();
            // 定向分日数据查询
            FeedflowItemCrowdRptdailylistResponse rsp = crowdAPI.crowdRptdailylist(startTime,crowd.getCampaignId(),crowd.getAdgroupId(),crowd.getCrowdId(),endTime,details.getSessionKey());
            if(null == rsp.getResult().getRptList()){
                AbstractReport abstractReport = new AbstractReport();
                abstractReport.setAdPv(0L);
                abstractReport.setAlipayInshopAmt(0.0);
                abstractReport.setAlipayInShopNum(0L);
                abstractReport.setCartNum(0L);
                abstractReport.setCharge(0.0);
                abstractReport.setClick(0L);
                abstractReport.setCvr(0.0);
                abstractReport.setEcpc(0.0);
                abstractReport.setEcpm(0.0);
                abstractReport.setInshopItemColNum(0L);
                abstractReport.setLogDate("");
                abstractReport.setRoi(0.0);
                abstractReportList.add(abstractReport);
            }else{
                for (FeedflowItemCrowdRptdailylistResponse.RptResultDto rptResultDto: rsp.getResult().getRptList()) {
                    AbstractReport abstractReport = new AbstractReport();
                    abstractReport.setAdPv(rptResultDto.getAdPv() == null ? 0L: rptResultDto.getAdPv());
                    abstractReport.setAlipayInshopAmt(Double.parseDouble(rptResultDto.getAlipayInshopAmt() == null ?"0.0":rptResultDto.getAlipayInshopAmt()));
                    abstractReport.setAlipayInShopNum(rptResultDto.getAlipayInShopNum() == null ?0L:rptResultDto.getAlipayInShopNum());
                    abstractReport.setCartNum(rptResultDto.getCartNum() == null ? 0L: rptResultDto.getCartNum());
                    abstractReport.setCharge(Double.parseDouble(rptResultDto.getCharge() == null?"0.0": rptResultDto.getCharge()));
                    abstractReport.setClick(rptResultDto.getClick() == null?0:rptResultDto.getClick());
                    abstractReport.setCvr(Double.parseDouble(rptResultDto.getCvr()==null? "0.0":rptResultDto.getCvr()));
                    abstractReport.setEcpc(Double.parseDouble(rptResultDto.getEcpc() == null?"0.0": rptResultDto.getEcpc()));
                    abstractReport.setEcpm(Double.parseDouble(rptResultDto.getEcpm() == null?"0.0": rptResultDto.getEcpm()));
                    abstractReport.setInshopItemColNum(rptResultDto.getInshopItemColNum() == null?0L:rptResultDto.getInshopItemColNum());
                    abstractReport.setLogDate(sdf.format(rptResultDto.getLogDate()));
                    abstractReport.setRoi(Double.parseDouble(rptResultDto.getRoi() == null?"0.0":rptResultDto.getRoi()));
                    abstractReportList.add(abstractReport);
                }
            }
            AbstractReport abstractReport = ReportUtils.mergeReport(abstractReportList);
            StatusAndReportVM statusAndReportVM = new StatusAndReportVM(null,abstractReport,null);
            statusAndReportVMList.add(statusAndReportVM);
        }
        return statusAndReportVMList;
    }

}
