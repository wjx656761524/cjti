import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {Principal} from "app/shared/auth/principal.service";
import {CampaignService} from "app/shared/campaign/campaign.service";
import {AdgroupService} from "app/shared/adgroup/adgroup.service";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {EngineCreative} from "app/creative/add/engineCreative";



@Component({
    selector: 'cjti-add-creative',
    templateUrl: './add-creative.component.html',

})
export class AddCreativeComponent implements OnInit {

    account: any; // 当前账户
    allCampaign:any[] = []; //接受数据

    filteredGroups: any[] = []; // 单元过滤后的数据
    queryParams: any = { // 过滤参数
        campaignName: '', // 标题

    }; // 过滤参数
    allGroup: any[] = [];
    allCampaigns: any[] = []; // 绑定单元数据de所有计划列表
    filteredCampaigns: any[] = []; // 计划过滤后的数据

    @Output() onShowPage = new EventEmitter<number>();
    @Output() private childOuter = new EventEmitter();

    constructor(
        public principal: Principal,
        private campaignService: CampaignService,
        private adgroupService: AdgroupService,
        private cjtjNotifyService: CjtjNotifyService,
        public activeModal: NgbActiveModal,
        public engineCreative: EngineCreative
    ) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;

        });
        this.loadAllCampaignsAndgroups(null, null, true);




    }

    /**
     * 加载数据
     * @param startTime
     * @param endTime
     * @param syn
     */

    loadAllCampaignsAndgroups(startTime?: string, endTime?: string, syn?: boolean) {


        this.campaignService.getAll(startTime, endTime, syn).subscribe(
            (response) => {
                this.allCampaign=response;
                this.bindGroupAndCampaign();

            });

        this.query();


    }



    /**
     * 将单元数据和计划数据进行合并
     */
    bindGroupAndCampaign() {

        for (const cam of this.allCampaign) {
            let obj = {
                campaignId: cam.campaignId,
                campaignName: cam.campaignName,
                adgroups: []=[]
            }
            obj.campaignId = cam.info.campaignId;
            obj.campaignName = cam.info.campaignName;
            this.adgroupService.getGroupbyId(null,null,true,cam.info.campaignId).subscribe(
                (response) => {
                    obj.adgroups=response;
                })

            this.allCampaigns.push(obj);
        }
    }


    /**
     * 下一步到创意页面
     * @param content
     */
    nextStep(): void{

        this.onShowPage.emit(2);


    }
    /**
     * 选取的数据绑定到engine
     */
    edit(group:any){
        if (null!=group)
        this.engineCreative.group=group.info;

    }

    /**
     * 查询过滤
     */
    query() {

        if (this.queryParams.campaignName=='') {
            this.filteredCampaigns = this.allCampaigns;
        }
        else{
            const filteredCampaigns: any[] = []; //
            if(null!=this.allCampaigns){
                for(const campaign of this.allCampaigns) {
                    let obj = {
                        campaignId: null,
                        campaignName: null,
                        adgroups: []=[]
                    }
                    if(null!=campaign.adgroups){
                        for(const group of campaign.adgroups){
                            if (group.info.groupName.includes(this.queryParams.campaignName)) {
                                obj.campaignId=campaign.campaignId
                                obj.campaignName=campaign.campaignName,
                                    obj.adgroups.push(group)
                            }
                        }

                    }
                    if(obj.campaignName!=null){
                        filteredCampaigns.push(obj)

                    }


                }
                this.filteredCampaigns= filteredCampaigns;
            }
        }
    }


}
