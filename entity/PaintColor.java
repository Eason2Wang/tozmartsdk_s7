package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tracy on 18/1/5.
 */

public class PaintColor implements Parcelable {
    private int LoosePartStart;// 轮廓中，宽松或者需要注意轮廓的段的起点 索引
    private int LoosePartEnd;// 轮廓中，宽松或者需要注意轮廓的段的终点 索引

    public int getLoosePartStart() {
        return LoosePartStart;
    }

    public void setLoosePartStart(int loosePartStart) {
        LoosePartStart = loosePartStart;
    }

    public int getLoosePartEnd() {
        return LoosePartEnd;
    }

    public void setLoosePartEnd(int loosePartEnd) {
        LoosePartEnd = loosePartEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.LoosePartStart);
        dest.writeInt(this.LoosePartEnd);
    }

    public PaintColor() {
    }

    protected PaintColor(Parcel in) {
        this.LoosePartStart = in.readInt();
        this.LoosePartEnd = in.readInt();
    }

    public static final Creator<PaintColor> CREATOR = new Creator<PaintColor>() {
        @Override
        public PaintColor createFromParcel(Parcel source) {
            return new PaintColor(source);
        }

        @Override
        public PaintColor[] newArray(int size) {
            return new PaintColor[size];
        }
    };
}
