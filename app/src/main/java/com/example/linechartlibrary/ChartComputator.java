package com.example.linechartlibrary;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;

/* loaded from: classes.dex */
public class ChartComputator {
    protected static final float DEFAULT_MAXIMUM_ZOOM = 20.0f;
    private static final String TAG = "ChartComputator";
    protected int chartHeight;
    protected int chartWidth;
    protected float minViewportHeight;
    protected float minViewportWidth;
    protected float maxZoom = DEFAULT_MAXIMUM_ZOOM;
    protected Rect contentRectMinusAllMargins = new Rect();
    protected Rect contentRectMinusAxesMargins = new Rect();
    protected Rect maxContentRect = new Rect();
    protected Viewport currentViewport = new Viewport();
    protected Viewport maxViewport = new Viewport();
    protected ViewportChangeListener viewportChangeListener = new DummyVieportChangeListener();

    public void setContentRect(int i, int i2, int i3, int i4, int i5, int i6) {
        this.chartWidth = i;
        this.chartHeight = i2;
        this.maxContentRect.set(i3, i4, i - i5, i2 - i6);
        this.contentRectMinusAxesMargins.set(this.maxContentRect);
        this.contentRectMinusAllMargins.set(this.maxContentRect);
    }

    public void resetContentRect() {
        this.contentRectMinusAxesMargins.set(this.maxContentRect);
        this.contentRectMinusAllMargins.set(this.maxContentRect);
    }

    public void insetContentRect(int i, int i2, int i3, int i4) {
        this.contentRectMinusAxesMargins.left += i;
        this.contentRectMinusAxesMargins.top += i2;
        this.contentRectMinusAxesMargins.right -= i3;
        this.contentRectMinusAxesMargins.bottom -= i4;
        insetContentRectByInternalMargins(i, i2, i3, i4);
    }

    public void insetContentRectByInternalMargins(int i, int i2, int i3, int i4) {
        Rect rect = this.contentRectMinusAllMargins;
        rect.left = rect.left;
        Rect rect2 = this.contentRectMinusAllMargins;
        rect2.top = rect2.top + i2 + 5;
        Rect rect3 = this.contentRectMinusAllMargins;
        rect3.right = rect3.right;
        this.contentRectMinusAllMargins.bottom -= i4;
    }

    public void constrainViewport(float f, float f2, float f3, float f4) {
        float f5 = this.minViewportWidth;
        if (f3 - f < f5) {
            f3 = f + f5;
            if (f < this.maxViewport.left) {
                f = this.maxViewport.left;
                f3 = this.minViewportWidth + f;
            } else if (f3 > this.maxViewport.right) {
                f3 = this.maxViewport.right;
                f = f3 - this.minViewportWidth;
            }
        }
        float f6 = this.minViewportHeight;
        if (f2 - f4 < f6) {
            f4 = f2 - f6;
            if (f2 > this.maxViewport.f12top) {
                f2 = this.maxViewport.f12top;
                f4 = f2 - this.minViewportHeight;
            } else if (f4 < this.maxViewport.bottom) {
                f4 = this.maxViewport.bottom;
                f2 = this.minViewportHeight + f4;
            }
        }
        this.currentViewport.left = Math.max(this.maxViewport.left, f);
        this.currentViewport.f12top = Math.min(this.maxViewport.f12top, f2);
        this.currentViewport.right = Math.min(this.maxViewport.right, f3);
        this.currentViewport.bottom = Math.max(this.maxViewport.bottom, f4);
        this.viewportChangeListener.onViewportChanged(this.currentViewport);
    }

    public void setViewportTopLeft(float f, float f2) {
        float width = this.currentViewport.width();
        float height = this.currentViewport.height();
        float max = Math.max(this.maxViewport.left, Math.min(f, this.maxViewport.right - width));
        float max2 = Math.max(this.maxViewport.bottom + height, Math.min(f2, this.maxViewport.f12top));
        constrainViewport(max, max2, width + max, max2 - height);
    }

    public float computeRawX(float f) {
        return this.contentRectMinusAllMargins.left + ((f - this.currentViewport.left) * (this.contentRectMinusAllMargins.width() / this.currentViewport.width()));
    }

