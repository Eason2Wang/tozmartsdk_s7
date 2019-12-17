package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetDataResponse {
    private String code;
    private String codeDesc;
    private String msg;
    private GetDataData data;

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

    public GetDataData getData() {
        return data;
    }

    public void setData(GetDataData data) {
        this.data = data;
    }
}
