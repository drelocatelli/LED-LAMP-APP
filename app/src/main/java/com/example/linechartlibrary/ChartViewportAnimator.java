package com.example.linechartlibrary;

/* loaded from: classes.dex */
public interface ChartViewportAnimator {
    public static final int FAST_ANIMATION_DURATION = 300;

    void cancelAnimation();

    boolean isAnimationStarted();

    void setChartAnimationListener(ChartAnimationListener chartAnimationListener);

    void startAnimation(Viewport viewport, Viewport viewport2);

    void startAnimation(Viewport viewport, Viewport viewport2, long j);
}
