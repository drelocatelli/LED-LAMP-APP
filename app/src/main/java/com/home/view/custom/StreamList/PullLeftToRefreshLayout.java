package com.home.view.custom.StreamList;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class PullLeftToRefreshLayout extends FrameLayout {
    private static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    private static final long BACK_ANIM_DUR = 500;
    private static final long BEZIER_BACK_DUR = 350;
    private static float MORE_VIEW_MOVE_DIMEN = 0.0f;
    private static String RELEASE_SCAN_MORE = null;
    private static final int ROTATION_ANGLE = 180;
    private static final int ROTATION_ANIM_DUR = 200;
    private static String SCAN_MORE;
    private ImageView arrowIv;
    private AnimView footerView;
    private int footerViewBgColor;
    private DecelerateInterpolator interpolator;
    private boolean isRefresh;
    private RotateAnimation mArrowRotateAnim;
    private RotateAnimation mArrowRotateBackAnim;
    private ValueAnimator mBackAnimator;
    private View mChildView;
    private float mFooterWidth;
    private float mPullWidth;
    private float mTouchCurX;
    private float mTouchStartX;
    private TextView moreText;
    private View moreView;
    private int moreViewMarginRight;
    OnRefreshListener onRefreshListener;
    OnScrollListener onScrollListener;
    private boolean scrollState;

    /* loaded from: classes.dex */
    public interface OnRefreshListener {
        void onRefresh();
    }

    public PullLeftToRefreshLayout(Context context) {
        this(context, null, 0);
    }

    public PullLeftToRefreshLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PullLeftToRefreshLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isRefresh = false;
        this.scrollState = false;
        this.interpolator = new DecelerateInterpolator(10.0f);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.mPullWidth = TypedValue.applyDimension(1, 100.0f, context.getResources().getDisplayMetrics());
        MORE_VIEW_MOVE_DIMEN = TypedValue.applyDimension(1, 40.0f, context.getResources().getDisplayMetrics());
        this.mFooterWidth = TypedValue.applyDimension(1, 20.0f, context.getResources().getDisplayMetrics());
        this.moreViewMarginRight = -getResources().getDimensionPixelSize(R.dimen.dp_26);
        SCAN_MORE = "最多5张图";
        RELEASE_SCAN_MORE = "最多5张图";
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PullLeftToRefreshLayout);
        this.footerViewBgColor = obtainStyledAttributes.getColor(0, Color.rgb(255, 255, 255));
        obtainStyledAttributes.recycle();
        post(new Runnable() { // from class: com.home.view.custom.StreamList.PullLeftToRefreshLayout.1
            @Override // java.lang.Runnable
            public void run() {
                PullLeftToRefreshLayout pullLeftToRefreshLayout = PullLeftToRefreshLayout.this;
                pullLeftToRefreshLayout.mChildView = pullLeftToRefreshLayout.getChildAt(0);
                PullLeftToRefreshLayout.this.addFooterView();
                PullLeftToRefreshLayout.this.addMoreView();
                PullLeftToRefreshLayout.this.initBackAnim();
                PullLeftToRefreshLayout.this.initRotateAnim();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addFooterView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0, -1);
        layoutParams.gravity = 5;
        AnimView animView = new AnimView(getContext());
        this.footerView = animView;
        animView.setLayoutParams(layoutParams);
        this.footerView.setBgColor(this.footerViewBgColor);
        this.footerView.setBezierBackDur(BEZIER_BACK_DUR);
        addViewInternal(this.footerView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addMoreView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 21;
        layoutParams.setMargins(0, 0, this.moreViewMarginRight, 0);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dmx02_item_load_more, (ViewGroup) this, false);
        this.moreView = inflate;
        inflate.setLayoutParams(layoutParams);
        this.moreText = (TextView) this.moreView.findViewById(R.id.tvMoreText);
        this.arrowIv = (ImageView) this.moreView.findViewById(R.id.ivRefreshArrow);
        addViewInternal(this.moreView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initRotateAnim() {
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180.0f, 1, 0.5f, 1, 0.5f);
        this.mArrowRotateAnim = rotateAnimation;
        Interpolator interpolator = ANIMATION_INTERPOLATOR;
        rotateAnimation.setInterpolator(interpolator);
        this.mArrowRotateAnim.setDuration(200L);
        this.mArrowRotateAnim.setFillAfter(true);
        RotateAnimation rotateAnimation2 = new RotateAnimation(180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mArrowRotateBackAnim = rotateAnimation2;
        rotateAnimation2.setInterpolator(interpolator);
        this.mArrowRotateBackAnim.setDuration(200L);
        this.mArrowRotateBackAnim.setFillAfter(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initBackAnim() {
        if (this.mChildView == null) {
            return;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mPullWidth, 0.0f);
        this.mBackAnimator = ofFloat;
        ofFloat.addListener(new AnimListener());
        this.mBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.home.view.custom.StreamList.PullLeftToRefreshLayout.2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                if (floatValue <= PullLeftToRefreshLayout.this.mFooterWidth) {
                    floatValue *= PullLeftToRefreshLayout.this.interpolator.getInterpolation(floatValue / PullLeftToRefreshLayout.this.mFooterWidth);
                    PullLeftToRefreshLayout.this.footerView.getLayoutParams().width = (int) floatValue;
                    PullLeftToRefreshLayout.this.footerView.requestLayout();
                }
                if (PullLeftToRefreshLayout.this.mChildView != null) {
                    PullLeftToRefreshLayout.this.mChildView.setTranslationX(-floatValue);
                }
                PullLeftToRefreshLayout.this.moveMoreView(floatValue, true);
            }
        });
        this.mBackAnimator.setDuration(500L);
    }

    private void addViewInternal(View view) {
        super.addView(view);
    }

    @Override // android.view.ViewGroup
    public void addView(View view) {
        if (getChildCount() >= 1) {
            throw new RuntimeException("you can only attach one child");
        }
        this.mChildView = view;
        super.addView(view);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.isRefresh) {
            return true;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            float x = motionEvent.getX();
            this.mTouchStartX = x;
            this.mTouchCurX = x;
            setScrollState(false);
        } else if (action == 2 && motionEvent.getX() - this.mTouchStartX < -10.0f && !canScrollRight()) {
            setScrollState(true);
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.isRefresh) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action != 1) {
            if (action == 2) {
                float x = motionEvent.getX();
                this.mTouchCurX = x;
                float max = Math.max(0.0f, Math.min(this.mPullWidth * 2.0f, this.mTouchStartX - x));
                if (this.mChildView != null && max > 0.0f) {
                    float f = max / 2.0f;
                    float interpolation = this.interpolator.getInterpolation(f / this.mPullWidth) * f;
                    this.mChildView.setTranslationX(-interpolation);
                    this.footerView.getLayoutParams().width = (int) interpolation;
                    this.footerView.requestLayout();
                    moveMoreView(interpolation, false);
                }
                return true;
            } else if (action != 3) {
                return super.onTouchEvent(motionEvent);
            }
        }
        View view = this.mChildView;
        if (view == null) {
            return true;
        }
        float abs = Math.abs(view.getTranslationX());
        if (abs >= this.mFooterWidth) {
            this.mBackAnimator.setFloatValues(abs, 0.0f);
            this.mBackAnimator.start();
            this.footerView.releaseDrag();
            if (reachReleasePoint()) {
                this.isRefresh = true;
            }
        } else {
            this.mBackAnimator.setFloatValues(abs, 0.0f);
            this.mBackAnimator.start();
        }
        setScrollState(false);
        return true;
    }

    private boolean canScrollRight() {
        View view = this.mChildView;
        if (view == null) {
            return false;
        }
        return ViewCompat.canScrollHorizontally(view, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void moveMoreView(float f, boolean z) {
        float f2 = f / 2.0f;
        if (f2 <= MORE_VIEW_MOVE_DIMEN) {
            this.moreView.setTranslationX(-f2);
            if (z || !switchMoreText(SCAN_MORE)) {
                return;
            }
            this.arrowIv.clearAnimation();
            this.arrowIv.startAnimation(this.mArrowRotateBackAnim);
        } else if (switchMoreText(RELEASE_SCAN_MORE)) {
            this.arrowIv.clearAnimation();
            this.arrowIv.startAnimation(this.mArrowRotateAnim);
        }
    }

    private boolean switchMoreText(String str) {
        if (str.equals(this.moreText.getText().toString())) {
            return false;
        }
        this.moreText.setText(str);
        return true;
    }

    private boolean reachReleasePoint() {
        return RELEASE_SCAN_MORE.equals(this.moreText.getText().toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AnimListener implements Animator.AnimatorListener {
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        private AnimListener() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (PullLeftToRefreshLayout.this.onRefreshListener != null && PullLeftToRefreshLayout.this.isRefresh) {
                PullLeftToRefreshLayout.this.onRefreshListener.onRefresh();
            }
            PullLeftToRefreshLayout.this.moreText.setText(PullLeftToRefreshLayout.SCAN_MORE);
            PullLeftToRefreshLayout.this.arrowIv.clearAnimation();
            PullLeftToRefreshLayout.this.isRefresh = false;
        }
    }

    private void setScrollState(boolean z) {
        if (this.scrollState == z) {
            return;
        }
        this.scrollState = z;
        OnScrollListener onScrollListener = this.onScrollListener;
        if (onScrollListener != null) {
            onScrollListener.onScrollChange(z);
        }
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }
}
