package com.yalantis.ucrop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yalantis.ucrop.PicturePhotoGalleryAdapter;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropMulti;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.immersion.CropImmersiveManage;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.model.CutInfo;
import com.yalantis.ucrop.util.FileUtils;
import com.yalantis.ucrop.util.SelectedStateListDrawable;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.AspectRatioTextView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;
import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class PictureMultiCuttingActivity extends AppCompatActivity {
    public static final int ALL = 3;
    public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
    public static final int DEFAULT_COMPRESS_QUALITY = 90;
    public static final int NONE = 0;
    public static final int ROTATE = 2;
    private static final int ROTATE_WIDGET_SENSITIVITY_COEFFICIENT = 42;
    public static final int SCALE = 1;
    private static final int SCALE_WIDGET_SENSITIVITY_COEFFICIENT = 15000;
    private static final int TABS_COUNT = 3;
    private static final String TAG = "UCropActivity";
    private PicturePhotoGalleryAdapter adapter;
    private boolean circleDimmedLayer;
    private int cutIndex;
    private boolean isDragFrame;
    private ArrayList<CutInfo> list;
    private int mActiveWidgetColor;
    private View mBlockingView;
    private GestureCropImageView mGestureCropImageView;
    private ViewGroup mLayoutAspectRatio;
    private ViewGroup mLayoutRotate;
    private ViewGroup mLayoutScale;
    private int mLogoColor;
    private OverlayView mOverlayView;
    private RecyclerView mRecyclerView;
    private int mRootViewBackgroundColor;
    private boolean mShowBottomControls;
    private int mStatusBarColor;
    private TextView mTextViewRotateAngle;
    private TextView mTextViewScalePercent;
    private int mToolbarCancelDrawable;
    private int mToolbarColor;
    private int mToolbarCropDrawable;
    private String mToolbarTitle;
    private int mToolbarWidgetColor;
    private UCropView mUCropView;
    private ViewGroup mWrapperStateAspectRatio;
    private ViewGroup mWrapperStateRotate;
    private ViewGroup mWrapperStateScale;
    private boolean openWhiteStatusBar;
    private boolean rotateEnabled;
    private boolean scaleEnabled;
    private RelativeLayout uCropMultiplePhotoBox;
    private boolean mShowLoader = true;
    private List<ViewGroup> mCropAspectRatioViews = new ArrayList();
    private Bitmap.CompressFormat mCompressFormat = DEFAULT_COMPRESS_FORMAT;
    private int mCompressQuality = 90;
    private int[] mAllowedGestures = {1, 2, 3};
    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity.1
        @Override // com.yalantis.ucrop.view.TransformImageView.TransformImageListener
        public void onRotate(float f) {
            PictureMultiCuttingActivity.this.setAngleText(f);
        }

        @Override // com.yalantis.ucrop.view.TransformImageView.TransformImageListener
        public void onScale(float f) {
            PictureMultiCuttingActivity.this.setScaleText(f);
        }

        @Override // com.yalantis.ucrop.view.TransformImageView.TransformImageListener
        public void onLoadComplete() {
            PictureMultiCuttingActivity.this.mUCropView.animate().alpha(1.0f).setDuration(300L).setInterpolator(new AccelerateInterpolator());
            PictureMultiCuttingActivity.this.mBlockingView.setClickable(false);
            PictureMultiCuttingActivity.this.mShowLoader = false;
            PictureMultiCuttingActivity.this.supportInvalidateOptionsMenu();
        }

        @Override // com.yalantis.ucrop.view.TransformImageView.TransformImageListener
        public void onLoadFailure(Exception exc) {
            PictureMultiCuttingActivity.this.setResultError(exc);
            PictureMultiCuttingActivity.this.closeActivity();
        }
    };
    private final View.OnClickListener mStateClickListener = new View.OnClickListener() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity$$ExternalSyntheticLambda0
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            PictureMultiCuttingActivity.this.m68lambda$new$4$comyalantisucropPictureMultiCuttingActivity(view);
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface GestureTypes {
    }

    private void setAllowedGestures(int i) {
    }

    @Override // android.app.Activity
    public boolean isImmersive() {
        return true;
    }

    public void immersive() {
        CropImmersiveManage.immersiveAboveAPI23(this, this.mStatusBarColor, this.mToolbarColor, this.openWhiteStatusBar);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        getIntentData(intent);
        if (isImmersive()) {
            immersive();
        }
        setContentView(R.layout.ucrop_picture_activity_multi_cutting);
        this.uCropMultiplePhotoBox = (RelativeLayout) findViewById(R.id.ucrop_mulit_photobox);
        initLoadCutData();
        addPhotoRecyclerView();
        setupViews(intent);
        setInitialState();
        addBlockingView();
        setImageData(intent);
    }

    private void initLoadCutData() {
        ArrayList<CutInfo> arrayList = (ArrayList) getIntent().getSerializableExtra(UCropMulti.Options.EXTRA_CUT_CROP);
        this.list = arrayList;
        if (arrayList == null || arrayList.size() == 0) {
            closeActivity();
        }
    }

    private void addPhotoRecyclerView() {
        RecyclerView recyclerView = new RecyclerView(this);
        this.mRecyclerView = recyclerView;
        recyclerView.setId(R.id.id_recycler);
        this.mRecyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.ucrop_color_widget_background));
        this.mRecyclerView.setLayoutParams(new RelativeLayout.LayoutParams(-1, dip2px(90.0f)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);
        resetCutDataStatus();
        this.list.get(this.cutIndex).setCut(true);
        PicturePhotoGalleryAdapter picturePhotoGalleryAdapter = new PicturePhotoGalleryAdapter(this, this.list);
        this.adapter = picturePhotoGalleryAdapter;
        this.mRecyclerView.setAdapter(picturePhotoGalleryAdapter);
        this.adapter.setOnItemClickListener(new PicturePhotoGalleryAdapter.OnItemClickListener() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity$$ExternalSyntheticLambda4
            @Override // com.yalantis.ucrop.PicturePhotoGalleryAdapter.OnItemClickListener
            public final void onItemClick(int i, View view) {
                PictureMultiCuttingActivity.this.m67x7f905d6e(i, view);
            }
        });
        this.uCropMultiplePhotoBox.addView(this.mRecyclerView);
        changeLayoutParams(this.mShowBottomControls);
        ((RelativeLayout.LayoutParams) ((FrameLayout) findViewById(R.id.ucrop_frame)).getLayoutParams()).addRule(2, R.id.id_recycler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$addPhotoRecyclerView$0$com-yalantis-ucrop-PictureMultiCuttingActivity  reason: not valid java name */
    public /* synthetic */ void m67x7f905d6e(int i, View view) {
        if (this.cutIndex == i) {
            return;
        }
        this.cutIndex = i;
        resetCutData();
    }

    private void refreshPhotoRecyclerData() {
        resetCutDataStatus();
        this.list.get(this.cutIndex).setCut(true);
        this.adapter.notifyDataSetChanged();
        this.uCropMultiplePhotoBox.addView(this.mRecyclerView);
        changeLayoutParams(this.mShowBottomControls);
        ((RelativeLayout.LayoutParams) ((FrameLayout) findViewById(R.id.ucrop_frame)).getLayoutParams()).addRule(2, R.id.id_recycler);
    }

    private void resetCutDataStatus() {
        int size = this.list.size();
        for (int i = 0; i < size; i++) {
            this.list.get(i).setCut(false);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);
        MenuItem findItem = menu.findItem(R.id.menu_loader);
        Drawable icon = findItem.getIcon();
        if (icon != null) {
            try {
                icon.mutate();
                icon.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
                findItem.setIcon(icon);
            } catch (IllegalStateException e) {
                Log.i(TAG, String.format("%s - %s", e.getMessage(), getString(R.string.ucrop_mutate_exception_hint)));
            }
            ((Animatable) findItem.getIcon()).start();
        }
        MenuItem findItem2 = menu.findItem(R.id.menu_crop);
        Drawable drawable = ContextCompat.getDrawable(this, this.mToolbarCropDrawable);
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
            findItem2.setIcon(drawable);
        }
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!this.mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(this.mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.menu_crop) {
            cropAndSaveImage();
        } else if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        exitAnimation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        GestureCropImageView gestureCropImageView = this.mGestureCropImageView;
        if (gestureCropImageView != null) {
            gestureCropImageView.cancelAllAnimations();
        }
    }

    private void setImageData(Intent intent) {
        Uri uri = (Uri) intent.getParcelableExtra(UCropMulti.EXTRA_INPUT_URI);
        Uri uri2 = (Uri) intent.getParcelableExtra(UCropMulti.EXTRA_OUTPUT_URI);
        processOptions(intent);
        if (uri != null && uri2 != null) {
            try {
                boolean isGifForSuffix = FileUtils.isGifForSuffix(FileUtils.extSuffix(new FileInputStream(getContentResolver().openFileDescriptor(uri, "r").getFileDescriptor())));
                boolean z = false;
                this.mGestureCropImageView.setRotateEnabled(isGifForSuffix ? false : this.rotateEnabled);
                GestureCropImageView gestureCropImageView = this.mGestureCropImageView;
                if (!isGifForSuffix) {
                    z = this.scaleEnabled;
                }
                gestureCropImageView.setScaleEnabled(z);
                this.mGestureCropImageView.setImageUri(uri, uri2);
                return;
            } catch (Exception e) {
                setResultError(e);
                closeActivity();
                return;
            }
        }
        setResultError(new NullPointerException(getString(R.string.ucrop_error_input_data_is_absent)));
        closeActivity();
    }

    private void processOptions(Intent intent) {
        String stringExtra = intent.getStringExtra(UCropMulti.Options.EXTRA_COMPRESSION_FORMAT_NAME);
        Bitmap.CompressFormat valueOf = !TextUtils.isEmpty(stringExtra) ? Bitmap.CompressFormat.valueOf(stringExtra) : null;
        if (valueOf == null) {
            valueOf = DEFAULT_COMPRESS_FORMAT;
        }
        this.mCompressFormat = valueOf;
        this.mCompressQuality = intent.getIntExtra(UCrop.Options.EXTRA_COMPRESSION_QUALITY, 90);
        int[] intArrayExtra = intent.getIntArrayExtra(UCropMulti.Options.EXTRA_ALLOWED_GESTURES);
        if (intArrayExtra != null && intArrayExtra.length == 3) {
            this.mAllowedGestures = intArrayExtra;
        }
        this.mGestureCropImageView.setMaxBitmapSize(intent.getIntExtra(UCropMulti.Options.EXTRA_MAX_BITMAP_SIZE, 0));
        this.mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(UCropMulti.Options.EXTRA_MAX_SCALE_MULTIPLIER, 10.0f));
        this.mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra(UCropMulti.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, 500));
        this.mOverlayView.setDragFrame(this.isDragFrame);
        this.mOverlayView.setFreestyleCropEnabled(intent.getBooleanExtra(UCropMulti.Options.EXTRA_FREE_STYLE_CROP, false));
        this.circleDimmedLayer = intent.getBooleanExtra(UCropMulti.Options.EXTRA_CIRCLE_DIMMED_LAYER, false);
        this.mOverlayView.setDimmedColor(intent.getIntExtra(UCropMulti.Options.EXTRA_DIMMED_LAYER_COLOR, getResources().getColor(R.color.ucrop_color_default_dimmed)));
        this.mOverlayView.setCircleDimmedLayer(this.circleDimmedLayer);
        this.mOverlayView.setShowCropFrame(intent.getBooleanExtra(UCropMulti.Options.EXTRA_SHOW_CROP_FRAME, true));
        this.mOverlayView.setCropFrameColor(intent.getIntExtra(UCropMulti.Options.EXTRA_CROP_FRAME_COLOR, getResources().getColor(R.color.ucrop_color_default_crop_frame)));
        this.mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra(UCropMulti.Options.EXTRA_CROP_FRAME_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width)));
        this.mOverlayView.setShowCropGrid(intent.getBooleanExtra(UCropMulti.Options.EXTRA_SHOW_CROP_GRID, true));
        this.mOverlayView.setCropGridRowCount(intent.getIntExtra(UCropMulti.Options.EXTRA_CROP_GRID_ROW_COUNT, 2));
        this.mOverlayView.setCropGridColumnCount(intent.getIntExtra(UCropMulti.Options.EXTRA_CROP_GRID_COLUMN_COUNT, 2));
        this.mOverlayView.setCropGridColor(intent.getIntExtra(UCropMulti.Options.EXTRA_CROP_GRID_COLOR, getResources().getColor(R.color.ucrop_color_default_crop_grid)));
        this.mOverlayView.setCropGridStrokeWidth(intent.getIntExtra(UCropMulti.Options.EXTRA_CROP_GRID_STROKE_WIDTH, getResources().getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)));
        float floatExtra = intent.getFloatExtra(UCropMulti.EXTRA_ASPECT_RATIO_X, 0.0f);
        float floatExtra2 = intent.getFloatExtra(UCropMulti.EXTRA_ASPECT_RATIO_Y, 0.0f);
        int intExtra = intent.getIntExtra(UCropMulti.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra(UCropMulti.Options.EXTRA_ASPECT_RATIO_OPTIONS);
        if (floatExtra > 0.0f && floatExtra2 > 0.0f) {
            ViewGroup viewGroup = this.mWrapperStateAspectRatio;
            if (viewGroup != null) {
                viewGroup.setVisibility(8);
            }
            this.mGestureCropImageView.setTargetAspectRatio(floatExtra / floatExtra2);
        } else if (parcelableArrayListExtra != null && intExtra < parcelableArrayListExtra.size()) {
            this.mGestureCropImageView.setTargetAspectRatio(((AspectRatio) parcelableArrayListExtra.get(intExtra)).getAspectRatioX() / ((AspectRatio) parcelableArrayListExtra.get(intExtra)).getAspectRatioY());
        } else {
            this.mGestureCropImageView.setTargetAspectRatio(0.0f);
        }
        int intExtra2 = intent.getIntExtra(UCropMulti.EXTRA_MAX_SIZE_X, 0);
        int intExtra3 = intent.getIntExtra(UCropMulti.EXTRA_MAX_SIZE_Y, 0);
        if (intExtra2 <= 0 || intExtra3 <= 0) {
            return;
        }
        this.mGestureCropImageView.setMaxResultImageSizeX(intExtra2);
        this.mGestureCropImageView.setMaxResultImageSizeY(intExtra3);
    }

    private void getIntentData(Intent intent) {
        this.openWhiteStatusBar = intent.getBooleanExtra(UCrop.Options.EXTRA_UCROP_WIDGET_CROP_OPEN_WHITE_STATUSBAR, false);
        this.mStatusBarColor = intent.getIntExtra(UCropMulti.Options.EXTRA_STATUS_BAR_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_statusbar));
        int intExtra = intent.getIntExtra(UCropMulti.Options.EXTRA_TOOL_BAR_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_toolbar));
        this.mToolbarColor = intExtra;
        if (intExtra == 0) {
            this.mToolbarColor = ContextCompat.getColor(this, R.color.ucrop_color_toolbar);
        }
        if (this.mStatusBarColor == 0) {
            this.mStatusBarColor = ContextCompat.getColor(this, R.color.ucrop_color_statusbar);
        }
    }

    private void setupViews(Intent intent) {
        this.scaleEnabled = intent.getBooleanExtra(UCropMulti.Options.EXTRA_SCALE, false);
        this.rotateEnabled = intent.getBooleanExtra(UCropMulti.Options.EXTRA_ROTATE, false);
        this.isDragFrame = intent.getBooleanExtra(UCrop.Options.EXTRA_DRAG_CROP_FRAME, true);
        this.mActiveWidgetColor = intent.getIntExtra(UCropMulti.Options.EXTRA_UCROP_COLOR_WIDGET_ACTIVE, ContextCompat.getColor(this, R.color.ucrop_color_widget_active));
        int intExtra = intent.getIntExtra(UCropMulti.Options.EXTRA_UCROP_WIDGET_COLOR_TOOLBAR, ContextCompat.getColor(this, R.color.ucrop_color_toolbar_widget));
        this.mToolbarWidgetColor = intExtra;
        if (intExtra == 0) {
            this.mToolbarWidgetColor = ContextCompat.getColor(this, R.color.ucrop_color_toolbar_widget);
        }
        this.mToolbarCancelDrawable = intent.getIntExtra(UCropMulti.Options.EXTRA_UCROP_WIDGET_CANCEL_DRAWABLE, R.drawable.ucrop_ic_cross);
        this.mToolbarCropDrawable = intent.getIntExtra(UCropMulti.Options.EXTRA_UCROP_WIDGET_CROP_DRAWABLE, R.drawable.ucrop_ic_done);
        String stringExtra = intent.getStringExtra(UCropMulti.Options.EXTRA_UCROP_TITLE_TEXT_TOOLBAR);
        this.mToolbarTitle = stringExtra;
        if (stringExtra == null) {
            stringExtra = getResources().getString(R.string.ucrop_label_edit_photo);
        }
        this.mToolbarTitle = stringExtra;
        this.mLogoColor = intent.getIntExtra(UCropMulti.Options.EXTRA_UCROP_LOGO_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_default_logo));
        this.mShowBottomControls = !intent.getBooleanExtra(UCropMulti.Options.EXTRA_HIDE_BOTTOM_CONTROLS, false);
        this.mRootViewBackgroundColor = intent.getIntExtra(UCropMulti.Options.EXTRA_UCROP_ROOT_VIEW_BACKGROUND_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_crop_background));
        setNavBarColor();
        setupAppBar();
        initiateRootViews();
        if (this.mShowBottomControls) {
            View.inflate(this, R.layout.ucrop_controls, this.uCropMultiplePhotoBox);
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.state_aspect_ratio);
            this.mWrapperStateAspectRatio = viewGroup;
            viewGroup.setOnClickListener(this.mStateClickListener);
            ViewGroup viewGroup2 = (ViewGroup) findViewById(R.id.state_rotate);
            this.mWrapperStateRotate = viewGroup2;
            viewGroup2.setOnClickListener(this.mStateClickListener);
            ViewGroup viewGroup3 = (ViewGroup) findViewById(R.id.state_scale);
            this.mWrapperStateScale = viewGroup3;
            viewGroup3.setOnClickListener(this.mStateClickListener);
            this.mLayoutAspectRatio = (ViewGroup) findViewById(R.id.layout_aspect_ratio);
            this.mLayoutRotate = (ViewGroup) findViewById(R.id.layout_rotate_wheel);
            this.mLayoutScale = (ViewGroup) findViewById(R.id.layout_scale_wheel);
            setupAspectRatioWidget(intent);
            setupRotateWidget();
            setupScaleWidget();
            setupStatesWrapper();
        }
        changeLayoutParams(this.mShowBottomControls);
    }

    private void setNavBarColor() {
        int intExtra;
        if (Build.VERSION.SDK_INT < 21 || (intExtra = getIntent().getIntExtra(UCropMulti.EXTRA_NAV_BAR_COLOR, 0)) == 0) {
            return;
        }
        getWindow().setNavigationBarColor(intExtra);
    }

    private void setupAppBar() {
        setStatusBarColor(this.mStatusBarColor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(this.mToolbarColor);
        toolbar.setTitleTextColor(this.mToolbarWidgetColor);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textView.setTextColor(this.mToolbarWidgetColor);
        textView.setText(this.mToolbarTitle);
        Drawable mutate = ContextCompat.getDrawable(this, this.mToolbarCancelDrawable).mutate();
        mutate.setColorFilter(this.mToolbarWidgetColor, PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationIcon(mutate);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initiateRootViews() {
        UCropView uCropView = (UCropView) findViewById(R.id.ucrop);
        this.mUCropView = uCropView;
        this.mGestureCropImageView = uCropView.getCropImageView();
        this.mOverlayView = this.mUCropView.getOverlayView();
        this.mGestureCropImageView.setTransformImageListener(this.mImageListener);
    }

    private void setupStatesWrapper() {
        ImageView imageView = (ImageView) findViewById(R.id.image_view_state_scale);
        ImageView imageView2 = (ImageView) findViewById(R.id.image_view_state_rotate);
        ImageView imageView3 = (ImageView) findViewById(R.id.image_view_state_aspect_ratio);
        imageView.setImageDrawable(new SelectedStateListDrawable(imageView.getDrawable(), this.mActiveWidgetColor));
        imageView2.setImageDrawable(new SelectedStateListDrawable(imageView2.getDrawable(), this.mActiveWidgetColor));
        imageView3.setImageDrawable(new SelectedStateListDrawable(imageView3.getDrawable(), this.mActiveWidgetColor));
    }

    private void setStatusBarColor(int i) {
        Window window;
        if (Build.VERSION.SDK_INT < 21 || (window = getWindow()) == null) {
            return;
        }
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(i);
    }

    private void setupAspectRatioWidget(Intent intent) {
        int intExtra = intent.getIntExtra(UCropMulti.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0);
        ArrayList parcelableArrayListExtra = intent.getParcelableArrayListExtra(UCropMulti.Options.EXTRA_ASPECT_RATIO_OPTIONS);
        if (parcelableArrayListExtra == null || parcelableArrayListExtra.isEmpty()) {
            intExtra = 2;
            parcelableArrayListExtra = new ArrayList();
            parcelableArrayListExtra.add(new AspectRatio(null, 1.0f, 1.0f));
            parcelableArrayListExtra.add(new AspectRatio(null, 3.0f, 4.0f));
            parcelableArrayListExtra.add(new AspectRatio(getString(R.string.ucrop_label_original).toUpperCase(), 0.0f, 0.0f));
            parcelableArrayListExtra.add(new AspectRatio(null, 3.0f, 2.0f));
            parcelableArrayListExtra.add(new AspectRatio(null, 16.0f, 9.0f));
        }
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_aspect_ratio);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -1);
        layoutParams.weight = 1.0f;
        Iterator it = parcelableArrayListExtra.iterator();
        while (it.hasNext()) {
            FrameLayout frameLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.ucrop_aspect_ratio, (ViewGroup) null);
            frameLayout.setLayoutParams(layoutParams);
            AspectRatioTextView aspectRatioTextView = (AspectRatioTextView) frameLayout.getChildAt(0);
            aspectRatioTextView.setActiveColor(this.mActiveWidgetColor);
            aspectRatioTextView.setAspectRatio((AspectRatio) it.next());
            linearLayout.addView(frameLayout);
            this.mCropAspectRatioViews.add(frameLayout);
        }
        this.mCropAspectRatioViews.get(intExtra).setSelected(true);
        for (ViewGroup viewGroup : this.mCropAspectRatioViews) {
            viewGroup.setOnClickListener(new View.OnClickListener() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PictureMultiCuttingActivity.this.m69xd9882fa8(view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setupAspectRatioWidget$1$com-yalantis-ucrop-PictureMultiCuttingActivity  reason: not valid java name */
    public /* synthetic */ void m69xd9882fa8(View view) {
        this.mGestureCropImageView.setTargetAspectRatio(((AspectRatioTextView) ((ViewGroup) view).getChildAt(0)).getAspectRatio(view.isSelected()));
        this.mGestureCropImageView.setImageToWrapCropBounds();
        if (view.isSelected()) {
            return;
        }
        Iterator<ViewGroup> it = this.mCropAspectRatioViews.iterator();
        while (it.hasNext()) {
            ViewGroup next = it.next();
            next.setSelected(next == view);
        }
    }

    private void setupRotateWidget() {
        this.mTextViewRotateAngle = (TextView) findViewById(R.id.text_view_rotate);
        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity.2
            @Override // com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.ScrollingListener
            public void onScroll(float f, float f2) {
                PictureMultiCuttingActivity.this.mGestureCropImageView.postRotate(f / 42.0f);
            }

            @Override // com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.ScrollingListener
            public void onScrollEnd() {
                PictureMultiCuttingActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override // com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.ScrollingListener
            public void onScrollStart() {
                PictureMultiCuttingActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(R.id.rotate_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
        findViewById(R.id.wrapper_reset_rotate).setOnClickListener(new View.OnClickListener() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PictureMultiCuttingActivity.this.m70x82cb16bf(view);
            }
        });
        findViewById(R.id.wrapper_rotate_by_angle).setOnClickListener(new View.OnClickListener() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PictureMultiCuttingActivity.this.m71xbc95b89e(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setupRotateWidget$2$com-yalantis-ucrop-PictureMultiCuttingActivity  reason: not valid java name */
    public /* synthetic */ void m70x82cb16bf(View view) {
        resetRotation();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$setupRotateWidget$3$com-yalantis-ucrop-PictureMultiCuttingActivity  reason: not valid java name */
    public /* synthetic */ void m71xbc95b89e(View view) {
        rotateByAngle(90);
    }

    private void setupScaleWidget() {
        this.mTextViewScalePercent = (TextView) findViewById(R.id.text_view_scale);
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel)).setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity.3
            @Override // com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.ScrollingListener
            public void onScroll(float f, float f2) {
                if (f > 0.0f) {
                    PictureMultiCuttingActivity.this.mGestureCropImageView.zoomInImage(PictureMultiCuttingActivity.this.mGestureCropImageView.getCurrentScale() + (f * ((PictureMultiCuttingActivity.this.mGestureCropImageView.getMaxScale() - PictureMultiCuttingActivity.this.mGestureCropImageView.getMinScale()) / 15000.0f)));
                } else {
                    PictureMultiCuttingActivity.this.mGestureCropImageView.zoomOutImage(PictureMultiCuttingActivity.this.mGestureCropImageView.getCurrentScale() + (f * ((PictureMultiCuttingActivity.this.mGestureCropImageView.getMaxScale() - PictureMultiCuttingActivity.this.mGestureCropImageView.getMinScale()) / 15000.0f)));
                }
            }

            @Override // com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.ScrollingListener
            public void onScrollEnd() {
                PictureMultiCuttingActivity.this.mGestureCropImageView.setImageToWrapCropBounds();
            }

            @Override // com.yalantis.ucrop.view.widget.HorizontalProgressWheelView.ScrollingListener
            public void onScrollStart() {
                PictureMultiCuttingActivity.this.mGestureCropImageView.cancelAllAnimations();
            }
        });
        ((HorizontalProgressWheelView) findViewById(R.id.scale_scroll_wheel)).setMiddleLineColor(this.mActiveWidgetColor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAngleText(float f) {
        TextView textView = this.mTextViewRotateAngle;
        if (textView != null) {
            textView.setText(String.format(Locale.getDefault(), "%.1f°", Float.valueOf(f)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setScaleText(float f) {
        TextView textView = this.mTextViewScalePercent;
        if (textView != null) {
            textView.setText(String.format(Locale.getDefault(), "%d%%", Integer.valueOf((int) (f * 100.0f))));
        }
    }

    private void resetRotation() {
        GestureCropImageView gestureCropImageView = this.mGestureCropImageView;
        gestureCropImageView.postRotate(-gestureCropImageView.getCurrentAngle());
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    private void rotateByAngle(int i) {
        this.mGestureCropImageView.postRotate(i);
        this.mGestureCropImageView.setImageToWrapCropBounds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-yalantis-ucrop-PictureMultiCuttingActivity  reason: not valid java name */
    public /* synthetic */ void m68lambda$new$4$comyalantisucropPictureMultiCuttingActivity(View view) {
        if (view.isSelected()) {
            return;
        }
        setWidgetState(view.getId());
    }

    private void setInitialState() {
        if (this.mShowBottomControls) {
            if (this.mWrapperStateAspectRatio.getVisibility() == 0) {
                setWidgetState(R.id.state_aspect_ratio);
                return;
            } else {
                setWidgetState(R.id.state_scale);
                return;
            }
        }
        setAllowedGestures(0);
    }

    private void setWidgetState(int i) {
        if (this.mShowBottomControls) {
            this.mWrapperStateAspectRatio.setSelected(i == R.id.state_aspect_ratio);
            this.mWrapperStateRotate.setSelected(i == R.id.state_rotate);
            this.mWrapperStateScale.setSelected(i == R.id.state_scale);
            this.mLayoutAspectRatio.setVisibility(i == R.id.state_aspect_ratio ? 0 : 8);
            this.mLayoutRotate.setVisibility(i == R.id.state_rotate ? 0 : 8);
            this.mLayoutScale.setVisibility(i == R.id.state_scale ? 0 : 8);
            if (i == R.id.state_scale) {
                setAllowedGestures(0);
            } else if (i == R.id.state_rotate) {
                setAllowedGestures(1);
            } else {
                setAllowedGestures(2);
            }
        }
    }

    private void addBlockingView() {
        if (this.mBlockingView == null) {
            this.mBlockingView = new View(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(3, R.id.toolbar);
            this.mBlockingView.setLayoutParams(layoutParams);
            this.mBlockingView.setClickable(true);
        }
        this.uCropMultiplePhotoBox.addView(this.mBlockingView);
    }

    protected void cropAndSaveImage() {
        this.mBlockingView.setClickable(true);
        this.mShowLoader = true;
        supportInvalidateOptionsMenu();
        this.mGestureCropImageView.cropAndSaveImage(this.mCompressFormat, this.mCompressQuality, new BitmapCropCallback() { // from class: com.yalantis.ucrop.PictureMultiCuttingActivity.4
            @Override // com.yalantis.ucrop.callback.BitmapCropCallback
            public void onBitmapCropped(Uri uri, int i, int i2, int i3, int i4) {
                PictureMultiCuttingActivity pictureMultiCuttingActivity = PictureMultiCuttingActivity.this;
                pictureMultiCuttingActivity.setResultUri(uri, pictureMultiCuttingActivity.mGestureCropImageView.getTargetAspectRatio(), i, i2, i3, i4);
            }

            @Override // com.yalantis.ucrop.callback.BitmapCropCallback
            public void onCropFailure(Throwable th) {
                PictureMultiCuttingActivity.this.setResultError(th);
                PictureMultiCuttingActivity.this.closeActivity();
            }
        });
    }

    protected void setResultUri(Uri uri, float f, int i, int i2, int i3, int i4) {
        try {
            CutInfo cutInfo = this.list.get(this.cutIndex);
            cutInfo.setCutPath(uri.getPath());
            cutInfo.setCut(true);
            cutInfo.setResultAspectRatio(f);
            cutInfo.setOffsetX(i);
            cutInfo.setOffsetY(i2);
            cutInfo.setImageWidth(i3);
            cutInfo.setImageHeight(i4);
            int i5 = this.cutIndex + 1;
            this.cutIndex = i5;
            if (i5 >= this.list.size()) {
                setResult(-1, new Intent().putExtra(UCropMulti.EXTRA_OUTPUT_URI_LIST, this.list));
                closeActivity();
            } else {
                resetCutData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void resetCutData() {
        this.uCropMultiplePhotoBox.removeView(this.mRecyclerView);
        setContentView(R.layout.ucrop_picture_activity_multi_cutting);
        this.uCropMultiplePhotoBox = (RelativeLayout) findViewById(R.id.ucrop_mulit_photobox);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        boolean z = Build.VERSION.SDK_INT >= 29;
        String path = this.list.get(this.cutIndex).getPath();
        boolean isHttp = FileUtils.isHttp(path);
        String lastImgType = getLastImgType(z ? FileUtils.getPath(this, Uri.parse(path)) : path);
        extras.putParcelable(UCropMulti.EXTRA_INPUT_URI, (isHttp || z) ? Uri.parse(path) : Uri.fromFile(new File(path)));
        File externalFilesDir = Environment.getExternalStorageState().equals("mounted") ? getExternalFilesDir(Environment.DIRECTORY_PICTURES) : getCacheDir();
        extras.putParcelable(UCropMulti.EXTRA_OUTPUT_URI, Uri.fromFile(new File(externalFilesDir, System.currentTimeMillis() + lastImgType)));
        intent.putExtras(extras);
        refreshPhotoRecyclerData();
        setupViews(intent);
        setImageData(intent);
        int i = this.cutIndex;
        if (i >= 5) {
            this.mRecyclerView.scrollToPosition(i);
        }
        changeLayoutParams(this.mShowBottomControls);
    }

    private void changeLayoutParams(boolean z) {
        if (this.mRecyclerView.getLayoutParams() == null) {
            return;
        }
        if (z) {
            ((RelativeLayout.LayoutParams) this.mRecyclerView.getLayoutParams()).addRule(12, 0);
            ((RelativeLayout.LayoutParams) this.mRecyclerView.getLayoutParams()).addRule(2, R.id.wrapper_controls);
            return;
        }
        ((RelativeLayout.LayoutParams) this.mRecyclerView.getLayoutParams()).addRule(12);
        ((RelativeLayout.LayoutParams) this.mRecyclerView.getLayoutParams()).addRule(2, 0);
    }

    public static String getLastImgType(String str) {
        try {
            int lastIndexOf = str.lastIndexOf(".");
            if (lastIndexOf > 0) {
                String substring = str.substring(lastIndexOf, str.length());
                char c = 65535;
                switch (substring.hashCode()) {
                    case 1436279:
                        if (substring.equals(".BMP")) {
                            c = 7;
                            break;
                        }
                        break;
                    case 1449755:
                        if (substring.equals(".PNG")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 1468055:
                        if (substring.equals(".bmp")) {
                            c = 6;
                            break;
                        }
                        break;
                    case 1475827:
                        if (substring.equals(".jpg")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 1481531:
                        if (substring.equals(PictureMimeType.PNG)) {
                            c = 0;
                            break;
                        }
                        break;
                    case 44765590:
                        if (substring.equals(PictureFileUtils.POSTFIX)) {
                            c = 4;
                            break;
                        }
                        break;
                    case 45142218:
                        if (substring.equals(".WEBP")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 45750678:
                        if (substring.equals(PictureMimeType.JPEG)) {
                            c = 3;
                            break;
                        }
                        break;
                    case 46127306:
                        if (substring.equals(".webp")) {
                            c = '\b';
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case '\b':
                        return substring;
                }
            }
            return PictureMimeType.PNG;
        } catch (Exception e) {
            e.printStackTrace();
            return PictureMimeType.PNG;
        }
    }

    protected void setResultError(Throwable th) {
        setResult(96, new Intent().putExtra(UCropMulti.EXTRA_ERROR, th));
    }

    protected void closeActivity() {
        finish();
        exitAnimation();
    }

    protected void exitAnimation() {
        int intExtra = getIntent().getIntExtra(UCropMulti.EXTRA_WINDOW_EXIT_ANIMATION, 0);
        int i = R.anim.ucrop_anim_fade_in;
        if (intExtra == 0) {
            intExtra = R.anim.ucrop_close;
        }
        overridePendingTransition(i, intExtra);
    }

    public int dip2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().density) + 0.5f);
    }
}
