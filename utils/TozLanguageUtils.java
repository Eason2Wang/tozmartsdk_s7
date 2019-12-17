package com.tozmart.tozisdk.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.tozmart.tozisdk.constant.SPKeys;

import java.util.Locale;

/**
 * Created by tracy on 17/12/26.
 */

public class TozLanguageUtils {
    public static final int AUTO = 0;
    public static final int ENGLISH = 1;
    public static final int SIMPLE_CHINESE = 2;
    public static final int TRADITION_CHINESE = 3;
    public static final int JAPANESE = 4;

    public static final String SERVER_LANG_ENGLISH = "en-US";
    public static final String SERVER_LANG_SIMPLE_CHINESE = "zh-CN";
    public static final String SERVER_LANG_TRADITION_CHINESE = "zh-HK";
    public static final String SERVER_LANG_JAPANESE = "ja-JP";

    public static final int SDK_ENGLISH = 0;
    public static final int SDK_SIMPLE_CHINESE = 1;
    public static final int SDK_TRADITION_CHINESE = 2;
    public static final int SDK_JAPANESE = 3;

    public static final String D3_ENGLISH = "en";
    public static final String D3_SIMPLE_CHINESE = "cn";
    public static final String D3_TRADITION_CHINESE = "tw";
    public static final String D3_JAPANESE = "ja";

    public static String[] languageStrings = new String[]{
            "auto",
            Locale.ENGLISH.toString(),
            Locale.SIMPLIFIED_CHINESE.toString(),
            Locale.CHINESE.toString(),
            Locale.JAPANESE.toString()
    };

    public static void initAppLanguage(Activity activity, int position) {
        setLanguage(position);
        Resources resources = activity.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        config.locale = getCurrentLanguageLocal();
        resources.updateConfiguration(config, dm);
    }

    private static String appLang = "";

    /**
     * 设置语言
     */
    public static void setLanguage(int position) {
        if (getLanguagePosition() != position) {
            if (position == SIMPLE_CHINESE) {
                appLang = languageStrings[SIMPLE_CHINESE];
            } else if (position == TRADITION_CHINESE) {
                appLang = languageStrings[TRADITION_CHINESE];
            } else if (position == ENGLISH) {
                appLang = languageStrings[ENGLISH];
            } else if (position == JAPANESE) {
                appLang = languageStrings[JAPANESE];
            } else {
                appLang = Locale.getDefault().toString();
            }
        }
    }

    /**
     * 获取语言position
     */
    public static int getLanguagePosition() {
        // 读取储存的语言设置信息
        int languagePosition;
        if (appLang.equals(languageStrings[SIMPLE_CHINESE])) {
            languagePosition = SIMPLE_CHINESE;
        } else if (appLang.equals(languageStrings[TRADITION_CHINESE])) {
            languagePosition = TRADITION_CHINESE;
        } else if (appLang.equals(languageStrings[ENGLISH])) {
            languagePosition = ENGLISH;
        } else if (appLang.equals(languageStrings[JAPANESE])) {
            languagePosition = JAPANESE;
        } else {
            languagePosition = AUTO;
        }
        return languagePosition;
    }

    /**
     * 获取当前语言
     */
    public static Locale getCurrentLanguageLocal() {
        // 读取储存的语言设置信息
        // 读取储存的语言设置信息
        String lang = appLang;
        if (lang.equals(languageStrings[SIMPLE_CHINESE])) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (lang.equals(languageStrings[TRADITION_CHINESE])) {
            return Locale.TRADITIONAL_CHINESE;
        } else if (lang.equals(languageStrings[ENGLISH])) {
            return Locale.ENGLISH;
        } else if (lang.equals(languageStrings[JAPANESE])) {
            return Locale.JAPANESE;
        } else {
            return Locale.getDefault();
        }
    }

    /**
     * 获取当前服务器国际化语言
     */
    public static String getCurrentServerLanguage() {
        // 读取储存的语言设置信息
        String lang = appLang;
        if (lang.equals(languageStrings[SIMPLE_CHINESE])) {
            return SERVER_LANG_SIMPLE_CHINESE;
        } else if (lang.equals(languageStrings[TRADITION_CHINESE])) {
            return SERVER_LANG_TRADITION_CHINESE;
        } else if (lang.equals(languageStrings[ENGLISH])) {
            return SERVER_LANG_ENGLISH;
        } else if (lang.equals(languageStrings[JAPANESE])) {
            return SERVER_LANG_JAPANESE;
        } else {
            String defaultLang = Locale.getDefault().toString();
            if (defaultLang.contains("zh_CN")) {
                return SERVER_LANG_SIMPLE_CHINESE;
            } else if ((defaultLang.contains("zh") && !defaultLang.contains("zh_CN"))) {
                return SERVER_LANG_TRADITION_CHINESE;
            } else if (defaultLang.contains("ja")) {
                return SERVER_LANG_JAPANESE;
            } else {
                return SERVER_LANG_ENGLISH;
            }
        }
    }
}
