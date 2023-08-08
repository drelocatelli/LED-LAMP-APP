package com.luck.picture.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.luck.picture.lib.PictureSelectorActivity;
import com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter;
import com.luck.picture.lib.adapter.PictureImageGridAdapter;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.dialog.PictureCustomDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.observable.ImagesObservable;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.DoubleUtils;
import com.luck.picture.lib.tools.JumpUtils;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.luck.picture.lib.widget.FolderPopWindow;
import com.luck.picture.lib.widget.PhotoPopupWindow;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropMulti;
import com.yalantis.ucrop.model.CutInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PictureSelectorActivity extends PictureBaseActivity implements View.OnClickListener, PictureAlbumDirectoryAdapter.OnItemClickListener, PictureImageGridAdapter.OnPhotoSelectChangedListener, PhotoPopupWindow.OnItemClickListener {
    private static final int DISMISS_DIALOG = 1;
    private static final int SHOW_DIALOG = 0;
    private PictureImageGridAdapter adapter;
    private PictureCustomDialog audioDialog;
    private int audioH;
    private FolderPopWindow folderWindow;
    private RelativeLayout mBottomLayout;
    private ImageView mIvArrow;
    private ImageView mIvPictureLeftBack;
    private RecyclerView mPictureRecycler;
    private TextView mTvEmpty;
    private TextView mTvMusicStatus;
    private TextView mTvMusicTime;
    private TextView mTvMusicTotal;
    private TextView mTvPictureImgNum;
    private TextView mTvPictureOk;
    private TextView mTvPicturePreview;
    private TextView mTvPictureRight;
    private TextView mTvPictureTitle;
    private TextView mTvPlayPause;
    private TextView mTvQuit;
    private TextView mTvStop;
    private LocalMediaLoader mediaLoader;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;
    private PhotoPopupWindow popupWindow;
    private View titleViewBg;
    private List<LocalMedia> images = new ArrayList();
    private List<LocalMediaFolder> foldersList = new ArrayList();
    private Animation animation = null;
    private boolean anim = false;
    private boolean isPlayAudio = false;
    private Handler mHandler = new Handler() { // from class: com.luck.picture.lib.PictureSelectorActivity.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            int i = message.what;
            if (i == 0) {
                PictureSelectorActivity.this.showPleaseDialog();
            } else if (i != 1) {
            } else {
                PictureSelectorActivity.this.dismissDialog();
            }
        }
    };
    public Handler handler = new Handler();
    public Runnable runnable = new Runnable() { // from class: com.luck.picture.lib.PictureSelectorActivity.3
        @Override // java.lang.Runnable
        public void run() {
            try {
                if (PictureSelectorActivity.this.mediaPlayer != null) {
                    PictureSelectorActivity.this.mTvMusicTime.setText(DateUtils.formatDurationTime(PictureSelectorActivity.this.mediaPlayer.getCurrentPosition()));
                    PictureSelectorActivity.this.musicSeekBar.setProgress(PictureSelectorActivity.this.mediaPlayer.getCurrentPosition());
                    PictureSelectorActivity.this.musicSeekBar.setMax(PictureSelectorActivity.this.mediaPlayer.getDuration());
                    PictureSelectorActivity.this.mTvMusicTotal.setText(DateUtils.formatDurationTime(PictureSelectorActivity.this.mediaPlayer.getDuration()));
                    if (PictureSelectorActivity.this.handler != null) {
                        PictureSelectorActivity.this.handler.postDelayed(PictureSelectorActivity.this.runnable, 200L);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private BroadcastReceiver commonBroadcastReceiver = new BroadcastReceiver() { // from class: com.luck.picture.lib.PictureSelectorActivity.4
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Bundle extras;
            String action = intent.getAction();
            action.hashCode();
            if (!action.equals(BroadcastAction.ACTION_PREVIEW_COMPRESSION)) {
                if (action.equals(BroadcastAction.ACTION_SELECTED_DATA) && (extras = intent.getExtras()) != null) {
                    ArrayList parcelableArrayList = extras.getParcelableArrayList("selectImages");
                    int i = extras.getInt("position");
                    PictureSelectorActivity.this.anim = true;
                    PictureSelectorActivity.this.adapter.bindSelectImages(parcelableArrayList);
                    PictureSelectorActivity.this.adapter.notifyItemChanged(i);
                    return;
                }
                return;
            }
            Bundle extras2 = intent.getExtras();
            if (extras2 != null) {
                ArrayList parcelableArrayList2 = extras2.getParcelableArrayList("selectImages");
                if (parcelableArrayList2.size() > 0) {
                    String mimeType = ((LocalMedia) parcelableArrayList2.get(0)).getMimeType();
                    if (PictureSelectorActivity.this.config.isCompress && PictureMimeType.eqImage(mimeType)) {
                        PictureSelectorActivity.this.compressImage(parcelableArrayList2);
                    } else {
                        PictureSelectorActivity.this.onResult(parcelableArrayList2);
                    }
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.picture_selector);
        BroadcastManager.getInstance(this).registerReceiver(this.commonBroadcastReceiver, BroadcastAction.ACTION_SELECTED_DATA, BroadcastAction.ACTION_PREVIEW_COMPRESSION);
        initView(bundle);
        initPictureSelectorStyle();
    }

    private void initView(Bundle bundle) {
        String string;
        this.titleViewBg = findViewById(R.id.titleViewBg);
        this.mIvPictureLeftBack = (ImageView) findViewById(R.id.picture_left_back);
        this.mTvPictureTitle = (TextView) findViewById(R.id.picture_title);
        this.mTvPictureRight = (TextView) findViewById(R.id.picture_right);
        this.mTvPictureOk = (TextView) findViewById(R.id.picture_tv_ok);
        this.mIvArrow = (ImageView) findViewById(R.id.ivArrow);
        this.mTvPicturePreview = (TextView) findViewById(R.id.picture_id_preview);
        this.mTvPictureImgNum = (TextView) findViewById(R.id.picture_tv_img_num);
        this.mPictureRecycler = (RecyclerView) findViewById(R.id.picture_recycler);
        this.mBottomLayout = (RelativeLayout) findViewById(R.id.rl_bottom);
        this.mTvEmpty = (TextView) findViewById(R.id.tv_empty);
        isNumComplete(this.numComplete);
        if (this.config.chooseMode == PictureMimeType.ofAll()) {
            PhotoPopupWindow photoPopupWindow = new PhotoPopupWindow(this);
            this.popupWindow = photoPopupWindow;
            photoPopupWindow.setOnItemClickListener(this);
        }
        this.mTvPicturePreview.setOnClickListener(this);
        int i = 8;
        if (this.config.chooseMode == PictureMimeType.ofAudio()) {
            this.mTvPicturePreview.setVisibility(8);
            this.audioH = ScreenUtils.getScreenHeight(this.mContext) + ScreenUtils.getStatusBarHeight(this.mContext);
        } else {
            this.mTvPicturePreview.setVisibility(this.config.chooseMode == PictureMimeType.ofVideo() ? 8 : 0);
        }
        this.mBottomLayout.setVisibility((this.config.selectionMode == 1 && this.config.isSingleDirectReturn) ? 0 : 0);
        this.mIvPictureLeftBack.setOnClickListener(this);
        this.mTvPictureRight.setOnClickListener(this);
        this.mTvPictureOk.setOnClickListener(this);
        this.mTvPictureImgNum.setOnClickListener(this);
        this.mTvPictureTitle.setOnClickListener(this);
        this.mTvPictureTitle.setText(getString(this.config.chooseMode == PictureMimeType.ofAudio() ? R.string.picture_all_audio : R.string.picture_camera_roll));
        FolderPopWindow folderPopWindow = new FolderPopWindow(this, this.config);
        this.folderWindow = folderPopWindow;
        folderPopWindow.setArrowImageView(this.mIvArrow);
        this.folderWindow.setOnItemClickListener(this);
        this.mPictureRecycler.setHasFixedSize(true);
        this.mPictureRecycler.addItemDecoration(new GridSpacingItemDecoration(this.config.imageSpanCount, ScreenUtils.dip2px(this, 2.0f), false));
        this.mPictureRecycler.setLayoutManager(new GridLayoutManager(this, this.config.imageSpanCount));
        ((SimpleItemAnimator) this.mPictureRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        this.mediaLoader = new LocalMediaLoader(this, this.config);
        if (PermissionChecker.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") && PermissionChecker.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            this.mHandler.sendEmptyMessage(0);
            readLocalMedia();
        } else {
            PermissionChecker.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        TextView textView = this.mTvEmpty;
        if (this.config.chooseMode == PictureMimeType.ofAudio()) {
            string = getString(R.string.picture_audio_empty);
        } else {
            string = getString(R.string.picture_empty);
        }
        textView.setText(string);
        StringUtils.tempTextFont(this.mTvEmpty, this.config.chooseMode);
        if (bundle != null) {
            this.selectionMedias = PictureSelector.obtainSelectorList(bundle);
        }
        PictureImageGridAdapter pictureImageGridAdapter = new PictureImageGridAdapter(this.mContext, this.config);
        this.adapter = pictureImageGridAdapter;
        pictureImageGridAdapter.setOnPhotoSelectChangedListener(this);
        this.adapter.bindSelectImages(this.selectionMedias);
        this.mPictureRecycler.setAdapter(this.adapter);
    }

    private void initPictureSelectorStyle() {
        if (this.config.style != null) {
            if (this.config.style.pictureTitleDownResId != 0) {
                this.mIvArrow.setImageDrawable(ContextCompat.getDrawable(this, this.config.style.pictureTitleDownResId));
            }
            if (this.config.style.pictureTitleTextColor != 0) {
                this.mTvPictureTitle.setTextColor(this.config.style.pictureTitleTextColor);
            }
            if (this.config.style.pictureCancelTextColor != 0) {
                this.mTvPictureRight.setTextColor(this.config.style.pictureCancelTextColor);
            }
            if (this.config.style.pictureLeftBackIcon != 0) {
                this.mIvPictureLeftBack.setImageResource(this.config.style.pictureLeftBackIcon);
            }
            if (this.config.style.pictureUnPreviewTextColor != 0) {
                this.mTvPicturePreview.setTextColor(this.config.style.pictureUnPreviewTextColor);
            }
            if (this.config.style.pictureCheckNumBgStyle != 0) {
                this.mTvPictureImgNum.setBackgroundResource(this.config.style.pictureCheckNumBgStyle);
            }
            if (this.config.style.pictureUnCompleteTextColor != 0) {
                this.mTvPictureOk.setTextColor(this.config.style.pictureUnCompleteTextColor);
            }
            if (this.config.style.pictureBottomBgColor != 0) {
                this.mBottomLayout.setBackgroundColor(this.config.style.pictureBottomBgColor);
            }
        } else if (this.config.downResId != 0) {
            this.mIvArrow.setImageDrawable(ContextCompat.getDrawable(this, this.config.downResId));
        }
        this.titleViewBg.setBackgroundColor(this.colorPrimary);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        PictureImageGridAdapter pictureImageGridAdapter = this.adapter;
        if (pictureImageGridAdapter != null) {
            PictureSelector.saveSelectorList(bundle, pictureImageGridAdapter.getSelectedImages());
        }
    }

    private void isNumComplete(boolean z) {
        String string;
        TextView textView = this.mTvPictureOk;
        if (z) {
            int i = R.string.picture_done_front_num;
            Object[] objArr = new Object[2];
            objArr[0] = 0;
            objArr[1] = Integer.valueOf(this.config.selectionMode == 1 ? 1 : this.config.maxSelectNum);
            string = getString(i, objArr);
        } else {
            string = getString(R.string.picture_please_select);
        }
        textView.setText(string);
        if (!z) {
            this.animation = AnimationUtils.loadAnimation(this, R.anim.picture_anim_modal_in);
        }
        this.animation = z ? null : AnimationUtils.loadAnimation(this, R.anim.picture_anim_modal_in);
    }

    protected void readLocalMedia() {
        this.mediaLoader.loadAllMedia();
        this.mediaLoader.setCompleteListener(new LocalMediaLoader.LocalMediaLoadListener() { // from class: com.luck.picture.lib.PictureSelectorActivity$$ExternalSyntheticLambda1
            @Override // com.luck.picture.lib.model.LocalMediaLoader.LocalMediaLoadListener
            public final void loadComplete(List list) {
                PictureSelectorActivity.this.m42x1b697bd1(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$readLocalMedia$0$com-luck-picture-lib-PictureSelectorActivity  reason: not valid java name */
    public /* synthetic */ void m42x1b697bd1(List list) {
        List<LocalMedia> list2;
        if (list.size() > 0) {
            this.foldersList = list;
            LocalMediaFolder localMediaFolder = (LocalMediaFolder) list.get(0);
            localMediaFolder.setChecked(true);
            List<LocalMedia> images = localMediaFolder.getImages();
            if (images.size() >= this.images.size()) {
                this.images = images;
                this.folderWindow.bindFolder(list);
            }
        }
        PictureImageGridAdapter pictureImageGridAdapter = this.adapter;
        if (pictureImageGridAdapter != null && (list2 = this.images) != null) {
            pictureImageGridAdapter.bindImagesData(list2);
            this.mTvEmpty.setVisibility(this.images.size() > 0 ? 4 : 0);
        }
        this.mHandler.sendEmptyMessage(1);
    }

    public void startCamera() {
        if (DoubleUtils.isFastDoubleClick()) {
            return;
        }
        int i = this.config.chooseMode;
        if (i == 0) {
            PhotoPopupWindow photoPopupWindow = this.popupWindow;
            if (photoPopupWindow != null) {
                if (photoPopupWindow.isShowing()) {
                    this.popupWindow.dismiss();
                }
                this.popupWindow.showAsDropDown(this.mTvPictureTitle);
                return;
            }
            startOpenCamera();
        } else if (i == 1) {
            startOpenCamera();
        } else if (i == 2) {
            startOpenCameraVideo();
        } else if (i != 3) {
        } else {
            startOpenCameraAudio();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String string;
        int id = view.getId();
        if (id == R.id.picture_left_back || id == R.id.picture_right) {
            if (this.folderWindow.isShowing()) {
                this.folderWindow.dismiss();
            } else {
                closeActivity();
            }
        }
        if (id == R.id.picture_title) {
            if (this.folderWindow.isShowing()) {
                this.folderWindow.dismiss();
            } else {
                List<LocalMedia> list = this.images;
                if (list != null && list.size() > 0) {
                    this.folderWindow.showAsDropDown(this.mTvPictureTitle);
                    this.folderWindow.notifyDataCheckedStatus(this.adapter.getSelectedImages());
                }
            }
        }
        if (id == R.id.picture_id_preview) {
            List<LocalMedia> selectedImages = this.adapter.getSelectedImages();
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
            int size = selectedImages.size();
            for (int i = 0; i < size; i++) {
                arrayList.add(selectedImages.get(i));
            }
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(PictureConfig.EXTRA_PREVIEW_SELECT_LIST, arrayList);
            bundle.putParcelableArrayList(PictureConfig.EXTRA_SELECT_LIST, (ArrayList) selectedImages);
            bundle.putBoolean(PictureConfig.EXTRA_BOTTOM_PREVIEW, true);
            JumpUtils.startPicturePreviewActivity(this.mContext, bundle, this.config.selectionMode == 1 ? 69 : UCropMulti.REQUEST_MULTI_CROP);
            overridePendingTransition((this.config.windowAnimationStyle == null || this.config.windowAnimationStyle.activityPreviewEnterAnimation == 0) ? R.anim.picture_anim_enter : this.config.windowAnimationStyle.activityPreviewEnterAnimation, R.anim.picture_anim_fade_in);
        }
        if (id == R.id.picture_tv_ok || id == R.id.picture_tv_img_num) {
            List<LocalMedia> selectedImages2 = this.adapter.getSelectedImages();
            LocalMedia localMedia = selectedImages2.size() > 0 ? selectedImages2.get(0) : null;
            String mimeType = localMedia != null ? localMedia.getMimeType() : "";
            int size2 = selectedImages2.size();
            boolean eqImage = PictureMimeType.eqImage(mimeType);
            if (this.config.minSelectNum > 0 && this.config.selectionMode == 2 && size2 < this.config.minSelectNum) {
                if (eqImage) {
                    string = getString(R.string.picture_min_img_num, new Object[]{Integer.valueOf(this.config.minSelectNum)});
                } else {
                    string = getString(R.string.picture_min_video_num, new Object[]{Integer.valueOf(this.config.minSelectNum)});
                }
                ToastUtils.s(this.mContext, string);
            } else if (this.config.enableCrop && eqImage) {
                if (this.config.selectionMode == 1) {
                    this.originalPath = localMedia.getPath();
                    startCrop(this.originalPath);
                    return;
                }
                ArrayList<CutInfo> arrayList2 = new ArrayList<>();
                int size3 = selectedImages2.size();
                for (int i2 = 0; i2 < size3; i2++) {
                    LocalMedia localMedia2 = selectedImages2.get(i2);
                    CutInfo cutInfo = new CutInfo();
                    cutInfo.setPath(localMedia2.getPath());
                    cutInfo.setImageWidth(localMedia2.getWidth());
                    cutInfo.setImageHeight(localMedia2.getHeight());
                    cutInfo.setMimeType(localMedia2.getMimeType());
                    if (SdkVersionUtils.checkedAndroid_Q()) {
                        cutInfo.setAndroidQToPath(localMedia2.getAndroidQToPath());
                    }
                    arrayList2.add(cutInfo);
                }
                startCrop(arrayList2);
            } else if (this.config.isCompress && eqImage) {
                compressImage(selectedImages2);
            } else {
                onResult(selectedImages2);
            }
        }
    }

    private void audioDialog(final String str) {
        PictureCustomDialog pictureCustomDialog = new PictureCustomDialog(this.mContext, -1, this.audioH, R.layout.picture_audio_dialog, R.style.Picture_Theme_Dialog);
        this.audioDialog = pictureCustomDialog;
        pictureCustomDialog.getWindow().setWindowAnimations(R.style.Picture_Theme_Dialog_AudioStyle);
        this.mTvMusicStatus = (TextView) this.audioDialog.findViewById(R.id.tv_musicStatus);
        this.mTvMusicTime = (TextView) this.audioDialog.findViewById(R.id.tv_musicTime);
        this.musicSeekBar = (SeekBar) this.audioDialog.findViewById(R.id.musicSeekBar);
        this.mTvMusicTotal = (TextView) this.audioDialog.findViewById(R.id.tv_musicTotal);
        this.mTvPlayPause = (TextView) this.audioDialog.findViewById(R.id.tv_PlayPause);
        this.mTvStop = (TextView) this.audioDialog.findViewById(R.id.tv_Stop);
        this.mTvQuit = (TextView) this.audioDialog.findViewById(R.id.tv_Quit);
        Handler handler = this.handler;
        if (handler != null) {
            handler.postDelayed(new Runnable() { // from class: com.luck.picture.lib.PictureSelectorActivity$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PictureSelectorActivity.this.m39x7601a95b(str);
                }
            }, 30L);
        }
        this.mTvPlayPause.setOnClickListener(new audioOnClick(str));
        this.mTvStop.setOnClickListener(new audioOnClick(str));
        this.mTvQuit.setOnClickListener(new audioOnClick(str));
        this.musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.luck.picture.lib.PictureSelectorActivity.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    PictureSelectorActivity.this.mediaPlayer.seekTo(i);
                }
            }
        });
        this.audioDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.luck.picture.lib.PictureSelectorActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                PictureSelectorActivity.this.m41xe8f0c499(str, dialogInterface);
            }
        });
        Handler handler2 = this.handler;
        if (handler2 != null) {
            handler2.post(this.runnable);
        }
        this.audioDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$audioDialog$3$com-luck-picture-lib-PictureSelectorActivity  reason: not valid java name */
    public /* synthetic */ void m41xe8f0c499(final String str, DialogInterface dialogInterface) {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacks(this.runnable);
        }
        new Handler().postDelayed(new Runnable() { // from class: com.luck.picture.lib.PictureSelectorActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                PictureSelectorActivity.this.m40x2f7936fa(str);
            }
        }, 30L);
        try {
            PictureCustomDialog pictureCustomDialog = this.audioDialog;
            if (pictureCustomDialog == null || !pictureCustomDialog.isShowing()) {
                return;
            }
            this.audioDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: initPlayer */
    public void m39x7601a95b(String str) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        this.mediaPlayer = mediaPlayer;
        try {
            mediaPlayer.setDataSource(str);
            this.mediaPlayer.prepare();
            this.mediaPlayer.setLooping(true);
            playAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes.dex */
    public class audioOnClick implements View.OnClickListener {
        private String path;

        public audioOnClick(String str) {
            this.path = str;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.tv_PlayPause) {
                PictureSelectorActivity.this.playAudio();
            }
            if (id == R.id.tv_Stop) {
                PictureSelectorActivity.this.mTvMusicStatus.setText(PictureSelectorActivity.this.getString(R.string.picture_stop_audio));
                PictureSelectorActivity.this.mTvPlayPause.setText(PictureSelectorActivity.this.getString(R.string.picture_play_audio));
                PictureSelectorActivity.this.m40x2f7936fa(this.path);
            }
            if (id != R.id.tv_Quit || PictureSelectorActivity.this.handler == null) {
                return;
            }
            PictureSelectorActivity.this.handler.postDelayed(new Runnable() { // from class: com.luck.picture.lib.PictureSelectorActivity$audioOnClick$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PictureSelectorActivity.audioOnClick.this.m43x5014c746();
                }
            }, 30L);
            try {
                if (PictureSelectorActivity.this.audioDialog != null && PictureSelectorActivity.this.audioDialog.isShowing()) {
                    PictureSelectorActivity.this.audioDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PictureSelectorActivity.this.handler.removeCallbacks(PictureSelectorActivity.this.runnable);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$onClick$0$com-luck-picture-lib-PictureSelectorActivity$audioOnClick  reason: not valid java name */
        public /* synthetic */ void m43x5014c746() {
            PictureSelectorActivity.this.m40x2f7936fa(this.path);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playAudio() {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            this.musicSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            this.musicSeekBar.setMax(this.mediaPlayer.getDuration());
        }
        if (this.mTvPlayPause.getText().toString().equals(getString(R.string.picture_play_audio))) {
            this.mTvPlayPause.setText(getString(R.string.picture_pause_audio));
            this.mTvMusicStatus.setText(getString(R.string.picture_play_audio));
            playOrPause();
        } else {
            this.mTvPlayPause.setText(getString(R.string.picture_play_audio));
            this.mTvMusicStatus.setText(getString(R.string.picture_pause_audio));
            playOrPause();
        }
        if (this.isPlayAudio) {
            return;
        }
        Handler handler = this.handler;
        if (handler != null) {
            handler.post(this.runnable);
        }
        this.isPlayAudio = true;
    }

    /* renamed from: stop */
    public void m40x2f7936fa(String str) {
        MediaPlayer mediaPlayer = this.mediaPlayer;
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                this.mediaPlayer.reset();
                this.mediaPlayer.setDataSource(str);
                this.mediaPlayer.prepare();
                this.mediaPlayer.seekTo(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void playOrPause() {
        try {
            MediaPlayer mediaPlayer = this.mediaPlayer;
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    this.mediaPlayer.pause();
                } else {
                    this.mediaPlayer.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.luck.picture.lib.adapter.PictureAlbumDirectoryAdapter.OnItemClickListener
    public void onItemClick(boolean z, String str, List<LocalMedia> list) {
        if (!this.config.isCamera) {
            z = false;
        }
        this.adapter.setShowCamera(z);
        this.mTvPictureTitle.setText(str);
        this.folderWindow.dismiss();
        this.adapter.bindImagesData(list);
        this.mPictureRecycler.smoothScrollToPosition(0);
    }

    @Override // com.luck.picture.lib.adapter.PictureImageGridAdapter.OnPhotoSelectChangedListener
    public void onTakePhoto() {
        if (PermissionChecker.checkSelfPermission(this, "android.permission.CAMERA")) {
            startCamera();
        } else {
            PermissionChecker.requestPermissions(this, new String[]{"android.permission.CAMERA"}, 2);
        }
    }

    @Override // com.luck.picture.lib.adapter.PictureImageGridAdapter.OnPhotoSelectChangedListener
    public void onChange(List<LocalMedia> list) {
        changeImageNumber(list);
    }

    @Override // com.luck.picture.lib.adapter.PictureImageGridAdapter.OnPhotoSelectChangedListener
    public void onPictureClick(LocalMedia localMedia, int i) {
        if (this.config.selectionMode == 1 && this.config.isSingleDirectReturn) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(localMedia);
            if (this.config.enableCrop) {
                this.adapter.bindSelectImages(arrayList);
                startCrop(localMedia.getPath());
                return;
            }
            handlerResult(arrayList);
            return;
        }
        startPreview(this.adapter.getImages(), i);
    }

    public void startPreview(List<LocalMedia> list, int i) {
        LocalMedia localMedia = list.get(i);
        String mimeType = localMedia.getMimeType();
        Bundle bundle = new Bundle();
        ArrayList arrayList = new ArrayList();
        if (PictureMimeType.eqVideo(mimeType)) {
            if (this.config.selectionMode == 1) {
                arrayList.add(localMedia);
                onResult(arrayList);
                return;
            }
            bundle.putString("video_path", localMedia.getPath());
            JumpUtils.startPictureVideoPlayActivity(this.mContext, bundle);
        } else if (PictureMimeType.eqAudio(mimeType)) {
            if (this.config.selectionMode == 1) {
                arrayList.add(localMedia);
                onResult(arrayList);
                return;
            }
            audioDialog(localMedia.getPath());
        } else {
            List<LocalMedia> selectedImages = this.adapter.getSelectedImages();
            ImagesObservable.getInstance().savePreviewMediaData(new ArrayList(list));
            bundle.putParcelableArrayList(PictureConfig.EXTRA_SELECT_LIST, (ArrayList) selectedImages);
            bundle.putInt("position", i);
            JumpUtils.startPicturePreviewActivity(this.mContext, bundle, this.config.selectionMode == 1 ? 69 : UCropMulti.REQUEST_MULTI_CROP);
            overridePendingTransition((this.config.windowAnimationStyle == null || this.config.windowAnimationStyle.activityPreviewEnterAnimation == 0) ? R.anim.picture_anim_enter : this.config.windowAnimationStyle.activityPreviewEnterAnimation, R.anim.picture_anim_fade_in);
        }
    }

    public void changeImageNumber(List<LocalMedia> list) {
        String mimeType = list.size() > 0 ? list.get(0).getMimeType() : "";
        int i = 8;
        if (this.config.chooseMode == PictureMimeType.ofAudio()) {
            this.mTvPicturePreview.setVisibility(8);
        } else {
            boolean eqVideo = PictureMimeType.eqVideo(mimeType);
            boolean z = this.config.chooseMode == 2;
            TextView textView = this.mTvPicturePreview;
            if (!eqVideo && !z) {
                i = 0;
            }
            textView.setVisibility(i);
        }
        if (list.size() != 0) {
            this.mTvPictureOk.setEnabled(true);
            this.mTvPictureOk.setSelected(true);
            if (this.config.style != null && this.config.style.pictureCompleteTextColor != 0) {
                this.mTvPictureOk.setTextColor(this.config.style.pictureCompleteTextColor);
            }
            this.mTvPicturePreview.setEnabled(true);
            this.mTvPicturePreview.setSelected(true);
            if (this.config.style != null && this.config.style.picturePreviewTextColor != 0) {
                this.mTvPicturePreview.setTextColor(this.config.style.picturePreviewTextColor);
            }
            if (this.numComplete) {
                TextView textView2 = this.mTvPictureOk;
                int i2 = R.string.picture_done_front_num;
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(list.size());
                objArr[1] = Integer.valueOf(this.config.selectionMode == 1 ? 1 : this.config.maxSelectNum);
                textView2.setText(getString(i2, objArr));
                return;
            }
            if (!this.anim) {
                this.mTvPictureImgNum.startAnimation(this.animation);
            }
            this.mTvPictureImgNum.setVisibility(0);
            this.mTvPictureImgNum.setText(String.valueOf(list.size()));
            this.mTvPictureOk.setText(getString(R.string.picture_completed));
            this.anim = false;
            return;
        }
        this.mTvPictureOk.setEnabled(false);
        this.mTvPictureOk.setSelected(false);
        if (this.config.style != null && this.config.style.pictureUnCompleteTextColor != 0) {
            this.mTvPictureOk.setTextColor(this.config.style.pictureUnCompleteTextColor);
        }
        this.mTvPicturePreview.setEnabled(false);
        this.mTvPicturePreview.setSelected(false);
        if (this.config.style != null && this.config.style.pictureUnPreviewTextColor != 0) {
            this.mTvPicturePreview.setTextColor(this.config.style.pictureUnPreviewTextColor);
        }
        if (this.numComplete) {
            TextView textView3 = this.mTvPictureOk;
            int i3 = R.string.picture_done_front_num;
            Object[] objArr2 = new Object[2];
            objArr2[0] = 0;
            objArr2[1] = Integer.valueOf(this.config.selectionMode == 1 ? 1 : this.config.maxSelectNum);
            textView3.setText(getString(i3, objArr2));
            return;
        }
        this.mTvPictureImgNum.setVisibility(4);
        this.mTvPictureOk.setText(getString(R.string.picture_please_select));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            if (i2 == 96) {
                ToastUtils.s(this.mContext, ((Throwable) intent.getSerializableExtra(UCrop.EXTRA_ERROR)).getMessage());
            }
        } else if (i == 69) {
            singleCropHandleResult(intent);
        } else if (i == 609) {
            multiCropHandleResult(intent);
        } else if (i != 909) {
        } else {
            requestCamera(intent);
        }
    }

    private void cameraHandleResult(LocalMedia localMedia, String str) {
        boolean eqImage = PictureMimeType.eqImage(str);
        if (this.config.enableCrop && eqImage) {
            this.originalPath = this.cameraPath;
            startCrop(this.cameraPath);
        } else if (this.config.isCompress && eqImage) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(localMedia);
            compressImage(arrayList);
            this.images.add(0, localMedia);
            this.adapter.bindSelectImages(arrayList);
            this.adapter.notifyDataSetChanged();
        } else {
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(localMedia);
            onResult(arrayList2);
        }
    }

    private void requestCamera(Intent intent) {
        long j;
        String fileToType;
        int[] localVideoSize;
        boolean checkedAndroid_Q = SdkVersionUtils.checkedAndroid_Q();
        int i = this.config.chooseMode;
        int ofAudio = PictureMimeType.ofAudio();
        long j2 = 0;
        String str = PictureMimeType.MIME_TYPE_AUDIO;
        if (i == ofAudio) {
            this.cameraPath = getAudioPath(intent);
            if (TextUtils.isEmpty(this.cameraPath)) {
                return;
            }
            if (checkedAndroid_Q) {
                j = MediaUtils.extractDuration(this.mContext, true, this.cameraPath);
            } else {
                j = MediaUtils.extractDuration(this.mContext, false, this.cameraPath);
            }
        } else {
            str = null;
            j = 0;
        }
        if (TextUtils.isEmpty(this.cameraPath)) {
            return;
        }
        new File(this.cameraPath);
        int[] iArr = new int[2];
        File file = new File(this.cameraPath);
        if (!checkedAndroid_Q) {
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        }
        LocalMedia localMedia = new LocalMedia();
        if (this.config.chooseMode != PictureMimeType.ofAudio()) {
            if (checkedAndroid_Q) {
                File file2 = new File(PictureFileUtils.getPath(getApplicationContext(), Uri.parse(this.cameraPath)));
                j2 = file2.length();
                fileToType = PictureMimeType.fileToType(file2);
                if (PictureMimeType.eqImage(fileToType)) {
                    localMedia.setAndroidQToPath(PictureFileUtils.rotateImageToAndroidQ(this, PictureFileUtils.readPictureDegree(this, this.cameraPath), this.cameraPath));
                    localVideoSize = MediaUtils.getLocalImageSizeToAndroidQ(this, this.cameraPath);
                } else {
                    localVideoSize = MediaUtils.getLocalVideoSize(this, Uri.parse(this.cameraPath));
                    j = MediaUtils.extractDuration(this.mContext, true, this.cameraPath);
                }
            } else {
                fileToType = PictureMimeType.fileToType(file);
                j2 = new File(this.cameraPath).length();
                if (PictureMimeType.eqImage(fileToType)) {
                    PictureFileUtils.rotateImage(PictureFileUtils.readPictureDegree(this, this.cameraPath), this.cameraPath);
                    localVideoSize = MediaUtils.getLocalImageWidthOrHeight(this.cameraPath);
                } else {
                    localVideoSize = MediaUtils.getLocalVideoSize(this.cameraPath);
                    j = MediaUtils.extractDuration(this.mContext, false, this.cameraPath);
                }
            }
            str = fileToType;
            iArr = localVideoSize;
            boolean eqImage = PictureMimeType.eqImage(str);
            int lastImageId = MediaUtils.getLastImageId(this, eqImage);
            if (lastImageId != -1) {
                removeImage(lastImageId, eqImage);
            }
        }
        localMedia.setDuration(j);
        localMedia.setWidth(iArr[0]);
        localMedia.setHeight(iArr[1]);
        localMedia.setPath(this.cameraPath);
        localMedia.setMimeType(str);
        localMedia.setSize(j2);
        localMedia.setChooseModel(this.config.chooseMode);
        if (this.adapter != null) {
            if (this.config.selectionMode == 1) {
                if (this.config.isSingleDirectReturn) {
                    cameraHandleResult(localMedia, str);
                } else {
                    this.images.add(0, localMedia);
                    List<LocalMedia> selectedImages = this.adapter.getSelectedImages();
                    if (PictureMimeType.isMimeTypeSame(selectedImages.size() > 0 ? selectedImages.get(0).getMimeType() : "", localMedia.getMimeType()) || selectedImages.size() == 0) {
                        singleRadioMediaImage();
                        selectedImages.add(localMedia);
                        this.adapter.bindSelectImages(selectedImages);
                    }
                }
            } else {
                this.images.add(0, localMedia);
                List<LocalMedia> selectedImages2 = this.adapter.getSelectedImages();
                if (selectedImages2.size() < this.config.maxSelectNum) {
                    if ((PictureMimeType.isMimeTypeSame(selectedImages2.size() > 0 ? selectedImages2.get(0).getMimeType() : "", localMedia.getMimeType()) || selectedImages2.size() == 0) && selectedImages2.size() < this.config.maxSelectNum) {
                        selectedImages2.add(localMedia);
                        this.adapter.bindSelectImages(selectedImages2);
                    }
                } else {
                    ToastUtils.s(this, StringUtils.getToastMsg(this, str, this.config.maxSelectNum));
                }
            }
            this.adapter.notifyDataSetChanged();
            manualSaveFolder(localMedia);
            this.mTvEmpty.setVisibility(this.images.size() > 0 ? 4 : 0);
        }
    }

    private void singleCropHandleResult(Intent intent) {
        ArrayList arrayList = new ArrayList();
        String path = UCrop.getOutput(intent).getPath();
        PictureImageGridAdapter pictureImageGridAdapter = this.adapter;
        if (pictureImageGridAdapter != null) {
            List<LocalMedia> selectedImages = pictureImageGridAdapter.getSelectedImages();
            LocalMedia localMedia = (selectedImages == null || selectedImages.size() <= 0) ? null : selectedImages.get(0);
            if (localMedia != null) {
                this.originalPath = localMedia.getPath();
                localMedia.setCutPath(path);
                localMedia.setSize(new File(path).length());
                localMedia.setChooseModel(this.config.chooseMode);
                localMedia.setCut(true);
                localMedia.setMimeType(PictureMimeType.getImageMimeType(path));
                if (SdkVersionUtils.checkedAndroid_Q()) {
                    if (!TextUtils.isEmpty(localMedia.getAndroidQToPath())) {
                        path = localMedia.getAndroidQToPath();
                    }
                    localMedia.setAndroidQToPath(path);
                }
                arrayList.add(localMedia);
                handlerResult(arrayList);
            }
        }
    }

    private void singleRadioMediaImage() {
        List<LocalMedia> selectedImages = this.adapter.getSelectedImages();
        if (selectedImages == null || selectedImages.size() <= 0) {
            return;
        }
        selectedImages.clear();
    }

    private void manualSaveFolder(LocalMedia localMedia) {
        try {
            createNewFolder(this.foldersList);
            LocalMediaFolder imageFolder = getImageFolder(localMedia.getPath(), this.foldersList);
            LocalMediaFolder localMediaFolder = this.foldersList.size() > 0 ? this.foldersList.get(0) : null;
            if (localMediaFolder == null || imageFolder == null) {
                return;
            }
            localMediaFolder.setFirstImagePath(localMedia.getPath());
            localMediaFolder.setImages(this.images);
            localMediaFolder.setImageNum(localMediaFolder.getImageNum() + 1);
            imageFolder.setImageNum(imageFolder.getImageNum() + 1);
            imageFolder.getImages().add(0, localMedia);
            imageFolder.setFirstImagePath(this.cameraPath);
            this.folderWindow.bindFolder(this.foldersList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        Handler handler;
        super.onDestroy();
        if (this.commonBroadcastReceiver != null) {
            BroadcastManager.getInstance(this).unregisterReceiver(this.commonBroadcastReceiver, BroadcastAction.ACTION_SELECTED_DATA, BroadcastAction.ACTION_PREVIEW_COMPRESSION);
        }
        Animation animation = this.animation;
        if (animation != null) {
            animation.cancel();
            this.animation = null;
        }
        if (this.mediaPlayer == null || (handler = this.handler) == null) {
            return;
        }
        handler.removeCallbacks(this.runnable);
        this.mediaPlayer.release();
        this.mediaPlayer = null;
    }

    @Override // com.luck.picture.lib.widget.PhotoPopupWindow.OnItemClickListener
    public void onItemClick(int i) {
        if (i == 0) {
            startOpenCamera();
        } else if (i != 1) {
        } else {
            startOpenCameraVideo();
        }
    }

    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1) {
            if (i != 2) {
                return;
            }
            if (iArr[0] == 0) {
                onTakePhoto();
                return;
            } else {
                ToastUtils.s(this.mContext, getString(R.string.picture_camera));
                return;
            }
        }
        for (int i2 : iArr) {
            if (i2 == 0) {
                this.mHandler.sendEmptyMessage(0);
                readLocalMedia();
            } else {
                ToastUtils.s(this.mContext, getString(R.string.picture_jurisdiction));
                onBackPressed();
            }
        }
    }
}
