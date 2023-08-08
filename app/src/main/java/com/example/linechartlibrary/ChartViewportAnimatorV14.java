package com.example.linechartlibrary;

import android.animation.Animator;
import android.animation.ValueAnimator;

/* loaded from: classes.dex */
public class ChartViewportAnimatorV14 implements ChartViewportAnimator, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ValueAnimator animator;
    private final Chart chart;
    private Viewport startViewport = new Viewport();
    private Viewport targetViewport = new Viewport();
    private Viewport newViewport = new Viewport();
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    public ChartViewportAnimatorV14(Chart chart) {
        this.chart = chart;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator = ofFloat;
        ofFloat.addListener(this);
        this.animator.addUpdateListener(this);
        this.animator.setDuration(300L);
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public void startAnimation(Viewport viewport, Viewport viewport2) {
        this.startViewport.set(viewport);
        this.targetViewport.set(viewport2);
        this.animator.setDuration(300L);
        this.animator.start();
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public void startAnimation(Viewport viewport, Viewport viewport2, long j) {
        this.startViewport.set(viewport);
        this.targetViewport.set(viewport2);
        this.animator.setDuration(j);
        this.animator.start();
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public void cancelAnimation() {
        this.animator.cancel();
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.newViewport.set(this.startViewport.left + ((this.targetViewport.left - this.startViewport.left) * animatedFraction), this.startViewport.f12top + ((this.targetViewport.f12top - this.startViewport.f12top) * animatedFraction), this.startViewport.right + ((this.targetViewport.right - this.startViewport.right) * animatedFraction), this.startViewport.bottom + ((this.targetViewport.bottom - this.startViewport.bottom) * animatedFraction));
        this.chart.setCurrentViewport(this.newViewport);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        this.chart.setCurrentViewport(this.targetViewport);
        this.animationListener.onAnimationFinished();
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        this.animationListener.onAnimationStarted();
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public boolean isAnimationStarted() {
        return this.animator.isStarted();
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public void setChartAnimationListener(ChartAnimationListener chartAnimationListener) {
        if (chartAnimationListener == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = chartAnimationListener;
        }
    }
}
