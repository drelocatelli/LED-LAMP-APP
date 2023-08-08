package com.example.linechartlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import java.lang.reflect.Array;

/* loaded from: classes.dex */
public class AxesRenderer {
    private static final int BOTTOM = 3;
    private static final int DEFAULT_AXIS_MARGIN_DP = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final String TAG = "AxesRenderer";
    private static final int TOP = 0;
    private static final char[] labelWidthChars = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
    private int axisMargin;
    private Chart chart;
    private ChartComputator computator;
    private float density;
    private float scaledDensity;
    private Paint[] labelPaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private Paint[] namePaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private Paint[] linePaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private float[] nameBaselineTab = new float[4];
    private float[] labelBaselineTab = new float[4];
    private float[] separationLineTab = new float[4];
    private int[] labelWidthTab = new int[4];
    private int[] labelTextAscentTab = new int[4];
    private int[] labelTextDescentTab = new int[4];
    private int[] labelDimensionForMarginsTab = new int[4];
    private int[] labelDimensionForStepsTab = new int[4];
    private int[] tiltedLabelXTranslation = new int[4];
    private int[] tiltedLabelYTranslation = new int[4];
    private Paint.FontMetricsInt[] fontMetricsTab = {new Paint.FontMetricsInt(), new Paint.FontMetricsInt(), new Paint.FontMetricsInt(), new Paint.FontMetricsInt()};
    private char[] labelBuffer = new char[64];
    private int[] valuesToDrawNumTab = new int[4];
    private float[][] rawValuesTab = (float[][]) Array.newInstance(float.class, 4, 0);
    private float[][] autoValuesToDrawTab = (float[][]) Array.newInstance(float.class, 4, 0);
    private AxisValue[][] valuesToDrawTab = (AxisValue[][]) Array.newInstance(AxisValue.class, 4, 0);
    private float[][] linesDrawBufferTab = (float[][]) Array.newInstance(float.class, 4, 0);
    private AxisAutoValues[] autoValuesBufferTab = {new AxisAutoValues(), new AxisAutoValues(), new AxisAutoValues(), new AxisAutoValues()};

    public AxesRenderer(Context context, Chart chart) {
        this.chart = chart;
        this.computator = chart.getChartComputator();
        this.density = context.getResources().getDisplayMetrics().density;
        this.scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.axisMargin = ChartUtils.dp2px(this.density, 0);
        for (int i = 0; i < 4; i++) {
            this.labelPaintTab[i].setStyle(Paint.Style.FILL);
            this.labelPaintTab[i].setAntiAlias(true);
            this.namePaintTab[i].setStyle(Paint.Style.FILL);
            this.namePaintTab[i].setAntiAlias(true);
            this.linePaintTab[i].setStyle(Paint.Style.STROKE);
            this.linePaintTab[i].setAntiAlias(true);
        }
    }

    public void onChartSizeChanged() {
        onChartDataOrSizeChanged();
    }

    public void onChartDataChanged() {
        onChartDataOrSizeChanged();
    }

    private void onChartDataOrSizeChanged() {
        initAxis(this.chart.getChartData().getAxisXTop(), 0);
        initAxis(this.chart.getChartData().getAxisXBottom(), 3);
        initAxis(this.chart.getChartData().getAxisYLeft(), 1);
        initAxis(this.chart.getChartData().getAxisYRight(), 2);
    }

    public void resetRenderer() {
        this.computator = this.chart.getChartComputator();
    }

    private void initAxis(Axis axis, int i) {
        if (axis == null) {
            return;
        }
        initAxisAttributes(axis, i);
        initAxisMargin(axis, i);
        initAxisMeasurements(axis, i);
    }

    private void initAxisAttributes(Axis axis, int i) {
        initAxisPaints(axis, i);
        initAxisTextAlignment(axis, i);
        if (axis.hasTiltedLabels()) {
            initAxisDimensionForTiltedLabels(i);
            intiTiltedLabelsTranslation(axis, i);
            return;
        }
        initAxisDimension(i);
    }

