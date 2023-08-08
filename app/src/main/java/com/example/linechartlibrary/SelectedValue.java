package com.example.linechartlibrary;

/* loaded from: classes.dex */
public class SelectedValue {
    private int firstIndex;
    private int secondIndex;
    private SelectedValueType type = SelectedValueType.NONE;

    /* loaded from: classes.dex */
    public enum SelectedValueType {
        NONE,
        LINE,
        COLUMN
    }

    public SelectedValue() {
        clear();
    }

    public SelectedValue(int i, int i2, SelectedValueType selectedValueType) {
        set(i, i2, selectedValueType);
    }

    public void set(int i, int i2, SelectedValueType selectedValueType) {
        this.firstIndex = i;
        this.secondIndex = i2;
        if (selectedValueType != null) {
            this.type = selectedValueType;
        } else {
            this.type = SelectedValueType.NONE;
        }
    }

    public void set(SelectedValue selectedValue) {
        this.firstIndex = selectedValue.firstIndex;
        this.secondIndex = selectedValue.secondIndex;
        this.type = selectedValue.type;
    }

    public void clear() {
        set(Integer.MIN_VALUE, Integer.MIN_VALUE, SelectedValueType.NONE);
    }

    public boolean isSet() {
        return this.firstIndex >= 0 && this.secondIndex >= 0;
    }

    public int getFirstIndex() {
        return this.firstIndex;
    }

    public void setFirstIndex(int i) {
        this.firstIndex = i;
    }

    public int getSecondIndex() {
        return this.secondIndex;
    }

    public void setSecondIndex(int i) {
        this.secondIndex = i;
    }

    public SelectedValueType getType() {
        return this.type;
    }

    public void setType(SelectedValueType selectedValueType) {
        this.type = selectedValueType;
    }

    public int hashCode() {
        int i = (((this.firstIndex + 31) * 31) + this.secondIndex) * 31;
        SelectedValueType selectedValueType = this.type;
        return i + (selectedValueType == null ? 0 : selectedValueType.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            SelectedValue selectedValue = (SelectedValue) obj;
            return this.firstIndex == selectedValue.firstIndex && this.secondIndex == selectedValue.secondIndex && this.type == selectedValue.type;
        }
        return false;
    }

    public String toString() {
        return "SelectedValue [firstIndex=" + this.firstIndex + ", secondIndex=" + this.secondIndex + ", type=" + this.type + "]";
    }
}
