package com.home.widget.effects;

import android.view.View;
import com.nineoldandroids.animation.ObjectAnimator;

/* loaded from: classes.dex */
public class Shake extends BaseEffects {
    @Override // com.home.widget.effects.BaseEffects
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(ObjectAnimator.ofFloat(view, "translationX", 0.0f, 0.1f, -25.0f, 0.26f, 25.0f, 0.42f, -25.0f, 0.58f, 25.0f, 0.74f, -25.0f, 0.9f, 1.0f, 0.0f).setDuration(this.mDuration));
    }
}