    private void initAxisPaints(Axis axis, int i) {
        Typeface typeface = axis.getTypeface();
        if (typeface != null) {
            this.labelPaintTab[i].setTypeface(typeface);
            this.namePaintTab[i].setTypeface(typeface);
        }
        this.labelPaintTab[i].setColor(axis.getTextColor());
        this.labelPaintTab[i].setTextSize(ChartUtils.sp2px(this.scaledDensity, axis.getTextSize()));
        this.labelPaintTab[i].getFontMetricsInt(this.fontMetricsTab[i]);
        this.namePaintTab[i].setColor(axis.getTextColor());
        this.namePaintTab[i].setTextSize(ChartUtils.sp2px(this.scaledDensity, axis.getTextSize()));
        this.linePaintTab[i].setColor(axis.getLineColor());
        this.labelTextAscentTab[i] = Math.abs(this.fontMetricsTab[i].ascent);
        this.labelTextDescentTab[i] = Math.abs(this.fontMetricsTab[i].descent);
        this.labelWidthTab[i] = (int) this.labelPaintTab[i].measureText(labelWidthChars, 0, axis.getMaxLabelChars());
    }

    private void initAxisTextAlignment(Axis axis, int i) {
        this.namePaintTab[i].setTextAlign(Paint.Align.CENTER);
        if (i == 0 || 3 == i) {
            this.labelPaintTab[i].setTextAlign(Paint.Align.CENTER);
        } else if (1 == i) {
            if (axis.isInside()) {
                this.labelPaintTab[i].setTextAlign(Paint.Align.LEFT);
            } else {
                this.labelPaintTab[i].setTextAlign(Paint.Align.RIGHT);
            }
        } else if (2 == i) {
            if (axis.isInside()) {
                this.labelPaintTab[i].setTextAlign(Paint.Align.RIGHT);
            } else {
                this.labelPaintTab[i].setTextAlign(Paint.Align.LEFT);
            }
        }
    }

    private void initAxisDimensionForTiltedLabels(int i) {
        int sqrt = (int) Math.sqrt(Math.pow(this.labelTextAscentTab[i], 2.0d) / 2.0d);
        int[] iArr = this.labelDimensionForMarginsTab;
        iArr[i] = sqrt + ((int) Math.sqrt(Math.pow(this.labelWidthTab[i], 2.0d) / 2.0d));
        this.labelDimensionForStepsTab[i] = Math.round(iArr[i] * 0.75f);
    }

    private void initAxisDimension(int i) {
        if (1 == i || 2 == i) {
            this.labelDimensionForMarginsTab[i] = this.labelWidthTab[i];
            this.labelDimensionForStepsTab[i] = this.labelTextAscentTab[i];
        } else if (i == 0 || 3 == i) {
            this.labelDimensionForMarginsTab[i] = this.labelTextAscentTab[i] + this.labelTextDescentTab[i];
            this.labelDimensionForStepsTab[i] = this.labelWidthTab[i];
        }
    }

    private void intiTiltedLabelsTranslation(Axis axis, int i) {
        int i2;
        int i3;
        int i4;
        int sqrt = (int) Math.sqrt(Math.pow(this.labelWidthTab[i], 2.0d) / 2.0d);
        int sqrt2 = (int) Math.sqrt(Math.pow(this.labelTextAscentTab[i], 2.0d) / 2.0d);
        int i5 = 0;
        if (!axis.isInside()) {
            if (1 == i) {
                i4 = (-sqrt) / 2;
            } else if (2 != i) {
                if (i == 0) {
                    i4 = (-sqrt) / 2;
                } else {
                    if (3 == i) {
                        i2 = sqrt2 + (sqrt / 2);
                        i3 = this.labelTextAscentTab[i];
                        i5 = i2 - i3;
                    }
                    sqrt2 = 0;
                }
            }
            i5 = i4;
            sqrt2 = 0;
        } else if (1 != i) {
            if (2 == i) {
                i4 = (-sqrt) / 2;
            } else if (i == 0) {
                i2 = sqrt2 + (sqrt / 2);
                i3 = this.labelTextAscentTab[i];
                i5 = i2 - i3;
                sqrt2 = 0;
            } else {
                if (3 == i) {
                    i4 = (-sqrt) / 2;
                }
                sqrt2 = 0;
            }
            i5 = i4;
            sqrt2 = 0;
        }
        this.tiltedLabelXTranslation[i] = sqrt2;
        this.tiltedLabelYTranslation[i] = i5;
    }

