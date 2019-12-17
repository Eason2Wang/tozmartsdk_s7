package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetDescripItem {
    private String idMsg;
    private String code;
    private String lang;
    private String content;

    public String getIdMsg() {
        return idMsg;
    }

    public void setIdMsg(String idMsg) {
        this.idMsg = idMsg;
    }

    public String getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
