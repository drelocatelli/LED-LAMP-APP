package top.defaults.colorpicker;

import android.view.MotionEvent;

/* loaded from: classes.dex */
class ThrottledTouchEventHandler {
    private long lastPassedEventTime;
    private int minInterval;
    private Updatable updatable;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ThrottledTouchEventHandler(Updatable updatable) {
        this(16, updatable);
    }

    private ThrottledTouchEventHandler(int i, Updatable updatable) {
        this.lastPassedEventTime = 0L;
        this.minInterval = i;
        this.updatable = updatable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTouchEvent(MotionEvent motionEvent) {
        if (this.updatable == null) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.lastPassedEventTime <= this.minInterval) {
            return;
        }
        this.lastPassedEventTime = currentTimeMillis;
        this.updatable.update(motionEvent);
    }
}
