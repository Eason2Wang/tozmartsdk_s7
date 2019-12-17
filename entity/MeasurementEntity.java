package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangyisong on 9/3/17.
 */

public class MeasurementEntity implements Parcelable {
    private String code;
    private String meaValue;
    private String imageUrl;
    private String sizeName;
    private String sizeIntro;
    private String unit;
    private int showValue;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeaValue() {
        return meaValue;
    }

    public void setMeaValue(String meaValue) {
        this.meaValue = meaValue;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getSizeIntro() {
        return sizeIntro;
    }

    public void setSizeIntro(String sizeIntro) {
        this.sizeIntro = sizeIntro;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getShowValue() {
        return showValue;
    }

    public void setShowValue(int showValue) {
        this.showValue = showValue;
    }

    public MeasurementEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.meaValue);
        dest.writeString(this.imageUrl);
        dest.writeString(this.sizeName);
        dest.writeString(this.sizeIntro);
        dest.writeString(this.unit);
        dest.writeInt(this.showValue);
    }

    protected MeasurementEntity(Parcel in) {
        this.code = in.readString();
        this.meaValue = in.readString();
        this.imageUrl = in.readString();
        this.sizeName = in.readString();
        this.sizeIntro = in.readString();
        this.unit = in.readString();
        this.showValue = in.readInt();
    }

    public static final Creator<MeasurementEntity> CREATOR = new Creator<MeasurementEntity>() {
        @Override
        public MeasurementEntity createFromParcel(Parcel source) {
            return new MeasurementEntity(source);
        }

        @Override
        public MeasurementEntity[] newArray(int size) {
            return new MeasurementEntity[size];
        }
    };
}
