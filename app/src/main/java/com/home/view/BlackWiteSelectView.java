package com.home.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class BlackWiteSelectView extends View {
    private Context context;
    int dra;
    int h;
    private int kx;
    private int ky;
    int[] mRectColors;
    private OnSelectColor onSelectColor;
    private Paint paint;
    Paint paint2;
    private int progress;
    private Shader shader;
    private float tx;
    private Bitmap txmap;
    private float ty;
    int w;

    /* loaded from: classes.dex */
    public interface OnSelectColor {
        void onColorSelect(int i, int i2);
    }

    private boolean inRectangle(int i, int i2, int i3, int i4) {
        return i >= 0 && i <= i3;
    }

    public BlackWiteSelectView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRectColors = new int[]{ViewCompat.MEASURED_STATE_MASK, -1};
        this.paint2 = new Paint();
        this.context = context;
        init();
    }

    private void init() {
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        this.paint.setStrokeWidth(5.0f);
        this.paint2.setAntiAlias(true);
        this.paint2.setStrokeWidth(5.0f);
        this.paint2.setStyle(Paint.Style.STROKE);
        this.paint2.setColor(-1);
    }

    public void setStartColor(int i) {
        this.mRectColors[1] = i;
        invalidate();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        this.tx = x;
        this.ty = this.h / 2;
        int color = getColor(this.mRectColors, x, this.w);
        int i = (int) ((this.tx / this.w) * 100.0f);
        this.progress = i;
        OnSelectColor onSelectColor = this.onSelectColor;
        if (onSelectColor != null) {
            onSelectColor.onColorSelect(color, i);
        }
        if (inRectangle((int) this.tx, (int) this.ty, this.dra, this.h)) {
            invalidate();
            return true;
        }
        return true;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int i) {
        double d;
        this.progress = i;
        if (this.w == 0 && this.h == 0) {
            DisplayMetrics displayMetrics = this.context.getResources().getDisplayMetrics();
            float f = displayMetrics.density;
            this.w = displayMetrics.widthPixels - dp2px(85, this.context);
            int i2 = displayMetrics.heightPixels;
            this.h = (int) TypedValue.applyDimension(1, 40.0f, getResources().getDisplayMetrics());
        }
        double d2 = this.w;
        Double.isNaN(i);
        Double.isNaN(d2);
        this.tx = (int) (d2 * (d / 100.0d));
        invalidate();
    }

    public static int dp2sp(int i, Context context) {
        return (int) TypedValue.applyDimension(2, i, context.getResources().getDisplayMetrics());
    }

    public static int dp2px(int i, Context context) {
        return (int) TypedValue.applyDimension(1, i, context.getResources().getDisplayMetrics());
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0);
        this.w = getWidth();
        this.h = getHeight();
        if (this.txmap == null) {
            Bitmap decodeResource = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.painttwo);
            this.txmap = decodeResource;
            this.kx = decodeResource.getWidth();
            this.ky = this.txmap.getHeight();
            this.dra = this.w - this.kx;
        }
        int i = this.h;
        LinearGradient linearGradient = new LinearGradient(0.0f, i / 2, this.w, i / 2, this.mRectColors, (float[]) null, Shader.TileMode.MIRROR);
        this.shader = linearGradient;
        this.paint.setShader(linearGradient);
        this.paint.setStyle(Paint.Style.FILL);
        int i2 = this.h;
        RectF rectF = new RectF(0.0f, i2 / 3, this.w, (i2 / 3) + (i2 / 3));
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        canvas.drawRoundRect(rectF, 20.0f, 20.0f, this.paint);
        if (this.ty == 0.0f) {
            this.ty = this.h / 2;
        }
        canvas.drawBitmap(this.txmap, this.tx, this.ty - (this.ky / 2), this.paint2);
    }

    private int getColor(int[] iArr, float f, int i) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        float f2 = i;
        if (f > f2) {
            f = f2;
        }
        float f3 = f / f2;
        int i2 = iArr[0];
        int i3 = iArr[1];
        return Color.argb(ave(Color.alpha(i2), Color.alpha(i3), f3), ave(Color.red(i2), Color.red(i3), f3), ave(Color.green(i2), Color.green(i3), f3), ave(Color.blue(i2), Color.blue(i3), f3));
    }

    private int interpRectColor(int[] iArr, float f, int i) {
        int i2;
        int i3;
        float f2;
        if (f < 0.0f) {
            i2 = iArr[0];
            i3 = iArr[1];
            f2 = i;
            f += f2;
        } else {
            i2 = iArr[0];
            i3 = iArr[1];
            f2 = i;
        }
        float f3 = f / f2;
        return Color.argb(ave(Color.alpha(i2), Color.alpha(i3), f3), ave(Color.red(i2), Color.red(i3), f3), ave(Color.green(i2), Color.green(i3), f3), ave(Color.blue(i2), Color.blue(i3), f3));
    }

    private int ave(int i, int i2, float f) {
        return i + Math.round(f * (i2 - i));
    }

    public OnSelectColor getOnSelectColor() {
        return this.onSelectColor;
    }

    public void setOnSelectColor(OnSelectColor onSelectColor) {
        this.onSelectColor = onSelectColor;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }
}
