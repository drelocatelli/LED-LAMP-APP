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
import com.yalantis.ucrop.model.CutInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class UCropMulti {
    public static final String EXTRA_ASPECT_RATIO_X = "BuildConfig.APPLICATION_ID.AspectRatioX";
    public static final String EXTRA_ASPECT_RATIO_Y = "BuildConfig.APPLICATION_ID.AspectRatioY";
    public static final String EXTRA_ERROR = "BuildConfig.APPLICATION_ID.Error";
    public static final String EXTRA_INPUT_URI = "BuildConfig.APPLICATION_ID.InputUri";
    public static final String EXTRA_MAX_SIZE_X = "BuildConfig.APPLICATION_ID.MaxSizeX";
    public static final String EXTRA_MAX_SIZE_Y = "BuildConfig.APPLICATION_ID.MaxSizeY";
    public static final String EXTRA_NAV_BAR_COLOR = "BuildConfig.APPLICATION_ID.navBarColor";
    public static final String EXTRA_OUTPUT_CROP_ASPECT_RATIO = "BuildConfig.APPLICATION_ID.CropAspectRatio";
    public static final String EXTRA_OUTPUT_IMAGE_HEIGHT = "BuildConfig.APPLICATION_ID.ImageHeight";
    public static final String EXTRA_OUTPUT_IMAGE_WIDTH = "BuildConfig.APPLICATION_ID.ImageWidth";
    public static final String EXTRA_OUTPUT_OFFSET_X = "BuildConfig.APPLICATION_ID.OffsetX";
    public static final String EXTRA_OUTPUT_OFFSET_Y = "BuildConfig.APPLICATION_ID.OffsetY";
    public static final String EXTRA_OUTPUT_URI = "BuildConfig.APPLICATION_ID.OutputUri";
    public static final String EXTRA_OUTPUT_URI_LIST = "BuildConfig.APPLICATION_ID.OutputUriList";
    private static final String EXTRA_PREFIX = "BuildConfig.APPLICATION_ID";
    public static final String EXTRA_WINDOW_EXIT_ANIMATION = "BuildConfig.APPLICATION_ID.WindowAnimation";
    public static final int REQUEST_MULTI_CROP = 609;
    public static final int RESULT_ERROR = 96;
    private Intent mCropIntent = new Intent();
    private Bundle mCropOptionsBundle;

    public static UCropMulti of(Uri uri, Uri uri2) {
        return new UCropMulti(uri, uri2);
    }

    private UCropMulti(Uri uri, Uri uri2) {
        Bundle bundle = new Bundle();
        this.mCropOptionsBundle = bundle;
        bundle.putParcelable(EXTRA_INPUT_URI, uri);
        this.mCropOptionsBundle.putParcelable(EXTRA_OUTPUT_URI, uri2);
    }

    public UCropMulti withAspectRatio(float f, float f2) {
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, f);
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, f2);
        return this;
    }

    public UCropMulti useSourceImageAspectRatio() {
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_X, 0.0f);
        this.mCropOptionsBundle.putFloat(EXTRA_ASPECT_RATIO_Y, 0.0f);
        return this;
    }

    public UCropMulti withMaxResultSize(int i, int i2) {
        this.mCropOptionsBundle.putInt(EXTRA_MAX_SIZE_X, i);
        this.mCropOptionsBundle.putInt(EXTRA_MAX_SIZE_Y, i2);
        return this;
    }

    public UCropMulti withOptions(Options options) {
        this.mCropOptionsBundle.putAll(options.getOptionBundle());
        return this;
    }

    public void startAnimation(Activity activity, int i) {
        if (i != 0) {
            start(activity, REQUEST_MULTI_CROP, i);
        } else {
            start(activity, REQUEST_MULTI_CROP);
        }
    }

    public void start(Activity activity, int i, int i2) {
        activity.startActivityForResult(getIntent(activity), i);
        activity.overridePendingTransition(i2, R.anim.ucrop_anim_fade_in);
    }

    public void start(Activity activity) {
        start(activity, REQUEST_MULTI_CROP);
    }

    public void start(Activity activity, int i) {
        activity.startActivityForResult(getIntent(activity), i);
    }

    public void start(Context context, Fragment fragment) {
        start(context, fragment, REQUEST_MULTI_CROP);
    }

    public void start(Context context, Fragment fragment, int i) {
        fragment.startActivityForResult(getIntent(context), i);
    }

    public Intent getIntent(Context context) {
        this.mCropIntent.setClass(context, PictureMultiCuttingActivity.class);
        this.mCropIntent.putExtras(this.mCropOptionsBundle);
        return this.mCropIntent;
    }

    public static List<CutInfo> getOutput(Intent intent) {
        return (List) intent.getSerializableExtra(EXTRA_OUTPUT_URI_LIST);
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
        public static final String EXTRA_ALLOWED_GESTURES = "BuildConfig.APPLICATION_ID.AllowedGestures";
        public static final String EXTRA_ASPECT_RATIO_OPTIONS = "BuildConfig.APPLICATION_ID.AspectRatioOptions";
        public static final String EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT = "BuildConfig.APPLICATION_ID.AspectRatioSelectedByDefault";
        public static final String EXTRA_CIRCLE_DIMMED_LAYER = "BuildConfig.APPLICATION_ID.CircleDimmedLayer";
        public static final String EXTRA_COMPRESSION_FORMAT_NAME = "BuildConfig.APPLICATION_ID.CompressionFormatName";
        public static final String EXTRA_COMPRESSION_QUALITY = "BuildConfig.APPLICATION_ID.CompressionQuality";
        public static final String EXTRA_CROP_FRAME_COLOR = "BuildConfig.APPLICATION_ID.CropFrameColor";
        public static final String EXTRA_CROP_FRAME_STROKE_WIDTH = "BuildConfig.APPLICATION_ID.CropFrameStrokeWidth";
        public static final String EXTRA_CROP_GRID_COLOR = "BuildConfig.APPLICATION_ID.CropGridColor";
        public static final String EXTRA_CROP_GRID_COLUMN_COUNT = "BuildConfig.APPLICATION_ID.CropGridColumnCount";
        public static final String EXTRA_CROP_GRID_ROW_COUNT = "BuildConfig.APPLICATION_ID.CropGridRowCount";
        public static final String EXTRA_CROP_GRID_STROKE_WIDTH = "BuildConfig.APPLICATION_ID.CropGridStrokeWidth";
        public static final String EXTRA_CUT_CROP = "BuildConfig.APPLICATION_ID.cuts";
        public static final String EXTRA_DIMMED_LAYER_COLOR = "BuildConfig.APPLICATION_ID.DimmedLayerColor";
        public static final String EXTRA_DRAG_CROP_FRAME = "BuildConfig.APPLICATION_ID.DragCropFrame";
        public static final String EXTRA_FREE_STATUS_FONT = "BuildConfig.APPLICATION_ID.StatusFont";
        public static final String EXTRA_FREE_STYLE_CROP = "BuildConfig.APPLICATION_ID.FreeStyleCrop";
        public static final String EXTRA_HIDE_BOTTOM_CONTROLS = "BuildConfig.APPLICATION_ID.HideBottomControls";
        public static final String EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = "BuildConfig.APPLICATION_ID.ImageToCropBoundsAnimDuration";
        public static final String EXTRA_MAX_BITMAP_SIZE = "BuildConfig.APPLICATION_ID.MaxBitmapSize";
        public static final String EXTRA_MAX_SCALE_MULTIPLIER = "BuildConfig.APPLICATION_ID.MaxScaleMultiplier";
        public static final String EXTRA_ROTATE = "BuildConfig.APPLICATION_ID.rotate";
        public static final String EXTRA_SCALE = "BuildConfig.APPLICATION_ID.scale";
        public static final String EXTRA_SHOW_CROP_FRAME = "BuildConfig.APPLICATION_ID.ShowCropFrame";
        public static final String EXTRA_SHOW_CROP_GRID = "BuildConfig.APPLICATION_ID.ShowCropGrid";
        public static final String EXTRA_STATUS_BAR_COLOR = "BuildConfig.APPLICATION_ID.StatusBarColor";
        public static final String EXTRA_TOOL_BAR_COLOR = "BuildConfig.APPLICATION_ID.ToolbarColor";
        public static final String EXTRA_UCROP_COLOR_WIDGET_ACTIVE = "BuildConfig.APPLICATION_ID.UcropColorWidgetActive";
        public static final String EXTRA_UCROP_LOGO_COLOR = "BuildConfig.APPLICATION_ID.UcropLogoColor";
        public static final String EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR = "BuildConfig.APPLICATION_ID.UcropRootViewBackgroundColor";
        public static final String EXTRA_UCROP_TITLE_TEXT_TOOLBAR = "BuildConfig.APPLICATION_ID.UcropToolbarTitleText";
        public static final String EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE = "BuildConfig.APPLICATION_ID.UcropToolbarCancelDrawable";
        public static final String EXTRA_UCROP_WIDGET_COLOR_TOOLBAR = "BuildConfig.APPLICATION_ID.UcropToolbarWidgetColor";
        public static final String EXTRA_UCROP_WIDGET_CROP_DRAWABLE = "BuildConfig.APPLICATION_ID.UcropToolbarCropDrawable";
        public static final String EXTRA_UCROP_WIDGET_CROP_OPEN_WHITE_STATUSBAR = "BuildConfig.APPLICATION_ID.openWhiteStatusBar";
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

        public void setScaleEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_SCALE, z);
        }

        public void setRotateEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_ROTATE, z);
        }

        public void setDragFrameEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_DRAG_CROP_FRAME, z);
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

        public void setCutListData(ArrayList<CutInfo> arrayList) {
            this.mOptionBundle.putSerializable(EXTRA_CUT_CROP, arrayList);
        }

        public void setFreeStyleCropEnabled(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_FREE_STYLE_CROP, z);
        }

        @Deprecated
        public void setStatusFont(boolean z) {
            this.mOptionBundle.putBoolean(EXTRA_FREE_STATUS_FONT, z);
        }

        public void setCropExitAnimation(int i) {
            this.mOptionBundle.putInt(UCropMulti.EXTRA_WINDOW_EXIT_ANIMATION, i);
        }

        public void setNavBarColor(int i) {
            this.mOptionBundle.putInt(UCropMulti.EXTRA_NAV_BAR_COLOR, i);
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
            this.mOptionBundle.putFloat(UCropMulti.EXTRA_ASPECT_RATIO_X, f);
            this.mOptionBundle.putFloat(UCropMulti.EXTRA_ASPECT_RATIO_Y, f2);
        }

        public void useSourceImageAspectRatio() {
            this.mOptionBundle.putFloat(UCropMulti.EXTRA_ASPECT_RATIO_X, 0.0f);
            this.mOptionBundle.putFloat(UCropMulti.EXTRA_ASPECT_RATIO_Y, 0.0f);
        }

        public void withMaxResultSize(int i, int i2) {
            this.mOptionBundle.putInt(UCropMulti.EXTRA_MAX_SIZE_X, i);
            this.mOptionBundle.putInt(UCropMulti.EXTRA_MAX_SIZE_Y, i2);
        }
    }
}
