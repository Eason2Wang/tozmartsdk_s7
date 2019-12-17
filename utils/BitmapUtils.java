package com.tozmart.tozisdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import com.tozmart.tozisdk.constant.SPKeys;

import java.io.ByteArrayOutputStream;

/**
 * Created by Chale on 2019/11/28 0028.
 */
public class BitmapUtils {
    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        String img = Base64.encodeToString(appicon, Base64.DEFAULT);
        return img;

    }


    /**
     * string转成bitmap
     *
     * @param str
     */
    public static Bitmap convertStringToIcon(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(str, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    private static String frontBitmapStr;
    private static String sideBitmapStr;

    public static void setFrontBitmap(Bitmap bitmap){
        frontBitmapStr = BitmapUtils.convertIconToString(bitmap);
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TOZ_KEY_FRONT_BITMAP, frontBitmapStr);
    }

    public static void setSideBitmap(Bitmap bitmap){
        sideBitmapStr = BitmapUtils.convertIconToString(bitmap);
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TOZ_KEY_SIDE_BITMAP, sideBitmapStr);
    }

    public static Bitmap getFrontBitmap() {
        frontBitmapStr = SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).getString(SPKeys.TOZ_KEY_FRONT_BITMAP, null);
        return BitmapUtils.convertStringToIcon(frontBitmapStr);
    }

    public static Bitmap getSideBitmap() {
        sideBitmapStr = SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).getString(SPKeys.TOZ_KEY_SIDE_BITMAP, null);
        return BitmapUtils.convertStringToIcon(sideBitmapStr);
    }

    public static void clearBitmap(){
        frontBitmapStr = null;
        sideBitmapStr = null;
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TOZ_KEY_FRONT_BITMAP, frontBitmapStr);
        SPUtils.getInstance(SPKeys.TOZSDK_SP_DATA).put(SPKeys.TOZ_KEY_SIDE_BITMAP, sideBitmapStr);
    }
}
