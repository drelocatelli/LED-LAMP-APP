package com.ccr.achenglibrary.photopicker.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/* loaded from: classes.dex */
public class CCRHeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_FOOTER = 4096;
    private static final int BASE_ITEM_TYPE_HEADER = 2048;
    private RecyclerView.Adapter mInnerAdapter;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public CCRHeaderAndFooterAdapter(RecyclerView.Adapter adapter) {
        this.mInnerAdapter = adapter;
    }

    public RecyclerView.Adapter getInnerAdapter() {
        return this.mInnerAdapter;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.mHeaderViews.get(i) != null) {
            return new RecyclerView.ViewHolder(this.mHeaderViews.get(i)) { // from class: com.ccr.achenglibrary.photopicker.adapter.CCRHeaderAndFooterAdapter.1
            };
        }
        if (this.mFootViews.get(i) != null) {
            return new RecyclerView.ViewHolder(this.mFootViews.get(i)) { // from class: com.ccr.achenglibrary.photopicker.adapter.CCRHeaderAndFooterAdapter.2
            };
        }
        return this.mInnerAdapter.onCreateViewHolder(viewGroup, i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (isHeaderView(i)) {
            return this.mHeaderViews.keyAt(i);
        }
        if (isFooterView(i)) {
            return this.mFootViews.keyAt((i - getHeadersCount()) - getRealItemCount());
        }
        return this.mInnerAdapter.getItemViewType(getRealItemPosition(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (isHeaderViewOrFooterView(i)) {
            return;
        }
        this.mInnerAdapter.onBindViewHolder(viewHolder, getRealItemPosition(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.ccr.achenglibrary.photopicker.adapter.CCRHeaderAndFooterAdapter.3
                @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
                public int getSpanSize(int i) {
                    if (CCRHeaderAndFooterAdapter.this.isHeaderViewOrFooterView(i)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    GridLayoutManager.SpanSizeLookup spanSizeLookup2 = spanSizeLookup;
                    if (spanSizeLookup2 != null) {
                        return spanSizeLookup2.getSpanSize(i - CCRHeaderAndFooterAdapter.this.getHeadersCount());
                    }
                    return 1;
                }
            });
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        ViewGroup.LayoutParams layoutParams;
        this.mInnerAdapter.onViewAttachedToWindow(viewHolder);
        if (isHeaderViewOrFooterView(viewHolder.getLayoutPosition()) && (layoutParams = viewHolder.itemView.getLayoutParams()) != null && (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    public int getRealItemCount() {
        return this.mInnerAdapter.getItemCount();
    }

    public int getRealItemPosition(int i) {
        return i - getHeadersCount();
    }

    public boolean isHeaderView(int i) {
        return i < getHeadersCount();
    }

    public boolean isFooterView(int i) {
        return i >= getHeadersCount() + getRealItemCount();
    }

    public boolean isHeaderViewOrFooterView(int i) {
        return isHeaderView(i) || isFooterView(i);
    }

    public void addHeaderView(View view) {
        SparseArrayCompat<View> sparseArrayCompat = this.mHeaderViews;
        sparseArrayCompat.put(sparseArrayCompat.size() + 2048, view);
    }

    public void addFooterView(View view) {
        SparseArrayCompat<View> sparseArrayCompat = this.mFootViews;
        sparseArrayCompat.put(sparseArrayCompat.size() + 4096, view);
    }

    public int getHeadersCount() {
        return this.mHeaderViews.size();
    }

    public int getFootersCount() {
        return this.mFootViews.size();
    }
}
