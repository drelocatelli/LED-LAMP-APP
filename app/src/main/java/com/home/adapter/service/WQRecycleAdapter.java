package com.home.adapter.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes.dex */
public class WQRecycleAdapter extends RecyclerView.Adapter<WQViewHolder> {
    private boolean clickFlag = true;
    public Context context;
    private List<? extends Object> data;
    private int layoutId;
    CallBack mCallBack;
    private OnItemClickListner onItemClickListner;
    private OnItemLongClickListner onItemLongClickListner;

    /* loaded from: classes.dex */
    public interface CallBack {
        <T> void convert(WQViewHolder wQViewHolder, T t, int i);
    }

    /* loaded from: classes.dex */
    public interface OnItemClickListner {
        void onItemClickListner(View view, int i);
    }

    /* loaded from: classes.dex */
    public interface OnItemLongClickListner {
        void onItemLongClickListner(View view, int i);
    }

    public WQRecycleAdapter(Context context, int i, List<? extends Object> list) {
        this.layoutId = i;
        this.data = list;
        this.context = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public WQViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(this.layoutId, viewGroup, false);
        final WQViewHolder wQViewHolder = new WQViewHolder(inflate, this.context);
        inflate.setOnClickListener(new View.OnClickListener() { // from class: com.home.adapter.service.WQRecycleAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (WQRecycleAdapter.this.onItemClickListner != null) {
                    if (WQRecycleAdapter.this.clickFlag) {
                        WQRecycleAdapter.this.onItemClickListner.onItemClickListner(view, wQViewHolder.getLayoutPosition());
                    }
                    WQRecycleAdapter.this.clickFlag = true;
                }
                WQRecycleAdapter.this.clickFlag = true;
            }
        });
        inflate.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.home.adapter.service.WQRecycleAdapter.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                if (WQRecycleAdapter.this.onItemLongClickListner == null) {
                    return false;
                }
                WQRecycleAdapter.this.onItemLongClickListner.onItemLongClickListner(view, wQViewHolder.getLayoutPosition());
                WQRecycleAdapter.this.clickFlag = false;
                return false;
            }
        });
        return wQViewHolder;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(WQViewHolder wQViewHolder, int i) {
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.convert(wQViewHolder, this.data.get(i), i);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.data.size();
    }

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public void setOnItemLongClickListner(OnItemLongClickListner onItemLongClickListner) {
        this.onItemLongClickListner = onItemLongClickListner;
    }

    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }
}
