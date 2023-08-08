package com.home.activity.service.category;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.common.uitl.SharePersistent;
import com.home.activity.main.MainActivity_BLE;
import com.home.activity.main.MainActivity_DMX02;
import com.home.activity.main.MainActivity_DMX03;
import com.home.activity.main.MainActivity_LIKE;
import com.home.activity.service.category.CategoryBean;
import com.home.base.LedBleApplication;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.fragment.service.ServicesFragment;
import com.ledlamp.R;
import java.util.List;

/* loaded from: classes.dex */
public class HomeAdapter extends BaseAdapter {
    private Context context;
    private List<CategoryBean.DataBean> foodDatas;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public HomeAdapter(Context context, List<CategoryBean.DataBean> list) {
        this.context = context;
        this.foodDatas = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        List<CategoryBean.DataBean> list = this.foodDatas;
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
        final CategoryBean.DataBean dataBean = this.foodDatas.get(i);
        final List<CategoryBean.DataBean.DataListBean> dataList = dataBean.getDataList();
        if (view == null) {
            view = View.inflate(this.context, R.layout.item_service_right, null);
            viewHold = new ViewHold();
            viewHold.gridView = (GridViewForScrollView) view.findViewById(R.id.gridView);
            viewHold.blank = (TextView) view.findViewById(R.id.blank);
            view.setTag(viewHold);
        } else {
            viewHold = (ViewHold) view.getTag();
        }
        HomeItemAdapter homeItemAdapter = new HomeItemAdapter(this.context, dataList);
        viewHold.blank.setText(dataBean.getModuleTitle());
        viewHold.gridView.setAdapter((ListAdapter) homeItemAdapter);
        viewHold.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.service.category.HomeAdapter.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view2, int i2, long j) {
                ServicesFragment.sceneBeanFragment = ((CategoryBean.DataBean.DataListBean) dataList.get(i2)).getAppkey();
                HomeAdapter.this.goToMain(dataBean.getDataList().get(i2).getAppkey());
            }
        });
        return view;
    }

    /* loaded from: classes.dex */
    public static class ViewHold {
        private TextView blank;
        public GridViewForScrollView gridView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goToMain(String str) {
        LedBleApplication.getApp().setSceneBean(str.replaceAll("-", ""));
        SharePersistent.savePerference(this.context, Constant.CUSTOM_DIY_APPKEY, str);
        if (str.contains(CommonConstant.LEDBLE) || str.contains("LEDDMX-00-") || str.contains("LEDDMX-01-") || str.contains("LEDCAR-00-") || str.contains("LEDCAR-01-") || str.equalsIgnoreCase(CommonConstant.LEDSMART) || str.equalsIgnoreCase(CommonConstant.LEDSTAGE) || str.equalsIgnoreCase(CommonConstant.LEDLIGHT) || str.equalsIgnoreCase(CommonConstant.LEDSUN)) {
            Intent intent = new Intent(this.context, MainActivity_BLE.class);
            intent.putExtra("scene", str);
            this.context.startActivity(intent);
        } else if (str.equalsIgnoreCase("LEDDMX-02-")) {
            Intent intent2 = new Intent(this.context, MainActivity_DMX02.class);
            intent2.putExtra("scene", str);
            this.context.startActivity(intent2);
        } else if (str.equalsIgnoreCase("LEDDMX-03-")) {
            Intent intent3 = new Intent(this.context, MainActivity_DMX03.class);
            intent3.putExtra("scene", str);
            this.context.startActivity(intent3);
        } else if (str.equalsIgnoreCase(CommonConstant.LEDLIKE)) {
            Intent intent4 = new Intent(this.context, MainActivity_LIKE.class);
            intent4.putExtra("scene", str);
            this.context.startActivity(intent4);
        } else {
            str.equalsIgnoreCase(CommonConstant.LEDWiFi);
        }
    }
}
