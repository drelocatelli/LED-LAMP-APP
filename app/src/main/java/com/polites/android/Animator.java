package com.polites.android;

/* loaded from: classes.dex */
public class Animator extends Thread {
    private boolean active;
    private Animation animation;
    private long lastTime;
    private boolean running;
    private GestureImageView view;

    public Animator(GestureImageView gestureImageView, String str) {
        super(str);
        this.running = false;
        this.active = false;
        this.lastTime = -1L;
        this.view = gestureImageView;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.running = true;
        while (this.running) {
            while (this.active && this.animation != null) {
                long currentTimeMillis = System.currentTimeMillis();
                this.active = this.animation.update(this.view, currentTimeMillis - this.lastTime);
                this.view.redraw();
                this.lastTime = currentTimeMillis;
                while (this.active) {
                    try {
                    } catch (InterruptedException unused) {
                        this.active = false;
                    }
                    if (this.view.waitForDraw(32L)) {
                        break;
                    }
                }
            }
            synchronized (this) {
                if (this.running) {
                    try {
                        wait();
                    } catch (InterruptedException unused2) {
                    }
                }
            }
        }
    }

    public synchronized void finish() {
        this.running = false;
        this.active = false;
        notifyAll();
    }

    public void play(Animation animation) {
        if (this.active) {
            cancel();
        }
        this.animation = animation;
        activate();
    }

    public synchronized void activate() {
        this.lastTime = System.currentTimeMillis();
        this.active = true;
        notifyAll();
    }

    public void cancel() {
        this.active = false;
    }
}
