package com.tozmart.tozisdk.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.gson.Gson;
import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.activity.RxAppCompatActivity;
import com.tozmart.tozisdk.api.ErrorWarnListener;
import com.tozmart.tozisdk.api.GetMeasurementsCallback;
import com.tozmart.tozisdk.api.GetProfileCallback;
import com.tozmart.tozisdk.api.GetRecommendSizeCallback;
import com.tozmart.tozisdk.api.MeasureInfoListener;
import com.tozmart.tozisdk.api.ProcessCallback;
import com.tozmart.tozisdk.constant.ApiType;
import com.tozmart.tozisdk.constant.Constants;
import com.tozmart.tozisdk.constant.Gender;
import com.tozmart.tozisdk.constant.Language;
import com.tozmart.tozisdk.constant.SDKCode;
import com.tozmart.tozisdk.constant.SPKeys;
import com.tozmart.tozisdk.constant.ServerKeys;
import com.tozmart.tozisdk.constant.Unit;
import com.tozmart.tozisdk.entity.CameraAngle;
import com.tozmart.tozisdk.entity.GetDataResponse;
import com.tozmart.tozisdk.entity.GetServerMsgResponse;
import com.tozmart.tozisdk.entity.GetUrlConfigResponse;
import com.tozmart.tozisdk.entity.MeasurementsData;
import com.tozmart.tozisdk.entity.OneMeasureSDKInfo;
import com.tozmart.tozisdk.entity.ProcessData;
import com.tozmart.tozisdk.entity.ProcessImageResponse;
import com.tozmart.tozisdk.entity.ProcessProfileResponse;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.tozmart.tozisdk.entity.RecommendSizeData;
import com.tozmart.tozisdk.entity.SdkResponse;
import com.tozmart.tozisdk.http.ApiMethods;
import com.tozmart.tozisdk.listener.RequireHandle;
import com.tozmart.tozisdk.utils.*;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.tozmart.tozisdk.constant.SDKCode.SERVER_UNKNOW_ERROR;

public class OneMeasureSDKLite {
    private static final String TAG = "OneMeasureSDKLite";

    private OneMeasureSDKInfo oneMeasureSDKInfo;
    private Context mApplicationContext;

    private static OneMeasureSDKLite oneMeasureSDKLite = null;

    static {
        System.loadLibrary("tozmart-native-lib");
    }

    public OneMeasureSDKLite() {
    }

    public static class SingleHolder {
        private static OneMeasureSDKLite oneMeasureSDKLite = new OneMeasureSDKLite();
    }

    public static OneMeasureSDKLite getInstance() {
//        synchronized (OneMeasureSDKLite.class) {
//            if (oneMeasureSDKLite == null) {
//                oneMeasureSDKLite = new OneMeasureSDKLite();
//            }
//        }
        return SingleHolder.oneMeasureSDKLite;
    }

    public static OneMeasureSDKLite getInstance(OneMeasureSDKInfo oneMeasureSDKInfo) {
//        synchronized (OneMeasureSDKLite.class) {
//            if (oneMeasureSDKLite == null) {
//                oneMeasureSDKLite = new OneMeasureSDKLite();
//            }
//        }
        oneMeasureSDKLite = getInstance();
        oneMeasureSDKLite.setOneMeasureSDKInfo(oneMeasureSDKInfo);
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("appKey", oneMeasureSDKInfo.getAppKey());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("appSecret", oneMeasureSDKInfo.getAppSecret());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("language", oneMeasureSDKInfo.getLanguage());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("name", oneMeasureSDKInfo.getUserName());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("gender", oneMeasureSDKInfo.getUserGender());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("height", oneMeasureSDKInfo.getUserHeight());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("weight", oneMeasureSDKInfo.getUserWeight());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("userId", oneMeasureSDKInfo.getUserId());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put("unit", oneMeasureSDKInfo.getUnit());
        return oneMeasureSDKLite;
    }

    public Context getApplicationContext() {
        mApplicationContext = oneMeasureSDKInfo.getContext();
        if (mApplicationContext == null) {
            throw new NullPointerException("not initialize Application Context");
        }

        return mApplicationContext;
    }

    private void setOneMeasureSDKInfo(OneMeasureSDKInfo oneMeasureSDKInfo) {
        this.oneMeasureSDKInfo = oneMeasureSDKInfo;
    }

    public OneMeasureSDKInfo getOneMeasureSDKInfo() {
        if (oneMeasureSDKInfo == null) {
            throw new NullPointerException("not initialize OneMeasureSDKInfo");
        }
        return oneMeasureSDKInfo;
    }

