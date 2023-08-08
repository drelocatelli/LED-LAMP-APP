package com.common.view;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

/* loaded from: classes.dex */
public class TextViewAutoAjust extends TextView {
    Context context;
    Paint paint;

    public TextViewAutoAjust(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paint = null;
        this.context = context;
        this.paint = new Paint();
    }

    public void setText(String str) {
        super.setText(Html.fromHtml(str));
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        super.setText(charSequence, bufferType);
    }
}
