package com.luck.picture.lib.tools;

import android.content.Context;
import android.widget.Toast;

/* loaded from: classes.dex */
public final class ToastUtils {
    public static void s(Context context, String str) {
        Toast.makeText(context.getApplicationContext(), str, 0).show();
    }
}