    public float computeRawY(float f) {
        return this.contentRectMinusAllMargins.bottom - ((f - this.currentViewport.bottom) * (this.contentRectMinusAllMargins.height() / this.currentViewport.height()));
    }

    public float computeRawDistanceX(float f) {
        return f * (this.contentRectMinusAllMargins.width() / this.currentViewport.width());
    }

    public float computeRawDistanceY(float f) {
        return f * (this.contentRectMinusAllMargins.height() / this.currentViewport.height());
    }

    public boolean rawPixelsToDataPoint(float f, float f2, PointF pointF) {
        if (this.contentRectMinusAllMargins.contains((int) f, (int) f2)) {
            pointF.set(this.currentViewport.left + (((f - this.contentRectMinusAllMargins.left) * this.currentViewport.width()) / this.contentRectMinusAllMargins.width()), this.currentViewport.bottom + (((f2 - this.contentRectMinusAllMargins.bottom) * this.currentViewport.height()) / (-this.contentRectMinusAllMargins.height())));
            return true;
        }
        return false;
    }

    public void computeScrollSurfaceSize(Point point) {
        point.set((int) ((this.maxViewport.width() * this.contentRectMinusAllMargins.width()) / this.currentViewport.width()), (int) ((this.maxViewport.height() * this.contentRectMinusAllMargins.height()) / this.currentViewport.height()));
    }

    public boolean isWithinContentRect(float f, float f2, float f3) {
        return f >= ((float) this.contentRectMinusAllMargins.left) - f3 && f <= ((float) this.contentRectMinusAllMargins.right) + f3 && f2 <= ((float) this.contentRectMinusAllMargins.bottom) + f3 && f2 >= ((float) this.contentRectMinusAllMargins.top) - f3;
    }

    public Rect getContentRectMinusAllMargins() {
        return this.contentRectMinusAllMargins;
    }

    public Rect getContentRectMinusAxesMargins() {
        return this.contentRectMinusAxesMargins;
    }

    public Viewport getCurrentViewport() {
        return this.currentViewport;
    }

    public void setCurrentViewport(Viewport viewport) {
        constrainViewport(viewport.left, viewport.f12top, viewport.right, viewport.bottom);
    }

    public void setCurrentViewport(float f, float f2, float f3, float f4) {
        constrainViewport(f, f2, f3, f4);
    }

    public Viewport getMaximumViewport() {
        return this.maxViewport;
    }

    public void setMaxViewport(Viewport viewport) {
        setMaxViewport(viewport.left, viewport.f12top, viewport.right, viewport.bottom);
    }

    public void setMaxViewport(float f, float f2, float f3, float f4) {
        this.maxViewport.set(f, f2, f3, f4);
        computeMinimumWidthAndHeight();
    }

    public Viewport getVisibleViewport() {
        return this.currentViewport;
    }

    public void setVisibleViewport(Viewport viewport) {
        setCurrentViewport(viewport);
    }

    public float getMinimumViewportWidth() {
        return this.minViewportWidth;
    }

    public float getMinimumViewportHeight() {
        return this.minViewportHeight;
    }

    public void setViewportChangeListener(ViewportChangeListener viewportChangeListener) {
        if (viewportChangeListener == null) {
            this.viewportChangeListener = new DummyVieportChangeListener();
        } else {
            this.viewportChangeListener = viewportChangeListener;
        }
    }

    public int getChartWidth() {
        return this.chartWidth;
    }

    public int getChartHeight() {
        return this.chartHeight;
    }

    public float getMaxZoom() {
        return this.maxZoom;
    }

    public void setMaxZoom(float f) {
        if (f < 1.0f) {
            f = 1.0f;
        }
        this.maxZoom = f;
        computeMinimumWidthAndHeight();
        setCurrentViewport(this.currentViewport);
    }

    private void computeMinimumWidthAndHeight() {
        this.minViewportWidth = this.maxViewport.width() / this.maxZoom;
        this.minViewportHeight = this.maxViewport.height() / this.maxZoom;
    }
}
