package com.common.uitl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;
import com.common.net.NetResult;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/* loaded from: classes.dex */
public class Tool {
    public static void startActivityForResult(Activity activity, Class cls, int i) {
        activity.startActivityForResult(new Intent(activity, cls), i);
    }

    public static void showMessageDialog(String str, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.common.uitl.Tool.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public static void startWifi(Context context) {
        context.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
    }

    public static int[] getRGB(int i) {
        new Color();
        return new int[]{Color.red(i), Color.green(i), Color.blue(i), Color.alpha(i)};
    }

    public static byte[] int2bytearray(int i) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(i);
        return byteArrayOutputStream.toByteArray();
    }

    public static void saveImg2Abulm(Activity activity, Bitmap bitmap) {
        MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "myPhoto", "this is a Photo");
        activity.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    public static String getTextFromClip(Context context) {
        return ((ClipboardManager) context.getSystemService("clipboard")).getText().toString();
    }

    public static void copyClip(String str, Context context) {
        ((ClipboardManager) context.getSystemService("clipboard")).setText(str);
    }

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean isImgUrl(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return lowerCase.endsWith(PictureMimeType.PNG) || lowerCase.endsWith(".jpg") || lowerCase.endsWith(".bmp") || lowerCase.endsWith(PictureMimeType.JPEG) || lowerCase.endsWith(".gif");
    }

    public static boolean checkIdCard(String str) {
        return new IdcardValidator().isValidatedAllIdcard(str);
    }

    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static void exitApp() {
        Process.killProcess(Process.myPid());
    }

    public static void startActivity(Context context, Class cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public static void installApk(Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(str)), "application/vnd.android.package-archive");
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    public static String[] getAppApkInfo(Context context, String str) {
        PackageInfo packageArchiveInfo = context.getPackageManager().getPackageArchiveInfo(str, 1);
        if (packageArchiveInfo != null) {
            return new String[]{packageArchiveInfo.applicationInfo.packageName, packageArchiveInfo.versionName};
        }
        return null;
    }

    public static boolean fileCopy(InputStream inputStream, File file) {
        if (file == null) {
            return false;
        }
        try {
            file.delete();
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[512];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    inputStream.close();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("skyworth", "copy file error:" + e.getMessage());
            return false;
        }
    }

    public static byte[] file2Data(String str) {
        try {
            File file = new File(str);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (file.exists()) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] bArr = new byte[512];
                while (bufferedInputStream.read(bArr) != -1) {
                    byteArrayOutputStream.write(bArr, 0, 512);
                }
                byteArrayOutputStream.flush();
                bufferedInputStream.close();
                return byteArrayOutputStream.toByteArray();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public int DipToPixels(Context context, int i) {
        return (int) ((i * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public float PixelsToDip(Context context, int i) {
        return i / context.getResources().getDisplayMetrics().density;
    }

    public static String getChannel(Context context) {
        return AssertTool.readLinesFromAssertsFiles(context, "channel.txt").get(0).trim();
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    public static void startOtherActivity(Activity activity, Class cls) {
        Intent intent = new Intent();
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

    public static void ToastShow(Activity activity, int i) {
        if (activity.isFinishing()) {
            return;
        }
        Toast.makeText(activity, activity.getResources().getString(i), 0).show();
    }

    public static void ToastShow(Activity activity, String str) {
        if (activity.isFinishing()) {
            return;
        }
        Toast.makeText(activity, str, 0).show();
    }

    public static void showWifiStrenth(Activity activity) {
        int rssi = ((WifiManager) activity.getSystemService("wifi")).getConnectionInfo().getRssi();
        if (rssi >= 0 || rssi < -50) {
            ToastShow(activity, "亲，你的wifi信号较差,使用起来不够流畅！");
        }
    }

    public static String getImei(Context context) {
        String str;
        try {
            str = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Exception unused) {
            str = null;
        }
        if (StringUtils.isEmpty(str) || NetResult.CODE_OK.equals(str)) {
            String macAddress = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            if (StringUtils.isEmpty(macAddress) || NetResult.CODE_OK.equals(macAddress)) {
                String perference = SharePersistent.getPerference(context, Constant.STRING_IMEI);
                if (StringUtils.isEmpty(perference)) {
                    perference = UUIDGenerator.getUUID(15);
                    if (StringUtils.isEmpty(perference)) {
                        return NetResult.CODE_OK;
                    }
                    SharePersistent.savePerference(context, Constant.STRING_IMEI, perference);
                }
                return perference;
            }
            return macAddress;
        }
        return str;
    }

    public static boolean compareAppVersionName(String str, String str2) {
        try {
            String[] split = str.split("\\.");
            String[] split2 = str2.split("\\.");
            int length = split.length;
            for (int i = 0; i < length; i++) {
                if (Integer.parseInt(split2[i].trim()) > Integer.parseInt(split[i].trim())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void delay(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
