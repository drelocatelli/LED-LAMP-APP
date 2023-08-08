package com.xian.freetype.bmp;

import android.content.Context;

/* loaded from: classes.dex */
public class BitmapFile {
    Context context;
    byte[] data;
    BitmapHeader dibHeader;
    String fileName;
    byte[] outPixels;
    byte[] pixels;
    byte[] type;

    public BitmapFile() {
        this.type = r0;
        byte[] bArr = {66, 77};
    }

    public BitmapFile(Context context) {
        this.context = context;
        this.dibHeader = new BitmapHeader();
    }

    public String getPixelsHex() {
        byte[] bArr = this.pixels;
        if (bArr == null) {
            return null;
        }
        return ByteUtils.bytesToHexString(bArr);
    }

    public String getDatasHex() {
        byte[] bArr = this.data;
        if (bArr == null) {
            return null;
        }
        return ByteUtils.bytesToHexString(bArr);
    }

    public byte[] getPixels() {
        return this.pixels;
    }

    public byte[] getDatas() {
        return this.data;
    }

    public int setPixels(byte[] bArr) {
        this.pixels = bArr;
        return bArr.length;
    }

    public int setOutPixels(byte[] bArr) {
        this.outPixels = bArr;
        return bArr.length;
    }

    public boolean setDibHeader(BitmapHeader bitmapHeader) {
        this.dibHeader = bitmapHeader;
        return true;
    }

    public boolean inversionPixels() {
        this.outPixels = new byte[this.pixels.length];
        int i = 0;
        while (true) {
            byte[] bArr = this.pixels;
            if (i >= bArr.length) {
                return true;
            }
            this.outPixels[i] = (byte) (255 - bArr[i]);
            i++;
        }
    }

    public String getString() {
        StringBuffer stringBuffer = new StringBuffer("Bitmap Info \ntype:");
        stringBuffer.append(ByteUtils.bytesToHexString(this.type));
        stringBuffer.append("\n");
        stringBuffer.append(this.dibHeader.getString());
        stringBuffer.append("\npixels:");
        stringBuffer.append(getPixelsHex());
        return stringBuffer.toString();
    }

    public boolean readBitmapById(int i) throws Exception {
        DataReader dataReader = new DataReader();
        dataReader.readFromFile(this.context, i);
        this.type = dataReader.readType();
        this.dibHeader.fileSize = dataReader.readInt();
        this.dibHeader.reserved1 = dataReader.readShort();
        this.dibHeader.reserved2 = dataReader.readShort();
        this.dibHeader.pixelsOffset = dataReader.readInt();
        this.dibHeader.dibSize = dataReader.readInt();
        this.dibHeader.width = dataReader.readInt();
        this.dibHeader.height = dataReader.readInt();
        this.dibHeader.planes = dataReader.readShort();
        this.dibHeader.bitPerPixel = dataReader.readShort();
        this.dibHeader.compressed = dataReader.readInt();
        this.dibHeader.pixelSize = dataReader.readInt();
        this.dibHeader.xPixelsPerMeter = dataReader.readInt();
        this.dibHeader.yPixelsPerMeter = dataReader.readInt();
        this.dibHeader.clrUsed = dataReader.readInt();
        this.dibHeader.clrImportant = dataReader.readInt();
        this.dibHeader.bytePerPixel = dataReader.readInt();
        this.data = dataReader.readBytesFrom(0);
        this.pixels = dataReader.readBytesFrom(this.dibHeader.pixelsOffset);
        return true;
    }

