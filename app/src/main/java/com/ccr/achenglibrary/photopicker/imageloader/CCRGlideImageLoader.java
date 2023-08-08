package com.ccr.achenglibrary.photopicker.imageloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;

/* loaded from: classes.dex */
public class CCRGlideImageLoader extends CCRImageLoader {
    @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader
    public void display(final ImageView imageView, String str, int i, int i2, int i3, int i4, final CCRImageLoader.DisplayDelegate displayDelegate) {
        final String path = getPath(str);
        Glide.with(getActivity(imageView)).load(path).apply((BaseRequestOptions<?>) new RequestOptions().placeholder(i).error(i2).override(i3, i4).dontAnimate()).listener(new RequestListener<Drawable>() { // from class: com.ccr.achenglibrary.photopicker.imageloader.CCRGlideImageLoader.1
            @Override // com.bumptech.glide.request.RequestListener
            public boolean onLoadFailed(GlideException glideException, Object obj, Target<Drawable> target, boolean z) {
                return false;
            }

            @Override // com.bumptech.glide.request.RequestListener
            public boolean onResourceReady(Drawable drawable, Object obj, Target<Drawable> target, DataSource dataSource, boolean z) {
                CCRImageLoader.DisplayDelegate displayDelegate2 = displayDelegate;
                if (displayDelegate2 != null) {
                    displayDelegate2.onSuccess(imageView, path);
                    return false;
                }
                return false;
            }
        }).into(imageView);
    }

    @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader
    public void download(String str, final CCRImageLoader.DownloadDelegate downloadDelegate) {
        final String path = getPath(str);
        Glide.with(CCRPhotoPickerUtil.sApp).asBitmap().load(path).into((RequestBuilder<Bitmap>) new SimpleTarget<Bitmap>() { // from class: com.ccr.achenglibrary.photopicker.imageloader.CCRGlideImageLoader.2
            @Override // com.bumptech.glide.request.target.Target
            public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
            }

            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                CCRImageLoader.DownloadDelegate downloadDelegate2 = downloadDelegate;
                if (downloadDelegate2 != null) {
                    downloadDelegate2.onSuccess(path, bitmap);
                }
            }

            @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
            public void onLoadFailed(Drawable drawable) {
                CCRImageLoader.DownloadDelegate downloadDelegate2 = downloadDelegate;
                if (downloadDelegate2 != null) {
                    downloadDelegate2.onFailed(path);
                }
            }
        });
    }

    @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader
    public void pause(Activity activity) {
        Glide.with(activity).pauseRequests();
    }

    @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader
    public void resume(Activity activity) {
        Glide.with(activity).resumeRequestsRecursive();
    }
}
