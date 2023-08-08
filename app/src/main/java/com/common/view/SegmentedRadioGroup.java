package com.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class SegmentedRadioGroup extends RadioGroup {
    public SegmentedRadioGroup(Context context) {
        super(context);
    }

    public SegmentedRadioGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.RadioGroup, android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        changeButtonsImages();
    }

    private void changeButtonsImages() {
        int childCount = super.getChildCount();
        if (childCount <= 1) {
            if (childCount == 1) {
                super.getChildAt(0).setBackgroundResource(R.drawable.segment_radio_button);
                return;
            }
            return;
        }
        super.getChildAt(0).setBackgroundResource(R.drawable.checkbox_seg1);
        int i = 1;
        while (true) {
            int i2 = childCount - 1;
            if (i < i2) {
                super.getChildAt(i).setBackgroundResource(R.drawable.checkbox_seg2);
                i++;
            } else {
                super.getChildAt(i2).setBackgroundResource(R.drawable.checkbox_seg3);
                return;
            }
        }
    }
}
