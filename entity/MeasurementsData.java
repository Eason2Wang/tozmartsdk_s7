package com.tozmart.tozisdk.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by tracy on 17/12/7.
 */

public class MeasurementsData implements Parcelable {
    private List<MeasurementEntity> measurementEntities;
    private String model3dUrl;
    private String measureId;
    private List<BodyCountrySize> bodyCountrySize;
    private String data;
    private String standardResponse;

    public List<MeasurementEntity> getMeasurementEntities() {
        return measurementEntities;
    }

    public void setMeasurementEntities(List<MeasurementEntity> measurementEntities) {
        this.measurementEntities = measurementEntities;
    }

    public String getModel3dUrl() {
        return model3dUrl;
    }

    public void setModel3dUrl(String model3dUrl) {
        this.model3dUrl = model3dUrl;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public List<BodyCountrySize> getBodyCountrySize() {
        return bodyCountrySize;
    }

    public void setBodyCountrySize(List<BodyCountrySize> bodyCountrySize) {
        this.bodyCountrySize = bodyCountrySize;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getStandardResponse() {
        return standardResponse;
    }

    public void setStandardResponse(String standardResponse) {
        this.standardResponse = standardResponse;
    }

    public MeasurementsData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.measurementEntities);
        dest.writeString(this.model3dUrl);
        dest.writeString(this.measureId);
        dest.writeTypedList(this.bodyCountrySize);
        dest.writeString(this.data);
        dest.writeString(this.standardResponse);
    }

    protected MeasurementsData(Parcel in) {
        this.measurementEntities = in.createTypedArrayList(MeasurementEntity.CREATOR);
        this.model3dUrl = in.readString();
        this.measureId = in.readString();
        this.bodyCountrySize = in.createTypedArrayList(BodyCountrySize.CREATOR);
        this.data = in.readString();
        this.standardResponse = in.readString();
    }

    public static final Creator<MeasurementsData> CREATOR = new Creator<MeasurementsData>() {
        @Override
        public MeasurementsData createFromParcel(Parcel source) {
            return new MeasurementsData(source);
        }

        @Override
        public MeasurementsData[] newArray(int size) {
            return new MeasurementsData[size];
        }
    };
}
