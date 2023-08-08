package com.example.linechartlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import androidx.core.view.ViewCompat;
import com.example.linechartlibrary.SelectedValue;

/* loaded from: classes.dex */
public class LineChartRenderer extends AbstractChartRenderer {
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 3;
    private static final int DEFAULT_TOUCH_TOLERANCE_MARGIN_DP = 4;
    private static final float LINE_SMOOTHNESS = 0.16f;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private float baseValue;
    private int checkPrecision;
    private LineChartDataProvider dataProvider;
    private Paint linePaint;
    private Path path;
    private Paint pointPaint;
    private Bitmap softwareBitmap;
    private Canvas softwareCanvas;
    private Viewport tempMaximumViewport;
    private int touchToleranceMargin;

    public LineChartRenderer(Context context, Chart chart, LineChartDataProvider lineChartDataProvider) {
        super(context, chart);
        this.path = new Path();
        this.linePaint = new Paint();
        this.pointPaint = new Paint();
        this.softwareCanvas = new Canvas();
        this.tempMaximumViewport = new Viewport();
        this.dataProvider = lineChartDataProvider;
        this.touchToleranceMargin = ChartUtils.dp2px(this.density, 4);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.linePaint.setStrokeWidth(ChartUtils.dp2px(this.density, 3));
        this.pointPaint.setAntiAlias(true);
        this.pointPaint.setStyle(Paint.Style.FILL);
        this.checkPrecision = ChartUtils.dp2px(this.density, 2);
    }

    @Override // com.example.linechartlibrary.ChartRenderer
    public void onChartSizeChanged() {
        int calculateContentRectInternalMargin = calculateContentRectInternalMargin();
        this.computator.insetContentRectByInternalMargins(calculateContentRectInternalMargin, calculateContentRectInternalMargin, calculateContentRectInternalMargin, calculateContentRectInternalMargin);
        if (this.computator.getChartWidth() <= 0 || this.computator.getChartHeight() <= 0) {
            return;
        }
        Bitmap createBitmap = Bitmap.createBitmap(this.computator.getChartWidth(), this.computator.getChartHeight(), Bitmap.Config.ARGB_4444);
        this.softwareBitmap = createBitmap;
        this.softwareCanvas.setBitmap(createBitmap);
    }

    @Override // com.example.linechartlibrary.AbstractChartRenderer, com.example.linechartlibrary.ChartRenderer
    public void onChartDataChanged() {
        super.onChartDataChanged();
        int calculateContentRectInternalMargin = calculateContentRectInternalMargin();
        this.computator.insetContentRectByInternalMargins(calculateContentRectInternalMargin, calculateContentRectInternalMargin, calculateContentRectInternalMargin, calculateContentRectInternalMargin);
        this.baseValue = this.dataProvider.getLineChartData().getBaseValue();
        onChartViewportChanged();
    }

    @Override // com.example.linechartlibrary.ChartRenderer
    public void onChartViewportChanged() {
        if (this.isViewportCalculationEnabled) {
            calculateMaxViewport();
            this.computator.setMaxViewport(this.tempMaximumViewport);
            this.computator.setCurrentViewport(this.computator.getMaximumViewport());
        }
    }

