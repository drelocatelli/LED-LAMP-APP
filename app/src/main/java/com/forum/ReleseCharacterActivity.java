package com.forum;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.forum.login.LogInActivity;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.home.utils.ImageUtils;
import com.home.utils.Utils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import wseemann.media.FFmpegMediaMetadataRetriever;

/* loaded from: classes.dex */
public class ReleseCharacterActivity extends LedBleActivity implements View.OnClickListener {
    private static final String TAG = "ReleseCharacterActivity";
    private GridImageAdapter adapter;
    Button btn_Publish1;
    TextView clear1;
    EditText et_Content;
    EditText et_Title;
    ImageView iv_Back;
    private int language;
    private PictureCropParameterStyle mCropParameterStyle;
    private PictureParameterStyle mPictureParameterStyle;
    private String parameters;
    private Map<String, String> params;
    private RecyclerView recyclerView;
    private String replyId;
    private String replyType;
    private String target;
    private int themeId;
    TextView tv_Title;
    private String url;
    private int index = 0;
    private Map<String, File> files = new HashMap();
    private List<File> fileList = new ArrayList();
    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList();
    private int chooseMode = PictureMimeType.ofImage();
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() { // from class: com.forum.ReleseCharacterActivity.2
        @Override // com.common.pictureselector.adapter.GridImageAdapter.onAddPicClickListener
        public void onAddPicClick() {
            PictureSelector.create(ReleseCharacterActivity.this).openGallery(ReleseCharacterActivity.this.chooseMode).loadImageEngine(GlideEngine.createGlideEngine()).theme(ReleseCharacterActivity.this.themeId).setPictureStyle(ReleseCharacterActivity.this.mPictureParameterStyle).setPictureCropStyle(ReleseCharacterActivity.this.mCropParameterStyle).maxSelectNum(ReleseCharacterActivity.this.maxSelectNum).minSelectNum(1).imageSpanCount(4).cameraFileName("").selectionMode(2).isSingleDirectReturn(false).previewImage(true).previewVideo(true).enablePreviewAudio(true).isCamera(true).isZoomAnim(true).enableCrop(false).compress(false).compressQuality(0).synOrAsy(true).isGif(false).openClickSound(false).selectionMedia(ReleseCharacterActivity.this.selectList).cutOutQuality(90).minimumCompressSize(1).forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.forum.ReleseCharacterActivity.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals(BroadcastAction.ACTION_DELETE_PREVIEW_POSITION)) {
                int i = intent.getExtras().getInt("position");
                ToastUtils.s(context, "delete image index:" + i);
                if (i < ReleseCharacterActivity.this.adapter.getItemCount()) {
                    ReleseCharacterActivity.this.selectList.remove(i);
                    ReleseCharacterActivity.this.adapter.notifyItemRemoved(i);
                }
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_relese_character);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        init();
    }

