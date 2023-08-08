package com.home.widget.effects;

import android.view.View;
import com.nineoldandroids.animation.ObjectAnimator;

/* loaded from: classes.dex */
public class NewsPaper extends BaseEffects {
    @Override // com.home.widget.effects.BaseEffects
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(ObjectAnimator.ofFloat(view, "rotation", 1080.0f, 720.0f, 360.0f, 0.0f).setDuration(this.mDuration), ObjectAnimator.ofFloat(view, "alpha", 0.0f, 1.0f).setDuration((this.mDuration * 3) / 2), ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.5f, 1.0f).setDuration(this.mDuration), ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.5f, 1.0f).setDuration(this.mDuration));
    }
}
