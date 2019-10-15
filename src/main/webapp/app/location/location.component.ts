import {Component, EventEmitter, OnInit, ViewChild} from '@angular/core';
import {Principal} from "app/shared/auth/principal.service";
import { REPORT_FIELDS,DEFAULT_COLUMNS} from 'app/report.constants';
import {HttpClient} from '@angular/common/http';
import {DateSelectChange} from "app/shared";
import {LocationService} from "app/shared/location/location.service";
import {PAGE_SIZE} from "app/app.constants";
import {LocationModel} from "app/shared/location/location.model";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {DelGroupModel} from "app/shared/adgroup/del-group.model";
import {DelLocationModel} from "app/shared/location/del-location.model";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddLocationComponent} from "app/location/add/add-location.component";
import {Engine} from "app/location/add/engine";
import {AddMainComponent} from "app/location/add/add-main.component";
import {ActivatedRoute, Router} from "@angular/router";




@Component({
    selector: 'cjti-location',
    templateUrl: './location.component.html',


})
export class LocationComponent implements OnInit {

    account: any; // 当前账户
    fields = REPORT_FIELDS;
    allSelect = false;
    selectColumns = [...DEFAULT_COLUMNS];
    pageSize = PAGE_SIZE;
    page = 1;

    locations: any[] = [];
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
    filterLocations:any[] = []; // 过滤后的数据
    location:any;
    paramsEventer: EventEmitter<any> = new EventEmitter();
    campaignName ='';
    groupName = '';
    show = false;
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

                this.campaignName=queryParams['campaignName'];
                this.groupName=queryParams['groupName'];
                if(this.groupName != undefined){
                    this.show = true;
                }else if(this.campaignId != undefined){
                    this.show = true;
                }
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

        this.locationService.getLocations(campaignId,groupId,startTime,endTime,syn).subscribe(
            response=>{
                this.allLocation = response;
                this.query();
            }
        )



    }
    /**
     * 查询过滤
     */
    query() {
        this.changeLocationsChecked(this.allLocation, false);
        const filteredLocations = this.allLocation.filter((location: any) => this.queryParams.adzoneName === '' || location.info.adzoneName.includes(this.queryParams.adzoneName));
        this.filterLocations= filteredLocations;

        this.setLocationsChecked(this.locations, false);
        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);
    }
    changeLocationsChecked(locations, checked) {
        if (locations) {
            for (const location of locations) {
                location.checked = checked;
            }
        }
    }

    setLocationsChecked(locations, checked) {
        for (const location of locations) {
            if(location.info.adzoneId == -99){
                location.checked = false;
            }else{
                location.checked = checked;
            }
        }
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
    clickSelectAll() {
        if (this.allSelect) {
            this.setLocationsChecked(this.locations, true);
        } else {
            this.setLocationsChecked(this.locations, false);
        }
    }
    updatePopover(slocation) {
        if(slocation) {
            this.updateLocation = slocation;
            this.updateLocation.premiumRatio= slocation.discount ;
        }

    }
    // 修该资源位
    updateLocations(slocation) {
        const locationModel = new LocationModel();
        const  locationModels=new Array<LocationModel>();
        locationModel.campaignId = slocation.campaignId;
        locationModel.groupId = slocation.groupId;
        locationModel.adzoneId = slocation.adzoneId;
        locationModel.discount=this.updateLocation.premiumRatio,
            locationModels.push( locationModel)
       this.locationService.updateLocations(locationModels).subscribe(
            (response) => {
                this.cjtjNotifyService.openMessage(response.success?'修改资源位溢价比例成功':'修改资源位溢价比例失败');
                this.loadAllLocatin(this.campaignId,this.groupId,null, null, true);
                if(response.success) {
                    slocation.discount = this.updateLocation.premiumRatio;
                    console.log(slocation.discount);
                }
            });
    }
    batchDeleteLocations() {
        const locations = this.getSelectLocations();
        if (locations.length == 0) return this.cjtjNotifyService.openMessage('请选中要删除的单元');
        const delLocationModels = new Array<DelLocationModel>();
        for (const location of locations) {
            const delLocationModel = new DelLocationModel();
            delLocationModel.sick = location.info.sick;
            delLocationModel.adzoneId = location.info.adzoneId;
            delLocationModel.groupId = location.info.groupId;
            delLocationModels.push( delLocationModel);
        }
        this.locationService.delLocation(delLocationModels).subscribe(
            (response) => {
                if (response.success) {
                    this.loadAllLocatin(this.campaignId,this.groupId,null,null,true);
                }else {

                    return this.cjtjNotifyService.openMessage('操作失败,投放状态不能删除');
                }
            }
        );
    }
    popoverCheck = () => {
        const selectIds = this.getSelectIds();
        if (selectIds.length === 0) {
            this.cjtjNotifyService.openMessage('请先选择要操作的广告位');
            return false;
        } else {
            return true;
        }

    }
    getSelectIds() {
        const selectIds = [];
        for (const location of this.locations) {
            if (location.checked) {
                selectIds.push(location.info.adzoneId);
            }
        }
        return selectIds;
    }
    getSelectLocations(){
        return this.locations.filter(location => location.checked);
    }
    /**
     * 添加资源位
     */
    addLocation() {
        const modalRef = this.modalService.open(AddMainComponent , {size: 'lg', windowClass: 'modal-large1-window'});
        modalRef.componentInstance.campaignId = this.campaignId;
        modalRef.componentInstance.groupId=this.groupId
        modalRef.result.then((value) => {
            if (value == true) {
                this.loadAllLocatin(this.campaignId,this.groupId,null,null,true);

            }
        });
    }


}

