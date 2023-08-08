package com.weigan.loopview;

import android.view.GestureDetector;
import android.view.MotionEvent;

/* loaded from: classes.dex */
final class LoopViewGestureListener extends GestureDetector.SimpleOnGestureListener {
    final LoopView loopView;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoopViewGestureListener(LoopView loopView) {
        this.loopView = loopView;
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public final boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.loopView.scrollBy(f2);
        return true;
    }
}
