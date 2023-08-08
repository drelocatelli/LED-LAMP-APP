package com.githang.statusbar;

import android.view.View;
import android.view.Window;

/* loaded from: classes.dex */
class StatusBarMImpl implements IStatusBar {
    @Override // com.githang.statusbar.IStatusBar
    public void setStatusBarColor(Window window, int i) {
        window.clearFlags(67108864);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(i);
        View findViewById = window.findViewById(16908290);
        if (findViewById != null) {
            findViewById.setForeground(null);
        }
    }
}
