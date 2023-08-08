package com.home.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.core.view.MotionEventCompat;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class SlideSwitch extends View {
    private static final int DEFAULT_COLOR_THEME = Color.parseColor("#ff00ee00");
    private static final int RIM_SIZE = 6;
    public static final int SHAPE_CIRCLE = 2;
    public static final int SHAPE_RECT = 1;
    private int alpha;
    private RectF backCircleRect;
    private Rect backRect;
    private int color_theme;
    private int diffX;
    private int eventLastX;
    private int eventStartX;
    private RectF frontCircleRect;
    private Rect frontRect;
    private int frontRect_left;
    private int frontRect_left_begin;
    private boolean isOpen;
    private SlideListener listener;
    private int max_left;
    private int min_left;
    private Paint paint;
    private int shape;
    private boolean slideable;

    /* loaded from: classes.dex */
    public interface SlideListener {
        void close();

        void open();
    }

    public SlideSwitch(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.frontRect_left_begin = 6;
        this.diffX = 0;
        this.slideable = true;
        this.listener = null;
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.slideswitch);
        this.color_theme = obtainStyledAttributes.getColor(2, DEFAULT_COLOR_THEME);
        this.isOpen = obtainStyledAttributes.getBoolean(0, false);
        this.shape = obtainStyledAttributes.getInt(1, 1);
        obtainStyledAttributes.recycle();
    }

    public SlideSwitch(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlideSwitch(Context context) {
        this(context, null);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measureDimension = measureDimension(280, i);
        int measureDimension2 = measureDimension(140, i2);
        if (this.shape == 2 && measureDimension < measureDimension2) {
            measureDimension = measureDimension2 * 2;
        }
        setMeasuredDimension(measureDimension, measureDimension2);
        initDrawingVal();
    }

    public void initDrawingVal() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        this.backCircleRect = new RectF();
        this.frontCircleRect = new RectF();
        this.frontRect = new Rect();
        this.backRect = new Rect(0, 0, measuredWidth, measuredHeight);
        this.min_left = 6;
        if (this.shape == 1) {
            this.max_left = measuredWidth / 2;
        } else {
            this.max_left = (measuredWidth - (measuredHeight - 12)) - 6;
        }
        if (this.isOpen) {
            this.frontRect_left = this.max_left;
            this.alpha = 255;
        } else {
            this.frontRect_left = 6;
            this.alpha = 0;
        }
        this.frontRect_left_begin = this.frontRect_left;
    }

    public int measureDimension(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        return mode == 1073741824 ? size : mode == Integer.MIN_VALUE ? Math.min(i, size) : i;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        if (this.shape == 1) {
            this.paint.setColor(-7829368);
            canvas.drawRect(this.backRect, this.paint);
            this.paint.setColor(this.color_theme);
            this.paint.setAlpha(this.alpha);
            canvas.drawRect(this.backRect, this.paint);
            Rect rect = this.frontRect;
            int i2 = this.frontRect_left;
            rect.set(i2, 6, ((getMeasuredWidth() / 2) + i2) - 6, getMeasuredHeight() - 6);
            this.paint.setColor(-1);
            canvas.drawRect(this.frontRect, this.paint);
            return;
        }
        this.paint.setColor(-7829368);
        this.backCircleRect.set(this.backRect);
        float height = (this.backRect.height() / 2) - 6;
        canvas.drawRoundRect(this.backCircleRect, height, height, this.paint);
        this.paint.setColor(this.color_theme);
        this.paint.setAlpha(this.alpha);
        canvas.drawRoundRect(this.backCircleRect, height, height, this.paint);
        this.frontRect.set(this.frontRect_left, 6, (this.backRect.height() + i) - 12, this.backRect.height() - 6);
        this.frontCircleRect.set(this.frontRect);
        this.paint.setColor(-1);
        canvas.drawRoundRect(this.frontCircleRect, height, height, this.paint);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.slideable) {
            return super.onTouchEvent(motionEvent);
        }
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked == 0) {
            this.eventStartX = (int) motionEvent.getRawX();
        } else if (actionMasked == 1) {
            int rawX = (int) (motionEvent.getRawX() - this.eventStartX);
            int i = this.frontRect_left;
            this.frontRect_left_begin = i;
            boolean z = i > this.max_left / 2;
            if (Math.abs(rawX) < 3) {
                z = !z;
            }
            moveToDest(z);
        } else if (actionMasked == 2) {
            int rawX2 = (int) motionEvent.getRawX();
            this.eventLastX = rawX2;
            int i2 = rawX2 - this.eventStartX;
            this.diffX = i2;
            int i3 = i2 + this.frontRect_left_begin;
            int i4 = this.max_left;
            if (i3 > i4) {
                i3 = i4;
            }
            int i5 = this.min_left;
            if (i3 < i5) {
                i3 = i5;
            }
            if (i3 >= i5 && i3 <= i4) {
                this.frontRect_left = i3;
                this.alpha = (int) ((i3 * 255.0f) / i4);
                invalidateView();
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public void setSlideListener(SlideListener slideListener) {
        this.listener = slideListener;
    }

    public void moveToDest(final boolean z) {
        int[] iArr = new int[2];
        iArr[0] = this.frontRect_left;
        iArr[1] = z ? this.max_left : this.min_left;
        ValueAnimator ofInt = ValueAnimator.ofInt(iArr);
        ofInt.setDuration(100L);
        ofInt.setInterpolator(new AccelerateDecelerateInterpolator());
        ofInt.start();
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.home.view.SlideSwitch.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SlideSwitch.this.frontRect_left = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                SlideSwitch slideSwitch = SlideSwitch.this;
                slideSwitch.alpha = (int) ((slideSwitch.frontRect_left * 255.0f) / SlideSwitch.this.max_left);
                SlideSwitch.this.invalidateView();
            }
        });
        ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.home.view.SlideSwitch.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (z) {
                    SlideSwitch.this.isOpen = true;
                    if (SlideSwitch.this.listener != null) {
                        SlideSwitch.this.listener.open();
                    }
                    SlideSwitch slideSwitch = SlideSwitch.this;
                    slideSwitch.frontRect_left_begin = slideSwitch.max_left;
                    return;
                }
                SlideSwitch.this.isOpen = false;
                if (SlideSwitch.this.listener != null) {
                    SlideSwitch.this.listener.close();
                }
                SlideSwitch slideSwitch2 = SlideSwitch.this;
                slideSwitch2.frontRect_left_begin = slideSwitch2.min_left;
            }
        });
    }

    public void setState(boolean z) {
        this.isOpen = z;
        initDrawingVal();
        invalidateView();
        SlideListener slideListener = this.listener;
        if (slideListener != null) {
            if (z) {
                slideListener.open();
            } else {
                slideListener.close();
            }
        }
    }

    public void setStateNoListener(boolean z) {
        this.isOpen = z;
        initDrawingVal();
        invalidateView();
    }

    public void setChecked(boolean z) {
        this.isOpen = z;
    }

    public boolean getChecked() {
        return this.isOpen;
    }

    public void setShapeType(int i) {
        this.shape = i;
    }

    public void setSlideable(boolean z) {
        this.slideable = z;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.isOpen = bundle.getBoolean("isOpen");
            parcelable = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putBoolean("isOpen", this.isOpen);
        return bundle;
    }
}
