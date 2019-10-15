import {Component, EventEmitter, OnInit, ViewChild} from "@angular/core";
import {Principal} from "app/shared/auth/principal.service";
import {DateSelectChange} from "app/shared";
import {DEFAULT_COLUMNS, REPORT_FIELDS} from "app/report.constants";
import {PAGE_SIZE} from "app/app.constants";
import {CampaignService} from "app/shared/campaign/campaign.service";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {UpdateCampaignStatusModel} from "app/shared/campaign/update-campaign-statues";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {AddCampaignComponent} from "app/campaign/add/add-campaign.component";


@Component({
    selector: 'cjtj-campaign',
    templateUrl: './campaign.component.html'
})
export class CampaignComponent implements OnInit {

    paramsEventer: EventEmitter<any> = new EventEmitter();
    account: any; // 当前账户
    selectColumns = [...DEFAULT_COLUMNS];
    fields = REPORT_FIELDS;
    pageSize = PAGE_SIZE;
    campaign: any;
    page = 1;
    pageCampaigns: any; // 每页选择的计划列表
    filteredCampaigns: any[] = []; // 过滤出的计划列表
    allCampaigns: any; // 从后端拿到的所有计划列表
    allSelect = false;
    @ViewChild('pagination') pagination: any;
    queryParams: any = { // 过滤参数
        campaignName: '', // 计划标题
        status: 'all', // 推广状态
    };
    startTime = '';
    endTime = '';

    isBudgetHidePopover = true;
    editBudget: any;
    budget: any;
    itBudget: any;
    itget: any;
    campaigns: any[] = [];
    BudgetHidePopover: any;
    getHidePopover=false;
    getPopover: any;
    allpover: any;
    Popover = false;
    data1: any[];
    data2: any[];
    private directional: any;
    private qParams:  UpdateCampaignStatusModel = new UpdateCampaignStatusModel();
    private ucs: any;
    private campaignId: any;
    isRealTime: boolean;

