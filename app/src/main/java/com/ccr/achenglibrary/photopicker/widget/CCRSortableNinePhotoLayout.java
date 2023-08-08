package com.ccr.achenglibrary.photopicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter;
import com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewHolder;
import com.ccr.achenglibrary.photopicker.adapter.CCRViewHolderHelper;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImage;
import com.ccr.achenglibrary.photopicker.listener.CCROnItemChildClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnRVItemClickListener;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import com.ccr.achenglibrary.photopicker.util.CCRSpaceItemDecoration;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class CCRSortableNinePhotoLayout extends RecyclerView implements CCROnItemChildClickListener, CCROnRVItemClickListener {
    private Delegate mDelegate;
    private boolean mDeleteDrawableOverlapQuarter;
    private int mDeleteDrawableResId;
    private boolean mEditable;
    private GridLayoutManager mGridLayoutManager;
    private int mItemCornerRadius;
    private int mItemSpanCount;
    private ItemTouchHelper mItemTouchHelper;
    private int mItemWhiteSpacing;
    private int mItemWidth;
    private int mMaxItemCount;
    private int mOtherWhiteSpacing;
    private PhotoAdapter mPhotoAdapter;
    private int mPhotoTopRightMargin;
    private int mPlaceholderDrawableResId;
    private int mPlusDrawableResId;
    private boolean mPlusEnable;
    private boolean mSortable;

    /* loaded from: classes.dex */
    public interface Delegate {
        void onClickAddNinePhotoItem(CCRSortableNinePhotoLayout cCRSortableNinePhotoLayout, View view, int i, ArrayList<String> arrayList);

        void onClickDeleteNinePhotoItem(CCRSortableNinePhotoLayout cCRSortableNinePhotoLayout, View view, int i, String str, ArrayList<String> arrayList);

        void onClickNinePhotoItem(CCRSortableNinePhotoLayout cCRSortableNinePhotoLayout, View view, int i, String str, ArrayList<String> arrayList);
    }

    public CCRSortableNinePhotoLayout(Context context) {
        this(context, null);
    }

    public CCRSortableNinePhotoLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CCRSortableNinePhotoLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initDefaultAttrs();
        initCustomAttrs(context, attributeSet);
        afterInitDefaultAndCustomAttrs();
    }

    private void initDefaultAttrs() {
        this.mPlusEnable = true;
        this.mSortable = true;
        this.mEditable = true;
        this.mDeleteDrawableResId = R.mipmap.bga_pp_ic_delete;
        this.mDeleteDrawableOverlapQuarter = false;
        this.mMaxItemCount = 9;
        this.mItemSpanCount = 3;
        this.mItemWidth = 0;
        this.mItemCornerRadius = 0;
        this.mPlusDrawableResId = R.mipmap.bga_pp_ic_plus;
        this.mItemWhiteSpacing = CCRPhotoPickerUtil.dp2px(4.0f);
        this.mPlaceholderDrawableResId = R.mipmap.bga_pp_ic_holder_light;
        this.mOtherWhiteSpacing = CCRPhotoPickerUtil.dp2px(100.0f);
    }

    private void initCustomAttrs(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CCRSortableNinePhotoLayout);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            initCustomAttr(obtainStyledAttributes.getIndex(i), obtainStyledAttributes);
        }
        obtainStyledAttributes.recycle();
    }

    private void initCustomAttr(int i, TypedArray typedArray) {
        if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_plusEnable) {
            this.mPlusEnable = typedArray.getBoolean(i, this.mPlusEnable);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_sortable) {
            this.mSortable = typedArray.getBoolean(i, this.mSortable);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_deleteDrawable) {
            this.mDeleteDrawableResId = typedArray.getResourceId(i, this.mDeleteDrawableResId);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_deleteDrawableOverlapQuarter) {
            this.mDeleteDrawableOverlapQuarter = typedArray.getBoolean(i, this.mDeleteDrawableOverlapQuarter);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_maxItemCount) {
            this.mMaxItemCount = typedArray.getInteger(i, this.mMaxItemCount);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_itemSpanCount) {
            this.mItemSpanCount = typedArray.getInteger(i, this.mItemSpanCount);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_plusDrawable) {
            this.mPlusDrawableResId = typedArray.getResourceId(i, this.mPlusDrawableResId);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_itemCornerRadius) {
            this.mItemCornerRadius = typedArray.getDimensionPixelSize(i, 0);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_itemWhiteSpacing) {
            this.mItemWhiteSpacing = typedArray.getDimensionPixelSize(i, this.mItemWhiteSpacing);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_otherWhiteSpacing) {
            this.mOtherWhiteSpacing = typedArray.getDimensionPixelOffset(i, this.mOtherWhiteSpacing);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_placeholderDrawable) {
            this.mPlaceholderDrawableResId = typedArray.getResourceId(i, this.mPlaceholderDrawableResId);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_editable) {
            this.mEditable = typedArray.getBoolean(i, this.mEditable);
        } else if (i == R.styleable.CCRSortableNinePhotoLayout_bga_snpl_itemWidth) {
            this.mItemWidth = typedArray.getDimensionPixelSize(i, this.mItemWidth);
        }
    }

    private void afterInitDefaultAndCustomAttrs() {
        int i = this.mItemWidth;
        if (i == 0) {
            this.mItemWidth = (CCRPhotoPickerUtil.getScreenWidth() - this.mOtherWhiteSpacing) / this.mItemSpanCount;
        } else {
            this.mItemWidth = i + this.mItemWhiteSpacing;
        }
        setOverScrollMode(2);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback());
        this.mItemTouchHelper = itemTouchHelper;
        itemTouchHelper.attachToRecyclerView(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), this.mItemSpanCount);
        this.mGridLayoutManager = gridLayoutManager;
        setLayoutManager(gridLayoutManager);
        addItemDecoration(new CCRSpaceItemDecoration(this.mItemWhiteSpacing / 2));
        calculatePhotoTopRightMargin();
        PhotoAdapter photoAdapter = new PhotoAdapter(this);
        this.mPhotoAdapter = photoAdapter;
        photoAdapter.setOnItemChildClickListener(this);
        this.mPhotoAdapter.setOnRVItemClickListener(this);
        setAdapter(this.mPhotoAdapter);
    }

    public void setSortable(boolean z) {
        this.mSortable = z;
    }

    public boolean isSortable() {
        return this.mSortable;
    }

    public void setEditable(boolean z) {
        this.mEditable = z;
        this.mPhotoAdapter.notifyDataSetChanged();
    }

    public boolean isEditable() {
        return this.mEditable;
    }

    public void setDeleteDrawableResId(int i) {
        this.mDeleteDrawableResId = i;
        calculatePhotoTopRightMargin();
    }

    public void setDeleteDrawableOverlapQuarter(boolean z) {
        this.mDeleteDrawableOverlapQuarter = z;
        calculatePhotoTopRightMargin();
    }

    private void calculatePhotoTopRightMargin() {
        if (this.mDeleteDrawableOverlapQuarter) {
            this.mPhotoTopRightMargin = getResources().getDimensionPixelOffset(R.dimen.bga_pp_size_delete_padding) + (BitmapFactory.decodeResource(getResources(), this.mDeleteDrawableResId).getWidth() / 2);
            return;
        }
        this.mPhotoTopRightMargin = 0;
    }

    public void setMaxItemCount(int i) {
        this.mMaxItemCount = i;
    }

    public int getMaxItemCount() {
        return this.mMaxItemCount;
    }

    public void setItemSpanCount(int i) {
        this.mItemSpanCount = i;
        this.mGridLayoutManager.setSpanCount(i);
    }

    public void setPlusDrawableResId(int i) {
        this.mPlusDrawableResId = i;
    }

    public void setItemCornerRadius(int i) {
        this.mItemCornerRadius = i;
    }

    public void setData(ArrayList<String> arrayList) {
        this.mPhotoAdapter.setData(arrayList);
    }

    public void addMoreData(ArrayList<String> arrayList) {
        if (arrayList != null) {
            this.mPhotoAdapter.getData().addAll(arrayList);
            this.mPhotoAdapter.notifyDataSetChanged();
        }
    }

    public void addLastItem(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mPhotoAdapter.getData().add(str);
        this.mPhotoAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.RecyclerView, android.view.View
    public void onMeasure(int i, int i2) {
        int i3 = this.mItemSpanCount;
        int itemCount = this.mPhotoAdapter.getItemCount();
        if (itemCount > 0 && itemCount < this.mItemSpanCount) {
            i3 = itemCount;
        }
        this.mGridLayoutManager.setSpanCount(i3);
        int i4 = this.mItemWidth;
        int i5 = i4 * i3;
        int i6 = itemCount > 0 ? i4 * (((itemCount - 1) / i3) + 1) : 0;
        setMeasuredDimension(Math.min(resolveSize(i5, i), i5), Math.min(resolveSize(i6, i2), i6));
    }

    public ArrayList<String> getData() {
        return (ArrayList) this.mPhotoAdapter.getData();
    }

    public void removeItem(int i) {
        this.mPhotoAdapter.removeItem(i);
    }

    public int getItemCount() {
        return this.mPhotoAdapter.getData().size();
    }

    @Override // com.ccr.achenglibrary.photopicker.listener.CCROnItemChildClickListener
    public void onItemChildClick(ViewGroup viewGroup, View view, int i) {
        Delegate delegate = this.mDelegate;
        if (delegate != null) {
            delegate.onClickDeleteNinePhotoItem(this, view, i, this.mPhotoAdapter.getItem(i), (ArrayList) this.mPhotoAdapter.getData());
        }
    }

    @Override // com.ccr.achenglibrary.photopicker.listener.CCROnRVItemClickListener
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (this.mPhotoAdapter.isPlusItem(i)) {
            Delegate delegate = this.mDelegate;
            if (delegate != null) {
                delegate.onClickAddNinePhotoItem(this, view, i, (ArrayList) this.mPhotoAdapter.getData());
            }
        } else if (this.mDelegate == null || ViewCompat.getScaleX(view) > 1.0f) {
        } else {
            this.mDelegate.onClickNinePhotoItem(this, view, i, this.mPhotoAdapter.getItem(i), (ArrayList) this.mPhotoAdapter.getData());
        }
    }

    public void setPlusEnable(boolean z) {
        this.mPlusEnable = z;
        this.mPhotoAdapter.notifyDataSetChanged();
    }

    public boolean isPlusEnable() {
        return this.mPlusEnable;
    }

    public void setDelegate(Delegate delegate) {
        this.mDelegate = delegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PhotoAdapter extends CCRRecyclerViewAdapter<String> {
        private int mImageSize;

        public PhotoAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.bga_pp_item_nine_photo);
            this.mImageSize = CCRPhotoPickerUtil.getScreenWidth() / (CCRSortableNinePhotoLayout.this.mItemSpanCount > 3 ? 8 : 6);
        }

        @Override // com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter
        protected void setItemChildListener(CCRViewHolderHelper cCRViewHolderHelper, int i) {
            cCRViewHolderHelper.setItemChildClickListener(R.id.iv_item_nine_photo_flag);
        }

        @Override // com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (CCRSortableNinePhotoLayout.this.mEditable && CCRSortableNinePhotoLayout.this.mPlusEnable && super.getItemCount() < CCRSortableNinePhotoLayout.this.mMaxItemCount) {
                return super.getItemCount() + 1;
            }
            return super.getItemCount();
        }

        @Override // com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter
        public String getItem(int i) {
            if (isPlusItem(i)) {
                return null;
            }
            return (String) super.getItem(i);
        }

        public boolean isPlusItem(int i) {
            return CCRSortableNinePhotoLayout.this.mEditable && CCRSortableNinePhotoLayout.this.mPlusEnable && super.getItemCount() < CCRSortableNinePhotoLayout.this.mMaxItemCount && i == getItemCount() - 1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.ccr.achenglibrary.photopicker.adapter.CCRRecyclerViewAdapter
        public void fillData(CCRViewHolderHelper cCRViewHolderHelper, int i, String str) {
            ((ViewGroup.MarginLayoutParams) cCRViewHolderHelper.getView(R.id.iv_item_nine_photo_photo).getLayoutParams()).setMargins(0, CCRSortableNinePhotoLayout.this.mPhotoTopRightMargin, CCRSortableNinePhotoLayout.this.mPhotoTopRightMargin, 0);
            if (CCRSortableNinePhotoLayout.this.mItemCornerRadius > 0) {
                ((CCRImageView) cCRViewHolderHelper.getView(R.id.iv_item_nine_photo_photo)).setCornerRadius(CCRSortableNinePhotoLayout.this.mItemCornerRadius);
            }
            if (!isPlusItem(i)) {
                if (CCRSortableNinePhotoLayout.this.mEditable) {
                    cCRViewHolderHelper.setVisibility(R.id.iv_item_nine_photo_flag, 0);
                    cCRViewHolderHelper.setImageResource(R.id.iv_item_nine_photo_flag, CCRSortableNinePhotoLayout.this.mDeleteDrawableResId);
                } else {
                    cCRViewHolderHelper.setVisibility(R.id.iv_item_nine_photo_flag, 8);
                }
                CCRImage.display(cCRViewHolderHelper.getImageView(R.id.iv_item_nine_photo_photo), CCRSortableNinePhotoLayout.this.mPlaceholderDrawableResId, str, this.mImageSize);
                return;
            }
            cCRViewHolderHelper.setVisibility(R.id.iv_item_nine_photo_flag, 8);
            cCRViewHolderHelper.setImageResource(R.id.iv_item_nine_photo_photo, CCRSortableNinePhotoLayout.this.mPlusDrawableResId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }

        private ItemTouchHelperCallback() {
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean isLongPressDragEnabled() {
            return CCRSortableNinePhotoLayout.this.mEditable && CCRSortableNinePhotoLayout.this.mSortable && CCRSortableNinePhotoLayout.this.mPhotoAdapter.getData().size() > 1;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(CCRSortableNinePhotoLayout.this.mPhotoAdapter.isPlusItem(viewHolder.getAdapterPosition()) ? 0 : 15, 0);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
            if (viewHolder.getItemViewType() != viewHolder2.getItemViewType() || CCRSortableNinePhotoLayout.this.mPhotoAdapter.isPlusItem(viewHolder2.getAdapterPosition())) {
                return false;
            }
            CCRSortableNinePhotoLayout.this.mPhotoAdapter.moveItem(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
            return true;
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
            super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
            if (i != 0) {
                ViewCompat.setScaleX(viewHolder.itemView, 1.2f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.2f);
                ((CCRRecyclerViewHolder) viewHolder).getViewHolderHelper().getImageView(R.id.iv_item_nine_photo_photo).setColorFilter(CCRSortableNinePhotoLayout.this.getResources().getColor(R.color.bga_pp_photo_selected_mask));
            }
            super.onSelectedChanged(viewHolder, i);
        }

        @Override // androidx.recyclerview.widget.ItemTouchHelper.Callback
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
            ViewCompat.setScaleY(viewHolder.itemView, 1.0f);
            ((CCRRecyclerViewHolder) viewHolder).getViewHolderHelper().getImageView(R.id.iv_item_nine_photo_photo).setColorFilter((ColorFilter) null);
            super.clearView(recyclerView, viewHolder);
        }
    }
}
