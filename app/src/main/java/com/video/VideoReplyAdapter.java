package com.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.home.base.LedBleActivity;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class VideoReplyAdapter extends BaseAdapter {
    public static List<VideoReplyBean> listVideoBean = new ArrayList();
    private LedBleActivity activity;
    private String foorHost = "";
    private Map<String, Boolean> addFlag = new HashMap();

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public VideoReplyAdapter(LedBleActivity ledBleActivity, List<VideoReplyBean> list) {
        listVideoBean.clear();
        this.activity = ledBleActivity;
        listVideoBean = list;
        for (int i = 0; i < list.size(); i++) {
            this.addFlag.put(String.valueOf(i), false);
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return listVideoBean.size();
    }

    @Override // android.widget.Adapter
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view2;
        Holder holder;
        if (view == null) {
            holder = new Holder();
            view2 = View.inflate(viewGroup.getContext(), R.layout.item_video_reply, null);
            holder.tvAuthor9 = (TextView) view2.findViewById(R.id.tvAuthor9);
            holder.tvFloorHost9 = (TextView) view2.findViewById(R.id.tvFloorHost9);
            holder.tvFloorNo9 = (TextView) view2.findViewById(R.id.tvFloorNo9);
            holder.tvDate9 = (TextView) view2.findViewById(R.id.tvDate9);
            holder.tvContent9 = (TextView) view2.findViewById(R.id.tvContent9);
            holder.tv_say9 = (TextView) view2.findViewById(R.id.tv_say9);
            holder.tv_comment9 = (TextView) view2.findViewById(R.id.tv_comment9);
            holder.ci_2 = (ImageView) view2.findViewById(R.id.ci_2);
            view2.setTag(holder);
        } else {
            view2 = view;
            holder = (Holder) view.getTag();
        }
        final VideoReplyBean videoReplyBean = listVideoBean.get(i);
        holder.tv_comment9.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoReplyAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                Intent intent = new Intent(VideoReplyAdapter.this.activity, com.forum.adapter.ReviewActivity.class);
                intent.putExtra("video11", 12);
                intent.putExtra("replyBean", i);
                VideoReplyAdapter.this.activity.startActivityForResult(intent, 11);
            }
        });
        holder.tv_say9.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoReplyAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                LedBleActivity unused = VideoReplyAdapter.this.activity;
                if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
                    VideoReplyAdapter.this.activity.setResult(119);
                    VideoReplyAdapter.this.activity.finish();
                    return;
                }
                Intent intent = new Intent(VideoReplyAdapter.this.activity, ReviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("replyId", String.valueOf(videoReplyBean.getId()));
                bundle.putString("replyType", "reply");
                bundle.putString("target", videoReplyBean.getAuthor());
                intent.putExtras(bundle);
                VideoReplyAdapter.this.activity.startActivityForResult(intent, 11);
            }
        });
        holder.tvAuthor9.setText(videoReplyBean.getAuthor());
        if (isFloorHost(videoReplyBean.getAuthor())) {
            holder.tvFloorHost9.setVisibility(0);
        } else {
            holder.tvFloorHost9.setVisibility(8);
        }
        if (i == 0) {
            holder.tv_say9.setVisibility(8);
            holder.tvFloorNo9.setVisibility(8);
            holder.tvContent9.setVisibility(8);
            Glide.with((FragmentActivity) this.activity).load(videoReplyBean.getVideoVisitUrl());
        } else {
            holder.tvFloorNo9.setVisibility(0);
            TextView textView = holder.tvFloorNo9;
            textView.setText(i + this.activity.getString(R.string.floor));
        }
        holder.tvDate9.setText(videoReplyBean.getPublishTime());
        holder.tvContent9.setText(videoReplyBean.getContent());
        if (videoReplyBean.getList() != null && videoReplyBean.getList().size() != 0) {
            holder.tv_comment9.setVisibility(0);
        } else {
            holder.tv_comment9.setVisibility(8);
        }
        return view2;
    }

    /* loaded from: classes.dex */
    class Holder {
        ImageView ci_2;
        TextView tvAuthor9;
        TextView tvContent9;
        TextView tvDate9;
        TextView tvFloorHost9;
        TextView tvFloorNo9;
        TextView tv_comment9;
        TextView tv_say9;

        Holder() {
        }
    }

    public void setFoorHost(String str) {
        this.foorHost = str;
    }

    public boolean isFloorHost(String str) {
        return this.foorHost.equals(str);
    }
}
