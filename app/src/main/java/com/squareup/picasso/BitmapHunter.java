package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.NetworkInfo;
import android.os.Build;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.squareup.picasso.NetworkRequestHandler;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BitmapHunter implements Runnable {
    Action action;
    List<Action> actions;
    final Cache cache;
    final Request data;
    final Dispatcher dispatcher;
    Exception exception;
    int exifOrientation;
    Future<?> future;
    final String key;
    Picasso.LoadedFrom loadedFrom;
    final int memoryPolicy;
    int networkPolicy;
    final Picasso picasso;
    Picasso.Priority priority;
    final RequestHandler requestHandler;
    Bitmap result;
    int retryCount;
    final int sequence = SEQUENCE_GENERATOR.incrementAndGet();
    final Stats stats;
    private static final Object DECODE_LOCK = new Object();
    private static final ThreadLocal<StringBuilder> NAME_BUILDER = new ThreadLocal<StringBuilder>() { // from class: com.squareup.picasso.BitmapHunter.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public StringBuilder initialValue() {
            return new StringBuilder("Picasso-");
        }
    };
    private static final AtomicInteger SEQUENCE_GENERATOR = new AtomicInteger();
    private static final RequestHandler ERRORING_HANDLER = new RequestHandler() { // from class: com.squareup.picasso.BitmapHunter.2
        @Override // com.squareup.picasso.RequestHandler
        public boolean canHandleRequest(Request request) {
            return true;
        }

        @Override // com.squareup.picasso.RequestHandler
        public RequestHandler.Result load(Request request, int i) throws IOException {
            throw new IllegalStateException("Unrecognized type of request: " + request);
        }
    };

    static int getExifRotation(int i) {
        switch (i) {
            case 3:
            case 4:
                return SubsamplingScaleImageView.ORIENTATION_180;
            case 5:
            case 6:
                return 90;
            case 7:
            case 8:
                return SubsamplingScaleImageView.ORIENTATION_270;
            default:
                return 0;
        }
    }

    static int getExifTranslation(int i) {
        return (i == 2 || i == 7 || i == 4 || i == 5) ? -1 : 1;
    }

    private static boolean shouldResize(boolean z, int i, int i2, int i3, int i4) {
        return !z || (i3 != 0 && i > i3) || (i4 != 0 && i2 > i4);
    }

    BitmapHunter(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action, RequestHandler requestHandler) {
        this.picasso = picasso;
        this.dispatcher = dispatcher;
        this.cache = cache;
        this.stats = stats;
        this.action = action;
        this.key = action.getKey();
        this.data = action.getRequest();
        this.priority = action.getPriority();
        this.memoryPolicy = action.getMemoryPolicy();
        this.networkPolicy = action.getNetworkPolicy();
        this.requestHandler = requestHandler;
        this.retryCount = requestHandler.getRetryCount();
    }

    static Bitmap decodeStream(Source source, Request request) throws IOException {
        BufferedSource buffer = Okio.buffer(source);
        boolean isWebPFile = Utils.isWebPFile(buffer);
        boolean z = request.purgeable && Build.VERSION.SDK_INT < 21;
        BitmapFactory.Options createBitmapOptions = RequestHandler.createBitmapOptions(request);
        boolean requiresInSampleSize = RequestHandler.requiresInSampleSize(createBitmapOptions);
        if (isWebPFile || z) {
            byte[] readByteArray = buffer.readByteArray();
            if (requiresInSampleSize) {
                BitmapFactory.decodeByteArray(readByteArray, 0, readByteArray.length, createBitmapOptions);
                RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, createBitmapOptions, request);
            }
            return BitmapFactory.decodeByteArray(readByteArray, 0, readByteArray.length, createBitmapOptions);
        }
        InputStream inputStream = buffer.inputStream();
        if (requiresInSampleSize) {
            MarkableInputStream markableInputStream = new MarkableInputStream(inputStream);
            markableInputStream.allowMarksToExpire(false);
            long savePosition = markableInputStream.savePosition(1024);
            BitmapFactory.decodeStream(markableInputStream, null, createBitmapOptions);
            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, createBitmapOptions, request);
            markableInputStream.reset(savePosition);
            markableInputStream.allowMarksToExpire(true);
            inputStream = markableInputStream;
        }
        Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, createBitmapOptions);
        if (decodeStream != null) {
            return decodeStream;
        }
        throw new IOException("Failed to decode stream.");
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                try {
                    updateThreadName(this.data);
                    if (this.picasso.loggingEnabled) {
                        Utils.log("Hunter", "executing", Utils.getLogIdsForHunter(this));
                    }
                    Bitmap hunt = hunt();
                    this.result = hunt;
                    if (hunt == null) {
                        this.dispatcher.dispatchFailed(this);
                    } else {
                        this.dispatcher.dispatchComplete(this);
                    }
                } catch (NetworkRequestHandler.ResponseException e) {
                    if (!NetworkPolicy.isOfflineOnly(e.networkPolicy) || e.code != 504) {
                        this.exception = e;
                    }
                    this.dispatcher.dispatchFailed(this);
                } catch (IOException e2) {
                    this.exception = e2;
                    this.dispatcher.dispatchRetry(this);
                }
            } catch (Exception e3) {
                this.exception = e3;
                this.dispatcher.dispatchFailed(this);
            } catch (OutOfMemoryError e4) {
                StringWriter stringWriter = new StringWriter();
                this.stats.createSnapshot().dump(new PrintWriter(stringWriter));
                this.exception = new RuntimeException(stringWriter.toString(), e4);
                this.dispatcher.dispatchFailed(this);
            }
        } finally {
            Thread.currentThread().setName("Picasso-Idle");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap hunt() throws IOException {
        Bitmap bitmap;
        if (MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy)) {
            bitmap = this.cache.get(this.key);
            if (bitmap != null) {
                this.stats.dispatchCacheHit();
                this.loadedFrom = Picasso.LoadedFrom.MEMORY;
                if (this.picasso.loggingEnabled) {
                    Utils.log("Hunter", "decoded", this.data.logId(), "from cache");
                }
                return bitmap;
            }
        } else {
            bitmap = null;
        }
        int i = this.retryCount == 0 ? NetworkPolicy.OFFLINE.index : this.networkPolicy;
        this.networkPolicy = i;
        RequestHandler.Result load = this.requestHandler.load(this.data, i);
        if (load != null) {
            this.loadedFrom = load.getLoadedFrom();
            this.exifOrientation = load.getExifOrientation();
            bitmap = load.getBitmap();
            if (bitmap == null) {
                Source source = load.getSource();
                try {
                    bitmap = decodeStream(source, this.data);
                } finally {
                    try {
                        source.close();
                    } catch (IOException unused) {
                    }
                }
            }
        }
        if (bitmap != null) {
            if (this.picasso.loggingEnabled) {
                Utils.log("Hunter", "decoded", this.data.logId());
            }
            this.stats.dispatchBitmapDecoded(bitmap);
            if (this.data.needsTransformation() || this.exifOrientation != 0) {
                synchronized (DECODE_LOCK) {
                    if (this.data.needsMatrixTransform() || this.exifOrientation != 0) {
                        bitmap = transformResult(this.data, bitmap, this.exifOrientation);
                        if (this.picasso.loggingEnabled) {
                            Utils.log("Hunter", "transformed", this.data.logId());
                        }
                    }
                    if (this.data.hasCustomTransformations()) {
                        bitmap = applyCustomTransformations(this.data.transformations, bitmap);
                        if (this.picasso.loggingEnabled) {
                            Utils.log("Hunter", "transformed", this.data.logId(), "from custom transformations");
                        }
                    }
                }
                if (bitmap != null) {
                    this.stats.dispatchBitmapTransformed(bitmap);
                }
            }
        }
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void attach(Action action) {
        boolean z = this.picasso.loggingEnabled;
        Request request = action.request;
        if (this.action == null) {
            this.action = action;
            if (z) {
                List<Action> list = this.actions;
                if (list == null || list.isEmpty()) {
                    Utils.log("Hunter", "joined", request.logId(), "to empty hunter");
                    return;
                } else {
                    Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
                    return;
                }
            }
            return;
        }
        if (this.actions == null) {
            this.actions = new ArrayList(3);
        }
        this.actions.add(action);
        if (z) {
            Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
        }
        Picasso.Priority priority = action.getPriority();
        if (priority.ordinal() > this.priority.ordinal()) {
            this.priority = priority;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void detach(Action action) {
        boolean remove;
        if (this.action == action) {
            this.action = null;
            remove = true;
        } else {
            List<Action> list = this.actions;
            remove = list != null ? list.remove(action) : false;
        }
        if (remove && action.getPriority() == this.priority) {
            this.priority = computeNewPriority();
        }
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "removed", action.request.logId(), Utils.getLogIdsForHunter(this, "from "));
        }
    }

    private Picasso.Priority computeNewPriority() {
        Picasso.Priority priority = Picasso.Priority.LOW;
        List<Action> list = this.actions;
        boolean z = true;
        boolean z2 = (list == null || list.isEmpty()) ? false : true;
        Action action = this.action;
        if (action == null && !z2) {
            z = false;
        }
        if (z) {
            if (action != null) {
                priority = action.getPriority();
            }
            if (z2) {
                int size = this.actions.size();
                for (int i = 0; i < size; i++) {
                    Picasso.Priority priority2 = this.actions.get(i).getPriority();
                    if (priority2.ordinal() > priority.ordinal()) {
                        priority = priority2;
                    }
                }
            }
            return priority;
        }
        return priority;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cancel() {
        Future<?> future;
        if (this.action == null) {
            List<Action> list = this.actions;
            return (list == null || list.isEmpty()) && (future = this.future) != null && future.cancel(false);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCancelled() {
        Future<?> future = this.future;
        return future != null && future.isCancelled();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldRetry(boolean z, NetworkInfo networkInfo) {
        int i = this.retryCount;
        if (i > 0) {
            this.retryCount = i - 1;
            return this.requestHandler.shouldRetry(z, networkInfo);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean supportsReplay() {
        return this.requestHandler.supportsReplay();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap getResult() {
        return this.result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getKey() {
        return this.key;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMemoryPolicy() {
        return this.memoryPolicy;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Request getData() {
        return this.data;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Action getAction() {
        return this.action;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Picasso getPicasso() {
        return this.picasso;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Action> getActions() {
        return this.actions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Exception getException() {
        return this.exception;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Picasso.LoadedFrom getLoadedFrom() {
        return this.loadedFrom;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Picasso.Priority getPriority() {
        return this.priority;
    }

    static void updateThreadName(Request request) {
        String name = request.getName();
        StringBuilder sb = NAME_BUILDER.get();
        sb.ensureCapacity(name.length() + 8);
        sb.replace(8, sb.length(), name);
        Thread.currentThread().setName(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BitmapHunter forRequest(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action) {
        Request request = action.getRequest();
        List<RequestHandler> requestHandlers = picasso.getRequestHandlers();
        int size = requestHandlers.size();
        for (int i = 0; i < size; i++) {
            RequestHandler requestHandler = requestHandlers.get(i);
            if (requestHandler.canHandleRequest(request)) {
                return new BitmapHunter(picasso, dispatcher, cache, stats, action, requestHandler);
            }
        }
        return new BitmapHunter(picasso, dispatcher, cache, stats, action, ERRORING_HANDLER);
    }

    static Bitmap applyCustomTransformations(List<Transformation> list, Bitmap bitmap) {
        int size = list.size();
        int i = 0;
        while (i < size) {
            final Transformation transformation = list.get(i);
            try {
                Bitmap transform = transformation.transform(bitmap);
                if (transform == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Transformation ");
                    sb.append(transformation.key());
                    sb.append(" returned null after ");
                    sb.append(i);
                    sb.append(" previous transformation(s).\n\nTransformation list:\n");
                    for (Transformation transformation2 : list) {
                        sb.append(transformation2.key());
                        sb.append('\n');
                    }
                    Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.BitmapHunter.4
                        @Override // java.lang.Runnable
                        public void run() {
                            throw new NullPointerException(sb.toString());
                        }
                    });
                    return null;
                } else if (transform == bitmap && bitmap.isRecycled()) {
                    Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.BitmapHunter.5
                        @Override // java.lang.Runnable
                        public void run() {
                            throw new IllegalStateException("Transformation " + Transformation.this.key() + " returned input Bitmap but recycled it.");
                        }
                    });
                    return null;
                } else if (transform != bitmap && !bitmap.isRecycled()) {
                    Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.BitmapHunter.6
                        @Override // java.lang.Runnable
                        public void run() {
                            throw new IllegalStateException("Transformation " + Transformation.this.key() + " mutated input Bitmap but failed to recycle the original.");
                        }
                    });
                    return null;
                } else {
                    i++;
                    bitmap = transform;
                }
            } catch (RuntimeException e) {
                Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.BitmapHunter.3
                    @Override // java.lang.Runnable
                    public void run() {
                        throw new RuntimeException("Transformation " + Transformation.this.key() + " crashed with exception.", e);
                    }
                });
                return null;
            }
        }
        return bitmap;
    }

    /* JADX WARN: Removed duplicated region for block: B:93:0x02c0  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x02c4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static Bitmap transformResult(Request request, Bitmap bitmap, int i) {
        int i2;
        int i3;
        boolean z;
        Matrix matrix;
        Matrix matrix2;
        int i4;
        int i5;
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        int i6;
        int i7;
        float f9;
        float f10;
        float f11;
        int i8;
        int i9;
        float f12;
        boolean z2;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        Bitmap createBitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean z3 = request.onlyScaleDown;
        Matrix matrix3 = new Matrix();
        if (request.needsMatrixTransform() || i != 0) {
            int i18 = request.targetWidth;
            int i19 = request.targetHeight;
            float f13 = request.rotationDegrees;
            if (f13 != 0.0f) {
                double d = f13;
                double cos = Math.cos(Math.toRadians(d));
                double sin = Math.sin(Math.toRadians(d));
                if (request.hasRotationPivot) {
                    matrix3.setRotate(f13, request.rotationPivotX, request.rotationPivotY);
                    double d2 = request.rotationPivotX;
                    double d3 = 1.0d - cos;
                    Double.isNaN(d2);
                    double d4 = request.rotationPivotY;
                    Double.isNaN(d4);
                    double d5 = (d2 * d3) + (d4 * sin);
                    double d6 = request.rotationPivotY;
                    Double.isNaN(d6);
                    double d7 = d6 * d3;
                    double d8 = request.rotationPivotX;
                    Double.isNaN(d8);
                    double d9 = d7 - (d8 * sin);
                    double d10 = request.targetWidth;
                    Double.isNaN(d10);
                    double d11 = (d10 * cos) + d5;
                    i3 = height;
                    z = z3;
                    double d12 = request.targetWidth;
                    Double.isNaN(d12);
                    double d13 = (d12 * sin) + d9;
                    matrix = matrix3;
                    double d14 = request.targetWidth;
                    Double.isNaN(d14);
                    i2 = width;
                    double d15 = request.targetHeight;
                    Double.isNaN(d15);
                    double d16 = ((d14 * cos) + d5) - (d15 * sin);
                    double d17 = request.targetWidth;
                    Double.isNaN(d17);
                    double d18 = request.targetHeight;
                    Double.isNaN(d18);
                    double d19 = (d17 * sin) + d9 + (d18 * cos);
                    double d20 = request.targetHeight;
                    Double.isNaN(d20);
                    double d21 = d5 - (d20 * sin);
                    double d22 = request.targetHeight;
                    Double.isNaN(d22);
                    double d23 = (d22 * cos) + d9;
                    double max = Math.max(d21, Math.max(d16, Math.max(d5, d11)));
                    double min = Math.min(d21, Math.min(d16, Math.min(d5, d11)));
                    double max2 = Math.max(d23, Math.max(d19, Math.max(d9, d13)));
                    double min2 = Math.min(d23, Math.min(d19, Math.min(d9, d13)));
                    i18 = (int) Math.floor(max - min);
                    i19 = (int) Math.floor(max2 - min2);
                } else {
                    i2 = width;
                    i3 = height;
                    z = z3;
                    matrix3.setRotate(f13);
                    double d24 = request.targetWidth;
                    Double.isNaN(d24);
                    double d25 = d24 * cos;
                    double d26 = request.targetWidth;
                    Double.isNaN(d26);
                    double d27 = d26 * sin;
                    double d28 = request.targetWidth;
                    Double.isNaN(d28);
                    double d29 = request.targetHeight;
                    Double.isNaN(d29);
                    double d30 = (d28 * cos) - (d29 * sin);
                    double d31 = request.targetWidth;
                    Double.isNaN(d31);
                    double d32 = request.targetHeight;
                    Double.isNaN(d32);
                    double d33 = (d31 * sin) + (d32 * cos);
                    double d34 = request.targetHeight;
                    Double.isNaN(d34);
                    double d35 = -(d34 * sin);
                    double d36 = request.targetHeight;
                    Double.isNaN(d36);
                    double d37 = d36 * cos;
                    matrix = matrix3;
                    double max3 = Math.max(d35, Math.max(d30, Math.max(0.0d, d25)));
                    double min3 = Math.min(d35, Math.min(d30, Math.min(0.0d, d25)));
                    i19 = (int) Math.floor(Math.max(d37, Math.max(d33, Math.max(0.0d, d27))) - Math.min(d37, Math.min(d33, Math.min(0.0d, d27))));
                    i18 = (int) Math.floor(max3 - min3);
                }
            } else {
                i2 = width;
                i3 = height;
                z = z3;
                matrix = matrix3;
            }
            if (i != 0) {
                int exifRotation = getExifRotation(i);
                int exifTranslation = getExifTranslation(i);
                if (exifRotation != 0) {
                    matrix2 = matrix;
                    matrix2.preRotate(exifRotation);
                    if (exifRotation == 90 || exifRotation == 270) {
                        int i20 = i19;
                        i19 = i18;
                        i18 = i20;
                    }
                } else {
                    matrix2 = matrix;
                }
                if (exifTranslation != 1) {
                    matrix2.postScale(exifTranslation, 1.0f);
                }
            } else {
                matrix2 = matrix;
            }
            if (request.centerCrop) {
                if (i18 != 0) {
                    i6 = i2;
                    f9 = i18 / i6;
                    i7 = i3;
                } else {
                    i6 = i2;
                    i7 = i3;
                    f9 = i19 / i7;
                }
                if (i19 != 0) {
                    f10 = i19;
                    f11 = i7;
                } else {
                    f10 = i18;
                    f11 = i6;
                }
                float f14 = f10 / f11;
                if (f9 > f14) {
                    int ceil = (int) Math.ceil(i7 * (f14 / f9));
                    if ((request.centerCropGravity & 48) == 48) {
                        i17 = 0;
                    } else {
                        i17 = (request.centerCropGravity & 80) == 80 ? i7 - ceil : (i7 - ceil) / 2;
                    }
                    f12 = i19 / ceil;
                    i9 = ceil;
                    z2 = z;
                    i11 = 0;
                    i10 = i17;
                    i8 = i6;
                } else if (f9 < f14) {
                    int ceil2 = (int) Math.ceil(i6 * (f9 / f14));
                    if ((request.centerCropGravity & 3) == 3) {
                        i12 = 0;
                    } else {
                        i12 = (request.centerCropGravity & 5) == 5 ? i6 - ceil2 : (i6 - ceil2) / 2;
                    }
                    i11 = i12;
                    i8 = ceil2;
                    i9 = i7;
                    f9 = i18 / ceil2;
                    z2 = z;
                    f12 = f14;
                    i10 = 0;
                } else {
                    i8 = i6;
                    i9 = i7;
                    f9 = f14;
                    f12 = f9;
                    z2 = z;
                    i10 = 0;
                    i11 = 0;
                }
                if (shouldResize(z2, i6, i7, i18, i19)) {
                    matrix2.preScale(f9, f12);
                }
                i13 = i10;
                i14 = i9;
                i15 = i11;
                i16 = i8;
                createBitmap = Bitmap.createBitmap(bitmap, i15, i13, i16, i14, matrix2, true);
                if (createBitmap == bitmap) {
                    bitmap.recycle();
                    return createBitmap;
                }
                return bitmap;
            }
            i4 = i3;
            boolean z4 = z;
            i5 = i2;
            if (request.centerInside) {
                if (i18 != 0) {
                    f5 = i18;
                    f6 = i5;
                } else {
                    f5 = i19;
                    f6 = i4;
                }
                float f15 = f5 / f6;
                if (i19 != 0) {
                    f7 = i19;
                    f8 = i4;
                } else {
                    f7 = i18;
                    f8 = i5;
                }
                float f16 = f7 / f8;
                if (f15 >= f16) {
                    f15 = f16;
                }
                if (shouldResize(z4, i5, i4, i18, i19)) {
                    matrix2.preScale(f15, f15);
                }
            } else if ((i18 != 0 || i19 != 0) && (i18 != i5 || i19 != i4)) {
                if (i18 != 0) {
                    f = i18;
                    f2 = i5;
                } else {
                    f = i19;
                    f2 = i4;
                }
                float f17 = f / f2;
                if (i19 != 0) {
                    f3 = i19;
                    f4 = i4;
                } else {
                    f3 = i18;
                    f4 = i5;
                }
                float f18 = f3 / f4;
                if (shouldResize(z4, i5, i4, i18, i19)) {
                    matrix2.preScale(f17, f18);
                }
            }
        } else {
            i4 = height;
            matrix2 = matrix3;
            i5 = width;
        }
        i16 = i5;
        i14 = i4;
        i15 = 0;
        i13 = 0;
        createBitmap = Bitmap.createBitmap(bitmap, i15, i13, i16, i14, matrix2, true);
        if (createBitmap == bitmap) {
        }
    }
}
