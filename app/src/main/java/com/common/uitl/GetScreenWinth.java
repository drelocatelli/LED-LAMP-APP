package com.common.uitl;

import android.util.DisplayMetrics;
import androidx.fragment.app.FragmentActivity;

/* loaded from: classes.dex */
public class GetScreenWinth {
    public static int getWinth(FragmentActivity fragmentActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getHeight(FragmentActivity fragmentActivity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        fragmentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getPx(int i, FragmentActivity fragmentActivity) {
        return (int) ((i * fragmentActivity.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getDp(int i, FragmentActivity fragmentActivity) {
        return (int) ((i / fragmentActivity.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
