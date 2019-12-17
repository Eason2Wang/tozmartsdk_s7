package com.tozmart.tozisdk.utils;

import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
import com.tozmart.tozisdk.constant.ApiNativeCode;
import com.tozmart.tozisdk.constant.Language;
import com.tozmart.tozisdk.http.ApiMethods;
import com.tozmart.tozisdk.http.GetApiFromJni;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;

public class OneMeasureSDKLiteUtils {
    private static final String TAG = "OneMeasureSDKLiteUtils";

    public static Observable getLocationByIp() {

        Map<String, Object> params = new LinkedHashMap<>();
        switch (OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()){
            case Language.ENGLISH:
                params.put("language", "en");
                break;
            case Language.CHINESE:
                params.put("language", "zh");
                break;
            case Language.TRADITION_CHINESE:
                params.put("language", "zh");
                break;
            case Language.JAPANESE:
                params.put("language", "ja");
                break;
            default:
                params.put("language", "en");
                break;
        }
        params.put("access_key", GetApiFromJni.getStringFromNative(ApiNativeCode.IP_LOCATION_ACCESS_KEY, OneMeasureSDKLite.getInstance().getApplicationContext()));

        return ApiMethods.getLocationByIp(params);
    }
}
