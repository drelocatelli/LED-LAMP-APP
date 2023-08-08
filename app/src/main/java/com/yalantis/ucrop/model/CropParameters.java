package com.yalantis.ucrop.model;

import android.graphics.Bitmap;
import android.net.Uri;

/* loaded from: classes.dex */
public class CropParameters {
    private Bitmap.CompressFormat mCompressFormat;
    private int mCompressQuality;
    private ExifInfo mExifInfo;
    private Uri mImageInputUri;
    private String mImageOutputPath;
    private int mMaxResultImageSizeX;
    private int mMaxResultImageSizeY;

    public CropParameters(int i, int i2, Bitmap.CompressFormat compressFormat, int i3, Uri uri, String str, ExifInfo exifInfo) {
        this.mMaxResultImageSizeX = i;
        this.mMaxResultImageSizeY = i2;
        this.mCompressFormat = compressFormat;
        this.mCompressQuality = i3;
        this.mImageInputUri = uri;
        this.mImageOutputPath = str;
        this.mExifInfo = exifInfo;
    }

    public int getMaxResultImageSizeX() {
        return this.mMaxResultImageSizeX;
    }

    public int getMaxResultImageSizeY() {
        return this.mMaxResultImageSizeY;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return this.mCompressFormat;
    }

    public int getCompressQuality() {
        return this.mCompressQuality;
    }

    public Uri getImageInputUri() {
        return this.mImageInputUri;
    }

    public String getImageOutputPath() {
        return this.mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return this.mExifInfo;
    }
}
