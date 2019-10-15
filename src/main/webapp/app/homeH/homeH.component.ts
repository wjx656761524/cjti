import {Component, EventEmitter, OnInit} from '@angular/core';
import {HttpService} from "app/crowd/serivce/http.service";
import {DateSelectChange} from "app/shared";
import {AddCampaignComponent} from "app/campaign/add/add-campaign.component";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
    selector: 'cjtj-homeH',
    templateUrl: './homeH.component.html',
})
export class HomeHComponent implements OnInit {
    rptdaily: any;
    startTime: string;
    endTime: string;

    charge = 0;
    adPv = 0;
    click= 0;
    ecpm = 0;
    ctr = 0;
    ecpc= 0;
    // 收藏宝贝量
    inshopItemColNum = 0;
    // 添加购物车数
    cartNum = 0;
    // 投资回报率
    roi = '0';
    // 成交订单金额
    alipayInshopAmt = 0;
    paramsEventer: EventEmitter<any> = new EventEmitter();
    isRealTime: boolean;
    allCampaigns: any;

    constructor(private httsrv: HttpService,private modalService: NgbModal,) {

    }

    ngOnInit() {
        this.findRptdaily(this.startTime,this.endTime);
    }

    findRptdaily = (startTime: string,endTime: string) => {
         this.httsrv.onHttpGet('/api/findRptdaily',{startTime: this.startTime,endTime: this.endTime}).subscribe((res) =>{
             this.rptdaily =   res;
             this.charge = 0;
             this.adPv = 0;
             this.click = 0;
             this.ecpm = 0;
             this.ctr = 0;
             this.ecpc = 0;
             for(let i =0;i<this.rptdaily.length;i++){
                 this.charge = this.rptdaily[i].report.charge + this.charge;
                 this.adPv = this.rptdaily[i].report.adPv + this.adPv;
                 this.click = this.rptdaily[i].report.click + this.click;
                 this.ecpm = this.rptdaily[i].report.ecpm + this.ecpm;
                 this.ctr = this.rptdaily[i].report.ctr + this.ctr;
                 this.ecpc = this.rptdaily[i].report.ecpc + this.ecpc;
                 this.inshopItemColNum = this.rptdaily[i].report.inshopItemColNum + this.inshopItemColNum;
                 this.cartNum = this.rptdaily[i].report.cartNum + this.cartNum;
                 this.alipayInshopAmt = this.rptdaily[i].report.alipayInshopAmt + this.alipayInshopAmt;
             }
             if (this.charge && this.charge != 0) {
                 this.roi = (this.alipayInshopAmt / this.charge).toFixed(2);
             }
         });
    }

    /**
     * 日历选择处理
     * @param {DateSelectChange} newSelect
     */
    onDateSelectChange(newSelect: DateSelectChange) {
        this.startTime = newSelect.startTime;
        this.endTime = newSelect.endTime;
        this.isRealTime = newSelect.isRealTime? true: false;
        this.findRptdaily(this.startTime,this.endTime);
        this.paramsEventer.emit({
            startTime: this.startTime,
            endTime: this.endTime,
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
