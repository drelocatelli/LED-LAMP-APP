package com.example.linechartlibrary;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/* loaded from: classes.dex */
public class ChartViewportAnimatorV8 implements ChartViewportAnimator {
    final Chart chart;
    long start;
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    boolean isAnimationStarted = false;
    private Viewport startViewport = new Viewport();
    private Viewport targetViewport = new Viewport();
    private Viewport newViewport = new Viewport();
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    private final Runnable runnable = new Runnable() { // from class: com.example.linechartlibrary.ChartViewportAnimatorV8.1
        @Override // java.lang.Runnable
        public void run() {
            long uptimeMillis = SystemClock.uptimeMillis() - ChartViewportAnimatorV8.this.start;
            if (uptimeMillis <= ChartViewportAnimatorV8.this.duration) {
                float min = Math.min(ChartViewportAnimatorV8.this.interpolator.getInterpolation(((float) uptimeMillis) / ((float) ChartViewportAnimatorV8.this.duration)), 1.0f);
                ChartViewportAnimatorV8.this.newViewport.set(ChartViewportAnimatorV8.this.startViewport.left + ((ChartViewportAnimatorV8.this.targetViewport.left - ChartViewportAnimatorV8.this.startViewport.left) * min), ChartViewportAnimatorV8.this.startViewport.f12top + ((ChartViewportAnimatorV8.this.targetViewport.f12top - ChartViewportAnimatorV8.this.startViewport.f12top) * min), ChartViewportAnimatorV8.this.startViewport.right + ((ChartViewportAnimatorV8.this.targetViewport.right - ChartViewportAnimatorV8.this.startViewport.right) * min), ChartViewportAnimatorV8.this.startViewport.bottom + ((ChartViewportAnimatorV8.this.targetViewport.bottom - ChartViewportAnimatorV8.this.startViewport.bottom) * min));
                ChartViewportAnimatorV8.this.chart.setCurrentViewport(ChartViewportAnimatorV8.this.newViewport);
                ChartViewportAnimatorV8.this.handler.postDelayed(this, 16L);
                return;
            }
            ChartViewportAnimatorV8.this.isAnimationStarted = false;
            ChartViewportAnimatorV8.this.handler.removeCallbacks(ChartViewportAnimatorV8.this.runnable);
            ChartViewportAnimatorV8.this.chart.setCurrentViewport(ChartViewportAnimatorV8.this.targetViewport);
            ChartViewportAnimatorV8.this.animationListener.onAnimationFinished();
        }
    };
    private long duration = 300;
    final Handler handler = new Handler();

    public ChartViewportAnimatorV8(Chart chart) {
        this.chart = chart;
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public void startAnimation(Viewport viewport, Viewport viewport2) {
        this.startViewport.set(viewport);
        this.targetViewport.set(viewport2);
        this.duration = 300L;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public void startAnimation(Viewport viewport, Viewport viewport2, long j) {
        this.startViewport.set(viewport);
        this.targetViewport.set(viewport2);
        this.duration = j;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.setCurrentViewport(this.targetViewport);
        this.animationListener.onAnimationFinished();
    }

    @Override // com.example.linechartlibrary.ChartViewportAnimator
    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
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
