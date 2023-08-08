package com.example.linechartlibrary;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class ChartZoomer {
    public static final float ZOOM_AMOUNT = 0.25f;
    private ZoomType zoomType;
    private ZoomerCompat zoomer;
    private PointF zoomFocalPoint = new PointF();
    private PointF viewportFocus = new PointF();
    private Viewport scrollerStartViewport = new Viewport();

    public ChartZoomer(Context context, ZoomType zoomType) {
        this.zoomer = new ZoomerCompat(context);
        this.zoomType = zoomType;
    }

    public boolean startZoom(MotionEvent motionEvent, ChartComputator chartComputator) {
        this.zoomer.forceFinished(true);
        this.scrollerStartViewport.set(chartComputator.getCurrentViewport());
        if (chartComputator.rawPixelsToDataPoint(motionEvent.getX(), motionEvent.getY(), this.zoomFocalPoint)) {
            this.zoomer.startZoom(0.25f);
            return true;
        }
        return false;
    }

    public boolean computeZoom(ChartComputator chartComputator) {
        if (this.zoomer.computeZoom()) {
            float currZoom = (1.0f - this.zoomer.getCurrZoom()) * this.scrollerStartViewport.width();
            float currZoom2 = (1.0f - this.zoomer.getCurrZoom()) * this.scrollerStartViewport.height();
            float width = (this.zoomFocalPoint.x - this.scrollerStartViewport.left) / this.scrollerStartViewport.width();
            float height = (this.zoomFocalPoint.y - this.scrollerStartViewport.bottom) / this.scrollerStartViewport.height();
            setCurrentViewport(chartComputator, this.zoomFocalPoint.x - (currZoom * width), this.zoomFocalPoint.y + ((1.0f - height) * currZoom2), this.zoomFocalPoint.x + (currZoom * (1.0f - width)), this.zoomFocalPoint.y - (currZoom2 * height));
            return true;
        }
        return false;
    }

    public boolean scale(ChartComputator chartComputator, float f, float f2, float f3) {
        float width = chartComputator.getCurrentViewport().width() * f3;
        float height = f3 * chartComputator.getCurrentViewport().height();
        if (chartComputator.rawPixelsToDataPoint(f, f2, this.viewportFocus)) {
            float width2 = this.viewportFocus.x - ((f - chartComputator.getContentRectMinusAllMargins().left) * (width / chartComputator.getContentRectMinusAllMargins().width()));
            float height2 = this.viewportFocus.y + ((f2 - chartComputator.getContentRectMinusAllMargins().top) * (height / chartComputator.getContentRectMinusAllMargins().height()));
            setCurrentViewport(chartComputator, width2, height2, width2 + width, height2 - height);
            return true;
        }
        return false;
    }

    private void setCurrentViewport(ChartComputator chartComputator, float f, float f2, float f3, float f4) {
        Viewport currentViewport = chartComputator.getCurrentViewport();
        if (ZoomType.HORIZONTAL_AND_VERTICAL == this.zoomType) {
            chartComputator.setCurrentViewport(f, f2, f3, f4);
        } else if (ZoomType.HORIZONTAL == this.zoomType) {
            chartComputator.setCurrentViewport(f, currentViewport.f12top, f3, currentViewport.bottom);
        } else if (ZoomType.VERTICAL == this.zoomType) {
            chartComputator.setCurrentViewport(currentViewport.left, f2, currentViewport.right, f4);
        }
    }

    public ZoomType getZoomType() {
        return this.zoomType;
    }

    public void setZoomType(ZoomType zoomType) {
        this.zoomType = zoomType;
    }
}
