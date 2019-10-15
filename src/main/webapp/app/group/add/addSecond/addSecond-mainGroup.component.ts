import {Component, ComponentFactoryResolver, OnInit, Output, EventEmitter} from '@angular/core';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {CjtjNotifyService} from "../../../shared/notify/notify.service";
import {Principal} from "app/shared/auth/principal.service";
import {EngineGroup} from "app/group/add/engineGroup";
import {AddSecondItemComponent} from "app/group/add/addSecond/addSecond-Item.component";
import {AddSecondDiscountComnpnent} from "app/group/add/addSecond/addSecond-discount.comnpnent";
import {HttpService} from "app/crowd/serivce/http.service";
import {CrowdList, DirectionalLabel, Options} from "app/group/add/CrowdList";
import {CrowdService} from "app/crowd/serivce/crowd.service";
import {PAGE_SIZE} from "app/app.constants";


@Component({
    selector: 'cjtj-addSecond-mainGroup',
    templateUrl: './addSecond-mainGroup.component.html',
})

export class AddSecondMainGroupComponent implements OnInit {
    account: any; // 当前账户
    pageSize = 5;
    page:any=1;
    campaignId: number;
    allSelect = false;
    locationNum:any;
    alldiscount:any='';
    judgeZndx = false;   //  控制开关  智能定向
    judgeLxdxDp = false; // 控制开关 拉新定向--店铺
    judgeLxdxBb = false; // 控制开关 拉新定向--宝贝
    judgeDp1 = false; // 控制开关 重定向店铺 -- 喜欢我店铺的人群
    judgeDp2 = false; // 控制开关 重定向店铺 -- 收藏加购成交人群
    amounts: number; //批量出价
    addGjcModal: any; // 控制关键词模态框 隐藏
    addCdxModal:any; // 控制重定向模态框
    num = 0; // 关键词添加数量
    cdxbbCount = 0; //添加重定向宝贝数量
    addCdxdpModal: any;
    addCdxBbModal: any;
    addDmpModal: any;
    lxdx: any[]; // 拉新定向--关键词
    cdxbb: any // 重定向---宝贝
    dmpjx: any[] // 达摩盘平台精选
    options: Array<Options> = new Array<Options>();
    optionsdmp:Array<Options> = new Array<Options>();
    ZndxCrowdList: CrowdList = new CrowdList(); // 智能定向
    LxdxDCrowdList: CrowdList = new CrowdList(); // 拉新定向店铺
    LxdxBbCrowdList: CrowdList = new CrowdList(); // 拉新定向宝贝
    GjcCrowdList: Array<CrowdList> = new Array<CrowdList>(); // 拉新定向关键词
    CdxDpXxCrowdList: CrowdList = new CrowdList(); // 重定向店铺 喜欢我店铺的人群
    CdxDpScCrowdList: CrowdList = new CrowdList(); // 重定向店铺 收藏加购成交人群
    CdxBbCrowdList: CrowdList = new CrowdList(); // 重定向宝贝
    DmpJxCrowdList: Array<CrowdList> = new Array<CrowdList>(); // 达摩盘平台精选

    ZndxLabel: DirectionalLabel = new DirectionalLabel(); //  智能定向
    LxdxDpLabel: DirectionalLabel = new DirectionalLabel(); // 拉新定向店铺
    LxdxBbLabel: DirectionalLabel = new DirectionalLabel();  // 拉新定向宝贝
    GjcLabel: Array<DirectionalLabel> = new Array<DirectionalLabel>(); // 拉新定向关键词
    CdxDpXxLabel: DirectionalLabel = new DirectionalLabel(); //重定向店铺 喜欢我店铺的人群
    CdxDpScLabel: DirectionalLabel = new DirectionalLabel(); //重定向店铺 收藏加购成交人群
    CdxBbLabel: DirectionalLabel = new DirectionalLabel(); // 重定向宝贝
    DmpJxLabel: Array<DirectionalLabel> = new Array<DirectionalLabel>(); // 达摩盘平台精选

    list: any;
    cdxbbs: any;

    batchOffer:number;
    dmpjxCount = 0;
    addgjcs = []; // 选中的关键词
    queryDmp: any = { // 过滤参数 达摩盘精选
        optionName: '', // 标题
    };
    DmpjxUnits: any[] = []; // 当前页显示的  达摩盘精选
    queryCdxbb: any = { // 过滤参数
        optionName: '', // 标题
    };
    CdxbbUnits: any[] = [];
    @Output() onShowPage = new EventEmitter<number>();

