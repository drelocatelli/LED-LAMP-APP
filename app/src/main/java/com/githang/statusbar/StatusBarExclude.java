package com.githang.statusbar;

import android.os.Build;

/* loaded from: classes.dex */
public class StatusBarExclude {
    static boolean exclude = false;

    public static void excludeIncompatibleFlyMe() {
        try {
            Build.class.getMethod("hasSmartBar", new Class[0]);
        } catch (NoSuchMethodException unused) {
            exclude |= Build.BRAND.contains("Meizu");
        }
    }
}
