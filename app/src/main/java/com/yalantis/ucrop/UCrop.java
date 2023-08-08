package com.yalantis.ucrop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import com.yalantis.ucrop.model.AspectRatio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: classes.dex */
public class UCrop {
    public static final String EXTRA_ASPECT_RATIO_X = ".AspectRatioX";
    public static final String EXTRA_ASPECT_RATIO_Y = ".AspectRatioY";
    public static final String EXTRA_ERROR = ".Error";
    public static final String EXTRA_INPUT_URI = ".InputUri";
    public static final String EXTRA_MAX_SIZE_X = ".MaxSizeX";
    public static final String EXTRA_MAX_SIZE_Y = ".MaxSizeY";
    public static final String EXTRA_NAV_BAR_COLOR = ".navBarColor";
    public static final String EXTRA_OUTPUT_CROP_ASPECT_RATIO = ".CropAspectRatio";
    public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = ".ImageHeight";
    public static final String EXTRA_OUTPUT_IMAGE_WIDTH = ".ImageWidth";
    public static final String EXTRA_OUTPUT_OFFSET_X = ".OffsetX";
    public static final String EXTRA_OUTPUT_OFFSET_Y = ".OffsetY";
    public static final String EXTRA_OUTPUT_URI = ".OutputUri";
    public static final String EXTRA_WINDOW_EXIT_ANIMATION = ".WindowAnimation";
    public static final int REQUEST_CROP = 69;
    public static final int RESULT_ERROR = 96;
    private Intent mCropIntent = new Intent();
    private Bundle mCropOptionsBundle;

    public static UCrop of(Uri uri, Uri uri2) {
        return new UCrop(uri, uri2);
    }

    private UCrop(Uri uri, Uri uri2) {
        Bundle bundle = new Bundle();
        this.mCropOptionsBundle = bundle;
        bundle.putParcelable(EXTRA_INPUT_URI, uri);
        this.mCropOptionsBundle.putParcelable(EXTRA_OUTPUT_URI, uri2);
    }