    public static class Builder {
        protected String appKey = "";
        protected String appSecret = "";
        protected String nodeId = "";
        protected int language = Language.ENGLISH;
        protected OneMeasureSDKInfo toziSDKInfo;
        protected Context mApplicationContext;
        protected String name = "";
        protected int gender;
        protected float height;
        protected float weight;
        protected String userId = "";
        protected int unit = Unit.METRIC;


        public Builder() {
            toziSDKInfo = new OneMeasureSDKInfo();
        }

        public Builder withActivity(Activity activity) {
            mApplicationContext = activity.getApplicationContext();
            return this;
        }

        public String getAppKey() {
            return appKey;
        }

        public Builder setAppKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public Builder setAppSecret(String appSecret) {
            this.appSecret = appSecret;
            return this;
        }

//        public String getNodeId() {
//            return nodeId;
//        }
//
//        public Builder setNodeId(String nodeId) {
//            this.nodeId = nodeId;
//            return this;
//        }

        public int getLanguage() {
            return language;
        }

        public Builder setLanguage(int language) {
            this.language = language;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public int getGender() {
            return gender;
        }

        public Builder setGender(int gender) {
            this.gender = gender;
            return this;
        }

        public float getHeight() {
            return height;
        }

        public Builder setHeight(float height) {
            this.height = height;
            return this;
        }

        public float getWeight() {
            return weight;
        }

        public Builder setWeight(float weight) {
            this.weight = weight;
            return this;
        }

        public String getUserId() {
            return userId;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public int getUnit() {
            return unit;
        }

        public Builder setUnit(int unit) {
            this.unit = unit;
            return this;
        }

        public void build() {
            if (TextUtils.isEmpty(appKey) || TextUtils.isEmpty(appSecret)) {
                Log.e(TAG, "missing appKey or appSecret", new Throwable("missing appKey or appSecret"));

            }
            toziSDKInfo.setContext(mApplicationContext);
            toziSDKInfo.setAppKey(appKey);
            toziSDKInfo.setAppSecret(appSecret);
//            toziSDKInfo.setNodeId(nodeId);
            toziSDKInfo.setLanguage(language);
            toziSDKInfo.setUserName(name);
            toziSDKInfo.setUserGender(gender);
            toziSDKInfo.setUserHeight(height);
            toziSDKInfo.setUserWeight(weight);
            toziSDKInfo.setUserId(userId);
            toziSDKInfo.setUnit(unit);
            OneMeasureSDKLite.getInstance(toziSDKInfo);

            // facebook 网络和本地加载图片的工具，支持gif
            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(mApplicationContext)
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(mApplicationContext, config);
        }
    }

    private void reInitSdk(Activity activity) {
        String appKey = SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getString("appKey", "");
        String appSecret = SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getString("appSecret", "");
        new Builder()
                .withActivity(activity)
                .setAppKey(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getString("appKey", ""))
                .setAppSecret(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getString("appSecret", ""))
                .setUserId(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getString("userId", ""))
                .setName(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getString("name", ""))
                .setGender(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getInt("gender", Gender.MALE))
                .setHeight(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getFloat("height", 0))
                .setWeight(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getFloat("weight", 0))
                .setLanguage(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getInt("language", Language.ENGLISH))
                .setUnit(SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA, activity).getInt("unit", Unit.METRIC))
                .build();
    }

    private float imageScale;
    private GetUrlConfigResponse getUrlConfigResponse;

