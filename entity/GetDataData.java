package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetDataData {
    private String taskId;
    private int status;
    private GetDataApiData apiData;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public GetDataApiData getApiData() {
        return apiData;
    }

    public void setApiData(GetDataApiData apiData) {
        this.apiData = apiData;
    }
}
