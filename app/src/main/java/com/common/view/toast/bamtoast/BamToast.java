package com.common.view.toast.bamtoast;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import com.common.view.toast.bamtoast.btoast.BToast;
import com.common.view.toast.bamtoast.etoast.EToast;

/* loaded from: classes.dex */
public class BamToast {
    public static final int ICONTYPE_ERROR = 2;
    public static final int ICONTYPE_NONE = 0;
    public static final int ICONTYPE_SUCCEED = 1;
    private static int checkNotification;
    private static Object mToast;

    private BamToast(Context context, CharSequence charSequence, int i, int i2) {
        if (mToast != null) {
            cancel();
        }
        if (context instanceof Application) {
            checkNotification = 0;
        } else if (checkNotification == 1) {
            mToast = new EToast(context, charSequence, i, i2);
        } else {
            mToast = BToast.getToast(context, charSequence, i, i2);
        }
    }

    public static void showText(Context context, int i) {
        new BamToast(context, context.getString(i), 0, 0).show();
    }

    public static void showText(Context context, CharSequence charSequence) {
        new BamToast(context, charSequence, 0, 0).show();
    }

    public static void showText(Context context, int i, boolean z) {
        new BamToast(context, context.getString(i), 0, z ? 1 : 2).show();
    }

    public static void showText(Context context, CharSequence charSequence, boolean z) {
        new BamToast(context, charSequence, 0, z ? 1 : 2).show();
    }

    public static void showText(Context context, int i, int i2) {
        new BamToast(context, context.getString(i), i2, 0).show();
    }

    public static void showText(Context context, CharSequence charSequence, int i) {
        new BamToast(context, charSequence, i, 0).show();
    }

    public static void showText(Context context, int i, int i2, boolean z) {
        new BamToast(context, context.getString(i), i2, z ? 1 : 2).show();
    }

    public static void showText(Context context, CharSequence charSequence, int i, boolean z) {
        new BamToast(context, charSequence, i, z ? 1 : 2).show();
    }

    public void show() {
        Object obj = mToast;
        if (obj instanceof EToast) {
            ((EToast) obj).show();
        } else if (obj instanceof Toast) {
            ((Toast) obj).show();
        }
    }

    public void cancel() {
        Object obj = mToast;
        if (obj instanceof EToast) {
            ((EToast) obj).cancel();
        } else if (obj instanceof Toast) {
            ((Toast) obj).cancel();
        }
    }
}
