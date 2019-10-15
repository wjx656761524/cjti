export const REPORT_FIELDS = [
    {field: 'adPv', title: '展现量', jhiSortBy: 'report.adPv', tooltip: '选定时间内的信息流广告展现总量。',isLZY: false},
    {field: 'click', title: '点击量', jhiSortBy: 'report.click', tooltip: '选定时间内的信息流广告点击总量。',isLZY: false},
    {field: 'ctr', title: '点击率(%)', jhiSortBy: 'report.ctr', tooltip: '点击率 = 点击量 / 展现量 * 100%。',isLZY: false},
    {field: 'ecpm', title: '千次展现成本', jhiSortBy: 'report.ecpm', tooltip: '千次展现成本 = 消耗 / 展现量 * 1000。',isLZY: false},
    {field: 'ecpc', title: '点击成本', jhiSortBy: 'report.ecpc', tooltip: '点击成本 = 消耗 / 点击量。',isLZY: false},
    {field: 'charge', title: '消耗', jhiSortBy: 'report.charge', tooltip: '选定时间内的信息流广告总花费。',isLZY: false},
    {field: 'inshopItemColNum', title: '收藏宝贝量', jhiSortBy: 'report.inshopItemColNum', tooltip: '通过展现/点击广告之后，一段时间周期内，带来的宝贝收藏量。',isLZY: false},
    {field: 'cartNum', title: '添加购物车量', jhiSortBy: 'report.cartNum', tooltip: '通过展现/点击广告之后，一段时间周期内，带来的购物车添加量。',isLZY: false},
    {field: 'roi', title: '投资回报率', jhiSortBy: 'report.roi', tooltip: '投资回报率 = 支付宝总成交金额 / 消耗',isLZY: false},
    {field: 'alipayInShopNum', title: '成交订单量', jhiSortBy: 'report.roi', tooltip: '投资回报率 = 支付宝总成交金额 / 消耗',isLZY: false},
    {field: 'alipayInshopAmt', title: '成交订单金额', jhiSortBy: 'report.roi', tooltip: '投资回报率 = 支付宝总成交金额 / 消耗',isLZY: false},
    {field: 'cvr', title: '点击转化率', jhiSortBy: 'report.roi', tooltip: '投资回报率 = 支付宝总成交金额 / 消耗',isLZY: false}
];

export const DEFAULT_COLUMNS = ['adPv', 'click', 'ctr', 'ecpm', 'ecpc', 'charge', 'inshopItemColNum', 'cartNum', 'roi','alipayInShopNum', 'alipayInshopAmt','cvr'];
export const OPTION_RESULTS=[
    {auditStatus:'W',result:'待审核'},
    {auditStatus:'P',result:'审核通过'},
    {auditStatus:'R',result:'审核拒绝'},

]
