package com.honghailt.cjtj.web.rest;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.Location;
import com.honghailt.cjtj.domain.LocationReport;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.LocationService;
import com.honghailt.cjtj.service.dto.DelLocation;
import com.honghailt.cjtj.service.dto.Engine;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.OperationResultVM;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 17:06 2019/5/17
 * @Modified By
 */
@RestController
@RequestMapping("/api/location")
public class LocationResource {

    @Autowired
    private LocationService locationService;

    /**
     *查询指定的广告位信息
     * @param campaignId
     * @param groupId
     * @param dateRange
     * @param syn
     * @return
     */

    @GetMapping("/getAll")
    public List<StatusAndReportVM> getAll(@RequestParam Long campaignId, @RequestParam Long groupId, DateRange dateRange,Boolean syn) {
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();

            List<Location> locationList = locationService.queryLocationList(details, campaignId, groupId, syn);
            List<StatusAndReportVM> results = Lists.newArrayList();
            for (Location location : locationList) {
                List<LocationReport> locationReports = locationService.findLocationLReportBygroupId(details, dateRange, location.getCampaignId(), location.getAdzoneId(), location.getGroupId(), syn);
                StatusAndReportVM vm = new StatusAndReportVM(location, ReportUtils.mergeReport(locationReports), locationReports);
                results.add(vm);
            }

            return results;

    }
    /**
     *查询指定的广告位信息和报表信息
     * @param campaignId
     * @param groupId
     * @param dateRange
     * @param syn
     * @return
     */

    @GetMapping("/getAllReport")
    public Map getAllReport(@RequestParam Long campaignId, @RequestParam Long groupId, DateRange dateRange,Boolean syn) {

        List<LocationReport>  locationReportlists =new ArrayList();
        LocationReport allLocationReport=new LocationReport();
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();

        List<Location> locationList = locationService.queryLocationList(details, campaignId, groupId, syn);
        List<StatusAndReportVM> results = Lists.newArrayList();
        for (Location location : locationList) {
            List<LocationReport> locationReports = locationService.findLocationLReportBygroupId(details, dateRange, location.getCampaignId(), location.getAdzoneId(), location.getGroupId(), syn);
            for(LocationReport locationReport:locationReports){
                locationReportlists.add(locationReport);
            }
            StatusAndReportVM vm = new StatusAndReportVM(location, ReportUtils.mergeReport(locationReports), locationReports);
            results.add(vm);
        }
        allLocationReport=ReportUtils.mergeReport(locationReportlists);
        Map resultMap =new HashMap<>();
        resultMap.put("allLocationReport",allLocationReport);
        resultMap.put("results",results);

        return resultMap;

    }


    /**
     * 批量删除
     * @param delLocation
     * @return
     */
    @PostMapping("/delLocations")
    public OperationResultVM delGroups(@RequestBody List<DelLocation> delLocation){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        return locationService.batchDelLocations(details,delLocation);
    }

    /**
     * 批量修改溢价
     * @param locations
     * @return
     */
    @PostMapping("/updateLocations")
    public OperationResultVM updateLocations(@RequestBody List<Location> locations){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
      return locationService.updateLocation(details,locations);

    }

    /**
     * 获取指定单元的所有资源位
     * @param engine

     */
    @PostMapping("/getAllLocations")
    public List<Location> getALLLocations(@RequestBody Engine engine){
          if(engine==null)return null;
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<Location> locations=locationService.getALLLocations(details,engine.getCampaignId(),engine.getGroupId());
        return  locations;
    }
}


