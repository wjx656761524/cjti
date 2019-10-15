import {Component, EventEmitter, OnInit} from '@angular/core';
import {Principal} from "app/shared/auth/principal.service";
import {DateSelectChange} from "app/shared";
import {HttpService} from "app/crowd/serivce/http.service";
import {AccountService} from "app/shared/auth/account.service";
import {AddCampaignComponent} from "app/campaign/add/add-campaign.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CampaignService} from "app/shared/campaign/campaign.service";

@Component({
    selector: 'cjtj-home',
    templateUrl: './home.component.html',
})
export class HomeComponent implements OnInit {

    rptdailyOne: any;
    rptdailyTwo: any;
    logDateOne: string;
    logDateTwo: string;
    dataContrast = 'charge';

    chargeOne = 0;
    adPvOne = 0;
    clickOne= 0;
    ecpmOne= '0';
    ctrOne = 0;
    ecpcOne= 0;

    chargeTwo= 0;
    adPvTwo= 0;
    clickTwo= 0;
    ecpmTwo= '0';
    ctrTwo= 0;
    ecpcTwo = 0;
    paramsEventer: EventEmitter<any> = new EventEmitter();
    isRealTime: boolean;
    blance: string;
    allCampaigns: any;
    constructor(private principal: Principal,
                private httsrv: HttpService,
                private accountService: AccountService,
                private modalService: NgbModal,
                public campaignService: CampaignService,) {
    }

    ngOnInit() {
        this.principal.identity(false).then((account) => {
        });
        this.findRpthourlist(this.logDateOne,this.logDateTwo);
        this.accountService.getAccountBlance().subscribe(
            (res) => {
                this.blance = res;
            }
        );
        this.campaignService.getAll('', '', true).subscribe(
            (response) => {
                this.allCampaigns = response;
            }
        );
    }



    findRpthourlist = (logDateOne: string,logDateTwo: string) => {
        this.httsrv.onHttpGet('/api/findRpthourlist',{logDate: logDateOne}).subscribe((res) =>{
            this.rptdailyOne =  res;
            this.chargeOne = 0;
            this.adPvOne = 0;
            this.clickOne = 0;
            this.ctrOne = 0;
            this.ecpcOne = 0;
            this.ecpmOne = '0';
            for(let i =0;i<this.rptdailyOne.length;i++){
                this.chargeOne = this.rptdailyOne[i].report.charge + this.chargeOne;
                this.adPvOne = this.rptdailyOne[i].report.adPv + this.adPvOne;
                this.clickOne = this.rptdailyOne[i].report.click + this.clickOne;
                this.ctrOne = this.rptdailyOne[i].report.ctr + this.ctrOne;
                this.ecpcOne = this.rptdailyOne[i].report.ecpc +  this.ecpcOne;
            }
            if (this.adPvOne && this.adPvOne != 0) {
                this.ecpmOne = (this.chargeOne * 1000 / this.adPvOne).toFixed(2);
            }
        });

        this.httsrv.onHttpGet('/api/findRpthourlist',{logDate: logDateTwo}).subscribe((res) =>{
            this.rptdailyTwo =   res;
            this.chargeTwo = 0;
            this.adPvTwo = 0;
            this.clickTwo = 0;
            this.ctrTwo = 0;
            this.ecpcTwo = 0;
            this.ecpmTwo = '0';
            for(let i =0;i<this.rptdailyTwo.length;i++){
                this.chargeTwo = this.rptdailyTwo[i].report.charge + this.chargeTwo;
                this.adPvTwo = this.rptdailyTwo[i].report.adPv + this.adPvTwo;
                this.clickTwo = this.rptdailyTwo[i].report.click + this.clickTwo;
                this.ctrTwo = this.rptdailyTwo[i].report.ctr + this.ctrTwo;
                this.ecpcTwo = this.rptdailyTwo[i].report.ecpc + this.ecpcTwo;
            }
            if (this.adPvTwo && this.adPvTwo != 0) {
                this.ecpmTwo = (this.chargeTwo * 1000 / this.adPvTwo).toFixed(2);
            }

        });
    }


    /**
     * 日历选择处理
     * @param {DateSelectChange} newSelect
     */
    onDateSelectChange(newSelect: DateSelectChange) {
        this.logDateOne = newSelect.startTime;
        this.logDateTwo = newSelect.endTime;
        this.isRealTime = newSelect.isRealTime? true: false;
        this.findRpthourlist(this.logDateOne,this.logDateTwo);
        this.paramsEventer.emit({
            startTime: this.logDateOne,
            endTime: this.logDateTwo,
            isRealTime: this.isRealTime,
        });
    }


    /**
     * 添加计划
     */
    addCampaign() {
        const modalRef = this.modalService.open(AddCampaignComponent, {size: 'lg', windowClass: 'modal-large1-window'});
        modalRef.componentInstance.allCampaigns = this.allCampaigns;
    }
}
