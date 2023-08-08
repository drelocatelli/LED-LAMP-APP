package com.home.view.dmx02;

import android.graphics.Canvas;
import android.graphics.Paint;

/* compiled from: BaseAction.java */
/* loaded from: classes.dex */
class MyRect extends BaseAction {
    private int size;
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    MyRect() {
        this.startX = 0.0f;
        this.startY = 0.0f;
        this.stopX = 0.0f;
        this.stopY = 0.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MyRect(float f, float f2, int i, int i2) {
        super(i2);
        this.startX = f;
        this.startY = f2;
        this.stopX = f;
        this.stopY = f2;
        this.size = i;
    }

    @Override // com.home.view.dmx02.BaseAction
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(this.color);
        paint.setStrokeWidth(this.size);
        canvas.drawRect(this.startX, this.startY, this.stopX, this.stopY, paint);
    }

    @Override // com.home.view.dmx02.BaseAction
    public void move(float f, float f2) {
        this.stopX = f;
        this.stopY = f2;
    }
}