    private void initAxisMargin(Axis axis, int i) {
        int i2 = 0;
        if (!axis.isInside() && (axis.isAutoGenerated() || !axis.getValues().isEmpty())) {
            i2 = 0 + this.axisMargin + this.labelDimensionForMarginsTab[i];
        }
        insetContentRectWithAxesMargins(i2 + getAxisNameMargin(axis, i), i);
    }

    private int getAxisNameMargin(Axis axis, int i) {
        if (TextUtils.isEmpty(axis.getName())) {
            return 0;
        }
        return this.labelTextAscentTab[i] + 0 + this.labelTextDescentTab[i] + this.axisMargin;
    }

    private void insetContentRectWithAxesMargins(int i, int i2) {
        if (1 == i2) {
            this.chart.getChartComputator().insetContentRect(i, 0, 0, 0);
        } else if (2 == i2) {
            this.chart.getChartComputator().insetContentRect(0, 0, i, 0);
        } else if (i2 == 0) {
            this.chart.getChartComputator().insetContentRect(0, i, 0, 0);
        } else if (3 == i2) {
            this.chart.getChartComputator().insetContentRect(0, 0, 0, i);
        }
    }

    private void initAxisMeasurements(Axis axis, int i) {
        if (1 == i) {
            if (axis.isInside()) {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAllMargins().left + this.axisMargin;
                this.nameBaselineTab[i] = (this.computator.getContentRectMinusAxesMargins().left - this.axisMargin) - this.labelTextDescentTab[i];
            } else {
                float[] fArr = this.labelBaselineTab;
                int i2 = this.computator.getContentRectMinusAxesMargins().left;
                int i3 = this.axisMargin;
                fArr[i] = i2 - i3;
                this.nameBaselineTab[i] = ((this.labelBaselineTab[i] - i3) - this.labelTextDescentTab[i]) - this.labelDimensionForMarginsTab[i];
            }
            this.separationLineTab[i] = this.computator.getContentRectMinusAllMargins().left;
        } else if (2 == i) {
            if (axis.isInside()) {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAllMargins().right - this.axisMargin;
                this.nameBaselineTab[i] = this.computator.getContentRectMinusAxesMargins().right + this.axisMargin + this.labelTextAscentTab[i];
            } else {
                float[] fArr2 = this.labelBaselineTab;
                int i4 = this.computator.getContentRectMinusAxesMargins().right;
                int i5 = this.axisMargin;
                fArr2[i] = i4 + i5;
                this.nameBaselineTab[i] = this.labelBaselineTab[i] + i5 + this.labelTextAscentTab[i] + this.labelDimensionForMarginsTab[i];
            }
            this.separationLineTab[i] = this.computator.getContentRectMinusAllMargins().right;
        } else if (3 == i) {
            if (axis.isInside()) {
                this.labelBaselineTab[i] = (this.computator.getContentRectMinusAllMargins().bottom - this.axisMargin) - this.labelTextDescentTab[i];
                this.nameBaselineTab[i] = this.computator.getContentRectMinusAxesMargins().bottom + this.axisMargin + this.labelTextAscentTab[i];
            } else {
                float[] fArr3 = this.labelBaselineTab;
                int i6 = this.computator.getContentRectMinusAxesMargins().bottom;
                int i7 = this.axisMargin;
                fArr3[i] = i6 + i7 + this.labelTextAscentTab[i];
                this.nameBaselineTab[i] = this.labelBaselineTab[i] + i7 + this.labelDimensionForMarginsTab[i];
            }
            this.separationLineTab[i] = this.computator.getContentRectMinusAllMargins().bottom;
        } else if (i == 0) {
            if (axis.isInside()) {
                this.labelBaselineTab[i] = this.computator.getContentRectMinusAllMargins().top + this.axisMargin + this.labelTextAscentTab[i];
                this.nameBaselineTab[i] = (this.computator.getContentRectMinusAxesMargins().top - this.axisMargin) - this.labelTextDescentTab[i];
            } else {
                float[] fArr4 = this.labelBaselineTab;
                int i8 = this.computator.getContentRectMinusAxesMargins().top;
                int i9 = this.axisMargin;
                fArr4[i] = (i8 - i9) - this.labelTextDescentTab[i];
                this.nameBaselineTab[i] = (this.labelBaselineTab[i] - i9) - this.labelDimensionForMarginsTab[i];
            }
            this.separationLineTab[i] = this.computator.getContentRectMinusAllMargins().top;
        } else {
            throw new IllegalArgumentException("Invalid axis position: " + i);
        }
    }

