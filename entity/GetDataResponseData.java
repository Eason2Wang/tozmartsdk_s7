package com.tozmart.tozisdk.entity;

/**
 * Created by tracy on 2018/2/25.
 */

public class GetDataResponseData {

    private GetProfilePro profile;
    private GetMeasurementsModel model;
    private GetRecomSizeRecom sizeRecmd;

    public GetProfilePro getProfile() {
        return profile;
    }

    public void setProfile(GetProfilePro profile) {
        this.profile = profile;
    }

    public GetMeasurementsModel getModel() {
        return model;
    }

    public void setModel(GetMeasurementsModel model) {
        this.model = model;
    }

    public GetRecomSizeRecom getSizeRecmd() {
        return sizeRecmd;
    }

    public void setSizeRecmd(GetRecomSizeRecom sizeRecmd) {
        this.sizeRecmd = sizeRecmd;
    }
}
