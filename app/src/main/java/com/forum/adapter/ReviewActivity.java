package com.forum.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.uitl.SoftKeyBoardListener;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.ReplyActivity;
import com.forum.bean.ReplyBean;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.home.utils.Utils;
import com.ledlamp.R;
import com.squareup.picasso.Picasso;
import com.video.VideoListAdapter;
import com.video.VideoReplyActivity;
import com.video.VideoReplyAdapter;
import com.video.VideoReplyBean;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ReviewActivity extends LedBleActivity {
    private static final String TAG = "ReviewActivity";
    public static List<ReplyBean> listReview = new ArrayList();
    private static boolean oneOrTwo = true;
    ReplyListAdapter adapter;
    EditText et_context_review;
    private View footerView;
    GridView gvImage_review;
    private View headView;
    ImageAdapter imageAdapter;
    ImageView iv_head;
    ListView listView;
    private LinearLayout llLoading;
    InputMethodManager manager;
    ReplyBean replyBean;
    private SoftKeyBoardListener softKeyBoardListener;
    String targe;
    TextView tvAuthor3;
    TextView tvContent3;
    TextView tvDate;
    TextView tvFloorHost3;
    private TextView tvTips;
    TextView tv_send_review;
    public List<VideoReplyBean> listVideo = new ArrayList();
    Map<String, String> reply = new HashMap();
    private Map<String, File> files = new HashMap();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_review);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        findViewById(R.id.iv_Back11).setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ReviewActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ReviewActivity.this.manager != null) {
                    ReviewActivity.this.manager.toggleSoftInput(0, 2);
                }
                ReviewActivity.this.finish();
            }
        });
        this.tv_send_review = (TextView) findViewById(R.id.tv_send_review);
        this.et_context_review = (EditText) findViewById(R.id.et_context_review);
        this.softKeyBoardListener = new SoftKeyBoardListener(this);
        View inflate = getLayoutInflater().inflate(R.layout.layout_listview_footer, (ViewGroup) null, false);
        this.footerView = inflate;
        this.llLoading = (LinearLayout) inflate.findViewById(R.id.llLoading);
        this.tvTips = (TextView) this.footerView.findViewById(R.id.tvTips);
        this.headView = getLayoutInflater().inflate(R.layout.layout_headview, (ViewGroup) null, false);
        this.listView = (ListView) findViewById(R.id.lvReply);
        this.iv_head = (ImageView) this.headView.findViewById(R.id.iv_head);
        this.tvAuthor3 = (TextView) this.headView.findViewById(R.id.tvAuthor3);
        this.tvContent3 = (TextView) this.headView.findViewById(R.id.tvContent3);
        this.tvDate = (TextView) this.headView.findViewById(R.id.tvDate3);
        this.gvImage_review = (GridView) this.headView.findViewById(R.id.gvImage_review);
        this.tvFloorHost3 = (TextView) this.headView.findViewById(R.id.tvFloorHost3);
        this.listView.addHeaderView(this.headView);
        this.listView.addFooterView(this.footerView);
        this.footerView.setVisibility(0);
        this.tvTips.setVisibility(0);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.forum.adapter.ReviewActivity.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Log.e(ReviewActivity.TAG, "onItemClick: " + i);
                ReviewActivity reviewActivity = ReviewActivity.this;
                reviewActivity.manager = (InputMethodManager) reviewActivity.getSystemService("input_method");
                ReviewActivity.this.manager.toggleSoftInput(0, 2);
                if (i > 0) {
                    ReviewActivity reviewActivity2 = ReviewActivity.this;
                    reviewActivity2.targe = reviewActivity2.replyBean.getList().get(i - 1).getAuthor();
                } else {
                    ReviewActivity reviewActivity3 = ReviewActivity.this;
                    reviewActivity3.targe = reviewActivity3.replyBean.getList().get(i).getAuthor();
                }
                ReviewActivity.this.et_context_review.setHint(ReviewActivity.this.targe);
                ReviewActivity.this.reply.clear();
                ReviewActivity.this.reply.put("replyId", String.valueOf(ReviewActivity.this.replyBean.getId()));
                ReviewActivity.this.reply.put("replyType", "reply");
                ReviewActivity.this.reply.put("target", ReviewActivity.this.targe);
                ReviewActivity.this.reply.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                boolean unused = ReviewActivity.oneOrTwo = false;
            }
        });
        this.tv_send_review.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ReviewActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
                    if (!ReviewActivity.this.et_context_review.getText().toString().equals("")) {
                        if (ReviewActivity.oneOrTwo) {
                            ReviewActivity.this.reply.clear();
                            ReviewActivity.this.reply.put("replyId", String.valueOf(ReviewActivity.this.replyBean.getId()));
                            ReviewActivity.this.reply.put("replyType", "reply");
                            ReviewActivity.this.reply.put("target", ReviewActivity.this.replyBean.getAuthor());
                            ReviewActivity.this.reply.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                            ReviewActivity.this.reply.put(Utils.RESPONSE_CONTENT, ReviewActivity.this.et_context_review.getText().toString());
                        } else {
                            ReviewActivity.this.reply.put(Utils.RESPONSE_CONTENT, ReviewActivity.this.et_context_review.getText().toString());
                        }
                        ReviewActivity.this.uploading();
                        return;
                    }
                    Toast.makeText(ReviewActivity.this, (int) R.string.text, 0).show();
                    return;
                }
                Toast.makeText(ReviewActivity.this, (int) R.string.loginapp, 0).show();
            }
        });
        this.softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() { // from class: com.forum.adapter.ReviewActivity.4
            @Override // com.common.uitl.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
            public void keyBoardShow(int i) {
                Log.e(ReviewActivity.TAG, "keyBoardShow: " + i);
                ReviewActivity.this.et_context_review.setFocusable(true);
                ReviewActivity.this.et_context_review.setFocusableInTouchMode(true);
                ReviewActivity.this.et_context_review.setCursorVisible(true);
                ReviewActivity.this.et_context_review.requestFocus();
            }

            @Override // com.common.uitl.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
            public void keyBoardHide(int i) {
                ReviewActivity.this.et_context_review.setHint(R.string.comment);
                ReviewActivity.this.et_context_review.setFocusableInTouchMode(false);
                ReviewActivity.this.et_context_review.setCursorVisible(false);
                boolean unused = ReviewActivity.oneOrTwo = true;
            }
        });
    }

    public void uploading() {
        HttpUtil.getInstance().uploadFile(true, this, Constant.publishReplyByApp, this.reply, this.files, new HttpUtil.HttpCallBack() { // from class: com.forum.adapter.ReviewActivity.5
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.forum.adapter.ReviewActivity.5.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                Toast.makeText(ReviewActivity.this.getApplicationContext(), (int) R.string.comment_publish_success, 0).show();
                ReplyBean replyBean = new ReplyBean();
                replyBean.setAuthor(LedBleActivity.getBaseApp().getUserName());
                if (ReviewActivity.oneOrTwo) {
                    replyBean.setTarget(ReviewActivity.this.replyBean.getAuthor());
                } else {
                    replyBean.setTarget(ReviewActivity.this.targe);
                }
                replyBean.setHeadImage(LedBleActivity.getBaseApp().getUserHeadImage());
                replyBean.setContent(ReviewActivity.this.et_context_review.getText().toString());
                ReplyListAdapter.list.add(0, replyBean);
                ReviewActivity.this.et_context_review.setText("");
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Toast.makeText(ReviewActivity.this.getApplicationContext(), (int) R.string.request_failed, 0).show();
            }
        });
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: 000");
        if (getIntent().getIntExtra("video11", 99) == 12) {
            int intExtra = getIntent().getIntExtra("replyBean", -1);
            List<VideoReplyBean> list = VideoReplyAdapter.listVideoBean;
            this.listVideo = list;
            VideoReplyBean videoReplyBean = list.get(intExtra);
            this.listView.setAdapter((ListAdapter) new VideoListAdapter(this, String.valueOf(videoReplyBean.getId()), videoReplyBean.getList()));
            return;
        }
        int intExtra2 = getIntent().getIntExtra("replyBean", -1);
        List<ReplyBean> list2 = ReplyAdapter.listReplyBean;
        listReview = list2;
        ReplyBean replyBean = list2.get(intExtra2);
        this.replyBean = replyBean;
        this.tvDate.setText(replyBean.getPublishTime());
        TextView textView = this.tvFloorHost3;
        textView.setText(intExtra2 + getResources().getString(R.string.floor));
        this.tvContent3.setText(this.replyBean.getContent());
        this.tvAuthor3.setText(this.replyBean.getAuthor());
        if (this.replyBean.getHeadImage() == null) {
            this.iv_head.setImageResource(R.drawable.user_pic);
        } else {
            Picasso.get().load(this.replyBean.getHeadImage()).placeholder(R.drawable.user_pic).into(this.iv_head);
        }
        if (!this.replyBean.getImageVisitUrl().equals("")) {
            this.gvImage_review.setVisibility(0);
            ArrayList arrayList = new ArrayList();
            ImageAdapter imageAdapter = new ImageAdapter(this, arrayList);
            this.imageAdapter = imageAdapter;
            this.gvImage_review.setAdapter((ListAdapter) imageAdapter);
            String imageVisitUrl = this.replyBean.getImageVisitUrl();
            if (imageVisitUrl.contains(",")) {
                arrayList.addAll(Arrays.asList(imageVisitUrl.split(",")));
            } else {
                arrayList.add(imageVisitUrl);
            }
            this.imageAdapter.notifyDataSetChanged();
        } else {
            this.gvImage_review.setVisibility(8);
        }
        ReplyListAdapter replyListAdapter = new ReplyListAdapter(this, String.valueOf(this.replyBean.getId()), this.replyBean.getList());
        this.adapter = replyListAdapter;
        this.listView.setAdapter((ListAdapter) replyListAdapter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 10) {
            if (getIntent().getIntExtra("video11", 99) == 12) {
                VideoReplyActivity.activity.getReplyList(true);
            } else {
                ReplyActivity.activity.getReplyList(true);
            }
            finish();
        }
        if (i2 == 222) {
            this.adapter.notifyDataSetChanged();
            this.listView.setSelection(0);
        }
        if (i2 == 111) {
            setResult(111);
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
}
