
export class CrowdList {
    public price?: number; // 出价
    public crowdName?: string; //人群名称
    public crowdDesc?: string; // 人群描述
    public optionName?: string;  // 选项名称
    public directionalLabel?: DirectionalLabel;
}

export class DirectionalLabel {
    public labelId?: number;  // 标签id
    public targetId?: number; // 定向id
    public targetType?: string; // 定向类型
    public labelValue?: string; // 标签值
    public options?: Array<Options> = new Array<Options>();
}

export class Options {
    public optionName?: string;  // 选项名称
    public optionValue?: string; // 选项值
    public optionDesc?: string; // 选项描述
    public checked?: boolean; // 是否选中
}
