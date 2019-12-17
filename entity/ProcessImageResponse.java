package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 18/1/5.
 */

public class ProcessImageResponse {
    private String code;
    private String codeDesc;
    private String msg;
    private ProcessImageData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ProcessImageData getData() {
        return data;
    }

    public void setData(ProcessImageData data) {
        this.data = data;
    }
}
