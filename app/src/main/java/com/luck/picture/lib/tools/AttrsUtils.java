package com.luck.picture.lib.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/* loaded from: classes.dex */
public class AttrsUtils {
    public static int getTypeValueColor(Context context, int i) {
        try {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new TypedValue().resourceId, new int[]{i});
            int color = obtainStyledAttributes.getColor(0, 0);
            obtainStyledAttributes.recycle();
            return color;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean getTypeValueBoolean(Context context, int i) {
        try {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new TypedValue().resourceId, new int[]{i});
            boolean z = obtainStyledAttributes.getBoolean(0, false);
            obtainStyledAttributes.recycle();
            return z;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Drawable getTypeValueDrawable(Context context, int i) {
        try {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new TypedValue().resourceId, new int[]{i});
            Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
