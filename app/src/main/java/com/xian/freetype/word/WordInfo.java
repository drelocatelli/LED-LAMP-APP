package com.xian.freetype.word;

/* loaded from: classes.dex */
public class WordInfo {
    public int bitmap_left;
    public int bitmap_top;
    public byte[] buffer;
    public int pitch;
    public int rows;
    public int width;

    public int getRows() {
        return this.rows;
    }

    public void setRows(int i) {
        this.rows = i;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int i) {
        this.width = i;
    }

    public byte[] getBuffer() {
        return this.buffer;
    }

    public void setBuffer(byte[] bArr) {
        this.buffer = bArr;
    }

    public int getBitmap_left() {
        return this.bitmap_left;
    }

    public void setBitmap_left(int i) {
        this.bitmap_left = i;
    }

    public int getBitmap_top() {
        return this.bitmap_top;
    }

    public void setBitmap_top(int i) {
        this.bitmap_top = i;
    }

    public int getPitch() {
        return this.pitch;
    }

    public void setPitch(int i) {
        this.pitch = i;
    }
}
