package com.example.linechartlibrary;

import android.content.Context;
import android.os.SystemClock;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/* loaded from: classes.dex */
public class ZoomerCompat {
    private static final int DEFAULT_SHORT_ANIMATION_DURATION = 200;
    private float mCurrentZoom;
    private float mEndZoom;
    private long mStartRTC;
    private boolean mFinished = true;
    private Interpolator mInterpolator = new DecelerateInterpolator();
    private long mAnimationDurationMillis = 200;

    public ZoomerCompat(Context context) {
    }

    public void forceFinished(boolean z) {
        this.mFinished = z;
    }

    public void abortAnimation() {
        this.mFinished = true;
        this.mCurrentZoom = this.mEndZoom;
    }

    public void startZoom(float f) {
        this.mStartRTC = SystemClock.elapsedRealtime();
        this.mEndZoom = f;
        this.mFinished = false;
        this.mCurrentZoom = 1.0f;
    }

    public boolean computeZoom() {
        if (this.mFinished) {
            return false;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime() - this.mStartRTC;
        long j = this.mAnimationDurationMillis;
        if (elapsedRealtime >= j) {
            this.mFinished = true;
            this.mCurrentZoom = this.mEndZoom;
            return false;
        }
        this.mCurrentZoom = this.mEndZoom * this.mInterpolator.getInterpolation((((float) elapsedRealtime) * 1.0f) / ((float) j));
        return true;
    }

    public float getCurrZoom() {
        return this.mCurrentZoom;
    }
}
