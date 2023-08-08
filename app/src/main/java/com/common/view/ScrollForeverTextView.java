package com.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class ScrollForeverTextView extends TextView {
    @Override // android.view.View
    public boolean isFocused() {
        return true;
    }

    public ScrollForeverTextView(Context context) {
        super(context);
    }

    public ScrollForeverTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ScrollForeverTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
