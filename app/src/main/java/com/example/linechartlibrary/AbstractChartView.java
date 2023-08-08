package com.example.linechartlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;

/* loaded from: classes.dex */
public abstract class AbstractChartView extends View implements Chart {
    private static final String TAG = "AbstractChartView";
    protected AxesRenderer axesRenderer;
    protected ChartComputator chartComputator;
    protected ChartRenderer chartRenderer;
    protected ContainerScrollType containerScrollType;
    protected ChartDataAnimator dataAnimator;
    protected boolean isContainerScrollEnabled;
    protected boolean isInteractive;
    protected ChartTouchHandler touchHandler;
    protected ChartViewportAnimator viewportAnimator;

    public AbstractChartView(Context context) {
        this(context, null, 0);
    }

    public AbstractChartView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AbstractChartView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isInteractive = true;
        this.isContainerScrollEnabled = false;
        this.chartComputator = new ChartComputator();
        this.touchHandler = new ChartTouchHandler(context, this);
        this.axesRenderer = new AxesRenderer(context, this);
        if (Build.VERSION.SDK_INT < 14) {
            this.dataAnimator = new ChartDataAnimatorV8(this);
            this.viewportAnimator = new ChartViewportAnimatorV8(this);
            return;
        }
        this.viewportAnimator = new ChartViewportAnimatorV14(this);
        this.dataAnimator = new ChartDataAnimatorV14(this);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.chartComputator.setContentRect(getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        this.chartRenderer.onChartSizeChanged();
        this.axesRenderer.onChartSizeChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnabled()) {
            this.axesRenderer.drawInBackground(canvas);
            this.axesRenderer.drawInForeground(canvas);
            if (LineChartView.isGone) {
                int save = canvas.save();
                canvas.clipRect(this.chartComputator.getContentRectMinusAllMargins());
                this.chartRenderer.draw(canvas);
                canvas.restoreToCount(save);
                this.chartRenderer.drawUnclipped(canvas);
                return;
            }
            return;
        }
        canvas.drawColor(ChartUtils.DEFAULT_COLOR);
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (this.isInteractive && this.touchHandler.computeScroll()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override // com.example.linechartlibrary.Chart
    public void startDataAnimation() {
        this.dataAnimator.startAnimation(Long.MIN_VALUE);
    }

    @Override // com.example.linechartlibrary.Chart
    public void startDataAnimation(long j) {
        this.dataAnimator.startAnimation(j);
    }

    @Override // com.example.linechartlibrary.Chart
    public void cancelDataAnimation() {
        this.dataAnimator.cancelAnimation();
    }

    @Override // com.example.linechartlibrary.Chart
    public void animationDataUpdate(float f) {
        getChartData().update(f);
        this.chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public void animationDataFinished() {
        getChartData().finish();
        this.chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public void setDataAnimationListener(ChartAnimationListener chartAnimationListener) {
        this.dataAnimator.setChartAnimationListener(chartAnimationListener);
    }

    @Override // com.example.linechartlibrary.Chart
    public void setViewportAnimationListener(ChartAnimationListener chartAnimationListener) {
        this.viewportAnimator.setChartAnimationListener(chartAnimationListener);
    }

    @Override // com.example.linechartlibrary.Chart
    public void setViewportChangeListener(ViewportChangeListener viewportChangeListener) {
        this.chartComputator.setViewportChangeListener(viewportChangeListener);
    }

    @Override // com.example.linechartlibrary.Chart
    public ChartRenderer getChartRenderer() {
        return this.chartRenderer;
    }

    @Override // com.example.linechartlibrary.Chart
    public void setChartRenderer(ChartRenderer chartRenderer) {
        this.chartRenderer = chartRenderer;
        resetRendererAndTouchHandler();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public AxesRenderer getAxesRenderer() {
        return this.axesRenderer;
    }

    @Override // com.example.linechartlibrary.Chart
    public ChartComputator getChartComputator() {
        return this.chartComputator;
    }

    @Override // com.example.linechartlibrary.Chart
    public ChartTouchHandler getTouchHandler() {
        return this.touchHandler;
    }

    @Override // com.example.linechartlibrary.Chart
    public boolean isInteractive() {
        return this.isInteractive;
    }

    @Override // com.example.linechartlibrary.Chart
    public void setInteractive(boolean z) {
        this.isInteractive = z;
    }

    @Override // com.example.linechartlibrary.Chart
    public boolean isZoomEnabled() {
        return this.touchHandler.isZoomEnabled();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setZoomEnabled(boolean z) {
        this.touchHandler.setZoomEnabled(z);
    }

    @Override // com.example.linechartlibrary.Chart
    public boolean isScrollEnabled() {
        return this.touchHandler.isScrollEnabled();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setScrollEnabled(boolean z) {
        this.touchHandler.setScrollEnabled(z);
    }

    @Override // com.example.linechartlibrary.Chart
    public void moveTo(float f, float f2) {
        setCurrentViewport(computeScrollViewport(f, f2));
    }

    @Override // com.example.linechartlibrary.Chart
    public void moveToWithAnimation(float f, float f2) {
        setCurrentViewportWithAnimation(computeScrollViewport(f, f2));
    }

    private Viewport computeScrollViewport(float f, float f2) {
        Viewport maximumViewport = getMaximumViewport();
        Viewport currentViewport = getCurrentViewport();
        Viewport viewport = new Viewport(currentViewport);
        if (maximumViewport.contains(f, f2)) {
            float width = currentViewport.width();
            float height = currentViewport.height();
            float max = Math.max(maximumViewport.left, Math.min(f - (width / 2.0f), maximumViewport.right - width));
            float max2 = Math.max(maximumViewport.bottom + height, Math.min(f2 + (height / 2.0f), maximumViewport.f12top));
            viewport.set(max, max2, width + max, max2 - height);
        }
        return viewport;
    }

    @Override // com.example.linechartlibrary.Chart
    public boolean isValueTouchEnabled() {
        return this.touchHandler.isValueTouchEnabled();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setValueTouchEnabled(boolean z) {
        this.touchHandler.setValueTouchEnabled(z);
    }

    @Override // com.example.linechartlibrary.Chart
    public ZoomType getZoomType() {
        return this.touchHandler.getZoomType();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setZoomType(ZoomType zoomType) {
        this.touchHandler.setZoomType(zoomType);
    }

    @Override // com.example.linechartlibrary.Chart
    public float getMaxZoom() {
        return this.chartComputator.getMaxZoom();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setMaxZoom(float f) {
        this.chartComputator.setMaxZoom(f);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public float getZoomLevel() {
        Viewport maximumViewport = getMaximumViewport();
        Viewport currentViewport = getCurrentViewport();
        return Math.max(maximumViewport.width() / currentViewport.width(), maximumViewport.height() / currentViewport.height());
    }

    @Override // com.example.linechartlibrary.Chart
    public void setZoomLevel(float f, float f2, float f3) {
        setCurrentViewport(computeZoomViewport(f, f2, f3));
    }

    @Override // com.example.linechartlibrary.Chart
    public void setZoomLevelWithAnimation(float f, float f2, float f3) {
        setCurrentViewportWithAnimation(computeZoomViewport(f, f2, f3));
    }

    private Viewport computeZoomViewport(float f, float f2, float f3) {
        Viewport maximumViewport = getMaximumViewport();
        Viewport viewport = new Viewport(getMaximumViewport());
        if (maximumViewport.contains(f, f2)) {
            if (f3 < 1.0f) {
                f3 = 1.0f;
            } else if (f3 > getMaxZoom()) {
                f3 = getMaxZoom();
            }
            float width = viewport.width() / f3;
            float height = viewport.height() / f3;
            float f4 = width / 2.0f;
            float f5 = height / 2.0f;
            float f6 = f - f4;
            float f7 = f + f4;
            float f8 = f2 + f5;
            float f9 = f2 - f5;
            if (f6 < maximumViewport.left) {
                f6 = maximumViewport.left;
                f7 = f6 + width;
            } else if (f7 > maximumViewport.right) {
                f7 = maximumViewport.right;
                f6 = f7 - width;
            }
            if (f8 > maximumViewport.f12top) {
                f8 = maximumViewport.f12top;
                f9 = f8 - height;
            } else if (f9 < maximumViewport.bottom) {
                f9 = maximumViewport.bottom;
                f8 = f9 + height;
            }
            ZoomType zoomType = getZoomType();
            if (ZoomType.HORIZONTAL_AND_VERTICAL == zoomType) {
                viewport.set(f6, f8, f7, f9);
            } else if (ZoomType.HORIZONTAL == zoomType) {
                viewport.left = f6;
                viewport.right = f7;
            } else if (ZoomType.VERTICAL == zoomType) {
                viewport.f12top = f8;
                viewport.bottom = f9;
            }
        }
        return viewport;
    }

    @Override // com.example.linechartlibrary.Chart
    public Viewport getMaximumViewport() {
        return this.chartRenderer.getMaximumViewport();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setMaximumViewport(Viewport viewport) {
        this.chartRenderer.setMaximumViewport(viewport);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public void setCurrentViewportWithAnimation(Viewport viewport) {
        if (viewport != null) {
            this.viewportAnimator.cancelAnimation();
            this.viewportAnimator.startAnimation(getCurrentViewport(), viewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public void setCurrentViewportWithAnimation(Viewport viewport, long j) {
        if (viewport != null) {
            this.viewportAnimator.cancelAnimation();
            this.viewportAnimator.startAnimation(getCurrentViewport(), viewport, j);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public Viewport getCurrentViewport() {
        return getChartRenderer().getCurrentViewport();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setCurrentViewport(Viewport viewport) {
        if (viewport != null) {
            this.chartRenderer.setCurrentViewport(viewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public void resetViewports() {
        this.chartRenderer.setMaximumViewport(null);
        this.chartRenderer.setCurrentViewport(null);
    }

    @Override // com.example.linechartlibrary.Chart
    public boolean isViewportCalculationEnabled() {
        return this.chartRenderer.isViewportCalculationEnabled();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setViewportCalculationEnabled(boolean z) {
        this.chartRenderer.setViewportCalculationEnabled(z);
    }

    @Override // com.example.linechartlibrary.Chart
    public boolean isValueSelectionEnabled() {
        return this.touchHandler.isValueSelectionEnabled();
    }

    @Override // com.example.linechartlibrary.Chart
    public void setValueSelectionEnabled(boolean z) {
        this.touchHandler.setValueSelectionEnabled(z);
    }

    @Override // com.example.linechartlibrary.Chart
    public void selectValue(SelectedValue selectedValue) {
        this.chartRenderer.selectValue(selectedValue);
        callTouchListener();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // com.example.linechartlibrary.Chart
    public SelectedValue getSelectedValue() {
        return this.chartRenderer.getSelectedValue();
    }

    @Override // com.example.linechartlibrary.Chart
    public boolean isContainerScrollEnabled() {
        return this.isContainerScrollEnabled;
    }

    @Override // com.example.linechartlibrary.Chart
    public void setContainerScrollEnabled(boolean z, ContainerScrollType containerScrollType) {
        this.isContainerScrollEnabled = z;
        this.containerScrollType = containerScrollType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onChartDataChange() {
        this.chartComputator.resetContentRect();
        this.chartRenderer.onChartDataChanged();
        this.axesRenderer.onChartDataChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    protected void resetRendererAndTouchHandler() {
        this.chartRenderer.resetRenderer();
        this.axesRenderer.resetRenderer();
        this.touchHandler.resetTouchHandler();
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i) {
        if (getZoomLevel() <= 1.0d) {
            return false;
        }
        Viewport currentViewport = getCurrentViewport();
        Viewport maximumViewport = getMaximumViewport();
        return i < 0 ? currentViewport.left > maximumViewport.left : currentViewport.right < maximumViewport.right;
    }
}
