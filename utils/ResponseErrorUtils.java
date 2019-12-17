/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tozmart.tozisdk.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ParseException;

import android.util.DisplayMetrics;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Locale;

import io.reactivex.exceptions.CompositeException;
import retrofit2.HttpException;

public class ResponseErrorUtils {

    public static String getResponseError(Throwable t) {
        setupLanguage();
        //这里不光只能打印错误, 还可以根据不同的错误做出不同的逻辑处理
        //这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        int code = -1;
        String msg = OneMeasureSDKLite.getInstance().getApplicationContext().getString(R.string.unknown_exception);
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            msg = OneMeasureSDKLite.getInstance().getApplicationContext().getString(R.string.connect_exception);
        } else if (t instanceof SocketTimeoutException) {
            msg = OneMeasureSDKLite.getInstance().getApplicationContext().getString(R.string.socket_time_out_exception);
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            code = httpException.code();
            msg = convertStatusCode(OneMeasureSDKLite.getInstance().getApplicationContext(), httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = OneMeasureSDKLite.getInstance().getApplicationContext().getString(R.string.data_parse_exception);
        } else if (t instanceof CompositeException) {
            CompositeException e = (CompositeException) t;
            msg = handleCompositeException(OneMeasureSDKLite.getInstance().getApplicationContext(), e);
        } else {
            msg = t.getMessage();
        }

        return msg;
    }

    private static String convertStatusCode(Context context, HttpException httpException) {
        setupLanguage();
        String msg;
        if (httpException.code() == 500) {
            msg = context.getString(R.string.server_error);
        } else if (httpException.code() == 404) {
            msg = context.getString(R.string.network_address_not_exist);
        } else if (httpException.code() == 403) {
            msg = context.getString(R.string.request_rejected);
        } else if (httpException.code() == 307) {
            msg = context.getString(R.string.request_redirected);
        } else if (httpException.code() == 401) {
            msg = context.getString(R.string.unauthorized);
        } else {
            msg = httpException.message() + "(" + httpException.code() + ")";
        }
        return msg;
    }

    private static String handleCompositeException (Context context, CompositeException exception) {
        setupLanguage();
        String msg = context.getString(R.string.unknown_exception);
        for (Throwable throwable : exception.getExceptions()) {
            if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
                msg = context.getString(R.string.connect_exception);
            } else if (throwable instanceof SocketTimeoutException) {
                msg = context.getString(R.string.socket_time_out_exception);
            } else if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                msg = convertStatusCode(context, httpException);
            } else if (throwable instanceof JsonParseException || throwable instanceof ParseException ||
                    throwable instanceof JSONException) {
                msg = context.getString(R.string.data_parse_exception);
            } else {
                msg = throwable.getMessage();
            }
        }
        return msg;
    }

    private static void setupLanguage(){
        Resources resources = OneMeasureSDKLite.getInstance().getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        config.locale = TozLanguageUtils.getCurrentLanguageLocal();
        resources.updateConfiguration(config, dm);
    }
}
