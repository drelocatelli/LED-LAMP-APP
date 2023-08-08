package com.common.uitl;

import android.util.Log;

/* loaded from: classes.dex */
public class LogUtil {
    private static final boolean DEBUG = true;

    public static void v(String str, String str2) {
        Log.v(str, str2);
    }

    public static void d(String str, String str2) {
        d(str, str2, null);
    }

    public static void d(String str, String str2, Throwable th) {
        Log.d(str, str2, th);
    }

    public static void i(String str, String str2) {
        Log.i(str, str2);
    }

    public static void w(String str, String str2) {
        Log.w(str, str2);
    }

    public static void e(String str, String str2) {
        e(str, str2, null);
    }

    public static void e(String str, String str2, Throwable th) {
        Log.e(str, str2, th);
    }

    public static void println(String str) {
        System.out.println(str);
    }
}