    public void processImage(final RxAppCompatActivity activity, final Bitmap bitmap, final int photoType,
                             final CameraAngle cameraAngle, final String taskId, final ProcessCallback callback) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        RequestBody request = RequestUtils.buildUrlConfigRequest();
        if (request == null) {
            Log.e(TAG, "request is null");
            return;
        }
        ApiMethods.getSdkUrlConfig(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetUrlConfigResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetUrlConfigResponse response) throws Exception {
                        long timeoutDataGet = response.getData().getTimeoutDataGet();
                        long longWaitDataGet = response.getData().getLongWaitDataGet();
                        SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.REQUEST_READ_TIME_OUT, timeoutDataGet);
                        SPUtils.getInstance(SPKeys.UPLOAD_LONG_TIME_WAIT).put(SPKeys.UPLOAD_PHOTO_LONG_TIME_WAIT, longWaitDataGet);
                        getUrlConfigResponse = response;
                        SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.MEASURE_RESULT_DATA, response.getData().getMeasureResultUrl());
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetServerMsgResponse>() {
                    @Override
                    public boolean test(GetServerMsgResponse response) throws Exception {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            if (response.getData().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                return true;
                            } else {
                                sdkResponse.setServerStatusCode(response.getData().getCode());
                                sdkResponse.setServerStatusText(response.getData().getContent());
                            }
                        } else {
                            sdkResponse.setServerStatusCode(response.getResult().getCode());
                            sdkResponse.setServerStatusText(response.getResult().getMessage());
                        }
                        callback.onResponse(sdkResponse, null, null);
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetServerMsgResponse, ObservableSource<RequestBody>>() {
                    @Override
                    public ObservableSource<RequestBody> apply(GetServerMsgResponse response) throws Exception {
                        imageScale = (float) bitmap.getHeight() / Constants.PHOTO_MAX_HEIGHT;
                        Bitmap bp = ImageUtils.scale(bitmap, (int) ((float) bitmap.getWidth() / imageScale), Constants.PHOTO_MAX_HEIGHT);
                        return RequestUtils.buildProcessImageRequest(getUrlConfigResponse, bp, photoType, cameraAngle, taskId);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<RequestBody, ObservableSource<ProcessImageResponse>>() {
                    @Override
                    public ObservableSource<ProcessImageResponse> apply(RequestBody requestBody) throws Exception {
                        return ApiMethods.processImage(requestBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<ProcessImageResponse>() {
                    @Override
                    public boolean test(final ProcessImageResponse response) throws Exception {
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(SDKCode.SERVER_SUCCESS);
                            sdkResponse.setServerStatusText(response.getMsg() + "(" + response.getCode() + ")");
                            final ProcessData processData = new ProcessData();
                            ResponseUtils.extractErrorWarnInfo(activity, response, sdkResponse, processData, photoType, imageScale, new ErrorWarnListener() {
                                @Override
                                public void onSuccess() {
                                    callback.onResponse(sdkResponse, response.getData().getTaskId(), processData);
                                }

                                @Override
                                public void onError() {
                                    // 国际化系统出错
                                    callback.onResponse(sdkResponse, response.getData().getTaskId(), processData);
                                }
                            });
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<ProcessImageResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(ProcessImageResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(final GetServerMsgResponse response) {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
//                        if (!sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS) && !TextUtils.isEmpty(sdkResponse.getServerStatusCode())) {
//                            callback.onResponse(sdkResponse, null, null);
//                        }
                    }
                });
    }

    private static final int REPEAT_COUNT = 30;
    private int getProfileDataStatus;// 异步获取data的状态
    private int getProfileDataCount;
    private String getProfileTraceId;

    public void getProfile(final RxAppCompatActivity activity, String taskId, final GetProfileCallback callback) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        getProfileDataStatus = 0;
        getProfileDataCount = 0;
        getProfileTraceId = "";
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(ServerKeys.TASK_ID, taskId)
                .addFormDataPart(ServerKeys.DATA_TYPE, String.valueOf(ApiType.GET_PROFILE))
                .build();
        ApiMethods.getData(requestBody)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                // 保证没网的情况下也会进行轮询
//                .retryUntil(new BooleanSupplier() {
//                    @Override
//                    public boolean getAsBoolean() throws Exception {
//                        return getProfileDataStatus == 1;
//                    }
//                })
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        getProfileDataCount++;
                        return getProfileDataStatus == 1 || getProfileDataCount == REPEAT_COUNT;
                    }
                })
//                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
//                        //必须作出反应，这里是通过flatMap操作符。
//                        return objectObservable.flatMap(new Function<Object, ObservableSource<Long>>() {
//
//                            @Override
//                            public ObservableSource<Long> apply(Object o) throws Exception {
//                                if (getProfileDataStatus == 1) {
////                                    return Observable.empty(); //发送onComplete消息，无法触发下游的onComplete回调。
//                                    return Observable.error(new Throwable("Polling work finished")); //发送onError消息，可以触发下游的onError回调。
//                                }
//                                Log.d(TAG, "startAdvancePolling apply");
//                                return Observable.timer(1000, TimeUnit.MILLISECONDS);
//                            }
//
//                        });
//                    }
//                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetDataResponse>() {
                    @Override
                    public boolean test(final GetDataResponse response) throws Exception {
                        if (getProfileDataStatus == 0) {
                            if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                getProfileDataStatus = response.getData().getStatus();
                                if (getProfileDataStatus == 1) {
                                    if (response.getData().getApiData().getApiStatus() == 1) {
                                        sdkResponse.setServerStatusCode(response.getCode());
                                        sdkResponse.setServerStatusText(response.getMsg() + "(" + response.getCode() + ")");
                                        final Profile2ModelData profile2ModelData = new Profile2ModelData();
                                        try {
                                            ResponseUtils.extractLineInfo(profile2ModelData, response.getData().getApiData().getResponseData());
                                            ResponseUtils.extractAllPoints(profile2ModelData, response.getData().getApiData().getResponseData());
                                            ResponseUtils.extractFaceRect(profile2ModelData, response.getData().getApiData().getResponseData());
                                            ResponseUtils.extractBitmap(profile2ModelData, response.getData().getApiData().getResponseData());
                                            ResponseUtils.extractConfig(profile2ModelData, response.getData().getApiData().getResponseData(), oneMeasureSDKInfo.getUserGender());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        ResponseUtils.extractErrorWarnInfo(activity, sdkResponse, profile2ModelData, response.getData().getApiData().getResponseData(), new ErrorWarnListener() {
                                            @Override
                                            public void onSuccess() {
                                                callback.onResponse(sdkResponse, profile2ModelData);
                                            }

                                            @Override
                                            public void onError() {
                                                // 国际化系统出错
                                                callback.onResponse(sdkResponse, profile2ModelData);
                                            }
                                        });
                                    } else {
                                        getProfileTraceId = response.getData().getApiData().getTraceId();
                                        return true;
                                    }
                                }
                                return false;
                            } else {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetDataResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetDataResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            params.put(ServerKeys.CODE, response.getData().getApiData().getApiCode());
                        }
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        getProfileDataStatus = 1;
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            if (!TextUtils.isEmpty(getProfileTraceId)) {
                                sdkResponse.setServerStatusCode(response.getData().getCode() + ":" + getProfileTraceId);
                            }
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        getProfileDataStatus = 1;
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
                        if (getProfileDataCount == REPEAT_COUNT) {
                            sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                            sdkResponse.setServerStatusText(activity.getString(R.string.socket_time_out_exception));
                            callback.onResponse(sdkResponse, null);
                        }
                    }
                });
    }

    public void getMeasurementsByProfile(final RxAppCompatActivity activity, final Profile2ModelData profile2ModelData, final GetMeasurementsCallback callback) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        RequestBody request = RequestUtils.buildUrlConfigRequest();
        ApiMethods.getSdkUrlConfig(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetUrlConfigResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetUrlConfigResponse response) throws Exception {
                        long timeoutDataGet = response.getData().getTimeoutDataGet();
                        SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.REQUEST_READ_TIME_OUT, timeoutDataGet);
                        getUrlConfigResponse = response;
                        SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.MEASURE_RESULT_DATA, response.getData().getMeasureResultUrl());
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        params.put(ServerKeys.NODE_ID, response.getCode());
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetServerMsgResponse>() {
                    @Override
                    public boolean test(GetServerMsgResponse response) throws Exception {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            if (response.getData().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                return true;
                            } else {
                                sdkResponse.setServerStatusCode(response.getData().getCode());
                                sdkResponse.setServerStatusText(response.getData().getContent());
                            }
                        } else {
                            sdkResponse.setServerStatusCode(response.getResult().getCode());
                            sdkResponse.setServerStatusText(response.getResult().getMessage());
                        }
                        callback.onResponse(sdkResponse, null);
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetServerMsgResponse, ObservableSource<ProcessProfileResponse>>() {
                    @Override
                    public ObservableSource<ProcessProfileResponse> apply(GetServerMsgResponse response) throws Exception {
                        return ApiMethods.processProfile(RequestUtils.buildProcessProfileRequest(getUrlConfigResponse, profile2ModelData));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<ProcessProfileResponse>() {
                    @Override
                    public boolean test(final ProcessProfileResponse response) throws Exception {
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(SDKCode.SERVER_SUCCESS);
                            sdkResponse.setServerStatusText(response.getMsg() + "(" + response.getCode() + ")");
                            getMeasurementsById(activity, response.getData().getTaskId(), callback);
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<ProcessProfileResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(ProcessProfileResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
//                        if (!sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS) && !TextUtils.isEmpty(sdkResponse.getServerStatusCode())) {
//                            callback.onResponse(sdkResponse, null);
//                        }
                    }
                });
    }

    public void getMeasurementsByTask(final RxAppCompatActivity activity, final String taskId, final GetMeasurementsCallback callback) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        RequestBody request = RequestUtils.buildUrlConfigRequest();
        ApiMethods.getSdkUrlConfig(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetUrlConfigResponse>() {
                    @Override
                    public boolean test(GetUrlConfigResponse response) throws Exception {
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            long timeoutDataGet = response.getData().getTimeoutDataGet();
                            SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.REQUEST_READ_TIME_OUT, timeoutDataGet);
                            SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.DATA_GET_URL, response.getData().getApiList().getSdkDataGet());
                            SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TUTORIAL_INFO_URL, response.getData().getApiList().getTutorialInfoUrl());
                            SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.MEASURE_RESULT_DATA, response.getData().getMeasureResultUrl());
                            getMeasurementsById(activity, taskId, callback);
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetUrlConfigResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetUrlConfigResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
//                        if (!sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS) && !TextUtils.isEmpty(sdkResponse.getServerStatusCode())) {
//                            callback.onResponse(sdkResponse, null);
//                        }
                    }
                });
    }

    private int getMeasurementsByProfileDataStatus;// 异步获取data的状态
    private int getMeasurementsByProfileDataCount;
    private String getMeasurementsByProfileTraceId;

    private void getMeasurementsById(final RxAppCompatActivity activity, final String taskId, final GetMeasurementsCallback callback) {
        final SdkResponse sdkResponse = new SdkResponse();
        getMeasurementsByProfileDataStatus = 0;
        getMeasurementsByProfileDataCount = 0;
        getMeasurementsByProfileTraceId = "";
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(ServerKeys.TASK_ID, taskId)
                .addFormDataPart(ServerKeys.DATA_TYPE, String.valueOf(ApiType.GET_MEASUREMENTS_BY_PROFILE))
                .build();
        ApiMethods.getData(requestBody)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
//                .retryUntil(new BooleanSupplier() {
//                    @Override
//                    public boolean getAsBoolean() throws Exception {
//                        return getMeasurementsByProfileDataStatus == 1;
//                    }
//                })
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        getMeasurementsByProfileDataCount++;
                        return getMeasurementsByProfileDataStatus == 1 || getMeasurementsByProfileDataCount == REPEAT_COUNT;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetDataResponse>() {
                    @Override
                    public boolean test(final GetDataResponse response) throws Exception {
                        if (getMeasurementsByProfileDataStatus == 0) {
                            if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                getMeasurementsByProfileDataStatus = response.getData().getStatus();
                                if (getMeasurementsByProfileDataStatus == 1) {
                                    if (response.getData().getApiData().getApiStatus() == 1) {
                                        sdkResponse.setServerStatusCode(response.getCode());
                                        sdkResponse.setServerStatusText(response.getMsg() + "(" + response.getCode() + ")");
                                        final MeasurementsData measurementsDataByProfile = new MeasurementsData();
                                        measurementsDataByProfile.setModel3dUrl(response.getData().getApiData().getResponseData().getModel().getUrl3d());
                                        measurementsDataByProfile.setMeasureId(response.getData().getApiData().getResponseData().getModel().getMeasureId());
                                        measurementsDataByProfile.setBodyCountrySize(response.getData().getApiData().getResponseData().getModel().getCountrySize());
                                        String toJson = new Gson().toJson(response.getData().getApiData().getResponseData());
                                        measurementsDataByProfile.setData(toJson);
                                        ResponseUtils.extractMeasurements(activity, sdkResponse,
                                                response.getData().getApiData().getResponseData().getModel(),
                                                measurementsDataByProfile,
                                                new MeasureInfoListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        callback.onResponse(sdkResponse, measurementsDataByProfile);
                                                    }

                                                    @Override
                                                    public void onError() {
                                                        // 国际化系统出错
                                                        callback.onResponse(sdkResponse, null);
                                                    }
                                                });
                                    } else {
                                        getMeasurementsByProfileTraceId = response.getData().getApiData().getTraceId();
                                        return true;
                                    }
                                }
                                return false;
                            } else {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetDataResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetDataResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            params.put(ServerKeys.CODE, response.getData().getApiData().getApiCode());
                        }
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        getMeasurementsByProfileDataStatus = 1;
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            if (!TextUtils.isEmpty(getMeasurementsByProfileTraceId)) {
                                sdkResponse.setServerStatusCode(response.getData().getCode() + ":" + getMeasurementsByProfileTraceId);
                            }
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        getMeasurementsByProfileDataStatus = 1;
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
                        if (getMeasurementsByProfileDataCount == REPEAT_COUNT) {
                            sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                            sdkResponse.setServerStatusText(activity.getString(R.string.socket_time_out_exception));
                            callback.onResponse(sdkResponse, null);
                        }
                    }
                });
    }

    private int getMeasurementsDataStatus;// 异步获取data的状态
    private int getMeasurementsDataCount;
    private String getMeasurementsTraceId;

    public void getMeasurements(final RxAppCompatActivity activity, final String taskId, final GetMeasurementsCallback callback) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        getMeasurementsDataStatus = 0;
        getMeasurementsDataCount = 0;
        getMeasurementsTraceId = "";
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(ServerKeys.TASK_ID, taskId)
                .addFormDataPart(ServerKeys.DATA_TYPE, String.valueOf(ApiType.GET_MEASUREMENTS))
                .build();
        ApiMethods.getData(requestBody)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
