package com.yalantis.ucrop.immersion;

import android.os.Build;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes.dex */
public class CropImmersiveManage {
    public static boolean immersiveUseful() {
        return Build.VERSION.SDK_INT >= 23;
    }

    public static void immersiveAboveAPI23(AppCompatActivity appCompatActivity, int i, int i2, boolean z) {
        if (Build.VERSION.SDK_INT >= 23) {
            immersiveAboveAPI23(appCompatActivity, false, false, i, i2, z);
        }
    }

    public static void immersiveAboveAPI23(AppCompatActivity appCompatActivity, boolean z, boolean z2, int i, int i2, boolean z3) {
        try {
            Window window = appCompatActivity.getWindow();
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                window.setFlags(67108864, 67108864);
            } else if (Build.VERSION.SDK_INT >= 21) {
                if (z && z2) {
                    window.clearFlags(201326592);
                    CropLightStatusBarUtils.setLightStatusBar(appCompatActivity, z, z2, i == 0, z3);
                    window.addFlags(Integer.MIN_VALUE);
                } else if (!z && !z2) {
                    window.requestFeature(1);
                    window.clearFlags(201326592);
                    CropLightStatusBarUtils.setLightStatusBar(appCompatActivity, z, z2, i == 0, z3);
                    window.addFlags(Integer.MIN_VALUE);
                } else if (z || !z2) {
                    return;
                } else {
                    window.requestFeature(1);
                    window.clearFlags(201326592);
                    CropLightStatusBarUtils.setLightStatusBar(appCompatActivity, z, z2, i == 0, z3);
                    window.addFlags(Integer.MIN_VALUE);
                }
                window.setStatusBarColor(i);
                window.setNavigationBarColor(i2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
