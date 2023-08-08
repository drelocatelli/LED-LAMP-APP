package cn.jzvd;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class JZUtils {
    public static final String TAG = "JiaoZiVideoPlayer";

    public static String stringForTime(long j) {
        if (j <= 0 || j >= 86400000) {
            return "00:00";
        }
        long j2 = j / 1000;
        int i = (int) (j2 % 60);
        int i2 = (int) ((j2 / 60) % 60);
        int i3 = (int) (j2 / 3600);
        Formatter formatter = new Formatter(new StringBuilder(), Locale.getDefault());
        return i3 > 0 ? formatter.format("%d:%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i2), Integer.valueOf(i)).toString() : formatter.format("%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i)).toString();
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }

    public static Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        }
        if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    public static void setRequestedOrientation(Context context, int i) {
        if (getAppCompActivity(context) != null) {
            getAppCompActivity(context).setRequestedOrientation(i);
        } else {
            scanForActivity(context).setRequestedOrientation(i);
        }
    }

    public static Window getWindow(Context context) {
        if (getAppCompActivity(context) != null) {
            return getAppCompActivity(context).getWindow();
        }
        return scanForActivity(context).getWindow();
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static void saveProgress(Context context, Object obj, long j) {
        if (JZVideoPlayer.SAVE_PROGRESS) {
            Log.i("JiaoZiVideoPlayer", "saveProgress: " + j);
            if (j < 5000) {
                j = 0;
            }
            SharedPreferences.Editor edit = context.getSharedPreferences("JZVD_PROGRESS", 0).edit();
            edit.putLong("newVersion:" + obj.toString(), j).apply();
        }
    }

    public static long getSavedProgress(Context context, Object obj) {
        if (JZVideoPlayer.SAVE_PROGRESS) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("JZVD_PROGRESS", 0);
            return sharedPreferences.getLong("newVersion:" + obj.toString(), 0L);
        }
        return 0L;
    }

    public static void clearSavedProgress(Context context, Object obj) {
        if (obj == null) {
            context.getSharedPreferences("JZVD_PROGRESS", 0).edit().clear().apply();
            return;
        }
        SharedPreferences.Editor edit = context.getSharedPreferences("JZVD_PROGRESS", 0).edit();
        edit.putLong("newVersion:" + obj.toString(), 0L).apply();
    }

    public static Object getCurrentFromDataSource(Object[] objArr, int i) {
        LinkedHashMap linkedHashMap = (LinkedHashMap) objArr[0];
        if (linkedHashMap == null || linkedHashMap.size() <= 0) {
            return null;
        }
        return getValueFromLinkedMap(linkedHashMap, i);
    }

    public static Object getValueFromLinkedMap(LinkedHashMap<String, Object> linkedHashMap, int i) {
        int i2 = 0;
        for (String str : linkedHashMap.keySet()) {
            if (i2 == i) {
                return linkedHashMap.get(str);
            }
            i2++;
        }
        return null;
    }

    public static boolean dataSourceObjectsContainsUri(Object[] objArr, Object obj) {
        LinkedHashMap linkedHashMap = (LinkedHashMap) objArr[0];
        if (linkedHashMap == null || obj == null) {
            return false;
        }
        return linkedHashMap.containsValue(obj);
    }

    public static String getKeyFromDataSource(Object[] objArr, int i) {
        int i2 = 0;
        for (String str : ((LinkedHashMap) objArr[0]).keySet()) {
            if (i2 == i) {
                return str;
            }
            i2++;
        }
        return null;
    }
}