//                .retryUntil(new BooleanSupplier() {
//                    @Override
//                    public boolean getAsBoolean() throws Exception {
//                        return getMeasurementsDataStatus == 1;
//                    }
//                })
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        getMeasurementsDataCount++;
                        return getMeasurementsDataStatus == 1 || getMeasurementsDataCount == REPEAT_COUNT;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetDataResponse>() {
                    @Override
                    public boolean test(final GetDataResponse response) throws Exception {
                        if (getMeasurementsDataStatus == 0) {
                            if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                getMeasurementsDataStatus = response.getData().getStatus();
                                if (getMeasurementsDataStatus == 1) {
                                    if (response.getData().getApiData().getApiStatus() == 1) {
                                        sdkResponse.setServerStatusCode(response.getCode());
                                        sdkResponse.setServerStatusText(response.getMsg() + "(" + response.getCode() + ")");
                                        final MeasurementsData measurementsData = new MeasurementsData();
                                        measurementsData.setModel3dUrl(response.getData().getApiData().getResponseData().getModel().getUrl3d());
                                        ResponseUtils.extractMeasurements(activity, sdkResponse,
                                                response.getData().getApiData().getResponseData().getModel(),
                                                measurementsData,
                                                new MeasureInfoListener() {
                                                    @Override
                                                    public void onSuccess() {
                                                        callback.onResponse(sdkResponse, measurementsData);
                                                    }

                                                    @Override
                                                    public void onError() {
                                                        // 国际化系统出错
                                                        callback.onResponse(sdkResponse, null);
                                                    }
                                                });
                                    } else {
                                        getMeasurementsTraceId = response.getData().getApiData().getTraceId();
                                        return true;
                                    }
                                }
                                return false;
                            } else {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetDataResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetDataResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            params.put(ServerKeys.CODE, response.getData().getApiData().getApiCode());
                        }
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        getMeasurementsDataStatus = 1;
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            if (!TextUtils.isEmpty(getMeasurementsTraceId)) {
                                sdkResponse.setServerStatusCode(response.getData().getCode() + ":" + getMeasurementsTraceId);
                            }
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        getMeasurementsDataStatus = 1;
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
                        if (getMeasurementsDataCount == REPEAT_COUNT) {
                            sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                            sdkResponse.setServerStatusText(activity.getString(R.string.socket_time_out_exception));
                            callback.onResponse(sdkResponse, null);
                        }
                    }
                });
    }

    private int getRecomSizeDataStatus;// 异步获取data的状态
    private int getRecomSizeDataCount;
    private String getRecomSizeTraceId;

    public void getRecommendSize(final RxAppCompatActivity activity, final String taskId, final GetRecommendSizeCallback callback) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        getRecomSizeDataStatus = 0;
        getRecomSizeDataCount = 0;
        getRecomSizeTraceId = "";
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(ServerKeys.TASK_ID, taskId)
                .addFormDataPart(ServerKeys.DATA_TYPE, String.valueOf(ApiType.GET_RECOM_SIZE))
                .build();
        ApiMethods.getData(requestBody)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
