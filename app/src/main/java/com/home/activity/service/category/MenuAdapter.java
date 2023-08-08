package com.home.activity.service.category;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ledlamp.R;
import java.util.List;

/* loaded from: classes.dex */
public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private int selectItem = 0;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MenuAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    public int getSelectItem() {
        return this.selectItem;
    }

    public void setSelectItem(int i) {
        this.selectItem = i;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = View.inflate(this.context, R.layout.item_service_left, null);
            viewHolder.tv_name = (TextView) view2.findViewById(R.id.item_name);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i == this.selectItem) {
            viewHolder.tv_name.setBackgroundColor(-1);
            viewHolder.tv_name.setTextColor(this.context.getResources().getColor(R.color.colorPrimary));
        } else {
            viewHolder.tv_name.setBackgroundColor(this.context.getResources().getColor(R.color.grayLight));
            viewHolder.tv_name.setTextColor(this.context.getResources().getColor(R.color.grayDark));
        }
        viewHolder.tv_name.setText(this.list.get(i));
        return view2;
    }

    /* loaded from: classes.dex */
    static class ViewHolder {
        private TextView tv_name;

        ViewHolder() {
        }
    }
}
