import {OperationSource} from "app/shared/enum/operation-source.enum";
import {OperationType} from "app/shared/enum/operation-type.enum";

export class UpdateGroupModel {

    /**
     * 店铺账号昵称
     */
    public nick? : string;

    /**
     * 推广计划ID
     */
    public campaignId? : number;

    /**
     * 推广单元ID
     */
    public groupId? : number;

    /**
     * 修改前的推广状态
     */
    public oldStatus? : string;

    /**
     * 修改后的推广状态
     */
    public newStatus? : string;

    /**
     * 修改前的单元名称
     */
    public oldGroupName? : string;

    /**
     * 修改后的单元名称
     */
    public newGroupName? : string;

    /**
     * 操作源
     */
    public operationSource? : OperationSource;

    /**
     * 操作类型
     */
    public operationType? : OperationType;

    /**
     * 操作结果
     */
    public operationReason? : string;
}