//                .retryUntil(new BooleanSupplier() {
//                    @Override
//                    public boolean getAsBoolean() throws Exception {
//                        return getRecomSizeDataStatus == 1;
//                    }
//                })
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        getRecomSizeDataCount++;
                        return getRecomSizeDataStatus == 1 || getRecomSizeDataCount == REPEAT_COUNT;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetDataResponse>() {
                    @Override
                    public boolean test(final GetDataResponse response) throws Exception {
                        if (getRecomSizeDataStatus == 0) {
                            if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                getRecomSizeDataStatus = response.getData().getStatus();
                                if (getRecomSizeDataStatus == 1) {
                                    if (response.getData().getApiData().getApiStatus() == 1) {
                                        sdkResponse.setServerStatusCode(response.getCode());
                                        sdkResponse.setServerStatusText(response.getMsg() + "(" + response.getCode() + ")");
                                        RecommendSizeData recommendSizeData = new RecommendSizeData();
                                        recommendSizeData.setRecommendSizeList(response.getData().getApiData().getResponseData().getSizeRecmd().getRecommendSizeList());
                                        callback.onResponse(sdkResponse, recommendSizeData);
                                    } else {
                                        getRecomSizeTraceId = response.getData().getApiData().getTraceId();
                                        return true;
                                    }
                                }
                                return false;
                            } else {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetDataResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetDataResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            params.put(ServerKeys.CODE, response.getData().getApiData().getApiCode());
                        }
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        getRecomSizeDataStatus = 1;
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            if (!TextUtils.isEmpty(getRecomSizeTraceId)) {
                                sdkResponse.setServerStatusCode(response.getData().getCode() + ":" + getRecomSizeTraceId);
                            }
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        getRecomSizeDataStatus = 1;
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
                        if (getRecomSizeDataCount == REPEAT_COUNT) {
                            sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                            sdkResponse.setServerStatusText(activity.getString(R.string.socket_time_out_exception));
                            callback.onResponse(sdkResponse, null);
                        }
                    }
                });
    }

    public void getRecomSizeByProfile(final RxAppCompatActivity activity, final Profile2ModelData profile2ModelData, final GetRecommendSizeCallback callback) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        RequestBody request = RequestUtils.buildUrlConfigRequest();
        ApiMethods.getSdkUrlConfig(request)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetUrlConfigResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetUrlConfigResponse response) throws Exception {
                        long timeoutDataGet = response.getData().getTimeoutDataGet();
                        SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.REQUEST_READ_TIME_OUT, timeoutDataGet);
                        getUrlConfigResponse = response;
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetServerMsgResponse>() {
                    @Override
                    public boolean test(GetServerMsgResponse response) throws Exception {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            if (response.getData().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                return true;
                            } else {
                                sdkResponse.setServerStatusCode(response.getData().getCode());
                                sdkResponse.setServerStatusText(response.getData().getContent());
                            }
                        } else {
                            sdkResponse.setServerStatusCode(response.getResult().getCode());
                            sdkResponse.setServerStatusText(response.getResult().getMessage());
                        }
                        callback.onResponse(sdkResponse, null);
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetServerMsgResponse, ObservableSource<ProcessProfileResponse>>() {
                    @Override
                    public ObservableSource<ProcessProfileResponse> apply(GetServerMsgResponse response) throws Exception {
                        return ApiMethods.processProfile(RequestUtils.buildProcessProfileRequest(getUrlConfigResponse, profile2ModelData));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<ProcessProfileResponse>() {
                    @Override
                    public boolean test(final ProcessProfileResponse response) throws Exception {
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(SDKCode.SERVER_SUCCESS);
                            sdkResponse.setServerStatusText(response.getMsg() + "(" + response.getCode() + ")");
                            getRecomSizeById(activity, response.getData().getTaskId(), callback);
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<ProcessProfileResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(ProcessProfileResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
//                        if (!sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS) && !TextUtils.isEmpty(sdkResponse.getServerStatusCode())) {
//                            callback.onResponse(sdkResponse, null);
//                        }
                    }
                });
    }

    public void getRecomSizeByTask(final RxAppCompatActivity activity, final String taskId, final GetRecommendSizeCallback callback) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        RequestBody request = RequestUtils.buildUrlConfigRequest();
        ApiMethods.getSdkUrlConfig(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetUrlConfigResponse>() {
                    @Override
                    public boolean test(GetUrlConfigResponse response) throws Exception {
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            long timeoutDataGet = response.getData().getTimeoutDataGet();
                            SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.REQUEST_READ_TIME_OUT, timeoutDataGet);
                            SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.DATA_GET_URL, response.getData().getApiList().getSdkDataGet());
                            SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TUTORIAL_INFO_URL, response.getData().getApiList().getTutorialInfoUrl());
                            getRecomSizeById(activity, taskId, callback);
                            return false;
                        }
                        return true;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetUrlConfigResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetUrlConfigResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
//                        if (!sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS) && !TextUtils.isEmpty(sdkResponse.getServerStatusCode())) {
//                            callback.onResponse(sdkResponse, null);
//                        }
                    }
                });
    }

    private int getRecomSizeByProfileDataStatus;// 异步获取data的状态
    private int getRecomSizeByProfileDataCount;
    private String getRecomSizeByProfileTraceId;

    private void getRecomSizeById(final RxAppCompatActivity activity, final String taskId, final GetRecommendSizeCallback callback) {
        final SdkResponse sdkResponse = new SdkResponse();
        getRecomSizeByProfileDataStatus = 0;
        getRecomSizeByProfileDataCount = 0;
        getRecomSizeByProfileTraceId = "";
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(ServerKeys.TASK_ID, taskId)
                .addFormDataPart(ServerKeys.DATA_TYPE, String.valueOf(ApiType.GET_RECOMSIZE_BY_PROFILE))
                .build();
        ApiMethods.getData(requestBody)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
//                .retryUntil(new BooleanSupplier() {
//                    @Override
//                    public boolean getAsBoolean() throws Exception {
//                        return getRecomSizeByProfileDataStatus == 1;
//                    }
//                })
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        getRecomSizeByProfileDataCount++;
                        return getRecomSizeByProfileDataStatus == 1 || getRecomSizeByProfileDataCount == REPEAT_COUNT;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetDataResponse>() {
                    @Override
                    public boolean test(final GetDataResponse response) throws Exception {
                        if (getRecomSizeByProfileDataStatus == 0) {
                            if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                getRecomSizeByProfileDataStatus = response.getData().getStatus();
                                if (getRecomSizeByProfileDataStatus == 1) {
                                    if (response.getData().getApiData().getApiStatus() == 1) {
                                        sdkResponse.setServerStatusCode(response.getCode());
                                        sdkResponse.setServerStatusText(response.getMsg() + "(" + response.getCode() + ")");
                                        RecommendSizeData recommendSizeDataByProfile = new RecommendSizeData();
                                        recommendSizeDataByProfile.setRecommendSizeList(response.getData().getApiData().getResponseData().getSizeRecmd().getRecommendSizeList());
                                        callback.onResponse(sdkResponse, recommendSizeDataByProfile);
                                    } else {
                                        getRecomSizeByProfileTraceId = response.getData().getApiData().getTraceId();
                                        return true;
                                    }
                                }
                                return false;
                            } else {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetDataResponse, ObservableSource<GetServerMsgResponse>>() {
                    @Override
                    public ObservableSource<GetServerMsgResponse> apply(GetDataResponse response) throws Exception {
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put(ServerKeys.CODE, response.getCode());
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            params.put(ServerKeys.CODE, response.getData().getApiData().getApiCode());
                        }
                        params.put(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                        return ApiMethods.getServerMsgByCode(params);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetServerMsgResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetServerMsgResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(GetServerMsgResponse response) {
                        getRecomSizeByProfileDataStatus = 1;
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            sdkResponse.setServerStatusCode(response.getData().getCode());
                            if (!TextUtils.isEmpty(getRecomSizeByProfileTraceId)) {
                                sdkResponse.setServerStatusCode(response.getData().getCode() + ":" + getRecomSizeByProfileTraceId);
                            }
                            sdkResponse.setServerStatusText(response.getData().getContent());
                            callback.onResponse(sdkResponse, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + ResponseErrorUtils.getResponseError(e));
                        getRecomSizeByProfileDataStatus = 1;
                        sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        callback.onResponse(sdkResponse, null);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Over!");
                        if (getRecomSizeByProfileDataCount == REPEAT_COUNT) {
                            sdkResponse.setServerStatusCode(SERVER_UNKNOW_ERROR);
                            sdkResponse.setServerStatusText(activity.getString(R.string.socket_time_out_exception));
                            callback.onResponse(sdkResponse, null);
                        }
                    }
                });
    }

    public void getSdkUrlConfig(RxAppCompatActivity activity) {
        reInitSdk(activity);
        final SdkResponse sdkResponse = new SdkResponse();
        RequestBody request = RequestUtils.buildUrlConfigRequest();
        ApiMethods.getSdkUrlConfig(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetUrlConfigResponse>() {
                    @Override
                    public boolean test(GetUrlConfigResponse response) throws Exception {
                        if (response.getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            long timeoutDataGet = response.getData().getTimeoutDataGet();
                            SPUtils.getInstance(SPKeys.MEASURE_RESULT_URL).put(SPKeys.REQUEST_READ_TIME_OUT, timeoutDataGet);
                            SPUtils.getInstance(com.tozmart.tozisdk.constant.SPKeys.TOZSDK_SP_DATA).put(SPKeys.DATA_GET_URL, response.getData().getApiList().getSdkDataGet());
                            SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TUTORIAL_INFO_URL, response.getData().getApiList().getTutorialInfoUrl());
                            SPUtils.getInstance(com.tozmart.tozisdk.constant.SPKeys.MEASURE_RESULT_URL).put(SPKeys.MEASURE_RESULT_DATA, response.getData().getMeasureResultUrl());
                            return false;
                        }
                        return false;
                    }
                }).subscribe();
    }
}
