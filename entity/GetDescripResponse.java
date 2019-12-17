package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetDescripResponse {
    private NetworkResult result;
    private GetDescripData data;

    public NetworkResult getResult() {
        return result;
    }

    public void setResult(NetworkResult result) {
        this.result = result;
    }

    public GetDescripData getData() {
        return data;
    }

    public void setData(GetDescripData data) {
        this.data = data;
    }
}
