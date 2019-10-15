export class AdgroupStatus {
    public nick:string;//店铺名称

    public groupId: number;//单元id
    public groupName: string;//单元名称

    public campaignId: number;//  计划id
    public  campaignName: string;//  计划名称

    public itemId: number;//商品id
    public itemName:string;//商品名称

    public locations: any[]=[];//资源位
    public labelId:number; //标签id
    public labelValue:number;//标签值

    public directionalUnit: any[]=[]; // 新建单元定向


}

