package com.common.uitl;

import android.view.View;

/* loaded from: classes.dex */
public interface OnViewPagerListener {
    void onInitComplete(View view);

    void onPageRelease(boolean z, int i, View view);

    void onPageSelected(int i, boolean z, View view);
}
