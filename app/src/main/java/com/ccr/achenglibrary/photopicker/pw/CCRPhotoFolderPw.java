package com.ccr.achenglibrary.photopicker.pw;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter;
import com.ccr.achenglibrary.photopicker.adapter.CCRViewHolderHelper;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImage;
import com.ccr.achenglibrary.photopicker.listener.CCROnRVItemClickListener;
import com.ccr.achenglibrary.photopicker.model.CCRImageFolderModel;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CCRPhotoFolderPw extends CCRBasePopupWindow implements CCROnRVItemClickListener {
    public static final int ANIM_DURATION = 300;
    private RecyclerView mContentRv;
    private int mCurrentPosition;
    private Delegate mDelegate;
    private FolderAdapter mFolderAdapter;
    private LinearLayout mRootLl;

    /* loaded from: classes.dex */
    public interface Delegate {
        void executeDismissAnim();

        void onSelectedFolder(int i);
    }

    public CCRPhotoFolderPw(Activity activity, View view, Delegate delegate) {
        super(activity, R.layout.bga_pp_pw_photo_folder, view, -1, -1);
        this.mDelegate = delegate;
    }

    @Override // com.ccr.achenglibrary.photopicker.pw.CCRBasePopupWindow
    protected void initView() {
        this.mRootLl = (LinearLayout) getViewById(R.id.ll_photo_folder_root);
        this.mContentRv = (RecyclerView) getViewById(R.id.rv_photo_folder_content);
    }

    @Override // com.ccr.achenglibrary.photopicker.pw.CCRBasePopupWindow
    protected void setListener() {
        this.mRootLl.setOnClickListener(this);
        FolderAdapter folderAdapter = new FolderAdapter(this.mContentRv);
        this.mFolderAdapter = folderAdapter;
        folderAdapter.setOnRVItemClickListener(this);
    }

    @Override // com.ccr.achenglibrary.photopicker.pw.CCRBasePopupWindow
    protected void processLogic() {
        setAnimationStyle(16973824);
        setBackgroundDrawable(new ColorDrawable(-1879048192));
        this.mContentRv.setLayoutManager(new LinearLayoutManager(this.mActivity));
        this.mContentRv.setAdapter(this.mFolderAdapter);
    }

    public void setData(ArrayList<CCRImageFolderModel> arrayList) {
        this.mFolderAdapter.setData(arrayList);
    }

    @Override // com.ccr.achenglibrary.photopicker.pw.CCRBasePopupWindow
    public void show() {
        showAsDropDown(this.mAnchorView);
        ViewCompat.animate(this.mContentRv).translationY(-this.mWindowRootView.getHeight()).setDuration(0L).start();
        ViewCompat.animate(this.mContentRv).translationY(0.0f).setDuration(300L).start();
        ViewCompat.animate(this.mRootLl).alpha(0.0f).setDuration(0L).start();
        ViewCompat.animate(this.mRootLl).alpha(1.0f).setDuration(300L).start();
    }

    @Override // com.ccr.achenglibrary.photopicker.pw.CCRBasePopupWindow, android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.ll_photo_folder_root) {
            dismiss();
        }
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        ViewCompat.animate(this.mContentRv).translationY(-this.mWindowRootView.getHeight()).setDuration(300L).start();
        ViewCompat.animate(this.mRootLl).alpha(1.0f).setDuration(0L).start();
        ViewCompat.animate(this.mRootLl).alpha(0.0f).setDuration(300L).start();
        Delegate delegate = this.mDelegate;
        if (delegate != null) {
            delegate.executeDismissAnim();
        }
        this.mContentRv.postDelayed(new Runnable() { // from class: com.ccr.achenglibrary.photopicker.pw.CCRPhotoFolderPw.1
            @Override // java.lang.Runnable
            public void run() {
                CCRPhotoFolderPw.super.dismiss();
            }
        }, 300L);
    }

    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }

    @Override // com.ccr.achenglibrary.photopicker.listener.CCROnRVItemClickListener
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        Delegate delegate = this.mDelegate;
        if (delegate != null && this.mCurrentPosition != i) {
            delegate.onSelectedFolder(i);
        }
        this.mCurrentPosition = i;
        dismiss();
    }

    /* loaded from: classes.dex */
    private class FolderAdapter extends CCRRecyclerViewAdapter<CCRImageFolderModel> {
        private int mImageSize;

        public FolderAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.bga_pp_item_photo_folder);
            this.mData = new ArrayList();
            this.mImageSize = CCRPhotoPickerUtil.getScreenWidth() / 10;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter
        public void fillData(CCRViewHolderHelper cCRViewHolderHelper, int i, CCRImageFolderModel cCRImageFolderModel) {
            cCRViewHolderHelper.setText(R.id.tv_item_photo_folder_name, cCRImageFolderModel.name);
            cCRViewHolderHelper.setText(R.id.tv_item_photo_folder_count, String.valueOf(cCRImageFolderModel.getCount()));
            CCRImage.display(cCRViewHolderHelper.getImageView(R.id.iv_item_photo_folder_photo), R.mipmap.bga_pp_ic_holder_light, cCRImageFolderModel.coverPath, this.mImageSize);
        }
    }
}
