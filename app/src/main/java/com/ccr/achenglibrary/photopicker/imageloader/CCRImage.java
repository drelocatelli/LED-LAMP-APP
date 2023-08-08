package com.ccr.achenglibrary.photopicker.imageloader;

import android.app.Activity;
import android.widget.ImageView;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader;

/* loaded from: classes.dex */
public class CCRImage {
    private static CCRImageLoader sImageLoader;

    private CCRImage() {
    }

    private static final CCRImageLoader getImageLoader() {
        if (sImageLoader == null) {
            synchronized (CCRImage.class) {
            }
        }
        return sImageLoader;
    }

    private static final boolean isClassExists(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }

    public static void display(ImageView imageView, int i, int i2, String str, int i3, int i4, CCRImageLoader.DisplayDelegate displayDelegate) {
        getImageLoader().display(imageView, str, i, i2, i3, i4, displayDelegate);
    }

    public static void display(ImageView imageView, int i, String str, int i2, int i3, CCRImageLoader.DisplayDelegate displayDelegate) {
        display(imageView, i, i, str, i2, i3, displayDelegate);
    }

    public static void display(ImageView imageView, int i, String str, int i2, int i3) {
        display(imageView, i, str, i2, i3, null);
    }

    public static void display(ImageView imageView, int i, String str, int i2) {
        display(imageView, i, str, i2, i2);
    }

    public static void download(String str, CCRImageLoader.DownloadDelegate downloadDelegate) {
        getImageLoader().download(str, downloadDelegate);
    }

    public static void pause(Activity activity) {
        getImageLoader().pause(activity);
    }

    public static void resume(Activity activity) {
        getImageLoader().resume(activity);
    }
}
