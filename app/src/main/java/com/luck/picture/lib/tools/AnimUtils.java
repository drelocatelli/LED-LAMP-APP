package com.luck.picture.lib.tools;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class AnimUtils {
    private static final int DURATION = 450;

    public static void zoom(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.12f), ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.12f));
        animatorSet.setDuration(450L);
        animatorSet.start();
    }

    public static void disZoom(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", 1.12f, 1.0f), ObjectAnimator.ofFloat(view, "scaleY", 1.12f, 1.0f));
        animatorSet.setDuration(450L);
        animatorSet.start();
    }

    public static void rotateArrow(ImageView imageView, boolean z) {
        RotateAnimation rotateAnimation = new RotateAnimation(180.0f, z ? 360.0f : 0.0f, imageView.getWidth() / 2.0f, imageView.getHeight() / 2.0f);
        rotateAnimation.setDuration(350L);
        imageView.startAnimation(rotateAnimation);
    }
}
