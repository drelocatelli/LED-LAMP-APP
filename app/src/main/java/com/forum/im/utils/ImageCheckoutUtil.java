package com.forum.im.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/* loaded from: classes.dex */
public class ImageCheckoutUtil {
    public static int getImageSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < 12) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
        return bitmap.getByteCount();
    }

    public static Bitmap getLoacalBitmap(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[2048];
            while (bufferedInputStream.read(bArr) > 0) {
                byteArrayOutputStream.write(bArr);
                byteArrayOutputStream.flush();
            }
            byteArrayOutputStream.close();
            fileInputStream.close();
            bufferedInputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
