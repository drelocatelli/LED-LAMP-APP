package com.ccr.achenglibrary.photopicker.imageloader;

import android.app.Activity;
import android.widget.ImageView;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader;

/* loaded from: classes.dex */
public class CCRPicassoImageLoader extends CCRImageLoader {
    @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader
    public void pause(Activity activity) {
    }

    @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader
    public void resume(Activity activity) {
    }

    @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader
    public void display(ImageView imageView, String str, int i, int i2, int i3, int i4, CCRImageLoader.DisplayDelegate displayDelegate) {
        getPath(str);
        getActivity(imageView);
    }

    @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader
    public void download(String str, CCRImageLoader.DownloadDelegate downloadDelegate) {
        getPath(str);
    }
}
