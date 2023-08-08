package com.ccr.achenglibrary.photopicker.adapter;

import android.graphics.ColorFilter;
import androidx.recyclerview.widget.RecyclerView;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImage;
import com.ccr.achenglibrary.photopicker.model.CCRImageFolderModel;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CCRPhotoPickerAdapter extends CCRRecyclerViewAdapter<String> {
    private int mImageSize;
    private ArrayList<String> mSelectedImages;
    private boolean mTakePhotoEnabled;

    public CCRPhotoPickerAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.bga_pp_item_photo_picker);
        this.mSelectedImages = new ArrayList<>();
        this.mImageSize = CCRPhotoPickerUtil.getScreenWidth() / 6;
    }

    @Override // com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (this.mTakePhotoEnabled && i == 0) {
            return R.layout.bga_pp_item_photo_camera;
        }
        return R.layout.bga_pp_item_photo_picker;
    }

    @Override // com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter
    public void setItemChildListener(CCRViewHolderHelper cCRViewHolderHelper, int i) {
        if (i == R.layout.bga_pp_item_photo_camera) {
            cCRViewHolderHelper.setItemChildClickListener(R.id.iv_item_photo_camera_camera);
            return;
        }
        cCRViewHolderHelper.setItemChildClickListener(R.id.iv_item_photo_picker_flag);
        cCRViewHolderHelper.setItemChildClickListener(R.id.iv_item_photo_picker_photo);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter
    public void fillData(CCRViewHolderHelper cCRViewHolderHelper, int i, String str) {
        if (getItemViewType(i) == R.layout.bga_pp_item_photo_picker) {
            CCRImage.display(cCRViewHolderHelper.getImageView(R.id.iv_item_photo_picker_photo), R.mipmap.bga_pp_ic_holder_dark, str, this.mImageSize);
            if (this.mSelectedImages.contains(str)) {
                cCRViewHolderHelper.setImageResource(R.id.iv_item_photo_picker_flag, R.mipmap.bga_pp_ic_cb_checked);
                cCRViewHolderHelper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(cCRViewHolderHelper.getConvertView().getResources().getColor(R.color.bga_pp_photo_selected_mask));
                return;
            }
            cCRViewHolderHelper.setImageResource(R.id.iv_item_photo_picker_flag, R.mipmap.bga_pp_ic_cb_normal);
            cCRViewHolderHelper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter((ColorFilter) null);
        }
    }

    public void setSelectedImages(ArrayList<String> arrayList) {
        if (arrayList != null) {
            this.mSelectedImages = arrayList;
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedImages() {
        return this.mSelectedImages;
    }

    public int getSelectedCount() {
        return this.mSelectedImages.size();
    }

    public void setImageFolderModel(CCRImageFolderModel cCRImageFolderModel) {
        this.mTakePhotoEnabled = cCRImageFolderModel.isTakePhotoEnabled();
        setData(cCRImageFolderModel.getImages());
    }
}
