package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tracy on 18/1/5.
 */

public class PaintMovPx implements Parcelable {
    private String PxName;// move pixel's name or ID 用于对应tutorial教程图片
    private int PxIndex = -1;// move pixel 在整个轮廓的索引，0位起点
    private int PxProfess;// 关键点

    public String getPxName() {
        return PxName;
    }

    public void setPxName(String pxName) {
        PxName = pxName;
    }

    public int getPxIndex() {
        return PxIndex;
    }

    public void setPxIndex(int pxIndex) {
        PxIndex = pxIndex;
    }

    public int getPxProfess() {
        return PxProfess;
    }

    public void setPxProfess(int pxProfess) {
        PxProfess = pxProfess;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.PxName);
        dest.writeInt(this.PxIndex);
        dest.writeInt(this.PxProfess);
    }

    public PaintMovPx() {
    }

    protected PaintMovPx(Parcel in) {
        this.PxName = in.readString();
        this.PxIndex = in.readInt();
        this.PxProfess = in.readInt();
    }

    public static final Creator<PaintMovPx> CREATOR = new Creator<PaintMovPx>() {
        @Override
        public PaintMovPx createFromParcel(Parcel source) {
            return new PaintMovPx(source);
        }

        @Override
        public PaintMovPx[] newArray(int size) {
            return new PaintMovPx[size];
        }
    };
}
