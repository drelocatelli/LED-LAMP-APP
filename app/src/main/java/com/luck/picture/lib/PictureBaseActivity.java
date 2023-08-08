package com.luck.picture.lib;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.compress.OnCompressListener;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.dialog.PictureLoadingDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.immersive.ImmersiveManage;
import com.luck.picture.lib.immersive.NavBarUtils;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.AndroidQTransformUtils;
import com.luck.picture.lib.tools.AttrsUtils;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropMulti;
import com.yalantis.ucrop.model.CutInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PictureBaseActivity extends AppCompatActivity implements Handler.Callback {
    private static final int MSG_ASY_COMPRESSION_RESULT_SUCCESS = 300;
    private static final int MSG_CHOOSE_RESULT_SUCCESS = 200;
    protected String cameraPath;
    protected int colorPrimary;
    protected int colorPrimaryDark;
    protected PictureLoadingDialog compressDialog;
    protected PictureSelectionConfig config;
    protected PictureLoadingDialog dialog;
    protected Context mContext;
    protected Handler mHandler;
    protected boolean numComplete;
    protected boolean openWhiteStatusBar;
    protected String originalPath;
    protected String outputCameraPath;
    protected List<LocalMedia> selectionMedias;

    @Override // android.app.Activity
    public boolean isImmersive() {
        return true;
    }

    public void immersive() {
        ImmersiveManage.immersiveAboveAPI23(this, this.colorPrimaryDark, this.colorPrimary, this.openWhiteStatusBar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            this.config = (PictureSelectionConfig) bundle.getParcelable(PictureConfig.EXTRA_CONFIG);
            this.cameraPath = bundle.getString(PictureConfig.BUNDLE_CAMERA_PATH);
            this.originalPath = bundle.getString(PictureConfig.BUNDLE_ORIGINAL_PATH);
        } else {
            this.config = PictureSelectionConfig.getInstance();
        }
        setTheme(this.config.themeStyleId);
        super.onCreate(bundle);
        initConfig();
        this.mContext = this;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        if (isImmersive()) {
            immersive();
        }
        if (this.config.style == null || this.config.style.pictureNavBarColor == 0) {
            return;
        }
        NavBarUtils.setNavBarColor(this, this.config.style.pictureNavBarColor);
    }

    private void initConfig() {
        this.outputCameraPath = this.config.outputCameraPath;
        boolean z = this.config.style != null ? this.config.style.isChangeStatusBarFontColor : false;
        this.openWhiteStatusBar = z;
        if (!z) {
            boolean z2 = this.config.isChangeStatusBarFontColor;
            this.openWhiteStatusBar = z2;
            if (!z2) {
                this.openWhiteStatusBar = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_statusFontColor);
            }
        }
        boolean z3 = this.config.style != null ? this.config.style.isOpenCompletedNumStyle : false;
        this.numComplete = z3;
        if (!z3) {
            boolean z4 = this.config.isOpenStyleNumComplete;
            this.numComplete = z4;
            if (!z4) {
                this.numComplete = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_style_numComplete);
            }
        }
        PictureSelectionConfig pictureSelectionConfig = this.config;
        pictureSelectionConfig.checkNumMode = pictureSelectionConfig.style != null ? this.config.style.isOpenCheckNumStyle : false;
        if (!this.config.checkNumMode) {
            PictureSelectionConfig pictureSelectionConfig2 = this.config;
            pictureSelectionConfig2.checkNumMode = pictureSelectionConfig2.isOpenStyleCheckNumMode;
            if (!this.config.checkNumMode) {
                this.config.checkNumMode = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_style_checkNumMode);
            }
        }
        if (this.config.style != null && this.config.style.pictureTitleBarBackgroundColor != 0) {
            this.colorPrimary = this.config.style.pictureTitleBarBackgroundColor;
        } else if (this.config.titleBarBackgroundColor != 0) {
            this.colorPrimary = this.config.titleBarBackgroundColor;
        } else {
            this.colorPrimary = AttrsUtils.getTypeValueColor(this, R.attr.colorPrimary);
        }
        if (this.config.style != null && this.config.style.pictureStatusBarColor != 0) {
            this.colorPrimaryDark = this.config.style.pictureStatusBarColor;
        } else if (this.config.pictureStatusBarColor != 0) {
            this.colorPrimaryDark = this.config.pictureStatusBarColor;
        } else {
            this.colorPrimaryDark = AttrsUtils.getTypeValueColor(this, R.attr.colorPrimaryDark);
        }
        List<LocalMedia> list = this.config.selectionMedias;
        this.selectionMedias = list;
        if (list == null) {
            this.selectionMedias = new ArrayList();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(PictureConfig.BUNDLE_CAMERA_PATH, this.cameraPath);
        bundle.putString(PictureConfig.BUNDLE_ORIGINAL_PATH, this.originalPath);
        bundle.putParcelable(PictureConfig.EXTRA_CONFIG, this.config);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showPleaseDialog() {
        if (isFinishing()) {
            return;
        }
        dismissDialog();
        PictureLoadingDialog pictureLoadingDialog = new PictureLoadingDialog(this);
        this.dialog = pictureLoadingDialog;
        pictureLoadingDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dismissDialog() {
        try {
            PictureLoadingDialog pictureLoadingDialog = this.dialog;
            if (pictureLoadingDialog == null || !pictureLoadingDialog.isShowing()) {
                return;
            }
            this.dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void showCompressDialog() {
        if (isFinishing()) {
            return;
        }
        dismissCompressDialog();
        PictureLoadingDialog pictureLoadingDialog = new PictureLoadingDialog(this);
        this.compressDialog = pictureLoadingDialog;
        pictureLoadingDialog.show();
    }

    protected void dismissCompressDialog() {
        PictureLoadingDialog pictureLoadingDialog;
        try {
            if (isFinishing() || (pictureLoadingDialog = this.compressDialog) == null || !pictureLoadingDialog.isShowing()) {
                return;
            }
            this.compressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void compressImage(final List<LocalMedia> list) {
        showCompressDialog();
        if (this.config.synOrAsy) {
            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() { // from class: com.luck.picture.lib.PictureBaseActivity$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PictureBaseActivity.this.m30lambda$compressImage$0$comluckpicturelibPictureBaseActivity(list);
                }
            });
        } else {
            Luban.with(this).loadMediaData(list, this.config.cameraFileName).ignoreBy(this.config.minimumCompressSize).setCompressQuality(this.config.compressQuality).setTargetDir(this.config.compressSavePath).setCompressListener(new OnCompressListener() { // from class: com.luck.picture.lib.PictureBaseActivity.1
                @Override // com.luck.picture.lib.compress.OnCompressListener
                public void onStart() {
                }

                @Override // com.luck.picture.lib.compress.OnCompressListener
                public void onSuccess(List<LocalMedia> list2) {
                    BroadcastManager.getInstance(PictureBaseActivity.this.getApplicationContext()).action(BroadcastAction.ACTION_CLOSE_PREVIEW).broadcast();
                    PictureBaseActivity.this.onResult(list2);
                }

                @Override // com.luck.picture.lib.compress.OnCompressListener
                public void onError(Throwable th) {
                    BroadcastManager.getInstance(PictureBaseActivity.this.getApplicationContext()).action(BroadcastAction.ACTION_CLOSE_PREVIEW).broadcast();
                    PictureBaseActivity.this.onResult(list);
                }
            }).launch();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$compressImage$0$com-luck-picture-lib-PictureBaseActivity  reason: not valid java name */
    public /* synthetic */ void m30lambda$compressImage$0$comluckpicturelibPictureBaseActivity(List list) {
        try {
            List<File> list2 = Luban.with(this.mContext).loadMediaData(list, this.config.cameraFileName).setTargetDir(this.config.compressSavePath).setCompressQuality(this.config.compressQuality).ignoreBy(this.config.minimumCompressSize).get();
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(300, new Object[]{list, list2}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCompressCallBack(List<LocalMedia> list, List<File> list2) {
        if (list == null || list2 == null) {
            closeActivity();
            return;
        }
        int size = list.size();
        if (list2.size() == size) {
            for (int i = 0; i < size; i++) {
                String path = list2.get(i).getPath();
                LocalMedia localMedia = list.get(i);
                boolean z = !TextUtils.isEmpty(path) && PictureMimeType.isHttp(path);
                localMedia.setCompressed(!z);
                localMedia.setCompressPath(z ? "" : path);
                if (SdkVersionUtils.checkedAndroid_Q()) {
                    if (z) {
                        path = localMedia.getCutPath();
                    }
                    localMedia.setAndroidQToPath(path);
                }
            }
        }
        BroadcastManager.getInstance(getApplicationContext()).action(BroadcastAction.ACTION_CLOSE_PREVIEW).broadcast();
        onResult(list);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startCrop(String str) {
        int typeValueColor;
        int typeValueColor2;
        int typeValueColor3;
        boolean z;
        String lastImgType;
        StringBuilder sb;
        UCrop.Options options = new UCrop.Options();
        if (this.config.cropStyle != null) {
            typeValueColor = this.config.cropStyle.cropTitleBarBackgroundColor != 0 ? this.config.cropStyle.cropTitleBarBackgroundColor : 0;
            typeValueColor2 = this.config.cropStyle.cropStatusBarColorPrimaryDark != 0 ? this.config.cropStyle.cropStatusBarColorPrimaryDark : 0;
            typeValueColor3 = this.config.cropStyle.cropTitleColor != 0 ? this.config.cropStyle.cropTitleColor : 0;
            z = this.config.cropStyle.isChangeStatusBarFontColor;
        } else {
            if (this.config.cropTitleBarBackgroundColor != 0) {
                typeValueColor = this.config.cropTitleBarBackgroundColor;
            } else {
                typeValueColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_toolbar_bg);
            }
            if (this.config.cropStatusBarColorPrimaryDark != 0) {
                typeValueColor2 = this.config.cropStatusBarColorPrimaryDark;
            } else {
                typeValueColor2 = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_status_color);
            }
            if (this.config.cropTitleColor != 0) {
                typeValueColor3 = this.config.cropTitleColor;
            } else {
                typeValueColor3 = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_title_color);
            }
            z = this.config.isChangeStatusBarFontColor;
            if (!z) {
                z = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_statusFontColor);
            }
        }
        options.isOpenWhiteStatusBar(z);
        options.setToolbarColor(typeValueColor);
        options.setStatusBarColor(typeValueColor2);
        options.setToolbarWidgetColor(typeValueColor3);
        options.setCircleDimmedLayer(this.config.circleDimmedLayer);
        options.setShowCropFrame(this.config.showCropFrame);
        options.setShowCropGrid(this.config.showCropGrid);
        options.setDragFrameEnabled(this.config.isDragFrame);
        options.setScaleEnabled(this.config.scaleEnabled);
        options.setRotateEnabled(this.config.rotateEnabled);
        options.setCompressionQuality(this.config.cropCompressQuality);
        options.setHideBottomControls(this.config.hideBottomControls);
        options.setFreeStyleCropEnabled(this.config.freeStyleCropEnabled);
        options.setCropExitAnimation(this.config.windowAnimationStyle != null ? this.config.windowAnimationStyle.activityCropExitAnimation : 0);
        options.setNavBarColor(this.config.cropStyle != null ? this.config.cropStyle.cropNavBarColor : 0);
        boolean isHttp = PictureMimeType.isHttp(str);
        boolean checkedAndroid_Q = SdkVersionUtils.checkedAndroid_Q();
        if (checkedAndroid_Q) {
            lastImgType = PictureMimeType.getLastImgSuffix(PictureMimeType.getMimeType(this.mContext, Uri.parse(str)));
        } else {
            lastImgType = PictureMimeType.getLastImgType(str);
        }
        Uri parse = (isHttp || checkedAndroid_Q) ? Uri.parse(str) : Uri.fromFile(new File(str));
        String diskCacheDir = PictureFileUtils.getDiskCacheDir(this);
        if (TextUtils.isEmpty(this.config.cameraFileName)) {
            sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
        } else {
            sb = new StringBuilder();
            sb.append(this.config.cameraFileName);
        }
        sb.append(lastImgType);
        UCrop.of(parse, Uri.fromFile(new File(diskCacheDir, sb.toString()))).withAspectRatio(this.config.aspect_ratio_x, this.config.aspect_ratio_y).withMaxResultSize(this.config.cropWidth, this.config.cropHeight).withOptions(options).startAnimation(this, this.config.windowAnimationStyle != null ? this.config.windowAnimationStyle.activityCropEnterAnimation : 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startCrop(ArrayList<CutInfo> arrayList) {
        int typeValueColor;
        int typeValueColor2;
        int typeValueColor3;
        boolean z;
        String lastImgType;
        StringBuilder sb;
        UCropMulti.Options options = new UCropMulti.Options();
        if (this.config.cropStyle != null) {
            typeValueColor = this.config.cropStyle.cropTitleBarBackgroundColor != 0 ? this.config.cropStyle.cropTitleBarBackgroundColor : 0;
            typeValueColor2 = this.config.cropStyle.cropStatusBarColorPrimaryDark != 0 ? this.config.cropStyle.cropStatusBarColorPrimaryDark : 0;
            typeValueColor3 = this.config.cropStyle.cropTitleColor != 0 ? this.config.cropStyle.cropTitleColor : 0;
            z = this.config.cropStyle.isChangeStatusBarFontColor;
        } else {
            if (this.config.cropTitleBarBackgroundColor != 0) {
                typeValueColor = this.config.cropTitleBarBackgroundColor;
            } else {
                typeValueColor = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_toolbar_bg);
            }
            if (this.config.cropStatusBarColorPrimaryDark != 0) {
                typeValueColor2 = this.config.cropStatusBarColorPrimaryDark;
            } else {
                typeValueColor2 = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_status_color);
            }
            if (this.config.cropTitleColor != 0) {
                typeValueColor3 = this.config.cropTitleColor;
            } else {
                typeValueColor3 = AttrsUtils.getTypeValueColor(this, R.attr.picture_crop_title_color);
            }
            z = this.config.isChangeStatusBarFontColor;
            if (!z) {
                z = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_statusFontColor);
            }
        }
        options.isOpenWhiteStatusBar(z);
        options.setToolbarColor(typeValueColor);
        options.setStatusBarColor(typeValueColor2);
        options.setToolbarWidgetColor(typeValueColor3);
        options.setCircleDimmedLayer(this.config.circleDimmedLayer);
        options.setShowCropFrame(this.config.showCropFrame);
        options.setDragFrameEnabled(this.config.isDragFrame);
        options.setShowCropGrid(this.config.showCropGrid);
        options.setScaleEnabled(this.config.scaleEnabled);
        options.setRotateEnabled(this.config.rotateEnabled);
        options.setHideBottomControls(this.config.hideBottomControls);
        options.setCompressionQuality(this.config.cropCompressQuality);
        options.setCutListData(arrayList);
        options.setFreeStyleCropEnabled(this.config.freeStyleCropEnabled);
        options.setCropExitAnimation(this.config.windowAnimationStyle != null ? this.config.windowAnimationStyle.activityCropExitAnimation : 0);
        options.setNavBarColor(this.config.cropStyle != null ? this.config.cropStyle.cropNavBarColor : 0);
        String path = arrayList.size() > 0 ? arrayList.get(0).getPath() : "";
        boolean checkedAndroid_Q = SdkVersionUtils.checkedAndroid_Q();
        boolean isHttp = PictureMimeType.isHttp(path);
        if (checkedAndroid_Q) {
            lastImgType = PictureMimeType.getLastImgSuffix(PictureMimeType.getMimeType(this.mContext, Uri.parse(path)));
        } else {
            lastImgType = PictureMimeType.getLastImgType(path);
        }
        Uri parse = (isHttp || checkedAndroid_Q) ? Uri.parse(path) : Uri.fromFile(new File(path));
        String diskCacheDir = PictureFileUtils.getDiskCacheDir(this);
        if (TextUtils.isEmpty(this.config.cameraFileName)) {
            sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
        } else {
            sb = new StringBuilder();
            sb.append(this.config.cameraFileName);
        }
        sb.append(lastImgType);
        UCropMulti.of(parse, Uri.fromFile(new File(diskCacheDir, sb.toString()))).withAspectRatio(this.config.aspect_ratio_x, this.config.aspect_ratio_y).withMaxResultSize(this.config.cropWidth, this.config.cropHeight).withOptions(options).startAnimation(this, this.config.windowAnimationStyle != null ? this.config.windowAnimationStyle.activityCropEnterAnimation : 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handlerResult(List<LocalMedia> list) {
        if (this.config.isCompress) {
            compressImage(list);
        } else {
            onResult(list);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void createNewFolder(List<LocalMediaFolder> list) {
        if (list.size() == 0) {
            LocalMediaFolder localMediaFolder = new LocalMediaFolder();
            localMediaFolder.setName(getString(this.config.chooseMode == PictureMimeType.ofAudio() ? R.string.picture_all_audio : R.string.picture_camera_roll));
            localMediaFolder.setFirstImagePath("");
            list.add(localMediaFolder);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public LocalMediaFolder getImageFolder(String str, List<LocalMediaFolder> list) {
        File parentFile = new File(str).getParentFile();
        for (LocalMediaFolder localMediaFolder : list) {
            if (localMediaFolder.getName().equals(parentFile.getName())) {
                return localMediaFolder;
            }
        }
        LocalMediaFolder localMediaFolder2 = new LocalMediaFolder();
        localMediaFolder2.setName(parentFile.getName());
        localMediaFolder2.setFirstImagePath(str);
        list.add(localMediaFolder2);
        return localMediaFolder2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onResult(List<LocalMedia> list) {
        boolean checkedAndroid_Q = SdkVersionUtils.checkedAndroid_Q();
        boolean eqVideo = PictureMimeType.eqVideo((list == null || list.size() <= 0) ? "" : list.get(0).getMimeType());
        if (checkedAndroid_Q && !eqVideo) {
            showCompressDialog();
        }
        if (checkedAndroid_Q) {
            onResultToAndroidAsy(list);
            return;
        }
        dismissCompressDialog();
        if (this.config.camera && this.config.selectionMode == 2 && this.selectionMedias != null) {
            list.addAll(list.size() > 0 ? list.size() - 1 : 0, this.selectionMedias);
        }
        setResult(-1, PictureSelector.putIntentResult(list));
        closeActivity();
    }

    private void onResultToAndroidAsy(final List<LocalMedia> list) {
        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() { // from class: com.luck.picture.lib.PictureBaseActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PictureBaseActivity.this.m31xd0a5db22(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onResultToAndroidAsy$1$com-luck-picture-lib-PictureBaseActivity  reason: not valid java name */
    public /* synthetic */ void m31xd0a5db22(List list) {
        String parseImagePathToAndroidQ;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            LocalMedia localMedia = (LocalMedia) list.get(i);
            if (localMedia != null && !TextUtils.isEmpty(localMedia.getPath())) {
                if (localMedia.isCut()) {
                    localMedia.setAndroidQToPath(TextUtils.isEmpty(localMedia.getAndroidQToPath()) ? localMedia.getCutPath() : localMedia.getAndroidQToPath());
                } else if (localMedia.isCompressed()) {
                    localMedia.setAndroidQToPath(TextUtils.isEmpty(localMedia.getAndroidQToPath()) ? localMedia.getCompressPath() : localMedia.getAndroidQToPath());
                } else if (TextUtils.isEmpty(localMedia.getAndroidQToPath())) {
                    if (PictureMimeType.eqVideo(localMedia.getMimeType())) {
                        parseImagePathToAndroidQ = AndroidQTransformUtils.parseVideoPathToAndroidQ(getApplicationContext(), localMedia.getPath(), this.config.cameraFileName, localMedia.getMimeType());
                    } else if (PictureMimeType.eqAudio(localMedia.getMimeType())) {
                        parseImagePathToAndroidQ = AndroidQTransformUtils.parseAudioPathToAndroidQ(getApplicationContext(), localMedia.getPath(), this.config.cameraFileName, localMedia.getMimeType());
                    } else {
                        parseImagePathToAndroidQ = AndroidQTransformUtils.parseImagePathToAndroidQ(getApplicationContext(), localMedia.getPath(), this.config.cameraFileName, localMedia.getMimeType());
                    }
                    localMedia.setAndroidQToPath(parseImagePathToAndroidQ);
                }
            }
        }
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(200, list));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void closeActivity() {
        finish();
        if (this.config.camera) {
            overridePendingTransition(0, R.anim.picture_anim_fade_out);
        } else {
            overridePendingTransition(0, (this.config.windowAnimationStyle == null || this.config.windowAnimationStyle.activityExitAnimation == 0) ? R.anim.picture_anim_exit : this.config.windowAnimationStyle.activityExitAnimation);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        dismissCompressDialog();
        dismissDialog();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeImage(int i, boolean z) {
        Uri uri;
        try {
            ContentResolver contentResolver = getContentResolver();
            if (z) {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            contentResolver.delete(uri, "_id=?", new String[]{Long.toString(i)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getAudioPath(Intent intent) {
        boolean z = Build.VERSION.SDK_INT <= 19;
        if (intent == null || this.config.chooseMode != PictureMimeType.ofAudio()) {
            return "";
        }
        try {
            Uri data = intent.getData();
            if (z) {
                return data.getPath();
            }
            return getAudioFilePathFromUri(data);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    protected String getAudioFilePathFromUri(Uri uri) {
        String str = "";
        try {
            Cursor query = getContentResolver().query(uri, null, null, null, null);
            query.moveToFirst();
            str = query.getString(query.getColumnIndex("_data"));
            query.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startOpenCamera() {
        Uri parUri;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (SdkVersionUtils.checkedAndroid_Q()) {
                parUri = MediaUtils.createImagePathUri(getApplicationContext(), this.config.cameraFileName);
                this.cameraPath = parUri.toString();
            } else {
                File createCameraFile = PictureFileUtils.createCameraFile(getApplicationContext(), this.config.chooseMode == 0 ? 1 : this.config.chooseMode, this.config.cameraFileName, this.config.suffixType);
                this.cameraPath = createCameraFile.getAbsolutePath();
                parUri = PictureFileUtils.parUri(this, createCameraFile);
            }
            intent.putExtra("output", parUri);
            startActivityForResult(intent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void startOpenCameraVideo() {
        Uri parUri;
        Intent intent = new Intent("android.media.action.VIDEO_CAPTURE");
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (SdkVersionUtils.checkedAndroid_Q()) {
                parUri = MediaUtils.createImageVideoUri(getApplicationContext(), this.config.cameraFileName);
                this.cameraPath = parUri.toString();
            } else {
                File createCameraFile = PictureFileUtils.createCameraFile(getApplicationContext(), this.config.chooseMode == 0 ? 2 : this.config.chooseMode, this.config.cameraFileName, this.config.suffixType);
                this.cameraPath = createCameraFile.getAbsolutePath();
                parUri = PictureFileUtils.parUri(this, createCameraFile);
            }
            intent.putExtra("output", parUri);
            intent.putExtra("android.intent.extra.durationLimit", this.config.recordVideoSecond);
            intent.putExtra("android.intent.extra.sizeLimit", 10485760);
            intent.putExtra("android.intent.extra.videoQuality", this.config.videoQuality);
            startActivityForResult(intent, PictureConfig.REQUEST_CAMERA);
        }
    }

    public void startOpenCameraAudio() {
        if (PermissionChecker.checkSelfPermission(this, "android.permission.RECORD_AUDIO")) {
            Intent intent = new Intent("android.provider.MediaStore.RECORD_SOUND");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, PictureConfig.REQUEST_CAMERA);
                return;
            }
            return;
        }
        PermissionChecker.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO"}, 3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void multiCropHandleResult(Intent intent) {
        ArrayList arrayList = new ArrayList();
        List<CutInfo> output = UCropMulti.getOutput(intent);
        int size = output.size();
        for (int i = 0; i < size; i++) {
            CutInfo cutInfo = output.get(i);
            LocalMedia localMedia = new LocalMedia();
            String imageMimeType = PictureMimeType.getImageMimeType(cutInfo.getCutPath());
            localMedia.setCut(!TextUtils.isEmpty(cutInfo.getCutPath()));
            localMedia.setPath(cutInfo.getPath());
            localMedia.setCutPath(cutInfo.getCutPath());
            localMedia.setMimeType(imageMimeType);
            localMedia.setWidth(cutInfo.getImageWidth());
            localMedia.setHeight(cutInfo.getImageHeight());
            localMedia.setSize(new File(TextUtils.isEmpty(cutInfo.getCutPath()) ? cutInfo.getPath() : cutInfo.getCutPath()).length());
            if (SdkVersionUtils.checkedAndroid_Q()) {
                localMedia.setAndroidQToPath(TextUtils.isEmpty(cutInfo.getAndroidQToPath()) ? cutInfo.getCutPath() : cutInfo.getAndroidQToPath());
            }
            localMedia.setChooseModel(this.config.chooseMode);
            arrayList.add(localMedia);
        }
        handlerResult(arrayList);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 200) {
            List list = (List) message.obj;
            dismissCompressDialog();
            if (list != null) {
                if (this.config.camera && this.config.selectionMode == 2 && this.selectionMedias != null) {
                    list.addAll(list.size() > 0 ? list.size() - 1 : 0, this.selectionMedias);
                }
                setResult(-1, PictureSelector.putIntentResult(list));
                closeActivity();
            }
        } else if (i == 300 && message.obj != null && (message.obj instanceof Object[])) {
            Object[] objArr = (Object[]) message.obj;
            if (objArr.length > 0) {
                handleCompressCallBack((List) objArr[0], (List) objArr[1]);
            }
        }
        return false;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 3) {
            return;
        }
        if (iArr[0] == 0) {
            Intent intent = new Intent("android.provider.MediaStore.RECORD_SOUND");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, PictureConfig.REQUEST_CAMERA);
                return;
            }
            return;
        }
        ToastUtils.s(this.mContext, getString(R.string.picture_audio));
    }
}
