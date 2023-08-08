package com.common.uitl;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class PagerLayoutManager extends LinearLayoutManager {
    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener;
    private int mDrift;
    private OnViewPagerListener mOnViewPagerListener;
    private PagerSnapHelper mPagerSnapHelper;
    private RecyclerView mRecyclerView;

    public PagerLayoutManager(Context context, int i) {
        super(context, i, false);
        this.mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() { // from class: com.common.uitl.PagerLayoutManager.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
            public void onChildViewAttachedToWindow(View view) {
                if (PagerLayoutManager.this.mOnViewPagerListener == null || PagerLayoutManager.this.getChildCount() != 1) {
                    return;
                }
                PagerLayoutManager.this.mOnViewPagerListener.onInitComplete(view);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
            public void onChildViewDetachedFromWindow(View view) {
                if (PagerLayoutManager.this.mDrift >= 0) {
                    if (PagerLayoutManager.this.mOnViewPagerListener != null) {
                        PagerLayoutManager.this.mOnViewPagerListener.onPageRelease(true, PagerLayoutManager.this.getPosition(view), view);
                    }
                } else if (PagerLayoutManager.this.mOnViewPagerListener != null) {
                    PagerLayoutManager.this.mOnViewPagerListener.onPageRelease(false, PagerLayoutManager.this.getPosition(view), view);
                }
            }
        };
        init();
    }

    public PagerLayoutManager(Context context, int i, boolean z) {
        super(context, i, z);
        this.mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() { // from class: com.common.uitl.PagerLayoutManager.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
            public void onChildViewAttachedToWindow(View view) {
                if (PagerLayoutManager.this.mOnViewPagerListener == null || PagerLayoutManager.this.getChildCount() != 1) {
                    return;
                }
                PagerLayoutManager.this.mOnViewPagerListener.onInitComplete(view);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
            public void onChildViewDetachedFromWindow(View view) {
                if (PagerLayoutManager.this.mDrift >= 0) {
                    if (PagerLayoutManager.this.mOnViewPagerListener != null) {
                        PagerLayoutManager.this.mOnViewPagerListener.onPageRelease(true, PagerLayoutManager.this.getPosition(view), view);
                    }
                } else if (PagerLayoutManager.this.mOnViewPagerListener != null) {
                    PagerLayoutManager.this.mOnViewPagerListener.onPageRelease(false, PagerLayoutManager.this.getPosition(view), view);
                }
            }
        };
        init();
    }

    private void init() {
        this.mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onAttachedToWindow(RecyclerView recyclerView) {
        super.onAttachedToWindow(recyclerView);
        this.mPagerSnapHelper.attachToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
        recyclerView.addOnChildAttachStateChangeListener(this.mChildAttachStateChangeListener);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onScrollStateChanged(int i) {
        if (i == 0) {
            Log.e("=====", "滑动监听");
            View findSnapView = this.mPagerSnapHelper.findSnapView(this);
            if (findSnapView != null) {
                int position = getPosition(findSnapView);
                if (this.mOnViewPagerListener == null || getChildCount() != 1) {
                    return;
                }
                this.mOnViewPagerListener.onPageSelected(position, position == getItemCount() - 1, findSnapView);
            }
        } else if (i == 1) {
            Log.e("=====", "滑动监听111");
            View findSnapView2 = this.mPagerSnapHelper.findSnapView(this);
            if (findSnapView2 != null) {
                getPosition(findSnapView2);
            }
        } else if (i != 2) {
        } else {
            Log.e("=====", "滑动监听222");
            View findSnapView3 = this.mPagerSnapHelper.findSnapView(this);
            if (findSnapView3 != null) {
                getPosition(findSnapView3);
            }
        }
    }

    public void setPosition() {
        View findSnapView = this.mPagerSnapHelper.findSnapView(this);
        if (findSnapView != null) {
            int position = getPosition(findSnapView);
            if (this.mOnViewPagerListener != null) {
                if (getChildCount() == 1) {
                    this.mOnViewPagerListener.onPageSelected(position, position == getItemCount() - 1, findSnapView);
                }
            }
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = i;
        return super.scrollVerticallyBy(i, recycler, state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = i;
        return super.scrollHorizontallyBy(i, recycler, state);
    }

    public void setOnViewPagerListener(OnViewPagerListener onViewPagerListener) {
        this.mOnViewPagerListener = onViewPagerListener;
    }
}
