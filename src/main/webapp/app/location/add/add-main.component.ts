import {Component, ViewChild, ComponentFactoryResolver, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {CjtjNotifyService} from "../../shared/notify/notify.service";
import {Principal} from "app/shared/auth/principal.service";
import {LocationService} from "app/shared/location/location.service";
import {Engine} from "app/location/add/engine";
import {THIS_EXPR} from "@angular/compiler/src/output/output_ast";


@Component({
    selector: 'cjtj-add-main',
    templateUrl: './add-main.component.html',
})

export class AddMainComponent implements OnInit {
    account: any; // 当前账户

    campaignId: number;
    showAddAdgroupPage: boolean = true;
    showAddLocationPage: boolean = false;
    adgroupClass: string = 'first current';
    locationClass: string = 'second nocurrent';
   groupId: number;
    data :any
    allLocations:any[]=[]//获取后端的数据
    locations:any[]=[];//进行检查后的数据
    count:any=0;//记录绑定的资源位
    constructor(private componentFactoryResolver: ComponentFactoryResolver,
                public activeModal: NgbActiveModal,
                private cjtjNotifyService: CjtjNotifyService,
                public principal: Principal,
                public locationService:LocationService,
                      public engine: Engine) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;
        });
        if ( null!=this.campaignId&&0!=this.campaignId&&null!=this.groupId&&0!=this.groupId){
            this.showAddAdgroupPage = false;
            this.showAddLocationPage = true;
            this.engine.campaignId=this.campaignId;
            this.engine.groupId=this.groupId
            this.getAllLoactions();

        }

    }

    onShowPage(value: number) {


         if(value==2){
             this.getAllLoactions();
        this.showAddAdgroupPage = false;
        this.showAddLocationPage = true;


     }
        if(value==1){
            this.showAddAdgroupPage = true;
            this.showAddLocationPage = false;

        }




    }

    onAddLocationSuccess(value: any) {
        if (value) {
            this.activeModal.close(true);
        }
    }
    getData(data:any)
    {
        this.data=data;

    }
    /**
     * 获取所有资源位
     *
     */
    getAllLoactions():void{
        this.locationService.getALLLocations(this.engine).subscribe(
            response=>{
                this.allLocations=response;
                this.query();
                if(!response){
                    this.cjtjNotifyService.openMessage('请选择单元');
                    this.showAddAdgroupPage = true;
                    this.showAddLocationPage = false;
                }
            }


        )


    }
    query(){
        for (const cam of this.allLocations){

            if(null!=cam.discount){

                cam.checked=true
                this.count+=1;
                console.log(this.count)

            }
            else{
                cam.checked=false
            }
            this.locations.push(cam);
        }
    }

}
