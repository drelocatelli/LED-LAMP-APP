package com.example.linechartlibrary;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import androidx.core.widget.ScrollerCompat;

/* loaded from: classes.dex */
public class ChartScroller {
    private ScrollerCompat scroller;
    private Viewport scrollerStartViewport = new Viewport();
    private Point surfaceSizeBuffer = new Point();

    /* loaded from: classes.dex */
    public static class ScrollResult {
        public boolean canScrollX;
        public boolean canScrollY;
    }

    public ChartScroller(Context context) {
        this.scroller = ScrollerCompat.create(context);
    }

    public boolean startScroll(ChartComputator chartComputator) {
        this.scroller.abortAnimation();
        this.scrollerStartViewport.set(chartComputator.getCurrentViewport());
        return true;
    }

    public boolean scroll(ChartComputator chartComputator, float f, float f2, ScrollResult scrollResult) {
        Viewport maximumViewport = chartComputator.getMaximumViewport();
        Viewport visibleViewport = chartComputator.getVisibleViewport();
        Viewport currentViewport = chartComputator.getCurrentViewport();
        Rect contentRectMinusAllMargins = chartComputator.getContentRectMinusAllMargins();
        boolean z = currentViewport.left > maximumViewport.left;
        boolean z2 = currentViewport.right < maximumViewport.right;
        boolean z3 = currentViewport.f12top < maximumViewport.f12top;
        boolean z4 = currentViewport.bottom > maximumViewport.bottom;
        boolean z5 = (z && f <= 0.0f) || (z2 && f >= 0.0f);
        boolean z6 = (z3 && f2 <= 0.0f) || (z4 && f2 >= 0.0f);
        if (z5 || z6) {
            chartComputator.computeScrollSurfaceSize(this.surfaceSizeBuffer);
            chartComputator.setViewportTopLeft(currentViewport.left + ((f * visibleViewport.width()) / contentRectMinusAllMargins.width()), currentViewport.f12top + (((-f2) * visibleViewport.height()) / contentRectMinusAllMargins.height()));
        }
        scrollResult.canScrollX = z5;
        scrollResult.canScrollY = z6;
        return z5 || z6;
    }

    public boolean computeScrollOffset(ChartComputator chartComputator) {
        if (this.scroller.computeScrollOffset()) {
            Viewport maximumViewport = chartComputator.getMaximumViewport();
            chartComputator.computeScrollSurfaceSize(this.surfaceSizeBuffer);
            chartComputator.setViewportTopLeft(maximumViewport.left + ((maximumViewport.width() * this.scroller.getCurrX()) / this.surfaceSizeBuffer.x), maximumViewport.f12top - ((maximumViewport.height() * this.scroller.getCurrY()) / this.surfaceSizeBuffer.y));
            return true;
        }
        return false;
    }

    public boolean fling(int i, int i2, ChartComputator chartComputator) {
        chartComputator.computeScrollSurfaceSize(this.surfaceSizeBuffer);
        this.scrollerStartViewport.set(chartComputator.getCurrentViewport());
        int width = (int) ((this.surfaceSizeBuffer.x * (this.scrollerStartViewport.left - chartComputator.getMaximumViewport().left)) / chartComputator.getMaximumViewport().width());
        int height = (int) ((this.surfaceSizeBuffer.y * (chartComputator.getMaximumViewport().f12top - this.scrollerStartViewport.f12top)) / chartComputator.getMaximumViewport().height());
        this.scroller.abortAnimation();
        this.scroller.fling(width, height, i, i2, 0, (this.surfaceSizeBuffer.x - chartComputator.getContentRectMinusAllMargins().width()) + 1, 0, (this.surfaceSizeBuffer.y - chartComputator.getContentRectMinusAllMargins().height()) + 1);
        return true;
    }
}
