package com.luck.picture.lib.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.luck.picture.lib.R;
import com.luck.picture.lib.anim.OptAnimationLoader;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.AnimUtils;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.luck.picture.lib.tools.VoiceUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class PictureImageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Animation animation;
    private int chooseMode;
    private PictureSelectionConfig config;
    private Context context;
    private boolean enablePreview;
    private boolean enablePreviewAudio;
    private boolean enablePreviewVideo;
    private boolean enableVoice;
    private OnPhotoSelectChangedListener imageSelectChangedListener;
    private boolean isGo;
    private boolean isSingleDirectReturn;
    private boolean is_checked_num;
    private int maxSelectNum;
    private int selectMode;
    private boolean showCamera;
    private boolean zoomAnim;
    private List<LocalMedia> images = new ArrayList();
    private List<LocalMedia> selectImages = new ArrayList();

    /* loaded from: classes.dex */
    public interface OnPhotoSelectChangedListener {
        void onChange(List<LocalMedia> list);

        void onPictureClick(LocalMedia localMedia, int i);

        void onTakePhoto();
    }

    public PictureImageGridAdapter(Context context, PictureSelectionConfig pictureSelectionConfig) {
        this.context = context;
        this.config = pictureSelectionConfig;
        this.selectMode = pictureSelectionConfig.selectionMode;
        this.showCamera = pictureSelectionConfig.isCamera;
        this.maxSelectNum = pictureSelectionConfig.maxSelectNum;
        this.enablePreview = pictureSelectionConfig.enablePreview;
        this.enablePreviewVideo = pictureSelectionConfig.enPreviewVideo;
        this.enablePreviewAudio = pictureSelectionConfig.enablePreviewAudio;
        this.is_checked_num = pictureSelectionConfig.checkNumMode;
        this.enableVoice = pictureSelectionConfig.openClickSound;
        this.chooseMode = pictureSelectionConfig.chooseMode;
        this.zoomAnim = pictureSelectionConfig.zoomAnim;
        this.isSingleDirectReturn = pictureSelectionConfig.isSingleDirectReturn;
        this.animation = OptAnimationLoader.loadAnimation(context, R.anim.picture_anim_modal_in);
    }

    public void setShowCamera(boolean z) {
        this.showCamera = z;
    }

    public void bindImagesData(List<LocalMedia> list) {
        this.images = list;
        notifyDataSetChanged();
    }

    public void bindSelectImages(List<LocalMedia> list) {
        ArrayList arrayList = new ArrayList();
        for (LocalMedia localMedia : list) {
            arrayList.add(localMedia);
        }
        this.selectImages = arrayList;
        subSelectPosition();
        OnPhotoSelectChangedListener onPhotoSelectChangedListener = this.imageSelectChangedListener;
        if (onPhotoSelectChangedListener != null) {
            onPhotoSelectChangedListener.onChange(this.selectImages);
        }
    }

    public List<LocalMedia> getSelectedImages() {
        if (this.selectImages == null) {
            this.selectImages = new ArrayList();
        }
        return this.selectImages;
    }

    public List<LocalMedia> getImages() {
        if (this.images == null) {
            this.images = new ArrayList();
        }
        return this.images;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return (this.showCamera && i == 0) ? 1 : 2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new HeaderViewHolder(LayoutInflater.from(this.context).inflate(R.layout.picture_item_camera, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.picture_image_grid_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == 1) {
            ((HeaderViewHolder) viewHolder).headerView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureImageGridAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PictureImageGridAdapter.this.m46x23ddad75(view);
                }
            });
            return;
        }
        final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        final LocalMedia localMedia = this.images.get(this.showCamera ? i - 1 : i);
        localMedia.position = viewHolder2.getAdapterPosition();
        final String path = localMedia.getPath();
        final String mimeType = localMedia.getMimeType();
        if (this.is_checked_num) {
            notifyCheckChanged(viewHolder2, localMedia);
        }
        selectImage(viewHolder2, isSelected(localMedia), false);
        boolean isGif = PictureMimeType.isGif(mimeType);
        viewHolder2.tvCheck.setVisibility(this.isSingleDirectReturn ? 8 : 0);
        viewHolder2.tvIsGif.setVisibility(isGif ? 0 : 8);
        if (this.chooseMode == PictureMimeType.ofAudio()) {
            viewHolder2.tvDuration.setVisibility(0);
            if (Build.VERSION.SDK_INT >= 17) {
                viewHolder2.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.picture_icon_audio, 0, 0, 0);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 17) {
                viewHolder2.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.picture_icon_video, 0, 0, 0);
            }
            viewHolder2.tvDuration.setVisibility(PictureMimeType.eqVideo(mimeType) ? 0 : 8);
        }
        if (PictureMimeType.eqImage(localMedia.getMimeType())) {
            viewHolder2.tvLongChart.setVisibility(MediaUtils.isLongImg(localMedia) ? 0 : 8);
        } else {
            viewHolder2.tvLongChart.setVisibility(8);
        }
        viewHolder2.tvDuration.setText(DateUtils.formatDurationTime(localMedia.getDuration()));
        if (this.chooseMode == PictureMimeType.ofAudio()) {
            viewHolder2.ivPicture.setImageResource(R.drawable.picture_audio_placeholder);
        } else {
            PictureSelectionConfig pictureSelectionConfig = this.config;
            if (pictureSelectionConfig != null && pictureSelectionConfig.imageEngine != null) {
                this.config.imageEngine.loadAsBitmapGridImage(this.context, path, viewHolder2.ivPicture, R.drawable.picture_image_placeholder);
            }
        }
        if (this.enablePreview || this.enablePreviewVideo || this.enablePreviewAudio) {
            viewHolder2.btnCheck.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureImageGridAdapter$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PictureImageGridAdapter.this.m47x3df92c14(path, mimeType, viewHolder2, localMedia, view);
                }
            });
        }
        viewHolder2.contentView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureImageGridAdapter$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PictureImageGridAdapter.this.m48x5814aab3(path, mimeType, i, localMedia, viewHolder2, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$0$com-luck-picture-lib-adapter-PictureImageGridAdapter  reason: not valid java name */
    public /* synthetic */ void m46x23ddad75(View view) {
        OnPhotoSelectChangedListener onPhotoSelectChangedListener = this.imageSelectChangedListener;
        if (onPhotoSelectChangedListener != null) {
            onPhotoSelectChangedListener.onTakePhoto();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$1$com-luck-picture-lib-adapter-PictureImageGridAdapter  reason: not valid java name */
    public /* synthetic */ void m47x3df92c14(String str, String str2, ViewHolder viewHolder, LocalMedia localMedia, View view) {
        if (SdkVersionUtils.checkedAndroid_Q()) {
            str = PictureFileUtils.getPath(this.context, Uri.parse(str));
        }
        if (!new File(str).exists()) {
            Context context = this.context;
            ToastUtils.s(context, PictureMimeType.s(context, str2));
            return;
        }
        changeCheckboxState(viewHolder, localMedia);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onBindViewHolder$2$com-luck-picture-lib-adapter-PictureImageGridAdapter  reason: not valid java name */
    public /* synthetic */ void m48x5814aab3(String str, String str2, int i, LocalMedia localMedia, ViewHolder viewHolder, View view) {
        if (SdkVersionUtils.checkedAndroid_Q()) {
            str = PictureFileUtils.getPath(this.context, Uri.parse(str));
        }
        if (!new File(str).exists()) {
            Context context = this.context;
            ToastUtils.s(context, PictureMimeType.s(context, str2));
            return;
        }
        if (this.showCamera) {
            i--;
        }
        if (i == -1) {
            return;
        }
        boolean z = true;
        if ((!PictureMimeType.eqImage(str2) || !this.enablePreview) && ((!PictureMimeType.eqVideo(str2) || (!this.enablePreviewVideo && this.selectMode != 1)) && (!PictureMimeType.eqAudio(str2) || (!this.enablePreviewAudio && this.selectMode != 1)))) {
            z = false;
        }
        if (z) {
            this.imageSelectChangedListener.onPictureClick(localMedia, i);
        } else {
            changeCheckboxState(viewHolder, localMedia);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.showCamera ? this.images.size() + 1 : this.images.size();
    }

    /* loaded from: classes.dex */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        View headerView;
        TextView tvCamera;

        public HeaderViewHolder(View view) {
            super(view);
            this.headerView = view;
            this.tvCamera = (TextView) view.findViewById(R.id.tvCamera);
            this.tvCamera.setText(PictureImageGridAdapter.this.chooseMode == PictureMimeType.ofAudio() ? PictureImageGridAdapter.this.context.getString(R.string.picture_tape) : PictureImageGridAdapter.this.context.getString(R.string.picture_take_picture));
        }
    }

    /* loaded from: classes.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        View btnCheck;
        View contentView;
        ImageView ivPicture;
        TextView tvCheck;
        TextView tvDuration;
        TextView tvIsGif;
        TextView tvLongChart;

        public ViewHolder(View view) {
            super(view);
            this.contentView = view;
            this.ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
            this.tvCheck = (TextView) view.findViewById(R.id.tvCheck);
            this.btnCheck = view.findViewById(R.id.btnCheck);
            this.tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            this.tvIsGif = (TextView) view.findViewById(R.id.tv_isGif);
            this.tvLongChart = (TextView) view.findViewById(R.id.tv_long_chart);
            if (PictureImageGridAdapter.this.config.style == null || PictureImageGridAdapter.this.config.style.pictureCheckedStyle == 0) {
                return;
            }
            this.tvCheck.setBackgroundResource(PictureImageGridAdapter.this.config.style.pictureCheckedStyle);
        }
    }

    public boolean isSelected(LocalMedia localMedia) {
        for (LocalMedia localMedia2 : this.selectImages) {
            if (localMedia2.getPath().equals(localMedia.getPath())) {
                return true;
            }
        }
        return false;
    }

    private void notifyCheckChanged(ViewHolder viewHolder, LocalMedia localMedia) {
        viewHolder.tvCheck.setText("");
        for (LocalMedia localMedia2 : this.selectImages) {
            if (localMedia2.getPath().equals(localMedia.getPath())) {
                localMedia.setNum(localMedia2.getNum());
                localMedia2.setPosition(localMedia.getPosition());
                viewHolder.tvCheck.setText(String.valueOf(localMedia.getNum()));
            }
        }
    }

    private void changeCheckboxState(ViewHolder viewHolder, LocalMedia localMedia) {
        boolean isSelected = viewHolder.tvCheck.isSelected();
        String mimeType = this.selectImages.size() > 0 ? this.selectImages.get(0).getMimeType() : "";
        if (!TextUtils.isEmpty(mimeType) && !PictureMimeType.isMimeTypeSame(mimeType, localMedia.getMimeType())) {
            Context context = this.context;
            ToastUtils.s(context, context.getString(R.string.picture_rule));
        } else if (this.selectImages.size() >= this.maxSelectNum && !isSelected) {
            Context context2 = this.context;
            ToastUtils.s(context2, StringUtils.getToastMsg(context2, mimeType, this.config.maxSelectNum));
        } else {
            if (isSelected) {
                Iterator<LocalMedia> it = this.selectImages.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    LocalMedia next = it.next();
                    if (next.getPath().equals(localMedia.getPath())) {
                        this.selectImages.remove(next);
                        subSelectPosition();
                        if (this.zoomAnim) {
                            AnimUtils.disZoom(viewHolder.ivPicture);
                        }
                    }
                }
            } else {
                if (this.selectMode == 1) {
                    singleRadioMediaImage();
                }
                this.selectImages.add(localMedia);
                localMedia.setNum(this.selectImages.size());
                VoiceUtils.playVoice(this.context, this.enableVoice);
                if (this.zoomAnim) {
                    AnimUtils.zoom(viewHolder.ivPicture);
                }
            }
            notifyItemChanged(viewHolder.getAdapterPosition());
            selectImage(viewHolder, !isSelected, true);
            OnPhotoSelectChangedListener onPhotoSelectChangedListener = this.imageSelectChangedListener;
            if (onPhotoSelectChangedListener != null) {
                onPhotoSelectChangedListener.onChange(this.selectImages);
            }
        }
    }

    private void singleRadioMediaImage() {
        List<LocalMedia> list = this.selectImages;
        if (list == null || list.size() <= 0) {
            return;
        }
        this.isGo = true;
        int i = 0;
        LocalMedia localMedia = this.selectImages.get(0);
        if (this.config.isCamera) {
            i = localMedia.position;
        } else if (this.isGo) {
            i = localMedia.position;
        } else if (localMedia.position > 0) {
            i = localMedia.position - 1;
        }
        notifyItemChanged(i);
        this.selectImages.clear();
    }

    private void subSelectPosition() {
        if (this.is_checked_num) {
            int size = this.selectImages.size();
            int i = 0;
            while (i < size) {
                LocalMedia localMedia = this.selectImages.get(i);
                i++;
                localMedia.setNum(i);
                notifyItemChanged(localMedia.position);
            }
        }
    }

    public void selectImage(ViewHolder viewHolder, boolean z, boolean z2) {
        viewHolder.tvCheck.setSelected(z);
        if (z) {
            if (z2 && this.animation != null) {
                viewHolder.tvCheck.startAnimation(this.animation);
            }
            viewHolder.ivPicture.setColorFilter(ContextCompat.getColor(this.context, R.color.picture_color_80), PorterDuff.Mode.SRC_ATOP);
            return;
        }
        viewHolder.ivPicture.setColorFilter(ContextCompat.getColor(this.context, R.color.picture_color_20), PorterDuff.Mode.SRC_ATOP);
    }

    public void setOnPhotoSelectChangedListener(OnPhotoSelectChangedListener onPhotoSelectChangedListener) {
        this.imageSelectChangedListener = onPhotoSelectChangedListener;
    }
}
