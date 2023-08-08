package com.luck.picture.lib.style;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class PictureCropParameterStyle implements Parcelable {
    public static final Parcelable.Creator<PictureCropParameterStyle> CREATOR = new Parcelable.Creator<PictureCropParameterStyle>() { // from class: com.luck.picture.lib.style.PictureCropParameterStyle.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureCropParameterStyle createFromParcel(Parcel parcel) {
            return new PictureCropParameterStyle(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureCropParameterStyle[] newArray(int i) {
            return new PictureCropParameterStyle[i];
        }
    };
    public int cropNavBarColor;
    public int cropStatusBarColorPrimaryDark;
    public int cropTitleBarBackgroundColor;
    public int cropTitleColor;
    public boolean isChangeStatusBarFontColor;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PictureCropParameterStyle() {
    }

    public PictureCropParameterStyle(int i, int i2, int i3, boolean z) {
        this.cropTitleBarBackgroundColor = i;
        this.cropStatusBarColorPrimaryDark = i2;
        this.cropTitleColor = i3;
        this.isChangeStatusBarFontColor = z;
    }

    public PictureCropParameterStyle(int i, int i2, int i3, int i4, boolean z) {
        this.cropTitleBarBackgroundColor = i;
        this.cropNavBarColor = i3;
        this.cropStatusBarColorPrimaryDark = i2;
        this.cropTitleColor = i4;
        this.isChangeStatusBarFontColor = z;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.isChangeStatusBarFontColor ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.cropTitleBarBackgroundColor);
        parcel.writeInt(this.cropStatusBarColorPrimaryDark);
        parcel.writeInt(this.cropTitleColor);
        parcel.writeInt(this.cropNavBarColor);
    }

    protected PictureCropParameterStyle(Parcel parcel) {
        this.isChangeStatusBarFontColor = parcel.readByte() != 0;
        this.cropTitleBarBackgroundColor = parcel.readInt();
        this.cropStatusBarColorPrimaryDark = parcel.readInt();
        this.cropTitleColor = parcel.readInt();
        this.cropNavBarColor = parcel.readInt();
    }
}
