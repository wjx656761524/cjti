package com.honghailt.cjtj.api.adgroup;


import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.GroupReport;
import com.honghailt.cjtj.domain.GroupStatus;

import com.honghailt.cjtj.domain.Location;
import com.honghailt.cjtj.service.dto.AddGroup;
import com.honghailt.cjtj.taobao.CompositeTaobaoClient;
import com.honghailt.cjtj.utils.StringUtils;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.taobao.api.response.FeedflowItemAdgroupRptdailylistResponse.RptResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Service
public class AdGroupAPI {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CompositeTaobaoClient compositeTaobaoClient;

    /**
     *根据单元id删除单元
     * @param accessToken
     * @param campaignId 计划id
     * @param adgroupIdList 单元id列表
     * @return FeedflowItemAdgroupDeleteResponse
     */
    public FeedflowItemAdgroupDeleteResponse deleteGroupByAPI(String accessToken, Long campaignId, String  adgroupIdList){
        FeedflowItemAdgroupDeleteRequest req = new FeedflowItemAdgroupDeleteRequest();
        req.setCampaignId(campaignId);
        req.setAdgroupIdList(adgroupIdList);
        FeedflowItemAdgroupDeleteResponse rsp = compositeTaobaoClient.execute(req, accessToken);
        return rsp;

    }

    /**
     *信息流单元修改
     * @param accessToken
     * @param adgroupId  	单元id
     * @param adgroupName  单元名称(非必填)
     * @param open          是否打开(非必填)
     * @param scopePercent  溢价范围(非必填)
     * @param strategy    人群策略(非必填)
     * @param status      单元名称(非必填)
     * @return FeedflowItemAdgroupModifyResponse
     */
    public FeedflowItemAdgroupModifyResponse modifyGroupByAPI(String accessToken, Long adgroupId, String adgroupName,
                                                              Boolean open, Long scopePercent, Long strategy, String status){
        FeedflowItemAdgroupModifyRequest req = new FeedflowItemAdgroupModifyRequest();
        FeedflowItemAdgroupModifyRequest.AdgroupDto obj1 = new  FeedflowItemAdgroupModifyRequest.AdgroupDto();
        obj1.setAdgroupId(adgroupId);
        if(null!=adgroupName)  obj1.setAdgroupName(adgroupName);
        if(null!=scopePercent){
            FeedflowItemAdgroupModifyRequest.IntelligentBidDto obj2 = new  FeedflowItemAdgroupModifyRequest.IntelligentBidDto();
            obj2.setOpen(open);
            obj2.setScopePercent(scopePercent);
            obj2.setStrategy(strategy);
            obj1.setIntelligentBid(obj2);
        }
        if (!StringUtils.isEmpty(status)) {
            obj1.setStatus(status);
        }
        req.setAdgroup(obj1);
        FeedflowItemAdgroupModifyResponse rsp = compositeTaobaoClient.execute(req, accessToken);
        return rsp;

    }

