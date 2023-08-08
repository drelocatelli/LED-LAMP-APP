package com.polites.android;

/* loaded from: classes.dex */
public class MoveAnimation implements Animation {
    private MoveAnimationListener moveAnimationListener;
    private float startX;
    private float startY;
    private float targetX;
    private float targetY;
    private boolean firstFrame = true;
    private long animationTimeMS = 100;
    private long totalTime = 0;

    @Override // com.polites.android.Animation
    public boolean update(GestureImageView gestureImageView, long j) {
        this.totalTime += j;
        if (this.firstFrame) {
            this.firstFrame = false;
            this.startX = gestureImageView.getImageX();
            this.startY = gestureImageView.getImageY();
        }
        long j2 = this.totalTime;
        long j3 = this.animationTimeMS;
        if (j2 < j3) {
            float f = ((float) j2) / ((float) j3);
            float f2 = this.targetX;
            float f3 = this.startX;
            float f4 = ((f2 - f3) * f) + f3;
            float f5 = this.targetY;
            float f6 = this.startY;
            float f7 = ((f5 - f6) * f) + f6;
            MoveAnimationListener moveAnimationListener = this.moveAnimationListener;
            if (moveAnimationListener != null) {
                moveAnimationListener.onMove(f4, f7);
                return true;
            }
            return true;
        }
        MoveAnimationListener moveAnimationListener2 = this.moveAnimationListener;
        if (moveAnimationListener2 != null) {
            moveAnimationListener2.onMove(this.targetX, this.targetY);
        }
        return false;
    }

    public void reset() {
        this.firstFrame = true;
        this.totalTime = 0L;
    }

    public float getTargetX() {
        return this.targetX;
    }

    public void setTargetX(float f) {
        this.targetX = f;
    }

    public float getTargetY() {
        return this.targetY;
    }

    public void setTargetY(float f) {
        this.targetY = f;
    }

    public long getAnimationTimeMS() {
        return this.animationTimeMS;
    }

    public void setAnimationTimeMS(long j) {
        this.animationTimeMS = j;
    }

    public void setMoveAnimationListener(MoveAnimationListener moveAnimationListener) {
        this.moveAnimationListener = moveAnimationListener;
    }
}