    constructor(private componentFactoryResolver: ComponentFactoryResolver,
                public activeModal: NgbActiveModal,
                private cjtjNotifyService: CjtjNotifyService,
                public principal: Principal,
                public modalService :NgbModal,
                public engineGroup: EngineGroup,
                private httsrv: HttpService,
                private srv: CrowdService) {
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
     * 下一步操作
     */
    nextStep(){
        this.collect();
    }
    /**
     * 添加宝贝
     */
    addItem() {
        const modalRef = this.modalService.open(AddSecondItemComponent, {size: 'lg', windowClass: 'modal-large1-window'});
        modalRef.componentInstance.campaignId = this.engineGroup.campaignId;
        modalRef.result.then((value) => {
            if (value==0) {
            this.engineGroup.itemchecked=true;
            }
            else{
                this.engineGroup.itemchecked=false;

            }
        });
}

    /**
     * 移除宝贝
     */
    delItem(id:any){
        const deleditems= this.engineGroup.items.filter((item: any) => item.id!=id);
        this.engineGroup.items=deleditems;

        if(this.engineGroup.items.length==0){
            this.engineGroup.itemchecked=true;
        }
    }

    /**
     *溢价的检查
     */
    discountCheck(){
        if(this.engineGroup.itemchecked==true){
            this.cjtjNotifyService.openMessage("请先添加宝贝,再选择溢价")
        }

    }
    /**
     *跳转到溢价界面
     */
    setDisconut(){
        const modalRef = this.modalService.open(AddSecondDiscountComnpnent, {size: 'lg', windowClass: 'modal-large1-window'});
        modalRef.componentInstance.campaignId = this.engineGroup.campaignId;
        modalRef.result.then((value) => {
            if (value ) {
                  this.locationNum=this.engineGroup.locations.length
            }
        });

    }
    setDisconutTwo(){
        const modalRef = this.modalService.open(AddSecondDiscountComnpnent, {size: 'lg', windowClass: 'modal-large1-window'});
        modalRef.componentInstance.locations = this.engineGroup.locations;
        modalRef.result.then((value) => {
            if (value ) {
                this.locationNum=this.engineGroup.locations.length
            }
        });

    }


    clickSelectAll() {
        if (this.allSelect) {
            this.setLocationsChecked(this.engineGroup.locations, true);
        } else {
            this.setLocationsChecked(this.engineGroup.locations, false);
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


    getSelectLocations(){
        return this.engineGroup.locations.filter(location => location.checked);
    }

    //  选择定向点击事件
    onChanges = (newSelect: boolean,targetId: number,targetType: string) =>{
        if(newSelect){
            let itemIds = '';
            for(let i=0;i<this.engineGroup.items.length;i++){
                itemIds = itemIds + this.engineGroup.items[i].id +',';
            }
            this.srv.getOptionPage(targetId,targetType,itemIds).subscribe(res =>{
                this.list = res.info[0];
                if(targetId == 517){
                    this.judgeZndx = true;
                    this.ZndxCrowdList.crowdName = '智能定向';
                    this.ZndxCrowdList.crowdDesc = '系统根据您的访客属性、宝贝标题、宝贝属性等维度，智能匹配出适合该宝贝的人群';
                    this.ZndxCrowdList.optionName = this.list.options[0].optionName;
                    this.ZndxCrowdList.price = 0;
                    this.ZndxLabel.labelId = this.list.labelId;
                    this.ZndxLabel.labelValue = this.list.labelValue;
                    this.ZndxLabel.targetId = this.list.targetId;
                    this.ZndxLabel.targetType = this.list.targetType;
                    this.ZndxLabel.options = this.list.options;
                    this.ZndxCrowdList.directionalLabel = this.ZndxLabel;
                }
                if(targetId == 519){
                    this.judgeLxdxDp = true;
                    this.LxdxDCrowdList.crowdName = '喜欢相似店铺的人群';
                    this.LxdxDCrowdList.crowdDesc = '近期对相似店铺有浏览、搜索、收藏、加购物车、购买等行为的人群';
                    this.LxdxDpLabel.labelId = this.list.labelId;
                    this.LxdxDpLabel.labelValue = this.list.labelValue;
                    this.LxdxDpLabel.targetId = this.list.targetId;
                    this.LxdxDpLabel.targetType = this.list.targetType;
                    this.LxdxDpLabel.options = this.list.options;
                    this.LxdxDCrowdList.directionalLabel = this.LxdxDpLabel;
                }
                if(targetId == 521){
                    this.judgeLxdxBb = true;
                    this.LxdxBbCrowdList.crowdName = '喜欢相似宝贝的人群';
                    this.LxdxBbCrowdList.crowdDesc = '喜欢相似宝贝的人群';
                    this.LxdxBbLabel.labelId = this.list.labelId;
                    this.LxdxBbLabel.labelValue = this.list.labelValue;
                    this.LxdxBbLabel.targetId = this.list.targetId;
                    this.LxdxBbLabel.targetType = this.list.targetType;
                    this.LxdxBbLabel.options = this.list.options;
                    this.LxdxBbCrowdList.directionalLabel = this.LxdxBbLabel;
                }
                if(targetId == 518){
                    this.judgeDp1 = true;
                    this.CdxDpXxCrowdList.crowdName = '喜欢我店铺的人群';
                    this.CdxDpXxCrowdList.crowdDesc = '近期及实时对本店铺有浏览、搜索、收藏、加购物车、购买等行为的人群';
                    this.CdxDpXxLabel.labelId = this.list.labelId;
                    this.CdxDpXxLabel.labelValue = this.list.labelValue;
                    this.CdxDpXxLabel.targetId = this.list.targetId;
                    this.CdxDpXxLabel.targetType = this.list.targetType;
                    this.CdxDpXxLabel.options = this.list.options;
                    this.CdxDpXxCrowdList.directionalLabel = this.CdxDpXxLabel;
                }
                if(targetId == 522){
                    this.judgeDp2 = true;
                    this.CdxDpScCrowdList.crowdName = '收藏加购成交人群';
                    this.CdxDpScCrowdList.crowdDesc = '近期及实时对本店铺宝贝有收藏、加购物车等行为的人群';
                    this.CdxDpScLabel.labelId = this.list.labelId;
                    this.CdxDpScLabel.labelValue = this.list.labelValue;
                    this.CdxDpScLabel.targetId = this.list.targetId;
                    this.CdxDpScLabel.targetType = this.list.targetType;
                    this.CdxDpScLabel.options = this.list.options;
                    this.CdxDpScCrowdList.directionalLabel = this.CdxDpScLabel;
                }
            });
        }
        if(!newSelect){
            if(targetId == 517){
                this.judgeZndx = false;
                this.ZndxCrowdList.crowdName = null;
                this.ZndxCrowdList.crowdDesc = null;
                this.ZndxLabel.labelId = null;
                this.ZndxLabel.labelValue = null;
                this.ZndxLabel.targetId = null;
                this.ZndxLabel.targetType = null;
                this.ZndxLabel.options = [];
                this.ZndxCrowdList.directionalLabel = this.ZndxLabel;
            }
            if(targetId == 519){
                this.judgeLxdxDp = false;
                this.LxdxDCrowdList.crowdName = null;
                this.LxdxDCrowdList.crowdDesc = null;
                this.LxdxDpLabel.labelId = null;
                this.LxdxDpLabel.labelValue = null;
                this.LxdxDpLabel.targetId = null;
                this.LxdxDpLabel.targetType = null;
                this.LxdxDpLabel.options = [];
                this.LxdxDCrowdList.directionalLabel = this.LxdxDpLabel;
            }
            if(targetId == 521){
                this.judgeLxdxBb = false;
                this.LxdxBbCrowdList.crowdName = null;
                this.LxdxBbCrowdList.crowdDesc = null;
                this.LxdxBbLabel.labelId = null;
                this.LxdxBbLabel.labelValue = null;
                this.LxdxBbLabel.targetId = null;
                this.LxdxBbLabel.targetType = null;
                this.LxdxBbLabel.options = [];
                this.LxdxBbCrowdList.directionalLabel = this.LxdxBbLabel;
            }
            if(targetId == 518){
                this.judgeDp1 = false;
                this.CdxDpXxCrowdList.crowdName = null;
                this.CdxDpXxCrowdList.crowdDesc = null;
                this.CdxDpXxLabel.labelId = null;
                this.CdxDpXxLabel.labelValue = null;
                this.CdxDpXxLabel.targetId = null;
                this.CdxDpXxLabel.targetType = null;
                this.CdxDpXxLabel.options = [];
                this.CdxDpXxCrowdList.directionalLabel = this.CdxDpXxLabel;
            }
            if(targetId == 522){
                this.judgeDp2 = false;
                this.CdxDpScCrowdList.crowdName = null;
                this.CdxDpScCrowdList.crowdDesc = null;
                this.CdxDpScLabel.labelId = null;
                this.CdxDpScLabel.labelValue = null;
                this.CdxDpScLabel.targetId = null;
                this.CdxDpScLabel.targetType = null;
                this.CdxDpScLabel.options = [];
                this.CdxDpScCrowdList.directionalLabel = this.CdxDpScLabel;
            }
        }
    }

    // 达摩盘和重定向宝贝
    selectCheckbox(check:boolean,value:any,targetId: number){
        if(check){
            let offer = new Options();
            offer.checked = true;
            if(targetId == 439){
                if(this.dmpjxCount > 8){
                    return;
                }
                let dmpjx = new CrowdList();
                let dmpjxLabel = new DirectionalLabel();
                dmpjx.crowdName = value.optionName;
                dmpjx.crowdDesc = '定制人群';
                dmpjxLabel.labelId = this.dmpjx[0].labelId;
                dmpjxLabel.labelValue = this.dmpjx[0].labelValue;
                dmpjxLabel.targetId = this.dmpjx[0].targetId;
                dmpjxLabel.targetType = this.dmpjx[0].targetType;
                offer.optionName = value.optionName;
                offer.optionValue = value.optionValue;
                offer.checked = true;
                dmpjxLabel.options.push(offer);
                dmpjx.directionalLabel = dmpjxLabel;
                this.DmpJxLabel.push(dmpjxLabel);
                this.DmpJxCrowdList.push(dmpjx);
                this.dmpjxCount = this.dmpjxCount + 1;
            }
            if(targetId == 520){
                if(this.cdxbbCount > 5){
                    return;
                }
                this.CdxBbCrowdList.crowdName = '喜欢我的宝贝的人群';
                this.CdxBbCrowdList.crowdDesc = '喜欢我的宝贝的人群';
                this.CdxBbLabel.labelId = this.cdxbbs.labelId;
                this.CdxBbLabel.labelValue = this.cdxbbs.labelValue;
                this.CdxBbLabel.targetId = this.cdxbbs.targetId;
                this.CdxBbLabel.targetType = this.cdxbbs.targetType;
                offer.optionName = value.title;
                offer.optionValue = value.itemId;
                offer.checked = true;
                this.CdxBbLabel.options.push(offer);
                this.CdxBbCrowdList.directionalLabel = this.CdxBbLabel;
                this.cdxbbCount = this.cdxbbCount + 1;
            }
        }
        if(!check){
            if(targetId == 520){
                let i = this.CdxBbLabel.options.indexOf(value.itemId);
                this.CdxBbLabel.options.splice(i,1);
                this.cdxbbCount = this.cdxbbCount -1;
                if(this.cdxbbCount == 0){
                    this.CdxBbLabel.labelId = null;
                    this.CdxBbLabel.labelValue = null;
                    this.CdxBbLabel.targetId = null;
                    this.CdxBbLabel.targetType = null;
                    this.CdxBbCrowdList.crowdDesc = null;
                    this.CdxBbCrowdList.crowdName = null;
                    this.CdxBbCrowdList.crowdName = '';
                    this.CdxBbCrowdList.crowdDesc = '';
                    this.CdxBbCrowdList.directionalLabel = this.CdxBbLabel;
                }
            }
            if(targetId == 439) {
                for(let i=0;i<this.DmpJxLabel.length;i++){
                  let j  = this.DmpJxLabel[i].options.indexOf(value.optionValue);
                  this.DmpJxLabel.splice(i,1);
                  this.DmpJxCrowdList.splice(i,1);
                }
                this.dmpjxCount = this.dmpjxCount - 1;
                if(this.dmpjxCount == 0){
                    this.DmpJxCrowdList = [];
                    this.DmpJxLabel = [];
                }
            }
        }
    }


    // 打开关键词模态框
    openGjcTemplate(modal) {
        this.num = 0;
        let gjcbb = new Array<DirectionalLabel>();
        let gjclist = new Array<CrowdList>();
        this.GjcLabel = gjcbb;
        this.GjcCrowdList = gjclist;
        this.addgjcs = [];
        // 初始化关键词
        let itemIds = '';
        for(let i=0;i<this.engineGroup.items.length;i++){
            itemIds = itemIds + this.engineGroup.items[i].id +',';
        }
        this.options = [];
        this.srv.getOptionPage(523,'ITEM_NODE',itemIds).subscribe(res =>{
            this.lxdx = res.info;
            for(const option of res.info[0].options){
                this.options.push(option);
            }
        })
        this.addGjcModal = this.modalService.open(modal, {size: 'lg', windowClass: 'modal-large2-window'});
    }
    // 打开重定向店铺模态框
    openCdxdpTemplate(modal) {
        this.addCdxdpModal = this.modalService.open(modal, {size: 'lg', windowClass: 'modal-large2-window'});
    }
    // 打开重定向宝贝模态框
    openCdxBbTemplate(modal){
        this.cdxbbCount = 0;
        let cdxbb = new DirectionalLabel();
        let cdxbblist = new CrowdList();
        this.CdxBbLabel = cdxbb;
        this.CdxBbCrowdList = cdxbblist;
        // 获取 计划下所有宝贝
        this.srv.getItemPage(this.engineGroup.campaignId).subscribe(res =>{
            const cdxbb =  res.info.filter((unit: any) =>  unit.isAccessAllowed == true);
            this.cdxbb = cdxbb;
            this.queryCdxBb();
        });
        //获取宝贝标签
        let itemIds = '';
        for(let i=0;i<this.engineGroup.items.length;i++){
            itemIds =itemIds + this.engineGroup.items[i].id + ',';
        }
        this.srv.getOptionPage(520,'LIKE_MY_ITEM',itemIds).subscribe(res =>{
            this.cdxbbs = res.info[0];
        })
        this.addCdxBbModal = this.modalService.open(modal, {size: 'lg', windowClass: 'modal-large2-window'});
    }
    // 打开达摩盘精选模态框
    openDmpTemplate(modal){
        this.dmpjxCount = 0;
        let dmpjx = new Array<DirectionalLabel>();
        let dmpjxlist = new Array<CrowdList>();
        this.DmpJxLabel = dmpjx;
        this.DmpJxCrowdList = dmpjxlist;
        for(let i = 0;i<this.DmpJxCrowdList.length;i++){
            this.DmpJxCrowdList.splice(i,1);
        }
        for(let i = 0;i<this.DmpJxLabel.length;i++){
            this.DmpJxLabel.splice(i,1);
        }
        // 初始化达摩盘数据
        let itemIds = '';
        for(let i=0;i<this.engineGroup.items.length;i++){
            itemIds =itemIds + this.engineGroup.items[i].id + ',';
        }
        this.optionsdmp = [];
        this.srv.getOptionPage(439,'DMP_BASE',itemIds).subscribe(res =>{
            this.dmpjx = res.info;
            for(const option of res.info[0].options){
                this.optionsdmp.push(option);
            }
        })
        this.queryDmpjx();
        this.addDmpModal = this.modalService.open(modal, {size: 'lg', windowClass: 'modal-large2-window'});
    }

    // 添加关键词
    addgjc = (params: any) =>{
        for(let i=0;i<this.options.length;i++){
            if(this.options[i].optionName === params.optionName){
                if(this.addgjcs.length>20){
                    break;
                }
                let offer = new Options();
                offer.checked = true;
                let gjcLabel = new DirectionalLabel();
                let gjc = new CrowdList();
                gjc.crowdName = '购物意图定向';
                gjc.crowdDesc = '系统推荐与单元所选宝贝相关性较高的宝贝关键词，并匹配出购买意向较强的人群进行投放';
                gjc.optionName = params.optionName;
                gjcLabel.labelId = this.lxdx[0].labelId;
                gjcLabel.labelValue = this.lxdx[0].labelValue;
                gjcLabel.targetId = this.lxdx[0].targetId;
                gjcLabel.targetType = this.lxdx[0].targetType;
                offer.optionName = params.optionName;
                offer.optionValue = params.optionValue;
                gjcLabel.options.push(offer);
                this.GjcLabel.push(gjcLabel);
                gjc.directionalLabel = gjcLabel;
                this.GjcCrowdList.push(gjc);
                this.num = this.addgjcs.length+1;
                this.addgjcs.push(params);
                this.options.splice(i,1);
            }

        }
    }

    // 删除关键词
    deletegjc = (params: any) => {
        for(let i= 0; i<this.addgjcs.length;i++){
            if(this.addgjcs[i].optionName === params.optionName){
                let l = this.GjcLabel.indexOf(params.optionName);
                this.GjcLabel.splice(l,1);
                this.GjcCrowdList.splice(l,1);
                this.options.push(this.addgjcs[i]);
                this.addgjcs.splice(i,1);
                this.num = this.addgjcs.length;
            }
        }
    }

    // 批量修改出价
    batchOffers = ()=> {
        this.ZndxCrowdList.price = this.batchOffer;
        this.LxdxDCrowdList.price = this.batchOffer;
        this.LxdxBbCrowdList.price = this.batchOffer;
        for(let i =0;i<this.GjcCrowdList.length;i++){
            this.GjcCrowdList[i].price = this.batchOffer;
        }
        this.CdxDpXxCrowdList.price = this.batchOffer;
        this.CdxDpScCrowdList.price = this.batchOffer;
        this.CdxBbCrowdList.price = this.batchOffer;
        for(let i=0;i<this.DmpJxCrowdList.length;i++){
            this.DmpJxCrowdList[i].price = this.batchOffer;
        }
    }


    collect = ()=>{
        if(this.ZndxCrowdList.crowdName != null){
            this.engineGroup.directionalUnit.push(this.ZndxCrowdList);
        }
        if(this.LxdxDCrowdList.crowdName != null){
            this.engineGroup.directionalUnit.push(this.LxdxDCrowdList);
        }
        if(this.LxdxBbCrowdList.crowdName != null){
            this.engineGroup.directionalUnit.push(this.LxdxBbCrowdList);
        }
        if(this.GjcCrowdList.length != 0){
            for(let i=0;i< this.GjcCrowdList.length;i++){
                if(this.GjcCrowdList[i].crowdName != null){
                    this.engineGroup.directionalUnit.push(this.GjcCrowdList[i]);
                }
            }
        }
        if(this.CdxDpXxCrowdList.crowdName != null){
            this.engineGroup.directionalUnit.push(this.CdxDpXxCrowdList);
        }
        if(this.CdxDpScCrowdList.crowdName != null){
            this.engineGroup.directionalUnit.push(this.CdxDpScCrowdList);
        }
        if(this.CdxBbCrowdList.crowdName != null){
            this.engineGroup.directionalUnit.push(this.CdxBbCrowdList);
        }
        if(this.DmpJxCrowdList.length != 0){
            for(let i=0;i< this.DmpJxCrowdList.length;i++){
                if(this.DmpJxCrowdList[i].crowdName != null){
                    this.engineGroup.directionalUnit.push(this.DmpJxCrowdList[i]);
                }
            }
        }
        if(this.engineGroup.itemchecked){
            this.cjtjNotifyService.openMessage("请先选择宝贝")
        }
        else {this.onShowPage.emit(3);}
    }
    /**
     * 选择页
     * @param showData
     */
    pageChangecdxbb(pageInfo) {
        this.CdxbbUnits = this.getShowDatacdxbb(pageInfo);
    }
    getShowDatacdxbb (pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;
        if (this.CdxbbUnits && this.CdxbbUnits.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.CdxbbUnits.length ? this.CdxbbUnits.length : pageNumber * pageSize;
            showDataSet = this.CdxbbUnits.slice(start, end);
        }
        return showDataSet;
    }

    /**
     * 选择页
     * @param showData
     */
    pageChangedmpjx(pageInfo) {
        this.DmpjxUnits = this.getShowDatadmpjx(pageInfo);
    }
    getShowDatadmpjx (pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;
        if (this.DmpjxUnits && this.DmpjxUnits.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.DmpjxUnits.length ? this.DmpjxUnits.length : pageNumber * pageSize;
            showDataSet = this.DmpjxUnits.slice(start, end);
        }
        return showDataSet;
    }


    // 搜索达摩盘
    queryDmpjx = () =>{
        if(this.queryDmp.optionName === ''){
            this.DmpjxUnits = this.optionsdmp;
        }else{
            const dmpjx = [];
            for(let i=0;i<this.dmpjx[0].options.length;i++){
                if(this.dmpjx[0].options[i].optionName.indexOf(this.queryDmp.optionName) != -1 ){
                    dmpjx.push(this.dmpjx[0].options[i]);
                }
            }
            this.DmpjxUnits = dmpjx;
        }
    }

    // 搜索重定向宝贝
    queryCdxBb = () =>{
        if(this.queryCdxbb.optionName === ''){
            this.CdxbbUnits = this.cdxbb;
        }else{
            const dmpjx = [];
            for(let i=0;i<this.cdxbb.length;i++){
                if(this.cdxbb[i].title.indexOf(this.queryCdxbb.optionName) != -1 ){
                    dmpjx.push(this.cdxbb[i]);
                }
            }
            this.CdxbbUnits = dmpjx;
        }
    }
}
