import {Component, EventEmitter, OnInit, ViewChild} from '@angular/core';
import {Principal} from "app/shared/auth/principal.service";
import { REPORT_FIELDS,DEFAULT_COLUMNS} from 'app/report.constants';
import {ITEMS_PER_PAGE} from 'app/shared/constants/pagination.constants';
import {NgbCalendar, NgbDate, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {AdgroupService} from "app/shared/adgroup/adgroup.service";
import {PAGE_SIZE} from "app/app.constants";
import {DateSelectChange} from "app/shared";
import{CjtjNotifyService} from "app/shared/notify/notify.service";
import any = jasmine.any;
import {UpdateGroupModel} from "app/shared/adgroup/update-group";



@Component({
    selector: 'cjti-adGroupReport',
    templateUrl: './adGroup-Report.component.html',

})
export class AdGroupReportComponent implements OnInit {

    account: any; // 当前账户
    fields = REPORT_FIELDS;
    selectColumns = [...DEFAULT_COLUMNS];
    test:any;
    campaign: any = {}; // 计划信息
    group: any;
    allSelect = false;
    campaignId:any=0;
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
    allCreativeeport:any

    @ViewChild('pagination') pagination: any;


    constructor(private http: HttpClient,
                public principal: Principal,
                private router: Router,
                private route: ActivatedRoute,
                private adgroupService:AdgroupService,
                public cjtjNotifyService: CjtjNotifyService,


    ) {
    }
    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;
        });
        this.route.queryParams.subscribe(
            (queryParams) => {
                this.campaignId = queryParams['campaignId'];

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
        this.adgroupService.getAllReport(startTime, endTime,syn,campaignId).subscribe(
            (response) => {
                this.allGroup = response.results;
                this.allCreativeeport=response.allGroupReport;
                this.query();

            }
        );
    }
    /**
     * 查询过滤
     */
    query() {

        const filteredGroups = this.allGroup.filter((group: any) => this.queryParams.groupName === '' || group.info.groupName .includes(this.queryParams.groupName));
        this.filteredGroups = filteredGroups;
        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);
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
}

