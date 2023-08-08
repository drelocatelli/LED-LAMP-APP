package com.home.utils.font;

import java.lang.Character;
import java.lang.reflect.Array;
import java.util.List;

/* loaded from: classes.dex */
public class FontUtils {
    public static final int FONT_SIZE_16 = 16;
    public static final int FONT_SIZE_24 = 24;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isAscII(char c) {
        return c < 128;
    }

    public static List<byte[]> makeFont16(String str, String str2, OnMakeFontListener onMakeFontListener) {
        return makeFont(16, str, str2, onMakeFontListener);
    }

    public static List<byte[]> makeFont24(String str, String str2, OnMakeFontListener onMakeFontListener) {
        return makeFont(24, str, str2, onMakeFontListener);
    }

    public static List<byte[]> makeFont(int i, String str, String str2, OnMakeFontListener onMakeFontListener) {
        if (i != 16) {
            if (i != 24) {
                return null;
            }
            return Font24.makeFont(str, str2, onMakeFontListener);
        }
        return Font16.makeFont(str, str2, onMakeFontListener);
    }

    public static boolean[][] convertMatrix16(List<byte[]> list) {
        return convertMatrix(16, list);
    }

    public static boolean[][] convertMatrix24(List<byte[]> list) {
        return convertMatrix(24, list);
    }

    public static boolean[][] convertMatrix(int i, List<byte[]> list) {
        int i2;
        int i3;
        int i4 = 0;
        for (byte[] bArr : list) {
            i4 += (bArr.length * 8) / i;
        }
        boolean[][] zArr = (boolean[][]) Array.newInstance(boolean.class, i4, i);
        int i5 = 0;
        for (byte[] bArr2 : list) {
            int i6 = i / 8;
            if (bArr2.length == 16) {
                i3 = 8;
                i2 = 2;
            } else if (bArr2.length == 36) {
                i3 = 12;
                i2 = 3;
            } else {
                i2 = i6;
                i3 = i;
            }
            for (int i7 = 0; i7 < i3; i7++) {
                for (int i8 = 0; i8 < i2; i8++) {
                    for (int i9 = 0; i9 < 8; i9++) {
                        if ((bArr2[(i7 * i2) + i8] & (128 >> i9)) >= 1) {
                            zArr[i7 + i5][(i8 * 8) + i9] = true;
                        } else {
                            zArr[i7 + i5][(i8 * 8) + i9] = false;
                        }
                    }
                }
            }
            i5 += i3;
        }
        return zArr;
    }

    public static boolean[][] makeMatrix16(String str, String str2) {
        return makeMatrix(16, str, str2);
    }

    public static boolean[][] makeMatrix24(String str, String str2) {
        return makeMatrix(24, str, str2);
    }

    public static boolean[][] makeMatrix(int i, String str, String str2) {
        return convertMatrix(i, makeFont(i, str, str2, null));
    }

    public static void printFontList(boolean z, List<byte[]> list) {
        for (byte[] bArr : list) {
            printFont(z, bArr);
        }
    }

    public static void printFont(boolean z, byte[] bArr) {
        boolean[][] zArr;
        int sqrt = (int) Math.sqrt(bArr.length * 8);
        int i = sqrt / 8;
        if (bArr.length == 16) {
            zArr = (boolean[][]) Array.newInstance(boolean.class, 16, 8);
            sqrt = 8;
            i = 2;
        } else if (bArr.length == 36) {
            sqrt = 12;
            i = 3;
            zArr = (boolean[][]) Array.newInstance(boolean.class, 24, 12);
        } else {
            zArr = (boolean[][]) Array.newInstance(boolean.class, sqrt, sqrt);
        }
        for (int i2 = 0; i2 < sqrt; i2++) {
            for (int i3 = 0; i3 < i; i3++) {
                for (int i4 = 0; i4 < 8; i4++) {
                    if ((bArr[(i2 * i) + i3] & (128 >> i4)) >= 1) {
                        zArr[(i3 * 8) + i4][i2] = true;
                        if (z) {
                            System.out.print("①");
                        }
                    } else {
                        zArr[(i3 * 8) + i4][i2] = false;
                        if (z) {
                            System.out.print(" ");
                        }
                    }
                }
            }
            if (z) {
                System.out.println();
            }
        }
        if (z) {
            return;
        }
        for (int i5 = 0; i5 < zArr.length; i5++) {
            for (int i6 = 0; i6 < zArr[i5].length; i6++) {
                if (zArr[i5][i6]) {
                    System.out.print("��");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock of = Character.UnicodeBlock.of(c);
        return of == Character.UnicodeBlock.GENERAL_PUNCTUATION || of == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || of == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || of == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS || of == Character.UnicodeBlock.VERTICAL_FORMS;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock of = Character.UnicodeBlock.of(c);
        return of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C || of == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D || of == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || of == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT;
    }
}
