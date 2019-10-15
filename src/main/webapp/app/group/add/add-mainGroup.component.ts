import {Component, ViewChild, ComponentFactoryResolver, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {CjtjNotifyService} from "../../shared/notify/notify.service";
import {Principal} from "app/shared/auth/principal.service";
import {LocationService} from "app/shared/location/location.service";
import {Engine} from "app/location/add/engine";
import {THIS_EXPR} from "@angular/compiler/src/output/output_ast";
import {EngineGroup} from "app/group/add/engineGroup";
import {WaterFallChartSeriesOptions} from "highcharts";
import {AdgroupService} from "app/shared/adgroup/adgroup.service";
import {AdgroupStatus} from "app/shared/adgroup/adgroup.model";
import {ActivatedRoute} from "@angular/router";
import {Options} from "app/group/add/CrowdList";


@Component({
    selector: 'cjtj-add-mainGroup',
    templateUrl: './add-mainGroup.component.html',
})

export class AddMainGroupComponent implements OnInit {
    account: any; // 当前账户

    campaignId: number;
    showSelCampaignPage: boolean = true;
    showAddSecondmainGroupPage: boolean = false;
    showAddThreemainGroupPage: boolean = false;


    constructor(private componentFactoryResolver: ComponentFactoryResolver,
                public activeModal: NgbActiveModal,
                private cjtjNotifyService: CjtjNotifyService,
                public principal: Principal,
                private route: ActivatedRoute,
                public locationService: LocationService,
                public engineGroup: EngineGroup,
                public adgroupService: AdgroupService) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;



        });

        this.route.queryParams.subscribe(
            (queryParams) => {
                this.campaignId = queryParams['campaignId'];

                if (null!= this.campaignId&& 0!=this.campaignId) {
                    this.showSelCampaignPage = false;
                    this.showAddSecondmainGroupPage = true;
                    this.showAddThreemainGroupPage = false
                   this.engineGroup.campaignId=this.campaignId
                }
            }
        );
    }

    onShowPage(value: number) {
        if (value == 2) {

            this.showSelCampaignPage = false;
            this.showAddSecondmainGroupPage = true;
            this.showAddThreemainGroupPage = false


        }
        if (value == 1) {
            this.showSelCampaignPage = true;
            this.showAddSecondmainGroupPage = false;
            this.showAddThreemainGroupPage = false

        }
        if (value == 3) {
            this.addgroup();
            this.showSelCampaignPage = false;
            this.showAddSecondmainGroupPage = false;
            this.showAddThreemainGroupPage = true

        }


    }

    onAddLocationSuccess(value: any) {
        if (value) {
            this.activeModal.close(true);
        }
    }

    /**
     * 添加单元
     */

    addgroup() {
        const adgroups = new Array<AdgroupStatus>()

        for (const item of this.engineGroup.items) {
            const group = new AdgroupStatus();
            group.groupName = item.unitName;
            group.itemId = item.id;
            group.locations = this.engineGroup.locations;
            group.campaignId = this.engineGroup.campaignId;
            group.directionalUnit = this.engineGroup.directionalUnit;
            for(let i =0;i<group.directionalUnit.length;i++){
                if(group.directionalUnit[i].directionalLabel.targetId == 521){
                    group.directionalUnit[i].directionalLabel.options = [];
                    const option = new Options();
                    option.optionValue = group.itemId.toString();
                    group.directionalUnit[i].directionalLabel.options.push(option);
                }
            }
            adgroups.push(group)
        }
        this.adgroupService.addGroup(adgroups).subscribe((response) => {
            if (response) {
                console.log(66666);
                console.log(response);
                for(let i=0;i<response.length;i++){
                    this.engineGroup.groupId = response[i];
                }
                return this.cjtjNotifyService.openMessage('操作成功');
            } else {
                return this.cjtjNotifyService.openMessage('操作失败');
            }
        })

    }
}