    public boolean readBitmapByPath(String str) throws Exception {
        DataReader dataReader = new DataReader();
        dataReader.readFromFile(str);
        this.fileName = str;
        this.type = dataReader.readType();
        this.dibHeader.fileSize = dataReader.readInt();
        this.dibHeader.reserved1 = dataReader.readShort();
        this.dibHeader.reserved2 = dataReader.readShort();
        this.dibHeader.pixelsOffset = dataReader.readInt();
        this.dibHeader.dibSize = dataReader.readInt();
        this.dibHeader.width = dataReader.readInt();
        this.dibHeader.height = dataReader.readInt();
        this.dibHeader.planes = dataReader.readShort();
        this.dibHeader.bitPerPixel = dataReader.readShort();
        this.dibHeader.compressed = dataReader.readInt();
        this.dibHeader.pixelSize = dataReader.readInt();
        this.dibHeader.xPixelsPerMeter = dataReader.readInt();
        this.dibHeader.yPixelsPerMeter = dataReader.readInt();
        this.dibHeader.clrUsed = dataReader.readInt();
        this.dibHeader.clrImportant = dataReader.readInt();
        this.dibHeader.bytePerPixel = dataReader.readInt();
        this.data = dataReader.readBytesFrom(0);
        this.pixels = dataReader.readBytesFrom(this.dibHeader.pixelsOffset);
        return true;
    }

    public byte[] saveData() throws Exception {
        DataWriter dataWriter = new DataWriter(this.dibHeader.fileSize);
        dataWriter.writeBytes(this.type, 2);
        dataWriter.writeInt(this.dibHeader.fileSize);
        dataWriter.writeShort(this.dibHeader.reserved1);
        dataWriter.writeShort(this.dibHeader.reserved2);
        dataWriter.writeInt(this.dibHeader.pixelsOffset);
        dataWriter.writeInt(this.dibHeader.dibSize);
        dataWriter.writeInt(this.dibHeader.width);
        dataWriter.writeInt(this.dibHeader.height);
        dataWriter.writeShort(this.dibHeader.planes);
        dataWriter.writeShort(this.dibHeader.bitPerPixel);
        dataWriter.writeInt(this.dibHeader.compressed);
        dataWriter.writeInt(this.dibHeader.pixelSize);
        dataWriter.writeInt(this.dibHeader.xPixelsPerMeter);
        dataWriter.writeInt(this.dibHeader.yPixelsPerMeter);
        dataWriter.writeInt(this.dibHeader.clrUsed);
        dataWriter.writeInt(this.dibHeader.clrImportant);
        dataWriter.writeInt(0);
        dataWriter.writeBytes(new byte[]{-1, -1, -1, 0}, 4);
        byte[] bArr = this.outPixels;
        dataWriter.writeBytes(bArr, bArr.length);
        return dataWriter.buf;
    }

    public int saveBitmap(String str) throws Exception {
        DataWriter dataWriter = new DataWriter(this.dibHeader.fileSize);
        dataWriter.writeBytes(this.type, 2);
        dataWriter.writeInt(this.dibHeader.fileSize);
        dataWriter.writeShort(this.dibHeader.reserved1);
        dataWriter.writeShort(this.dibHeader.reserved2);
        dataWriter.writeInt(this.dibHeader.pixelsOffset);
        dataWriter.writeInt(this.dibHeader.dibSize);
        dataWriter.writeInt(this.dibHeader.width);
        dataWriter.writeInt(this.dibHeader.height);
        dataWriter.writeShort(this.dibHeader.planes);
        dataWriter.writeShort(this.dibHeader.bitPerPixel);
        dataWriter.writeInt(this.dibHeader.compressed);
        dataWriter.writeInt(this.dibHeader.pixelSize);
        dataWriter.writeInt(this.dibHeader.xPixelsPerMeter);
        dataWriter.writeInt(this.dibHeader.yPixelsPerMeter);
        dataWriter.writeInt(this.dibHeader.clrUsed);
        dataWriter.writeInt(this.dibHeader.clrImportant);
        dataWriter.writeInt(0);
        dataWriter.writeBytes(new byte[]{-1, -1, -1, 0}, 4);
        byte[] bArr = this.outPixels;
        dataWriter.writeBytes(bArr, bArr.length);
        dataWriter.writeToFile(str);
        return this.dibHeader.fileSize;
    }
}
