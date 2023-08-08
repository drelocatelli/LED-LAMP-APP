package com.example.linechartlibrary;

import android.graphics.PathEffect;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Line {
    private static final int DEFAULT_AREA_TRANSPARENCY = 64;
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 3;
    private static final int DEFAULT_POINT_RADIUS_DP = 6;
    public static final int UNINITIALIZED = 0;
    private int areaTransparency;
    private int color;
    private int darkenColor;
    private LineChartValueFormatter formatter;
    private boolean hasGradientToTransparent;
    private boolean hasLabels;
    private boolean hasLabelsOnlyForSelected;
    private boolean hasLines;
    private boolean hasPoints;
    private boolean isCubic;
    private boolean isFilled;
    private boolean isSquare;
    private PathEffect pathEffect;
    private int pointColor;
    private int pointRadius;
    private ValueShape shape;
    private int strokeWidth;
    private List<PointValue> values;

    public Line() {
        this.color = ChartUtils.DEFAULT_COLOR;
        this.pointColor = 0;
        this.darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
        this.areaTransparency = 64;
        this.strokeWidth = 3;
        this.pointRadius = 6;
        this.hasGradientToTransparent = false;
        this.hasPoints = true;
        this.hasLines = true;
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.isCubic = false;
        this.isSquare = false;
        this.isFilled = false;
        this.shape = ValueShape.CIRCLE;
        this.formatter = new SimpleLineChartValueFormatter();
        this.values = new ArrayList();
    }

    public Line(List<PointValue> list) {
        this.color = ChartUtils.DEFAULT_COLOR;
        this.pointColor = 0;
        this.darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
        this.areaTransparency = 64;
        this.strokeWidth = 3;
        this.pointRadius = 6;
        this.hasGradientToTransparent = false;
        this.hasPoints = true;
        this.hasLines = true;
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.isCubic = false;
        this.isSquare = false;
        this.isFilled = false;
        this.shape = ValueShape.CIRCLE;
        this.formatter = new SimpleLineChartValueFormatter();
        this.values = new ArrayList();
        setValues(list);
    }

    public Line(Line line) {
        this.color = ChartUtils.DEFAULT_COLOR;
        this.pointColor = 0;
        this.darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
        this.areaTransparency = 64;
        this.strokeWidth = 3;
        this.pointRadius = 6;
        this.hasGradientToTransparent = false;
        this.hasPoints = true;
        this.hasLines = true;
        this.hasLabels = false;
        this.hasLabelsOnlyForSelected = false;
        this.isCubic = false;
        this.isSquare = false;
        this.isFilled = false;
        this.shape = ValueShape.CIRCLE;
        this.formatter = new SimpleLineChartValueFormatter();
        this.values = new ArrayList();
        this.color = line.color;
        this.pointColor = line.pointColor;
        this.darkenColor = line.darkenColor;
        this.areaTransparency = line.areaTransparency;
        this.strokeWidth = line.strokeWidth;
        this.pointRadius = line.pointRadius;
        this.hasGradientToTransparent = line.hasGradientToTransparent;
        this.hasPoints = line.hasPoints;
        this.hasLines = line.hasLines;
        this.hasLabels = line.hasLabels;
        this.hasLabelsOnlyForSelected = line.hasLabelsOnlyForSelected;
        this.isSquare = line.isSquare;
        this.isCubic = line.isCubic;
        this.isFilled = line.isFilled;
        this.shape = line.shape;
        this.pathEffect = line.pathEffect;
        this.formatter = line.formatter;
        for (PointValue pointValue : line.values) {
            this.values.add(new PointValue(pointValue));
        }
    }

    public void update(float f) {
        for (PointValue pointValue : this.values) {
            pointValue.update(f);
        }
    }

    public void finish() {
        for (PointValue pointValue : this.values) {
            pointValue.finish();
        }
    }

    public List<PointValue> getValues() {
        return this.values;
    }

    public void setValues(List<PointValue> list) {
        if (list == null) {
            this.values = new ArrayList();
        } else {
            this.values = list;
        }
    }

    public int getColor() {
        return this.color;
    }

    public Line setColor(int i) {
        this.color = i;
        if (this.pointColor == 0) {
            this.darkenColor = ChartUtils.darkenColor(i);
        }
        return this;
    }

    public int getPointColor() {
        int i = this.pointColor;
        return i == 0 ? this.color : i;
    }

    public Line setPointColor(int i) {
        this.pointColor = i;
        if (i == 0) {
            this.darkenColor = ChartUtils.darkenColor(this.color);
        } else {
            this.darkenColor = ChartUtils.darkenColor(i);
        }
        return this;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    public int getAreaTransparency() {
        return this.areaTransparency;
    }

    public Line setAreaTransparency(int i) {
        this.areaTransparency = i;
        return this;
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public Line setStrokeWidth(int i) {
        this.strokeWidth = i;
        return this;
    }

    public boolean hasPoints() {
        return this.hasPoints;
    }

    public Line setHasPoints(boolean z) {
        this.hasPoints = z;
        return this;
    }

    public boolean hasLines() {
        return this.hasLines;
    }

    public Line setHasLines(boolean z) {
        this.hasLines = z;
        return this;
    }

    public boolean hasLabels() {
        return this.hasLabels;
    }

    public Line setHasLabels(boolean z) {
        this.hasLabels = z;
        if (z) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public Line setHasLabelsOnlyForSelected(boolean z) {
        this.hasLabelsOnlyForSelected = z;
        if (z) {
            this.hasLabels = false;
        }
        return this;
    }

    public int getPointRadius() {
        return this.pointRadius;
    }

    public Line setPointRadius(int i) {
        this.pointRadius = i;
        return this;
    }

    public boolean getGradientToTransparent() {
        return this.hasGradientToTransparent;
    }

    public Line setHasGradientToTransparent(boolean z) {
        this.hasGradientToTransparent = z;
        return this;
    }

    public boolean isCubic() {
        return this.isCubic;
    }

    public Line setCubic(boolean z) {
        this.isCubic = z;
        if (this.isSquare) {
            setSquare(false);
        }
        return this;
    }

    public boolean isSquare() {
        return this.isSquare;
    }

    public Line setSquare(boolean z) {
        this.isSquare = z;
        if (this.isCubic) {
            setCubic(false);
        }
        return this;
    }

    public boolean isFilled() {
        return this.isFilled;
    }

    public Line setFilled(boolean z) {
        this.isFilled = z;
        return this;
    }

    public ValueShape getShape() {
        return this.shape;
    }

    public Line setShape(ValueShape valueShape) {
        this.shape = valueShape;
        return this;
    }

    public PathEffect getPathEffect() {
        return this.pathEffect;
    }

    public void setPathEffect(PathEffect pathEffect) {
        this.pathEffect = pathEffect;
    }

    public LineChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public Line setFormatter(LineChartValueFormatter lineChartValueFormatter) {
        if (lineChartValueFormatter != null) {
            this.formatter = lineChartValueFormatter;
        }
        return this;
    }
}
