package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tracy on 18/1/5.
 */

public class Pixel implements Parcelable {
    private float X;
    private float Y;

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.X);
        dest.writeFloat(this.Y);
    }

    public Pixel() {
    }

    public Pixel(float x, float y) {
        this.X = x;
        this.Y = y;
    }

    protected Pixel(Parcel in) {
        this.X = in.readFloat();
        this.Y = in.readFloat();
    }

    public static final Creator<Pixel> CREATOR = new Creator<Pixel>() {
        @Override
        public Pixel createFromParcel(Parcel source) {
            return new Pixel(source);
        }

        @Override
        public Pixel[] newArray(int size) {
            return new Pixel[size];
        }
    };
}
