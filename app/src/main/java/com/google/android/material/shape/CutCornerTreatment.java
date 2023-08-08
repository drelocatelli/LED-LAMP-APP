package com.google.android.material.shape;

/* loaded from: classes.dex */
public class CutCornerTreatment extends CornerTreatment {
    private final float size;

    public CutCornerTreatment(float f) {
        this.size = f;
    }

    @Override // com.google.android.material.shape.CornerTreatment
    public void getCornerPath(float f, float f2, ShapePath shapePath) {
        shapePath.reset(0.0f, this.size * f2);
        double d = f;
        double sin = Math.sin(d);
        double d2 = this.size;
        Double.isNaN(d2);
        double d3 = f2;
        Double.isNaN(d3);
        double cos = Math.cos(d);
        double d4 = this.size;
        Double.isNaN(d4);
        Double.isNaN(d3);
        shapePath.lineTo((float) (sin * d2 * d3), (float) (cos * d4 * d3));
    }
}
