package com.polites.android;

import android.view.GestureDetector;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class FlingListener extends GestureDetector.SimpleOnGestureListener {
    private float velocityX;
    private float velocityY;

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.velocityX = f;
        this.velocityY = f2;
        return true;
    }

    public float getVelocityX() {
        return this.velocityX;
    }

    public float getVelocityY() {
        return this.velocityY;
    }
}
