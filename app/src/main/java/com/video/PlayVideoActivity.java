package com.video;

import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.FirstActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.net.NetResult;
import com.common.uitl.CommitAdapter;
import com.common.uitl.GetScreenWinth;
import com.common.uitl.MyVideoPlayer;
import com.common.uitl.OnViewPagerListener;
import com.common.uitl.PagerLayoutManager;
import com.common.uitl.SoftKeyBoardListener;
import com.common.uitl.SoftKeyHideShow;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.bean.PageBean;
import com.forum.login.LogInActivity;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.fragment.help.HelpVideoFragment;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.home.utils.Utils;
import com.ledlamp.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.video.VideoAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class PlayVideoActivity extends LedBleActivity {
    private static final String TAG = "PlayVideoActivity";
    public static VideoAdapter adapter = null;
    public static String fromHelpActivity = "NO";
    public static List<String> myData = null;
    public static int positionClick = 0;
    public static boolean refresh = false;
    private View commit;
    private CommitAdapter commitAdapter;
    private TextView contentlenth;
    private EditText et_context;
    private MyVideoPlayer jzVideo;
    LinearLayoutManager linearLayoutManager;
    private LinearLayout ll_cancel;
    private PagerLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewCommit;
    private RelativeLayout rl_all;
    private RelativeLayout rl_bottom;
    private Animation showAction;
    private SoftKeyBoardListener softKeyBoardListener;
    SwipeRefreshLayout srl_Comment789;
    private TextView tv_context;
    private TextView tv_send;
    private View video_content;
    public static List<VideoReplyBean> listReply = new ArrayList();
    public static final int[] DOT_COLORS = {-2446854, -868533, -1852250, -13460755, -5117031, -9975379, ViewCompat.MEASURED_STATE_MASK};
    private boolean isScroll = true;
    boolean first = true;
    private Map<String, String> params = new HashMap();
    private Map<String, String> getVideoNews = new HashMap();
    private Map<String, String> likeParams = new HashMap();
    private int pageNum = 1;
    private String isLast = NetResult.CODE_OK;
    private boolean isRespon = false;

    static /* synthetic */ int access$404(PlayVideoActivity playVideoActivity) {
        int i = playVideoActivity.pageNum + 1;
        playVideoActivity.pageNum = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_play_video);
        setRequestedOrientation(4);
        myData = new ArrayList();
        positionClick = getIntent().getIntExtra("position", -1);
        fromHelpActivity = getIntent().getStringExtra("fromHelpActivity");
        setView();
        setAdapter();
        setSoftKeyBoardListener();
    }

    public void getReplyList(final boolean z) {
        HttpUtil.getInstance().getSourceData(false, this, Constant.queryShortVideoReplyByApp, this.params, new HttpUtil.HttpCallBack() { // from class: com.video.PlayVideoActivity.1
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                Log.e("Rep", "onSuccess: " + str);
                PlayVideoActivity.this.isRespon = true;
                if (PlayVideoActivity.this.srl_Comment789 != null) {
                    PlayVideoActivity.this.srl_Comment789.setRefreshing(false);
                }
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<PageBean<VideoReplyBean>>>() { // from class: com.video.PlayVideoActivity.1.1
                }, new Feature[0]);
                if (responseBean != null) {
                    if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                        if (z) {
                            PlayVideoActivity.listReply.clear();
                            PlayVideoActivity.this.commitAdapter.notifyDataSetChanged();
                        }
                        PageBean pageBean = (PageBean) responseBean.getContent();
                        if (pageBean != null) {
                            PlayVideoActivity.this.isLast = pageBean.getIsLast();
                            if (pageBean.getList() != null && pageBean.getList().size() != 0) {
                                PlayVideoActivity.listReply.addAll(pageBean.getList());
                            }
                            PlayVideoActivity.this.commitAdapter.notifyDataSetChanged();
                            TextView textView = PlayVideoActivity.this.contentlenth;
                            textView.setText(pageBean.getCount() + PlayVideoActivity.this.getString(R.string.comment));
                        }
                    } else if (Constant.NODATA_CODE.equals(responseBean.getReturnCode())) {
                        TextView textView2 = PlayVideoActivity.this.contentlenth;
                        textView2.setText(NetResult.CODE_OK + PlayVideoActivity.this.getString(R.string.comment));
                        PlayVideoActivity.this.isLast = "1";
                    } else {
                        Toast.makeText(PlayVideoActivity.this, responseBean.getReturnDesc(), 0).show();
                    }
                }
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                PlayVideoActivity.this.isRespon = true;
                if (PlayVideoActivity.this.srl_Comment789 != null) {
                    PlayVideoActivity.this.srl_Comment789.setRefreshing(false);
                }
                if (PlayVideoActivity.this.pageNum > 0) {
                    PlayVideoActivity playVideoActivity = PlayVideoActivity.this;
                    Toast.makeText(playVideoActivity, playVideoActivity.getString(R.string.request_failed), 0).show();
                }
            }
        });
    }

    private void setView() {
        this.rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerViewCommit = (RecyclerView) findViewById(R.id.recyclerViewCommit);
        View findViewById = findViewById(R.id.commit);
        this.commit = findViewById;
        findViewById.setVisibility(8);
        this.tv_send = (TextView) findViewById(R.id.tv_send);
        this.tv_context = (TextView) findViewById(R.id.tv_context);
        this.ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        this.rl_all = (RelativeLayout) findViewById(R.id.rl_all);
        this.et_context = (EditText) findViewById(R.id.et_context);
        this.video_content = findViewById(R.id.video_content);
        this.contentlenth = (TextView) findViewById(R.id.contentlenth);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_Comment789);
        this.srl_Comment789 = swipeRefreshLayout;
        int i = 0;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        myData.clear();
        if (fromHelpActivity.equalsIgnoreCase("YES")) {
            getSwipeBackLayout().setEnableGesture(false);
            while (i < HelpVideoFragment.listUrl.size()) {
                myData.add((String) HelpVideoFragment.listUrl.get(i));
                i++;
            }
        } else {
            while (i < VideoFragment.list.size()) {
                myData.add(VideoFragment.list.get(i).getVideoVisitUrl());
                i++;
            }
        }
        this.srl_Comment789.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.video.PlayVideoActivity.2
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                if (PlayVideoActivity.this.isLast.equals("1")) {
                    PlayVideoActivity.this.isLast = NetResult.CODE_OK;
                }
                PlayVideoActivity.this.pageNum = 1;
                PlayVideoActivity.this.params.put("pageNum", "1");
                PlayVideoActivity.this.getReplyList(true);
            }
        });
    }

    private void setAdapter() {
        CommitAdapter commitAdapter = this.commitAdapter;
        if (commitAdapter == null) {
            this.commitAdapter = new CommitAdapter(this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            this.linearLayoutManager = linearLayoutManager;
            linearLayoutManager.setOrientation(0);
            this.recyclerViewCommit.setHasFixedSize(true);
            this.recyclerViewCommit.setLayoutManager(this.linearLayoutManager);
            this.recyclerViewCommit.setAdapter(this.commitAdapter);
        } else {
            commitAdapter.notifyDataSetChanged();
        }
        VideoAdapter videoAdapter = new VideoAdapter(this, myData);
        adapter = videoAdapter;
        this.recyclerView.setAdapter(videoAdapter);
        PagerLayoutManager pagerLayoutManager = new PagerLayoutManager(this, 1);
        this.mLayoutManager = pagerLayoutManager;
        this.recyclerView.setLayoutManager(pagerLayoutManager);
        this.recyclerView.scrollToPosition(positionClick);
        if (positionClick == myData.size() - 1 && FirstActivity.videoFragment != null) {
            refresh = true;
            FirstActivity.videoFragment.addnewvideo();
        }
        this.recyclerViewCommit.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.video.PlayVideoActivity.3
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i != 0 || PlayVideoActivity.this.isLast.equals("1")) {
                    return;
                }
                PlayVideoActivity.this.params.put("pageNum", String.valueOf(PlayVideoActivity.access$404(PlayVideoActivity.this)));
                PlayVideoActivity.this.getReplyList(false);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
            }
        });
        this.mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() { // from class: com.video.PlayVideoActivity.4
            @Override // com.common.uitl.OnViewPagerListener
            public void onInitComplete(View view) {
                Log.e(PlayVideoActivity.TAG, "onInitComplete: ");
                PlayVideoActivity.this.playVideo(view, false);
                PlayVideoActivity.this.browseVideo();
            }

            @Override // com.common.uitl.OnViewPagerListener
            public void onPageSelected(int i, boolean z, View view) {
                Log.e(PlayVideoActivity.TAG, "onPageSelected: ");
                PlayVideoActivity.positionClick = i;
                PlayVideoActivity.this.playVideo(view, z);
                PlayVideoActivity.this.browseVideo();
            }

            @Override // com.common.uitl.OnViewPagerListener
            public void onPageRelease(boolean z, int i, View view) {
                Log.e(PlayVideoActivity.TAG, "onPageRelease: ");
            }
        });
        adapter.setOnItemClickListerer(new VideoAdapter.OnItemClickListener() { // from class: com.video.PlayVideoActivity.5
            @Override // com.video.VideoAdapter.OnItemClickListener
            public void onItemClick(int i, String str, View view, View view2, View view3) {
                if (str.equals("back")) {
                    PlayVideoActivity.this.setResult(99);
                    PlayVideoActivity.this.finish();
                } else if (str.equals("commit")) {
                    PlayVideoActivity.this.getComment();
                    PlayVideoActivity.this.showCommitDialog();
                } else if (str.equals("like")) {
                    PlayVideoActivity.this.likeParams.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                    PlayVideoActivity.this.likeParams.put("supportNum", String.valueOf(VideoFragment.list.get(PlayVideoActivity.positionClick).getSupportNum() + 1));
                    PlayVideoActivity.this.likeParams.put("videoId", String.valueOf(VideoFragment.list.get(PlayVideoActivity.positionClick).getVideoId()));
                    PlayVideoActivity playVideoActivity = PlayVideoActivity.this;
                    playVideoActivity.uploading(Constant.supportShortVideo, playVideoActivity.likeParams);
                } else if (str.equals("unlike")) {
                    PlayVideoActivity.this.likeParams.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                    PlayVideoActivity.this.likeParams.put("supportNum", String.valueOf(VideoFragment.list.get(PlayVideoActivity.positionClick).getSupportNum() - 1));
                    PlayVideoActivity.this.likeParams.put("videoId", String.valueOf(VideoFragment.list.get(PlayVideoActivity.positionClick).getVideoId()));
                    PlayVideoActivity playVideoActivity2 = PlayVideoActivity.this;
                    playVideoActivity2.uploading(Constant.supportShortVideo, playVideoActivity2.likeParams);
                } else if (str.equals("horizontal")) {
                    PlayVideoActivity.this.setRequestedOrientation(0);
                }
            }
        });
    }

    private void setSoftKeyBoardListener() {
        SoftKeyBoardListener softKeyBoardListener = new SoftKeyBoardListener(this);
        this.softKeyBoardListener = softKeyBoardListener;
        softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() { // from class: com.video.PlayVideoActivity.6
            @Override // com.common.uitl.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
            public void keyBoardShow(int i) {
                ViewGroup.LayoutParams layoutParams = PlayVideoActivity.this.rl_bottom.getLayoutParams();
                PlayVideoActivity.this.rl_bottom.setPadding(0, 0, 0, i);
                PlayVideoActivity.this.rl_bottom.setLayoutParams(layoutParams);
                PlayVideoActivity.this.et_context.setFocusable(true);
                PlayVideoActivity.this.et_context.setFocusableInTouchMode(true);
                PlayVideoActivity.this.et_context.setCursorVisible(true);
                PlayVideoActivity.this.et_context.requestFocus();
            }

            @Override // com.common.uitl.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
            public void keyBoardHide(int i) {
                PlayVideoActivity.this.rl_bottom.setVisibility(8);
                PlayVideoActivity.this.et_context.setFocusable(false);
                PlayVideoActivity.this.et_context.setFocusableInTouchMode(false);
                PlayVideoActivity.this.et_context.setCursorVisible(false);
            }
        });
        this.et_context.setOnClickListener(new View.OnClickListener() { // from class: com.video.PlayVideoActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ((InputMethodManager) PlayVideoActivity.this.getSystemService("input_method")).toggleSoftInput(0, 2);
            }
        });
        this.et_context.setOnTouchListener(new View.OnTouchListener() { // from class: com.video.PlayVideoActivity.8
            int flag = 0;

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int i = this.flag + 1;
                this.flag = i;
                if (i == 2) {
                    this.flag = 0;
                    PlayVideoActivity.this.et_context.setFocusable(true);
                    PlayVideoActivity.this.et_context.setFocusableInTouchMode(true);
                    PlayVideoActivity.this.et_context.setCursorVisible(true);
                }
                return false;
            }
        });
    }

    public void getComment() {
        VideoBeanMode videoBeanMode = VideoFragment.list.get(positionClick);
        if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
            this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        }
        this.params.put("videoId", String.valueOf(videoBeanMode.getVideoId()));
        this.params.put("pageNum", String.valueOf(this.pageNum));
        this.params.put("pageSize", "10");
        listReply.clear();
        this.commitAdapter.notifyDataSetChanged();
        getReplyList(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playVideo(View view, boolean z) {
        if (view != null) {
            this.jzVideo = (MyVideoPlayer) view.findViewById(R.id.jzVideo);
            MyVideoPlayer.SAVE_PROGRESS = false;
            this.jzVideo.startVideo();
            if (z && VideoFragment.isLast.equals(NetResult.CODE_OK) && fromHelpActivity.equalsIgnoreCase("YES")) {
                refresh = true;
                FirstActivity.videoFragment.addnewvideo();
            }
            this.jzVideo.setFinishListerer(new MyVideoPlayer.OnItemClickListener() { // from class: com.video.PlayVideoActivity.9
                @Override // com.common.uitl.MyVideoPlayer.OnItemClickListener
                public void onItemClick() {
                    if (!PlayVideoActivity.this.isScroll) {
                        PlayVideoActivity.this.jzVideo.startVideo();
                        return;
                    }
                    PlayVideoActivity playVideoActivity = PlayVideoActivity.this;
                    RecyclerView recyclerView = playVideoActivity.recyclerView;
                    int i = PlayVideoActivity.positionClick;
                    PlayVideoActivity.positionClick = i + 1;
                    playVideoActivity.smoothMoveToPosition(recyclerView, i);
                }
            });
        }
    }

    public void showCommitDialog() {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.actionsheet_dialog_in);
        this.showAction = loadAnimation;
        this.commit.startAnimation(loadAnimation);
        this.commit.setVisibility(0);
        this.isScroll = false;
        this.ll_cancel.setOnClickListener(new View.OnClickListener() { // from class: com.video.PlayVideoActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PlayVideoActivity.this.commit.setVisibility(8);
                PlayVideoActivity.this.isScroll = true;
            }
        });
        this.tv_context.setOnClickListener(new View.OnClickListener() { // from class: com.video.PlayVideoActivity.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PlayVideoActivity.this.tv_context.setVisibility(0);
                SoftKeyHideShow.HideShowSoftKey(PlayVideoActivity.this);
            }
        });
        this.tv_send.setOnClickListener(new View.OnClickListener() { // from class: com.video.PlayVideoActivity.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
                    if (!TextUtils.isEmpty(PlayVideoActivity.this.et_context.getText())) {
                        PlayVideoActivity.this.getVideoNews.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                        PlayVideoActivity.this.getVideoNews.put("replyId", String.valueOf(VideoFragment.list.get(PlayVideoActivity.positionClick).getVideoId()));
                        PlayVideoActivity.this.getVideoNews.put("replyType", "comment");
                        PlayVideoActivity.this.getVideoNews.put("target", VideoFragment.list.get(PlayVideoActivity.positionClick).getAuthor());
                        PlayVideoActivity.this.getVideoNews.put(Utils.RESPONSE_CONTENT, PlayVideoActivity.this.et_context.getText().toString());
                        PlayVideoActivity playVideoActivity = PlayVideoActivity.this;
                        playVideoActivity.uploading(Constant.publishShortVideoReply, playVideoActivity.getVideoNews);
                        return;
                    }
                    Toast.makeText(PlayVideoActivity.this, (int) R.string.text, 0).show();
                    return;
                }
                PlayVideoActivity.this.startActivity(new Intent(PlayVideoActivity.this.getApplicationContext(), LogInActivity.class));
            }
        });
    }

    public void uploading(final String str, Map<String, String> map) {
        HttpUtil.getInstance().getSourceData(false, this, str, map, new HttpUtil.HttpCallBack() { // from class: com.video.PlayVideoActivity.13
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str2) {
                Log.e("--", "onSuccess: " + str2);
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str2, new TypeReference<ResponseBean<String>>() { // from class: com.video.PlayVideoActivity.13.1
                }, new Feature[0]);
                if (responseBean != null && Constant.SUCCESS_CODE.equals(responseBean.getReturnCode()) && str.equalsIgnoreCase(Constant.publishShortVideoReply)) {
                    Toast.makeText(PlayVideoActivity.this.getApplicationContext(), (int) R.string.comment_publish_success, 0).show();
                    SoftKeyHideShow.HideShowSoftKey(PlayVideoActivity.this);
                    VideoReplyBean videoReplyBean = new VideoReplyBean();
                    videoReplyBean.setAuthor(LedBleActivity.getBaseApp().getUserName());
                    videoReplyBean.setContent(PlayVideoActivity.this.et_context.getText().toString());
                    videoReplyBean.setHeadImage(LedBleActivity.getBaseApp().getUserHeadImage());
                    PlayVideoActivity.listReply.add(0, videoReplyBean);
                    PlayVideoActivity.this.commitAdapter.notifyDataSetChanged();
                    PlayVideoActivity.this.recyclerViewCommit.scrollToPosition(0);
                    PlayVideoActivity.this.et_context.setText("");
                }
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str2) {
                Log.e("---", "onException: " + str2);
                Toast.makeText(PlayVideoActivity.this.getApplicationContext(), (int) R.string.request_failed, 0).show();
            }
        });
    }

    public void browseVideo() {
        setRequestedOrientation(4);
        HashMap hashMap = new HashMap();
        if (fromHelpActivity.equalsIgnoreCase("YES")) {
            if (positionClick < HelpVideoFragment.videoId.length) {
                hashMap.put("videoId", String.valueOf(HelpVideoFragment.videoId[positionClick]));
                Log.e(TAG, ",   VideoId = " + HelpVideoFragment.videoId[positionClick]);
            }
        } else if (positionClick < VideoFragment.list.size()) {
            hashMap.put("videoId", String.valueOf(VideoFragment.list.get(positionClick).getVideoId()));
            Log.e(TAG, ",   VideoId = " + VideoFragment.list.get(positionClick).getVideoId());
        }
        if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
            hashMap.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        }
        HttpUtil.getInstance().getSourceData(false, this, Constant.browseVideo, hashMap, new HttpUtil.HttpCallBack() { // from class: com.video.PlayVideoActivity.14
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                Log.e(PlayVideoActivity.TAG, "browseVideo onSuccess: " + str);
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                PlayVideoActivity playVideoActivity = PlayVideoActivity.this;
                Toast.makeText(playVideoActivity, playVideoActivity.getString(R.string.request_failed), 0).show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void smoothMoveToPosition(RecyclerView recyclerView, int i) {
        int childLayoutPosition = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
        int childLayoutPosition2 = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.getChildCount() - 1));
        if (i < childLayoutPosition) {
            recyclerView.smoothScrollToPosition(i);
        } else if (i <= childLayoutPosition2) {
            recyclerView.smoothScrollBy(0, GetScreenWinth.getHeight(this) - Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")));
        } else {
            recyclerView.smoothScrollToPosition(i);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed: " + positionClick);
        releasejzVideo();
        super.onBackPressed();
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " + positionClick);
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " + positionClick);
        ((AudioManager) getSystemService(PictureMimeType.MIME_TYPE_PREFIX_AUDIO)).requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() { // from class: com.video.PlayVideoActivity.15
            @Override // android.media.AudioManager.OnAudioFocusChangeListener
            public void onAudioFocusChange(int i) {
            }
        }, 3, 2);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        Log.e(TAG, "onKeyDown: " + positionClick);
        if (i == 4) {
            releasejzVideo();
            if (!this.isScroll) {
                this.commit.setVisibility(8);
                this.isScroll = true;
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        Log.e(TAG, "onDestroy: " + positionClick);
        releasejzVideo();
        super.onDestroy();
    }

    private void releasejzVideo() {
        MyVideoPlayer myVideoPlayer = this.jzVideo;
        if (myVideoPlayer != null) {
            myVideoPlayer.release();
            this.jzVideo = null;
        }
    }
}
