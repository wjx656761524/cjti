package com.honghailt.cjtj.api.crowd;

import com.honghailt.cjtj.domain.AddCrowd;
import com.honghailt.cjtj.taobao.CompositeTaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value="CrowdAPI",tags={"CrowdAPI接口"})
@Service
public class CrowdAPI {

    @Autowired
    private CompositeTaobaoClient taobaoClient;

    @ApiOperation( value = "分页查询单品单元下人群列表",notes = "分页查询单品单元下人群列表" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "targetTypeList", value = "定向类型", required = false, dataType = "List<String>"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "pageSize", value = "分页条件", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "crowdId", value = "人群id", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "statusList", value = "人群状态", required = false, dataType = "List<String>"),
        @ApiImplicitParam(name = "offset", value = "分页条件", required = false, dataType = "Long"),
    })
    public FeedflowItemCrowdPageResponse crowdPage(List<String> targetTypeList,Long adgroupId,Long pageSize,Long crowdId,List<String> statusList,Long offset, String sessionKey){
        FeedflowItemCrowdPageRequest req = new FeedflowItemCrowdPageRequest();
        FeedflowItemCrowdPageRequest.CrowdQueryDto crowdQueryDto = new FeedflowItemCrowdPageRequest.CrowdQueryDto();
        if(null != targetTypeList) crowdQueryDto.setTargetTypeList(targetTypeList);
        if(null != adgroupId) crowdQueryDto.setAdgroupId(adgroupId);
        if(null != pageSize) crowdQueryDto.setPageSize(pageSize);
        if(null != crowdId) crowdQueryDto.setCrowdId(crowdId);
        if(null != statusList) crowdQueryDto.setStatusList(statusList);
        if(null != offset) crowdQueryDto.setOffset(offset);
        req.setCrowdQuery(crowdQueryDto);
        FeedflowItemCrowdPageResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }



    @ApiOperation( value = "单品单元下，新增定向人群",notes = "单品单元下，新增定向人群" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "targetId", value = "定向id，可通过标签接口获取", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "labelId", value = "标签id，可通过标签接口获取", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "labelValue", value = "标签值，可通过标签接口获取", required = true, dataType = "String"),
        @ApiImplicitParam(name = "targetType", value = "定向类型", required = true, dataType = "String"),
        @ApiImplicitParam(name = "optionValue", value = "选项值", required = true, dataType = "String"),
        @ApiImplicitParam(name = "price", value = "人群出价，单位：分", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "crowdDesc", value = "人群描述", required = false, dataType = "String"),
        @ApiImplicitParam(name = "crowdName", value = "人群名称", required = false, dataType = "String"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
    })
    public FeedflowItemCrowdAddResponse crowdAdd(AddCrowd addCrowd, String sessionKey){
        FeedflowItemCrowdAddRequest req = new FeedflowItemCrowdAddRequest();
        List<FeedflowItemCrowdAddRequest.CrowdDto> crowdDtoArrayList = new ArrayList<FeedflowItemCrowdAddRequest.CrowdDto>();
        FeedflowItemCrowdAddRequest.CrowdDto crowdDto = new FeedflowItemCrowdAddRequest.CrowdDto();
        if(null != addCrowd.getPrice())crowdDto.setPrice(addCrowd.getPrice());
        if(null != addCrowd.getCrowdDesc())crowdDto.setCrowdDesc(addCrowd.getCrowdDesc());
        if(null != addCrowd.getOptionName())crowdDto.setCrowdName(addCrowd.getOptionName());
        FeedflowItemCrowdAddRequest.LabelDto labelDto = new FeedflowItemCrowdAddRequest.LabelDto();
        if(null != addCrowd.getTargetId())labelDto.setTargetId(addCrowd.getTargetId());
        if(null != addCrowd.getLabelId())labelDto.setLabelId(addCrowd.getLabelId());
        if(null != addCrowd.getLabelValue())labelDto.setLabelValue(addCrowd.getLabelValue());
        if(null != addCrowd.getTargetType())labelDto.setTargetType(addCrowd.getTargetType());
        List<FeedflowItemCrowdAddRequest.OptionDto> optionDtoList = new ArrayList<FeedflowItemCrowdAddRequest.OptionDto>();
       if(null != addCrowd.getOptionValue()){
           for(int i = 0;i< addCrowd.getOptionValue().size();i++){
               FeedflowItemCrowdAddRequest.OptionDto optionDto = new FeedflowItemCrowdAddRequest.OptionDto();
               optionDto.setOptionValue(addCrowd.getOptionValue().get(i));
               optionDtoList.add(optionDto);
           }
       }
        labelDto.setOptions(optionDtoList);
        crowdDto.setTargetLabel(labelDto);
        crowdDtoArrayList.add(crowdDto);
        req.setCrowds(crowdDtoArrayList);
        if(null != addCrowd.getAdgroupId())req.setAdgroupId(addCrowd.getAdgroupId());
        FeedflowItemCrowdAddResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }


    @ApiOperation( value = " 覆盖单元下同类型定向人群",notes = " 覆盖单元下同类型定向人群" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "targetId", value = "定向id，可通过标签接口获取", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "labelId", value = "标签id，可通过标签接口获取", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "labelValue", value = "标签值，可通过标签接口获取", required = true, dataType = "String"),
        @ApiImplicitParam(name = "targetType", value = "定向类型", required = true, dataType = "String"),
        @ApiImplicitParam(name = "optionValue", value = "选项值", required = true, dataType = "String"),
        @ApiImplicitParam(name = "price", value = "人群出价，单位：分", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "crowdDesc", value = "人群描述", required = false, dataType = "String"),
        @ApiImplicitParam(name = "crowdId", value = "人群id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
    })
    public FeedflowItemCrowdModifyResponse crowdModify(Long targetId,Long labelId,String labelValue,String targetType,String optionValue,Long price,String crowdDesc ,Long crowdId,Long adgroupId,String sessionKey){
        FeedflowItemCrowdModifyRequest req = new FeedflowItemCrowdModifyRequest();
        List<FeedflowItemCrowdModifyRequest.CrowdDto> crowdDtos = new ArrayList<FeedflowItemCrowdModifyRequest.CrowdDto>();
        FeedflowItemCrowdModifyRequest.CrowdDto crowdDto = new FeedflowItemCrowdModifyRequest.CrowdDto();
        crowdDto.setPrice(price);
        crowdDto.setCrowdDesc(crowdDesc);
        crowdDto.setCrowdId(crowdId);
        crowdDtos.add(crowdDto);
        FeedflowItemCrowdModifyRequest.LabelDto labelDto = new FeedflowItemCrowdModifyRequest.LabelDto();
        labelDto.setTargetId(targetId);
        labelDto.setLabelId(labelId);
        labelDto.setLabelValue(labelValue);
        labelDto.setTargetType(targetType);
        List<FeedflowItemCrowdModifyRequest.OptionDto> optionDtos = new ArrayList<FeedflowItemCrowdModifyRequest.OptionDto>();
        FeedflowItemCrowdModifyRequest.OptionDto optionDto = new FeedflowItemCrowdModifyRequest.OptionDto();
        optionDtos.add(optionDto);
        optionDto.setOptionValue(optionValue);
        labelDto.setOptions(optionDtos);
        req.setCrowds(crowdDtos);
        req.setAdgroupId(adgroupId);
        FeedflowItemCrowdModifyResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    @ApiOperation( value = "删除单品人群",notes = "删除单品人群" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "crowdId", value = "人群id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
    })
    public FeedflowItemCrowdDeleteResponse crowdDelete( Long crowdId,Long adgroupId, String sessionKey){
        FeedflowItemCrowdDeleteRequest req = new FeedflowItemCrowdDeleteRequest();
        List<FeedflowItemCrowdDeleteRequest.CrowdDto> crowdDtos = new ArrayList<FeedflowItemCrowdDeleteRequest.CrowdDto>();
        FeedflowItemCrowdDeleteRequest.CrowdDto crowdDto = new FeedflowItemCrowdDeleteRequest.CrowdDto();
        crowdDto.setCrowdId(crowdId);
        crowdDtos.add(crowdDto);
        req.setCrowds(crowdDtos);
        req.setAdgroupId(adgroupId);
        FeedflowItemCrowdDeleteResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }


    @ApiOperation( value = "修改人群出价或状态",notes = "修改人群出价或状态" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "price", value = "出价，单位：分", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "status", value = "人群状态", required = false, dataType = "String"),
        @ApiImplicitParam(name = "crowdId", value = "人群id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
    })
    public FeedflowItemCrowdModifybindResponse crowdModifybind( Long price,String status,Long crowdId,Long adgroupId, String sessionKey){
        FeedflowItemCrowdModifybindRequest req = new FeedflowItemCrowdModifybindRequest();
        List<FeedflowItemCrowdModifybindRequest.CrowdDto> crowdDtos = new ArrayList<FeedflowItemCrowdModifybindRequest.CrowdDto>();
        FeedflowItemCrowdModifybindRequest.CrowdDto crowdDto = new FeedflowItemCrowdModifybindRequest.CrowdDto();
        if(null != price)crowdDto.setPrice(price);
        if(null != status)crowdDto.setStatus(status);
        if(null != crowdId)crowdDto.setCrowdId(crowdId);
        crowdDtos.add(crowdDto);
        req.setCrowds(crowdDtos);
        if(null != adgroupId)req.setAdgroupId(adgroupId);
        FeedflowItemCrowdModifybindResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }


    @ApiOperation( value = "定向分日数据查询",notes = "定向分日数据查询" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "查询开始时间", required = true, dataType = "String"),
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "crowdId", value = "定向id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "endTime", value = "查询结束时间", required = true, dataType = "String"),
    })
    public FeedflowItemCrowdRptdailylistResponse crowdRptdailylist( String startTime,Long campaignId,Long adgroupId,Long crowdId,String endTime, String sessionKey){
        FeedflowItemCrowdRptdailylistRequest req = new FeedflowItemCrowdRptdailylistRequest();
        FeedflowItemCrowdRptdailylistRequest.RptQueryDto rptQueryDto = new FeedflowItemCrowdRptdailylistRequest.RptQueryDto();
        rptQueryDto.setStartTime(startTime);
        rptQueryDto.setCampaignId(campaignId);
        rptQueryDto.setAdgroupId(adgroupId);
        rptQueryDto.setCrowdId(crowdId);
        rptQueryDto.setEndTime(endTime);
        req.setRptQueryDTO(rptQueryDto);
        FeedflowItemCrowdRptdailylistResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    @ApiOperation( value = " 超级推荐【商品推广】定向分时报表查询",notes = " 超级推荐【商品推广】定向分时报表查询" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "endHourId", value = "结束小时", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "crowdId", value = "定向id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "logDate", value = "日期", required = true, dataType = "String"),
        @ApiImplicitParam(name = "startHourId", value = "开始小时", required = true, dataType = "Long"),
    })
    public FeedflowItemCrowdRpthourlistResponse crowdRpthourlist( Long campaignId,Long endHourId,Long adgroupId,Long crowdId,String logDate,Long startHourId, String sessionKey){
        FeedflowItemCrowdRpthourlistRequest req = new FeedflowItemCrowdRpthourlistRequest();
        FeedflowItemCrowdRpthourlistRequest.RptQueryDto rptQueryDto = new FeedflowItemCrowdRpthourlistRequest.RptQueryDto();
        rptQueryDto.setCampaignId(campaignId);
        rptQueryDto.setEndHourId(endHourId);
        rptQueryDto.setAdgroupId(adgroupId);
        rptQueryDto.setCrowdId(crowdId);
        rptQueryDto.setLogDate(logDate);
        rptQueryDto.setStartHourId(startHourId);
        req.setRptQuery(rptQueryDto);
        FeedflowItemCrowdRpthourlistResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }



    @ApiOperation( value = "获取有权限的定向列表",notes = "获取有权限的定向列表" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
    })
    public FeedflowItemTargetValidlistResponse targetValidList( Long campaignId, String sessionKey){
        FeedflowItemTargetValidlistRequest req = new FeedflowItemTargetValidlistRequest();
        if(null != campaignId) req.setCampaignId(campaignId);
        FeedflowItemTargetValidlistResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    @ApiOperation( value = "分页查询定向标签列表",notes = "分页查询定向标签列表" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "optionName", value = "选项值", required = false, dataType = "String"),
        @ApiImplicitParam(name = "pageSize", value = "分页条件", required = false	, dataType = "Long"),
        @ApiImplicitParam(name = "targetId", value = "定向id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "offset", value = "分页条件", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "targetType", value = "定向类型", required = true, dataType = "String"),
        @ApiImplicitParam(name = "itemIdList", value = "宝贝id列表", required = true, dataType = "Long[]"),
    })
    public FeedflowItemOptionPageResponse optionPage( String optionName,Long pageSize,Long targetId,Long offset,String targetType,List<Long> itemIdList, String sessionKey){
        FeedflowItemOptionPageRequest req = new FeedflowItemOptionPageRequest();
        FeedflowItemOptionPageRequest.LabelQueryDto labelQueryDto = new FeedflowItemOptionPageRequest.LabelQueryDto();
        if(null != optionName) labelQueryDto.setOptionName(optionName);
        if(null != pageSize) labelQueryDto.setPageSize(pageSize);
        if(null != targetId)  labelQueryDto.setTargetId(targetId);
        if(null != offset)  labelQueryDto.setOffset(offset);
        if(null != targetType)  labelQueryDto.setTargetType(targetType);
        if(null != itemIdList)  labelQueryDto.setItemIdList(itemIdList);
        req.setLabelQuery(labelQueryDto);
        FeedflowItemOptionPageResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    @ApiOperation( value = "信息流查看商品列表",notes = "信息流查看商品列表" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "title", value = "商品标题", required = false	, dataType = "String"),
        @ApiImplicitParam(name = "pageSize", value = "分页页码，不得超过20", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "itemIdList", value = "商品id列表", required = false, dataType = "List<Long>"),
        @ApiImplicitParam(name = "currentPage", value = "当前页数", required = false, dataType = "Long"),
    })
    public FeedflowItemItemPageResponse itemPage( Long campaignId,String title,Long pageSize,List<Long> itemIdList,Long currentPage, String sessionKey){
        FeedflowItemItemPageRequest req = new FeedflowItemItemPageRequest();
        FeedflowItemItemPageRequest.ItemQueryDto itemQueryDto = new FeedflowItemItemPageRequest.ItemQueryDto();
        if(null != campaignId) itemQueryDto.setCampaignId(campaignId);
        if(null != title) itemQueryDto.setTitle(title);
        if(null != pageSize) itemQueryDto.setPageSize(pageSize);
        if(null != itemIdList) itemQueryDto.setItemIdList(itemIdList);
        if(null != currentPage) itemQueryDto.setCurrentPage(currentPage);
        req.setItemQuery(itemQueryDto);
        FeedflowItemItemPageResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

}
