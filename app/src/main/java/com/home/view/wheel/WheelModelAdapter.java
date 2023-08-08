package com.home.view.wheel;

import android.content.Context;

/* loaded from: classes.dex */
public class WheelModelAdapter extends ArrayWheelAdapter {
    private String[] model;

    public WheelModelAdapter(Context context, String[] strArr) {
        super(context, strArr);
        this.model = strArr;
    }

    @Override // com.home.view.wheel.ArrayWheelAdapter, com.home.view.wheel.AbstractWheelTextAdapter
    public CharSequence getItemText(int i) {
        return this.model[i];
    }
}
