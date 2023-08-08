package com.itheima.wheelpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import androidx.appcompat.widget.ActivityChooserView;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class WheelPicker extends View implements IDebug, IWheelPicker, Runnable {
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SCROLLING = 2;
    private static final String TAG = "WheelPicker";
    private boolean hasAtmospheric;
    private boolean hasCurtain;
    private boolean hasIndicator;
    private boolean hasSameWidth;
    private boolean isClick;
    private boolean isCurved;
    private boolean isCyclic;
    private boolean isDebug;
    private boolean isForceFinishScroll;
    private Camera mCamera;
    private int mCurrentItemPosition;
    private int mCurtainColor;
    private List mData;
    private int mDownPointY;
    private int mDrawnCenterX;
    private int mDrawnCenterY;
    private int mDrawnItemCount;
    private int mHalfDrawnItemCount;
    private int mHalfItemHeight;
    private int mHalfWheelHeight;
    private final Handler mHandler;
    private int mIndicatorColor;
    private int mIndicatorSize;
    private int mItemAlign;
    private int mItemHeight;
    private int mItemSpace;
    private int mItemTextColor;
    private int mItemTextSize;
    private int mLastPointY;
    private Matrix mMatrixDepth;
    private Matrix mMatrixRotate;
    private int mMaxFlingY;
    private String mMaxWidthText;
    private int mMaximumVelocity;
    private int mMinFlingY;
    private int mMinimumVelocity;
    private OnItemSelectedListener mOnItemSelectedListener;
    private OnWheelChangeListener mOnWheelChangeListener;
    private Paint mPaint;
    private Rect mRectCurrentItem;
    private Rect mRectDrawn;
    private Rect mRectIndicatorFoot;
    private Rect mRectIndicatorHead;
    private int mScrollOffsetY;
    private Scroller mScroller;
    private int mSelectedItemPosition;
    private int mSelectedItemTextColor;
    private int mTextMaxHeight;
    private int mTextMaxWidth;
    private int mTextMaxWidthPosition;
    private int mTouchSlop;
    private VelocityTracker mTracker;
    private int mVisibleItemCount;
    private int mWheelCenterX;
    private int mWheelCenterY;

    /* loaded from: classes.dex */
    public interface OnItemSelectedListener {
        void onItemSelected(WheelPicker wheelPicker, Object obj, int i);
    }

    /* loaded from: classes.dex */
    public interface OnWheelChangeListener {
        void onWheelScrollStateChanged(int i);

        void onWheelScrolled(int i);

        void onWheelSelected(int i);
    }

    public WheelPicker(Context context) {
        this(context, null);
    }

    public WheelPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHandler = new Handler();
        this.mMinimumVelocity = 50;
        this.mMaximumVelocity = 8000;
        this.mTouchSlop = 8;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.WheelPicker);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.WheelPicker_wheel_data, 0);
        this.mData = Arrays.asList(getResources().getStringArray(resourceId == 0 ? R.array.WheelArrayDefault : resourceId));
        this.mItemTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WheelPicker_wheel_item_text_size, getResources().getDimensionPixelSize(R.dimen.WheelItemTextSize));
        this.mVisibleItemCount = obtainStyledAttributes.getInt(R.styleable.WheelPicker_wheel_visible_item_count, 7);
        this.mSelectedItemPosition = obtainStyledAttributes.getInt(R.styleable.WheelPicker_wheel_selected_item_position, 0);
        this.hasSameWidth = obtainStyledAttributes.getBoolean(R.styleable.WheelPicker_wheel_same_width, false);
        this.mTextMaxWidthPosition = obtainStyledAttributes.getInt(R.styleable.WheelPicker_wheel_maximum_width_text_position, -1);
        this.mMaxWidthText = obtainStyledAttributes.getString(R.styleable.WheelPicker_wheel_maximum_width_text);
        this.mSelectedItemTextColor = obtainStyledAttributes.getColor(R.styleable.WheelPicker_wheel_selected_item_text_color, -1);
        this.mItemTextColor = obtainStyledAttributes.getColor(R.styleable.WheelPicker_wheel_item_text_color, -7829368);
        this.mItemSpace = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WheelPicker_wheel_item_space, getResources().getDimensionPixelSize(R.dimen.WheelItemSpace));
        this.isCyclic = obtainStyledAttributes.getBoolean(R.styleable.WheelPicker_wheel_cyclic, false);
        this.hasIndicator = obtainStyledAttributes.getBoolean(R.styleable.WheelPicker_wheel_indicator, false);
        this.mIndicatorColor = obtainStyledAttributes.getColor(R.styleable.WheelPicker_wheel_indicator_color, -1166541);
        this.mIndicatorSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WheelPicker_wheel_indicator_size, getResources().getDimensionPixelSize(R.dimen.WheelIndicatorSize));
        this.hasCurtain = obtainStyledAttributes.getBoolean(R.styleable.WheelPicker_wheel_curtain, false);
        this.mCurtainColor = obtainStyledAttributes.getColor(R.styleable.WheelPicker_wheel_curtain_color, -1996488705);
        this.hasAtmospheric = obtainStyledAttributes.getBoolean(R.styleable.WheelPicker_wheel_atmospheric, false);
        this.isCurved = obtainStyledAttributes.getBoolean(R.styleable.WheelPicker_wheel_curved, false);
        this.mItemAlign = obtainStyledAttributes.getInt(R.styleable.WheelPicker_wheel_item_align, 0);
        obtainStyledAttributes.recycle();
        updateVisibleItemCount();
        Paint paint = new Paint(69);
        this.mPaint = paint;
        paint.setTextSize(this.mItemTextSize);
        updateItemTextAlign();
        computeTextSize();
        this.mScroller = new Scroller(getContext());
        if (Build.VERSION.SDK_INT >= 4) {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
            this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
            this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        }
        this.mRectDrawn = new Rect();
        this.mRectIndicatorHead = new Rect();
        this.mRectIndicatorFoot = new Rect();
        this.mRectCurrentItem = new Rect();
        this.mCamera = new Camera();
        this.mMatrixRotate = new Matrix();
        this.mMatrixDepth = new Matrix();
    }

    private void updateVisibleItemCount() {
        int i = this.mVisibleItemCount;
        if (i < 2) {
            throw new ArithmeticException("Wheel's visible item count can not be less than 2!");
        }
        if (i % 2 == 0) {
            this.mVisibleItemCount = i + 1;
        }
        int i2 = this.mVisibleItemCount + 2;
        this.mDrawnItemCount = i2;
        this.mHalfDrawnItemCount = i2 / 2;
    }

    private void computeTextSize() {
        this.mTextMaxHeight = 0;
        this.mTextMaxWidth = 0;
        if (this.hasSameWidth) {
            this.mTextMaxWidth = (int) this.mPaint.measureText(String.valueOf(this.mData.get(0)));
        } else if (isPosInRang(this.mTextMaxWidthPosition)) {
            this.mTextMaxWidth = (int) this.mPaint.measureText(String.valueOf(this.mData.get(this.mTextMaxWidthPosition)));
        } else if (!TextUtils.isEmpty(this.mMaxWidthText)) {
            this.mTextMaxWidth = (int) this.mPaint.measureText(this.mMaxWidthText);
        } else {
            for (Object obj : this.mData) {
                String valueOf = String.valueOf(obj);
                this.mTextMaxWidth = Math.max(this.mTextMaxWidth, (int) this.mPaint.measureText(valueOf));
            }
        }
        Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
        this.mTextMaxHeight = (int) (fontMetrics.bottom - fontMetrics.top);
    }

    private void updateItemTextAlign() {
        int i = this.mItemAlign;
        if (i == 1) {
            this.mPaint.setTextAlign(Paint.Align.LEFT);
        } else if (i == 2) {
            this.mPaint.setTextAlign(Paint.Align.RIGHT);
        } else {
            this.mPaint.setTextAlign(Paint.Align.CENTER);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int i3 = this.mTextMaxWidth;
        int i4 = this.mTextMaxHeight;
        int i5 = this.mVisibleItemCount;
        int i6 = (i4 * i5) + (this.mItemSpace * (i5 - 1));
        if (this.isCurved) {
            double d = i6 * 2;
            Double.isNaN(d);
            i6 = (int) (d / 3.141592653589793d);
        }
        if (this.isDebug) {
            Log.i(TAG, "Wheel's content size is (" + i3 + ":" + i6 + ")");
        }
        int paddingLeft = i3 + getPaddingLeft() + getPaddingRight();
        int paddingTop = i6 + getPaddingTop() + getPaddingBottom();
        if (this.isDebug) {
            Log.i(TAG, "Wheel's size is (" + paddingLeft + ":" + paddingTop + ")");
        }
        setMeasuredDimension(measureSize(mode, size, paddingLeft), measureSize(mode2, size2, paddingTop));
    }

    private int measureSize(int i, int i2, int i3) {
        return i == 1073741824 ? i2 : i == Integer.MIN_VALUE ? Math.min(i3, i2) : i3;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mRectDrawn.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        if (this.isDebug) {
            String str = TAG;
            Log.i(str, "Wheel's drawn rect size is (" + this.mRectDrawn.width() + ":" + this.mRectDrawn.height() + ") and location is (" + this.mRectDrawn.left + ":" + this.mRectDrawn.top + ")");
        }
        this.mWheelCenterX = this.mRectDrawn.centerX();
        this.mWheelCenterY = this.mRectDrawn.centerY();
        computeDrawnCenter();
        this.mHalfWheelHeight = this.mRectDrawn.height() / 2;
        int height = this.mRectDrawn.height() / this.mVisibleItemCount;
        this.mItemHeight = height;
        this.mHalfItemHeight = height / 2;
        computeFlingLimitY();
        computeIndicatorRect();
        computeCurrentItemRect();
    }

    private void computeDrawnCenter() {
        int i = this.mItemAlign;
        if (i == 1) {
            this.mDrawnCenterX = this.mRectDrawn.left;
        } else if (i == 2) {
            this.mDrawnCenterX = this.mRectDrawn.right;
        } else {
            this.mDrawnCenterX = this.mWheelCenterX;
        }
        this.mDrawnCenterY = (int) (this.mWheelCenterY - ((this.mPaint.ascent() + this.mPaint.descent()) / 2.0f));
    }

    private void computeFlingLimitY() {
        int i = this.mSelectedItemPosition;
        int i2 = this.mItemHeight;
        int i3 = i * i2;
        this.mMinFlingY = this.isCyclic ? Integer.MIN_VALUE : ((-i2) * (this.mData.size() - 1)) + i3;
        if (this.isCyclic) {
            i3 = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        }
        this.mMaxFlingY = i3;
    }

    private void computeIndicatorRect() {
        if (this.hasIndicator) {
            int i = this.mIndicatorSize / 2;
            int i2 = this.mWheelCenterY;
            int i3 = this.mHalfItemHeight;
            int i4 = i2 + i3;
            int i5 = i2 - i3;
            this.mRectIndicatorHead.set(this.mRectDrawn.left, i4 - i, this.mRectDrawn.right, i4 + i);
            this.mRectIndicatorFoot.set(this.mRectDrawn.left, i5 - i, this.mRectDrawn.right, i5 + i);
        }
    }

    private void computeCurrentItemRect() {
        if (this.hasCurtain || this.mSelectedItemTextColor != -1) {
            this.mRectCurrentItem.set(this.mRectDrawn.left, this.mWheelCenterY - this.mHalfItemHeight, this.mRectDrawn.right, this.mWheelCenterY + this.mHalfItemHeight);
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        String valueOf;
        int i;
        int i2;
        OnWheelChangeListener onWheelChangeListener = this.mOnWheelChangeListener;
        if (onWheelChangeListener != null) {
            onWheelChangeListener.onWheelScrolled(this.mScrollOffsetY);
        }
        int i3 = (-this.mScrollOffsetY) / this.mItemHeight;
        int i4 = this.mHalfDrawnItemCount;
        int i5 = i3 - i4;
        int i6 = this.mSelectedItemPosition + i5;
        int i7 = -i4;
        while (i6 < this.mSelectedItemPosition + i5 + this.mDrawnItemCount) {
            if (this.isCyclic) {
                int size = i6 % this.mData.size();
                if (size < 0) {
                    size += this.mData.size();
                }
                valueOf = String.valueOf(this.mData.get(size));
            } else {
                valueOf = isPosInRang(i6) ? String.valueOf(this.mData.get(i6)) : "";
            }
            this.mPaint.setColor(this.mItemTextColor);
            this.mPaint.setTextSize(this.mItemTextSize);
            this.mPaint.setStyle(Paint.Style.FILL);
            int i8 = this.mDrawnCenterY;
            int i9 = this.mItemHeight;
            int i10 = (i7 * i9) + i8 + (this.mScrollOffsetY % i9);
            if (this.isCurved) {
                float abs = (((i8 - Math.abs(i8 - i10)) - this.mRectDrawn.top) * 1.0f) / (this.mDrawnCenterY - this.mRectDrawn.top);
                int i11 = this.mDrawnCenterY;
                float f = (-(1.0f - abs)) * 90.0f * (i10 > i11 ? 1 : i10 < i11 ? -1 : 0);
                if (f < -90.0f) {
                    f = -90.0f;
                }
                float f2 = f <= 90.0f ? f : 90.0f;
                i = computeSpace((int) f2);
                int i12 = this.mWheelCenterX;
                int i13 = this.mItemAlign;
                if (i13 == 1) {
                    i12 = this.mRectDrawn.left;
                } else if (i13 == 2) {
                    i12 = this.mRectDrawn.right;
                }
                int i14 = this.mWheelCenterY - i;
                this.mCamera.save();
                this.mCamera.rotateX(f2);
                this.mCamera.getMatrix(this.mMatrixRotate);
                this.mCamera.restore();
                float f3 = -i12;
                float f4 = -i14;
                this.mMatrixRotate.preTranslate(f3, f4);
                float f5 = i12;
                float f6 = i14;
                this.mMatrixRotate.postTranslate(f5, f6);
                this.mCamera.save();
                this.mCamera.translate(0.0f, 0.0f, computeDepth(i2));
                this.mCamera.getMatrix(this.mMatrixDepth);
                this.mCamera.restore();
                this.mMatrixDepth.preTranslate(f3, f4);
                this.mMatrixDepth.postTranslate(f5, f6);
                this.mMatrixRotate.postConcat(this.mMatrixDepth);
            } else {
                i = 0;
            }
            if (this.hasAtmospheric) {
                int i15 = this.mDrawnCenterY;
                int abs2 = (int) ((((i15 - Math.abs(i15 - i10)) * 1.0f) / this.mDrawnCenterY) * 255.0f);
                this.mPaint.setAlpha(abs2 < 0 ? 0 : abs2);
            }
            if (this.isCurved) {
                i10 = this.mDrawnCenterY - i;
            }
            if (this.mSelectedItemTextColor != -1) {
                canvas.save();
                if (this.isCurved) {
                    canvas.concat(this.mMatrixRotate);
                }
                canvas.clipRect(this.mRectCurrentItem, Region.Op.DIFFERENCE);
                float f7 = i10;
                canvas.drawText(valueOf, this.mDrawnCenterX, f7, this.mPaint);
                canvas.restore();
                this.mPaint.setColor(this.mSelectedItemTextColor);
                this.mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
                this.mPaint.setTextSize(this.mItemTextSize + 15);
                canvas.save();
                if (this.isCurved) {
                    canvas.concat(this.mMatrixRotate);
                }
                canvas.clipRect(this.mRectCurrentItem);
                canvas.drawText(valueOf, this.mDrawnCenterX, f7, this.mPaint);
                canvas.restore();
            } else {
                canvas.save();
                canvas.clipRect(this.mRectDrawn);
                if (this.isCurved) {
                    canvas.concat(this.mMatrixRotate);
                }
                canvas.drawText(valueOf, this.mDrawnCenterX, i10, this.mPaint);
                canvas.restore();
            }
            i6++;
            i7++;
        }
        if (this.hasCurtain) {
            this.mPaint.setColor(this.mCurtainColor);
            this.mPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(this.mRectCurrentItem, this.mPaint);
        }
    }

    private boolean isPosInRang(int i) {
        return i >= 0 && i < this.mData.size();
    }

    private int computeSpace(int i) {
        double sin = Math.sin(Math.toRadians(i));
        double d = this.mHalfWheelHeight;
        Double.isNaN(d);
        return (int) (sin * d);
    }

    private int computeDepth(int i) {
        double d = this.mHalfWheelHeight;
        double cos = Math.cos(Math.toRadians(i));
        double d2 = this.mHalfWheelHeight;
        Double.isNaN(d2);
        Double.isNaN(d);
        return (int) (d - (cos * d2));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            VelocityTracker velocityTracker = this.mTracker;
            if (velocityTracker == null) {
                this.mTracker = VelocityTracker.obtain();
            } else {
                velocityTracker.clear();
            }
            this.mTracker.addMovement(motionEvent);
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                this.isForceFinishScroll = true;
            }
            int y = (int) motionEvent.getY();
            this.mLastPointY = y;
            this.mDownPointY = y;
        } else if (action == 1) {
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
            if (!this.isClick) {
                this.mTracker.addMovement(motionEvent);
                if (Build.VERSION.SDK_INT >= 4) {
                    this.mTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                } else {
                    this.mTracker.computeCurrentVelocity(1000);
                }
                this.isForceFinishScroll = false;
                int yVelocity = (int) this.mTracker.getYVelocity();
                if (Math.abs(yVelocity) > this.mMinimumVelocity) {
                    this.mScroller.fling(0, this.mScrollOffsetY, 0, yVelocity, 0, 0, this.mMinFlingY, this.mMaxFlingY);
                    Scroller scroller = this.mScroller;
                    scroller.setFinalY(scroller.getFinalY() + computeDistanceToEndPoint(this.mScroller.getFinalY() % this.mItemHeight));
                } else {
                    Scroller scroller2 = this.mScroller;
                    int i = this.mScrollOffsetY;
                    scroller2.startScroll(0, i, 0, computeDistanceToEndPoint(i % this.mItemHeight));
                }
                if (!this.isCyclic) {
                    int finalY = this.mScroller.getFinalY();
                    int i2 = this.mMaxFlingY;
                    if (finalY > i2) {
                        this.mScroller.setFinalY(i2);
                    } else {
                        int finalY2 = this.mScroller.getFinalY();
                        int i3 = this.mMinFlingY;
                        if (finalY2 < i3) {
                            this.mScroller.setFinalY(i3);
                        }
                    }
                }
                this.mHandler.post(this);
                VelocityTracker velocityTracker2 = this.mTracker;
                if (velocityTracker2 != null) {
                    velocityTracker2.recycle();
                    this.mTracker = null;
                }
            }
        } else if (action != 2) {
            if (action == 3) {
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                VelocityTracker velocityTracker3 = this.mTracker;
                if (velocityTracker3 != null) {
                    velocityTracker3.recycle();
                    this.mTracker = null;
                }
            }
        } else if (Math.abs(this.mDownPointY - motionEvent.getY()) < this.mTouchSlop) {
            this.isClick = true;
        } else {
            this.isClick = false;
            this.mTracker.addMovement(motionEvent);
            OnWheelChangeListener onWheelChangeListener = this.mOnWheelChangeListener;
            if (onWheelChangeListener != null) {
                onWheelChangeListener.onWheelScrollStateChanged(1);
            }
            float y2 = motionEvent.getY() - this.mLastPointY;
            if (Math.abs(y2) >= 1.0f) {
                this.mScrollOffsetY = (int) (this.mScrollOffsetY + y2);
                this.mLastPointY = (int) motionEvent.getY();
                invalidate();
            }
        }
        return true;
    }

    private int computeDistanceToEndPoint(int i) {
        int i2;
        if (Math.abs(i) > this.mHalfItemHeight) {
            if (this.mScrollOffsetY < 0) {
                i2 = -this.mItemHeight;
            } else {
                i2 = this.mItemHeight;
            }
            return i2 - i;
        }
        return -i;
    }

    @Override // java.lang.Runnable
    public void run() {
        List list = this.mData;
        if (list == null || list.size() == 0) {
            return;
        }
        if (this.mScroller.isFinished() && !this.isForceFinishScroll) {
            int i = this.mItemHeight;
            if (i == 0) {
                return;
            }
            int size = (((-this.mScrollOffsetY) / i) + this.mSelectedItemPosition) % this.mData.size();
            if (size < 0) {
                size += this.mData.size();
            }
            if (this.isDebug) {
                String str = TAG;
                Log.i(str, size + ":" + this.mData.get(size) + ":" + this.mScrollOffsetY);
            }
            this.mCurrentItemPosition = size;
            OnItemSelectedListener onItemSelectedListener = this.mOnItemSelectedListener;
            if (onItemSelectedListener != null) {
                onItemSelectedListener.onItemSelected(this, this.mData.get(size), size);
            }
            OnWheelChangeListener onWheelChangeListener = this.mOnWheelChangeListener;
            if (onWheelChangeListener != null) {
                onWheelChangeListener.onWheelSelected(size);
                this.mOnWheelChangeListener.onWheelScrollStateChanged(0);
            }
        }
        if (this.mScroller.computeScrollOffset()) {
            OnWheelChangeListener onWheelChangeListener2 = this.mOnWheelChangeListener;
            if (onWheelChangeListener2 != null) {
                onWheelChangeListener2.onWheelScrollStateChanged(2);
            }
            this.mScrollOffsetY = this.mScroller.getCurrY();
            postInvalidate();
            this.mHandler.postDelayed(this, 16L);
        }
    }

    @Override // com.itheima.wheelpicker.IDebug
    public void setDebug(boolean z) {
        this.isDebug = z;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getVisibleItemCount() {
        return this.mVisibleItemCount;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setVisibleItemCount(int i) {
        this.mVisibleItemCount = i;
        updateVisibleItemCount();
        requestLayout();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean isCyclic() {
        return this.isCyclic;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setCyclic(boolean z) {
        this.isCyclic = z;
        computeFlingLimitY();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getSelectedItemPosition() {
        return this.mSelectedItemPosition;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setSelectedItemPosition(int i) {
        int max = Math.max(Math.min(i, this.mData.size() - 1), 0);
        this.mSelectedItemPosition = max;
        this.mCurrentItemPosition = max;
        this.mScrollOffsetY = 0;
        computeFlingLimitY();
        requestLayout();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getCurrentItemPosition() {
        return this.mCurrentItemPosition;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public List getData() {
        return this.mData;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setData(List list) {
        Objects.requireNonNull(list, "WheelPicker's data can not be null!");
        this.mData = list;
        if (this.mSelectedItemPosition > list.size() - 1 || this.mCurrentItemPosition > list.size() - 1) {
            int size = list.size() - 1;
            this.mCurrentItemPosition = size;
            this.mSelectedItemPosition = size;
        } else {
            this.mSelectedItemPosition = this.mCurrentItemPosition;
        }
        this.mScrollOffsetY = 0;
        computeTextSize();
        computeFlingLimitY();
        requestLayout();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setSameWidth(boolean z) {
        this.hasSameWidth = z;
        computeTextSize();
        requestLayout();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean hasSameWidth() {
        return this.hasSameWidth;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setOnWheelChangeListener(OnWheelChangeListener onWheelChangeListener) {
        this.mOnWheelChangeListener = onWheelChangeListener;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public String getMaximumWidthText() {
        return this.mMaxWidthText;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setMaximumWidthText(String str) {
        Objects.requireNonNull(str, "Maximum width text can not be null!");
        this.mMaxWidthText = str;
        computeTextSize();
        requestLayout();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getMaximumWidthTextPosition() {
        return this.mTextMaxWidthPosition;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setMaximumWidthTextPosition(int i) {
        if (!isPosInRang(i)) {
            throw new ArrayIndexOutOfBoundsException("Maximum width text Position must in [0, " + this.mData.size() + "), but current is " + i);
        }
        this.mTextMaxWidthPosition = i;
        computeTextSize();
        requestLayout();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getSelectedItemTextColor() {
        return this.mSelectedItemTextColor;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setSelectedItemTextColor(int i) {
        this.mSelectedItemTextColor = i;
        computeCurrentItemRect();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getItemTextColor() {
        return this.mItemTextColor;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setItemTextColor(int i) {
        this.mItemTextColor = i;
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getItemTextSize() {
        return this.mItemTextSize;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setItemTextSize(int i) {
        this.mItemTextSize = i;
        this.mPaint.setTextSize(i);
        computeTextSize();
        requestLayout();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getItemSpace() {
        return this.mItemSpace;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setItemSpace(int i) {
        this.mItemSpace = i;
        requestLayout();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setIndicator(boolean z) {
        this.hasIndicator = z;
        computeIndicatorRect();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean hasIndicator() {
        return this.hasIndicator;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getIndicatorSize() {
        return this.mIndicatorSize;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setIndicatorSize(int i) {
        this.mIndicatorSize = i;
        computeIndicatorRect();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getIndicatorColor() {
        return this.mIndicatorColor;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setIndicatorColor(int i) {
        this.mIndicatorColor = i;
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setCurtain(boolean z) {
        this.hasCurtain = z;
        computeCurrentItemRect();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean hasCurtain() {
        return this.hasCurtain;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getCurtainColor() {
        return this.mCurtainColor;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setCurtainColor(int i) {
        this.mCurtainColor = i;
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setAtmospheric(boolean z) {
        this.hasAtmospheric = z;
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean hasAtmospheric() {
        return this.hasAtmospheric;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean isCurved() {
        return this.isCurved;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setCurved(boolean z) {
        this.isCurved = z;
        requestLayout();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getItemAlign() {
        return this.mItemAlign;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setItemAlign(int i) {
        this.mItemAlign = i;
        updateItemTextAlign();
        computeDrawnCenter();
        invalidate();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public Typeface getTypeface() {
        Paint paint = this.mPaint;
        if (paint != null) {
            return paint.getTypeface();
        }
        return null;
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setTypeface(Typeface typeface) {
        Paint paint = this.mPaint;
        if (paint != null) {
            paint.setTypeface(typeface);
        }
        computeTextSize();
        requestLayout();
        invalidate();
    }
}
