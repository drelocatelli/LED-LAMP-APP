package com.home.view.wheel;

import android.content.Context;

/* loaded from: classes.dex */
public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {
    private T[] items;

    public ArrayWheelAdapter(Context context, T[] tArr) {
        super(context);
        this.items = tArr;
    }

    @Override // com.home.view.wheel.AbstractWheelTextAdapter
    public CharSequence getItemText(int i) {
        if (i >= 0) {
            T[] tArr = this.items;
            if (i < tArr.length) {
                T t = tArr[i];
                if (t instanceof CharSequence) {
                    return (CharSequence) t;
                }
                return t.toString();
            }
            return null;
        }
        return null;
    }

    @Override // com.home.view.wheel.WheelViewAdapter
    public int getItemsCount() {
        return this.items.length;
    }
}
