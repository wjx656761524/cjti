package com.honghailt.cjtj.taobao;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.ListPage;
import com.honghailt.cjtj.taobao.exception.*;
import com.taobao.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * taobao client composite
 */
public class CompositeTaobaoClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeTaobaoClient.class);

    private final int BATCH_REQUEST_SIZE = 20;

    private TaobaoClient taobaoClient;

    private BatchTaobaoClient batchTaobaoClient;

    public CompositeTaobaoClient(TaobaoClient taobaoClient, BatchTaobaoClient batchTaobaoClient) {
        this.taobaoClient = taobaoClient;
        this.batchTaobaoClient = batchTaobaoClient;
    }

    /**
     * 执行单个api请求（无需sessionkey）
     *
     * @param request
     * @param <T>
     * @return
     */
    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request) {
        return this.execute(request, null);
    }

    /**
     * 异步执行单个api请求
     *
     * @param request
     * @param sessionkey
     * @param <T>
     * @return
     */
    @Async
    public <T extends TaobaoResponse> Future<T> executeAsync(TaobaoRequest<T> request, String sessionkey) {
        T response = execute(request, sessionkey);
        return new AsyncResult<>(response);
    }

    /**
     * 执行单个api请求
     *
     * @param request
     * @param sessionKey
     * @param <T>
     * @return
     */
    public <T extends TaobaoResponse> T execute(TaobaoRequest<T> request, String sessionKey) {
        T response = null;
        try {
            response = taobaoClient.execute(request, sessionKey);
        } catch (ApiException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("api异常", e);
            }
            transformError(e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getSubErrMsg());
        }

        //检查是否成功
        checkSuccess(response);

        return response;
    }

    /**
     * 异步批量执行api请求
     *
     * @param requests
     * @param sessionKey
     * @param <T>
     * @param <E>
     * @return
     */
    @Async
    public <T extends TaobaoRequest<E>, E extends TaobaoResponse> Future<List<E>> executeBatchAsync(List<T> requests, String sessionKey) {
        List<E> result = execute(requests, sessionKey);
        return new AsyncResult<>(result);
    }

    /**
     * 批量执行api请求
     *
     * @param requests
     * @param sessionKey
     * @param <T>
     * @param <E>
     * @return
     */
    public <T extends TaobaoRequest<E>, E extends TaobaoResponse> List<E> execute(List<T> requests, String sessionKey) {
        List<E> results = new ArrayList<>();

        if (CollectionUtils.isEmpty(requests)) {
            return Lists.newArrayList();
        }

        if (requests.size() == 1) {
            E response = this.execute(requests.get(0), sessionKey);
            results.add(response);
        } else {
            List<E> batchResponses = batchRequest(requests, sessionKey);
            results = batchResponses;
        }

        return results;
    }

    /**
     * 批量请求淘宝接口
     *
     * @param requests 请求列表，这里进行了封装，不再局限于淘宝每批次的20调用限制，但是如果每个单独的请求有其他限制需要自己处理
     * @return
     * @throws ApiException
     */
    public <T extends TaobaoRequest<E>, E extends TaobaoResponse> List<E> batchRequest(List<T> requests, String sessionKey) {
        String clazzName = requests != null ? requests.get(0).getClass().toString() : "";
        LOGGER.debug("收到批量请求，总数量[" + requests.size() + "]," + clazzName);
        List<E> allResponse = Lists.newArrayList();
        if (CollectionUtils.isEmpty(requests)) {
            return allResponse;
        }
        //淘宝接口每次接受20个请求
        ListPage<T> page = new ListPage<>(requests, BATCH_REQUEST_SIZE);
        while (page.hasNext()) {
            List<T> batchRequestList = page.next();
            TaobaoBatchRequest batchRequest = new TaobaoBatchRequest();
            for (T taobaoRequest : batchRequestList) {
                batchRequest.addRequest(taobaoRequest);
            }
            TaobaoBatchResponse batchResponse = null;
            LOGGER.debug("开始批量请求请求...");
            long startTime = System.currentTimeMillis();
            try {
                batchResponse = batchTaobaoClient.execute(batchRequest, sessionKey);
            } catch (ApiException e) {
                LOGGER.error("批量请求失败，耗时" + (System.currentTimeMillis() - startTime) + "ms,响应:" + batchResponse.getMsg());
                transformError(e.getErrCode(), e.getErrMsg(), e.getSubErrCode(), e.getSubErrMsg());
            }
            checkSuccess(batchResponse);
            checkSuccess(batchResponse.getResponseList());
            LOGGER.debug(clazzName + "批量请求成功，耗时" + (System.currentTimeMillis() - startTime) + "ms");
            allResponse.addAll((List<E>) batchResponse.getResponseList());
        }
        return allResponse;
    }

    /**
     * 检查api响应是否成功
     *
     * @param response
     * @param <T>
     */
    private <T extends TaobaoResponse> void checkSuccess(T response) {
        if (!response.isSuccess()) {
            String errorCode = response.getErrorCode();
            String errorMsg = response.getMsg();
            String subErrorCode = response.getSubCode();
            String subErrorMsg = response.getSubMsg();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("errorCode [{}] ,errorMsg [{}] ,subErrorCode [{}] ,subErrorMsg [{}]", errorCode, errorMsg, subErrorCode, subErrorMsg);
            }
            transformError(errorCode, errorMsg, subErrorCode, subErrorMsg);
        }
    }

    /**
     * 检查api响应是否成功
     *
     * @param responses
     * @param <T>
     */
    private <T extends TaobaoResponse> void checkSuccess(List<T> responses) {
        if (responses == null) {
            return;
        }
        for (T response : responses) {
            checkSuccess(response);
        }
    }

    /**
     * 转换错误码为自定义异常类型
     *
     * @param errorCodeStr
     * @param errorMsg
     * @param subErrorCode
     * @param subErrorMsg
     */
    private void transformError(String errorCodeStr, String errorMsg, String subErrorCode, String subErrorMsg) throws RuntimeException {
        ApiException result;

        if (errorCodeStr == null || "".equals(errorCodeStr)) {
            //一般就是连接超时了，直接没有错误码
            throw new RuntimeException(new UnknownException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg));
        }

        int errorCode = Integer.valueOf(errorCodeStr);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("errorCode [{}] ,errorMsg [{}] ,subErrorCode [{}] ,subErrorMsg [{}]", errorCode, errorMsg, subErrorCode, subErrorMsg);
        }

        if (subErrorMsg != null && subErrorMsg.contains("推广组未找到")) {
            //推广组不存在
            result = new AdgroupNotExistException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg);
        } else if (errorCode == 40
            || errorCode == 41
            || (subErrorCode != null && subErrorCode.startsWith("isv.") && (errorCode > 100 || errorCode == 15))) {
            //isv 异常
            result = new ISVException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg);
        } else if (errorCode < 100 && errorCode != 15 && errorCode != 40 && errorCode != 41) {

            if (errorCode == 12) {
                //子帐号无权限
                result = new SubuserNoPermissionException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg);
            } else if (errorCode == 7) {
                //流控
                result = new CallLimitedException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg);
            } else if (errorCode == 26 || errorCode == 27) {
                //sessionkey 异常
                result = new SessionInvalidException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg);
            } else {
                //平台异常
                result = new ISPException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg);
            }
        } else if (subErrorCode == null && errorCode == 15) {
            // 开放平台远程服务异常
            result = new ISPException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg);
        } else {
            result = new UnknownException(errorCodeStr, errorMsg, subErrorCode, subErrorMsg);
        }

        throw new APIExceptionWrapper(result);
    }
}
