package com.home.utils;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.File;
import java.io.IOException;
import org.apache.http.protocol.HTTP;

/* loaded from: classes.dex */
public class FileUtils {
    public static void deleteFilesByDirectory(File file) {
        if (isHasStorage() && file != null && file.exists() && file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                file2.delete();
            }
        }
    }

    public static boolean deleteFile(String str) {
        File file = new File(str);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    public static boolean deleteDirectory(String str) {
        if (!str.endsWith(File.separator)) {
            str = str + File.separator;
        }
        File file = new File(str);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            boolean z = true;
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isFile()) {
                    z = deleteFile(listFiles[i].getAbsolutePath());
                    if (!z) {
                        break;
                    }
                } else {
                    z = deleteDirectory(listFiles[i].getAbsolutePath());
                    if (!z) {
                        break;
                    }
                }
            }
            if (z && file.delete()) {
                return true;
            }
        }
        return false;
    }

    public static long getDirectorySize(File file) {
        long j = 0;
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                j += file2.length();
            }
        }
        return j;
    }

    public static void openFile(String str, Context context) {
        String lowerCase = str.substring(str.lastIndexOf(".") + 1).toLowerCase();
        if (lowerCase.equals("doc")) {
            openWordFile(str, context);
        } else if (lowerCase.equals("pdf")) {
            openPdfFile(str, context);
        } else if (lowerCase.equals("png")) {
            openPngFile(str, context);
        } else if (lowerCase.equals("jpeg") || lowerCase.equals("jpg")) {
            openJpegFile(str, context);
        } else if (lowerCase.equals("txt")) {
            openTextFile(str, false, context);
        } else {
            new AlertDialog.Builder(context).setTitle("提示").setMessage("请安装阅读软件后查看附件信息.").setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.home.utils.FileUtils.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).show();
        }
    }

    public static void openWordFile(String str, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(str)), "application/msword");
        context.startActivity(intent);
    }

    public static void openJpegFile(String str, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(str)), PictureMimeType.MIME_TYPE_IMAGE);
        context.startActivity(intent);
    }

    public static void openPngFile(String str, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(str)), "image/png");
        context.startActivity(intent);
    }

    public static void openTextFile(String str, boolean z, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        if (z) {
            intent.setDataAndType(Uri.parse(str), HTTP.PLAIN_TEXT_TYPE);
        } else {
            intent.setDataAndType(Uri.fromFile(new File(str)), HTTP.PLAIN_TEXT_TYPE);
        }
        context.startActivity(intent);
    }

    public static void openPdfFile(String str, Context context) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(str)), "application/pdf");
        context.startActivity(intent);
    }

    public static boolean isHasStorage() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static File getStorageFilePath() {
        if (isHasStorage()) {
            return Environment.getExternalStorageDirectory();
        }
        Log.d("FileUtils", "SD卡不存在或未挂载");
        return null;
    }

    public static File getStorageDerectory(String str) {
        File storageFilePath = getStorageFilePath();
        if (storageFilePath != null) {
            File file = new File(storageFilePath.getAbsolutePath() + str);
            if (file.exists() || file.mkdirs()) {
                return file;
            }
            return null;
        }
        return null;
    }

    public static File getStorageFile(String str, String str2) {
        File storageDerectory;
        if (getStorageDerectory(str) != null) {
            String str3 = storageDerectory.getAbsolutePath() + "/" + str2;
            Log.d("FileUtils", "targetFile=" + str3);
            File file = new File(str3);
            if (file.exists()) {
                return file;
            }
            try {
                if (file.createNewFile()) {
                    return file;
                }
                return null;
            } catch (IOException unused) {
                Log.d("FileUtils", "目标文件创建失败");
            }
        }
        return null;
    }

    public static String getFilePathByUri(Context context, Uri uri) {
        int columnIndexOrThrow;
        if ("file".equals(uri.getScheme())) {
            return uri.getPath();
        }
        Uri uri2 = null;
        r3 = null;
        r3 = null;
        String str = null;
        if (Utils.RESPONSE_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < 19) {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (query != null) {
                if (query.moveToFirst() && (columnIndexOrThrow = query.getColumnIndexOrThrow("_data")) > -1) {
                    str = query.getString(columnIndexOrThrow);
                }
                query.close();
            }
            return str;
        }
        if (Utils.RESPONSE_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else {
                if (isMediaDocument(uri)) {
                    String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                    String str2 = split2[0];
                    if (PictureMimeType.MIME_TYPE_PREFIX_IMAGE.equals(str2)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if (PictureMimeType.MIME_TYPE_PREFIX_VIDEO.equals(str2)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if (PictureMimeType.MIME_TYPE_PREFIX_AUDIO.equals(str2)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
                }
            }
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String string = query.getString(query.getColumnIndexOrThrow("_data"));
                        if (query != null) {
                            query.close();
                        }
                        return string;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
