package com.tozmart.tozisdk.utils;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tozmart.tozisdk.constant.ApiNativeCode;
import com.tozmart.tozisdk.constant.Constants;
import com.tozmart.tozisdk.constant.Language;
import com.tozmart.tozisdk.constant.PhotoType;
import com.tozmart.tozisdk.constant.SPKeys;
import com.tozmart.tozisdk.constant.ServerKeys;
import com.tozmart.tozisdk.constant.Unit;
import com.tozmart.tozisdk.entity.CameraAngle;
import com.tozmart.tozisdk.entity.GetUrlConfigResponse;
import com.tozmart.tozisdk.entity.Pixel;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.tozmart.tozisdk.entity.ProfileBodyBean;
import com.tozmart.tozisdk.http.GetApiFromJni;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestUtils {

    public static RequestBody buildUrlConfigRequest() {
        RequestBody requestBody = null;
        String apiVersion = "v2.4";
        try {
            long timestamp = System.currentTimeMillis();
            Map<String, String> signMap = new LinkedHashMap<>();
            signMap.put(ServerKeys.APP_KEY, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppKey());
            signMap.put(ServerKeys.TIMESTAMP, String.valueOf(timestamp));
            signMap.put(ServerKeys.SIGN_METHOD, Constants.SIGN_METHOD_MD5);
            signMap.put(ServerKeys.NODE_ID, SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).getString(ServerKeys.NODE_ID, ""));
            signMap.put(ServerKeys.API_VERSION, apiVersion);
            String requestSign = SignUtils.signTopRequest(signMap, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppSecret(), Constants.SIGN_METHOD_MD5);
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(ServerKeys.APP_KEY, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppKey())
                    .addFormDataPart(ServerKeys.TIMESTAMP, String.valueOf(timestamp))
                    .addFormDataPart(ServerKeys.SIGN_METHOD, Constants.SIGN_METHOD_MD5)
                    .addFormDataPart(ServerKeys.SIGN, requestSign)
                    .addFormDataPart(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()))
                    .addFormDataPart(ServerKeys.NODE_ID, SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).getString(ServerKeys.NODE_ID, ""))
                    .addFormDataPart(ServerKeys.API_VERSION, apiVersion)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestBody;
    }

    /**
     * @param response
     * @param cameraAngle 倾斜的角度
     * @return
     */
    public static Observable<RequestBody> buildProcessImageRequest(
            final GetUrlConfigResponse response,
            final Bitmap bitmap, final int photoType,
            final CameraAngle cameraAngle,
            final String taskId) {
        return Observable.defer(new Callable<ObservableSource<RequestBody>>() {
            @Override
            public ObservableSource<RequestBody> call() throws Exception {
                // 配置请求url
                SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.IMAGE_PROCESS_URL, response.getData().getApiList().getSdkImgProcess());
                SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.DATA_GET_URL, response.getData().getApiList().getSdkDataGet());
                SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TUTORIAL_INFO_URL, response.getData().getApiList().getTutorialInfoUrl());

                /** encode image */
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, ImageUtils.getCompressQuality(bitmap, Constants.PHOTO_MAX_BYTE_SIZE), stream);
                byte[] photoByteArray = stream.toByteArray();
