package com.home.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/* loaded from: classes.dex */
public class ScreenUtils {
    private static int height;
    private static ScreenUtils newInstance;
    private static int width;

    private ScreenUtils(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
    }

    public static ScreenUtils getInstance(Activity activity) {
        if (newInstance == null) {
            newInstance = new ScreenUtils(activity);
        }
        return newInstance;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
