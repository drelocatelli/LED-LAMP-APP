package com.home.view.dmx02;

import android.graphics.Canvas;

/* loaded from: classes.dex */
abstract class BaseAction {
    public int color;

    public abstract void draw(Canvas canvas);

    public abstract void move(float f, float f2);

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAction() {
        this.color = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseAction(int i) {
        this.color = i;
    }
}
