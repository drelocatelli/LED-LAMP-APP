package com.forum.im.widget;

import android.content.Context;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class HeadIconSelectorView extends RelativeLayout implements GestureDetector.OnGestureListener {
    public static final int BLANK_CANCEL = 5;
    public static final int CANCEL = 4;
    public static final int FROM_CAMERA = 2;
    public static final int FROM_GALLERY = 3;
    private View baseView;
    private LinearLayout bottomLl;
    private LinearLayout cameraLl;
    private LinearLayout cancelLl;
    private LinearLayout galleryLl;
    private GestureDetector gestureDetector;
    private boolean isAnimationing;
    private RelativeLayout mainRl;
    private float minDistanceY;
    private float minVelocityY;
    private OnHeadIconClickListener onHeadIconClickListener;

    /* loaded from: classes.dex */
    public interface OnHeadIconClickListener {
        void onClick(int i);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public HeadIconSelectorView(Context context) {
        super(context);
        this.isAnimationing = false;
        this.minVelocityY = 100.0f;
        this.minDistanceY = 100.0f;
        init();
    }

    private void init() {
        findView();
        setOnTouchListener(new View.OnTouchListener() { // from class: com.forum.im.widget.HeadIconSelectorView.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return HeadIconSelectorView.this.gestureDetector.onTouchEvent(motionEvent);
            }
        });
        this.bottomLl.setVisibility(4);
        this.mainRl.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.widget.HeadIconSelectorView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (HeadIconSelectorView.this.onHeadIconClickListener != null) {
                    HeadIconSelectorView.this.onHeadIconClickListener.onClick(5);
                }
                HeadIconSelectorView.this.cancel();
            }
        });
        this.cancelLl.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.widget.HeadIconSelectorView.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (HeadIconSelectorView.this.onHeadIconClickListener != null) {
                    HeadIconSelectorView.this.onHeadIconClickListener.onClick(4);
                }
                HeadIconSelectorView.this.cancel();
            }
        });
        this.cameraLl.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.widget.HeadIconSelectorView.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (HeadIconSelectorView.this.onHeadIconClickListener != null) {
                    HeadIconSelectorView.this.onHeadIconClickListener.onClick(2);
                }
                HeadIconSelectorView.this.cancel();
            }
        });
        this.galleryLl.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.widget.HeadIconSelectorView.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (HeadIconSelectorView.this.onHeadIconClickListener != null) {
                    HeadIconSelectorView.this.onHeadIconClickListener.onClick(3);
                }
                HeadIconSelectorView.this.cancel();
            }
        });
    }

    private void findView() {
        this.gestureDetector = new GestureDetector(this);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_view_headicon, this);
        this.baseView = inflate;
        this.mainRl = (RelativeLayout) inflate.findViewById(R.id.head_icon_main_rl);
        this.bottomLl = (LinearLayout) this.baseView.findViewById(R.id.head_icon_main_ll);
        this.cameraLl = (LinearLayout) this.baseView.findViewById(R.id.head_icon_camera_ll);
        this.galleryLl = (LinearLayout) this.baseView.findViewById(R.id.head_icon_gallery_ll);
        this.cancelLl = (LinearLayout) this.baseView.findViewById(R.id.head_icon_cancel_ll);
    }

    protected void bottomViewFlyIn() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.2f, 1, 1.0f, 1, 1.0f);
        scaleAnimation.setDuration(250L);
        final ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.0f, 1.0f, 1.2f, 1.0f, 1, 1.0f, 1, 1.0f);
        scaleAnimation2.setDuration(150L);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.forum.im.widget.HeadIconSelectorView.6
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                HeadIconSelectorView.this.isAnimationing = true;
                HeadIconSelectorView.this.bottomLl.setVisibility(0);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                HeadIconSelectorView.this.isAnimationing = false;
                HeadIconSelectorView.this.bottomLl.startAnimation(scaleAnimation2);
            }
        });
        this.bottomLl.startAnimation(scaleAnimation);
    }

    protected void bottomViewFlyOut() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.2f, 1, 1.0f, 1, 1.0f);
        scaleAnimation.setDuration(150L);
        final ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.0f, 1.0f, 1.2f, 0.0f, 1, 1.0f, 1, 1.0f);
        scaleAnimation2.setDuration(250L);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.forum.im.widget.HeadIconSelectorView.7
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                HeadIconSelectorView.this.isAnimationing = true;
                HeadIconSelectorView.this.bottomLl.setVisibility(0);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                HeadIconSelectorView.this.bottomLl.startAnimation(scaleAnimation2);
            }
        });
        scaleAnimation2.setAnimationListener(new Animation.AnimationListener() { // from class: com.forum.im.widget.HeadIconSelectorView.8
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                HeadIconSelectorView.this.isAnimationing = false;
                HeadIconSelectorView.this.bottomLl.setVisibility(4);
                HeadIconSelectorView.this.flyOut();
            }
        });
        this.bottomLl.startAnimation(scaleAnimation);
    }

    protected void cancel() {
        if (this.isAnimationing || this.bottomLl.getVisibility() != 0) {
            return;
        }
        bottomViewFlyOut();
    }

    public void flyIn() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300L);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.forum.im.widget.HeadIconSelectorView.9
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                HeadIconSelectorView.this.isAnimationing = true;
                HeadIconSelectorView.this.setVisibility(0);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                HeadIconSelectorView.this.isAnimationing = false;
                HeadIconSelectorView.this.bottomViewFlyIn();
            }
        });
        startAnimation(alphaAnimation);
    }

    public void flyOut() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300L);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.forum.im.widget.HeadIconSelectorView.10
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
                HeadIconSelectorView.this.isAnimationing = true;
                HeadIconSelectorView.this.setVisibility(0);
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                HeadIconSelectorView.this.isAnimationing = false;
                HeadIconSelectorView.this.destroy();
            }
        });
        startAnimation(alphaAnimation);
    }

    protected void destroy() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (!this.isAnimationing) {
                if (this.bottomLl.getVisibility() == 0) {
                    bottomViewFlyOut();
                }
            }
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public OnHeadIconClickListener getOnHeadIconClickListener() {
        return this.onHeadIconClickListener;
    }

    public void setOnHeadIconClickListener(OnHeadIconClickListener onHeadIconClickListener) {
        this.onHeadIconClickListener = onHeadIconClickListener;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }

    @Override // android.view.GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (motionEvent2.getY() - motionEvent.getY() <= this.minDistanceY || f2 <= this.minVelocityY) {
            return false;
        }
        cancel();
        return false;
    }
}
