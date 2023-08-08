package com.luck.picture.lib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PictureSelectorCameraEmptyActivity extends PictureBaseActivity {
    @Override // com.luck.picture.lib.PictureBaseActivity, android.app.Activity
    public boolean isImmersive() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (PermissionChecker.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") && PermissionChecker.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            onTakePhoto();
            setTheme(R.style.Picture_Theme_Translucent);
            setContentView(R.layout.picture_empty);
            return;
        }
        ToastUtils.s(this.mContext, getString(R.string.picture_camera));
        closeActivity();
    }

    private void onTakePhoto() {
        if (PermissionChecker.checkSelfPermission(this, "android.permission.CAMERA")) {
            startCamera();
        } else {
            PermissionChecker.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 2);
        }
    }

    private void startCamera() {
        int i = this.config.chooseMode;
        if (i == 0 || i == 1) {
            startOpenCamera();
        } else if (i == 2) {
            startOpenCameraVideo();
        } else if (i != 3) {
        } else {
            startOpenCameraAudio();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            if (i2 == 0) {
                closeActivity();
            } else if (i2 == 96) {
                ToastUtils.s(this.mContext, ((Throwable) intent.getSerializableExtra(UCrop.EXTRA_ERROR)).getMessage());
            }
        } else if (i == 69) {
            singleCropHandleResult(intent);
        } else if (i == 609) {
            multiCropHandleResult(intent);
        } else if (i != 909) {
        } else {
            requestCamera(intent);
        }
    }

    private void singleCropHandleResult(Intent intent) {
        ArrayList arrayList = new ArrayList();
        String path = UCrop.getOutput(intent).getPath();
        String str = this.cameraPath;
        boolean z = this.config.isCamera;
        LocalMedia localMedia = new LocalMedia(str, 0L, false, z ? 1 : 0, 0, this.config.chooseMode);
        localMedia.setCut(true);
        localMedia.setCutPath(path);
        localMedia.setMimeType(PictureMimeType.getImageMimeType(path));
        arrayList.add(localMedia);
        handlerResult(arrayList);
    }

    private void requestCamera(Intent intent) {
        long j;
        String fileToType;
        int[] localVideoSize;
        boolean checkedAndroid_Q = SdkVersionUtils.checkedAndroid_Q();
        int i = this.config.chooseMode;
        int ofAudio = PictureMimeType.ofAudio();
        long j2 = 0;
        String str = PictureMimeType.MIME_TYPE_AUDIO;
        if (i == ofAudio) {
            this.cameraPath = getAudioPath(intent);
            if (TextUtils.isEmpty(this.cameraPath)) {
                return;
            }
            if (checkedAndroid_Q) {
                j = MediaUtils.extractDuration(this.mContext, true, this.cameraPath);
            } else {
                j = MediaUtils.extractDuration(this.mContext, false, this.cameraPath);
            }
        } else {
            str = null;
            j = 0;
        }
        if (TextUtils.isEmpty(this.cameraPath)) {
            return;
        }
        new File(this.cameraPath);
        int[] iArr = new int[2];
        File file = new File(this.cameraPath);
        if (!checkedAndroid_Q) {
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        }
        LocalMedia localMedia = new LocalMedia();
        if (this.config.chooseMode != PictureMimeType.ofAudio()) {
            if (checkedAndroid_Q) {
                File file2 = new File(PictureFileUtils.getPath(getApplicationContext(), Uri.parse(this.cameraPath)));
                j2 = file2.length();
                fileToType = PictureMimeType.fileToType(file2);
                if (PictureMimeType.eqImage(fileToType)) {
                    localMedia.setAndroidQToPath(PictureFileUtils.rotateImageToAndroidQ(this, PictureFileUtils.readPictureDegree(this, this.cameraPath), this.cameraPath));
                    localVideoSize = MediaUtils.getLocalImageSizeToAndroidQ(this, this.cameraPath);
                } else {
                    localVideoSize = MediaUtils.getLocalVideoSize(this, Uri.parse(this.cameraPath));
                    j = MediaUtils.extractDuration(this.mContext, true, this.cameraPath);
                }
            } else {
                fileToType = PictureMimeType.fileToType(file);
                j2 = new File(this.cameraPath).length();
                if (PictureMimeType.eqImage(fileToType)) {
                    PictureFileUtils.rotateImage(PictureFileUtils.readPictureDegree(this, this.cameraPath), this.cameraPath);
                    localVideoSize = MediaUtils.getLocalImageWidthOrHeight(this.cameraPath);
                } else {
                    localVideoSize = MediaUtils.getLocalVideoSize(this.cameraPath);
                    j = MediaUtils.extractDuration(this.mContext, false, this.cameraPath);
                }
            }
            str = fileToType;
            iArr = localVideoSize;
            boolean eqImage = PictureMimeType.eqImage(str);
            int lastImageId = MediaUtils.getLastImageId(this, eqImage);
            if (lastImageId != -1) {
                removeImage(lastImageId, eqImage);
            }
        }
        localMedia.setDuration(j);
        localMedia.setWidth(iArr[0]);
        localMedia.setHeight(iArr[1]);
        localMedia.setPath(this.cameraPath);
        localMedia.setMimeType(str);
        localMedia.setSize(j2);
        localMedia.setChooseModel(this.config.chooseMode);
        cameraHandleResult(localMedia, str);
    }

    private void cameraHandleResult(LocalMedia localMedia, String str) {
        boolean eqImage = PictureMimeType.eqImage(str);
        if (this.config.enableCrop && eqImage) {
            this.originalPath = this.cameraPath;
            startCrop(this.cameraPath);
        } else if (this.config.isCompress && eqImage) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(localMedia);
            compressImage(arrayList);
        } else {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(localMedia);
            onResult(arrayList2);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1) {
            for (int i2 : iArr) {
                if (i2 == 0) {
                    onTakePhoto();
                } else {
                    closeActivity();
                    ToastUtils.s(this.mContext, getString(R.string.picture_camera));
                }
            }
        } else if (i == 2) {
            if (iArr[0] == 0) {
                onTakePhoto();
                return;
            }
            closeActivity();
            ToastUtils.s(this.mContext, getString(R.string.picture_camera));
        }
    }
}
