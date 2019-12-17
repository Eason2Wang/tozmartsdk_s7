package com.tozmart.tozisdk.utils;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.activity.RxAppCompatActivity;
import com.tozmart.tozisdk.api.ErrorWarnListener;
import com.tozmart.tozisdk.api.MeasureInfoListener;
import com.tozmart.tozisdk.constant.Gender;
import com.tozmart.tozisdk.constant.Language;
import com.tozmart.tozisdk.constant.MeasurementsSpecialIds;
import com.tozmart.tozisdk.constant.PhotoType;
import com.tozmart.tozisdk.constant.SDKCode;
import com.tozmart.tozisdk.entity.ErrorWarn;
import com.tozmart.tozisdk.entity.GetDataResponseData;
import com.tozmart.tozisdk.entity.GetDescripCode;
import com.tozmart.tozisdk.entity.GetDescripItem;
import com.tozmart.tozisdk.entity.GetDescripRequest;
import com.tozmart.tozisdk.entity.GetDescripResponse;
import com.tozmart.tozisdk.entity.GetMeaInfoResponse;
import com.tozmart.tozisdk.entity.GetMeasurementsModel;
import com.tozmart.tozisdk.entity.ImageErrorWarnInfo;
import com.tozmart.tozisdk.entity.ImageProc;
import com.tozmart.tozisdk.entity.ImageProcessFeedback;
import com.tozmart.tozisdk.entity.MeaInfoEntity;
import com.tozmart.tozisdk.entity.MeasureResult;
import com.tozmart.tozisdk.entity.MeasurementEntity;
import com.tozmart.tozisdk.entity.MeasurementsData;
import com.tozmart.tozisdk.entity.Pixel;
import com.tozmart.tozisdk.entity.ProcessData;
import com.tozmart.tozisdk.entity.ProcessImageResponse;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.tozmart.tozisdk.entity.ProfileConfigBodyBean;
import com.tozmart.tozisdk.entity.ProfilePaintLine;
import com.tozmart.tozisdk.entity.SdkResponse;
import com.tozmart.tozisdk.http.ApiMethods;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.tozmart.tozisdk.constant.Unit.METRIC;

public class ResponseUtils {
    /**
     * 得到预测有问题的点的下标，设置为红色
     *
     * @param
     */

