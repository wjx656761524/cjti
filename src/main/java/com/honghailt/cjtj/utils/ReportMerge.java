package com.honghailt.cjtj.utils;

import com.honghailt.cjtj.domain.AbstractReport;

import java.text.DecimalFormat;
import java.util.Collection;

/**
 * 报表合并类
 *
 * @param <T>
 */
public class ReportMerge<T extends AbstractReport> {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    private Collection<T> reports;

    // 消耗
    protected Double charge = 0.0;
    // 展现量
    protected Long adPv = 0l;
    // 点击量
    protected Long click = 0l;
    // 点击转化率
    protected Double cvr = 0d;
    // 千人展现成本
    protected Double ecpm = 0.0;
    // 点击成本
    protected Double ecpc = 0.0;
    // 收藏宝贝量
    protected Long inshopItemColNum = 0l;
    // 添加购物车量
    protected Long cartNum = 0l;
    // 投资回报率
    protected Double roi = 0d;
    // 成交订单金额
    protected Double alipayInshopAmt = 0.0;
    // 点击率
    protected Double ctr = 0d;
    // 成交订单数
    protected Long alipayInShopNum = 0l;

    private T result;

    public ReportMerge(Collection<T> reports, T result) {
        this.reports = reports;
        this.result = result;
    }

    public T getMergedReport() {

        //计算合并值
        compute();

        //填充值
        fillPropertyValue();

        return result;
    }


    private void compute() {
        for (AbstractReport report : reports) {
            charge += safeDouble(report.getCharge());
            adPv += safeNumber(report.getAdPv());
            click += safeNumber(report.getClick());
            inshopItemColNum += safeNumber(report.getInshopItemColNum());
            cartNum += safeNumber(report.getCartNum());
            alipayInshopAmt += safeDouble(report.getAlipayInshopAmt());
            alipayInShopNum += safeNumber(report.getAlipayInShopNum());
        }

    }

    private void fillPropertyValue() {
        result.setCharge(charge);
        result.setAdPv(adPv);
        result.setClick(click);
        result.setInshopItemColNum(inshopItemColNum);
        result.setCartNum(cartNum);
        result.setAlipayInshopAmt(alipayInshopAmt);
        result.setAlipayInShopNum(alipayInShopNum);

        ReportUtils.fillCustomProperty(result);
    }

    private long safeNumber(Long num) {
        return num != null ? num : 0;
    }


    private Double safeDouble(Double num) {
        return num != null ? num : 0.0;
    }

}
