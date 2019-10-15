package com.honghailt.cjtj.api.creative;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.Creative;
import com.honghailt.cjtj.domain.CreativeReport;
import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.LocationReport;
import com.honghailt.cjtj.taobao.CompositeTaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Api(value="CreativeAPI",tags={"CreativeAPI接口"})
@Service
public class CreativeAPI {

    @Autowired
    private CompositeTaobaoClient taobaoClient;

    @ApiOperation( value = "信息流增加推广计划",notes = "信息流增加推广计划" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "imgUrl", value = "创意图片地址", required = true, dataType = "String"),
        @ApiImplicitParam(name = "creativeName", value = "创意名称，同时会展现给客户", required = true, dataType = "String"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
    })
    public FeedflowItemAdgroupCreativeAddBindResponse addBindCreative(List<Creative> creatives ,String sessionKey){
        FeedflowItemAdgroupCreativeAddBindRequest req = new FeedflowItemAdgroupCreativeAddBindRequest();
        List<FeedflowItemAdgroupCreativeAddBindRequest.CreativeBindDto> creativeBindDtos = new ArrayList<FeedflowItemAdgroupCreativeAddBindRequest.CreativeBindDto>();
       Long adgroupId=null;
        for(Creative creative:creatives){
            FeedflowItemAdgroupCreativeAddBindRequest.CreativeBindDto creativeBindDto = new FeedflowItemAdgroupCreativeAddBindRequest.CreativeBindDto();
        creativeBindDto.setImgUrl(creative.getImgUrl());
        creativeBindDto.setCreativeName(creative.getCreativeName());
            adgroupId=creative.getGroupId();
            creativeBindDtos.add(creativeBindDto);
        }
        req.setCreativeBindList(creativeBindDtos);
        req.setAdgroupId(adgroupId);
        FeedflowItemAdgroupCreativeAddBindResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }

    @ApiOperation( value = "信息流单元下查看创意",notes = "信息流单元下查看创意" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "pageSize", value = "分页页码", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "offset", value = "分页起始位置", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "creativeIdList", value = "创意id列表", required = false, dataType = "Long"),
        @ApiImplicitParam(name = "creativeName", value = "创意名称", required = false, dataType = "String"),
        @ApiImplicitParam(name = "auditStatus", value = "审核状态，W待审核，P审核通过，R审核拒绝", required = false, dataType = "Long"),
    })
    public  List<Creative> creativePage(Long adgroupId, Long pageSize, Long offset,List<Long> creativeIdList,String creativeName,String auditStatus, String sessionKey){
        FeedflowItemAdgroupCreativePageRequest req = new FeedflowItemAdgroupCreativePageRequest();
        FeedflowItemAdgroupCreativePageRequest.CreativeBindQueryDto creativeBindQueryDto = new FeedflowItemAdgroupCreativePageRequest.CreativeBindQueryDto();
        if(null!=adgroupId) creativeBindQueryDto.setAdgroupId(adgroupId);
        if(null!=pageSize) creativeBindQueryDto.setPageSize(pageSize);
        if(null!=offset) creativeBindQueryDto.setOffset(offset);
        if(null!=creativeIdList) creativeBindQueryDto.setCreativeIdList(creativeIdList);
        if(null!=creativeName) creativeBindQueryDto.setCreativeName(creativeName);
        if(null!=auditStatus)creativeBindQueryDto.setAuditStatus(auditStatus);
        req.setCreativeBindQuery(creativeBindQueryDto);
         FeedflowItemAdgroupCreativePageResponse rsp = taobaoClient.execute(req, sessionKey);


        List<Creative> apiStatusList = Lists.newArrayList();
       List<FeedflowItemAdgroupCreativePageResponse.CreativeBindDto> list= rsp.getResult().getCreativeBindList();
       if (!CollectionUtils.isEmpty(list)){
           for(FeedflowItemAdgroupCreativePageResponse.CreativeBindDto dto:list) {
               Creative creative=new Creative();
               creative.setGroupId(dto.getAdgroupId());
               creative.setAuditReason(dto.getAuditReason());
               creative.setAuditStatus(dto.getAuditStatus());
               creative.setCampaignId(dto.getCampaignId());
               creative.setCreativeId(dto.getCreativeId());
               creative.setCreativeName(dto.getCreativeName());
               creative.setTitle(dto.getTitle());
               creative.setImgUrl(dto.getImgUrl());

               apiStatusList.add(creative);

           }
       }
        return  apiStatusList;
    }


    @ApiOperation( value = "信息流删除创意",notes = "信息流删除创意" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "longList", value = "单元id", required = true, dataType = "List<Long>"),
    })
    public FeedflowItemCreativeDeleteResponse creativeDelete(String creativeIdList, String sessionKey){
        FeedflowItemCreativeDeleteRequest req = new FeedflowItemCreativeDeleteRequest();
        req.setCreativeIdList(creativeIdList);
       FeedflowItemCreativeDeleteResponse rsp = taobaoClient.execute(req, sessionKey);
       return rsp;

    }


    @ApiOperation( value = "创意分日数据查询",notes = "创意分日数据查询" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "startTime", value = "查询开始日期", required = true, dataType = "String"),
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "creativeId", value = "创意id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "adgroupId", value = "单元id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "endTime", value = "查询结束日期", required = true, dataType = "String"),
    })
    public List<CreativeReport> dcreativeRptdailylist(String sessionKey, Long campaignId,Long adgroupId,Long creativeId, DateRange dateRange){
        FeedflowItemCreativeRptdailylistRequest req = new FeedflowItemCreativeRptdailylistRequest();
        FeedflowItemCreativeRptdailylistRequest.RptQueryDto rptQueryDto = new FeedflowItemCreativeRptdailylistRequest.RptQueryDto();
        rptQueryDto.setStartTime(dateRange.getStartTimeStr());
        rptQueryDto.setCampaignId(campaignId);
        rptQueryDto.setCreativeId(creativeId);
        rptQueryDto.setAdgroupId(adgroupId);
        rptQueryDto.setEndTime(dateRange.getEndTimeStr());
        req.setRptQueryDTO(rptQueryDto);
        FeedflowItemCreativeRptdailylistResponse rsp = taobaoClient.execute(req, sessionKey);
        List<CreativeReport> listNew=new ArrayList<>();
       List<FeedflowItemCreativeRptdailylistResponse .RptResultDto> list=rsp.getResult().getRptList();

       if (list!=null){

           for(FeedflowItemCreativeRptdailylistResponse .RptResultDto dto:list){
               CreativeReport   creativeReport=new   CreativeReport(dto);
               listNew.add(creativeReport);

           }
       }
//        CreativeReport creativeReport=new CreativeReport();
//        // 消耗
//        creativeReport.setCharge(6L);
//        // 展现量
//        creativeReport.setAdPv(2L);
//        // 点击量
//        creativeReport.setClick(3L);
//        // 千人展现成本
//        creativeReport.setEcpm(4L);
//        // 点击成本
//        creativeReport.setEcpc(5L);
//        // 统计时间
//        creativeReport.setLogDate("2019-05-21");
//        // 点击转化率
//        creativeReport.setCvr(0.1);
//        // 投资回报率
//        creativeReport.setRoi(0.2);
//        // 收藏宝贝量
//        creativeReport.setInshopItemColNum(6L);
//        // 添加购物车量
//        creativeReport.setCartNum(7L);
//        // 成交订单金额
//        creativeReport.setAlipayInshopAmt(8L);
//        // 成交订单数
//        creativeReport.setAlipayInShopNum(9L);
//        listNew.add( creativeReport);
//        CreativeReport  creativeReport2=new   CreativeReport ();
//        // 消耗
//        creativeReport2.setCharge(6L);
//        // 展现量
//        creativeReport2.setAdPv(2L);
//        // 点击量
//        creativeReport2.setClick(3L);
//        // 千人展现成本
//        creativeReport2.setEcpm(4L);
//        // 点击成本
//        creativeReport2.setEcpc(5L);
//        // 统计时间
//        creativeReport2.setLogDate("2019-05-22");
//        // 点击转化率
//        creativeReport2.setCvr(0.1);
//        // 投资回报率
//        creativeReport2.setRoi(0.2);
//        // 收藏宝贝量
//        creativeReport2.setInshopItemColNum(6L);
//        // 添加购物车量
//        creativeReport2.setCartNum(7L);
//        // 成交订单金额
//        creativeReport2.setAlipayInshopAmt(8L);
//        // 成交订单数
//        creativeReport2.setAlipayInShopNum(9L);
//
//        listNew.add(creativeReport2);
        return listNew;
    }

    @ApiOperation( value = " 超级推荐【商品推广】创意分时报表查询",notes = " 超级推荐【商品推广】创意分时报表查询" )
    @ApiImplicitParams({
        @ApiImplicitParam(name = "campaignId", value = "计划id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "creativeId", value = "创意id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "endHourId", value = "结束小时", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "adgroupId", value = "推广组id", required = true, dataType = "Long"),
        @ApiImplicitParam(name = "logDate", value = "日期", required = true, dataType = "String"),
        @ApiImplicitParam(name = "startHourId", value = "开始小时", required = true, dataType = "Long"),
    })
    public FeedflowItemCreativeRpthourlistResponse creativeRpthourlist(Long campaignId,Long creativeId,Long endHourId,Long adgroupId, String logDate,Long startHourId,String sessionKey){

        FeedflowItemCreativeRpthourlistRequest req = new FeedflowItemCreativeRpthourlistRequest();
        FeedflowItemCreativeRpthourlistRequest.RptQueryDto rptQueryDto = new FeedflowItemCreativeRpthourlistRequest.RptQueryDto();
        rptQueryDto.setCampaignId(campaignId);
        rptQueryDto.setCreativeId(creativeId);
        rptQueryDto.setEndHourId(endHourId);
        rptQueryDto.setAdgroupId(adgroupId);
        rptQueryDto.setLogDate(logDate);
        rptQueryDto.setStartHourId(startHourId);
        req.setRptQuery(rptQueryDto);
        FeedflowItemCreativeRpthourlistResponse rsp = taobaoClient.execute(req, sessionKey);
        return rsp;
    }
}
