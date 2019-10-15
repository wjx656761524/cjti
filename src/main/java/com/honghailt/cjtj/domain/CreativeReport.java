package com.honghailt.cjtj.domain;

import com.taobao.api.response.FeedflowItemAdzoneRptdailylistResponse;
import com.taobao.api.response.FeedflowItemCreativeRptdailylistResponse;

import java.text.SimpleDateFormat;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 14:54 2019/5/20
 * @Modified By
 */
public class CreativeReport extends AbstractReport{
    private Long compaignId;

    private Long groupId;


    public CreativeReport() {
    }
    public CreativeReport(  FeedflowItemCreativeRptdailylistResponse.RptResultDto rptResultDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 消耗
        this.charge=  Double.parseDouble(rptResultDto.getCharge()==null?"0.0":rptResultDto.getCharge());
        // 展现量
        this.adPv=rptResultDto.getAdPv() == null?0L:rptResultDto.getAdPv() ;
        // 点击量
        this.click=rptResultDto.getClick() == null?0L:rptResultDto.getClick();
        // 千人展现成本
        this.ecpm=Double.parseDouble(rptResultDto.getEcpm()==null?"0.0":rptResultDto.getEcpm());
        // 点击成本
        this.ecpc= Double.parseDouble(rptResultDto.getEcpc()==null?"0.0":rptResultDto.getEcpc());
        // 统计时间
        this.logDate=sdf.format(rptResultDto.getLogDate());
        // 点击转化率
        this.cvr= Double.parseDouble(rptResultDto.getIcvr() ==null?"0.0":rptResultDto.getIcvr());
        // 投资回报率
        this.roi=Double.parseDouble(rptResultDto.getRoi() ==null?"0.0":rptResultDto.getRoi());
        // 收藏宝贝量
        this.inshopItemColNum=rptResultDto.getInshopItemColNum()==null?0L:rptResultDto.getInshopItemColNum();
        // 添加购物车量
        this.cartNum=rptResultDto.getCartNum()==null?0L:rptResultDto.getCartNum();
        // 成交订单金额
        this.alipayInshopAmt=Double.parseDouble(rptResultDto.getAlipayInshopAmt()==null?"0.0":rptResultDto.getAlipayInshopAmt());
        // 成交订单数
        this.alipayInShopNum=rptResultDto.getAlipayInShopNum()==null?0L:rptResultDto.getAlipayInShopNum();


    }
    public Long getCompaignId() {
        return compaignId;
    }

    public void setCompaignId(Long compaignId) {
        this.compaignId = compaignId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }



}
