package com.githang.statusbar;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

/* loaded from: classes.dex */
class StatusBarKitkatImpl implements IStatusBar {
    private static final String STATUS_BAR_VIEW_TAG = "ghStatusBarView";

    @Override // com.githang.statusbar.IStatusBar
    public void setStatusBarColor(Window window, int i) {
        window.addFlags(67108864);
        ViewGroup viewGroup = (ViewGroup) window.getDecorView();
        View findViewWithTag = viewGroup.findViewWithTag(STATUS_BAR_VIEW_TAG);
        if (findViewWithTag == null) {
            findViewWithTag = new StatusBarView(window.getContext());
            findViewWithTag.setTag(STATUS_BAR_VIEW_TAG);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
            layoutParams.gravity = 48;
            findViewWithTag.setLayoutParams(layoutParams);
            viewGroup.addView(findViewWithTag);
        }
        findViewWithTag.setBackgroundColor(i);
        StatusBarCompat.internalSetFitsSystemWindows(window, true);
    }
}
