package org.apache.http.client.utils;

import java.util.StringTokenizer;

@Deprecated
/* loaded from: classes.dex */
public class Rfc3492Idn implements Idn {
    private static final String ACE_PREFIX = "xn--";
    private static final int base = 36;
    private static final int damp = 700;
    private static final char delimiter = '-';
    private static final int initial_bias = 72;
    private static final int initial_n = 128;
    private static final int skew = 38;
    private static final int tmax = 26;
    private static final int tmin = 1;

    private int adapt(int i, int i2, boolean z) {
        int i3;
        if (z) {
            i3 = i / damp;
        } else {
            i3 = i / 2;
        }
        int i4 = i3 + (i3 / i2);
        int i5 = 0;
        while (i4 > 455) {
            i4 /= 35;
            i5 += 36;
        }
        return i5 + ((i4 * 36) / (i4 + 38));
    }

    private int digit(char c) {
        if (c < 'A' || c > 'Z') {
            if (c < 'a' || c > 'z') {
                if (c < '0' || c > '9') {
                    throw new IllegalArgumentException("illegal digit: " + c);
                }
                return (c - '0') + 26;
            }
            return c - 'a';
        }
        return c - 'A';
    }

    @Override // org.apache.http.client.utils.Idn
    public String toUnicode(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        StringTokenizer stringTokenizer = new StringTokenizer(str, ".");
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            if (sb.length() > 0) {
                sb.append('.');
            }
            if (nextToken.startsWith(ACE_PREFIX)) {
                nextToken = decode(nextToken.substring(4));
            }
            sb.append(nextToken);
        }
        return sb.toString();
    }

    protected String decode(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        int lastIndexOf = str.lastIndexOf(45);
        int i = 128;
        int i2 = 72;
        if (lastIndexOf != -1) {
            sb.append(str.subSequence(0, lastIndexOf));
            str = str.substring(lastIndexOf + 1);
        }
        int i3 = 0;
        while (!str.isEmpty()) {
            int i4 = 36;
            int i5 = i3;
            int i6 = 1;
            while (!str.isEmpty()) {
                char charAt = str.charAt(0);
                str = str.substring(1);
                int digit = digit(charAt);
                i5 += digit * i6;
                int i7 = i4 <= i2 + 1 ? 1 : i4 >= i2 + 26 ? 26 : i4 - i2;
                if (digit < i7) {
                    break;
                }
                i6 *= 36 - i7;
                i4 += 36;
            }
            i2 = adapt(i5 - i3, sb.length() + 1, i3 == 0);
            i += i5 / (sb.length() + 1);
            int length = i5 % (sb.length() + 1);
            sb.insert(length, (char) i);
            i3 = length + 1;
        }
        return sb.toString();
    }
}
