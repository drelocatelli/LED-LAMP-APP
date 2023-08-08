package com.ccr.achenglibrary.photopicker.listener;

import android.view.View;

/* loaded from: classes.dex */
public abstract class CCROnNoDoubleClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private int mThrottleFirstTime;

    public abstract void onNoDoubleClick(View view);

    public CCROnNoDoubleClickListener() {
        this.mThrottleFirstTime = 1000;
        this.mLastClickTime = 0L;
    }

    public CCROnNoDoubleClickListener(int i) {
        this.mLastClickTime = 0L;
        this.mThrottleFirstTime = i;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastClickTime > this.mThrottleFirstTime) {
            this.mLastClickTime = currentTimeMillis;
            onNoDoubleClick(view);
        }
    }
}
