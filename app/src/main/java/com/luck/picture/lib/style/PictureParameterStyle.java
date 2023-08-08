package com.luck.picture.lib.style;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class PictureParameterStyle implements Parcelable {
    public static final Parcelable.Creator<PictureParameterStyle> CREATOR = new Parcelable.Creator<PictureParameterStyle>() { // from class: com.luck.picture.lib.style.PictureParameterStyle.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureParameterStyle createFromParcel(Parcel parcel) {
            return new PictureParameterStyle(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureParameterStyle[] newArray(int i) {
            return new PictureParameterStyle[i];
        }
    };
    public boolean isChangeStatusBarFontColor;
    public boolean isOpenCheckNumStyle;
    public boolean isOpenCompletedNumStyle;
    public int pictureBottomBgColor;
    public int pictureCancelTextColor;
    public int pictureCheckNumBgStyle;
    public int pictureCheckedStyle;
    public int pictureCompleteTextColor;
    public int pictureExternalPreviewDeleteStyle;
    public boolean pictureExternalPreviewGonePreviewDelete;
    public int pictureFolderCheckedDotStyle;
    public int pictureLeftBackIcon;
    public int pictureNavBarColor;
    public int picturePreviewBottomBgColor;
    public int picturePreviewTextColor;
    public int pictureStatusBarColor;
    public int pictureTitleBarBackgroundColor;
    public int pictureTitleDownResId;
    public int pictureTitleTextColor;
    public int pictureTitleUpResId;
    public int pictureUnCompleteTextColor;
    public int pictureUnPreviewTextColor;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PictureParameterStyle() {
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.isChangeStatusBarFontColor ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isOpenCompletedNumStyle ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.isOpenCheckNumStyle ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.pictureStatusBarColor);
        parcel.writeInt(this.pictureTitleBarBackgroundColor);
        parcel.writeInt(this.pictureTitleTextColor);
        parcel.writeInt(this.pictureCancelTextColor);
        parcel.writeInt(this.pictureBottomBgColor);
        parcel.writeInt(this.pictureCompleteTextColor);
        parcel.writeInt(this.pictureUnCompleteTextColor);
        parcel.writeInt(this.pictureUnPreviewTextColor);
        parcel.writeInt(this.picturePreviewTextColor);
        parcel.writeInt(this.picturePreviewBottomBgColor);
        parcel.writeInt(this.pictureNavBarColor);
        parcel.writeInt(this.pictureTitleUpResId);
        parcel.writeInt(this.pictureTitleDownResId);
        parcel.writeInt(this.pictureLeftBackIcon);
        parcel.writeInt(this.pictureCheckedStyle);
        parcel.writeInt(this.pictureCheckNumBgStyle);
        parcel.writeInt(this.pictureFolderCheckedDotStyle);
        parcel.writeInt(this.pictureExternalPreviewDeleteStyle);
        parcel.writeByte(this.pictureExternalPreviewGonePreviewDelete ? (byte) 1 : (byte) 0);
    }

    protected PictureParameterStyle(Parcel parcel) {
        this.isChangeStatusBarFontColor = parcel.readByte() != 0;
        this.isOpenCompletedNumStyle = parcel.readByte() != 0;
        this.isOpenCheckNumStyle = parcel.readByte() != 0;
        this.pictureStatusBarColor = parcel.readInt();
        this.pictureTitleBarBackgroundColor = parcel.readInt();
        this.pictureTitleTextColor = parcel.readInt();
        this.pictureCancelTextColor = parcel.readInt();
        this.pictureBottomBgColor = parcel.readInt();
        this.pictureCompleteTextColor = parcel.readInt();
        this.pictureUnCompleteTextColor = parcel.readInt();
        this.pictureUnPreviewTextColor = parcel.readInt();
        this.picturePreviewTextColor = parcel.readInt();
        this.picturePreviewBottomBgColor = parcel.readInt();
        this.pictureNavBarColor = parcel.readInt();
        this.pictureTitleUpResId = parcel.readInt();
        this.pictureTitleDownResId = parcel.readInt();
        this.pictureLeftBackIcon = parcel.readInt();
        this.pictureCheckedStyle = parcel.readInt();
        this.pictureCheckNumBgStyle = parcel.readInt();
        this.pictureFolderCheckedDotStyle = parcel.readInt();
        this.pictureExternalPreviewDeleteStyle = parcel.readInt();
        this.pictureExternalPreviewGonePreviewDelete = parcel.readByte() != 0;
    }
}
