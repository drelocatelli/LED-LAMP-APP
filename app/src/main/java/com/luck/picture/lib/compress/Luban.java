package com.luck.picture.lib.compress;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.AndroidQTransformUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class Luban implements Handler.Callback {
    private static final int MSG_COMPRESS_ERROR = 2;
    private static final int MSG_COMPRESS_START = 1;
    private static final int MSG_COMPRESS_SUCCESS = 0;
    private static final String TAG = "Luban";
    private int compressQuality;
    private String customFileName;
    private boolean focusAlpha;
    private int index;
    private OnCompressListener mCompressListener;
    private CompressionPredicate mCompressionPredicate;
    private Handler mHandler;
    private int mLeastCompressSize;
    private List<String> mPaths;
    private OnRenameListener mRenameListener;
    private List<InputStreamProvider> mStreamProviders;
    private String mTargetDir;
    private List<LocalMedia> mediaList;

    private Luban(Builder builder) {
        this.index = -1;
        this.mPaths = builder.mPaths;
        this.mediaList = builder.mediaList;
        this.mTargetDir = builder.mTargetDir;
        this.mRenameListener = builder.mRenameListener;
        this.mStreamProviders = builder.mStreamProviders;
        this.mCompressListener = builder.mCompressListener;
        this.mLeastCompressSize = builder.mLeastCompressSize;
        this.mCompressionPredicate = builder.mCompressionPredicate;
        this.customFileName = builder.customFileName;
        this.compressQuality = builder.compressQuality;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    private File getImageCacheFile(Context context, String str) {
        String str2;
        if (TextUtils.isEmpty(this.mTargetDir) && getImageCacheDir(context) != null) {
            this.mTargetDir = getImageCacheDir(context).getAbsolutePath();
        }
        if (TextUtils.isEmpty(this.customFileName)) {
            str2 = System.currentTimeMillis() + "";
        } else {
            str2 = this.customFileName;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.mTargetDir);
        sb.append("/");
        sb.append(str2);
        if (TextUtils.isEmpty(str)) {
            str = ".jpg";
        }
        sb.append(str);
        return new File(sb.toString());
    }

    private File getImageCustomFile(Context context, String str) {
        if (TextUtils.isEmpty(this.mTargetDir)) {
            this.mTargetDir = getImageCacheDir(context).getAbsolutePath();
        }
        return new File(this.mTargetDir + "/" + str);
    }

    private static File getImageCacheDir(Context context) {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir != null) {
            if (externalFilesDir.mkdirs() || (externalFilesDir.exists() && externalFilesDir.isDirectory())) {
                return externalFilesDir;
            }
            return null;
        }
        if (Log.isLoggable(TAG, 6)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launch(final Context context) {
        List<InputStreamProvider> list = this.mStreamProviders;
        if (list == null || this.mPaths == null || (list.size() == 0 && this.mCompressListener != null)) {
            this.mCompressListener.onError(new NullPointerException("image file cannot be null"));
        }
        Iterator<InputStreamProvider> it = this.mStreamProviders.iterator();
        this.index = -1;
        while (it.hasNext()) {
            final InputStreamProvider next = it.next();
            AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() { // from class: com.luck.picture.lib.compress.Luban$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Luban.this.m52lambda$launch$0$comluckpicturelibcompressLuban(context, next);
                }
            });
            it.remove();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$launch$0$com-luck-picture-lib-compress-Luban  reason: not valid java name */
    public /* synthetic */ void m52lambda$launch$0$comluckpicturelibcompressLuban(Context context, InputStreamProvider inputStreamProvider) {
        try {
            boolean z = true;
            this.index++;
            Handler handler = this.mHandler;
            handler.sendMessage(handler.obtainMessage(1));
            File compress = compress(context, inputStreamProvider);
            List<LocalMedia> list = this.mediaList;
            if (list != null && list.size() > 0) {
                LocalMedia localMedia = this.mediaList.get(this.index);
                boolean isHttp = PictureMimeType.isHttp(compress.getAbsolutePath());
                localMedia.setCompressed(!isHttp);
                localMedia.setCompressPath(isHttp ? "" : compress.getAbsolutePath());
                if (this.index != this.mediaList.size() - 1) {
                    z = false;
                }
                if (z) {
                    Handler handler2 = this.mHandler;
                    handler2.sendMessage(handler2.obtainMessage(0, this.mediaList));
                    return;
                }
                return;
            }
            Handler handler3 = this.mHandler;
            handler3.sendMessage(handler3.obtainMessage(2, new IOException()));
        } catch (IOException e) {
            Handler handler4 = this.mHandler;
            handler4.sendMessage(handler4.obtainMessage(2, e));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File get(InputStreamProvider inputStreamProvider, Context context) throws IOException {
        try {
            return new Engine(inputStreamProvider, getImageCacheFile(context, Checker.SINGLE.extSuffix(inputStreamProvider)), this.focusAlpha, this.compressQuality).compress();
        } finally {
            inputStreamProvider.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<File> get(Context context) throws IOException {
        ArrayList arrayList = new ArrayList();
        Iterator<InputStreamProvider> it = this.mStreamProviders.iterator();
        while (it.hasNext()) {
            arrayList.add(compress(context, it.next()));
            it.remove();
        }
        return arrayList;
    }

    private File compress(Context context, InputStreamProvider inputStreamProvider) throws IOException {
        try {
            return compressReal(context, inputStreamProvider);
        } finally {
            inputStreamProvider.close();
        }
    }

    private File compressReal(Context context, InputStreamProvider inputStreamProvider) throws IOException {
        String extSuffix = Checker.SINGLE.extSuffix(inputStreamProvider.getMedia() != null ? inputStreamProvider.getMedia().getMimeType() : "");
        if (TextUtils.isEmpty(extSuffix)) {
            extSuffix = Checker.SINGLE.extSuffix(inputStreamProvider);
        }
        File imageCacheFile = getImageCacheFile(context, extSuffix);
        OnRenameListener onRenameListener = this.mRenameListener;
        if (onRenameListener != null) {
            imageCacheFile = getImageCustomFile(context, onRenameListener.rename(inputStreamProvider.getPath()));
        }
        CompressionPredicate compressionPredicate = this.mCompressionPredicate;
        if (compressionPredicate != null) {
            if (compressionPredicate.apply(inputStreamProvider.getPath()) && Checker.SINGLE.needCompress(this.mLeastCompressSize, inputStreamProvider.getPath())) {
                return new Engine(inputStreamProvider, imageCacheFile, this.focusAlpha, this.compressQuality).compress();
            }
            return new File(inputStreamProvider.getPath());
        } else if (Checker.SINGLE.extSuffix(inputStreamProvider).startsWith(".gif")) {
            return new File(inputStreamProvider.getPath());
        } else {
            if (Checker.SINGLE.needCompress(this.mLeastCompressSize, inputStreamProvider.getPath())) {
                return new Engine(inputStreamProvider, imageCacheFile, this.focusAlpha, this.compressQuality).compress();
            }
            return new File(inputStreamProvider.getPath());
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (this.mCompressListener == null) {
            return false;
        }
        int i = message.what;
        if (i == 0) {
            this.mCompressListener.onSuccess((List) message.obj);
        } else if (i == 1) {
            this.mCompressListener.onStart();
        } else if (i == 2) {
            this.mCompressListener.onError((Throwable) message.obj);
        }
        return false;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private int compressQuality;
        private Context context;
        private String customFileName;
        private boolean focusAlpha;
        private OnCompressListener mCompressListener;
        private CompressionPredicate mCompressionPredicate;
        private OnRenameListener mRenameListener;
        private String mTargetDir;
        private int mLeastCompressSize = 100;
        private List<String> mPaths = new ArrayList();
        private List<LocalMedia> mediaList = new ArrayList();
        private List<InputStreamProvider> mStreamProviders = new ArrayList();

        public Builder putGear(int i) {
            return this;
        }

        Builder(Context context) {
            this.context = context;
        }

        private Luban build() {
            return new Luban(this);
        }

        public Builder load(InputStreamProvider inputStreamProvider) {
            this.mStreamProviders.add(inputStreamProvider);
            return this;
        }

        public Builder load(final File file) {
            this.mStreamProviders.add(new InputStreamAdapter() { // from class: com.luck.picture.lib.compress.Luban.Builder.1
                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public LocalMedia getMedia() {
                    return null;
                }

                @Override // com.luck.picture.lib.compress.InputStreamAdapter
                public InputStream openInternal() throws IOException {
                    return new FileInputStream(file);
                }

                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public String getPath() {
                    return file.getAbsolutePath();
                }
            });
            return this;
        }

        private Builder load(final LocalMedia localMedia) {
            this.mStreamProviders.add(new InputStreamAdapter() { // from class: com.luck.picture.lib.compress.Luban.Builder.2
                @Override // com.luck.picture.lib.compress.InputStreamAdapter
                public InputStream openInternal() throws IOException {
                    String androidQToPath;
                    if (localMedia.isCut()) {
                        androidQToPath = localMedia.getCutPath();
                    } else {
                        androidQToPath = SdkVersionUtils.checkedAndroid_Q() ? localMedia.getAndroidQToPath() : localMedia.getPath();
                    }
                    return new FileInputStream(androidQToPath);
                }

                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public String getPath() {
                    return localMedia.isCut() ? localMedia.getCutPath() : SdkVersionUtils.checkedAndroid_Q() ? localMedia.getAndroidQToPath() : localMedia.getPath();
                }

                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public LocalMedia getMedia() {
                    return localMedia;
                }
            });
            return this;
        }

        public Builder load(final String str) {
            this.mStreamProviders.add(new InputStreamAdapter() { // from class: com.luck.picture.lib.compress.Luban.Builder.3
                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public LocalMedia getMedia() {
                    return null;
                }

                @Override // com.luck.picture.lib.compress.InputStreamAdapter
                public InputStream openInternal() throws IOException {
                    return new FileInputStream(str);
                }

                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public String getPath() {
                    return str;
                }
            });
            return this;
        }

        public <T> Builder load(List<T> list) {
            for (T t : list) {
                if (t instanceof String) {
                    load((String) t);
                } else if (t instanceof File) {
                    load((File) t);
                } else if (t instanceof Uri) {
                    load((Uri) t);
                } else {
                    throw new IllegalArgumentException("Incoming data type exception, it must be String, File, Uri or Bitmap");
                }
            }
            return this;
        }

        public <T> Builder loadMediaData(List<LocalMedia> list, String str) {
            this.mediaList = list;
            this.customFileName = str;
            boolean checkedAndroid_Q = SdkVersionUtils.checkedAndroid_Q();
            for (LocalMedia localMedia : list) {
                if (checkedAndroid_Q && !localMedia.isCut() && TextUtils.isEmpty(localMedia.getAndroidQToPath())) {
                    Uri.parse(localMedia.getPath());
                    localMedia.setAndroidQToPath(AndroidQTransformUtils.parseImagePathToAndroidQ(this.context, localMedia.getPath(), str, localMedia.getMimeType()));
                }
                load(localMedia);
            }
            return this;
        }

        public <T> Builder loadMediaData(List<LocalMedia> list) {
            this.mediaList = list;
            boolean checkedAndroid_Q = SdkVersionUtils.checkedAndroid_Q();
            for (LocalMedia localMedia : list) {
                if (checkedAndroid_Q && !localMedia.isCut() && TextUtils.isEmpty(localMedia.getAndroidQToPath())) {
                    Uri.parse(localMedia.getPath());
                    localMedia.setAndroidQToPath(AndroidQTransformUtils.parseImagePathToAndroidQ(this.context, localMedia.getPath(), "", localMedia.getMimeType()));
                }
                load(localMedia);
            }
            return this;
        }

        public Builder load(final Uri uri) {
            this.mStreamProviders.add(new InputStreamAdapter() { // from class: com.luck.picture.lib.compress.Luban.Builder.4
                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public LocalMedia getMedia() {
                    return null;
                }

                @Override // com.luck.picture.lib.compress.InputStreamAdapter
                public InputStream openInternal() throws IOException {
                    return Builder.this.context.getContentResolver().openInputStream(uri);
                }

                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public String getPath() {
                    return uri.getPath();
                }
            });
            return this;
        }

        public Builder setRenameListener(OnRenameListener onRenameListener) {
            this.mRenameListener = onRenameListener;
            return this;
        }

        public Builder setCompressListener(OnCompressListener onCompressListener) {
            this.mCompressListener = onCompressListener;
            return this;
        }

        public Builder setTargetDir(String str) {
            this.mTargetDir = str;
            return this;
        }

        public Builder setFocusAlpha(boolean z) {
            this.focusAlpha = z;
            return this;
        }

        public Builder setCompressQuality(int i) {
            this.compressQuality = i;
            return this;
        }

        public Builder ignoreBy(int i) {
            this.mLeastCompressSize = i;
            return this;
        }

        public Builder filter(CompressionPredicate compressionPredicate) {
            this.mCompressionPredicate = compressionPredicate;
            return this;
        }

        public void launch() {
            build().launch(this.context);
        }

        public File get(final String str) throws IOException {
            return build().get(new InputStreamAdapter() { // from class: com.luck.picture.lib.compress.Luban.Builder.5
                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public LocalMedia getMedia() {
                    return null;
                }

                @Override // com.luck.picture.lib.compress.InputStreamAdapter
                public InputStream openInternal() throws IOException {
                    return new FileInputStream(str);
                }

                @Override // com.luck.picture.lib.compress.InputStreamProvider
                public String getPath() {
                    return str;
                }
            }, this.context);
        }

        public List<File> get() throws IOException {
            return build().get(this.context);
        }
    }
}
