package com.ccr.achenglibrary.photopicker.adapter;

import android.content.Context;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnRVItemClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnRVItemLongClickListener;

/* loaded from: classes.dex */
public class CCRRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    protected Context mContext;
    protected CCROnRVItemClickListener mOnRVItemClickListener;
    protected CCROnRVItemLongClickListener mOnRVItemLongClickListener;
    protected RecyclerView mRecyclerView;
    protected CCRRecyclerViewAdapter mRecyclerViewAdapter;
    protected CCRViewHolderHelper mViewHolderHelper;

    public CCRRecyclerViewHolder(CCRRecyclerViewAdapter cCRRecyclerViewAdapter, RecyclerView recyclerView, View view, CCROnRVItemClickListener cCROnRVItemClickListener, CCROnRVItemLongClickListener cCROnRVItemLongClickListener) {
        super(view);
        this.mRecyclerViewAdapter = cCRRecyclerViewAdapter;
        this.mRecyclerView = recyclerView;
        this.mContext = recyclerView.getContext();
        this.mOnRVItemClickListener = cCROnRVItemClickListener;
        this.mOnRVItemLongClickListener = cCROnRVItemLongClickListener;
        view.setOnClickListener(new CCROnNoDoubleClickListener() { // from class: com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewHolder.1
            @Override // com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener
            public void onNoDoubleClick(View view2) {
                if (view2.getId() != CCRRecyclerViewHolder.this.itemView.getId() || CCRRecyclerViewHolder.this.mOnRVItemClickListener == null) {
                    return;
                }
                CCRRecyclerViewHolder.this.mOnRVItemClickListener.onRVItemClick(CCRRecyclerViewHolder.this.mRecyclerView, view2, CCRRecyclerViewHolder.this.getAdapterPositionWrapper());
            }
        });
        view.setOnLongClickListener(this);
        this.mViewHolderHelper = new CCRViewHolderHelper(this.mRecyclerView, this);
    }

    public CCRViewHolderHelper getViewHolderHelper() {
        return this.mViewHolderHelper;
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        CCROnRVItemLongClickListener cCROnRVItemLongClickListener;
        if (view.getId() != this.itemView.getId() || (cCROnRVItemLongClickListener = this.mOnRVItemLongClickListener) == null) {
            return false;
        }
        return cCROnRVItemLongClickListener.onRVItemLongClick(this.mRecyclerView, view, getAdapterPositionWrapper());
    }

    public int getAdapterPositionWrapper() {
        if (this.mRecyclerViewAdapter.getHeadersCount() > 0) {
            return getAdapterPosition() - this.mRecyclerViewAdapter.getHeadersCount();
        }
        return getAdapterPosition();
    }
}
