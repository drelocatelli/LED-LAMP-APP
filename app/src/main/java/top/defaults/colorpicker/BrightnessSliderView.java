package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

/* loaded from: classes.dex */
public class BrightnessSliderView extends ColorSliderView {
    public BrightnessSliderView(Context context) {
        super(context);
    }

    public BrightnessSliderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BrightnessSliderView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // top.defaults.colorpicker.ColorSliderView
    protected float resolveValue(int i) {
        float[] fArr = new float[3];
        Color.colorToHSV(i, fArr);
        return fArr[2];
    }

    @Override // top.defaults.colorpicker.ColorSliderView
    protected void configurePaint(Paint paint) {
        Color.colorToHSV(this.baseColor, r0);
        float[] fArr = {0.0f, 0.0f, 0.0f};
        int HSVToColor = Color.HSVToColor(fArr);
        fArr[2] = 1.0f;
        paint.setShader(new LinearGradient(0.0f, 0.0f, getWidth(), getHeight(), HSVToColor, Color.HSVToColor(fArr), Shader.TileMode.CLAMP));
    }

    @Override // top.defaults.colorpicker.ColorSliderView
    protected int assembleColor() {
        Color.colorToHSV(this.baseColor, r0);
        float[] fArr = {0.0f, 0.0f, this.currentValue};
        return Color.HSVToColor(fArr);
    }
}
