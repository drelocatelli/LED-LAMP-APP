package com.forum.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.forum.ReleseCharacterActivity;
import com.forum.bean.ReplyBean;
import com.home.base.LedBleActivity;
import com.ledlamp.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ReplyListAdapter extends BaseAdapter {
    public static List<ReplyBean> list = new ArrayList();
    private LedBleActivity activity;
    private String replyId;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return 0L;
    }

    public ReplyListAdapter(LedBleActivity ledBleActivity, String str, List<ReplyBean> list2) {
        this.activity = ledBleActivity;
        this.replyId = str;
        list = list2;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return list.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return list.get(i);
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
            viewHolder.iv_contenthead = (ImageView) view2.findViewById(R.id.iv_contenthead);
            viewHolder.tv_con = (TextView) view2.findViewById(R.id.tv_con);
            view2.setTag(viewHolder);
        } else {
            view2 = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        final ReplyBean replyBean = list.get(i);
        if (replyBean != null) {
            viewHolder.tvAuthor.setText(replyBean.getAuthor());
            TextView textView = viewHolder.tvTarget;
            textView.setText(replyBean.getTarget() + " :");
            viewHolder.tvContent.setText(replyBean.getContent());
            if (replyBean.getHeadImage() == null || replyBean.getHeadImage().equals("")) {
                viewHolder.iv_contenthead.setImageResource(R.drawable.user_pic);
            } else {
                Picasso.get().load(replyBean.getHeadImage()).placeholder(R.drawable.user_pic).into(viewHolder.iv_contenthead);
            }
            viewHolder.tv_con.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.ReplyListAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view3) {
                    Intent intent = new Intent(ReplyListAdapter.this.activity, ReleseCharacterActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ReplyActivity", "ReplyListAdapter");
                    bundle.putString("replyId", ReplyListAdapter.this.replyId);
                    bundle.putString("replyType", "reply");
                    bundle.putString("target", replyBean.getAuthor());
                    intent.putExtras(bundle);
                    ReplyListAdapter.this.activity.startActivityForResult(intent, 10);
                }
            });
        }
        return view2;
    }

    /* loaded from: classes.dex */
    class ViewHolder {
        ImageView iv_contenthead;
        LinearLayout llReply;
        TextView tvAuthor;
        TextView tvContent;
        TextView tvTarget;
        TextView tv_con;

        ViewHolder() {
        }
    }
}
