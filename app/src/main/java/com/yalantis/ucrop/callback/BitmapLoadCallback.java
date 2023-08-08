package com.yalantis.ucrop.callback;

import android.graphics.Bitmap;
import android.net.Uri;
import com.yalantis.ucrop.model.ExifInfo;

/* loaded from: classes.dex */
public interface BitmapLoadCallback {
    void onBitmapLoaded(Bitmap bitmap, ExifInfo exifInfo, Uri uri, Uri uri2);

    void onFailure(Exception exc);
}
