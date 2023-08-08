package com.home.widget.effects;

import android.view.View;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.view.ViewHelper;

/* loaded from: classes.dex */
public abstract class BaseEffects {
    private static final int DURATION = 700;
    protected long mDuration = 700;
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    protected abstract void setupAnimation(View view);

    public void start(View view) {
        reset(view);
        setupAnimation(view);
        this.mAnimatorSet.start();
    }

    public void reset(View view) {
        ViewHelper.setPivotX(view, view.getMeasuredWidth() / 2.0f);
        ViewHelper.setPivotY(view, view.getMeasuredHeight() / 2.0f);
    }

    public AnimatorSet getAnimatorSet() {
        return this.mAnimatorSet;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }
}
