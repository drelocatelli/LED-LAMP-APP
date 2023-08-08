package com.xian.freetype.bmp;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class DataReader {
    private static String TAG = "BitmapFile";
    protected int mark = 0;
    protected byte[] buf = null;
    protected int pos = 0;
    protected int count = 0;

    public int readFromFile(Context context, int i) {
        try {
            try {
                InputStream openRawResource = context.getResources().openRawResource(i);
                byte[] bArr = new byte[openRawResource.available()];
                this.buf = bArr;
                openRawResource.read(bArr);
                openRawResource.close();
                byte[] bArr2 = this.buf;
                this.count = bArr2.length;
                String bytesToHexString = ByteUtils.bytesToHexString(bArr2);
                String str = TAG;
                Log.d(str, "readSaveFile HEX:" + bytesToHexString);
                return this.count;
            } catch (Exception e) {
                String str2 = TAG;
                Log.e(str2, "readFromFile fail " + e.toString());
                return this.count;
            }
        } catch (Throwable unused) {
            return this.count;
        }
    }

    public int readFromFile(String str) {
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(str));
                byte[] bArr = new byte[fileInputStream.available()];
                this.buf = bArr;
                fileInputStream.read(bArr);
                fileInputStream.close();
                byte[] bArr2 = this.buf;
                this.count = bArr2.length;
                String bytesToHexString = ByteUtils.bytesToHexString(bArr2);
                String str2 = TAG;
                Log.d(str2, "readSaveFile HEX:" + bytesToHexString);
                return this.count;
            } catch (Exception e) {
                String str3 = TAG;
                Log.d(str3, "readFromFile fail " + e.toString());
                return this.count;
            }
        } catch (Throwable unused) {
            return this.count;
        }
    }

    public int readInt() throws Exception {
        if (this.count < this.pos + 4) {
            throw new Exception("can not read int value at " + this.pos);
        }
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            i += this.buf[this.pos + i2] << (i2 * 8);
        }
        this.pos += 4;
        return i;
    }

    public short readShort() throws Exception {
        if (this.count < this.pos + 2) {
            throw new Exception("can not read short value at " + this.pos);
        }
        short s = 0;
        for (int i = 0; i < 2; i++) {
            s = (short) (s + (this.buf[this.pos + i] << (i * 8)));
        }
        this.pos += 2;
        return s;
    }

    public byte[] readType() throws Exception {
        byte[] bArr = new byte[2];
        if (this.count < this.pos + 2) {
            throw new Exception("can not read char value at " + this.pos);
        }
        for (int i = 0; i < 2; i++) {
            byte[] bArr2 = this.buf;
            int i2 = this.pos;
            this.pos = i2 + 1;
            bArr[i] = bArr2[i2];
        }
        return bArr;
    }

    public byte[] readBytesFrom(int i) {
        int i2 = this.count;
        if (i >= i2) {
            return null;
        }
        int i3 = i2 - i;
        byte[] bArr = new byte[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[i4] = this.buf[i + i4];
        }
        return bArr;
    }
}
