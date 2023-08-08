package com.home.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.common.uitl.LogUtil;
import com.home.base.LedBleApplication;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class MyColorPickerImageView extends ImageView {
    private static final int MIN_POINT = 2;
    private Bitmap bitmap;
    private int borderWidth;
    private Context context;
    private int h;
    private float innerCircle;
    private int kx;
    private int ky;
    private OnTouchPixListener onTouchPixListener;
    private Paint p;
    private double tx;
    private Bitmap txmap;
    private double ty;
    private int w;

    /* loaded from: classes.dex */
    public interface OnTouchPixListener {
        void onColorSelect(int i, float f);
    }

    public MyColorPickerImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.innerCircle = 0.0f;
        this.borderWidth = 0;
        this.p = new Paint();
        this.context = context;
        setDrawingCacheEnabled(true);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        this.w = getWidth();
        this.h = getHeight();
        if (this.txmap == null) {
            Bitmap decodeResource = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bg_picker_knob);
            this.txmap = decodeResource;
            this.kx = decodeResource.getWidth();
            this.ky = this.txmap.getHeight();
        }
        int i2 = this.w;
        int i3 = i2 / 2;
        int i4 = i2 / 2;
        int i5 = this.h / 2;
        double d = this.tx;
        if (d == 0.0d) {
            double d2 = this.ty;
            if (d2 == 0.0d) {
                if (d == 0.0d) {
                    this.tx = i2 / 2;
                }
                if (d2 == 0.0d) {
                    this.ty = i / 2;
                }
                Bitmap bitmap = this.txmap;
                double d3 = this.tx;
                double d4 = this.kx / 2;
                Double.isNaN(d4);
                double d5 = this.ty;
                double d6 = this.ky / 2;
                Double.isNaN(d6);
                canvas.drawBitmap(bitmap, (float) (d3 - d4), (float) (d5 - d6), this.p);
            }
        }
        if (inCircle((int) this.tx, (int) this.ty, i3, i4, i5)) {
            Bitmap bitmap2 = this.txmap;
            double d7 = this.tx;
            double d8 = this.kx / 2;
            Double.isNaN(d8);
            double d9 = this.ty;
            double d10 = this.ky / 2;
            Double.isNaN(d10);
            canvas.drawBitmap(bitmap2, (float) (d7 - d8), (float) (d9 - d10), this.p);
            return;
        }
        LogUtil.i(LedBleApplication.tag, "not in circle:" + i3);
    }

    public void move2Ege(double d) {
        int i;
        int i2 = this.w;
        int i3 = this.h / 2;
        int i4 = this.borderWidth;
        float f = (i2 / 2) - i4;
        if (d == 0.0d) {
            this.tx = i2 - i4;
            this.ty = i / 2;
        } else if (d == 1.0471975511965976d) {
            double d2 = f;
            Double.isNaN(d2);
            double d3 = 0.5d * d2;
            Double.isNaN(d2);
            this.tx = d2 + d3;
            Double.isNaN(d2);
            this.ty = d2 - (d3 * Math.sqrt(3.0d));
        } else if (d == 2.0943951023931953d) {
            double d4 = f;
            Double.isNaN(d4);
            double d5 = 0.5d * d4;
            Double.isNaN(d4);
            this.tx = (int) (d4 - d5);
            Double.isNaN(d4);
            this.ty = (int) (d4 - (d5 * Math.sqrt(3.0d)));
        } else if (d == 3.141592653589793d) {
            this.tx = i4;
            this.ty = i / 2;
        } else if (d == 4.1887902047863905d) {
            double d6 = f;
            Double.isNaN(d6);
            Double.isNaN(d6);
            this.tx = d6 - (0.5d * d6);
            Double.isNaN(d6);
            Double.isNaN(d6);
            this.ty = d6 + ((1.0d / Math.sqrt(3.0d)) * d6);
        } else if (d == 5.235987755982989d) {
            double d7 = f;
            Double.isNaN(d7);
            double d8 = 0.5d * d7;
            Double.isNaN(d7);
            this.tx = d7 + d8;
            Double.isNaN(d7);
            this.ty = d7 + (d8 * Math.sqrt(3.0d));
        }
        LogUtil.i(LedBleApplication.tag, "tx:" + this.tx + " ty:" + this.ty + " w:" + this.w);
        invalidate();
    }

    public int getBolorWidth() {
        return this.borderWidth;
    }

    public void setBolorWidth(int i) {
        this.borderWidth = i;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.w = getWidth();
        this.h = getHeight();
        int i = this.w / 2;
        this.tx = (int) motionEvent.getX();
        this.ty = (int) motionEvent.getY();
        int i2 = this.w / 2;
        int i3 = this.h / 2;
        Bitmap bitmap = this.bitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            this.bitmap = getDrawingCache();
        }
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            double d = this.tx;
            if (this.ty * d <= 0.0d || d >= bitmap2.getWidth() || this.ty >= this.bitmap.getHeight() || !inInnerCircle((int) this.tx, (int) this.ty, i, i2, i3, this.innerCircle)) {
                return true;
            }
            float angleFrom0 = angleFrom0(new Point((int) this.tx, (int) this.ty), new Point(i2, i3));
            OnTouchPixListener onTouchPixListener = this.onTouchPixListener;
            if (onTouchPixListener != null) {
                onTouchPixListener.onColorSelect(this.bitmap.getPixel((int) this.tx, (int) this.ty), angleFrom0);
            }
            invalidate();
            return true;
        }
        return true;
    }

    private boolean inCircle(int i, int i2, int i3, int i4, int i5) {
        int i6 = i - i4;
        int i7 = i2 - i5;
        return ((int) Math.sqrt((double) ((i6 * i6) + (i7 * i7)))) <= i3 - this.borderWidth;
    }

    private boolean inInnerCircle(int i, int i2, int i3, int i4, int i5, float f) {
        int i6 = i - i4;
        int i7 = i2 - i5;
        int sqrt = (int) Math.sqrt((i6 * i6) + (i7 * i7));
        return sqrt < i3 && ((float) sqrt) > ((float) i3) * f;
    }

    public OnTouchPixListener getOnTouchPixListener() {
        return this.onTouchPixListener;
    }

    public void setOnTouchPixListener(OnTouchPixListener onTouchPixListener) {
        this.onTouchPixListener = onTouchPixListener;
    }

    public float getInnerCircle() {
        return this.innerCircle;
    }

    public void setInnerCircle(float f) {
        this.innerCircle = f;
    }

    protected float angleFrom0(Point point, Point point2) {
        int i = point.x - point2.x;
        int i2 = point.y - point2.y;
        double hypot = Math.hypot(Math.abs(i), Math.abs(i2));
        double abs = Math.abs(i2);
        Double.isNaN(abs);
        float asin = (float) ((Math.asin(abs / hypot) / 3.140000104904175d) * 180.0d);
        return (i > 0 || i2 > 0) ? (i < 0 || i2 > 0) ? (i > 0 || i2 < 0) ? asin : 180.0f - asin : 360.0f - asin : asin + 180.0f;
    }
}
