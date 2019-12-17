package com.tozmart.tozisdk.api;

import com.tozmart.tozisdk.entity.MeasurementsData;
import com.tozmart.tozisdk.entity.SdkResponse;

public interface GetMeasurementsCallback {

    void onResponse(SdkResponse sdkResponse, MeasurementsData measurementsData);

}
