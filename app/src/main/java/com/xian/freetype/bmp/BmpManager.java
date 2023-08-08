package com.xian.freetype.bmp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import com.common.net.NetResult;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class BmpManager {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private String TAG = "BmpManager";
    private BitmapFile bitmapFile;
    private Context context;

    public BmpManager(Context context) {
        this.context = context;
        this.bitmapFile = new BitmapFile(context);
    }

    public Bitmap bmpMerge(Bitmap bitmap, Bitmap bitmap2, int i) {
        int width = bitmap.getWidth();
        if (bitmap2.getWidth() == width) {
            if (i == 0) {
                Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth() + width, bitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(createBitmap);
                canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
                canvas.drawBitmap(bitmap2, width, 0.0f, (Paint) null);
                return createBitmap;
            }
            Bitmap createBitmap2 = Bitmap.createBitmap(width, bitmap.getHeight() + bitmap2.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas2 = new Canvas(createBitmap2);
            canvas2.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas2.drawBitmap(bitmap2, 0.0f, bitmap.getHeight(), (Paint) null);
            return createBitmap2;
        } else if (i == 0) {
            Bitmap createBitmap3 = Bitmap.createBitmap(width + bitmap2.getWidth(), Math.max(bitmap2.getHeight(), bitmap.getHeight()), Bitmap.Config.RGB_565);
            Canvas canvas3 = new Canvas(createBitmap3);
            canvas3.drawColor(-1);
            canvas3.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas3.drawBitmap(bitmap2, 0.0f, bitmap.getHeight(), (Paint) null);
            return createBitmap3;
        } else {
            Bitmap createBitmap4 = Bitmap.createBitmap(Math.max(bitmap2.getWidth(), bitmap.getWidth()), bitmap2.getHeight() + bitmap.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas4 = new Canvas(createBitmap4);
            canvas4.drawColor(-1);
            canvas4.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            canvas4.drawBitmap(bitmap2, 0.0f, bitmap.getHeight(), (Paint) null);
            return createBitmap4;
        }
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {
        Matrix matrix = new Matrix();
        matrix.postScale(i / bitmap.getWidth(), i2 / bitmap.getHeight());
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public byte[] bitmapToBmpData(Bitmap bitmap) {
        int i;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i2 = width * height;
        int[] iArr = new int[i2];
        Log.i(this.TAG, "宽度：" + width + " -高度-> " + height);
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        byte b = 1;
        byte[][] bArr = (byte[][]) Array.newInstance(byte.class, height, width);
        StringBuilder sb = new StringBuilder();
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 1; i5 <= i2; i5++) {
            if (iArr[i5 - 1] == -1) {
                sb.append("1");
                i = i4 + 1;
                bArr[i3][i4] = 1;
            } else {
                sb.append(NetResult.CODE_OK);
                i = i4 + 1;
                bArr[i3][i4] = 0;
            }
            if (i5 % width == 0) {
                sb.append("\n");
                i3++;
                i4 = 0;
            } else {
                i4 = i;
            }
        }
        Log.i(this.TAG, sb.toString());
        sb.delete(0, sb.length());
        int width2 = (bitmap.getWidth() * bitmap.getHeight()) / 4;
        byte[] bArr2 = new byte[width2];
        StringBuilder sb2 = new StringBuilder();
        byte[] bArr3 = {1, 2, 4, 8, 16, 32, 64, Byte.MIN_VALUE};
        int length = bArr.length - 1;
        int i6 = 0;
        while (length >= 0) {
            int i7 = 1;
            byte b2 = 0;
            while (i7 <= bArr[0].length) {
                int i8 = i7 - 1;
                if (bArr[length][i8] == b) {
                    b2 = (byte) (b2 | bArr3[7 - (i8 % 8)]);
                }
                if (i7 % 8 == 0) {
                    bArr2[i6] = b2;
                    sb2.append(Integer.toHexString(b2) + " ");
                    i6++;
                    b2 = 0;
                }
                i7++;
                b = 1;
            }
            if ((bitmap.getWidth() / 16) % 2 != 0) {
                if (i6 < width2) {
                    bArr2[i6] = 0;
                    i6++;
                }
                if (i6 < width2) {
                    bArr2[i6] = 0;
                    i6++;
                }
                sb2.append("0 ");
                sb2.append("0 ");
            }
            length--;
            b = 1;
        }
        int i9 = width2 + 62;
        try {
            Log.i(this.TAG, "文件长度：" + i9 + "  " + width2 + " " + sb2.toString());
            DataWriter dataWriter = new DataWriter(i9);
            dataWriter.writeBytes(new byte[]{66, 77}, 2);
            dataWriter.writeInt(i9);
            dataWriter.writeShort((short) 0);
            dataWriter.writeShort((short) 0);
            dataWriter.writeInt(62);
            dataWriter.writeInt(40);
            dataWriter.writeInt(width);
            dataWriter.writeInt(height);
            dataWriter.writeShort((short) 1);
            dataWriter.writeShort((short) 1);
            dataWriter.writeInt(0);
            dataWriter.writeInt(width2);
            dataWriter.writeInt(3524);
            dataWriter.writeInt(3524);
            dataWriter.writeInt(0);
            dataWriter.writeInt(0);
            dataWriter.writeInt(0);
            dataWriter.writeBytes(new byte[]{-1, -1, -1, 0}, 4);
            dataWriter.writeBytes(bArr2, width2);
            return dataWriter.buf;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }
}
