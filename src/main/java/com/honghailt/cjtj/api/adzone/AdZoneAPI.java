package com.honghailt.cjtj.api.adzone;


import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.GroupReport;
import com.honghailt.cjtj.domain.Location;
import com.honghailt.cjtj.domain.LocationReport;
import com.honghailt.cjtj.taobao.CompositeTaobaoClient;
import com.taobao.api.request.FeedflowItemAdgroupRpthourlistRequest;
import com.taobao.api.request.FeedflowItemAdzoneListRequest;
import com.taobao.api.request.FeedflowItemAdzoneRptdailylistRequest;
import com.taobao.api.response.FeedflowItemAdgroupRpthourlistResponse;
import com.taobao.api.response.FeedflowItemAdzoneListResponse;
import com.taobao.api.response.FeedflowItemAdzoneRptdailylistResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdZoneAPI {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CompositeTaobaoClient client;

    /**
     *批量查询可用广告位列表
     * @param accessToken
     * @param campaignId    计划id
     * @param adzoneIdList  广告位id列表(非必须)
     * @param adzoneName  	广告位名称(非必须)
     * @return
     */
    public  List<Location>  listZoneByAPI(String accessToken, Long campaignId, List<Long> adzoneIdList, String adzoneName)
    {

        FeedflowItemAdzoneListRequest req = new FeedflowItemAdzoneListRequest();
        FeedflowItemAdzoneListRequest.AdzoneQueryDto obj1 = new  FeedflowItemAdzoneListRequest.AdzoneQueryDto();
       if(null!=campaignId) obj1.setCampaignId(campaignId);
        if(null!=adzoneIdList)obj1.setAdzoneIdList(adzoneIdList);

       if(null!=adzoneName) obj1.setAdzoneName(adzoneName);
        req.setAdzoneQuery(obj1);
        FeedflowItemAdzoneListResponse rsp = client.execute(req, accessToken);
         List<Location> locations= Lists.newArrayList();
        List<FeedflowItemAdzoneListResponse.AdzoneDto> list= rsp.getResult().getAdzoneList();
        if(null!=list){
            for(FeedflowItemAdzoneListResponse.AdzoneDto dto:list){
                Location location=new Location();
                location.setCampaignId(campaignId);
               location.setAdzoneId(dto.getAdzoneId());
               location.setAdzoneName(dto.getAdzoneName());
                locations.add(location);
            }


        }
        return  locations;
    }

    /**
     * 按日查询广告位日报信息
     * @param accessToken
     * @param dateRange
     * @param campaignId
     * @param adzoneId
     * @param adgroupId
     * @return
     */
    public List<LocationReport> rptdailylistZoneByDayByAPI(String accessToken, DateRange dateRange, Long campaignId,
                                                           Long adzoneId, Long adgroupId)
    {

        FeedflowItemAdzoneRptdailylistRequest req = new FeedflowItemAdzoneRptdailylistRequest();
        FeedflowItemAdzoneRptdailylistRequest.RptQueryDto obj1 = new  FeedflowItemAdzoneRptdailylistRequest.RptQueryDto();
        obj1.setStartTime(dateRange.getStartTimeStr());
        obj1.setCampaignId(campaignId);
        obj1.setAdgroupId(adzoneId);
        obj1.setAdzoneId(adgroupId);
        obj1.setEndTime(dateRange.getEndTimeStr());
        req.setRptQueryDTO(obj1);
        FeedflowItemAdzoneRptdailylistResponse rsp = client.execute(req, accessToken);
        List<LocationReport>locationReports=new ArrayList<>();
        List< FeedflowItemAdzoneRptdailylistResponse.RptResultDto>rptResultDtos=rsp.getResult().getRptList();
        if(rptResultDtos!=null){
            for (FeedflowItemAdzoneRptdailylistResponse.RptResultDto rptResultDto: rptResultDtos) {
                LocationReport locationReport=new   LocationReport(rptResultDto);
                locationReports.add(locationReport);

            }}
//        LocationReport locationReport=new LocationReport();
//        // 消耗
//        locationReport.setCharge(6L);
//        // 展现量
//        locationReport.setAdPv(2L);
//        // 点击量
//        locationReport.setClick(3L);
//        // 千人展现成本
//        locationReport.setEcpm(4L);
//        // 点击成本
//        locationReport.setEcpc(5L);
//        // 统计时间
//        locationReport.setLogDate("2019-05-21");
//        // 点击转化率
//        locationReport.setCvr(0.1);
//        // 投资回报率
//        locationReport.setRoi(0.2);
//        // 收藏宝贝量
//        locationReport.setInshopItemColNum(6L);
//        // 添加购物车量
//        locationReport.setCartNum(7L);
//        // 成交订单金额
//        locationReport.setAlipayInshopAmt(8L);
//        // 成交订单数
//        locationReport.setAlipayInShopNum(9L);
//        locationReports.add( locationReport);
//        LocationReport locationReport2=new LocationReport();
//        // 消耗
//        locationReport2.setCharge(6L);
//        // 展现量
//        locationReport2.setAdPv(2L);
//        // 点击量
//        locationReport2.setClick(3L);
//        // 千人展现成本
//        locationReport2.setEcpm(4L);
//        // 点击成本
//        locationReport2.setEcpc(5L);
//        // 统计时间
//        locationReport2.setLogDate("2019-05-22");
//        // 点击转化率
//        locationReport2.setCvr(0.1);
//        // 投资回报率
//        locationReport2.setRoi(0.2);
//        // 收藏宝贝量
//        locationReport2.setInshopItemColNum(6L);
//        // 添加购物车量
//        locationReport2.setCartNum(7L);
//        // 成交订单金额
//        locationReport2.setAlipayInshopAmt(8L);
//        // 成交订单数
//        locationReport2.setAlipayInShopNum(9L);
//
//        locationReports.add(locationReport2);
        return locationReports;
    }

    /**
     *超级推荐【商品推广】单元分时报表查询
     * @param accessToken
     * @param campaignId  计划id
     * @param endHourId  结束小时
     * @param adgroupId  推广组id
     * @param logDate    日期
     * @param startHourId  	开始小时
     * @return
     */
    public FeedflowItemAdgroupRpthourlistResponse rpthourlistZoneByHourByAPI(String accessToken, Long campaignId, Long endHourId,
                                                                             Long adgroupId, String logDate, Long startHourId)
    {

        FeedflowItemAdgroupRpthourlistRequest req = new FeedflowItemAdgroupRpthourlistRequest();
        FeedflowItemAdgroupRpthourlistRequest.RptQueryDto obj1 = new FeedflowItemAdgroupRpthourlistRequest.RptQueryDto();
        obj1.setCampaignId(campaignId);
        obj1.setEndHourId(adgroupId);
        obj1.setAdgroupId(adgroupId);
        obj1.setLogDate(logDate);
        obj1.setStartHourId(startHourId);
        req.setRptQuery(obj1);
        FeedflowItemAdgroupRpthourlistResponse rsp = client.execute(req,accessToken);
        return rsp;
    }




}
