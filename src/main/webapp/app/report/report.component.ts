import {Component, EventEmitter, OnInit, ViewChild} from "@angular/core";
import {Principal} from "app/shared/auth/principal.service";
import {DateSelectChange} from "app/shared";
import {PAGE_SIZE} from "app/app.constants";
import {DEFAULT_COLUMNS, REPORT_FIELDS} from "app/report.constants";
import {HttpClient} from "@angular/common/http";
import {ReportService} from "app/shared/report/report.service";
import {ActivatedRoute} from "@angular/router";



@Component({
    selector: 'cjtj-report',
    templateUrl: './report.component.html'
})
export class ReportComponent implements OnInit {

    queryParams: any = { // 过滤参数
        campaignName: '', // 计划标题
        status: 'all', // 推广状态
    };
    allSelect = false;
    @ViewChild('pagination') pagination: any;
    allCampaigns: any; // 从后端拿到的所有计划列表
    account: any; // 当前账户
    pageCampaigns: any; // 每页选择的计划列表
    pageSize = PAGE_SIZE;
    selectColumns = [...DEFAULT_COLUMNS];
    fields = REPORT_FIELDS;
    allCampaignReport:any;
    campaign: any;
    isRealTime: boolean;
    startTime: string;
    endTime: string;
    campaignId:any=0;
    campaignShow=true;
    groupShow=true;
    report:any;
    campai:any;
    filtercampaign: any[] = []; // 过滤后的数据
    paramsEventer: EventEmitter<any> = new EventEmitter();
    page = 1;

    constructor(public principal: Principal,
                private http: HttpClient,
                private reportService:ReportService,
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

                if(this.campaignId!=null&&0!=this.campaignId){
                    this.campaignShow=false;
                }
                else {
                    this.campaignId=0;
                }
            }
        );

        this.reportLocatin(null, null,true);
    }

    reportLocatin(startTime: string, endTime: string,syn:boolean ) {

        this.reportService.getReport(startTime,endTime,syn).subscribe(
            response=>{
                this.campaign = response.k2;
                this.allCampaigns = response.k2;
                this.allCampaignReport = response.k1;
                this.query();
            }
        )
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
            this.reportLocatin(this.startTime, this.endTime, true);
        } else {
            this.isRealTime = false;
            this.startTime = newSelect.startTime;
            this.endTime = newSelect.endTime;
            this.reportLocatin(this.startTime, this.endTime, true);

        }
        this.paramsEventer.emit({
            startTime: this.startTime,
            endTime: this.endTime,
            isRealTime: this.isRealTime,
        });
    }

    /**
     * 查询过滤
     */
    query() {
        this.changeCampaignsChecked(this.allCampaigns, false);
        const filtercampaign = this.allCampaigns
            .filter((campaign: any) => this.queryParams.campaignName === '' || campaign.info.campaignName .includes(this.queryParams.campaignName));
            this.filtercampaign = filtercampaign;

        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);
    }

    changeCampaignsChecked(campaigns, checked) {
        if (campaigns) {
            for (const campaign of campaigns) {
                campaign.checked = checked;
            }
        }
    }

    /**
     * 选择页
     * @param showData
     */
    pageChange(pageInfo) {
        this.pageCampaigns = this.getShowData(pageInfo);
    }
    getShowData(pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;
        if (this.filtercampaign && this.filtercampaign.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.filtercampaign.length ? this.filtercampaign.length : pageNumber * pageSize;
            showDataSet = this.filtercampaign.slice(start, end);
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
}
