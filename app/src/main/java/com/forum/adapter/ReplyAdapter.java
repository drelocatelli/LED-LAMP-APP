package com.forum.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.forum.ReleseCharacterActivity;
import com.forum.bean.ReplyBean;
import com.home.base.LedBleActivity;
import com.ledlamp.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ReplyAdapter extends BaseAdapter {
    public static List<ReplyBean> listReplyBean = new ArrayList();
    private LedBleActivity activity;
    ImageAdapter adapter;
    private Map<String, Boolean> addFlag = new HashMap();
    private String foorHost = "";

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public ReplyAdapter(LedBleActivity ledBleActivity, List<ReplyBean> list) {
        this.activity = ledBleActivity;
        listReplyBean = list;
        for (int i = 0; i < list.size(); i++) {
            this.addFlag.put(String.valueOf(i), false);
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return listReplyBean.size();
    }

    @Override // android.widget.Adapter
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view2;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view2 = View.inflate(viewGroup.getContext(), R.layout.item_reply, null);
            viewHolder.tvAuthor = (TextView) view2.findViewById(R.id.tvAuthor);
            viewHolder.tvFloorHost = (TextView) view2.findViewById(R.id.tvFloorHost);
            viewHolder.tvFloorNo = (TextView) view2.findViewById(R.id.tvFloorNo);
            viewHolder.tvDate = (TextView) view2.findViewById(R.id.tvDate);
            viewHolder.tvContent = (TextView) view2.findViewById(R.id.tvContent);
            viewHolder.gvImage = (GridView) view2.findViewById(R.id.gvImage);
            viewHolder.tv_say = (TextView) view2.findViewById(R.id.tv_say);
            viewHolder.tv_comment = (TextView) view2.findViewById(R.id.tv_comment);
            viewHolder.ci_1 = (ImageView) view2.findViewById(R.id.ci_1);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        final ReplyBean replyBean = listReplyBean.get(i);
        if (i == 0) {
            viewHolder.tv_say.setVisibility(8);
            viewHolder.tvFloorNo.setVisibility(8);
            viewHolder.tv_comment.setVisibility(8);
        } else {
            viewHolder.tvFloorNo.setVisibility(0);
            TextView textView = viewHolder.tvFloorNo;
            textView.setText(i + this.activity.getString(R.string.floor));
        }
        if (replyBean.getHeadImage() == null || replyBean.getHeadImage().equals("")) {
            viewHolder.ci_1.setImageResource(R.drawable.user_pic);
        } else {
            Picasso.get().load(replyBean.getHeadImage()).placeholder(R.drawable.user_pic).into(viewHolder.ci_1);
        }
        viewHolder.tv_comment.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ReplyAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                Intent intent = new Intent(ReplyAdapter.this.activity, ReviewActivity.class);
                intent.putExtra("replyBean", i);
                ReplyAdapter.this.activity.startActivityForResult(intent, 10);
            }
        });
        viewHolder.tv_say.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ReplyAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                LedBleActivity unused = ReplyAdapter.this.activity;
                if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
                    ReplyAdapter.this.activity.setResult(111);
                    ReplyAdapter.this.activity.finish();
                    return;
                }
                Intent intent = new Intent(ReplyAdapter.this.activity, ReleseCharacterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ReplyActivity", "ReplyAdapter");
                bundle.putString("replyId", String.valueOf(replyBean.getId()));
                bundle.putString("replyType", "reply");
                bundle.putString("target", replyBean.getAuthor());
                intent.putExtras(bundle);
                ReplyAdapter.this.activity.startActivityForResult(intent, 10);
            }
        });
        viewHolder.tvAuthor.setText(replyBean.getAuthor());
        if (isFloorHost(replyBean.getAuthor())) {
            viewHolder.tvFloorHost.setVisibility(0);
        } else {
            viewHolder.tvFloorHost.setVisibility(8);
        }
        viewHolder.tvDate.setText(replyBean.getPublishTime());
        viewHolder.tvContent.setText(replyBean.getContent());
        if (!replyBean.getImageVisitUrl().equals("")) {
            viewHolder.gvImage.setVisibility(0);
            ArrayList arrayList = new ArrayList();
            this.adapter = new ImageAdapter(this.activity, arrayList);
            viewHolder.gvImage.setAdapter((ListAdapter) this.adapter);
            String imageVisitUrl = replyBean.getImageVisitUrl();
            if (imageVisitUrl.contains(",")) {
                arrayList.addAll(Arrays.asList(imageVisitUrl.split(",")));
            } else {
                arrayList.add(imageVisitUrl);
            }
            this.adapter.notifyDataSetChanged();
        } else {
            viewHolder.gvImage.setVisibility(8);
        }
        if (replyBean.getList() != null && replyBean.getList().size() != 0) {
            viewHolder.tv_comment.setVisibility(0);
        } else {
            viewHolder.tv_comment.setVisibility(8);
        }
        return view2;
    }

    /* loaded from: classes.dex */
    class ViewHolder {
        ImageView ci_1;
        GridView gvImage;
        TextView tvAuthor;
        TextView tvContent;
        TextView tvDate;
        TextView tvFloorHost;
        TextView tvFloorNo;
        TextView tv_comment;
        TextView tv_say;

        ViewHolder() {
        }
    }

    public void setFoorHost(String str) {
        this.foorHost = str;
    }

    public boolean isFloorHost(String str) {
        return this.foorHost.equals(str);
    }
}
