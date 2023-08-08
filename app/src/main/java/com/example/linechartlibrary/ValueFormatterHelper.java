package com.example.linechartlibrary;

import android.util.Log;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/* loaded from: classes.dex */
public class ValueFormatterHelper {
    public static final int DEFAULT_DIGITS_NUMBER = 0;
    private static final String TAG = "ValueFormatterHelper";
    private int decimalDigitsNumber = Integer.MIN_VALUE;
    private char[] appendedText = new char[0];
    private char[] prependedText = new char[0];
    private char decimalSeparator = '.';

    public void determineDecimalSeparator() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        if (numberFormat instanceof DecimalFormat) {
            this.decimalSeparator = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();
        }
    }

    public int getDecimalDigitsNumber() {
        return this.decimalDigitsNumber;
    }

    public ValueFormatterHelper setDecimalDigitsNumber(int i) {
        this.decimalDigitsNumber = i;
        return this;
    }

    public char[] getAppendedText() {
        return this.appendedText;
    }

    public ValueFormatterHelper setAppendedText(char[] cArr) {
        if (cArr != null) {
            this.appendedText = cArr;
        }
        return this;
    }

    public char[] getPrependedText() {
        return this.prependedText;
    }

    public ValueFormatterHelper setPrependedText(char[] cArr) {
        if (cArr != null) {
            this.prependedText = cArr;
        }
        return this;
    }

    public char getDecimalSeparator() {
        return this.decimalSeparator;
    }

    public ValueFormatterHelper setDecimalSeparator(char c) {
        if (c != 0) {
            this.decimalSeparator = c;
        }
        return this;
    }

    public int formatFloatValueWithPrependedAndAppendedText(char[] cArr, float f, int i, char[] cArr2) {
        if (cArr2 != null) {
            int length = cArr2.length;
            if (length > cArr.length) {
                Log.w(TAG, "Label length is larger than buffer size(64chars), some chars will be skipped!");
                length = cArr.length;
            }
            System.arraycopy(cArr2, 0, cArr, cArr.length - length, length);
            return length;
        }
        int formatFloatValue = formatFloatValue(cArr, f, getAppliedDecimalDigitsNumber(i));
        appendText(cArr);
        prependText(cArr, formatFloatValue);
        return formatFloatValue + getPrependedText().length + getAppendedText().length;
    }

    public int formatFloatValueWithPrependedAndAppendedText(char[] cArr, float f, char[] cArr2) {
        return formatFloatValueWithPrependedAndAppendedText(cArr, f, 0, cArr2);
    }

    public int formatFloatValueWithPrependedAndAppendedText(char[] cArr, float f, int i) {
        return formatFloatValueWithPrependedAndAppendedText(cArr, f, i, null);
    }

    public int formatFloatValue(char[] cArr, float f, int i) {
        return FloatUtils.formatFloat(cArr, f, cArr.length - this.appendedText.length, i, this.decimalSeparator);
    }

    public void appendText(char[] cArr) {
        char[] cArr2 = this.appendedText;
        if (cArr2.length > 0) {
            System.arraycopy(cArr2, 0, cArr, cArr.length - cArr2.length, cArr2.length);
        }
    }

    public void prependText(char[] cArr, int i) {
        char[] cArr2 = this.prependedText;
        if (cArr2.length > 0) {
            System.arraycopy(cArr2, 0, cArr, ((cArr.length - i) - this.appendedText.length) - cArr2.length, cArr2.length);
        }
    }

    public int getAppliedDecimalDigitsNumber(int i) {
        int i2 = this.decimalDigitsNumber;
        return i2 < 0 ? i : i2;
    }
}
