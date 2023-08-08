package top.defaults.logger;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import androidx.exifinterface.media.ExifInterface;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/* loaded from: classes2.dex */
public final class Logger {
    private static final String DEFAULT_TAG = "TopDefaultsLogger";
    private static SparseArray<String> PRIORITY_MAP = null;
    private static ExecutorService executorService = null;
    private static int level = 2;
    private static String logFilePath = null;
    private static int logFileSizeInMegabytes = 2;
    private static BufferedWriter logWriter = null;
    private static final String prevLogFileSuffix = "-prev";
    private static TimerTask scheduledCloseTask = null;
    private static TimerTask scheduledFlushTask = null;
    private static String tagPrefix = "TopDefaultsLogger";
    private static Timer timer;

    public static void setLevel(int i) {
        level = i;
    }

    public static int getLevel() {
        return level;
    }

    public static void setTagPrefix(String str) {
        tagPrefix = str;
    }

    public static void setLogFile(String str) {
        logFilePath = str;
        if (str == null) {
            executorService.shutdown();
            timer.cancel();
            return;
        }
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        if (timer == null) {
            timer = new Timer();
        }
    }

    public static void setLogFileMaxSizeInMegabytes(int i) {
        logFileSizeInMegabytes = i;
    }

    public static void v(String str, Object... objArr) {
        vWithTag(realTag(), str, objArr);
    }

    public static void d(String str, Object... objArr) {
        dWithTag(realTag(), str, objArr);
    }

    public static void i(String str, Object... objArr) {
        iWithTag(realTag(), str, objArr);
    }

    public static void w(String str, Object... objArr) {
        wWithTag(realTag(), str, objArr);
    }

    public static void e(String str, Object... objArr) {
        eWithTag(realTag(), str, objArr);
    }

    public static void wtf(String str, Object... objArr) {
        wtfWithTag(realTag(), str, objArr);
    }

    public static void vWithTag(String str, String str2, Object... objArr) {
        log(2, str, str2, objArr);
    }

    public static void dWithTag(String str, String str2, Object... objArr) {
        log(3, str, str2, objArr);
    }

    public static void iWithTag(String str, String str2, Object... objArr) {
        log(4, str, str2, objArr);
    }

    public static void wWithTag(String str, String str2, Object... objArr) {
        log(5, str, str2, objArr);
    }

    public static void eWithTag(String str, String str2, Object... objArr) {
        log(6, str, str2, objArr);
    }

    public static void wtfWithTag(String str, String str2, Object... objArr) {
        log(7, str, str2, objArr);
    }

    public static void logThreadStart() {
        String realTag = realTag();
        dWithTag(realTag, ">>>>>>>> " + Thread.currentThread().getClass() + " start running >>>>>>>>", new Object[0]);
    }

    public static void logThreadFinish() {
        String realTag = realTag();
        dWithTag(realTag, "<<<<<<<< " + Thread.currentThread().getClass() + " finished running <<<<<<<<", new Object[0]);
    }

    private static void log(int i, String str, String str2, Object... objArr) {
        if (level <= i || Log.isLoggable(tagPrefix, 3)) {
            String formatMessage = formatMessage(str2, objArr);
            if (i == 7) {
                Log.wtf(str, formatMessage);
            } else {
                Log.println(i, str, formatMessage);
            }
            try {
                writeLogFile(priorityAbbr(i) + "/" + str + "\t" + formatMessage);
            } catch (Exception unused) {
            }
        }
    }

