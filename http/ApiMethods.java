package com.tozmart.tozisdk.http;

import com.tozmart.tozisdk.activity.RxAppCompatActivity;
import com.tozmart.tozisdk.constant.SPKeys;
import com.tozmart.tozisdk.entity.GetDataResponse;
import com.tozmart.tozisdk.entity.GetDescripRequest;
import com.tozmart.tozisdk.entity.GetDescripResponse;
import com.tozmart.tozisdk.entity.GetMeaInfoResponse;
import com.tozmart.tozisdk.entity.GetServerMsgResponse;
import com.tozmart.tozisdk.entity.GetUrlConfigResponse;
import com.tozmart.tozisdk.entity.LocationInfoResponse;
import com.tozmart.tozisdk.entity.ProcessImageResponse;
import com.tozmart.tozisdk.entity.ProcessProfileResponse;
import com.tozmart.tozisdk.utils.SPUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * Created by DeMon on 2017/9/6.
 */

public class ApiMethods {

    /**
     * 封装线程管理和订阅的过程
     */
    public static void ApiSubscribe(Observable observable, Observer observer, RxAppCompatActivity activity) {
        observable.subscribeOn(Schedulers.io())
                .compose(activity.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static Observable<LocationInfoResponse> getLocationByIp(Map<String, Object> params) {
        return ApiStrategy.getApiService(false)
                .getLocationByIp(params);
    }

    public static Observable<GetMeaInfoResponse> getMeasurementInfo(){
        return ApiStrategy.getApiService(true)
                .getMeaInfo();
    }

    public static Observable<GetDescripResponse> getDescripByCodes(GetDescripRequest getDescripRequest) {
        return ApiStrategy.getApiService(true)
                .getDescripByCodes(getDescripRequest);
    }

    public static Observable<GetServerMsgResponse> getServerMsgByCode(Map<String, Object> params) {
        return ApiStrategy.getApiService(true)
                .getServerMsgByCode(params);
    }

    public static Observable<GetUrlConfigResponse> getSdkUrlConfig(RequestBody requestBody) {
        //defaultValue   false 线上服务器     true 本地服务器
        if (SPUtils.getInstance(SPKeys.APP_SP_DATA).getBoolean(SPKeys.APP_TEST, false)) {
            return ApiStrategy.getApiService(true)
                    .getSdkUrlConfigTest(requestBody);
        } else {
            return ApiStrategy.getApiService(true)
                    .getSdkUrlConfig(requestBody);
        }
    }

    public static Observable<ProcessImageResponse> processImage(RequestBody requestBody) {
        return ApiStrategy.getApiService(true)
                .processImage(requestBody);
    }

    public static Observable<ProcessProfileResponse> processProfile(RequestBody requestBody) {
        return ApiStrategy.getApiService(true)
                .processProfile(requestBody);
    }

    public static Observable<GetDataResponse> getData(RequestBody requestBody) {
        return ApiStrategy.getApiService(true)
                .getData(requestBody);
    }
}
