package com.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.home.bean.AdapterBean;
import com.ledlamp.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ModelAdapter extends BaseAdapter {
    private Context context;
    private int index = -1;
    private ArrayList<AdapterBean> list;

    public ModelAdapter(Context context, ArrayList<AdapterBean> arrayList) {
        this.list = arrayList;
        this.context = context;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return Integer.parseInt(this.list.get(i).getValue().trim());
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = View.inflate(this.context, R.layout.item_model, null);
            viewHolder.textView = (TextView) view2.findViewById(R.id.textViewLabelId);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(this.list.get(i).getLabel());
        if (this.index == i) {
            viewHolder.textView.setTextColor(this.context.getResources().getColor(R.color.blue_text_color));
        } else {
            viewHolder.textView.setTextColor(this.context.getResources().getColor(17170443));
        }
        return view2;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    /* loaded from: classes.dex */
    class ViewHolder {
        TextView textView;

        ViewHolder() {
        }
    }
}
