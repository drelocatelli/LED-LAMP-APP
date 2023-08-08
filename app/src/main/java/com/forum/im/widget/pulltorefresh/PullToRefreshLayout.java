package com.forum.im.widget.pulltorefresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.forum.im.utils.ScreenUtil;
import me.imid.swipebacklayout.lib.ViewDragHelper;

/* loaded from: classes.dex */
public class PullToRefreshLayout extends LinearLayout {
    private static int viewHeight;
    private ViewDragHelper VDH;
    private boolean isPull;
    private View myList;
    private pulltorefreshNotifier pullNotifier;
    private TextView pullText;

    /* loaded from: classes.dex */
    public interface pulltorefreshNotifier {
        void onPull();
    }

    public PullToRefreshLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isPull = true;
        this.VDH = ViewDragHelper.create(this, 10.0f, new DragHelperCallback());
    }

    public void setSlideView(View view) {
        init(view);
    }

    private void init(View view) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        this.myList = view;
        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.myList.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, ScreenUtil.dip2px(getContext(), 100.0f));
        TextView textView = new TextView(getContext());
        this.pullText = textView;
        textView.setText("下拉加载更多");
        this.pullText.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.pullText.setGravity(17);
        this.pullText.setLayoutParams(layoutParams2);
        setOrientation(1);
        addView(this.myList);
    }

    public View returnMylist() {
        return this.myList;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        measureChildren(i, i2);
        setMeasuredDimension(resolveSizeAndState(View.MeasureSpec.getSize(i), i, 0), resolveSizeAndState(View.MeasureSpec.getSize(i2), i2, 0));
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.pullText.getTop() == 0) {
            int measuredHeight = this.pullText.getMeasuredHeight();
            viewHeight = measuredHeight;
            this.pullText.layout(i, 0, i3, measuredHeight);
            this.myList.layout(i, 0, i3, i4 - ScreenUtil.dip2px(getContext(), 48.0f));
            this.pullText.offsetTopAndBottom(-viewHeight);
            return;
        }
        TextView textView = this.pullText;
        textView.layout(i, textView.getTop(), i3, this.pullText.getBottom());
        View view = this.myList;
        view.layout(i, view.getTop(), i3, this.myList.getBottom());
    }

    public static int resolveSizeAndState(int i, int i2, int i3) {
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        if (mode != Integer.MIN_VALUE) {
            if (mode == 1073741824) {
                i = size;
            }
        } else if (size < i) {
            i = 16777216 | size;
        }
        return i | ((-16777216) & i3);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.VDH.shouldInterceptTouchEvent(motionEvent) && this.isPull;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.VDH.processTouchEvent(motionEvent);
        return true;
    }

    /* loaded from: classes.dex */
    private class DragHelperCallback extends ViewDragHelper.Callback {
        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int getViewVerticalDragRange(View view) {
            return 1;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public boolean tryCaptureView(View view, int i) {
            return true;
        }

        private DragHelperCallback() {
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            PullToRefreshLayout.this.onViewPosChanged(view == PullToRefreshLayout.this.myList ? 2 : 1, i2);
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public void onViewReleased(View view, float f, float f2) {
            PullToRefreshLayout.this.refreshOrNot(view, f2);
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int clampViewPositionVertical(View view, int i, int i2) {
            if (view == PullToRefreshLayout.this.pullText) {
                if (i > 0) {
                    i = 0;
                }
            } else if (view == PullToRefreshLayout.this.myList) {
                int i3 = i >= 0 ? i : 0;
                if (i >= PullToRefreshLayout.viewHeight) {
                    PullToRefreshLayout.this.pullText.setText("松开加载");
                } else {
                    PullToRefreshLayout.this.pullText.setText("下拉加载更多");
                }
                i = i3;
            }
            return view.getTop() + ((i - view.getTop()) / 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onViewPosChanged(int i, int i2) {
        if (i == 1) {
            this.myList.offsetTopAndBottom((viewHeight + this.pullText.getTop()) - this.myList.getTop());
        } else if (i == 2) {
            this.pullText.offsetTopAndBottom((this.myList.getTop() - viewHeight) - this.pullText.getTop());
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshOrNot(View view, float f) {
        int i;
        if (view == this.pullText) {
            if (f >= -50.0f) {
                i = viewHeight;
            }
            i = 0;
        } else {
            if (f > viewHeight - 5 || view.getTop() >= viewHeight) {
                i = viewHeight;
                pulltorefreshNotifier pulltorefreshnotifier = this.pullNotifier;
                if (pulltorefreshnotifier != null) {
                    pulltorefreshnotifier.onPull();
                }
                this.pullText.setText("正在加载");
            }
            i = 0;
        }
        if (this.VDH.smoothSlideViewTo(this.myList, 0, i)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void refreshComplete() {
        if (this.VDH.smoothSlideViewTo(this.myList, 0, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.VDH.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setpulltorefreshNotifier(pulltorefreshNotifier pulltorefreshnotifier) {
        this.pullNotifier = pulltorefreshnotifier;
    }

    public void setPullGone() {
        this.isPull = false;
    }
}