    constructor(public principal: Principal,
                public cjtjNotifyService: CjtjNotifyService,
                public campaignService: CampaignService,
                private modalService: NgbModal,) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;
            this.loadAllCampaigns(null, null, true);
        });
    }

    loadAllCampaigns(startTime?: string, endTime?: string, syn?: boolean) {
        this.campaignService.getAll(startTime, endTime, syn).subscribe(
            (response) => {
                this.allCampaigns = response;
                this.query();
            }
        );
    }
    allpovers(){
        this.Popover = true;
    }
    /**
     * 查询过滤
     */
    query() {
        this.changeCampaignsChecked(this.allCampaigns, false);
        const filteredCampaigns = this.allCampaigns
            .filter((campaign: any) => this.queryParams.campaignName === '' || campaign.info.campaignName.includes(this.queryParams.campaignName))
            .filter((campaign: any) => this.queryParams.status === 'valid' ? campaign.info.status != 'erminate' : this.queryParams.status === 'all' || campaign.info.status === this.queryParams.status);
        this.filteredCampaigns = filteredCampaigns;

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

    setCampaignsChecked(campaigns, checked) {
        for (let campaign of campaigns) {
            campaign.checked = checked;
        }
    }

    changeCreativesChecked(campaigns, checked) {
        if (campaigns) {
            for (const campaign of campaigns) {
                campaign.checked =checked;
            }
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
            this. loadAllCampaigns(this.startTime, this.endTime,true);


        } else {
            this.isRealTime = false;
            this.startTime = newSelect.startTime;
            this.endTime = newSelect.endTime;
            this. loadAllCampaigns(this.startTime, this.endTime,true);

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
        this.pageCampaigns = this.getShowData(pageInfo);
    }

    getShowData(pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;
        if (this.filteredCampaigns && this.filteredCampaigns.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.filteredCampaigns.length ? this.filteredCampaigns.length : pageNumber * pageSize;
            showDataSet = this.filteredCampaigns.slice(start, end);
        }
        return showDataSet;
    }

    statusChange(event: any) {
        this.queryParams.status = event.target.value;
        this.query();
    }

    clickSelectAll() {
        if (this.allSelect) {
            this.setCampaignsChecked(this.filteredCampaigns, true);
        } else {
            this.setCampaignsChecked(this.filteredCampaigns, false);
        }
    }

    clickSelect(data: any){
        if(data.checked == true){
            data.checked = false;
        }else{
            data.checked = true;
        }
    }




    /**
     * 添加计划
     */
    addCampaign() {
        const modalRef = this.modalService.open(AddCampaignComponent, {size: 'lg', windowClass: 'modal-large1-window'});
        modalRef.componentInstance.allCampaigns = this.allCampaigns;
        modalRef.result.then((value) => {
            if (value == true) {
                this.loadAllCampaigns(this.startTime, this.endTime, true);
            }
            else{
                this.cjtjNotifyService.openMessage('添加失败,网络故障')
            }});

    }


    /**
     * 打开编辑计划信息窗口
     * @param campaign
     */
    EditBudget(campaign) {
        this.BudgetHidePopover = true;
        this.itBudget = campaign;
    }



    updateChangeStatus(campaign: any, status: any) {
        campaign.info.status = status;
        const updateCampaignStatusModel = new UpdateCampaignStatusModel();

        updateCampaignStatusModel.status = status;
        updateCampaignStatusModel.campaignId = campaign.info.campaignId;
        this.campaignService.updateCampaignStatus(updateCampaignStatusModel).subscribe(
            (response) => {
                if (response) {

                } else {
                    this.cjtjNotifyService.openMessage('更新单元状态失败');
                }

            }
        );
    }

    /**
     * 打开编辑器批量修改日预算窗口
     * @param campaign
     * @constructor
     */
    GetPopover() {
        this.getPopover = true;
    }

    /**
     * 打开编辑器批量修改状态窗口
     * @param campaign
     * @constructor
     */
    pover(campaign) {
        this.allpover = true;
        this.budget = campaign/100;
    }
    dayBudget(data){
        this.data1 = this.getSelectKeywords();
        for(const dat of this.data1){
            dat.status = data;
        }
    }
    /**
     * 修改计划信息
     * @param campaign
     */
    bitBudget() {
        if (this.data1!= null){
            for(const dat of this.data1){
                this.campaignService.updateBatchStatus(dat).subscribe(
                    (response) => {
                        if (response) {

                        } else {
                            this.cjtjNotifyService.openMessage('更新单元状态失败');
                        }

                    }
                );
        }

        }else {
            alert("请选择你要修改的数据");
        }
    }

    /**
     * 打开编辑器批量修改日预算窗口
     * @param campaign
     * @constructor
     */
    ddget(data){
        this.data2 = this.getSelectKeywords();
        this.qParams.dayBudget = this.budget * 100;
        for(const dat of this.data2){
            dat.dayBudget = data*100;
        }
    }


    dayBudgetdayBudget() {
        if (this.data2 != null) {
            for(const dat of this.data2){
                this.campaignService.updateBatchPrice(dat).subscribe(
                    (response) => {
                        if (response) {

                        } else {
                            this.cjtjNotifyService.openMessage('更新日限额失败');
                        }
                    }
                );
            }
        }else {
            alert("请选择你要修改的数据");
        }
    }


    getSelectKeywords(): any[] {
        const selectKeywords = [];
        for (const keyword of this.pageCampaigns) {
            if (keyword.checked) {
                selectKeywords.push(keyword.info);
            }
        }
        return selectKeywords;
    }


    getSelectIds() {
        const selectIds = [];
        for (const campaign of this.campaigns) {
            if (campaign.checked) {
                selectIds.push(campaign.info.campaign);
            }
        }
        return selectIds;
    }

    popoverCheck = () => {
        const selectIds = this.getSelectIds();
        if (selectIds.length === 0) {
            this.cjtjNotifyService.openMessage('');
            return false;
        } else {
            return true;
        }

    };



    /**
     * 打开编辑日预算窗口
     * @param campaign
     */
    openEditBudget(campaign) {
        this.isBudgetHidePopover = true;
        this.editBudget = campaign/100;
    }

    getCampaignRptdailylist = (Params: UpdateCampaignStatusModel)=>{
        this.campaignService.getCampaign(Params).subscribe((response) => {
            this.directional = response;
            this.allCampaigns = response;
            this.query();
        });
    };
    start: any;
    pause: any;
    terminate: any;
    bdget: any;

    /**
     * 修改出价
     * @param campaign
     */
    submitBudget(data) {
        this.qParams.campaignId = data.campaignId;
        this.qParams.dayBudget = this.editBudget * 100;
        this.campaignService.updatePrice(this.qParams).subscribe(res =>{
            this.allCampaigns = res;
            this.getCampaignRptdailylist(this.qParams);
            if(this.editBudget>=30){
                alert("修改成功")
            }else {
                alert("不能小于30元")
            }
        });
    }

}
