package com.home.activity.service.category;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.home.activity.service.category.CategoryBean;
import com.ledlamp.BuildConfig;
import com.ledlamp.R;
import java.util.List;

/* loaded from: classes.dex */
public class HomeItemAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean.DataBean.DataListBean> foodDatas;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public HomeItemAdapter(Context context, List<CategoryBean.DataBean.DataListBean> list) {
        this.context = context;
        this.foodDatas = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        List<CategoryBean.DataBean.DataListBean> list = this.foodDatas;
        if (list != null) {
            return list.size();
        }
        return 10;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return Integer.valueOf(this.foodDatas.size());
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold;
        CategoryBean.DataBean.DataListBean dataListBean = this.foodDatas.get(i);
        if (view == null) {
            view = View.inflate(this.context, R.layout.item_service_right_detail, null);
            viewHold = new ViewHold();
            viewHold.tv_name = (TextView) view.findViewById(R.id.item_home_name);
            viewHold.iv_icon = (ImageView) view.findViewById(R.id.item_album);
            view.setTag(viewHold);
        } else {
            viewHold = (ViewHold) view.getTag();
        }
        viewHold.tv_name.setText(dataListBean.getTitle());
        Drawable image = getImage(dataListBean.getImgURL());
        if (image != null) {
            viewHold.iv_icon.setBackgroundDrawable(image);
        }
        return view;
    }

    /* loaded from: classes.dex */
    private static class ViewHold {
        private ImageView iv_icon;
        private TextView tv_name;

        private ViewHold() {
        }
    }

    public Drawable getImage(String str) {
        Resources resources = this.context.getResources();
        int identifier = resources.getIdentifier("" + str, "drawable", BuildConfig.APPLICATION_ID);
        Log.e("====", "getImage: " + identifier + "===" + str);
        if (identifier > 0) {
            return this.context.getResources().getDrawable(identifier);
        }
        return null;
    }
}
