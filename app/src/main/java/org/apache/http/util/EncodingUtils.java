package org.apache.http.util;

import java.io.UnsupportedEncodingException;
import org.apache.http.Consts;

/* loaded from: classes.dex */
public final class EncodingUtils {
    public static String getString(byte[] bArr, int i, int i2, String str) {
        Args.notNull(bArr, "Input");
        Args.notEmpty(str, "Charset");
        try {
            return new String(bArr, i, i2, str);
        } catch (UnsupportedEncodingException unused) {
            return new String(bArr, i, i2);
        }
    }

    public static String getString(byte[] bArr, String str) {
        Args.notNull(bArr, "Input");
        return getString(bArr, 0, bArr.length, str);
    }

    public static byte[] getBytes(String str, String str2) {
        Args.notNull(str, "Input");
        Args.notEmpty(str2, "Charset");
        try {
            return str.getBytes(str2);
        } catch (UnsupportedEncodingException unused) {
            return str.getBytes();
        }
    }

    public static byte[] getAsciiBytes(String str) {
        Args.notNull(str, "Input");
        return str.getBytes(Consts.ASCII);
    }

    public static String getAsciiString(byte[] bArr, int i, int i2) {
        Args.notNull(bArr, "Input");
        return new String(bArr, i, i2, Consts.ASCII);
    }

    public static String getAsciiString(byte[] bArr) {
        Args.notNull(bArr, "Input");
        return getAsciiString(bArr, 0, bArr.length);
    }

    private EncodingUtils() {
    }
}
