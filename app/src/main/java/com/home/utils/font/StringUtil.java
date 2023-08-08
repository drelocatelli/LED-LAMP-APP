package com.home.utils.font;

import androidx.exifinterface.media.ExifInterface;
import com.common.net.NetResult;

/* loaded from: classes.dex */
public class StringUtil extends StringUtils {
    private static final String[] hexTextArray = {NetResult.CODE_OK, "1", ExifInterface.GPS_MEASUREMENT_2D, ExifInterface.GPS_MEASUREMENT_3D, "4", "5", "6", "7", "8", "9", ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "B", "C", "D", ExifInterface.LONGITUDE_EAST, "F"};

    private StringUtil() {
    }

    public static String double2String(double d) {
        int i = (int) d;
        return d == ((double) i) ? String.valueOf(i) : String.valueOf(d);
    }

    public static String float2String(float f) {
        int i = (int) f;
        return f == ((float) i) ? String.valueOf(i) : String.valueOf(f);
    }

    public static String byteArray2hexString(byte[] bArr) {
        return byteArray2hexString(0, bArr.length, bArr);
    }

    public static String byteArray2hexString(int i, int i2, byte[] bArr) {
        if (i + i2 <= bArr.length) {
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bArr) {
                String[] strArr = hexTextArray;
                stringBuffer.append(strArr[(b >> 4) & 15]);
                stringBuffer.append(strArr[b & 15]);
            }
            return stringBuffer.toString();
        }
        throw new IllegalArgumentException("out of bound of array");
    }
}
