package com.forum.login;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/* loaded from: classes.dex */
public class CustomView extends RelativeLayout {
    static final String ANDROIDXML = "http://schemas.android.com/apk/res/android";
    static final String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    public boolean isLastTouch;

    public CustomView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isLastTouch = false;
    }
}
