package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class ProfileData {
    private ProfileBodyBean ProfileBody;
    private ImageProc ImgProc;
    private ImageErrorWarnInfo[] ErrorInfo;
    private ImageErrorWarnInfo[] WarnInfo;
    private String cutImg;
    private String mosaicImg;

    public ProfileBodyBean getProfileBody() {
        return ProfileBody;
    }

    public void setProfileBody(ProfileBodyBean profileBody) {
        ProfileBody = profileBody;
    }

    public ImageProc getImgProc() {
        return ImgProc;
    }

    public void setImgProc(ImageProc imgProc) {
        ImgProc = imgProc;
    }

    public ImageErrorWarnInfo[] getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(ImageErrorWarnInfo[] errorInfo) {
        ErrorInfo = errorInfo;
    }

    public ImageErrorWarnInfo[] getWarnInfo() {
        return WarnInfo;
    }

    public void setWarnInfo(ImageErrorWarnInfo[] warnInfo) {
        WarnInfo = warnInfo;
    }

    public String getCutImg() {
        return cutImg;
    }

    public void setCutImg(String cutImg) {
        this.cutImg = cutImg;
    }

    public String getMosaicImg() {
        return mosaicImg;
    }

    public void setMosaicImg(String mosaicImg) {
        this.mosaicImg = mosaicImg;
    }
}
