package com.forum.im.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/* loaded from: classes.dex */
public class KeyBoardUtils {
    public static void hideKeyBoard(Context context, View view) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyBoard(Context context, View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.findFocus();
        ((InputMethodManager) context.getSystemService("input_method")).toggleSoftInput(0, 2);
    }
}
