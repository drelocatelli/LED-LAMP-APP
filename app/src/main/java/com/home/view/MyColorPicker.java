package com.home.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class MyColorPicker extends View {
    private static final int[] COLORS = {SupportMenu.CATEGORY_MASK, -65281, -16776961, -16711681, -16711936, InputDeviceCompat.SOURCE_ANY, SupportMenu.CATEGORY_MASK};
    private static final String STATE_ANGLE = "angle";
    private static final String STATE_OLD_COLOR = "color";
    private static final String STATE_PARENT = "parent";
    private static final String STATE_SHOW_OLD_COLOR = "showColor";
    private static final String TAG = "MyColorPicker";
    private int addSize;
    private float mAngle;
    private Paint mCenterHaloPaint;
    private int mCenterNewColor;
    private Paint mCenterNewPaint;
    private int mCenterOldColor;
    private Paint mCenterOldPaint;
    private RectF mCenterRectangle;
    private int mColor;
    private int mColorCenterHaloRadius;
    private int mColorCenterRadius;
    private int mColorPointerHaloRadius;
    private int mColorPointerRadius;
    private Paint mColorWheelPaint;
    private int mColorWheelRadius;
    private RectF mColorWheelRectangle;
    private int mColorWheelThickness;
    private float[] mHSV;
    private Paint mPaint;
    private Paint mPointerColor;
    private Paint mPointerHaloPaint;
    private int mPreferredColorCenterHaloRadius;
    private int mPreferredColorCenterRadius;
    private int mPreferredColorWheelRadius;
    private RectF mRectF;
    private boolean mShowCenterOldColor;
    private float mSlopX;
    private float mSlopY;
    private float mTranslationOffset;
    private boolean mUserIsMovingPointer;
    private int oldChangedListenerColor;
    private int oldSelectedListenerColor;
    private OnColorChangedListener onColorChangedListener;
    private OnColorSelectedListener onColorSelectedListener;

    /* loaded from: classes.dex */
    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    /* loaded from: classes.dex */
    public interface OnColorSelectedListener {
        void onColorSelected(int i);
    }

    public MyColorPicker(Context context) {
        super(context);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mRectF = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        init(null, 0);
    }

    public MyColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mRectF = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        init(attributeSet, 0);
    }

    public MyColorPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mRectF = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        init(attributeSet, i);
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        this.onColorChangedListener = onColorChangedListener;
    }

    public OnColorChangedListener getOnColorChangedListener() {
        return this.onColorChangedListener;
    }

    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        this.onColorSelectedListener = onColorSelectedListener;
    }

    public OnColorSelectedListener getOnColorSelectedListener() {
        return this.onColorSelectedListener;
    }

    private void init(AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ColorPicker, i, 0);
        Resources resources = getContext().getResources();
        this.mColorWheelThickness = obtainStyledAttributes.getDimensionPixelSize(5, resources.getDimensionPixelSize(R.dimen.color_wheel_thickness));
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(4, resources.getDimensionPixelSize(R.dimen.color_wheel_radius));
        this.mColorWheelRadius = dimensionPixelSize;
        this.mPreferredColorWheelRadius = dimensionPixelSize;
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(1, resources.getDimensionPixelSize(R.dimen.color_center_radius));
        this.mColorCenterRadius = dimensionPixelSize2;
        this.mPreferredColorCenterRadius = dimensionPixelSize2;
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(0, resources.getDimensionPixelSize(R.dimen.color_center_halo_radius));
        this.mColorCenterHaloRadius = dimensionPixelSize3;
        this.mPreferredColorCenterHaloRadius = dimensionPixelSize3;
        this.mColorPointerRadius = obtainStyledAttributes.getDimensionPixelSize(3, resources.getDimensionPixelSize(R.dimen.color_pointer_radius));
        this.mColorPointerHaloRadius = obtainStyledAttributes.getDimensionPixelSize(2, resources.getDimensionPixelSize(R.dimen.color_pointer_halo_radius));
        obtainStyledAttributes.recycle();
        this.mAngle = -1.5707964f;
        SweepGradient sweepGradient = new SweepGradient(0.0f, 0.0f, COLORS, (float[]) null);
        Paint paint = new Paint(1);
        this.mColorWheelPaint = paint;
        paint.setShader(sweepGradient);
        this.mColorWheelPaint.setStyle(Paint.Style.STROKE);
        this.mColorWheelPaint.setStrokeWidth(this.mColorWheelThickness);
        Paint paint2 = new Paint(1);
        this.mPointerHaloPaint = paint2;
        paint2.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPointerHaloPaint.setAlpha(80);
        Paint paint3 = new Paint(1);
        this.mPointerColor = paint3;
        paint3.setColor(calculateColor(this.mAngle));
        Paint paint4 = new Paint();
        this.mPaint = paint4;
        paint4.setStrokeWidth(10.0f);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setColor(calculateColor(this.mAngle));
        Paint paint5 = new Paint(1);
        this.mCenterNewPaint = paint5;
        paint5.setColor(calculateColor(this.mAngle));
        this.mCenterNewPaint.setStyle(Paint.Style.FILL);
        Paint paint6 = new Paint(1);
        this.mCenterOldPaint = paint6;
        paint6.setColor(calculateColor(this.mAngle));
        this.mCenterOldPaint.setStyle(Paint.Style.FILL);
        Paint paint7 = new Paint(1);
        this.mCenterHaloPaint = paint7;
        paint7.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCenterHaloPaint.setAlpha(0);
        this.mCenterNewColor = calculateColor(this.mAngle);
        this.mCenterOldColor = calculateColor(this.mAngle);
        this.mShowCenterOldColor = false;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float f = this.mTranslationOffset;
        canvas.translate(f, f);
        canvas.drawOval(this.mColorWheelRectangle, this.mColorWheelPaint);
        float[] calculatePointerPosition = calculatePointerPosition(this.mAngle);
        canvas.drawCircle(calculatePointerPosition[0], calculatePointerPosition[1], this.mColorPointerRadius, this.mPointerColor);
        SweepGradient sweepGradient = new SweepGradient(0.0f, 0.0f, new int[]{0, calculateColor(this.mAngle), Color.parseColor("#00FFFFFF")}, (float[]) null);
        Matrix matrix = new Matrix();
        matrix.setRotate(getVelue(this.mAngle) - 180, 0.0f, 0.0f);
        sweepGradient.setLocalMatrix(matrix);
        this.mPaint.setShader(sweepGradient);
        canvas.drawArc(this.mRectF, getVelue(this.mAngle) - 180, 180.0f, false, this.mPaint);
        if (this.mShowCenterOldColor) {
            canvas.drawArc(this.mCenterRectangle, 0.0f, 360.0f, true, this.mCenterNewPaint);
            return;
        }
        this.mCenterNewPaint.setMaskFilter(new BlurMaskFilter(20.0f, BlurMaskFilter.Blur.NORMAL));
        canvas.drawArc(this.mCenterRectangle, 0.0f, 360.0f, true, this.mCenterNewPaint);
    }

    private int getVelue(float f) {
        if (f >= 0.0f) {
            double d = f;
            Double.isNaN(d);
            return (int) (d * 57.29577951308232d);
        }
        double d2 = f;
        Double.isNaN(d2);
        return (int) (((d2 + 3.141592653589793d) * 57.29577951308232d) + 180.0d);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3 = (this.mPreferredColorWheelRadius + this.mColorPointerHaloRadius) * 2;
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        if (size > 1100) {
            this.addSize = 120;
        } else if (1000 >= size && size >= 800) {
            this.addSize = 100;
        } else {
            this.addSize = 80;
        }
        Log.e(TAG, "onMeasure: " + size + "+++" + size2);
        if (mode != 1073741824) {
            size = mode == Integer.MIN_VALUE ? Math.min(i3, size) : i3;
        }
        if (mode2 == 1073741824) {
            i3 = size2;
        } else if (mode2 == Integer.MIN_VALUE) {
            i3 = Math.min(i3, size2);
        }
        int min = Math.min(size, i3);
        setMeasuredDimension(min, min);
        this.mTranslationOffset = min * 0.5f;
        int i4 = ((min / 2) - this.mColorWheelThickness) - this.mColorPointerHaloRadius;
        this.mColorWheelRadius = i4;
        this.mColorWheelRectangle.set(-i4, -i4, i4, i4);
        int i5 = this.mColorWheelRadius;
        int i6 = this.mPreferredColorWheelRadius;
        int i7 = (int) (this.mPreferredColorCenterRadius * (i5 / i6));
        this.mColorCenterRadius = i7;
        this.mColorCenterHaloRadius = (int) (this.mPreferredColorCenterHaloRadius * (i5 / i6));
        this.mCenterRectangle.set(-i7, -i7, i7, i7);
        RectF rectF = this.mRectF;
        int i8 = this.mColorWheelRadius;
        int i9 = this.addSize;
        rectF.set((-i8) - i9, (-i8) - i9, i8 + i9, i8 + i9);
    }

    private int ave(int i, int i2, float f) {
        return i + Math.round(f * (i2 - i));
    }

    private int calculateColor(float f) {
        double d = f;
        Double.isNaN(d);
        float f2 = (float) (d / 6.283185307179586d);
        if (f2 < 0.0f) {
            f2 += 1.0f;
        }
        if (f2 <= 0.0f) {
            int[] iArr = COLORS;
            this.mColor = iArr[0];
            return iArr[0];
        } else if (f2 >= 1.0f) {
            int[] iArr2 = COLORS;
            this.mColor = iArr2[iArr2.length - 1];
            return iArr2[iArr2.length - 1];
        } else {
            int[] iArr3 = COLORS;
            float length = f2 * (iArr3.length - 1);
            int i = (int) length;
            float f3 = length - i;
            int i2 = iArr3[i];
            int i3 = iArr3[i + 1];
            int ave = ave(Color.alpha(i2), Color.alpha(i3), f3);
            int ave2 = ave(Color.red(i2), Color.red(i3), f3);
            int ave3 = ave(Color.green(i2), Color.green(i3), f3);
            int ave4 = ave(Color.blue(i2), Color.blue(i3), f3);
            this.mColor = Color.argb(ave, ave2, ave3, ave4);
            return Color.argb(ave, ave2, ave3, ave4);
        }
    }

    public int getColor() {
        return this.mCenterNewColor;
    }

    public void setColor(int i) {
        float colorToAngle = colorToAngle(i);
        this.mAngle = colorToAngle;
        this.mPointerColor.setColor(calculateColor(colorToAngle));
        this.mCenterNewPaint.setColor(calculateColor(this.mAngle));
        invalidate();
    }

    private float colorToAngle(int i) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        return (float) Math.toRadians(-fArr[0]);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        OnColorSelectedListener onColorSelectedListener;
        int i2;
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = motionEvent.getX() - this.mTranslationOffset;
        float y = motionEvent.getY() - this.mTranslationOffset;
        int action = motionEvent.getAction();
        if (action == 0) {
            float[] calculatePointerPosition = calculatePointerPosition(this.mAngle);
            float f = calculatePointerPosition[0];
            int i3 = this.mColorPointerHaloRadius;
            if (x >= f - i3 && x <= calculatePointerPosition[0] + i3 && y >= calculatePointerPosition[1] - i3 && y <= calculatePointerPosition[1] + i3) {
                this.mSlopX = x - calculatePointerPosition[0];
                this.mSlopY = y - calculatePointerPosition[1];
                this.mUserIsMovingPointer = true;
                invalidate();
            } else {
                int i4 = this.mColorCenterRadius;
                if (x >= (-i4) && x <= i4 && y >= (-i4) && y <= i4 && this.mShowCenterOldColor) {
                    this.mCenterHaloPaint.setAlpha(80);
                    setColor(getOldCenterColor());
                    invalidate();
                } else {
                    float atan2 = (float) Math.atan2(y, x);
                    this.mAngle = atan2;
                    this.mPointerColor.setColor(calculateColor(atan2));
                    int calculateColor = calculateColor(this.mAngle);
                    this.mCenterNewColor = calculateColor;
                    setNewCenterColor(calculateColor);
                    invalidate();
                }
            }
        } else if (action == 1) {
            this.mUserIsMovingPointer = false;
            this.mCenterHaloPaint.setAlpha(0);
            OnColorSelectedListener onColorSelectedListener2 = this.onColorSelectedListener;
            if (onColorSelectedListener2 != null && (i = this.mCenterNewColor) != this.oldSelectedListenerColor) {
                onColorSelectedListener2.onColorSelected(i);
                this.oldSelectedListenerColor = this.mCenterNewColor;
            }
            invalidate();
        } else if (action != 2) {
            if (action == 3 && (onColorSelectedListener = this.onColorSelectedListener) != null && (i2 = this.mCenterNewColor) != this.oldSelectedListenerColor) {
                onColorSelectedListener.onColorSelected(i2);
                this.oldSelectedListenerColor = this.mCenterNewColor;
            }
        } else if (this.mUserIsMovingPointer) {
            float atan22 = (float) Math.atan2(y - this.mSlopY, x - this.mSlopX);
            this.mAngle = atan22;
            this.mPointerColor.setColor(calculateColor(atan22));
            int calculateColor2 = calculateColor(this.mAngle);
            this.mCenterNewColor = calculateColor2;
            setNewCenterColor(calculateColor2);
            invalidate();
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            return false;
        }
        return true;
    }

    private float[] calculatePointerPosition(float f) {
        double d = this.mColorWheelRadius + this.addSize;
        double d2 = f;
        double cos = Math.cos(d2);
        Double.isNaN(d);
        float f2 = (float) (d * cos);
        double d3 = this.mColorWheelRadius + this.addSize;
        double sin = Math.sin(d2);
        Double.isNaN(d3);
        return new float[]{f2, (float) (d3 * sin)};
    }

    public void setNewCenterColor(int i) {
        this.mCenterNewColor = i;
        this.mCenterNewPaint.setColor(i);
        if (this.mCenterOldColor == 0) {
            this.mCenterOldColor = i;
            this.mCenterOldPaint.setColor(i);
        }
        OnColorChangedListener onColorChangedListener = this.onColorChangedListener;
        if (onColorChangedListener != null && i != this.oldChangedListenerColor) {
            onColorChangedListener.onColorChanged(i);
            this.oldChangedListenerColor = i;
        }
        invalidate();
    }

    public void setOldCenterColor(int i) {
        this.mCenterOldColor = i;
        this.mCenterOldPaint.setColor(i);
        invalidate();
    }

    public int getOldCenterColor() {
        return this.mCenterOldColor;
    }

    public void setShowOldCenterColor(boolean z) {
        this.mShowCenterOldColor = z;
        invalidate();
    }

    public boolean getShowOldCenterColor() {
        return this.mShowCenterOldColor;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_PARENT, onSaveInstanceState);
        bundle.putFloat(STATE_ANGLE, this.mAngle);
        bundle.putInt(STATE_OLD_COLOR, this.mCenterOldColor);
        bundle.putBoolean(STATE_SHOW_OLD_COLOR, this.mShowCenterOldColor);
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable(STATE_PARENT));
        this.mAngle = bundle.getFloat(STATE_ANGLE);
        setOldCenterColor(bundle.getInt(STATE_OLD_COLOR));
        this.mShowCenterOldColor = bundle.getBoolean(STATE_SHOW_OLD_COLOR);
        int calculateColor = calculateColor(this.mAngle);
        this.mPointerColor.setColor(calculateColor);
        setNewCenterColor(calculateColor);
    }
}
