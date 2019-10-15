import {Component, EventEmitter, OnInit, ViewChild } from '@angular/core';
import {addCrowdParem, CrowdQuery, CrowdService, ModiFyBind, Offer} from "app/crowd/serivce/crowd.service";
import {DateSelectChange} from "app/shared";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DEFAULT_COLUMNS, REPORT_FIELDS} from "app/report.constants";
import {ActivatedRoute} from "@angular/router";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {PAGE_SIZE} from "app/app.constants";
@Component({
    selector: 'cjtj-crowd',
    templateUrl: './crowd.component.html',
})
export class CrowdComponent implements OnInit {
    directional:any;
    selectCounts = 0;
    allSelect = false;
    filteredUnits: any[] = []; // 过滤后的数据
    units: any[] = []; // 当前页显示的
    DmpjxUnits: any[] = []; // 当前页显示的  达摩盘精选
    CdxbbUnits: any[] = []; // 当前页 重定向宝贝
    fields = REPORT_FIELDS;
    selectColumns = [...DEFAULT_COLUMNS];
    isRealTime: boolean;
    paramsEventer: EventEmitter<any> = new EventEmitter();
    isBudgetHidePopover = true;
    isDelete = true;
    status:'';
    addCrowdModal: any;
    pages = '';
    lxxx = -1;
    jllx = 0;
    CampaignGroup: any;
    dh = -1;
    msg ='';
    value: boolean;


    zndx: any; // 智能定向
    lxdxdp: any; // 拉新定向店铺
    lxdxbb: any; // 拉新定向宝贝
    cdxdpxx: any; // 重定向店铺-喜欢我店铺的人群
    cdxdpsc: any; // 重定向店铺--收藏加购成交人群

    lxdx: any; // 拉新定向--关键词
    cdxbb: any // 重定向---宝贝
    cdxbbCount = 0;
    dmpjx: any // 达摩盘平台精选
    dmpjxCount = 0;
    dmp: any; // 达摩盘
    addgjcs = [];
    num = 0;
    adgroupName = '';
    campaignName = '';
    camName = '';
    groupName = '';
    Parameter = [];
    amounts = '';
    judgeZndx = false;
    judgeLxdxDp = false;
    judgeLxdxBB = false;
    judgeDp1 = false;
    judgeDp2 = false;
    modifybind: ModiFyBind = {
        price:null,
        status:null,
        crowdId: null,
        adgroupId:null,
    }
    queryParams: CrowdQuery = {
        startTime: '',
        campaignId: null,
        adgroupId: null,
        endTime: '',
        itemId: null
    }
    addCrowdParams: addCrowdParem = {
        campaignId:null,
        adgroupId:null,
        targetId:null,
        targetType:null,
        labelId: null,
        labelValue: null,
        price: null,
        optionName: null,
        checked:null,
        optionValue: null,
        crowdName: null,
        crowdDesc: null,
        itemId: null
    }
    queryParamss: any = { // 过滤参数
        adgroupName: '', // 标题
    };
    queryDmp: any = { // 过滤参数
        optionName: '', // 标题
    };
    queryCdxbb: any = { // 过滤参数
        optionName: '', // 标题
    };
    editBudget: any;
    @ViewChild('pagination') pagination: any;
    page = 1;
    pageSize = PAGE_SIZE;
    batchOffer = '';
    campaignShow = true;
    groupShow = true;
    show = false;
    constructor(private srv: CrowdService,
                private modalService: NgbModal,
                private activatedRoute: ActivatedRoute,
                private cjtjNotifyService: CjtjNotifyService,) { }

    ngOnInit() {

        this.activatedRoute.queryParams.subscribe(queryParams => {
            this.queryParams.adgroupId = queryParams['groupId'];
            this.queryParams.campaignId = queryParams['campaignId'];
            this.camName = queryParams['campaignName'];
            this.groupName = queryParams['groupName'];
            this.queryParams.itemId = queryParams['itemId'];
            if(this.queryParams.adgroupId == 0){
                this.queryParams.adgroupId = null
            }

            if(this.groupName != undefined){
                this.show = true;
            }else if(this.queryParams.campaignId != undefined){
                this.show = true;
            }
            if(this.queryParams.adgroupId!=null&&0!=this.queryParams.adgroupId){
                this.campaignShow=false;
                this.groupShow = false;
            }
            if(this.queryParams.campaignId!= null&& 0!= this.queryParams.campaignId){
                this.campaignShow=false;
            }
        });
        this.getCrowdRptdailylist(this.queryParams);
    }


