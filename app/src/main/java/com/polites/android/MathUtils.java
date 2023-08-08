package com.polites.android;

import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;

/* loaded from: classes.dex */
public class MathUtils {
    public static float distance(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return FloatMath.sqrt((x * x) + (y * y));
    }

    public static float distance(PointF pointF, PointF pointF2) {
        float f = pointF.x - pointF2.x;
        float f2 = pointF.y - pointF2.y;
        return FloatMath.sqrt((f * f) + (f2 * f2));
    }

    public static float distance(float f, float f2, float f3, float f4) {
        float f5 = f - f3;
        float f6 = f2 - f4;
        return FloatMath.sqrt((f5 * f5) + (f6 * f6));
    }

    public static void midpoint(MotionEvent motionEvent, PointF pointF) {
        midpoint(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1), pointF);
    }

    public static void midpoint(float f, float f2, float f3, float f4, PointF pointF) {
        pointF.x = (f + f3) / 2.0f;
        pointF.y = (f2 + f4) / 2.0f;
    }

    public void rotate(PointF pointF, PointF pointF2, float f) {
        float f2 = pointF.x;
        float f3 = pointF.y;
        float f4 = pointF2.x;
        float f5 = pointF2.y;
        float f6 = f2 - f4;
        float f7 = f3 - f5;
        pointF.x = ((FloatMath.cos(f) * f6) - (FloatMath.sin(f) * f7)) + f4;
        pointF.y = (FloatMath.sin(f) * f6) + (FloatMath.cos(f) * f7) + f5;
    }

    public static float angle(PointF pointF, PointF pointF2) {
        return angle(pointF.x, pointF.y, pointF2.x, pointF2.y);
    }

    public static float angle(float f, float f2, float f3, float f4) {
        return (float) Math.atan2(f4 - f2, f3 - f);
    }
}
