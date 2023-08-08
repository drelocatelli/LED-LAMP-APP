package com.video;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.net.NetResult;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.bean.PageBean;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class VideoReplyActivity extends LedBleActivity implements View.OnClickListener {
    public static final int PUBLISH_REQUEST_CODE = 11;
    public static VideoReplyActivity activity;
    public static List<VideoReplyBean> listReply = new ArrayList();
    VideoReplyAdapter adapter;
    @BindView(R.id.btnReply7)
    Button btnReply7;
    private View footerView;
    private View headerView;
    @BindView(R.id.ivBack7)
    ImageView ivBack7;
    private LinearLayout llLoading;
    @BindView(R.id.lv_Reply7)
    ListView lv_Reply7;
    private String replyId;
    @BindView(R.id.srl_Comment7)
    SwipeRefreshLayout srl_Comment7;
    private String target;
    private TextView tvCommentTitle;
    private TextView tvCount;
    private TextView tvTips;
    private VideoReplyBean videoReplyBean;
    private int visibleItem;
    private Map<String, String> params = new HashMap();
    private int pageNum = 1;
    private boolean isRespon = false;
    private String isLast = NetResult.CODE_OK;

    static /* synthetic */ int access$304(VideoReplyActivity videoReplyActivity) {
        int i = videoReplyActivity.pageNum + 1;
        videoReplyActivity.pageNum = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        VideoBeanMode videoBeanMode;
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_reply);
        activity = this;
        this.adapter = new VideoReplyAdapter(this, listReply);
        this.ivBack7.setOnClickListener(this);
        this.btnReply7.setOnClickListener(this);
        this.srl_Comment7.setColorSchemeResources(R.color.colorPrimary);
        View inflate = getLayoutInflater().inflate(R.layout.layout_reply_header, (ViewGroup) null, false);
        this.headerView = inflate;
        this.tvCommentTitle = (TextView) inflate.findViewById(R.id.tvCommentTitle);
        this.tvCount = (TextView) this.headerView.findViewById(R.id.tvCount);
        View inflate2 = getLayoutInflater().inflate(R.layout.layout_listview_footer, (ViewGroup) null, false);
        this.footerView = inflate2;
        this.llLoading = (LinearLayout) inflate2.findViewById(R.id.llLoading);
        this.tvTips = (TextView) this.footerView.findViewById(R.id.tvTips);
        this.footerView.setVisibility(8);
        this.videoReplyBean = new VideoReplyBean();
        Bundle extras = getIntent().getExtras();
        if (extras != null && (videoBeanMode = (VideoBeanMode) extras.getSerializable("comment")) != null) {
            listReply.clear();
            this.tvCommentTitle.setText(videoBeanMode.getTitle());
            this.replyId = String.valueOf(videoBeanMode.getVideoId());
            this.target = videoBeanMode.getAuthor();
            this.videoReplyBean.setVideoVisitUrl(videoBeanMode.getVideoVisitUrl());
            this.videoReplyBean.setAuthor(videoBeanMode.getAuthor());
            this.videoReplyBean.setPublishTime(videoBeanMode.getPublishTime());
            this.videoReplyBean.setImageVisitUrl(videoBeanMode.getImageVisitUrl());
            listReply.add(this.videoReplyBean);
            this.adapter.setFoorHost(videoBeanMode.getAuthor());
            this.lv_Reply7.addHeaderView(this.headerView, null, false);
            this.lv_Reply7.setAdapter((ListAdapter) this.adapter);
            this.adapter.notifyDataSetChanged();
            if (!getBaseApp().getUserToken().equals("")) {
                this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, getBaseApp().getUserToken());
            }
            this.params.put("videoId", this.replyId);
            this.params.put("pageNum", String.valueOf(this.pageNum));
            this.params.put("pageSize", "10");
            getReplyList(true);
        }
        this.srl_Comment7.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.video.VideoReplyActivity.1
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                VideoReplyActivity.this.getReplyList(true);
            }
        });
        this.lv_Reply7.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: com.video.VideoReplyActivity.2
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                int count = VideoReplyActivity.this.adapter.getCount();
                if (i == 0 && VideoReplyActivity.this.visibleItem == count && NetResult.CODE_OK.equals(VideoReplyActivity.this.isLast) && VideoReplyActivity.this.isRespon) {
                    VideoReplyActivity.this.params.put("pageNum", String.valueOf(VideoReplyActivity.access$304(VideoReplyActivity.this)));
                    VideoReplyActivity.this.getReplyList(false);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                VideoReplyActivity.this.visibleItem = (i + i2) - 1;
            }
        });
    }

    public void getReplyList(final boolean z) {
        HttpUtil.getInstance().getSourceData(z, this, Constant.queryShortVideoReplyByApp, this.params, new HttpUtil.HttpCallBack() { // from class: com.video.VideoReplyActivity.3
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                Log.e("Rep", "onSuccess: " + str);
                VideoReplyActivity.this.isRespon = true;
                if (VideoReplyActivity.this.srl_Comment7 != null) {
                    VideoReplyActivity.this.srl_Comment7.setRefreshing(false);
                }
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<PageBean<VideoReplyBean>>>() { // from class: com.video.VideoReplyActivity.3.1
                }, new Feature[0]);
                if (responseBean != null) {
                    if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                        if (z) {
                            VideoReplyActivity.listReply.clear();
                            VideoReplyActivity.listReply.add(VideoReplyActivity.this.videoReplyBean);
                        }
                        VideoReplyActivity.this.adapter.notifyDataSetChanged();
                        PageBean pageBean = (PageBean) responseBean.getContent();
                        if (pageBean != null) {
                            VideoReplyActivity.this.isLast = pageBean.getIsLast();
                            if (pageBean.getList() != null && pageBean.getList().size() != 0) {
                                VideoReplyActivity.listReply.addAll(pageBean.getList());
                            }
                            VideoReplyActivity.this.adapter.notifyDataSetChanged();
                            TextView textView = VideoReplyActivity.this.tvCount;
                            textView.setText(pageBean.getCount() + VideoReplyActivity.this.getString(R.string.reply));
                        }
                        if (NetResult.CODE_OK.equals(VideoReplyActivity.this.isLast)) {
                            VideoReplyActivity.this.footerView.setVisibility(0);
                            VideoReplyActivity.this.llLoading.setVisibility(0);
                            VideoReplyActivity.this.tvTips.setVisibility(8);
                            return;
                        }
                        VideoReplyActivity.this.footerView.setVisibility(8);
                    } else if (Constant.NODATA_CODE.equals(responseBean.getReturnCode())) {
                        TextView textView2 = VideoReplyActivity.this.tvCount;
                        textView2.setText(NetResult.CODE_OK + VideoReplyActivity.this.getString(R.string.reply));
                        VideoReplyActivity.this.isLast = "1";
                    } else {
                        Toast.makeText(VideoReplyActivity.this, responseBean.getReturnDesc(), 0).show();
                    }
                }
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                VideoReplyActivity.this.isRespon = true;
                if (VideoReplyActivity.this.srl_Comment7 != null) {
                    VideoReplyActivity.this.srl_Comment7.setRefreshing(false);
                }
                if (VideoReplyActivity.this.pageNum > 0) {
                    VideoReplyActivity videoReplyActivity = VideoReplyActivity.this;
                    Toast.makeText(videoReplyActivity, videoReplyActivity.getString(R.string.request_failed), 0).show();
                }
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btnReply7) {
            if (id != R.id.ivBack7) {
                return;
            }
            finish();
        } else if (!getBaseApp().getUserToken().equals("")) {
            Intent intent = new Intent(this, ReviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("replyId", this.replyId);
            bundle.putString("replyType", "comment");
            bundle.putString("target", this.target);
            intent.putExtras(bundle);
            startActivityForResult(intent, 11);
        } else {
            setResult(119);
            finish();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finish();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 11) {
            getReplyList(true);
        }
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }
}
