package com.yalantis.ucrop.callback;

import android.net.Uri;

/* loaded from: classes.dex */
public interface BitmapCropCallback {
    void onBitmapCropped(Uri uri, int i, int i2, int i3, int i4);

    void onCropFailure(Throwable th);
}
