package com.xian.freetype.bmp;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;

/* loaded from: classes.dex */
public class DataWriter {
    public byte[] buf;
    protected int count;
    private String TAG = "DataWriter";
    protected int mark = 0;
    protected int pos = 0;

    public DataWriter(int i) {
        this.count = i;
        this.buf = new byte[i];
    }

    public int writeToFile(String str) {
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
                fileOutputStream.write(this.buf, 0, this.count);
                fileOutputStream.flush();
                fileOutputStream.close();
                String str2 = this.TAG;
                Log.d(str2, "writeToFile " + str);
                return this.count;
            } catch (Exception e) {
                String str3 = this.TAG;
                Log.d(str3, "writeToFile fail " + e.toString());
                return this.count;
            }
        } catch (Throwable unused) {
            return this.count;
        }
    }

    public int writeBytes(byte[] bArr, int i) throws Exception {
        if (this.count < this.pos + bArr.length) {
            throw new Exception("can not write bytes value at " + this.pos);
        }
        for (byte b : bArr) {
            byte[] bArr2 = this.buf;
            int i2 = this.pos;
            this.pos = i2 + 1;
            bArr2[i2] = b;
        }
        for (int i3 = 0; i3 < i - bArr.length; i3++) {
            byte[] bArr3 = this.buf;
            int i4 = this.pos;
            this.pos = i4 + 1;
            bArr3[i4] = 0;
        }
        return this.pos;
    }

    public int writeInt(int i) throws Exception {
        if (this.count < this.pos + 4) {
            throw new Exception("can not write int value at " + this.pos);
        }
        byte[] int2Byte = ByteUtils.int2Byte(i);
        String str = this.TAG;
        Log.d(str, "writeInt " + i + "  [" + ((int) int2Byte[0]) + " " + ((int) int2Byte[1]) + " " + ((int) int2Byte[2]) + " " + ((int) int2Byte[3]) + "]");
        writeBytes(int2Byte, 4);
        return this.pos;
    }

    public int writeShort(short s) throws Exception {
        if (this.count < this.pos + 2) {
            throw new Exception("can not write short value at " + this.pos);
        }
        byte[] short2Byte = ByteUtils.short2Byte(s);
        String str = this.TAG;
        Log.d(str, "writeShort " + ((int) s) + "  [" + ((int) short2Byte[0]) + " " + ((int) short2Byte[1]) + "]");
        writeBytes(short2Byte, 2);
        return this.pos;
    }
}
