package com.forum.im.widget.pulltorefresh;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class WrapContentLinearLayoutManager extends LinearLayoutManager {
    private int[] mMeasuredDimension;
    private RecyclerView.Recycler recycler;
    private int recyclerHeight;

    public WrapContentLinearLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
        this.mMeasuredDimension = new int[2];
        this.recyclerHeight = 0;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            this.recycler = recycler;
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("probe", "ERROR = " + e.getMessage());
        }
    }

    public int getRecyclerHeight() {
        if (this.recycler == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            measureScrapChild(this.recycler, i2, View.MeasureSpec.makeMeasureSpec(i2, 0), View.MeasureSpec.makeMeasureSpec(i2, 0), this.mMeasuredDimension);
            if (getOrientation() != 0) {
                i += this.mMeasuredDimension[1];
            } else if (i2 == 0) {
                i = this.mMeasuredDimension[1];
            }
        }
        this.recyclerHeight = i;
        return i;
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int i, int i2, int i3, int[] iArr) {
        try {
            View viewForPosition = recycler.getViewForPosition(0);
            if (viewForPosition != null) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewForPosition.getLayoutParams();
                viewForPosition.measure(ViewGroup.getChildMeasureSpec(i2, getPaddingLeft() + getPaddingRight(), layoutParams.width), ViewGroup.getChildMeasureSpec(i3, getPaddingTop() + getPaddingBottom(), layoutParams.height));
                iArr[0] = viewForPosition.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                iArr[1] = viewForPosition.getMeasuredHeight() + layoutParams.bottomMargin + layoutParams.topMargin;
                recycler.recycleView(viewForPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
