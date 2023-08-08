package com.itheima.wheelpicker;

import android.graphics.Typeface;
import com.itheima.wheelpicker.WheelPicker;
import java.util.List;

/* loaded from: classes.dex */
public interface IWheelPicker {
    int getCurrentItemPosition();

    int getCurtainColor();

    List getData();

    int getIndicatorColor();

    int getIndicatorSize();

    int getItemAlign();

    int getItemSpace();

    int getItemTextColor();

    int getItemTextSize();

    String getMaximumWidthText();

    int getMaximumWidthTextPosition();

    int getSelectedItemPosition();

    int getSelectedItemTextColor();

    Typeface getTypeface();

    int getVisibleItemCount();

    boolean hasAtmospheric();

    boolean hasCurtain();

    boolean hasIndicator();

    boolean hasSameWidth();

    boolean isCurved();

    boolean isCyclic();

    void setAtmospheric(boolean z);

    void setCurtain(boolean z);

    void setCurtainColor(int i);

    void setCurved(boolean z);

    void setCyclic(boolean z);

    void setData(List list);

    void setIndicator(boolean z);

    void setIndicatorColor(int i);

    void setIndicatorSize(int i);

    void setItemAlign(int i);

    void setItemSpace(int i);

    void setItemTextColor(int i);

    void setItemTextSize(int i);

    void setMaximumWidthText(String str);

    void setMaximumWidthTextPosition(int i);

    void setOnItemSelectedListener(WheelPicker.OnItemSelectedListener onItemSelectedListener);

    void setOnWheelChangeListener(WheelPicker.OnWheelChangeListener onWheelChangeListener);

    void setSameWidth(boolean z);

    void setSelectedItemPosition(int i);

    void setSelectedItemTextColor(int i);

    void setTypeface(Typeface typeface);

    void setVisibleItemCount(int i);
}
