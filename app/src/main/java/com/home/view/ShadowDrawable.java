package com.home.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public class ShadowDrawable extends Drawable {
    public static final int SHAPE_CIRCLE = 2;
    public static final int SHAPE_ROUND = 1;
    private int[] mBgColor;
    private Paint mBgPaint;
    private int mOffsetX;
    private int mOffsetY;
    private RectF mRect;
    private Paint mShadowPaint;
    private int mShadowRadius;
    private int mShape;
    private int mShapeRadius;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    private ShadowDrawable(int i, int[] iArr, int i2, int i3, int i4, int i5, int i6) {
        this.mShape = i;
        this.mBgColor = iArr;
        this.mShapeRadius = i2;
        this.mShadowRadius = i4;
        this.mOffsetX = i5;
        this.mOffsetY = i6;
        Paint paint = new Paint();
        this.mShadowPaint = paint;
        paint.setColor(0);
        this.mShadowPaint.setAntiAlias(true);
        this.mShadowPaint.setShadowLayer(i4, i5, i6, i3);
        this.mShadowPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
        Paint paint2 = new Paint();
        this.mBgPaint = paint2;
        paint2.setAntiAlias(true);
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int i, int i2, int i3, int i4) {
        super.setBounds(i, i2, i3, i4);
        int i5 = this.mShadowRadius;
        int i6 = this.mOffsetX;
        int i7 = this.mOffsetY;
        this.mRect = new RectF((i + i5) - i6, (i2 + i5) - i7, (i3 - i5) - i6, (i4 - i5) - i7);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        int[] iArr = this.mBgColor;
        if (iArr != null) {
            if (iArr.length == 1) {
                this.mBgPaint.setColor(iArr[0]);
            } else {
                this.mBgPaint.setShader(new LinearGradient(this.mRect.left, this.mRect.height() / 2.0f, this.mRect.right, this.mRect.height() / 2.0f, this.mBgColor, (float[]) null, Shader.TileMode.CLAMP));
            }
        }
        if (this.mShape == 1) {
            RectF rectF = this.mRect;
            int i = this.mShapeRadius;
            canvas.drawRoundRect(rectF, i, i, this.mShadowPaint);
            RectF rectF2 = this.mRect;
            int i2 = this.mShapeRadius;
            canvas.drawRoundRect(rectF2, i2, i2, this.mBgPaint);
            return;
        }
        canvas.drawCircle(this.mRect.centerX(), this.mRect.centerY(), Math.min(this.mRect.width(), this.mRect.height()) / 2.0f, this.mShadowPaint);
        canvas.drawCircle(this.mRect.centerX(), this.mRect.centerY(), Math.min(this.mRect.width(), this.mRect.height()) / 2.0f, this.mBgPaint);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mShadowPaint.setAlpha(i);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mShadowPaint.setColorFilter(colorFilter);
    }

    public static void setShadowDrawable(View view, Drawable drawable) {
        view.setLayerType(1, null);
        ViewCompat.setBackground(view, drawable);
    }

    public static void setShadowDrawable(View view, int i, int i2, int i3, int i4, int i5) {
        ShadowDrawable builder = new Builder().setShapeRadius(i).setShadowColor(i2).setShadowRadius(i3).setOffsetX(i4).setOffsetY(i5).builder();
        view.setLayerType(1, null);
        ViewCompat.setBackground(view, builder);
    }

    public static void setShadowDrawable(View view, int i, int i2, int i3, int i4, int i5, int i6) {
        ShadowDrawable builder = new Builder().setBgColor(i).setShapeRadius(i2).setShadowColor(i3).setShadowRadius(i4).setOffsetX(i5).setOffsetY(i6).builder();
        view.setLayerType(1, null);
        ViewCompat.setBackground(view, builder);
    }

    public static void setShadowDrawable(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        ShadowDrawable builder = new Builder().setShape(i).setBgColor(i2).setShapeRadius(i3).setShadowColor(i4).setShadowRadius(i5).setOffsetX(i6).setOffsetY(i7).builder();
        view.setLayerType(1, null);
        ViewCompat.setBackground(view, builder);
    }

    public static void setShadowDrawable(View view, int[] iArr, int i, int i2, int i3, int i4, int i5) {
        ShadowDrawable builder = new Builder().setBgColor(iArr).setShapeRadius(i).setShadowColor(i2).setShadowRadius(i3).setOffsetX(i4).setOffsetY(i5).builder();
        view.setLayerType(1, null);
        ViewCompat.setBackground(view, builder);
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private int[] mBgColor;
        private int mOffsetX;
        private int mOffsetY;
        private int mShape = 2;
        private int mShapeRadius = 12;
        private int mShadowColor = Color.parseColor("#4d000000");
        private int mShadowRadius = 18;

        public Builder() {
            this.mOffsetX = 0;
            this.mOffsetY = 0;
            this.mOffsetX = 0;
            this.mOffsetY = 0;
            this.mBgColor = r1;
            int[] iArr = {0};
        }

        public Builder setShape(int i) {
            this.mShape = i;
            return this;
        }

        public Builder setShapeRadius(int i) {
            this.mShapeRadius = i;
            return this;
        }

        public Builder setShadowColor(int i) {
            this.mShadowColor = i;
            return this;
        }

        public Builder setShadowRadius(int i) {
            this.mShadowRadius = i;
            return this;
        }

        public Builder setOffsetX(int i) {
            this.mOffsetX = i;
            return this;
        }

        public Builder setOffsetY(int i) {
            this.mOffsetY = i;
            return this;
        }

        public Builder setBgColor(int i) {
            this.mBgColor[0] = i;
            return this;
        }

        public Builder setBgColor(int[] iArr) {
            this.mBgColor = iArr;
            return this;
        }

        public ShadowDrawable builder() {
            return new ShadowDrawable(this.mShape, this.mBgColor, this.mShapeRadius, this.mShadowColor, this.mShadowRadius, this.mOffsetX, this.mOffsetY);
        }
    }
}
