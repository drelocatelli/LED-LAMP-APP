package com.ccr.achenglibrary.photopicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/* loaded from: classes.dex */
public class CCRHeightWrapGridView extends GridView {
    public CCRHeightWrapGridView(Context context) {
        this(context, null);
    }

    public CCRHeightWrapGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842865);
    }

    public CCRHeightWrapGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setSelector(17170445);
        setOverScrollMode(2);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
    }

    @Override // android.widget.GridView, android.widget.AbsListView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
    }
}
