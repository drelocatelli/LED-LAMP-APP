package com.common.uitl;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

/* loaded from: classes.dex */
public class SharePersistent {
    public static final String PREFS_NAME = "skyworth";

    public static void setObjectValue(Context context, String str, Object obj) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences("skyworth", 0).edit();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);
            edit.putString(str, new String(Base64Coder.encode(byteArrayOutputStream.toByteArray())));
            edit.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized Object getObjectValue(Context context, String str) {
        ObjectInputStream objectInputStream;
        synchronized (SharePersistent.class) {
            String string = context.getSharedPreferences("skyworth", 0).getString(str, null);
            if (string == null) {
                return null;
            }
            try {
                try {
                    try {
                        try {
                            objectInputStream = new ObjectInputStream(new ByteArrayInputStream(Base64Coder.decode(string)));
                        } catch (IOException e) {
                            e.printStackTrace();
                            objectInputStream = null;
                            return objectInputStream.readObject();
                        }
                    } catch (StreamCorruptedException e2) {
                        e2.printStackTrace();
                        objectInputStream = null;
                        return objectInputStream.readObject();
                    }
                    return objectInputStream.readObject();
                } catch (OptionalDataException e3) {
                    e3.printStackTrace();
                    return null;
                } catch (ClassNotFoundException e4) {
                    e4.printStackTrace();
                    return null;
                }
            } catch (IOException e5) {
                e5.printStackTrace();
                return null;
            }
        }
    }

    public static void savePerference(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("skyworth", 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static void savePerference(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences("skyworth", 0).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static void saveBoolean(Context context, String str, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences("skyworth", 0).edit();
        edit.putBoolean(str, z);
        edit.commit();
    }

    public static void saveInt(Context context, String str, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences("skyworth", 0).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static String getPerference(Context context, String str) {
        return context.getSharedPreferences("skyworth", 0).getString(str, "");
    }

    public static int getInt(Context context, String str) {
        return context.getSharedPreferences("skyworth", 0).getInt(str, 0);
    }

    public static String getSmartModeString(Context context, String str) {
        return context.getSharedPreferences("skyworth", 0).getString(str, "RGBW");
    }

    public static int getSmartModeInt(Context context, String str) {
        return context.getSharedPreferences("skyworth", 0).getInt(str, 3);
    }

    public static boolean getBoolean(Context context, String str) {
        return context.getSharedPreferences("skyworth", 0).getBoolean(str, false);
    }

    public static boolean getBoolean(Context context, String str, boolean z) {
        return context.getSharedPreferences("skyworth", 0).getBoolean(str, z);
    }

    public static void saveBrightData(Context context, String str, String str2, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putInt(str2, i);
        edit.commit();
    }

    public static int getBrightData(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getInt(str2, 0);
    }

    public static void saveTimerData(Context context, String str, int i, int i2, int i3, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("skyworth", 0).edit();
        edit.putString(str + "hour", String.valueOf(i));
        edit.putString(str + "minute", String.valueOf(i2));
        edit.putString(str + "model", String.valueOf(i3));
        edit.putString(str + "modelText", str2);
        edit.commit();
    }

    public static String[] getTimerData(Context context, String str) {
        String[] strArr = {str + "hour", str + "minute", str + "model", str + "modelText"};
        String[] strArr2 = new String[4];
        for (int i = 0; i < 4; i++) {
            strArr2[i] = context.getSharedPreferences("skyworth", 0).getString(strArr[i], "-1");
        }
        return strArr2;
    }

    public static int[] getWiFiTimerData(Context context, String str) {
        String[] strArr = {str + "monValue", str + "tueValue", str + "wedValue", str + "thuValue", str + "friValue", str + "satValue", str + "sunValue"};
        int[] iArr = new int[7];
        for (int i = 0; i < 7; i++) {
            iArr[i] = context.getSharedPreferences("skyworth", 0).getInt(strArr[i], 0);
        }
        return iArr;
    }

    public static void saveConfigData(Context context, String str, String str2, String str3, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putString(str2, str3 + "," + i);
        edit.commit();
    }

    public static String getConfigData(Context context, String str, String str2) {
        return context.getSharedPreferences(str, 0).getString(str2, "");
    }

    public static void saveWeekData(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("skyworth", 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static String getWeekData(Context context, String str) {
        return context.getSharedPreferences("skyworth", 0).getString(str, "null");
    }

    public static void saveTimerData(Context context, String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
        edit.putInt(str2 + "hour", i);
        edit.putInt(str2 + "minute", i2);
        edit.putInt(str2 + "redValue", i3);
        edit.putInt(str2 + "greenValue", i4);
        edit.putInt(str2 + "lightblueValue", i5);
        edit.putInt(str2 + "whiteValue", i6);
        edit.putInt(str2 + "crystalValue", i7);
        edit.putInt(str2 + "pinkValue", i8);
        edit.commit();
    }

    public static int[] getTimerData(Context context, String str, String str2) {
        String[] strArr = {str2 + "hour", str2 + "minute", str2 + "redValue", str2 + "greenValue", str2 + "lightblueValue", str2 + "whiteValue", str2 + "crystalValue", str2 + "pinkValue"};
        int[] iArr = new int[8];
        for (int i = 0; i < 8; i++) {
            iArr[i] = context.getSharedPreferences(str, 0).getInt(strArr[i], 0);
        }
        return iArr;
    }
}
