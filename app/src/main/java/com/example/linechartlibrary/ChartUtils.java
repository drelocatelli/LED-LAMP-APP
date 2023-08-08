package com.example.linechartlibrary;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

/* loaded from: classes.dex */
public abstract class ChartUtils {
    public static final int[] COLORS;
    public static final int COLOR_BLUE;
    public static final int COLOR_GREEN;
    private static int COLOR_INDEX = 0;
    public static final int COLOR_ORANGE;
    public static final int COLOR_RED;
    public static final int COLOR_VIOLET;
    private static final float DARKEN_INTENSITY = 0.9f;
    private static final float DARKEN_SATURATION = 1.1f;
    public static final int DEFAULT_COLOR = Color.parseColor("#DFDFDF");
    public static final int DEFAULT_DARKEN_COLOR = Color.parseColor("#DDDDDD");

    public static int dp2px(float f, int i) {
        if (i == 0) {
            return 0;
        }
        return (int) ((i * f) + 0.5f);
    }

    public static int sp2px(float f, int i) {
        if (i == 0) {
            return 0;
        }
        return (int) ((i * f) + 0.5f);
    }

    static {
        int parseColor = Color.parseColor("#33B5E5");
        COLOR_BLUE = parseColor;
        int parseColor2 = Color.parseColor("#AA66CC");
        COLOR_VIOLET = parseColor2;
        int parseColor3 = Color.parseColor("#99CC00");
        COLOR_GREEN = parseColor3;
        int parseColor4 = Color.parseColor("#FFBB33");
        COLOR_ORANGE = parseColor4;
        int parseColor5 = Color.parseColor("#FF4444");
        COLOR_RED = parseColor5;
        COLORS = new int[]{parseColor, parseColor2, parseColor3, parseColor4, parseColor5};
        COLOR_INDEX = 0;
    }

    public static final int pickColor() {
        int[] iArr = COLORS;
        double random = Math.random();
        double length = iArr.length - 1;
        Double.isNaN(length);
        return iArr[(int) Math.round(random * length)];
    }

    public static final int nextColor() {
        int i = COLOR_INDEX;
        int[] iArr = COLORS;
        if (i >= iArr.length) {
            COLOR_INDEX = 0;
        }
        int i2 = COLOR_INDEX;
        COLOR_INDEX = i2 + 1;
        return iArr[i2];
    }

    public static int px2dp(float f, int i) {
        return (int) Math.ceil(i / f);
    }

    public static int px2sp(float f, int i) {
        return (int) Math.ceil(i / f);
    }

    public static int mm2px(Context context, int i) {
        return (int) (TypedValue.applyDimension(5, i, context.getResources().getDisplayMetrics()) + 0.5f);
    }

    public static int darkenColor(int i) {
        int alpha = Color.alpha(i);
        Color.colorToHSV(i, r0);
        float[] fArr = {0.0f, Math.min(fArr[1] * DARKEN_SATURATION, 1.0f), fArr[2] * DARKEN_INTENSITY};
        int HSVToColor = Color.HSVToColor(fArr);
        return Color.argb(alpha, Color.red(HSVToColor), Color.green(HSVToColor), Color.blue(HSVToColor));
    }
}
