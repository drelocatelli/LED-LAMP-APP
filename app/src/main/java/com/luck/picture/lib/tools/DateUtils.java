package com.luck.picture.lib.tools;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class DateUtils {
    private static SimpleDateFormat msFormat = new SimpleDateFormat("mm:ss");

    public static int dateDiffer(long j) {
        try {
            return (int) Math.abs(Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10)) - j);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String formatDurationTime(long j) {
        return String.format(Locale.getDefault(), "%02d:%02d", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j))));
    }

    public static String cdTime(long j, long j2) {
        long j3 = j2 - j;
        if (j3 > 1000) {
            return (j3 / 1000) + "秒";
        }
        return j3 + "毫秒";
    }
}