    batchDelete = () =>{
        let keywords = [];
        keywords = this.getSelectKeywords();
        keywords.forEach(v => {
            this.srv.batchDelete(v.crowdId,v.adgroupId).subscribe(re =>{
                this.getCrowdRptdailylist(this.queryParams);
            })
        });
    }



    getCrowdRptdailylist = (queryParams: CrowdQuery)=>{
        this.srv.getCrowdRptdailylist(queryParams).subscribe((response) => {
            this.directional = response.list;
            this.query();
        });
    }

    selcetedChange(all: boolean, single?: boolean) {
        if (all && this.directional) {
            this.selectCounts = this.directional.length;
            this.setKeywordsChecked(this.directional, true);
        } else if(all) {
            this.selectCounts = 0;
            this.setKeywordsChecked(this.directional, false);
        } else if(single){
            this.selectCounts = this.selectCounts + 1;
            if(this.selectCounts == this.directional.length) {
                this.allSelect = true;
            }
        } else if(!single) {
            this.allSelect = false;
            this.selectCounts = this.selectCounts - 1;
        }
    }

    /**
     * 设置关键词是否选中状态
     * @param keys
     * @param checked
     */
    setKeywordsChecked(keys, checked) {
        for (const key of keys) {
            key.checked = checked;
        }
    }

    getSelectKeywords(): any[] {
        const selectKeywords = [];
        for (const keyword of this.directional) {
            if (keyword.checked) {
                selectKeywords.push(keyword.info);
            }
        }
        return selectKeywords;
    }

    /**
     * 日历选择处理
     * @param {DateSelectChange} newSelect
     */
    onDateSelectChange(newSelect: DateSelectChange) {
        this.queryParams.startTime = newSelect.startTime;
        this.queryParams.endTime = newSelect.endTime;
        this.isRealTime = newSelect.isRealTime? true: false;
        this.getCrowdRptdailylist(this.queryParams);
        this.paramsEventer.emit({
            startTime: this.queryParams.startTime,
            endTime: this.queryParams.endTime,
            isRealTime: this.isRealTime,
        });
    }


