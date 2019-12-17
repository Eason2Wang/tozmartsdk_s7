package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wys on 17/5/13.
 */

public class BodyCountrySize implements Parcelable {
    private String Country;
    private String ChartVersion;
    private String TopSz;
    private String TopSzTight;
    private String BottomSz;
    private String BottomSzTight;
    private String Size;
    private String SizeTight;

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getChartVersion() {
        return ChartVersion;
    }

    public void setChartVersion(String chartVersion) {
        ChartVersion = chartVersion;
    }

    public String getTopSz() {
        return TopSz;
    }

    public void setTopSz(String topSz) {
        TopSz = topSz;
    }

    public String getTopSzTight() {
        return TopSzTight;
    }

    public void setTopSzTight(String topSzTight) {
        TopSzTight = topSzTight;
    }

    public String getBottomSz() {
        return BottomSz;
    }

    public void setBottomSz(String bottomSz) {
        BottomSz = bottomSz;
    }

    public String getBottomSzTight() {
        return BottomSzTight;
    }

    public void setBottomSzTight(String bottomSzTight) {
        BottomSzTight = bottomSzTight;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getSizeTight() {
        return SizeTight;
    }

    public void setSizeTight(String sizeTight) {
        SizeTight = sizeTight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Country);
        dest.writeString(this.ChartVersion);
        dest.writeString(this.TopSz);
        dest.writeString(this.TopSzTight);
        dest.writeString(this.BottomSz);
        dest.writeString(this.BottomSzTight);
        dest.writeString(this.Size);
        dest.writeString(this.SizeTight);
    }

    public BodyCountrySize() {
    }

    protected BodyCountrySize(Parcel in) {
        this.Country = in.readString();
        this.ChartVersion = in.readString();
        this.TopSz = in.readString();
        this.TopSzTight = in.readString();
        this.BottomSz = in.readString();
        this.BottomSzTight = in.readString();
        this.Size = in.readString();
        this.SizeTight = in.readString();
    }

    public static final Creator<BodyCountrySize> CREATOR = new Creator<BodyCountrySize>() {
        @Override
        public BodyCountrySize createFromParcel(Parcel source) {
            return new BodyCountrySize(source);
        }

        @Override
        public BodyCountrySize[] newArray(int size) {
            return new BodyCountrySize[size];
        }
    };
}
