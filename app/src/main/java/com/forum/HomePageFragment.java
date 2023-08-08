package com.forum;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import com.FirstActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.net.NetResult;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.adapter.CommentAdapter;
import com.forum.bean.CommentBean;
import com.forum.bean.PageBean;
import com.forum.im.ui.ListViewChatActivity;
import com.forum.login.LogInActivity;
import com.home.base.LedBleActivity;
import com.home.base.LedBleFragment;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class HomePageFragment extends LedBleFragment {
    private static final String TAG = "HomePageFragment";
    private static Toast toast;
    private CommentAdapter adapter;
    private ImageView imageViewIM;
    private int lastVisibleItem;
    public LinearLayoutManager layoutManager;
    private LinearLayout ll_tab_item;
    @BindView(R.id.lvComment)
    RecyclerView lvComment;
    public Map<String, String> params;
    @BindView(R.id.srlComment)
    SwipeRefreshLayout srlComment;
    private TextView tvAdvisory;
    private TextView tvAdvisoryLine;
    private TextView tvConnect;
    private TextView tvConnectLine;
    private TextView tvHot;
    private TextView tvHotLine;
    private TextView tvOther;
    private TextView tvOtherLine;
    private TextView tvSuggest;
    private TextView tvSuggestLine;
    private int type;
    private List<CommentBean> list = new ArrayList();
    private int pageNum = 1;
    private String isLast = NetResult.CODE_OK;
    private boolean isRespon = false;

    static /* synthetic */ int access$704(HomePageFragment homePageFragment) {
        int i = homePageFragment.pageNum + 1;
        homePageFragment.pageNum = i;
        return i;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_home_page, viewGroup, false);
    }

    public boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo;
        if (context == null || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return false;
        }
        return activeNetworkInfo.isAvailable();
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        LinearLayout forumTopTabItem = FirstActivity.getForumTopTabItem();
        this.ll_tab_item = forumTopTabItem;
        this.tvOtherLine = (TextView) forumTopTabItem.findViewById(R.id.tvOtherLine);
        this.tvConnectLine = (TextView) this.ll_tab_item.findViewById(R.id.tvConnectLine);
        this.tvAdvisoryLine = (TextView) this.ll_tab_item.findViewById(R.id.tvAdvisoryLine);
        this.tvSuggestLine = (TextView) this.ll_tab_item.findViewById(R.id.tvSuggestLine);
        this.tvHotLine = (TextView) this.ll_tab_item.findViewById(R.id.tvHotLine);
        ImageView imageView = (ImageView) this.ll_tab_item.findViewById(R.id.imageViewIM);
        this.imageViewIM = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.forum.HomePageFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HomePageFragment homePageFragment = HomePageFragment.this;
                if (!homePageFragment.isNetworkConnected(homePageFragment.getContext())) {
                    Toast.makeText(HomePageFragment.this.getContext(), HomePageFragment.this.getString(R.string.check_network_connection), 1).show();
                } else if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
                    HomePageFragment.this.startActivity(new Intent(HomePageFragment.this.getContext(), LogInActivity.class));
                } else {
                    HomePageFragment.this.startActivity(new Intent(HomePageFragment.this.getContext(), ListViewChatActivity.class));
                }
            }
        });
        TextView textView = (TextView) this.ll_tab_item.findViewById(R.id.tvOther);
        this.tvOther = textView;
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.forum.HomePageFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HomePageFragment.this.tvOtherLine.setBackgroundResource(R.color.white);
                HomePageFragment.this.tvConnectLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvAdvisoryLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvSuggestLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvHotLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.type = 0;
                HomePageFragment.this.getContent();
            }
        });
        TextView textView2 = (TextView) this.ll_tab_item.findViewById(R.id.tvConnect);
        this.tvConnect = textView2;
        textView2.setOnClickListener(new View.OnClickListener() { // from class: com.forum.HomePageFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HomePageFragment.this.tvOtherLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvConnectLine.setBackgroundResource(R.color.white);
                HomePageFragment.this.tvAdvisoryLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvSuggestLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvHotLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.type = 1;
                HomePageFragment.this.getContent();
            }
        });
        TextView textView3 = (TextView) this.ll_tab_item.findViewById(R.id.tvAdvisory);
        this.tvAdvisory = textView3;
        textView3.setOnClickListener(new View.OnClickListener() { // from class: com.forum.HomePageFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HomePageFragment.this.tvOtherLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvConnectLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvAdvisoryLine.setBackgroundResource(R.color.white);
                HomePageFragment.this.tvSuggestLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvHotLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.type = 2;
                HomePageFragment.this.getContent();
            }
        });
        TextView textView4 = (TextView) this.ll_tab_item.findViewById(R.id.tvSuggest);
        this.tvSuggest = textView4;
        textView4.setOnClickListener(new View.OnClickListener() { // from class: com.forum.HomePageFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HomePageFragment.this.tvOtherLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvConnectLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvAdvisoryLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvSuggestLine.setBackgroundResource(R.color.white);
                HomePageFragment.this.tvHotLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.type = 3;
                HomePageFragment.this.getContent();
            }
        });
        TextView textView5 = (TextView) this.ll_tab_item.findViewById(R.id.tvHot);
        this.tvHot = textView5;
        textView5.setOnClickListener(new View.OnClickListener() { // from class: com.forum.HomePageFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HomePageFragment.this.tvOtherLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvConnectLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvAdvisoryLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvSuggestLine.setBackgroundResource(R.color.transparent);
                HomePageFragment.this.tvHotLine.setBackgroundResource(R.color.white);
                HomePageFragment.this.type = 4;
                HomePageFragment.this.getContent();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.activity);
        this.layoutManager = linearLayoutManager;
        linearLayoutManager.setOrientation(1);
        this.lvComment.setHasFixedSize(true);
        this.lvComment.setLayoutManager(this.layoutManager);
        CommentAdapter commentAdapter = new CommentAdapter(this.activity, this.list);
        this.adapter = commentAdapter;
        this.lvComment.setAdapter(commentAdapter);
        this.adapter.setIsLast(this.isLast);
        this.adapter.notifyDataSetChanged();
        this.params = new HashMap();
        getContent();
    }

    public void getContent() {
        String str;
        this.params.clear();
        this.pageNum = 1;
        if (getResources().getString(R.string.home).equalsIgnoreCase("首页")) {
            str = "cn";
        } else {
            str = getResources().getString(R.string.home).equalsIgnoreCase("Home") ? "en" : "dmx02_other";
        }
        this.params.put(FFmpegMediaMetadataRetriever.METADATA_KEY_LANGUAGE, str);
        this.params.put("pageNum", String.valueOf(this.pageNum));
        this.params.put("pageSize", "10");
        this.params.put("type", String.valueOf(this.type));
        if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
            this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        }
        getCommentList(true);
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        SwipeRefreshLayout swipeRefreshLayout = this.srlComment;
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        }
    }

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
        this.srlComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.forum.HomePageFragment.7
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                if (HomePageFragment.this.isLast.equals("1")) {
                    HomePageFragment.this.isLast = NetResult.CODE_OK;
                }
                HomePageFragment.this.pageNum = 1;
                HomePageFragment.this.params.put("pageNum", "1");
                HomePageFragment.this.getCommentList(true);
            }
        });
        this.lvComment.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.forum.HomePageFragment.8
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i == 0 && HomePageFragment.this.lastVisibleItem + 1 == HomePageFragment.this.adapter.getItemCount() && !HomePageFragment.this.isLast.equals("1")) {
                    HomePageFragment.this.params.put("pageNum", String.valueOf(HomePageFragment.access$704(HomePageFragment.this)));
                    HomePageFragment.this.getCommentList(false);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                HomePageFragment homePageFragment = HomePageFragment.this;
                homePageFragment.lastVisibleItem = homePageFragment.layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    public void getCommentList(final boolean z) {
        if (!isNetworkConnected(getContext())) {
            Toast.makeText(getContext(), getString(R.string.check_network_connection), 1).show();
        } else {
            HttpUtil.getInstance().getSourceData(false, this.activity, Constant.queryCommentByApp, this.params, new HttpUtil.HttpCallBack() { // from class: com.forum.HomePageFragment.9
                @Override // com.home.http.HttpUtil.HttpCallBack
                public void onSuccess(String str) {
                    HomePageFragment.this.isRespon = true;
                    if (HomePageFragment.this.srlComment != null) {
                        HomePageFragment.this.srlComment.setRefreshing(false);
                    }
                    JSON.parse(str);
                    ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<PageBean<CommentBean>>>() { // from class: com.forum.HomePageFragment.9.1
                    }, new Feature[0]);
                    if (responseBean != null) {
                        if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                            if (z) {
                                HomePageFragment.this.list.clear();
                                HomePageFragment.this.adapter.notifyDataSetChanged();
                            }
                            PageBean pageBean = (PageBean) responseBean.getContent();
                            if (pageBean != null) {
                                HomePageFragment.this.isLast = pageBean.getIsLast();
                                if (pageBean.getList() != null && pageBean.getList().size() != 0) {
                                    HomePageFragment.this.list.addAll(pageBean.getList());
                                }
                                HomePageFragment.this.adapter.setIsLast(HomePageFragment.this.isLast);
                                HomePageFragment.this.adapter.notifyDataSetChanged();
                            }
                        } else if (Constant.NODATA_CODE.equals(responseBean.getReturnCode())) {
                            if (z) {
                                HomePageFragment.this.list.clear();
                                HomePageFragment.this.adapter.notifyDataSetChanged();
                            }
                            HomePageFragment.shouToast(HomePageFragment.this.getContext(), HomePageFragment.this.getResources().getString(R.string.no_more_data));
                        } else {
                            HomePageFragment.shouToast(HomePageFragment.this.getContext(), HomePageFragment.this.getResources().getString(R.string.no_more_data));
                        }
                    }
                }

                @Override // com.home.http.HttpUtil.HttpCallBack
                public void onException(String str) {
                    HomePageFragment.this.isRespon = true;
                    if (HomePageFragment.this.srlComment != null) {
                        HomePageFragment.this.srlComment.setRefreshing(false);
                    }
                    if (HomePageFragment.this.pageNum <= 0 || HomePageFragment.this.getActivity() == null || !HomePageFragment.this.isAdded()) {
                        return;
                    }
                    Toast.makeText(HomePageFragment.this.getActivity(), HomePageFragment.this.getResources().getString(R.string.request_failed), 0).show();
                }
            });
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.list.clear();
        this.adapter.notifyDataSetChanged();
    }

    public static void shouToast(Context context, String str) {
        Toast toast2 = toast;
        if (toast2 == null) {
            toast = Toast.makeText(context, str, 0);
        } else {
            toast2.setText(str);
        }
        toast.show();
    }
}
