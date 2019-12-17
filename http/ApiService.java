package com.tozmart.tozisdk.http;

import com.tozmart.tozisdk.entity.GetDataResponse;
import com.tozmart.tozisdk.entity.GetDescripRequest;
import com.tozmart.tozisdk.entity.GetDescripResponse;
import com.tozmart.tozisdk.entity.GetMeaInfoResponse;
import com.tozmart.tozisdk.entity.GetServerMsgResponse;
import com.tozmart.tozisdk.entity.GetUrlConfigResponse;
import com.tozmart.tozisdk.entity.LocationInfoResponse;
import com.tozmart.tozisdk.entity.ProcessImageResponse;
import com.tozmart.tozisdk.entity.ProcessProfileResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import static com.tozmart.tozisdk.http.BaseUrl.BASE_URL;
import static com.tozmart.tozisdk.http.BaseUrl.DATA_GET_URL;
import static com.tozmart.tozisdk.http.BaseUrl.GET_CODE_INFO_URL;
import static com.tozmart.tozisdk.http.BaseUrl.GET_SERVER_MSG_URL;
import static com.tozmart.tozisdk.http.BaseUrl.IMAGE_PROCESS_URL;
import static com.tozmart.tozisdk.http.BaseUrl.IP_GET_URL;
import static com.tozmart.tozisdk.http.BaseUrl.MEASURE_INFO_URL;
import static com.tozmart.tozisdk.http.BaseUrl.PROFILE_PROCESS_URL;
import static com.tozmart.tozisdk.http.BaseUrl.SDK_CONFIG_URL;
import static com.tozmart.tozisdk.http.BaseUrl.SDK_CONFIG_URL_TEST;

/**
 * Created by DeMon on 2017/9/6.
 */

public interface ApiService {

    @Headers({BASE_URL + ":"  + IP_GET_URL})
    @POST("check")
    Observable<LocationInfoResponse> getLocationByIp(@QueryMap Map<String, Object> params);

    @Headers({BASE_URL + ":" + MEASURE_INFO_URL})
    @POST("/")
    Observable<GetMeaInfoResponse> getMeaInfo();

    @Headers({BASE_URL + ":" + GET_CODE_INFO_URL})
    @POST("/")
    Observable<GetDescripResponse> getDescripByCodes(@Body GetDescripRequest getDescripRequest);

    @Headers({BASE_URL + ":" + GET_SERVER_MSG_URL})
    @GET("/")
    Observable<GetServerMsgResponse> getServerMsgByCode(@QueryMap Map<String, Object> params);


    @Headers({BASE_URL + ":" + SDK_CONFIG_URL})
    @POST("/")
    Observable<GetUrlConfigResponse> getSdkUrlConfig(@Body RequestBody requestBody);

    @Headers({BASE_URL + ":" + SDK_CONFIG_URL_TEST})
    @POST("/")
    Observable<GetUrlConfigResponse> getSdkUrlConfigTest(@Body RequestBody requestBody);

    @Headers({BASE_URL + ":" + IMAGE_PROCESS_URL})
    @POST("/")
    Observable<ProcessImageResponse> processImage(@Body RequestBody requestBody);

    @Headers({BASE_URL + ":" + PROFILE_PROCESS_URL})
    @POST("/")
    Observable<ProcessProfileResponse> processProfile(@Body RequestBody requestBody);

    @Headers({BASE_URL + ":" + DATA_GET_URL})
    @POST("/")
    Observable<GetDataResponse> getData(@Body RequestBody requestBody);
}