    public void init() {
        this.et_Content = (EditText) findViewById(R.id.et_Content);
        this.et_Title = (EditText) findViewById(R.id.et_Title);
        this.btn_Publish1 = (Button) findViewById(R.id.btn_Publish1);
        this.iv_Back = (ImageView) findViewById(R.id.iv_Back);
        this.tv_Title = (TextView) findViewById(R.id.tv_Title);
        TextView textView = (TextView) findViewById(R.id.clear1);
        this.clear1 = textView;
        textView.setOnClickListener(this);
        this.btn_Publish1.setOnClickListener(this);
        this.iv_Back.setOnClickListener(this);
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
        this.adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() { // from class: com.forum.ReleseCharacterActivity$$ExternalSyntheticLambda0
            @Override // com.common.pictureselector.adapter.GridImageAdapter.OnItemClickListener
            public final void onItemClick(int i, View view) {
                ReleseCharacterActivity.this.m12lambda$init$0$comforumReleseCharacterActivity(i, view);
            }
        });
        if (PermissionChecker.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
        } else {
            PermissionChecker.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
        BroadcastManager.getInstance(this).registerReceiver(this.broadcastReceiver, BroadcastAction.ACTION_DELETE_PREVIEW_POSITION);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.replyId = extras.getString("replyId");
            this.replyType = extras.getString("replyType");
            this.target = extras.getString("target");
            this.parameters = extras.getString("ReplyActivity");
            if (!TextUtils.isEmpty(this.replyId)) {
                this.et_Title.setVisibility(8);
            }
            if (this.replyType.equals("reply")) {
                this.recyclerView.setVisibility(8);
                this.tv_Title.setText(R.string.reply);
                return;
            }
            this.recyclerView.setVisibility(8);
            this.tv_Title.setText(R.string.comment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$init$0$com-forum-ReleseCharacterActivity  reason: not valid java name */
    public /* synthetic */ void m12lambda$init$0$comforumReleseCharacterActivity(int i, View view) {
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

    public void replyContent() {
        if (TextUtils.isEmpty(this.et_Content.getText())) {
            Toast.makeText(this, (int) R.string.text, 0).show();
            return;
        }
        this.url = Constant.publishReplyByApp;
        this.params.put("replyId", this.replyId);
        this.params.put("replyType", this.replyType);
        this.params.put("target", this.target);
        this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        this.params.put(Utils.RESPONSE_CONTENT, this.et_Content.getText().toString());
        this.files.clear();
        for (int i = 0; i < this.fileList.size(); i++) {
            File file = this.fileList.get(i);
            this.files.put(file.getName(), file);
        }
        uploading();
    }

    public void publishContent() {
        String str;
        if (TextUtils.isEmpty(this.et_Title.getText())) {
            Toast.makeText(this, (int) R.string.tiele, 0).show();
        } else if (!TextUtils.isEmpty(this.et_Content.getText())) {
            if (getResources().getString(R.string.home).equalsIgnoreCase("首页")) {
                str = "cn";
            } else {
                str = getResources().getString(R.string.home).equalsIgnoreCase("Home") ? "en" : "dmx02_other";
            }
            this.url = Constant.publishCommentByApp;
            this.params.put(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE, this.et_Title.getText().toString());
            this.params.put(Utils.RESPONSE_CONTENT, this.et_Content.getText().toString());
            this.params.put(FFmpegMediaMetadataRetriever.METADATA_KEY_LANGUAGE, str);
            this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
            this.files.clear();
            for (int i = 0; i < this.fileList.size(); i++) {
                File file = this.fileList.get(i);
                this.files.put(file.getName(), file);
            }
            uploading();
        } else {
            Toast.makeText(this, (int) R.string.text, 0).show();
        }
    }

    public void uploading() {
        HttpUtil.getInstance().uploadFile(true, this, this.url, this.params, this.files, new HttpUtil.HttpCallBack() { // from class: com.forum.ReleseCharacterActivity.1
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.forum.ReleseCharacterActivity.1.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                Toast.makeText(ReleseCharacterActivity.this.getApplicationContext(), (int) R.string.comment_publish_success, 0).show();
                ReleseCharacterActivity.this.setResult(FirstActivity.RESULT222);
                ReleseCharacterActivity.this.finish();
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Toast.makeText(ReleseCharacterActivity.this.getApplicationContext(), (int) R.string.request_failed, 0).show();
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_Publish1) {
            if (LedBleActivity.getBaseApp().getUserToken().equals("")) {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            } else if (!TextUtils.isEmpty(this.replyId)) {
                replyContent();
            } else {
                publishContent();
            }
        } else if (id != R.id.clear1) {
            if (id != R.id.iv_Back) {
                return;
            }
            finish();
        } else {
            this.et_Title.setText("");
            this.et_Content.setText("");
            this.fileList.clear();
            this.params.clear();
        }
    }

    private void compressPhoto(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        new ScalPhotoTask(str).execute("");
    }

    /* loaded from: classes.dex */
    public class ScalPhotoTask extends AsyncTask<String, Void, File> {
        private String picturePath;

        public ScalPhotoTask(String str) {
            this.picturePath = str;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public File doInBackground(String... strArr) {
            File file = new File(this.picturePath);
            return ImageUtils.scalFile(file, file.getName());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(File file) {
            super.onPostExecute((ScalPhotoTask) file);
            if (file != null) {
                ReleseCharacterActivity.this.fileList.add(ReleseCharacterActivity.this.index, file);
            }
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

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 188) {
            return;
        }
        this.selectList = PictureSelector.obtainMultipleResult(intent);
        for (int i3 = 0; i3 < this.selectList.size(); i3++) {
            if (SdkVersionUtils.checkedAndroid_Q()) {
                compressPhoto(this.selectList.get(i3).getAndroidQToPath());
            } else {
                compressPhoto(this.selectList.get(i3).getPath());
            }
        }
        for (LocalMedia localMedia : this.selectList) {
            Log.i(TAG, "压缩---->" + localMedia.getCompressPath());
            Log.i(TAG, "原图---->" + localMedia.getPath());
            Log.i(TAG, "裁剪---->" + localMedia.getCutPath());
            Log.i(TAG, "Android Q 特有Path---->" + localMedia.getAndroidQToPath());
        }
        this.adapter.setList(this.selectList);
        this.adapter.notifyDataSetChanged();
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
