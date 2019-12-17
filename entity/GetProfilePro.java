package com.tozmart.tozisdk.entity;

public class GetProfilePro {

    private String measureId;
    private ProfileData front;
    private ProfileData side;
    private ProfileConfigData outlineConfig;

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public ProfileData getFront() {
        return front;
    }

    public void setFront(ProfileData front) {
        this.front = front;
    }

    public ProfileData getSide() {
        return side;
    }

    public void setSide(ProfileData side) {
        this.side = side;
    }

    public ProfileConfigData getOutlineConfig() {
        return outlineConfig;
    }

    public void setOutlineConfig(ProfileConfigData outlineConfig) {
        this.outlineConfig = outlineConfig;
    }
}