//                ImageUtils.save(ImageUtils.getBitmap(photoByteArray, 0), OneMeasureSDKLite.getInstance().getApplicationContext().getExternalFilesDir(null).getAbsoluteFile() + "/bit.jpg", Bitmap.CompressFormat.JPEG);
                if (TextUtils.isEmpty(taskId)) {
                    BitmapUtils.clearBitmap();
                }
                if (photoType == PhotoType.FRONT) {
                    BitmapUtils.setFrontBitmap(bitmap);
                } else if (photoType == PhotoType.SIDE) {
                    BitmapUtils.setSideBitmap(bitmap);
                }

                long timestamp = System.currentTimeMillis();
                Map<String, String> signMap = new LinkedHashMap<>();
                signMap.put(ServerKeys.APP_KEY, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppKey());
                signMap.put(ServerKeys.TIMESTAMP, String.valueOf(timestamp));
                signMap.put(ServerKeys.SIGN_METHOD, Constants.SIGN_METHOD_MD5);
                signMap.put(ServerKeys.USER_GENDER, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserGender()));
                signMap.put(ServerKeys.USER_HEIGHT, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserHeight()));
                signMap.put(ServerKeys.USER_WEIGHT, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserWeight()));
                signMap.put(ServerKeys.PHOTO_TYPE, String.valueOf(photoType));
                signMap.put(ServerKeys.SOURCE_TYPE, "1");
                signMap.put(ServerKeys.NODE_ID, response.getData().getNodeId());
                signMap.put(ServerKeys.USER_ID, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserId()));
                signMap.put(ServerKeys.TASK_ID, taskId == null ? "" : taskId);
                signMap.put(ServerKeys.CAMERA_ANGLE, new Gson().toJson(cameraAngle));
                signMap.put(ServerKeys.UNIT, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUnit() == Unit.METRIC ? "cm" : "inch");
                signMap.put(ServerKeys.MEASURE_NAME, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserName());

                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), photoByteArray);
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(ServerKeys.APP_KEY, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppKey())
                        .addFormDataPart(ServerKeys.TIMESTAMP, String.valueOf(timestamp))
                        .addFormDataPart(ServerKeys.SIGN_METHOD, Constants.SIGN_METHOD_MD5)
                        .addFormDataPart(ServerKeys.SIGN, SignUtils.signTopRequest(signMap, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppSecret(), Constants.SIGN_METHOD_MD5))
                        .addFormDataPart(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()))
                        .addFormDataPart(ServerKeys.NODE_ID, response.getData().getNodeId())
                        .addFormDataPart(ServerKeys.USER_GENDER, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserGender()))
                        .addFormDataPart(ServerKeys.USER_HEIGHT, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserHeight()))
                        .addFormDataPart(ServerKeys.USER_WEIGHT, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserWeight()))
                        .addFormDataPart(ServerKeys.MEASURE_NAME, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserName())
                        .addFormDataPart(ServerKeys.PHOTO_TYPE, String.valueOf(photoType))
                        .addFormDataPart(ServerKeys.SOURCE_TYPE, "1")
                        .addFormDataPart(ServerKeys.USER_ID, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserId()))
                        .addFormDataPart(ServerKeys.TASK_ID, taskId == null ? "" : taskId)
                        .addFormDataPart(ServerKeys.CAMERA_ANGLE, new Gson().toJson(cameraAngle))
                        .addFormDataPart(ServerKeys.UNIT, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUnit() == Unit.METRIC ? "cm" : "inch")
                        .addFormDataPart(ServerKeys.PHOTO_SRC, "file", fileBody)
                        .build();

                return Observable.just(requestBody);
            }
        });
    }

    public static RequestBody buildProcessProfileRequest(GetUrlConfigResponse response, Profile2ModelData profile2ModelData) {

        // 配置请求url
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.PROFILE_PROCESS_URL, response.getData().getApiList().getSdkProfileSize());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.DATA_GET_URL, response.getData().getApiList().getSdkDataGet());
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TUTORIAL_INFO_URL, response.getData().getApiList().getTutorialInfoUrl());

        ProfileBodyBean frontProfileBodyBean = new ProfileBodyBean();
        frontProfileBodyBean.setCAddInfo(profile2ModelData.getFrontCAddInfo());
        frontProfileBodyBean.setCLooseIdx(profile2ModelData.getfLooseIdx());
        frontProfileBodyBean.setCMovPxIdx(profile2ModelData.getFrontMoveIndex());
        frontProfileBodyBean.setCPaintLines(profile2ModelData.getFrontPaintLines());
        frontProfileBodyBean.setCSizeLines(profile2ModelData.getFrontCSizeLines());
        int l = profile2ModelData.getFrontAllPoints().size();
        Pixel[] frontPoints = new Pixel[l];
        Pixel pixel;
        PointF tempP;
        for (int i = 0; i < l; i++) {
            tempP = profile2ModelData.getFrontAllPoints().get(i);
            pixel = new Pixel(tempP.x, tempP.y);
            frontPoints[i] = pixel;
        }
        frontProfileBodyBean.setCallPx(frontPoints);

        ProfileBodyBean sideProfileBodyBean = new ProfileBodyBean();
        sideProfileBodyBean.setCAddInfo(profile2ModelData.getSideCAddInfo());
        sideProfileBodyBean.setCLooseIdx(profile2ModelData.getsLooseIdx());
        sideProfileBodyBean.setCMovPxIdx(profile2ModelData.getSideMoveIndex());
        sideProfileBodyBean.setCPaintLines(profile2ModelData.getSidePaintLines());
        sideProfileBodyBean.setCSizeLines(profile2ModelData.getSideCSizeLines());
        l = profile2ModelData.getSideAllPoints().size();
        Pixel[] sidePoints = new Pixel[l];
        for (int i = 0; i < l; i++) {
            tempP = profile2ModelData.getSideAllPoints().get(i);
            pixel = new Pixel(tempP.x, tempP.y);
            sidePoints[i] = pixel;
        }
        sideProfileBodyBean.setCallPx(sidePoints);

        Gson gson = new Gson();
        long timestamp = System.currentTimeMillis();
        Map<String, String> signMap = new LinkedHashMap<>();
        signMap.put(ServerKeys.APP_KEY, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppKey());
        signMap.put(ServerKeys.TIMESTAMP, String.valueOf(timestamp));
        signMap.put(ServerKeys.SIGN_METHOD, Constants.SIGN_METHOD_MD5);
        signMap.put(ServerKeys.USER_GENDER, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserGender()));
        signMap.put(ServerKeys.USER_HEIGHT, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserHeight()));
        signMap.put(ServerKeys.USER_WEIGHT, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserWeight()));
        signMap.put(ServerKeys.MEASURE_ID, profile2ModelData.getMeasureId());
        signMap.put(ServerKeys.PHOTO_NUM, String.valueOf(Constants.PHOTO_2_PIC_MODE));
        signMap.put(ServerKeys.USER_ID, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserId());
        signMap.put(ServerKeys.PROFESS_FLAG, String.valueOf(1));
        signMap.put(ServerKeys.NODE_ID, response.getData().getNodeId());
        signMap.put(ServerKeys.UNIT, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUnit() == Unit.METRIC ? "cm" : "inch");

        RequestBody requestBody = null;
        try {
            requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(ServerKeys.APP_KEY, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppKey())
                    .addFormDataPart(ServerKeys.TIMESTAMP, String.valueOf(timestamp))
                    .addFormDataPart(ServerKeys.SIGN_METHOD, Constants.SIGN_METHOD_MD5)
                    .addFormDataPart(ServerKeys.SIGN, SignUtils.signTopRequest(signMap, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getAppSecret(), Constants.SIGN_METHOD_MD5))
                    .addFormDataPart(ServerKeys.LANGUAGE, SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()))
                    .addFormDataPart(ServerKeys.USER_GENDER, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserGender()))
                    .addFormDataPart(ServerKeys.USER_HEIGHT, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserHeight()))
                    .addFormDataPart(ServerKeys.USER_WEIGHT, String.valueOf(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserWeight()))
                    .addFormDataPart(ServerKeys.MEASURE_ID, profile2ModelData.getMeasureId())
                    .addFormDataPart(ServerKeys.PHOTO_NUM, String.valueOf(Constants.PHOTO_2_PIC_MODE))
                    .addFormDataPart(ServerKeys.FRONT_PROFILE_BODY, gson.toJson(frontProfileBodyBean))
                    .addFormDataPart(ServerKeys.SIDE_PROFILE_BODY, gson.toJson(sideProfileBodyBean))
                    .addFormDataPart(ServerKeys.USER_ID, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserId())
                    .addFormDataPart(ServerKeys.PROFESS_FLAG, String.valueOf(1))
                    .addFormDataPart(ServerKeys.NODE_ID, response.getData().getNodeId())
                    .addFormDataPart(ServerKeys.UNIT, OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUnit() == Unit.METRIC ? "cm" : "inch")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return requestBody;
    }

    public static Map<String, Object> buildIPRequest() {
        Map<String, Object> params = new LinkedHashMap<>();
        switch (OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()) {
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
        return params;
    }
}
