package com.home.view.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import androidx.core.view.MotionEventCompat;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class LevelProgressBar extends ProgressBar {
    private final int EMPTY_MESSAGE;
    private int animInterval;
    private int animMaxTime;
    private ValueAnimator animator;
    private int currentLevel;
    private Handler handler;
    private int levelTextChooseColor;
    private int levelTextSize;
    private int levelTextUnChooseColor;
    private String[] levelTexts;
    private int levels;
    private Paint mPaint;
    private int mTotalWidth;
    private int progressBgColor;
    private int progressEndColor;
    private int progressHeight;
    private int progressStartColor;
    private int targetProgress;
    int textHeight;

    public LevelProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LevelProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.EMPTY_MESSAGE = 1;
        this.handler = new Handler() { // from class: com.home.view.custom.LevelProgressBar.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int progress = LevelProgressBar.this.getProgress();
                if (progress >= LevelProgressBar.this.targetProgress) {
                    if (progress <= LevelProgressBar.this.targetProgress) {
                        LevelProgressBar.this.handler.removeMessages(1);
                        return;
                    }
                    LevelProgressBar.this.setProgress(progress - 1);
                    LevelProgressBar.this.handler.sendEmptyMessageDelayed(1, LevelProgressBar.this.animInterval);
                    return;
                }
                LevelProgressBar.this.setProgress(progress + 1);
                LevelProgressBar.this.handler.sendEmptyMessageDelayed(1, LevelProgressBar.this.animInterval);
            }
        };
        obtainStyledAttributes(attributeSet);
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setTextSize(this.levelTextSize);
        this.mPaint.setColor(this.levelTextUnChooseColor);
    }

    private void obtainStyledAttributes(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.LevelProgressBar);
        this.levelTextUnChooseColor = obtainStyledAttributes.getColor(2, 0);
        this.levelTextChooseColor = obtainStyledAttributes.getColor(0, 3355443);
        this.levelTextSize = (int) obtainStyledAttributes.getDimension(1, dpTopx(15));
        this.progressStartColor = obtainStyledAttributes.getColor(6, 13434828);
        this.progressEndColor = obtainStyledAttributes.getColor(4, MotionEventCompat.ACTION_POINTER_INDEX_MASK);
        this.progressBgColor = obtainStyledAttributes.getColor(3, 0);
        this.progressHeight = (int) obtainStyledAttributes.getDimension(5, dpTopx(20));
        obtainStyledAttributes.recycle();
    }

    private int dpTopx(int i) {
        return (int) TypedValue.applyDimension(1, i, getResources().getDisplayMetrics());
    }

    public void setLevels(int i) {
        this.levels = i;
    }

    public void setCurrentLevel(int i) {
        this.currentLevel = i;
        this.targetProgress = (int) (((i * 1.0f) / this.levels) * getMax());
    }

    public void setAnimInterval(int i) {
        this.animInterval = i;
        this.handler.sendEmptyMessage(1);
    }

    public void setAnimMaxTime(int i) {
        this.animMaxTime = i;
        ValueAnimator ofInt = ValueAnimator.ofInt(getProgress(), this.targetProgress);
        this.animator = ofInt;
        ofInt.setDuration((i * Math.abs(getProgress() - this.targetProgress)) / getMax());
        this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.home.view.custom.LevelProgressBar.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int progress = LevelProgressBar.this.getProgress();
                if (progress < LevelProgressBar.this.targetProgress || progress > LevelProgressBar.this.targetProgress) {
                    LevelProgressBar.this.setProgress(((Integer) valueAnimator.getAnimatedValue()).intValue());
                }
            }
        });
        this.animator.start();
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        if (View.MeasureSpec.getMode(i2) != 1073741824) {
            this.textHeight = (int) (this.mPaint.descent() - this.mPaint.ascent());
            size2 = dpTopx(10) + getPaddingTop() + getPaddingBottom() + this.progressHeight;
        }
        setMeasuredDimension(size, size2);
        this.mTotalWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        this.mPaint.setColor(this.progressBgColor);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setStrokeWidth(this.progressHeight);
        int i = this.progressHeight;
        float f = this.progressHeight / 2;
        canvas.drawLine((i / 2) + 0, f, this.mTotalWidth - (i / 2), f, this.mPaint);
        int progress = (int) (((getProgress() * 1.0f) / getMax()) * this.mTotalWidth);
        if (progress > 0) {
            this.mPaint.setStrokeCap(Paint.Cap.ROUND);
            this.mPaint.setShader(new LinearGradient(0.0f, f, getWidth(), f, this.progressStartColor, this.progressEndColor, Shader.TileMode.REPEAT));
            int i2 = this.progressHeight;
            int i3 = progress - (i2 / 2);
            int i4 = (i2 / 2) + 0;
            if (i3 > i4) {
                canvas.drawLine(i4, f, i3, f, this.mPaint);
            } else {
                float f2 = i4;
                canvas.drawLine(f2, f, f2, f, this.mPaint);
            }
            this.mPaint.setShader(null);
        }
        canvas.restore();
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }
}
