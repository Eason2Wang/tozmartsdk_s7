package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CameraAngle implements Parcelable {
    private float XAng;// 手机朝前倾斜是正数度数，向后是负数
    private float YAng;// 左正右负

    public float getXAng() {
        return XAng;
    }

    public void setXAng(float XAng) {
        this.XAng = XAng;
    }

    public float getYAng() {
        return YAng;
    }

    public void setYAng(float YAng) {
        this.YAng = YAng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.XAng);
        dest.writeFloat(this.YAng);
    }

    public CameraAngle(float xAng, float yAng) {
        XAng = xAng;
        YAng = yAng;
    }

    protected CameraAngle(Parcel in) {
        this.XAng = in.readFloat();
        this.YAng = in.readFloat();
    }

    public static final Creator<CameraAngle> CREATOR = new Creator<CameraAngle>() {
        @Override
        public CameraAngle createFromParcel(Parcel source) {
            return new CameraAngle(source);
        }

        @Override
        public CameraAngle[] newArray(int size) {
            return new CameraAngle[size];
        }
    };
}
