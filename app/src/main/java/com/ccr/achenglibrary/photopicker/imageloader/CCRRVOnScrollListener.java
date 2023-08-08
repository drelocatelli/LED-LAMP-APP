package com.ccr.achenglibrary.photopicker.imageloader;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class CCRRVOnScrollListener extends RecyclerView.OnScrollListener {
    private Activity mActivity;

    public CCRRVOnScrollListener(Activity activity) {
        this.mActivity = activity;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
    public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        if (i == 0) {
            CCRImage.resume(this.mActivity);
        } else if (i == 1) {
            CCRImage.pause(this.mActivity);
        }
    }
}
