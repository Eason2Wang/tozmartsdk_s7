package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class ProcessImageData {
    private String taskId;
    private ImageErrorWarnInfo[] errorInfo;
    private ImageErrorWarnInfo[] warnInfo;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ImageErrorWarnInfo[] getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ImageErrorWarnInfo[] errorInfo) {
        this.errorInfo = errorInfo;
    }

    public ImageErrorWarnInfo[] getWarnInfo() {
        return warnInfo;
    }

    public void setWarnInfo(ImageErrorWarnInfo[] warnInfo) {
        this.warnInfo = warnInfo;
    }
}
