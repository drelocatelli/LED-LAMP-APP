package com.luck.picture.lib.engine;

import android.content.Context;
import android.widget.ImageView;

/* loaded from: classes.dex */
public interface ImageEngine {
    void loadAsBitmapGridImage(Context context, String str, ImageView imageView, int i);

    void loadAsGifImage(Context context, String str, ImageView imageView);

    void loadFolderAsBitmapImage(Context context, String str, ImageView imageView, int i);

    void loadImage(Context context, String str, ImageView imageView);
}
