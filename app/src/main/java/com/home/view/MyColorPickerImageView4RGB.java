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
import androidx.core.internal.view.SupportMenu;
import com.common.uitl.DrawTool;
import com.common.uitl.LogUtil;
import com.home.base.LedBleApplication;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class MyColorPickerImageView4RGB extends ImageView {
    private static final int MIN_POINT = 2;
    private Bitmap bitmap;
    private Bitmap bitmapBg;
    private int borderWidth;
    private Context context;
    private int h;
    private float innerCircle;
    private int kx;
    private int ky;
    double lx;
    double ly;
    private OnTouchPixListener onTouchPixListener;
    private Paint p;
    private float r;
    private double tx;
    private Bitmap txmap;
    private double ty;
    private int w;

    /* loaded from: classes.dex */
    public interface OnTouchPixListener {
        void onColorSelect(int i, float f);
    }

    public MyColorPickerImageView4RGB(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.innerCircle = 0.0f;
        this.borderWidth = 0;
        this.lx = 0.0d;
        this.ly = 0.0d;
        Paint paint = new Paint();
        this.p = paint;
        paint.setAntiAlias(true);
        this.p.setColor(SupportMenu.CATEGORY_MASK);
        this.p.setStyle(Paint.Style.STROKE);
        this.context = context;
        setDrawingCacheEnabled(true);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        Bitmap decodeResource;
        super.onDraw(canvas);
        this.w = getWidth();
        int height = getHeight();
        this.h = height;
        int i2 = this.w / 2;
        int i3 = height / 2;
        this.r = (Math.min(i, height) / 2) - (this.borderWidth / 2);
        LogUtil.i(LedBleApplication.tag, "r:" + this.r);
        Bitmap bitmap = this.txmap;
        if (bitmap == null || bitmap.isRecycled()) {
            Bitmap decodeResource2 = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bg_picker_knob);
            this.txmap = decodeResource2;
            this.kx = decodeResource2.getWidth();
            this.ky = this.txmap.getHeight();
        }
        Bitmap bitmap2 = this.bitmapBg;
        if (bitmap2 == null || bitmap2.isRecycled()) {
            this.bitmapBg = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.bg_collor_picker);
            this.bitmapBg = DrawTool.scale(this.bitmapBg, (this.r * 2.0f) / decodeResource.getHeight());
        }
        LogUtil.i(LedBleApplication.tag, "bgmap:" + this.bitmapBg.getWidth() + " " + this.bitmapBg.getHeight() + " r:" + this.r);
        Bitmap bitmap3 = this.bitmapBg;
        float f = this.r;
        canvas.drawBitmap(bitmap3, ((float) i2) - f, ((float) i3) - f, this.p);
        double d = this.tx;
        if (d == 0.0d) {
            double d2 = this.ty;
            if (d2 == 0.0d) {
                if (d == 0.0d) {
                    this.tx = this.w / 2;
                }
                if (d2 == 0.0d) {
                    this.ty = this.h / 2;
                }
                Bitmap bitmap4 = this.txmap;
                double d3 = this.tx;
                double d4 = this.kx / 2;
                Double.isNaN(d4);
                float f2 = (float) (d3 - d4);
                double d5 = this.ty;
                double d6 = this.ky / 2;
                Double.isNaN(d6);
                canvas.drawBitmap(bitmap4, f2, (float) (d5 - d6), this.p);
            }
        }
        if (inCircle(this.tx, this.ty, this.r, i2, i3)) {
            double d7 = this.tx;
            this.lx = d7;
            double d8 = this.ty;
            this.ly = d8;
            Bitmap bitmap5 = this.txmap;
            double d9 = this.kx / 2;
            Double.isNaN(d9);
            double d10 = this.ky / 2;
            Double.isNaN(d10);
            canvas.drawBitmap(bitmap5, (float) (d7 - d9), (float) (d8 - d10), this.p);
            return;
        }
        LogUtil.i(LedBleApplication.tag, "not in circle:" + this.r);
        Bitmap bitmap6 = this.txmap;
        double d11 = this.lx;
        double d12 = (double) (this.kx / 2);
        Double.isNaN(d12);
        double d13 = this.ly;
        double d14 = this.ky / 2;
        Double.isNaN(d14);
        canvas.drawBitmap(bitmap6, (float) (d11 - d12), (float) (d13 - d14), this.p);
    }

    public void move2Ege(double d) {
        if (d == 0.0d) {
            this.tx = this.w - this.borderWidth;
            this.ty = this.h / 2;
        } else if (d == 1.0471975511965976d) {
            float f = this.r;
            double d2 = f;
            double d3 = f;
            Double.isNaN(d3);
            Double.isNaN(d2);
            this.tx = d2 + (d3 * 0.5d);
            double d4 = this.h / 2;
            double d5 = f;
            Double.isNaN(d5);
            Double.isNaN(d4);
            this.ty = d4 - (d5 * (Math.sqrt(3.0d) / 2.0d));
        } else if (d == 2.0943951023931953d) {
            float f2 = this.r;
            double d6 = f2;
            double d7 = f2;
            Double.isNaN(d7);
            Double.isNaN(d6);
            this.tx = d6 - (d7 * 0.5d);
            double d8 = this.h / 2;
            double d9 = f2;
            Double.isNaN(d9);
            Double.isNaN(d8);
            this.ty = d8 - (d9 * (Math.sqrt(3.0d) / 2.0d));
        } else if (d == 3.141592653589793d) {
            this.tx = this.borderWidth;
            this.ty = this.h / 2;
        } else if (d == 4.1887902047863905d) {
            float f3 = this.r;
            double d10 = f3;
            double d11 = f3;
            Double.isNaN(d11);
            Double.isNaN(d10);
            this.tx = d10 - (d11 * 0.5d);
            double d12 = this.h / 2;
            double d13 = f3;
            Double.isNaN(d13);
            Double.isNaN(d12);
            this.ty = d12 + (d13 * (Math.sqrt(3.0d) / 2.0d));
        } else if (d == 5.235987755982989d) {
            float f4 = this.r;
            double d14 = f4;
            double d15 = f4;
            Double.isNaN(d15);
            Double.isNaN(d14);
            this.tx = d14 + (d15 * 0.5d);
            double d16 = this.h / 2;
            double d17 = f4;
            Double.isNaN(d17);
            Double.isNaN(d16);
            this.ty = d16 + (d17 * (Math.sqrt(3.0d) / 2.0d));
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
        this.tx = (int) motionEvent.getX();
        this.ty = (int) motionEvent.getY();
        int i = this.w / 2;
        int i2 = this.h / 2;
        LogUtil.i(LedBleApplication.tag, "w:" + this.w + " h:" + this.h + " r:" + this.r + " c(x,y):" + i + "," + i2 + "");
        Bitmap bitmap = this.bitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            this.bitmap = getDrawingCache();
        }
        if (this.bitmap != null) {
            double d = this.tx;
            double d2 = this.ty;
            if (d * d2 <= 0.0d || !inCircle((int) d, (int) d2, this.r, i, i2)) {
                return true;
            }
            float angleFrom0 = angleFrom0(new Point((int) this.tx, (int) this.ty), new Point(i, i2));
            invalidate();
            if (this.onTouchPixListener != null) {
                this.onTouchPixListener.onColorSelect(this.bitmap.getPixel((int) this.tx, (int) this.ty), angleFrom0);
                return true;
            }
            return true;
        }
        return true;
    }

    private boolean inCircle(double d, double d2, double d3, double d4, double d5) {
        double d6 = d - d4;
        double d7 = d2 - d5;
        return ((double) ((int) Math.sqrt((d6 * d6) + (d7 * d7)))) < d3 - 5.0d;
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
