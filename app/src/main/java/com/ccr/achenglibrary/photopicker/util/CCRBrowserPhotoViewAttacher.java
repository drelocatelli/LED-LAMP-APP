package com.ccr.achenglibrary.photopicker.util;

import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.ccr.achenglibrary.photoview.PhotoViewAttacher;

/* loaded from: classes.dex */
public class CCRBrowserPhotoViewAttacher extends PhotoViewAttacher {
    private boolean isSetTopCrop;

    public CCRBrowserPhotoViewAttacher(ImageView imageView) {
        super(imageView);
        this.isSetTopCrop = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ccr.achenglibrary.photoview.PhotoViewAttacher
    public void updateBaseMatrix(Drawable drawable) {
        if (this.isSetTopCrop) {
            setTopCrop(drawable);
        } else {
            super.updateBaseMatrix(drawable);
        }
    }

    public void setIsSetTopCrop(boolean z) {
        this.isSetTopCrop = z;
    }

    public void setUpdateBaseMatrix() {
        ImageView imageView = getImageView();
        if (imageView == null) {
            return;
        }
        updateBaseMatrix(imageView.getDrawable());
    }

    private void setTopCrop(Drawable drawable) {
        ImageView imageView = getImageView();
        if (imageView == null || drawable == null) {
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Matrix matrix = new Matrix();
        float max = Math.max(getImageViewWidth(imageView) / intrinsicWidth, getImageViewHeight(imageView) / intrinsicHeight);
        matrix.postScale(max, max);
        matrix.postTranslate(0.0f, 0.0f);
        updateBaseMatrix(matrix);
    }
}
