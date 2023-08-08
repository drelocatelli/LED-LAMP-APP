package com.home.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;
import com.common.uitl.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class Utils {
    protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
    public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
    public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
    public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
    protected static final String EXTRA_ACCESS_TOKEN = "access_token";
    public static final String EXTRA_MESSAGE = "message";
    public static final String RESPONSE_CONTENT = "content";
    public static final String RESPONSE_ERRCODE = "errcode";
    public static final String RESPONSE_METHOD = "method";
    public static int SCREEN_WIDTH = 0;
    public static final String TAG = "PushDemoActivity";
    public static float density = 1.0f;
    public static String logStringCache = "";
    private static Utils mSingleton;
    private static Toast toast;

    public static Utils getInstance() {
        if (mSingleton == null) {
            synchronized (Utils.class) {
                if (mSingleton == null) {
                    mSingleton = new Utils();
                }
            }
        }
        return mSingleton;
    }

    public static String getMetaValue(Context context, String str) {
        if (context == null || str == null) {
            return null;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            Bundle bundle = applicationInfo != null ? applicationInfo.metaData : null;
            if (bundle != null) {
                return bundle.getString(str);
            }
            return null;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "error " + e.getMessage());
            return null;
        }
    }

    public static int dpToPx(float f, Resources resources) {
        return (int) TypedValue.applyDimension(1, f, resources.getDisplayMetrics());
    }

    public static int dip2px(float f) {
        return (int) ((f * density) + 0.5f);
    }

    public static List<String> getTagsList(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        int indexOf = str.indexOf(44);
        while (indexOf != -1) {
            arrayList.add(str.substring(0, indexOf));
            str = str.substring(indexOf + 1);
            indexOf = str.indexOf(44);
        }
        arrayList.add(str);
        return arrayList;
    }

    public static String getLogText(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("log_text", "");
    }

    public static void setLogText(Context context, String str) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putString("log_text", str);
        edit.commit();
    }

    public static boolean isAppAlive(Context context, String str) {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        Log.d(TAG, "packageName:" + str);
        for (int i = 0; i < runningAppProcesses.size(); i++) {
            if (runningAppProcesses.get(i).processName.equals(str)) {
                Log.d(TAG, String.format("the %s is running, isAppAlive return true", str));
                return true;
            }
        }
        Log.d(TAG, String.format("the %s is not running, isAppAlive return false", str));
        return false;
    }

    public static boolean isEmail(String str) {
        return (str == null || "".equals(str) || !Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$").matcher(str).matches()) ? false : true;
    }

    public static boolean isMobileNo(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        return Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$").matcher(str).matches();
    }

    public static void versionIdentificationTips(Context context, String str) {
        ArrayList<String> deviceNameCompairResult = StringUtils.getDeviceNameCompairResult(str);
        if (deviceNameCompairResult == null || deviceNameCompairResult.size() <= 0) {
            return;
        }
        Iterator<String> it = deviceNameCompairResult.iterator();
        while (it.hasNext()) {
            it.next();
            if (context != null) {
                context.getResources();
            }
        }
    }

    public static void shouToast(Context context, String str) {
        Toast toast2 = toast;
        if (toast2 == null) {
            toast = Toast.makeText(context, str, 0);
        } else {
            toast2.setText(str);
        }
        toast.show();
    }
}
