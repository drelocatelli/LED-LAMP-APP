package com.home.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String CRASH_REPORTER_EXTENSION = ".log";
    public static final String TAG = "CrashHandler";
    private static CrashHandler instance;
    private String CRASH_DIR_PATH;
    File crashDirFile;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Map<String, String> infos = new HashMap();
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            synchronized (CrashHandler.class) {
                if (instance == null) {
                    instance = new CrashHandler();
                }
            }
        }
        return instance;
    }

    private boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (isSdCardExist()) {
            this.CRASH_DIR_PATH = FileUtils.getStorageDerectory("/LedBle").getAbsolutePath();
        } else {
            this.CRASH_DIR_PATH = this.mContext.getFilesDir().getAbsolutePath();
        }
        File file = new File(this.CRASH_DIR_PATH);
        this.crashDirFile = file;
        if (file.exists() && this.crashDirFile.isDirectory()) {
            return;
        }
        this.crashDirFile.mkdirs();
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
        if (!handleException(th) && (uncaughtExceptionHandler = this.mDefaultHandler) != null) {
            uncaughtExceptionHandler.uncaughtException(thread, th);
            return;
        }
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            Log.e(TAG, "error : ", e);
        }
        exit();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.home.utils.CrashHandler$1] */
    private boolean handleException(Throwable th) {
        if (th == null) {
            return false;
        }
        collectDeviceInfo(this.mContext);
        new Thread() { // from class: com.home.utils.CrashHandler.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Looper.prepare();
                Toast.makeText(CrashHandler.this.mContext, "sorry app is crash!", 0).show();
                Looper.loop();
            }
        }.start();
        saveCatchInfo2File(th);
        return true;
    }

    private void collectDeviceInfo(Context context) {
        Field[] declaredFields;
        try {
            Log.d(TAG, "-----------------------collectDeviceInfo-----------------------");
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
            if (packageInfo != null) {
                this.infos.put("versionName", packageInfo.versionName == null ? "null" : packageInfo.versionName);
                this.infos.put("versionCode", packageInfo.versionCode + "");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "collectDeviceInfo Exception:an error occured when collect package info", e);
        }
        for (Field field : Build.class.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                this.infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e2) {
                Log.e(TAG, "collectDeviceInfo Exception:an error occured when collect crash info", e2);
            }
        }
    }

    private String saveCatchInfo2File(Throwable th) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : this.infos.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        printWriter.close();
        stringBuffer.append(stringWriter.toString());
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String str = "crash-" + this.formatter.format(new Date()) + "-" + currentTimeMillis + CRASH_REPORTER_EXTENSION;
            if (Environment.getExternalStorageState().equals("mounted")) {
                File file = new File(this.CRASH_DIR_PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(this.CRASH_DIR_PATH + "/" + str);
                fileOutputStream.write(stringBuffer.toString().getBytes());
                fileOutputStream.close();
                Log.d(TAG, "-----------------------saveCatchInfo2File ok-----------------------");
                Log.d(TAG, "fileName:" + this.CRASH_DIR_PATH + str);
                StringBuilder sb = new StringBuilder();
                sb.append(this.CRASH_DIR_PATH);
                sb.append(str);
                sendCrashLog2PM(sb.toString());
            }
            return str;
        } catch (Exception e) {
            Log.e(TAG, "saveCatchInfo2File Exception:an error occured while writing file...", e);
            return null;
        }
    }

    private void sendCrashLog2PM(String str) {
        FileInputStream fileInputStream;
        String readLine;
        if (!new File(str).exists()) {
            return;
        }
        Log.d(TAG, "-----------------------send email-----------------------");
        Log.d(TAG, "-----------------------show CrashLog -----------------------");
        BufferedReader bufferedReader = null;
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(str);
                    try {
                        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(fileInputStream, "GBK"));
                        while (true) {
                            try {
                                readLine = bufferedReader2.readLine();
                                if (readLine == null) {
                                    break;
                                }
                                Log.d(TAG, readLine.toString());
                            } catch (FileNotFoundException e) {
                                e = e;
                                bufferedReader = bufferedReader2;
                                e.printStackTrace();
                                bufferedReader.close();
                                fileInputStream.close();
                                bufferedReader = bufferedReader;
                            } catch (IOException e2) {
                                e = e2;
                                bufferedReader = bufferedReader2;
                                e.printStackTrace();
                                bufferedReader.close();
                                fileInputStream.close();
                                bufferedReader = bufferedReader;
                            } catch (Throwable th) {
                                th = th;
                                bufferedReader = bufferedReader2;
                                try {
                                    bufferedReader.close();
                                    fileInputStream.close();
                                } catch (IOException e3) {
                                    e3.printStackTrace();
                                }
                                throw th;
                            }
                        }
                        bufferedReader2.close();
                        fileInputStream.close();
                        bufferedReader = readLine;
                    } catch (FileNotFoundException e4) {
                        e = e4;
                    } catch (IOException e5) {
                        e = e5;
                    }
                } catch (FileNotFoundException e6) {
                    e = e6;
                    fileInputStream = null;
                } catch (IOException e7) {
                    e = e7;
                    fileInputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream = null;
                }
            } catch (IOException e8) {
                e8.printStackTrace();
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    private void exit() {
        Process.killProcess(Process.myPid());
        ((ActivityManager) this.mContext.getSystemService("activity")).restartPackage(this.mContext.getPackageName());
        System.exit(0);
        System.gc();
    }
}
