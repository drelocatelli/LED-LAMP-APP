package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import top.defaults.checkerboarddrawable.CheckerboardDrawable;

/* loaded from: classes.dex */
public class AlphaSliderView extends ColorSliderView {
    private Bitmap backgroundBitmap;
    private Canvas backgroundCanvas;

    public AlphaSliderView(Context context) {
        super(context);
    }

    public AlphaSliderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AlphaSliderView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // top.defaults.colorpicker.ColorSliderView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.backgroundBitmap = Bitmap.createBitmap((int) (i - (this.selectorSize * 2.0f)), (int) (i2 - this.selectorSize), Bitmap.Config.ARGB_8888);
        this.backgroundCanvas = new Canvas(this.backgroundBitmap);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // top.defaults.colorpicker.ColorSliderView, android.view.View
    public void onDraw(Canvas canvas) {
        CheckerboardDrawable create = CheckerboardDrawable.create();
        create.setBounds(0, 0, this.backgroundCanvas.getWidth(), this.backgroundCanvas.getHeight());
        create.draw(this.backgroundCanvas);
        canvas.drawBitmap(this.backgroundBitmap, this.selectorSize, this.selectorSize, (Paint) null);
        super.onDraw(canvas);
    }

    @Override // top.defaults.colorpicker.ColorSliderView
    protected float resolveValue(int i) {
        return Color.alpha(i) / 255.0f;
    }

    @Override // top.defaults.colorpicker.ColorSliderView
    protected void configurePaint(Paint paint) {
        float[] fArr = new float[3];
        Color.colorToHSV(this.baseColor, fArr);
        paint.setShader(new LinearGradient(0.0f, 0.0f, getWidth(), getHeight(), Color.HSVToColor(0, fArr), Color.HSVToColor(255, fArr), Shader.TileMode.CLAMP));
    }

    @Override // top.defaults.colorpicker.ColorSliderView
    protected int assembleColor() {
        float[] fArr = new float[3];
        Color.colorToHSV(this.baseColor, fArr);
        return Color.HSVToColor((int) (this.currentValue * 255.0f), fArr);
    }
}
