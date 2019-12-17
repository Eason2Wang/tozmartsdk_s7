package com.tozmart.tozisdk.entity;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class OneMeasureSDKInfo implements Parcelable {
    private String appKey;
    private String appSecret;
//    private String nodeId;
    private int language;
    private int unit;
    private Context context;
    private String userId;
    private String userName;
    private float userHeight;
    private float userWeight;
    private int userGender;// 0 is female, 1 is male

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

//    public String getNodeId() {
//        return nodeId;
//    }
//
//    public void setNodeId(String nodeId) {
//        this.nodeId = nodeId;
//    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(float userHeight) {
        this.userHeight = userHeight;
    }

    public float getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(float userWeight) {
        this.userWeight = userWeight;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public OneMeasureSDKInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appKey);
        dest.writeString(this.appSecret);
//        dest.writeString(this.nodeId);
        dest.writeInt(this.language);
        dest.writeInt(this.unit);
        dest.writeString(this.userId);
        dest.writeString(this.userName);
        dest.writeFloat(this.userHeight);
        dest.writeFloat(this.userWeight);
        dest.writeInt(this.userGender);
    }

    protected OneMeasureSDKInfo(Parcel in) {
        this.appKey = in.readString();
        this.appSecret = in.readString();
//        this.nodeId = in.readString();
        this.language = in.readInt();
        this.unit = in.readInt();
        this.userId = in.readString();
        this.userName = in.readString();
        this.userHeight = in.readFloat();
        this.userWeight = in.readFloat();
        this.userGender = in.readInt();
    }

    public static final Creator<OneMeasureSDKInfo> CREATOR = new Creator<OneMeasureSDKInfo>() {
        @Override
        public OneMeasureSDKInfo createFromParcel(Parcel source) {
            return new OneMeasureSDKInfo(source);
        }

        @Override
        public OneMeasureSDKInfo[] newArray(int size) {
            return new OneMeasureSDKInfo[size];
        }
    };
}
