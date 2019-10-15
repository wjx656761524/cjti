import {Component, EventEmitter, Input, OnInit, ViewChild} from "@angular/core";
import {Principal} from "app/shared/auth/principal.service";
import {CampaignService} from "app/shared/campaign/campaign.service";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DateSelectChange} from "app/shared";
import {AddCampaignModel} from "app/shared/campaign/add-campaign.model";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import any = jasmine.any;


@Component({
    selector: 'cjtj-addCampaign',
    templateUrl: './add-campaign.component.html'
})
export class AddCampaignComponent implements OnInit {

    account: any; // 当前账户
    paramsEventer: EventEmitter<any> = new EventEmitter();
    campaignId:any; //计划Id
    dayBudget:any;//每日预算
    startTime:any='2019-05-30 00:00:00';//开始时间
    endTime:any='2019-06-30 00:00:00';//结束时间
    launchForever:any=false;//是否永久投放
    campaignName:any//计划名称
    allCampaigns:any;//已有的所有计划
    constructor(public principal: Principal,
                public cjtjNotifyService: CjtjNotifyService,
                public campaignService: CampaignService,
                private router: Router,
                private route: ActivatedRoute,
                public activeModal: NgbActiveModal) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;

        });
    }
//生成新的计划,并且跳转到添加单元页面
    nextStep(){
        if (this.campaignNameCheck()&&this. dayBudgetCheck()) {
            let campaign = new AddCampaignModel()
            campaign.campaignName = this.campaignName;
            campaign.dayBudget = this.dayBudget * 100;
            campaign.launchForever = this.launchForever;
            campaign.endTime = this.endTime;
            campaign.startTime = this.startTime;
            this.campaignService.addCampaign(campaign).subscribe(
                (response) => {
                    if ("" != response && null != response) {
                        this.router.navigate(['/group/addGroup'], {
                            queryParams: {
                                campaignId: response
                            }
                        });
                        this.activeModal.close(true)
                    } else {
                        this.activeModal.close(false)

                    }

                }
            )

        }
    }
    /**
     * 日历选择处理
     * @param {DateSelectChange} newSelect
     */
    onDateSelectChange(newSelect: DateSelectChange) {
        if (newSelect.isRealTime) {
            this.startTime = newSelect.startTime;
            this.endTime = newSelect.endTime;


        } else {
            this.startTime = newSelect.startTime;
            this.endTime = newSelect.endTime;

        }
        this.paramsEventer.emit({
            startTime: this.startTime,
            endTime: this.endTime,
        });


    }

    campaignNameCheck():boolean{
        if(''==this.campaignName||null==this.campaignName){
            this.cjtjNotifyService.openMessage('此计划名称不能为空');
            return false
        }
        for (const cam of  this.allCampaigns) {
            if (this.campaignName==cam.info.campaignName) {

                this.cjtjNotifyService.openMessage('此计划名称已存在');
                return false
            }
        }
        return true;

    }

    /**
     * 每日预算的检查
     */
    dayBudgetCheck(){
        if(''==this.dayBudget||null==this.dayBudget){
            this.cjtjNotifyService.openMessage('每日预算不得为空');
            return false
        }
        if(this.dayBudget<30){
            this.cjtjNotifyService.openMessage('每日预算不得低于30元');
            return false
        }
        return true
    }

    query(){}
}
