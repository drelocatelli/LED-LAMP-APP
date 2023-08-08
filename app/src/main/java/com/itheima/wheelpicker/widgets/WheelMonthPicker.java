package com.itheima.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;
import com.itheima.wheelpicker.WheelPicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* loaded from: classes.dex */
public class WheelMonthPicker extends WheelPicker implements IWheelMonthPicker {
    private int mSelectedMonth;

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= 12; i++) {
            arrayList.add(Integer.valueOf(i));
        }
        super.setData(arrayList);
        this.mSelectedMonth = Calendar.getInstance().get(2) + 1;
        updateSelectedYear();
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(this.mSelectedMonth - 1);
    }

    @Override // com.itheima.wheelpicker.WheelPicker, com.itheima.wheelpicker.IWheelPicker
    public void setData(List list) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelMonthPicker
    public int getSelectedMonth() {
        return this.mSelectedMonth;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelMonthPicker
    public void setSelectedMonth(int i) {
        this.mSelectedMonth = i;
        updateSelectedYear();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelMonthPicker
    public int getCurrentMonth() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition()))).intValue();
    }
}
