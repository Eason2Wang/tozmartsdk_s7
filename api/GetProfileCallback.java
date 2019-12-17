package com.tozmart.tozisdk.api;

import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.tozmart.tozisdk.entity.SdkResponse;

public interface GetProfileCallback {

    void onResponse(SdkResponse sdkResponse, Profile2ModelData profile2ModelData);

}
