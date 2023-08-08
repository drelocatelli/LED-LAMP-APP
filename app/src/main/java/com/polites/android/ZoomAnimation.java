package com.polites.android;

import android.graphics.PointF;

/* loaded from: classes.dex */
public class ZoomAnimation implements Animation {
    private float scaleDiff;
    private float startScale;
    private float startX;
    private float startY;
    private float touchX;
    private float touchY;
    private float xDiff;
    private float yDiff;
    private float zoom;
    private ZoomAnimationListener zoomAnimationListener;
    private boolean firstFrame = true;
    private long animationLengthMS = 200;
    private long totalTime = 0;

    @Override // com.polites.android.Animation
    public boolean update(GestureImageView gestureImageView, long j) {
        if (this.firstFrame) {
            this.firstFrame = false;
            this.startX = gestureImageView.getImageX();
            this.startY = gestureImageView.getImageY();
            float scale = gestureImageView.getScale();
            this.startScale = scale;
            float f = (this.zoom * scale) - scale;
            this.scaleDiff = f;
            if (f > 0.0f) {
                VectorF vectorF = new VectorF();
                vectorF.setStart(new PointF(this.touchX, this.touchY));
                vectorF.setEnd(new PointF(this.startX, this.startY));
                vectorF.calculateAngle();
                vectorF.length = vectorF.calculateLength() * this.zoom;
                vectorF.calculateEndPoint();
                this.xDiff = vectorF.end.x - this.startX;
                this.yDiff = vectorF.end.y - this.startY;
            } else {
                this.xDiff = gestureImageView.getCenterX() - this.startX;
                this.yDiff = gestureImageView.getCenterY() - this.startY;
            }
        }
        long j2 = this.totalTime + j;
        this.totalTime = j2;
        float f2 = ((float) j2) / ((float) this.animationLengthMS);
        if (f2 >= 1.0f) {
            float f3 = this.scaleDiff + this.startScale;
            float f4 = this.xDiff + this.startX;
            float f5 = this.yDiff + this.startY;
            ZoomAnimationListener zoomAnimationListener = this.zoomAnimationListener;
            if (zoomAnimationListener != null) {
                zoomAnimationListener.onZoom(f3, f4, f5);
                this.zoomAnimationListener.onComplete();
            }
            return false;
        } else if (f2 > 0.0f) {
            float f6 = (this.scaleDiff * f2) + this.startScale;
            float f7 = (this.xDiff * f2) + this.startX;
            float f8 = (f2 * this.yDiff) + this.startY;
            ZoomAnimationListener zoomAnimationListener2 = this.zoomAnimationListener;
            if (zoomAnimationListener2 != null) {
                zoomAnimationListener2.onZoom(f6, f7, f8);
                return true;
            }
            return true;
        } else {
            return true;
        }
    }

    public void reset() {
        this.firstFrame = true;
        this.totalTime = 0L;
    }

    public float getZoom() {
        return this.zoom;
    }

    public void setZoom(float f) {
        this.zoom = f;
    }

    public float getTouchX() {
        return this.touchX;
    }

    public void setTouchX(float f) {
        this.touchX = f;
    }

    public float getTouchY() {
        return this.touchY;
    }

    public void setTouchY(float f) {
        this.touchY = f;
    }

    public long getAnimationLengthMS() {
        return this.animationLengthMS;
    }

    public void setAnimationLengthMS(long j) {
        this.animationLengthMS = j;
    }

    public ZoomAnimationListener getZoomAnimationListener() {
        return this.zoomAnimationListener;
    }

    public void setZoomAnimationListener(ZoomAnimationListener zoomAnimationListener) {
        this.zoomAnimationListener = zoomAnimationListener;
    }
}
