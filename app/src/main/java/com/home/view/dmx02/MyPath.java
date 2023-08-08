package com.home.view.dmx02;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/* compiled from: BaseAction.java */
/* loaded from: classes.dex */
class MyPath extends BaseAction {
    private Path path;
    private int size;

    MyPath() {
        this.path = new Path();
        this.size = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MyPath(float f, float f2, int i, int i2) {
        super(i2);
        Path path = new Path();
        this.path = path;
        this.size = i;
        path.moveTo(f, f2);
        this.path.lineTo(f, f2);
    }

    @Override // com.home.view.dmx02.BaseAction
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(this.color);
        paint.setStrokeWidth(this.size);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPath(this.path, paint);
    }

    @Override // com.home.view.dmx02.BaseAction
    public void move(float f, float f2) {
        this.path.lineTo(f, f2);
    }
}
