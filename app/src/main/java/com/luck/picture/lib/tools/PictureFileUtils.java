package com.luck.picture.lib.tools;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.content.FileProvider;
import com.home.utils.Utils;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Locale;

/* loaded from: classes.dex */
public class PictureFileUtils {
    public static final String APP_NAME = "PictureSelector";
    public static final String CAMERA_PATH_AUDIO = "/PictureSelector/CameraAudio/";
    public static final String CAMERA_PATH_IMAGE = "/PictureSelector/CameraImage/";
    public static final String CAMERA_PATH_VIDEO = "/PictureSelector/CameraVideo/";
    public static final String POSTFIX = ".JPEG";
    public static final String POST_AUDIO = ".mp3";
    public static final String POST_VIDEO = ".mp4";
    static final String TAG = "PictureFileUtils";

    private static String getParentPath(int i) {
        return i != 2 ? i != 3 ? CAMERA_PATH_IMAGE : CAMERA_PATH_AUDIO : CAMERA_PATH_VIDEO;
    }

    public static File createCameraFile(Context context, int i, String str, String str2) {
        return createMediaFile(context, i, str, str2);
    }

    private static File createMediaFile(Context context, int i, String str, String str2) {
        File externalStorageDirectory;
        String str3;
        if (SdkVersionUtils.checkedAndroid_Q()) {
            externalStorageDirectory = getRootDirFile(context, i);
        } else {
            externalStorageDirectory = Environment.getExternalStorageState().equals("mounted") ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        }
        if (externalStorageDirectory != null && !externalStorageDirectory.exists()) {
            externalStorageDirectory.mkdirs();
        }
        String parentPath = getParentPath(i);
        if (SdkVersionUtils.checkedAndroid_Q()) {
            str3 = externalStorageDirectory.getAbsolutePath();
        } else {
            str3 = externalStorageDirectory.getAbsolutePath() + parentPath;
        }
        File file = new File(str3);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (TextUtils.isEmpty(str)) {
            str = String.valueOf(System.currentTimeMillis());
        }
        if (i == 2) {
            return new File(file, str + POST_VIDEO);
        } else if (i == 3) {
            return new File(file, str + POST_AUDIO);
        } else {
            if (TextUtils.isEmpty(str2)) {
                str2 = POSTFIX;
            }
            return new File(file, str + str2);
        }
    }

