package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
public class ColorWheelView extends FrameLayout implements ColorObservable, Updatable {
    private float centerX;
    private float centerY;
    private int currentColor;
    private PointF currentPoint;
    private ColorObservableEmitter emitter;
    private ThrottledTouchEventHandler handler;
    private boolean onlyUpdateOnTouchEventUp;
    private float radius;
    private ColorWheelSelector selector;
    private float selectorRadiusPx;

    public ColorWheelView(Context context) {
        this(context, null);
    }

    public ColorWheelView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorWheelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.selectorRadiusPx = 27.0f;
        this.currentPoint = new PointF();
        this.currentColor = -65281;
        this.emitter = new ColorObservableEmitter();
        this.handler = new ThrottledTouchEventHandler(this);
        this.selectorRadiusPx = getResources().getDisplayMetrics().density * 9.0f;
        ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        ColorWheelPalette colorWheelPalette = new ColorWheelPalette(context);
        int i2 = (int) this.selectorRadiusPx;
        colorWheelPalette.setPadding(i2, i2, i2, i2);
        addView(colorWheelPalette, layoutParams);
        ViewGroup.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-1, -1);
        ColorWheelSelector colorWheelSelector = new ColorWheelSelector(context);
        this.selector = colorWheelSelector;
        colorWheelSelector.setSelectorRadiusPx(this.selectorRadiusPx);
        addView(this.selector, layoutParams2);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int min = Math.min(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(min, 1073741824), View.MeasureSpec.makeMeasureSpec(min, 1073741824));
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int paddingLeft = (i - getPaddingLeft()) - getPaddingRight();
        int paddingTop = (i2 - getPaddingTop()) - getPaddingBottom();
        float min = (Math.min(paddingLeft, paddingTop) * 0.5f) - this.selectorRadiusPx;
        this.radius = min;
        if (min < 0.0f) {
            return;
        }
        this.centerX = paddingLeft * 0.5f;
        this.centerY = paddingTop * 0.5f;
        setColor(this.currentColor, false);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                update(motionEvent);
                return true;
            } else if (actionMasked != 2) {
                return super.onTouchEvent(motionEvent);
            }
        }
        this.handler.onTouchEvent(motionEvent);
        return true;
    }

    @Override // top.defaults.colorpicker.Updatable
    public void update(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        boolean z = motionEvent.getActionMasked() == 1;
        if (!this.onlyUpdateOnTouchEventUp || z) {
            this.emitter.onColor(getColorAtPoint(x, y), true, z);
        }
        updateSelector(x, y);
    }

    private int getColorAtPoint(float f, float f2) {
        float f3 = f - this.centerX;
        float f4 = f2 - this.centerY;
        double sqrt = Math.sqrt((f3 * f3) + (f4 * f4));
        float[] fArr = {0.0f, 0.0f, 1.0f};
        fArr[0] = ((float) ((Math.atan2(f4, -f3) / 3.141592653589793d) * 180.0d)) + 180.0f;
        double d = this.radius;
        Double.isNaN(d);
        fArr[1] = Math.max(0.0f, Math.min(1.0f, (float) (sqrt / d)));
        return Color.HSVToColor(fArr);
    }

    public void setOnlyUpdateOnTouchEventUp(boolean z) {
        this.onlyUpdateOnTouchEventUp = z;
    }

    public void setColor(int i, boolean z) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        float f = fArr[1] * this.radius;
        double d = fArr[0] / 180.0f;
        Double.isNaN(d);
        float f2 = (float) (d * 3.141592653589793d);
        double d2 = f;
        double d3 = f2;
        double cos = Math.cos(d3);
        Double.isNaN(d2);
        double d4 = d2 * cos;
        double d5 = this.centerX;
        Double.isNaN(d5);
        float f3 = (float) (d4 + d5);
        double d6 = -f;
        double sin = Math.sin(d3);
        Double.isNaN(d6);
        double d7 = d6 * sin;
        double d8 = this.centerY;
        Double.isNaN(d8);
        updateSelector(f3, (float) (d7 + d8));
        this.currentColor = i;
        if (this.onlyUpdateOnTouchEventUp) {
            return;
        }
        this.emitter.onColor(i, false, z);
    }

    private void updateSelector(float f, float f2) {
        float f3 = f - this.centerX;
        float f4 = f2 - this.centerY;
        double sqrt = Math.sqrt((f3 * f3) + (f4 * f4));
        float f5 = this.radius;
        if (sqrt > f5) {
            double d = f3;
            double d2 = f5;
            Double.isNaN(d2);
            Double.isNaN(d);
            f3 = (float) (d * (d2 / sqrt));
            double d3 = f4;
            double d4 = f5;
            Double.isNaN(d4);
            Double.isNaN(d3);
            f4 = (float) (d3 * (d4 / sqrt));
        }
        this.currentPoint.x = f3 + this.centerX;
        this.currentPoint.y = f4 + this.centerY;
        this.selector.setCurrentPoint(this.currentPoint);
    }

    @Override // top.defaults.colorpicker.ColorObservable
    public void subscribe(ColorObserver colorObserver) {
        this.emitter.subscribe(colorObserver);
    }

    @Override // top.defaults.colorpicker.ColorObservable
    public void unsubscribe(ColorObserver colorObserver) {
        this.emitter.unsubscribe(colorObserver);
    }

    @Override // top.defaults.colorpicker.ColorObservable
    public int getColor() {
        return this.emitter.getColor();
    }
}