    /**
     *信息流增加单元
     * @param accessToken
     * @param addGroup
     * @return  FeedflowItemAdgroupAddResponse
     */
    public FeedflowItemAdgroupAddResponse addGroupByAPI(String accessToken, AddGroup addGroup){
        FeedflowItemAdgroupAddRequest req = new FeedflowItemAdgroupAddRequest();
        FeedflowItemAdgroupAddRequest.AdgroupDto adgroupDto = new FeedflowItemAdgroupAddRequest.AdgroupDto();
        if(null != addGroup.getCampaignId())adgroupDto.setCampaignId(addGroup.getCampaignId());
        if(null != addGroup.getGroupName())adgroupDto.setAdgroupName(addGroup.getGroupName());
//        List<FeedflowItemAdgroupAddRequest.AdzoneBindDto> adzoneBindDtos = new ArrayList<FeedflowItemAdgroupAddRequest.AdzoneBindDto>();
//        FeedflowItemAdgroupAddRequest.AdzoneBindDto adzoneBindDto = new FeedflowItemAdgroupAddRequest.AdzoneBindDto();
//        if(null != addGroup.getAdzoneId())adzoneBindDto.setAdzoneId(addGroup.getAdzoneId());
//        if(null != addGroup.getScopePercent())adzoneBindDto.setDiscount(addGroup.getScopePercent());
//        adzoneBindDtos.add(adzoneBindDto);
//        adgroupDto.setAdzoneList(adzoneBindDtos);
        List<FeedflowItemAdgroupAddRequest.CrowdDto> crowdDtos = new ArrayList<FeedflowItemAdgroupAddRequest.CrowdDto>();
        for(int i = 0;i< addGroup.getDirectionalUnit().size();i++){
            FeedflowItemAdgroupAddRequest.CrowdDto crowdDto = new FeedflowItemAdgroupAddRequest.CrowdDto();
            if(null != addGroup.getDirectionalUnit().get(i).getCrowdDesc())crowdDto.setCrowdDesc(addGroup.getDirectionalUnit().get(i).getCrowdDesc());
            if(null != addGroup.getDirectionalUnit().get(i).getCrowdName())crowdDto.setCrowdName(addGroup.getDirectionalUnit().get(i).getCrowdName());
            if(null != addGroup.getDirectionalUnit().get(i).getPrice())crowdDto.setPrice(addGroup.getDirectionalUnit().get(i).getPrice());
            FeedflowItemAdgroupAddRequest.LabelDto labelDto = new FeedflowItemAdgroupAddRequest.LabelDto();
            if(null != addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getLabelId())labelDto.setLabelId(addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getLabelId());
            if(null != addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getLabelValue())labelDto.setLabelValue(addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getLabelValue());
            if(null != addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getTargetId())labelDto.setTargetId(addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getTargetId());
            if(null != addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getTargetType())labelDto.setTargetType(addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getTargetType());
            List<FeedflowItemAdgroupAddRequest.OptionDto> optionDtos = new ArrayList<FeedflowItemAdgroupAddRequest.OptionDto>();
            for(int j = 0;j< addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getOptions().size(); j++){
                FeedflowItemAdgroupAddRequest.OptionDto optionDto = new FeedflowItemAdgroupAddRequest.OptionDto();
                if(null != addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getOptions().get(j).getOptionValue())optionDto.setOptionValue(addGroup.getDirectionalUnit().get(i).getDirectionalLabel().getOptions().get(j).getOptionValue());
                optionDtos.add(optionDto);
            }
            labelDto.setOptions(optionDtos);
            crowdDto.setTargetLabel(labelDto);
            crowdDtos.add(crowdDto);
        }
        adgroupDto.setCrowdList(crowdDtos);
        FeedflowItemAdgroupAddRequest.IntelligentBidDto intelligentBidDto = new FeedflowItemAdgroupAddRequest.IntelligentBidDto();
        //intelligentBidDto.setOpen(true);
        //intelligentBidDto.setScopePercent(1L);
        //intelligentBidDto.setStrategy(1L);
        //adgroupDto.setIntelligentBid(intelligentBidDto);
        if(null != addGroup.getItemId())adgroupDto.setItemId(addGroup.getItemId());
        req.setAdgroup(adgroupDto);
        FeedflowItemAdgroupAddResponse rsp = compositeTaobaoClient.execute(req, accessToken);
        return rsp;
    }

    /**
     *信息流单元内绑定资源位
     * @param accessToken
     * @return FeedflowItemAdgroupAdzoneBindResponse
     */
    public FeedflowItemAdgroupAdzoneBindResponse bindGroupByAPI(String accessToken,List<Location> locations){

        FeedflowItemAdgroupAdzoneBindRequest req = new FeedflowItemAdgroupAdzoneBindRequest();
        List<FeedflowItemAdgroupAdzoneBindRequest.AdzoneBindDto> list2 = new ArrayList<FeedflowItemAdgroupAdzoneBindRequest.AdzoneBindDto>();
       Long groupId=null;
     for (Location location:locations){
         FeedflowItemAdgroupAdzoneBindRequest.AdzoneBindDto obj3 = new  FeedflowItemAdgroupAdzoneBindRequest.AdzoneBindDto();
        obj3.setAdzoneId(location.getAdzoneId());
        obj3.setDiscount(location.getDiscount());
         groupId=location.getGroupId();
        list2.add(obj3);
     }
        req.setBindAdzoneList(list2);
        req.setAdgroupId(groupId);
        FeedflowItemAdgroupAdzoneBindResponse rsp = compositeTaobaoClient.execute(req, accessToken);
        return rsp;

    }

