package com.common.view.selectabletext;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import com.common.view.selectabletext.SelectableTextHelper;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class SelectableTextView extends AppCompatTextView {
    private int cursorHandleColor;
    private SelectableTextHelper mSelectableTextHelper;
    private int selectedColor;

    public SelectableTextView(Context context) {
        super(context);
    }

    public SelectableTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public SelectableTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SelectableTextView);
        this.selectedColor = obtainStyledAttributes.getColor(1, ContextCompat.getColor(context, R.color.colorAccent));
        this.cursorHandleColor = obtainStyledAttributes.getColor(0, ContextCompat.getColor(context, R.color.colorAccent));
        this.mSelectableTextHelper = new SelectableTextHelper.Builder(this).setSelectedColor(this.selectedColor).setCursorHandleSizeInDp(20.0f).setCursorHandleColor(this.cursorHandleColor).build();
    }

    public void setSelectedColor(int i) {
        this.selectedColor = i;
    }

    public void setCursorHandleColor(int i) {
        this.cursorHandleColor = i;
    }
}
