package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.api.adgroup.AdGroupAPI;
import com.honghailt.cjtj.domain.*;
import com.honghailt.cjtj.repository.CampaignRepository;
import com.honghailt.cjtj.repository.GroupRepository;
import com.honghailt.cjtj.repository.ItemRepository;
import com.honghailt.cjtj.repository.LogGroupRepository;
import com.honghailt.cjtj.service.dto.AddGroup;
import com.honghailt.cjtj.service.dto.DelGroup;
import com.honghailt.cjtj.service.dto.UpdateGroupDto;
import com.honghailt.cjtj.web.rest.vm.OperationResultVM;
import com.taobao.api.response.FeedflowItemAdgroupAddResponse;
import com.taobao.api.response.FeedflowItemAdgroupDeleteResponse;
import com.taobao.api.response.FeedflowItemAdgroupModifyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;



/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 11:59 2019/5/11
 * @Modified By
 */

@Service
public class GroupService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
     private  GroupRepository groupRepository;
    @Autowired
    AdGroupAPI adGroupAPI;
    @Autowired
   CampaignService campaignService;
    @Autowired
    CampaignRepository campaignRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    LogGroupRepository logGroupRepository;
    /**
     * 获取所有的单元
     * @param details
     * @param
     * @return
     */
    public List<GroupStatus> findAll(TaobaoUserDetails details, Boolean syn) {
        List<GroupStatus> list=Lists.newArrayList();
        if(syn){
            List<Campaign> campaigns=campaignService.getCampaigns(details,syn);
            List<Long> campaignIds=campaigns.stream().map(Campaign::getCampaignId).collect(Collectors.toList());
            for(Long campaignId:campaignIds){
                synGroupRealTimeData(details,campaignId);
            }
        }
        list= groupRepository.findAll();
        return list;
    }

    /**
     * 同步推广计划下推广单元的实时数据
     *
     * @param details
     * @param campaignId
     * @return
     */
    public List<GroupStatus> synGroupRealTimeData(TaobaoUserDetails details, Long campaignId) {
        List<GroupStatus> apiStatusList = Lists.newArrayList();
        List<GroupStatus> localGroupStatusList = groupRepository.findByCampaignId(campaignId);
        //将这个list转化为Map,key为GroupID,value为GroupStatus
        Map<Long, GroupStatus> existGroupMap = localGroupStatusList.stream().collect(Collectors.toMap((status) -> status.getGroupId(), Function.identity()));
        try {
            List<Long> campaignIdList = new ArrayList();
            campaignIdList.add(campaignId);
            apiStatusList = adGroupAPI.pageGroupByAPI(details.getSessionKey(), null, campaignIdList, null, null, null, null);
            if (!CollectionUtils.isEmpty(apiStatusList)) {
                List<GroupStatus> updateResult = Lists.newArrayList();
                //指定修改对象
                for (GroupStatus apiStatus : apiStatusList) {
                    //判断取得单元在数据库是否存在
                    GroupStatus status = existGroupMap.get(apiStatus.getGroupId());
                    //不存在的话
                    if (status == null) {
                        status = apiStatus;
                        Campaign campaign= campaignRepository.findCampaignByCampaignId(campaignId);
                        status.setCampaignName(campaign.getCampaignName());
                        Item item=itemRepository.findOne(status.getItemId());
                        status.setItemId(item.getId());
                        status.setItemName(item.getTitle());
                         status.setPicUrl(item.getPicUrl());
                        status.setNick(details.getNick());
                        status.setDeleted(0);


                    } else {
                        status.setDeleted(0);
                        Campaign campaign= campaignRepository.findCampaignByCampaignId(campaignId);
                        status.setCampaignName(campaign.getCampaignName());
                        Item item=itemRepository.findOne(status.getItemId());
                        status.setItemName(item.getTitle());
                        status.setPicUrl(item.getPicUrl());
                        status.setCampaignId(apiStatus.getCampaignId());
                        status.setGroupId(apiStatus.getGroupId());
                        status.setItemId(apiStatus.getItemId());
                        status.setStatus(apiStatus.getStatus());
                        status.setGroupName(apiStatus.getGroupName());
                       if(null!=apiStatus.getScopePercent()){

                        status.setScopePercent(apiStatus.getScopePercent());
                        status.setStrategy(apiStatus.getStrategy());
                       }
                        existGroupMap.remove(status.getGroupId());



                    }
                    // 同步商品信息(待续)

                    updateResult.add(status);

                }
                //更新

              groupRepository.saveAll(updateResult);

            }
            // 删除的API不存在的
            if (!CollectionUtils.isEmpty(existGroupMap)) {
                Set<Long> keys = existGroupMap.keySet();
                for (Long key : keys) {
                    GroupStatus deleteStatus = existGroupMap.get(key);
                    deleteStatus.setDeleted(1);
                    groupRepository.save(deleteStatus);
                }
            }
        } catch (Exception e) {
            logger.error("推广计划ID[{}]同步推广单元实时数据报错。", campaignId, e);
        }
        return apiStatusList;


    }


    /**
     * 获取推广计划下的推广单元的状态数据
     *
     * @param details
     * @param campaignId
     * @param syn
     * @return
     */
    public List<GroupStatus> findGroupStatus(TaobaoUserDetails details, Long campaignId, Boolean syn) {
        //查询所有单元
        if (campaignId == null) return null;
            if (syn) this.synGroupRealTimeData(details, campaignId);
            List<GroupStatus> groupStatuses= groupRepository.findByCampaignId(campaignId);
           return groupStatuses;

        }




        /**
         * 获得计划下所有单元在指定时间段内的汇总统计数据
         * @param details    店铺账号信息
         * @param compaignId 推广计划ID
         * @param isSyn      是否同步
         * @return
         */
        public List<GroupReport> findGroupReportBycompaignId(TaobaoUserDetails details, Long compaignId, DateRange dateRange, Long groupId, Boolean isSyn) {
            List<GroupReport> groupReportList = Lists.newArrayList();
            if (isSyn) {
                groupReportList = adGroupAPI.rpthourlistGroupByDayByAPI(details.getSessionKey(), compaignId, dateRange, groupId);

                return groupReportList;
            }
            return groupReportList;


        }
    /**
     * 删除推广单元
     * @param details
     * @param delGroups
     * @return
     */
    public OperationResultVM batchDelGroups(TaobaoUserDetails details, List<DelGroup> delGroups){
        if (CollectionUtils.isEmpty(delGroups)) {
            return new OperationResultVM(null, false, "集合为空");
        }
        boolean success = true;

        try {
            Map<Long, StringBuffer> map=getMapByCampaignId( delGroups);
            Iterator<Map.Entry<Long, StringBuffer> >entries =map.entrySet().iterator();
            while (entries.hasNext()) {

                Map.Entry<Long, StringBuffer> entry = entries.next();
                String  str=new String();
                str=entry.getValue().toString();
              str=str.substring(0, str.length()-1);
                FeedflowItemAdgroupDeleteResponse rsp =adGroupAPI.deleteGroupByAPI(details.getSessionKey(),entry.getKey(),str);
               if (!rsp.isSuccess()) {
                   success=false;
                   return  null;
                }

            }

            for (DelGroup delGroup : delGroups) {
              Long groupId=delGroup.getGroupId();
               GroupStatus groupStatus = groupRepository.getOne(groupId);
                groupStatus.setDeleted(1);
                groupRepository.save(groupStatus);
                // 记录操作日志
//                GroupLog groupLog = new GroupLog();
//                groupLog.setSick(delGroup.getSick());
//                groupLog.setCampaignId(delGroup.getCampaignId());
//                groupLog.setGroupId(delGroup.getGroupId());
//
//                groupLog.setOperatonTime(Instant.now());
//                groupLogRepository.save(Lists.newArrayList(groupLog));
            }
        }catch (Exception e){
            success=false;


        }
        return new OperationResultVM(null,success,null);
    }

    private Map<Long, StringBuffer> getMapByCampaignId( List<DelGroup> delGroups){
        Map<Long,  StringBuffer> map = new HashMap<>();

        for (DelGroup delGroup : delGroups) {
            if (map.containsKey(delGroup.getCampaignId())) {//map中存在此id，将数据存放当前key的map中
                map.get(delGroup.getCampaignId()).append(delGroup.getGroupId()+",");
            } else {//map中不存在，新建key，用来存放数据
                StringBuffer str=new StringBuffer();
                str.append(delGroup.getGroupId()+",");
                map.put(delGroup.getCampaignId(), str);
            }
        }

        return map;


    }

    /**
     * 修改推广单元状态
     * @param details
     * @param updateGroupDtoList
     * @return
     */
    public  Boolean updateStatus(TaobaoUserDetails details, List<UpdateGroupDto> updateGroupDtoList) {

        Boolean tag = true;

        if (CollectionUtils.isEmpty(updateGroupDtoList)) {
            return false;
        }


        for (UpdateGroupDto updateGroupDto : updateGroupDtoList) {
            FeedflowItemAdgroupModifyResponse res = adGroupAPI.modifyGroupByAPI(details.getSessionKey(), updateGroupDto.getGroupId(), null, false, null, null, updateGroupDto.getNewStatus());
            if (res.isSuccess()) {
                GroupStatus groupStatus = groupRepository.getOne(updateGroupDto.getGroupId());
                if (null != groupStatus) {
                    groupStatus.setStatus(updateGroupDto.getNewStatus());
                    groupRepository.save(groupStatus);
                }
                // 记录操作日志
                GroupLog groupLog = new GroupLog();
                groupLog.setNick(details.getNick());
                groupLog.setCampaignId(updateGroupDto.getCampaignId());
                groupLog.setGroupId(updateGroupDto.getGroupId());
                groupLog.setOldStatus(updateGroupDto.getOldStatus());
                groupLog.setNewStatus(updateGroupDto.getNewStatus());
                groupLog.setOperationType(updateGroupDto.getOperationType());
                groupLog.setOperationSource(updateGroupDto.getOperationSource());
                groupLog.setOperationReason(updateGroupDto.getOperationReason());
                groupLog.setOperatonTime(Instant.now());
                logGroupRepository.save(Lists.newArrayList(groupLog));
            }
        }

        return tag;

    }

    /**
     * 修改单元名称
     * @param details
     * @param updateGroupDto
     * @return
     */
    public Boolean updateGroupName(TaobaoUserDetails details, UpdateGroupDto updateGroupDto) {
        Boolean success = false;
        FeedflowItemAdgroupModifyResponse res = adGroupAPI.modifyGroupByAPI(details.getSessionKey(), updateGroupDto.getGroupId(), updateGroupDto.getNewGroupName(), null, null, null, null);
        success = res.isSuccess();
        if (success) {
            // 记录日志
            GroupLog groupLog = new GroupLog();
            groupLog.setNick(details.getNick());
            groupLog.setCampaignId(updateGroupDto.getCampaignId());
            groupLog.setGroupId(updateGroupDto.getGroupId());
            groupLog.setOldGroupName(updateGroupDto.getOldGroupName());
            groupLog.setNewGroupName(updateGroupDto.getNewGroupName());
            groupLog.setOperationReason(updateGroupDto.getOperationReason());
            groupLog.setOperationSource(updateGroupDto.getOperationSource());
            groupLog.setOperationType(updateGroupDto.getOperationType());
            groupLog.setOperatonTime(Instant.now());
            logGroupRepository.save(Lists.newArrayList(groupLog));
        }
        return success;
    }

    public List<Long>  addGroup(TaobaoUserDetails details,List<AddGroup> addGroups){
        FeedflowItemAdgroupAddResponse rsp = null;
        List<Long> longs = Lists.newArrayList();
        for(int i=0;i<addGroups.size();i++){
            rsp =  adGroupAPI.addGroupByAPI(details.getSessionKey(),addGroups.get(i));
            longs.add(rsp.getResult().getResult());
        }
        return longs;
    }

}


