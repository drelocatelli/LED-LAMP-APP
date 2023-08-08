package com.ccr.achenglibrary.photopicker.model;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class CCRImageFolderModel {
    public String coverPath;
    private ArrayList<String> mImages;
    private boolean mTakePhotoEnabled;
    public String name;

    public CCRImageFolderModel(boolean z) {
        ArrayList<String> arrayList = new ArrayList<>();
        this.mImages = arrayList;
        this.mTakePhotoEnabled = z;
        if (z) {
            arrayList.add("");
        }
    }

    public CCRImageFolderModel(String str, String str2) {
        this.mImages = new ArrayList<>();
        this.name = str;
        this.coverPath = str2;
    }

    public boolean isTakePhotoEnabled() {
        return this.mTakePhotoEnabled;
    }

    public void addLastImage(String str) {
        this.mImages.add(str);
    }

    public ArrayList<String> getImages() {
        return this.mImages;
    }

    public int getCount() {
        return this.mTakePhotoEnabled ? this.mImages.size() - 1 : this.mImages.size();
    }
}
