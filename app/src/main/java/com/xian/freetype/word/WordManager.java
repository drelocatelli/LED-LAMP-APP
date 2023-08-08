package com.xian.freetype.word;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import com.common.net.NetResult;
import com.xian.freetype.bmp.DataWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class WordManager {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static WordManager instance;
    private String TAG = "WordManager";
    private Context context;
    private String fontPath;

    private WordManager() {
    }

    public static WordManager getInstance() {
        if (instance == null) {
            synchronized (WordManager.class) {
                if (instance == null) {
                    instance = new WordManager();
                }
            }
        }
        return instance;
    }

    public synchronized boolean init(Context context) {
        this.context = context;
        this.fontPath = this.context.getCacheDir().getAbsolutePath() + "/font/simsun.ttc";
        File file = new File(this.fontPath);
        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream open = context.getAssets().open("simsun.ttc");
            byte[] bArr = new byte[1024];
            for (int read = open.read(bArr); read > 0; read = open.read(bArr)) {
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NdkFreeType.FT_Init_FreeType(this.fontPath);
    }

    public synchronized boolean init(Context context, String str) {
        this.context = context;
        this.fontPath = str;
        return NdkFreeType.FT_Init_FreeType(str);
    }

    public void destroy() {
        NdkFreeType.FT_Destroy_FreeType();
    }

    public byte[] stringToBmpByte(String str) {
        return stringToBmpByte(str, 16, 0);
    }

    public byte[] stringToBmpByte(String str, int i, int i2) {
        return latticeToBmpByte(stringToLattice(str, i, i2));
    }

    public Bitmap stringToBmp(String str, int i, int i2) {
        return latticeToBmp(stringToLattice(str, i, i2));
    }

    public String getBitmapData(String str, int i, int i2) {
        byte[][] stringToLattice = stringToLattice(str, i, i2);
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < stringToLattice.length; i3++) {
            for (int i4 = 0; i4 < stringToLattice[i3].length; i4++) {
                sb.append(stringToLattice[i3][i4] == 1 ? 0 : 1);
            }
        }
        String sb2 = sb.toString();
        int length = ((sb2.length() + 8) - 1) / 8;
        String[] strArr = new String[length];
        for (int i5 = 0; i5 < length; i5++) {
            if (i5 < length - 1) {
                strArr[i5] = sb2.substring(i5 * 8, (i5 + 1) * 8);
            } else {
                strArr[i5] = sb2.substring(i5 * 8);
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i6 = 0; i6 < length; i6++) {
            String hexString = Integer.toHexString(Integer.parseInt(strArr[i6], 2));
            if (hexString.length() == 1) {
                hexString = NetResult.CODE_OK + hexString;
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x009b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public byte[] latticeToBmpByte(byte[][] bArr) {
        byte b;
        if (bArr == null) {
            return null;
        }
        int length = bArr[0].length;
        if (length < 32) {
            length = 32;
        } else if (length % 32 != 0) {
            length = ((length / 32) + 1) * 32;
        }
        int length2 = (bArr.length * length) / 8;
        Log.i(this.TAG, "像素点的长度： " + length2 + "  宽= " + length + " 高=" + bArr.length);
        byte[] bArr2 = new byte[length2];
        StringBuilder sb = new StringBuilder();
        byte[] bArr3 = {1, 2, 4, 8, 16, 32, 64, Byte.MIN_VALUE};
        int i = 0;
        for (int length3 = bArr.length - 1; length3 >= 0; length3--) {
            byte b2 = 0;
            for (int i2 = 1; i2 <= length; i2++) {
                int i3 = i2 - 1;
                if (i3 < bArr[0].length) {
                    if (bArr[length3][i3] == 1) {
                        b = bArr3[7 - (i3 % 8)];
                    }
                    if (i2 % 8 == 0) {
                        if (i < length2) {
                            bArr2[i] = b2;
                            sb.append(Integer.toHexString(b2) + " ");
                            i++;
                        }
                        b2 = 0;
                    }
                } else {
                    b = bArr3[7 - (i3 % 8)];
                }
                b2 = (byte) (b2 | b);
                if (i2 % 8 == 0) {
                }
            }
        }
        Log.i("像素的内容", sb.toString());
        int i4 = length2 + 62;
        try {
            Log.i(this.TAG, "文件长度：" + i4 + "  " + length2);
            DataWriter dataWriter = new DataWriter(i4);
            dataWriter.writeBytes(new byte[]{66, 77}, 2);
            dataWriter.writeInt(i4);
            dataWriter.writeShort((short) 0);
            dataWriter.writeShort((short) 0);
            dataWriter.writeInt(62);
            dataWriter.writeInt(40);
            dataWriter.writeInt(bArr[0].length);
            dataWriter.writeInt(bArr.length);
            dataWriter.writeShort((short) 1);
            dataWriter.writeShort((short) 1);
            dataWriter.writeInt(0);
            dataWriter.writeInt(length2);
            dataWriter.writeInt(3524);
            dataWriter.writeInt(3524);
            dataWriter.writeInt(0);
            dataWriter.writeInt(0);
            dataWriter.writeInt(0);
            dataWriter.writeBytes(new byte[]{-1, -1, -1, 0}, 4);
            dataWriter.writeBytes(bArr2, length2);
            return dataWriter.buf;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public Bitmap latticeToBmp(byte[][] bArr) {
        try {
            byte[] latticeToBmpByte = latticeToBmpByte(bArr);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < latticeToBmpByte.length; i++) {
                sb.append(Integer.toHexString(latticeToBmpByte[i]) + " ");
            }
            Log.i(this.TAG, sb.toString());
            return BitmapFactory.decodeByteArray(latticeToBmpByte, 0, latticeToBmpByte.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[][] stringToLattice(String str, int i, int i2) {
        return NdkFreeType.FT_GET_Word_Lattice(str, i, i2);
    }

    public WordInfo getWordInfo(int i, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.length() > 1) {
            throw new IllegalArgumentException("只支持一个字提取");
        }
        return NdkFreeType.FT_GET_Word_Info(i, str.codePointAt(0));
    }
}
