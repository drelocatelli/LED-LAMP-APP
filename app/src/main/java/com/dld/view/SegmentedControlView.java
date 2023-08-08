package com.dld.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SegmentedControlView extends View implements ISegmentedControl {
    private static final int ANIMATION_DURATION = 300;
    public static final int Circle = 1;
    private static final int DEFAULT_ITEM_COLOR = -1;
    private static final int DEFAULT_RADIUS = 10;
    private static final int DEFAULT_TEXT_COLOR = -1;
    private static final float MIN_MOVE_X = 5.0f;
    private static final int MOVE_ITEM_MIN_VELOCITY = 1500;
    public static final int Round = 0;
    private static final float VELOCITY_CHANGE_POSITION_THRESHOLD = 0.25f;
    private static final int VELOCITY_UNITS = 1000;
    private int backgroundColor;
    private int cornersMode;
    private int cornersRadius;
    private int itemHorizontalMargin;
    private int itemVerticalMargin;
    private int mEnd;
    private int mHeight;
    private int mItemWidth;
    private int mMaximumFlingVelocity;
    private Paint mPaint;
    private RectF mRectF;
    private Scroller mScroller;
    private List<SegmentedControlItem> mSegmentedControlItems;
    private int mStart;
    private Paint mTextPaint;
    private VelocityTracker mVelocityTracker;
    private int onClickDownPosition;
    private OnSegItemClickListener onSegItemClickListener;
    private boolean scrollSelectEnabled;
    private int selectedItem;
    private int selectedItemBackgroundColor;
    private int selectedItemTextColor;
    private int textColor;
    private int textSize;
    private float x;
    private static final int DEFAULT_OUTER_COLOR = Color.parseColor("#00000000");
    private static final int DEFAULT_SELECTED_TEXT_COLOR = Color.parseColor("#00A5E0");

    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes.dex */
    public @interface Mode {
    }

    /* loaded from: classes.dex */
    public interface OnSegItemClickListener {
        void onItemClick(SegmentedControlItem segmentedControlItem, int i);
    }

    public SegmentedControlView(Context context) {
        this(context, null);
    }

    public SegmentedControlView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SegmentedControlView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.cornersMode = 0;
        this.scrollSelectEnabled = true;
        this.onClickDownPosition = -1;
        this.x = 0.0f;
        this.mSegmentedControlItems = new ArrayList();
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (isInEditMode()) {
            return;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SegmentedControlView);
        this.cornersRadius = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SegmentedControlView_segCornersRadius, 10);
        this.backgroundColor = obtainStyledAttributes.getColor(R.styleable.SegmentedControlView_segBackgroundColor, DEFAULT_OUTER_COLOR);
        this.selectedItemBackgroundColor = obtainStyledAttributes.getColor(R.styleable.SegmentedControlView_segSelectedItemBackgroundColor, -1);
        this.textColor = obtainStyledAttributes.getColor(R.styleable.SegmentedControlView_segTextColor, -1);
        this.selectedItemTextColor = obtainStyledAttributes.getColor(R.styleable.SegmentedControlView_segSelectedItemTextColor, DEFAULT_SELECTED_TEXT_COLOR);
        this.itemHorizontalMargin = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SegmentedControlView_segItemHorizontalMargin, 0);
        this.itemVerticalMargin = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SegmentedControlView_segItemVerticalMargin, 0);
        this.selectedItem = obtainStyledAttributes.getInteger(R.styleable.SegmentedControlView_segSelectedItem, 0);
        this.textSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SegmentedControlView_segTextSize, (int) getResources().getDimension(R.dimen.seg_textSize));
        this.cornersMode = obtainStyledAttributes.getInt(R.styleable.SegmentedControlView_segCornersMode, 0);
        this.scrollSelectEnabled = obtainStyledAttributes.getBoolean(R.styleable.SegmentedControlView_segScrollSelectEnabled, true);
        obtainStyledAttributes.recycle();
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
        this.mScroller = new Scroller(context, null);
        this.mMaximumFlingVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        this.mRectF = new RectF();
        Paint paint = new Paint(5);
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setColor(this.backgroundColor);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        Paint paint2 = new Paint(5);
        this.mTextPaint = paint2;
        paint2.setAntiAlias(true);
        this.mTextPaint.setColor(this.textColor);
        this.mTextPaint.setTextSize(this.textSize);
    }

    public void setCornersMode(@Mode int i) {
        this.cornersMode = i;
        invalidate();
    }

    public void setTextColor(int i) {
        this.textColor = i;
        invalidate();
    }

    public void setSelectedItemTextColor(int i) {
        this.selectedItemTextColor = i;
        invalidate();
    }

    public void setSelectedItem(int i) {
        if (i < 0 || i >= getCount()) {
            throw new IllegalArgumentException("position error");
        }
        this.selectedItem = i;
        invalidate();
    }

    public int getSelectedItem() {
        return this.selectedItem;
    }

    public int getCornersMode() {
        return this.cornersMode;
    }

    public boolean isScrollSelectEnabled() {
        return this.scrollSelectEnabled;
    }

    public void setOnSegItemClickListener(OnSegItemClickListener onSegItemClickListener) {
        this.onSegItemClickListener = onSegItemClickListener;
    }

    public void addItems(List<SegmentedControlItem> list) {
        if (list == null) {
            throw new IllegalArgumentException("list is null");
        }
        this.mSegmentedControlItems.addAll(list);
        requestLayout();
        invalidate();
    }

    public void addItem(SegmentedControlItem segmentedControlItem) {
        if (segmentedControlItem == null) {
            throw new IllegalArgumentException("item is null");
        }
        this.mSegmentedControlItems.add(segmentedControlItem);
        requestLayout();
        invalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isItemZero()) {
            return;
        }
        drawBackgroundRect(canvas);
        drawUnselectedItemsText(canvas);
        drawSelectedItem(canvas);
        drawSelectedItemsText(canvas);
    }

    private void drawSelectedItem(Canvas canvas) {
        int i;
        float f = this.cornersMode == 0 ? this.cornersRadius : (this.mHeight >> 1) - this.itemVerticalMargin;
        this.mPaint.setColor(this.selectedItemBackgroundColor);
        this.mRectF.set(this.mStart, this.itemVerticalMargin, i + this.mItemWidth, getHeight() - this.itemVerticalMargin);
        canvas.drawRoundRect(this.mRectF, f, f, this.mPaint);
    }

    private void drawBackgroundRect(Canvas canvas) {
        float f = this.cornersMode == 0 ? this.cornersRadius : this.mHeight >> 1;
        this.mPaint.setXfermode(null);
        this.mPaint.setColor(this.backgroundColor);
        this.mRectF.set(0.0f, 0.0f, getWidth(), getHeight());
        canvas.drawRoundRect(this.mRectF, f, f, this.mPaint);
    }

    private void drawUnselectedItemsText(Canvas canvas) {
        this.mTextPaint.setColor(this.textColor);
        this.mTextPaint.setXfermode(null);
        for (int i = 0; i < getCount(); i++) {
            int i2 = this.itemHorizontalMargin;
            int i3 = this.mItemWidth;
            canvas.drawText(getName(i), ((i2 + (i * i3)) + (i3 >> 1)) - (this.mTextPaint.measureText(getName(i)) / 2.0f), (getHeight() >> 1) - ((this.mTextPaint.ascent() + this.mTextPaint.descent()) / 2.0f), this.mTextPaint);
        }
    }

    private void drawSelectedItemsText(Canvas canvas) {
        int i = this.mStart;
        canvas.saveLayer(i, 0.0f, i + this.mItemWidth, getHeight(), null, 31);
        this.mTextPaint.setColor(this.selectedItemTextColor);
        this.mTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        int i2 = this.mStart / this.mItemWidth;
        int i3 = i2 + 2;
        if (i3 >= getCount()) {
            i3 = getCount();
        }
        while (i2 < i3) {
            int i4 = this.itemHorizontalMargin;
            int i5 = this.mItemWidth;
            canvas.drawText(getName(i2), ((i4 + (i2 * i5)) + (i5 >> 1)) - (this.mTextPaint.measureText(getName(i2)) / 2.0f), (getHeight() >> 1) - ((this.mTextPaint.ascent() + this.mTextPaint.descent()) / 2.0f), this.mTextPaint);
            i2++;
        }
        canvas.restore();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int round;
        int max;
        if (isEnabled() && isInTouchMode() && getCount() != 0) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.x = motionEvent.getX();
                this.onClickDownPosition = -1;
                float y = motionEvent.getY();
                if (isItemInside(this.x, y)) {
                    return this.scrollSelectEnabled;
                }
                if (isItemOutside(this.x, y)) {
                    if (!this.mScroller.isFinished()) {
                        this.mScroller.abortAnimation();
                    }
                    float f = this.x;
                    this.onClickDownPosition = (int) ((f - this.itemHorizontalMargin) / this.mItemWidth);
                    startScroll(positionStart(f));
                    return true;
                }
                return false;
            } else if (actionMasked == 2) {
                if (this.mScroller.isFinished() && this.scrollSelectEnabled) {
                    float x = motionEvent.getX() - this.x;
                    if (Math.abs(x) > MIN_MOVE_X) {
                        int i = (int) (this.mStart + x);
                        this.mStart = i;
                        this.mStart = Math.min(Math.max(i, this.itemHorizontalMargin), this.mEnd);
                        postInvalidate();
                        this.x = motionEvent.getX();
                    }
                }
                return true;
            } else if (actionMasked == 1) {
                int i2 = this.mStart;
                int i3 = this.itemHorizontalMargin;
                int i4 = this.mItemWidth;
                float f2 = (i2 - i3) % i4;
                float f3 = ((i2 - i3) * 1.0f) / i4;
                if (this.mScroller.isFinished() || (max = this.onClickDownPosition) == -1) {
                    if (f2 == 0.0f) {
                        max = (int) f3;
                    } else {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
                        int xVelocity = (int) velocityTracker.getXVelocity();
                        if (isXVelocityCanMoveNextItem(xVelocity, f2 / this.mItemWidth)) {
                            round = (int) f3;
                            if (xVelocity > 0) {
                                round++;
                            }
                        } else {
                            round = Math.round(f3);
                        }
                        max = Math.max(Math.min(round, getCount() - 1), 0);
                        startScroll(getXByPosition(max));
                    }
                }
                onStateChange(max);
                this.mVelocityTracker = null;
                this.onClickDownPosition = -1;
                return true;
            } else {
                return super.onTouchEvent(motionEvent);
            }
        }
        return false;
    }

    private void startScroll(int i) {
        Scroller scroller = this.mScroller;
        int i2 = this.mStart;
        scroller.startScroll(i2, 0, i - i2, 0, 300);
        postInvalidate();
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller.computeScrollOffset()) {
            this.mStart = this.mScroller.getCurrX();
            postInvalidate();
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (isItemZero() || getMeasuredWidth() == 0) {
            return;
        }
        this.mHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        int count = (measuredWidth - (this.itemHorizontalMargin * 2)) / getCount();
        this.mItemWidth = count;
        int i3 = this.itemHorizontalMargin;
        this.mStart = (this.selectedItem * count) + i3;
        this.mEnd = (measuredWidth - i3) - count;
    }

    private void onStateChange(int i) {
        if (this.selectedItem != i) {
            this.selectedItem = i;
            onItemClick(getItem(i), i);
        }
    }

    private void onItemClick(SegmentedControlItem segmentedControlItem, int i) {
        OnSegItemClickListener onSegItemClickListener = this.onSegItemClickListener;
        if (onSegItemClickListener != null) {
            onSegItemClickListener.onItemClick(segmentedControlItem, i);
        }
    }

    private int getXByPosition(int i) {
        return (i * this.mItemWidth) + this.itemHorizontalMargin;
    }

    private int positionStart(float f) {
        int i = this.itemHorizontalMargin;
        float f2 = f - i;
        int i2 = this.mItemWidth;
        return i + (((int) (f2 / i2)) * i2);
    }

    private boolean isItemInside(float f, float f2) {
        int i = this.mStart;
        if (f >= i && f <= i + this.mItemWidth) {
            int i2 = this.itemVerticalMargin;
            if (f2 > i2 && f2 < this.mHeight - i2) {
                return true;
            }
        }
        return false;
    }

    private boolean isItemOutside(float f, float f2) {
        if (!isItemInside(f, f2)) {
            int i = this.itemVerticalMargin;
            if (f2 > i && f2 < this.mHeight - i && f < this.mEnd + this.mItemWidth) {
                return true;
            }
        }
        return false;
    }

    private boolean isXVelocityCanMoveNextItem(int i, float f) {
        return Math.abs(i) > MOVE_ITEM_MIN_VELOCITY && ((i > 0 && f >= 0.25f) || (i < 0 && f < 0.75f));
    }

    private boolean isItemZero() {
        return getCount() == 0;
    }

    @Override // com.dld.view.ISegmentedControl
    public int getCount() {
        return this.mSegmentedControlItems.size();
    }

    @Override // com.dld.view.ISegmentedControl
    public SegmentedControlItem getItem(int i) {
        return this.mSegmentedControlItems.get(i);
    }

    @Override // com.dld.view.ISegmentedControl
    public String getName(int i) {
        return getItem(i).getName();
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SelectedItemState selectedItemState = new SelectedItemState(super.onSaveInstanceState());
        selectedItemState.setSelectedItem(this.selectedItem);
        return selectedItemState;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SelectedItemState) {
            SelectedItemState selectedItemState = (SelectedItemState) parcelable;
            super.onRestoreInstanceState(selectedItemState.getSuperState());
            this.selectedItem = selectedItemState.getSelectedItem();
            invalidate();
        }
    }
}