    public void drawInBackground(Canvas canvas) {
        Axis axisYLeft = this.chart.getChartData().getAxisYLeft();
        if (axisYLeft != null) {
            prepareAxisToDraw(axisYLeft, 1);
            drawAxisLines(canvas, axisYLeft, 1);
        }
        Axis axisYRight = this.chart.getChartData().getAxisYRight();
        if (axisYRight != null) {
            prepareAxisToDraw(axisYRight, 2);
        }
        Axis axisXBottom = this.chart.getChartData().getAxisXBottom();
        if (axisXBottom != null) {
            prepareAxisToDraw(axisXBottom, 3);
        }
        Axis axisXTop = this.chart.getChartData().getAxisXTop();
        if (axisXTop != null) {
            prepareAxisToDraw(axisXTop, 0);
        }
    }

    private void prepareAxisToDraw(Axis axis, int i) {
        if (axis.isAutoGenerated()) {
            prepareAutoGeneratedAxis(axis, i);
        } else {
            prepareCustomAxis(axis, i);
        }
    }

    public void drawInForeground(Canvas canvas) {
        Axis axisYLeft = this.chart.getChartData().getAxisYLeft();
        if (axisYLeft != null) {
            drawAxisLabelsAndName(canvas, axisYLeft, 1);
        }
        Axis axisYRight = this.chart.getChartData().getAxisYRight();
        if (axisYRight != null) {
            drawAxisLabelsAndName(canvas, axisYRight, 2);
        }
        Axis axisXBottom = this.chart.getChartData().getAxisXBottom();
        if (axisXBottom != null) {
            drawAxisLabelsAndName(canvas, axisXBottom, 3);
        }
        Axis axisXTop = this.chart.getChartData().getAxisXTop();
        if (axisXTop != null) {
            drawAxisLabelsAndName(canvas, axisXTop, 0);
        }
    }

    private void prepareCustomAxis(Axis axis, int i) {
        float width;
        float f;
        float f2;
        float computeRawX;
        Viewport maximumViewport = this.computator.getMaximumViewport();
        Viewport visibleViewport = this.computator.getVisibleViewport();
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        boolean isAxisVertical = isAxisVertical(i);
        if (isAxisVertical) {
            width = (maximumViewport.height() <= 0.0f || visibleViewport.height() <= 0.0f) ? 1.0f : contentRectMinusAllMargins.height() * (maximumViewport.height() / visibleViewport.height());
            f = visibleViewport.bottom;
            f2 = visibleViewport.f12top;
        } else {
            width = (maximumViewport.width() <= 0.0f || visibleViewport.width() <= 0.0f) ? 1.0f : contentRectMinusAllMargins.width() * (maximumViewport.width() / visibleViewport.width());
            f = visibleViewport.left;
            f2 = visibleViewport.right;
        }
        float f3 = f;
        float f4 = f2;
        float f5 = width != 0.0f ? width : 1.0f;
        double size = axis.getValues().size() * this.labelDimensionForStepsTab[i];
        Double.isNaN(size);
        double d = f5;
        Double.isNaN(d);
        int max = (int) Math.max(1.0d, Math.ceil((size * 1.5d) / d));
        if (axis.hasLines() && this.linesDrawBufferTab[i].length < axis.getValues().size() * 4) {
            this.linesDrawBufferTab[i] = new float[axis.getValues().size() * 4];
        }
        if (this.rawValuesTab[i].length < axis.getValues().size()) {
            this.rawValuesTab[i] = new float[axis.getValues().size()];
        }
        if (this.valuesToDrawTab[i].length < axis.getValues().size()) {
            this.valuesToDrawTab[i] = new AxisValue[axis.getValues().size()];
        }
        int i2 = 0;
        int i3 = 0;
        for (AxisValue axisValue : axis.getValues()) {
            float value = axisValue.getValue();
            if (value >= f3 && value <= f4) {
                if (i3 % max == 0) {
                    if (isAxisVertical) {
                        computeRawX = this.computator.computeRawY(value);
                    } else {
                        computeRawX = this.computator.computeRawX(value);
                    }
                    float f6 = computeRawX;
                    if (checkRawValue(contentRectMinusAllMargins, f6, axis.isInside(), i, isAxisVertical)) {
                        this.rawValuesTab[i][i2] = f6;
                        this.valuesToDrawTab[i][i2] = axisValue;
                        i2++;
                    }
                }
                i3++;
            }
        }
        this.valuesToDrawNumTab[i] = i2;
    }

