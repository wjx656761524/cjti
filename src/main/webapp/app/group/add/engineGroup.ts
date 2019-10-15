import {Injectable} from '@angular/core';

@Injectable()
export class EngineGroup {

    public nick: string;//店铺名称
    public campaignId: number; //计划id
    public campaignName: string;//计划名称
    public discount:number;//溢价
    public groupId: number;//单元id
    public groupName: string;//单元名称
    public groupStatus :string//单元状态
    public adzoneId: number;//广告id
    public adzoneName: string;//广告名称
    public groups:any[]=[]; //单元对象
    public campaign:any; //计划
    public items:any[]=[];//选择的推广宝贝
    public itemchecked:boolean=true;//推广宝贝控制显示
    public locations:any;//绑定的资源位
    public creatives:any[]=[];//创意
    public directionalUnit: any[]=[]; // 单元定向
}
