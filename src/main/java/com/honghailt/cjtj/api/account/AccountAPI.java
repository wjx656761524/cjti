package com.honghailt.cjtj.api.account;


import com.honghailt.cjtj.taobao.CompositeTaobaoClient;
import com.taobao.api.request.FeedflowAccountRpthourlistRequest.RptQueryDto ;

import com.taobao.api.request.FeedflowAccountGetRequest;
import com.taobao.api.request.FeedflowAccountRptdailylistRequest;
import com.taobao.api.request.FeedflowAccountRpthourlistRequest;
import com.taobao.api.response.FeedflowAccountGetResponse;
import com.taobao.api.response.FeedflowAccountRptdailylistResponse;
import com.taobao.api.response.FeedflowAccountRpthourlistResponse;
import com.taobao.api.response.FeedflowItemItemPageResponse;
import io.undertow.security.idm.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountAPI {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CompositeTaobaoClient  compositeTaobaoClient;
    /**
     * 获取信息流账户详情
     * @return account
     */
    public FeedflowAccountGetResponse.AccountDTO findAccountByAPI(String accessToken){
        FeedflowAccountGetResponse.AccountDTO accountDTO = new FeedflowAccountGetResponse.AccountDTO();
        FeedflowAccountRptdailylistRequest req = new FeedflowAccountRptdailylistRequest();
        FeedflowAccountGetRequest request = new FeedflowAccountGetRequest();
        FeedflowAccountGetResponse rsp =compositeTaobaoClient.execute(request, accessToken);
        FeedflowAccountGetResponse.ResultDTO resultDTO = rsp.getResult();
        if (resultDTO != null) {
            accountDTO = resultDTO.getResult();
        }
        return accountDTO;

    }

    /**
     *  超级推荐广告主分时报表查询
     * @param endHourId 	结束小时
     * @param logDate       查询日期
     * @param startHourId   开始小时
     * @return
     */
    public FeedflowAccountRpthourlistResponse findRptResultByHourByAPI(Long endHourId,
                                                               String logDate,Long startHourId,String accessToken){
        FeedflowAccountRpthourlistRequest req = new FeedflowAccountRpthourlistRequest();
        RptQueryDto obj1 = new RptQueryDto();
        obj1.setEndHourId(endHourId);
        obj1.setLogDate(logDate);
        obj1.setStartHourId(startHourId);
        req.setRptQuery(obj1);
        FeedflowAccountRpthourlistResponse rsp = compositeTaobaoClient.execute(req, accessToken);

       return rsp;



    }
    /**
     *  获取广告主分日数据
     * @param startTime 	查询开始日期
     * @param endTime      查询结束日期
     * @return
     */
    public  FeedflowAccountRptdailylistResponse  findRptResultByDayByAPI(String startTime,
                                                               String endTime,String accessToken){

        FeedflowAccountRptdailylistRequest req = new FeedflowAccountRptdailylistRequest();
        com.taobao.api.request.FeedflowAccountRptdailylistRequest.RptQueryDto obj1 = new com.taobao.api.request.FeedflowAccountRptdailylistRequest.RptQueryDto();
        obj1.setStartTime(startTime);
        obj1.setEndTime(endTime);
        req.setRptQueryDTO(obj1);
        FeedflowAccountRptdailylistResponse rsp = compositeTaobaoClient.execute(req, accessToken);

        return rsp;


    }


}
