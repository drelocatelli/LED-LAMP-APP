package com.common.view.selectabletext;

import android.content.Context;
import android.text.Layout;
import android.widget.TextView;

/* loaded from: classes.dex */
public class TextLayoutUtil {
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getPreciseOffset(TextView textView, int i, int i2) {
        Layout layout = textView.getLayout();
        if (layout != null) {
            int offsetForHorizontal = layout.getOffsetForHorizontal(layout.getLineForVertical(i2), i);
            return ((int) layout.getPrimaryHorizontal(offsetForHorizontal)) > i ? layout.getOffsetToLeftOf(offsetForHorizontal) : offsetForHorizontal;
        }
        return -1;
    }

    public static int getHysteresisOffset(TextView textView, int i, int i2, int i3) {
        Layout layout = textView.getLayout();
        if (layout == null) {
            return -1;
        }
        int lineForVertical = layout.getLineForVertical(i2);
        if (isEndOfLineOffset(layout, i3)) {
            int lineRight = (int) layout.getLineRight(lineForVertical);
            if (i > lineRight - ((lineRight - ((int) layout.getPrimaryHorizontal(i3 - 1))) / 2)) {
                i3--;
            }
        }
        int lineForOffset = layout.getLineForOffset(i3);
        int lineTop = layout.getLineTop(lineForOffset);
        int lineBottom = layout.getLineBottom(lineForOffset);
        int i4 = (lineBottom - lineTop) / 2;
        if ((lineForVertical == lineForOffset + 1 && i2 - lineBottom < i4) || (lineForVertical == lineForOffset - 1 && lineTop - i2 < i4)) {
            lineForVertical = lineForOffset;
        }
        int offsetForHorizontal = layout.getOffsetForHorizontal(lineForVertical, i);
        if (offsetForHorizontal < textView.getText().length() - 1) {
            int i5 = offsetForHorizontal + 1;
            if (isEndOfLineOffset(layout, i5)) {
                int lineRight2 = (int) layout.getLineRight(lineForVertical);
                return i > lineRight2 - ((lineRight2 - ((int) layout.getPrimaryHorizontal(offsetForHorizontal))) / 2) ? i5 : offsetForHorizontal;
            }
            return offsetForHorizontal;
        }
        return offsetForHorizontal;
    }

    private static boolean isEndOfLineOffset(Layout layout, int i) {
        return i > 0 && layout.getLineForOffset(i) == layout.getLineForOffset(i - 1) + 1;
    }

    public static int dp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
