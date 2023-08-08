package com.luck.picture.lib;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.DoubleUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class PictureSelectionModel {
    private PictureSelectionConfig selectionConfig;
    private PictureSelector selector;

    public PictureSelectionModel(PictureSelector pictureSelector, int i) {
        this.selector = pictureSelector;
        PictureSelectionConfig cleanInstance = PictureSelectionConfig.getCleanInstance();
        this.selectionConfig = cleanInstance;
        cleanInstance.chooseMode = i;
    }

    public PictureSelectionModel(PictureSelector pictureSelector, int i, boolean z) {
        this.selector = pictureSelector;
        PictureSelectionConfig cleanInstance = PictureSelectionConfig.getCleanInstance();
        this.selectionConfig = cleanInstance;
        cleanInstance.camera = z;
        this.selectionConfig.chooseMode = i;
    }

    public PictureSelectionModel theme(int i) {
        this.selectionConfig.themeStyleId = i;
        return this;
    }

    public PictureSelectionModel setLanguage(int i) {
        this.selectionConfig.language = i;
        return this;
    }

    public PictureSelectionModel loadImageEngine(ImageEngine imageEngine) {
        if (this.selectionConfig.imageEngine != imageEngine) {
            this.selectionConfig.imageEngine = imageEngine;
        }
        return this;
    }

    public PictureSelectionModel selectionMode(int i) {
        this.selectionConfig.selectionMode = i;
        return this;
    }

    public PictureSelectionModel enableCrop(boolean z) {
        this.selectionConfig.enableCrop = z;
        return this;
    }

    public PictureSelectionModel enablePreviewAudio(boolean z) {
        this.selectionConfig.enablePreviewAudio = z;
        return this;
    }

    public PictureSelectionModel freeStyleCropEnabled(boolean z) {
        this.selectionConfig.freeStyleCropEnabled = z;
        return this;
    }

    public PictureSelectionModel scaleEnabled(boolean z) {
        this.selectionConfig.scaleEnabled = z;
        return this;
    }

    public PictureSelectionModel rotateEnabled(boolean z) {
        this.selectionConfig.rotateEnabled = z;
        return this;
    }

    public PictureSelectionModel circleDimmedLayer(boolean z) {
        this.selectionConfig.circleDimmedLayer = z;
        return this;
    }

    public PictureSelectionModel showCropFrame(boolean z) {
        this.selectionConfig.showCropFrame = z;
        return this;
    }

    public PictureSelectionModel showCropGrid(boolean z) {
        this.selectionConfig.showCropGrid = z;
        return this;
    }

    public PictureSelectionModel hideBottomControls(boolean z) {
        this.selectionConfig.hideBottomControls = z;
        return this;
    }

    public PictureSelectionModel withAspectRatio(int i, int i2) {
        this.selectionConfig.aspect_ratio_x = i;
        this.selectionConfig.aspect_ratio_y = i2;
        return this;
    }

    public PictureSelectionModel maxSelectNum(int i) {
        this.selectionConfig.maxSelectNum = i;
        return this;
    }

    public PictureSelectionModel minSelectNum(int i) {
        this.selectionConfig.minSelectNum = i;
        return this;
    }

    public PictureSelectionModel isSingleDirectReturn(boolean z) {
        PictureSelectionConfig pictureSelectionConfig = this.selectionConfig;
        if (pictureSelectionConfig.selectionMode != 1) {
            z = false;
        }
        pictureSelectionConfig.isSingleDirectReturn = z;
        return this;
    }

    public PictureSelectionModel videoQuality(int i) {
        this.selectionConfig.videoQuality = i;
        return this;
    }

    public PictureSelectionModel imageFormat(String str) {
        this.selectionConfig.suffixType = str;
        return this;
    }

    public PictureSelectionModel cropWH(int i, int i2) {
        this.selectionConfig.cropWidth = i;
        this.selectionConfig.cropHeight = i2;
        return this;
    }

    public PictureSelectionModel videoMaxSecond(int i) {
        this.selectionConfig.videoMaxSecond = i * 1000;
        return this;
    }

    public PictureSelectionModel videoMinSecond(int i) {
        this.selectionConfig.videoMinSecond = i * 1000;
        return this;
    }

    public PictureSelectionModel recordVideoSecond(int i) {
        this.selectionConfig.recordVideoSecond = i;
        return this;
    }

    @Deprecated
    public PictureSelectionModel glideOverride(int i, int i2) {
        this.selectionConfig.overrideWidth = i;
        this.selectionConfig.overrideHeight = i2;
        return this;
    }

    @Deprecated
    public PictureSelectionModel sizeMultiplier(float f) {
        this.selectionConfig.sizeMultiplier = f;
        return this;
    }

    public PictureSelectionModel imageSpanCount(int i) {
        this.selectionConfig.imageSpanCount = i;
        return this;
    }

    public PictureSelectionModel minimumCompressSize(int i) {
        this.selectionConfig.minimumCompressSize = i;
        return this;
    }

    @Deprecated
    public PictureSelectionModel cropCompressQuality(int i) {
        this.selectionConfig.cropCompressQuality = i;
        return this;
    }

    public PictureSelectionModel cutOutQuality(int i) {
        this.selectionConfig.cropCompressQuality = i;
        return this;
    }

    public PictureSelectionModel compress(boolean z) {
        this.selectionConfig.isCompress = z;
        return this;
    }

    public PictureSelectionModel compressQuality(int i) {
        this.selectionConfig.compressQuality = i;
        return this;
    }

    public PictureSelectionModel synOrAsy(boolean z) {
        this.selectionConfig.synOrAsy = z;
        return this;
    }

    public PictureSelectionModel compressSavePath(String str) {
        this.selectionConfig.compressSavePath = str;
        return this;
    }

    public PictureSelectionModel cameraFileName(String str) {
        this.selectionConfig.cameraFileName = str;
        return this;
    }

    public PictureSelectionModel isZoomAnim(boolean z) {
        this.selectionConfig.zoomAnim = z;
        return this;
    }

    public PictureSelectionModel previewEggs(boolean z) {
        this.selectionConfig.previewEggs = z;
        return this;
    }

    public PictureSelectionModel isCamera(boolean z) {
        this.selectionConfig.isCamera = z;
        return this;
    }

    @Deprecated
    public PictureSelectionModel setOutputCameraPath(String str) {
        this.selectionConfig.outputCameraPath = str;
        return this;
    }

    public PictureSelectionModel queryMaxFileSize(int i) {
        this.selectionConfig.filterFileSize = i;
        return this;
    }

    public PictureSelectionModel isGif(boolean z) {
        this.selectionConfig.isGif = z;
        return this;
    }

    public PictureSelectionModel previewImage(boolean z) {
        this.selectionConfig.enablePreview = z;
        return this;
    }

    public PictureSelectionModel previewVideo(boolean z) {
        this.selectionConfig.enPreviewVideo = z;
        return this;
    }

    public PictureSelectionModel isNotPreviewDownload(boolean z) {
        this.selectionConfig.isNotPreviewDownload = z;
        return this;
    }

    public PictureSelectionModel querySpecifiedFormatSuffix(String str) {
        this.selectionConfig.specifiedFormat = str;
        return this;
    }

    public PictureSelectionModel openClickSound(boolean z) {
        this.selectionConfig.openClickSound = z;
        return this;
    }

    public PictureSelectionModel isDragFrame(boolean z) {
        this.selectionConfig.isDragFrame = z;
        return this;
    }

    public PictureSelectionModel selectionMedia(List<LocalMedia> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (this.selectionConfig.selectionMode == 1 && this.selectionConfig.isSingleDirectReturn) {
            list.clear();
        }
        this.selectionConfig.selectionMedias = list;
        return this;
    }

    @Deprecated
    public PictureSelectionModel isChangeStatusBarFontColor(boolean z) {
        this.selectionConfig.isChangeStatusBarFontColor = z;
        return this;
    }

    @Deprecated
    public PictureSelectionModel isOpenStyleNumComplete(boolean z) {
        this.selectionConfig.isOpenStyleNumComplete = z;
        return this;
    }

    @Deprecated
    public PictureSelectionModel isOpenStyleCheckNumMode(boolean z) {
        this.selectionConfig.isOpenStyleCheckNumMode = z;
        return this;
    }

    @Deprecated
    public PictureSelectionModel setTitleBarBackgroundColor(int i) {
        this.selectionConfig.titleBarBackgroundColor = i;
        return this;
    }

    @Deprecated
    public PictureSelectionModel setStatusBarColorPrimaryDark(int i) {
        this.selectionConfig.pictureStatusBarColor = i;
        return this;
    }

    @Deprecated
    public PictureSelectionModel setCropTitleBarBackgroundColor(int i) {
        this.selectionConfig.cropTitleBarBackgroundColor = i;
        return this;
    }

    @Deprecated
    public PictureSelectionModel setCropStatusBarColorPrimaryDark(int i) {
        this.selectionConfig.cropStatusBarColorPrimaryDark = i;
        return this;
    }

    @Deprecated
    public PictureSelectionModel setCropTitleColor(int i) {
        this.selectionConfig.cropTitleColor = i;
        return this;
    }

    @Deprecated
    public PictureSelectionModel setUpArrowDrawable(int i) {
        this.selectionConfig.upResId = i;
        return this;
    }

    @Deprecated
    public PictureSelectionModel setDownArrowDrawable(int i) {
        this.selectionConfig.downResId = i;
        return this;
    }

    public PictureSelectionModel setPictureCropStyle(PictureCropParameterStyle pictureCropParameterStyle) {
        this.selectionConfig.cropStyle = pictureCropParameterStyle;
        return this;
    }

    public PictureSelectionModel setPictureStyle(PictureParameterStyle pictureParameterStyle) {
        this.selectionConfig.style = pictureParameterStyle;
        return this;
    }

    public PictureSelectionModel setPictureWindowAnimationStyle(PictureWindowAnimationStyle pictureWindowAnimationStyle) {
        this.selectionConfig.windowAnimationStyle = pictureWindowAnimationStyle;
        return this;
    }

    public PictureSelectionModel isFallbackVersion(boolean z) {
        this.selectionConfig.isFallbackVersion = z;
        return this;
    }

    public void forResult(int i) {
        Activity activity;
        int i2;
        if (DoubleUtils.isFastDoubleClick() || (activity = this.selector.getActivity()) == null || this.selectionConfig == null) {
            return;
        }
        Intent intent = new Intent(activity, this.selectionConfig.camera ? PictureSelectorCameraEmptyActivity.class : PictureSelectorActivity.class);
        Fragment fragment = this.selector.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, i);
        } else {
            activity.startActivityForResult(intent, i);
        }
        PictureWindowAnimationStyle pictureWindowAnimationStyle = this.selectionConfig.windowAnimationStyle;
        if (pictureWindowAnimationStyle != null && pictureWindowAnimationStyle.activityEnterAnimation != 0) {
            i2 = pictureWindowAnimationStyle.activityEnterAnimation;
        } else {
            i2 = R.anim.picture_anim_enter;
        }
        activity.overridePendingTransition(i2, R.anim.picture_anim_fade_in);
    }

    @Deprecated
    public void forResult(int i, int i2, int i3) {
        Activity activity;
        if (DoubleUtils.isFastDoubleClick() || (activity = this.selector.getActivity()) == null) {
            return;
        }
        PictureSelectionConfig pictureSelectionConfig = this.selectionConfig;
        Intent intent = new Intent(activity, (pictureSelectionConfig == null || !pictureSelectionConfig.camera) ? PictureSelectorActivity.class : PictureSelectorCameraEmptyActivity.class);
        Fragment fragment = this.selector.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, i);
        } else {
            activity.startActivityForResult(intent, i);
        }
        activity.overridePendingTransition(i2, i3);
    }

    public void openExternalPreview(int i, List<LocalMedia> list) {
        PictureSelector pictureSelector = this.selector;
        Objects.requireNonNull(pictureSelector, "This PictureSelector is Null");
        pictureSelector.externalPicturePreview(i, list, (this.selectionConfig.windowAnimationStyle == null || this.selectionConfig.windowAnimationStyle.activityPreviewEnterAnimation == 0) ? 0 : this.selectionConfig.windowAnimationStyle.activityPreviewEnterAnimation);
    }

    public void openExternalPreview(int i, String str, List<LocalMedia> list) {
        PictureSelector pictureSelector = this.selector;
        Objects.requireNonNull(pictureSelector, "This PictureSelector is Null");
        pictureSelector.externalPicturePreview(i, str, list, (this.selectionConfig.windowAnimationStyle == null || this.selectionConfig.windowAnimationStyle.activityPreviewEnterAnimation == 0) ? 0 : this.selectionConfig.windowAnimationStyle.activityPreviewEnterAnimation);
    }
}
