package com.luck.picture.lib.style;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class PictureWindowAnimationStyle implements Parcelable {
    public static final Parcelable.Creator<PictureWindowAnimationStyle> CREATOR = new Parcelable.Creator<PictureWindowAnimationStyle>() { // from class: com.luck.picture.lib.style.PictureWindowAnimationStyle.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureWindowAnimationStyle createFromParcel(Parcel parcel) {
            return new PictureWindowAnimationStyle(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PictureWindowAnimationStyle[] newArray(int i) {
            return new PictureWindowAnimationStyle[i];
        }
    };
    public int activityCropEnterAnimation;
    public int activityCropExitAnimation;
    public int activityEnterAnimation;
    public int activityExitAnimation;
    public int activityPreviewEnterAnimation;
    public int activityPreviewExitAnimation;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PictureWindowAnimationStyle() {
    }

    public PictureWindowAnimationStyle(int i, int i2) {
        this.activityEnterAnimation = i;
        this.activityExitAnimation = i2;
    }

    public PictureWindowAnimationStyle(int i, int i2, int i3, int i4) {
        this.activityEnterAnimation = i;
        this.activityExitAnimation = i2;
        this.activityPreviewEnterAnimation = i3;
        this.activityPreviewExitAnimation = i4;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.activityEnterAnimation);
        parcel.writeInt(this.activityExitAnimation);
        parcel.writeInt(this.activityPreviewEnterAnimation);
        parcel.writeInt(this.activityPreviewExitAnimation);
        parcel.writeInt(this.activityCropEnterAnimation);
        parcel.writeInt(this.activityCropExitAnimation);
    }

    protected PictureWindowAnimationStyle(Parcel parcel) {
        this.activityEnterAnimation = parcel.readInt();
        this.activityExitAnimation = parcel.readInt();
        this.activityPreviewEnterAnimation = parcel.readInt();
        this.activityPreviewExitAnimation = parcel.readInt();
        this.activityCropEnterAnimation = parcel.readInt();
        this.activityCropExitAnimation = parcel.readInt();
    }
}
