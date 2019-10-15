import {Component, ViewChild, ComponentFactoryResolver, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {CjtjNotifyService} from "../../shared/notify/notify.service";
import {Principal} from "app/shared/auth/principal.service";
import any = jasmine.any;
import {LocationService} from "app/shared/location/location.service";
import {DelLocationModel} from "app/shared/location/del-location.model";
import {LocationModel} from "app/shared/location/location.model";
import {Engine} from "app/location/add/engine";


@Component({
    selector: 'cjtj-addSecond-location',
    templateUrl: './addSecond-location.component.html',
})

export class AddSecondLocationComnpnent implements OnInit {
    account: any; // 当前账户
    @Input() locations:any[]=[];//所有广告位
    @Output() onShowPage = new EventEmitter<number>();
    allSelect = false;
    @Input()locationNum:any;
   alldiscount:any='';
    campaignId:any;

    constructor(private componentFactoryResolver: ComponentFactoryResolver,
                public activeModal: NgbActiveModal,
                private cjtjNotifyService: CjtjNotifyService,
                public principal: Principal,
                public locationService:LocationService,
                public engine:Engine
                ) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;



        });

    }
    /**
     * 获取所有资源位
     *
     */
    getAllLoactions():void{
        this.locationService.getALLLocations(this.engine).subscribe(
            response=>{
                this.locations=response;

            })

    }

    /**
     * 上一步操作
     */
    previousstep() :void{
      this.onShowPage.emit(1);

    console.log(this.locationNum)
    }

    clickSelectAll() {
        if (this.allSelect) {
            console.log(this.allSelect)
            this.setLocationsChecked(this.locations, true);
        } else {
            this.setLocationsChecked(this.locations, false);
        }
    }
    setLocationsChecked(locations, checked) {
        for (const location of locations) {
            if(checked==true)this.locationNum=locations.length;
            if(checked==false)this.locationNum=0;
            location.checked = checked;
        }
    }

    /**
     * 绑定资源位的计数
     */
    countNum(checked:boolean){
        if(checked)this.locationNum+=1;
        else this.locationNum-=1;

    }

    /**
     * 批量设置溢价
     */
    changAllDiscount(){
        const locationss = this.getSelectLocations();
        for (const cam of locationss ){
            cam.discount=this.alldiscount;

        }



    }
    /**
     * 批量溢价提交前的检查
     */
    popoverCheck = () => {
        const selectIds = this.getSelectIds();
        if (selectIds.length === 0) {
            this.cjtjNotifyService.openMessage('请先选择要操作的广告位');
            return false;
        }
            else if(!this.discountCheck){
                this.cjtjNotifyService.openMessage('请按规范设置溢价');
            return false;
            }

            else {
            return true;
        }

    }

    /**
     * 获取选中ID 的检查
     */
    getSelectIds() {
        const selectIds = [];
        for (const location of this.locations) {
            if (location.checked) {
                selectIds.push(location.adzoneId);
            }
        }
        return selectIds;
    }

    /**
     * 溢价的检查
     */

  discountCheck():boolean{
      for (const location of this.locations) {
          if(location.checked){
              if (null==location.discount||!(location.discount>1&&location.discount<300)){
                  return false
              }
          }
      }
      return true

  }
    batchUpdateDiscount(){
        const locations = this.getSelectLocations();
        if (locations.length == 0) return this.cjtjNotifyService.openMessage('请选中要修改的广告位');
        const alllocation = new Array<LocationModel>();
        for (const location of locations) {
            const slocation= new LocationModel();
            slocation.adzoneId = location.adzoneId;
            slocation.discount = location.discount;
            slocation.groupId=this.engine.groupId;
            slocation.campaignId=this.engine.campaignId;
            slocation.adzoneName=location.adzoneName
            alllocation.push( slocation);
        }

        this.locationService.updateLocations(alllocation).subscribe(
            (response) => {
                if (response.success) {
                   this.cjtjNotifyService.openMessage('修改成功');
                    this.activeModal.close(response.success);

                }else {
                    return this.cjtjNotifyService.openMessage('操作失败');
                }
            }
        );
    }


    getSelectLocations(){
        return this.locations.filter(location => location.checked);
    }
}
