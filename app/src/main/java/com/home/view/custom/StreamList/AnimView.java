package com.home.view.custom.StreamList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/* loaded from: classes.dex */
public class AnimView extends View {
    private int PULL_DELTA;
    private int PULL_WIDTH;
    private long bezierBackDur;
    private float bezierBackRatio;
    boolean isBezierBackDone;
    private AnimatorStatus mAniStatus;
    private Paint mBackPaint;
    private int mBezierDeta;
    private int mHeight;
    private Path mPath;
    private long mStart;
    private long mStop;
    private int mWidth;

    /* loaded from: classes.dex */
    enum AnimatorStatus {
        PULL_LEFT,
        DRAG_LEFT,
        RELEASE
    }

    public AnimView(Context context) {
        this(context, null, 0);
    }

    public AnimView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AnimView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isBezierBackDone = false;
        this.mAniStatus = AnimatorStatus.PULL_LEFT;
        init(context);
    }

    private void init(Context context) {
        this.PULL_WIDTH = (int) TypedValue.applyDimension(1, 20.0f, context.getResources().getDisplayMetrics());
        this.PULL_DELTA = (int) TypedValue.applyDimension(1, 80.0f, context.getResources().getDisplayMetrics());
        this.mPath = new Path();
        Paint paint = new Paint();
        this.mBackPaint = paint;
        paint.setAntiAlias(true);
        this.mBackPaint.setStyle(Paint.Style.FILL);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int i3 = this.PULL_DELTA;
        int i4 = this.PULL_WIDTH;
        if (size > i3 + i4) {
            i = View.MeasureSpec.makeMeasureSpec(i3 + i4, View.MeasureSpec.getMode(i));
        }
        super.onMeasure(i, i2);
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.mWidth = getWidth();
            this.mHeight = getHeight();
            if (this.mWidth < this.PULL_WIDTH) {
                this.mAniStatus = AnimatorStatus.PULL_LEFT;
            }
            if (AnonymousClass1.$SwitchMap$com$home$view$custom$StreamList$AnimView$AnimatorStatus[this.mAniStatus.ordinal()] == 1 && this.mWidth >= this.PULL_WIDTH) {
                this.mAniStatus = AnimatorStatus.DRAG_LEFT;
            }
        }
    }

    /* renamed from: com.home.view.custom.StreamList.AnimView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$home$view$custom$StreamList$AnimView$AnimatorStatus;

        static {
            int[] iArr = new int[AnimatorStatus.values().length];
            $SwitchMap$com$home$view$custom$StreamList$AnimView$AnimatorStatus = iArr;
            try {
                iArr[AnimatorStatus.PULL_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$home$view$custom$StreamList$AnimView$AnimatorStatus[AnimatorStatus.DRAG_LEFT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$home$view$custom$StreamList$AnimView$AnimatorStatus[AnimatorStatus.RELEASE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = AnonymousClass1.$SwitchMap$com$home$view$custom$StreamList$AnimView$AnimatorStatus[this.mAniStatus.ordinal()];
        if (i == 1) {
            canvas.drawRect(0.0f, 0.0f, this.mWidth, this.mHeight, this.mBackPaint);
        } else if (i == 2) {
            drawDrag(canvas);
        } else if (i != 3) {
        } else {
            drawBack(canvas, getBezierDelta());
        }
    }

    private void drawDrag(Canvas canvas) {
        int i = this.mWidth;
        canvas.drawRect(i - this.PULL_WIDTH, 0.0f, i, this.mHeight, this.mBackPaint);
        this.mPath.reset();
        this.mPath.moveTo(this.mWidth - this.PULL_WIDTH, 0.0f);
        Path path = this.mPath;
        int i2 = this.mHeight;
        path.quadTo(0.0f, i2 / 2, this.mWidth - this.PULL_WIDTH, i2);
        canvas.drawPath(this.mPath, this.mBackPaint);
    }

    private void drawBack(Canvas canvas, int i) {
        this.mPath.reset();
        this.mPath.moveTo(this.mWidth, 0.0f);
        this.mPath.lineTo(this.mWidth - this.PULL_WIDTH, 0.0f);
        int i2 = this.mHeight;
        this.mPath.quadTo(i, i2 / 2, this.mWidth - this.PULL_WIDTH, i2);
        this.mPath.lineTo(this.mWidth, this.mHeight);
        canvas.drawPath(this.mPath, this.mBackPaint);
        invalidate();
        if (this.bezierBackRatio == 1.0f) {
            this.isBezierBackDone = true;
        }
        if (!this.isBezierBackDone || this.mWidth > this.PULL_WIDTH) {
            return;
        }
        drawFooterBack(canvas);
    }

    private void drawFooterBack(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, this.mWidth, this.mHeight, this.mBackPaint);
    }

    public void releaseDrag() {
        this.mAniStatus = AnimatorStatus.RELEASE;
        long currentTimeMillis = System.currentTimeMillis();
        this.mStart = currentTimeMillis;
        this.mStop = currentTimeMillis + this.bezierBackDur;
        this.mBezierDeta = this.mWidth - this.PULL_WIDTH;
        this.isBezierBackDone = false;
        requestLayout();
    }

    public void setBezierBackDur(long j) {
        this.bezierBackDur = j;
    }

    private int getBezierDelta() {
        float bezierBackRatio = getBezierBackRatio();
        this.bezierBackRatio = bezierBackRatio;
        return (int) (this.mBezierDeta * bezierBackRatio);
    }

    private float getBezierBackRatio() {
        if (System.currentTimeMillis() >= this.mStop) {
            return 1.0f;
        }
        return Math.min(1.0f, ((float) (System.currentTimeMillis() - this.mStart)) / ((float) this.bezierBackDur));
    }

    public void setBgColor(int i) {
        this.mBackPaint.setColor(i);
    }
}
