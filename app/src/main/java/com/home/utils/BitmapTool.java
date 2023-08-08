package com.home.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

/* loaded from: classes.dex */
public class BitmapTool {
    public static byte[] Bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap Bytes2Bimap(byte[] bArr) {
        if (bArr.length != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPurgeable = true;
            Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            decodeByteArray.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        }
        return null;
    }

    public static Bitmap Drawable2Bimap(Context context, int i) {
        new BitmapFactory.Options().inSampleSize = 2;
        return BitmapFactory.decodeResource(context.getResources(), i);
    }

    public static final Bitmap lessenUriImage(String str) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i = (int) (options.outHeight / 320.0f);
        options.inSampleSize = i > 0 ? i : 1;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        int width = decodeFile.getWidth();
        int height = decodeFile.getHeight();
        PrintStream printStream = System.out;
        printStream.println(width + " " + height);
        return decodeFile;
    }

    public static Bitmap getPicFromBytes(byte[] bArr, BitmapFactory.Options options) {
        if (bArr != null) {
            if (options != null) {
                return BitmapFactory.decodeByteArray(bArr, 0, bArr.length, options);
            }
            return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        }
        return null;
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] bArr = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                inputStream.close();
                return byteArray;
            }
        }
    }
}
