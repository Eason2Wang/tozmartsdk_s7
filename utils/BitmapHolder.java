//package com.tozmart.tozisdk.utils;
//
//import android.graphics.Bitmap;
//import android.support.annotation.Nullable;
//
//public class BitmapHolder {
//
//    private static Bitmap frontBitmap;
//    private static Bitmap sideBitmap;
//
//    public static void setFrontBitmap(@Nullable Bitmap frontBitmap) {
//        BitmapHolder.frontBitmap = frontBitmap;
//    }
//
//    @Nullable
//    public static Bitmap getFrontBitmap() {
//        return frontBitmap;
//    }
//
//    public static void setSideBitmap(@Nullable Bitmap sideBitmap) {
//        BitmapHolder.sideBitmap = sideBitmap;
//    }
//
//    @Nullable
//    public static Bitmap getSideBitmap() {
//        return sideBitmap;
//    }
//
//    public static void recycle() {
//        if (frontBitmap != null && !frontBitmap.isRecycled()){
//            frontBitmap.recycle();
//        }
//        if (sideBitmap != null && !sideBitmap.isRecycled()){
//            sideBitmap.recycle();
//        }
//    }
//}
