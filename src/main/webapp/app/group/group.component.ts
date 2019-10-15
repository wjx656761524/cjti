import {Component, EventEmitter, OnInit, ViewChild} from '@angular/core';
import {Principal} from "app/shared/auth/principal.service";
import {DEFAULT_COLUMNS, REPORT_FIELDS} from 'app/report.constants';
import {NgbDate, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AdgroupService} from "app/shared/adgroup/adgroup.service";
import {PAGE_SIZE} from "app/app.constants";
import {DateSelectChange} from "app/shared";
import {CjtjNotifyService} from "app/shared/notify/notify.service";

import {DelGroupModel} from "app/shared/adgroup/del-group.model";
import {OperationType} from "app/shared/enum/operation-type.enum";
import {OperationSource} from "app/shared/enum/operation-source.enum";
import any = jasmine.any;
import {UpdateGroupModel} from "app/shared/adgroup/update-group";


@Component({
    selector: 'cjti-group',
    templateUrl: './group.component.html',

})
export class GroupComponent implements OnInit {

    account: any; // 当前账户
    fields = REPORT_FIELDS;
    selectColumns = [...DEFAULT_COLUMNS];
    test:any;
    campaign: any = {}; // 计划信息
    group: any;
    allSelect = false;
    campaignId:any=0;
    campaignName = '';
    page = 1;
    isRealTime = true;
    startTime: string;
    endTime: string;
    fromDate: any;
    toDate: any;
    groups: any[] = []; // 当前页显示的
    filteredGroups: any[] = []; // 过滤后的数据
    queryParams: any = { // 过滤参数
       groupName: '', // 标题

    }; // 过滤参数
    pageSize = PAGE_SIZE;
    allGroup: any;
    predicate = 'impression'; // 排序字段
    reverse: any; // 正反序
    paramsEventer: EventEmitter<any> = new EventEmitter();
    campaignShow:boolean=true;
    status: any;
    editGroupName: string;
    editGroupStatus: string;

    @ViewChild('pagination') pagination: any;


    constructor(private http: HttpClient,
                public principal: Principal,
                private router: Router,
                private route: ActivatedRoute,
                private adgroupService:AdgroupService,
                public cjtjNotifyService: CjtjNotifyService,
                private modalService: NgbModal,

               ) {
    }

