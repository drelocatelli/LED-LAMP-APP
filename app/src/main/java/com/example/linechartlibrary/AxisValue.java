package com.example.linechartlibrary;

import java.util.Arrays;

/* loaded from: classes.dex */
public class AxisValue {
    private char[] label;
    private float value;

    public AxisValue(float f) {
        setValue(f);
    }

    @Deprecated
    public AxisValue(float f, char[] cArr) {
        this.value = f;
        this.label = cArr;
    }

    public AxisValue(AxisValue axisValue) {
        this.value = axisValue.value;
        this.label = axisValue.label;
    }

    public float getValue() {
        return this.value;
    }

    public AxisValue setValue(float f) {
        this.value = f;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public AxisValue setLabel(String str) {
        this.label = str.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public AxisValue setLabel(char[] cArr) {
        this.label = cArr;
        return this;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AxisValue axisValue = (AxisValue) obj;
        return Float.compare(axisValue.value, this.value) == 0 && Arrays.equals(this.label, axisValue.label);
    }

    public int hashCode() {
        float f = this.value;
        int floatToIntBits = (f != 0.0f ? Float.floatToIntBits(f) : 0) * 31;
        char[] cArr = this.label;
        return floatToIntBits + (cArr != null ? Arrays.hashCode(cArr) : 0);
    }
}