    private void prepareAutoGeneratedAxis(Axis axis, int i) {
        float f;
        float f2;
        int width;
        float computeRawX;
        Viewport visibleViewport = this.computator.getVisibleViewport();
        Rect contentRectMinusAllMargins = this.computator.getContentRectMinusAllMargins();
        boolean isAxisVertical = isAxisVertical(i);
        if (isAxisVertical) {
            f = visibleViewport.bottom;
            f2 = visibleViewport.f12top;
            width = contentRectMinusAllMargins.height();
        } else {
            f = visibleViewport.left;
            f2 = visibleViewport.right;
            width = contentRectMinusAllMargins.width();
        }
        FloatUtils.computeAutoGeneratedAxisValues(f, f2, (Math.abs(width) / this.labelDimensionForStepsTab[i]) / 2, this.autoValuesBufferTab[i]);
        if (axis.hasLines() && this.linesDrawBufferTab[i].length < this.autoValuesBufferTab[i].valuesNumber * 4) {
            this.linesDrawBufferTab[i] = new float[this.autoValuesBufferTab[i].valuesNumber * 4];
        }
        if (this.rawValuesTab[i].length < this.autoValuesBufferTab[i].valuesNumber) {
            this.rawValuesTab[i] = new float[this.autoValuesBufferTab[i].valuesNumber];
        }
        if (this.autoValuesToDrawTab[i].length < this.autoValuesBufferTab[i].valuesNumber) {
            this.autoValuesToDrawTab[i] = new float[this.autoValuesBufferTab[i].valuesNumber];
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.autoValuesBufferTab[i].valuesNumber; i3++) {
            if (isAxisVertical) {
                computeRawX = this.computator.computeRawY(this.autoValuesBufferTab[i].values[i3]);
            } else {
                computeRawX = this.computator.computeRawX(this.autoValuesBufferTab[i].values[i3]);
            }
            float f3 = computeRawX;
            if (checkRawValue(contentRectMinusAllMargins, f3, axis.isInside(), i, isAxisVertical)) {
                this.rawValuesTab[i][i2] = f3;
                this.autoValuesToDrawTab[i][i2] = this.autoValuesBufferTab[i].values[i3];
                i2++;
            }
        }
        this.valuesToDrawNumTab[i] = i2;
    }

    private boolean checkRawValue(Rect rect, float f, boolean z, int i, boolean z2) {
        if (z) {
            if (z2) {
                int[] iArr = this.labelTextAscentTab;
                int i2 = iArr[3];
                int i3 = this.axisMargin;
                return f <= ((float) rect.bottom) - ((float) (i2 + i3)) && f >= ((float) rect.top) + ((float) (iArr[0] + i3));
            }
            float f2 = this.labelWidthTab[i] / 2;
            return f >= ((float) rect.left) + f2 && f <= ((float) rect.right) - f2;
        }
        return true;
    }

