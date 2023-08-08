package com.weigan.loopview;

import androidx.appcompat.widget.ActivityChooserView;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SmoothScrollTimerTask implements Runnable {
    final LoopView loopView;
    int offset;
    int realTotalOffset = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
    int realOffset = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SmoothScrollTimerTask(LoopView loopView, int i) {
        this.loopView = loopView;
        this.offset = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.realTotalOffset == Integer.MAX_VALUE) {
            this.realTotalOffset = this.offset;
        }
        int i = this.realTotalOffset;
        int i2 = (int) (i * 0.1f);
        this.realOffset = i2;
        if (i2 == 0) {
            if (i < 0) {
                this.realOffset = -1;
            } else {
                this.realOffset = 1;
            }
        }
        if (Math.abs(i) <= 0) {
            this.loopView.cancelFuture();
            this.loopView.handler.sendEmptyMessage(3000);
            return;
        }
        this.loopView.totalScrollY += this.realOffset;
        this.loopView.handler.sendEmptyMessage(1000);
        this.realTotalOffset -= this.realOffset;
    }
}
