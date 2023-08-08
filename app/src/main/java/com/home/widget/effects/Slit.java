package com.home.widget.effects;

import android.view.View;
import com.nineoldandroids.animation.ObjectAnimator;

/* loaded from: classes.dex */
public class Slit extends BaseEffects {
    @Override // com.home.widget.effects.BaseEffects
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(ObjectAnimator.ofFloat(view, "rotationY", 90.0f, 88.0f, 88.0f, 45.0f, 0.0f).setDuration(this.mDuration), ObjectAnimator.ofFloat(view, "alpha", 0.0f, 0.4f, 0.8f, 1.0f).setDuration((this.mDuration * 3) / 2), ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 0.5f, 0.9f, 0.9f, 1.0f).setDuration(this.mDuration), ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 0.5f, 0.9f, 0.9f, 1.0f).setDuration(this.mDuration));
    }
}
