package com.luck.picture.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import com.luck.picture.lib.PicturePreviewActivity;
import com.luck.picture.lib.adapter.PictureSimpleFragmentAdapter;
import com.luck.picture.lib.anim.OptAnimationLoader;
import com.luck.picture.lib.broadcast.BroadcastAction;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.observable.ImagesObservable;
import com.luck.picture.lib.tools.ScreenUtils;
import com.luck.picture.lib.tools.ToastUtils;
import com.luck.picture.lib.tools.VoiceUtils;
import com.luck.picture.lib.widget.PreviewViewPager;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropMulti;
import com.yalantis.ucrop.model.CutInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class PicturePreviewActivity extends PictureBaseActivity implements View.OnClickListener, PictureSimpleFragmentAdapter.OnCallBackActivity {
    private PictureSimpleFragmentAdapter adapter;
    private Animation animation;
    private View btnCheck;
    private TextView check;
    private int index;
    private Handler mHandler;
    private TextView mTvPictureOk;
    private ImageView picture_left_back;
    private int position;
    private boolean refresh;
    private int screenWidth;
    private RelativeLayout selectBarLayout;
    private TextView tv_img_num;
    private TextView tv_title;
    private PreviewViewPager viewPager;
    private List<LocalMedia> images = new ArrayList();
    private List<LocalMedia> selectImages = new ArrayList();
    private BroadcastReceiver commonBroadcastReceiver = new AnonymousClass2();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String string;
        List<LocalMedia> readPreviewMediaData;
        super.onCreate(bundle);
        setContentView(R.layout.picture_preview);
        BroadcastManager.getInstance(this).registerReceiver(this.commonBroadcastReceiver, BroadcastAction.ACTION_CLOSE_PREVIEW);
        this.mHandler = new Handler();
        this.screenWidth = ScreenUtils.getScreenWidth(this);
        this.animation = OptAnimationLoader.loadAnimation(this, R.anim.picture_anim_modal_in);
        this.picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        this.viewPager = (PreviewViewPager) findViewById(R.id.preview_pager);
        this.btnCheck = findViewById(R.id.btnCheck);
        this.check = (TextView) findViewById(R.id.check);
        this.picture_left_back.setOnClickListener(this);
        this.mTvPictureOk = (TextView) findViewById(R.id.tv_ok);
        this.tv_img_num = (TextView) findViewById(R.id.tv_img_num);
        this.selectBarLayout = (RelativeLayout) findViewById(R.id.select_bar_layout);
        this.mTvPictureOk.setOnClickListener(this);
        this.tv_img_num.setOnClickListener(this);
        this.tv_title = (TextView) findViewById(R.id.picture_title);
        this.position = getIntent().getIntExtra("position", 0);
        TextView textView = this.mTvPictureOk;
        if (this.numComplete) {
            int i = R.string.picture_done_front_num;
            Object[] objArr = new Object[2];
            objArr[0] = 0;
            objArr[1] = Integer.valueOf(this.config.selectionMode == 1 ? 1 : this.config.maxSelectNum);
            string = getString(i, objArr);
        } else {
            string = getString(R.string.picture_please_select);
        }
        textView.setText(string);
        this.tv_img_num.setSelected(this.config.checkNumMode);
        initPictureSelectorStyle();
        this.selectImages = getIntent().getParcelableArrayListExtra(PictureConfig.EXTRA_SELECT_LIST);
        if (getIntent().getBooleanExtra(PictureConfig.EXTRA_BOTTOM_PREVIEW, false)) {
            readPreviewMediaData = getIntent().getParcelableArrayListExtra(PictureConfig.EXTRA_PREVIEW_SELECT_LIST);
        } else {
            readPreviewMediaData = ImagesObservable.getInstance().readPreviewMediaData();
        }
        this.images = readPreviewMediaData;
        initViewPageAdapterData();
        this.btnCheck.setOnClickListener(new View.OnClickListener() { // from class: com.luck.picture.lib.PicturePreviewActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PicturePreviewActivity.this.m37lambda$onCreate$0$comluckpicturelibPicturePreviewActivity(view);
            }
        });
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() { // from class: com.luck.picture.lib.PicturePreviewActivity.1
            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) {
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i3) {
                PicturePreviewActivity picturePreviewActivity = PicturePreviewActivity.this;
                picturePreviewActivity.isPreviewEggs(picturePreviewActivity.config.previewEggs, i2, i3);
            }

            @Override // androidx.viewpager.widget.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) {
                PicturePreviewActivity.this.position = i2;
                TextView textView2 = PicturePreviewActivity.this.tv_title;
                textView2.setText((PicturePreviewActivity.this.position + 1) + "/" + PicturePreviewActivity.this.images.size());
                LocalMedia localMedia = (LocalMedia) PicturePreviewActivity.this.images.get(PicturePreviewActivity.this.position);
                PicturePreviewActivity.this.index = localMedia.getPosition();
                if (PicturePreviewActivity.this.config.previewEggs) {
                    return;
                }
                if (PicturePreviewActivity.this.config.checkNumMode) {
                    TextView textView3 = PicturePreviewActivity.this.check;
                    textView3.setText(localMedia.getNum() + "");
                    PicturePreviewActivity.this.notifyCheckChanged(localMedia);
                }
                PicturePreviewActivity picturePreviewActivity = PicturePreviewActivity.this;
                picturePreviewActivity.onImageChecked(picturePreviewActivity.position);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-luck-picture-lib-PicturePreviewActivity  reason: not valid java name */
    public /* synthetic */ void m37lambda$onCreate$0$comluckpicturelibPicturePreviewActivity(View view) {
        boolean z;
        List<LocalMedia> list = this.images;
        if (list == null || list.size() <= 0) {
            return;
        }
        LocalMedia localMedia = this.images.get(this.viewPager.getCurrentItem());
        String mimeType = this.selectImages.size() > 0 ? this.selectImages.get(0).getMimeType() : "";
        if (!TextUtils.isEmpty(mimeType) && !PictureMimeType.isMimeTypeSame(mimeType, localMedia.getMimeType())) {
            ToastUtils.s(this.mContext, getString(R.string.picture_rule));
            return;
        }
        if (!this.check.isSelected()) {
            this.check.setSelected(true);
            this.check.startAnimation(this.animation);
            z = true;
        } else {
            this.check.setSelected(false);
            z = false;
        }
        if (this.selectImages.size() >= this.config.maxSelectNum && z) {
            ToastUtils.s(this.mContext, getString(R.string.picture_message_max_num, new Object[]{Integer.valueOf(this.config.maxSelectNum)}));
            this.check.setSelected(false);
            return;
        }
        if (z) {
            VoiceUtils.playVoice(this.mContext, this.config.openClickSound);
            if (this.config.selectionMode == 1) {
                singleRadioMediaImage();
            }
            this.selectImages.add(localMedia);
            localMedia.setNum(this.selectImages.size());
            if (this.config.checkNumMode) {
                this.check.setText(String.valueOf(localMedia.getNum()));
            }
        } else {
            Iterator<LocalMedia> it = this.selectImages.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                LocalMedia next = it.next();
                if (next.getPath().equals(localMedia.getPath())) {
                    this.selectImages.remove(next);
                    subSelectPosition();
                    notifyCheckChanged(next);
                    break;
                }
            }
        }
        onSelectNumChange(true);
    }

    private void initPictureSelectorStyle() {
        if (this.config.style != null) {
            if (this.config.style.pictureTitleTextColor != 0) {
                this.tv_title.setTextColor(this.config.style.pictureTitleTextColor);
            }
            if (this.config.style.pictureLeftBackIcon != 0) {
                this.picture_left_back.setImageResource(this.config.style.pictureLeftBackIcon);
            }
            if (this.config.style.picturePreviewBottomBgColor != 0) {
                this.selectBarLayout.setBackgroundColor(this.config.style.picturePreviewBottomBgColor);
            }
            if (this.config.style.pictureCheckNumBgStyle != 0) {
                this.tv_img_num.setBackgroundResource(this.config.style.pictureCheckNumBgStyle);
            }
            if (this.config.style.pictureCheckedStyle != 0) {
                this.check.setBackgroundResource(this.config.style.pictureCheckedStyle);
            }
            if (this.config.style.pictureUnCompleteTextColor != 0) {
                this.mTvPictureOk.setTextColor(this.config.style.pictureUnCompleteTextColor);
            }
        }
        this.tv_title.setBackgroundColor(this.colorPrimary);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void isPreviewEggs(boolean z, int i, int i2) {
        List<LocalMedia> list;
        if (!z || this.images.size() <= 0 || (list = this.images) == null) {
            return;
        }
        if (i2 < this.screenWidth / 2) {
            LocalMedia localMedia = list.get(i);
            this.check.setSelected(isSelected(localMedia));
            if (this.config.checkNumMode) {
                int num = localMedia.getNum();
                TextView textView = this.check;
                textView.setText(num + "");
                notifyCheckChanged(localMedia);
                onImageChecked(i);
                return;
            }
            return;
        }
        int i3 = i + 1;
        LocalMedia localMedia2 = list.get(i3);
        this.check.setSelected(isSelected(localMedia2));
        if (this.config.checkNumMode) {
            int num2 = localMedia2.getNum();
            TextView textView2 = this.check;
            textView2.setText(num2 + "");
            notifyCheckChanged(localMedia2);
            onImageChecked(i3);
        }
    }

    private void singleRadioMediaImage() {
        List<LocalMedia> list = this.selectImages;
        LocalMedia localMedia = (list == null || list.size() <= 0) ? null : this.selectImages.get(0);
        if (localMedia != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("position", localMedia.getPosition());
            bundle.putParcelableArrayList("selectImages", (ArrayList) this.selectImages);
            BroadcastManager.getInstance(this).action(BroadcastAction.ACTION_SELECTED_DATA).extras(bundle).broadcast();
            this.selectImages.clear();
        }
    }

    private void initViewPageAdapterData() {
        TextView textView = this.tv_title;
        textView.setText((this.position + 1) + "/" + this.images.size());
        PictureSimpleFragmentAdapter pictureSimpleFragmentAdapter = new PictureSimpleFragmentAdapter(this.config, this.images, this, this);
        this.adapter = pictureSimpleFragmentAdapter;
        this.viewPager.setAdapter(pictureSimpleFragmentAdapter);
        this.viewPager.setCurrentItem(this.position);
        onSelectNumChange(false);
        onImageChecked(this.position);
        if (this.images.size() > 0) {
            LocalMedia localMedia = this.images.get(this.position);
            this.index = localMedia.getPosition();
            if (this.config.checkNumMode) {
                this.tv_img_num.setSelected(true);
                TextView textView2 = this.check;
                textView2.setText(localMedia.getNum() + "");
                notifyCheckChanged(localMedia);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCheckChanged(LocalMedia localMedia) {
        if (this.config.checkNumMode) {
            this.check.setText("");
            for (LocalMedia localMedia2 : this.selectImages) {
                if (localMedia2.getPath().equals(localMedia.getPath())) {
                    localMedia.setNum(localMedia2.getNum());
                    this.check.setText(String.valueOf(localMedia.getNum()));
                }
            }
        }
    }

    private void subSelectPosition() {
        int size = this.selectImages.size();
        int i = 0;
        while (i < size) {
            i++;
            this.selectImages.get(i).setNum(i);
        }
    }

    public void onImageChecked(int i) {
        List<LocalMedia> list = this.images;
        if (list != null && list.size() > 0) {
            this.check.setSelected(isSelected(this.images.get(i)));
        } else {
            this.check.setSelected(false);
        }
    }

    public boolean isSelected(LocalMedia localMedia) {
        for (LocalMedia localMedia2 : this.selectImages) {
            if (localMedia2.getPath().equals(localMedia.getPath())) {
                return true;
            }
        }
        return false;
    }

    public void onSelectNumChange(boolean z) {
        this.refresh = z;
        if (this.selectImages.size() != 0) {
            this.mTvPictureOk.setEnabled(true);
            this.mTvPictureOk.setSelected(true);
            if (this.config.style != null && this.config.style.pictureCompleteTextColor != 0) {
                this.mTvPictureOk.setTextColor(this.config.style.pictureCompleteTextColor);
            }
            if (this.numComplete) {
                TextView textView = this.mTvPictureOk;
                int i = R.string.picture_done_front_num;
                Object[] objArr = new Object[2];
                objArr[0] = Integer.valueOf(this.selectImages.size());
                objArr[1] = Integer.valueOf(this.config.selectionMode == 1 ? 1 : this.config.maxSelectNum);
                textView.setText(getString(i, objArr));
            } else {
                if (this.refresh) {
                    this.tv_img_num.startAnimation(this.animation);
                }
                this.tv_img_num.setVisibility(0);
                this.tv_img_num.setText(String.valueOf(this.selectImages.size()));
                this.mTvPictureOk.setText(getString(R.string.picture_completed));
            }
        } else {
            this.mTvPictureOk.setEnabled(false);
            this.mTvPictureOk.setSelected(false);
            if (this.config.style != null && this.config.style.pictureUnCompleteTextColor != 0) {
                this.mTvPictureOk.setTextColor(this.config.style.pictureUnCompleteTextColor);
            }
            if (this.numComplete) {
                TextView textView2 = this.mTvPictureOk;
                int i2 = R.string.picture_done_front_num;
                Object[] objArr2 = new Object[2];
                objArr2[0] = 0;
                objArr2[1] = Integer.valueOf(this.config.selectionMode == 1 ? 1 : this.config.maxSelectNum);
                textView2.setText(getString(i2, objArr2));
            } else {
                this.tv_img_num.setVisibility(4);
                this.mTvPictureOk.setText(getString(R.string.picture_please_select));
            }
        }
        updateSelector(this.refresh);
    }

    private void updateSelector(boolean z) {
        if (z) {
            Bundle bundle = new Bundle();
            bundle.putInt("position", this.index);
            bundle.putParcelableArrayList("selectImages", (ArrayList) this.selectImages);
            BroadcastManager.getInstance(this).action(BroadcastAction.ACTION_SELECTED_DATA).extras(bundle).broadcast();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String string;
        int id = view.getId();
        if (id == R.id.picture_left_back) {
            onBackPressed();
        }
        if (id == R.id.tv_ok || id == R.id.tv_img_num) {
            int size = this.selectImages.size();
            LocalMedia localMedia = this.selectImages.size() > 0 ? this.selectImages.get(0) : null;
            String mimeType = localMedia != null ? localMedia.getMimeType() : "";
            if (this.config.minSelectNum > 0 && size < this.config.minSelectNum && this.config.selectionMode == 2) {
                if (PictureMimeType.eqImage(mimeType)) {
                    string = getString(R.string.picture_min_img_num, new Object[]{Integer.valueOf(this.config.minSelectNum)});
                } else {
                    string = getString(R.string.picture_min_video_num, new Object[]{Integer.valueOf(this.config.minSelectNum)});
                }
                ToastUtils.s(this.mContext, string);
            } else if (this.config.enableCrop && PictureMimeType.eqImage(mimeType)) {
                if (this.config.selectionMode == 1) {
                    this.originalPath = localMedia.getPath();
                    startCrop(this.originalPath);
                    return;
                }
                ArrayList<CutInfo> arrayList = new ArrayList<>();
                int size2 = this.selectImages.size();
                for (int i = 0; i < size2; i++) {
                    LocalMedia localMedia2 = this.selectImages.get(i);
                    CutInfo cutInfo = new CutInfo();
                    cutInfo.setPath(localMedia2.getPath());
                    cutInfo.setImageWidth(localMedia2.getWidth());
                    cutInfo.setImageHeight(localMedia2.getHeight());
                    cutInfo.setMimeType(localMedia2.getMimeType());
                    cutInfo.setAndroidQToPath(localMedia2.getAndroidQToPath());
                    arrayList.add(cutInfo);
                }
                startCrop(arrayList);
            } else {
                onResult(this.selectImages);
            }
        }
    }

    @Override // com.luck.picture.lib.PictureBaseActivity
    public void onResult(List<LocalMedia> list) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("selectImages", (ArrayList) list);
        BroadcastManager.getInstance(this).action(BroadcastAction.ACTION_PREVIEW_COMPRESSION).extras(bundle).broadcast();
        if (!this.config.isCompress) {
            onBackPressed();
        } else {
            showPleaseDialog();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            if (i2 == 96) {
                ToastUtils.s(this.mContext, ((Throwable) intent.getSerializableExtra(UCrop.EXTRA_ERROR)).getMessage());
            }
        } else if (i == 69) {
            if (intent != null) {
                setResult(-1, intent);
            }
            finish();
        } else if (i != 609) {
        } else {
            setResult(-1, new Intent().putExtra(UCropMulti.EXTRA_OUTPUT_URI_LIST, (Serializable) UCropMulti.getOutput(intent)));
            finish();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.config.windowAnimationStyle != null && this.config.windowAnimationStyle.activityPreviewExitAnimation != 0) {
            finish();
            overridePendingTransition(0, (this.config.windowAnimationStyle == null || this.config.windowAnimationStyle.activityPreviewExitAnimation == 0) ? R.anim.picture_anim_exit : this.config.windowAnimationStyle.activityPreviewExitAnimation);
            return;
        }
        closeActivity();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.luck.picture.lib.PictureBaseActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        ImagesObservable.getInstance().clearPreviewMediaData();
        if (this.commonBroadcastReceiver != null) {
            BroadcastManager.getInstance(this).unregisterReceiver(this.commonBroadcastReceiver, BroadcastAction.ACTION_CLOSE_PREVIEW);
        }
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        Animation animation = this.animation;
        if (animation != null) {
            animation.cancel();
            this.animation = null;
        }
    }

    @Override // com.luck.picture.lib.adapter.PictureSimpleFragmentAdapter.OnCallBackActivity
    public void onActivityBackPressed() {
        onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.luck.picture.lib.PicturePreviewActivity$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends BroadcastReceiver {
        AnonymousClass2() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals(BroadcastAction.ACTION_CLOSE_PREVIEW)) {
                PicturePreviewActivity.this.dismissDialog();
                PicturePreviewActivity.this.mHandler.postDelayed(new Runnable() { // from class: com.luck.picture.lib.PicturePreviewActivity$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PicturePreviewActivity.AnonymousClass2.this.m38lambda$onReceive$0$comluckpicturelibPicturePreviewActivity$2();
                    }
                }, 150L);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$onReceive$0$com-luck-picture-lib-PicturePreviewActivity$2  reason: not valid java name */
        public /* synthetic */ void m38lambda$onReceive$0$comluckpicturelibPicturePreviewActivity$2() {
            PicturePreviewActivity.this.onBackPressed();
        }
    }
}