    public static void extractConfig(Profile2ModelData profile2ModelData, GetDataResponseData processData, int userGender) {
        String configString = processData.getProfile().getOutlineConfig().getConfigContent();
        if (configString != null) {
            try {
                JSONObject configObj = new JSONObject(configString);
                JSONArray male = configObj.isNull("1") ? null : configObj.getJSONArray("1");
                JSONArray female = configObj.isNull("0") ? null : configObj.getJSONArray("0");
                if (userGender == 1 && male != null) {
                    ProfileConfigBodyBean frontBean = new Gson().fromJson(male.get(0).toString(), ProfileConfigBodyBean.class);
                    ProfileConfigBodyBean sideBean = new Gson().fromJson(male.get(1).toString(), ProfileConfigBodyBean.class);
                    profile2ModelData.setFrontPaintLinesSelected(frontBean.getLinesSelected());
                    profile2ModelData.setFrontMoveIndexSelected(frontBean.getMptsSelected());
                    profile2ModelData.setSidePaintLinesSelected(sideBean.getLinesSelected());
                    profile2ModelData.setSideMoveIndexSelected(sideBean.getMptsSelected());
                }
                if (userGender == 0 && female != null) {
                    ProfileConfigBodyBean frontBean = new Gson().fromJson(female.get(0).toString(), ProfileConfigBodyBean.class);
                    ProfileConfigBodyBean sideBean = new Gson().fromJson(female.get(1).toString(), ProfileConfigBodyBean.class);
                    profile2ModelData.setFrontPaintLinesSelected(frontBean.getLinesSelected());
                    profile2ModelData.setFrontMoveIndexSelected(frontBean.getMptsSelected());
                    profile2ModelData.setSidePaintLinesSelected(sideBean.getLinesSelected());
                    profile2ModelData.setSideMoveIndexSelected(sideBean.getMptsSelected());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void extractLineInfo(Profile2ModelData profile2ModelData, GetDataResponseData processData) {
        profile2ModelData.setfLooseIdx(processData.getProfile().getFront().getProfileBody().getCLooseIdx());
        profile2ModelData.setsLooseIdx(processData.getProfile().getSide().getProfileBody().getCLooseIdx());

        profile2ModelData.setMeasureId(processData.getProfile().getMeasureId());
        profile2ModelData.setFrontCAddInfo(processData.getProfile().getFront().getProfileBody().getCAddInfo());
        profile2ModelData.setSideCAddInfo(processData.getProfile().getSide().getProfileBody().getCAddInfo());
        profile2ModelData.setFrontCSizeLines(processData.getProfile().getFront().getProfileBody().getCSizeLines());
        profile2ModelData.setSideCSizeLines(processData.getProfile().getSide().getProfileBody().getCSizeLines());
    }

    public static void extractAllPoints(Profile2ModelData profile2ModelData, GetDataResponseData processData) {
        Pixel[] frontPts = processData.getProfile().getFront().getProfileBody().getCallPx();
        Pixel[] sidePts = processData.getProfile().getSide().getProfileBody().getCallPx();

        // front
        ArrayList<PointF> frontAllPoints = new ArrayList<>();
        for (Pixel pointBean : frontPts) {
            frontAllPoints.add(new PointF(pointBean.getX(), pointBean.getY()));
        }

        // side
        ArrayList<PointF> sideAllPoints = new ArrayList<>();
        for (Pixel pointBean : sidePts) {
            sideAllPoints.add(new PointF(pointBean.getX(), pointBean.getY()));
        }

        profile2ModelData.setFrontAllPoints(frontAllPoints);
        profile2ModelData.setSideAllPoints(sideAllPoints);

        profile2ModelData.setFrontMoveIndex(processData.getProfile().getFront().getProfileBody().getCMovPxIdx());
        profile2ModelData.setSideMoveIndex(processData.getProfile().getSide().getProfileBody().getCMovPxIdx());

        profile2ModelData.setFrontStickIndex(new ArrayList<Integer>());
        profile2ModelData.setSideStickIndex(new ArrayList<Integer>());

        profile2ModelData.setFrontSpecialMoveIndex(0);
        profile2ModelData.setSideSpecialMoveIndex(0);

        ProfilePaintLine[] frontPaintLines = processData.getProfile().getFront().getProfileBody().getCPaintLines();
        for (ProfilePaintLine paintLine : frontPaintLines) {
            paintLine.setPaintLineId(new Random().nextInt());
        }
        profile2ModelData.setFrontPaintLines(frontPaintLines);
        ProfilePaintLine[] sidePaintLines = processData.getProfile().getSide().getProfileBody().getCPaintLines();
        for (ProfilePaintLine paintLine : sidePaintLines) {
            paintLine.setPaintLineId(new Random().nextInt());
        }
        profile2ModelData.setSidePaintLines(sidePaintLines);
    }

    public static void extractFaceRect(Profile2ModelData profile2ModelData, GetDataResponseData processData) {
        int[] frontFaceRectPts = processData.getProfile().getFront().getImgProc().getFaceRect();
        if (frontFaceRectPts.length == 4) {
            android.graphics.RectF frontFaceRect =
                    new android.graphics.RectF(
                            frontFaceRectPts[0],
                            frontFaceRectPts[1],
                            frontFaceRectPts[2],
                            frontFaceRectPts[3]);
            profile2ModelData.setFrontFaceRect(frontFaceRect);
        }

        int[] sideFaceRectPts = processData.getProfile().getSide().getImgProc().getFaceRect();
        if (sideFaceRectPts.length == 4) {
            android.graphics.RectF sideFaceRect =
                    new android.graphics.RectF(
                            sideFaceRectPts[0],
                            sideFaceRectPts[1],
                            sideFaceRectPts[2],
                            sideFaceRectPts[3]);
            profile2ModelData.setSideFaceRect(sideFaceRect);
        }
    }

    public static void extractBitmap(Profile2ModelData profile2ModelData, GetDataResponseData processData) {
        if (BitmapUtils.getFrontBitmap() == null) {
            Log.e("extractBitmap", "front bitmap is null");
            return;
        }
        Bitmap frontOriBitmap = Bitmap.createBitmap(BitmapUtils.getFrontBitmap());
        ImageProc frontImageProc = processData.getProfile().getFront().getImgProc();
        float refRatio = frontImageProc.getWidth() / (float) frontImageProc.getHeight();
        float ratio = frontOriBitmap.getWidth() / (float) frontOriBitmap.getHeight();
        // 参考值的宽高比例大，则以参考值的高作为缩放参考，反之以参考的宽作为缩放参考
        if (refRatio > ratio) {
            frontOriBitmap = ImageUtils.scale(frontOriBitmap, (int) (frontImageProc.getHeight() * ratio), frontImageProc.getHeight(), true);
        } else {
            frontOriBitmap = ImageUtils.scale(frontOriBitmap, frontImageProc.getWidth(), (int) (frontImageProc.getWidth() / ratio), true);
        }
        // 确保裁剪的宽高不会超过图片的大小
        int clipWidth = frontImageProc.getHumanRect()[2] > frontOriBitmap.getWidth() ?
                frontOriBitmap.getWidth() - frontImageProc.getHumanRect()[0] : frontImageProc.getHumanRect()[2] - frontImageProc.getHumanRect()[0];
        int clipHeight = frontImageProc.getHumanRect()[3] > frontOriBitmap.getHeight() ?
                frontOriBitmap.getHeight() - frontImageProc.getHumanRect()[1] : frontImageProc.getHumanRect()[3] - frontImageProc.getHumanRect()[1];
        Bitmap frontBitmap = ImageUtils.clip(frontOriBitmap, frontImageProc.getHumanRect()[0], frontImageProc.getHumanRect()[1],
                clipWidth, clipHeight, true);

        if (BitmapUtils.getSideBitmap() == null) {
            Log.e("extractBitmap", "side bitmap is null");
            return;
        }
        Bitmap sideOriBitmap = Bitmap.createBitmap(BitmapUtils.getSideBitmap());
        ImageProc sideImageProc = processData.getProfile().getSide().getImgProc();
        refRatio = sideImageProc.getWidth() / (float) sideImageProc.getHeight();
        ratio = sideOriBitmap.getWidth() / (float) sideOriBitmap.getHeight();
        // 参考值的宽高比例大，则以参考值的高作为缩放参考，反之以参考的宽作为缩放参考
        if (refRatio > ratio) {
            sideOriBitmap = ImageUtils.scale(sideOriBitmap, (int) (sideImageProc.getHeight() * ratio), sideImageProc.getHeight(), true);
        } else {
            sideOriBitmap = ImageUtils.scale(sideOriBitmap, sideImageProc.getWidth(), (int) (sideImageProc.getWidth() / ratio), true);
        }
        // 确保裁剪的宽高不会超过图片的大小
        clipWidth = sideImageProc.getHumanRect()[2] > sideOriBitmap.getWidth() ?
                sideOriBitmap.getWidth() - sideImageProc.getHumanRect()[0] : sideImageProc.getHumanRect()[2] - sideImageProc.getHumanRect()[0];
        clipHeight = sideImageProc.getHumanRect()[3] > sideOriBitmap.getHeight() ?
                sideOriBitmap.getHeight() - sideImageProc.getHumanRect()[1] : sideImageProc.getHumanRect()[3] - sideImageProc.getHumanRect()[1];
        Bitmap sideBitmap = ImageUtils.clip(sideOriBitmap, sideImageProc.getHumanRect()[0], sideImageProc.getHumanRect()[1],
                clipWidth, clipHeight, true);
        if (sideImageProc.getHorizonal_INV() == 1) {
            sideBitmap = ImageUtils.mirrorY(sideBitmap);
        }
        if (sideImageProc.getVerizonal_INV() == 1) {
            sideBitmap = ImageUtils.mirrorX(sideBitmap);
        }

        profile2ModelData.setFrontProcessedBitmap(frontBitmap);
        profile2ModelData.setSideProcessedBitmap(sideBitmap);
    }

    public static void extractErrorWarnInfo(RxAppCompatActivity activity, final ProcessImageResponse processImageResponse,
                                            final SdkResponse sdkResponse, final ProcessData processData,
                                            final int photoType, final float imageScale, final ErrorWarnListener listener) {
        GetDescripRequest getDescripRequest = new GetDescripRequest();
        List<GetDescripCode> getDescripCodeList = new ArrayList<>();
        if (processImageResponse.getData().getErrorInfo() != null && processImageResponse.getData().getErrorInfo().length != 0) {
            for (ImageErrorWarnInfo imageErrorWarnInfo : processImageResponse.getData().getErrorInfo()) {
                GetDescripCode getDescripCode = new GetDescripCode();
                getDescripCode.setIdApp("20");
                getDescripCode.setCode(String.valueOf(imageErrorWarnInfo.getID()));
                getDescripCode.setLang(SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                getDescripCodeList.add(getDescripCode);
            }
        }
        if (processImageResponse.getData().getWarnInfo() != null && processImageResponse.getData().getWarnInfo().length != 0) {
            for (ImageErrorWarnInfo imageErrorWarnInfo : processImageResponse.getData().getWarnInfo()) {
                GetDescripCode getDescripCode = new GetDescripCode();
                getDescripCode.setIdApp("20");
                getDescripCode.setCode(String.valueOf(imageErrorWarnInfo.getID()));
                getDescripCode.setLang(SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                getDescripCodeList.add(getDescripCode);
            }
        }
        getDescripRequest.setCodes(getDescripCodeList);

        ApiMethods.getDescripByCodes(getDescripRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetDescripResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetDescripResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetDescripResponse response) {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            ImageProcessFeedback imageProcessFeedback = new ImageProcessFeedback();
                            List<ErrorWarn> imageErrors = new ArrayList<>();
                            for (GetDescripItem getDescripItem : response.getData().getList()) {
                                if (processImageResponse.getData().getErrorInfo() != null && processImageResponse.getData().getErrorInfo().length != 0) {
                                    for (ImageErrorWarnInfo imageErrorWarnInfo : processImageResponse.getData().getErrorInfo()) {
                                        if (String.valueOf(imageErrorWarnInfo.getID()).equals(getDescripItem.getCode())) {
                                            List<Point> points = new ArrayList<>();
                                            if (!imageErrorWarnInfo.getPosition().equals("null")) {
                                                String[] positions = imageErrorWarnInfo.getPosition().split(",");
                                                for (String position : positions) {
                                                    if (!TextUtils.isEmpty(position)) {
                                                        String[] p = position.split(":");
                                                        if (p.length == 3) {
                                                            points.add(new Point((int) (Integer.parseInt(p[0]) * imageScale), (int) (Integer.parseInt(p[1]) * imageScale)));
                                                        }
                                                    }
                                                }
                                            }
                                            ErrorWarn error = new ErrorWarn(getDescripItem.getContent(), points);
                                            imageErrors.add(error);
                                            break;
                                        }
                                    }
                                }
                            }
                            if (photoType == PhotoType.FRONT) {
                                imageProcessFeedback.setFrontImageErrors(imageErrors);
                            } else {
                                imageProcessFeedback.setSideImageErrors(imageErrors);
                            }
                            processData.setImageProcessFeedback(imageProcessFeedback);
                            if (listener != null) {
                                listener.onSuccess();
                            }
                        } else {
                            sdkResponse.setServerStatusCode(String.valueOf(response.getResult().getCode()));
                            sdkResponse.setServerStatusText(response.getResult().getMessage());
                            if (listener != null) {
                                listener.onError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        sdkResponse.setServerStatusCode(SDKCode.SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        if (listener != null) {
                            listener.onError();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static void extractErrorWarnInfo(RxAppCompatActivity activity, final SdkResponse sdkResponse, final Profile2ModelData profile2ModelData,
                                            final GetDataResponseData processData, final ErrorWarnListener listener) {
        GetDescripRequest getDescripRequest = new GetDescripRequest();
        List<GetDescripCode> getDescripCodeList = new ArrayList<>();
        if (processData.getProfile().getFront() != null && processData.getProfile().getFront().getErrorInfo() != null && processData.getProfile().getFront().getErrorInfo().length != 0) {
            for (ImageErrorWarnInfo imageErrorWarnInfo : processData.getProfile().getFront().getErrorInfo()) {
                GetDescripCode getDescripCode = new GetDescripCode();
                getDescripCode.setIdApp("20");
                getDescripCode.setCode(String.valueOf(imageErrorWarnInfo.getID()));
                getDescripCode.setLang(SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                getDescripCodeList.add(getDescripCode);
            }
        }
        if (processData.getProfile().getFront() != null && processData.getProfile().getFront().getWarnInfo() != null && processData.getProfile().getFront().getWarnInfo().length != 0) {
            for (ImageErrorWarnInfo imageErrorWarnInfo : processData.getProfile().getFront().getWarnInfo()) {
                GetDescripCode getDescripCode = new GetDescripCode();
                getDescripCode.setIdApp("20");
                getDescripCode.setCode(String.valueOf(imageErrorWarnInfo.getID()));
                getDescripCode.setLang(SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                getDescripCodeList.add(getDescripCode);
            }
        }
        if (processData.getProfile().getSide() != null && processData.getProfile().getSide().getErrorInfo() != null && processData.getProfile().getSide().getErrorInfo().length != 0) {
            for (ImageErrorWarnInfo imageErrorWarnInfo : processData.getProfile().getSide().getErrorInfo()) {
                GetDescripCode getDescripCode = new GetDescripCode();
                getDescripCode.setIdApp("20");
                getDescripCode.setCode(String.valueOf(imageErrorWarnInfo.getID()));
                getDescripCode.setLang(SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                getDescripCodeList.add(getDescripCode);
            }
        }
        if (processData.getProfile().getSide() != null && processData.getProfile().getSide().getWarnInfo() != null && processData.getProfile().getSide().getWarnInfo().length != 0) {
            for (ImageErrorWarnInfo imageErrorWarnInfo : processData.getProfile().getSide().getWarnInfo()) {
                GetDescripCode getDescripCode = new GetDescripCode();
                getDescripCode.setIdApp("20");
                getDescripCode.setCode(String.valueOf(imageErrorWarnInfo.getID()));
                getDescripCode.setLang(SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                getDescripCodeList.add(getDescripCode);
            }
        }
        getDescripRequest.setCodes(getDescripCodeList);

        ApiMethods.getDescripByCodes(getDescripRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetDescripResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetDescripResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetDescripResponse response) {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            ImageProcessFeedback imageProcessFeedback = new ImageProcessFeedback();
                            List<ErrorWarn> frontImageErrors = new ArrayList<>();
                            List<ErrorWarn> frontImageWarns = new ArrayList<>();
                            List<ErrorWarn> sideImageErrors = new ArrayList<>();
                            List<ErrorWarn> sideImageWarns = new ArrayList<>();
                            for (GetDescripItem getDescripItem : response.getData().getList()) {
                                if (processData.getProfile().getFront() != null && processData.getProfile().getFront().getErrorInfo() != null && processData.getProfile().getFront().getErrorInfo().length != 0) {
                                    for (ImageErrorWarnInfo imageErrorWarnInfo : processData.getProfile().getFront().getErrorInfo()) {
                                        if (String.valueOf(imageErrorWarnInfo.getID()).equals(getDescripItem.getCode())) {
                                            List<Point> points = new ArrayList<>();
                                            if (!imageErrorWarnInfo.getPosition().equals("null")) {
                                                String[] positions = imageErrorWarnInfo.getPosition().split(",");
                                                for (String position : positions) {
                                                    if (!TextUtils.isEmpty(position)) {
                                                        String[] p = position.split(":");
                                                        if (p.length == 3) {
                                                            points.add(new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])));
                                                        }
                                                    }
                                                }
                                            }
                                            ErrorWarn error = new ErrorWarn(getDescripItem.getContent(), points);
                                            frontImageErrors.add(error);
                                            break;
                                        }
                                    }
                                }
                                if (processData.getProfile().getFront() != null && processData.getProfile().getFront().getWarnInfo() != null && processData.getProfile().getFront().getWarnInfo().length != 0) {
                                    for (ImageErrorWarnInfo imageErrorWarnInfo : processData.getProfile().getFront().getWarnInfo()) {
                                        if (String.valueOf(imageErrorWarnInfo.getID()).equals(getDescripItem.getCode())) {
                                            List<Point> points = new ArrayList<>();
                                            if (!imageErrorWarnInfo.getPosition().equals("null")) {
                                                String[] positions = imageErrorWarnInfo.getPosition().split(",");
                                                for (String position : positions) {
                                                    if (!TextUtils.isEmpty(position)) {
                                                        String[] p = position.split(":");
                                                        if (p.length == 3) {
                                                            points.add(new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])));
                                                        }
                                                    }
                                                }
                                            }
                                            ErrorWarn warn = new ErrorWarn(getDescripItem.getContent(), points);
                                            frontImageWarns.add(warn);
                                            break;
                                        }
                                    }
                                }
                                if (processData.getProfile().getSide() != null && processData.getProfile().getSide().getErrorInfo() != null && processData.getProfile().getSide().getErrorInfo().length != 0) {
                                    for (ImageErrorWarnInfo imageErrorWarnInfo : processData.getProfile().getSide().getErrorInfo()) {
                                        if (String.valueOf(imageErrorWarnInfo.getID()).equals(getDescripItem.getCode())) {
                                            List<Point> points = new ArrayList<>();
                                            if (!imageErrorWarnInfo.getPosition().equals("null")) {
                                                String[] positions = imageErrorWarnInfo.getPosition().split(",");
                                                for (String position : positions) {
                                                    if (!TextUtils.isEmpty(position)) {
                                                        String[] p = position.split(":");
                                                        if (p.length == 3) {
                                                            points.add(new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])));
                                                        }
                                                    }
                                                }
                                            }
                                            ErrorWarn error = new ErrorWarn(getDescripItem.getContent(), points);
                                            sideImageErrors.add(error);
                                            break;
                                        }
                                    }
                                }
                                if (processData.getProfile().getSide() != null && processData.getProfile().getSide().getWarnInfo() != null && processData.getProfile().getSide().getWarnInfo().length != 0) {
                                    for (ImageErrorWarnInfo imageErrorWarnInfo : processData.getProfile().getSide().getWarnInfo()) {
                                        if (String.valueOf(imageErrorWarnInfo.getID()).equals(getDescripItem.getCode())) {
                                            List<Point> points = new ArrayList<>();
                                            if (!imageErrorWarnInfo.getPosition().equals("null")) {
                                                String[] positions = imageErrorWarnInfo.getPosition().split(",");
                                                for (String position : positions) {
                                                    if (!TextUtils.isEmpty(position)) {
                                                        String[] p = position.split(":");
                                                        if (p.length == 3) {
                                                            points.add(new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])));
                                                        }
                                                    }
                                                }
                                            }
                                            ErrorWarn warn = new ErrorWarn(getDescripItem.getContent(), points);
                                            sideImageWarns.add(warn);
                                            break;
                                        }
                                    }
                                }
                            }
                            imageProcessFeedback.setFrontImageErrors(frontImageErrors);
                            imageProcessFeedback.setFrontImageWarns(frontImageWarns);
                            imageProcessFeedback.setSideImageErrors(sideImageErrors);
                            imageProcessFeedback.setSideImageWarns(sideImageWarns);
                            profile2ModelData.setImageProcessFeedback(imageProcessFeedback);
                            if (listener != null) {
                                listener.onSuccess();
                            }
                        } else {
                            sdkResponse.setServerStatusCode(String.valueOf(response.getResult().getCode()));
                            sdkResponse.setServerStatusText(response.getResult().getMessage());
                            if (listener != null) {
                                listener.onError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        sdkResponse.setServerStatusCode(SDKCode.SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        if (listener != null) {
                            listener.onError();
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static void extractMeasurements(final RxAppCompatActivity activity, final SdkResponse sdkResponse,
                                           final GetMeasurementsModel responseData,
                                           final MeasurementsData measurementsData,
                                           final MeasureInfoListener listener) {

        ApiMethods.getMeasurementInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<GetMeaInfoResponse>() {
                    @Override
                    public boolean test(GetMeaInfoResponse response) throws Exception {
                        if (response != null) {
                            if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                                String json = new Gson().toJson(response);
                                measurementsData.setStandardResponse(json);
                                return true;
                            } else {
                                sdkResponse.setServerStatusCode(String.valueOf(response.getResult().getCode()));
                                sdkResponse.setServerStatusText(response.getResult().getMessage() + "(" + response.getResult().getCode() + ")");
                            }
                        } else {
                            sdkResponse.setServerStatusCode(SDKCode.SERVER_UNKNOW_ERROR);
                            sdkResponse.setServerStatusText(activity.getString(R.string.server_error));
                        }
                        return false;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<GetMeaInfoResponse, ObservableSource<GetDescripResponse>>() {
                    @Override
                    public ObservableSource<GetDescripResponse> apply(GetMeaInfoResponse response) throws Exception {
                        List<MeasurementEntity> measurementEntities = new ArrayList<>();
                        GetDescripRequest getDescripRequest = new GetDescripRequest();
                        List<GetDescripCode> getDescripCodeList = new ArrayList<>();
                        List<MeaInfoEntity> meaInfoEntities;
                        if (OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserGender() == Gender.MALE) {
                            meaInfoEntities = response.getMeasItem4Male();
                        } else {
                            meaInfoEntities = response.getMeasItem4Female();
                        }
                        if (responseData != null) {
                            try {
                                int l = responseData.getMeasureInfo().size();
                                float cm = 0;
                                for (int i = 0; i < l; i++) {
                                    MeasureResult measureResult = responseData.getMeasureInfo().get(i);
                                    cm = measureResult.getMeasure();

                                    MeasurementEntity entity = new MeasurementEntity();
                                    entity.setCode(new String(Base64.encode(measureResult.getName().getBytes(), Base64.NO_WRAP)));

                                    for (MeaInfoEntity meaInfoEntity : meaInfoEntities) {
                                        if (meaInfoEntity.getItemCode().equals(measureResult.getName())) {
                                            if (measureResult.getIsShow() == 1) {
                                                if (OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUserGender() == Gender.MALE) {
                                                    if (measureResult.getName().equals(MeasurementsSpecialIds.MALE_SHOULDER_SLOPE_ID) ||
                                                            measureResult.getName().equals(MeasurementsSpecialIds.MALE_SHOULDER_SLOPE_ID1)) {
                                                        entity.setMeaValue(String.valueOf(Math.round(cm * 10) / 10.f));
                                                        entity.setUnit("°");
                                                    } else {
                                                        if (OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUnit() == METRIC) {
                                                            entity.setMeaValue(String.valueOf(Math.round(cm * 10) / 10.f));
                                                            entity.setUnit("cm");
                                                        } else {
                                                            entity.setMeaValue(CalcUnit.cm2inch(cm));
                                                            entity.setUnit("''");
                                                        }
                                                    }
                                                } else {
                                                    if (measureResult.getName().equals(MeasurementsSpecialIds.FEMALE_SHOULDER_SLOPE_ID) ||
                                                            measureResult.getName().equals(MeasurementsSpecialIds.FEMALE_SHOULDER_SLOPE_ID1)) {
                                                        entity.setMeaValue(String.valueOf(Math.round(cm * 10) / 10.f));
                                                        entity.setUnit("°");
                                                    } else {
                                                        if (OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getUnit() == METRIC) {
                                                            entity.setMeaValue(String.valueOf(Math.round(cm * 10) / 10.f));
                                                            entity.setUnit("cm");
                                                        } else {
                                                            entity.setMeaValue(CalcUnit.cm2inch(cm));
                                                            entity.setUnit("''");
                                                        }
                                                    }
                                                }
                                            } else {
                                                switch (OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()) {
                                                    case Language.CHINESE:
                                                        entity.setMeaValue("专业版限定尺寸");
                                                        break;
                                                    case Language.ENGLISH:
                                                        entity.setMeaValue("Available in pro version");
                                                        break;
                                                    case Language.TRADITION_CHINESE:
                                                        entity.setMeaValue("專業版限定尺寸");
                                                        break;
                                                    case Language.JAPANESE:
                                                        entity.setMeaValue("企業版限定サイズ");
                                                        break;
                                                    default:
                                                        entity.setMeaValue("Available in pro version");
                                                        break;
                                                }
                                                entity.setUnit("");
                                            }
                                            entity.setShowValue(measureResult.getIsShow());
                                            entity.setImageUrl(meaInfoEntity.getIconUrl());
                                            GetDescripCode getDescripCode = new GetDescripCode();
                                            getDescripCode.setCode(meaInfoEntity.getItemCode());

                                            GetDescripCode getDescripCodeDesc = new GetDescripCode();
                                            getDescripCodeDesc.setCode(meaInfoEntity.getItemCode() + "_desc");
                                            getDescripCode.setLang(SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                                            getDescripCodeDesc.setLang(SDKUtil.getLangStr(OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo().getLanguage()));
                                            getDescripCodeList.add(getDescripCode);
                                            getDescripCodeList.add(getDescripCodeDesc);
                                            measurementEntities.add(entity);
                                            break;
                                        }
                                    }
                                }
                                measurementsData.setMeasurementEntities(measurementEntities);
                                getDescripRequest.setCodes(getDescripCodeList);
                            } catch (Exception je) {
                                je.printStackTrace();
                            }
                        }
                        return ApiMethods.getDescripByCodes(getDescripRequest);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(activity.<GetDescripResponse>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<GetDescripResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetDescripResponse response) {
                        if (response.getResult().getCode().equals(SDKCode.SERVER_SUCCESS)) {
                            for (GetDescripItem getDescripItem : response.getData().getList()) {
                                for (MeasurementEntity measurementEntity : measurementsData.getMeasurementEntities()) {
                                    if (getDescripItem.getCode().equals(new String(Base64.decode(measurementEntity.getCode(), Base64.NO_WRAP)))) {
                                        measurementEntity.setSizeName(getDescripItem.getContent());
                                    } else if (getDescripItem.getCode().equals(new String(Base64.decode(measurementEntity.getCode(), Base64.NO_WRAP)) + "_desc")) {
                                        measurementEntity.setSizeIntro(getDescripItem.getContent());
                                    }
                                }
                            }
                            if (listener != null) {
                                listener.onSuccess();
                            }
                        } else {
                            sdkResponse.setServerStatusCode(String.valueOf(response.getResult().getCode()));
                            sdkResponse.setServerStatusText(response.getResult().getMessage());
                            if (listener != null) {
                                listener.onError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        sdkResponse.setServerStatusCode(SDKCode.SERVER_UNKNOW_ERROR);
                        sdkResponse.setServerStatusText(ResponseErrorUtils.getResponseError(e));
                        if (listener != null) {
                            listener.onError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (!sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS)) {
                            if (listener != null) {
                                listener.onError();
                            }
                        }
                    }
                });
    }
}
