package com.luck.picture.lib.tools;

import android.os.Build;

/* loaded from: classes.dex */
public class SdkVersionUtils {
    public static boolean checkedAndroid_Q() {
        return Build.VERSION.SDK_INT >= 29;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= 14;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }
}
