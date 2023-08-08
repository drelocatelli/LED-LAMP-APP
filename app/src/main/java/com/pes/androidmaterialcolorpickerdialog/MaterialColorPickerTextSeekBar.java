package com.pes.androidmaterialcolorpickerdialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
class MaterialColorPickerTextSeekBar extends AppCompatSeekBar {
    private String text;
    private int textColor;
    private Paint textPaint;
    private Rect textRect;
    private float textSize;

    public MaterialColorPickerTextSeekBar(Context context) {
        super(context);
        init(null);
    }

    public MaterialColorPickerTextSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public MaterialColorPickerTextSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        this.textPaint = new Paint(65);
        this.textRect = new Rect();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.MaterialColorPickerTextSeekBar);
            try {
                this.textColor = obtainStyledAttributes.getColor(R.styleable.MaterialColorPickerTextSeekBar_android_textColor, ViewCompat.MEASURED_STATE_MASK);
                this.textSize = obtainStyledAttributes.getDimension(R.styleable.MaterialColorPickerTextSeekBar_android_textSize, TypedValue.applyDimension(2, 18.0f, getResources().getDisplayMetrics()));
                this.text = obtainStyledAttributes.getString(R.styleable.MaterialColorPickerTextSeekBar_android_text);
            } finally {
                obtainStyledAttributes.recycle();
            }
        }
        this.textPaint.setColor(this.textColor);
        this.textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        this.textPaint.setTextSize(this.textSize);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textPaint.getTextBounds("255", 0, 3, this.textRect);
        int paddingLeft = getPaddingLeft();
        double height = this.textRect.height();
        Double.isNaN(height);
        setPadding(paddingLeft, (int) TypedValue.applyDimension(1, (float) (height * 0.6d), getResources().getDisplayMetrics()), getPaddingRight(), getPaddingBottom());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatSeekBar, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String str = this.text;
        if (str == null) {
            str = String.valueOf(getProgress());
        }
        canvas.drawText(str, getThumb().getBounds().left + getPaddingLeft(), this.textRect.height() + (getPaddingTop() >> 2), this.textPaint);
    }
}