    public UCrop withAspectRatio(float f, float f2) {
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, f);
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, f2);
        return this;
    }

    public UCrop useSourceImageAspectRatio() {
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, 0.0f);
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, 0.0f);
        return this;
    }

    public UCrop withMaxResultSize(int i, int i2) {
        this.mCropOptionsBundle.putInt(EXTRA_MAX_SIZE_X, i);
        this.mCropOptionsBundle.putInt(EXTRA_MAX_SIZE_Y, i2);
        return this;
    }

    public UCrop withOptions(Options options) {
        this.mCropOptionsBundle.putAll(options.getOptionBundle());
        return this;
    }

    public void startAnimation(Activity activity, int i) {
        if (i != 0) {
            start(activity, 69, i);
        } else {
            start(activity, 69);
        }
    }

    public void start(Activity activity, int i, int i2) {
        activity.startActivityForResult(getIntent(activity), i);
        activity.overridePendingTransition(i2, R.anim.ucrop_anim_fade_in);
    }

    public void start(Activity activity) {
        start(activity, 69);
    }

    public void start(Activity activity, int i) {
        activity.startActivityForResult(getIntent(activity), i);
    }

    public void start(Context context, Fragment fragment) {
        start(context, fragment, 69);
    }

    public void start(Context context, Fragment fragment, int i) {
        fragment.startActivityForResult(getIntent(context), i);
    }

    public Intent getIntent(Context context) {
        this.mCropIntent.setClass(context, UCropActivity.class);
        this.mCropIntent.putExtras(this.mCropOptionsBundle);
        return this.mCropIntent;
    }

    public static Uri getOutput(Intent intent) {
        return (Uri) intent.getParcelableExtra(EXTRA_OUTPUT_URI);
    }

    public static int getOutputImageWidth(Intent intent) {
        return intent.getIntExtra(EXTRA_OUTPUT_IMAGE_WIDTH, -1);
    }

    public static int getOutputImageHeight(Intent intent) {
        return intent.getIntExtra(EXTRA_OUTPUT_IMAGE_HEIGHT, -1);
    }

    public static Parcelable getOutputCropAspectRatio(Intent intent) {
        return intent.getParcelableExtra(EXTRA_OUTPUT_CROP_ASPECT_RATIO);
    }

    public static Throwable getError(Intent intent) {
        return (Throwable) intent.getSerializableExtra(EXTRA_ERROR);
    }

    /* loaded from: classes.dex */
    public static class Options {
        public static final String EXTRA_ALLOWED_GESTURES = ".AllowedGestures";
        public static final String EXTRA_ASPECT_RATIO_OPTIONS = ".AspectRatioOptions";
        public static final String EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT = ".AspectRatioSelectedByDefault";
        public static final String EXTRA_CIRCLE_DIMMED_LAYER = ".CircleDimmedLayer";
        public static final String EXTRA_COMPRESSION_FORMAT_NAME = ".CompressionFormatName";
        public static final String EXTRA_COMPRESSION_QUALITY = ".CompressionQuality";
        public static final String EXTRA_CROP_FRAME_COLOR = ".CropFrameColor";
        public static final String EXTRA_CROP_FRAME_STROKE_WIDTH = ".CropFrameStrokeWidth";
        public static final String EXTRA_CROP_GRID_COLOR = ".CropGridColor";
        public static final String EXTRA_CROP_GRID_COLUMN_COUNT = ".CropGridColumnCount";
        public static final String EXTRA_CROP_GRID_ROW_COUNT = ".CropGridRowCount";
        public static final String EXTRA_CROP_GRID_STROKE_WIDTH = ".CropGridStrokeWidth";
        public static final String EXTRA_CUT_CROP = ".cuts";
        public static final String EXTRA_DIMMED_LAYER_COLOR = ".DimmedLayerColor";
        public static final String EXTRA_DRAG_CROP_FRAME = ".DragCropFrame";
        public static final String EXTRA_FREE_STATUS_FONT = ".StatusFont";
        public static final String EXTRA_FREE_STYLE_CROP = ".FreeStyleCrop";
        public static final String EXTRA_HIDE_BOTTOM_CONTROLS = ".HideBottomControls";
        public static final String EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = ".ImageToCropBoundsAnimDuration";
        public static final String EXTRA_MAX_BITMAP_SIZE = ".MaxBitmapSize";
        public static final String EXTRA_MAX_SCALE_MULTIPLIER = ".MaxScaleMultiplier";
        public static final String EXTRA_ROTATE = ".rotate";
        public static final String EXTRA_SCALE = ".scale";
        public static final String EXTRA_SHOW_CROP_FRAME = ".ShowCropFrame";
        public static final String EXTRA_SHOW_CROP_GRID = ".ShowCropGrid";
        public static final String EXTRA_STATUS_BAR_COLOR = ".StatusBarColor";
        public static final String EXTRA_TOOL_BAR_COLOR = ".ToolbarColor";
        public static final String EXTRA_UCROP_COLOR_WIDGET_ACTIVE = ".UcropColorWidgetActive";
        public static final String EXTRA_UCROP_LOGO_COLOR = ".UcropLogoColor";
        public static final String EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR = ".UcropRootViewBackgroundColor";
        public static final String EXTRA_UCROP_TITLE_TEXT_TOOLBAR = ".UcropToolbarTitleText";
        public static final String EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE = ".UcropToolbarCancelDrawable";
        public static final String EXTRA_UCROP_WIDGET_COLOR_TOOLBAR = ".UcropToolbarWidgetColor";
        public static final String EXTRA_UCROP_WIDGET_CROP_DRAWABLE = ".UcropToolbarCropDrawable";
        public static final String EXTRA_UCROP_WIDGET_CROP_OPEN_WHITE_STATUSBAR = ".openWhiteStatusBar";
        private final Bundle mOptionBundle = new Bundle();

        public Bundle getOptionBundle() {
            return this.mOptionBundle;
        }

        public void setCompressionFormat(Bitmap.CompressFormat compressFormat) {
            this.mOptionBundle.putString(EXTRA_COMPRESSION_FORMAT_NAME, compressFormat.name());
        }

        public void setCompressionQuality(int i) {
            this.mOptionBundle.putInt(EXTRA_COMPRESSION_QUALITY, i);
        }

        public void setAllowedGestures(int i, int i2, int i3) {
            this.mOptionBundle.putIntArray(EXTRA_ALLOWED_GESTURES, new int[]{i, i2, i3});
        }

        public void setMaxScaleMultiplier(float f) {
            this.mOptionBundle.putFloat(EXTRA_MAX_SCALE_MULTIPLIER, f);
        }

        public void setImageToCropBoundsAnimDuration(int i) {
            this.mOptionBundle.putInt(EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, i);
        }

        public void setMaxBitmapSize(int i) {
            this.mOptionBundle.putInt(EXTRA_MAX_BITMAP_SIZE, i);
        }

        public void setDimmedLayerColor(int i) {
            this.mOptionBundle.putInt(EXTRA_DIMMED_LAYER_COLOR, i);
        }

        public void setCircleDimmedLayer(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_CIRCLE_DIMMED_LAYER, z);
        }

        public void setShowCropFrame(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_SHOW_CROP_FRAME, z);
        }

        public void setCropFrameColor(int i) {
            this.mOptionBundle.putInt(EXTRA_CROP_FRAME_COLOR, i);
        }

        public void setCropFrameStrokeWidth(int i) {
            this.mOptionBundle.putInt(EXTRA_CROP_FRAME_STROKE_WIDTH, i);
        }

        public void setShowCropGrid(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_SHOW_CROP_GRID, z);
        }

        public void setDragFrameEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_DRAG_CROP_FRAME, z);
        }

        public void setScaleEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_SCALE, z);
        }

        public void setRotateEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_ROTATE, z);
        }

        public void setCropGridRowCount(int i) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_ROW_COUNT, i);
        }

        public void setCropGridColumnCount(int i) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_COLUMN_COUNT, i);
        }

        public void setCropGridColor(int i) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_COLOR, i);
        }

        public void setCropGridStrokeWidth(int i) {
            this.mOptionBundle.putInt(EXTRA_CROP_GRID_STROKE_WIDTH, i);
        }

        public void setToolbarColor(int i) {
            this.mOptionBundle.putInt(EXTRA_TOOL_BAR_COLOR, i);
        }

        public void setStatusBarColor(int i) {
            this.mOptionBundle.putInt(EXTRA_STATUS_BAR_COLOR, i);
        }

        public void setActiveWidgetColor(int i) {
            this.mOptionBundle.putInt(EXTRA_UCROP_COLOR_WIDGET_ACTIVE, i);
        }

        public void setToolbarWidgetColor(int i) {
            this.mOptionBundle.putInt(EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, i);
        }

        public void isOpenWhiteStatusBar(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_UCROP_WIDGET_CROP_OPEN_WHITE_STATUSBAR, z);
        }

        public void setToolbarTitle(String str) {
            this.mOptionBundle.putString(EXTRA_UCROP_TITLE_TEXT_TOOLBAR, str);
        }

        public void setToolbarCancelDrawable(int i) {
            this.mOptionBundle.putInt(EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, i);
        }

        public void setToolbarCropDrawable(int i) {
            this.mOptionBundle.putInt(EXTRA_UCROP_WIDGET_CROP_DRAWABLE, i);
        }

        public void setLogoColor(int i) {
            this.mOptionBundle.putInt(EXTRA_UCROP_LOGO_COLOR, i);
        }

        public void setHideBottomControls(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_HIDE_BOTTOM_CONTROLS, z);
        }

        public void setCutListData(ArrayList<String> arrayList) {
            this.mOptionBundle.putStringArrayList(EXTRA_CUT_CROP, arrayList);
        }

        public void setFreeStyleCropEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_FREE_STYLE_CROP, z);
        }

        public void setStatusFont(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_FREE_STATUS_FONT, z);
        }

        public void setAspectRatioOptions(int i, AspectRatio... aspectRatioArr) {
            if (i > aspectRatioArr.length) {
                throw new IllegalArgumentException(String.format(Locale.US, "Index [selectedByDefault = %d] cannot be higher than aspect ratio options count [count = %d].", Integer.valueOf(i), Integer.valueOf(aspectRatioArr.length)));
            }
            this.mOptionBundle.putInt(EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, i);
            this.mOptionBundle.putParcelableArrayList(EXTRA_ASPECT_RATIO_OPTIONS, new ArrayList<>(Arrays.asList(aspectRatioArr)));
        }

        public void setRootViewBackgroundColor(int i) {
            this.mOptionBundle.putInt(EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR, i);
        }

        public void withAspectRatio(float f, float f2) {
            this.mOptionBundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_X, f);
            this.mOptionBundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_Y, f2);
        }

        public void useSourceImageAspectRatio() {
            this.mOptionBundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_X, 0.0f);
            this.mOptionBundle.putFloat(UCrop.EXTRA_ASPECT_RATIO_Y, 0.0f);
        }

        public void withMaxResultSize(int i, int i2) {
            this.mOptionBundle.putInt(UCrop.EXTRA_MAX_SIZE_X, i);
            this.mOptionBundle.putInt(UCrop.EXTRA_MAX_SIZE_Y, i2);
        }

        public void setCropExitAnimation(int i) {
            this.mOptionBundle.putInt(UCrop.EXTRA_WINDOW_EXIT_ANIMATION, i);
        }

        public void setNavBarColor(int i) {
            this.mOptionBundle.putInt(UCrop.EXTRA_NAV_BAR_COLOR, i);
        }
    }
}
