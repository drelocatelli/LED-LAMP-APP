package com.example.linechartlibrary;

/* loaded from: classes.dex */
public class SimpleLineChartValueFormatter implements LineChartValueFormatter {
    private ValueFormatterHelper valueFormatterHelper;

    public SimpleLineChartValueFormatter() {
        ValueFormatterHelper valueFormatterHelper = new ValueFormatterHelper();
        this.valueFormatterHelper = valueFormatterHelper;
        valueFormatterHelper.determineDecimalSeparator();
    }

    public SimpleLineChartValueFormatter(int i) {
        this();
        this.valueFormatterHelper.setDecimalDigitsNumber(i);
    }

    @Override // com.example.linechartlibrary.LineChartValueFormatter
    public int formatChartValue(char[] cArr, PointValue pointValue) {
        return this.valueFormatterHelper.formatFloatValueWithPrependedAndAppendedText(cArr, pointValue.getY(), pointValue.getLabelAsChars());
    }

    public int getDecimalDigitsNumber() {
        return this.valueFormatterHelper.getDecimalDigitsNumber();
    }

    public SimpleLineChartValueFormatter setDecimalDigitsNumber(int i) {
        this.valueFormatterHelper.setDecimalDigitsNumber(i);
        return this;
    }

    public char[] getAppendedText() {
        return this.valueFormatterHelper.getAppendedText();
    }

    public SimpleLineChartValueFormatter setAppendedText(char[] cArr) {
        this.valueFormatterHelper.setAppendedText(cArr);
        return this;
    }

    public char[] getPrependedText() {
        return this.valueFormatterHelper.getPrependedText();
    }

    public SimpleLineChartValueFormatter setPrependedText(char[] cArr) {
        this.valueFormatterHelper.setPrependedText(cArr);
        return this;
    }

    public char getDecimalSeparator() {
        return this.valueFormatterHelper.getDecimalSeparator();
    }

    public SimpleLineChartValueFormatter setDecimalSeparator(char c) {
        this.valueFormatterHelper.setDecimalSeparator(c);
        return this;
    }
}
