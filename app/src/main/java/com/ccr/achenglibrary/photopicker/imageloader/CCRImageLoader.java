package com.ccr.achenglibrary.photopicker.imageloader;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import org.apache.http.HttpHost;

/* loaded from: classes.dex */
public abstract class CCRImageLoader {

    /* loaded from: classes.dex */
    public interface DisplayDelegate {
        void onSuccess(View view, String str);
    }

    /* loaded from: classes.dex */
    public interface DownloadDelegate {
        void onFailed(String str);

        void onSuccess(String str, Bitmap bitmap);
    }

    public abstract void display(ImageView imageView, String str, int i, int i2, int i3, int i4, DisplayDelegate displayDelegate);

    public abstract void download(String str, DownloadDelegate downloadDelegate);

    public abstract void pause(Activity activity);

    public abstract void resume(Activity activity);

    /* JADX INFO: Access modifiers changed from: protected */
    public String getPath(String str) {
        if (str == null) {
            str = "";
        }
        if (str.startsWith(HttpHost.DEFAULT_SCHEME_NAME) || str.startsWith("file")) {
            return str;
        }
        return "file://" + str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Activity getActivity(View view) {
        for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
        }
        return null;
    }
}
