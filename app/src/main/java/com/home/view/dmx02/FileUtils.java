package com.home.view.dmx02;

import android.graphics.Bitmap;
import com.luck.picture.lib.config.PictureMimeType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: classes.dex */
public class FileUtils {

    /* renamed from: com.home.view.dmx02.FileUtils$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat;

        static {
            int[] iArr = new int[Bitmap.CompressFormat.values().length];
            $SwitchMap$android$graphics$Bitmap$CompressFormat = iArr;
            try {
                iArr[Bitmap.CompressFormat.JPEG.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r3v0, types: [android.graphics.Bitmap] */
    public static boolean saveBitmap(String str, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int i) {
        if (i > 100) {
            i = 100;
        }
        ?? r0 = 0;
        ?? r02 = 0;
        try {
            try {
                try {
                    if (AnonymousClass1.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()] != 1) {
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(str + PictureMimeType.PNG));
                        boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, i, fileOutputStream);
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return compress;
                    }
                    FileOutputStream fileOutputStream2 = new FileOutputStream(new File(str + ".jpg"));
                    boolean compress2 = bitmap.compress(Bitmap.CompressFormat.JPEG, i, fileOutputStream2);
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    return compress2;
                } catch (Exception e3) {
                    e = e3;
                    r02 = str;
                    e.printStackTrace();
                    if (r02 != 0) {
                        try {
                            r02.close();
                            return false;
                        } catch (IOException e4) {
                            e4.printStackTrace();
                            return false;
                        }
                    }
                    return false;
                } catch (Throwable th) {
                    th = th;
                    r0 = str;
                    if (r0 != 0) {
                        try {
                            r0.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e6) {
                e = e6;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
