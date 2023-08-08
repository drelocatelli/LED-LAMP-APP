package com.common.uitl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

/* loaded from: classes.dex */
public class GlobalTool {
    public static void startOtherActivity(Activity activity, Class cls) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
    }

    public static void startOtherActivity(Activity activity, Class cls, Serializable serializable) {
        Intent intent = new Intent();
        intent.putExtra("data", serializable);
        intent.setClass(activity, cls);
        activity.startActivity(intent);
    }

    public static Point getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Point point = new Point();
        point.x = displayMetrics.widthPixels;
        point.y = displayMetrics.heightPixels;
        return point;
    }

    public static Point getRandomPoint(Point point) {
        Random random = new Random();
        return new Point(random.nextInt(point.x), random.nextInt(point.y));
    }

    public static Point getDimensionsByDimens(int i, int i2) {
        Random random = new Random();
        return new Point(random.nextInt(i), random.nextInt(i2));
    }

    public static AlertDialog.Builder createADialig(Context context, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        return builder;
    }

    public static void sendBoardCast(Context context, Intent intent) {
        context.sendBroadcast(intent);
    }

    public static void startUrl(Context context, String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        context.startActivity(intent);
    }

    public static int getAppRect(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return activity.getWindow().findViewById(16908290).getTop() - rect.top;
    }

    public static int geteAppUnVisibleHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int i = rect.top;
        return i + (activity.getWindow().findViewById(16908290).getTop() - i);
    }

    public static void mkdir(String str) {
        new File(str).mkdir();
    }

    public static void copyAssetFile2Sdcard(Context context, String str, String str2) throws IOException {
        AssetManager assets = context.getAssets();
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdir();
            return;
        }
        InputStream open = assets.open(str);
        byte[] bArr = new byte[256];
        FileOutputStream fileOutputStream = new FileOutputStream(str2 + str);
        while (open.read(bArr) != -1) {
            fileOutputStream.write(bArr, 0, 256);
        }
        fileOutputStream.flush();
        open.close();
        fileOutputStream.close();
    }

    public static void delay(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getLogPrffix() {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length > 1) {
            String className = stackTrace[1].getClassName();
            String substring = className.substring(className.lastIndexOf(".") + 1);
            String methodName = stackTrace[1].getMethodName();
            return new String("[(" + substring + ":" + methodName + ") line:" + stackTrace[1].getLineNumber() + " ]");
        }
        return null;
    }

    public static LayoutInflater getInflater(Context context) {
        return LayoutInflater.from(context);
    }
}
