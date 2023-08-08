package com.example.linechartlibrary;

/* loaded from: classes.dex */
public interface ChartDataAnimator {
    public static final long DEFAULT_ANIMATION_DURATION = 500;

    void cancelAnimation();

    boolean isAnimationStarted();

    void setChartAnimationListener(ChartAnimationListener chartAnimationListener);

    void startAnimation(long j);
}
