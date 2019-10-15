import {Component, EventEmitter, OnInit, ViewChild} from "@angular/core";
import {Principal} from "app/shared/auth/principal.service";
import {DateSelectChange} from "app/shared";
import {DEFAULT_COLUMNS, OPTION_RESULTS, REPORT_FIELDS} from "app/report.constants";
import {PAGE_SIZE} from "app/app.constants";
import {HttpClient} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {CreativeService} from "app/shared/creative/creative.service";
import {DelGroupModel} from "app/shared/adgroup/del-group.model";
import {CreativeModel} from "app/shared/creative/creative.model";
import {AddMainComponent} from "app/location/add/add-main.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddMainCreativeComponent} from "app/creative/add/add-MainCreative.component";
@Component({
    selector: 'cjti-creative',
    templateUrl: './creative.component.html',

})
export class CreativeComponent implements OnInit {
    account: any; // 当前账户
    fields = REPORT_FIELDS;
    allSelect = false;
    options= OPTION_RESULTS;
    selectColumns = [...DEFAULT_COLUMNS];
    queryParams: any = { // 过滤参数
        creativeName:'', // 创意名称
        auditStatus:'',//审核状态
    }; // 过滤参数
    pageSize = PAGE_SIZE;
    page = 1;
    creatives: any[] = [];
    startTime: string;
    campaignId:any=0;
    groupId:any=0;
    campaignShow=true;
    groupShow=true;
    endTime: string;
    isRealTime: boolean;
    path: any;
    allCreative:any;
    filterCreatives:any[] = []; // 过滤后的数据
    creative:any;
    paramsEventer: EventEmitter<any> = new EventEmitter();
    info:any;
    optionValue:string
    campaignName ='';
    groupName = '';
    show = false;
    @ViewChild('pagination') pagination: any;


    constructor(private http: HttpClient,
                public principal: Principal,
                private route: ActivatedRoute,
                private modalService: NgbModal,
                public cjtjNotifyService: CjtjNotifyService,
                public creativeService:CreativeService
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
        this. loadCreativeByGroupId(this.campaignId,this.groupId,null,null,true);
    }

    loadCreativeByGroupId(campaignId:number,groupId:number, startTime: string, endTime: string,syn:boolean ) {

        this.creativeService.getCreativesBygroupId(campaignId,groupId,startTime,endTime,syn).subscribe(
            response=>{
                this.allCreative= response;
                this.query();
            }
        )



    }
    /**
     * 查询过滤
     */
    query() {
        this.changeCreativesChecked(this.allCreative, false);

        const filteredCreatives = this.allCreative.filter((creative: any) => this.queryParams.creativeName === '' || creative.info.creativeName.includes(this.queryParams.creativeName));

        this.filterCreatives= filteredCreatives;

        this.setCreativesChecked(this.creatives, false);
        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);
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
            this. loadCreativeByGroupId(this.campaignId,this.groupId, this.startTime, this.endTime,true);


        } else {
            this.isRealTime = false;
            this.startTime = newSelect.startTime;
            this.endTime = newSelect.endTime;
            this. loadCreativeByGroupId(this.campaignId,this.groupId, this.startTime, this.endTime,true);

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
    /**
     * 选择页
     * @param showData
     */
    pageChange(pageInfo) {
        this.creatives = this.getShowData(pageInfo);
    }
    getShowData(pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;

        if (this. filterCreatives && this.filterCreatives.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.filterCreatives.length ? this. filterCreatives.length : pageNumber * pageSize;
            showDataSet = this.filterCreatives.slice(start, end);
        }

        return showDataSet;
    }
    clickSelectAll() {
        if (this.allSelect) {
            this.setCreativesChecked(this.creatives, true);
        } else {
            this.setCreativesChecked(this.creatives, false);
        }
    }
    changeCreativesChecked(creatives, checked) {
        if (creatives) {
            for (const creative of creatives) {
                creative.checked = checked;
            }
        }
    }

    setCreativesChecked(creatives, checked) {
        for (const creative of creatives) {
            creative.checked = checked;
        }
    }
    onchange(auditStatus: any){

        this.changeCreativesChecked(this.allCreative, false);

        const filteredCreatives = this.allCreative.filter((creative: any) => auditStatus === 'A' || creative.info.auditStatus.includes(auditStatus));

        this.filterCreatives= filteredCreatives;

        this.setCreativesChecked(this.creatives, false);
        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);

    }
    popoverCheck = () => {
        const selectIds = this.getSelectIds();
        if (selectIds.length === 0) {
            this.cjtjNotifyService.openMessage('请先选择要操作的创意');
            return false;
        } else {
            return true;
        }

    }
    getSelectIds() {
        const selectIds = [];
        for (const creative of this.creatives) {
            if (creative.checked) {
                selectIds.push(creative.info.groupId);
            }
        }
        return selectIds;
    }
    batchDeleteCreatives() {
        const creatives = this.getSelectCreatives();
        if (creatives.length == 0) return this.cjtjNotifyService.openMessage('请选中要删除的创意');
        const creativeModels = new Array<CreativeModel>();
        for (const creative of creatives) {
            const creativeModel = new CreativeModel();
            creativeModel.sick =  creative.info.sick;
            creativeModel.campaignId =  creative.info.campaignId;
            creativeModel.groupId =  creative.info.groupId;
            creativeModel.creativeId= creative.info.creativeId;
            creativeModels.push(creativeModel);
        }
        this.creativeService.delCreative(creativeModels).subscribe(
            (response) => {
                if (response.success) {
                    this.loadCreativeByGroupId(1,1,null,null,false);
                }else {

                    return this.cjtjNotifyService.openMessage('操作失败');
                }
            }
        );
    }
    getSelectCreatives(){
        return this.creatives.filter(creative => creative.checked);
    }
    /**
     * 添加资源位
     */
    addCreative() {
        const modalRef = this.modalService.open(AddMainCreativeComponent, {size: 'lg', windowClass: 'modal-large1-window'});
        modalRef.componentInstance.campaignId = this.campaignId;
        modalRef.componentInstance.groupId=this.groupId
        modalRef.result.then((value) => {
            if (value == true) {

                this.loadCreativeByGroupId(this.campaignId,this.groupId,null,null,true);
            }
        });
    }
}