    /**
     *查询单元列表
     * @param accessToken
     * @param adgroupIdList   	单元id列表(非必须)
     * @param campaignIdList 计划id列表(非必须)
     * @param offset   	分页起始位置(非必须)
     * @param pageSize 	每页大小  (非必须)
     * @param statusList PAUSE("投放暂停"),START("投放开始"),ERMINATE("投放停止")(非必须)
     * @param adgroupName  单元名称(非必须)
     * @return  FeedflowItemAdgroupPageResponse
     */
    public  List<GroupStatus> pageGroupByAPI(String accessToken, List<Long> adgroupIdList,
                                             List<Long> campaignIdList, Long offset, Long pageSize, List<String> statusList,
                                             String adgroupName){
        FeedflowItemAdgroupPageRequest req = new FeedflowItemAdgroupPageRequest();
        FeedflowItemAdgroupPageRequest.AdgroupQueryDto obj1 = new  FeedflowItemAdgroupPageRequest.AdgroupQueryDto();
        if(null!= adgroupIdList){ obj1.setAdgroupIdList( adgroupIdList);}
        if(null!= campaignIdList){obj1.setCampaignIdList(campaignIdList);}
        if(null!= offset){obj1.setOffset(offset);}
        if(null!= pageSize){obj1.setPageSize(pageSize);}
        if(null!= statusList) {obj1.setStatusList(statusList);}
        if(null!= adgroupName) {obj1.setAdgroupName(adgroupName);}
        req.setAdgroupQuery(obj1);
        FeedflowItemAdgroupPageResponse rsp = compositeTaobaoClient.execute(req, accessToken);
        List<GroupStatus> groupStatusList=new ArrayList<>();

        List<FeedflowItemAdgroupPageResponse.AdgroupDTo> adgroupDTos =rsp.getResult().getResults();
        if(adgroupDTos!=null){

        for (FeedflowItemAdgroupPageResponse.AdgroupDTo adgroupDTo: adgroupDTos ) {
            GroupStatus groupStatus=new GroupStatus();
            groupStatus.setCampaignId(adgroupDTo.getCampaignId());
            groupStatus.setGroupId(adgroupDTo.getAdgroupId());
            groupStatus.setItemId(adgroupDTo.getItemId());
            groupStatus.setStatus(adgroupDTo.getStatus());
            groupStatus.setGroupName(adgroupDTo.getAdgroupName());
         //   groupStatus.setOpen(adgroupDTo.getIntelligentBid().getOpen());
            groupStatus.setScopePercent(adgroupDTo.getIntelligentBid().getScopePercent());
            groupStatus.setStrategy(adgroupDTo.getIntelligentBid().getStrategy());

            groupStatusList.add( groupStatus);

        }}
        return groupStatusList;

    }

    /**
     * 信息流新增并且绑定创意
     * @param accessToken
     * @param imgUrl 创意图片地址
     * @param creativeName 创意名称，同时会展现给客户
     * @param adgroupId 单元id
     * @return
     */
    public FeedflowItemAdgroupCreativeAddBindResponse bindGroupAddCreativeByAPI(String accessToken, String imgUrl, String creativeName, Long adgroupId){

        FeedflowItemAdgroupCreativeAddBindRequest req = new FeedflowItemAdgroupCreativeAddBindRequest();
        List<FeedflowItemAdgroupCreativeAddBindRequest.CreativeBindDto> list2 = new ArrayList<FeedflowItemAdgroupCreativeAddBindRequest.CreativeBindDto>();
        FeedflowItemAdgroupCreativeAddBindRequest.CreativeBindDto obj3 = new FeedflowItemAdgroupCreativeAddBindRequest.CreativeBindDto();
        list2.add(obj3);
        obj3.setImgUrl(imgUrl);
        obj3.setCreativeName(creativeName);
        req.setCreativeBindList(list2);
        req.setAdgroupId(adgroupId);
        FeedflowItemAdgroupCreativeAddBindResponse rsp = compositeTaobaoClient.execute(req, accessToken);

        return rsp;
    }

