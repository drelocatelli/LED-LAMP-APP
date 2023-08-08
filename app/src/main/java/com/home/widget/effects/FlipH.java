package com.home.widget.effects;

import android.view.View;
import com.nineoldandroids.animation.ObjectAnimator;

/* loaded from: classes.dex */
public class FlipH extends BaseEffects {
    @Override // com.home.widget.effects.BaseEffects
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(ObjectAnimator.ofFloat(view, "rotationY", -90.0f, 0.0f).setDuration(this.mDuration));
    }
}
