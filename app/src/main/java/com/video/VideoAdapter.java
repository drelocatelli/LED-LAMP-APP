package com.video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.jzvd.JZVideoPlayerStandard;
import com.ledlamp.R;
import com.squareup.picasso.Picasso;
import java.util.List;

/* loaded from: classes.dex */
public class VideoAdapter extends RecyclerView.Adapter<ViewHolder> {
    private LayoutInflater inflater;
    private boolean isChecked;
    private PlayVideoActivity mContext;
    private List<String> mDatas;
    public OnItemClickListener mOnItemClickListerer;
    private int supportNum;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(int i, String str, View view, View view2, View view3);
    }

    public void setOnItemClickListerer(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListerer = onItemClickListener;
    }

    public VideoAdapter(PlayVideoActivity playVideoActivity, List<String> list) {
        this.mContext = playVideoActivity;
        this.mDatas = list;
        this.inflater = LayoutInflater.from(playVideoActivity);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.inflater.inflate(R.layout.adapter_play_video, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.jzVideo.setUp(String.valueOf(this.mDatas.get(i)), 0, "");
        viewHolder.jzVideo.fullscreenButton.setVisibility(8);
        viewHolder.jzVideo.backButton.setVisibility(0);
        viewHolder.jzVideo.backButton.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoAdapter.this.mOnItemClickListerer.onItemClick(i, "back", view, view, view);
            }
        });
        viewHolder.ll_back.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoAdapter.this.mOnItemClickListerer.onItemClick(i, "back", view, view, view);
            }
        });
        viewHolder.iv_commit.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoAdapter.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoAdapter.this.mOnItemClickListerer.onItemClick(i, "commit", view, view, view);
            }
        });
        viewHolder.iv_horizontal.setOnClickListener(new View.OnClickListener() { // from class: com.video.VideoAdapter.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VideoAdapter.this.mOnItemClickListerer.onItemClick(i, "horizontal", view, view, view);
            }
        });
        if (PlayVideoActivity.fromHelpActivity.equalsIgnoreCase("YES")) {
            viewHolder.tv_name.setVisibility(8);
            viewHolder.tv_uid.setVisibility(8);
            viewHolder.tv_context.setVisibility(8);
            viewHolder.iv_icon.setVisibility(8);
            viewHolder.iv_commit.setVisibility(8);
        } else if (VideoFragment.list.size() > 0) {
            viewHolder.tv_name.setText(VideoFragment.list.get(i).getAuthor());
            viewHolder.tv_context.setText(VideoFragment.list.get(i).getTitle());
            viewHolder.tv_uid.setText(VideoFragment.list.get(i).getPublishTime());
            viewHolder.tv_likeNum.setText(String.valueOf(VideoFragment.list.get(i).getSupportNum()));
            viewHolder.tv_commentNum.setText(String.valueOf(VideoFragment.list.get(i).getCommentNum()));
            Picasso.get().load(VideoFragment.list.get(i).getHeadImage()).placeholder(R.drawable.user_pic).into(viewHolder.iv_icon);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDatas.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_commit;
        ImageView iv_horizontal;
        ImageView iv_icon;
        public JZVideoPlayerStandard jzVideo;
        LinearLayout ll_back;
        TextView tv_commentNum;
        TextView tv_context;
        TextView tv_likeNum;
        TextView tv_name;
        TextView tv_uid;

        public ViewHolder(View view) {
            super(view);
            this.ll_back = (LinearLayout) view.findViewById(R.id.ll_back);
            this.iv_commit = (ImageView) view.findViewById(R.id.iv_commit);
            this.jzVideo = (JZVideoPlayerStandard) view.findViewById(R.id.jzVideo);
            this.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            this.tv_uid = (TextView) view.findViewById(R.id.tv_uid);
            this.tv_context = (TextView) view.findViewById(R.id.tv_context);
            this.tv_name = (TextView) view.findViewById(R.id.tv_name);
            this.tv_likeNum = (TextView) view.findViewById(R.id.tv_likeNum);
            this.tv_commentNum = (TextView) view.findViewById(R.id.tv_commentNum);
            this.iv_horizontal = (ImageView) view.findViewById(R.id.iv_horizontal);
        }
    }
}
