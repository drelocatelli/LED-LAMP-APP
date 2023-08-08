package com.home.utils.font;

import androidx.exifinterface.media.ExifInterface;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class Font24 {
    private static final String ENCODE = "GB2312";
    private static Map<String, String> fontStyleMap;

    Font24() {
    }

    static {
        HashMap hashMap = new HashMap();
        fontStyleMap = hashMap;
        hashMap.put("仿宋", "F");
        fontStyleMap.put("黑体", "H");
        fontStyleMap.put("楷体", "K");
        fontStyleMap.put("宋体", ExifInterface.LATITUDE_SOUTH);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<byte[]> makeFont(String str, String str2, OnMakeFontListener onMakeFontListener) {
        String sb;
        long offset;
        String str3;
        long j;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < str2.length()) {
            try {
                int i2 = i + 1;
                String substring = str2.substring(i, i2);
                char charAt = substring.charAt(0);
                int i3 = 72;
                if (FontUtils.isAscII(charAt)) {
                    str3 = "font/asc/ASC24_12";
                    j = (charAt - ' ') * 36;
                    i3 = 36;
                } else {
                    if (FontUtils.isChinesePunctuation(charAt)) {
                        sb = "font/24x24/HZK24T";
                        offset = getPunctuationOffset(substring);
                    } else if (FontUtils.isChinese(charAt)) {
                        String str4 = fontStyleMap.get(str);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("font/24x24/HZK24");
                        if (str4 == null) {
                            str4 = ExifInterface.LATITUDE_SOUTH;
                        }
                        sb2.append(str4);
                        sb = sb2.toString();
                        offset = getOffset(substring);
                    } else {
                        i = i2;
                    }
                    str3 = sb;
                    j = offset;
                }
                InputStream open = Utils.getApp().getAssets().open(str3);
                open.skip(j);
                byte[] bArr = new byte[i3];
                open.read(bArr);
                open.close();
                arrayList.add(bArr);
                if (onMakeFontListener != null) {
                    onMakeFontListener.schedule(i, str2.length());
                }
                i = i2;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private static long getOffset(String str) {
        try {
            byte[] bytes = str.getBytes(ENCODE);
            return ((((((bytes[0] < 0 ? bytes[0] + 256 : bytes[0]) - 175) - 1) * 94) + ((bytes[1] < 0 ? bytes[1] + 256 : bytes[1]) - 160)) - 1) * 72;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private static long getPunctuationOffset(String str) {
        try {
            byte[] bytes = str.getBytes(ENCODE);
            return ((((((bytes[0] < 0 ? bytes[0] + 256 : bytes[0]) - 160) - 1) * 94) + ((bytes[1] < 0 ? bytes[1] + 256 : bytes[1]) - 160)) - 1) * 72;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