    private void drawAxisLines(Canvas canvas, Axis axis, int i) {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        Rect contentRectMinusAxesMargins = this.computator.getContentRectMinusAxesMargins();
        boolean isAxisVertical = isAxisVertical(i);
        float f8 = 0.0f;
        if (1 == i || 2 == i) {
            float f9 = this.separationLineTab[i];
            f = contentRectMinusAxesMargins.right;
            f2 = f9;
            f3 = f2;
            f4 = contentRectMinusAxesMargins.bottom;
            f5 = contentRectMinusAxesMargins.top;
            f8 = contentRectMinusAxesMargins.left;
            f6 = 0.0f;
            f7 = 0.0f;
        } else if (i == 0 || 3 == i) {
            float f10 = this.separationLineTab[i];
            f7 = contentRectMinusAxesMargins.top;
            f2 = contentRectMinusAxesMargins.left;
            f3 = contentRectMinusAxesMargins.right;
            f4 = f10;
            f5 = f4;
            f6 = contentRectMinusAxesMargins.bottom;
            f = 0.0f;
        } else {
            f = 0.0f;
            f6 = 0.0f;
            f7 = 0.0f;
            f2 = 0.0f;
            f4 = 0.0f;
            f3 = 0.0f;
            f5 = 0.0f;
        }
        if (axis.hasSeparationLine()) {
            this.labelPaintTab[i].setColor(axis.getHasSeparationLineColor());
            canvas.drawLine(f2, f4, f3, f5, this.labelPaintTab[i]);
        }
        this.labelPaintTab[i].setColor(axis.getTextColor());
        if (axis.hasLines()) {
            int i2 = 0;
            while (i2 < this.valuesToDrawNumTab[i]) {
                if (isAxisVertical) {
                    f6 = this.rawValuesTab[i][i2];
                    f7 = f6;
                } else {
                    f = this.rawValuesTab[i][i2];
                    f8 = f;
                }
                float[][] fArr = this.linesDrawBufferTab;
                int i3 = i2 * 4;
                fArr[i][i3 + 0] = f8;
                fArr[i][i3 + 1] = f7;
                fArr[i][i3 + 2] = f;
                fArr[i][i3 + 3] = f6;
                i2++;
            }
            canvas.drawLines(this.linesDrawBufferTab[i], 0, i2 * 4, this.linePaintTab[i]);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void drawAxisLabelsAndName(Canvas canvas, Axis axis, int i) {
        float f;
        int i2;
        int formatValueForManualAxis;
        boolean isAxisVertical = isAxisVertical(i);
        float f2 = 0.0f;
        if (1 == i || 2 == i) {
            f2 = this.labelBaselineTab[i];
        } else if (i == 0 || 3 == i) {
            f = this.labelBaselineTab[i];
            for (i2 = 0; i2 < this.valuesToDrawNumTab[i]; i2++) {
                if (axis.isAutoGenerated()) {
                    formatValueForManualAxis = axis.getFormatter().formatValueForAutoGeneratedAxis(this.labelBuffer, this.autoValuesToDrawTab[i][i2], this.autoValuesBufferTab[i].decimals);
                } else {
                    formatValueForManualAxis = axis.getFormatter().formatValueForManualAxis(this.labelBuffer, this.valuesToDrawTab[i][i2]);
                }
                int i3 = formatValueForManualAxis;
                if (isAxisVertical) {
                    f = this.rawValuesTab[i][i2];
                } else {
                    f2 = this.rawValuesTab[i][i2];
                }
                if (axis.hasTiltedLabels()) {
                    canvas.save();
                    canvas.translate(this.tiltedLabelXTranslation[i], this.tiltedLabelYTranslation[i]);
                    char[] cArr = this.labelBuffer;
                    canvas.drawText(cArr, cArr.length - i3, i3, f2, f, this.labelPaintTab[i]);
                    canvas.restore();
                } else {
                    char[] cArr2 = this.labelBuffer;
                    canvas.drawText(cArr2, cArr2.length - i3, i3, f2, f, this.labelPaintTab[i]);
                }
            }
            Rect contentRectMinusAxesMargins = this.computator.getContentRectMinusAxesMargins();
            if (TextUtils.isEmpty(axis.getName())) {
                if (isAxisVertical) {
                    canvas.save();
                    canvas.rotate(-90.0f, contentRectMinusAxesMargins.centerY(), contentRectMinusAxesMargins.centerY());
                    canvas.drawText(axis.getName(), contentRectMinusAxesMargins.centerY(), this.nameBaselineTab[i], this.namePaintTab[i]);
                    canvas.restore();
                    return;
                }
                canvas.drawText(axis.getName(), contentRectMinusAxesMargins.centerX(), this.nameBaselineTab[i], this.namePaintTab[i]);
                return;
            }
            return;
        }
        f = 0.0f;
        while (i2 < this.valuesToDrawNumTab[i]) {
        }
        Rect contentRectMinusAxesMargins2 = this.computator.getContentRectMinusAxesMargins();
        if (TextUtils.isEmpty(axis.getName())) {
        }
    }

    private boolean isAxisVertical(int i) {
        if (1 == i || 2 == i) {
            return true;
        }
        if (i == 0 || 3 == i) {
            return false;
        }
        throw new IllegalArgumentException("Invalid axis position " + i);
    }
}
