package com.home.view.wheel;

import android.content.Context;

/* loaded from: classes.dex */
public class AdapterWheel extends AbstractWheelTextAdapter {
    private WheelAdapter adapter;

    public AdapterWheel(Context context, WheelAdapter wheelAdapter) {
        super(context);
        this.adapter = wheelAdapter;
    }

    public WheelAdapter getAdapter() {
        return this.adapter;
    }

    @Override // com.home.view.wheel.WheelViewAdapter
    public int getItemsCount() {
        return this.adapter.getItemsCount();
    }

    @Override // com.home.view.wheel.AbstractWheelTextAdapter
    protected CharSequence getItemText(int i) {
        return this.adapter.getItem(i);
    }
}
