package com.video;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.common.net.NetResult;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.bean.PageBean;
import com.home.base.LedBleActivity;
import com.home.base.LedBleFragment;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.ledlamp.R;
import com.squareup.picasso.Picasso;
import in.srain.cube.views.GridViewWithHeaderAndFooter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class VideoFragment extends LedBleFragment {
    private View footerView;
    @BindView(R.id.gv_video)
    GridViewWithHeaderAndFooter gv_video;
    private LinearLayout llLoading;
    private View mView;
    MyVideoAdaptr myVideoAdaptr;
    public Map<String, String> params;
    @BindView(R.id.srl_Comment11)
    SwipeRefreshLayout srl_Comment11;
    private TextView tvTips;
    public static List<VideoBeanMode> list = new ArrayList();
    public static String isLast = NetResult.CODE_OK;
    private int pageNum = 1;
    private boolean isRespon = false;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_publish_comment, viewGroup, false);
        this.mView = inflate;
        return inflate;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        String str;
        View inflate = getLayoutInflater().inflate(R.layout.layout_listview_footer, (ViewGroup) null, false);
        this.footerView = inflate;
        this.llLoading = (LinearLayout) inflate.findViewById(R.id.llLoading);
        this.tvTips = (TextView) this.footerView.findViewById(R.id.tvTips);
        this.gv_video.addFooterView(this.footerView);
        this.footerView.setVisibility(8);
        MyVideoAdaptr myVideoAdaptr = new MyVideoAdaptr();
        this.myVideoAdaptr = myVideoAdaptr;
        this.gv_video.setAdapter((ListAdapter) myVideoAdaptr);
        this.params = new HashMap();
        if (getResources().getString(R.string.home).equalsIgnoreCase("首页")) {
            str = "cn";
        } else {
            str = getResources().getString(R.string.home).equalsIgnoreCase("Home") ? "en" : "dmx02_other";
        }
        this.params.put(FFmpegMediaMetadataRetriever.METADATA_KEY_LANGUAGE, str);
        this.params.put("pageNum", String.valueOf(this.pageNum));
        this.params.put("pageSize", "10");
        if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
            return;
        }
        this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
    }

    public boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo;
        if (context == null || (activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return false;
        }
        return activeNetworkInfo.isAvailable();
    }

    public void addnewvideo() {
        if (isLast.equals(NetResult.CODE_OK)) {
            Map<String, String> map = this.params;
            int i = this.pageNum + 1;
            this.pageNum = i;
            map.put("pageNum", String.valueOf(i));
            getVideoList(false);
        }
    }

    public void getVideo() {
        this.params.clear();
        this.pageNum = 1;
        this.params.put("pageNum", String.valueOf(1));
        this.params.put("pageSize", "10");
        if (LedBleActivity.getBaseApp().getUserToken() != null && !LedBleActivity.getBaseApp().getUserToken().equals("")) {
            this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        }
        getVideoList(true);
    }

    public void getVideoList(final boolean z) {
        if (!isNetworkConnected(getContext())) {
            Toast.makeText(getContext(), getString(R.string.check_network_connection), 1).show();
        } else {
            HttpUtil.getInstance().getSourceData(false, this.activity, Constant.queryShortVideoByApp, this.params, new HttpUtil.HttpCallBack() { // from class: com.video.VideoFragment.1
                @Override // com.home.http.HttpUtil.HttpCallBack
                public void onSuccess(String str) {
                    Log.e("skyworth", "onSuccess: " + str);
                    VideoFragment.this.isRespon = true;
                    if (VideoFragment.this.srl_Comment11 != null) {
                        VideoFragment.this.srl_Comment11.setRefreshing(false);
                    }
                    ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<PageBean<VideoBeanMode>>>() { // from class: com.video.VideoFragment.1.1
                    }, new Feature[0]);
                    if (responseBean != null) {
                        if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                            if (z) {
                                VideoFragment.list.clear();
                                VideoFragment.this.myVideoAdaptr.notifyDataSetChanged();
                            }
                            PageBean pageBean = (PageBean) responseBean.getContent();
                            if (pageBean != null) {
                                VideoFragment.isLast = pageBean.getIsLast();
                                if (pageBean.getList() != null && pageBean.getList().size() != 0) {
                                    VideoFragment.list.addAll(pageBean.getList());
                                }
                                VideoFragment.this.myVideoAdaptr.notifyDataSetChanged();
                                if (PlayVideoActivity.refresh) {
                                    for (int size = PlayVideoActivity.myData.size(); size < VideoFragment.list.size(); size++) {
                                        PlayVideoActivity.myData.add(VideoFragment.list.get(size).getVideoVisitUrl());
                                    }
                                    PlayVideoActivity.adapter.notifyDataSetChanged();
                                    PlayVideoActivity.refresh = false;
                                }
                                if (VideoFragment.isLast.equals("1")) {
                                    VideoFragment.this.footerView.setVisibility(0);
                                    VideoFragment.this.tvTips.setVisibility(0);
                                    return;
                                }
                                VideoFragment.this.footerView.setVisibility(8);
                                VideoFragment.this.tvTips.setVisibility(8);
                            }
                        } else if (Constant.NODATA_CODE.equals(responseBean.getReturnCode())) {
                            Toast.makeText(VideoFragment.this.activity, VideoFragment.this.getString(R.string.no_more_data), 0).show();
                        } else {
                            Toast.makeText(VideoFragment.this.activity, responseBean.getReturnDesc(), 0).show();
                        }
                    }
                }

                @Override // com.home.http.HttpUtil.HttpCallBack
                public void onException(String str) {
                    VideoFragment.this.isRespon = true;
                    if (VideoFragment.this.srl_Comment11 != null) {
                        VideoFragment.this.srl_Comment11.setRefreshing(false);
                    }
                    if (VideoFragment.this.pageNum <= 0 || VideoFragment.this.getActivity() == null) {
                        return;
                    }
                    Toast.makeText(VideoFragment.this.getActivity(), VideoFragment.this.getString(R.string.request_failed), 0).show();
                }
            });
        }
    }

    public void deleteItem(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("videoId", String.valueOf(i));
        if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
            hashMap.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        }
        HttpUtil.getInstance().getSourceData(false, getActivity(), Constant.deleteShortVideoByApp, hashMap, new HttpUtil.HttpCallBack() { // from class: com.video.VideoFragment.2
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                Log.e("Rep", "onSuccess: " + str);
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Toast.makeText(VideoFragment.this.getActivity(), VideoFragment.this.getString(R.string.request_failed), 0).show();
            }
        });
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        this.srl_Comment11.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
        this.srl_Comment11.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.video.VideoFragment.3
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                if (VideoFragment.isLast.equals("1")) {
                    VideoFragment.isLast = NetResult.CODE_OK;
                }
                VideoFragment.this.pageNum = 1;
                VideoFragment.this.params.put("pageNum", "1");
                VideoFragment.this.getVideoList(true);
            }
        });
        this.gv_video.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: com.video.VideoFragment.4
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 0 && absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                    VideoFragment.this.addnewvideo();
                }
            }
        });
    }

    @Override // com.home.base.LedBleFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    /* loaded from: classes.dex */
    class TextHolder {
        ImageView backgr;
        Button bt_delete;
        ImageView iv_headVideo;
        ImageView iv_video;
        TextView tv_Author1;
        TextView tv_time1;
        TextView tv_title1;

        TextHolder() {
        }
    }

    /* loaded from: classes.dex */
    class MyVideoAdaptr extends BaseAdapter {
        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return null;
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        MyVideoAdaptr() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return VideoFragment.list.size();
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view2;
            TextHolder textHolder;
            final VideoBeanMode videoBeanMode = VideoFragment.list.get(i);
            if (view == null) {
                textHolder = new TextHolder();
                view2 = LayoutInflater.from(VideoFragment.this.getActivity()).inflate(R.layout.video, viewGroup, false);
                double width = viewGroup.getWidth();
                Double.isNaN(width);
                double width2 = viewGroup.getWidth();
                Double.isNaN(width2);
                view2.setLayoutParams(new AbsListView.LayoutParams((int) (width * 0.5d), (int) ((width2 * 0.5d) - 40.0d)));
                textHolder.iv_video = (ImageView) view2.findViewById(R.id.iv_video);
                textHolder.tv_Author1 = (TextView) view2.findViewById(R.id.tv_Author1);
                textHolder.iv_headVideo = (ImageView) view2.findViewById(R.id.iv_headVideo);
                textHolder.tv_title1 = (TextView) view2.findViewById(R.id.tv_title1);
                textHolder.tv_time1 = (TextView) view2.findViewById(R.id.tv_time1);
                textHolder.backgr = (ImageView) view2.findViewById(R.id.backgr);
                textHolder.bt_delete = (Button) view2.findViewById(R.id.bt_delete);
                view2.setTag(textHolder);
            } else {
                view2 = view;
                textHolder = (TextHolder) view.getTag();
            }
            if (textHolder.backgr != null) {
                textHolder.backgr.getBackground().setAlpha(100);
            }
            textHolder.tv_Author1.setText(videoBeanMode.getAuthor());
            VideoFragment.loadCover(textHolder.iv_video, videoBeanMode.getVideoVisitUrl(), VideoFragment.this.getContext());
            if (videoBeanMode.getHeadImage() == null) {
                textHolder.iv_headVideo.setImageResource(R.drawable.user_pic);
            } else {
                Picasso.get().load(videoBeanMode.getHeadImage()).placeholder(R.drawable.user_pic).into(textHolder.iv_headVideo);
            }
            if (videoBeanMode.getPublishTime().contains(" ") && videoBeanMode.getPublishTime().split(" ").length == 2) {
                textHolder.tv_time1.setText(videoBeanMode.getPublishTime().split(" ")[0]);
            } else {
                textHolder.tv_time1.setText("");
            }
            textHolder.tv_title1.setText(videoBeanMode.getTitle());
            textHolder.iv_video.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoFragment.MyVideoAdaptr.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    Intent intent = new Intent(VideoFragment.this.getActivity(), VideoDetailActivity.class);
                    intent.putExtra("videoPath", videoBeanMode.getVideoVisitUrl());
                    intent.putExtra(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE, videoBeanMode.getTitle());
                    VideoFragment.this.startActivity(intent);
                }
            });
            if (videoBeanMode.isHasDelete()) {
                textHolder.bt_delete.setVisibility(0);
            } else {
                textHolder.bt_delete.setVisibility(8);
            }
            textHolder.bt_delete.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoFragment.MyVideoAdaptr.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    VideoFragment.this.deleteItem(videoBeanMode.getVideoId());
                    VideoFragment.list.remove(videoBeanMode);
                    VideoFragment.this.myVideoAdaptr.notifyDataSetChanged();
                }
            });
            return view2;
        }
    }

    public static void loadCover(ImageView imageView, String str, Context context) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).setDefaultRequestOptions(new RequestOptions().frame(1000000L).centerCrop().fitCenter().placeholder(R.drawable.default_common).error(R.drawable.default_common)).load(str).into(imageView);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        list.clear();
        this.myVideoAdaptr.notifyDataSetChanged();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 99) {
            this.gv_video.setSelection(PlayVideoActivity.positionClick);
        }
    }
}
