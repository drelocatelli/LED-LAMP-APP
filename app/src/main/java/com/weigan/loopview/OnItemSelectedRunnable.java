package com.weigan.loopview;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class OnItemSelectedRunnable implements Runnable {
    final LoopView loopView;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OnItemSelectedRunnable(LoopView loopView) {
        this.loopView = loopView;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.loopView.onItemSelectedListener.onItemSelected(this.loopView.getSelectedItem());
    }
}