    /**
     * 列选择变化
     * @param {any[]} selectColumns
     */
    onColumnsSelectChange(selectColumns) {
        this.selectColumns = selectColumns;
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
        this.pageChangecdxbb({page: 1, pageSize: this.pageSize});
    }
    // 搜索达摩盘
    queryDmpjx = () =>{
        if(this.queryDmp.optionName === ''){
            this.DmpjxUnits = this.dmpjx[0].options;
        }else{
            const dmpjx = [];
            for(let i=0;i<this.dmpjx[0].options.length;i++){
                if(this.dmpjx[0].options[i].optionName.indexOf(this.queryDmp.optionName) != -1 ){
                    dmpjx.push(this.dmpjx[0].options[i]);
                }
            }
            this.DmpjxUnits = dmpjx;
        }
        this.pageChangedmpjx({page: 1, pageSize: this.pageSize});
    }
    /**
     * 查询过滤
     */
    query() {
        this.changeUnitsChecked(this.directional, false);
        const filteredUnits = this.directional
            .filter((unit: any) =>  unit.info.adgroupName.includes(this.queryParamss.adgroupName));
        this.filteredUnits = filteredUnits;
        this.setUnitsChecked(this.units, false);
        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);
    }
    setUnitsChecked(units, checked) {
        for (const unit of units) {
            unit.checked = checked;
        }
    }
    changeUnitsChecked(units, checked) {
        if (units) {
            for (const unit of units) {
                unit.checked = checked;
            }
        }
    }
    /**
     * 选择页
     * @param showData
     */
    pageChange(pageInfo) {
        this.units = this.getShowData(pageInfo);
    }
    getShowData(pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;
        if (this.filteredUnits && this.filteredUnits.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.filteredUnits.length ? this.filteredUnits.length : pageNumber * pageSize;
            showDataSet = this.filteredUnits.slice(start, end);
        }
        return showDataSet;
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
        if (this.cdxbb && this.cdxbb.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.cdxbb.length ? this.cdxbb.length : pageNumber * pageSize;
            showDataSet = this.cdxbb.slice(start, end);
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
        if (this.dmpjx[0].options && this.dmpjx[0].options.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.dmpjx[0].options.length ? this.dmpjx[0].options.length : pageNumber * pageSize;
            showDataSet = this.dmpjx[0].options.slice(start, end);
        }
        return showDataSet;
    }

    /**
     * 打开编辑日限额窗口
     * @param campaign
     */
    openEditBudget(campaign) {
        this.isBudgetHidePopover = true;
        this.editBudget = campaign/100;
    }
    /**
     * 修改出价
     * @param campaign
     */
    submitBudget(data) {
        this.modifybind.adgroupId = data.adgroupId;
        this.modifybind.crowdId = data.crowdId;
        this.modifybind.price = this.editBudget * 100;
        this.srv.updatePrice(this.modifybind).subscribe(res =>{
            this.getCrowdRptdailylist(this.queryParams);
        }, error =>{
            this.cjtjNotifyService.openMessage('修改失败！');
        });
    }

    // 修改状态 启动
    updateStart = (data) =>{
        this.modifybind.status =  'start';
        this.modifybind.adgroupId = data.adgroupId;
        this.modifybind.crowdId = data.crowdId;
        this.srv.updatePrice(this.modifybind).subscribe(res =>{
            data.status = 'start';
        },error =>{
            this.cjtjNotifyService.openMessage('修改失败！');
        });
    }
    // 状态 暂停
    updatePausr = (data) =>{
        this.modifybind.status =  'pause';
        this.modifybind.adgroupId = data.adgroupId;
        this.modifybind.crowdId = data.crowdId;
        this.modifybind.price = data.price;
        this.srv.updatePrice(this.modifybind).subscribe(res =>{
            data.status = 'pause';
        },error =>{
            this.cjtjNotifyService.openMessage('修改失败！');
        });
    }



    /**
     * 打开是否删除弹框
     * @param campaign
     */
    openDelete() {
        this.isDelete = true;
    }


    openTemplate(modal) {
        this.addCrowdParams.targetId = null;
        this.addCrowdParams.campaignId = null;
        this.addCrowdParams.adgroupId = null;
        this.addCrowdParams.targetType = null;
        this.getCampaignGroup();
        this.pages = 'danyuan';
        this.lxxx = -1;
        this.jllx = -1;
        this.addgjcs = [];
        this.Parameter = [];
        this.judgeZndx = false;
        this.judgeLxdxDp = false;
        this.judgeLxdxBB = false;
        this.judgeDp1 = false;
        this.judgeDp2 = false;
        this.num = 0;
        if( this.queryParams.campaignId!= undefined && this.queryParams.campaignId != null && this.queryParams.campaignId!=0&&
            this.queryParams.adgroupId!=undefined && this.queryParams.adgroupId!=0 && this.queryParams.adgroupId!=null&&
            this.queryParams.itemId != undefined && this.queryParams.itemId != null){
            this.addCrowdParams.campaignId = this.queryParams.campaignId;
            this.addCrowdParams.adgroupId = this.queryParams.adgroupId;
            this.addCrowdParams.itemId = this.queryParams.itemId;
            this.addCrowdModal = this.modalService.open(modal, {size: 'lg', windowClass: 'modal-large2-window'});
            this.next();
            return;
        }
        this.addCrowdModal = this.modalService.open(modal, {size: 'lg', windowClass: 'modal-large2-window'});
    }


    // 获取所有的计划和单元
    getCampaignGroup = () =>{
        this.srv.getCampaignGroup().subscribe(
            res =>{
                this.CampaignGroup = res;
            });
    }

    //新增定向，选中单元赋值
    ModifyProgram = (campaignId: number,groupId: number,groupName: string,campaignName: string,itemId: number)=> {
        this.addCrowdParams.campaignId = campaignId;
        this.addCrowdParams.adgroupId = groupId;
        this.adgroupName = groupName;
        this.campaignName = campaignName;
        this.addCrowdParams.itemId = itemId;
    }

    // 获取定向类型下所有标签
    getOptionPage = (crowd: addCrowdParem) => {
        let itemIds = '';
        itemIds = this.addCrowdParams.itemId.toString()+',';
        if(this.dh == 5){
            this.srv.getOptionPage(518,'LIKE_MY_SHOP',itemIds).subscribe(res =>{
                this.cdxdpxx = res.info;
            })
            this.srv.getOptionPage(522,'SHOP_BEHAVIOR',itemIds).subscribe(res =>{
                this.cdxdpsc = res.info;
            })
            return;
        }
        this.srv.getOptionPage(crowd.targetId,crowd.targetType,itemIds).subscribe(res =>{
            if(this.dh == 1){
                this.zndx = res.info;
            }
            if(this.dh == 2){
                this.lxdxdp = res.info;
            }
            if(this.dh == 3) {
                this.lxdxbb = res.info;
            }
            if(this.dh == 4){
                this.lxdx = res.info; // 拉新定向--关键词
            }
            if(this.dh == 7){
                this.dmpjx = res.info; // 达摩盘平台精选
                this.queryDmpjx();
            }
            if(this.dh == 8){
                console.log(11111);
                console.log(res.info);
                this.dmp = res.info; // 达摩盘
            }
            if(this.dh == 6){
                this.addCrowdParams.labelId = res.info[0].labelId;
                this.addCrowdParams.labelValue = res.info[0].labelValue;
            }
        });
    }

    // 获取 计划下所有宝贝
    getItemPage = (campaignId: number) =>{
        this.srv.getItemPage(campaignId).subscribe(res =>{
            // this.cdxbb = res.info; // 重定向---宝贝
            const cdxbb =  res.info.filter((unit: any) =>  unit.isAccessAllowed == true);
            this.cdxbb = cdxbb;
            this.queryCdxBb();
        });
    }



    onLxdxbb = (newSelect: boolean,targetId: number,targetType: string,data:any) => {
        if(newSelect){
            if(521 == targetId){
                this.judgeLxdxBB = true;
            }
            this.addCrowdParams.targetId = targetId;
            this.addCrowdParams.targetType = targetType;
            this.addCrowdParams.labelId = data.labelId;
            this.addCrowdParams.labelValue = data.labelValue;

            let offer = new Offer();
            offer.optionName = data.labelName;
            offer.price = null;
            offer.checked = true;
            offer.crowdName = '喜欢相似宝贝的人群';
            offer.crowdDesc = '喜欢相似宝贝的人群';
            offer.optionValue = [];
            offer.optionValue.push(this.addCrowdParams.itemId);
            this.Parameter.push(offer);
        }
        if(!newSelect){
            this.panduan();
            this.addCrowdParams.targetId = null;
            this.addCrowdParams.targetType = null;
            this.addCrowdParams.labelId = null;
            this.addCrowdParams.labelValue = null;
            let i =  this.Parameter.indexOf(data.labelName);
            this.Parameter.splice(i,1);
        }
    }
    /**
     * 定向类型下 标签列表开关
     */
    onChanges = (newSelect: boolean,targetId: number,targetType: string,data:any) =>{
        if(newSelect){
            let offer = new Offer();
            if(517 == targetId){
                this.judgeZndx = true;
                offer.crowdName = '智能定向';
                offer.crowdDesc = '系统根据您的访客属性、宝贝标题、宝贝属性等维度，智能匹配出适合该宝贝的人群';
            }
            if(519 == targetId){
                this.judgeLxdxDp = true;
                offer.crowdName = '喜欢相似店铺的人群';
                offer.crowdDesc = '近期对相似店铺有浏览、搜索、收藏、加购物车、购买等行为的人群';
            }
            if(518 == targetId){
                this.judgeDp1 = true;
                offer.crowdName = '喜欢我店铺的人群';
                offer.crowdDesc = '近期及实时对本店铺有浏览、搜索、收藏、加购物车、购买等行为的人群';
            }
            if(522 == targetId){
                this.judgeDp2 = true;
                offer.crowdName = '收藏加购成交人群';
                offer.crowdDesc = '近期及实时对本店铺宝贝有收藏、加购物车等行为的人群';
            }
            this.addCrowdParams.targetId = targetId;
            this.addCrowdParams.targetType = targetType;
            this.addCrowdParams.labelId = data.labelId;
            this.addCrowdParams.labelValue = data.labelValue;
            offer.checked = true;
            offer.price = null;
            offer.optionName = data.labelName;
            offer.optionValue = [];
            for(let i= 0;i<data.options.length;i++){
                offer.optionValue.push(data.options[i].optionValue)
            }
            this.Parameter.push(offer)
        }
        if(!newSelect){
            this.panduan();
            this.addCrowdParams.targetId = null;
            this.addCrowdParams.targetType = null;
            this.addCrowdParams.labelId = null;
            this.addCrowdParams.labelValue = null;
            let i =  this.Parameter.indexOf(data.optionName);
            this.Parameter.splice(i,1);
            // for(let i=0;i<this.addCrowdParams.optionValue.length;i++){
            //     let indexc = this.addCrowdParams.optionValue.indexOf(optionValue);
            //     this.addCrowdParams.optionValue.splice(indexc,1)
            // }
        }
    }


    //下一页
    next = () =>{
        if(this.pages === 'danyuan'){
            if(null == this.addCrowdParams.adgroupId || null == this.addCrowdParams.campaignId){
                this.msg = '请选择单元！';
                return;
            }
            this.pages = 'leixing';
            this.msg = '';
            return;
        }
        if(this.pages === 'leixing' && this.dh == -1){
            this.msg = '请选择定向类型！';
            return;
        }
        if(this.pages === 'leixing' && this.dh != -1){
            if(this.dh == 1){
                this.addCrowdParams.targetId = 517;
                this.addCrowdParams.targetType = 'ITEM_RECOMMEND';
                this.getOptionPage(this.addCrowdParams);
            }
            if(this.dh == 2){
                this.addCrowdParams.targetId = 519;
                this.addCrowdParams.targetType = 'LOOK_LIKE_SHOP';
                this.getOptionPage(this.addCrowdParams);
            }
            if(this.dh == 3){
                this.addCrowdParams.targetId = 521;
                this.addCrowdParams.targetType = 'LOOK_LIKE_ITEM';
                this.getOptionPage(this.addCrowdParams);
            }
            if(this.dh == 5){
                this.getOptionPage(this.addCrowdParams);
            }
            if(this.dh == 4){
                this.addCrowdParams.targetId = 523;
                this.addCrowdParams.targetType = 'ITEM_NODE';
                this.getOptionPage(this.addCrowdParams);
            }
            if(this.dh == 6){ // 获取宝贝
                this.addCrowdParams.targetId = 520;
                this.addCrowdParams.targetType = 'LIKE_MY_ITEM';
                this.getOptionPage(this.addCrowdParams);
                this.getItemPage(this.addCrowdParams.campaignId);
            }
            if(this.dh == 7){
                this.addCrowdParams.targetId = 439;
                this.addCrowdParams.targetType = 'DMP_BASE';
                this.getOptionPage(this.addCrowdParams);
            }
            if(this.dh == 8){
                this.addCrowdParams.targetId = 152;
                this.addCrowdParams.targetType = 'DMP';
                this.getOptionPage(this.addCrowdParams);
            }
            this.pages = '';
            this.msg = '';
            this.lxxx = this.dh;
            return;
        }
        if(this.dh != -1){
            if(this.Parameter.length == 0){
                this.msg = '至少选择一个定向类型！';
                return
            }
            if(null == this.addCrowdParams.targetId){
                this.msg = '至少选择一个定向！';
                return
            }
            this.jllx = this.lxxx;
            this.lxxx = -1;
            this.pages = 'chujia';
            this.msg = '';
            return;
        }

    }

    //上一页
    previous = () =>{
        if(this.pages === 'chujia'){
            this.pages = '';
            this.lxxx = this.jllx;
            return;
        }
        if(this.pages === '' && this.lxxx != -1){
            this.pages = 'leixing';
            this.jllx = this.lxxx;
            this.lxxx = -1;
            this.addgjcs = [];
            this.Parameter = [];
            this.panduan();
            return;
        }
        if(this.jllx != -1 && this.pages === ''){
            this.pages = 'leixing';
            this.addgjcs = [];
            this.Parameter = [];
            this.panduan();
            return;
        }
        if(this.pages = 'leixing'){
            this.pages = 'danyuan';
            this.lxxx = -1;
            this.jllx = -1;
        }
    }

    panduan = () =>{
        if(517 == this.addCrowdParams.targetId){
            this.judgeZndx = false;
        }
        if(519 == this.addCrowdParams.targetId){
            this.judgeLxdxDp = false;
        }
        if(521 == this.addCrowdParams.targetId){
            this.judgeLxdxBB = false;
        }
        if(518 == this.addCrowdParams.targetId){
            this.judgeDp1 = false;
        }
        if(522 == this.addCrowdParams.targetId){
            this.judgeDp2 = false;
        }
    }

    // 选择定向类型
    determine = (type) => {
        this.dh = type;
    }

    addgjc = (params: any) =>{
        this.addCrowdParams.labelId = this.lxdx[0].labelId;
        this.addCrowdParams.labelValue = this.lxdx[0].labelValue;
        for(let i=0;i<this.lxdx[0].options.length;i++){
            if(this.lxdx[0].options[i].optionName === params.optionName){
                if(this.addgjcs.length>20){
                    break;
                }
                this.num = this.addgjcs.length+1;
                let offer = new Offer();
                offer.optionName = "购物意图定向"+this.lxdx[0].options[i].optionName;
                offer.price = null;
                offer.checked = true;
                offer.crowdName = '购物意图定向';
                offer.crowdDesc = this.lxdx[0].options[i].optionName;
                offer.optionValue = [];
                offer.optionValue.push(this.lxdx[0].options[i].optionValue);
                this.Parameter.push(offer);
                this.addgjcs.push(this.lxdx[0].options[i]);
                this.lxdx[0].options.splice(i,1);
            }
        }
    }

    deletegjc = (params: any) => {
        for(let i= 0; i<this.addgjcs.length;i++){
            if(this.addgjcs[i].optionName === params.optionName){
                this.lxdx[0].options.push(this.addgjcs[i]);
                this.addgjcs.splice(i,1);
                this.Parameter.splice(i,1);
                this.num = this.addgjcs.length;
            }
        }
    }

    selectCheckbox(check:boolean,value:any){
        if(check){
            let offer = new Offer();
            offer.checked = true;
            offer.price = null;
            offer.optionValue = [];
            if(this.dh == 7){
                this.addCrowdParams.labelId = this.dmpjx[0].labelId;
                this.addCrowdParams.labelValue = this.dmpjx[0].labelValue;
                if(this.dmpjxCount > 8){
                    return;
                }
                this.dmpjxCount = this.dmpjxCount + 1;
                offer.optionName = "达摩盘_平台精选-"+this.dmpjx[0].labelName+"："+value.optionName;
                offer.crowdName = '达摩盘_平台精选';
                offer.crowdDesc = this.dmpjx[0].labelName+"："+value.optionName;
                offer.optionValue.push(value.optionValue);
                this.Parameter.push(offer);
            }
            if(this.dh == 8){
                let name = '';
                if(null != value.options){
                    name = value.options.optionName;
                }
                offer.optionName ="达摩盘-"+value.labelName+"："+name;
                this.Parameter.push(offer);
            }
            if(this.dh == 6){
                if(this.cdxbbCount > 5){
                    return;
                }
                this.cdxbbCount = this.cdxbbCount + 1;
                if(this.Parameter.length <1){
                    offer.optionName = "喜欢我的宝贝的人群："+value.title;
                    offer.optionValue.push(value.itemId);
                    this.Parameter.push(offer);
                }else{
                    this.Parameter[0].optionValue.push(value.itemId);
                }


            }

        }
        if(!check){
            let idx = this.Parameter.indexOf(value.title);
            this.Parameter.splice(idx,1);
            if(this.dh == 6){
                this.cdxbbCount = this.cdxbbCount -1;
            }
            if(this.dh == 7) {
                this.dmpjxCount = this.dmpjxCount - 1;
            }
        }
    }

    saveCrowd = () =>{
        for(let i =0;i<this.Parameter.length;i++){
            this.addCrowdParams.crowdName = this.Parameter[i].crowdName;
            this.addCrowdParams.crowdDesc = this.Parameter[i].crowdDesc;
            this.addCrowdParams.price = this.Parameter[i].price;
            this.addCrowdParams.optionValue = this.Parameter[i].optionValue;
            this.addCrowdParams.checked = this.Parameter[i].checked;
            this.srv.saveCrowd(this.addCrowdParams).subscribe();
            if(i == this.Parameter.length -1){
                this.addCrowdModal.dismiss('Cross click');
                this.getCrowdRptdailylist(this.queryParams);
            }
        }
    }
    batchOffers = ()=> {
        for(let i=0;i< this.Parameter.length;i++){
            if(this.Parameter[i].checked){
                this.Parameter[i].price = this.batchOffer;
            }
        }
    }
}
