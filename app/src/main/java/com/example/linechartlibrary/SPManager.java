package com.example.linechartlibrary;

import android.content.Context;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class SPManager {
    public static void clearUserInfo(Context context) {
        setAuthCode(context, "");
        setUserName(context, "");
        setRealName(context, "");
    }

    public static boolean isLogin(Context context) {
        return !TextUtils.isEmpty(getAuthCode(context));
    }

    public static String getAuthCode(Context context) {
        return (String) SPUtils.get(context, SharedPreferencesKey.SP_KEY_TOKEN, "");
    }

    public static String getUsername(Context context) {
        return (String) SPUtils.get(context, SharedPreferencesKey.SP_KEY_USERNAME, "");
    }

    public static String getRealName(Context context) {
        return (String) SPUtils.get(context, SharedPreferencesKey.SP_KEY_REALNAME, "");
    }

    public static boolean getExit(Context context) {
        return ((Boolean) SPUtils.get(context, SharedPreferencesKey.SP_KEY_EXIT, false)).booleanValue();
    }

    public static void setAuthCode(Context context, String str) {
        SPUtils.put(context, SharedPreferencesKey.SP_KEY_TOKEN, str, false);
    }

    public static void setUserName(Context context, String str) {
        SPUtils.put(context, SharedPreferencesKey.SP_KEY_USERNAME, str, false);
    }

    public static void setRealName(Context context, String str) {
        SPUtils.put(context, SharedPreferencesKey.SP_KEY_REALNAME, str, false);
    }

    public static void setExit(Context context, boolean z) {
        SPUtils.put(context, SharedPreferencesKey.SP_KEY_EXIT, Boolean.valueOf(z), false);
    }
}
