package com.example.linechartlibrary;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LineChartData extends AbstractChartData {
    public static final float DEFAULT_BASE_VALUE = 0.0f;
    private float baseValue;
    private List<Line> lines;

    public LineChartData() {
        this.lines = new ArrayList();
        this.baseValue = 0.0f;
    }

    public LineChartData(List<Line> list) {
        this.lines = new ArrayList();
        this.baseValue = 0.0f;
        setLines(list);
    }

    public LineChartData(LineChartData lineChartData) {
        super(lineChartData);
        this.lines = new ArrayList();
        this.baseValue = 0.0f;
        this.baseValue = lineChartData.baseValue;
        for (Line line : lineChartData.lines) {
            this.lines.add(new Line(line));
        }
    }

    public static LineChartData generateDummyData() {
        LineChartData lineChartData = new LineChartData();
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(new PointValue(0.0f, 2.0f));
        arrayList.add(new PointValue(1.0f, 4.0f));
        arrayList.add(new PointValue(2.0f, 3.0f));
        arrayList.add(new PointValue(3.0f, 4.0f));
        Line line = new Line(arrayList);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList2.add(line);
        lineChartData.setLines(arrayList2);
        return lineChartData;
    }

    @Override // com.example.linechartlibrary.ChartData
    public void update(float f) {
        for (Line line : this.lines) {
            line.update(f);
        }
    }

    @Override // com.example.linechartlibrary.ChartData
    public void finish() {
        for (Line line : this.lines) {
            line.finish();
        }
    }

    public List<Line> getLines() {
        return this.lines;
    }

    public LineChartData setLines(List<Line> list) {
        if (list == null) {
            this.lines = new ArrayList();
        } else {
            this.lines = list;
        }
        return this;
    }

    public float getBaseValue() {
        return this.baseValue;
    }

    public LineChartData setBaseValue(float f) {
        this.baseValue = f;
        return this;
    }
}
