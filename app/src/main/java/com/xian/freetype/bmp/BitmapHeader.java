package com.xian.freetype.bmp;

/* loaded from: classes.dex */
public class BitmapHeader {
    short bitPerPixel;
    int bytePerPixel;
    int clrImportant;
    int clrUsed;
    int compressed;
    int dibSize;
    int fileSize;
    int height;
    int pixelSize;
    int pixelsOffset;
    short planes;
    short reserved1;
    short reserved2;
    int width;
    int xPixelsPerMeter;
    int yPixelsPerMeter;

    public String getString() {
        StringBuffer stringBuffer = new StringBuffer("fileSize:0x");
        stringBuffer.append(Integer.toHexString(this.fileSize));
        stringBuffer.append("\nreserved1:0x");
        stringBuffer.append(Integer.toHexString(this.reserved1));
        stringBuffer.append("\nreserved2:0x");
        stringBuffer.append(Integer.toHexString(this.reserved2));
        stringBuffer.append("\npixelsOffset:0x");
        stringBuffer.append(Integer.toHexString(this.pixelsOffset));
        stringBuffer.append("\ndibSize:0x");
        stringBuffer.append(Integer.toHexString(this.dibSize));
        stringBuffer.append("\nwidth:0x");
        stringBuffer.append(Integer.toHexString(this.width));
        stringBuffer.append("\nheight:0x");
        stringBuffer.append(Integer.toHexString(this.height));
        stringBuffer.append("\nplanes:0x");
        stringBuffer.append(Integer.toHexString(this.planes));
        stringBuffer.append("\nbitPerPixel:0x");
        stringBuffer.append(Integer.toHexString(this.bitPerPixel));
        stringBuffer.append("\ncompressed:0x");
        stringBuffer.append(Integer.toHexString(this.compressed));
        stringBuffer.append("\npixelSize:0x");
        stringBuffer.append(Integer.toHexString(this.pixelSize));
        stringBuffer.append("\nxPixelsPerMeter:0x");
        stringBuffer.append(Integer.toHexString(this.xPixelsPerMeter));
        stringBuffer.append("\nyPixelsPerMeter:0x");
        stringBuffer.append(Integer.toHexString(this.yPixelsPerMeter));
        stringBuffer.append("\nclrUsed:0x");
        stringBuffer.append(Integer.toHexString(this.clrUsed));
        stringBuffer.append("\nclrImportant:0x");
        stringBuffer.append(Integer.toHexString(this.clrImportant));
        return stringBuffer.toString();
    }
}
