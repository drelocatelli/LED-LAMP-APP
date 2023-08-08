package com.common.pictureselector.adapter;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ledlamp.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class GridImageAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_PICTURE = 2;
    private LayoutInflater mInflater;
    protected OnItemClickListener mItemClickListener;
    private onAddPicClickListener mOnAddPicClickListener;
    private List<LocalMedia> list = new ArrayList();
    private int selectMax = 9;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(int i, View view);
    }

    /* loaded from: classes.dex */
    public interface onAddPicClickListener {
        void onAddPicClick();
    }

    public GridImageAdapter(Context context, onAddPicClickListener onaddpicclicklistener) {
        this.mInflater = LayoutInflater.from(context);
        this.mOnAddPicClickListener = onaddpicclicklistener;
    }

    public void setSelectMax(int i) {
        this.selectMax = i;
    }

    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llDel;
        ImageView mImg;
        TextView tvDuration;

        public ViewHolder(View view) {
            super(view);
            this.mImg = (ImageView) view.findViewById(R.id.fiv);
            this.llDel = (LinearLayout) view.findViewById(R.id.ll_del);
            this.tvDuration = (TextView) view.findViewById(R.id.tv_duration);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (this.list.size() < this.selectMax) {
            return this.list.size() + 1;
        }
        return this.list.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return isShowAddItem(i) ? 1 : 2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.gv_filter_image, viewGroup, false));
    }

    private boolean isShowAddItem(int i) {
        return i == (this.list.size() == 0 ? 0 : this.list.size());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String compressPath;
        if (getItemViewType(i) == 1) {
            viewHolder.mImg.setImageResource(R.drawable.icon_plus);
            viewHolder.mImg.setOnClickListener(new View.OnClickListener() { // from class: com.common.pictureselector.adapter.GridImageAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GridImageAdapter.this.m9xdc1f0d30(view);
                }
            });
            viewHolder.llDel.setVisibility(4);
            return;
        }
        viewHolder.llDel.setVisibility(0);
        viewHolder.llDel.setOnClickListener(new View.OnClickListener() { // from class: com.common.pictureselector.adapter.GridImageAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GridImageAdapter.this.m10x6959beb1(viewHolder, view);
            }
        });
        LocalMedia localMedia = this.list.get(i);
        int chooseModel = localMedia.getChooseModel();
        if (localMedia.isCut() && !localMedia.isCompressed()) {
            compressPath = localMedia.getCutPath();
        } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
            compressPath = localMedia.getCompressPath();
        } else {
            compressPath = SdkVersionUtils.checkedAndroid_Q() ? localMedia.getAndroidQToPath() : localMedia.getPath();
        }
        if (localMedia.isCompressed()) {
            Log.i("compress image result:", (new File(localMedia.getCompressPath()).length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) + "k");
            Log.i("压缩地址::", localMedia.getCompressPath());
        }
        Log.i("原图地址::", localMedia.getPath());
        if (localMedia.isCut()) {
            Log.i("裁剪地址::", localMedia.getCutPath());
        }
        long duration = localMedia.getDuration();
        viewHolder.tvDuration.setVisibility(PictureMimeType.eqVideo(localMedia.getMimeType()) ? 0 : 8);
        if (chooseModel == PictureMimeType.ofAudio()) {
            viewHolder.tvDuration.setVisibility(0);
            viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.picture_icon_audio, 0, 0, 0);
        } else {
            viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.picture_icon_video, 0, 0, 0);
        }
        viewHolder.tvDuration.setText(DateUtils.formatDurationTime(duration));
        if (chooseModel == PictureMimeType.ofAudio()) {
            viewHolder.mImg.setImageResource(R.drawable.picture_audio_placeholder);
        } else {
            Glide.with(viewHolder.itemView.getContext()).load(compressPath).apply((BaseRequestOptions<?>) new RequestOptions().centerCrop().placeholder(R.color.app_color_f6).diskCacheStrategy(DiskCacheStrategy.ALL)).into(viewHolder.mImg);
        }
        if (this.mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.common.pictureselector.adapter.GridImageAdapter$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GridImageAdapter.this.m11xf6947032(viewHolder, view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$com-common-pictureselector-adapter-GridImageAdapter  reason: not valid java name */
    public /* synthetic */ void m9xdc1f0d30(View view) {
        this.mOnAddPicClickListener.onAddPicClick();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$1$com-common-pictureselector-adapter-GridImageAdapter  reason: not valid java name */
    public /* synthetic */ void m10x6959beb1(ViewHolder viewHolder, View view) {
        int adapterPosition = viewHolder.getAdapterPosition();
        if (adapterPosition != -1) {
            this.list.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
            notifyItemRangeChanged(adapterPosition, this.list.size());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$2$com-common-pictureselector-adapter-GridImageAdapter  reason: not valid java name */
    public /* synthetic */ void m11xf6947032(ViewHolder viewHolder, View view) {
        this.mItemClickListener.onItemClick(viewHolder.getAdapterPosition(), view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }
}
