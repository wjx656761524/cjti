import {Component, ViewChild, ComponentFactoryResolver, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {CjtjNotifyService} from "../../shared/notify/notify.service";
import {Principal} from "app/shared/auth/principal.service";

import {EngineCreative} from "app/creative/add/engineCreative";
import {LocationModel} from "app/shared/location/location.model";
import {CreativeModel} from "app/shared/creative/creative.model";
import {CreativeService} from "app/shared/creative/creative.service";


@Component({
    selector: 'cjtj-addSecond-creative',
    templateUrl: './add-creativeSecond.component.html',
})

export class AddCreativeSecondComponent implements OnInit {
    account: any; // 当前账户


    @Output() onShowPage = new EventEmitter<number>();
    @Input() item:any;
    @Input()titlevalue:any='';
    @Input()mainurl:any;


    constructor(private componentFactoryResolver: ComponentFactoryResolver,
                public activeModal: NgbActiveModal,
                private cjtjNotifyService: CjtjNotifyService,
                public principal: Principal,
                public enginecreative:EngineCreative,
                public creativeService :CreativeService
    ) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;


        });

    }

    /**
     * 上一步操作
     */
    previousstep() :void{
        this.onShowPage.emit(1);


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

        console.log(this.titlevalue);

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

        const allCreative = new Array<CreativeModel>();

            const screative= new CreativeModel();
            screative.groupId=this.enginecreative.groupId;
            screative.imgUrl=this.mainurl;
            screative.creativeName=this.titlevalue;
            allCreative.push(screative);


        this.creativeService.updateCreative(allCreative).subscribe(
            (response) => {
                if (response.success) {
                    this.cjtjNotifyService.openMessage('修改成功');
                    this.activeModal.close(response.success);

                }else {
                    return this.cjtjNotifyService.openMessage('操作失败,超过单元最大绑定数');
                }
            }
        );

    }


}
