package com.ccr.achenglibrary.photopicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import com.ccr.achenglibrary.R;
import com.ccr.achenglibrary.photopicker.adapter.CCRAdapterViewAdapter;
import com.ccr.achenglibrary.photopicker.adapter.CCRViewHolderHelper;
import com.ccr.achenglibrary.photopicker.imageloader.CCRImage;
import com.ccr.achenglibrary.photopicker.util.CCRPhotoPickerUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CCRNinePhotoLayout extends FrameLayout implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int ITEM_NUM_COLUMNS = 3;
    private int mCurrentClickItemPosition;
    private Delegate mDelegate;
    private int mItemCornerRadius;
    private int mItemSpanCount;
    private int mItemWhiteSpacing;
    private int mItemWidth;
    private int mOtherWhiteSpacing;
    private PhotoAdapter mPhotoAdapter;
    private CCRHeightWrapGridView mPhotoGv;
    private CCRImageView mPhotoIv;
    private int mPlaceholderDrawableResId;
    private boolean mShowAsLargeWhenOnlyOne;

    /* loaded from: classes.dex */
    public interface Delegate {
        void onClickNinePhotoItem(CCRNinePhotoLayout cCRNinePhotoLayout, View view, int i, String str, List<String> list);
    }

    public CCRNinePhotoLayout(Context context) {
        this(context, null);
    }

    public CCRNinePhotoLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CCRNinePhotoLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initDefaultAttrs();
        initCustomAttrs(context, attributeSet);
        afterInitDefaultAndCustomAttrs();
    }

    private void initDefaultAttrs() {
        this.mItemWidth = 0;
        this.mShowAsLargeWhenOnlyOne = true;
        this.mItemCornerRadius = 0;
        this.mItemWhiteSpacing = CCRPhotoPickerUtil.dp2px(4.0f);
        this.mPlaceholderDrawableResId = R.mipmap.bga_pp_ic_holder_light;
        this.mOtherWhiteSpacing = CCRPhotoPickerUtil.dp2px(100.0f);
        this.mItemSpanCount = 3;
    }

    private void initCustomAttrs(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CCRNinePhotoLayout);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            initCustomAttr(obtainStyledAttributes.getIndex(i), obtainStyledAttributes);
        }
        obtainStyledAttributes.recycle();
    }

    private void initCustomAttr(int i, TypedArray typedArray) {
        if (i == R.styleable.CCRNinePhotoLayout_bga_npl_showAsLargeWhenOnlyOne) {
            this.mShowAsLargeWhenOnlyOne = typedArray.getBoolean(i, this.mShowAsLargeWhenOnlyOne);
        } else if (i == R.styleable.CCRNinePhotoLayout_bga_npl_itemCornerRadius) {
            this.mItemCornerRadius = typedArray.getDimensionPixelSize(i, this.mItemCornerRadius);
        } else if (i == R.styleable.CCRNinePhotoLayout_bga_npl_itemWhiteSpacing) {
            this.mItemWhiteSpacing = typedArray.getDimensionPixelSize(i, this.mItemWhiteSpacing);
        } else if (i == R.styleable.CCRNinePhotoLayout_bga_npl_otherWhiteSpacing) {
            this.mOtherWhiteSpacing = typedArray.getDimensionPixelOffset(i, this.mOtherWhiteSpacing);
        } else if (i == R.styleable.CCRNinePhotoLayout_bga_npl_placeholderDrawable) {
            this.mPlaceholderDrawableResId = typedArray.getResourceId(i, this.mPlaceholderDrawableResId);
        } else if (i == R.styleable.CCRNinePhotoLayout_bga_npl_itemWidth) {
            this.mItemWidth = typedArray.getDimensionPixelSize(i, this.mItemWidth);
        } else if (i == R.styleable.CCRNinePhotoLayout_bga_npl_itemSpanCount) {
            this.mItemSpanCount = typedArray.getInteger(i, this.mItemSpanCount);
        }
    }

    private void afterInitDefaultAndCustomAttrs() {
        if (this.mItemWidth == 0) {
            int screenWidth = CCRPhotoPickerUtil.getScreenWidth() - this.mOtherWhiteSpacing;
            int i = this.mItemSpanCount;
            this.mItemWidth = (screenWidth - ((i - 1) * this.mItemWhiteSpacing)) / i;
        }
        CCRImageView cCRImageView = new CCRImageView(getContext());
        this.mPhotoIv = cCRImageView;
        cCRImageView.setClickable(true);
        this.mPhotoIv.setOnClickListener(this);
        CCRHeightWrapGridView cCRHeightWrapGridView = new CCRHeightWrapGridView(getContext());
        this.mPhotoGv = cCRHeightWrapGridView;
        cCRHeightWrapGridView.setHorizontalSpacing(this.mItemWhiteSpacing);
        this.mPhotoGv.setVerticalSpacing(this.mItemWhiteSpacing);
        this.mPhotoGv.setNumColumns(3);
        this.mPhotoGv.setOnItemClickListener(this);
        PhotoAdapter photoAdapter = new PhotoAdapter(getContext());
        this.mPhotoAdapter = photoAdapter;
        this.mPhotoGv.setAdapter((ListAdapter) photoAdapter);
        addView(this.mPhotoIv, new FrameLayout.LayoutParams(-2, -2));
        addView(this.mPhotoGv);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        this.mCurrentClickItemPosition = i;
        Delegate delegate = this.mDelegate;
        if (delegate != null) {
            delegate.onClickNinePhotoItem(this, view, i, this.mPhotoAdapter.getItem(i), this.mPhotoAdapter.getData());
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.mCurrentClickItemPosition = 0;
        Delegate delegate = this.mDelegate;
        if (delegate != null) {
            delegate.onClickNinePhotoItem(this, view, 0, this.mPhotoAdapter.getItem(0), this.mPhotoAdapter.getData());
        }
    }

    public void setData(ArrayList<String> arrayList) {
        if (arrayList.size() == 0) {
            setVisibility(8);
            return;
        }
        setVisibility(0);
        if (arrayList.size() == 1 && this.mShowAsLargeWhenOnlyOne) {
            this.mPhotoGv.setVisibility(8);
            this.mPhotoAdapter.setData(arrayList);
            this.mPhotoIv.setVisibility(0);
            int i = this.mItemWidth;
            int i2 = (i * 2) + this.mItemWhiteSpacing + (i / 4);
            this.mPhotoIv.setMaxWidth(i2);
            this.mPhotoIv.setMaxHeight(i2);
            int i3 = this.mItemCornerRadius;
            if (i3 > 0) {
                this.mPhotoIv.setCornerRadius(i3);
            }
            CCRImage.display(this.mPhotoIv, this.mPlaceholderDrawableResId, arrayList.get(0), i2);
            return;
        }
        this.mPhotoIv.setVisibility(8);
        this.mPhotoGv.setVisibility(0);
        ViewGroup.LayoutParams layoutParams = this.mPhotoGv.getLayoutParams();
        if (this.mItemSpanCount > 3) {
            int size = arrayList.size();
            int i4 = this.mItemSpanCount;
            if (size < i4) {
                i4 = arrayList.size();
            }
            this.mPhotoGv.setNumColumns(i4);
            layoutParams.width = (this.mItemWidth * i4) + ((i4 - 1) * this.mItemWhiteSpacing);
        } else if (arrayList.size() == 1) {
            this.mPhotoGv.setNumColumns(1);
            layoutParams.width = this.mItemWidth * 1;
        } else if (arrayList.size() == 2) {
            this.mPhotoGv.setNumColumns(2);
            layoutParams.width = (this.mItemWidth * 2) + this.mItemWhiteSpacing;
        } else if (arrayList.size() == 4) {
            this.mPhotoGv.setNumColumns(2);
            layoutParams.width = (this.mItemWidth * 2) + this.mItemWhiteSpacing;
        } else {
            this.mPhotoGv.setNumColumns(3);
            layoutParams.width = (this.mItemWidth * 3) + (this.mItemWhiteSpacing * 2);
        }
        this.mPhotoGv.setLayoutParams(layoutParams);
        this.mPhotoAdapter.setData(arrayList);
    }

    public void setDelegate(Delegate delegate) {
        this.mDelegate = delegate;
    }

    public ArrayList<String> getData() {
        return (ArrayList) this.mPhotoAdapter.getData();
    }

    public int getItemCount() {
        return this.mPhotoAdapter.getCount();
    }

    public String getCurrentClickItem() {
        return this.mPhotoAdapter.getItem(this.mCurrentClickItemPosition);
    }

    public int getCurrentClickItemPosition() {
        return this.mCurrentClickItemPosition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class PhotoAdapter extends CCRAdapterViewAdapter<String> {
        private int mImageSize;

        public PhotoAdapter(Context context) {
            super(context, R.layout.bga_pp_item_nine_photo);
            this.mImageSize = CCRPhotoPickerUtil.getScreenWidth() / (CCRNinePhotoLayout.this.mItemSpanCount > 3 ? 8 : 6);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.ccr.achenglibrary.photopicker.adapter.CCRAdapterViewAdapter
        public void fillData(CCRViewHolderHelper cCRViewHolderHelper, int i, String str) {
            if (CCRNinePhotoLayout.this.mItemCornerRadius > 0) {
                ((CCRImageView) cCRViewHolderHelper.getView(R.id.iv_item_nine_photo_photo)).setCornerRadius(CCRNinePhotoLayout.this.mItemCornerRadius);
            }
            CCRImage.display(cCRViewHolderHelper.getImageView(R.id.iv_item_nine_photo_photo), CCRNinePhotoLayout.this.mPlaceholderDrawableResId, str, this.mImageSize);
        }
    }
}
