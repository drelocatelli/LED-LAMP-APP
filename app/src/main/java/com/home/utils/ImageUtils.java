package com.home.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.home.constant.Constant;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class ImageUtils {
    public static final int CROP_IMAGE = 5003;
    public static final int GET_IMAGE_BY_CAMERA = 5001;
    public static final int GET_IMAGE_FROM_PHONE = 5002;
    public static Uri cropImageUri;
    public static Uri imageUriFromCamera;

    public static void openCameraImage(Activity activity) {
        imageUriFromCamera = createImagePathUri(activity);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", imageUriFromCamera);
        activity.startActivityForResult(intent, GET_IMAGE_BY_CAMERA);
    }

    public static void openLocalImage(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        activity.startActivityForResult(intent, GET_IMAGE_FROM_PHONE);
    }

    public static void cropImage(Activity activity, Uri uri) {
        cropImageUri = createImagePathUri(activity);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("output", cropImageUri);
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, CROP_IMAGE);
    }

    private static Uri createImagePathUri(Context context) {
        Uri insert;
        String externalStorageState = Environment.getExternalStorageState();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long currentTimeMillis = System.currentTimeMillis();
        String format = simpleDateFormat.format(new Date(currentTimeMillis));
        ContentValues contentValues = new ContentValues(3);
        contentValues.put("_display_name", format);
        contentValues.put("datetaken", Long.valueOf(currentTimeMillis));
        contentValues.put("mime_type", PictureMimeType.MIME_TYPE_IMAGE);
        if (externalStorageState.equals("mounted")) {
            insert = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            insert = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, contentValues);
        }
        Log.i("", "生成的照片输出路径：" + insert.toString());
        return insert;
    }

    public static File scalFile(File file, String str) {
        if (file.length() > 51200) {
            try {
                byte[] bytesFromFile = getBytesFromFile(file);
                BitmapFactory.Options options = new BitmapFactory.Options();
                int i = 1;
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytesFromFile, 0, bytesFromFile.length, options);
                int max = Math.max(options.outWidth / 720, options.outHeight / 480);
                if (max >= 1) {
                    i = max;
                }
                System.out.println("sampleSize-->" + i);
                options.inJustDecodeBounds = false;
                options.inSampleSize = i;
                Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bytesFromFile, 0, bytesFromFile.length, options);
                if (decodeByteArray != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int i2 = 100;
                    decodeByteArray.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    while (byteArrayOutputStream.toByteArray().length > 51200) {
                        byteArrayOutputStream.reset();
                        i2 -= 10;
                        decodeByteArray.compress(Bitmap.CompressFormat.JPEG, i2, byteArrayOutputStream);
                    }
                    byteArrayOutputStream.close();
                    File file2 = new File(Constant.UPLOAD_PHOTO_PATH);
                    if (file2.exists() || file2.mkdirs()) {
                        File file3 = new File(Constant.UPLOAD_PHOTO_PATH + str);
                        if (file3.exists()) {
                            Log.i("ImageUtils.scalFile()", "flag: " + file3.delete());
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file3);
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        return file3;
                    }
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("ImageUtils.scalFile()", e.getMessage());
            }
            return null;
        }
        return file;
    }

    public static byte[] getBytesFromFile(File file) throws Exception {
        if (file != null && file.length() <= 2147483647L) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                int length = (int) file.length();
                byte[] bArr = new byte[length];
                int i = 0;
                while (i < length) {
                    int read = fileInputStream.read(bArr, i, length - i);
                    if (read < 0) {
                        break;
                    }
                    i += read;
                }
                fileInputStream.close();
                return bArr;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new Exception("File is null or file is to large");
    }
}
