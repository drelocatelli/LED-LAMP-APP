package com.ccr.achenglibrary.photopicker.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import com.ccr.achenglibrary.R;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class MyUtilHelper {
    public static void showBottomUIMenu(Context context) {
        ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(0);
    }

    public static void hideBottomUIMenu(Context context) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(8);
        } else if (Build.VERSION.SDK_INT >= 19) {
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(4102);
        }
    }

    public static void showAnimation(int i, boolean z, View view, Context context) {
        if (i == 1) {
            if (z) {
                if (view.getVisibility() == 8) {
                    view.setVisibility(0);
                    view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom));
                }
            } else if (view.getVisibility() == 0) {
                view.setVisibility(8);
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom));
            }
        } else if (z) {
            if (view.getVisibility() == 8) {
                view.setVisibility(0);
                view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_left_in));
            }
        } else if (view.getVisibility() == 0) {
            view.setVisibility(8);
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_right_out));
        }
    }

    public static void releaseInputMethodManagerFocus(Activity activity) {
        if (activity == null) {
            return;
        }
        int i = 0;
        while (true) {
            i++;
            if (i == 5) {
                return;
            }
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService("input_method");
                if (inputMethodManager == null) {
                    continue;
                } else {
                    Method method = InputMethodManager.class.getMethod("windowDismissed", IBinder.class);
                    if (method != null) {
                        method.invoke(inputMethodManager, activity.getWindow().getDecorView().getWindowToken());
                    }
                    Field declaredField = InputMethodManager.class.getDeclaredField("mLastSrvView");
                    if (declaredField != null) {
                        declaredField.setAccessible(true);
                        declaredField.set(inputMethodManager, null);
                        return;
                    }
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
