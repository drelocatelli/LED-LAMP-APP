package com.common.pictureselector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.common.pictureselector.adapter.GridImageAdapter;
import com.ledlamp.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PhotoActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "PhotoActivity";
    private GridImageAdapter adapter;
    private int aspect_ratio_x;
    private int aspect_ratio_y;
    private CheckBox cb_choose_mode;
    private CheckBox cb_compress;
    private CheckBox cb_crop;
    private CheckBox cb_crop_circular;
    private CheckBox cb_hide;
    private CheckBox cb_isCamera;
    private CheckBox cb_isGif;
    private CheckBox cb_mode;
    private CheckBox cb_preview_audio;
    private CheckBox cb_preview_img;
    private CheckBox cb_preview_video;
    private CheckBox cb_showCropFrame;
    private CheckBox cb_showCropGrid;
    private CheckBox cb_styleCrop;
    private CheckBox cb_voice;
    private int language;
    private ImageView left_back;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureParameterStyle mPictureParameterStyle;
    private ImageView minus;
    private ImageView plus;
    private RecyclerView recyclerView;
    private RadioGroup rgb_crop;
    private RadioGroup rgb_langue;
    private RadioGroup rgb_photo_mode;
    private RadioGroup rgb_style;
    private int themeId;
    private TextView tv_select_num;
    private List<LocalMedia> selectList = new ArrayList();
    private int maxSelectNum = 9;
    private int chooseMode = PictureMimeType.ofAll();
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() { // from class: com.common.pictureselector.PhotoActivity.1
        @Override // com.common.pictureselector.adapter.GridImageAdapter.onAddPicClickListener
        public void onAddPicClick() {
            if (PhotoActivity.this.cb_mode.isChecked()) {
                PictureSelector.create(PhotoActivity.this).openGallery(PhotoActivity.this.chooseMode).loadImageEngine(GlideEngine.createGlideEngine()).theme(PhotoActivity.this.themeId).setLanguage(PhotoActivity.this.language).setPictureStyle(PhotoActivity.this.mPictureParameterStyle).setPictureCropStyle(PhotoActivity.this.mCropParameterStyle).maxSelectNum(PhotoActivity.this.maxSelectNum).minSelectNum(1).imageSpanCount(4).cameraFileName("").selectionMode(PhotoActivity.this.cb_choose_mode.isChecked() ? 2 : 1).isSingleDirectReturn(false).previewImage(PhotoActivity.this.cb_preview_img.isChecked()).previewVideo(PhotoActivity.this.cb_preview_video.isChecked()).enablePreviewAudio(PhotoActivity.this.cb_preview_audio.isChecked()).isCamera(PhotoActivity.this.cb_isCamera.isChecked()).isZoomAnim(true).enableCrop(PhotoActivity.this.cb_crop.isChecked()).compress(PhotoActivity.this.cb_compress.isChecked()).compressQuality(80).synOrAsy(true).withAspectRatio(PhotoActivity.this.aspect_ratio_x, PhotoActivity.this.aspect_ratio_y).hideBottomControls(!PhotoActivity.this.cb_hide.isChecked()).isGif(PhotoActivity.this.cb_isGif.isChecked()).freeStyleCropEnabled(PhotoActivity.this.cb_styleCrop.isChecked()).circleDimmedLayer(PhotoActivity.this.cb_crop_circular.isChecked()).showCropFrame(PhotoActivity.this.cb_showCropFrame.isChecked()).showCropGrid(PhotoActivity.this.cb_showCropGrid.isChecked()).openClickSound(PhotoActivity.this.cb_voice.isChecked()).selectionMedia(PhotoActivity.this.selectList).cutOutQuality(90).minimumCompressSize(100).forResult(PictureConfig.CHOOSE_REQUEST);
            } else {
                PictureSelector.create(PhotoActivity.this).openCamera(PhotoActivity.this.chooseMode).theme(PhotoActivity.this.themeId).loadImageEngine(GlideEngine.createGlideEngine()).setPictureStyle(PhotoActivity.this.mPictureParameterStyle).setPictureCropStyle(PhotoActivity.this.mCropParameterStyle).maxSelectNum(PhotoActivity.this.maxSelectNum).minSelectNum(1).selectionMode(PhotoActivity.this.cb_choose_mode.isChecked() ? 2 : 1).previewImage(PhotoActivity.this.cb_preview_img.isChecked()).previewVideo(PhotoActivity.this.cb_preview_video.isChecked()).enablePreviewAudio(PhotoActivity.this.cb_preview_audio.isChecked()).isCamera(PhotoActivity.this.cb_isCamera.isChecked()).enableCrop(PhotoActivity.this.cb_crop.isChecked()).compress(PhotoActivity.this.cb_compress.isChecked()).compressQuality(60).glideOverride(160, 160).withAspectRatio(PhotoActivity.this.aspect_ratio_x, PhotoActivity.this.aspect_ratio_y).hideBottomControls(!PhotoActivity.this.cb_hide.isChecked()).isGif(PhotoActivity.this.cb_isGif.isChecked()).freeStyleCropEnabled(PhotoActivity.this.cb_styleCrop.isChecked()).circleDimmedLayer(PhotoActivity.this.cb_crop_circular.isChecked()).showCropFrame(PhotoActivity.this.cb_showCropFrame.isChecked()).showCropGrid(PhotoActivity.this.cb_showCropGrid.isChecked()).openClickSound(PhotoActivity.this.cb_voice.isChecked()).selectionMedia(PhotoActivity.this.selectList).previewEggs(false).cutOutQuality(90).minimumCompressSize(100).forResult(PictureConfig.CHOOSE_REQUEST);
            }
        }
    };
    private int x = 0;
    private int y = 0;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.common.pictureselector.PhotoActivity.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals(BroadcastAction.ACTION_DELETE_PREVIEW_POSITION)) {
                int i = intent.getExtras().getInt("position");
                ToastUtils.s(context, "delete image index:" + i);
                if (i < PhotoActivity.this.adapter.getItemCount()) {
                    PhotoActivity.this.selectList.remove(i);
                    PhotoActivity.this.adapter.notifyItemRemoved(i);
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo);
        this.themeId = 2131821094;
        getDefaultStyle();
        this.minus = (ImageView) findViewById(R.id.minus);
        this.plus = (ImageView) findViewById(R.id.plus);
        this.tv_select_num = (TextView) findViewById(R.id.tv_select_num);
        this.rgb_crop = (RadioGroup) findViewById(R.id.rgb_crop);
        this.rgb_style = (RadioGroup) findViewById(R.id.rgb_style);
        this.rgb_photo_mode = (RadioGroup) findViewById(R.id.rgb_photo_mode);
        this.rgb_langue = (RadioGroup) findViewById(R.id.rgb_langue);
        this.cb_voice = (CheckBox) findViewById(R.id.cb_voice);
        this.cb_choose_mode = (CheckBox) findViewById(R.id.cb_choose_mode);
        this.cb_isCamera = (CheckBox) findViewById(R.id.cb_isCamera);
        this.cb_isGif = (CheckBox) findViewById(R.id.cb_isGif);
        this.cb_preview_img = (CheckBox) findViewById(R.id.cb_preview_img);
        this.cb_preview_video = (CheckBox) findViewById(R.id.cb_preview_video);
        this.cb_crop = (CheckBox) findViewById(R.id.cb_crop);
        this.cb_styleCrop = (CheckBox) findViewById(R.id.cb_styleCrop);
        this.cb_compress = (CheckBox) findViewById(R.id.cb_compress);
        this.cb_mode = (CheckBox) findViewById(R.id.cb_mode);
        this.cb_showCropGrid = (CheckBox) findViewById(R.id.cb_showCropGrid);
        this.cb_showCropFrame = (CheckBox) findViewById(R.id.cb_showCropFrame);
        this.cb_preview_audio = (CheckBox) findViewById(R.id.cb_preview_audio);
        this.cb_hide = (CheckBox) findViewById(R.id.cb_hide);
        this.cb_crop_circular = (CheckBox) findViewById(R.id.cb_crop_circular);
        this.rgb_crop.setOnCheckedChangeListener(this);
        this.rgb_style.setOnCheckedChangeListener(this);
        this.rgb_photo_mode.setOnCheckedChangeListener(this);
        this.rgb_photo_mode.check(R.id.rb_image);
        this.rgb_langue.setOnCheckedChangeListener(this);
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler);
        ImageView imageView = (ImageView) findViewById(R.id.left_back);
        this.left_back = imageView;
        imageView.setOnClickListener(this);
        this.minus.setOnClickListener(this);
        this.plus.setOnClickListener(this);
        this.cb_crop.setOnCheckedChangeListener(this);
        this.cb_crop_circular.setOnCheckedChangeListener(this);
        this.cb_compress.setOnCheckedChangeListener(this);
        this.recyclerView.setLayoutManager(new FullyGridLayoutManager(this, 4, 1, false));
        GridImageAdapter gridImageAdapter = new GridImageAdapter(this, this.onAddPicClickListener);
        this.adapter = gridImageAdapter;
        gridImageAdapter.setList(this.selectList);
        this.adapter.setSelectMax(this.maxSelectNum);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() { // from class: com.common.pictureselector.PhotoActivity$$ExternalSyntheticLambda0
            @Override // com.common.pictureselector.adapter.GridImageAdapter.OnItemClickListener
            public final void onItemClick(int i, View view) {
                PhotoActivity.this.m8lambda$onCreate$0$comcommonpictureselectorPhotoActivity(i, view);
            }
        });
        if (PermissionChecker.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
        } else {
            PermissionChecker.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        BroadcastManager.getInstance(this).registerReceiver(this.broadcastReceiver, BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-common-pictureselector-PhotoActivity  reason: not valid java name */
    public /* synthetic */ void m8lambda$onCreate$0$comcommonpictureselectorPhotoActivity(int i, View view) {
        if (this.selectList.size() > 0) {
            LocalMedia localMedia = this.selectList.get(i);
            int mimeType = PictureMimeType.getMimeType(localMedia.getMimeType());
            if (mimeType == 2) {
                PictureSelector.create(this).externalPictureVideo(localMedia.getPath());
            } else if (mimeType == 3) {
                PictureSelector.create(this).externalPictureAudio(localMedia.getPath());
            } else {
                PictureSelector.create(this).setPictureStyle(this.mPictureParameterStyle).isNotPreviewDownload(true).loadImageEngine(GlideEngine.createGlideEngine()).openExternalPreview(i, this.selectList);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 188) {
            List<LocalMedia> obtainMultipleResult = PictureSelector.obtainMultipleResult(intent);
            this.selectList = obtainMultipleResult;
            for (LocalMedia localMedia : obtainMultipleResult) {
                String str = TAG;
                Log.i(str, "压缩---->" + localMedia.getCompressPath());
                Log.i(str, "原图---->" + localMedia.getPath());
                Log.i(str, "裁剪---->" + localMedia.getCutPath());
                Log.i(str, "Android Q 特有Path---->" + localMedia.getAndroidQToPath());
            }
            this.adapter.setList(this.selectList);
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.left_back) {
            finish();
        } else if (id != R.id.minus) {
            if (id != R.id.plus) {
                return;
            }
            this.maxSelectNum++;
            this.tv_select_num.setText(this.maxSelectNum + "");
            this.adapter.setSelectMax(this.maxSelectNum);
        } else {
            int i = this.maxSelectNum;
            if (i > 1) {
                this.maxSelectNum = i - 1;
            }
            this.tv_select_num.setText(this.maxSelectNum + "");
            this.adapter.setSelectMax(this.maxSelectNum);
        }
    }

    @Override // android.widget.RadioGroup.OnCheckedChangeListener
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_all /* 2131297209 */:
                this.chooseMode = PictureMimeType.ofAll();
                this.cb_preview_img.setChecked(true);
                this.cb_preview_video.setChecked(true);
                this.cb_isGif.setChecked(false);
                this.cb_preview_video.setChecked(true);
                this.cb_preview_img.setChecked(true);
                this.cb_preview_video.setVisibility(0);
                this.cb_preview_img.setVisibility(0);
                this.cb_compress.setVisibility(0);
                this.cb_crop.setVisibility(0);
                this.cb_isGif.setVisibility(0);
                this.cb_preview_audio.setVisibility(8);
                return;
            case R.id.rb_audio /* 2131297210 */:
                this.chooseMode = PictureMimeType.ofAudio();
                this.cb_preview_audio.setVisibility(0);
                return;
            case R.id.rb_crop_16to9 /* 2131297211 */:
                this.aspect_ratio_x = 16;
                this.aspect_ratio_y = 9;
                return;
            case R.id.rb_crop_1to1 /* 2131297212 */:
                this.aspect_ratio_x = 1;
                this.aspect_ratio_y = 1;
                return;
            case R.id.rb_crop_3to2 /* 2131297213 */:
                this.aspect_ratio_x = 3;
                this.aspect_ratio_y = 2;
                return;
            case R.id.rb_crop_3to4 /* 2131297214 */:
                this.aspect_ratio_x = 3;
                this.aspect_ratio_y = 4;
                return;
            case R.id.rb_crop_default /* 2131297215 */:
                this.aspect_ratio_x = 0;
                this.aspect_ratio_y = 0;
                return;
            case R.id.rb_de /* 2131297216 */:
                this.language = 4;
                return;
            case R.id.rb_default_style /* 2131297217 */:
                this.themeId = 2131821094;
                getDefaultStyle();
                return;
            case R.id.rb_fr /* 2131297218 */:
                this.language = 5;
                return;
            case R.id.rb_image /* 2131297219 */:
                this.chooseMode = PictureMimeType.ofImage();
                this.cb_preview_img.setChecked(true);
                this.cb_preview_video.setChecked(false);
                this.cb_isGif.setChecked(false);
                this.cb_preview_video.setChecked(false);
                this.cb_preview_video.setVisibility(8);
                this.cb_preview_img.setChecked(true);
                this.cb_preview_audio.setVisibility(8);
                this.cb_preview_img.setVisibility(0);
                this.cb_compress.setVisibility(0);
                this.cb_crop.setVisibility(0);
                this.cb_isGif.setVisibility(0);
                return;
            case R.id.rb_ka /* 2131297220 */:
                this.language = 3;
                return;
            case R.id.rb_num_style /* 2131297221 */:
                this.themeId = R.style.picture_QQ_style;
                getNumStyle();
                return;
            case R.id.rb_sina_style /* 2131297222 */:
                this.themeId = R.style.picture_Sina_style;
                getSinaStyle();
                return;
            case R.id.rb_tw /* 2131297223 */:
                this.language = 1;
                return;
            case R.id.rb_us /* 2131297224 */:
                this.language = 2;
                return;
            case R.id.rb_video /* 2131297225 */:
                this.chooseMode = PictureMimeType.ofVideo();
                this.cb_preview_img.setChecked(false);
                this.cb_preview_video.setChecked(true);
                this.cb_isGif.setChecked(false);
                this.cb_isGif.setVisibility(8);
                this.cb_preview_video.setChecked(true);
                this.cb_preview_video.setVisibility(0);
                this.cb_preview_img.setVisibility(8);
                this.cb_preview_img.setChecked(false);
                this.cb_compress.setVisibility(8);
                this.cb_preview_audio.setVisibility(8);
                this.cb_crop.setVisibility(8);
                return;
            case R.id.rb_white_style /* 2131297226 */:
                this.themeId = R.style.picture_white_style;
                getWhiteStyle();
                return;
            case R.id.rb_zh /* 2131297227 */:
                this.language = 0;
                return;
            default:
                return;
        }
    }

    private void getDefaultStyle() {
        PictureParameterStyle pictureParameterStyle = new PictureParameterStyle();
        this.mPictureParameterStyle = pictureParameterStyle;
        pictureParameterStyle.isChangeStatusBarFontColor = false;
        this.mPictureParameterStyle.isOpenCompletedNumStyle = false;
        this.mPictureParameterStyle.isOpenCheckNumStyle = false;
        this.mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        this.mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        this.mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        this.mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        this.mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        this.mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        this.mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        this.mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(this, R.color.picture_color_white);
        this.mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        this.mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_fa);
        this.mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        this.mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_fa632d);
        this.mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_9b);
        this.mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_fa632d);
        this.mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_9b);
        this.mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_grey_3e);
        this.mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        this.mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        this.mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");
        this.mCropParameterStyle = new PictureCropParameterStyle(ContextCompat.getColor(this, R.color.app_color_grey), ContextCompat.getColor(this, R.color.app_color_grey), Color.parseColor("#393a3e"), ContextCompat.getColor(this, R.color.app_color_white), this.mPictureParameterStyle.isChangeStatusBarFontColor);
    }

    private void getWhiteStyle() {
        PictureParameterStyle pictureParameterStyle = new PictureParameterStyle();
        this.mPictureParameterStyle = pictureParameterStyle;
        pictureParameterStyle.isChangeStatusBarFontColor = true;
        this.mPictureParameterStyle.isOpenCompletedNumStyle = false;
        this.mPictureParameterStyle.isOpenCheckNumStyle = false;
        this.mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#FFFFFF");
        this.mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#FFFFFF");
        this.mPictureParameterStyle.pictureTitleUpResId = R.drawable.ic_orange_arrow_up;
        this.mPictureParameterStyle.pictureTitleDownResId = R.drawable.ic_orange_arrow_down;
        this.mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        this.mPictureParameterStyle.pictureLeftBackIcon = R.drawable.ic_back_arrow;
        this.mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(this, R.color.app_color_black);
        this.mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(this, R.color.app_color_black);
        this.mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        this.mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_fa);
        this.mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        this.mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_fa632d);
        this.mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_9b);
        this.mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_fa632d);
        this.mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_9b);
        this.mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_grey_3e);
        this.mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_black_delete;
        this.mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        this.mCropParameterStyle = new PictureCropParameterStyle(ContextCompat.getColor(this, R.color.app_color_white), ContextCompat.getColor(this, R.color.app_color_white), ContextCompat.getColor(this, R.color.app_color_black), this.mPictureParameterStyle.isChangeStatusBarFontColor);
    }

    private void getNumStyle() {
        PictureParameterStyle pictureParameterStyle = new PictureParameterStyle();
        this.mPictureParameterStyle = pictureParameterStyle;
        pictureParameterStyle.isChangeStatusBarFontColor = false;
        this.mPictureParameterStyle.isOpenCompletedNumStyle = false;
        this.mPictureParameterStyle.isOpenCheckNumStyle = true;
        this.mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#7D7DFF");
        this.mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#7D7DFF");
        this.mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        this.mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        this.mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        this.mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        this.mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(this, R.color.app_color_white);
        this.mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(this, R.color.app_color_white);
        this.mPictureParameterStyle.pictureCheckedStyle = R.drawable.checkbox_num_selector;
        this.mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_fa);
        this.mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.num_oval_blue;
        this.mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_blue);
        this.mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(this, R.color.app_color_blue);
        this.mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(this, R.color.app_color_blue);
        this.mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(this, R.color.app_color_blue);
        this.mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_fa);
        this.mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        this.mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        this.mCropParameterStyle = new PictureCropParameterStyle(ContextCompat.getColor(this, R.color.app_color_blue), ContextCompat.getColor(this, R.color.app_color_blue), ContextCompat.getColor(this, R.color.app_color_white), this.mPictureParameterStyle.isChangeStatusBarFontColor);
    }

    private void getSinaStyle() {
        PictureParameterStyle pictureParameterStyle = new PictureParameterStyle();
        this.mPictureParameterStyle = pictureParameterStyle;
        pictureParameterStyle.isChangeStatusBarFontColor = true;
        this.mPictureParameterStyle.isOpenCompletedNumStyle = true;
        this.mPictureParameterStyle.isOpenCheckNumStyle = false;
        this.mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#FFFFFF");
        this.mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#FFFFFF");
        this.mPictureParameterStyle.pictureTitleUpResId = R.drawable.ic_orange_arrow_up;
        this.mPictureParameterStyle.pictureTitleDownResId = R.drawable.ic_orange_arrow_down;
        this.mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        this.mPictureParameterStyle.pictureLeftBackIcon = R.drawable.ic_back_arrow;
        this.mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(this, R.color.app_color_black);
        this.mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(this, R.color.app_color_black);
        this.mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_selector;
        this.mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_fa);
        this.mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        this.mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_fa632d);
        this.mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(this, R.color.picture_color_9b);
        this.mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_fa632d);
        this.mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(this, R.color.picture_color_9b);
        this.mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(this, R.color.picture_color_grey_3e);
        this.mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_black_delete;
        this.mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        this.mCropParameterStyle = new PictureCropParameterStyle(ContextCompat.getColor(this, R.color.app_color_white), ContextCompat.getColor(this, R.color.app_color_white), ContextCompat.getColor(this, R.color.app_color_black), this.mPictureParameterStyle.isChangeStatusBarFontColor);
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        switch (compoundButton.getId()) {
            case R.id.cb_crop /* 2131296456 */:
                this.rgb_crop.setVisibility(z ? 0 : 8);
                this.cb_hide.setVisibility(z ? 0 : 8);
                this.cb_crop_circular.setVisibility(z ? 0 : 8);
                this.cb_styleCrop.setVisibility(z ? 0 : 8);
                this.cb_showCropFrame.setVisibility(z ? 0 : 8);
                this.cb_showCropGrid.setVisibility(z ? 0 : 8);
                return;
            case R.id.cb_crop_circular /* 2131296457 */:
                if (z) {
                    this.x = this.aspect_ratio_x;
                    this.y = this.aspect_ratio_y;
                    this.aspect_ratio_x = 1;
                    this.aspect_ratio_y = 1;
                } else {
                    this.aspect_ratio_x = this.x;
                    this.aspect_ratio_y = this.y;
                }
                this.rgb_crop.setVisibility(z ? 8 : 0);
                if (z) {
                    this.cb_showCropFrame.setChecked(false);
                    this.cb_showCropGrid.setChecked(false);
                    return;
                }
                this.cb_showCropFrame.setChecked(true);
                this.cb_showCropGrid.setChecked(true);
                return;
            default:
                return;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1) {
            return;
        }
        for (int i2 : iArr) {
            if (i2 == 0) {
                PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
            } else {
                Toast.makeText(this, getString(R.string.picture_jurisdiction), 0).show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.broadcastReceiver != null) {
            BroadcastManager.getInstance(this).unregisterReceiver(this.broadcastReceiver, BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
        }
    }
}
