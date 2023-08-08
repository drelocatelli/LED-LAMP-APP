package com.forum.im.utils;

import android.graphics.Bitmap;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;
import java.io.InputStream;
import java.util.Vector;

/* loaded from: classes.dex */
public class GifOpenHelper {
    protected static final int MaxStackSize = 4096;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    protected int[] act;
    protected int bgColor;
    protected int bgIndex;
    protected int frameCount;
    protected Vector<GifFrame> frames;
    protected int[] gct;
    protected boolean gctFlag;
    protected int gctSize;
    protected int height;
    protected int ih;
    protected Bitmap image;

    /* renamed from: in  reason: collision with root package name */
    protected InputStream f14in;
    protected boolean interlace;
    protected int iw;
    protected int ix;
    protected int iy;
    protected int lastBgColor;
    protected Bitmap lastImage;
    protected int[] lct;
    protected boolean lctFlag;
    protected int lctSize;
    protected int lrh;
    protected int lrw;
    protected int lrx;
    protected int lry;
    protected int pixelAspect;
    protected byte[] pixelStack;
    protected byte[] pixels;
    protected short[] prefix;
    protected int status;
    protected byte[] suffix;
    protected int transIndex;
    protected int width;
    protected int loopCount = 1;
    protected int frameindex = 0;
    protected byte[] block = new byte[256];
    protected int blockSize = 0;
    protected int dispose = 0;
    protected int lastDispose = 0;
    protected boolean transparency = false;
    protected int delay = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class GifFrame {
        public int delay;
        public Bitmap image;

        public GifFrame(Bitmap bitmap, int i) {
            this.image = bitmap;
            this.delay = i;
        }
    }

    public int getFrameindex() {
        return this.frameindex;
    }

