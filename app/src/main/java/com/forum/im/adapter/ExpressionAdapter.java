package com.forum.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.ledlamp.R;
import java.util.List;

/* loaded from: classes.dex */
public class ExpressionAdapter extends ArrayAdapter<String> {
    public ExpressionAdapter(Context context, int i, List<String> list) {
        super(context, i, list);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(getContext(), R.layout.layout_row_expression, null);
        }
        ((ImageView) view.findViewById(R.id.iv_expression)).setImageResource(getContext().getResources().getIdentifier(getItem(i), "mipmap", getContext().getPackageName()));
        return view;
    }
}
