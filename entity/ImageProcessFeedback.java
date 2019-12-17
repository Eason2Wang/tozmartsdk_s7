package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tracy on 17/12/7.
 */

public class ImageProcessFeedback implements Parcelable {
    private List<ErrorWarn> frontImageErrors = new ArrayList<>();
    private List<ErrorWarn> frontImageWarns = new ArrayList<>();
    private List<ErrorWarn> sideImageErrors = new ArrayList<>();
    private List<ErrorWarn> sideImageWarns = new ArrayList<>();

    public List<ErrorWarn> getFrontImageErrors() {
        return frontImageErrors;
    }

    public void setFrontImageErrors(List<ErrorWarn> frontImageErrors) {
        this.frontImageErrors = frontImageErrors;
    }

    public List<ErrorWarn> getFrontImageWarns() {
        return frontImageWarns;
    }

    public void setFrontImageWarns(List<ErrorWarn> frontImageWarns) {
        this.frontImageWarns = frontImageWarns;
    }

    public List<ErrorWarn> getSideImageErrors() {
        return sideImageErrors;
    }

    public void setSideImageErrors(List<ErrorWarn> sideImageErrors) {
        this.sideImageErrors = sideImageErrors;
    }

    public List<ErrorWarn> getSideImageWarns() {
        return sideImageWarns;
    }

    public void setSideImageWarns(List<ErrorWarn> sideImageWarns) {
        this.sideImageWarns = sideImageWarns;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.frontImageErrors);
        dest.writeList(this.frontImageWarns);
        dest.writeList(this.sideImageErrors);
        dest.writeList(this.sideImageWarns);
    }

    public ImageProcessFeedback() {
    }

    protected ImageProcessFeedback(Parcel in) {
        this.frontImageErrors = new ArrayList<ErrorWarn>();
        in.readList(this.frontImageErrors, ErrorWarn.class.getClassLoader());
        this.frontImageWarns = new ArrayList<ErrorWarn>();
        in.readList(this.frontImageWarns, ErrorWarn.class.getClassLoader());
        this.sideImageErrors = new ArrayList<ErrorWarn>();
        in.readList(this.sideImageErrors, ErrorWarn.class.getClassLoader());
        this.sideImageWarns = new ArrayList<ErrorWarn>();
        in.readList(this.sideImageWarns, ErrorWarn.class.getClassLoader());
    }

    public static final Parcelable.Creator<ImageProcessFeedback> CREATOR = new Parcelable.Creator<ImageProcessFeedback>() {
        @Override
        public ImageProcessFeedback createFromParcel(Parcel source) {
            return new ImageProcessFeedback(source);
        }

        @Override
        public ImageProcessFeedback[] newArray(int size) {
            return new ImageProcessFeedback[size];
        }
    };
}
