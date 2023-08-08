package com.luck.picture.lib.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.luck.picture.lib.PictureVideoPlayActivity;
import com.luck.picture.lib.R;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.photoview.OnViewTapListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import java.io.File;
import java.util.List;

/* loaded from: classes.dex */
public class PictureSimpleFragmentAdapter extends PagerAdapter {
    private PictureSelectionConfig config;
    private List<LocalMedia> images;
    private Context mContext;
    private OnCallBackActivity onBackPressed;

    /* loaded from: classes.dex */
    public interface OnCallBackActivity {
        void onActivityBackPressed();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public PictureSimpleFragmentAdapter(PictureSelectionConfig pictureSelectionConfig, List<LocalMedia> list, Context context, OnCallBackActivity onCallBackActivity) {
        this.config = pictureSelectionConfig;
        this.images = list;
        this.mContext = context;
        this.onBackPressed = onCallBackActivity;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        List<LocalMedia> list = this.images;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        final String compressPath;
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_image_preview, viewGroup, false);
        PhotoView photoView = (PhotoView) inflate.findViewById(R.id.preview_image);
        SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) inflate.findViewById(R.id.longImg);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_play);
        LocalMedia localMedia = this.images.get(i);
        if (localMedia != null) {
            String mimeType = localMedia.getMimeType();
            int i2 = 8;
            imageView.setVisibility(PictureMimeType.eqVideo(mimeType) ? 0 : 8);
            if (localMedia.isCut() && !localMedia.isCompressed()) {
                compressPath = localMedia.getCutPath();
            } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
                compressPath = localMedia.getCompressPath();
            } else {
                compressPath = localMedia.getPath();
            }
            boolean isGif = PictureMimeType.isGif(mimeType);
            boolean isLongImg = MediaUtils.isLongImg(localMedia);
            photoView.setVisibility((!isLongImg || isGif) ? 0 : 8);
            if (isLongImg && !isGif) {
                i2 = 0;
            }
            subsamplingScaleImageView.setVisibility(i2);
            if (isGif && !localMedia.isCompressed()) {
                PictureSelectionConfig pictureSelectionConfig = this.config;
                if (pictureSelectionConfig != null && pictureSelectionConfig.imageEngine != null) {
                    this.config.imageEngine.loadAsGifImage(inflate.getContext(), compressPath, photoView);
                }
            } else {
                PictureSelectionConfig pictureSelectionConfig2 = this.config;
                if (pictureSelectionConfig2 != null && pictureSelectionConfig2.imageEngine != null) {
                    if (isLongImg) {
                        displayLongPic(SdkVersionUtils.checkedAndroid_Q() ? Uri.parse(compressPath) : Uri.fromFile(new File(compressPath)), subsamplingScaleImageView);
                    } else {
                        this.config.imageEngine.loadImage(inflate.getContext(), compressPath, photoView);
                    }
                }
            }
            photoView.setOnViewTapListener(new OnViewTapListener() { // from class: com.luck.picture.lib.adapter.PictureSimpleFragmentAdapter$$ExternalSyntheticLambda2
                @Override // com.luck.picture.lib.photoview.OnViewTapListener
                public final void onViewTap(View view, float f, float f2) {
                    PictureSimpleFragmentAdapter.this.m49xef283bfc(view, f, f2);
                }
            });
            subsamplingScaleImageView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureSimpleFragmentAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PictureSimpleFragmentAdapter.this.m50xffde08bd(view);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.adapter.PictureSimpleFragmentAdapter$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PictureSimpleFragmentAdapter.this.m51x1093d57e(compressPath, view);
                }
            });
        }
        viewGroup.addView(inflate, 0);
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$instantiateItem$0$com-luck-picture-lib-adapter-PictureSimpleFragmentAdapter  reason: not valid java name */
    public /* synthetic */ void m49xef283bfc(View view, float f, float f2) {
        OnCallBackActivity onCallBackActivity = this.onBackPressed;
        if (onCallBackActivity != null) {
            onCallBackActivity.onActivityBackPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$instantiateItem$1$com-luck-picture-lib-adapter-PictureSimpleFragmentAdapter  reason: not valid java name */
    public /* synthetic */ void m50xffde08bd(View view) {
        OnCallBackActivity onCallBackActivity = this.onBackPressed;
        if (onCallBackActivity != null) {
            onCallBackActivity.onActivityBackPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$instantiateItem$2$com-luck-picture-lib-adapter-PictureSimpleFragmentAdapter  reason: not valid java name */
    public /* synthetic */ void m51x1093d57e(String str, View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("video_path", str);
        intent.putExtras(bundle);
        intent.setClass(this.mContext, PictureVideoPlayActivity.class);
        this.mContext.startActivity(intent);
    }

    private void displayLongPic(Uri uri, SubsamplingScaleImageView subsamplingScaleImageView) {
        subsamplingScaleImageView.setQuickScaleEnabled(true);
        subsamplingScaleImageView.setZoomEnabled(true);
        subsamplingScaleImageView.setPanEnabled(true);
        subsamplingScaleImageView.setDoubleTapZoomDuration(100);
        subsamplingScaleImageView.setMinimumScaleType(2);
        subsamplingScaleImageView.setDoubleTapZoomDpi(2);
        subsamplingScaleImageView.setImage(ImageSource.uri(uri), new ImageViewState(0.0f, new PointF(0.0f, 0.0f), 0));
    }
}
