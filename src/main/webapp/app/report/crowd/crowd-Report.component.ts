
import {CrowdQuery} from "app/crowd/serivce/crowd.service";
import {DateSelectChange} from "app/shared";
import {Principal} from "app/shared/auth/principal.service";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {HttpClient} from "@angular/common/http";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {ActivatedRoute, Router} from "@angular/router";
import {CrowdReportService} from "app/shared/crowd/crowd.service";
import any = jasmine.any;
import {Component, OnInit, ViewChild} from "@angular/core";
import {DEFAULT_COLUMNS, REPORT_FIELDS} from "app/report.constants";
@Component({
    selector: 'cjtj-crowdReport',
    templateUrl: './crowd-Report.component.html'
})
export class CrowdReportComponent implements OnInit{
    @ViewChild('pagination') pagination: any;
    page = 1;
    pageSize = 10;
    campaignShow = true;
    groupShow = true;
    show = false;
    allOrient:any;
    allOrientReport:any;
    startTime: string;
    endTime: string;
    groupName = '';
    filteredOrients:any[] = []; // 过滤后的数据

    queryParams: CrowdQuery = {
        startTime: '',
        campaignId: null,
        crowdId: null,
        endTime: '',
        itemId: null
    }
    queryParamss: any = { // 过滤参数
        adgroupName: '', // 标题
    };
     account: any;
     campaignId: any;
     adgroupId: any;
     units: any[];
    fields = REPORT_FIELDS;
    selectColumns = [...DEFAULT_COLUMNS];
     crowds: any;
    constructor(private http: HttpClient,
                public principal: Principal,
                public cjtjNotifyService: CjtjNotifyService,
                private crowdReportService: CrowdReportService,
                private modalService: NgbModal,
                private router: Router,
                private route: ActivatedRoute,){}

    ngOnInit():void {
        this.principal.identity(false).then((account) => {
            this.account = account;
        });

        this.route.queryParams.subscribe(
            (queryParams) => {
                this.campaignId = queryParams['campaignId'];
                this.adgroupId=queryParams['adgroupId'];
                if(this.campaignId!=null&&0!=this.campaignId){
                    this.campaignShow=false;
                }
                else this.campaignId=0;

                if(this.adgroupId!=null&&0!=this.adgroupId){
                    this.groupShow=false
                }
                else this.adgroupId=0;

            }
        );
        this.loadAllLocatin(this.campaignId,this.adgroupId, null, null,true);

        this.loadAllCrowd(this.campaignId,this.adgroupId, null,null,true);

    }
    loadAllLocatin(campaignId:number,adgroupId:number, startTime: string, endTime: string,syn:boolean ) {

        this.crowdReportService.getCrowdReport(campaignId,adgroupId,startTime,endTime).subscribe(
            response=>{
                this.allOrient = response.list;
                this.query();
            }
        )

    }


    loadAllCrowd(campaignId:number,adgroupId:number, startTime: string, endTime: string, syn: boolean) {
        this.crowdReportService.getCrowdlist(campaignId,adgroupId,startTime,endTime).subscribe(
            response=>{
                this.allOrientReport = response.abstractReport;
                console.log(11111111111111);
                console.log(11111111111111);
                console.log(this.allOrientReport);
                this.query();
            }
        )
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
        this.units = this.getShowData(pageInfo);
    }
    getShowData(pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;
        if (this.filteredOrients && this.filteredOrients.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.filteredOrients.length ? this.filteredOrients.length : pageNumber * pageSize;
            showDataSet = this.filteredOrients.slice(start, end);
        }
        return showDataSet;
    }

    /**
     * 日历选择处理
     * @param {DateSelectChange} newSelect
     */
    onDateSelectChange(newSelect: DateSelectChange) {
        this.startTime = newSelect.startTime;
        this.endTime = newSelect.endTime;
        this.loadAllLocatin(this.campaignId,this.adgroupId,this.startTime, this.endTime, true);
        this.loadAllCrowd(this.campaignId,this.adgroupId,this.startTime, this.endTime, true);
    }

    /**
     * 查询过滤
     */
    query() {
        this.changeCreativesChecked(this.crowds, false);
        const filteredUnits =
            this.allOrient.filter((unit: any) =>
                this.queryParamss.adgroupName === '' || unit.info.adgroupName.includes(this.queryParamss.adgroupName));


        this.filteredOrients = filteredUnits;

        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);
    }

    changeCreativesChecked(crowds, checked) {
        if (crowds) {
            for (const crowd of crowds) {
                crowd.checked = checked;
            }
        }
    }
}
