package com.tozmart.tozisdk.http;

import android.content.Context;

/**
 * Created by wangyisong on 15/3/17.
 */

public class GetApiFromJni {

    public static native String getStringFromNative(int code, Context context);
}
