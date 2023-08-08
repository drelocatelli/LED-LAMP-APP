package com.home.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class IndicatorSeekBar extends AppCompatSeekBar {
    private OnIndicatorSeekBarChangeListener mIndicatorSeekBarChangeListener;
    private int mIndicatorWidth;
    private Paint mPaint;
    private Rect mProgressTextRect;
    private int mThumbWidth;

    /* loaded from: classes.dex */
    public interface OnIndicatorSeekBarChangeListener {
        void onProgressChanged(SeekBar seekBar, int i, float f);

        void onStartTrackingTouch(SeekBar seekBar);

        void onStopTrackingTouch(SeekBar seekBar);
    }

    public IndicatorSeekBar(Context context) {
        this(context, null);
    }

    public IndicatorSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.seekBarStyle);
    }

    public IndicatorSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mProgressTextRect = new Rect();
        this.mThumbWidth = dp2px(50.0f);
        this.mIndicatorWidth = dp2px(50.0f);
        init();
    }

    private void init() {
        TextPaint textPaint = new TextPaint();
        this.mPaint = textPaint;
        textPaint.setAntiAlias(true);
        this.mPaint.setColor(Color.parseColor("#00574B"));
        this.mPaint.setTextSize(sp2px(16.0f));
        int i = this.mThumbWidth;
        setPadding(i / 2, 0, i / 2, 0);
        setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.view.custom.IndicatorSeekBar.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (IndicatorSeekBar.this.mIndicatorSeekBarChangeListener != null) {
                    IndicatorSeekBar.this.mIndicatorSeekBarChangeListener.onStartTrackingTouch(seekBar);
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (IndicatorSeekBar.this.mIndicatorSeekBarChangeListener != null) {
                    IndicatorSeekBar.this.mIndicatorSeekBarChangeListener.onStopTrackingTouch(seekBar);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatSeekBar, android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public synchronized void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        String str = getProgress() + "";
        this.mPaint.getTextBounds(str, 0, str.length(), this.mProgressTextRect);
        float progress = getProgress() / getMax();
        canvas.drawText(str, (getWidth() * progress) + (((this.mThumbWidth - this.mProgressTextRect.width()) / 2) - (this.mThumbWidth * progress)), (getHeight() / 2.0f) + (this.mProgressTextRect.height() / 2.0f), this.mPaint);
        if (this.mIndicatorSeekBarChangeListener != null) {
            int i2 = this.mIndicatorWidth;
            this.mIndicatorSeekBarChangeListener.onProgressChanged(this, getProgress(), ((getWidth() * progress) - ((i2 - i) / 2)) - (this.mThumbWidth * progress));
        }
    }

    public void setOnSeekBarChangeListener(OnIndicatorSeekBarChangeListener onIndicatorSeekBarChangeListener) {
        this.mIndicatorSeekBarChangeListener = onIndicatorSeekBarChangeListener;
    }

    public int dp2px(float f) {
        return (int) TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }

    private int sp2px(float f) {
        return (int) TypedValue.applyDimension(2, f, getResources().getDisplayMetrics());
    }
}
