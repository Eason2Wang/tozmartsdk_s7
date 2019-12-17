package com.tozmart.tozisdk.entity;

import java.util.List;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetUrlConfigData {
    /**
     * apiVersion : v2.5
     * nodeId : 231426688267980800
     * apiList : {"sdkImgProcess":"http://10.168.1.122/bnd/tozapi/api/sdk/v2.5/img/process","sdkProfileSize":"http://10.168.1.122/bnd/tozapi/api/sdk/v2.5/profile/process","sdkDataGet":"http://10.168.1.122/bnd/tozapi/api/sdk/v2.5/data/get","tutorialInfoUrl":"http://10.168.1.122/bnd/tozapi/api/sdk/tutorial/info"}
     * measureResultUrl : http://10.168.1.122/measure_result/#/
     * timeoutDataGet : 300
     * longWaitDataGet : 150
     * privacyList : [{"lang":"zh-CN","url":"http://10.168.1.122/bnd/tozapi/api/comm/file/download?code=59a2d0d6fa3548db99cd4cc3a9568d30"},{"lang":"zh-TW","url":"http://10.168.1.122/bnd/tozapi/api/comm/file/download?code=c59b845dce2f451dad01d5dca264b26f"},{"lang":"en-US","url":"http://10.168.1.122/bnd/tozapi/api/comm/file/download?code=c990506ee07f4168a696b669ea879f38"},{"lang":"ja-JP","url":"http://10.168.1.122/bnd/tozapi/api/comm/file/download?code=e2bfd95683944ddfa0df4b2696ff1c63"}]
     */

    private String nodeId;
    private GetUrlConfigUrls apiList;
    private List<UrlConfigNode> nodeList;
    private String measureResultUrl;
    private long timeoutDataGet;
    private long longWaitDataGet;
    private List<PrivacyListBean> privacyList;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public GetUrlConfigUrls getApiList() {
        return apiList;
    }

    public void setApiList(GetUrlConfigUrls apiList) {
        this.apiList = apiList;
    }

    public List<UrlConfigNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<UrlConfigNode> nodeList) {
        this.nodeList = nodeList;
    }

    public String getMeasureResultUrl() {
        return measureResultUrl;
    }

    public void setMeasureResultUrl(String measureResultUrl) {
        this.measureResultUrl = measureResultUrl;
    }

    public long getTimeoutDataGet() {
        return timeoutDataGet;
    }

    public void setTimeoutDataGet(long timeoutDataGet) {
        this.timeoutDataGet = timeoutDataGet;
    }

    public long getLongWaitDataGet() {
        return longWaitDataGet;
    }

    public void setLongWaitDataGet(long longWaitDataGet) {
        this.longWaitDataGet = longWaitDataGet;
    }

    public List<PrivacyListBean> getPrivacyList() {
        return privacyList;
    }

    public void setPrivacyList(List<PrivacyListBean> privacyList) {
        this.privacyList = privacyList;
    }

    public static class ApiListBean {
        /**
         * sdkImgProcess : http://10.168.1.122/bnd/tozapi/api/sdk/v2.5/img/process
         * sdkProfileSize : http://10.168.1.122/bnd/tozapi/api/sdk/v2.5/profile/process
         * sdkDataGet : http://10.168.1.122/bnd/tozapi/api/sdk/v2.5/data/get
         * tutorialInfoUrl : http://10.168.1.122/bnd/tozapi/api/sdk/tutorial/info
         */

        private String sdkImgProcess;
        private String sdkProfileSize;
        private String sdkDataGet;
        private String tutorialInfoUrl;

        public String getSdkImgProcess() {
            return sdkImgProcess;
        }

        public void setSdkImgProcess(String sdkImgProcess) {
            this.sdkImgProcess = sdkImgProcess;
        }

        public String getSdkProfileSize() {
            return sdkProfileSize;
        }

        public void setSdkProfileSize(String sdkProfileSize) {
            this.sdkProfileSize = sdkProfileSize;
        }

        public String getSdkDataGet() {
            return sdkDataGet;
        }

        public void setSdkDataGet(String sdkDataGet) {
            this.sdkDataGet = sdkDataGet;
        }

        public String getTutorialInfoUrl() {
            return tutorialInfoUrl;
        }

        public void setTutorialInfoUrl(String tutorialInfoUrl) {
            this.tutorialInfoUrl = tutorialInfoUrl;
        }
    }

    public static class PrivacyListBean {
        /**
         * lang : zh-CN
         * url : http://10.168.1.122/bnd/tozapi/api/comm/file/download?code=59a2d0d6fa3548db99cd4cc3a9568d30
         */

        private String lang;
        private String url;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
