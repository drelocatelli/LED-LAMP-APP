package com.ccr.achenglibrary.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.adapter.CCRPhotoPageAdapter;
import com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import com.ccr.achenglibrary.photopicker.widget.CCRHackyViewPager;
import com.ccr.achenglibrary.photoview.PhotoViewAttacher;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CCRPhotoPickerPreviewActivity extends CCRPPToolbarActivity implements PhotoViewAttacher.OnViewTapListener, CCRPhotoPageAdapter.LongClickListener {
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_FROM_TAKE_PHOTO = "EXTRA_IS_FROM_TAKE_PHOTO";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_PREVIEW_IMAGES = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    private RelativeLayout mChooseRl;
    private TextView mChooseTv;
    private CCRHackyViewPager mContentHvp;
    private boolean mIsFromTakePhoto;
    private long mLastShowHiddenTime;
    private CCRPhotoPageAdapter mPhotoPageAdapter;
    private ArrayList<String> mSelectedImages;
    private TextView mSubmitTv;
    private TextView mTitleTv;
    private String mTopRightBtnText;
    private int mMaxChooseCount = 1;
    private boolean mIsHidden = false;

    @Override // com.ccr.achenglibrary.photopicker.adapter.CCRPhotoPageAdapter.LongClickListener
    public boolean onLongClick(View view) {
        return false;
    }

    public static Intent newIntent(Context context, int i, ArrayList<String> arrayList, ArrayList<String> arrayList2, int i2, boolean z) {
        Intent intent = new Intent(context, CCRPhotoPickerPreviewActivity.class);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, arrayList);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, arrayList2);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, i);
        intent.putExtra(EXTRA_CURRENT_POSITION, i2);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, z);
        return intent;
    }

    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    public static boolean getIsFromTakePhoto(Intent intent) {
        return intent.getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void initView(Bundle bundle) {
        setNoLinearContentView(R.layout.bga_pp_activity_photo_picker_preview);
        this.mContentHvp = (CCRHackyViewPager) getViewById(R.id.hvp_photo_picker_preview_content);
        this.mChooseRl = (RelativeLayout) getViewById(R.id.rl_photo_picker_preview_choose);
        this.mChooseTv = (TextView) getViewById(R.id.tv_photo_picker_preview_choose);
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void setListener() {
        this.mChooseTv.setOnClickListener(new CCROnNoDoubleClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerPreviewActivity.1
            @Override // com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener
            public void onNoDoubleClick(View view) {
                String item = CCRPhotoPickerPreviewActivity.this.mPhotoPageAdapter.getItem(CCRPhotoPickerPreviewActivity.this.mContentHvp.getCurrentItem());
                if (CCRPhotoPickerPreviewActivity.this.mSelectedImages.contains(item)) {
                    CCRPhotoPickerPreviewActivity.this.mSelectedImages.remove(item);
                    CCRPhotoPickerPreviewActivity.this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_normal, 0, 0, 0);
                    CCRPhotoPickerPreviewActivity.this.renderTopRightBtn();
                } else if (CCRPhotoPickerPreviewActivity.this.mMaxChooseCount == 1) {
                    CCRPhotoPickerPreviewActivity.this.mSelectedImages.clear();
                    CCRPhotoPickerPreviewActivity.this.mSelectedImages.add(item);
                    CCRPhotoPickerPreviewActivity.this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_checked, 0, 0, 0);
                    CCRPhotoPickerPreviewActivity.this.renderTopRightBtn();
                } else if (CCRPhotoPickerPreviewActivity.this.mMaxChooseCount == CCRPhotoPickerPreviewActivity.this.mSelectedImages.size()) {
                    CCRPhotoPickerUtil.show(CCRPhotoPickerPreviewActivity.this.getString(R.string.bga_pp_toast_photo_picker_max, new Object[]{Integer.valueOf(CCRPhotoPickerPreviewActivity.this.mMaxChooseCount)}));
                } else {
                    CCRPhotoPickerPreviewActivity.this.mSelectedImages.add(item);
                    CCRPhotoPickerPreviewActivity.this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_checked, 0, 0, 0);
                    CCRPhotoPickerPreviewActivity.this.renderTopRightBtn();
                }
            }
        });
        this.mContentHvp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerPreviewActivity.2
            @Override // androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener, androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                CCRPhotoPickerPreviewActivity.this.handlePageSelectedStatus();
            }
        });
    }

    @Override // com.ccr.achenglibrary.photopicker.activity.CCRPPToolbarActivity
    protected void processLogic(Bundle bundle) {
        int intExtra = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        this.mMaxChooseCount = intExtra;
        if (intExtra < 1) {
            this.mMaxChooseCount = 1;
        }
        this.mSelectedImages = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);
        if (TextUtils.isEmpty(stringArrayListExtra.get(0))) {
            stringArrayListExtra.remove(0);
        }
        boolean booleanExtra = getIntent().getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
        this.mIsFromTakePhoto = booleanExtra;
        if (booleanExtra) {
            this.mChooseRl.setVisibility(4);
        }
        int intExtra2 = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);
        this.mTopRightBtnText = getString(R.string.bga_pp_confirm);
        CCRPhotoPageAdapter cCRPhotoPageAdapter = new CCRPhotoPageAdapter(this, this, stringArrayListExtra, this);
        this.mPhotoPageAdapter = cCRPhotoPageAdapter;
        this.mContentHvp.setAdapter(cCRPhotoPageAdapter);
        this.mContentHvp.setCurrentItem(intExtra2);
        this.mToolbar.postDelayed(new Runnable() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerPreviewActivity.3
            @Override // java.lang.Runnable
            public void run() {
                CCRPhotoPickerPreviewActivity.this.hiddenToolBarAndChooseBar();
            }
        }, 2000L);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bga_pp_menu_photo_picker_preview, menu);
        View actionView = menu.findItem(R.id.item_photo_picker_preview_title).getActionView();
        this.mTitleTv = (TextView) actionView.findViewById(R.id.tv_photo_picker_preview_title);
        TextView textView = (TextView) actionView.findViewById(R.id.tv_photo_picker_preview_submit);
        this.mSubmitTv = textView;
        textView.setOnClickListener(new CCROnNoDoubleClickListener() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerPreviewActivity.4
            @Override // com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener
            public void onNoDoubleClick(View view) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(CCRPhotoPickerPreviewActivity.EXTRA_SELECTED_IMAGES, CCRPhotoPickerPreviewActivity.this.mSelectedImages);
                intent.putExtra(CCRPhotoPickerPreviewActivity.EXTRA_IS_FROM_TAKE_PHOTO, CCRPhotoPickerPreviewActivity.this.mIsFromTakePhoto);
                CCRPhotoPickerPreviewActivity.this.setResult(-1, intent);
                CCRPhotoPickerPreviewActivity.this.finish();
            }
        });
        renderTopRightBtn();
        handlePageSelectedStatus();
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, this.mSelectedImages);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, this.mIsFromTakePhoto);
        setResult(0, intent);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePageSelectedStatus() {
        TextView textView = this.mTitleTv;
        if (textView == null || this.mPhotoPageAdapter == null) {
            return;
        }
        textView.setText((this.mContentHvp.getCurrentItem() + 1) + "/" + this.mPhotoPageAdapter.getCount());
        if (this.mSelectedImages.contains(this.mPhotoPageAdapter.getItem(this.mContentHvp.getCurrentItem()))) {
            this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_checked, 0, 0, 0);
        } else {
            this.mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.bga_pp_ic_cb_normal, 0, 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void renderTopRightBtn() {
        if (this.mIsFromTakePhoto) {
            this.mSubmitTv.setEnabled(true);
            this.mSubmitTv.setText(this.mTopRightBtnText);
        } else if (this.mSelectedImages.size() == 0) {
            this.mSubmitTv.setEnabled(false);
            this.mSubmitTv.setText(this.mTopRightBtnText);
        } else {
            this.mSubmitTv.setEnabled(true);
            TextView textView = this.mSubmitTv;
            textView.setText(this.mTopRightBtnText + "(" + this.mSelectedImages.size() + "/" + this.mMaxChooseCount + ")");
        }
    }

    @Override // com.ccr.achenglibrary.photoview.PhotoViewAttacher.OnViewTapListener
    public void onViewTap(View view, float f, float f2) {
        if (System.currentTimeMillis() - this.mLastShowHiddenTime > 500) {
            this.mLastShowHiddenTime = System.currentTimeMillis();
            if (this.mIsHidden) {
                showTitleBarAndChooseBar();
            } else {
                hiddenToolBarAndChooseBar();
            }
        }
    }

    private void showTitleBarAndChooseBar() {
        RelativeLayout relativeLayout;
        if (this.mToolbar != null) {
            ViewCompat.animate(this.mToolbar).translationY(0.0f).setInterpolator(new DecelerateInterpolator(2.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerPreviewActivity.5
                @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
                public void onAnimationEnd(View view) {
                    CCRPhotoPickerPreviewActivity.this.mIsHidden = false;
                }
            }).start();
        }
        if (this.mIsFromTakePhoto || (relativeLayout = this.mChooseRl) == null) {
            return;
        }
        relativeLayout.setVisibility(0);
        ViewCompat.setAlpha(this.mChooseRl, 0.0f);
        ViewCompat.animate(this.mChooseRl).alpha(1.0f).setInterpolator(new DecelerateInterpolator(2.0f)).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hiddenToolBarAndChooseBar() {
        RelativeLayout relativeLayout;
        if (this.mToolbar != null) {
            ViewCompat.animate(this.mToolbar).translationY(-this.mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2.0f)).setListener(new ViewPropertyAnimatorListenerAdapter() { // from class: com.ccr.achenglibrary.photopicker.activity.CCRPhotoPickerPreviewActivity.6
                @Override // androidx.core.view.ViewPropertyAnimatorListenerAdapter, androidx.core.view.ViewPropertyAnimatorListener
                public void onAnimationEnd(View view) {
                    CCRPhotoPickerPreviewActivity.this.mIsHidden = true;
                    if (CCRPhotoPickerPreviewActivity.this.mChooseRl != null) {
                        CCRPhotoPickerPreviewActivity.this.mChooseRl.setVisibility(4);
                    }
                }
            }).start();
        }
        if (this.mIsFromTakePhoto || (relativeLayout = this.mChooseRl) == null) {
            return;
        }
        ViewCompat.animate(relativeLayout).alpha(0.0f).setInterpolator(new DecelerateInterpolator(2.0f)).start();
    }
}
