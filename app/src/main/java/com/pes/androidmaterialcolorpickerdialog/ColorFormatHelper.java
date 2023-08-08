package com.pes.androidmaterialcolorpickerdialog;

/* loaded from: classes.dex */
final class ColorFormatHelper {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int assertColorValueInRange(int i) {
        if (i < 0 || i > 255) {
            return 0;
        }
        return i;
    }

    ColorFormatHelper() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatColorValues(int i, int i2, int i3) {
        return String.format("%02X%02X%02X", Integer.valueOf(assertColorValueInRange(i)), Integer.valueOf(assertColorValueInRange(i2)), Integer.valueOf(assertColorValueInRange(i3)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String formatColorValues(int i, int i2, int i3, int i4) {
        return String.format("%02X%02X%02X%02X", Integer.valueOf(assertColorValueInRange(i)), Integer.valueOf(assertColorValueInRange(i2)), Integer.valueOf(assertColorValueInRange(i3)), Integer.valueOf(assertColorValueInRange(i4)));
    }
}
