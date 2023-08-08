package com.example.linechartlibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;

/* loaded from: classes.dex */
public class ChartDataAnimatorV14 implements ChartDataAnimator, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    private ValueAnimator animator;
    private final Chart chart;

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    public ChartDataAnimatorV14(Chart chart) {
        this.chart = chart;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator = ofFloat;
        ofFloat.addListener(this);
        this.animator.addUpdateListener(this);
    }

    @Override // com.example.linechartlibrary.ChartDataAnimator
    public void startAnimation(long j) {
        if (j >= 0) {
            this.animator.setDuration(j);
        } else {
            this.animator.setDuration(500L);
        }
        this.animator.start();
    }

    @Override // com.example.linechartlibrary.ChartDataAnimator
    public void cancelAnimation() {
        this.animator.cancel();
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.chart.animationDataUpdate(valueAnimator.getAnimatedFraction());
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.chart.animationDataFinished();
        this.animationListener.onAnimationFinished();
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        this.animationListener.onAnimationStarted();
    }

    @Override // com.example.linechartlibrary.ChartDataAnimator
    public boolean isAnimationStarted() {
        return this.animator.isStarted();
    }

    @Override // com.example.linechartlibrary.ChartDataAnimator
    public void setChartAnimationListener(ChartAnimationListener chartAnimationListener) {
        if (chartAnimationListener == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = chartAnimationListener;
        }
    }
}
