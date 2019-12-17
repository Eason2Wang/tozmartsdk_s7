package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetServerMsgResponse {
    private NetworkResult result;
    private GetDescripItem data;

    public NetworkResult getResult() {
        return result;
    }

    public void setResult(NetworkResult result) {
        this.result = result;
    }

    public GetDescripItem getData() {
        return data;
    }

    public void setData(GetDescripItem data) {
        this.data = data;
    }
}
