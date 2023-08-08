package com.home.view.dmx02;

import android.graphics.Canvas;
import android.graphics.Paint;

/* compiled from: BaseAction.java */
/* loaded from: classes.dex */
class MyFillCircle extends BaseAction {
    private float radius;
    private int size;
    private float startX;
    private float startY;
    private float stopX;
    private float stopY;

    public MyFillCircle(float f, float f2, int i, int i2) {
        super(i2);
        this.startX = f;
        this.startY = f2;
        this.stopX = f;
        this.stopY = f2;
        this.radius = 0.0f;
        this.size = i;
    }

    @Override // com.home.view.dmx02.BaseAction
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(this.color);
        paint.setStrokeWidth(this.size);
        canvas.drawCircle((this.startX + this.stopX) / 2.0f, (this.startY + this.stopY) / 2.0f, this.radius, paint);
    }

    @Override // com.home.view.dmx02.BaseAction
    public void move(float f, float f2) {
        this.stopX = f;
        this.stopY = f2;
        float f3 = this.startX;
        float f4 = (f - f3) * (f - f3);
        float f5 = this.startY;
        this.radius = (float) (Math.sqrt(f4 + ((f2 - f5) * (f2 - f5))) / 2.0d);
    }
}
