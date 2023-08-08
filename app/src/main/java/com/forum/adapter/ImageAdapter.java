package com.forum.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";
    private List<String> listS;
    private Context mContext;

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public ImageAdapter(Context context, List<String> list) {
        new ArrayList();
        this.listS = list;
        this.mContext = context;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.listS.size();
    }

    @Override // android.widget.Adapter
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = View.inflate(viewGroup.getContext(), R.layout.item_image, null);
            viewHolder.imageView = (ImageView) view2.findViewById(R.id.imageView11);
            viewHolder.textView = (TextView) view2.findViewById(R.id.textview);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        String str = this.listS.get(i);
        if (i == 2 && this.listS.size() > 3) {
            viewHolder.textView.setVisibility(0);
            viewHolder.textView.setTextColor(Color.argb(255, 255, 255, 255));
            viewHolder.textView.setText("+" + (this.listS.size() - 3));
        } else {
            viewHolder.textView.setVisibility(8);
        }
        loadCover(viewHolder.imageView, str, this.mContext);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ImageAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                ScaleImageView scaleImageView = new ScaleImageView(ImageAdapter.this.mContext);
                scaleImageView.setUrls(ImageAdapter.this.listS, i);
                scaleImageView.create();
            }
        });
        return view2;
    }

    public static void loadCover(ImageView imageView, String str, Context context) {
        Glide.with(context).setDefaultRequestOptions(new RequestOptions().frame(1000000L).placeholder(R.drawable.default_common).error(R.drawable.default_common)).load(str).into(imageView);
    }

    /* loaded from: classes.dex */
    class ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder() {
        }
    }
}
