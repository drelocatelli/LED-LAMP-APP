package com.example.linechartlibrary;

import androidx.core.view.ViewCompat;
import java.util.Arrays;

/* loaded from: classes.dex */
public class PointValue {
    private float diffX;
    private float diffY;
    private char[] label;
    private int labelColor = ViewCompat.MEASURED_STATE_MASK;
    private int labelTextSize = CommonUtils.dp2px(MyApp.getInstance(), 14.0f);
    private float originX;
    private float originY;
    private float x;
    private float y;

    public PointValue() {
        set(0.0f, 0.0f);
    }

    public PointValue(float f, float f2) {
        set(f, f2);
    }

    public PointValue(PointValue pointValue) {
        set(pointValue.x, pointValue.y);
        this.label = pointValue.label;
    }

    public void update(float f) {
        this.x = this.originX + (this.diffX * f);
        this.y = this.originY + (this.diffY * f);
    }

    public void finish() {
        set(this.originX + this.diffX, this.originY + this.diffY);
    }

    public PointValue set(float f, float f2) {
        this.x = f;
        this.y = f2;
        this.originX = f;
        this.originY = f2;
        this.diffX = 0.0f;
        this.diffY = 0.0f;
        return this;
    }

    public PointValue setTarget(float f, float f2) {
        set(this.x, this.y);
        this.diffX = f - this.originX;
        this.diffY = f2 - this.originY;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public PointValue setLabel(String str) {
        this.label = str.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public PointValue setLabel(char[] cArr) {
        this.label = cArr;
        return this;
    }

    public PointValue setLabelColor(int i) {
        this.labelColor = i;
        return this;
    }

    public int getLabelColor() {
        return this.labelColor;
    }

    public PointValue setLabelTextsize(int i) {
        this.labelTextSize = i;
        return this;
    }

    public int getLabelTextSize() {
        return this.labelTextSize;
    }

    public String toString() {
        return "PointValue [x=" + this.x + ", y=" + this.y + "]";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PointValue pointValue = (PointValue) obj;
        return Float.compare(pointValue.diffX, this.diffX) == 0 && Float.compare(pointValue.diffY, this.diffY) == 0 && Float.compare(pointValue.originX, this.originX) == 0 && Float.compare(pointValue.originY, this.originY) == 0 && Float.compare(pointValue.x, this.x) == 0 && Float.compare(pointValue.y, this.y) == 0 && Arrays.equals(this.label, pointValue.label);
    }

    public int hashCode() {
        float f = this.x;
        int floatToIntBits = (f != 0.0f ? Float.floatToIntBits(f) : 0) * 31;
        float f2 = this.y;
        int floatToIntBits2 = (floatToIntBits + (f2 != 0.0f ? Float.floatToIntBits(f2) : 0)) * 31;
        float f3 = this.originX;
        int floatToIntBits3 = (floatToIntBits2 + (f3 != 0.0f ? Float.floatToIntBits(f3) : 0)) * 31;
        float f4 = this.originY;
        int floatToIntBits4 = (floatToIntBits3 + (f4 != 0.0f ? Float.floatToIntBits(f4) : 0)) * 31;
        float f5 = this.diffX;
        int floatToIntBits5 = (floatToIntBits4 + (f5 != 0.0f ? Float.floatToIntBits(f5) : 0)) * 31;
        float f6 = this.diffY;
        int floatToIntBits6 = (floatToIntBits5 + (f6 != 0.0f ? Float.floatToIntBits(f6) : 0)) * 31;
        char[] cArr = this.label;
        return floatToIntBits6 + (cArr != null ? Arrays.hashCode(cArr) : 0);
    }
}
