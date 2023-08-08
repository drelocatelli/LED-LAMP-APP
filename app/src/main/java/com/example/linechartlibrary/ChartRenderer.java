package com.example.linechartlibrary;

import android.graphics.Canvas;

/* loaded from: classes.dex */
public interface ChartRenderer {
    boolean checkTouch(float f, float f2);

    void clearTouch();

    void draw(Canvas canvas);

    void drawUnclipped(Canvas canvas);

    Viewport getCurrentViewport();

    Viewport getMaximumViewport();

    SelectedValue getSelectedValue();

    boolean isTouched();

    boolean isViewportCalculationEnabled();

    void onChartDataChanged();

    void onChartSizeChanged();

    void onChartViewportChanged();

    void resetRenderer();

    void selectValue(SelectedValue selectedValue);

    void setCurrentViewport(Viewport viewport);

    void setMaximumViewport(Viewport viewport);

    void setViewportCalculationEnabled(boolean z);
}
