package com.polites.android;

/* loaded from: classes.dex */
public class FlingAnimation implements Animation {
    private FlingAnimationListener listener;
    private float velocityX;
    private float velocityY;
    private float factor = 0.95f;
    private float threshold = 10.0f;

    @Override // com.polites.android.Animation
    public boolean update(GestureImageView gestureImageView, long j) {
        float f = ((float) j) / 1000.0f;
        float f2 = this.velocityX;
        float f3 = f2 * f;
        float f4 = this.velocityY;
        float f5 = f * f4;
        float f6 = this.factor;
        float f7 = f2 * f6;
        this.velocityX = f7;
        this.velocityY = f4 * f6;
        boolean z = Math.abs(f7) > this.threshold && Math.abs(this.velocityY) > this.threshold;
        FlingAnimationListener flingAnimationListener = this.listener;
        if (flingAnimationListener != null) {
            flingAnimationListener.onMove(f3, f5);
            if (!z) {
                this.listener.onComplete();
            }
        }
        return z;
    }

    public void setVelocityX(float f) {
        this.velocityX = f;
    }

    public void setVelocityY(float f) {
        this.velocityY = f;
    }

    public void setFactor(float f) {
        this.factor = f;
    }

    public void setListener(FlingAnimationListener flingAnimationListener) {
        this.listener = flingAnimationListener;
    }
}
