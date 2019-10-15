import {Component, EventEmitter, OnInit, ViewChild} from '@angular/core';
import {Principal} from "app/shared/auth/principal.service";
import { REPORT_FIELDS,DEFAULT_COLUMNS} from 'app/report.constants';
import {HttpClient} from '@angular/common/http';
import {DateSelectChange} from "app/shared";
import {LocationService} from "app/shared/location/location.service";
import {PAGE_SIZE} from "app/app.constants";
import {LocationModel} from "app/shared/location/location.model";
import {CjtjNotifyService} from "app/shared/notify/notify.service";

import {DelLocationModel} from "app/shared/location/del-location.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

import {ActivatedRoute, Router} from "@angular/router";




@Component({
    selector: 'cjti-locationReport',
    templateUrl: './location-Report.component.html',


})
export class LocationReportComponent implements OnInit {

    account: any; // 当前账户
    fields = REPORT_FIELDS;
    allSelect = false;
    selectColumns = [...DEFAULT_COLUMNS];
    pageSize = PAGE_SIZE;
    page = 1;

    locations: any;
    startTime: string;
    endTime: string;
    campaignId:any=0;
    groupId:any=0;
    campaignShow=true;
    groupShow=true;
    isRealTime: boolean;
    queryParams: any = { // 过滤参数
        adzoneName: '', // 标题
    }; // 过滤参数
    path: any;
    campaign: any;
    group: any;
    allLocation:any;
    allLocationReport:any;
    filterLocations:any[] = []; // 过滤后的数据
    location:any;
    paramsEventer: EventEmitter<any> = new EventEmitter();
    @ViewChild('pagination') pagination: any;
    updateLocation = {
        premiumRatio: 1,
        targetPriceMin: 1.1,
        targetPriceMax: 1.1 * 1.2,
    };


    constructor(private http: HttpClient,
                public principal: Principal,
                public cjtjNotifyService: CjtjNotifyService,
                private locationService:LocationService,
                private modalService: NgbModal,
                private router: Router,
                private route: ActivatedRoute,
    ) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;


        });
        this.route.queryParams.subscribe(
            (queryParams) => {
                this.campaignId = queryParams['campaignId'];
                this.groupId=queryParams['groupId'];
                if(this.campaignId!=null&&0!=this.campaignId){
                    this.campaignShow=false;
                }
                else this.campaignId=0;

                if(this.groupId!=null&&0!=this.groupId){
                    this.groupShow=false
                }
                else this.groupId=0;

            }
        );

        this.loadAllLocatin(this.campaignId,this.groupId, null, null,true);

    }
    loadAllLocatin(campaignId:number,groupId:number, startTime: string, endTime: string,syn:boolean ) {

        this.locationService.getAllReport(campaignId,groupId,startTime,endTime,syn).subscribe(
            response=>{
                this.allLocation = response.results;
                this.allLocationReport = response.allLocationReport;
                this.query();
            }
        )



    }
    /**
     * 查询过滤
     */
    query() {

        const filteredLocations = this.allLocation.filter((location: any) => this.queryParams.adzoneName === '' || location.info.adzoneName.includes(this.queryParams.adzoneName));
        this.filterLocations= filteredLocations;

        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);
    }


    /**
     * 选择页
     * @param showData
     */
    pageChange(pageInfo) {
        this.locations = this.getShowData(pageInfo);
    }
    getShowData(pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;

        if (this. filterLocations && this. filterLocations.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.filterLocations.length ? this. filterLocations.length : pageNumber * pageSize;
            showDataSet = this.filterLocations.slice(start, end);
        }

        return showDataSet;
    }
    /**
     * 列选择变化
     * @param {any[]} selectColumns
     */
    onColumnsSelectChange(selectColumns) {
        this.selectColumns = selectColumns;
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
            this.loadAllLocatin(this.campaignId,this.groupId,this.startTime, this.endTime, true);


        } else {
            this.isRealTime = false;
            this.startTime = newSelect.startTime;
            this.endTime = newSelect.endTime;
            this.loadAllLocatin(this.campaignId,this.groupId,this.startTime, this.endTime, true);

        }
        this.paramsEventer.emit({
            startTime: this.startTime,
            endTime: this.endTime,
            isRealTime: this.isRealTime,
        });


    }




}

