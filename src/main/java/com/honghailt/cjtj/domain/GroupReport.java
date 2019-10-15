package com.honghailt.cjtj.domain;

import com.taobao.api.response.FeedflowItemAdgroupRptdailylistResponse;
import com.taobao.api.response.FeedflowItemAdgroupRptdailylistResponse.RptResultDto;


import javax.swing.*;
import java.text.SimpleDateFormat;

/**
 * @Author: WujinXian
 * @Description:
 * @Date: Created in 19:23 2019/5/11
 * @Modified By
 */
public class GroupReport extends AbstractReport {

    private Long compaignId;

    private Long groupId;


    public GroupReport() {
    }
    public GroupReport(RptResultDto rptResultDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 消耗
        this.charge=  Double.parseDouble(rptResultDto.getCharge());
        // 展现量
        this.adPv=rptResultDto.getAdPv();
        // 点击量
        this.click=rptResultDto.getClick();
        // 千人展现成本
        this.ecpm=Double.parseDouble(rptResultDto.getEcpm());
        // 点击成本
        this.ecpc= Double.parseDouble(rptResultDto.getEcpc());
        // 统计时间
        this.logDate=sdf.format(rptResultDto.getLogDate());
        // 点击转化率
       this.cvr= Double.parseDouble(rptResultDto.getIcvr());
        // 投资回报率
         this.roi=Double.parseDouble(rptResultDto.getRoi());
        // 收藏宝贝量
       this.inshopItemColNum=rptResultDto.getInshopItemColNum();
        // 添加购物车量
       this.cartNum=rptResultDto.getCartNum();
       // 成交订单金额
      this.alipayInshopAmt=Double.parseDouble(rptResultDto.getAlipayInshopAmt());
      // 成交订单数
      this.alipayInShopNum=rptResultDto.getAlipayInShopNum();


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
