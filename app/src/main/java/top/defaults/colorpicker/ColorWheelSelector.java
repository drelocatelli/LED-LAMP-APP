package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class ColorWheelSelector extends View {
    private PointF currentPoint;
    private Paint selectorPaint;
    private float selectorRadiusPx;

    public ColorWheelSelector(Context context) {
        this(context, null);
    }

    public ColorWheelSelector(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorWheelSelector(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.selectorRadiusPx = 27.0f;
        this.currentPoint = new PointF();
        Paint paint = new Paint(1);
        this.selectorPaint = paint;
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.selectorPaint.setStyle(Paint.Style.STROKE);
        this.selectorPaint.setStrokeWidth(4.0f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(this.currentPoint.x - this.selectorRadiusPx, this.currentPoint.y, this.currentPoint.x + this.selectorRadiusPx, this.currentPoint.y, this.selectorPaint);
        canvas.drawLine(this.currentPoint.x, this.currentPoint.y - this.selectorRadiusPx, this.currentPoint.x, this.currentPoint.y + this.selectorRadiusPx, this.selectorPaint);
        canvas.drawCircle(this.currentPoint.x, this.currentPoint.y, this.selectorRadiusPx * 0.66f, this.selectorPaint);
    }

    public void setSelectorRadiusPx(float f) {
        this.selectorRadiusPx = f;
    }

    public void setCurrentPoint(PointF pointF) {
        this.currentPoint = pointF;
        invalidate();
    }
}
