package com.home.utils.font;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Font16 {
    private static final String ENCODE = "GB2312";
    private static Map<String, String> fontStyleMap;

    Font16() {
    }

    static {
        HashMap hashMap = new HashMap();
        fontStyleMap = hashMap;
        hashMap.put("仿宋", "f");
        fontStyleMap.put("黑体", "h");
        fontStyleMap.put("楷体", "k");
        fontStyleMap.put("宋体", "s");
        fontStyleMap.put("幼圆", "y");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<byte[]> makeFont(String str, String str2, OnMakeFontListener onMakeFontListener) {
        long offset;
        String str3;
        int i;
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < str2.length()) {
            try {
                int i3 = i2 + 1;
                String substring = str2.substring(i2, i3);
                char charAt = substring.charAt(0);
                if (FontUtils.isAscII(charAt)) {
                    str3 = "font/asc/ASC16_8";
                    i = 16;
                    offset = (charAt - ' ') * 16;
                } else if (FontUtils.isChinese(charAt)) {
                    String str4 = fontStyleMap.get(str);
                    StringBuilder sb = new StringBuilder();
                    sb.append("font/16x16/hzk16");
                    if (str4 == null) {
                        str4 = "s";
                    }
                    sb.append(str4);
                    String sb2 = sb.toString();
                    offset = getOffset(substring);
                    str3 = sb2;
                    i = 32;
                } else {
                    i2 = i3;
                }
                InputStream open = Utils.getApp().getAssets().open(str3);
                open.skip(offset);
                byte[] bArr = new byte[i];
                open.read(bArr);
                open.close();
                int i4 = 128;
                if (charAt > 128) {
                    int i5 = 2;
                    boolean[][] zArr = (boolean[][]) Array.newInstance(boolean.class, 16, 16);
                    int i6 = 0;
                    while (i6 < 16) {
                        int i7 = 0;
                        while (i7 < i5) {
                            int i8 = 0;
                            while (i8 < 8) {
                                if ((bArr[(i6 * 2) + i7] & (i4 >> i8)) >= 1) {
                                    zArr[(i7 * 8) + i8][i6] = true;
                                } else {
                                    zArr[(i7 * 8) + i8][i6] = false;
                                }
                                i8++;
                                i4 = 128;
                            }
                            i7++;
                            i4 = 128;
                            i5 = 2;
                        }
                        i6++;
                        i4 = 128;
                        i5 = 2;
                    }
                    Arrays.fill(bArr, (byte) 0);
                    for (int i9 = 0; i9 < zArr.length; i9++) {
                        for (int i10 = 0; i10 < zArr[i9].length; i10++) {
                            int length = ((zArr[i9].length * i9) + i10) / 8;
                            if (zArr[i9][i10]) {
                                bArr[length] = (byte) (bArr[length] | (1 << (7 - (i10 % 8))));
                            }
                        }
                    }
                }
                arrayList.add(bArr);
                if (onMakeFontListener != null) {
                    onMakeFontListener.schedule(i2, str2.length());
                }
                i2 = i3;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private static long getOffset(String str) {
        try {
            byte[] bytes = str.getBytes(ENCODE);
            return ((((((bytes[0] < 0 ? bytes[0] + 256 : bytes[0]) - 160) - 1) * 94) + ((bytes[1] < 0 ? bytes[1] + 256 : bytes[1]) - 160)) - 1) * 32;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
