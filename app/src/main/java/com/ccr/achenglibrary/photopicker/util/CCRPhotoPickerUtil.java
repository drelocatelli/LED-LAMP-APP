package com.ccr.achenglibrary.photopicker.util;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.Toast;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class CCRPhotoPickerUtil {
    public static final Application sApp;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    static {
        Application application;
        Application application2 = null;
        try {
            try {
                application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication", new Class[0]).invoke(null, new Object[0]);
                if (application == null) {
                    try {
                        throw new IllegalStateException("Static initialization of Applications must be on main thread.");
                    } catch (Exception e) {
                        e = e;
                        Log.e("CCRPhotoPickerUtil", "Failed to get current application from AppGlobals." + e.getMessage());
                        try {
                            application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication", new Class[0]).invoke(null, new Object[0]);
                        } catch (Exception unused) {
                            Log.e("CCRPhotoPickerUtil", "Failed to get current application from ActivityThread." + e.getMessage());
                        }
                        sApp = application;
                    }
                }
            } catch (Exception e2) {
                e = e2;
                application = null;
            } catch (Throwable th) {
                th = th;
                sApp = application2;
                throw th;
            }
            sApp = application;
        } catch (Throwable th2) {
            th = th2;
            application2 = application;
            sApp = application2;
            throw th;
        }
    }

    private CCRPhotoPickerUtil() {
    }

    public static void runInThread(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static void runInUIThread(Runnable runnable) {
        sHandler.post(runnable);
    }

    public static void runInUIThread(Runnable runnable, long j) {
        sHandler.postDelayed(runnable, j);
    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) sApp.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) sApp.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int dp2px(float f) {
        return (int) TypedValue.applyDimension(1, f, sApp.getResources().getDisplayMetrics());
    }

    public static String md5(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            throw new RuntimeException("请输入需要加密的字符串!");
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            boolean z = true;
            for (String str : strArr) {
                if (!TextUtils.isEmpty(str)) {
                    messageDigest.update(str.getBytes());
                    z = false;
                }
            }
            if (z) {
                throw new RuntimeException("请输入需要加密的字符串!");
            }
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void show(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        if (charSequence.length() < 10) {
            Toast.makeText(sApp, charSequence, 0).show();
        } else {
            Toast.makeText(sApp, charSequence, 1).show();
        }
    }

    public static void show(int i) {
        show(sApp.getString(i));
    }

    public static void showSafe(final CharSequence charSequence) {
        runInUIThread(new Runnable() { // from class: com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil.1
            @Override // java.lang.Runnable
            public void run() {
                CCRPhotoPickerUtil.show(charSequence);
            }
        });
    }

    public static void showSafe(int i) {
        showSafe(sApp.getString(i));
    }
}
