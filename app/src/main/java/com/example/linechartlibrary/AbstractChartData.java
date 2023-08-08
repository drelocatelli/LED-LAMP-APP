package com.example.linechartlibrary;

import android.graphics.Typeface;

/* loaded from: classes.dex */
public abstract class AbstractChartData implements ChartData {
    public static final int DEFAULT_TEXT_SIZE_SP = 12;
    protected Axis axisXBottom;
    protected Axis axisXTop;
    protected Axis axisYLeft;
    protected Axis axisYRight;
    protected boolean isValueLabelBackgroundEnabled;
    protected boolean isValueLabelBackgrountAuto;
    protected int valueLabelBackgroundColor;
    protected int valueLabelTextColor;
    protected int valueLabelTextSize;
    protected Typeface valueLabelTypeface;

    public AbstractChartData() {
        this.valueLabelTextColor = -1;
        this.valueLabelTextSize = 12;
        this.isValueLabelBackgroundEnabled = true;
        this.isValueLabelBackgrountAuto = true;
        this.valueLabelBackgroundColor = ChartUtils.darkenColor(ChartUtils.DEFAULT_DARKEN_COLOR);
    }

    public AbstractChartData(AbstractChartData abstractChartData) {
        this.valueLabelTextColor = -1;
        this.valueLabelTextSize = 12;
        this.isValueLabelBackgroundEnabled = true;
        this.isValueLabelBackgrountAuto = true;
        this.valueLabelBackgroundColor = ChartUtils.darkenColor(ChartUtils.DEFAULT_DARKEN_COLOR);
        Axis axis = abstractChartData.axisXBottom;
        if (axis != null) {
            this.axisXBottom = new Axis(axis);
        }
        Axis axis2 = abstractChartData.axisXTop;
        if (axis2 != null) {
            this.axisXTop = new Axis(axis2);
        }
        Axis axis3 = abstractChartData.axisYLeft;
        if (axis3 != null) {
            this.axisYLeft = new Axis(axis3);
        }
        Axis axis4 = abstractChartData.axisYRight;
        if (axis4 != null) {
            this.axisYRight = new Axis(axis4);
        }
        this.valueLabelTextColor = abstractChartData.valueLabelTextColor;
        this.valueLabelTextSize = abstractChartData.valueLabelTextSize;
        this.valueLabelTypeface = abstractChartData.valueLabelTypeface;
    }

    @Override // com.example.linechartlibrary.ChartData
    public Axis getAxisXBottom() {
        return this.axisXBottom;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setAxisXBottom(Axis axis) {
        this.axisXBottom = axis;
    }

    @Override // com.example.linechartlibrary.ChartData
    public Axis getAxisYLeft() {
        return this.axisYLeft;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setAxisYLeft(Axis axis) {
        this.axisYLeft = axis;
    }

    @Override // com.example.linechartlibrary.ChartData
    public Axis getAxisXTop() {
        return this.axisXTop;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setAxisXTop(Axis axis) {
        this.axisXTop = axis;
    }

    @Override // com.example.linechartlibrary.ChartData
    public Axis getAxisYRight() {
        return this.axisYRight;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setAxisYRight(Axis axis) {
        this.axisYRight = axis;
    }

    @Override // com.example.linechartlibrary.ChartData
    public int getValueLabelTextColor() {
        return this.valueLabelTextColor;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setValueLabelsTextColor(int i) {
        this.valueLabelTextColor = i;
    }

    @Override // com.example.linechartlibrary.ChartData
    public int getValueLabelTextSize() {
        return this.valueLabelTextSize;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setValueLabelTextSize(int i) {
        this.valueLabelTextSize = i;
    }

    @Override // com.example.linechartlibrary.ChartData
    public Typeface getValueLabelTypeface() {
        return this.valueLabelTypeface;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setValueLabelTypeface(Typeface typeface) {
        this.valueLabelTypeface = typeface;
    }

    @Override // com.example.linechartlibrary.ChartData
    public boolean isValueLabelBackgroundEnabled() {
        return this.isValueLabelBackgroundEnabled;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setValueLabelBackgroundEnabled(boolean z) {
        this.isValueLabelBackgroundEnabled = z;
    }

    @Override // com.example.linechartlibrary.ChartData
    public boolean isValueLabelBackgroundAuto() {
        return this.isValueLabelBackgrountAuto;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setValueLabelBackgroundAuto(boolean z) {
        this.isValueLabelBackgrountAuto = z;
    }

    @Override // com.example.linechartlibrary.ChartData
    public int getValueLabelBackgroundColor() {
        return this.valueLabelBackgroundColor;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void setValueLabelBackgroundColor(int i) {
        this.valueLabelBackgroundColor = i;
    }
}
