package com.common.uitl;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/* loaded from: classes.dex */
public class SoftKeyHideShow {
    public static void HideShowSoftKey(Context context) {
        ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(0, 2);
    }
}
