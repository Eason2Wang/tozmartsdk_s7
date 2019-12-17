package com.tozmart.tozisdk.entity;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by tracy on 17/12/7.
 */

public class ErrorWarn implements Parcelable {
    private String content;
    private List<Point> position;

    public ErrorWarn(String content, List<Point> position) {
        this.content = content;
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Point> getPosition() {
        return position;
    }

    public void setPosition(List<Point> position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeTypedList(this.position);
    }

    protected ErrorWarn(Parcel in) {
        this.content = in.readString();
        this.position = in.createTypedArrayList(Point.CREATOR);
    }

    public static final Creator<ErrorWarn> CREATOR = new Creator<ErrorWarn>() {
        @Override
        public ErrorWarn createFromParcel(Parcel source) {
            return new ErrorWarn(source);
        }

        @Override
        public ErrorWarn[] newArray(int size) {
            return new ErrorWarn[size];
        }
    };
}
