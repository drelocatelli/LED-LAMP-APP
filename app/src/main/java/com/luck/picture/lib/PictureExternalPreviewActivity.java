package com.luck.picture.lib;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.luck.picture.lib.PictureExternalPreviewActivity;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.dialog.PictureCustomDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.photoview.OnViewTapListener;
import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.SdkVersionUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.luck.picture.lib.widget.PreviewViewPager;
import com.luck.picture.lib.widget.longimage.ImageSource;
import com.luck.picture.lib.widget.longimage.ImageViewState;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PictureExternalPreviewActivity extends PictureBaseActivity implements View.OnClickListener {
    private SimpleFragmentAdapter adapter;
    private String downloadPath;
    private ImageButton ibDelete;
    private ImageButton ibLeftBack;
    private LayoutInflater inflater;
    private loadDataThread loadDataThread;
    private String mimeType;
    private TextView tvTitle;
    private PreviewViewPager viewPager;
    private List<LocalMedia> images = new ArrayList();
    private int position = 0;
    private Handler handler = new Handler() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what != 200) {
                return;
            }
            Context context = PictureExternalPreviewActivity.this.mContext;
            ToastUtils.s(context, PictureExternalPreviewActivity.this.getString(R.string.picture_save_success) + "\n" + ((String) message.obj));
            PictureExternalPreviewActivity.this.dismissDialog();
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.picture_activity_external_preview);
        this.inflater = LayoutInflater.from(this);
        this.tvTitle = (TextView) findViewById(R.id.picture_title);
        this.ibLeftBack = (ImageButton) findViewById(R.id.left_back);
        this.ibDelete = (ImageButton) findViewById(R.id.ib_delete);
        this.viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);
        int i = 0;
        this.position = getIntent().getIntExtra("position", 0);
        this.images = (List) getIntent().getSerializableExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST);
        this.ibLeftBack.setOnClickListener(this);
        this.ibDelete.setOnClickListener(this);
        this.ibDelete.setVisibility((this.config.style == null || !this.config.style.pictureExternalPreviewGonePreviewDelete) ? 8 : 8);
        initViewPageAdapterData();
        initPictureSelectorStyle();
    }

    private void initPictureSelectorStyle() {
        if (this.config.style != null) {
            if (this.config.style.pictureTitleTextColor != 0) {
                this.tvTitle.setTextColor(this.config.style.pictureTitleTextColor);
            }
            if (this.config.style.pictureLeftBackIcon != 0) {
                this.ibLeftBack.setImageResource(this.config.style.pictureLeftBackIcon);
            }
            if (this.config.style.pictureExternalPreviewDeleteStyle != 0) {
                this.ibDelete.setImageResource(this.config.style.pictureExternalPreviewDeleteStyle);
            }
        }
        this.tvTitle.setBackgroundColor(this.colorPrimary);
    }

    private void initViewPageAdapterData() {
        this.tvTitle.setText(getString(R.string.picture_preview_image_num, new Object[]{Integer.valueOf(this.position + 1), Integer.valueOf(this.images.size())}));
        SimpleFragmentAdapter simpleFragmentAdapter = new SimpleFragmentAdapter();
        this.adapter = simpleFragmentAdapter;
        this.viewPager.setAdapter(simpleFragmentAdapter);
        this.viewPager.setCurrentItem(this.position);
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity.1
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                PictureExternalPreviewActivity.this.tvTitle.setText(PictureExternalPreviewActivity.this.getString(R.string.picture_preview_image_num, new Object[]{Integer.valueOf(i + 1), Integer.valueOf(PictureExternalPreviewActivity.this.images.size())}));
                PictureExternalPreviewActivity.this.position = i;
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        List<LocalMedia> list;
        int id = view.getId();
        if (id == R.id.left_back) {
            finish();
            exitAnimation();
        } else if (id != R.id.ib_delete || (list = this.images) == null || list.size() <= 0) {
        } else {
            int currentItem = this.viewPager.getCurrentItem();
            this.images.remove(currentItem);
            Bundle bundle = new Bundle();
            bundle.putInt("position", currentItem);
            BroadcastManager.getInstance(this).action(BroadcastAction.ACTION_DELETE_PREVIEW_POSITION).extras(bundle).broadcast();
            if (this.images.size() == 0) {
                onBackPressed();
                return;
            }
            this.tvTitle.setText(getString(R.string.picture_preview_image_num, new Object[]{Integer.valueOf(this.position + 1), Integer.valueOf(this.images.size())}));
            this.position = currentItem;
            this.adapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes.dex */
    public class SimpleFragmentAdapter extends PagerAdapter {
        @Override // androidx.viewpager.widget.PagerAdapter
        public int getItemPosition(Object obj) {
            return -2;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        public SimpleFragmentAdapter() {
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            if (PictureExternalPreviewActivity.this.images != null) {
                return PictureExternalPreviewActivity.this.images.size();
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
            View inflate = PictureExternalPreviewActivity.this.inflater.inflate(R.layout.picture_image_preview, viewGroup, false);
            PhotoView photoView = (PhotoView) inflate.findViewById(R.id.preview_image);
            SubsamplingScaleImageView subsamplingScaleImageView = (SubsamplingScaleImageView) inflate.findViewById(R.id.longImg);
            LocalMedia localMedia = (LocalMedia) PictureExternalPreviewActivity.this.images.get(i);
            if (localMedia != null) {
                PictureExternalPreviewActivity.this.mimeType = localMedia.getMimeType();
                if (localMedia.isCut() && !localMedia.isCompressed()) {
                    compressPath = localMedia.getCutPath();
                } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
                    compressPath = localMedia.getCompressPath();
                } else {
                    compressPath = SdkVersionUtils.checkedAndroid_Q() ? localMedia.getAndroidQToPath() : localMedia.getPath();
                }
                boolean isGif = PictureMimeType.isGif(PictureExternalPreviewActivity.this.mimeType);
                boolean isLongImg = MediaUtils.isLongImg(localMedia);
                int i2 = 8;
                photoView.setVisibility((!isLongImg || isGif) ? 0 : 8);
                if (isLongImg && !isGif) {
                    i2 = 0;
                }
                subsamplingScaleImageView.setVisibility(i2);
                if (isGif && !localMedia.isCompressed()) {
                    if (PictureExternalPreviewActivity.this.config != null && PictureExternalPreviewActivity.this.config.imageEngine != null) {
                        PictureExternalPreviewActivity.this.config.imageEngine.loadAsGifImage(PictureExternalPreviewActivity.this, compressPath, photoView);
                    }
                } else if (PictureExternalPreviewActivity.this.config != null && PictureExternalPreviewActivity.this.config.imageEngine != null) {
                    if (isLongImg) {
                        PictureExternalPreviewActivity.this.displayLongPic(SdkVersionUtils.checkedAndroid_Q() ? Uri.parse(compressPath) : Uri.fromFile(new File(compressPath)), subsamplingScaleImageView);
                    } else {
                        PictureExternalPreviewActivity.this.config.imageEngine.loadImage(inflate.getContext(), compressPath, photoView);
                    }
                }
                photoView.setOnViewTapListener(new OnViewTapListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity$SimpleFragmentAdapter$$ExternalSyntheticLambda2
                    @Override // com.luck.picture.lib.photoview.OnViewTapListener
                    public final void onViewTap(View view, float f, float f2) {
                        PictureExternalPreviewActivity.SimpleFragmentAdapter.this.m33x43ca1726(view, f, f2);
                    }
                });
                subsamplingScaleImageView.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity$SimpleFragmentAdapter$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PictureExternalPreviewActivity.SimpleFragmentAdapter.this.m34x777841e7(view);
                    }
                });
                photoView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity$SimpleFragmentAdapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        return PictureExternalPreviewActivity.SimpleFragmentAdapter.this.m35xab266ca8(compressPath, view);
                    }
                });
            }
            viewGroup.addView(inflate, 0);
            return inflate;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$instantiateItem$0$com-luck-picture-lib-PictureExternalPreviewActivity$SimpleFragmentAdapter  reason: not valid java name */
        public /* synthetic */ void m33x43ca1726(View view, float f, float f2) {
            PictureExternalPreviewActivity.this.finish();
            PictureExternalPreviewActivity.this.exitAnimation();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$instantiateItem$1$com-luck-picture-lib-PictureExternalPreviewActivity$SimpleFragmentAdapter  reason: not valid java name */
        public /* synthetic */ void m34x777841e7(View view) {
            PictureExternalPreviewActivity.this.finish();
            PictureExternalPreviewActivity.this.exitAnimation();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$instantiateItem$2$com-luck-picture-lib-PictureExternalPreviewActivity$SimpleFragmentAdapter  reason: not valid java name */
        public /* synthetic */ boolean m35xab266ca8(String str, View view) {
            if (PictureExternalPreviewActivity.this.config.isNotPreviewDownload) {
                if (PermissionChecker.checkSelfPermission(PictureExternalPreviewActivity.this.mContext, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                    PictureExternalPreviewActivity.this.downloadPath = str;
                    PictureExternalPreviewActivity.this.showDownLoadDialog();
                } else {
                    PermissionChecker.requestPermissions(PictureExternalPreviewActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                }
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayLongPic(Uri uri, SubsamplingScaleImageView subsamplingScaleImageView) {
        subsamplingScaleImageView.setQuickScaleEnabled(true);
        subsamplingScaleImageView.setZoomEnabled(true);
        subsamplingScaleImageView.setPanEnabled(true);
        subsamplingScaleImageView.setDoubleTapZoomDuration(100);
        subsamplingScaleImageView.setMinimumScaleType(2);
        subsamplingScaleImageView.setDoubleTapZoomDpi(2);
        subsamplingScaleImageView.setImage(ImageSource.uri(uri), new ImageViewState(0.0f, new PointF(0.0f, 0.0f), 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDownLoadDialog() {
        if (TextUtils.isEmpty(this.downloadPath)) {
            return;
        }
        final PictureCustomDialog pictureCustomDialog = new PictureCustomDialog(this, (ScreenUtils.getScreenWidth(this) * 3) / 4, ScreenUtils.getScreenHeight(this) / 4, R.layout.picture_wind_base_dialog_xml, R.style.Picture_Theme_Dialog);
        ((TextView) pictureCustomDialog.findViewById(R.id.tv_title)).setText(getString(R.string.picture_prompt));
        ((TextView) pictureCustomDialog.findViewById(R.id.tv_content)).setText(getString(R.string.picture_prompt_content));
        ((Button) pictureCustomDialog.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PictureCustomDialog.this.dismiss();
            }
        });
        ((Button) pictureCustomDialog.findViewById(R.id.btn_commit)).setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PictureExternalPreviewActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PictureExternalPreviewActivity.this.m32x2eaaba08(pictureCustomDialog, view);
            }
        });
        pictureCustomDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$showDownLoadDialog$1$com-luck-picture-lib-PictureExternalPreviewActivity  reason: not valid java name */
    public /* synthetic */ void m32x2eaaba08(PictureCustomDialog pictureCustomDialog, View view) {
        if (PictureMimeType.isHttp(this.downloadPath)) {
            showPleaseDialog();
            loadDataThread loaddatathread = new loadDataThread(this.downloadPath);
            this.loadDataThread = loaddatathread;
            loaddatathread.start();
        } else {
            try {
                String lastImgSuffix = PictureMimeType.getLastImgSuffix(this.mimeType);
                String createDir = PictureFileUtils.createDir(this, System.currentTimeMillis() + lastImgSuffix);
                PictureFileUtils.copyFile(this.downloadPath, createDir);
                Context context = this.mContext;
                ToastUtils.s(context, getString(R.string.picture_save_success) + "\n" + createDir);
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(new File(createDir)));
                sendBroadcast(intent);
                dismissDialog();
            } catch (IOException e) {
                Context context2 = this.mContext;
                ToastUtils.s(context2, getString(R.string.picture_save_error) + "\n" + e.getMessage());
                dismissDialog();
                e.printStackTrace();
            }
        }
        pictureCustomDialog.dismiss();
    }

    /* loaded from: classes.dex */
    public class loadDataThread extends Thread {
        private String path;

        public loadDataThread(String str) {
            this.path = str;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                PictureExternalPreviewActivity.this.showLoadingImage(this.path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showLoadingImage(String str) {
        try {
            URL url = new URL(str);
            String lastImgSuffix = PictureMimeType.getLastImgSuffix(this.mimeType);
            String createDir = PictureFileUtils.createDir(this, System.currentTimeMillis() + lastImgSuffix);
            byte[] bArr = new byte[8192];
            long currentTimeMillis = System.currentTimeMillis();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(createDir));
            int i = 0;
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read > -1) {
                    bufferedOutputStream.write(bArr, 0, read);
                    i += read;
                    long currentTimeMillis2 = i / (System.currentTimeMillis() - currentTimeMillis);
                } else {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    Message obtainMessage = this.handler.obtainMessage();
                    obtainMessage.what = 200;
                    obtainMessage.obj = createDir;
                    this.handler.sendMessage(obtainMessage);
                    return;
                }
            }
        } catch (IOException e) {
            Context context = this.mContext;
            ToastUtils.s(context, getString(R.string.picture_save_error) + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        exitAnimation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitAnimation() {
        overridePendingTransition(R.anim.picture_anim_fade_in, (this.config.windowAnimationStyle == null || this.config.windowAnimationStyle.activityPreviewExitAnimation == 0) ? R.anim.picture_anim_exit : this.config.windowAnimationStyle.activityPreviewExitAnimation);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        loadDataThread loaddatathread = this.loadDataThread;
        if (loaddatathread != null) {
            this.handler.removeCallbacks(loaddatathread);
            this.loadDataThread = null;
        }
    }

    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 1) {
            return;
        }
        for (int i2 : iArr) {
            if (i2 == 0) {
                showDownLoadDialog();
            } else {
                ToastUtils.s(this.mContext, getString(R.string.picture_jurisdiction));
            }
        }
    }
}
