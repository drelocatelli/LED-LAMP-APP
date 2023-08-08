package com.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class VolumCircleBar extends View {
    private static final String TAG = "VolumCircleBar";
    private final int CIRCLE_INNER_DISTANCE_TO_OUTSIDE;
    private final int VOLUM_INDICATE_LENGTH;
    private RectF arcRect;
    private int circleWidth;
    private Object lock;
    private Paint mPaint;
    private Matrix matrix;
    private Paint paintCircle;
    private Paint paintText;
    private int recordMode;
    private int recordingColor;
    private int spliteAngle;
    private int stoppedColor;
    private int totalBlockCount;
    private Thread uiThread;
    private float volumRate;

    private void initBitmapMatrix() {
    }

    public VolumCircleBar(Context context) {
        this(context, null);
    }

    public VolumCircleBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VolumCircleBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.lock = new Object();
        this.paintText = new Paint();
        this.paintCircle = new Paint();
        this.matrix = new Matrix();
        this.VOLUM_INDICATE_LENGTH = 15;
        this.CIRCLE_INNER_DISTANCE_TO_OUTSIDE = 8;
        init(context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.VolumCircleBar, i, 0), context);
    }

    private void init(TypedArray typedArray, Context context) {
        for (int i = 0; i < typedArray.length(); i++) {
            if (i == 0) {
                this.recordingColor = typedArray.getColor(i, 0);
            } else if (i == 1) {
                this.stoppedColor = typedArray.getColor(i, 0);
            } else if (i == 3) {
                this.totalBlockCount = typedArray.getInt(i, 100);
            } else if (i == 4) {
                this.spliteAngle = typedArray.getInt(i, 1);
            }
        }
        typedArray.recycle();
        this.uiThread = Thread.currentThread();
        this.mPaint = new Paint();
        if (this.spliteAngle * this.totalBlockCount > 360) {
            throw new IllegalArgumentException("spliteAngle * blockCount > 360, while the result should be less than 360.");
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        if (size >= size2) {
            size = size2;
        }
        this.circleWidth = size;
        setMeasuredDimension(size, size);
    }

    public void updateVolumRate(float f) {
        synchronized (this.lock) {
            this.volumRate = f;
            if (Thread.currentThread() != this.uiThread) {
                postInvalidate();
            } else {
                invalidate();
            }
        }
    }

    public void toggleRecord(int i) {
        synchronized (this.lock) {
            this.recordMode = i;
            if (Thread.currentThread() != this.uiThread) {
                postInvalidate();
            } else {
                invalidate();
            }
        }
    }

    public int recordMode() {
        return this.recordMode;
    }

    public void setRecording(int i) {
        this.recordMode = i;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        if (this.arcRect == null) {
            int i2 = this.circleWidth;
            this.arcRect = new RectF(8.0f, 8.0f, i2 - 8, i2 - 8);
        }
        canvas.drawColor(0);
        synchronized (this.lock) {
            if (this.recordMode != 4) {
                int i3 = 1;
                this.mPaint.setAntiAlias(true);
                this.mPaint.setColor(this.recordingColor);
                this.mPaint.setStrokeWidth(1.0f);
                this.mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                int i4 = this.circleWidth;
                canvas.drawCircle(i4 / 2.0f, i4 / 2.0f, i4 / 2.0f, this.mPaint);
                int i5 = this.spliteAngle;
                int i6 = this.totalBlockCount;
                float f = (360.0f - (i5 * i6)) / i6;
                int i7 = (int) (i6 * this.volumRate);
                if (i7 >= 1) {
                    i3 = i7;
                }
                this.mPaint.setStrokeWidth(15.0f);
                this.mPaint.setStyle(Paint.Style.STROKE);
                for (int i8 = 0; i8 < i3; i8++) {
                    int i9 = i8 * 254;
                    this.mPaint.setColor(Color.rgb(i9 / i3, 100, i9 / i3));
                    canvas.drawArc(this.arcRect, (i8 * (this.spliteAngle + f)) - 90.0f, f, false, this.mPaint);
                }
                this.paintText.setColor(-1);
                this.paintText.setTextSize(80.0f);
                this.paintText.setStrokeWidth(0.4f);
                this.paintText.setStyle(Paint.Style.FILL);
                this.paintText.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(i3 + "%", i / 2, this.circleWidth / 4.0f, this.paintText);
            } else {
                this.mPaint.setColor(this.stoppedColor);
                this.mPaint.setStrokeWidth(1.0f);
                this.mPaint.setStyle(Paint.Style.FILL);
                int i10 = this.circleWidth;
                canvas.drawCircle(i10 / 2.0f, i10 / 2.0f, i10 / 2.0f, this.mPaint);
            }
        }
    }
}
