package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.honghailt.cjtj.api.creative.CreativeAPI;
import com.honghailt.cjtj.domain.*;
import com.honghailt.cjtj.repository.CreativeRepository;
import com.honghailt.cjtj.repository.GroupRepository;
import com.honghailt.cjtj.service.dto.DelLocation;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.OperationResultVM;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import com.taobao.api.request.FeedflowItemCreativeDeleteRequest;
import com.taobao.api.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CreativeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CreativeAPI creativeAPI;

    @Autowired
    private CreativeRepository creativeRepository;

    @Autowired
    private GroupService  groupService;
    @Autowired
    private GroupRepository groupRepository;
    /**
     * 通过单元Id查询创意
     * @param details
     * @param groupId
     * @param syn
     * @param campaignId
     * @return
     */

    public List<Creative> findCreatives(TaobaoUserDetails details,Long campaignId, Long groupId, Boolean syn) {
        List<Creative> creativeList = Lists.newArrayList();
        //查找所有资源位
        if(0==campaignId&&0==groupId){
            if (syn) {
                List<GroupStatus>groups=groupService.findAll(details,syn);
                for (GroupStatus group:groups){
                    synCreativeByAPI(details,group.getGroupId());
                }
            }
            creativeList=creativeRepository.findAll();
            return creativeList;
        }
        //查找指定计划的资源位
        if(0!=campaignId&&0==groupId){
            if (syn) {
                List<GroupStatus> groups= groupService.findGroupStatus(details,campaignId,syn);
                List<Long> groupIds=groups.stream().map(GroupStatus::getGroupId).collect(Collectors.toList());
                for(Long sgroupId:groupIds){
                    synCreativeByAPI(details,sgroupId);

                }
            }
            creativeList=creativeRepository.findCreativeByCampaignId(campaignId);
            return creativeList;

        }
        //查找指定单元的资源位
        if(0!=campaignId&&0!=groupId){
            if(syn)synCreativeByAPI(details,groupId);

            creativeList= creativeRepository.findCreativeByGroupId(groupId);
            return  creativeList;

        }

          if (syn)  synCreativeByAPI(details,groupId);
        creativeList = creativeRepository.findCreativeByGroupId(groupId);
        return  creativeList;
    }
    /**
     * 同步推广单元下创意的实时数据
     * @param details
     * @param groupId
     * @return
     */
    public List<Creative> synCreativeByAPI(TaobaoUserDetails details, Long groupId) {
        List<Creative> apiStatusList = Lists.newArrayList();
        List<Creative> localCreativeList = creativeRepository.findCreativeByGroupId(groupId);
        //将这个list转化为Map,key为GroupID,value为GroupStatus
        Map<Long, Creative> existGroupMap = localCreativeList.stream().collect(Collectors.toMap((status) -> status.getCreativeId(), Function.identity()));
        try {
            apiStatusList = creativeAPI.creativePage(groupId,null, null, null, null,null, details.getSessionKey());
            if (!CollectionUtils.isEmpty(apiStatusList)) {
                List<Creative> updateResult = Lists.newArrayList();
                //指定修改对象
                for (Creative apiStatus : apiStatusList) {
                    //判断取得单元在数据库是否存在c
                    Creative status = existGroupMap.get(apiStatus.getCreativeId());
                    //不存在的话
                    if (status == null) {
                        status = apiStatus;
//                        CampaignStatus campaignStatus = campaignService.findCampaignStatusById(campaignId);
//                        status.setCampaignType(campaignStatus.getCampaignType());
                        status.setNick(details.getNick());
                        status.setDeleted(0);
                        status.setGroupId(groupId);
                        GroupStatus groupStatus=groupRepository.findByGroupId(groupId);
                        status.setGroupName(groupStatus.getGroupName());
                        status.setCampaignName(groupStatus.getCampaignName());
                    }


                     else {
                        status.setDeleted(0);
                        status.setGroupId(apiStatus.getGroupId());
                        GroupStatus groupStatus=groupRepository.findByGroupId(groupId);
                        status.setGroupName(groupStatus.getGroupName());
                        status.setCampaignName(groupStatus.getCampaignName());
                        status.setAuditReason(apiStatus.getAuditReason());
                        status.setAuditStatus(apiStatus.getAuditStatus());
                        status.setCampaignId(apiStatus.getCampaignId());
                        status.setCreativeId(apiStatus.getCreativeId());
                        status.setCreativeName(apiStatus.getCreativeName());
                        status.setTitle(apiStatus.getTitle());
                        status.setImgUrl(apiStatus.getImgUrl());

                        existGroupMap.remove(status.getCreativeId());



                    }
                    // 同步商品信息(待续)

                    updateResult.add(status);

                }
                //更新

                creativeRepository.saveAll(updateResult);

            }
            // 删除的API不存在的
            if (!CollectionUtils.isEmpty(existGroupMap)) {
                Set<Long> keys = existGroupMap.keySet();
                for (Long key : keys) {
                    Creative deleteStatus = existGroupMap.get(key);
                    deleteStatus.setDeleted(1);
                    creativeRepository.save(deleteStatus);
                }
            }
        } catch (Exception e) {
            logger.error("推广计划ID[{}]同步推广单元实时数据报错。", groupId, e);
        }
        return apiStatusList;


    }
    /**
     * 获得单元下指定创意在指定时间段内的汇总统计数据
     * @param details
     * @param campaignId
     * @param dateRange
     * @param groupId
     * @param creativeId
     * @param syn
     * @return
     */
    public List<CreativeReport> findCreativeReport(TaobaoUserDetails details, Long campaignId, Long groupId,Long creativeId,DateRange dateRange,  Boolean syn) {
        List<CreativeReport> creativeReportList=Lists.newArrayList();
        if(syn)   creativeReportList =creativeAPI.dcreativeRptdailylist(details.getSessionKey(),campaignId,groupId, creativeId, dateRange);
        return creativeReportList;


    }

    /**
     * 批量删除创意
     * @param details
     * @param delCreatives
     * @return
     */

    public OperationResultVM batchDelCreatives(TaobaoUserDetails details, List<Creative> delCreatives){
        if (CollectionUtils.isEmpty( delCreatives)) {
            return new OperationResultVM(null, false, "集合为空");
        }
        boolean success = false;

        try {
               String str=ListToString(delCreatives);
            FeedflowItemCreativeDeleteResponse rsp =creativeAPI.creativeDelete(str,details.getSessionKey());

            success=rsp.isSuccess();
            if(success){
            for (Creative creative : delCreatives) {
                Long creativeId=creative.getCreativeId();
                Creative creativeNew = creativeRepository.getOne(creativeId);
                creativeNew.setDeleted(1);
                creativeRepository.save(creativeNew);
                // 记录操作日志

            }}
        }catch (Exception e){
            success=false;


        }

        return new OperationResultVM(null,success,null);
    }
    /**
     * 批量绑定创意
     * @param details
     * @param updateCreatives
     * @return
     */

    public OperationResultVM batchUpdateCreatives(TaobaoUserDetails details, List<Creative> updateCreatives){
        if (CollectionUtils.isEmpty( updateCreatives)) {
            return new OperationResultVM(null, false, "集合为空");
        }
        Long goupId=null;
//        goupId= updateCreatives.get(0).getGroupId();
//        List<Creative> creatives= creativeAPI.creativePage(goupId,null,null,null,null,null,details.getSessionKey());

//        if(null==creatives){
//            return new OperationResultVM(null, false, "集合为空");
//        }
        List<Creative> creativeList = Lists.newArrayList();
        int i = 0;
        FeedflowItemAdgroupCreativeAddBindResponse rsp = null;
        if(updateCreatives.size() == 1){
            rsp =creativeAPI.addBindCreative(updateCreatives,details.getSessionKey());
        }else{
            for(int j=0;j<updateCreatives.size();j++){
                creativeList.add(updateCreatives.get(i));
                i = i+1;
                if(i == 2){
                    rsp =creativeAPI.addBindCreative(creativeList,details.getSessionKey());
                    i = 1;
                    creativeList.clear();
                }
            }
        }
        return new OperationResultVM(null,rsp.getResult().getSuccess(),null);
    }

    private String  ListToString(List<Creative> creatives)
    {
        if(creatives==null) return null;
        StringBuilder str=new  StringBuilder();
        for(Creative creative :creatives){

            str.append(creative.getCreativeId()+",");
        }
         String strNew=str.toString();
        strNew=strNew.substring(0,strNew.length()-1);
        return strNew;

    }

    /**
     * 组装StatusAndReportVM对象
     * @param creatives
     * @param cReports
     * @param dateRange
     * @return
     */
    public List<StatusAndReportVM> buildStatusAndReportResults(List<Creative> creatives, List<CreativeReport> cReports, DateRange dateRange) {

        Map<Long, List<CreativeReport>> reportMap = Maps.newHashMap();

        // 每个计划对应的所有计划的报表
        if (!CollectionUtils.isEmpty(cReports)) {
            reportMap = cReports.stream().collect(groupingBy((report) -> report.getCompaignId(), Collectors.toList()));
        }
        List<StatusAndReportVM> vms = Lists.newArrayList();
        for (Creative creative : creatives) {
            List<CreativeReport> reports = reportMap.get(creative.getGroupId());
            CreativeReport mergedReport = new CreativeReport();
            List<CreativeReport> everyDayReports = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(reports)) {
                // 根据日期范围过滤掉日期范围之外的报表
                List<CreativeReport> needMergeReports = ReportUtils.filterReportsByDateRange(reports, dateRange);
                mergedReport = ReportUtils.mergeReport(needMergeReports);

                everyDayReports = ReportUtils.groupByMergeReport(reports, new Function<CreativeReport, String>() {
                    @Override
                    public String apply(CreativeReport creativeReport) {
                        return creativeReport.getGroupId().toString() + creativeReport.getLogDate();
                    }
                });
            }
            ReportUtils.sortByDate(everyDayReports);
            StatusAndReportVM vm = new StatusAndReportVM(creative, mergedReport, everyDayReports);
            vms.add(vm);
        }
        return vms;
    }

}
