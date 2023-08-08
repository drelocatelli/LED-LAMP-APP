package com.ccr.achenglibrary.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.adapter.CCRPhotoPickerAdapter;
import com.ccr.achenglibrary.photopicker.imageloader.CCRRVOnScrollListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnItemChildClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener;
import com.ccr.achenglibrary.photopicker.model.CCRImageFolderModel;
import com.ccr.achenglibrary.photopicker.pw.CCRPhotoFolderPw;
import com.ccr.achenglibrary.photopicker.util.CCRAsyncTask;
import com.ccr.achenglibrary.photopicker.util.CCRImageCaptureManager;
import com.ccr.achenglibrary.photopicker.util.CCRLoadPhotoTask;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import com.ccr.achenglibrary.photopicker.util.CCRSpaceItemDecoration;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CCRPhotoPickerActivity extends CCRPPToolbarActivity implements CCROnItemChildClickListener, CCRAsyncTask.Callback<ArrayList<CCRImageFolderModel>> {
    private static final String EXTRA_IMAGE_DIR = "EXTRA_IMAGE_DIR";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_PAUSE_ON_SCROLL = "EXTRA_PAUSE_ON_SCROLL";
    private static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    private static final int REQUEST_CODE_PREVIEW = 2;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int SPAN_COUNT = 3;
    private ImageView mArrowIv;
    private RecyclerView mContentRv;
    private CCRImageFolderModel mCurrentImageFolderModel;
    private CCRImageCaptureManager mImageCaptureManager;
    private ArrayList<CCRImageFolderModel> mImageFolderModels;
    private CCRLoadPhotoTask mLoadPhotoTask;
    private AppCompatDialog mLoadingDialog;
    private int mMaxChooseCount = 1;
    private CCROnNoDoubleClickListener mOnClickShowPhotoFolderListener = new CCROnNoDoubleClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerActivity.1
        @Override // com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener
        public void onNoDoubleClick(View view) {
            if (CCRPhotoPickerActivity.this.mImageFolderModels == null || CCRPhotoPickerActivity.this.mImageFolderModels.size() <= 0) {
                return;
            }
            CCRPhotoPickerActivity.this.showPhotoFolderPw();
        }
    };
    private CCRPhotoFolderPw mPhotoFolderPw;
    private CCRPhotoPickerAdapter mPicAdapter;
    private TextView mSubmitTv;
    private boolean mTakePhotoEnabled;
    private TextView mTitleTv;
    private String mTopRightBtnText;

    public static Intent newIntent(Context context, File file, int i, ArrayList<String> arrayList, boolean z) {
        Intent intent = new Intent(context, CCRPhotoPickerActivity.class);
        intent.putExtra(EXTRA_IMAGE_DIR, file);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, i);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, arrayList);
        intent.putExtra(EXTRA_PAUSE_ON_SCROLL, z);
        return intent;
    }

    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void initView(Bundle bundle) {
        setContentView(R.layout.bga_pp_activity_photo_picker);
        this.mContentRv = (RecyclerView) getViewById(R.id.rv_photo_picker_content);
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void setListener() {
        CCRPhotoPickerAdapter cCRPhotoPickerAdapter = new CCRPhotoPickerAdapter(this.mContentRv);
        this.mPicAdapter = cCRPhotoPickerAdapter;
        cCRPhotoPickerAdapter.setOnItemChildClickListener(this);
        if (getIntent().getBooleanExtra(EXTRA_PAUSE_ON_SCROLL, false)) {
            this.mContentRv.addOnScrollListener(new CCRRVOnScrollListener(this));
        }
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void processLogic(Bundle bundle) {
        File file = (File) getIntent().getSerializableExtra(EXTRA_IMAGE_DIR);
        if (file != null) {
            this.mTakePhotoEnabled = true;
            this.mImageCaptureManager = new CCRImageCaptureManager(file);
        }
        int intExtra = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        this.mMaxChooseCount = intExtra;
        if (intExtra < 1) {
            this.mMaxChooseCount = 1;
        }
        this.mTopRightBtnText = getString(R.string.bga_pp_confirm);
        this.mContentRv.setLayoutManager(new GridLayoutManager((Context) this, 3, 1, false));
        this.mContentRv.addItemDecoration(new CCRSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.bga_pp_size_photo_divider)));
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        if (stringArrayListExtra != null && stringArrayListExtra.size() > this.mMaxChooseCount) {
            stringArrayListExtra.clear();
            stringArrayListExtra.add(stringArrayListExtra.get(0));
        }
        this.mContentRv.setAdapter(this.mPicAdapter);
        this.mPicAdapter.setSelectedImages(stringArrayListExtra);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        showLoadingDialog();
        this.mLoadPhotoTask = new CCRLoadPhotoTask(this, this, this.mTakePhotoEnabled).perform();
    }

    private void showLoadingDialog() {
        if (this.mLoadingDialog == null) {
            AppCompatDialog appCompatDialog = new AppCompatDialog(this);
            this.mLoadingDialog = appCompatDialog;
            appCompatDialog.setContentView(R.layout.bga_pp_dialog_loading);
            this.mLoadingDialog.setCancelable(false);
        }
        this.mLoadingDialog.show();
    }

    private void dismissLoadingDialog() {
        AppCompatDialog appCompatDialog = this.mLoadingDialog;
        if (appCompatDialog == null || !appCompatDialog.isShowing()) {
            return;
        }
        this.mLoadingDialog.dismiss();
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_picker, menu);
        View actionView = menu.findItem(R.id.item_photo_picker_title).getActionView();
        this.mTitleTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_title);
        this.mArrowIv = (ImageView) actionView.findViewById(R.id.iv_photo_picker_arrow);
        this.mSubmitTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_submit);
        this.mTitleTv.setOnClickListener(this.mOnClickShowPhotoFolderListener);
        this.mArrowIv.setOnClickListener(this.mOnClickShowPhotoFolderListener);
        this.mSubmitTv.setOnClickListener(new CCROnNoDoubleClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerActivity.2
            @Override // com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener
            public void onNoDoubleClick(View view) {
                CCRPhotoPickerActivity cCRPhotoPickerActivity = CCRPhotoPickerActivity.this;
                cCRPhotoPickerActivity.returnSelectedImages(cCRPhotoPickerActivity.mPicAdapter.getSelectedImages());
            }
        });
        this.mTitleTv.setText(R.string.bga_pp_all_image);
        CCRImageFolderModel cCRImageFolderModel = this.mCurrentImageFolderModel;
        if (cCRImageFolderModel != null) {
            this.mTitleTv.setText(cCRImageFolderModel.name);
        }
        renderTopRightBtn();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void returnSelectedImages(ArrayList<String> arrayList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, arrayList);
        setResult(-1, intent);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPhotoFolderPw() {
        if (this.mPhotoFolderPw == null) {
            this.mPhotoFolderPw = new CCRPhotoFolderPw(this, this.mToolbar, new CCRPhotoFolderPw.Delegate() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerActivity.3
                @Override // com.ccr.achenglibrary.photopicker.pw.CCRPhotoFolderPw.Delegate
                public void onSelectedFolder(int i) {
                    CCRPhotoPickerActivity.this.reloadPhotos(i);
                }

                @Override // com.ccr.achenglibrary.photopicker.pw.CCRPhotoFolderPw.Delegate
                public void executeDismissAnim() {
                    ViewCompat.animate(CCRPhotoPickerActivity.this.mArrowIv).setDuration(300L).rotation(0.0f).start();
                }
            });
        }
        this.mPhotoFolderPw.setData(this.mImageFolderModels);
        this.mPhotoFolderPw.show();
        ViewCompat.animate(this.mArrowIv).setDuration(300L).rotation(-180.0f).start();
    }

    private void toastMaxCountTip() {
        CCRPhotoPickerUtil.show(getString(R.string.bga_pp_toast_photo_picker_max, new Object[]{Integer.valueOf(this.mMaxChooseCount)}));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            if (i2 == 0 && i == 2) {
                if (CCRPhotoPickerPreviewActivity.getIsFromTakePhoto(intent)) {
                    this.mImageCaptureManager.deletePhotoFile();
                    return;
                }
                this.mPicAdapter.setSelectedImages(CCRPhotoPickerPreviewActivity.getSelectedImages(intent));
                renderTopRightBtn();
            }
        } else if (i == 1) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.mImageCaptureManager.getCurrentPhotoPath());
            startActivityForResult(CCRPhotoPickerPreviewActivity.newIntent(this, 1, arrayList, arrayList, 0, true), 2);
        } else if (i == 2) {
            if (CCRPhotoPickerPreviewActivity.getIsFromTakePhoto(intent)) {
                this.mImageCaptureManager.refreshGallery();
            }
            returnSelectedImages(CCRPhotoPickerPreviewActivity.getSelectedImages(intent));
        }
    }

    private void renderTopRightBtn() {
        if (this.mPicAdapter.getSelectedCount() == 0) {
            this.mSubmitTv.setEnabled(false);
            this.mSubmitTv.setText(this.mTopRightBtnText);
            return;
        }
        this.mSubmitTv.setEnabled(true);
        TextView textView = this.mSubmitTv;
        textView.setText(this.mTopRightBtnText + "(" + this.mPicAdapter.getSelectedCount() + "/" + this.mMaxChooseCount + ")");
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        if (this.mTakePhotoEnabled) {
            this.mImageCaptureManager.onSaveInstanceState(bundle);
        }
        super.onSaveInstanceState(bundle);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        if (this.mTakePhotoEnabled) {
            this.mImageCaptureManager.onRestoreInstanceState(bundle);
        }
        super.onRestoreInstanceState(bundle);
    }

    @Override // com.ccr.achenglibrary.photopicker.listener.CCROnItemChildClickListener
    public void onItemChildClick(ViewGroup viewGroup, View view, int i) {
        if (view.getId() == R.id.iv_item_photo_camera_camera) {
            handleTakePhoto();
        } else if (view.getId() == R.id.iv_item_photo_picker_photo) {
            changeToPreview(i);
        } else if (view.getId() == R.id.iv_item_photo_picker_flag) {
            handleClickSelectFlagIv(i);
        }
    }

    private void handleTakePhoto() {
        if (this.mMaxChooseCount == 1) {
            takePhoto();
        } else if (this.mPicAdapter.getSelectedCount() == this.mMaxChooseCount) {
            toastMaxCountTip();
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        try {
            startActivityForResult(this.mImageCaptureManager.getTakePictureIntent(), 1);
        } catch (Exception unused) {
            CCRPhotoPickerUtil.show(R.string.bga_pp_photo_not_support);
        }
    }

    private void changeToPreview(int i) {
        if (this.mCurrentImageFolderModel.isTakePhotoEnabled()) {
            i--;
        }
        startActivityForResult(CCRPhotoPickerPreviewActivity.newIntent(this, this.mMaxChooseCount, this.mPicAdapter.getSelectedImages(), (ArrayList) this.mPicAdapter.getData(), i, false), 2);
    }

    private void handleClickSelectFlagIv(int i) {
        String item = this.mPicAdapter.getItem(i);
        if (this.mMaxChooseCount == 1) {
            if (this.mPicAdapter.getSelectedCount() > 0) {
                String remove = this.mPicAdapter.getSelectedImages().remove(0);
                if (TextUtils.equals(remove, item)) {
                    this.mPicAdapter.notifyItemChanged(i);
                } else {
                    this.mPicAdapter.notifyItemChanged(this.mPicAdapter.getData().indexOf(remove));
                    this.mPicAdapter.getSelectedImages().add(item);
                    this.mPicAdapter.notifyItemChanged(i);
                }
            } else {
                this.mPicAdapter.getSelectedImages().add(item);
                this.mPicAdapter.notifyItemChanged(i);
            }
            renderTopRightBtn();
        } else if (!this.mPicAdapter.getSelectedImages().contains(item) && this.mPicAdapter.getSelectedCount() == this.mMaxChooseCount) {
            toastMaxCountTip();
        } else {
            if (this.mPicAdapter.getSelectedImages().contains(item)) {
                this.mPicAdapter.getSelectedImages().remove(item);
            } else {
                this.mPicAdapter.getSelectedImages().add(item);
            }
            this.mPicAdapter.notifyItemChanged(i);
            renderTopRightBtn();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reloadPhotos(int i) {
        if (i < this.mImageFolderModels.size()) {
            CCRImageFolderModel cCRImageFolderModel = this.mImageFolderModels.get(i);
            this.mCurrentImageFolderModel = cCRImageFolderModel;
            TextView textView = this.mTitleTv;
            if (textView != null) {
                textView.setText(cCRImageFolderModel.name);
            }
            this.mPicAdapter.setImageFolderModel(this.mCurrentImageFolderModel);
        }
    }

    @Override // com.ccr.achenglibrary.photopicker.util.CCRAsyncTask.Callback
    public void onPostExecute(ArrayList<CCRImageFolderModel> arrayList) {
        dismissLoadingDialog();
        this.mLoadPhotoTask = null;
        this.mImageFolderModels = arrayList;
        CCRPhotoFolderPw cCRPhotoFolderPw = this.mPhotoFolderPw;
        reloadPhotos(cCRPhotoFolderPw == null ? 0 : cCRPhotoFolderPw.getCurrentPosition());
    }

    @Override // com.ccr.achenglibrary.photopicker.util.CCRAsyncTask.Callback
    public void onTaskCancelled() {
        dismissLoadingDialog();
        this.mLoadPhotoTask = null;
    }

    private void cancelLoadPhotoTask() {
        CCRLoadPhotoTask cCRLoadPhotoTask = this.mLoadPhotoTask;
        if (cCRLoadPhotoTask != null) {
            cCRLoadPhotoTask.cancelTask();
            this.mLoadPhotoTask = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        dismissLoadingDialog();
        cancelLoadPhotoTask();
        super.onDestroy();
    }
}
