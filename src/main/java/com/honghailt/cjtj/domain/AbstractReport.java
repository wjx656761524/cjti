package com.honghailt.cjtj.domain;

/**
 *  报表抽象类
 */
public class AbstractReport {

    // 店铺的昵称
    protected String nick;
    // 统计时间
    protected String logDate;
    // 小时
    protected Long hourId;
    // 消耗
    protected Double charge;
    // 展现量
    protected Long adPv;
    // 点击量
    protected Long click;
    // 点击转化率
    protected Double cvr;
    // 千次展现成本
    protected Double ecpm;
    // 点击成本
    protected Double ecpc;
    // 收藏宝贝量
    protected Long inshopItemColNum;
    // 添加购物车量
    protected Long cartNum;
    // 投资回报率
    protected Double roi;
    // 成交订单金额
    protected Double alipayInshopAmt;
    // 点击率
    protected Double ctr;
    // 成交订单数
    protected Long alipayInShopNum;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }


    public Long getAdPv() {
        if(null == adPv) adPv = 0L;
        return adPv;
    }

    public void setAdPv(Long adPv) {
        this.adPv = adPv;
    }

    public Long getClick() {
        if(null == click) click = 0L;
        return click;
    }

    public void setClick(Long click) {
        this.click = click;
    }

    public Double getCvr() {
        if(null == cvr) cvr = 0.0;
        return cvr;
    }

    public void setCvr(Double cvr) {
        this.cvr = cvr;
    }


    public Long getInshopItemColNum() {
        if(null == inshopItemColNum) inshopItemColNum = 0L;
        return inshopItemColNum;
    }

    public void setInshopItemColNum(Long inshopItemColNum) {
        this.inshopItemColNum = inshopItemColNum;
    }

    public Long getCartNum() {
        if(null == cartNum) cartNum = 0L;
        return cartNum;
    }

    public void setCartNum(Long cartNum) {
        this.cartNum = cartNum;
    }

    public Double getRoi() {
        if(null == roi) roi = 0.0;
        return roi;
    }

    public void setRoi(Double roi) {
        this.roi = roi;
    }

    public Double getCtr() {
        if(null == ctr) ctr = 0.0;
        return ctr;
    }

    public void setCtr(Double ctr) {
        this.ctr = ctr;
    }

    public Long getAlipayInShopNum() {
        if(null == alipayInShopNum) alipayInShopNum = 0L;
        return alipayInShopNum;
    }

    public void setAlipayInShopNum(Long alipayInShopNum) {
        this.alipayInShopNum = alipayInShopNum;
    }

    public Long getHourId() {
        if(null == hourId) hourId = 0L;
        return hourId;
    }

    public void setHourId(Long hourId) {
        this.hourId = hourId;
    }

    public Double getCharge() {
        if(null == charge) charge = 0.0;
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Double getEcpm() {
        if(null == ecpm) ecpm = 0.0;
        return ecpm;
    }

    public void setEcpm(Double ecpm) {
        this.ecpm = ecpm;
    }

    public Double getEcpc() {
        if(null == ecpc) ecpc = 0.0;
        return ecpc;
    }

    public void setEcpc(Double ecpc) {
        this.ecpc = ecpc;
    }

    public Double getAlipayInshopAmt() {
        if(null == alipayInshopAmt) alipayInshopAmt = 0.0;
        return alipayInshopAmt;
    }

    public void setAlipayInshopAmt(Double alipayInshopAmt) {
        this.alipayInshopAmt = alipayInshopAmt;
    }
}
