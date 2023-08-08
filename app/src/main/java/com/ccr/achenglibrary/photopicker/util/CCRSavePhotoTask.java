package com.ccr.achenglibrary.photopicker.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.util.CCRAsyncTask;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;

/* loaded from: classes.dex */
public class CCRSavePhotoTask extends CCRAsyncTask<Void, Void> {
    private SoftReference<Bitmap> mBitmap;
    private Context mContext;
    private File mNewFile;

    public CCRSavePhotoTask(CCRAsyncTask.Callback<Void> callback, Context context, File file) {
        super(callback);
        this.mContext = context.getApplicationContext();
        this.mNewFile = file;
    }

    public void setBitmapAndPerform(Bitmap bitmap) {
        this.mBitmap = new SoftReference<>(bitmap);
        if (Build.VERSION.SDK_INT >= 11) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else {
            execute(new Void[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x005b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v0, types: [int] */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r0v10, types: [java.io.OutputStream, java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r1v4, types: [android.graphics.Bitmap] */
    @Override // android.os.AsyncTask
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Void doInBackground(Void... voidArr) {
        ?? r0;
        Throwable th;
        try {
        } catch (IOException unused) {
            r0 = R.string.bga_pp_save_img_failure;
            CCRPhotoPickerUtil.showSafe((int) r0);
        }
        try {
            try {
                r0 = new FileOutputStream(this.mNewFile);
            } catch (Exception unused2) {
                r0 = 0;
            } catch (Throwable th2) {
                r0 = 0;
                th = th2;
                if (r0 != 0) {
                }
                recycleBitmap();
                throw th;
            }
            try {
                this.mBitmap.get().compress(Bitmap.CompressFormat.PNG, 100, r0);
                r0.flush();
                CCRPhotoPickerUtil.showSafe(this.mContext.getString(R.string.bga_pp_save_img_success_folder, this.mNewFile.getParentFile().getAbsolutePath()));
                updateImg(this.mNewFile);
                r0.close();
                r0 = r0;
            } catch (Exception unused3) {
                CCRPhotoPickerUtil.showSafe(R.string.bga_pp_save_img_failure);
                if (r0 != 0) {
                    r0.close();
                    r0 = r0;
                }
                recycleBitmap();
                return null;
            }
            recycleBitmap();
            return null;
        } catch (Throwable th3) {
            th = th3;
            if (r0 != 0) {
                try {
                    r0.close();
                } catch (IOException unused4) {
                    CCRPhotoPickerUtil.showSafe(R.string.bga_pp_save_img_failure);
                }
            }
            recycleBitmap();
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ccr.achenglibrary.photopicker.util.CCRAsyncTask, android.os.AsyncTask
    public void onCancelled() {
        super.onCancelled();
        recycleBitmap();
    }

    private void recycleBitmap() {
        SoftReference<Bitmap> softReference = this.mBitmap;
        if (softReference == null || softReference.get() == null || this.mBitmap.get().isRecycled()) {
            return;
        }
        this.mBitmap.get().recycle();
        this.mBitmap = null;
    }

    private void updateImg(File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        this.mContext.sendBroadcast(intent);
        Toast.makeText(this.mContext, "更新啦", 0).show();
        Log.d("Acheng", "更新啦");
    }
}
