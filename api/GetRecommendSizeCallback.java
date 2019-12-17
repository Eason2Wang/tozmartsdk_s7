package com.tozmart.tozisdk.api;

import com.tozmart.tozisdk.entity.RecommendSizeData;
import com.tozmart.tozisdk.entity.SdkResponse;

public interface GetRecommendSizeCallback {

    void onResponse(SdkResponse sdkResponse, RecommendSizeData recommendSizeData);

}
