package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetDataApiData {
    private String apiMsg;
    private int apiStatus;
    private String apiCode;
    private String traceId;
    private GetDataResponseData responseData;

    public String getApiMsg() {
        return apiMsg;
    }

    public void setApiMsg(String apiMsg) {
        this.apiMsg = apiMsg;
    }

    public int getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(int apiStatus) {
        this.apiStatus = apiStatus;
    }

    public GetDataResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(GetDataResponseData responseData) {
        this.responseData = responseData;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
