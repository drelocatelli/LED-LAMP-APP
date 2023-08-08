package com.ccr.achenglibrary.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.adapter.CCRPhotoPageAdapter;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImage;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader;
import com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener;
import com.ccr.achenglibrary.photopicker.util.CCRAsyncTask;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import com.ccr.achenglibrary.photopicker.util.CCRSavePhotoTask;
import com.ccr.achenglibrary.photopicker.util.MyUtilHelper;
import com.ccr.achenglibrary.photopicker.widget.CCRHackyViewPager;
import com.ccr.achenglibrary.photoview.PhotoViewAttacher;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CCRPhotoPreviewActivity extends CCRPPToolbarActivity implements PhotoViewAttacher.OnViewTapListener, CCRAsyncTask.Callback<Void>, CCRPhotoPageAdapter.LongClickListener {
    private static final String CLICK_CLOSE = "CLICK_CLOSE";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_SINGLE_PREVIEW = "EXTRA_IS_SINGLE_PREVIEW";
    private static final String EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH";
    private static final String EXTRA_PREVIEW_IMAGES = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_SAVE_IMG_DIR = "EXTRA_SAVE_IMG_DIR";
    private static final String IS_SHARE = "IS_SHARE";
    private static final String IS_SHOW_SAVE = "IS_SHOW_SAVE";
    private CCRHackyViewPager mContentHvp;
    private ImageView mDownloadIv;
    private boolean mIsSinglePreview;
    private long mLastShowHiddenTime;
    private CCRPhotoPageAdapter mPhotoPageAdapter;
    private File mSaveImgDir;
    private CCRSavePhotoTask mSavePhotoTask;
    private TextView mTitleTv;
    private TextView numberText;
    private ArrayList<String> previewImages;
    private TextView save;
    private TextView saveButton;
    private RelativeLayout saveLayout;
    private TextView shareButton;
    private boolean mIsHidden = false;
    private boolean isClickClose = false;
    private boolean isShare = false;
    private boolean isSave = false;

    public static Intent newIntent(Context context, File file, ArrayList<String> arrayList, int i, boolean z, boolean z2, boolean z3) {
        Intent intent = new Intent(context, CCRPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, file);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, arrayList);
        intent.putExtra(EXTRA_CURRENT_POSITION, i);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        intent.putExtra(CLICK_CLOSE, z);
        intent.putExtra(IS_SHARE, z2);
        intent.putExtra(IS_SHOW_SAVE, z3);
        return intent;
    }

    public static Intent newIntent(Context context, File file, String str, boolean z, boolean z2, boolean z3) {
        Intent intent = new Intent(context, CCRPhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, file);
        intent.putExtra(EXTRA_PHOTO_PATH, str);
        intent.putExtra(EXTRA_CURRENT_POSITION, 0);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, true);
        intent.putExtra(CLICK_CLOSE, z);
        intent.putExtra(IS_SHARE, z2);
        intent.putExtra(IS_SHOW_SAVE, z3);
        return intent;
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void initView(Bundle bundle) {
        getWindow().setFlags(1024, 1024);
        setNoLinearContentView(R.layout.bga_pp_activity_photo_preview);
        this.mContentHvp = (CCRHackyViewPager) getViewById(R.id.hvp_photo_preview_content);
        this.numberText = (TextView) findViewById(R.id.number_text);
        this.saveButton = (TextView) findViewById(R.id.save_button);
        this.shareButton = (TextView) findViewById(R.id.share_button);
        this.save = (TextView) findViewById(R.id.save);
        this.saveLayout = (RelativeLayout) findViewById(R.id.save_layout);
        this.mToolbar.setVisibility(8);
        MyUtilHelper.hideBottomUIMenu(this);
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void setListener() {
        this.mContentHvp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.1
            @Override // androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener, androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                CCRPhotoPreviewActivity.this.renderTitleTv();
            }
        });
        this.saveButton.setOnClickListener(new View.OnClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (CCRPhotoPreviewActivity.this.mSavePhotoTask == null) {
                    CCRPhotoPreviewActivity.this.savePic();
                }
            }
        });
        this.shareButton.setOnClickListener(new View.OnClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Toast.makeText(CCRPhotoPreviewActivity.this, "分享", 0).show();
            }
        });
        this.save.setOnClickListener(new View.OnClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MyUtilHelper.showAnimation(1, false, CCRPhotoPreviewActivity.this.saveLayout, CCRPhotoPreviewActivity.this);
                CCRPhotoPreviewActivity.this.savePic();
            }
        });
        this.saveLayout.setOnClickListener(new View.OnClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                MyUtilHelper.showAnimation(1, false, CCRPhotoPreviewActivity.this.saveLayout, CCRPhotoPreviewActivity.this);
            }
        });
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void processLogic(Bundle bundle) {
        this.mSaveImgDir = (File) getIntent().getSerializableExtra(EXTRA_SAVE_IMG_DIR);
        this.isClickClose = getIntent().getBooleanExtra(CLICK_CLOSE, false);
        this.isShare = getIntent().getBooleanExtra(IS_SHARE, false);
        this.isSave = getIntent().getBooleanExtra(IS_SHOW_SAVE, false);
        if (!this.isShare) {
            this.shareButton.setVisibility(4);
        }
        File file = this.mSaveImgDir;
        if (file != null && !file.exists()) {
            this.mSaveImgDir.mkdirs();
        }
        this.previewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);
        boolean booleanExtra = getIntent().getBooleanExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        this.mIsSinglePreview = booleanExtra;
        if (booleanExtra) {
            ArrayList<String> arrayList = new ArrayList<>();
            this.previewImages = arrayList;
            arrayList.add(getIntent().getStringExtra(EXTRA_PHOTO_PATH));
        }
        int intExtra = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);
        CCRPhotoPageAdapter cCRPhotoPageAdapter = new CCRPhotoPageAdapter(this, this, this.previewImages, this);
        this.mPhotoPageAdapter = cCRPhotoPageAdapter;
        this.mContentHvp.setAdapter(cCRPhotoPageAdapter);
        this.mContentHvp.setCurrentItem(intExtra);
        if (this.isClickClose) {
            return;
        }
        this.mToolbar.postDelayed(new Runnable() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.6
            @Override // java.lang.Runnable
            public void run() {
                CCRPhotoPreviewActivity.this.hiddenTitleBar();
            }
        }, 2000L);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_preview, menu);
        View actionView = menu.findItem(R.id.item_photo_preview_title).getActionView();
        this.mTitleTv = (TextView) actionView.findViewById(R.id.tv_photo_preview_title);
        ImageView imageView = (ImageView) actionView.findViewById(R.id.iv_photo_preview_download);
        this.mDownloadIv = imageView;
        imageView.setOnClickListener(new CCROnNoDoubleClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.7
            @Override // com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener
            public void onNoDoubleClick(View view) {
                if (CCRPhotoPreviewActivity.this.mSavePhotoTask == null) {
                    CCRPhotoPreviewActivity.this.savePic();
                }
            }
        });
        if (!this.isSave) {
            this.mDownloadIv.setVisibility(4);
            this.saveButton.setVisibility(4);
        }
        renderTitleTv();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renderTitleTv() {
        TextView textView = this.mTitleTv;
        if (textView == null || this.mPhotoPageAdapter == null) {
            return;
        }
        if (this.mIsSinglePreview) {
            textView.setText(R.string.bga_pp_view_photo);
            return;
        }
        TextView textView2 = this.numberText;
        textView2.setText((this.mContentHvp.getCurrentItem() + 1) + "/" + this.mPhotoPageAdapter.getCount());
    }

    @Override // com.ccr.achenglibrary.photoview.PhotoViewAttacher.OnViewTapListener
    public void onViewTap(View view, float f, float f2) {
        if (this.isClickClose) {
            finish();
            overridePendingTransition(0, R.anim.a3);
        } else if (System.currentTimeMillis() - this.mLastShowHiddenTime > 500) {
            this.mLastShowHiddenTime = System.currentTimeMillis();
            if (this.mIsHidden) {
                showTitleBar();
            } else {
                hiddenTitleBar();
            }
        }
    }

    private void showTitleBar() {
        if (this.mToolbar != null) {
            ViewCompat.animate(this.mToolbar).translationY(0.0f).setInterpolator(new DecelerateInterpolator(2.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.8
                @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
                public void onAnimationEnd(View view) {
                    CCRPhotoPreviewActivity.this.mIsHidden = false;
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hiddenTitleBar() {
        if (this.mToolbar != null) {
            ViewCompat.animate(this.mToolbar).translationY(-this.mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.9
                @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
                public void onAnimationEnd(View view) {
                    CCRPhotoPreviewActivity.this.mIsHidden = true;
                }
            }).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void savePic() {
        if (this.mSavePhotoTask != null) {
            return;
        }
        String item = this.mPhotoPageAdapter.getItem(this.mContentHvp.getCurrentItem());
        if (item.startsWith("file")) {
            File file = new File(item.replace("file://", ""));
            if (file.exists()) {
                CCRPhotoPickerUtil.showSafe(getString(R.string.bga_pp_save_img_success_folder, new Object[]{file.getParentFile().getAbsolutePath()}));
                updateImg(file);
                return;
            }
        }
        File file2 = this.mSaveImgDir;
        File file3 = new File(file2, CCRPhotoPickerUtil.md5(item) + ".jpg");
        if (file3.exists()) {
            CCRPhotoPickerUtil.showSafe(getString(R.string.bga_pp_save_img_success_folder, new Object[]{this.mSaveImgDir.getAbsolutePath()}));
            updateImg(file3);
            return;
        }
        this.mSavePhotoTask = new CCRSavePhotoTask(this, this, file3);
        CCRImage.download(item, new CCRImageLoader.DownloadDelegate() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPreviewActivity.10
            @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader.DownloadDelegate
            public void onSuccess(String str, Bitmap bitmap) {
                CCRPhotoPreviewActivity.this.mSavePhotoTask.setBitmapAndPerform(bitmap);
            }

            @Override // com.ccr.achenglibrary.photopicker.imageloader.CCRImageLoader.DownloadDelegate
            public void onFailed(String str) {
                CCRPhotoPreviewActivity.this.mSavePhotoTask = null;
                CCRPhotoPickerUtil.show(R.string.bga_pp_save_img_failure);
            }
        });
    }

    private void updateImg(File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    @Override // com.ccr.achenglibrary.photopicker.util.CCRAsyncTask.Callback
    public void onPostExecute(Void r1) {
        this.mSavePhotoTask = null;
    }

    @Override // com.ccr.achenglibrary.photopicker.util.CCRAsyncTask.Callback
    public void onTaskCancelled() {
        this.mSavePhotoTask = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        CCRSavePhotoTask cCRSavePhotoTask = this.mSavePhotoTask;
        if (cCRSavePhotoTask != null) {
            cCRSavePhotoTask.cancelTask();
            this.mSavePhotoTask = null;
        }
        MyUtilHelper.showBottomUIMenu(this);
        super.onDestroy();
        MyUtilHelper.releaseInputMethodManagerFocus(this);
    }

    @Override // com.ccr.achenglibrary.photopicker.adapter.CCRPhotoPageAdapter.LongClickListener
    public boolean onLongClick(View view) {
        MyUtilHelper.showAnimation(1, true, this.saveLayout, this);
        return true;
    }
}