    public void setFrameindex(int i) {
        this.frameindex = i;
        this.frames.size();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeigh() {
        return this.height;
    }

    public int getDelay(int i) {
        this.delay = -1;
        if (i >= 0 && i < this.frameCount) {
            this.delay = this.frames.elementAt(i).delay;
        }
        return this.delay;
    }

    public int getFrameCount() {
        return this.frameCount;
    }

    public Bitmap getImage() {
        return getFrame(0);
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    protected void setPixels() {
        int i;
        int[] iArr = new int[this.width * this.height];
        int i2 = this.lastDispose;
        int i3 = 0;
        if (i2 > 0) {
            if (i2 == 3) {
                int i4 = this.frameCount - 2;
                if (i4 > 0) {
                    this.lastImage = getFrame(i4 - 1);
                } else {
                    this.lastImage = null;
                }
            }
            Bitmap bitmap = this.lastImage;
            if (bitmap != null) {
                int i5 = this.width;
                bitmap.getPixels(iArr, 0, i5, 0, 0, i5, this.height);
                if (this.lastDispose == 2) {
                    int i6 = !this.transparency ? this.lastBgColor : 0;
                    for (int i7 = 0; i7 < this.lrh; i7++) {
                        int i8 = ((this.lry + i7) * this.width) + this.lrx;
                        int i9 = this.lrw + i8;
                        while (i8 < i9) {
                            iArr[i8] = i6;
                            i8++;
                        }
                    }
                }
            }
        }
        int i10 = 8;
        int i11 = 0;
        int i12 = 1;
        while (true) {
            int i13 = this.ih;
            if (i3 < i13) {
                if (this.interlace) {
                    if (i11 >= i13) {
                        i12++;
                        if (i12 == 2) {
                            i11 = 4;
                        } else if (i12 == 3) {
                            i10 = 4;
                            i11 = 2;
                        } else if (i12 == 4) {
                            i10 = 2;
                            i11 = 1;
                        }
                    }
                    i = i11 + i10;
                } else {
                    i = i11;
                    i11 = i3;
                }
                int i14 = i11 + this.iy;
                if (i14 < this.height) {
                    int i15 = this.width;
                    int i16 = i14 * i15;
                    int i17 = this.ix + i16;
                    int i18 = this.iw;
                    int i19 = i17 + i18;
                    if (i16 + i15 < i19) {
                        i19 = i16 + i15;
                    }
                    int i20 = i18 * i3;
                    while (i17 < i19) {
                        int i21 = i20 + 1;
                        int i22 = this.act[this.pixels[i20] & 255];
                        if (i22 != 0) {
                            iArr[i17] = i22;
                        }
                        i17++;
                        i20 = i21;
                    }
                }
                i3++;
                i11 = i;
            } else {
                this.image = Bitmap.createBitmap(iArr, this.width, this.height, Bitmap.Config.ARGB_4444);
                return;
            }
        }
    }

    public Bitmap getFrame(int i) {
        if (i < 0 || i >= this.frameCount) {
            return null;
        }
        return this.frames.elementAt(i).image;
    }

    public Bitmap nextBitmap() {
        int i = this.frameindex + 1;
        this.frameindex = i;
        if (i > this.frames.size() - 1) {
            this.frameindex = 0;
        }
        return this.frames.elementAt(this.frameindex).image;
    }

    public int nextDelay() {
        return this.frames.elementAt(this.frameindex).delay;
    }

    public int read(InputStream inputStream) {
        init();
        if (inputStream != null) {
            this.f14in = inputStream;
            readHeader();
            if (!err()) {
                readContents();
                if (this.frameCount < 0) {
                    this.status = 1;
                }
            }
        } else {
            this.status = 2;
        }
        try {
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.status;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void decodeImageData() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        short s;
        int i6 = this.iw * this.ih;
        byte[] bArr = this.pixels;
        if (bArr == null || bArr.length < i6) {
            this.pixels = new byte[i6];
        }
        if (this.prefix == null) {
            this.prefix = new short[4096];
        }
        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }
        if (this.pixelStack == null) {
            this.pixelStack = new byte[FragmentTransaction.TRANSIT_FRAGMENT_OPEN];
        }
        int read = read();
        int i7 = 1 << read;
        int i8 = i7 + 1;
        int i9 = i7 + 2;
        int i10 = read + 1;
        int i11 = (1 << i10) - 1;
        for (int i12 = 0; i12 < i7; i12++) {
            this.prefix[i12] = 0;
            this.suffix[i12] = (byte) i12;
        }
        int i13 = i10;
        int i14 = i9;
        int i15 = i11;
        int i16 = -1;
        int i17 = 0;
        int i18 = 0;
        int i19 = 0;
        int i20 = 0;
        int i21 = 0;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        while (i17 < i6) {
            if (i18 != 0) {
                i = i10;
                i2 = i8;
                int i25 = i23;
                i3 = i7;
                i4 = i25;
            } else if (i19 >= i13) {
                int i26 = i20 & i15;
                i20 >>= i13;
                i19 -= i13;
                if (i26 > i14 || i26 == i8) {
                    break;
                } else if (i26 == i7) {
                    i13 = i10;
                    i14 = i9;
                    i15 = i11;
                    i16 = -1;
                } else if (i16 == -1) {
                    this.pixelStack[i18] = this.suffix[i26];
                    i16 = i26;
                    i23 = i16;
                    i18++;
                    i10 = i10;
                } else {
                    i = i10;
                    if (i26 == i14) {
                        i5 = i26;
                        this.pixelStack[i18] = (byte) i23;
                        s = i16;
                        i18++;
                    } else {
                        i5 = i26;
                        s = i5;
                    }
                    while (s > i7) {
                        this.pixelStack[i18] = this.suffix[s];
                        s = this.prefix[s];
                        i18++;
                        i7 = i7;
                    }
                    i3 = i7;
                    byte[] bArr2 = this.suffix;
                    i4 = bArr2[s] & 255;
                    if (i14 >= 4096) {
                        break;
                    }
                    int i27 = i18 + 1;
                    i2 = i8;
                    byte b = (byte) i4;
                    this.pixelStack[i18] = b;
                    this.prefix[i14] = (short) i16;
                    bArr2[i14] = b;
                    i14++;
                    if ((i14 & i15) == 0 && i14 < 4096) {
                        i13++;
                        i15 += i14;
                    }
                    i18 = i27;
                    i16 = i5;
                }
            } else {
                if (i21 == 0) {
                    i21 = readBlock();
                    if (i21 <= 0) {
                        break;
                    }
                    i22 = 0;
                }
                i20 += (this.block[i22] & 255) << i19;
                i19 += 8;
                i22++;
                i21--;
            }
            i18--;
            this.pixels[i24] = this.pixelStack[i18];
            i17++;
            i24++;
            i7 = i3;
            i8 = i2;
            i23 = i4;
            i10 = i;
        }
        for (int i28 = i24; i28 < i6; i28++) {
            this.pixels[i28] = 0;
        }
    }

    protected boolean err() {
        return this.status != 0;
    }

    public void init() {
        this.status = 0;
        this.frameCount = 0;
        this.frames = new Vector<>();
        this.gct = null;
        this.lct = null;
    }

    protected int read() {
        try {
            return this.f14in.read();
        } catch (Exception unused) {
            this.status = 1;
            return 0;
        }
    }

    protected int readBlock() {
        int read = read();
        this.blockSize = read;
        int i = 0;
        if (read > 0) {
            while (true) {
                try {
                    int i2 = this.blockSize;
                    if (i >= i2) {
                        break;
                    }
                    int read2 = this.f14in.read(this.block, i, i2 - i);
                    if (read2 == -1) {
                        break;
                    }
                    i += read2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (i < this.blockSize) {
                this.status = 1;
            }
        }
        return i;
    }

    protected int[] readColorTable(int i) {
        int i2;
        int i3 = i * 3;
        byte[] bArr = new byte[i3];
        try {
            i2 = this.f14in.read(bArr);
        } catch (Exception e) {
            e.printStackTrace();
            i2 = 0;
        }
        if (i2 < i3) {
            this.status = 1;
            return null;
        }
        int[] iArr = new int[256];
        int i4 = 0;
        for (int i5 = 0; i5 < i; i5++) {
            int i6 = i4 + 1;
            int i7 = i6 + 1;
            iArr[i5] = ((bArr[i4] & 255) << 16) | ViewCompat.MEASURED_STATE_MASK | ((bArr[i6] & 255) << 8) | (bArr[i7] & 255);
            i4 = i7 + 1;
        }
        return iArr;
    }

    protected void readContents() {
        boolean z = false;
        while (!z && !err()) {
            int read = read();
            if (read != 0) {
                if (read == 33) {
                    int read2 = read();
                    if (read2 == 249) {
                        readGraphicControlExt();
                    } else if (read2 == 255) {
                        readBlock();
                        String str = "";
                        for (int i = 0; i < 11; i++) {
                            str = str + ((char) this.block[i]);
                        }
                        if (str.equals("NETSCAPE2.0")) {
                            readNetscapeExt();
                        } else {
                            skip();
                        }
                    } else {
                        skip();
                    }
                } else if (read == 44) {
                    readImage();
                } else if (read != 59) {
                    this.status = 1;
                } else {
                    z = true;
                }
            }
        }
    }

    protected void readGraphicControlExt() {
        read();
        int read = read();
        int i = (read & 28) >> 2;
        this.dispose = i;
        if (i == 0) {
            this.dispose = 1;
        }
        this.transparency = (read & 1) != 0;
        this.delay = readShort() * 10;
        this.transIndex = read();
        read();
    }

    protected void readHeader() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            str = str + ((char) read());
        }
        if (!str.startsWith("GIF")) {
            this.status = 1;
            return;
        }
        readLSD();
        if (!this.gctFlag || err()) {
            return;
        }
        int[] readColorTable = readColorTable(this.gctSize);
        this.gct = readColorTable;
        this.bgColor = readColorTable[this.bgIndex];
    }

    protected void readImage() {
        this.ix = readShort();
        this.iy = readShort();
        this.iw = readShort();
        this.ih = readShort();
        int read = read();
        int i = 0;
        boolean z = (read & 128) != 0;
        this.lctFlag = z;
        this.interlace = (read & 64) != 0;
        int i2 = 2 << (read & 7);
        this.lctSize = i2;
        if (z) {
            int[] readColorTable = readColorTable(i2);
            this.lct = readColorTable;
            this.act = readColorTable;
        } else {
            this.act = this.gct;
            if (this.bgIndex == this.transIndex) {
                this.bgColor = 0;
            }
        }
        if (this.transparency) {
            int[] iArr = this.act;
            int i3 = this.transIndex;
            int i4 = iArr[i3];
            iArr[i3] = 0;
            i = i4;
        }
        if (this.act == null) {
            this.status = 1;
        }
        if (err()) {
            return;
        }
        decodeImageData();
        skip();
        if (err()) {
            return;
        }
        this.frameCount++;
        this.image = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_4444);
        setPixels();
        this.frames.addElement(new GifFrame(this.image, this.delay));
        if (this.transparency) {
            this.act[this.transIndex] = i;
        }
        resetFrame();
    }

    protected void readLSD() {
        this.width = readShort();
        this.height = readShort();
        int read = read();
        this.gctFlag = (read & 128) != 0;
        this.gctSize = 2 << (read & 7);
        this.bgIndex = read();
        this.pixelAspect = read();
    }

    protected void readNetscapeExt() {
        do {
            readBlock();
            byte[] bArr = this.block;
            if (bArr[0] == 1) {
                this.loopCount = ((bArr[2] & 255) << 8) | (bArr[1] & 255);
            }
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }

    protected int readShort() {
        return read() | (read() << 8);
    }

    protected void resetFrame() {
        this.lastDispose = this.dispose;
        this.lrx = this.ix;
        this.lry = this.iy;
        this.lrw = this.iw;
        this.lrh = this.ih;
        this.lastImage = this.image;
        this.lastBgColor = this.bgColor;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.lct = null;
    }

    protected void skip() {
        do {
            readBlock();
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }
}
