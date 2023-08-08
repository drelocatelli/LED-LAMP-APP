package com.common.pictureselector;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;

/* loaded from: classes.dex */
public class GlideEngine implements ImageEngine {
    private static GlideEngine instance;

    @Override // com.luck.picture.lib.engine.ImageEngine
    public void loadImage(Context context, String str, ImageView imageView) {
        Glide.with(context).load(str).into(imageView);
    }

    @Override // com.luck.picture.lib.engine.ImageEngine
    public void loadFolderAsBitmapImage(final Context context, String str, final ImageView imageView, int i) {
        Glide.with(context).asBitmap().override(SubsamplingScaleImageView.ORIENTATION_180, SubsamplingScaleImageView.ORIENTATION_180).centerCrop().sizeMultiplier(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(i).load(str).into((RequestBuilder) new BitmapImageViewTarget(imageView) { // from class: com.common.pictureselector.GlideEngine.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.bumptech.glide.request.target.BitmapImageViewTarget, com.bumptech.glide.request.target.ImageViewTarget
            public void setResource(Bitmap bitmap) {
                RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
                create.setCornerRadius(8.0f);
                imageView.setImageDrawable(create);
            }
        });
    }

    @Override // com.luck.picture.lib.engine.ImageEngine
    public void loadAsGifImage(Context context, String str, ImageView imageView) {
        Glide.with(context).asGif().load(str).into(imageView);
    }

    @Override // com.luck.picture.lib.engine.ImageEngine
    public void loadAsBitmapGridImage(Context context, String str, ImageView imageView, int i) {
        Glide.with(context).asBitmap().override(200, 200).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(i).load(str).into(imageView);
    }

    private GlideEngine() {
    }

    public static GlideEngine createGlideEngine() {
        if (instance == null) {
            synchronized (GlideEngine.class) {
                if (instance == null) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }
}
