package com.forum.im.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.common.uitl.Constant;
import com.forum.im.db.ChatMessageBean;
import com.forum.im.ui.ImageViewActivity;
import com.forum.im.utils.FileSaveUtil;
import com.forum.im.utils.ImageCheckoutUtil;
import com.forum.im.widget.BubbleImageView;
import com.forum.im.widget.GifTextView;
import com.forum.im.widget.MediaManager;
import com.home.base.LedBleActivity;
import com.ledlamp.R;
import com.squareup.picasso.Picasso;
import com.video.CircleImageView;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class ChatListViewAdapter extends BaseAdapter {
    public static final int FROM_USER_IMG = 2;
    public static final int FROM_USER_MSG = 0;
    public static final int FROM_USER_VOICE = 4;
    public static final int TO_USER_IMG = 3;
    public static final int TO_USER_MSG = 1;
    public static final int TO_USER_VOICE = 5;
    private Animation an;
    private Context context;
    public MyHandler handler;
    private LongPressListener longPressListener;
    private LayoutInflater mLayoutInflater;
    private int mMaxItemWith;
    private int mMinItemWith;
    private SendErrorListener sendErrorListener;
    private VoiceIsRead voiceIsRead;
    private List<ChatMessageBean> userList = new ArrayList();
    private ArrayList<String> imageList = new ArrayList<>();
    private HashMap<Integer, Integer> imagePosition = new HashMap<>();
    public List<String> unReadPosition = new ArrayList();
    private int voicePlayPosition = -1;
    private boolean isGif = true;
    public boolean isPicRefresh = true;

    /* loaded from: classes.dex */
    public interface LongPressListener {
        void onLongPress(View view, int i);
    }

    /* loaded from: classes.dex */
    public interface SendErrorListener {
        void onClick(int i);
    }

    /* loaded from: classes.dex */
    public interface VoiceIsRead {
        void voiceOnClick(int i);
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return 6;
    }

    public void setLongPressListener(LongPressListener longPressListener) {
        this.longPressListener = longPressListener;
    }

    public void setSendErrorListener(SendErrorListener sendErrorListener) {
        this.sendErrorListener = sendErrorListener;
    }

    public void setVoiceIsReadListener(VoiceIsRead voiceIsRead) {
        this.voiceIsRead = voiceIsRead;
    }

    public ChatListViewAdapter(Context context) {
        this.context = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        this.mMaxItemWith = (int) (displayMetrics.widthPixels * 0.5f);
        this.mMinItemWith = (int) (displayMetrics.widthPixels * 0.15f);
        this.handler = new MyHandler(this);
    }

    /* loaded from: classes.dex */
    public static class MyHandler extends Handler {
        private final WeakReference<ChatListViewAdapter> mTbAdapter;

        public MyHandler(ChatListViewAdapter chatListViewAdapter) {
            this.mTbAdapter = new WeakReference<>(chatListViewAdapter);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            this.mTbAdapter.get();
        }
    }

    public void setIsGif(boolean z) {
        this.isGif = z;
    }

    public void setUserList(List<ChatMessageBean> list) {
        this.userList = list;
    }

    public void setImageList(ArrayList<String> arrayList) {
        this.imageList = arrayList;
    }

    public void setImagePosition(HashMap<Integer, Integer> hashMap) {
        this.imagePosition = hashMap;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.userList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.userList.get(i);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        if (this.userList.size() > 0) {
            return this.userList.get(i).getType();
        }
        return 0;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        FromUserMsgViewHolder fromUserMsgViewHolder;
        ToUserMsgViewHolder toUserMsgViewHolder;
        FromUserImageViewHolder fromUserImageViewHolder;
        ToUserImgViewHolder toUserImgViewHolder;
        FromUserVoiceViewHolder fromUserVoiceViewHolder;
        ToUserVoiceViewHolder toUserVoiceViewHolder;
        if (this.userList.size() > 0) {
            ChatMessageBean chatMessageBean = this.userList.get(i);
            int itemViewType = getItemViewType(i);
            if (itemViewType == 0) {
                if (view == null) {
                    fromUserMsgViewHolder = new FromUserMsgViewHolder();
                    view2 = this.mLayoutInflater.inflate(R.layout.layout_msgfrom_list_item, (ViewGroup) null);
                    fromUserMsgViewHolder.headicon = (ImageView) view2.findViewById(R.id.tb_other_user_icon);
                    fromUserMsgViewHolder.chat_time = (TextView) view2.findViewById(R.id.chat_time);
                    fromUserMsgViewHolder.content = (GifTextView) view2.findViewById(R.id.content);
                    view2.setTag(fromUserMsgViewHolder);
                } else {
                    view2 = view;
                    fromUserMsgViewHolder = (FromUserMsgViewHolder) view.getTag();
                }
                fromMsgUserLayout(fromUserMsgViewHolder, chatMessageBean, i);
            } else if (itemViewType == 1) {
                if (view == null) {
                    toUserMsgViewHolder = new ToUserMsgViewHolder();
                    view2 = this.mLayoutInflater.inflate(R.layout.layout_msgto_list_item, (ViewGroup) null);
                    toUserMsgViewHolder.headicon = (CircleImageView) view2.findViewById(R.id.tb_my_user_icon);
                    toUserMsgViewHolder.chat_time = (TextView) view2.findViewById(R.id.mychat_time);
                    toUserMsgViewHolder.content = (GifTextView) view2.findViewById(R.id.mycontent);
                    toUserMsgViewHolder.sendFailImg = (ImageView) view2.findViewById(R.id.mysend_fail_img);
                    view2.setTag(toUserMsgViewHolder);
                } else {
                    view2 = view;
                    toUserMsgViewHolder = (ToUserMsgViewHolder) view.getTag();
                }
                toMsgUserLayout(toUserMsgViewHolder, chatMessageBean, i);
            } else if (itemViewType == 2) {
                if (view == null) {
                    fromUserImageViewHolder = new FromUserImageViewHolder();
                    view2 = this.mLayoutInflater.inflate(R.layout.layout_imagefrom_list_item, (ViewGroup) null);
                    fromUserImageViewHolder.headicon = (ImageView) view2.findViewById(R.id.tb_other_user_icon);
                    fromUserImageViewHolder.chat_time = (TextView) view2.findViewById(R.id.chat_time);
                    fromUserImageViewHolder.image_Msg = (BubbleImageView) view2.findViewById(R.id.image_message);
                    view2.setTag(fromUserImageViewHolder);
                } else {
                    view2 = view;
                    fromUserImageViewHolder = (FromUserImageViewHolder) view.getTag();
                }
                fromImgUserLayout(fromUserImageViewHolder, chatMessageBean, i);
            } else if (itemViewType == 3) {
                if (view == null) {
                    toUserImgViewHolder = new ToUserImgViewHolder();
                    view2 = this.mLayoutInflater.inflate(R.layout.layout_imageto_list_item, (ViewGroup) null);
                    toUserImgViewHolder.headicon = (CircleImageView) view2.findViewById(R.id.tb_my_user_icon);
                    toUserImgViewHolder.chat_time = (TextView) view2.findViewById(R.id.mychat_time);
                    toUserImgViewHolder.sendFailImg = (ImageView) view2.findViewById(R.id.mysend_fail_img);
                    toUserImgViewHolder.image_group = (LinearLayout) view2.findViewById(R.id.image_group);
                    toUserImgViewHolder.image_Msg = (BubbleImageView) view2.findViewById(R.id.image_message);
                    view2.setTag(toUserImgViewHolder);
                } else {
                    view2 = view;
                    toUserImgViewHolder = (ToUserImgViewHolder) view.getTag();
                }
                toImgUserLayout(toUserImgViewHolder, chatMessageBean, i);
            } else if (itemViewType == 4) {
                if (view == null) {
                    fromUserVoiceViewHolder = new FromUserVoiceViewHolder();
                    view2 = this.mLayoutInflater.inflate(R.layout.layout_voicefrom_list_item, (ViewGroup) null);
                    fromUserVoiceViewHolder.headicon = (ImageView) view2.findViewById(R.id.tb_other_user_icon);
                    fromUserVoiceViewHolder.chat_time = (TextView) view2.findViewById(R.id.chat_time);
                    fromUserVoiceViewHolder.voice_group = (LinearLayout) view2.findViewById(R.id.voice_group);
                    fromUserVoiceViewHolder.voice_time = (TextView) view2.findViewById(R.id.voice_time);
                    fromUserVoiceViewHolder.receiver_voice_unread = view2.findViewById(R.id.receiver_voice_unread);
                    fromUserVoiceViewHolder.voice_image = (FrameLayout) view2.findViewById(R.id.voice_receiver_image);
                    fromUserVoiceViewHolder.voice_anim = view2.findViewById(R.id.id_receiver_recorder_anim);
                    view2.setTag(fromUserVoiceViewHolder);
                } else {
                    view2 = view;
                    fromUserVoiceViewHolder = (FromUserVoiceViewHolder) view.getTag();
                }
                fromVoiceUserLayout(fromUserVoiceViewHolder, chatMessageBean, i);
            } else if (itemViewType != 5) {
                return view;
            } else {
                if (view == null) {
                    toUserVoiceViewHolder = new ToUserVoiceViewHolder();
                    view2 = this.mLayoutInflater.inflate(R.layout.layout_voiceto_list_item, (ViewGroup) null);
                    toUserVoiceViewHolder.headicon = (ImageView) view2.findViewById(R.id.tb_my_user_icon);
                    toUserVoiceViewHolder.chat_time = (TextView) view2.findViewById(R.id.mychat_time);
                    toUserVoiceViewHolder.voice_group = (LinearLayout) view2.findViewById(R.id.voice_group);
                    toUserVoiceViewHolder.voice_time = (TextView) view2.findViewById(R.id.voice_time);
                    toUserVoiceViewHolder.voice_image = (FrameLayout) view2.findViewById(R.id.voice_image);
                    toUserVoiceViewHolder.voice_anim = view2.findViewById(R.id.id_recorder_anim);
                    toUserVoiceViewHolder.sendFailImg = (ImageView) view2.findViewById(R.id.mysend_fail_img);
                    view2.setTag(toUserVoiceViewHolder);
                } else {
                    view2 = view;
                    toUserVoiceViewHolder = (ToUserVoiceViewHolder) view.getTag();
                }
                toVoiceUserLayout(toUserVoiceViewHolder, chatMessageBean, i);
            }
            return view2;
        }
        return view;
    }

    /* loaded from: classes.dex */
    public class FromUserMsgViewHolder {
        public TextView chat_time;
        public GifTextView content;
        public ImageView headicon;

        public FromUserMsgViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    public class FromUserImageViewHolder {
        public TextView chat_time;
        public ImageView headicon;
        public BubbleImageView image_Msg;

        public FromUserImageViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    public class FromUserVoiceViewHolder {
        public TextView chat_time;
        public ImageView headicon;
        public View receiver_voice_unread;
        public View voice_anim;
        public LinearLayout voice_group;
        public FrameLayout voice_image;
        public TextView voice_time;

        public FromUserVoiceViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    public class ToUserMsgViewHolder {
        public TextView chat_time;
        public GifTextView content;
        public CircleImageView headicon;
        public ImageView sendFailImg;

        public ToUserMsgViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    public class ToUserImgViewHolder {
        public TextView chat_time;
        public CircleImageView headicon;
        public BubbleImageView image_Msg;
        public LinearLayout image_group;
        public ImageView sendFailImg;

        public ToUserImgViewHolder() {
        }
    }

    /* loaded from: classes.dex */
    public class ToUserVoiceViewHolder {
        public TextView chat_time;
        public ImageView headicon;
        public View receiver_voice_unread;
        public ImageView sendFailImg;
        public View voice_anim;
        public LinearLayout voice_group;
        public FrameLayout voice_image;
        public TextView voice_time;

        public ToUserVoiceViewHolder() {
        }
    }

    private void fromMsgUserLayout(FromUserMsgViewHolder fromUserMsgViewHolder, ChatMessageBean chatMessageBean, int i) {
        fromUserMsgViewHolder.headicon.setBackgroundResource(R.drawable.customer_service_icon);
        if (i != 0) {
            if (chatMessageBean.getTime() != null) {
                fromUserMsgViewHolder.chat_time.setVisibility(0);
                fromUserMsgViewHolder.chat_time.setText(chatMessageBean.getTime());
            } else {
                fromUserMsgViewHolder.chat_time.setVisibility(8);
            }
        } else {
            fromUserMsgViewHolder.chat_time.setVisibility(0);
            fromUserMsgViewHolder.chat_time.setText(chatMessageBean.getTime());
        }
        fromUserMsgViewHolder.content.setVisibility(0);
        fromUserMsgViewHolder.content.setSpanText(this.handler, chatMessageBean.getUserContent(), this.isGif);
    }

    private void fromImgUserLayout(FromUserImageViewHolder fromUserImageViewHolder, ChatMessageBean chatMessageBean, final int i) {
        fromUserImageViewHolder.headicon.setBackgroundResource(R.drawable.customer_service_icon);
        boolean z = false;
        if (i != 0) {
            String time = getTime(chatMessageBean.getTime(), this.userList.get(i - 1).getTime());
            if (time != null) {
                fromUserImageViewHolder.chat_time.setVisibility(0);
                fromUserImageViewHolder.chat_time.setText(time);
            } else {
                fromUserImageViewHolder.chat_time.setVisibility(8);
            }
        } else {
            String time2 = getTime(chatMessageBean.getTime(), null);
            fromUserImageViewHolder.chat_time.setVisibility(0);
            fromUserImageViewHolder.chat_time.setText(time2);
        }
        if (this.isPicRefresh) {
            String imageLocal = chatMessageBean.getImageLocal() == null ? "" : chatMessageBean.getImageLocal();
            String imageUrl = chatMessageBean.getImageUrl() == null ? "" : chatMessageBean.getImageUrl();
            if (chatMessageBean.getImageIconUrl() != null) {
                chatMessageBean.getImageIconUrl();
            }
            File file = new File(imageLocal);
            if (!imageLocal.equals("") && FileSaveUtil.isFileExists(file)) {
                z = true;
            }
            if (z) {
                fromUserImageViewHolder.image_Msg.setLocalImageBitmap(ImageCheckoutUtil.getLoacalBitmap(imageLocal), R.drawable.chatfrom_bg_focused);
            }
            if (imageUrl.length() > 0) {
                Log.e("ChatListViewAdapter", "imageUrlSrc = " + imageUrl);
                Glide.with(this.context).load(imageUrl).into(fromUserImageViewHolder.image_Msg);
            }
            fromUserImageViewHolder.image_Msg.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ChatListViewAdapter.this.stopPlayVoice();
                    Intent intent = new Intent(ChatListViewAdapter.this.context, ImageViewActivity.class);
                    intent.putStringArrayListExtra("images", ChatListViewAdapter.this.imageList);
                    intent.putExtra("clickedIndex", (Serializable) ChatListViewAdapter.this.imagePosition.get(Integer.valueOf(i)));
                    ChatListViewAdapter.this.context.startActivity(intent);
                }
            });
        }
    }

    private void fromVoiceUserLayout(final FromUserVoiceViewHolder fromUserVoiceViewHolder, final ChatMessageBean chatMessageBean, final int i) {
        List<String> list;
        fromUserVoiceViewHolder.headicon.setBackgroundResource(R.drawable.customer_service_icon);
        if (i != 0) {
            String time = getTime(chatMessageBean.getTime(), this.userList.get(i - 1).getTime());
            if (time != null) {
                fromUserVoiceViewHolder.chat_time.setVisibility(0);
                fromUserVoiceViewHolder.chat_time.setText(time);
            } else {
                fromUserVoiceViewHolder.chat_time.setVisibility(8);
            }
        } else {
            String time2 = getTime(chatMessageBean.getTime(), null);
            fromUserVoiceViewHolder.chat_time.setVisibility(0);
            fromUserVoiceViewHolder.chat_time.setText(time2);
        }
        fromUserVoiceViewHolder.voice_group.setVisibility(0);
        if (fromUserVoiceViewHolder.receiver_voice_unread != null) {
            fromUserVoiceViewHolder.receiver_voice_unread.setVisibility(8);
        }
        if (fromUserVoiceViewHolder.receiver_voice_unread != null && (list = this.unReadPosition) != null) {
            Iterator<String> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (it.next().equals(i + "")) {
                    fromUserVoiceViewHolder.receiver_voice_unread.setVisibility(0);
                    break;
                }
            }
        }
        fromUserVoiceViewHolder.voice_anim.setId(i);
        if (i == this.voicePlayPosition) {
            fromUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.receiver_voice_node_playing003);
            fromUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.voice_play_receiver);
            ((AnimationDrawable) fromUserVoiceViewHolder.voice_anim.getBackground()).start();
        } else {
            fromUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.receiver_voice_node_playing003);
        }
        fromUserVoiceViewHolder.voice_group.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (fromUserVoiceViewHolder.receiver_voice_unread != null) {
                    fromUserVoiceViewHolder.receiver_voice_unread.setVisibility(8);
                }
                fromUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.receiver_voice_node_playing003);
                ChatListViewAdapter.this.stopPlayVoice();
                ChatListViewAdapter.this.voicePlayPosition = fromUserVoiceViewHolder.voice_anim.getId();
                fromUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.voice_play_receiver);
                ((AnimationDrawable) fromUserVoiceViewHolder.voice_anim.getBackground()).start();
                String userVoicePath = chatMessageBean.getUserVoicePath() == null ? "" : chatMessageBean.getUserVoicePath();
                File file = new File(userVoicePath);
                if (userVoicePath.equals("") || !FileSaveUtil.isFileExists(file)) {
                    userVoicePath = chatMessageBean.getUserVoiceUrl() != null ? chatMessageBean.getUserVoiceUrl() : "";
                }
                if (ChatListViewAdapter.this.voiceIsRead != null) {
                    ChatListViewAdapter.this.voiceIsRead.voiceOnClick(i);
                }
                MediaManager.playSound(userVoicePath, new MediaPlayer.OnCompletionListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.2.1
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ChatListViewAdapter.this.voicePlayPosition = -1;
                        fromUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.receiver_voice_node_playing003);
                    }
                });
            }
        });
        float floatValue = new BigDecimal(chatMessageBean.getUserVoiceTime()).setScale(1, 4).floatValue();
        TextView textView = fromUserVoiceViewHolder.voice_time;
        textView.setText(floatValue + "\"");
        ViewGroup.LayoutParams layoutParams = fromUserVoiceViewHolder.voice_image.getLayoutParams();
        layoutParams.width = (int) (((float) this.mMinItemWith) + ((((float) this.mMaxItemWith) / 60.0f) * chatMessageBean.getUserVoiceTime()));
        fromUserVoiceViewHolder.voice_image.setLayoutParams(layoutParams);
    }

    private void toMsgUserLayout(ToUserMsgViewHolder toUserMsgViewHolder, ChatMessageBean chatMessageBean, final int i) {
        if (!LedBleActivity.getBaseApp().getUserHeadImage().equals("")) {
            Picasso.get().load(LedBleActivity.getBaseApp().getUserHeadImage()).placeholder(R.drawable.user_pic).into(toUserMsgViewHolder.headicon);
        } else {
            toUserMsgViewHolder.headicon.setImageResource(R.drawable.user_pic);
        }
        if (i != 0) {
            if (chatMessageBean.getTime() != null) {
                toUserMsgViewHolder.chat_time.setVisibility(0);
                toUserMsgViewHolder.chat_time.setText(chatMessageBean.getTime());
            } else {
                toUserMsgViewHolder.chat_time.setVisibility(8);
            }
        } else {
            toUserMsgViewHolder.chat_time.setVisibility(0);
            toUserMsgViewHolder.chat_time.setText(chatMessageBean.getTime());
        }
        toUserMsgViewHolder.content.setVisibility(0);
        toUserMsgViewHolder.content.setSpanText(this.handler, chatMessageBean.getUserContent(), this.isGif);
        toUserMsgViewHolder.content.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                if (ChatListViewAdapter.this.longPressListener != null) {
                    ChatListViewAdapter.this.longPressListener.onLongPress(view, i);
                    return false;
                }
                return false;
            }
        });
    }

    private void toImgUserLayout(ToUserImgViewHolder toUserImgViewHolder, ChatMessageBean chatMessageBean, final int i) {
        if (!LedBleActivity.getBaseApp().getUserHeadImage().equals("")) {
            Picasso.get().load(LedBleActivity.getBaseApp().getUserHeadImage()).placeholder(R.drawable.user_pic).into(toUserImgViewHolder.headicon);
        } else {
            toUserImgViewHolder.headicon.setImageResource(R.drawable.user_pic);
        }
        int sendState = chatMessageBean.getSendState();
        boolean z = true;
        if (sendState == 1) {
            toUserImgViewHolder.sendFailImg.clearAnimation();
            toUserImgViewHolder.sendFailImg.setVisibility(8);
        } else if (sendState == 2) {
            toUserImgViewHolder.sendFailImg.clearAnimation();
            toUserImgViewHolder.sendFailImg.setBackgroundResource(R.drawable.msg_state_fail_resend_pressed);
            toUserImgViewHolder.sendFailImg.setVisibility(0);
            toUserImgViewHolder.sendFailImg.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ChatListViewAdapter.this.sendErrorListener != null) {
                        ChatListViewAdapter.this.sendErrorListener.onClick(i);
                    }
                }
            });
        }
        if (i != 0) {
            String time = getTime(chatMessageBean.getTime(), this.userList.get(i - 1).getTime());
            if (time != null) {
                toUserImgViewHolder.chat_time.setVisibility(0);
                toUserImgViewHolder.chat_time.setText(time);
            } else {
                toUserImgViewHolder.chat_time.setVisibility(8);
            }
        } else {
            String time2 = getTime(chatMessageBean.getTime(), null);
            toUserImgViewHolder.chat_time.setVisibility(0);
            toUserImgViewHolder.chat_time.setText(time2);
        }
        if (this.isPicRefresh) {
            toUserImgViewHolder.image_group.setVisibility(0);
            String imageLocal = chatMessageBean.getImageLocal() == null ? "" : chatMessageBean.getImageLocal();
            String imageUrl = chatMessageBean.getImageUrl() == null ? "" : chatMessageBean.getImageUrl();
            String imageIconUrl = chatMessageBean.getImageIconUrl() == null ? "" : chatMessageBean.getImageIconUrl();
            if ((imageLocal.equals("") || !FileSaveUtil.isFileExists(new File(imageLocal))) ? false : false) {
                toUserImgViewHolder.image_Msg.setLocalImageBitmap(ImageCheckoutUtil.getLoacalBitmap(imageLocal), R.drawable.chatto_bg_focused);
            } else {
                toUserImgViewHolder.image_Msg.load(imageIconUrl, R.drawable.chatto_bg_focused, R.drawable.cygs_cs);
            }
            if (imageUrl.length() > 0) {
                toUserImgViewHolder.image_Msg.load(imageUrl, R.drawable.chatto_bg_focused, R.drawable.cygs_cs);
            }
            toUserImgViewHolder.image_Msg.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.5
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    Log.e("imageList", "imageList = " + ChatListViewAdapter.this.imageList);
                    Intent intent = new Intent(ChatListViewAdapter.this.context, ImageViewActivity.class);
                    intent.putStringArrayListExtra("images", ChatListViewAdapter.this.imageList);
                    intent.putExtra("clickedIndex", (Serializable) ChatListViewAdapter.this.imagePosition.get(Integer.valueOf(i)));
                    ChatListViewAdapter.this.context.startActivity(intent);
                }
            });
            toUserImgViewHolder.image_Msg.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.6
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    if (ChatListViewAdapter.this.longPressListener != null) {
                        ChatListViewAdapter.this.longPressListener.onLongPress(view, i);
                        return false;
                    }
                    return false;
                }
            });
        }
    }

    private void toVoiceUserLayout(final ToUserVoiceViewHolder toUserVoiceViewHolder, final ChatMessageBean chatMessageBean, final int i) {
        List<String> list;
        toUserVoiceViewHolder.headicon.setBackgroundResource(R.drawable.user_pic);
        int sendState = chatMessageBean.getSendState();
        if (sendState == 1) {
            toUserVoiceViewHolder.sendFailImg.clearAnimation();
            toUserVoiceViewHolder.sendFailImg.setVisibility(8);
        } else if (sendState == 2) {
            toUserVoiceViewHolder.sendFailImg.clearAnimation();
            toUserVoiceViewHolder.sendFailImg.setBackgroundResource(R.drawable.msg_state_fail_resend_pressed);
            toUserVoiceViewHolder.sendFailImg.setVisibility(0);
            toUserVoiceViewHolder.sendFailImg.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.7
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ChatListViewAdapter.this.sendErrorListener != null) {
                        ChatListViewAdapter.this.sendErrorListener.onClick(i);
                    }
                }
            });
        }
        toUserVoiceViewHolder.headicon.setImageDrawable(this.context.getResources().getDrawable(R.drawable.user_pic));
        if (i != 0) {
            String time = getTime(chatMessageBean.getTime(), this.userList.get(i - 1).getTime());
            if (time != null) {
                toUserVoiceViewHolder.chat_time.setVisibility(0);
                toUserVoiceViewHolder.chat_time.setText(time);
            } else {
                toUserVoiceViewHolder.chat_time.setVisibility(8);
            }
        } else {
            String time2 = getTime(chatMessageBean.getTime(), null);
            toUserVoiceViewHolder.chat_time.setVisibility(0);
            toUserVoiceViewHolder.chat_time.setText(time2);
        }
        toUserVoiceViewHolder.voice_group.setVisibility(0);
        if (toUserVoiceViewHolder.receiver_voice_unread != null) {
            toUserVoiceViewHolder.receiver_voice_unread.setVisibility(8);
        }
        if (toUserVoiceViewHolder.receiver_voice_unread != null && (list = this.unReadPosition) != null) {
            Iterator<String> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (it.next().equals(i + "")) {
                    toUserVoiceViewHolder.receiver_voice_unread.setVisibility(0);
                    break;
                }
            }
        }
        toUserVoiceViewHolder.voice_anim.setId(i);
        if (i == this.voicePlayPosition) {
            toUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.adj);
            toUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.voice_play_send);
            ((AnimationDrawable) toUserVoiceViewHolder.voice_anim.getBackground()).start();
        } else {
            toUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.adj);
        }
        toUserVoiceViewHolder.voice_group.setOnClickListener(new View.OnClickListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (toUserVoiceViewHolder.receiver_voice_unread != null) {
                    toUserVoiceViewHolder.receiver_voice_unread.setVisibility(8);
                }
                toUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.adj);
                ChatListViewAdapter.this.stopPlayVoice();
                ChatListViewAdapter.this.voicePlayPosition = toUserVoiceViewHolder.voice_anim.getId();
                toUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.voice_play_send);
                ((AnimationDrawable) toUserVoiceViewHolder.voice_anim.getBackground()).start();
                String userVoiceUrl = chatMessageBean.getUserVoiceUrl() == null ? "" : chatMessageBean.getUserVoiceUrl();
                if (ChatListViewAdapter.this.voiceIsRead != null) {
                    ChatListViewAdapter.this.voiceIsRead.voiceOnClick(i);
                }
                MediaManager.playSound(userVoiceUrl, new MediaPlayer.OnCompletionListener() { // from class: com.forum.im.adapter.ChatListViewAdapter.8.1
                    @Override // android.media.MediaPlayer.OnCompletionListener
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        ChatListViewAdapter.this.voicePlayPosition = -1;
                        toUserVoiceViewHolder.voice_anim.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });
        float floatValue = new BigDecimal(chatMessageBean.getUserVoiceTime()).setScale(1, 4).floatValue();
        TextView textView = toUserVoiceViewHolder.voice_time;
        textView.setText(floatValue + "\"");
        ViewGroup.LayoutParams layoutParams = toUserVoiceViewHolder.voice_image.getLayoutParams();
        layoutParams.width = (int) (((float) this.mMinItemWith) + ((((float) this.mMaxItemWith) / 60.0f) * chatMessageBean.getUserVoiceTime()));
        toUserVoiceViewHolder.voice_image.setLayoutParams(layoutParams);
    }

    public String getTime(String str, String str2) {
        String str3 = null;
        if (str2 != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.STRING_DAY_FORMAT4);
                long time = simpleDateFormat.parse(str).getTime() - simpleDateFormat.parse(str2).getTime();
                long j = (time / 86400000) * 24;
                if (((time / 60000) - (j * 60)) - (((time / 3600000) - j) * 60) >= 1) {
                    str3 = str.substring(11);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            str3 = str.substring(11);
        }
        String day = getDay(str);
        if (str3 == null || day == null) {
            return str3;
        }
        return day + " " + str3;
    }

    public static String returnTime() {
        return new SimpleDateFormat(Constant.STRING_DAY_FORMAT4).format(new Date());
    }

    public String getDay(String str) {
        String substring;
        String returnTime = returnTime();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constant.STRING_DAY_FORMAT4);
            long time = (simpleDateFormat.parse(returnTime).getTime() - simpleDateFormat.parse(str).getTime()) / 86400000;
            if (time >= 365) {
                substring = str.substring(0, 10);
            } else if (time < 1 || time >= 365) {
                return null;
            } else {
                substring = str.substring(5, 10);
            }
            return substring;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void stopPlayVoice() {
        int i = this.voicePlayPosition;
        if (i != -1) {
            View findViewById = ((Activity) this.context).findViewById(i);
            if (findViewById != null) {
                if (getItemViewType(this.voicePlayPosition) == 4) {
                    findViewById.setBackgroundResource(R.drawable.receiver_voice_node_playing003);
                } else {
                    findViewById.setBackgroundResource(R.drawable.adj);
                }
            }
            MediaManager.pause();
            this.voicePlayPosition = -1;
        }
    }
}