    /**
     *信息流单元下查看绑定资源位
     * @param accessToken
     * @param adzoneIdList 	广告位id列表(非必须)
     * @param adgroupId    单元id
     * @param pageSize    	分页数(非必须)
     * @param offset   	    分页条件(非必须)
     * @param adzoneName   广告位名称(非必须)
     * @return
     */
    public  List<Location> pageGroupAdZoneByAPI(String accessToken,List<Long> adzoneIdList,Long adgroupId,
                                                                      Long pageSize,Long offset,String adzoneName){

        FeedflowItemAdgroupAdzonePageRequest req = new FeedflowItemAdgroupAdzonePageRequest();
        FeedflowItemAdgroupAdzonePageRequest.AdzoneBindQueryDto obj1 = new  FeedflowItemAdgroupAdzonePageRequest.AdzoneBindQueryDto();
        if(null!=adzoneIdList){obj1.setAdzoneIdList(adzoneIdList);}

        obj1.setAdgroupId(adgroupId);
        if(null!=pageSize){obj1.setPageSize(pageSize);}
        if(null!=offset){obj1.setOffset(offset);}
        if (null!=adzoneName)obj1.setAdzoneName(adzoneName);
        req.setAdzoneBindQuery(obj1);
        FeedflowItemAdgroupAdzonePageResponse rsp = compositeTaobaoClient.execute(req, accessToken);
        List<Location> Lists=new ArrayList<>();
        List<FeedflowItemAdgroupAdzonePageResponse.AdzoneBindDto>Dtolist=rsp.getResult().getAdzoneBindList();
        if(Dtolist!=null){
        for(FeedflowItemAdgroupAdzonePageResponse.AdzoneBindDto adzoneBindDto:Dtolist)
        {
          Location location=new Location();
         location.setAdzoneId(adzoneBindDto.getAdzoneId());
         location.setAdzoneName(adzoneBindDto.getAdzoneName());
         location.setDiscount(adzoneBindDto.getDiscount());

            Lists.add(location);
        }}

        return Lists;
    }

    /**
     *信息流单元内解绑资源位
     * @param accessToken
     * @param adzoneIdList  广告位id
     * @param adgroupId   单元id
     * @return
     */

    public FeedflowItemAdgroupAdzoneUnbindResponse unbindGroupadZoneByAPI(String accessToken, String adzoneIdList, Long adgroupId){

        FeedflowItemAdgroupAdzoneUnbindRequest req = new FeedflowItemAdgroupAdzoneUnbindRequest();
        req.setAdzoneIdList(adzoneIdList);
        req.setAdgroupId(adgroupId);
        FeedflowItemAdgroupAdzoneUnbindResponse rsp = compositeTaobaoClient.execute(req, accessToken);

        return rsp;
    }

    /**
     *信息流单元下查看创意
     * @param accessToken
     * @param adgroupId 单元id
     * @param pageSize  分页页码(非必须)
     * @param offset    分页起始位置(非必须)
     * @param creativeIdList 创意id列表(非必须)
     * @param creativeName  创意名称(非必须)
     * @param auditStatus 审核状态，W待审核，P审核通过，R审核拒绝(非必须)
     * @return
     */
    public FeedflowItemAdgroupCreativePageResponse pageCreativeGroupByAPI(String accessToken, Long adgroupId, Long pageSize, Long offset,
                                                                          List<Long> creativeIdList, String creativeName, String auditStatus){

        FeedflowItemAdgroupCreativePageRequest req = new FeedflowItemAdgroupCreativePageRequest();
        FeedflowItemAdgroupCreativePageRequest.CreativeBindQueryDto obj1 = new FeedflowItemAdgroupCreativePageRequest.CreativeBindQueryDto();
        obj1.setAdgroupId(adgroupId);
        obj1.setPageSize(pageSize);
        obj1.setOffset(offset);
        obj1.setCreativeIdList(creativeIdList);

        obj1.setCreativeName(creativeName);
        obj1.setAuditStatus(auditStatus);
        req.setCreativeBindQuery(obj1);
        FeedflowItemAdgroupCreativePageResponse rsp = compositeTaobaoClient.execute(req, accessToken);

        return rsp;
    }

