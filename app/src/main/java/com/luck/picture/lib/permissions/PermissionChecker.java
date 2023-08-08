package com.luck.picture.lib.permissions;

import android.app.Activity;
import android.content.Context;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/* loaded from: classes.dex */
public class PermissionChecker {
    public static boolean checkSelfPermission(Context context, String str) {
        return ContextCompat.checkSelfPermission(context.getApplicationContext(), str) == 0;
    }

    public static void requestPermissions(Activity activity, String[] strArr, int i) {
        ActivityCompat.requestPermissions(activity, strArr, i);
    }
}
