package com.common.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import com.ledlamp.R;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class DragGridView extends GridView {
    public static final String TAG = "DragGridView";
    private static final int speed = 20;
    private long dragResponseMS;
    private boolean isDrag;
    private boolean mAnimationEnd;
    ClickCallBack mClickCallBack;
    private int mColumnWidth;
    private int mDownScrollBorder;
    private int mDownX;
    private int mDownY;
    private DragGridBaseAdapter mDragAdapter;
    private Bitmap mDragBitmap;
    private ImageView mDragImageView;
    private boolean mDragLastPosition;
    private FrameLayout mDragLayout;
    private int mDragPosition;
    private float mDragScale;
    private int mDragStartPosition;
    private Handler mHandler;
    private int mHorizontalSpacing;
    private boolean mIsScaleAnima;
    private boolean mIsVibrator;
    private Runnable mLongClickRunnable;
    private int mNumColumns;
    private boolean mNumColumnsSet;
    private int mOffset2Left;
    private int mOffset2Top;
    private int mPoint2ItemLeft;
    private int mPoint2ItemTop;
    private int mScaleMill;
    private Runnable mScrollRunnable;
    private View mStartDragItemView;
    private int mStartDragPosition;
    private int mStatusHeight;
    private int mUpScrollBorder;
    private Vibrator mVibrator;
    private int mViewHeight;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private WindowManager mWindowManager;
    private int moveX;
    private int moveY;

    /* loaded from: classes.dex */
    public interface ClickCallBack {
        void clickD();

        void clickG(int i);

        void clickGone(int i, int i2);

        void clickV();
    }

    /* loaded from: classes.dex */
    public interface DragGridBaseAdapter {
        void deleteItem(int i);

        void reorderItems(int i, int i2);

        void setHideItem(int i);
    }

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DragGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.dragResponseMS = 700L;
        this.isDrag = false;
        this.mStartDragItemView = null;
        this.mAnimationEnd = true;
        this.mDragScale = 1.2f;
        this.mScaleMill = 200;
        this.mDragLastPosition = false;
        this.mDragStartPosition = 7;
        this.mIsScaleAnima = false;
        this.mIsVibrator = false;
        this.mHandler = new Handler();
        this.mLongClickRunnable = new Runnable() { // from class: com.common.view.DragGridView.1
            @Override // java.lang.Runnable
            public void run() {
                DragGridView.this.isDrag = true;
                DragGridView.this.mClickCallBack.clickV();
                if (DragGridView.this.mIsVibrator) {
                    DragGridView.this.mVibrator.vibrate(50L);
                }
                DragGridView.this.mStartDragItemView.setVisibility(4);
                DragGridView dragGridView = DragGridView.this;
                dragGridView.createDragImage(dragGridView.mDragBitmap, DragGridView.this.mDownX, DragGridView.this.mDownY);
            }
        };
        this.mScrollRunnable = new Runnable() { // from class: com.common.view.DragGridView.2
            @Override // java.lang.Runnable
            public void run() {
                int i2;
                if (DragGridView.this.getFirstVisiblePosition() == 0 || DragGridView.this.getLastVisiblePosition() == DragGridView.this.getCount() - 1) {
                    DragGridView.this.mHandler.removeCallbacks(DragGridView.this.mScrollRunnable);
                }
                if (DragGridView.this.moveY > DragGridView.this.mUpScrollBorder) {
                    i2 = 20;
                    DragGridView.this.mHandler.postDelayed(DragGridView.this.mScrollRunnable, 25L);
                } else if (DragGridView.this.moveY < DragGridView.this.mDownScrollBorder) {
                    i2 = -20;
                    DragGridView.this.mHandler.postDelayed(DragGridView.this.mScrollRunnable, 25L);
                } else {
                    i2 = 0;
                    DragGridView.this.mHandler.removeCallbacks(DragGridView.this.mScrollRunnable);
                }
                DragGridView.this.smoothScrollBy(i2, 10);
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DragGridView);
        this.mDragScale = obtainStyledAttributes.getFloat(1, 1.2f);
        this.mScaleMill = obtainStyledAttributes.getInteger(3, 200);
        this.mDragStartPosition = obtainStyledAttributes.getInteger(2, 7);
        this.mDragLastPosition = obtainStyledAttributes.getBoolean(0, false);
        this.mIsVibrator = obtainStyledAttributes.getBoolean(4, true);
        obtainStyledAttributes.recycle();
        if (!isInEditMode()) {
            this.mVibrator = (Vibrator) context.getSystemService("vibrator");
            this.mWindowManager = (WindowManager) context.getSystemService("window");
            this.mStatusHeight = getStatusHeight(context);
        }
        if (this.mNumColumnsSet) {
            return;
        }
        this.mNumColumns = -1;
    }

    @Override // android.widget.AdapterView
    public void setAdapter(ListAdapter listAdapter) {
        super.setAdapter(listAdapter);
        if (isInEditMode()) {
            return;
        }
        if (listAdapter instanceof DragGridBaseAdapter) {
            this.mDragAdapter = (DragGridBaseAdapter) listAdapter;
            return;
        }
        throw new IllegalStateException("the adapter must be implements DragGridAdapter");
    }

    public void setDragStartPosition(int i) {
        this.mDragStartPosition = i;
    }

    @Override // android.widget.GridView
    public void setNumColumns(int i) {
        super.setNumColumns(i);
        this.mNumColumnsSet = true;
        this.mNumColumns = i;
    }

    @Override // android.widget.GridView
    public void setColumnWidth(int i) {
        super.setColumnWidth(i);
        this.mColumnWidth = i;
    }

    @Override // android.widget.GridView
    public void setHorizontalSpacing(int i) {
        super.setHorizontalSpacing(i);
        this.mHorizontalSpacing = i;
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        if (this.mNumColumns == -1) {
            if (this.mColumnWidth > 0) {
                int max = Math.max((View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight(), 0);
                int i4 = max / this.mColumnWidth;
                i3 = 1;
                if (i4 > 0) {
                    while (i4 != 1 && (this.mColumnWidth * i4) + ((i4 - 1) * this.mHorizontalSpacing) > max) {
                        i4--;
                    }
                    i3 = i4;
                }
            } else {
                i3 = 2;
            }
            this.mNumColumns = i3;
        }
        super.onMeasure(i, i2);
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    public void setViewHeight(int i) {
        this.mViewHeight = i;
    }

    public void setDragResponseMS(long j) {
        this.dragResponseMS = j;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mDownX = (int) motionEvent.getX();
            this.mDownY = (int) motionEvent.getY();
            ClickCallBack clickCallBack = this.mClickCallBack;
            if (clickCallBack != null) {
                clickCallBack.clickD();
            }
            this.mDragPosition = pointToPosition(this.mDownX, this.mDownY);
            this.mStartDragPosition = pointToPosition(this.mDownX, this.mDownY);
            if (this.mDragPosition < this.mDragStartPosition) {
                return super.dispatchTouchEvent(motionEvent);
            }
            if (getAdapter() != null && this.mDragPosition == getAdapter().getCount() - 1 && !this.mDragLastPosition) {
                return super.dispatchTouchEvent(motionEvent);
            }
            if (this.mDragPosition == -1) {
                return super.dispatchTouchEvent(motionEvent);
            }
            this.mHandler.postDelayed(this.mLongClickRunnable, this.dragResponseMS);
            View childAt = getChildAt(this.mDragPosition - getFirstVisiblePosition());
            this.mStartDragItemView = childAt;
            this.mPoint2ItemTop = this.mDownY - childAt.getTop();
            this.mPoint2ItemLeft = this.mDownX - this.mStartDragItemView.getLeft();
            this.mOffset2Top = (int) (motionEvent.getRawY() - this.mDownY);
            this.mOffset2Left = (int) (motionEvent.getRawX() - this.mDownX);
            this.mDownScrollBorder = getHeight() / 5;
            this.mUpScrollBorder = (getHeight() * 4) / 5;
            this.mStartDragItemView.setDrawingCacheEnabled(true);
            Bitmap drawingCache = this.mStartDragItemView.getDrawingCache();
            this.mDragBitmap = Bitmap.createScaledBitmap(drawingCache, (int) (drawingCache.getWidth() * this.mDragScale), (int) (drawingCache.getHeight() * this.mDragScale), true);
            this.mStartDragItemView.destroyDrawingCache();
        } else if (action == 1) {
            this.mHandler.removeCallbacks(this.mLongClickRunnable);
            this.mHandler.removeCallbacks(this.mScrollRunnable);
            ClickCallBack clickCallBack2 = this.mClickCallBack;
            if (clickCallBack2 != null) {
                clickCallBack2.clickGone(this.mStartDragPosition, this.mDragPosition);
            }
        } else if (action == 2) {
            if (!isTouchInItem(this.mStartDragItemView, (int) motionEvent.getX(), (int) motionEvent.getY())) {
                this.mHandler.removeCallbacks(this.mLongClickRunnable);
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setSendCallBack(ClickCallBack clickCallBack) {
        this.mClickCallBack = clickCallBack;
    }

    private boolean isTouchInItem(View view, int i, int i2) {
        if (view == null) {
            return false;
        }
        int left = view.getLeft();
        int top2 = view.getTop();
        return i >= left && i <= left + view.getWidth() && i2 >= top2 && i2 <= top2 + view.getHeight();
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.isDrag && this.mDragImageView != null) {
            int action = motionEvent.getAction();
            if (action == 1) {
                onStopDrag((int) motionEvent.getX(), (int) motionEvent.getY());
                this.isDrag = false;
            } else if (action == 2) {
                this.moveX = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                this.moveY = y;
                onDragItem(this.moveX, y);
            }
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createDragImage(Bitmap bitmap, int i, int i2) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        this.mWindowLayoutParams = layoutParams;
        layoutParams.format = -3;
        this.mWindowLayoutParams.gravity = 51;
        this.mWindowLayoutParams.x = (int) ((i - (this.mPoint2ItemLeft * this.mDragScale)) + this.mOffset2Left);
        this.mWindowLayoutParams.y = (int) (((i2 - (this.mPoint2ItemTop * this.mDragScale)) + this.mOffset2Top) - this.mStatusHeight);
        this.mWindowLayoutParams.alpha = 1.0f;
        this.mWindowLayoutParams.width = -2;
        this.mWindowLayoutParams.height = -2;
        this.mWindowLayoutParams.flags = 24;
        ImageView imageView = new ImageView(getContext());
        this.mDragImageView = imageView;
        imageView.setImageBitmap(bitmap);
        FrameLayout frameLayout = new FrameLayout(getContext());
        this.mDragLayout = frameLayout;
        frameLayout.addView(this.mDragImageView);
        this.mWindowManager.addView(this.mDragLayout, this.mWindowLayoutParams);
    }

    private void removeDragImage() {
        if (this.mDragImageView != null) {
            this.mWindowManager.removeView(this.mDragLayout);
            this.mDragLayout = null;
            this.mDragImageView = null;
        }
    }

    private void onDragItem(int i, int i2) {
        this.mWindowLayoutParams.x = (i - this.mPoint2ItemLeft) + this.mOffset2Left;
        this.mWindowLayoutParams.y = ((i2 - this.mPoint2ItemTop) + this.mOffset2Top) - this.mStatusHeight;
        this.mWindowManager.updateViewLayout(this.mDragLayout, this.mWindowLayoutParams);
        onSwapItem(i, i2);
        this.mHandler.post(this.mScrollRunnable);
        if (i2 > this.mViewHeight) {
            if (this.mIsVibrator) {
                this.mVibrator.vibrate(50L);
            }
            if (this.mIsScaleAnima) {
                return;
            }
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, 1, 0.5f, 1, 0.5f);
            TranslateAnimation translateAnimation = new TranslateAnimation(2, 0.0f, 2, 0.0f, 2, 0.0f, 2, 0.25f);
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(translateAnimation);
            animationSet.setDuration(this.mScaleMill);
            animationSet.setFillAfter(true);
            this.mDragImageView.clearAnimation();
            this.mDragImageView.startAnimation(animationSet);
            this.mIsScaleAnima = true;
        } else if (this.mIsScaleAnima) {
            this.mDragImageView.clearAnimation();
            this.mIsScaleAnima = false;
        }
    }

    private void onSwapItem(int i, int i2) {
        int i3;
        final int pointToPosition = pointToPosition(i, i2);
        if (pointToPosition < this.mDragStartPosition) {
            return;
        }
        if ((getAdapter() == null || pointToPosition != getAdapter().getCount() - 1 || this.mDragLastPosition) && pointToPosition != (i3 = this.mDragPosition) && pointToPosition != -1 && this.mAnimationEnd) {
            this.mDragAdapter.reorderItems(i3, pointToPosition);
            this.mDragAdapter.setHideItem(pointToPosition);
            final ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.common.view.DragGridView.3
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    viewTreeObserver.removeOnPreDrawListener(this);
                    DragGridView dragGridView = DragGridView.this;
                    dragGridView.animateReorder(dragGridView.mDragPosition, pointToPosition);
                    DragGridView.this.mDragPosition = pointToPosition;
                    return true;
                }
            });
        }
    }

    private AnimatorSet createTranslationAnimations(View view, float f, float f2, float f3, float f4) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "translationX", f, f2);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", f3, f4);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofFloat, ofFloat2);
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateReorder(int i, int i2) {
        boolean z = i2 > i;
        LinkedList linkedList = new LinkedList();
        if (z) {
            while (i < i2) {
                View childAt = getChildAt(i - getFirstVisiblePosition());
                if (childAt == null) {
                    return;
                }
                i++;
                if (i % this.mNumColumns == 0) {
                    linkedList.add(createTranslationAnimations(childAt, (-childAt.getWidth()) * (this.mNumColumns - 1), 0.0f, childAt.getHeight(), 0.0f));
                } else {
                    linkedList.add(createTranslationAnimations(childAt, childAt.getWidth(), 0.0f, 0.0f, 0.0f));
                }
            }
        } else {
            while (i > i2) {
                View childAt2 = getChildAt(i - getFirstVisiblePosition());
                if (childAt2 == null) {
                    return;
                }
                int i3 = this.mNumColumns;
                if ((i + i3) % i3 == 0) {
                    linkedList.add(createTranslationAnimations(childAt2, childAt2.getWidth() * (this.mNumColumns - 1), 0.0f, -childAt2.getHeight(), 0.0f));
                } else {
                    linkedList.add(createTranslationAnimations(childAt2, -childAt2.getWidth(), 0.0f, 0.0f, 0.0f));
                }
                i--;
            }
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(linkedList);
        animatorSet.setDuration(300L);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.common.view.DragGridView.4
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                DragGridView.this.mAnimationEnd = false;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                DragGridView.this.mAnimationEnd = true;
            }
        });
        animatorSet.start();
    }

    private void onStopDrag(int i, int i2) {
        View childAt = getChildAt(this.mDragPosition - getFirstVisiblePosition());
        if (childAt != null) {
            childAt.setVisibility(0);
        }
        this.mDragAdapter.setHideItem(-1);
        removeDragImage();
        if (i2 > this.mViewHeight) {
            this.mIsScaleAnima = false;
            this.mClickCallBack.clickG(this.mDragPosition);
        }
    }

    private static int getStatusHeight(Context context) {
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int i = rect.top;
        if (i == 0) {
            try {
                Class<?> cls = Class.forName("com.android.internal.R$dimen");
                return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
            } catch (Exception e) {
                e.printStackTrace();
                return i;
            }
        }
        return i;
    }
}