    /**
     *推广单元分日数据查询
     * @param accessToken
     * @param campaignId  计划id
     * @param adgroupId  	单元id
     * @return
     */
    public    List<GroupReport> rpthourlistGroupByDayByAPI(String accessToken, Long campaignId, DateRange dateRange, Long adgroupId
                                                                               ){

        FeedflowItemAdgroupRptdailylistRequest req = new FeedflowItemAdgroupRptdailylistRequest();
        FeedflowItemAdgroupRptdailylistRequest.RptQueryDto obj1 = new FeedflowItemAdgroupRptdailylistRequest.RptQueryDto();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD", Locale.FRANCE);
        obj1.setStartTime( dateRange.getStartTimeStr());
       if(null!=campaignId) obj1.setCampaignId(campaignId);
        if(null!=adgroupId)obj1.setAdgroupId(adgroupId);
        obj1.setEndTime(dateRange.getEndTimeStr());
        req.setRptQueryDTO(obj1);
        FeedflowItemAdgroupRptdailylistResponse rsp = compositeTaobaoClient.execute(req, accessToken);
        List<GroupReport>groupReports=new ArrayList<>();
        List<RptResultDto>rptResultDtos=rsp.getResult().getRptList();
        if(rptResultDtos!=null){
        for (RptResultDto rptResultDto: rptResultDtos) {
            GroupReport groupReport=new GroupReport(rptResultDto);
            groupReports.add(groupReport);

        }}
//        GroupReport groupReport=new GroupReport();
//        // 消耗
//        groupReport.setCharge(6L);
//        // 展现量
//        groupReport.setAdPv(2L);
//        // 点击量
//        groupReport.setClick(3L);
//        // 千人展现成本
//        groupReport.setEcpm(4L);
//        // 点击成本
//        groupReport.setEcpc(5L);
//        // 统计时间
//        groupReport.setLogDate("2019-05-21");
//        // 点击转化率
//        groupReport.setCvr(0.1);
//        // 投资回报率
//        groupReport.setRoi(0.2);
//        // 收藏宝贝量
//        groupReport.setInshopItemColNum(6L);
//        // 添加购物车量
//        groupReport.setCartNum(7L);
//        // 成交订单金额
//        groupReport.setAlipayInshopAmt(8L);
//        // 成交订单数
//        groupReport.setAlipayInShopNum(9L);
//        groupReports.add(groupReport);
//        GroupReport groupReport2=new GroupReport();
//        // 消耗
//        groupReport2.setCharge(6L);
//        // 展现量
//        groupReport2.setAdPv(2L);
//        // 点击量
//        groupReport2.setClick(3L);
//        // 千人展现成本
//        groupReport2.setEcpm(4L);
//        // 点击成本
//        groupReport2.setEcpc(5L);
//        // 统计时间
//        groupReport2.setLogDate("2019-05-22");
//        // 点击转化率
//        groupReport2.setCvr(0.1);
//        // 投资回报率
//        groupReport2.setRoi(0.2);
//        // 收藏宝贝量
//        groupReport2.setInshopItemColNum(6L);
//        // 添加购物车量
//        groupReport2.setCartNum(7L);
//        // 成交订单金额
//        groupReport2.setAlipayInshopAmt(8L);
//        // 成交订单数
//        groupReport2.setAlipayInShopNum(9L);
//
//        groupReports.add(groupReport2);

        return groupReports;
    }

    /**
     *超级推荐【商品推广】单元分时报表查询
     * @param accessToken
     * @param campaignId 	计划id
     * @param endHourId    结束小时
     * @param adgroupId   推广组id
     * @param logDate   	日期
     * @param startHourId  开始小时
     * @return
     */
    public FeedflowItemAdgroupRpthourlistResponse rpthourlistGroupByHourByAPI(String accessToken, Long campaignId, Long endHourId, Long adgroupId,
                                                                              String logDate, Long startHourId){
        FeedflowItemAdgroupRpthourlistRequest req = new FeedflowItemAdgroupRpthourlistRequest();
        FeedflowItemAdgroupRpthourlistRequest.RptQueryDto obj1 = new FeedflowItemAdgroupRpthourlistRequest.RptQueryDto();
        obj1.setCampaignId(campaignId);
        obj1.setEndHourId(endHourId);
        obj1.setAdgroupId(adgroupId);
        obj1.setLogDate(logDate);
        obj1.setStartHourId(startHourId);
        req.setRptQuery(obj1);
        FeedflowItemAdgroupRpthourlistResponse rsp = compositeTaobaoClient.execute(req, accessToken);
        return rsp;
    }


    }