    ngOnInit(): void {
         this.principal.identity(false).then((account) => {
            this.account = account;
         });
        this.route.queryParams.subscribe(
            (queryParams) => {
                this.campaignId = queryParams['campaignId'];
                this.campaignName = queryParams['campaignName'];
               if(null!=this.campaignId&&0!=this.campaignId) {
                   this.campaignShow=false
               }
               else {
                   this.campaignId=0
               }

            }
        );

        this.loadAllGroup(null, null,true,this.campaignId);

    }
    loadAllGroup(startTime: string, endTime: string ,syn: boolean,campaignId:any) {
        this.adgroupService.getAll(startTime, endTime,syn,campaignId).subscribe(
            (response) => {
                this.allGroup = response;
                this.query();

            }
        );
    }
    /**
     * 查询过滤
     */
    query() {
        this.changeGroupsChecked(this.allGroup, false);
        const filteredGroups = this.allGroup.filter((group: any) => this.queryParams.groupName === '' || group.info.groupName .includes(this.queryParams.groupName));
        this.filteredGroups = filteredGroups;
        this.setGroupsChecked(this.groups, false);
        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);
    }
    changeGroupsChecked(groups, checked) {
        if (groups) {
            for (const group of groups) {
                group.checked = checked;
            }
        }
    }

    setGroupsChecked(groups, checked) {
        for (const group of groups) {
            group.checked = checked;
        }
    }
    /**
     * 选择页
     * @param showData
     */
    pageChange(pageInfo) {
        this.groups = this.getShowData(pageInfo);
    }

    getShowData(pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;
        if (this.filteredGroups && this.filteredGroups.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.filteredGroups.length ? this.filteredGroups.length : pageNumber * pageSize;
            showDataSet = this.filteredGroups.slice(start, end);
        }
        return showDataSet;
    }
    clickSelectAll() {
        if (this.allSelect) {
            this.setGroupsChecked(this.filteredGroups, true);
        } else {
            this.setGroupsChecked(this.filteredGroups, false);
        }
    }

    /**
     * 日历选择处理
     * @param {DateSelectChange} newSelect
     */
    onDateSelectChange(newSelect: DateSelectChange) {
        if (newSelect.isRealTime) {
            this.isRealTime = true;
            this.startTime = newSelect.startTime;
            this.endTime = newSelect.endTime;
            this.loadAllGroup(this.startTime, this.endTime,true,this.campaignId);

        } else {
            this.isRealTime = false;
            this.startTime = newSelect.startTime;
            this.endTime = newSelect.endTime;
            this.loadAllGroup(this.startTime, this.endTime,true,this.campaignId);
        }
        this.paramsEventer.emit({
            startTime: this.startTime,
            endTime: this.endTime,
            isRealTime: this.isRealTime,
        });


    }

    /**
     * 列选择变化
     * @param {any[]} selectColumns
     */
    onColumnsSelectChange(selectColumns) {
        this.selectColumns = selectColumns;
    }

    batchDeleteGroups() {
        const groups = this.getSelectGroups();
        if (groups.length == 0) return this.cjtjNotifyService.openMessage('请选中要删除的单元');
        const delGroupModels = new Array<DelGroupModel>();
        for (const group of groups) {
            const delGroupModel = new DelGroupModel();
            delGroupModel.sick = group.info.sick;
            delGroupModel.campaignId = group.info.campaignId;
            delGroupModel.groupId = group.info.groupId;
            delGroupModels.push(delGroupModel);
        }
        this.adgroupService.delGroup(delGroupModels).subscribe(
            (response) => {
                if (response.success) {
                    this.loadAllGroup(this.startTime, this.endTime,true,this.campaignId);
                }else {

                    return this.cjtjNotifyService.openMessage('操作失败,投放状态单元不能删除');
                }
            }
        );
    }

    popoverCheck = () => {
        const selectIds = this.getSelectIds();
        if (selectIds.length === 0) {
            this.cjtjNotifyService.openMessage('请先选择要操作的推广组');
            return false;
        } else {
            return true;
        }

    }

    getSelectIds() {
        const selectIds = [];
        for (const group of this.filteredGroups) {
            if (group.checked) {
                selectIds.push(group.info.groupId);
            }
        }
        return selectIds;
    }

    getSelectGroups(){
        return this.filteredGroups.filter(group => group.checked);
    }

    stringToDate(dateStr: string): NgbDate {

        const arrayStartTime: string[] = dateStr.split("-");
        const date = NgbDate.from({
            year: parseInt(arrayStartTime[0]),
            month: parseInt(arrayStartTime[1]),
            day: parseInt(arrayStartTime[2])
        });
        return date;
    }

    updateGroupStatus(group:any,status:any)
    {
        group.info.status=status;
        const updateGroupStatusModel=new UpdateGroupModel();
        updateGroupStatusModel.nick = group.info.nick;
        updateGroupStatusModel.campaignId = group.info.campaignId;
        updateGroupStatusModel.groupId = group.info.groupId;
        updateGroupStatusModel.oldStatus = group.info.status;
        updateGroupStatusModel.newStatus=status;
        const updataStatus = new Array<UpdateGroupModel>();
        updataStatus.push(updateGroupStatusModel);
        this.adgroupService.updateGroupStatus(updataStatus).subscribe(
            (response) => {
                if (response.success) {
                    group.status = status;
                } else {
                    this.cjtjNotifyService.openMessage('更新单元状态失败');
                }

            }
        );
    }

    editGroupNameFn(groupName: string) {
        this.editGroupName = groupName;
    }

    submitUpdateGroupName(group: any) {
        const updateGroupName = new UpdateGroupModel();
        updateGroupName.nick = group.nick;
        updateGroupName.campaignId = group.campaignId;
        updateGroupName.groupId = group.groupId;
        updateGroupName.oldGroupName = group.groupName;
        updateGroupName.newGroupName = this.editGroupName;
        updateGroupName.operationType = OperationType.UPDATE_OPERATE_STATUS;
        updateGroupName.operationSource = OperationSource.MANUAL;
        updateGroupName.operationReason = "单元列表页单个修改单元名称";
        this.adgroupService.updateGroupName(updateGroupName).subscribe(
            (response) => {
                if (response.success) {
                    group.groupName = this.editGroupName;
                } else {
                    this.cjtjNotifyService.openMessage(response.msg);
                }
            }
        );
    }

    submitBatchUpdateGroupStatus() {
        const selectGroups = this.getSelectGroups();
        if (selectGroups) {
            const updataStatus = new Array<UpdateGroupModel>();
            for (const group of selectGroups) {
                const updateGroupStatusModel=new UpdateGroupModel();
                updateGroupStatusModel.nick = group.info.nick;
                updateGroupStatusModel.campaignId = group.info.campaignId;
                updateGroupStatusModel.groupId = group.info.groupId;
                updateGroupStatusModel.oldStatus = group.info.status;
                updateGroupStatusModel.newStatus=this.editGroupStatus;
                updataStatus.push(updateGroupStatusModel);
            }
            this.adgroupService.updateGroupStatus(updataStatus).subscribe(
                (response) => {
                    if (response.success) {
                        this.loadAllGroup(this.startTime ? this.startTime : null, this.endTime ? this.endTime : null,true,this.campaignId);
                    } else {
                        this.cjtjNotifyService.openMessage('更新单元状态失败');
                    }

                }
            );
        }
    }

    clickRadio(radioValue: any) {
        this.editGroupStatus = radioValue;
    }

}

