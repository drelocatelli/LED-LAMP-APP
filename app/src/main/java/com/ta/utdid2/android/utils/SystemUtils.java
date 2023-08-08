package com.ta.utdid2.android.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/* loaded from: classes.dex */
public class SystemUtils {
    public static String getCpuInfo() {
        String str = null;
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            try {
                BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
                str = bufferedReader.readLine();
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                Log.e("Could not read from file /proc/cpuinfo", e.toString());
            }
        } catch (FileNotFoundException e2) {
            Log.e("BaseParameter-Could not open file /proc/cpuinfo", e2.toString());
        }
        return str != null ? str.substring(str.indexOf(58) + 1).trim() : "";
    }

    public static int getSystemVersion() {
        try {
            try {
                return Build.VERSION.class.getField("SDK_INT").getInt(null);
            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }
        } catch (Exception unused) {
            return Integer.parseInt((String) Build.VERSION.class.getField("SDK").get(null));
        }
    }

    public static File getRootFolder(String str) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory != null) {
            File file = new File(String.format("%s%s%s", externalStorageDirectory.getAbsolutePath(), File.separator, str));
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        return null;
    }

    public static String getAppLabel(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            if (packageManager == null || packageName == null) {
                return null;
            }
            return packageManager.getApplicationLabel(packageManager.getPackageInfo(packageName, 1).applicationInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
