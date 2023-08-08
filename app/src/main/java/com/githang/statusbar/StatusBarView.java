package com.githang.statusbar;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes.dex */
public class StatusBarView extends View {
    private int mStatusBarHeight;

    public StatusBarView(Context context) {
        this(context, null);
    }

    public StatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (Build.VERSION.SDK_INT >= 19) {
            this.mStatusBarHeight = StatusBarTools.getStatusBarHeight(context);
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        setMeasuredDimension(View.MeasureSpec.getSize(i), this.mStatusBarHeight);
    }
}
