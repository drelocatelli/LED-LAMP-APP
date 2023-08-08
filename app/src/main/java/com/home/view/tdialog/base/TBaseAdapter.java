package com.home.view.tdialog.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.home.view.tdialog.TDialog;
import java.util.List;

/* loaded from: classes.dex */
public abstract class TBaseAdapter<T> extends RecyclerView.Adapter<BindViewHolder> {
    private OnAdapterItemClickListener adapterItemClickListener;
    private List<T> datas;
    private TDialog dialog;
    private final int layoutRes;

    /* loaded from: classes.dex */
    public interface OnAdapterItemClickListener<T> {
        void onItemClick(BindViewHolder bindViewHolder, int i, T t, TDialog tDialog);
    }

    protected abstract void onBind(BindViewHolder bindViewHolder, int i, T t);

    public TBaseAdapter(int i, List<T> list) {
        this.layoutRes = i;
        this.datas = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public BindViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BindViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.layoutRes, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final BindViewHolder bindViewHolder, final int i) {
        onBind(bindViewHolder, i, this.datas.get(i));
        bindViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.home.view.tdialog.base.TBaseAdapter.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TBaseAdapter.this.adapterItemClickListener.onItemClick(bindViewHolder, i, TBaseAdapter.this.datas.get(i), TBaseAdapter.this.dialog);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.datas.size();
    }

    public void setTDialog(TDialog tDialog) {
        this.dialog = tDialog;
    }

    public void setOnAdapterItemClickListener(OnAdapterItemClickListener onAdapterItemClickListener) {
        this.adapterItemClickListener = onAdapterItemClickListener;
    }
}
