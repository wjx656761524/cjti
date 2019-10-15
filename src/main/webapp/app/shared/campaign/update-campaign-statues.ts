
export class UpdateCampaignStatusModel {

    /**
     * 店铺账号昵称
     */
    public sick?: string;

    public dayBudget?: number;

    public ids?:string;

    /**
     * 推广计划ID
     */
    public campaignId?: number;


    /**
     * 推广单元ID
     */
    public groupId?: number;

    public status?:string;

    /**
     * 计划名称
     */
    public campaignName?: string;

    /**
     * 投放开始时间
     */
    public beginTime?: string;

    /**
     * 投放结束时间
     */
    public endTime?: string;


    private deleted?: string;

}
