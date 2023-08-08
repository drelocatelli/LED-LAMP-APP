package com.ccr.achenglibrary.photopicker.util;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class CCRImageCaptureManager {
    private static final String CAPTURE_PHOTO_PATH_KEY = "CAPTURE_PHOTO_PATH_KEY";
    private static final SimpleDateFormat PICTURE_NAME_POSTFIX_SDF = new SimpleDateFormat("yyyy-MM-dd_HH-mm_ss", Locale.CHINESE);
    private String mCurrentPhotoPath;
    private File mImageDir;

    public CCRImageCaptureManager(File file) {
        this.mImageDir = file;
        if (file.exists()) {
            return;
        }
        this.mImageDir.mkdirs();
    }

    private File createCaptureFile() throws IOException {
        File createTempFile = File.createTempFile("Capture_" + PICTURE_NAME_POSTFIX_SDF.format(new Date()), ".jpg", this.mImageDir);
        this.mCurrentPhotoPath = createTempFile.getAbsolutePath();
        return createTempFile;
    }

    public Intent getTakePictureIntent() throws IOException {
        File createCaptureFile;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(CCRPhotoPickerUtil.sApp.getPackageManager()) != null && (createCaptureFile = createCaptureFile()) != null) {
            if (Build.VERSION.SDK_INT < 24) {
                intent.putExtra("output", Uri.fromFile(createCaptureFile));
            } else {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put("_data", createCaptureFile.getAbsolutePath());
                intent.putExtra("output", CCRPhotoPickerUtil.sApp.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));
            }
        }
        return intent;
    }

    public void refreshGallery() {
        if (TextUtils.isEmpty(this.mCurrentPhotoPath)) {
            return;
        }
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(this.mCurrentPhotoPath)));
        CCRPhotoPickerUtil.sApp.sendBroadcast(intent);
        this.mCurrentPhotoPath = null;
    }

    public void deletePhotoFile() {
        if (TextUtils.isEmpty(this.mCurrentPhotoPath)) {
            return;
        }
        try {
            new File(this.mCurrentPhotoPath).deleteOnExit();
            this.mCurrentPhotoPath = null;
        } catch (Exception unused) {
        }
    }

    public String getCurrentPhotoPath() {
        return this.mCurrentPhotoPath;
    }

    public void onSaveInstanceState(Bundle bundle) {
        String str;
        if (bundle == null || (str = this.mCurrentPhotoPath) == null) {
            return;
        }
        bundle.putString(CAPTURE_PHOTO_PATH_KEY, str);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        if (bundle == null || !bundle.containsKey(CAPTURE_PHOTO_PATH_KEY)) {
            return;
        }
        this.mCurrentPhotoPath = bundle.getString(CAPTURE_PHOTO_PATH_KEY);
    }

    private File createCropFile() throws IOException {
        File createTempFile = File.createTempFile("Crop_" + PICTURE_NAME_POSTFIX_SDF.format(new Date()), PictureMimeType.PNG, CCRPhotoPickerUtil.sApp.getExternalCacheDir());
        this.mCurrentPhotoPath = createTempFile.getAbsolutePath();
        return createTempFile;
    }

    public Intent getCropIntent(String str, int i, int i2) throws IOException {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(str)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", i);
        intent.putExtra("outputY", i2);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra("output", Uri.fromFile(createCropFile()));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        return intent;
    }
}
