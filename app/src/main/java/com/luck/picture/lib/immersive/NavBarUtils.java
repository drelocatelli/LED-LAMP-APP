package com.luck.picture.lib.immersive;

import android.app.Activity;
import android.os.Build;
import android.view.Window;

/* loaded from: classes.dex */
public class NavBarUtils {
    public static void setNavBarColor(Activity activity, int i) {
        setNavBarColor(activity.getWindow(), i);
    }

    public static void setNavBarColor(Window window, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.setNavigationBarColor(i);
        }
    }
}
