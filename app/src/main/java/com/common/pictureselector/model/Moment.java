package com.common.pictureselector.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class Moment implements Parcelable {
    public static final Parcelable.Creator<Moment> CREATOR = new Parcelable.Creator<Moment>() { // from class: com.common.pictureselector.model.Moment.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Moment createFromParcel(Parcel parcel) {
            return new Moment(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Moment[] newArray(int i) {
            return new Moment[i];
        }
    };
    public String content;
    public ArrayList<String> photos;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Moment(String str, ArrayList<String> arrayList) {
        this.content = str;
        this.photos = arrayList;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public ArrayList<String> getPhotos() {
        return this.photos;
    }

    public void setPhotos(ArrayList<String> arrayList) {
        this.photos = arrayList;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.content);
        parcel.writeStringList(this.photos);
    }

    public Moment() {
    }

    protected Moment(Parcel parcel) {
        this.content = parcel.readString();
        this.photos = parcel.createStringArrayList();
    }
}
