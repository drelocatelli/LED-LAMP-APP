package com.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class TextViewBorder extends TextView {
    private Paint paint;

    public TextViewBorder(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = new Paint();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        this.paint.setStrokeWidth(2.0f);
        this.paint.setColor(-11567999);
        float f = width;
        canvas.drawLine(0.0f, 0.0f, f, 0.0f, this.paint);
        float f2 = height;
        canvas.drawLine(0.0f, f2, f, f2, this.paint);
        canvas.drawLine(0.0f, 0.0f, 0.0f, f2, this.paint);
        canvas.drawLine(f, 0.0f, f, f2, this.paint);
    }
}
