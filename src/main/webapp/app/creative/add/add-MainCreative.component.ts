import {Component, ComponentFactoryResolver, OnInit} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {Principal} from "app/shared/auth/principal.service";

import {EngineCreative} from "app/creative/add/engineCreative";
import {ItemService} from "app/shared/Item/item.service";
import {AdgroupService} from "app/shared/adgroup/adgroup.service";

@Component({
    selector: 'cjtj-add-mainCreative',
    templateUrl: './add-mainCreative.component.html',
})

export class AddMainCreativeComponent implements OnInit {
    account: any; // 当前账户
    item:any//商品的信息
    campaignId: any;
    showAddAdgroupPage: boolean =true;
    showAddCreativePage: boolean =false;
    adgroupClass: string = 'first current';
    locationClass: string = 'second nocurrent';
   groupId: number;
    titleValue:any='';//创意标题内容
    mainurl:any
    itemId;any

    constructor(private componentFactoryResolver: ComponentFactoryResolver,
                public activeModal: NgbActiveModal,
                private cjtjNotifyService: CjtjNotifyService,
                public principal: Principal,
                public engineCreative: EngineCreative,
                private adgroupService: AdgroupService,
                public itemService:ItemService) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;
        });
        if ( null!=this.campaignId&&0!=this.campaignId&&null!=this.groupId&&0!=this.groupId){
            this.showAddAdgroupPage = false;
            this.showAddCreativePage= true;
            this.engineCreative.campaignId=this.campaignId;
            this.engineCreative.groupId=this.groupId
            this.adgroupService.getAll(null,null,true,this.campaignId).subscribe(
                (response) => {
                    for(const group of response){
                        console.log(group.info.groupId)
                        if(group.info.groupId==this.groupId){
                            this.itemId=group.info.itemId
                            this.getAllCreatives();

                        }
                    }
                })

        }

    }

    onShowPage(value: number) {
        if(value==2){
            this.getAllCreatives();
            this.showAddAdgroupPage = false;
            this.showAddCreativePage= true;
        }
        if(value==1){
            this.showAddAdgroupPage = true;
            this.showAddCreativePage = false;

        }




    }

    onAddCreativeSuccess(value: any) {
        if (value) {
            this.activeModal.close(true);
        }
    }

    /**
     * 获取指定单元的商品信息
     *
     */
    getAllCreatives():void{
        this.itemService.getItemDetailsByItemId(this.itemId,true).subscribe(
            response=>{
                this.item=response;
                this.mainurl=this.item.picUrl;
                this.titleValue=this.item.title;
                if(!response){
                    this.cjtjNotifyService.openMessage('请选择单元');
                    this.showAddAdgroupPage = true;
                    this.showAddCreativePage = false;
                }
            }


        )


    }

}
