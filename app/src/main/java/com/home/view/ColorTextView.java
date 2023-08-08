package com.home.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class ColorTextView extends TextView {
    private int color;

    public ColorTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.color = 0;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
    }
}
