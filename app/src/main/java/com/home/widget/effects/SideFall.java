package com.home.widget.effects;

import android.view.View;
import com.nineoldandroids.animation.ObjectAnimator;

/* loaded from: classes.dex */
public class SideFall extends BaseEffects {
    @Override // com.home.widget.effects.BaseEffects
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(ObjectAnimator.ofFloat(view, "scaleX", 2.0f, 1.5f, 1.0f).setDuration(this.mDuration), ObjectAnimator.ofFloat(view, "scaleY", 2.0f, 1.5f, 1.0f).setDuration(this.mDuration), ObjectAnimator.ofFloat(view, "rotation", 25.0f, 0.0f).setDuration(this.mDuration), ObjectAnimator.ofFloat(view, "translationX", 80.0f, 0.0f).setDuration(this.mDuration), ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f).setDuration((this.mDuration * 3) / 2));
    }
}
