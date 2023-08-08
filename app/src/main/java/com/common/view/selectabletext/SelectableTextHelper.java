package com.common.view.selectabletext;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class SelectableTextHelper {
    private static final int DEFAULT_SELECTION_LENGTH = 1;
    private static final int DEFAULT_SHOW_DURATION = 100;
    private boolean isHideWhenScroll;
    private Context mContext;
    private int mCursorHandleColor;
    private int mCursorHandleSize;
    private CursorHandle mEndHandle;
    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener;
    ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;
    private OperateWindow mOperateWindow;
    private OnSelectListener mSelectListener;
    private int mSelectedColor;
    private BackgroundColorSpan mSpan;
    private Spannable mSpannable;
    private CursorHandle mStartHandle;
    private TextView mTextView;
    private int mTouchX;
    private int mTouchY;
    private SelectionInfo mSelectionInfo = new SelectionInfo();
    private boolean isHide = true;
    private final Runnable mShowSelectViewRunnable = new Runnable() { // from class: com.common.view.selectabletext.SelectableTextHelper.7
        @Override // java.lang.Runnable
        public void run() {
            if (SelectableTextHelper.this.isHide) {
                return;
            }
            if (SelectableTextHelper.this.mOperateWindow != null) {
                SelectableTextHelper.this.mOperateWindow.show();
            }
            if (SelectableTextHelper.this.mStartHandle != null) {
                SelectableTextHelper selectableTextHelper = SelectableTextHelper.this;
                selectableTextHelper.showCursorHandle(selectableTextHelper.mStartHandle);
            }
            if (SelectableTextHelper.this.mEndHandle != null) {
                SelectableTextHelper selectableTextHelper2 = SelectableTextHelper.this;
                selectableTextHelper2.showCursorHandle(selectableTextHelper2.mEndHandle);
            }
        }
    };

    public SelectableTextHelper(Builder builder) {
        TextView textView = builder.mTextView;
        this.mTextView = textView;
        this.mContext = textView.getContext();
        this.mSelectedColor = builder.mSelectedColor;
        this.mCursorHandleColor = builder.mCursorHandleColor;
        this.mCursorHandleSize = TextLayoutUtil.dp2px(this.mContext, builder.mCursorHandleSizeInDp);
        init();
    }

    private void init() {
        TextView textView = this.mTextView;
        textView.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        this.mTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.common.view.selectabletext.SelectableTextHelper.1
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                SelectableTextHelper selectableTextHelper = SelectableTextHelper.this;
                selectableTextHelper.showSelectView(selectableTextHelper.mTouchX, SelectableTextHelper.this.mTouchY);
                return true;
            }
        });
        this.mTextView.setOnTouchListener(new View.OnTouchListener() { // from class: com.common.view.selectabletext.SelectableTextHelper.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                SelectableTextHelper.this.mTouchX = (int) motionEvent.getX();
                SelectableTextHelper.this.mTouchY = (int) motionEvent.getY();
                return false;
            }
        });
        this.mTextView.setOnClickListener(new View.OnClickListener() { // from class: com.common.view.selectabletext.SelectableTextHelper.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SelectableTextHelper.this.resetSelectionInfo();
                SelectableTextHelper.this.hideSelectView();
            }
        });
        this.mTextView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.common.view.selectabletext.SelectableTextHelper.4
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                SelectableTextHelper.this.destroy();
            }
        });
        this.mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.common.view.selectabletext.SelectableTextHelper.5
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                if (SelectableTextHelper.this.isHideWhenScroll) {
                    SelectableTextHelper.this.isHideWhenScroll = false;
                    SelectableTextHelper.this.postShowSelectView(100);
                    return true;
                }
                return true;
            }
        };
        this.mTextView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        this.mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() { // from class: com.common.view.selectabletext.SelectableTextHelper.6
            @Override // android.view.ViewTreeObserver.OnScrollChangedListener
            public void onScrollChanged() {
                if (SelectableTextHelper.this.isHideWhenScroll || SelectableTextHelper.this.isHide) {
                    return;
                }
                SelectableTextHelper.this.isHideWhenScroll = true;
                if (SelectableTextHelper.this.mOperateWindow != null) {
                    SelectableTextHelper.this.mOperateWindow.dismiss();
                }
                if (SelectableTextHelper.this.mStartHandle != null) {
                    SelectableTextHelper.this.mStartHandle.dismiss();
                }
                if (SelectableTextHelper.this.mEndHandle != null) {
                    SelectableTextHelper.this.mEndHandle.dismiss();
                }
            }
        };
        this.mTextView.getViewTreeObserver().addOnScrollChangedListener(this.mOnScrollChangedListener);
        this.mOperateWindow = new OperateWindow(this.mContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postShowSelectView(int i) {
        this.mTextView.removeCallbacks(this.mShowSelectViewRunnable);
        if (i <= 0) {
            this.mShowSelectViewRunnable.run();
        } else {
            this.mTextView.postDelayed(this.mShowSelectViewRunnable, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideSelectView() {
        this.isHide = true;
        CursorHandle cursorHandle = this.mStartHandle;
        if (cursorHandle != null) {
            cursorHandle.dismiss();
        }
        CursorHandle cursorHandle2 = this.mEndHandle;
        if (cursorHandle2 != null) {
            cursorHandle2.dismiss();
        }
        OperateWindow operateWindow = this.mOperateWindow;
        if (operateWindow != null) {
            operateWindow.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetSelectionInfo() {
        BackgroundColorSpan backgroundColorSpan;
        this.mSelectionInfo.mSelectionContent = null;
        Spannable spannable = this.mSpannable;
        if (spannable == null || (backgroundColorSpan = this.mSpan) == null) {
            return;
        }
        spannable.removeSpan(backgroundColorSpan);
        this.mSpan = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSelectView(int i, int i2) {
        hideSelectView();
        resetSelectionInfo();
        this.isHide = false;
        if (this.mStartHandle == null) {
            this.mStartHandle = new CursorHandle(true);
        }
        if (this.mEndHandle == null) {
            this.mEndHandle = new CursorHandle(false);
        }
        int preciseOffset = TextLayoutUtil.getPreciseOffset(this.mTextView, i, i2);
        int i3 = preciseOffset + 1;
        if (this.mTextView.getText() instanceof Spannable) {
            this.mSpannable = (Spannable) this.mTextView.getText();
        }
        if (this.mSpannable == null || preciseOffset >= this.mTextView.getText().length()) {
            return;
        }
        selectText(preciseOffset, i3);
        showCursorHandle(this.mStartHandle);
        showCursorHandle(this.mEndHandle);
        this.mOperateWindow.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCursorHandle(CursorHandle cursorHandle) {
        Layout layout = this.mTextView.getLayout();
        int i = cursorHandle.isLeft ? this.mSelectionInfo.mStart : this.mSelectionInfo.mEnd;
        cursorHandle.show((int) layout.getPrimaryHorizontal(i), layout.getLineBottom(layout.getLineForOffset(i)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectText(int i, int i2) {
        if (i != -1) {
            this.mSelectionInfo.mStart = i;
        }
        if (i2 != -1) {
            this.mSelectionInfo.mEnd = i2;
        }
        if (this.mSelectionInfo.mStart > this.mSelectionInfo.mEnd) {
            int i3 = this.mSelectionInfo.mStart;
            SelectionInfo selectionInfo = this.mSelectionInfo;
            selectionInfo.mStart = selectionInfo.mEnd;
            this.mSelectionInfo.mEnd = i3;
        }
        if (this.mSpannable != null) {
            if (this.mSpan == null) {
                this.mSpan = new BackgroundColorSpan(this.mSelectedColor);
            }
            SelectionInfo selectionInfo2 = this.mSelectionInfo;
            selectionInfo2.mSelectionContent = this.mSpannable.subSequence(selectionInfo2.mStart, this.mSelectionInfo.mEnd).toString();
            this.mSpannable.setSpan(this.mSpan, this.mSelectionInfo.mStart, this.mSelectionInfo.mEnd, 17);
            OnSelectListener onSelectListener = this.mSelectListener;
            if (onSelectListener != null) {
                onSelectListener.onTextSelected(this.mSelectionInfo.mSelectionContent);
            }
        }
    }

    public void setSelectListener(OnSelectListener onSelectListener) {
        this.mSelectListener = onSelectListener;
    }

    public void destroy() {
        this.mTextView.getViewTreeObserver().removeOnScrollChangedListener(this.mOnScrollChangedListener);
        this.mTextView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
        resetSelectionInfo();
        hideSelectView();
        this.mStartHandle = null;
        this.mEndHandle = null;
        this.mOperateWindow = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OperateWindow {
        private int mHeight;
        private int[] mTempCoors = new int[2];
        private int mWidth;
        private PopupWindow mWindow;

        public OperateWindow(Context context) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.layout_selectable_textview, (ViewGroup) null);
            inflate.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            this.mWidth = inflate.getMeasuredWidth();
            this.mHeight = inflate.getMeasuredHeight();
            PopupWindow popupWindow = new PopupWindow(inflate, -2, -2, false);
            this.mWindow = popupWindow;
            popupWindow.setClippingEnabled(false);
            inflate.findViewById(R.id.tv_copy).setOnClickListener(new View.OnClickListener() { // from class: com.common.view.selectabletext.SelectableTextHelper.OperateWindow.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ((ClipboardManager) SelectableTextHelper.this.mContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(SelectableTextHelper.this.mSelectionInfo.mSelectionContent, SelectableTextHelper.this.mSelectionInfo.mSelectionContent));
                    if (SelectableTextHelper.this.mSelectListener != null) {
                        SelectableTextHelper.this.mSelectListener.onTextSelected(SelectableTextHelper.this.mSelectionInfo.mSelectionContent);
                    }
                    SelectableTextHelper.this.resetSelectionInfo();
                    SelectableTextHelper.this.hideSelectView();
                }
            });
            inflate.findViewById(R.id.tv_select_all).setOnClickListener(new View.OnClickListener() { // from class: com.common.view.selectabletext.SelectableTextHelper.OperateWindow.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    SelectableTextHelper.this.hideSelectView();
                    SelectableTextHelper.this.selectText(0, SelectableTextHelper.this.mTextView.getText().length());
                    SelectableTextHelper.this.isHide = false;
                    SelectableTextHelper.this.showCursorHandle(SelectableTextHelper.this.mStartHandle);
                    SelectableTextHelper.this.showCursorHandle(SelectableTextHelper.this.mEndHandle);
                    SelectableTextHelper.this.mOperateWindow.show();
                }
            });
        }

        public void show() {
            SelectableTextHelper.this.mTextView.getLocationInWindow(this.mTempCoors);
            Layout layout = SelectableTextHelper.this.mTextView.getLayout();
            int primaryHorizontal = ((int) layout.getPrimaryHorizontal(SelectableTextHelper.this.mSelectionInfo.mStart)) + this.mTempCoors[0];
            int lineTop = ((layout.getLineTop(layout.getLineForOffset(SelectableTextHelper.this.mSelectionInfo.mStart)) + this.mTempCoors[1]) - this.mHeight) - 16;
            if (primaryHorizontal <= 0) {
                primaryHorizontal = 16;
            }
            if (lineTop < 0) {
                lineTop = 16;
            }
            if (this.mWidth + primaryHorizontal > TextLayoutUtil.getScreenWidth(SelectableTextHelper.this.mContext)) {
                primaryHorizontal = (TextLayoutUtil.getScreenWidth(SelectableTextHelper.this.mContext) - this.mWidth) - 16;
            }
            if (Build.VERSION.SDK_INT >= 21) {
                this.mWindow.setElevation(8.0f);
            }
            this.mWindow.showAtLocation(SelectableTextHelper.this.mTextView, 0, primaryHorizontal, lineTop);
        }

        public void dismiss() {
            this.mWindow.dismiss();
        }

        public boolean isShowing() {
            return this.mWindow.isShowing();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CursorHandle extends View {
        private boolean isLeft;
        private int mAdjustX;
        private int mAdjustY;
        private int mBeforeDragEnd;
        private int mBeforeDragStart;
        private int mCircleRadius;
        private int mHeight;
        private int mPadding;
        private Paint mPaint;
        private PopupWindow mPopupWindow;
        private int[] mTempCoors;
        private int mWidth;

        public CursorHandle(boolean z) {
            super(SelectableTextHelper.this.mContext);
            int i = SelectableTextHelper.this.mCursorHandleSize / 2;
            this.mCircleRadius = i;
            this.mWidth = i * 2;
            this.mHeight = i * 2;
            this.mPadding = 25;
            this.mTempCoors = new int[2];
            this.isLeft = z;
            Paint paint = new Paint(1);
            this.mPaint = paint;
            paint.setColor(SelectableTextHelper.this.mCursorHandleColor);
            PopupWindow popupWindow = new PopupWindow(this);
            this.mPopupWindow = popupWindow;
            popupWindow.setClippingEnabled(false);
            this.mPopupWindow.setWidth(this.mWidth + (this.mPadding * 2));
            this.mPopupWindow.setHeight(this.mHeight + (this.mPadding / 2));
            invalidate();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int i = this.mCircleRadius;
            canvas.drawCircle(this.mPadding + i, i, i, this.mPaint);
            if (this.isLeft) {
                int i2 = this.mCircleRadius;
                int i3 = this.mPadding;
                canvas.drawRect(i2 + i3, 0.0f, (i2 * 2) + i3, i2, this.mPaint);
                return;
            }
            int i4 = this.mPadding;
            int i5 = this.mCircleRadius;
            canvas.drawRect(i4, 0.0f, i4 + i5, i5, this.mPaint);
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x000d, code lost:
            if (r0 != 3) goto L8;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action != 1) {
                    if (action == 2) {
                        SelectableTextHelper.this.mOperateWindow.dismiss();
                        update((((int) motionEvent.getRawX()) + this.mAdjustX) - this.mWidth, (((int) motionEvent.getRawY()) + this.mAdjustY) - this.mHeight);
                    }
                }
                SelectableTextHelper.this.mOperateWindow.show();
            } else {
                this.mBeforeDragStart = SelectableTextHelper.this.mSelectionInfo.mStart;
                this.mBeforeDragEnd = SelectableTextHelper.this.mSelectionInfo.mEnd;
                this.mAdjustX = (int) motionEvent.getX();
                this.mAdjustY = (int) motionEvent.getY();
            }
            return true;
        }

        private void changeDirection() {
            this.isLeft = !this.isLeft;
            invalidate();
        }

        public void dismiss() {
            this.mPopupWindow.dismiss();
        }

        public void update(int i, int i2) {
            SelectableTextHelper.this.mTextView.getLocationInWindow(this.mTempCoors);
            int i3 = this.isLeft ? SelectableTextHelper.this.mSelectionInfo.mStart : SelectableTextHelper.this.mSelectionInfo.mEnd;
            int hysteresisOffset = TextLayoutUtil.getHysteresisOffset(SelectableTextHelper.this.mTextView, i, i2 - this.mTempCoors[1], i3);
            if (hysteresisOffset != i3) {
                SelectableTextHelper.this.resetSelectionInfo();
                if (this.isLeft) {
                    if (hysteresisOffset > this.mBeforeDragEnd) {
                        CursorHandle cursorHandle = SelectableTextHelper.this.getCursorHandle(false);
                        changeDirection();
                        cursorHandle.changeDirection();
                        int i4 = this.mBeforeDragEnd;
                        this.mBeforeDragStart = i4;
                        SelectableTextHelper.this.selectText(i4, hysteresisOffset);
                        cursorHandle.updateCursorHandle();
                    } else {
                        SelectableTextHelper.this.selectText(hysteresisOffset, -1);
                    }
                    updateCursorHandle();
                    return;
                }
                int i5 = this.mBeforeDragStart;
                if (hysteresisOffset < i5) {
                    CursorHandle cursorHandle2 = SelectableTextHelper.this.getCursorHandle(true);
                    cursorHandle2.changeDirection();
                    changeDirection();
                    int i6 = this.mBeforeDragStart;
                    this.mBeforeDragEnd = i6;
                    SelectableTextHelper.this.selectText(hysteresisOffset, i6);
                    cursorHandle2.updateCursorHandle();
                } else {
                    SelectableTextHelper.this.selectText(i5, hysteresisOffset);
                }
                updateCursorHandle();
            }
        }

        private void updateCursorHandle() {
            SelectableTextHelper.this.mTextView.getLocationInWindow(this.mTempCoors);
            Layout layout = SelectableTextHelper.this.mTextView.getLayout();
            if (this.isLeft) {
                this.mPopupWindow.update((((int) layout.getPrimaryHorizontal(SelectableTextHelper.this.mSelectionInfo.mStart)) - this.mWidth) + getExtraX(), layout.getLineBottom(layout.getLineForOffset(SelectableTextHelper.this.mSelectionInfo.mStart)) + getExtraY(), -1, -1);
            } else {
                this.mPopupWindow.update(((int) layout.getPrimaryHorizontal(SelectableTextHelper.this.mSelectionInfo.mEnd)) + getExtraX(), layout.getLineBottom(layout.getLineForOffset(SelectableTextHelper.this.mSelectionInfo.mEnd)) + getExtraY(), -1, -1);
            }
        }

        public void show(int i, int i2) {
            SelectableTextHelper.this.mTextView.getLocationInWindow(this.mTempCoors);
            this.mPopupWindow.showAtLocation(SelectableTextHelper.this.mTextView, 0, (i - (this.isLeft ? this.mWidth : 0)) + getExtraX(), i2 + getExtraY());
        }

        public int getExtraX() {
            return (this.mTempCoors[0] - this.mPadding) + SelectableTextHelper.this.mTextView.getPaddingLeft();
        }

        public int getExtraY() {
            return this.mTempCoors[1] + SelectableTextHelper.this.mTextView.getPaddingTop();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CursorHandle getCursorHandle(boolean z) {
        if (this.mStartHandle.isLeft == z) {
            return this.mStartHandle;
        }
        return this.mEndHandle;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private TextView mTextView;
        private int mCursorHandleColor = -15500842;
        private int mSelectedColor = -5250572;
        private float mCursorHandleSizeInDp = 24.0f;

        public Builder(TextView textView) {
            this.mTextView = textView;
        }

        public Builder setCursorHandleColor(int i) {
            this.mCursorHandleColor = i;
            return this;
        }

        public Builder setCursorHandleSizeInDp(float f) {
            this.mCursorHandleSizeInDp = f;
            return this;
        }

        public Builder setSelectedColor(int i) {
            this.mSelectedColor = i;
            return this;
        }

        public SelectableTextHelper build() {
            return new SelectableTextHelper(this);
        }
    }
}
