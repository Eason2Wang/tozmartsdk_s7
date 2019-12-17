package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tracy on 2018/2/25.
 */

public class FaceKeyPts implements Parcelable{
    public float[] FaceRect;
    public float[] EyeRect;
    public float[] LipRect;
    public float[] NoseRect;
    public float[] EarRect;

    public float[] getFaceRect() {
        return FaceRect;
    }

    public void setFaceRect(float[] faceRect) {
        FaceRect = faceRect;
    }

    public FaceKeyPts() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloatArray(this.FaceRect);
        dest.writeFloatArray(this.EyeRect);
        dest.writeFloatArray(this.LipRect);
        dest.writeFloatArray(this.NoseRect);
        dest.writeFloatArray(this.EarRect);
    }

    protected FaceKeyPts(Parcel in) {
        this.FaceRect = in.createFloatArray();
        this.EyeRect = in.createFloatArray();
        this.LipRect = in.createFloatArray();
        this.NoseRect = in.createFloatArray();
        this.EarRect = in.createFloatArray();
    }

    public static final Creator<FaceKeyPts> CREATOR = new Creator<FaceKeyPts>() {
        @Override
        public FaceKeyPts createFromParcel(Parcel source) {
            return new FaceKeyPts(source);
        }

        @Override
        public FaceKeyPts[] newArray(int size) {
            return new FaceKeyPts[size];
        }
    };
}
