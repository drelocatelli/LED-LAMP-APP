package com.ccr.achenglibrary.photopicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.ccr.achenglibrary.R;

/* loaded from: classes.dex */
public class CCRImageView extends AppCompatImageView {
    private int mBorderColor;
    private Paint mBorderPaint;
    private int mBorderWidth;
    private boolean mCircle;
    private int mCornerRadius;
    private int mDefaultImageId;
    private Delegate mDelegate;
    private boolean mSquare;

    /* loaded from: classes.dex */
    public interface Delegate {
        void onDrawableChanged(Drawable drawable);
    }

    public CCRImageView(Context context) {
        this(context, null);
    }

    public CCRImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CCRImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCornerRadius = 0;
        this.mCircle = false;
        this.mSquare = false;
        this.mBorderWidth = 0;
        this.mBorderColor = -1;
        initCustomAttrs(context, attributeSet);
        initBorderPaint();
        setDefaultImage();
    }

    private void initBorderPaint() {
        Paint paint = new Paint();
        this.mBorderPaint = paint;
        paint.setAntiAlias(true);
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setColor(this.mBorderColor);
        this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
    }

    private void initCustomAttrs(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CCRImageView);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            initCustomAttr(obtainStyledAttributes.getIndex(i), obtainStyledAttributes);
        }
        obtainStyledAttributes.recycle();
    }

    private void initCustomAttr(int i, TypedArray typedArray) {
        if (i == R.styleable.CCRImageView_android_src) {
            this.mDefaultImageId = typedArray.getResourceId(i, 0);
        } else if (i == R.styleable.CCRImageView_bga_iv_circle) {
            this.mCircle = typedArray.getBoolean(i, this.mCircle);
        } else if (i == R.styleable.CCRImageView_bga_iv_cornerRadius) {
            this.mCornerRadius = typedArray.getDimensionPixelSize(i, this.mCornerRadius);
        } else if (i == R.styleable.CCRImageView_bga_iv_square) {
            this.mSquare = typedArray.getBoolean(i, this.mSquare);
        } else if (i == R.styleable.CCRImageView_bga_iv_borderWidth) {
            this.mBorderWidth = typedArray.getDimensionPixelSize(i, this.mBorderWidth);
        } else if (i == R.styleable.CCRImageView_bga_iv_borderColor) {
            this.mBorderColor = typedArray.getColor(i, this.mBorderColor);
        }
    }

    private void setDefaultImage() {
        int i = this.mDefaultImageId;
        if (i != 0) {
            setImageResource(i);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageResource(int i) {
        setImageDrawable(getResources().getDrawable(i));
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        boolean z = drawable instanceof BitmapDrawable;
        if (z && this.mCornerRadius > 0) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                super.setImageDrawable(getRoundedDrawable(getContext(), bitmap, this.mCornerRadius));
            } else {
                super.setImageDrawable(drawable);
            }
        } else if (z && this.mCircle) {
            Bitmap bitmap2 = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap2 != null) {
                super.setImageDrawable(getCircleDrawable(getContext(), bitmap2));
            } else {
                super.setImageDrawable(drawable);
            }
        } else {
            super.setImageDrawable(drawable);
        }
        notifyDrawableChanged(drawable);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.mCircle || this.mSquare) {
            setMeasuredDimension(getDefaultSize(0, i), getDefaultSize(0, i2));
            i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
            i2 = i;
        }
        super.onMeasure(i, i2);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.mCircle || this.mBorderWidth <= 0) {
            return;
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2) - ((this.mBorderWidth * 1.0f) / 2.0f), this.mBorderPaint);
    }

    private void notifyDrawableChanged(Drawable drawable) {
        Delegate delegate = this.mDelegate;
        if (delegate != null) {
            delegate.onDrawableChanged(drawable);
        }
    }

    public void setCornerRadius(int i) {
        this.mCornerRadius = i;
    }

    public void setDelegate(Delegate delegate) {
        this.mDelegate = delegate;
    }

    public static RoundedBitmapDrawable getCircleDrawable(Context context, Bitmap bitmap) {
        Bitmap createBitmap;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            createBitmap = Bitmap.createBitmap(bitmap, (bitmap.getWidth() / 2) - (bitmap.getHeight() / 2), 0, bitmap.getHeight(), bitmap.getHeight());
        } else {
            createBitmap = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() / 2) - (bitmap.getWidth() / 2), bitmap.getWidth(), bitmap.getWidth());
        }
        RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), createBitmap);
        create.setAntiAlias(true);
        return create;
    }

    public static RoundedBitmapDrawable getRoundedDrawable(Context context, Bitmap bitmap, float f) {
        RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        create.setAntiAlias(true);
        create.setCornerRadius(f);
        return create;
    }
}
