package com.yalantis.ucrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.yalantis.ucrop.callback.BitmapLoadCallback;
import com.yalantis.ucrop.model.CutInfo;
import com.yalantis.ucrop.model.ExifInfo;
import com.yalantis.ucrop.util.BitmapLoadUtils;
import com.yalantis.ucrop.util.FileUtils;
import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
public class PicturePhotoGalleryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private boolean isAndroidQ;
    private List<CutInfo> list;
    private OnItemClickListener listener;
    private LayoutInflater mInflater;
    private final int maxImageWidth = 200;
    private final int maxImageHeight = 220;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(int i, View view);
    }

    public PicturePhotoGalleryAdapter(Context context, List<CutInfo> list) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.isAndroidQ = Build.VERSION.SDK_INT >= 29;
    }

    public void setData(List<CutInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(this.mInflater.inflate(R.layout.ucrop_picture_gf_adapter_edit_list, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        CutInfo cutInfo = this.list.get(i);
        String path = cutInfo != null ? cutInfo.getPath() : "";
        if (cutInfo.isCut()) {
            viewHolder.iv_dot.setVisibility(0);
            viewHolder.iv_dot.setImageResource(R.drawable.ucrop_oval_true);
        } else {
            viewHolder.iv_dot.setVisibility(8);
        }
        Uri parse = this.isAndroidQ ? Uri.parse(path) : Uri.fromFile(new File(path));
        viewHolder.tvGif.setVisibility(FileUtils.isGif(cutInfo.getMimeType()) ? 0 : 8);
        BitmapLoadUtils.decodeBitmapInBackground(this.context, parse, null, 200, 220, new BitmapLoadCallback() { // from class: com.yalantis.ucrop.PicturePhotoGalleryAdapter.1
            @Override // com.yalantis.ucrop.callback.BitmapLoadCallback
            public void onBitmapLoaded(Bitmap bitmap, ExifInfo exifInfo, Uri uri, Uri uri2) {
                if (viewHolder.mIvPhoto != null) {
                    viewHolder.mIvPhoto.setImageBitmap(bitmap);
                }
            }

            @Override // com.yalantis.ucrop.callback.BitmapLoadCallback
            public void onFailure(Exception exc) {
                if (viewHolder.mIvPhoto != null) {
                    viewHolder.mIvPhoto.setImageResource(R.color.ucrop_color_ba3);
                }
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.yalantis.ucrop.PicturePhotoGalleryAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PicturePhotoGalleryAdapter.this.m72xf87943bf(viewHolder, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$com-yalantis-ucrop-PicturePhotoGalleryAdapter  reason: not valid java name */
    public /* synthetic */ void m72xf87943bf(ViewHolder viewHolder, View view) {
        OnItemClickListener onItemClickListener = this.listener;
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(viewHolder.getAdapterPosition(), view);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<CutInfo> list = this.list;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    /* loaded from: classes.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_dot;
        ImageView mIvPhoto;
        TextView tvGif;

        public ViewHolder(View view) {
            super(view);
            this.mIvPhoto = (ImageView) view.findViewById(R.id.iv_photo);
            this.iv_dot = (ImageView) view.findViewById(R.id.iv_dot);
            this.tvGif = (TextView) view.findViewById(R.id.tv_gif);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
}
