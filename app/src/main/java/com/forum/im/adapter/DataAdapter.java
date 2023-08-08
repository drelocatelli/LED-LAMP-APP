package com.forum.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class DataAdapter extends BaseAdapter {
    private Context context;
    private String[] title;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public DataAdapter(Context context, String[] strArr) {
        this.context = context;
        this.title = strArr;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.title.length;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.title[i];
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = LayoutInflater.from(this.context).inflate(R.layout.layout_mess_iv_listitem, (ViewGroup) null);
            viewHolder.money_Tv = (TextView) view2.findViewById(R.id.title);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.money_Tv.setText(this.title[i]);
        return view2;
    }

    /* loaded from: classes.dex */
    class ViewHolder {
        TextView money_Tv;

        ViewHolder() {
        }
    }
}
