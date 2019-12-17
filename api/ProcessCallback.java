package com.tozmart.tozisdk.api;

import com.tozmart.tozisdk.entity.ProcessData;
import com.tozmart.tozisdk.entity.SdkResponse;

public interface ProcessCallback {

    void onResponse(SdkResponse sdkResponse, String taskId, ProcessData processData);

}
