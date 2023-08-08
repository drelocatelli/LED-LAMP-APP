package com.xian.freetype.bmp;

import android.util.Log;

/* loaded from: classes.dex */
public class ByteUtils {
    private static String TAG = "ByteUtils";

    public static byte[] short2Byte(short s) {
        byte[] bArr = new byte[2];
        int i = 0;
        int i2 = s;
        while (i < 2) {
            bArr[i] = new Integer(i2 & 255).byteValue();
            i++;
            i2 >>= 8;
        }
        return bArr;
    }

    public static byte[] int2Byte(int i) {
        int numberOfLeadingZeros = (40 - Integer.numberOfLeadingZeros(i < 0 ? i ^ (-1) : i)) / 8;
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < numberOfLeadingZeros; i2++) {
            bArr[i2] = (byte) (i >>> (i2 * 8));
        }
        String str = TAG;
        Log.d(str, "int2Byte integer " + i + " byteNum " + numberOfLeadingZeros + " [" + ((int) bArr[0]) + " " + ((int) bArr[1]) + " " + ((int) bArr[2]) + " " + ((int) bArr[3]) + "]");
        return bArr;
    }

    public static short char2short(char[] cArr) {
        if (cArr.length > 2) {
            String str = TAG;
            Log.e(str, "char2short fail too many elment [" + cArr[0] + " " + cArr[1] + " " + cArr[3] + "]");
        }
        short s = 0;
        for (int i = 0; i < cArr.length; i++) {
            s = (short) (s + ((short) (((short) cArr[i]) << (i * 8))));
        }
        return s;
    }

    public static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static byte[] bytesToByte(byte[][] bArr) {
        byte[] bArr2 = new byte[32];
        byte[] bArr3 = {1, 2, 4, 8, 16, 32, 64, Byte.MIN_VALUE};
        for (int i = 0; i < 16; i++) {
            int i2 = 0;
            byte b = 0;
            for (int i3 = 0; i3 < 16; i3++) {
                i2++;
                if (bArr[i3][i] == 1) {
                    b = (byte) (b | bArr3[i3 % 8]);
                }
                if (i2 % 8 == 0) {
                    if (i2 > 8) {
                        bArr2[i + 16] = b;
                    } else {
                        bArr2[i] = b;
                    }
                    b = 0;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i4 = 0; i4 < 32; i4++) {
            sb.append(Integer.toHexString(bArr2[i4]) + " ,");
            if (i4 == 15) {
                sb.append("\n");
            }
        }
        String str = TAG;
        Log.i(str, "结果：" + sb.toString());
        return bArr2;
    }
}
