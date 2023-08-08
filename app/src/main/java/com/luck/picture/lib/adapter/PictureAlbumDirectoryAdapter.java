package com.luck.picture.lib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PictureAlbumDirectoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private int chooseMode;
    private PictureSelectionConfig config;
    private List<LocalMediaFolder> folders = new ArrayList();
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick(boolean z, String str, List<LocalMedia> list);
    }

    public PictureAlbumDirectoryAdapter(Context context, PictureSelectionConfig pictureSelectionConfig) {
        this.mContext = context;
        this.config = pictureSelectionConfig;
        this.chooseMode = pictureSelectionConfig.chooseMode;
    }

    public void bindFolderData(List<LocalMediaFolder> list) {
        this.folders = list;
        notifyDataSetChanged();
    }

    public void setChooseMode(int i) {
        this.chooseMode = i;
    }

    public List<LocalMediaFolder> getFolderData() {
        if (this.folders == null) {
            this.folders = new ArrayList();
        }
        return this.folders;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.picture_album_folder_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final LocalMediaFolder localMediaFolder = this.folders.get(i);
        String name = localMediaFolder.getName();
        int imageNum = localMediaFolder.getImageNum();
        String firstImagePath = localMediaFolder.getFirstImagePath();
        boolean isChecked = localMediaFolder.isChecked();
        viewHolder.tvSign.setVisibility(localMediaFolder.getCheckedNum() > 0 ? 0 : 4);
        viewHolder.itemView.setSelected(isChecked);
        if (this.chooseMode == PictureMimeType.ofAudio()) {
            viewHolder.ivFirstImage.setImageResource(R.drawable.picture_audio_placeholder);
        } else {
            PictureSelectionConfig pictureSelectionConfig = this.config;
            if (pictureSelectionConfig != null && pictureSelectionConfig.imageEngine != null) {
                this.config.imageEngine.loadFolderAsBitmapImage(viewHolder.itemView.getContext(), firstImagePath, viewHolder.ivFirstImage, R.drawable.picture_icon_placeholder);
            }
        }
        Context context = viewHolder.itemView.getContext();
        if (localMediaFolder.getOfAllType() != -1) {
            if (localMediaFolder.getOfAllType() == PictureMimeType.ofAudio()) {
                name = context.getString(R.string.picture_all_audio);
            } else {
                name = context.getString(R.string.picture_camera_roll);
            }
        }
        viewHolder.tvFolderName.setText(context.getString(R.string.picture_camera_roll_num, name, Integer.valueOf(imageNum)));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PictureAlbumDirectoryAdapter.this.m45xf1d7dc6a(localMediaFolder, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$com-luck-picture-lib-adapter-PictureAlbumDirectoryAdapter  reason: not valid java name */
    public /* synthetic */ void m45xf1d7dc6a(LocalMediaFolder localMediaFolder, View view) {
        if (this.onItemClickListener != null) {
            for (LocalMediaFolder localMediaFolder2 : this.folders) {
                localMediaFolder2.setChecked(false);
            }
            localMediaFolder.setChecked(true);
            notifyDataSetChanged();
            this.onItemClickListener.onItemClick(localMediaFolder.isCameraFolder(), localMediaFolder.getName(), localMediaFolder.getImages());
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.folders.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFirstImage;
        TextView tvFolderName;
        TextView tvSign;

        public ViewHolder(View view) {
            super(view);
            this.ivFirstImage = (ImageView) view.findViewById(R.id.first_image);
            this.tvFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
            this.tvSign = (TextView) view.findViewById(R.id.tv_sign);
            if (PictureAlbumDirectoryAdapter.this.config.style == null || PictureAlbumDirectoryAdapter.this.config.style.pictureFolderCheckedDotStyle == 0) {
                return;
            }
            this.tvSign.setBackgroundResource(PictureAlbumDirectoryAdapter.this.config.style.pictureFolderCheckedDotStyle);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
