package com.home.view.dmx02;

import android.graphics.Canvas;
import android.graphics.Paint;

/* compiled from: BaseAction.java */
/* loaded from: classes.dex */
class MyPoint extends BaseAction {
    private float x;
    private float y;

    @Override // com.home.view.dmx02.BaseAction
    public void move(float f, float f2) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MyPoint(float f, float f2, int i) {
        super(i);
        this.x = f;
        this.y = f2;
    }

    @Override // com.home.view.dmx02.BaseAction
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(this.color);
        canvas.drawPoint(this.x, this.y, paint);
    }
}
