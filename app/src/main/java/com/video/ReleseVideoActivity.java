package com.video;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.FirstActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.pictureselector.FullyGridLayoutManager;
import com.common.pictureselector.GlideEngine;
import com.common.pictureselector.adapter.GridImageAdapter;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
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
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.ToastUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class ReleseVideoActivity extends LedBleActivity implements View.OnClickListener {
    private static final String TAG = "ReleseVideoActivity";
    private GridImageAdapter adapter;
    Button btn_Publish;
    TextView clear2;
    EditText etTitle12;
    private File fileVideo;
    ImageView ivBack12;
    private int language;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureParameterStyle mPictureParameterStyle;
    File newfile;
    private Map<String, String> params;
    private RecyclerView recyclerView;
    private int themeId;
    private int index = 0;
    private Map<String, File> fileMap = new HashMap();
    private ArrayList<String> selectedVedioPaths = new ArrayList<>();
    private int maxSelectNum = 1;
    private List<LocalMedia> selectList = new ArrayList();
    private int chooseMode = PictureMimeType.ofVideo();
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() { // from class: com.video.ReleseVideoActivity.2
        @Override // com.common.pictureselector.adapter.GridImageAdapter.onAddPicClickListener
        public void onAddPicClick() {
            PictureSelector.create(ReleseVideoActivity.this).openGallery(ReleseVideoActivity.this.chooseMode).loadImageEngine(GlideEngine.createGlideEngine()).theme(ReleseVideoActivity.this.themeId).setPictureStyle(ReleseVideoActivity.this.mPictureParameterStyle).setPictureCropStyle(ReleseVideoActivity.this.mCropParameterStyle).maxSelectNum(ReleseVideoActivity.this.maxSelectNum).minSelectNum(1).imageSpanCount(4).cameraFileName("").selectionMode(2).isSingleDirectReturn(false).previewImage(true).previewVideo(true).enablePreviewAudio(true).isCamera(true).isZoomAnim(true).enableCrop(false).compress(true).compressQuality(0).synOrAsy(true).isGif(false).openClickSound(false).selectionMedia(ReleseVideoActivity.this.selectList).cutOutQuality(10).minimumCompressSize(100).videoQuality(1).recordVideoSecond(10).forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.video.ReleseVideoActivity.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals(BroadcastAction.ACTION_DELETE_PREVIEW_POSITION)) {
                int i = intent.getExtras().getInt("position");
                ToastUtils.s(context, "delete image index:" + i);
                if (i < ReleseVideoActivity.this.adapter.getItemCount()) {
                    ReleseVideoActivity.this.selectList.remove(i);
                    ReleseVideoActivity.this.adapter.notifyItemRemoved(i);
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_relese_video);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        this.ivBack12 = (ImageView) findViewById(R.id.ivBack12);
        this.btn_Publish = (Button) findViewById(R.id.btn_Publish);
        this.etTitle12 = (EditText) findViewById(R.id.etTitle12);
        TextView textView = (TextView) findViewById(R.id.clear2);
        this.clear2 = textView;
        textView.setOnClickListener(this);
        this.ivBack12.setOnClickListener(this);
        this.btn_Publish.setOnClickListener(this);
        this.fileMap.clear();
        this.params = new HashMap();
        getDefaultStyle();
        this.themeId = 2131821094;
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler);
        this.recyclerView.setLayoutManager(new FullyGridLayoutManager(this, 4, 1, false));
        GridImageAdapter gridImageAdapter = new GridImageAdapter(this, this.onAddPicClickListener);
        this.adapter = gridImageAdapter;
        gridImageAdapter.setList(this.selectList);
        this.adapter.setSelectMax(this.maxSelectNum);
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() { // from class: com.video.ReleseVideoActivity$$ExternalSyntheticLambda0
            @Override // com.common.pictureselector.adapter.GridImageAdapter.OnItemClickListener
            public final void onItemClick(int i, View view) {
                ReleseVideoActivity.this.m66lambda$onCreate$0$comvideoReleseVideoActivity(i, view);
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
    /* renamed from: lambda$onCreate$0$com-video-ReleseVideoActivity  reason: not valid java name */
    public /* synthetic */ void m66lambda$onCreate$0$comvideoReleseVideoActivity(int i, View view) {
        if (this.selectList.size() > 0) {
            LocalMedia localMedia = this.selectList.get(i);
            int mimeType = PictureMimeType.getMimeType(localMedia.getMimeType());
            if (mimeType == 2) {
                if (SdkVersionUtils.checkedAndroid_Q()) {
                    PictureSelector.create(this).externalPictureVideo(localMedia.getAndroidQToPath());
                } else {
                    PictureSelector.create(this).externalPictureVideo(localMedia.getPath());
                }
            } else if (mimeType == 3) {
                if (SdkVersionUtils.checkedAndroid_Q()) {
                    PictureSelector.create(this).externalPictureAudio(localMedia.getAndroidQToPath());
                } else {
                    PictureSelector.create(this).externalPictureAudio(localMedia.getPath());
                }
            } else {
                PictureSelector.create(this).setPictureStyle(this.mPictureParameterStyle).isNotPreviewDownload(true).loadImageEngine(GlideEngine.createGlideEngine()).openExternalPreview(i, this.selectList);
            }
        }
    }

    public void uploading() {
        HttpUtil.getInstance().uploadFile(true, this, Constant.uploadShortVideo, this.params, this.fileMap, new HttpUtil.HttpCallBack() { // from class: com.video.ReleseVideoActivity.1
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.video.ReleseVideoActivity.1.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                Toast.makeText(ReleseVideoActivity.this.getApplicationContext(), (int) R.string.comment_publish_success, 0).show();
                ReleseVideoActivity.this.setResult(FirstActivity.RESULT333);
                ReleseVideoActivity.this.finish();
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Toast.makeText(ReleseVideoActivity.this.getApplicationContext(), (int) R.string.request_failed, 0).show();
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_Publish) {
            if (!LedBleActivity.getBaseApp().getUserToken().equals("")) {
                commit();
            } else {
                Toast.makeText(this, getString(R.string.loginapp), 1).show();
            }
        } else if (id != R.id.clear2) {
            if (id != R.id.ivBack12) {
                return;
            }
            finish();
        } else {
            this.etTitle12.setText("");
            this.params.clear();
            this.fileVideo = null;
            this.selectedVedioPaths.clear();
            this.fileMap.clear();
        }
    }

    public void commit() {
        String str;
        if (!TextUtils.isEmpty(this.etTitle12.getText())) {
            File file = this.fileVideo;
            if (file != null) {
                if (file.length() / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED <= 50) {
                    if (getResources().getString(R.string.home).equalsIgnoreCase("首页")) {
                        str = "cn";
                    } else {
                        str = getResources().getString(R.string.home).equalsIgnoreCase("Home") ? "en" : "dmx02_other";
                    }
                    this.params.put(FFmpegMediaMetadataRetriever.METADATA_KEY_LANGUAGE, str);
                    this.params.put(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE, this.etTitle12.getText().toString());
                    this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
                    this.fileMap.put(PictureMimeType.MIME_TYPE_PREFIX_VIDEO, this.fileVideo);
                    uploading();
                    return;
                }
                Toast.makeText(this, (int) R.string.filesize, 0).show();
                return;
            }
            Toast.makeText(this, (int) R.string.chose, 0).show();
            return;
        }
        Toast.makeText(this, (int) R.string.tiele, 0).show();
    }

    private void savePic(Bitmap bitmap, String str) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finish();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 188) {
            List<LocalMedia> obtainMultipleResult = PictureSelector.obtainMultipleResult(intent);
            this.selectList = obtainMultipleResult;
            for (LocalMedia localMedia : obtainMultipleResult) {
                Log.i(TAG, "压缩---->" + localMedia.getCompressPath());
                Log.i(TAG, "原图---->" + localMedia.getPath());
                Log.i(TAG, "裁剪---->" + localMedia.getCutPath());
                Log.i(TAG, "Android Q 特有Path---->" + localMedia.getAndroidQToPath());
            }
            this.adapter.setList(this.selectList);
            this.adapter.notifyDataSetChanged();
            if (SdkVersionUtils.checkedAndroid_Q()) {
                this.fileVideo = new File(this.selectList.get(0).getAndroidQToPath());
                try {
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(this.selectList.get(0).getAndroidQToPath());
                    savePic(mediaMetadataRetriever.getFrameAtTime(), "/mnt/sdcard/Pictures/short.png");
                    this.newfile = new File("/mnt/sdcard/Pictures/short.png");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            this.fileVideo = new File(this.selectList.get(0).getPath());
            try {
                MediaMetadataRetriever mediaMetadataRetriever2 = new MediaMetadataRetriever();
                mediaMetadataRetriever2.setDataSource(this.selectList.get(0).getPath());
                savePic(mediaMetadataRetriever2.getFrameAtTime(), "/mnt/sdcard/Pictures/short.png");
                this.newfile = new File("/mnt/sdcard/Pictures/short.png");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.broadcastReceiver != null) {
            BroadcastManager.getInstance(this).unregisterReceiver(this.broadcastReceiver, BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
        }
    }
}
