package com.forum.im.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.FirstActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.view.popwindow.PopWindow;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.bean.CommentBean;
import com.forum.bean.PageBean;
import com.forum.im.adapter.ChatListViewAdapter;
import com.forum.im.db.ChatMessageBean;
import com.forum.im.ui.base.IMBaseActivity;
import com.forum.im.utils.KeyBoardUtils;
import com.forum.im.widget.AudioRecordButton;
import com.forum.im.widget.pulltorefresh.PullToRefreshListView;
import com.forum.im.widget.pulltorefresh.base.PullToRefreshView;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.home.utils.Utils;
import com.ledlamp.R;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ListViewChatActivity extends IMBaseActivity {
    private static Toast toast;
    private Handler mHandlerGetImMsg;
    private Runnable mRunnableGetImMsg;
    public PullToRefreshListView myList;
    private int rawX;
    private int rawY;
    private SendMessageHandler sendMessageHandler;
    public ChatListViewAdapter tbAdapter;
    private Map<String, String> params = new HashMap();
    private Map<String, String> reply = new HashMap();
    private Map<String, File> files = new HashMap();
    String sendContent = "";
    String sendTime = "";
    String filePath = "";
    float seconds = 0.0f;
    String voiceFilePath = "";
    private Handler receriveHandler = new Handler() { // from class: com.forum.im.ui.ListViewChatActivity.13
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 2) {
                return;
            }
            ListViewChatActivity listViewChatActivity = ListViewChatActivity.this;
            listViewChatActivity.receriveVoiceText(listViewChatActivity.seconds, ListViewChatActivity.this.voiceFilePath);
        }
    };
    private int k = 0;

    static /* synthetic */ int access$708(ListViewChatActivity listViewChatActivity) {
        int i = listViewChatActivity.k;
        listViewChatActivity.k = i + 1;
        return i;
    }

    @Override // com.forum.im.ui.base.IMBaseActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.tvTitle.setText(getString(R.string.online_service));
        this.mHandlerGetImMsg = new Handler();
        Runnable runnable = new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.1
            @Override // java.lang.Runnable
            public void run() {
                ListViewChatActivity.this.tblist.clear();
                ListViewChatActivity.this.getImMsgList();
                ListViewChatActivity.this.mHandlerGetImMsg.removeCallbacks(ListViewChatActivity.this.mRunnableGetImMsg);
            }
        };
        this.mRunnableGetImMsg = runnable;
        this.mHandlerGetImMsg.postDelayed(runnable, 1L);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
    }

    @Override // com.forum.im.ui.base.IMBaseActivity, android.app.Activity
    protected void onDestroy() {
        this.tblist.clear();
        this.tbAdapter.notifyDataSetChanged();
        this.myList.setAdapter((ListAdapter) null);
        this.sendMessageHandler.removeCallbacksAndMessages(null);
        Handler handler = this.mHandlerGetImMsg;
        if (handler != null) {
            handler.removeCallbacks(this.mRunnableGetImMsg);
        }
        super.onDestroy();
    }

    @Override // com.forum.im.ui.base.IMBaseActivity
    protected void findView() {
        super.findView();
        this.pullList.setSlideView(new PullToRefreshView(this).getSlideView(0));
        this.myList = (PullToRefreshListView) this.pullList.returnMylist();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        this.rawX = (int) motionEvent.getRawX();
        this.rawY = (int) motionEvent.getRawY();
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // com.forum.im.ui.base.IMBaseActivity
    protected void init() {
        this.sendMessageHandler = new SendMessageHandler(this);
        ChatListViewAdapter chatListViewAdapter = new ChatListViewAdapter(this);
        this.tbAdapter = chatListViewAdapter;
        chatListViewAdapter.setUserList(this.tblist);
        this.myList.setAdapter((ListAdapter) this.tbAdapter);
        this.myList.setVerticalScrollBarEnabled(false);
        this.tbAdapter.isPicRefresh = true;
        this.tbAdapter.notifyDataSetChanged();
        this.tbAdapter.setSendErrorListener(new ChatListViewAdapter.SendErrorListener() { // from class: com.forum.im.ui.ListViewChatActivity.2
            @Override // com.forum.im.adapter.ChatListViewAdapter.SendErrorListener
            public void onClick(int i) {
                ChatMessageBean chatMessageBean = ListViewChatActivity.this.tblist.get(i);
                if (chatMessageBean.getType() == 5) {
                    ListViewChatActivity.this.sendVoice(chatMessageBean.getUserVoiceTime(), chatMessageBean.getUserVoicePath());
                    ListViewChatActivity.this.tblist.remove(i);
                } else if (chatMessageBean.getType() == 3) {
                    ListViewChatActivity.this.sendImage(null, chatMessageBean.getImageLocal(), false);
                    ListViewChatActivity.this.tblist.remove(i);
                }
            }
        });
        this.tbAdapter.setLongPressListener(new ChatListViewAdapter.LongPressListener() { // from class: com.forum.im.ui.ListViewChatActivity.3
            @Override // com.forum.im.adapter.ChatListViewAdapter.LongPressListener
            public void onLongPress(View view, final int i) {
                PopWindow.getInstance(ListViewChatActivity.this).builder.bindView(view, ListViewChatActivity.this.position).setPopupItemList(new String[]{ListViewChatActivity.this.getString(R.string.delete)}).setPointers(ListViewChatActivity.this.rawX, ListViewChatActivity.this.rawY).setOnPopuListItemClickListener(new PopWindow.OnPopuListItemClickListener() { // from class: com.forum.im.ui.ListViewChatActivity.3.1
                    @Override // com.common.view.popwindow.PopWindow.OnPopuListItemClickListener
                    public void onPopuListItemClick(View view2, int i2, int i3) {
                        ListViewChatActivity.this.delectMsg(ListViewChatActivity.this.tblist.get(i).getId());
                        ListViewChatActivity.this.tblist.remove(i);
                        ListViewChatActivity.this.tbAdapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
        this.tbAdapter.setVoiceIsReadListener(new ChatListViewAdapter.VoiceIsRead() { // from class: com.forum.im.ui.ListViewChatActivity.4
            @Override // com.forum.im.adapter.ChatListViewAdapter.VoiceIsRead
            public void voiceOnClick(int i) {
                for (int i2 = 0; i2 < ListViewChatActivity.this.tbAdapter.unReadPosition.size(); i2++) {
                    if (ListViewChatActivity.this.tbAdapter.unReadPosition.get(i2).equals(i + "")) {
                        ListViewChatActivity.this.tbAdapter.unReadPosition.remove(i2);
                        return;
                    }
                }
            }
        });
        this.myList.setOnScrollListener(new AbsListView.OnScrollListener() { // from class: com.forum.im.ui.ListViewChatActivity.5
            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 0) {
                    ListViewChatActivity.this.tbAdapter.handler.removeCallbacksAndMessages(null);
                    ListViewChatActivity.this.tbAdapter.setIsGif(true);
                    ListViewChatActivity.this.tbAdapter.isPicRefresh = false;
                    ListViewChatActivity.this.tbAdapter.notifyDataSetChanged();
                } else if (i != 1) {
                } else {
                    ListViewChatActivity.this.tbAdapter.handler.removeCallbacksAndMessages(null);
                    ListViewChatActivity.this.tbAdapter.setIsGif(false);
                    ListViewChatActivity.this.tbAdapter.isPicRefresh = true;
                    ListViewChatActivity.this.reset();
                    ListViewChatActivity listViewChatActivity = ListViewChatActivity.this;
                    KeyBoardUtils.hideKeyBoard(listViewChatActivity, listViewChatActivity.mEditTextContent);
                }
            }
        });
        this.voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() { // from class: com.forum.im.ui.ListViewChatActivity.6
            @Override // com.forum.im.widget.AudioRecordButton.AudioFinishRecorderListener
            public void onFinished(float f, String str) {
                ListViewChatActivity.this.sendVoice(f, str);
            }

            @Override // com.forum.im.widget.AudioRecordButton.AudioFinishRecorderListener
            public void onStart() {
                ListViewChatActivity.this.tbAdapter.stopPlayVoice();
            }
        });
        super.init();
    }

    /* loaded from: classes.dex */
    static class SendMessageHandler extends Handler {
        WeakReference<ListViewChatActivity> mActivity;

        SendMessageHandler(ListViewChatActivity listViewChatActivity) {
            this.mActivity = new WeakReference<>(listViewChatActivity);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            ListViewChatActivity listViewChatActivity = this.mActivity.get();
            if (listViewChatActivity != null) {
                int i = message.what;
                if (i == 17) {
                    listViewChatActivity.tbAdapter.isPicRefresh = true;
                    listViewChatActivity.tbAdapter.notifyDataSetChanged();
                    listViewChatActivity.myList.setSelection(listViewChatActivity.tblist.size() - 1);
                } else if (i == 273) {
                    listViewChatActivity.tbAdapter.notifyDataSetChanged();
                    listViewChatActivity.myList.setSelection(listViewChatActivity.position - 1);
                    listViewChatActivity.isDown = false;
                } else if (i == 4368) {
                    listViewChatActivity.mEditTextContent.setText("");
                    listViewChatActivity.tbAdapter.isPicRefresh = true;
                    listViewChatActivity.tbAdapter.notifyDataSetChanged();
                    listViewChatActivity.myList.setSelection(listViewChatActivity.tblist.size() - 1);
                } else if (i != 4369) {
                } else {
                    listViewChatActivity.tbAdapter.isPicRefresh = true;
                    listViewChatActivity.tbAdapter.notifyDataSetChanged();
                    listViewChatActivity.myList.setSelection(listViewChatActivity.tblist.size() - 1);
                }
            }
        }
    }

    @Override // com.forum.im.ui.base.IMBaseActivity
    protected void loadRecords() {
        this.isDown = true;
        if (this.pagelist != null) {
            this.pagelist.clear();
        }
        this.pagelist = this.mChatDbManager.loadPages(this.page, this.number);
        this.position = this.pagelist.size();
        if (this.pagelist.size() != 0) {
            this.pagelist.addAll(this.tblist);
            this.tblist.clear();
            this.tblist.addAll(this.pagelist);
            if (this.imageList != null) {
                this.imageList.clear();
            }
            if (this.imagePosition != null) {
                this.imagePosition.clear();
            }
            int i = 0;
            int i2 = 0;
            for (ChatMessageBean chatMessageBean : this.tblist) {
                if (chatMessageBean.getType() == 2 || chatMessageBean.getType() == 3) {
                    this.imageList.add(chatMessageBean.getImageLocal());
                    this.imagePosition.put(Integer.valueOf(i), Integer.valueOf(i2));
                    i2++;
                }
                i++;
            }
            this.tbAdapter.setImageList(this.imageList);
            this.tbAdapter.setImagePosition(this.imagePosition);
            this.sendMessageHandler.sendEmptyMessage(IMBaseActivity.PULL_TO_REFRESH_DOWN);
            if (this.page == 0) {
                this.pullList.setPullGone();
            } else {
                this.page--;
            }
        } else if (this.page == 0) {
            this.pullList.setPullGone();
        }
    }

    @Override // com.forum.im.ui.base.IMBaseActivity
    protected void sendMessage(final Long l, final Boolean bool, final String str) {
        new Thread(new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.7
            @Override // java.lang.Runnable
            public void run() {
                Time time;
                String obj = ListViewChatActivity.this.mEditTextContent.getText().toString();
                Log.e("ListViewChatActivity", "content = " + obj);
                boolean booleanValue = bool.booleanValue();
                Float valueOf = Float.valueOf(0.0f);
                if (booleanValue) {
                    if (obj.length() == 0) {
                        Looper.prepare();
                        Toast.makeText(ListViewChatActivity.this, (int) R.string.input_content, 1).show();
                        Looper.loop();
                        return;
                    }
                    new Time().setToNow();
                    String str2 = time.year + "-" + (time.month + 1) + "-" + time.monthDay + " " + time.hour + ":" + time.minute + ":" + time.second;
                    Log.e("ListViewChatActivity", "t.month = " + time.month);
                    List<ChatMessageBean> list = ListViewChatActivity.this.tblist;
                    ListViewChatActivity listViewChatActivity = ListViewChatActivity.this;
                    list.add(listViewChatActivity.getTbub(l, listViewChatActivity.userName, 1, ListViewChatActivity.this.mEditTextContent.getText().toString(), str2, null, null, null, null, null, valueOf, 1));
                    ListViewChatActivity.this.runOnUiThread(new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ListViewChatActivity.this.mEditTextContent.setText("");
                            ListViewChatActivity.this.tbAdapter.notifyDataSetChanged();
                        }
                    });
                    if (ListViewChatActivity.this.mCurrentPhotoFile != null) {
                        ListViewChatActivity.this.uploadFile();
                        return;
                    } else {
                        ListViewChatActivity.this.uploadMsg();
                        return;
                    }
                }
                List<ChatMessageBean> list2 = ListViewChatActivity.this.tblist;
                ListViewChatActivity listViewChatActivity2 = ListViewChatActivity.this;
                list2.add(listViewChatActivity2.getTbub(l, listViewChatActivity2.userName, 1, ListViewChatActivity.this.sendContent, str, null, null, null, null, null, valueOf, 1));
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void receriveMsgText(final Long l, final String str) {
        new Thread(new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.8
            @Override // java.lang.Runnable
            public void run() {
                ChatMessageBean chatMessageBean = new ChatMessageBean();
                chatMessageBean.setUserName(ListViewChatActivity.this.userName);
                chatMessageBean.setId(l);
                chatMessageBean.setTime(ListViewChatActivity.this.sendTime);
                chatMessageBean.setUserContent("" + str);
                chatMessageBean.setType(0);
                ListViewChatActivity.this.tblist.add(chatMessageBean);
            }
        }).start();
    }

    @Override // com.forum.im.ui.base.IMBaseActivity
    protected void sendImage(final Long l, final String str, final Boolean bool) {
        Log.e("ListViewChatActivity", "mCurrentPhotopath = " + this.mCurrentPhotoFile);
        new Thread(new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.9
            @Override // java.lang.Runnable
            public void run() {
                List<ChatMessageBean> list = ListViewChatActivity.this.tblist;
                ListViewChatActivity listViewChatActivity = ListViewChatActivity.this;
                Long l2 = l;
                String str2 = listViewChatActivity.userName;
                String str3 = ListViewChatActivity.this.sendTime;
                String str4 = str;
                list.add(listViewChatActivity.getTbub(l2, str2, 3, null, str3, null, str4, str4, null, null, Float.valueOf(0.0f), 0));
                ListViewChatActivity.this.imageList.add(ListViewChatActivity.this.tblist.get(ListViewChatActivity.this.tblist.size() - 1).getImageLocal());
                ListViewChatActivity.this.imagePosition.put(Integer.valueOf(ListViewChatActivity.this.tblist.size() - 1), Integer.valueOf(ListViewChatActivity.this.imageList.size() - 1));
                ListViewChatActivity.this.sendMessageHandler.sendEmptyMessage(IMBaseActivity.SEND_OK);
                if (bool.booleanValue()) {
                    Log.e("ListViewChatActivity", "mCurrentPhotoFile = " + ListViewChatActivity.this.mCurrentPhotoFile);
                    ListViewChatActivity.this.uploadFile();
                }
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void receriveImageText(final Long l, final String str) {
        new Thread(new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.10
            @Override // java.lang.Runnable
            public void run() {
                ChatMessageBean chatMessageBean = new ChatMessageBean();
                chatMessageBean.setUserName(ListViewChatActivity.this.userName);
                chatMessageBean.setId(l);
                chatMessageBean.setTime(ListViewChatActivity.this.sendTime);
                chatMessageBean.setImageLocal(str);
                chatMessageBean.setImageUrl(str);
                chatMessageBean.setImageLocal(str);
                chatMessageBean.setType(2);
                ListViewChatActivity.this.tblist.add(chatMessageBean);
                ListViewChatActivity.this.imageList.add(ListViewChatActivity.this.tblist.get(ListViewChatActivity.this.tblist.size() - 1).getImageLocal());
                ListViewChatActivity.this.imagePosition.put(Integer.valueOf(ListViewChatActivity.this.tblist.size() - 1), Integer.valueOf(ListViewChatActivity.this.imageList.size() - 1));
                ListViewChatActivity.this.sendMessageHandler.sendEmptyMessage(IMBaseActivity.RECERIVE_OK);
                ListViewChatActivity.this.mChatDbManager.insert(chatMessageBean);
            }
        }).start();
    }

    @Override // com.forum.im.ui.base.IMBaseActivity
    protected void sendVoice(float f, String str) {
        new Thread(new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.11
            @Override // java.lang.Runnable
            public void run() {
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void receriveVoiceText(final float f, final String str) {
        new Thread(new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.12
            @Override // java.lang.Runnable
            public void run() {
                ChatMessageBean chatMessageBean = new ChatMessageBean();
                chatMessageBean.setUserName(ListViewChatActivity.this.userName);
                chatMessageBean.setTime(IMBaseActivity.returnTime());
                chatMessageBean.setUserVoiceTime(f);
                chatMessageBean.setUserVoicePath(str);
                List<String> list = ListViewChatActivity.this.tbAdapter.unReadPosition;
                list.add(ListViewChatActivity.this.tblist.size() + "");
                chatMessageBean.setType(4);
                ListViewChatActivity.this.tblist.add(chatMessageBean);
                ListViewChatActivity.this.sendMessageHandler.sendEmptyMessage(IMBaseActivity.RECERIVE_OK);
                ListViewChatActivity.this.mChatDbManager.insert(chatMessageBean);
            }
        }).start();
    }

    public void getImMsgList() {
        this.tblist.clear();
        this.params.clear();
        if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
            this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        }
        HttpUtil.getInstance().getSourceData(false, this, Constant.queryInstantMsgByApp, this.params, new HttpUtil.HttpCallBack() { // from class: com.forum.im.ui.ListViewChatActivity.14
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                JSON.parse(str);
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<PageBean<CommentBean>>>() { // from class: com.forum.im.ui.ListViewChatActivity.14.1
                }, new Feature[0]);
                if (responseBean != null) {
                    if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                        final PageBean pageBean = (PageBean) responseBean.getContent();
                        if (pageBean == null || pageBean.getList().size() <= 0) {
                            return;
                        }
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() { // from class: com.forum.im.ui.ListViewChatActivity.14.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (ListViewChatActivity.this.k >= pageBean.getList().size()) {
                                    ListViewChatActivity.this.k = 0;
                                    ListViewChatActivity.this.tbAdapter.notifyDataSetChanged();
                                    handler.removeCallbacks(this);
                                    return;
                                }
                                ListViewChatActivity.this.sendTime = ((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getPublishTime();
                                if (((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getAuthor().equalsIgnoreCase(LedBleActivity.getBaseApp().getUserName())) {
                                    if (((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getImageVisitUrl().length() <= 0) {
                                        ListViewChatActivity.this.sendContent = ((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getContent();
                                        ListViewChatActivity.this.sendMessage(Long.valueOf(((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getId()), false, ((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getPublishTime());
                                    } else {
                                        ListViewChatActivity.this.sendImage(Long.valueOf(((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getId()), ((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getImageVisitUrl(), false);
                                    }
                                } else if (((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getImageVisitUrl().length() > 0) {
                                    ListViewChatActivity.this.receriveImageText(Long.valueOf(((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getId()), ((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getImageVisitUrl());
                                } else {
                                    ListViewChatActivity.this.receriveMsgText(Long.valueOf(((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getId()), ((CommentBean) pageBean.getList().get(ListViewChatActivity.this.k)).getContent());
                                }
                                ListViewChatActivity.access$708(ListViewChatActivity.this);
                                handler.postDelayed(this, 10L);
                            }
                        }, 1L);
                    } else if (Constant.NODATA_CODE.equals(responseBean.getReturnCode())) {
                        ListViewChatActivity listViewChatActivity = ListViewChatActivity.this;
                        listViewChatActivity.receriveMsgText(null, listViewChatActivity.getString(R.string.please_describe_your_problem));
                        ListViewChatActivity.this.tbAdapter.notifyDataSetChanged();
                    } else {
                        ListViewChatActivity listViewChatActivity2 = ListViewChatActivity.this;
                        listViewChatActivity2.receriveMsgText(null, listViewChatActivity2.getString(R.string.please_describe_your_problem));
                        ListViewChatActivity.shouToast(ListViewChatActivity.this.getApplicationContext(), ListViewChatActivity.this.getResources().getString(R.string.no_more_data));
                    }
                }
            }
        });
    }

    public void uploadMsg() {
        this.reply.clear();
        this.reply.put("imageUrl", "");
        this.reply.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        this.reply.put(Utils.RESPONSE_CONTENT, this.mEditTextContent.getText().toString());
        HttpUtil.getInstance().getSourceData(false, this, Constant.sendMessageByApp, this.reply, new HttpUtil.HttpCallBack() { // from class: com.forum.im.ui.ListViewChatActivity.15
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.forum.im.ui.ListViewChatActivity.15.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                Toast.makeText(ListViewChatActivity.this.getApplicationContext(), (int) R.string.comment_publish_success, 0).show();
            }
        });
    }

    public void uploadFile() {
        this.files.clear();
        this.reply.clear();
        this.reply.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        this.reply.put(Utils.RESPONSE_CONTENT, this.mEditTextContent.getText().toString());
        if (this.mCurrentPhotoFile != null) {
            this.files.put(this.mCurrentPhotoFile.getName(), this.mCurrentPhotoFile);
        }
        HttpUtil.getInstance().uploadFile(false, this, Constant.sendMessageByApp, this.reply, this.files, new HttpUtil.HttpCallBack() { // from class: com.forum.im.ui.ListViewChatActivity.16
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.forum.im.ui.ListViewChatActivity.16.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                Toast.makeText(ListViewChatActivity.this.getApplicationContext(), (int) R.string.comment_publish_success, 0).show();
                ListViewChatActivity.this.setResult(FirstActivity.RESULT222);
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Context applicationContext = ListViewChatActivity.this.getApplicationContext();
                Toast.makeText(applicationContext, "" + str, 0).show();
            }
        });
    }

    public void delectMsg(Long l) {
        this.reply.clear();
        this.reply.put("messageId", String.valueOf(l));
        this.reply.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        HttpUtil.getInstance().getSourceData(false, this, Constant.deleteMessageByApp, this.reply, new HttpUtil.HttpCallBack() { // from class: com.forum.im.ui.ListViewChatActivity.17
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.forum.im.ui.ListViewChatActivity.17.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                Toast.makeText(ListViewChatActivity.this.getApplicationContext(), (int) R.string.delete_message_success, 0).show();
            }
        });
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
