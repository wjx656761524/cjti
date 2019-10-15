export class CreativeModel {

    public sick: string;//店铺名称

    public campaignId: number;//计划id
    public campaignName: string;//计划名称
    public discount:number;//溢价

    public groupId: number;//单元id

    public groupName: string;//单元名称
    public creativeId: number;//创意id
    public adzoneName: string;//创意名称
    public  creatives:any[]=[]
    public groupStatus :string;//单元状态
    public itemId:any;//宝贝id

    /**
     * 图片地址
     */
    public imgUrl:string;

    /**
     * 创意文案
     */
    public  title :string;

    /**
     * 审核状态，W待审核，P审核通过，R审核拒绝
     */

    public auditStatus:string;

    /**
     * 审核拒绝原因
     */

    public auditReason:string;

    /**
     * 创意名称
     */

    public creativeName:string;

    public  deleted :number = 0;










}
