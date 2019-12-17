package com.tozmart.tozisdk.http;

import android.text.TextUtils;
import android.util.Base64;

import com.tozmart.tozisdk.constant.ApiNativeCode;
import com.tozmart.tozisdk.constant.SPKeys;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
import com.tozmart.tozisdk.utils.NetWorkUtils;
import com.tozmart.tozisdk.utils.SPUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

public class ApiStrategy {
    //读超时长，单位：秒
    public static final int READ_TIME_OUT = 4 * 60;
    //连接时长，单位：秒
    public static final int CONNECT_TIME_OUT = 4 * 60;

    private static boolean useFullUrl;

    public static ApiService apiService;

    public static ApiService getApiService(boolean useFullUr) {
        useFullUrl = useFullUr;
        if (apiService == null) {
            synchronized (ApiStrategy.class) {
                if (apiService == null) {
                    new ApiStrategy();
                }
            }
        }
        return apiService;
    }

    private ApiStrategy() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //缓存
        File cacheFile = new File(OneMeasureSDKLite.getInstance().getApplicationContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        //增加头部信息
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                int timeoutDataGet = (int) SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).getLong(SPKeys.REQUEST_READ_TIME_OUT, 0);
                if (timeoutDataGet == 0) {
                    timeoutDataGet = READ_TIME_OUT;
                }
                //获取request
                Request request = chain.request();
                //从request中获取原有的HttpUrl实例oldHttpUrl
                HttpUrl oldHttpUrl = request.url();
                //获取request的创建者builder
                Request.Builder builder = request.newBuilder();

                // 设置授权
                builder.addHeader("Authorization", "Basic " +
                        Base64.encodeToString(
                                (GetApiFromJni.getStringFromNative(ApiNativeCode.HTTPS_SERVER_USER_NAME, OneMeasureSDKLite.getInstance().getApplicationContext())
                                        + ":"
                                        + GetApiFromJni.getStringFromNative(ApiNativeCode.HTTPS_SERVER_PASSWORD, OneMeasureSDKLite.getInstance().getApplicationContext())).getBytes(), Base64.NO_WRAP));
//                builder.addHeader("accept-encoding", "gzip, deflate");

                //从request中获取headers，通过给定的键url_name
                List<String> headerValues = request.headers(BASE_URL);
                if (headerValues != null && headerValues.size() > 0) {
                    //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
                    builder.removeHeader(BASE_URL);
                    //匹配获得新的BaseUrl
                    String headerValue = headerValues.get(0);
                    HttpUrl newBaseUrl = null;
                    if (SDK_CONFIG_URL.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(GetApiFromJni.getStringFromNative(ApiNativeCode.SDK_CONFIG, OneMeasureSDKLite.getInstance().getApplicationContext()));
                    } else if (SDK_CONFIG_URL_TEST.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(GetApiFromJni.getStringFromNative(ApiNativeCode.SDK_CONFIG_TEST, OneMeasureSDKLite.getInstance().getApplicationContext()));
                    } else if (IMAGE_PROCESS_URL.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).getString(SPKeys.IMAGE_PROCESS_URL, ""));
                    } else if (PROFILE_PROCESS_URL.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).getString(SPKeys.PROFILE_PROCESS_URL, ""));
                    } else if (DATA_GET_URL.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).getString(SPKeys.DATA_GET_URL, ""));
                    } else if (IP_GET_URL.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(GetApiFromJni.getStringFromNative(ApiNativeCode.IP_GET, OneMeasureSDKLite.getInstance().getApplicationContext()));
                    } else if (GET_CODE_INFO_URL.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(GetApiFromJni.getStringFromNative(ApiNativeCode.GET_CODE_INFO, OneMeasureSDKLite.getInstance().getApplicationContext()));
                    } else if (MEASURE_INFO_URL.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(GetApiFromJni.getStringFromNative(ApiNativeCode.MEASURE_INFO, OneMeasureSDKLite.getInstance().getApplicationContext()));
                    } else if (GET_SERVER_MSG_URL.equals(headerValue)) {
                        newBaseUrl = HttpUrl.parse(GetApiFromJni.getStringFromNative(ApiNativeCode.GET_SERVER_MSG_URL, OneMeasureSDKLite.getInstance().getApplicationContext()));
                    } else {
                        newBaseUrl = oldHttpUrl;
                    }
                    //重建新的HttpUrl，修改需要修改的url部分
                    HttpUrl newFullUrl;
                    if (useFullUrl) {
                        newFullUrl = oldHttpUrl
                                .newBuilder()
                                .scheme(newBaseUrl.scheme())//更换网络协议
                                .host(newBaseUrl.host())//更换主机名
                                .port(newBaseUrl.port())//更换端口
                                .encodedPath(newBaseUrl.encodedPath())
                                .build();
                    } else {
                        newFullUrl = oldHttpUrl
                                .newBuilder()
                                .scheme(newBaseUrl.scheme())//更换网络协议
                                .host(newBaseUrl.host())//更换主机名
                                .port(newBaseUrl.port())//更换端口
                                .build();
                    }

                    //重建这个request，通过builder.url(newFullUrl).build()；
                    // 然后返回一个response至此结束修改
//                    Log.e("Url", "intercept: "+newFullUrl.toString());

                    return chain.withConnectTimeout(timeoutDataGet, TimeUnit.SECONDS)
                            .withReadTimeout(timeoutDataGet, TimeUnit.SECONDS)
                            .withWriteTimeout(timeoutDataGet, TimeUnit.SECONDS)
                            .proceed(builder.url(newFullUrl).build());
                }
                return chain.proceed(request);
            }
        };

        //创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(mRewriteCacheControlInterceptor)
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(headerInterceptor)
                .cache(cache);
//        clientBuilder.addInterceptor(logInterceptor);
        OkHttpClient client = clientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://www.baidu.com")
                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                //适配RxJava2.0,RxJava1.x则为RxJavaCallAdapterFactory.create()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }


    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();
            if (!NetWorkUtils.isNetConnected(OneMeasureSDKLite.getInstance().getApplicationContext())) {
                request = request.newBuilder()
                        .cacheControl(TextUtils.isEmpty(cacheControl) ? CacheControl
                                .FORCE_NETWORK : CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetConnected(OneMeasureSDKLite.getInstance().getApplicationContext())) {
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" +
                                CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };
}

