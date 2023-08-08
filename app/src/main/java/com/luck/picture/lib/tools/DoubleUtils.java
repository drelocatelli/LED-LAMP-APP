package com.luck.picture.lib.tools;

/* loaded from: classes.dex */
public class DoubleUtils {
    private static final long TIME = 800;
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime < TIME) {
            return true;
        }
        lastClickTime = currentTimeMillis;
        return false;
    }
}
