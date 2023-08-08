package com.yalantis.ucrop.model;

import java.io.Serializable;

/* loaded from: classes.dex */
public class CutInfo implements Serializable {
    private String androidQToPath;
    private String cutPath;
    private int imageHeight;
    private int imageWidth;
    private boolean isCut;
    private String mimeType;
    private int offsetX;
    private int offsetY;
    private String path;
    private float resultAspectRatio;

    public CutInfo() {
    }

    public CutInfo(String str, boolean z) {
        this.path = str;
        this.isCut = z;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getCutPath() {
        return this.cutPath;
    }

    public void setCutPath(String str) {
        this.cutPath = str;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(int i) {
        this.offsetX = i;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(int i) {
        this.offsetY = i;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public void setImageWidth(int i) {
        this.imageWidth = i;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public void setImageHeight(int i) {
        this.imageHeight = i;
    }

    public float getResultAspectRatio() {
        return this.resultAspectRatio;
    }

    public void setResultAspectRatio(float f) {
        this.resultAspectRatio = f;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public boolean isCut() {
        return this.isCut;
    }

    public void setCut(boolean z) {
        this.isCut = z;
    }

    public String getAndroidQToPath() {
        return this.androidQToPath;
    }

    public void setAndroidQToPath(String str) {
        this.androidQToPath = str;
    }
}
