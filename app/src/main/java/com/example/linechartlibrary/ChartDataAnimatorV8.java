package com.example.linechartlibrary;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/* loaded from: classes.dex */
public class ChartDataAnimatorV8 implements ChartDataAnimator {
    final Chart chart;
    long duration;
    long start;
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    boolean isAnimationStarted = false;
    private final Runnable runnable = new Runnable() { // from class: com.example.linechartlibrary.ChartDataAnimatorV8.1
        @Override // java.lang.Runnable
        public void run() {
            long uptimeMillis = SystemClock.uptimeMillis() - ChartDataAnimatorV8.this.start;
            if (uptimeMillis > ChartDataAnimatorV8.this.duration) {
                ChartDataAnimatorV8.this.isAnimationStarted = false;
                ChartDataAnimatorV8.this.handler.removeCallbacks(ChartDataAnimatorV8.this.runnable);
                ChartDataAnimatorV8.this.chart.animationDataFinished();
                return;
            }
            ChartDataAnimatorV8.this.chart.animationDataUpdate(Math.min(ChartDataAnimatorV8.this.interpolator.getInterpolation(((float) uptimeMillis) / ((float) ChartDataAnimatorV8.this.duration)), 1.0f));
            ChartDataAnimatorV8.this.handler.postDelayed(this, 16L);
        }
    };
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    final Handler handler = new Handler();

    public ChartDataAnimatorV8(Chart chart) {
        this.chart = chart;
    }

    @Override // com.example.linechartlibrary.ChartDataAnimator
    public void startAnimation(long j) {
        if (j >= 0) {
            this.duration = j;
        } else {
            this.duration = 500L;
        }
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    @Override // com.example.linechartlibrary.ChartDataAnimator
    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.animationDataFinished();
        this.animationListener.onAnimationFinished();
    }

    @Override // com.example.linechartlibrary.ChartDataAnimator
    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
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
