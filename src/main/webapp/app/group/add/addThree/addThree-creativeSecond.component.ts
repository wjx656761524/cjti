import {Principal} from "app/shared/auth/principal.service";
import {CreativeModel} from "app/shared/creative/creative.model";
import {CreativeService} from "app/shared/creative/creative.service";
import {EngineGroup} from "app/group/add/engineGroup";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {ItemService} from "app/shared/Item/item.service";
import {HttpService} from "app/crowd/serivce/http.service";
import {Component, ComponentFactoryResolver, OnInit} from "@angular/core";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";


@Component({
    selector: 'cjtj-addThree-creativeSecond',
    templateUrl: './addThree-creativeSecond.component.html',
})
export class AddThreeCreativeSecondComponent implements OnInit {
    account: any; // 当前账户
   item:any;
   titlevalue:any='';
   mainurl:any;
    itemId :any;
     mark:any;
    imageId:any;
    show = true;
    constructor(private componentFactoryResolver: ComponentFactoryResolver,
                public activeModal: NgbActiveModal,
                private cjtjNotifyService: CjtjNotifyService,
                public principal: Principal,
                public engineGroup: EngineGroup,
                public creativeService :CreativeService,
                public itemService:ItemService,
                private httsrv: HttpService

    ) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;
        });
       this.getAllLoactions()

    }

    /**
     * 获取指定单元的商品信息
     *
     */
    getAllLoactions():void{
        this.itemService.getItemDetailsByItemId(this.itemId,true).subscribe(
            response=>{
                this.item=response;
                this.setValue();
            }


        )


    }
    setValue(){
        for(let i=0;i<this.engineGroup.creatives.length;i++){
            if(this.engineGroup.creatives[i].imageId==this.imageId && this.engineGroup.creatives[i].itemId == this.itemId){
                this.titlevalue=this.engineGroup.creatives[i].creativeName
                this.mainurl=this.engineGroup.creatives[i].creativePic
                break;
            }
        }
    }

    /**
     * 进行图片的选择
     * @param url
     */


    onchange(itemImg:any){
        this.mainurl=itemImg.url;
    }

    /**
     * 提交前的检查
     */

    popoverCheck = () => {


     if (''==this.titlevalue) {
         this.cjtjNotifyService.openMessage('创意标题不能为空');
         return false;
     }

     else if(((this.titlevalue.toString().length)*2)>60){
            this.cjtjNotifyService.openMessage('创意标题不能超过60个字符');
            return false;
        }
     else
         return true

    }


    /**
     * 提交处理
     */
    public submit(){

        this.modifyCreative();

        this.activeModal.close();


    }
    close(){
        this.activeModal.close();
    }

    /**
     *添加创意
     */
modifyCreative(){
        this.activeModal.close();
        const ceativeModel = new Array<CreativeModel>();
        for(const creative of this.engineGroup.creatives){
            if(creative.imageId==this.imageId){
                const creative = new CreativeModel();
                creative.creativeName =  this.titlevalue;
                creative.imgUrl =this.mainurl;
                creative.groupId = this.engineGroup.groupId;
                ceativeModel.push(creative);
                this.creativeService.updateCreative(ceativeModel).subscribe(res=>{});
                break;
            }
        }
    }
}
