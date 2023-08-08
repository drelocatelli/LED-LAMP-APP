package com.weigan.loopview;

import android.util.Log;

/* loaded from: classes.dex */
final class InertiaTimerTask implements Runnable {
    float a = 2.14748365E9f;
    final LoopView loopView;
    final float velocityY;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InertiaTimerTask(LoopView loopView, float f) {
        this.loopView = loopView;
        this.velocityY = f;
    }

    @Override // java.lang.Runnable
    public final void run() {
        LoopView loopView;
        LoopView loopView2;
        if (this.a == 2.14748365E9f) {
            if (Math.abs(this.velocityY) > 2000.0f) {
                if (this.velocityY > 0.0f) {
                    this.a = 2000.0f;
                } else {
                    this.a = -2000.0f;
                }
            } else {
                this.a = this.velocityY;
            }
        }
        if (Math.abs(this.a) >= 0.0f && Math.abs(this.a) <= 20.0f) {
            Log.i("gy", "WHAT_SMOOTH_SCROLL_INERTIA");
            this.loopView.handler.sendEmptyMessageDelayed(MessageHandler.WHAT_SMOOTH_SCROLL_INERTIA, 60L);
            this.loopView.cancelFuture();
            this.loopView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        this.loopView.totalScrollY -= (int) ((this.a * 10.0f) / 1000.0f);
        if (!this.loopView.isLoop) {
            float f = this.loopView.lineSpacingMultiplier * this.loopView.itemTextHeight;
            if (this.loopView.totalScrollY <= ((int) ((-this.loopView.initPosition) * f))) {
                this.a = 40.0f;
                this.loopView.totalScrollY = (int) ((-loopView2.initPosition) * f);
            } else if (this.loopView.totalScrollY >= ((int) (((this.loopView.items.size() - 1) - this.loopView.initPosition) * f))) {
                this.loopView.totalScrollY = (int) (((loopView.items.size() - 1) - this.loopView.initPosition) * f);
                this.a = -40.0f;
            }
        }
        float f2 = this.a;
        if (f2 < 0.0f) {
            this.a = f2 + 20.0f;
        } else {
            this.a = f2 - 20.0f;
        }
        this.loopView.handler.sendEmptyMessage(1000);
    }
}