    private static File getRootDirFile(Context context, int i) {
        if (i != 2) {
            if (i == 3) {
                return context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            }
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        return context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
    }

    private PictureFileUtils() {
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002b, code lost:
        if (r8 != null) goto L6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002d, code lost:
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x004e, code lost:
        if (r8 == null) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0051, code lost:
        return null;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0056  */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r7v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Cursor cursor;
        ?? r7 = 0;
        try {
            try {
                cursor = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
                if (cursor != null) {
                    try {
                        if (cursor.moveToFirst()) {
                            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                            if (cursor != null) {
                                cursor.close();
                            }
                            return string;
                        }
                    } catch (IllegalArgumentException e) {
                        e = e;
                        Log.i(TAG, String.format(Locale.getDefault(), "getDataColumn: _data - [%s]", e.getMessage()));
                    }
                }
            } catch (IllegalArgumentException e2) {
                e = e2;
                cursor = null;
            } catch (Throwable th) {
                th = th;
                if (r7 != 0) {
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            r7 = context;
            if (r7 != 0) {
                r7.close();
            }
            throw th;
        }
    }

    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    if (SdkVersionUtils.checkedAndroid_Q()) {
                        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + split[1];
                    }
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else {
                if (isMediaDocument(uri)) {
                    String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                    String str = split2[0];
                    if (PictureMimeType.MIME_TYPE_PREFIX_IMAGE.equals(str)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if (PictureMimeType.MIME_TYPE_PREFIX_VIDEO.equals(str)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if (PictureMimeType.MIME_TYPE_PREFIX_AUDIO.equals(str)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
                }
            }
        } else if (Utils.RESPONSE_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static void copyFile(String str, String str2) throws IOException {
        FileChannel fileChannel;
        if (str.equalsIgnoreCase(str2)) {
            return;
        }
        FileChannel fileChannel2 = null;
        try {
            FileChannel channel = new FileInputStream(new File(str)).getChannel();
            try {
                fileChannel2 = new FileOutputStream(new File(str2)).getChannel();
                channel.transferTo(0L, channel.size(), fileChannel2);
                channel.close();
                if (channel != null) {
                    channel.close();
                }
                if (fileChannel2 != null) {
                    fileChannel2.close();
                }
            } catch (Throwable th) {
                th = th;
                FileChannel fileChannel3 = fileChannel2;
                fileChannel2 = channel;
                fileChannel = fileChannel3;
                if (fileChannel2 != null) {
                    fileChannel2.close();
                }
                if (fileChannel != null) {
                    fileChannel.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileChannel = null;
        }
    }

    public static int readPictureDegree(Context context, String str) {
        ExifInterface exifInterface;
        try {
            if (SdkVersionUtils.checkedAndroid_Q()) {
                exifInterface = new ExifInterface(context.getContentResolver().openFileDescriptor(Uri.parse(str), "r").getFileDescriptor());
            } else {
                exifInterface = new ExifInterface(str);
            }
            int attributeInt = exifInterface.getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 1);
            if (attributeInt != 3) {
                if (attributeInt != 6) {
                    if (attributeInt != 8) {
                        return 0;
                    }
                    return SubsamplingScaleImageView.ORIENTATION_270;
                }
                return 90;
            }
            return SubsamplingScaleImageView.ORIENTATION_180;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Bitmap rotatingImageView(int i, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(i);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bufferedOutputStream);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createDir(Context context, String str) {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!externalFilesDir.exists()) {
            externalFilesDir.mkdirs();
        }
        return externalFilesDir + "/" + str;
    }

    public static String getDCIMCameraPath(Context context) {
        try {
            if (SdkVersionUtils.checkedAndroid_Q()) {
                return "%" + context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Camera";
            }
            return "%" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void deleteCacheDirFile(Context context, int i) {
        File[] listFiles;
        File externalFilesDir = context.getExternalFilesDir(i == PictureMimeType.ofImage() ? Environment.DIRECTORY_PICTURES : Environment.DIRECTORY_MOVIES);
        if (externalFilesDir != null) {
            for (File file : externalFilesDir.listFiles()) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }

    public static String getDiskCacheDir(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
    }

    public static Uri parUri(Context context, File file) {
        String str = context.getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT > 23) {
            return FileProvider.getUriForFile(context, str, file);
        }
        return Uri.fromFile(file);
    }

    public static String extSuffix(InputStream inputStream) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            return options.outMimeType.replace("image/", ".");
        } catch (Exception unused) {
            return PictureMimeType.JPEG;
        }
    }

    public static void rotateImage(int i, String str) {
        if (i > 0) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                File file = new File(str);
                Bitmap rotatingImageView = rotatingImageView(i, BitmapFactory.decodeFile(file.getAbsolutePath(), options));
                if (rotatingImageView != null) {
                    saveBitmapFile(rotatingImageView, file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String rotateImageToAndroidQ(Context context, int i, String str) {
        if (i > 0) {
            try {
                if (SdkVersionUtils.checkedAndroid_Q()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    FileInputStream fileInputStream = new FileInputStream(context.getContentResolver().openFileDescriptor(Uri.parse(str), "r").getFileDescriptor());
                    Bitmap decodeStream = BitmapFactory.decodeStream(fileInputStream, null, options);
                    String extSuffix = extSuffix(fileInputStream);
                    Bitmap rotatingImageView = rotatingImageView(i, decodeStream);
                    if (rotatingImageView != null) {
                        String createDir = createDir(context, System.currentTimeMillis() + extSuffix);
                        saveBitmapFile(rotatingImageView, new File(createDir));
                        return createDir;
                    }
                    return "";
                }
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }
}
