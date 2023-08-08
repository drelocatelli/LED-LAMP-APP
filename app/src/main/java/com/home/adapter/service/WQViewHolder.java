package com.home.adapter.service;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes.dex */
public class WQViewHolder extends RecyclerView.ViewHolder {
    Context context;
    View convertView;

    /* loaded from: classes.dex */
    public interface OnClickListener {
        void onClickListner(View view);
    }

    public WQViewHolder(View view, Context context) {
        super(view);
        this.convertView = view;
        this.context = context;
    }

    public View getItemView() {
        return this.convertView;
    }

    public void setText(int i, String str) {
        ((TextView) this.convertView.findViewById(i)).setText(str);
    }

    public void setText(int i, String str, final OnClickListener onClickListener) {
        TextView textView = (TextView) this.convertView.findViewById(i);
        textView.setText(str);
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.home.adapter.service.WQViewHolder.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                onClickListener.onClickListner(view);
            }
        });
    }

    public View getView(int i) {
        return this.convertView.findViewById(i);
    }
}
