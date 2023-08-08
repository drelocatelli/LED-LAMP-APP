package com.home.utils.font;

import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class RadioGroupHelper implements View.OnClickListener {
    private int checkedRadioIndex;
    private OnCheckedChangeListener onCheckedChangeListener;
    private ViewGroup viewGroup;

    /* loaded from: classes.dex */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(View view, int i);
    }

    public RadioGroupHelper(ViewGroup viewGroup, int i) {
        this.viewGroup = viewGroup;
        this.checkedRadioIndex = i;
        int childCount = viewGroup.getChildCount();
        int i2 = 0;
        while (i2 < childCount) {
            View childAt = viewGroup.getChildAt(i2);
            childAt.setOnClickListener(this);
            childAt.setTag(Integer.valueOf(i2));
            childAt.setSelected(i2 == i);
            i2++;
        }
        this.checkedRadioIndex = i;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int childCount = this.viewGroup.getChildCount();
        int intValue = ((Integer) view.getTag()).intValue();
        if (this.checkedRadioIndex == intValue) {
            return;
        }
        this.checkedRadioIndex = intValue;
        for (int i = 0; i < childCount; i++) {
            View childAt = this.viewGroup.getChildAt(i);
            childAt.setSelected(((Integer) childAt.getTag()).intValue() == this.checkedRadioIndex);
        }
        OnCheckedChangeListener onCheckedChangeListener = this.onCheckedChangeListener;
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(view, this.checkedRadioIndex);
        }
    }

    public int getCheckedRadioIndex() {
        return this.checkedRadioIndex;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
}