    static {
        SparseArray<String> sparseArray = new SparseArray<>();
        PRIORITY_MAP = sparseArray;
        sparseArray.append(2, ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
        PRIORITY_MAP.append(3, "D");
        PRIORITY_MAP.append(4, "I");
        PRIORITY_MAP.append(5, ExifInterface.LONGITUDE_WEST);
        PRIORITY_MAP.append(6, ExifInterface.LONGITUDE_EAST);
        PRIORITY_MAP.append(7, "X");
    }

    private static String priorityAbbr(int i) {
        return PRIORITY_MAP.get(i);
    }

    public static void logWithTimber(int i, String str, String str2) {
        String realTimberTag = realTimberTag(str);
        switch (i) {
            case 2:
                vWithTag(realTimberTag, str2, new Object[0]);
                return;
            case 3:
                dWithTag(realTimberTag, str2, new Object[0]);
                return;
            case 4:
                iWithTag(realTimberTag, str2, new Object[0]);
                return;
            case 5:
                wWithTag(realTimberTag, str2, new Object[0]);
                return;
            case 6:
                eWithTag(realTimberTag, str2, new Object[0]);
                return;
            case 7:
                wtfWithTag(realTimberTag, str2, new Object[0]);
                return;
            default:
                return;
        }
    }

    private static String formatMessage(String str, Object[] objArr) {
        return (objArr == null || objArr.length <= 0) ? str : String.format(str, objArr);
    }

    private static void writeLogFile(String str) {
        if (logFilePath != null) {
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(new Date());
            executorService.submit(new LogWriterRunnable(format + " " + str));
            TimerTask timerTask = scheduledFlushTask;
            if (timerTask != null) {
                timerTask.cancel();
            }
            TimerTask timerTask2 = new TimerTask() { // from class: top.defaults.logger.Logger.1
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    Logger.executorService.submit(new FutureTask(new Callable<Void>() { // from class: top.defaults.logger.Logger.1.1
                        @Override // java.util.concurrent.Callable
                        public Void call() throws Exception {
                            Logger.logWriter.flush();
                            return null;
                        }
                    }));
                }
            };
            scheduledFlushTask = timerTask2;
            timer.schedule(timerTask2, 1000L);
            TimerTask timerTask3 = scheduledCloseTask;
            if (timerTask3 != null) {
                timerTask3.cancel();
            }
            TimerTask timerTask4 = new TimerTask() { // from class: top.defaults.logger.Logger.2
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    Logger.executorService.submit(new FutureTask(new Callable<Void>() { // from class: top.defaults.logger.Logger.2.1
                        @Override // java.util.concurrent.Callable
                        public Void call() throws Exception {
                            Logger.logWriter.close();
                            BufferedWriter unused = Logger.logWriter = null;
                            return null;
                        }
                    }));
                }
            };
            scheduledCloseTask = timerTask4;
            timer.schedule(timerTask4, 60000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class LogWriterRunnable implements Runnable {
        private final String line;

        LogWriterRunnable(String str) {
            this.line = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (Logger.logWriter == null) {
                    BufferedWriter unused = Logger.logWriter = new BufferedWriter(new FileWriter(Logger.logFilePath, true));
                }
                Logger.logWriter.append((CharSequence) this.line);
                Logger.logWriter.newLine();
            } catch (IOException unused2) {
            }
            File file = new File(Logger.logFilePath);
            if (file.length() >= Logger.logFileSizeInMegabytes * 1024 * 1024) {
                file.renameTo(new File(Logger.logFilePath + Logger.prevLogFileSuffix));
                try {
                    Logger.logWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedWriter unused3 = Logger.logWriter = null;
            }
        }
    }

    private static String realTag() {
        return tagPrefix + "|" + getLineInfo();
    }

    private static String realTimberTag(String str) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(str)) {
            str = tagPrefix;
        }
        sb.append(str);
        sb.append("|");
        sb.append(getLineInfoBypassTimber());
        return sb.toString();
    }

    private static String getLineInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String fileName = stackTrace[5].getFileName();
        int lineNumber = stackTrace[5].getLineNumber();
        return ".(" + fileName + ":" + lineNumber + ")";
    }

    private static String getLineInfoBypassTimber() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int stackOffsetBypassTimber = getStackOffsetBypassTimber(stackTrace);
        if (stackOffsetBypassTimber < 0) {
            return "";
        }
        String fileName = stackTrace[stackOffsetBypassTimber].getFileName();
        int lineNumber = stackTrace[stackOffsetBypassTimber].getLineNumber();
        return ".(" + fileName + ":" + lineNumber + ")";
    }

    private static int getStackOffsetBypassTimber(StackTraceElement[] stackTraceElementArr) {
        for (int i = 6; i < stackTraceElementArr.length; i++) {
            if (!stackTraceElementArr[i].getClassName().startsWith("timber.log.Timber")) {
                return i;
            }
        }
        return -1;
    }
}
