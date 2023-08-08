package com.home.activity.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.service.category.CategoryBean;
import com.home.activity.service.category.HomeAdapter;
import com.home.activity.service.category.MenuAdapter;
import com.home.base.LedBleActivity;
import com.ledlamp.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AddSceneActivity extends LedBleActivity implements View.OnClickListener {
    private ImageView backImg;
    private int currentItem;
    private HomeAdapter homeAdapter;
    private ListView lv_home;
    private ListView lv_menu;
    private MenuAdapter menuAdapter;
    private List<Integer> showTitle;
    private TextView tv_title;
    private List<String> menuList = new ArrayList();
    private List<CategoryBean.DataBean> homeList = new ArrayList();
    private List<CategoryBean.DataBean> homeListTemp = new ArrayList();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        initData();
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_addscene);
        ImageView imageView = (ImageView) findViewById(R.id.backImg);
        this.backImg = imageView;
        imageView.setOnClickListener(this);
        this.lv_menu = (ListView) findViewById(R.id.lv_menu);
        this.tv_title = (TextView) findViewById(R.id.tv_titile);
        this.lv_home = (ListView) findViewById(R.id.lv_home);
        MenuAdapter menuAdapter = new MenuAdapter(this, this.menuList);
        this.menuAdapter = menuAdapter;
        this.lv_menu.setAdapter((ListAdapter) menuAdapter);
        this.lv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.service.AddSceneActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                AddSceneActivity.this.menuAdapter.setSelectItem(i);
                AddSceneActivity.this.menuAdapter.notifyDataSetInvalidated();
                AddSceneActivity.this.tv_title.setText((CharSequence) AddSceneActivity.this.menuList.get(i));
                AddSceneActivity.this.homeList.clear();
                AddSceneActivity.this.homeList.add((CategoryBean.DataBean) AddSceneActivity.this.homeListTemp.get(i));
                AddSceneActivity.this.homeAdapter.notifyDataSetInvalidated();
            }
        });
        this.homeList.clear();
        this.homeList.add(this.homeListTemp.get(0));
        HomeAdapter homeAdapter = new HomeAdapter(this, this.homeList);
        this.homeAdapter = homeAdapter;
        this.lv_home.setAdapter((ListAdapter) homeAdapter);
        this.lv_home.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: com.home.activity.service.AddSceneActivity.2
            private int scrollState;

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                this.scrollState = i;
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                int indexOf;
                if (this.scrollState == 0 || AddSceneActivity.this.currentItem == (indexOf = AddSceneActivity.this.showTitle.indexOf(Integer.valueOf(i))) || indexOf < 0) {
                    return;
                }
                AddSceneActivity.this.currentItem = indexOf;
                AddSceneActivity.this.tv_title.setText((CharSequence) AddSceneActivity.this.menuList.get(AddSceneActivity.this.currentItem));
                AddSceneActivity.this.menuAdapter.setSelectItem(AddSceneActivity.this.currentItem);
                AddSceneActivity.this.menuAdapter.notifyDataSetInvalidated();
            }
        });
    }

    private void initData() {
        CategoryBean categoryBean = (CategoryBean) JSONObject.parseObject(getJson(this, "category.json"), CategoryBean.class);
        this.showTitle = new ArrayList();
        for (int i = 0; i < categoryBean.getData().size() - 2; i++) {
            this.menuList.add(getTitles()[i]);
            this.showTitle.add(Integer.valueOf(i));
            this.homeListTemp.add(categoryBean.getData().get(i));
        }
    }

    public static String getJson(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(str), "utf-8"));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                sb.append(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String[] getTitles() {
        return getResources().getStringArray(R.array.app_service);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = getIntent();
        if (id != R.id.backImg) {
            return;
        }
        setResult(0, intent);
        finish();
    }
}
