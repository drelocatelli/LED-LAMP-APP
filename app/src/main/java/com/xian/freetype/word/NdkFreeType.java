package com.xian.freetype.word;

import android.util.Log;
import com.common.net.NetResult;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class NdkFreeType {
    public static final int HORIZONTAL = 0;
    private static String TAG = "NdkFreeType";
    public static final int VERTICAL = 1;

    public static native void FT_Destroy_FreeType();

    public static native WordInfo FT_GET_Word_Info(int i, long j);

    public static native boolean FT_Init_FreeType(String str);

    static {
        System.loadLibrary("freetype");
    }

    public static int[] FT_GET_Word_Unicode(String str) {
        int[] iArr = new int[str.length()];
        for (int i = 0; i < str.length(); i++) {
            iArr[i] = str.codePointAt(i);
            String str2 = TAG;
            Log.i(str2, "字符集： " + iArr[i]);
        }
        return iArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0129 A[LOOP:7: B:34:0x00f5->B:49:0x0129, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static byte[][] FT_GET_Word_Lattice(int i, long j) {
        float f;
        int i2;
        int i3 = j <= 128 ? i / 2 : i;
        byte b = 1;
        byte[][] bArr = (byte[][]) Array.newInstance(byte.class, i, i3);
        if (j == 32) {
            for (byte[] bArr2 : bArr) {
                for (int i4 = 0; i4 < bArr[0].length; i4++) {
                    bArr2[i4] = 1;
                }
            }
            return bArr;
        }
        WordInfo FT_GET_Word_Info = FT_GET_Word_Info(i, j);
        StringBuilder sb = new StringBuilder();
        int i5 = i * 26;
        int floor = (int) Math.floor(f - FT_GET_Word_Info.getBitmap_top());
        String str = TAG;
        Log.i(str, "size1: " + (i5 / 32.0f));
        Log.i(TAG, "size2: " + (f - FT_GET_Word_Info.getBitmap_top()));
        Log.i(TAG, "size3: " + floor);
        int i6 = 0;
        int i7 = 0;
        while (i6 < floor) {
            for (int i8 = 0; i8 < i3; i8++) {
                sb.append("_");
                bArr[i7][i8] = 1;
            }
            sb.append("\n");
            i7++;
            i6++;
        }
        while (i6 < FT_GET_Word_Info.getRows() + floor) {
            int i9 = 1;
            int i10 = 0;
            while (i9 <= FT_GET_Word_Info.getBitmap_left()) {
                sb.append("_");
                bArr[i7][i10] = b;
                i9++;
                i10++;
            }
            int i11 = 0;
            while (i11 < FT_GET_Word_Info.pitch) {
                int i12 = floor;
                byte b2 = FT_GET_Word_Info.buffer[(FT_GET_Word_Info.pitch * ((i6 + FT_GET_Word_Info.getBitmap_top()) - (i5 / 32))) + i11];
                WordInfo wordInfo = FT_GET_Word_Info;
                for (int i13 = 0; i13 < 8; i13++) {
                    if ((b2 & 128) > 0) {
                        sb.append("*");
                        if (i10 < i3) {
                            i2 = i10 + 1;
                            bArr[i7][i10] = 0;
                            i10 = i2;
                            b2 = (byte) (b2 << 1);
                            i9++;
                            if (i9 > i3) {
                                break;
                            }
                        } else {
                            b2 = (byte) (b2 << 1);
                            i9++;
                            if (i9 > i3) {
                            }
                        }
                    } else {
                        sb.append("_");
                        if (i10 < i3) {
                            i2 = i10 + 1;
                            bArr[i7][i10] = 1;
                            i10 = i2;
                        }
                        b2 = (byte) (b2 << 1);
                        i9++;
                        if (i9 > i3) {
                            break;
                        }
                    }
                    i11++;
                    floor = i12;
                    FT_GET_Word_Info = wordInfo;
                }
                i11++;
                floor = i12;
                FT_GET_Word_Info = wordInfo;
            }
            WordInfo wordInfo2 = FT_GET_Word_Info;
            int i14 = floor;
            int i15 = i9;
            int i16 = 0;
            while (i16 < (i3 - i9) + 1) {
                sb.append("-");
                int i17 = i10 + 1;
                bArr[i7][i10] = 1;
                i15++;
                if (i15 > i3) {
                    break;
                }
                i16++;
                i10 = i17;
            }
            i7++;
            sb.append("\n");
            i6++;
            floor = i14;
            FT_GET_Word_Info = wordInfo2;
            b = 1;
        }
        while (i6 < i) {
            for (int i18 = 0; i18 < i3; i18++) {
                sb.append("-");
                bArr[i7][i18] = 1;
            }
            i7++;
            sb.append("\n");
            i6++;
        }
        Log.i(TAG, "111\n" + sb.toString());
        return bArr;
    }

    public static byte[][] FT_GET_Word_Lattice(String str, int i, int i2) {
        byte[][] bArr;
        if (i2 == 0) {
            int i3 = 0;
            for (int i4 = 0; i4 < str.length(); i4++) {
                i3 = str.codePointAt(i4) <= 128 ? i3 + (i / 2) : i3 + i;
            }
            bArr = (byte[][]) Array.newInstance(byte.class, i, i3);
        } else {
            boolean z = false;
            for (int i5 = 0; i5 < str.length(); i5++) {
                if (str.codePointAt(i5) > 128) {
                    z = true;
                }
            }
            bArr = (byte[][]) Array.newInstance(byte.class, str.length() * i, z ? i : i / 2);
        }
        int i6 = 0;
        int i7 = 0;
        for (int i8 = 0; i8 < str.length(); i8++) {
            byte[][] FT_GET_Word_Lattice = FT_GET_Word_Lattice(i, str.codePointAt(i8));
            for (int i9 = 0; i9 < FT_GET_Word_Lattice.length; i9++) {
                for (int i10 = 0; i10 < FT_GET_Word_Lattice[0].length; i10++) {
                    bArr[i7 + i9][i6 + i10] = FT_GET_Word_Lattice[i9][i10];
                }
            }
            if (i2 == 0) {
                i6 += FT_GET_Word_Lattice[0].length;
            } else {
                i7 += i;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (byte[] bArr2 : bArr) {
            for (int i11 = 0; i11 < bArr[0].length; i11++) {
                if (bArr2[i11] == 1) {
                    sb.append("1");
                } else {
                    sb.append(NetResult.CODE_OK);
                }
            }
            sb.append("\n");
        }
        Log.i(TAG, "\n" + sb.toString());
        return bArr;
    }
}
