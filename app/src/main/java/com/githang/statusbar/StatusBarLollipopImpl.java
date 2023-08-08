package com.githang.statusbar;

import android.view.Window;

/* loaded from: classes.dex */
class StatusBarLollipopImpl implements IStatusBar {
    @Override // com.githang.statusbar.IStatusBar
    public void setStatusBarColor(Window window, int i) {
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(i);
    }
}
