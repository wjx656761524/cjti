import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {Principal} from "app/shared/auth/principal.service";
import {CampaignService} from "app/shared/campaign/campaign.service";
import {AdgroupService} from "app/shared/adgroup/adgroup.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {Engine} from "app/location/add/engine";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {EngineGroup} from "app/group/add/engineGroup";



@Component({
    selector: 'cjti-add-selcampaign',
    templateUrl: './add-selcampaign.component.html',

})
export class AddSelcampaignComponent implements OnInit {

    account: any; // 当前账户
    allCampaigns:any;//后端获取的所有计划
    queryParams: any = { // 过滤参数
        campaignName: '', // 计划标题
        status: 'all', // 推广状态
    };
    filteredCampaigns: any; // 过滤出的计划列表

    @Output() onShowPage = new EventEmitter<number>();

    constructor(
        public principal: Principal,
        private campaignService: CampaignService,
        private adgroupService: AdgroupService,
        private cjtjNotifyService: CjtjNotifyService,
        public activeModal: NgbActiveModal,
        public engineGroup: EngineGroup
    ) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;

        });

        this.loadAllCampaigns(null, null, true);


    }

    /**
     * 加载数据
     * @param startTime
     * @param endTime
     * @param syn
     */

    loadAllCampaigns(startTime?: string, endTime?: string, syn?: boolean) {
        this.campaignService.getAll(startTime, endTime, syn).subscribe(
            (response) => {
                console.log(1111111111111);
                console.log(response)
                this.allCampaigns = response;
                this.query();
            }
        );
    }


    /**
     * 查询过滤
     */
    query() {
        const filteredCampaigns = this.allCampaigns
            .filter((campaign: any) => this.queryParams.campaignName === '' || campaign.info.campaignName.includes(this.queryParams.campaignName))
            .filter((campaign: any) => this.queryParams.status === 'valid' ? campaign.info.status != 'erminate' : this.queryParams.status === 'all' || campaign.info.status === this.queryParams.status);
        this.filteredCampaigns = filteredCampaigns;


    }



    /**
     * 选取的数据绑定到engine
     */
    edit(campaign:any){
        this.engineGroup.campaign=campaign.info;
    }

    /**
     * 下一步到添加计划的主页面
     * @param content
     */
    nextStep(): void{
        if (this.engineGroup.campaignId==null){
            this.cjtjNotifyService.openMessage("请先选择计划")
        }
       else {this.onShowPage.emit(2);}
//         this.adgroupService.addAdgroup(this.updateAdgroup.name, this.campaignId, this.updateAdgroup.newAreaId,
//         this.updateAdgroup.recommendFee, this.updateAdgroup.searchFee, this.updateAdgroup.mobilePriceCoef, this.updateAdgroup.autoOptimizeStatus,
//         this.updateAdgroup.pcMaxLimitPrice, this.updateAdgroup.mobileMaxLimitPirce).subscribe(
//         (response) => {
//         if (response.success) {
//         this.onShowPage.emit(2);
//     } else {
//       this.cjtjNotifyService.openMessage(response.msg);
// }

    }


}
