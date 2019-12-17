package com.tozmart.tozisdk.entity;

import java.util.List;

public class GetMeasurementsModel {

    private List<MeasureResult> measureInfo;
    private String measureId;
    private String url3d;
    private List<BodyCountrySize> countrySize;

    public List<MeasureResult> getMeasureInfo() {
        return measureInfo;
    }

    public void setMeasureInfo(List<MeasureResult> measureInfo) {
        this.measureInfo = measureInfo;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public String getUrl3d() {
        return url3d;
    }

    public void setUrl3d(String url3d) {
        this.url3d = url3d;
    }

    public List<BodyCountrySize> getCountrySize() {
        return countrySize;
    }

    public void setCountrySize(List<BodyCountrySize> countrySize) {
        this.countrySize = countrySize;
    }
}
