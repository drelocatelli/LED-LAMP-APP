package com.ccr.achenglibrary.photopicker.util;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class CCRSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;

    public CCRSpaceItemDecoration(int i) {
        this.mSpace = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        rect.left = this.mSpace;
        rect.right = this.mSpace;
        rect.top = this.mSpace;
        rect.bottom = this.mSpace;
    }
}
