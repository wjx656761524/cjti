import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Principal} from "app/shared/auth/principal.service";
import {CampaignService} from "app/shared/campaign/campaign.service";
import {NgbActiveModal, NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {EngineGroup} from "app/group/add/engineGroup";
import {AddThreeCreativeSecondComponent} from "app/group/add/addThree/addThree-creativeSecond.component";
import {CreativeService} from "app/shared/creative/creative.service";
import {CreativeModel} from "app/shared/creative/creative.model";
import {AdgroupStatus} from "app/shared/adgroup/adgroup.model";
import {Options} from "app/group/add/CrowdList";
import {Router} from "@angular/router";



@Component({
    selector: 'cjti-addThree-creative',
    templateUrl: './addThree-creative.component.html',

})
export class AddThreeCreativeComponent implements OnInit {

    account: any; // 当前账户

    @Output() onShowPage = new EventEmitter<number>();


    constructor(
        public principal: Principal,
        private campaignService: CampaignService,
        private cjtjNotifyService: CjtjNotifyService,
        public activeModal: NgbActiveModal,
        public engineGroup: EngineGroup,
        private modalService: NgbModal,
        private creativeService :CreativeService,
        private router: Router
    ) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;

        });

        this.productCreative()

    }

    /**
     * 生成创意
     */
productCreative(){

    console.log(this.engineGroup.items);
    for(const item of this.engineGroup.items){
        for(const image of (item.itemImgs)){
            let creatve={
                itemId:item.id,
                creativeName:item.title,
                creativePic:image.url,
                imageId:image.id

            }
            console.log(111111111);
            console.log(creatve);
            this.engineGroup.creatives.push(creatve)
        }
    }

    }
    /**
     * 跳转到创意页面
     */

    addCreative(itemId,url,id) {
        const modalRef = this.modalService.open(AddThreeCreativeSecondComponent, {size: 'lg', windowClass: 'modal-large1-window'});
        modalRef.componentInstance.itemId = itemId;
        modalRef.componentInstance.mark = url;
        modalRef.componentInstance.imageId = id;
    }

    /**
     * 上一步到添加计划的主页面
     * @param content
     */
    pretStep(): void{
        this.onShowPage.emit(2);
        this.engineGroup.directionalUnit = [];
    }

    /**
     * 确认完成
     */
    end(){
        const ceativeModel = new Array<CreativeModel>();
        for (let j=0;j< this.engineGroup.groups.length;j++ ) {
            for(let i=1; i<this.engineGroup.items[j].itemImgs.length;i++){
                const creative = new CreativeModel();
                creative.creativeName =  this.engineGroup.items[j].creativeName;
                creative.imgUrl =this.engineGroup.items[j].itemImgs[i].url;
                creative.groupId = this.engineGroup.groups[j];
                ceativeModel.push(creative)
            }
        }
        this.creativeService.updateCreative(ceativeModel).subscribe(res =>{
            this.router.navigate(['/campaign'], {

            });
        });
    }
}
