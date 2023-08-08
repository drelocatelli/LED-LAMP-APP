package com.githang.statusbar;

import android.content.Context;
import android.content.res.Resources;

/* loaded from: classes.dex */
public class StatusBarTools {
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (identifier > 0) {
            return resources.getDimensionPixelSize(identifier);
        }
        return 0;
    }
}
