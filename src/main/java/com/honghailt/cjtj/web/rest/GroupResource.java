package com.honghailt.cjtj.web.rest;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.GroupReport;
import com.honghailt.cjtj.domain.GroupStatus;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.service.GroupService;
import com.honghailt.cjtj.service.SynItfData;
import com.honghailt.cjtj.service.dto.AddGroup;
import com.honghailt.cjtj.service.dto.DelGroup;
import com.honghailt.cjtj.service.dto.UpdateGroupDto;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.OperationResultVM;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 11:31 2019/5/11
 * @Modified By
 */


@RestController
@RequestMapping("/api/group")
public class GroupResource  {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GroupService groupService;

    @Autowired
    private SynItfData synItfData;

    /**
     * 获取单元状态和报表信息
     * @param dateRange
     * @return
     */
    @GetMapping("/getAll")

    public List<StatusAndReportVM> getAll( DateRange dateRange,@RequestParam Boolean syn,Long campaignId){
        synItfData.synGroup();
        //获取店铺消息
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<GroupStatus>groupStatuses=Lists.newArrayList();
        //判断campaignId, 如果为空,查询所有单元,如果不为空,查询指定单元
        if(0==campaignId){
            //获取所有公司id
           groupStatuses=groupService.findAll(details, syn);
        }
      else{
          groupStatuses = groupService.findGroupStatus(details, campaignId, syn);
      }
        List<StatusAndReportVM> results = Lists.newArrayList();
        for(GroupStatus groupStatus: groupStatuses) {
            List<GroupReport> groupReports = groupService.findGroupReportBycompaignId(details, groupStatus.getCampaignId(),dateRange,groupStatus.getGroupId(),syn);
            StatusAndReportVM vm = new StatusAndReportVM(groupStatus, ReportUtils.mergeReport( groupReports),  groupReports);
            results.add(vm);
            }

            return results;
        }

    /**
     * 获取单元状态和报表信息以及总和信息
     * @param dateRange
     * @return
     */
    @GetMapping("/getAllReport")

    public   Map getAllReport( DateRange dateRange,@RequestParam Boolean syn,Long campaignId){
        synItfData.synGroup();

        List<GroupReport>  groupReportlists =new ArrayList();
        GroupReport allGroupReport=new  GroupReport();
        //获取店铺消息
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<GroupStatus>groupStatuses=Lists.newArrayList();
        //判断campaignId, 如果为空,查询所有单元,如果不为空,查询指定单元
        if(0==campaignId){
            //获取所有公司id
            groupStatuses=groupService.findAll(details, syn);
        }
        else{
            groupStatuses = groupService.findGroupStatus(details, campaignId, syn);
        }
        List<StatusAndReportVM> results = Lists.newArrayList();
        for(GroupStatus groupStatus: groupStatuses) {
            List<GroupReport> groupReports = groupService.findGroupReportBycompaignId(details, groupStatus.getCampaignId(),dateRange,groupStatus.getGroupId(),syn);
            for(GroupReport groupReport:groupReports){
                groupReportlists.add(groupReport);
            }
            StatusAndReportVM vm = new StatusAndReportVM(groupStatus, ReportUtils.mergeReport( groupReports),  groupReports);
            results.add(vm);
        }
        allGroupReport=ReportUtils.mergeReport(groupReportlists);
        Map resultMap =new HashMap<>();
        resultMap.put("allGroupReport",allGroupReport);
        resultMap.put("results",results);
        return  resultMap;

    }


    /**
     * 根据计划Id获取单元状态和报表信息
     * @param dateRange
     * @return
     */
    @GetMapping("/getGroupbyId")
    public List<StatusAndReportVM> getGroupbyId( DateRange dateRange,@RequestParam Boolean syn,@RequestParam Long campaignId){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<GroupStatus> groupStatuses = groupService.findGroupStatus(details, campaignId,syn );
        List<StatusAndReportVM> results = Lists.newArrayList();
        for(GroupStatus groupStatus: groupStatuses) {
            List<GroupReport> groupReports = groupService.findGroupReportBycompaignId(details, groupStatus.getCampaignId(),dateRange,groupStatus.getGroupId(),syn);
            StatusAndReportVM vm = new StatusAndReportVM(groupStatus, ReportUtils.mergeReport( groupReports),  groupReports);
            results.add(vm);
        }

        return results;
    }
        /**
         * 修改单元状态
         */
    @PostMapping("/updateStatus")
    public OperationResultVM updateGroupStatus(@RequestBody List<UpdateGroupDto> updateGroupDtoList)
    {
        OperationResultVM operationResultVM = new OperationResultVM(null, false, "修改单元状态失败");
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        try {
            boolean tag = groupService.updateStatus(details, updateGroupDtoList);
            operationResultVM = new OperationResultVM(null, true, "修改单元状态成功");
        }catch(Exception e) {
            logger.error("店铺{[]}批量修改单元失败！",details.getNick(), e);
        }

        return operationResultVM;
    }


    /***
     * 批量删除单元
     * @param delGroup
     * @return
     */

    @PostMapping("/delGroups")
    public OperationResultVM delGroups(@RequestBody List<DelGroup> delGroup){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        return groupService.batchDelGroups(details,delGroup);
    }

    /**
     * 添加单元
     * @param addGroup
     * @return
     */
    @PostMapping("/addGroup")
    public List<Long>  addGroup(@RequestBody List<AddGroup> addGroup){
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        return groupService.addGroup(details,addGroup);
    }

    /**
     * 修改单元名称
     * @param updateGroupDto
     * @return
     */
    @PostMapping("/updateGroupName")
    public OperationResultVM updateGroupName(@RequestBody UpdateGroupDto updateGroupDto) {
        OperationResultVM operationResultVM = new OperationResultVM(null, false, "修改单元名称失败");
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        try {
            Boolean success = groupService.updateGroupName(details, updateGroupDto);
            if (success) {
                operationResultVM = new OperationResultVM(null, true, "修改单元名称成功");
            }
        }catch(Exception e) {
            logger.error("店铺{[]}修改单元{[]}名称失败！",details.getNick(), updateGroupDto.getGroupId(), e);
        }
        return operationResultVM;
    }

}

