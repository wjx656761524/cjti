package com.honghailt.cjtj.service;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.api.account.AccountAPI;
import com.honghailt.cjtj.domain.AbstractReport;
import com.honghailt.cjtj.domain.TaobaoUserDetails;
import com.honghailt.cjtj.security.SecurityUtils;
import com.honghailt.cjtj.utils.ReportUtils;
import com.honghailt.cjtj.web.rest.vm.StatusAndReportVM;
import com.taobao.api.response.FeedflowAccountRptdailylistResponse;
import com.taobao.api.response.FeedflowAccountRpthourlistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountAPI accountAPI;

    // 历史投放数据
    public List<StatusAndReportVM> findRptResultByDayByAPI(String startTime, String endTime){
        List<StatusAndReportVM> statusAndReportVMList = Lists.newArrayList();
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        List<String> stringList = getDays(startTime,endTime);
        for (String data:stringList) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            FeedflowAccountRptdailylistResponse rsp = accountAPI.findRptResultByDayByAPI(data,data,details.getSessionKey());
            if(null == rsp.getResult().getRptList()){
                AbstractReport abstractReport = isEmpty(data,null);
                StatusAndReportVM  statusAndReportVM = new StatusAndReportVM(null,abstractReport,null);
                statusAndReportVMList.add(statusAndReportVM);
            }else {
                AbstractReport abstractReport = new AbstractReport();
                for (FeedflowAccountRptdailylistResponse.RptResultDto rptResultDto :rsp.getResult().getRptList()) {
                    abstractReport.setAdPv(rptResultDto.getAdPv()== null? 0L:rptResultDto.getAdPv());
                    abstractReport.setAlipayInshopAmt(Double.parseDouble(rptResultDto.getAlipayInshopAmt() == null?"0.0":rptResultDto.getAlipayInshopAmt()));
                    abstractReport.setAlipayInShopNum(rptResultDto.getAlipayInShopNum() == null? 0L: rptResultDto.getAlipayInShopNum());
                    abstractReport.setCartNum(rptResultDto.getCartNum() == null? 0L:rptResultDto.getCartNum());
                    abstractReport.setCharge(Double.parseDouble(rptResultDto.getCharge() == null?"0.0": rptResultDto.getCharge() ));
                    abstractReport.setClick(rptResultDto.getClick() == null? 0L: rptResultDto.getClick());
                    abstractReport.setCvr(Double.parseDouble(rptResultDto.getCvr() == null ? "0.0": rptResultDto.getCvr()));
                    abstractReport.setEcpc(Double.parseDouble(rptResultDto.getEcpc() == null ? "0.0": rptResultDto.getEcpc()));
                    abstractReport.setEcpm(Double.parseDouble(rptResultDto.getEcpm() == null? "0.0": rptResultDto.getEcpm()));
                    abstractReport.setInshopItemColNum(rptResultDto.getInshopItemColNum() == null ? 0L: rptResultDto.getInshopItemColNum());
                    abstractReport.setLogDate(sdf.format(rptResultDto.getLogDate()));
                    abstractReport.setRoi(Double.parseDouble(rptResultDto.getRoi() == null ? "0.0": rptResultDto.getRoi()));
                    ReportUtils.fillCustomProperty(abstractReport);
                }
                StatusAndReportVM  statusAndReportVM = new StatusAndReportVM(null,abstractReport,null);
                statusAndReportVMList.add(statusAndReportVM);
            }
        }
        return statusAndReportVMList;
    }

    // 单日投放数据
    public List<StatusAndReportVM>  findRpthourlist(Long endHourId,String logDate,Long startHourId){
        List<StatusAndReportVM> statusAndReportVMList = Lists.newArrayList();
        TaobaoUserDetails details = SecurityUtils.getCurrentUser();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        FeedflowAccountRpthourlistResponse rsp = accountAPI.findRptResultByHourByAPI(endHourId,logDate,startHourId,details.getSessionKey());
        if(null == rsp.getResult().getRptList()){
            for(int i=0;i<24;i++){
                AbstractReport abstractReport = isEmpty(logDate,Long.parseLong(i+""));
                StatusAndReportVM  statusAndReportVM = new StatusAndReportVM(null,abstractReport,null);
                statusAndReportVMList.add(statusAndReportVM);
            }
        }else{
            for(int i=0;i<24;i++){
                int t = 0;
                for(int j=0;j<rsp.getResult().getRptList().size();j++){
                    if(i == rsp.getResult().getRptList().get(j).getHourId()){
                        AbstractReport abstractReport = new AbstractReport();
                        abstractReport.setAdPv(rsp.getResult().getRptList().get(j).getAdPv() == null? 0L: rsp.getResult().getRptList().get(j).getAdPv());
                        abstractReport.setAlipayInshopAmt(Double.parseDouble(rsp.getResult().getRptList().get(j).getAlipayInshopAmt() == null? "0.0": rsp.getResult().getRptList().get(j).getAlipayInshopAmt()));
                        abstractReport.setAlipayInShopNum(rsp.getResult().getRptList().get(j).getAlipayInShopNum() == null ? 0L: rsp.getResult().getRptList().get(j).getAlipayInShopNum());
                        abstractReport.setCartNum(rsp.getResult().getRptList().get(j).getCartNum() == null ? 0L: rsp.getResult().getRptList().get(j).getCartNum());
                        abstractReport.setCharge(Double.parseDouble(rsp.getResult().getRptList().get(j).getCharge() == null ? "0.0": rsp.getResult().getRptList().get(j).getCharge()));
                        abstractReport.setClick(rsp.getResult().getRptList().get(j).getClick() == null ? 0L: rsp.getResult().getRptList().get(j).getClick());
                        abstractReport.setCvr(Double.parseDouble(rsp.getResult().getRptList().get(j).getCvr() == null ? "0.0": rsp.getResult().getRptList().get(j).getCvr()));
                        abstractReport.setEcpc(Double.parseDouble(rsp.getResult().getRptList().get(j).getEcpc() == null ? "0.0": rsp.getResult().getRptList().get(j).getEcpc()));
                        abstractReport.setEcpm(Double.parseDouble(rsp.getResult().getRptList().get(j).getEcpm() == null? "0.0": rsp.getResult().getRptList().get(j).getEcpm() ));
                        abstractReport.setInshopItemColNum(rsp.getResult().getRptList().get(j).getInshopItemColNum() == null? 0L: rsp.getResult().getRptList().get(j).getInshopItemColNum());
                        abstractReport.setLogDate(sdf.format(rsp.getResult().getRptList().get(j).getLogDate()));
                        abstractReport.setRoi(Double.parseDouble(rsp.getResult().getRptList().get(j).getRoi() == null ? "0.0": rsp.getResult().getRptList().get(j).getRoi()));
                        ReportUtils.fillCustomProperty(abstractReport);
                        StatusAndReportVM  statusAndReportVM = new StatusAndReportVM(null,abstractReport,null);
                        statusAndReportVMList.add(statusAndReportVM);
                        t = 1;
                        break;
                    }
                }
                if(t == 0){
                    AbstractReport abstractReport = isEmpty(logDate,Long.parseLong(i+""));
                    StatusAndReportVM  statusAndReportVM = new StatusAndReportVM(null,abstractReport,null);
                    statusAndReportVMList.add(statusAndReportVM);
                }
            }
        }


        return statusAndReportVMList;
    }


    // 如果为空赋默认值
    public AbstractReport isEmpty(String logDate,Long hourId){
        AbstractReport abstractReport = new AbstractReport();
        abstractReport.setAdPv(0L);
        abstractReport.setAlipayInshopAmt(0.0);
        abstractReport.setAlipayInShopNum(0L);
        abstractReport.setCartNum(0L);
        abstractReport.setCharge(0.0);
        abstractReport.setClick(0L);
        abstractReport.setCvr(0.0);
        abstractReport.setEcpc(0.0);
        abstractReport.setEcpm(0.0);
        abstractReport.setInshopItemColNum(0L);
        abstractReport.setLogDate(logDate);
        if(null != hourId) abstractReport.setHourId(hourId);
        abstractReport.setRoi(0.0);
        abstractReport.setCtr(0.0);
        return abstractReport;
    }


    /**
     * 获取两个日期之间的所有日期
     *
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }
}
