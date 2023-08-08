package com.forum.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.common.net.NetResult;
import com.forum.ReplyActivity;
import com.forum.bean.CommentBean;
import com.home.base.LedBleActivity;
import com.ledlamp.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "CommentAdapter";
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 0;
    private LedBleActivity activity;
    ImageAdapter adapter;
    private String isLast;
    boolean isShowDes;
    private List<CommentBean> listC;

    public CommentAdapter(LedBleActivity ledBleActivity, List<CommentBean> list) {
        new ArrayList();
        this.isLast = NetResult.CODE_OK;
        this.isShowDes = false;
        this.activity = ledBleActivity;
        this.listC = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.listC.size() + 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return i + 1 == getItemCount() ? 1 : 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemViewHolder) {
            final CommentBean commentBean = this.listC.get(i);
            ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            if (commentBean != null) {
                itemViewHolder.tvTitle.setText(commentBean.getTitle());
                itemViewHolder.tvAuthor.setText(commentBean.getAuthor());
                itemViewHolder.tv_content.setText(commentBean.getContent());
                if (commentBean.getHeadImage() != null) {
                    Picasso.get().load(commentBean.getHeadImage()).placeholder(R.drawable.user_pic).into(itemViewHolder.iv_Author);
                } else {
                    itemViewHolder.iv_Author.setImageResource(R.drawable.user_pic);
                }
                if (commentBean.getPublishTime().contains(" ") && commentBean.getPublishTime().split(" ").length == 2) {
                    itemViewHolder.tvDate.setText(commentBean.getPublishTime().split(" ")[0]);
                } else {
                    itemViewHolder.tvDate.setText("");
                }
                itemViewHolder.llComment.setOnClickListener(new View.OnClickListener() { // from class: com.forum.adapter.CommentAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        Intent intent = new Intent(CommentAdapter.this.activity, ReplyActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("comment", commentBean);
                        intent.putExtras(bundle);
                        CommentAdapter.this.activity.startActivityForResult(intent, 111);
                    }
                });
                if (!commentBean.getImageVisitUrl().equals("")) {
                    itemViewHolder.iv_pichtor.setVisibility(0);
                    String imageVisitUrl = commentBean.getImageVisitUrl();
                    if (imageVisitUrl.contains(",")) {
                        String[] split = imageVisitUrl.split(",");
                        Log.e(TAG, "onBindViewHolder: " + split[0]);
                        loadCover(itemViewHolder.iv_pichtor, split[0], this.activity);
                        return;
                    }
                    loadCover(itemViewHolder.iv_pichtor, imageVisitUrl, this.activity);
                    return;
                }
                itemViewHolder.iv_pichtor.setVisibility(8);
            }
        } else if (viewHolder instanceof FooterViewHolder) {
            if ("1".equals(this.isLast)) {
                ((FooterViewHolder) viewHolder).tvTips.setVisibility(0);
            } else {
                ((FooterViewHolder) viewHolder).tvTips.setVisibility(8);
            }
        }
    }

    public static void loadCover(ImageView imageView, String str, Context context) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).setDefaultRequestOptions(new RequestOptions().frame(1000000L).centerCrop().fitCenter().placeholder(R.drawable.default_common).error(R.drawable.default_common)).load(str).into(imageView);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, (ViewGroup) null);
            inflate.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            return new ItemViewHolder(inflate);
        } else if (i == 1) {
            View inflate2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listview_footer, (ViewGroup) null);
            inflate2.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
            return new FooterViewHolder(inflate2);
        } else {
            return null;
        }
    }

    /* loaded from: classes.dex */
    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llLoading;
        RelativeLayout rlFooter;
        TextView tvTips;

        public FooterViewHolder(View view) {
            super(view);
            this.rlFooter = (RelativeLayout) view.findViewById(R.id.rlFooter);
            this.llLoading = (LinearLayout) view.findViewById(R.id.llLoading);
            this.tvTips = (TextView) view.findViewById(R.id.tvTips);
        }
    }

    /* loaded from: classes.dex */
    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Author;
        ImageView iv_pichtor;
        LinearLayout llComment;
        TextView tvAuthor;
        TextView tvDate;
        TextView tvTitle;
        TextView tv_content;

        public ItemViewHolder(View view) {
            super(view);
            this.llComment = (LinearLayout) view.findViewById(R.id.llComment);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.iv_Author = (ImageView) view.findViewById(R.id.iv_Author);
            this.tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
            this.tvDate = (TextView) view.findViewById(R.id.tvDate);
            this.tv_content = (TextView) view.findViewById(R.id.tv_content);
            this.iv_pichtor = (ImageView) view.findViewById(R.id.iv_pichtor);
        }
    }

    public void setIsLast(String str) {
        this.isLast = str;
    }

    private boolean calLines(TextView textView, String str) {
        int windowWidth = (DisplayUtil.getWindowWidth(this.activity) - (DisplayUtil.dip2px(this.activity, 12.0f) * 2)) / DisplayUtil.sp2px(this.activity, 16.0f);
        if (str.length() != 0) {
            int length = str.length();
            int i = length / windowWidth;
            if (i > 2) {
                return true;
            }
            return i == 2 && length % windowWidth > 0;
        }
        return false;
    }
}
