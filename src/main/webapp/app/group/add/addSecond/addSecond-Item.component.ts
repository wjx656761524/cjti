import {Component, ViewChild, ComponentFactoryResolver, OnInit, Output, EventEmitter, Input} from '@angular/core';
import {NgbActiveModal, NgbDate} from '@ng-bootstrap/ng-bootstrap';
import {Principal} from "app/shared/auth/principal.service";
import {CjtjNotifyService} from "app/shared/notify/notify.service";
import {ItemService} from "app/shared/Item/item.service";
import {PAGE_SIZE} from "app/app.constants";
import Any = jasmine.Any;
import {EngineGroup} from "app/group/add/engineGroup";


@Component({
    selector: 'cjtj-addSecond-Item',
    templateUrl: './addSecond-Item.component.html',
})

export class AddSecondItemComponent implements OnInit {
    account: any; // 当前账户
    pageSize = PAGE_SIZE;
    campaignId:any//单元ID
    allItem:any[]=[];//指定计划除现有单元宝贝外的其他宝贝
    queryParams: any = { // 过滤参数
        itemName: '', // 标题
    }; // 过滤参数
    filteredItems:any[]=[];//过滤后的指定计划除现有单元宝贝外的其他宝贝
   items: any[] = []; // 当前页显示的
    page=1;
    allSelect=false;
    selectedItem:any[]=[]//选中的宝贝id
    @ViewChild('pagination') pagination: any;
    constructor(private componentFactoryResolver: ComponentFactoryResolver,
                public activeModal: NgbActiveModal,
                private cjtjNotifyService: CjtjNotifyService,
                public principal: Principal,
                public itemService:ItemService,
    public engineGroup: EngineGroup

    ) {
    }

    ngOnInit(): void {
        this.principal.identity(false).then((account) => {
            this.account = account;
           this.loadItems(this.campaignId,true)

        });

    }

    /**
     * 加载指定计划除现有单元宝贝的其他宝贝
     * @param campaignId
     * @param syn
     */
    loadItems(campaignId:number,syn:boolean){

        this.itemService.getItemDetailsByCampaignId(campaignId,syn).subscribe(
            (res) => {
                this.allItem = res;
                this.query();

            }
        );

    }
    /**
     * 查询过滤
     */
    query() {
        this.setIUnitName(this.allItem);
        this.changeItemsChecked(this.allItem, false);
        const filteredItems = this.allItem.filter((item: any) => this.queryParams.itemName === '' || item.title .includes(this.queryParams.itemName));
        this.filteredItems = filteredItems;
        this.setItemsChecked(this.items, false);
        this.pageChange({page: 1, pageSize: this.pageSize});
        this.pagination.restPage(1);

    }
    changeItemsChecked(items, checked) {
        if (items) {
            for (const item of items) {
                item.checked = checked;
            }
        }
    }

    /**
     *
     * @param items
     * @param checked
     */

    setItemsChecked(items, checked) {
        for (const item of items) {
            item.checked = checked;
        }
    }
    /**
     * 选择页
     * @param showData
     */
    pageChange(pageInfo) {
        this.items = this.getShowData(pageInfo);
    }

    getShowData(pageInfo): any[] {
        const pageNumber = pageInfo.page;
        const pageSize = pageInfo.pageSize;
        let showDataSet = [];
        let start = -1;
        let end = 0;
        if (this.filteredItems && this.filteredItems.length > 0) {
            start = (pageNumber - 1) * pageSize;
            end = pageNumber * pageSize > this.filteredItems.length ? this.filteredItems.length : pageNumber * pageSize;
            showDataSet = this.filteredItems.slice(start, end);
        }
        return showDataSet;
    }
    clickSelectAll() {
        if (this.allSelect) {
            this.setItemsChecked(this.items, true);
        } else {
            this.setItemsChecked(this.items, false);
        }
    }

    /**
     * 确定前的检查
     */
    popoverCheck = () => {
        const selectIds = this.getSelectIds();
        if (selectIds.length === 0) {
            this.cjtjNotifyService.openMessage('请先选择要操作的推广宝贝');
            return false;
        } else {
            return true;
        }

    }

    /**
     * 获取选中的商品id
     */
    getSelectIds() {
        const selectIds = [];
        for (const item of this.items) {
            if (item.checked) {
                selectIds.push(item.id);
                this.selectedItem.push(item);
            }
        }

        return selectIds;
    }

    /**
     * 确定
     */
    submit(){
        const selectedItem=this.selectedItem;
        this.engineGroup.items=selectedItem;
        this.activeModal.close(this.engineGroup.items.length);
    }
    check(){

        this.activeModal.close(this.engineGroup.items.length)
    }

    /**
     *  添加单元名称
     * @param items
     * @param checked
     */
    setIUnitName(items) {
        for (const item of items) {
            item.unitName =item.title;
            item.creativeName =item.title
        }
    }
}
