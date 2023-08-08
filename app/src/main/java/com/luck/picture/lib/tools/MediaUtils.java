package com.luck.picture.lib.tools;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class MediaUtils {
    public static Uri createImagePathUri(Context context, String str) {
        Uri[] uriArr = {null};
        String externalStorageState = Environment.getExternalStorageState();
        String valueOf = String.valueOf(System.currentTimeMillis());
        if (TextUtils.isEmpty(str)) {
            str = valueOf;
        }
        ContentValues contentValues = new ContentValues(3);
        contentValues.put("_display_name", str);
        contentValues.put("datetaken", valueOf);
        contentValues.put("mime_type", PictureMimeType.MIME_TYPE_IMAGE);
        if (externalStorageState.equals("mounted")) {
            uriArr[0] = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);
        } else {
            uriArr[0] = context.getContentResolver().insert(MediaStore.Files.getContentUri("internal"), contentValues);
        }
        return uriArr[0];
    }

    public static Uri createImageVideoUri(Context context, String str) {
        Uri[] uriArr = {null};
        String externalStorageState = Environment.getExternalStorageState();
        String valueOf = String.valueOf(System.currentTimeMillis());
        if (TextUtils.isEmpty(str)) {
            str = valueOf;
        }
        ContentValues contentValues = new ContentValues(3);
        contentValues.put("_display_name", str);
        contentValues.put("datetaken", valueOf);
        contentValues.put("mime_type", PictureMimeType.MIME_TYPE_VIDEO);
        if (externalStorageState.equals("mounted")) {
            uriArr[0] = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);
        } else {
            uriArr[0] = context.getContentResolver().insert(MediaStore.Files.getContentUri("internal"), contentValues);
        }
        return uriArr[0];
    }

    public static long extractDuration(Context context, boolean z, String str) {
        if (z) {
            return getLocalDuration(context, Uri.parse(str));
        }
        return getLocalDuration(str);
    }

    public static boolean isLongImg(LocalMedia localMedia) {
        if (localMedia != null) {
            return localMedia.getHeight() > localMedia.getWidth() * 3;
        }
        return false;
    }

    private static long getLocalDuration(Context context, Uri uri) {
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, uri);
            return Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private static long getLocalDuration(String str) {
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(str);
            return Long.parseLong(mediaMetadataRetriever.extractMetadata(9));
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static int[] getLocalVideoSizeToAndroidQ(Context context, String str) {
        Cursor query;
        int[] iArr = new int[2];
        try {
            if (Build.VERSION.SDK_INT >= 26 && (query = context.getApplicationContext().getContentResolver().query(Uri.parse(str), null, null, null)) != null) {
                query.moveToFirst();
                iArr[0] = query.getInt(query.getColumnIndexOrThrow("width"));
                iArr[1] = query.getInt(query.getColumnIndexOrThrow("height"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iArr;
    }

    public static int[] getLocalImageSizeToAndroidQ(Context context, String str) {
        Cursor query;
        int[] iArr = new int[2];
        try {
            if (Build.VERSION.SDK_INT >= 26 && (query = context.getApplicationContext().getContentResolver().query(Uri.parse(str), null, null, null)) != null) {
                query.moveToFirst();
                iArr[0] = query.getInt(query.getColumnIndexOrThrow("width"));
                iArr[1] = query.getInt(query.getColumnIndexOrThrow("height"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iArr;
    }

    public static int[] getLocalVideoSize(String str) {
        int[] iArr = new int[2];
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(str);
            iArr[0] = ValueOf.toInt(mediaMetadataRetriever.extractMetadata(18));
            iArr[1] = ValueOf.toInt(mediaMetadataRetriever.extractMetadata(19));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iArr;
    }

    public static int[] getLocalVideoSize(Context context, Uri uri) {
        int[] iArr = new int[2];
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(context, uri);
            iArr[0] = ValueOf.toInt(mediaMetadataRetriever.extractMetadata(18));
            iArr[1] = ValueOf.toInt(mediaMetadataRetriever.extractMetadata(19));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iArr;
    }

    public static int[] getLocalImageWidthOrHeight(String str) {
        int[] iArr = new int[2];
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            iArr[0] = options.outWidth;
            iArr[1] = options.outHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iArr;
    }

    public static int getLastImageId(Context context, boolean z) {
        Uri uri;
        int columnIndex;
        int columnIndex2;
        try {
            String dCIMCameraPath = PictureFileUtils.getDCIMCameraPath(context);
            String[] strArr = {dCIMCameraPath + "%"};
            ContentResolver contentResolver = context.getContentResolver();
            if (z) {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            Cursor query = contentResolver.query(uri, null, "_data like ?", strArr, "_id DESC");
            if (query.moveToFirst()) {
                if (z) {
                    columnIndex = query.getColumnIndex("_id");
                } else {
                    columnIndex = query.getColumnIndex("_id");
                }
                int i = query.getInt(columnIndex);
                if (z) {
                    columnIndex2 = query.getColumnIndex(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
                } else {
                    columnIndex2 = query.getColumnIndex("date_added");
                }
                int dateDiffer = DateUtils.dateDiffer(query.getLong(columnIndex2));
                query.close();
                if (dateDiffer <= 30) {
                    return i;
                }
                return -1;
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
