package me.imid.swipebacklayout.lib;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.List;
import me.imid.swipebacklayout.lib.ViewDragHelper;
import me.imid.swipebacklayout.lib.app.SwipeBackListenerActivityAdapter;

/* loaded from: classes.dex */
public class SwipeBackLayout extends FrameLayout {
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;
    public static final int EDGE_ALL = 11;
    public static final int EDGE_BOTTOM = 8;
    private static final int[] EDGE_FLAGS = {1, 2, 8, 11};
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int FULL_ALPHA = 255;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int OVERSCROLL_DISTANCE = 10;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private Activity mActivity;
    private int mContentLeft;
    private int mContentTop;
    private View mContentView;
    private ViewDragHelper mDragHelper;
    private int mEdgeFlag;
    private boolean mEnable;
    private boolean mInLayout;
    private List<SwipeListener> mListeners;
    private int mScrimColor;
    private float mScrimOpacity;
    private float mScrollPercent;
    private float mScrollThreshold;
    private Drawable mShadowBottom;
    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private Rect mTmpRect;
    private int mTrackingEdge;

    /* loaded from: classes.dex */
    public interface SwipeListener {
        void onEdgeTouch(int i);

        void onScrollOverThreshold();

        void onScrollStateChange(int i, float f);
    }

    /* loaded from: classes.dex */
    public interface SwipeListenerEx extends SwipeListener {
        void onContentViewSwipedBack();
    }

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.SwipeBackLayoutStyle);
    }

    public SwipeBackLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet);
        this.mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;
        this.mEnable = true;
        this.mScrimColor = DEFAULT_SCRIM_COLOR;
        this.mTmpRect = new Rect();
        this.mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SwipeBackLayout, i, R.style.SwipeBackLayout);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SwipeBackLayout_edge_size, -1);
        if (dimensionPixelSize > 0) {
            setEdgeSize(dimensionPixelSize);
        }
        setEdgeTrackingEnabled(EDGE_FLAGS[obtainStyledAttributes.getInt(R.styleable.SwipeBackLayout_edge_flag, 0)]);
        int resourceId = obtainStyledAttributes.getResourceId(R.styleable.SwipeBackLayout_shadow_left, R.drawable.shadow_left);
        int resourceId2 = obtainStyledAttributes.getResourceId(R.styleable.SwipeBackLayout_shadow_right, R.drawable.shadow_right);
        int resourceId3 = obtainStyledAttributes.getResourceId(R.styleable.SwipeBackLayout_shadow_bottom, R.drawable.shadow_bottom);
        setShadow(resourceId, 1);
        setShadow(resourceId2, 2);
        setShadow(resourceId3, 8);
        obtainStyledAttributes.recycle();
        float f = getResources().getDisplayMetrics().density * 400.0f;
        this.mDragHelper.setMinVelocity(f);
        this.mDragHelper.setMaxVelocity(f * 2.0f);
    }

    public void setSensitivity(Context context, float f) {
        this.mDragHelper.setSensitivity(context, f);
    }

    public void setContentView(View view) {
        this.mContentView = view;
    }

    public void setEnableGesture(boolean z) {
        this.mEnable = z;
    }

    public void setEdgeTrackingEnabled(int i) {
        this.mEdgeFlag = i;
        this.mDragHelper.setEdgeTrackingEnabled(i);
    }

    public void setScrimColor(int i) {
        this.mScrimColor = i;
        invalidate();
    }

    public void setEdgeSize(int i) {
        this.mDragHelper.setEdgeSize(i);
    }

    @Deprecated
    public void setSwipeListener(SwipeListener swipeListener) {
        addSwipeListener(swipeListener);
    }

    public void addSwipeListener(SwipeListener swipeListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(swipeListener);
    }

    public void removeSwipeListener(SwipeListener swipeListener) {
        List<SwipeListener> list = this.mListeners;
        if (list == null) {
            return;
        }
        list.remove(swipeListener);
    }

    public void setScrollThresHold(float f) {
        if (f >= 1.0f || f <= 0.0f) {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
        this.mScrollThreshold = f;
    }

    public void setShadow(Drawable drawable, int i) {
        if ((i & 1) != 0) {
            this.mShadowLeft = drawable;
        } else if ((i & 2) != 0) {
            this.mShadowRight = drawable;
        } else if ((i & 8) != 0) {
            this.mShadowBottom = drawable;
        }
        invalidate();
    }

    public void setShadow(int i, int i2) {
        setShadow(getResources().getDrawable(i), i2);
    }

    public void scrollToFinishActivity() {
        int intrinsicHeight;
        int intrinsicWidth;
        int width = this.mContentView.getWidth();
        int height = this.mContentView.getHeight();
        int i = this.mEdgeFlag;
        int i2 = 0;
        if ((i & 1) != 0) {
            intrinsicWidth = width + this.mShadowLeft.getIntrinsicWidth() + 10;
            this.mTrackingEdge = 1;
        } else if ((i & 2) != 0) {
            intrinsicWidth = ((-width) - this.mShadowRight.getIntrinsicWidth()) - 10;
            this.mTrackingEdge = 2;
        } else {
            if ((i & 8) != 0) {
                intrinsicHeight = ((-height) - this.mShadowBottom.getIntrinsicHeight()) - 10;
                this.mTrackingEdge = 8;
                this.mDragHelper.smoothSlideViewTo(this.mContentView, i2, intrinsicHeight);
                invalidate();
            }
            intrinsicHeight = 0;
            this.mDragHelper.smoothSlideViewTo(this.mContentView, i2, intrinsicHeight);
            invalidate();
        }
        i2 = intrinsicWidth;
        intrinsicHeight = 0;
        this.mDragHelper.smoothSlideViewTo(this.mContentView, i2, intrinsicHeight);
        invalidate();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mEnable) {
            try {
                return this.mDragHelper.shouldInterceptTouchEvent(motionEvent);
            } catch (ArrayIndexOutOfBoundsException unused) {
                return false;
            }
        }
        return false;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mEnable) {
            this.mDragHelper.processTouchEvent(motionEvent);
            return true;
        }
        return false;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mInLayout = true;
        View view = this.mContentView;
        if (view != null) {
            int i5 = this.mContentLeft;
            view.layout(i5, this.mContentTop, view.getMeasuredWidth() + i5, this.mContentTop + this.mContentView.getMeasuredHeight());
        }
        this.mInLayout = false;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.mInLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        boolean z = view == this.mContentView;
        boolean drawChild = super.drawChild(canvas, view, j);
        if (this.mScrimOpacity > 0.0f && z && this.mDragHelper.getViewDragState() != 0) {
            drawShadow(canvas, view);
            drawScrim(canvas, view);
        }
        return drawChild;
    }

    private void drawScrim(Canvas canvas, View view) {
        int i = this.mScrimColor;
        int i2 = (i & ViewCompat.MEASURED_SIZE_MASK) | (((int) ((((-16777216) & i) >>> 24) * this.mScrimOpacity)) << 24);
        int i3 = this.mTrackingEdge;
        if ((i3 & 1) != 0) {
            canvas.clipRect(0, 0, view.getLeft(), getHeight());
        } else if ((i3 & 2) != 0) {
            canvas.clipRect(view.getRight(), 0, getRight(), getHeight());
        } else if ((i3 & 8) != 0) {
            canvas.clipRect(view.getLeft(), view.getBottom(), getRight(), getHeight());
        }
        canvas.drawColor(i2);
    }

    private void drawShadow(Canvas canvas, View view) {
        Rect rect = this.mTmpRect;
        view.getHitRect(rect);
        if ((this.mEdgeFlag & 1) != 0) {
            this.mShadowLeft.setBounds(rect.left - this.mShadowLeft.getIntrinsicWidth(), rect.top, rect.left, rect.bottom);
            this.mShadowLeft.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowLeft.draw(canvas);
        }
        if ((this.mEdgeFlag & 2) != 0) {
            this.mShadowRight.setBounds(rect.right, rect.top, rect.right + this.mShadowRight.getIntrinsicWidth(), rect.bottom);
            this.mShadowRight.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowRight.draw(canvas);
        }
        if ((this.mEdgeFlag & 8) != 0) {
            this.mShadowBottom.setBounds(rect.left, rect.bottom, rect.right, rect.bottom + this.mShadowBottom.getIntrinsicHeight());
            this.mShadowBottom.setAlpha((int) (this.mScrimOpacity * 255.0f));
            this.mShadowBottom.draw(canvas);
        }
    }

    public void attachToActivity(Activity activity) {
        this.mActivity = activity;
        TypedArray obtainStyledAttributes = activity.getTheme().obtainStyledAttributes(new int[]{16842836});
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        ViewGroup viewGroup = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup viewGroup2 = (ViewGroup) viewGroup.getChildAt(0);
        viewGroup2.setBackgroundResource(resourceId);
        viewGroup.removeView(viewGroup2);
        addView(viewGroup2);
        setContentView(viewGroup2);
        addSwipeListener(new SwipeBackListenerActivityAdapter(activity));
        viewGroup.addView(this);
    }

    @Override // android.view.View
    public void computeScroll() {
        this.mScrimOpacity = 1.0f - this.mScrollPercent;
        if (this.mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /* loaded from: classes.dex */
    private class ViewDragCallback extends ViewDragHelper.Callback {
        private boolean mIsScrollOverValid;

        private ViewDragCallback() {
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public boolean tryCaptureView(View view, int i) {
            boolean checkTouchSlop;
            boolean isEdgeTouched = SwipeBackLayout.this.mDragHelper.isEdgeTouched(SwipeBackLayout.this.mEdgeFlag, i);
            boolean z = true;
            if (isEdgeTouched) {
                if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(1, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 1;
                } else if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(2, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 2;
                } else if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(8, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 8;
                }
                if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners.isEmpty()) {
                    for (SwipeListener swipeListener : SwipeBackLayout.this.mListeners) {
                        swipeListener.onEdgeTouch(SwipeBackLayout.this.mTrackingEdge);
                    }
                }
                this.mIsScrollOverValid = true;
            }
            if (SwipeBackLayout.this.mEdgeFlag == 1 || SwipeBackLayout.this.mEdgeFlag == 2) {
                checkTouchSlop = SwipeBackLayout.this.mDragHelper.checkTouchSlop(2, i);
            } else if (SwipeBackLayout.this.mEdgeFlag != 8) {
                if (SwipeBackLayout.this.mEdgeFlag != 11) {
                    z = false;
                }
                return isEdgeTouched & z;
            } else {
                checkTouchSlop = SwipeBackLayout.this.mDragHelper.checkTouchSlop(1, i);
            }
            z = true ^ checkTouchSlop;
            return isEdgeTouched & z;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int getViewHorizontalDragRange(View view) {
            return SwipeBackLayout.this.mEdgeFlag & 3;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int getViewVerticalDragRange(View view) {
            return SwipeBackLayout.this.mEdgeFlag & 8;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
            super.onViewPositionChanged(view, i, i2, i3, i4);
            if ((SwipeBackLayout.this.mTrackingEdge & 1) == 0) {
                if ((SwipeBackLayout.this.mTrackingEdge & 2) == 0) {
                    if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                        SwipeBackLayout swipeBackLayout = SwipeBackLayout.this;
                        swipeBackLayout.mScrollPercent = Math.abs(i2 / (swipeBackLayout.mContentView.getHeight() + SwipeBackLayout.this.mShadowBottom.getIntrinsicHeight()));
                    }
                } else {
                    SwipeBackLayout swipeBackLayout2 = SwipeBackLayout.this;
                    swipeBackLayout2.mScrollPercent = Math.abs(i / (swipeBackLayout2.mContentView.getWidth() + SwipeBackLayout.this.mShadowRight.getIntrinsicWidth()));
                }
            } else {
                SwipeBackLayout swipeBackLayout3 = SwipeBackLayout.this;
                swipeBackLayout3.mScrollPercent = Math.abs(i / (swipeBackLayout3.mContentView.getWidth() + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth()));
            }
            SwipeBackLayout.this.mContentLeft = i;
            SwipeBackLayout.this.mContentTop = i2;
            SwipeBackLayout.this.invalidate();
            if (SwipeBackLayout.this.mScrollPercent < SwipeBackLayout.this.mScrollThreshold && !this.mIsScrollOverValid) {
                this.mIsScrollOverValid = true;
            }
            if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners.isEmpty()) {
                for (SwipeListener swipeListener : SwipeBackLayout.this.mListeners) {
                    swipeListener.onScrollStateChange(SwipeBackLayout.this.mDragHelper.getViewDragState(), SwipeBackLayout.this.mScrollPercent);
                }
            }
            if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners.isEmpty() && SwipeBackLayout.this.mDragHelper.getViewDragState() == 1 && SwipeBackLayout.this.mScrollPercent >= SwipeBackLayout.this.mScrollThreshold && this.mIsScrollOverValid) {
                this.mIsScrollOverValid = false;
                for (SwipeListener swipeListener2 : SwipeBackLayout.this.mListeners) {
                    swipeListener2.onScrollOverThreshold();
                }
            }
            if (SwipeBackLayout.this.mScrollPercent < 1.0f || SwipeBackLayout.this.mListeners == null || SwipeBackLayout.this.mListeners.isEmpty()) {
                return;
            }
            for (SwipeListener swipeListener3 : SwipeBackLayout.this.mListeners) {
                if (swipeListener3 instanceof SwipeListenerEx) {
                    ((SwipeListenerEx) swipeListener3).onContentViewSwipedBack();
                }
            }
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public void onViewReleased(View view, float f, float f2) {
            int i;
            int width = view.getWidth();
            int height = view.getHeight();
            int i2 = 0;
            if ((SwipeBackLayout.this.mTrackingEdge & 1) != 0) {
                i2 = (f > 0.0f || (f == 0.0f && SwipeBackLayout.this.mScrollPercent > SwipeBackLayout.this.mScrollThreshold)) ? width + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth() + 10 : 0;
            } else if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                i2 = (f < 0.0f || (f == 0.0f && SwipeBackLayout.this.mScrollPercent > SwipeBackLayout.this.mScrollThreshold)) ? -(width + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth() + 10) : 0;
            } else if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0 && (f2 < 0.0f || (f2 == 0.0f && SwipeBackLayout.this.mScrollPercent > SwipeBackLayout.this.mScrollThreshold))) {
                i = -(height + SwipeBackLayout.this.mShadowBottom.getIntrinsicHeight() + 10);
                SwipeBackLayout.this.mDragHelper.settleCapturedViewAt(i2, i);
                SwipeBackLayout.this.invalidate();
            }
            i = 0;
            SwipeBackLayout.this.mDragHelper.settleCapturedViewAt(i2, i);
            SwipeBackLayout.this.invalidate();
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int clampViewPositionHorizontal(View view, int i, int i2) {
            if ((SwipeBackLayout.this.mTrackingEdge & 1) == 0) {
                if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                    return Math.min(0, Math.max(i, -view.getWidth()));
                }
                return 0;
            }
            return Math.min(view.getWidth(), Math.max(i, 0));
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public int clampViewPositionVertical(View view, int i, int i2) {
            if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                return Math.min(0, Math.max(i, -view.getHeight()));
            }
            return 0;
        }

        @Override // me.imid.swipebacklayout.lib.ViewDragHelper.Callback
        public void onViewDragStateChanged(int i) {
            super.onViewDragStateChanged(i);
            if (SwipeBackLayout.this.mListeners == null || SwipeBackLayout.this.mListeners.isEmpty()) {
                return;
            }
            for (SwipeListener swipeListener : SwipeBackLayout.this.mListeners) {
                swipeListener.onScrollStateChange(i, SwipeBackLayout.this.mScrollPercent);
            }
        }
    }
}
