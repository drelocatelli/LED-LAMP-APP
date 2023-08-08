package com.forum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
import com.common.uitl.SoftKeyBoardListener;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.adapter.ImageAdapter;
import com.forum.adapter.ReplyAdapter;
import com.forum.bean.CommentBean;
import com.forum.bean.PageBean;
import com.forum.bean.ReplyBean;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.home.utils.Utils;
import com.ledlamp.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ReplyActivity extends LedBleActivity implements View.OnClickListener {
    public static final int PUBLISH_REQUEST_CODE = 10;
    private static final String TAG = "ReplyActivity";
    public static ReplyActivity activity = null;
    private static boolean oneOrTwo = true;
    private ReplyAdapter adapter;
    @BindView(R.id.btnReply)
    Button btnReply;
    @BindView(R.id.et_context111)
    EditText et_context111;
    private View footerView;
    private GridView gvImage;
    private View headerView;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    private LinearLayout llLoading;
    @BindView(R.id.lv_Reply)
    ListView lv_Reply;
    @BindView(R.id.imageViewAdd)
    ImageView releseCharacter;
    private String replyId;
    private SoftKeyBoardListener softKeyBoardListener;
    @BindView(R.id.srl_Comment)
    SwipeRefreshLayout srl_Comment;
    private String target;
    private TextView tvAuthor;
    private TextView tvCommentContent;
    private TextView tvCommentTitle;
    private TextView tvCount;
    private TextView tvDate;
    private TextView tvTips;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tv_send111)
    TextView tv_send111;
    private int visibleItem;
    private List<ReplyBean> listReply = new ArrayList();
    private Map<String, String> params = new HashMap();
    Map<String, String> reply = new HashMap();
    private Map<String, File> files = new HashMap();
    private ReplyBean replyBean = new ReplyBean();
    private int pageNum = 1;
    private String isLast = NetResult.CODE_OK;
    private boolean isRespon = false;

    static /* synthetic */ int access$104(ReplyActivity replyActivity) {
        int i = replyActivity.pageNum + 1;
        replyActivity.pageNum = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        CommentBean commentBean;
        super.onCreate(bundle);
        setContentView(R.layout.activity_reply);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        this.tvTitle.setText(getString(R.string.comment_detail));
        this.releseCharacter.setVisibility(8);
        activity = this;
        this.adapter = new ReplyAdapter(this, this.listReply);
        this.ivBack.setOnClickListener(this);
        this.btnReply.setOnClickListener(this);
        this.tv_send111.setOnClickListener(this);
        this.softKeyBoardListener = new SoftKeyBoardListener(this);
        this.srl_Comment.setColorSchemeResources(R.color.colorPrimary);
        View inflate = getLayoutInflater().inflate(R.layout.layout_reply_header, (ViewGroup) null, false);
        this.headerView = inflate;
        this.tvCommentTitle = (TextView) inflate.findViewById(R.id.tvCommentTitle);
        this.tvCount = (TextView) this.headerView.findViewById(R.id.tvCount);
        this.tvCommentContent = (TextView) this.headerView.findViewById(R.id.tvCommentContent);
        this.tvAuthor = (TextView) this.headerView.findViewById(R.id.tvAuthor);
        this.tvDate = (TextView) this.headerView.findViewById(R.id.tvDate);
        this.gvImage = (GridView) this.headerView.findViewById(R.id.gvImage);
        View inflate2 = getLayoutInflater().inflate(R.layout.layout_listview_footer, (ViewGroup) null, false);
        this.footerView = inflate2;
        this.llLoading = (LinearLayout) inflate2.findViewById(R.id.llLoading);
        this.tvTips = (TextView) this.footerView.findViewById(R.id.tvTips);
        this.footerView.setVisibility(8);
        Bundle extras = getIntent().getExtras();
        if (extras != null && (commentBean = (CommentBean) extras.getSerializable("comment")) != null) {
            this.listReply.clear();
            this.tvCommentTitle.setText(commentBean.getTitle());
            this.tvCommentContent.setText(commentBean.getContent());
            this.tvAuthor.setText(commentBean.getAuthor());
            this.replyId = String.valueOf(commentBean.getId());
            this.target = commentBean.getAuthor();
            this.tvDate.setText(commentBean.getPublishTime());
            if (!commentBean.getImageVisitUrl().equals("")) {
                this.gvImage.setVisibility(0);
                ArrayList arrayList = new ArrayList();
                ImageAdapter imageAdapter = new ImageAdapter(activity, arrayList);
                this.gvImage.setAdapter((ListAdapter) imageAdapter);
                String imageVisitUrl = commentBean.getImageVisitUrl();
                if (imageVisitUrl.contains(",")) {
                    arrayList.addAll(Arrays.asList(imageVisitUrl.split(",")));
                    if (arrayList.size() == 2) {
                        this.gvImage.setNumColumns(2);
                    }
                } else {
                    arrayList.add(imageVisitUrl);
                    this.gvImage.setNumColumns(1);
                }
                imageAdapter.notifyDataSetChanged();
            } else {
                this.gvImage.setVisibility(8);
            }
            this.replyBean.setContent(commentBean.getContent());
            this.replyBean.setAuthor(commentBean.getAuthor());
            this.replyBean.setPublishTime(commentBean.getPublishTime());
            this.replyBean.setImageVisitUrl(commentBean.getImageVisitUrl());
            if (commentBean.getHeadImage() != null) {
                this.replyBean.setHeadImage(commentBean.getHeadImage());
            } else {
                this.replyBean.setHeadImage("");
            }
            this.lv_Reply.addHeaderView(this.headerView, null, false);
            this.lv_Reply.addFooterView(this.footerView, null, false);
            this.adapter.setFoorHost(commentBean.getAuthor());
            this.lv_Reply.setAdapter((ListAdapter) this.adapter);
            if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
                this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
            }
            this.params.put("commentId", this.replyId);
            this.params.put("pageNum", String.valueOf(this.pageNum));
            this.params.put("pageSize", "10");
            getReplyList(true);
        }
        this.srl_Comment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.forum.ReplyActivity.1
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                if (ReplyActivity.this.isLast.equals("1")) {
                    ReplyActivity.this.isLast = NetResult.CODE_OK;
                }
                ReplyActivity.this.pageNum = 1;
                ReplyActivity.this.params.put("pageNum", "1");
                ReplyActivity.this.getReplyList(true);
            }
        });
        this.lv_Reply.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: com.forum.ReplyActivity.2
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                int count = ReplyActivity.this.adapter.getCount();
                if (i == 0 && ReplyActivity.this.visibleItem == count && ReplyActivity.this.isLast.equals(NetResult.CODE_OK) && ReplyActivity.this.isRespon) {
                    ReplyActivity.this.footerView.setVisibility(0);
                    ReplyActivity.this.llLoading.setVisibility(0);
                    ReplyActivity.this.params.put("pageNum", String.valueOf(ReplyActivity.access$104(ReplyActivity.this)));
                    ReplyActivity.this.getReplyList(false);
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                ReplyActivity.this.visibleItem = (i + i2) - 2;
            }
        });
        this.lv_Reply.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.forum.ReplyActivity.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Log.e("===", "onItemClick: " + i + ReplyActivity.this.listReply.size());
                ((InputMethodManager) ReplyActivity.this.getSystemService("input_method")).toggleSoftInput(0, 2);
                int i2 = i + (-1);
                ReplyActivity.this.et_context111.setHint(((ReplyBean) ReplyActivity.this.listReply.get(i2)).getAuthor());
                ReplyActivity.this.reply.clear();
                ReplyActivity.this.reply.put("replyId", String.valueOf(((ReplyBean) ReplyActivity.this.listReply.get(i2)).getId()));
                ReplyActivity.this.reply.put("replyType", "reply");
                ReplyActivity.this.reply.put("target", ((ReplyBean) ReplyActivity.this.listReply.get(i2)).getAuthor());
                ReplyActivity.this.reply.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                boolean unused = ReplyActivity.oneOrTwo = false;
            }
        });
        this.softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() { // from class: com.forum.ReplyActivity.4
            @Override // com.common.uitl.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
            public void keyBoardShow(int i) {
                Log.e(ReplyActivity.TAG, "keyBoardShow: " + i);
                ReplyActivity.this.et_context111.setFocusable(true);
                ReplyActivity.this.et_context111.setFocusableInTouchMode(true);
                ReplyActivity.this.et_context111.setCursorVisible(true);
                ReplyActivity.this.et_context111.requestFocus();
            }

            @Override // com.common.uitl.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
            public void keyBoardHide(int i) {
                ReplyActivity.this.et_context111.setHint(R.string.comment);
                ReplyActivity.this.et_context111.setFocusableInTouchMode(false);
                ReplyActivity.this.et_context111.setCursorVisible(false);
                boolean unused = ReplyActivity.oneOrTwo = true;
            }
        });
    }

    public void getReplyList(final boolean z) {
        HttpUtil.getInstance().getSourceData(false, this, Constant.queryReplyByApp, this.params, new HttpUtil.HttpCallBack() { // from class: com.forum.ReplyActivity.5
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                Log.e("222222222", "onSuccess: " + str);
                ReplyActivity.this.footerView.setVisibility(8);
                ReplyActivity.this.llLoading.setVisibility(8);
                ReplyActivity.this.isRespon = true;
                if (ReplyActivity.this.srl_Comment != null) {
                    ReplyActivity.this.srl_Comment.setRefreshing(false);
                }
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<PageBean<ReplyBean>>>() { // from class: com.forum.ReplyActivity.5.1
                }, new Feature[0]);
                if (responseBean != null) {
                    if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                        if (z) {
                            ReplyActivity.this.listReply.clear();
                        }
                        ReplyActivity.this.adapter.notifyDataSetChanged();
                        PageBean pageBean = (PageBean) responseBean.getContent();
                        if (pageBean != null) {
                            ReplyActivity.this.isLast = pageBean.getIsLast();
                            if (pageBean.getList() != null && pageBean.getList().size() != 0) {
                                ReplyActivity.this.listReply.addAll(pageBean.getList());
                            }
                            ReplyActivity.this.adapter.notifyDataSetChanged();
                            TextView textView = ReplyActivity.this.tvCount;
                            textView.setText(pageBean.getCount() + ReplyActivity.this.getString(R.string.reply));
                        }
                        if ("1".equals(ReplyActivity.this.isLast)) {
                            ReplyActivity.this.footerView.setVisibility(0);
                            ReplyActivity.this.tvTips.setVisibility(0);
                            return;
                        }
                        ReplyActivity.this.footerView.setVisibility(8);
                        ReplyActivity.this.tvTips.setVisibility(8);
                    } else if (Constant.NODATA_CODE.equals(responseBean.getReturnCode())) {
                        TextView textView2 = ReplyActivity.this.tvCount;
                        textView2.setText(NetResult.CODE_OK + ReplyActivity.this.getString(R.string.reply));
                        ReplyActivity.this.isLast = "1";
                        ReplyActivity.this.footerView.setVisibility(0);
                        ReplyActivity.this.tvTips.setVisibility(0);
                    } else {
                        Toast.makeText(ReplyActivity.this, responseBean.getReturnDesc(), 0).show();
                    }
                }
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                ReplyActivity.this.footerView.setVisibility(8);
                ReplyActivity.this.llLoading.setVisibility(8);
                ReplyActivity.this.isRespon = true;
                if (ReplyActivity.this.srl_Comment != null) {
                    ReplyActivity.this.srl_Comment.setRefreshing(false);
                }
                if (ReplyActivity.this.pageNum > 0) {
                    ReplyActivity replyActivity = ReplyActivity.this;
                    Toast.makeText(replyActivity, replyActivity.getString(R.string.request_failed), 0).show();
                }
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnReply) {
            if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
                Intent intent = new Intent(this, ReleseCharacterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(TAG, TAG);
                bundle.putString("replyId", this.replyId);
                bundle.putString("replyType", "comment");
                bundle.putString("target", this.target);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10);
                return;
            }
            Toast.makeText(this, getString(R.string.loginapp), 1).show();
        } else if (id == R.id.ivBack) {
            finish();
        } else if (id != R.id.tv_send111) {
        } else {
            if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
                if (!this.et_context111.getText().toString().equals("")) {
                    if (oneOrTwo) {
                        this.reply.clear();
                        this.reply.put("replyId", this.replyId);
                        this.reply.put("replyType", "comment");
                        this.reply.put("target", this.target);
                        this.reply.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                        this.reply.put(Utils.RESPONSE_CONTENT, this.et_context111.getText().toString());
                    } else {
                        this.reply.put(Utils.RESPONSE_CONTENT, this.et_context111.getText().toString());
                    }
                    uploading();
                    return;
                }
                Toast.makeText(this, (int) R.string.text, 0).show();
                return;
            }
            Toast.makeText(this, getString(R.string.loginapp), 1).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == 222) {
            this.adapter.notifyDataSetChanged();
            this.lv_Reply.setSelection(0);
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

    public void uploading() {
        HttpUtil.getInstance().uploadFile(true, this, Constant.publishReplyByApp, this.reply, this.files, new HttpUtil.HttpCallBack() { // from class: com.forum.ReplyActivity.6
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.forum.ReplyActivity.6.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                Toast.makeText(ReplyActivity.this.getApplicationContext(), (int) R.string.comment_publish_success, 0).show();
                if (ReplyActivity.oneOrTwo) {
                    ReplyActivity.this.adapter.notifyDataSetChanged();
                }
                ReplyActivity.this.et_context111.setText("");
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Toast.makeText(ReplyActivity.this.getApplicationContext(), (int) R.string.request_failed, 0).show();
            }
        });
    }
}
