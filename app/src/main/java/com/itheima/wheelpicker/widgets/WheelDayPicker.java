package com.itheima.wheelpicker.widgets;

import android.content.Context;
import android.util.AttributeSet;
import com.itheima.wheelpicker.WheelPicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class WheelDayPicker extends WheelPicker implements IWheelDayPicker {
    private static final Map<Integer, List<Integer>> DAYS = new HashMap();
    private Calendar mCalendar;
    private int mMonth;
    private int mSelectedDay;
    private int mYear;

    public WheelDayPicker(Context context) {
        this(context, null);
    }

    public WheelDayPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Calendar calendar = Calendar.getInstance();
        this.mCalendar = calendar;
        this.mYear = calendar.get(1);
        this.mMonth = this.mCalendar.get(2);
        updateDays();
        this.mSelectedDay = this.mCalendar.get(5);
        updateSelectedDay();
    }

    private void updateDays() {
        this.mCalendar.set(1, this.mYear);
        this.mCalendar.set(2, this.mMonth);
        int actualMaximum = this.mCalendar.getActualMaximum(5);
        List<Integer> list = DAYS.get(Integer.valueOf(actualMaximum));
        if (list == null) {
            list = new ArrayList<>();
            for (int i = 1; i <= actualMaximum; i++) {
                list.add(Integer.valueOf(i));
            }
            DAYS.put(Integer.valueOf(actualMaximum), list);
        }
        super.setData(list);
    }

    private void updateSelectedDay() {
        setSelectedItemPosition(this.mSelectedDay - 1);
    }

    @Override // com.itheima.wheelpicker.WheelPicker, com.itheima.wheelpicker.IWheelPicker
    public void setData(List list) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelDayPicker");
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public int getSelectedDay() {
        return this.mSelectedDay;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public void setSelectedDay(int i) {
        this.mSelectedDay = i;
        updateSelectedDay();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public int getCurrentDay() {
        return Integer.valueOf(String.valueOf(getData().get(getCurrentItemPosition()))).intValue();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public void setYearAndMonth(int i, int i2) {
        this.mYear = i;
        this.mMonth = i2 - 1;
        updateDays();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public int getYear() {
        return this.mYear;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public void setYear(int i) {
        this.mYear = i;
        updateDays();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public int getMonth() {
        return this.mMonth;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public void setMonth(int i) {
        this.mMonth = i - 1;
        updateDays();
    }
}