    @Override // com.example.linechartlibrary.ChartRenderer
    public void draw(Canvas canvas) {
        Canvas canvas2;
        LineChartData lineChartData = this.dataProvider.getLineChartData();
        if (this.softwareBitmap != null) {
            canvas2 = this.softwareCanvas;
            canvas2.drawColor(0, PorterDuff.Mode.CLEAR);
        } else {
            canvas2 = canvas;
        }
        for (Line line : lineChartData.getLines()) {
            if (line.hasLines()) {
                if (line.isCubic()) {
                    drawSmoothPath(canvas2, line);
                } else if (line.isSquare()) {
                    drawSquarePath(canvas2, line);
                } else {
                    drawPath(canvas2, line);
                }
            }
        }
        Bitmap bitmap = this.softwareBitmap;
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    @Override // com.example.linechartlibrary.ChartRenderer
    public void drawUnclipped(Canvas canvas) {
        int i = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                drawPoints(canvas, line, i, 0);
            }
            i++;
        }
        if (isTouched()) {
            highlightPoints(canvas);
        }
    }

    private boolean checkIfShouldDrawPoints(Line line) {
        return line.hasPoints() || line.getValues().size() == 1;
    }

    @Override // com.example.linechartlibrary.ChartRenderer
    public boolean checkTouch(float f, float f2) {
        this.selectedValue.clear();
        int i = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                int dp2px = ChartUtils.dp2px(this.density, line.getPointRadius());
                int i2 = 0;
                for (PointValue pointValue : line.getValues()) {
                    if (isInArea(this.computator.computeRawX(pointValue.getX()), this.computator.computeRawY(pointValue.getY()), f, f2, this.touchToleranceMargin + dp2px)) {
                        this.selectedValue.set(i, i2, SelectedValue.SelectedValueType.LINE);
                    }
                    i2++;
                }
            }
            i++;
        }
        return isTouched();
    }

    private void calculateMaxViewport() {
        this.tempMaximumViewport.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE);
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            for (PointValue pointValue : line.getValues()) {
                if (pointValue.getX() < this.tempMaximumViewport.left) {
                    this.tempMaximumViewport.left = pointValue.getX();
                }
                if (pointValue.getX() > this.tempMaximumViewport.right) {
                    this.tempMaximumViewport.right = pointValue.getX();
                }
                if (pointValue.getY() < this.tempMaximumViewport.bottom) {
                    this.tempMaximumViewport.bottom = pointValue.getY();
                }
                if (pointValue.getY() > this.tempMaximumViewport.f12top) {
                    this.tempMaximumViewport.f12top = pointValue.getY();
                }
            }
        }
    }

    private int calculateContentRectInternalMargin() {
        int pointRadius;
        int i = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line) && (pointRadius = line.getPointRadius() + 4) > i) {
                i = pointRadius;
            }
        }
        return ChartUtils.dp2px(this.density, i);
    }

    private void drawPath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int i = 0;
        for (PointValue pointValue : line.getValues()) {
            float computeRawX = this.computator.computeRawX(pointValue.getX());
            float computeRawY = this.computator.computeRawY(pointValue.getY());
            if (i == 0) {
                this.path.moveTo(computeRawX, computeRawY);
            } else {
                this.path.lineTo(computeRawX, computeRawY);
            }
            i++;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void drawSquarePath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int i = 0;
        float f = 0.0f;
        for (PointValue pointValue : line.getValues()) {
            float computeRawX = this.computator.computeRawX(pointValue.getX());
            float computeRawY = this.computator.computeRawY(pointValue.getY());
            if (i == 0) {
                this.path.moveTo(computeRawX, computeRawY);
            } else {
                this.path.lineTo(computeRawX, f);
                this.path.lineTo(computeRawX, computeRawY);
            }
            i++;
            f = computeRawY;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void drawSmoothPath(Canvas canvas, Line line) {
        float f;
        float f2;
        prepareLinePaint(line);
        int size = line.getValues().size();
        float f3 = Float.NaN;
        float f4 = Float.NaN;
        float f5 = Float.NaN;
        float f6 = Float.NaN;
        float f7 = Float.NaN;
        float f8 = Float.NaN;
        int i = 0;
        while (i < size) {
            if (Float.isNaN(f3)) {
                PointValue pointValue = line.getValues().get(i);
                float computeRawX = this.computator.computeRawX(pointValue.getX());
                f5 = this.computator.computeRawY(pointValue.getY());
                f3 = computeRawX;
            }
            if (Float.isNaN(f4)) {
                if (i > 0) {
                    PointValue pointValue2 = line.getValues().get(i - 1);
                    float computeRawX2 = this.computator.computeRawX(pointValue2.getX());
                    f7 = this.computator.computeRawY(pointValue2.getY());
                    f4 = computeRawX2;
                } else {
                    f4 = f3;
                    f7 = f5;
                }
            }
            if (Float.isNaN(f6)) {
                if (i > 1) {
                    PointValue pointValue3 = line.getValues().get(i - 2);
                    float computeRawX3 = this.computator.computeRawX(pointValue3.getX());
                    f8 = this.computator.computeRawY(pointValue3.getY());
                    f6 = computeRawX3;
                } else {
                    f6 = f4;
                    f8 = f7;
                }
            }
            if (i < size - 1) {
                PointValue pointValue4 = line.getValues().get(i + 1);
                float computeRawX4 = this.computator.computeRawX(pointValue4.getX());
                f2 = this.computator.computeRawY(pointValue4.getY());
                f = computeRawX4;
            } else {
                f = f3;
                f2 = f5;
            }
            if (i == 0) {
                this.path.moveTo(f3, f5);
            } else {
                this.path.cubicTo(((f3 - f6) * LINE_SMOOTHNESS) + f4, ((f5 - f8) * LINE_SMOOTHNESS) + f7, f3 - ((f - f4) * LINE_SMOOTHNESS), f5 - ((f2 - f7) * LINE_SMOOTHNESS), f3, f5);
            }
            i++;
            f6 = f4;
            f8 = f7;
            f4 = f3;
            f7 = f5;
            f3 = f;
            f5 = f2;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void prepareLinePaint(Line line) {
        this.linePaint.setStrokeWidth(ChartUtils.dp2px(this.density, line.getStrokeWidth()));
        this.linePaint.setColor(line.getColor());
        this.linePaint.setPathEffect(line.getPathEffect());
        this.linePaint.setShader(null);
    }

    private void drawPoints(Canvas canvas, Line line, int i, int i2) {
        this.pointPaint.setColor(line.getPointColor());
        int i3 = 0;
        for (PointValue pointValue : line.getValues()) {
            int dp2px = ChartUtils.dp2px(this.density, line.getPointRadius());
            float computeRawX = this.computator.computeRawX(pointValue.getX());
            float computeRawY = this.computator.computeRawY(pointValue.getY());
            if (this.computator.isWithinContentRect(computeRawX, computeRawY, this.checkPrecision)) {
                if (i2 == 0) {
                    drawPoint(canvas, line, pointValue, computeRawX, computeRawY, dp2px);
                    if (line.hasLabels()) {
                        drawLabel(canvas, line, pointValue, computeRawX, computeRawY, dp2px + this.labelOffset);
                    }
                } else if (1 == i2) {
                    highlightPoint(canvas, line, pointValue, computeRawX, computeRawY, i, i3);
                } else {
                    throw new IllegalStateException("Cannot process points in mode: " + i2);
                }
            }
            i3++;
        }
    }

    private void drawPoint(Canvas canvas, Line line, PointValue pointValue, float f, float f2, float f3) {
        if (ValueShape.SQUARE.equals(line.getShape())) {
            canvas.drawRect(f - f3, f2 - f3, f + f3, f2 + f3, this.pointPaint);
        } else if (ValueShape.CIRCLE.equals(line.getShape())) {
            canvas.drawCircle(f, f2, f3, this.pointPaint);
        } else if (ValueShape.DIAMOND.equals(line.getShape())) {
            canvas.save();
            canvas.rotate(45.0f, f, f2);
            canvas.drawRect(f - f3, f2 - f3, f + f3, f2 + f3, this.pointPaint);
            canvas.restore();
        } else {
            throw new IllegalArgumentException("Invalid point shape: " + line.getShape());
        }
    }

    private void highlightPoints(Canvas canvas) {
        int firstIndex = this.selectedValue.getFirstIndex();
        drawPoints(canvas, this.dataProvider.getLineChartData().getLines().get(firstIndex), firstIndex, 1);
    }

    private void highlightPoint(Canvas canvas, Line line, PointValue pointValue, float f, float f2, int i, int i2) {
        if (this.selectedValue.getFirstIndex() == i && this.selectedValue.getSecondIndex() == i2) {
            int dp2px = ChartUtils.dp2px(this.density, line.getPointRadius());
            this.pointPaint.setColor(line.getDarkenColor());
            drawPoint(canvas, line, pointValue, f, f2, this.touchToleranceMargin + dp2px);
            if (line.hasLabels() || line.hasLabelsOnlyForSelected()) {
                drawLabel(canvas, line, pointValue, f, f2, dp2px + this.labelOffset);
            }
        }
    }

    private void drawLabel(Canvas canvas, Line line, PointValue pointValue, float f, float f2, float f3) {
        float f4;
        float f5;
        this.labelPaint.setColor(line.getDarkenColor());
        this.labelPaint.setTextSize(pointValue.getLabelTextSize());
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        int formatChartValue = line.getFormatter().formatChartValue(this.labelBuffer, pointValue);
        if (formatChartValue == 0) {
            return;
        }
        float measureText = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - formatChartValue, formatChartValue);
        int abs = Math.abs(this.fontMetrics.ascent);
        float f6 = measureText / 2.0f;
        float f7 = (f - f6) - this.labelMargin;
        float f8 = f6 + f + this.labelMargin;
        if (pointValue.getY() >= this.baseValue) {
            f5 = f2 - f3;
            f4 = (f5 - abs) - (this.labelMargin * 2);
        } else {
            f4 = f2 + f3;
            f5 = abs + f4 + (this.labelMargin * 2);
        }
        if (f4 < contentRectMinusAllMargins.top) {
            f4 = f2 + f3;
            f5 = abs + f4 + (this.labelMargin * 2);
        }
        if (f5 > contentRectMinusAllMargins.bottom) {
            f5 = f2 - f3;
            f4 = (f5 - abs) - (this.labelMargin * 2);
        }
        if (f7 < contentRectMinusAllMargins.left) {
            f8 = f + measureText + (this.labelMargin * 2);
            f7 = f;
        }
        if (f8 > contentRectMinusAllMargins.right) {
            f7 = (f - measureText) - (this.labelMargin * 2);
        } else {
            f = f8;
        }
        this.labelBackgroundRect.set(f7, f4, f, f5);
        drawLabelTextAndBackground(canvas, this.labelBuffer, this.labelBuffer.length - formatChartValue, formatChartValue, 0);
    }

    private void drawArea(Canvas canvas, Line line) {
        int size = line.getValues().size();
        if (size < 2) {
            return;
        }
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        float min = Math.min(contentRectMinusAllMargins.bottom, Math.max(this.computator.computeRawY(this.baseValue), contentRectMinusAllMargins.top));
        float max = Math.max(this.computator.computeRawX(line.getValues().get(0).getX()), contentRectMinusAllMargins.left);
        this.path.lineTo(Math.min(this.computator.computeRawX(line.getValues().get(size - 1).getX()), contentRectMinusAllMargins.right), min);
        this.path.lineTo(max, min);
        this.path.close();
        this.linePaint.setStyle(Paint.Style.FILL);
        this.linePaint.setAlpha(line.getAreaTransparency());
        this.linePaint.setShader(line.getGradientToTransparent() ? new LinearGradient(0.0f, 0.0f, 0.0f, canvas.getHeight(), line.getColor(), line.getColor() & ViewCompat.MEASURED_SIZE_MASK, Shader.TileMode.MIRROR) : null);
        canvas.drawPath(this.path, this.linePaint);
        this.linePaint.setStyle(Paint.Style.STROKE);
    }

    private boolean isInArea(float f, float f2, float f3, float f4, float f5) {
        return Math.pow((double) (f3 - f), 2.0d) + Math.pow((double) (f4 - f2), 2.0d) <= Math.pow((double) f5, 2.0d) * 2.0d;
    }
}
