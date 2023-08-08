package com.luck.picture.lib.tools;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.yalantis.ucrop.util.FileUtils;
import java.io.File;
import java.io.FileInputStream;

/* loaded from: classes.dex */
public class AndroidQTransformUtils {
    public static String parseVideoPathToAndroidQ(Context context, String str, String str2, String str3) {
        try {
            String lastImgSuffix = PictureMimeType.getLastImgSuffix(str3);
            File externalFilesDir = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            if (externalFilesDir != null) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(externalFilesDir);
                stringBuffer.append(File.separator);
                boolean isEmpty = TextUtils.isEmpty(str2);
                Object obj = str2;
                if (isEmpty) {
                    obj = Long.valueOf(System.currentTimeMillis());
                }
                stringBuffer.append(obj);
                stringBuffer.append(lastImgSuffix);
                String stringBuffer2 = stringBuffer.toString();
                return FileUtils.copyFile(new FileInputStream(context.getContentResolver().openFileDescriptor(Uri.parse(str), "r").getFileDescriptor()), stringBuffer2) ? stringBuffer2 : "";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseImagePathToAndroidQ(Context context, String str, String str2, String str3) {
        try {
            String lastImgSuffix = PictureMimeType.getLastImgSuffix(str3);
            String diskCacheDir = PictureFileUtils.getDiskCacheDir(context.getApplicationContext());
            if (diskCacheDir != null) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(diskCacheDir);
                stringBuffer.append(File.separator);
                boolean isEmpty = TextUtils.isEmpty(str2);
                Object obj = str2;
                if (isEmpty) {
                    obj = Long.valueOf(System.currentTimeMillis());
                }
                stringBuffer.append(obj);
                stringBuffer.append(lastImgSuffix);
                String stringBuffer2 = stringBuffer.toString();
                return FileUtils.copyFile(new FileInputStream(context.getContentResolver().openFileDescriptor(Uri.parse(str), "r").getFileDescriptor()), stringBuffer2) ? stringBuffer2 : "";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseAudioPathToAndroidQ(Context context, String str, String str2, String str3) {
        try {
            String lastImgSuffix = PictureMimeType.getLastImgSuffix(str3);
            File externalFilesDir = context.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            if (externalFilesDir != null) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(externalFilesDir);
                stringBuffer.append(File.separator);
                boolean isEmpty = TextUtils.isEmpty(str2);
                Object obj = str2;
                if (isEmpty) {
                    obj = Long.valueOf(System.currentTimeMillis());
                }
                stringBuffer.append(obj);
                stringBuffer.append(lastImgSuffix);
                String stringBuffer2 = stringBuffer.toString();
                return FileUtils.copyFile(new FileInputStream(context.getApplicationContext().getContentResolver().openFileDescriptor(Uri.parse(str), "r").getFileDescriptor()), stringBuffer2) ? stringBuffer2 : "";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
