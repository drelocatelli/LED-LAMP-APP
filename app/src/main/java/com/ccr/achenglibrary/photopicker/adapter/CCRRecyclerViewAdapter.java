package com.ccr.achenglibrary.photopicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.ccr.achenglibrary.photopicker.listener.CCROnItemChildCheckedChangeListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnItemChildClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnItemChildLongClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnRVItemChildTouchListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnRVItemClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnRVItemLongClickListener;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public abstract class CCRRecyclerViewAdapter<M> extends RecyclerView.Adapter<CCRRecyclerViewHolder> {
    protected Context mContext;
    protected List<M> mData;
    protected int mDefaultItemLayoutId;
    protected CCRHeaderAndFooterAdapter mHeaderAndFooterAdapter;
    private boolean mIsIgnoreCheckedChanged;
    protected CCROnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;
    protected CCROnItemChildClickListener mOnItemChildClickListener;
    protected CCROnItemChildLongClickListener mOnItemChildLongClickListener;
    protected CCROnRVItemChildTouchListener mOnRVItemChildTouchListener;
    protected CCROnRVItemClickListener mOnRVItemClickListener;
    protected CCROnRVItemLongClickListener mOnRVItemLongClickListener;
    protected RecyclerView mRecyclerView;

    protected abstract void fillData(CCRViewHolderHelper cCRViewHolderHelper, int i, M m);

    protected void setItemChildListener(CCRViewHolderHelper cCRViewHolderHelper, int i) {
    }

    public CCRRecyclerViewAdapter(RecyclerView recyclerView) {
        this.mIsIgnoreCheckedChanged = true;
        this.mRecyclerView = recyclerView;
        this.mContext = recyclerView.getContext();
        this.mData = new ArrayList();
    }

    public CCRRecyclerViewAdapter(RecyclerView recyclerView, int i) {
        this(recyclerView);
        this.mDefaultItemLayoutId = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mData.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public CCRRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CCRRecyclerViewHolder cCRRecyclerViewHolder = new CCRRecyclerViewHolder(this, this.mRecyclerView, LayoutInflater.from(this.mContext).inflate(i, viewGroup, false), this.mOnRVItemClickListener, this.mOnRVItemLongClickListener);
        cCRRecyclerViewHolder.getViewHolderHelper().setOnItemChildClickListener(this.mOnItemChildClickListener);
        cCRRecyclerViewHolder.getViewHolderHelper().setOnItemChildLongClickListener(this.mOnItemChildLongClickListener);
        cCRRecyclerViewHolder.getViewHolderHelper().setOnItemChildCheckedChangeListener(this.mOnItemChildCheckedChangeListener);
        cCRRecyclerViewHolder.getViewHolderHelper().setOnRVItemChildTouchListener(this.mOnRVItemChildTouchListener);
        setItemChildListener(cCRRecyclerViewHolder.getViewHolderHelper(), i);
        return cCRRecyclerViewHolder;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        int i2 = this.mDefaultItemLayoutId;
        if (i2 != 0) {
            return i2;
        }
        throw new RuntimeException("请在 " + getClass().getSimpleName() + " 中重写 getItemViewType 方法返回布局资源 id，或者使用 CCRRecyclerViewAdapter 两个参数的构造方法 CCRRecyclerViewAdapter(RecyclerView recyclerView, int itemLayoutId)");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(CCRRecyclerViewHolder cCRRecyclerViewHolder, int i) {
        this.mIsIgnoreCheckedChanged = true;
        fillData(cCRRecyclerViewHolder.getViewHolderHelper(), i, getItem(i));
        this.mIsIgnoreCheckedChanged = false;
    }

    public boolean isIgnoreCheckedChanged() {
        return this.mIsIgnoreCheckedChanged;
    }

    public void setOnRVItemClickListener(CCROnRVItemClickListener cCROnRVItemClickListener) {
        this.mOnRVItemClickListener = cCROnRVItemClickListener;
    }

    public void setOnRVItemLongClickListener(CCROnRVItemLongClickListener cCROnRVItemLongClickListener) {
        this.mOnRVItemLongClickListener = cCROnRVItemLongClickListener;
    }

    public void setOnItemChildClickListener(CCROnItemChildClickListener cCROnItemChildClickListener) {
        this.mOnItemChildClickListener = cCROnItemChildClickListener;
    }

    public void setOnItemChildLongClickListener(CCROnItemChildLongClickListener cCROnItemChildLongClickListener) {
        this.mOnItemChildLongClickListener = cCROnItemChildLongClickListener;
    }

    public void setOnItemChildCheckedChangeListener(CCROnItemChildCheckedChangeListener cCROnItemChildCheckedChangeListener) {
        this.mOnItemChildCheckedChangeListener = cCROnItemChildCheckedChangeListener;
    }

    public void setOnRVItemChildTouchListener(CCROnRVItemChildTouchListener cCROnRVItemChildTouchListener) {
        this.mOnRVItemChildTouchListener = cCROnRVItemChildTouchListener;
    }

    public M getItem(int i) {
        return this.mData.get(i);
    }

    public List<M> getData() {
        return this.mData;
    }

    public final void notifyItemRangeInsertedWrapper(int i, int i2) {
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter == null) {
            notifyItemRangeInserted(i, i2);
        } else {
            cCRHeaderAndFooterAdapter.notifyItemRangeInserted(cCRHeaderAndFooterAdapter.getHeadersCount() + i, i2);
        }
    }

    public void addNewData(List<M> list) {
        if (list != null) {
            this.mData.addAll(0, list);
            notifyItemRangeInsertedWrapper(0, list.size());
        }
    }

    public void addMoreData(List<M> list) {
        if (list != null) {
            List<M> list2 = this.mData;
            list2.addAll(list2.size(), list);
            notifyItemRangeInsertedWrapper(this.mData.size(), list.size());
        }
    }

    public final void notifyDataSetChangedWrapper() {
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter == null) {
            notifyDataSetChanged();
        } else {
            cCRHeaderAndFooterAdapter.notifyDataSetChanged();
        }
    }

    public void setData(List<M> list) {
        if (list != null) {
            this.mData = list;
        } else {
            this.mData.clear();
        }
        notifyDataSetChangedWrapper();
    }

    public void clear() {
        this.mData.clear();
        notifyDataSetChangedWrapper();
    }

    public final void notifyItemRemovedWrapper(int i) {
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter == null) {
            notifyItemRemoved(i);
        } else {
            cCRHeaderAndFooterAdapter.notifyItemRemoved(cCRHeaderAndFooterAdapter.getHeadersCount() + i);
        }
    }

    public void removeItem(int i) {
        this.mData.remove(i);
        notifyItemRemovedWrapper(i);
    }

    public void removeItem(RecyclerView.ViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter != null) {
            this.mData.remove(adapterPosition - cCRHeaderAndFooterAdapter.getHeadersCount());
            this.mHeaderAndFooterAdapter.notifyItemRemoved(adapterPosition);
            return;
        }
        removeItem(adapterPosition);
    }

    public void removeItem(M m) {
        removeItem(this.mData.indexOf(m));
    }

    public final void notifyItemInsertedWrapper(int i) {
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter == null) {
            notifyItemInserted(i);
        } else {
            cCRHeaderAndFooterAdapter.notifyItemInserted(cCRHeaderAndFooterAdapter.getHeadersCount() + i);
        }
    }

    public void addItem(int i, M m) {
        this.mData.add(i, m);
        notifyItemInsertedWrapper(i);
    }

    public void addFirstItem(M m) {
        addItem(0, m);
    }

    public void addLastItem(M m) {
        addItem(this.mData.size(), m);
    }

    public final void notifyItemChangedWrapper(int i) {
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter == null) {
            notifyItemChanged(i);
        } else {
            cCRHeaderAndFooterAdapter.notifyItemChanged(cCRHeaderAndFooterAdapter.getHeadersCount() + i);
        }
    }

    public void setItem(int i, M m) {
        this.mData.set(i, m);
        notifyItemChangedWrapper(i);
    }

    public void setItem(M m, M m2) {
        setItem(this.mData.indexOf(m), (int) m2);
    }

    public final void notifyItemMovedWrapper(int i, int i2) {
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter == null) {
            notifyItemMoved(i, i2);
        } else {
            cCRHeaderAndFooterAdapter.notifyItemMoved(cCRHeaderAndFooterAdapter.getHeadersCount() + i, this.mHeaderAndFooterAdapter.getHeadersCount() + i2);
        }
    }

    public void moveItem(int i, int i2) {
        notifyItemChangedWrapper(i);
        notifyItemChangedWrapper(i2);
        List<M> list = this.mData;
        list.add(i2, list.remove(i));
        notifyItemMovedWrapper(i, i2);
    }

    public void moveItem(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        int adapterPosition = viewHolder.getAdapterPosition();
        int adapterPosition2 = viewHolder2.getAdapterPosition();
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter != null) {
            cCRHeaderAndFooterAdapter.notifyItemChanged(adapterPosition);
            this.mHeaderAndFooterAdapter.notifyItemChanged(adapterPosition2);
            this.mData.add(adapterPosition2 - this.mHeaderAndFooterAdapter.getHeadersCount(), this.mData.remove(adapterPosition - this.mHeaderAndFooterAdapter.getHeadersCount()));
            this.mHeaderAndFooterAdapter.notifyItemMoved(adapterPosition, adapterPosition2);
            return;
        }
        moveItem(adapterPosition, adapterPosition2);
    }

    public M getFirstItem() {
        if (getItemCount() > 0) {
            return getItem(0);
        }
        return null;
    }

    public M getLastItem() {
        if (getItemCount() > 0) {
            return getItem(getItemCount() - 1);
        }
        return null;
    }

    public void addHeaderView(View view) {
        getHeaderAndFooterAdapter().addHeaderView(view);
    }

    public void addFooterView(View view) {
        getHeaderAndFooterAdapter().addFooterView(view);
    }

    public int getHeadersCount() {
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter == null) {
            return 0;
        }
        return cCRHeaderAndFooterAdapter.getHeadersCount();
    }

    public int getFootersCount() {
        CCRHeaderAndFooterAdapter cCRHeaderAndFooterAdapter = this.mHeaderAndFooterAdapter;
        if (cCRHeaderAndFooterAdapter == null) {
            return 0;
        }
        return cCRHeaderAndFooterAdapter.getFootersCount();
    }

    public CCRHeaderAndFooterAdapter getHeaderAndFooterAdapter() {
        if (this.mHeaderAndFooterAdapter == null) {
            synchronized (this) {
                if (this.mHeaderAndFooterAdapter == null) {
                    this.mHeaderAndFooterAdapter = new CCRHeaderAndFooterAdapter(this);
                }
            }
        }
        return this.mHeaderAndFooterAdapter;
    }

    public boolean isHeaderOrFooter(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() < getHeadersCount() || viewHolder.getAdapterPosition() >= getHeadersCount() + getItemCount();
    }
}
