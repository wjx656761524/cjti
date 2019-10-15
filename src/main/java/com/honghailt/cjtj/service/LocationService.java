package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.api.adgroup.AdGroupAPI;
import com.honghailt.cjtj.api.adzone.AdZoneAPI;
import com.honghailt.cjtj.domain.*;
import com.honghailt.cjtj.repository.CampaignRepository;
import com.honghailt.cjtj.repository.GroupRepository;
import com.honghailt.cjtj.repository.LocationRepository;
import com.honghailt.cjtj.service.dto.DelGroup;
import com.honghailt.cjtj.service.dto.DelLocation;
import com.honghailt.cjtj.web.rest.vm.OperationResultVM;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.taobao.api.response.FeedflowItemAdgroupAdzoneBindResponse;
import com.taobao.api.response.FeedflowItemAdgroupAdzoneUnbindResponse;
import com.taobao.api.response.FeedflowItemAdgroupDeleteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.acl.Group;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 17:28 2019/5/17
 * @Modified By
 */
@Service
public class LocationService {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private AdGroupAPI adGroupAPI;
    @Autowired
    private AdZoneAPI adZoneAPI;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupService  groupService;
    @Autowired
    private CampaignService campaignService;
    /**
     * 根据推广单元id获取现有资源位
     * @param details
     * @param campaignId
     * @param groupId
     * @param syn
     * @return
     */
    public List<Location> queryLocationList(TaobaoUserDetails details, Long campaignId, Long groupId, Boolean syn) {

        List<Location> locations=Lists.newArrayList();
        //查找所有资源位
        if(0==campaignId&&0==groupId){
            if (syn) {
                List<GroupStatus>groups=groupService.findAll(details,syn);
                for (GroupStatus group:groups){
                synLocationByAPI(details,group.getCampaignId(),group.getGroupId());
                }
            }
            locations=locationRepository.findAll();
            return locations;
        }
        //查找指定计划的资源位
        if(0!=campaignId&&0==groupId){
            if (syn) {
                List<GroupStatus> groups= groupService.findGroupStatus(details,campaignId,syn);
                List<Long> groupIds=groups.stream().map(GroupStatus::getGroupId).collect(Collectors.toList());
             for(Long sgroupId:groupIds){
                 synLocationByAPI(details,campaignId,sgroupId);

             }
            }
            locations=locationRepository.findByCampaignId(campaignId);
            return locations;

        }
        //查找指定单元的资源位
        if(0!=campaignId&&0!=groupId){
            if(syn)synLocationByAPI(details,campaignId,groupId);

            locations= locationRepository.findLocationByGroupId(groupId);
            return  locations;

        }
       return locations;

    }
    /**
     * 同步资源位的实时数据
     * @param details
     * @param campaignId
     * @param groupId
     * @return
     */
    public List<Location> synLocationByAPI(TaobaoUserDetails details, Long campaignId, Long groupId ) {
        List<Location> locationAPIList = Lists.newArrayList();
        List<Location> localLocationList = locationRepository.findLocationByGroupId(groupId);
        Map<Long, Location> existLocationMap = localLocationList.stream().collect(Collectors.toMap((location) -> location.getAdzoneId(), Function.identity()));
        try {
            if(0==groupId) locationAPIList=adZoneAPI.listZoneByAPI(details.getSessionKey(),campaignId,null,null);
               else locationAPIList= adGroupAPI.pageGroupAdZoneByAPI(details.getSessionKey(),null,groupId,null,null,null);
            if (!CollectionUtils.isEmpty(locationAPIList)) {
                List<Location> locationList = Lists.newArrayList();
                for (Location apiLocation : locationAPIList) {
                    Location location = existLocationMap.get(apiLocation.getAdzoneId());
                    if (location == null) {
                        location=apiLocation;
                        location.setDeleted(0);
                        if(0!=groupId){
                        GroupStatus groupStatus=groupRepository.findByGroupId(groupId);
                        location.setGroupId(groupId);
                        location.setGroupName(groupStatus.getGroupName());
                        location.setStatues(groupStatus.getStatus());
                        location.setCampaignName(groupStatus.getCampaignName());
                        }
                        location.setSick(details.getNick());
                        location.setCampaignId(campaignId);

                        locationList.add(location);
                    }else{
                        location.setDeleted(0);
                        location.setAdzoneId(apiLocation.getAdzoneId());
                        location.setAdzoneName(apiLocation.getAdzoneName());
                        if(0!=groupId){
                        GroupStatus groupStatus=groupRepository.findByGroupId(groupId);
                        location.setGroupId(groupId);
                        location.setGroupName(groupStatus.getGroupName());
                        location.setStatues(groupStatus.getStatus());
                        location.setCampaignName(groupStatus.getCampaignName());
                        }
                        location.setSick(details.getNick());
                        location.setCampaignId(campaignId);
                        location.setDiscount(apiLocation.getDiscount());


                        existLocationMap.remove(location.getAdzoneId());
                        locationList.add(location);



                    }

                }
                // 删除本地保存但API接口数据中已不存在的资源位
                if (!CollectionUtils.isEmpty(existLocationMap)){
                    Set<Long> keys = existLocationMap.keySet();
                    for (Long key : keys) {

                        Location location = existLocationMap.get(key);
                        location.setDeleted(1);
                        locationRepository.save(location);
                        }


                }
                //更新本地保存的资源位数据
                locationRepository.saveAll(locationList);
            }
        }catch (Exception e) {
            logger.error("推广单元ID[{}]同步资源位实时数据报错", groupId, e);
        }
        return locationAPIList;
    }
        /**
         * 获得计划下所有广告位在指定时间段内的汇总统计数据
         * @param details    店铺账号信息
         * @param groupId 推广计划ID
         * @param syn      是否同步
         * @return
         */
        public List<LocationReport> findLocationLReportBygroupId(TaobaoUserDetails details, DateRange dateRange, Long campaignId, Long adZoneId, Long groupId, Boolean syn) {
             List<LocationReport>groupReportList=Lists.newArrayList();
            //非绑定状态下
            if(null==groupId)return null;
            if (syn){ groupReportList = adZoneAPI.rptdailylistZoneByDayByAPI(details.getSessionKey(),dateRange,campaignId, adZoneId, groupId);}
            return groupReportList;



        }
    public OperationResultVM updateLocation(TaobaoUserDetails details,List<Location> locations){

        if (locations==null) {
            return new OperationResultVM(null, false, "集合为空");

        }
        boolean success = true;
        try {

                  FeedflowItemAdgroupAdzoneBindResponse res = adGroupAPI.bindGroupByAPI(details.getSessionKey(),  locations);
                  if(!res.isSuccess()){
                      return new OperationResultVM(null, false, "集合为空");
                  }
            locationRepository.saveAll(locations);
        }
      catch (Exception e) {
          success  = false;
            }
        return new OperationResultVM(null,success,null);



    }
    /**
     * 删除广告位
     * @param details
     * @param  delLocations
     * @return
     */
    public OperationResultVM batchDelLocations(TaobaoUserDetails details, List<DelLocation> delLocations){
        if (CollectionUtils.isEmpty( delLocations)) {
            return new OperationResultVM(null, false, "集合为空");
        }
        boolean success = true;

        try {
            Map<Long, StringBuffer> map=getMapByGroupId( delLocations);
            Iterator<Map.Entry<Long, StringBuffer> > entries =map.entrySet().iterator();
            while (entries.hasNext()) {

                Map.Entry<Long, StringBuffer> entry = entries.next();
                String  str=new String();
                str=entry.getValue().toString();
                str=str.substring(0, str.length()-1);
                FeedflowItemAdgroupAdzoneUnbindResponse rsp =adGroupAPI.unbindGroupadZoneByAPI( details.getSessionKey(),str,entry.getKey());
                if (!rsp.isSuccess()) {
                    success=false;
                    return  null;
                }

            }

            for (DelLocation delLocation : delLocations) {
                Long adzoneId=delLocation.getAdzoneId();
              Location location = locationRepository.getOne(adzoneId);
                location.setDeleted(1);
                locationRepository.save(location);
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
    private Map<Long, StringBuffer> getMapByGroupId( List<DelLocation> delLocations){
        Map<Long,  StringBuffer> map = new HashMap<>();
        for (DelLocation delLocation : delLocations) {
            if (map.containsKey(delLocation.getGroupId())) {//map中存在此id，将数据存放当前key的map中
                map.get(delLocation.getGroupId()).append(delLocation.getAdzoneId()+",");
            } else {//map中不存在，新建key，用来存放数据
                StringBuffer str=new StringBuffer();
                str.append(delLocation.getAdzoneId()+",");
                map.put(delLocation.getGroupId(), str);
            }
        }

        return map;


    }

    /**
     * 获取指定单元的所有资源位
     * @param campaignId
     * @param groupId

     * @return
     */
   public  List<Location> getALLLocations(TaobaoUserDetails details,Long campaignId,Long groupId){


        if(null==campaignId){
            return null;
        }
        List<Location> locations=Lists.newArrayList();
        try {
         locations= adZoneAPI.listZoneByAPI(details.getSessionKey(),campaignId,null,null);
   if(null!=groupId) {
       List<Location> bindlocations = adGroupAPI.pageGroupAdZoneByAPI(details.getSessionKey(), null, groupId, null, null, null);
       //将溢价进行合并
       for(Location location:locations){

           for(Location locationNew:bindlocations){
               if(null!=locationNew.getDiscount()){
                   if(locationNew.getAdzoneId()== location.getAdzoneId()){
                       location.setDiscount(locationNew.getDiscount());

                   }

               }

           }

       }
   }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return  locations;


    }




}
