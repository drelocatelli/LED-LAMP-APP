package com.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.home.base.LedBleActivity;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class VideoListAdapter extends BaseAdapter {
    private LedBleActivity activity;
    private List<VideoReplyBean> list;
    private String replyId;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public VideoListAdapter(LedBleActivity ledBleActivity, String str, List<VideoReplyBean> list) {
        new ArrayList();
        this.activity = ledBleActivity;
        this.replyId = str;
        this.list = list;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = View.inflate(viewGroup.getContext(), R.layout.item_reply_list, null);
            viewHolder.llReply = (LinearLayout) view2.findViewById(R.id.llReply);
            viewHolder.tvAuthor = (TextView) view2.findViewById(R.id.tvAuthor);
            viewHolder.tvTarget = (TextView) view2.findViewById(R.id.tvTarget);
            viewHolder.tvContent = (TextView) view2.findViewById(R.id.tvContent);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        final VideoReplyBean videoReplyBean = this.list.get(i);
        if (videoReplyBean != null) {
            viewHolder.tvAuthor.setText(videoReplyBean.getAuthor());
            TextView textView = viewHolder.tvTarget;
            textView.setText(videoReplyBean.getTarget() + ":");
            viewHolder.tvContent.setText(videoReplyBean.getContent());
            viewHolder.llReply.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoListAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    Intent intent = new Intent(VideoListAdapter.this.activity, ReviewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("replyId", VideoListAdapter.this.replyId);
                    bundle.putString("replyType", "reply");
                    bundle.putString("target", videoReplyBean.getAuthor());
                    intent.putExtras(bundle);
                    VideoListAdapter.this.activity.startActivityForResult(intent, 10);
                }
            });
        }
        return view2;
    }

    /* loaded from: classes.dex */
    class ViewHolder {
        LinearLayout llReply;
        TextView tvAuthor;
        TextView tvContent;
        TextView tvTarget;

        ViewHolder() {
        }
    }
}
