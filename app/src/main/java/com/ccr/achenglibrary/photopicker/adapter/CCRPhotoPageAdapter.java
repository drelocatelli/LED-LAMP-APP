package com.ccr.achenglibrary.photopicker.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImage;
import com.ccr.achenglibrary.photopicker.util.CCRBrowserPhotoViewAttacher;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import com.ccr.achenglibrary.photopicker.widget.CCRImageView;
import com.ccr.achenglibrary.photoview.PhotoViewAttacher;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CCRPhotoPageAdapter extends PagerAdapter {
    private Activity mActivity;
    LongClickListener mLongClickListener;
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener;
    private ArrayList<String> mPreviewImages;

    /* loaded from: classes.dex */
    public interface LongClickListener {
        boolean onLongClick(View view);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public CCRPhotoPageAdapter(Activity activity, PhotoViewAttacher.OnViewTapListener onViewTapListener, ArrayList<String> arrayList, LongClickListener longClickListener) {
        this.mOnViewTapListener = onViewTapListener;
        this.mPreviewImages = arrayList;
        this.mActivity = activity;
        this.mLongClickListener = longClickListener;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        ArrayList<String> arrayList = this.mPreviewImages;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public View instantiateItem(ViewGroup viewGroup, int i) {
        final CCRImageView cCRImageView = new CCRImageView(viewGroup.getContext());
        viewGroup.addView(cCRImageView, -1, -1);
        final CCRBrowserPhotoViewAttacher cCRBrowserPhotoViewAttacher = new CCRBrowserPhotoViewAttacher(cCRImageView);
        cCRBrowserPhotoViewAttacher.setOnViewTapListener(this.mOnViewTapListener);
        cCRImageView.setDelegate(new CCRImageView.Delegate() { // from class: com.ccr.achenglibrary.photopicker.adapter.CCRPhotoPageAdapter.1
            @Override // com.ccr.achenglibrary.photopicker.widget.CCRImageView.Delegate
            public void onDrawableChanged(Drawable drawable) {
                if (drawable != null && drawable.getIntrinsicHeight() > drawable.getIntrinsicWidth() && drawable.getIntrinsicHeight() > CCRPhotoPickerUtil.getScreenHeight()) {
                    cCRBrowserPhotoViewAttacher.setIsSetTopCrop(true);
                    cCRBrowserPhotoViewAttacher.setUpdateBaseMatrix();
                    return;
                }
                cCRBrowserPhotoViewAttacher.update();
            }
        });
        cCRBrowserPhotoViewAttacher.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.ccr.achenglibrary.photopicker.adapter.CCRPhotoPageAdapter.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                if (CCRPhotoPageAdapter.this.mLongClickListener != null) {
                    CCRPhotoPageAdapter.this.mLongClickListener.onLongClick(cCRImageView);
                    return true;
                }
                return true;
            }
        });
        CCRImage.display(cCRImageView, R.mipmap.bga_pp_ic_holder_dark, this.mPreviewImages.get(i), CCRPhotoPickerUtil.getScreenWidth(), CCRPhotoPickerUtil.getScreenHeight());
        return cCRImageView;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    public String getItem(int i) {
        ArrayList<String> arrayList = this.mPreviewImages;
        return arrayList == null ? "" : arrayList.get(i);
    }
}
