package com.luck.picture.lib.config;

import android.os.Parcel;
import android.os.Parcelable;
import com.luck.picture.lib.R;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class PictureSelectionConfig implements Parcelable {
    public static final Parcelable.Creator<PictureSelectionConfig> CREATOR = new Parcelable.Creator<PictureSelectionConfig>() { // from class: com.luck.picture.lib.config.PictureSelectionConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureSelectionConfig createFromParcel(Parcel parcel) {
            return new PictureSelectionConfig(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureSelectionConfig[] newArray(int i) {
            return new PictureSelectionConfig[i];
        }
    };
    public int aspect_ratio_x;
    public int aspect_ratio_y;
    public boolean camera;
    public String cameraFileName;
    public boolean checkNumMode;
    public int chooseMode;
    public boolean circleDimmedLayer;
    public int compressQuality;
    public String compressSavePath;
    public int cropCompressQuality;
    public int cropHeight;
    @Deprecated
    public int cropStatusBarColorPrimaryDark;
    public PictureCropParameterStyle cropStyle;
    @Deprecated
    public int cropTitleBarBackgroundColor;
    @Deprecated
    public int cropTitleColor;
    public int cropWidth;
    @Deprecated
    public int downResId;
    public boolean enPreviewVideo;
    public boolean enableCrop;
    public boolean enablePreview;
    public boolean enablePreviewAudio;
    public int filterFileSize;
    public boolean freeStyleCropEnabled;
    public boolean hideBottomControls;
    public ImageEngine imageEngine;
    public int imageSpanCount;
    public boolean isCamera;
    @Deprecated
    public boolean isChangeStatusBarFontColor;
    public boolean isCompress;
    public boolean isDragFrame;
    public boolean isFallbackVersion;
    public boolean isGif;
    public boolean isNotPreviewDownload;
    @Deprecated
    public boolean isOpenStyleCheckNumMode;
    @Deprecated
    public boolean isOpenStyleNumComplete;
    public boolean isSingleDirectReturn;
    public int language;
    public int maxSelectNum;
    public int minSelectNum;
    public int minimumCompressSize;
    public boolean openClickSound;
    @Deprecated
    public String outputCameraPath;
    @Deprecated
    public int overrideHeight;
    @Deprecated
    public int overrideWidth;
    @Deprecated
    public int pictureStatusBarColor;
    public boolean previewEggs;
    public int recordVideoSecond;
    public boolean rotateEnabled;
    public boolean scaleEnabled;
    public List<LocalMedia> selectionMedias;
    public int selectionMode;
    public boolean showCropFrame;
    public boolean showCropGrid;
    @Deprecated
    public float sizeMultiplier;
    public String specifiedFormat;
    public PictureParameterStyle style;
    public String suffixType;
    public boolean synOrAsy;
    public int themeStyleId;
    @Deprecated
    public int titleBarBackgroundColor;
    @Deprecated
    public int upResId;
    public int videoMaxSecond;
    public int videoMinSecond;
    public int videoQuality;
    public PictureWindowAnimationStyle windowAnimationStyle;
    public boolean zoomAnim;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    private void reset() {
        this.chooseMode = PictureMimeType.ofImage();
        this.camera = false;
        this.themeStyleId = R.style.picture_default_style;
        this.selectionMode = 2;
        this.maxSelectNum = 9;
        this.minSelectNum = 0;
        this.videoQuality = 1;
        this.language = -1;
        this.cropCompressQuality = 90;
        this.videoMaxSecond = 0;
        this.videoMinSecond = 0;
        this.filterFileSize = -1;
        this.recordVideoSecond = 60;
        this.compressQuality = 60;
        this.minimumCompressSize = 100;
        this.imageSpanCount = 4;
        this.isCompress = false;
        this.aspect_ratio_x = 0;
        this.aspect_ratio_y = 0;
        this.cropWidth = 0;
        this.cropHeight = 0;
        this.isCamera = true;
        this.isGif = false;
        this.isSingleDirectReturn = false;
        this.enablePreview = true;
        this.enPreviewVideo = true;
        this.enablePreviewAudio = true;
        this.checkNumMode = false;
        this.isNotPreviewDownload = false;
        this.openClickSound = false;
        this.isFallbackVersion = false;
        this.enableCrop = false;
        this.freeStyleCropEnabled = false;
        this.circleDimmedLayer = false;
        this.showCropFrame = true;
        this.showCropGrid = true;
        this.hideBottomControls = true;
        this.rotateEnabled = true;
        this.scaleEnabled = true;
        this.previewEggs = false;
        this.synOrAsy = true;
        this.zoomAnim = true;
        this.isDragFrame = true;
        this.compressSavePath = "";
        this.suffixType = PictureFileUtils.POSTFIX;
        this.cameraFileName = "";
        this.specifiedFormat = "";
        this.selectionMedias = new ArrayList();
        this.imageEngine = null;
        this.style = null;
        this.cropStyle = null;
        this.windowAnimationStyle = null;
        this.titleBarBackgroundColor = 0;
        this.pictureStatusBarColor = 0;
        this.cropTitleBarBackgroundColor = 0;
        this.cropStatusBarColorPrimaryDark = 0;
        this.cropTitleColor = 0;
        this.upResId = 0;
        this.downResId = 0;
        this.isChangeStatusBarFontColor = false;
        this.isOpenStyleNumComplete = false;
        this.isOpenStyleCheckNumMode = false;
        this.outputCameraPath = "";
        this.sizeMultiplier = 0.5f;
        this.overrideWidth = 0;
        this.overrideHeight = 0;
    }

    public static PictureSelectionConfig getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public static PictureSelectionConfig getCleanInstance() {
        PictureSelectionConfig pictureSelectionConfig = getInstance();
        pictureSelectionConfig.reset();
        return pictureSelectionConfig;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        private static final PictureSelectionConfig INSTANCE = new PictureSelectionConfig();

        private InstanceHolder() {
        }
    }

    public PictureSelectionConfig() {
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.chooseMode);
        parcel.writeByte(this.camera ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isSingleDirectReturn ? (byte) 1 : (byte) 0);
        parcel.writeParcelable(this.style, i);
        parcel.writeParcelable(this.cropStyle, i);
        parcel.writeParcelable(this.windowAnimationStyle, i);
        parcel.writeString(this.compressSavePath);
        parcel.writeString(this.suffixType);
        parcel.writeString(this.cameraFileName);
        parcel.writeString(this.specifiedFormat);
        parcel.writeInt(this.themeStyleId);
        parcel.writeInt(this.selectionMode);
        parcel.writeInt(this.maxSelectNum);
        parcel.writeInt(this.minSelectNum);
        parcel.writeInt(this.videoQuality);
        parcel.writeInt(this.cropCompressQuality);
        parcel.writeInt(this.videoMaxSecond);
        parcel.writeInt(this.videoMinSecond);
        parcel.writeInt(this.recordVideoSecond);
        parcel.writeInt(this.minimumCompressSize);
        parcel.writeInt(this.imageSpanCount);
        parcel.writeInt(this.aspect_ratio_x);
        parcel.writeInt(this.aspect_ratio_y);
        parcel.writeInt(this.cropWidth);
        parcel.writeInt(this.cropHeight);
        parcel.writeInt(this.compressQuality);
        parcel.writeInt(this.filterFileSize);
        parcel.writeInt(this.language);
        parcel.writeByte(this.zoomAnim ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isCompress ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isCamera ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isGif ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.enablePreview ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.enPreviewVideo ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.enablePreviewAudio ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.checkNumMode ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.openClickSound ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.enableCrop ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.freeStyleCropEnabled ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.circleDimmedLayer ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.showCropFrame ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.showCropGrid ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.hideBottomControls ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.rotateEnabled ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.scaleEnabled ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.previewEggs ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.synOrAsy ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isDragFrame ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isNotPreviewDownload ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isFallbackVersion ? (byte) 1 : (byte) 0);
        parcel.writeTypedList(this.selectionMedias);
        parcel.writeInt(this.overrideWidth);
        parcel.writeInt(this.overrideHeight);
        parcel.writeFloat(this.sizeMultiplier);
        parcel.writeByte(this.isChangeStatusBarFontColor ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isOpenStyleNumComplete ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isOpenStyleCheckNumMode ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.titleBarBackgroundColor);
        parcel.writeInt(this.pictureStatusBarColor);
        parcel.writeInt(this.cropTitleBarBackgroundColor);
        parcel.writeInt(this.cropStatusBarColorPrimaryDark);
        parcel.writeInt(this.cropTitleColor);
        parcel.writeInt(this.upResId);
        parcel.writeInt(this.downResId);
        parcel.writeString(this.outputCameraPath);
    }

    protected PictureSelectionConfig(Parcel parcel) {
        this.chooseMode = parcel.readInt();
        this.camera = parcel.readByte() != 0;
        this.isSingleDirectReturn = parcel.readByte() != 0;
        this.style = (PictureParameterStyle) parcel.readParcelable(PictureParameterStyle.class.getClassLoader());
        this.cropStyle = (PictureCropParameterStyle) parcel.readParcelable(PictureCropParameterStyle.class.getClassLoader());
        this.windowAnimationStyle = (PictureWindowAnimationStyle) parcel.readParcelable(PictureWindowAnimationStyle.class.getClassLoader());
        this.compressSavePath = parcel.readString();
        this.suffixType = parcel.readString();
        this.cameraFileName = parcel.readString();
        this.specifiedFormat = parcel.readString();
        this.themeStyleId = parcel.readInt();
        this.selectionMode = parcel.readInt();
        this.maxSelectNum = parcel.readInt();
        this.minSelectNum = parcel.readInt();
        this.videoQuality = parcel.readInt();
        this.cropCompressQuality = parcel.readInt();
        this.videoMaxSecond = parcel.readInt();
        this.videoMinSecond = parcel.readInt();
        this.recordVideoSecond = parcel.readInt();
        this.minimumCompressSize = parcel.readInt();
        this.imageSpanCount = parcel.readInt();
        this.aspect_ratio_x = parcel.readInt();
        this.aspect_ratio_y = parcel.readInt();
        this.cropWidth = parcel.readInt();
        this.cropHeight = parcel.readInt();
        this.compressQuality = parcel.readInt();
        this.filterFileSize = parcel.readInt();
        this.language = parcel.readInt();
        this.zoomAnim = parcel.readByte() != 0;
        this.isCompress = parcel.readByte() != 0;
        this.isCamera = parcel.readByte() != 0;
        this.isGif = parcel.readByte() != 0;
        this.enablePreview = parcel.readByte() != 0;
        this.enPreviewVideo = parcel.readByte() != 0;
        this.enablePreviewAudio = parcel.readByte() != 0;
        this.checkNumMode = parcel.readByte() != 0;
        this.openClickSound = parcel.readByte() != 0;
        this.enableCrop = parcel.readByte() != 0;
        this.freeStyleCropEnabled = parcel.readByte() != 0;
        this.circleDimmedLayer = parcel.readByte() != 0;
        this.showCropFrame = parcel.readByte() != 0;
        this.showCropGrid = parcel.readByte() != 0;
        this.hideBottomControls = parcel.readByte() != 0;
        this.rotateEnabled = parcel.readByte() != 0;
        this.scaleEnabled = parcel.readByte() != 0;
        this.previewEggs = parcel.readByte() != 0;
        this.synOrAsy = parcel.readByte() != 0;
        this.isDragFrame = parcel.readByte() != 0;
        this.isNotPreviewDownload = parcel.readByte() != 0;
        this.isFallbackVersion = parcel.readByte() != 0;
        this.selectionMedias = parcel.createTypedArrayList(LocalMedia.CREATOR);
        this.overrideWidth = parcel.readInt();
        this.overrideHeight = parcel.readInt();
        this.sizeMultiplier = parcel.readFloat();
        this.isChangeStatusBarFontColor = parcel.readByte() != 0;
        this.isOpenStyleNumComplete = parcel.readByte() != 0;
        this.isOpenStyleCheckNumMode = parcel.readByte() != 0;
        this.titleBarBackgroundColor = parcel.readInt();
        this.pictureStatusBarColor = parcel.readInt();
        this.cropTitleBarBackgroundColor = parcel.readInt();
        this.cropStatusBarColorPrimaryDark = parcel.readInt();
        this.cropTitleColor = parcel.readInt();
        this.upResId = parcel.readInt();
        this.downResId = parcel.readInt();
        this.outputCameraPath = parcel.readString();
    }
}
