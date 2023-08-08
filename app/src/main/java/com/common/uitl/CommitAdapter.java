package com.common.uitl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.ledlamp.R;
import com.squareup.picasso.Picasso;
import com.video.PlayVideoActivity;
import com.video.VideoReplyBean;

/* loaded from: classes.dex */
public class CommitAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    public OnItemClickListener mOnItemClickListerer;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(int i, String str);
    }

    public void setmOnItemClickListerer(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListerer = onItemClickListener;
    }

    public CommitAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(this.inflater.inflate(R.layout.item_video_commit, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        VideoReplyBean videoReplyBean = PlayVideoActivity.listReply.get(i);
        if (videoReplyBean.getHeadImage() == null || videoReplyBean.getHeadImage().equals("")) {
            myViewHolder.iv_icon111.setImageResource(R.drawable.user_pic);
        } else {
            Picasso.get().load(videoReplyBean.getHeadImage()).placeholder(R.drawable.user_pic).into(myViewHolder.iv_icon111);
        }
        myViewHolder.tv_context111.setText(videoReplyBean.getContent());
        myViewHolder.tv_time111.setText(videoReplyBean.getPublishTime());
        myViewHolder.tv_name111.setText(videoReplyBean.getAuthor());
        TextView textView = myViewHolder.lou;
        textView.setText((i + 1) + this.context.getString(R.string.floor));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return PlayVideoActivity.listReply.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_icon111;
        TextView lou;
        TextView tv_context111;
        TextView tv_name111;
        TextView tv_time111;

        public MyViewHolder(View view) {
            super(view);
            this.iv_icon111 = (ImageView) view.findViewById(R.id.iv_icon111);
            this.tv_name111 = (TextView) view.findViewById(R.id.tv_name111);
            this.tv_context111 = (TextView) view.findViewById(R.id.tv_context111);
            this.tv_time111 = (TextView) view.findViewById(R.id.tv_time111);
            this.lou = (TextView) view.findViewById(R.id.lou);
        }
    }
}
