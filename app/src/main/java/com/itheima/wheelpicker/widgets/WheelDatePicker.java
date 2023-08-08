package com.itheima.wheelpicker.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.common.net.NetResult;
import com.itheima.wheelpicker.IDebug;
import com.itheima.wheelpicker.IWheelPicker;
import com.itheima.wheelpicker.R;
import com.itheima.wheelpicker.WheelPicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class WheelDatePicker extends LinearLayout implements WheelPicker.OnItemSelectedListener, IDebug, IWheelPicker, IWheelDatePicker, IWheelYearPicker, IWheelMonthPicker, IWheelDayPicker {
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
    private int mDay;
    private OnDateSelectedListener mListener;
    private int mMonth;
    private WheelDayPicker mPickerDay;
    private WheelMonthPicker mPickerMonth;
    private WheelYearPicker mPickerYear;
    private TextView mTVDay;
    private TextView mTVMonth;
    private TextView mTVYear;
    private int mYear;

    /* loaded from: classes.dex */
    public interface OnDateSelectedListener {
        void onDateSelected(WheelDatePicker wheelDatePicker, Date date);
    }

    public WheelDatePicker(Context context) {
        this(context, null);
    }

    public WheelDatePicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.view_wheel_date_picker, this);
        this.mPickerYear = (WheelYearPicker) findViewById(R.id.wheel_date_picker_year);
        this.mPickerMonth = (WheelMonthPicker) findViewById(R.id.wheel_date_picker_month);
        this.mPickerDay = (WheelDayPicker) findViewById(R.id.wheel_date_picker_day);
        this.mPickerYear.setOnItemSelectedListener(this);
        this.mPickerMonth.setOnItemSelectedListener(this);
        this.mPickerDay.setOnItemSelectedListener(this);
        setMaximumWidthTextYear();
        this.mPickerMonth.setMaximumWidthText("00");
        this.mPickerDay.setMaximumWidthText("00");
        this.mTVYear = (TextView) findViewById(R.id.wheel_date_picker_year_tv);
        this.mTVMonth = (TextView) findViewById(R.id.wheel_date_picker_month_tv);
        this.mTVDay = (TextView) findViewById(R.id.wheel_date_picker_day_tv);
        this.mYear = this.mPickerYear.getCurrentYear();
        this.mMonth = this.mPickerMonth.getCurrentMonth();
        this.mDay = this.mPickerDay.getCurrentDay();
    }

    private void setMaximumWidthTextYear() {
        List data = this.mPickerYear.getData();
        String valueOf = String.valueOf(data.get(data.size() - 1));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valueOf.length(); i++) {
            sb.append(NetResult.CODE_OK);
        }
        this.mPickerYear.setMaximumWidthText(sb.toString());
    }

    @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
    public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
        if (wheelPicker.getId() == R.id.wheel_date_picker_year) {
            int intValue = ((Integer) obj).intValue();
            this.mYear = intValue;
            this.mPickerDay.setYear(intValue);
        } else if (wheelPicker.getId() == R.id.wheel_date_picker_month) {
            int intValue2 = ((Integer) obj).intValue();
            this.mMonth = intValue2;
            this.mPickerDay.setMonth(intValue2);
        }
        this.mDay = this.mPickerDay.getCurrentDay();
        String str = this.mYear + "-" + this.mMonth + "-" + this.mDay;
        OnDateSelectedListener onDateSelectedListener = this.mListener;
        if (onDateSelectedListener != null) {
            try {
                onDateSelectedListener.onDateSelected(this, SDF.parse(str));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override // com.itheima.wheelpicker.IDebug
    public void setDebug(boolean z) {
        this.mPickerYear.setDebug(false);
        this.mPickerMonth.setDebug(false);
        this.mPickerDay.setDebug(false);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getVisibleItemCount() {
        if (this.mPickerYear.getVisibleItemCount() == this.mPickerMonth.getVisibleItemCount() && this.mPickerMonth.getVisibleItemCount() == this.mPickerDay.getVisibleItemCount()) {
            return this.mPickerYear.getVisibleItemCount();
        }
        throw new ArithmeticException("Can not get visible item count correctly fromWheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setVisibleItemCount(int i) {
        this.mPickerYear.setVisibleItemCount(i);
        this.mPickerMonth.setVisibleItemCount(i);
        this.mPickerDay.setVisibleItemCount(i);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean isCyclic() {
        return this.mPickerYear.isCyclic() && this.mPickerMonth.isCyclic() && this.mPickerDay.isCyclic();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setCyclic(boolean z) {
        this.mPickerYear.setCyclic(z);
        this.mPickerMonth.setCyclic(z);
        this.mPickerDay.setCyclic(z);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public void setOnItemSelectedListener(WheelPicker.OnItemSelectedListener onItemSelectedListener) {
        throw new UnsupportedOperationException("You can not set OnItemSelectedListener forWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public int getSelectedItemPosition() {
        throw new UnsupportedOperationException("You can not get position of selected item fromWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public void setSelectedItemPosition(int i) {
        throw new UnsupportedOperationException("You can not set position of selected item forWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public int getCurrentItemPosition() {
        throw new UnsupportedOperationException("You can not get position of current item fromWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public List getData() {
        throw new UnsupportedOperationException("You can not get data source from WheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public void setData(List list) {
        throw new UnsupportedOperationException("You don't need to set data source forWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public void setSameWidth(boolean z) {
        throw new UnsupportedOperationException("You don't need to set same width forWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public boolean hasSameWidth() {
        throw new UnsupportedOperationException("You don't need to set same width forWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public void setOnWheelChangeListener(WheelPicker.OnWheelChangeListener onWheelChangeListener) {
        throw new UnsupportedOperationException("WheelDatePicker unsupport setOnWheelChangeListener");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public String getMaximumWidthText() {
        throw new UnsupportedOperationException("You can not get maximum width text fromWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public void setMaximumWidthText(String str) {
        throw new UnsupportedOperationException("You don't need to set maximum width text forWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public int getMaximumWidthTextPosition() {
        throw new UnsupportedOperationException("You can not get maximum width text positionfrom WheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public void setMaximumWidthTextPosition(int i) {
        throw new UnsupportedOperationException("You don't need to set maximum width textposition for WheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getSelectedItemTextColor() {
        if (this.mPickerYear.getSelectedItemTextColor() == this.mPickerMonth.getSelectedItemTextColor() && this.mPickerMonth.getSelectedItemTextColor() == this.mPickerDay.getSelectedItemTextColor()) {
            return this.mPickerYear.getSelectedItemTextColor();
        }
        throw new RuntimeException("Can not get color of selected item text correctly fromWheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setSelectedItemTextColor(int i) {
        this.mPickerYear.setSelectedItemTextColor(i);
        this.mPickerMonth.setSelectedItemTextColor(i);
        this.mPickerDay.setSelectedItemTextColor(i);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getItemTextColor() {
        if (this.mPickerYear.getItemTextColor() == this.mPickerMonth.getItemTextColor() && this.mPickerMonth.getItemTextColor() == this.mPickerDay.getItemTextColor()) {
            return this.mPickerYear.getItemTextColor();
        }
        throw new RuntimeException("Can not get color of item text correctly fromWheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setItemTextColor(int i) {
        this.mPickerYear.setItemTextColor(i);
        this.mPickerMonth.setItemTextColor(i);
        this.mPickerDay.setItemTextColor(i);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getItemTextSize() {
        if (this.mPickerYear.getItemTextSize() == this.mPickerMonth.getItemTextSize() && this.mPickerMonth.getItemTextSize() == this.mPickerDay.getItemTextSize()) {
            return this.mPickerYear.getItemTextSize();
        }
        throw new RuntimeException("Can not get size of item text correctly fromWheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setItemTextSize(int i) {
        this.mPickerYear.setItemTextSize(i);
        this.mPickerMonth.setItemTextSize(i);
        this.mPickerDay.setItemTextSize(i);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getItemSpace() {
        if (this.mPickerYear.getItemSpace() == this.mPickerMonth.getItemSpace() && this.mPickerMonth.getItemSpace() == this.mPickerDay.getItemSpace()) {
            return this.mPickerYear.getItemSpace();
        }
        throw new RuntimeException("Can not get item space correctly from WheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setItemSpace(int i) {
        this.mPickerYear.setItemSpace(i);
        this.mPickerMonth.setItemSpace(i);
        this.mPickerDay.setItemSpace(i);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setIndicator(boolean z) {
        this.mPickerYear.setIndicator(z);
        this.mPickerMonth.setIndicator(z);
        this.mPickerDay.setIndicator(z);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean hasIndicator() {
        return this.mPickerYear.hasIndicator() && this.mPickerMonth.hasIndicator() && this.mPickerDay.hasIndicator();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getIndicatorSize() {
        if (this.mPickerYear.getIndicatorSize() == this.mPickerMonth.getIndicatorSize() && this.mPickerMonth.getIndicatorSize() == this.mPickerDay.getIndicatorSize()) {
            return this.mPickerYear.getIndicatorSize();
        }
        throw new RuntimeException("Can not get indicator size correctly from WheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setIndicatorSize(int i) {
        this.mPickerYear.setIndicatorSize(i);
        this.mPickerMonth.setIndicatorSize(i);
        this.mPickerDay.setIndicatorSize(i);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getIndicatorColor() {
        if (this.mPickerYear.getCurtainColor() == this.mPickerMonth.getCurtainColor() && this.mPickerMonth.getCurtainColor() == this.mPickerDay.getCurtainColor()) {
            return this.mPickerYear.getCurtainColor();
        }
        throw new RuntimeException("Can not get indicator color correctly from WheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setIndicatorColor(int i) {
        this.mPickerYear.setIndicatorColor(i);
        this.mPickerMonth.setIndicatorColor(i);
        this.mPickerDay.setIndicatorColor(i);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setCurtain(boolean z) {
        this.mPickerYear.setCurtain(z);
        this.mPickerMonth.setCurtain(z);
        this.mPickerDay.setCurtain(z);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean hasCurtain() {
        return this.mPickerYear.hasCurtain() && this.mPickerMonth.hasCurtain() && this.mPickerDay.hasCurtain();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public int getCurtainColor() {
        if (this.mPickerYear.getCurtainColor() == this.mPickerMonth.getCurtainColor() && this.mPickerMonth.getCurtainColor() == this.mPickerDay.getCurtainColor()) {
            return this.mPickerYear.getCurtainColor();
        }
        throw new RuntimeException("Can not get curtain color correctly from WheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setCurtainColor(int i) {
        this.mPickerYear.setCurtainColor(i);
        this.mPickerMonth.setCurtainColor(i);
        this.mPickerDay.setCurtainColor(i);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setAtmospheric(boolean z) {
        this.mPickerYear.setAtmospheric(z);
        this.mPickerMonth.setAtmospheric(z);
        this.mPickerDay.setAtmospheric(z);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean hasAtmospheric() {
        return this.mPickerYear.hasAtmospheric() && this.mPickerMonth.hasAtmospheric() && this.mPickerDay.hasAtmospheric();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public boolean isCurved() {
        return this.mPickerYear.isCurved() && this.mPickerMonth.isCurved() && this.mPickerDay.isCurved();
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setCurved(boolean z) {
        this.mPickerYear.setCurved(z);
        this.mPickerMonth.setCurved(z);
        this.mPickerDay.setCurved(z);
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public int getItemAlign() {
        throw new UnsupportedOperationException("You can not get item align from WheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    @Deprecated
    public void setItemAlign(int i) {
        throw new UnsupportedOperationException("You don't need to set item align forWheelDatePicker");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public Typeface getTypeface() {
        if (this.mPickerYear.getTypeface().equals(this.mPickerMonth.getTypeface()) && this.mPickerMonth.getTypeface().equals(this.mPickerDay.getTypeface())) {
            return this.mPickerYear.getTypeface();
        }
        throw new RuntimeException("Can not get typeface correctly from WheelDatePicker!");
    }

    @Override // com.itheima.wheelpicker.IWheelPicker
    public void setTypeface(Typeface typeface) {
        this.mPickerYear.setTypeface(typeface);
        this.mPickerMonth.setTypeface(typeface);
        this.mPickerDay.setTypeface(typeface);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.mListener = onDateSelectedListener;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public Date getCurrentDate() {
        try {
            return SDF.parse(this.mYear + "-" + this.mMonth + "-" + this.mDay);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public int getItemAlignYear() {
        return this.mPickerYear.getItemAlign();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public void setItemAlignYear(int i) {
        this.mPickerYear.setItemAlign(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public int getItemAlignMonth() {
        return this.mPickerMonth.getItemAlign();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public void setItemAlignMonth(int i) {
        this.mPickerMonth.setItemAlign(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public int getItemAlignDay() {
        return this.mPickerDay.getItemAlign();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public void setItemAlignDay(int i) {
        this.mPickerDay.setItemAlign(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public WheelYearPicker getWheelYearPicker() {
        return this.mPickerYear;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public WheelMonthPicker getWheelMonthPicker() {
        return this.mPickerMonth;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public WheelDayPicker getWheelDayPicker() {
        return this.mPickerDay;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public TextView getTextViewYear() {
        return this.mTVYear;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public TextView getTextViewMonth() {
        return this.mTVMonth;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDatePicker
    public TextView getTextViewDay() {
        return this.mTVDay;
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelYearPicker
    public void setYearFrame(int i, int i2) {
        this.mPickerYear.setYearFrame(i, i2);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelYearPicker
    public int getYearStart() {
        return this.mPickerYear.getYearStart();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelYearPicker
    public void setYearStart(int i) {
        this.mPickerYear.setYearStart(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelYearPicker
    public int getYearEnd() {
        return this.mPickerYear.getYearEnd();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelYearPicker
    public void setYearEnd(int i) {
        this.mPickerYear.setYearEnd(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelYearPicker
    public int getSelectedYear() {
        return this.mPickerYear.getSelectedYear();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelYearPicker
    public void setSelectedYear(int i) {
        this.mYear = i;
        this.mPickerYear.setSelectedYear(i);
        this.mPickerDay.setYear(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelYearPicker
    public int getCurrentYear() {
        return this.mPickerYear.getCurrentYear();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelMonthPicker
    public int getSelectedMonth() {
        return this.mPickerMonth.getSelectedMonth();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelMonthPicker
    public void setSelectedMonth(int i) {
        this.mMonth = i;
        this.mPickerMonth.setSelectedMonth(i);
        this.mPickerDay.setMonth(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelMonthPicker
    public int getCurrentMonth() {
        return this.mPickerMonth.getCurrentMonth();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public int getSelectedDay() {
        return this.mPickerDay.getSelectedDay();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public void setSelectedDay(int i) {
        this.mDay = i;
        this.mPickerDay.setSelectedDay(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public int getCurrentDay() {
        return this.mPickerDay.getCurrentDay();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public void setYearAndMonth(int i, int i2) {
        this.mYear = i;
        this.mMonth = i2;
        this.mPickerYear.setSelectedYear(i);
        this.mPickerMonth.setSelectedMonth(i2);
        this.mPickerDay.setYearAndMonth(i, i2);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public int getYear() {
        return getSelectedYear();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public void setYear(int i) {
        this.mYear = i;
        this.mPickerYear.setSelectedYear(i);
        this.mPickerDay.setYear(i);
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public int getMonth() {
        return getSelectedMonth();
    }

    @Override // com.itheima.wheelpicker.widgets.IWheelDayPicker
    public void setMonth(int i) {
        this.mMonth = i;
        this.mPickerMonth.setSelectedMonth(i);
        this.mPickerDay.setMonth(i);
    }
}
