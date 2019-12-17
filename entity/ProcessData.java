package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tracy on 17/12/7.
 */

public class ProcessData implements Parcelable {

    private ImageProcessFeedback imageProcessFeedback;

    public ImageProcessFeedback getImageProcessFeedback() {
        return imageProcessFeedback;
    }

    public void setImageProcessFeedback(ImageProcessFeedback imageProcessFeedback) {
        this.imageProcessFeedback = imageProcessFeedback;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.imageProcessFeedback, flags);
    }

    public ProcessData() {
    }

    protected ProcessData(Parcel in) {
        this.imageProcessFeedback = in.readParcelable(ImageProcessFeedback.class.getClassLoader());
    }

    public static final Creator<ProcessData> CREATOR = new Creator<ProcessData>() {
        @Override
        public ProcessData createFromParcel(Parcel source) {
            return new ProcessData(source);
        }

        @Override
        public ProcessData[] newArray(int size) {
            return new ProcessData[size];
        }
    };
}
