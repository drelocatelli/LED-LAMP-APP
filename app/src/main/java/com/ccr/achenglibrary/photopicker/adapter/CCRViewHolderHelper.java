package com.ccr.achenglibrary.photopicker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.ccr.achenglibrary.photopicker.listener.CCROnItemChildCheckedChangeListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnItemChildClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnItemChildLongClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener;
import com.ccr.achenglibrary.photopicker.listener.CCROnRVItemChildTouchListener;

/* loaded from: classes.dex */
public class CCRViewHolderHelper implements View.OnLongClickListener, CompoundButton.OnCheckedChangeListener, View.OnTouchListener {
    protected AdapterView mAdapterView;
    protected Context mContext;
    protected View mConvertView;
    protected Object mObj;
    protected CCROnItemChildCheckedChangeListener mOnItemChildCheckedChangeListener;
    protected CCROnItemChildClickListener mOnItemChildClickListener;
    protected CCROnItemChildLongClickListener mOnItemChildLongClickListener;
    protected CCROnRVItemChildTouchListener mOnRVItemChildTouchListener;
    protected int mPosition;
    protected RecyclerView mRecyclerView;
    protected CCRRecyclerViewHolder mRecyclerViewHolder;
    protected final SparseArrayCompat<View> mViews = new SparseArrayCompat<>();

    public CCRViewHolderHelper(ViewGroup viewGroup, View view) {
        this.mAdapterView = (AdapterView) viewGroup;
        this.mConvertView = view;
        this.mContext = view.getContext();
    }

    public CCRViewHolderHelper(RecyclerView recyclerView, CCRRecyclerViewHolder cCRRecyclerViewHolder) {
        this.mRecyclerView = recyclerView;
        this.mRecyclerViewHolder = cCRRecyclerViewHolder;
        View view = cCRRecyclerViewHolder.itemView;
        this.mConvertView = view;
        this.mContext = view.getContext();
    }

    public CCRRecyclerViewHolder getRecyclerViewHolder() {
        return this.mRecyclerViewHolder;
    }

    public void setPosition(int i) {
        this.mPosition = i;
    }

    public int getPosition() {
        CCRRecyclerViewHolder cCRRecyclerViewHolder = this.mRecyclerViewHolder;
        if (cCRRecyclerViewHolder != null) {
            return cCRRecyclerViewHolder.getAdapterPositionWrapper();
        }
        return this.mPosition;
    }

    public void setOnItemChildClickListener(CCROnItemChildClickListener cCROnItemChildClickListener) {
        this.mOnItemChildClickListener = cCROnItemChildClickListener;
    }

    public void setItemChildClickListener(int i) {
        View view = getView(i);
        if (view != null) {
            view.setOnClickListener(new CCROnNoDoubleClickListener() { // from class: com.ccr.achenglibrary.photopicker.adapter.CCRViewHolderHelper.1
                @Override // com.ccr.achenglibrary.photopicker.listener.CCROnNoDoubleClickListener
                public void onNoDoubleClick(View view2) {
                    if (CCRViewHolderHelper.this.mOnItemChildClickListener != null) {
                        if (CCRViewHolderHelper.this.mRecyclerView != null) {
                            CCRViewHolderHelper.this.mOnItemChildClickListener.onItemChildClick(CCRViewHolderHelper.this.mRecyclerView, view2, CCRViewHolderHelper.this.getPosition());
                        } else if (CCRViewHolderHelper.this.mAdapterView != null) {
                            CCRViewHolderHelper.this.mOnItemChildClickListener.onItemChildClick(CCRViewHolderHelper.this.mAdapterView, view2, CCRViewHolderHelper.this.getPosition());
                        }
                    }
                }
            });
        }
    }

    public void setOnItemChildLongClickListener(CCROnItemChildLongClickListener cCROnItemChildLongClickListener) {
        this.mOnItemChildLongClickListener = cCROnItemChildLongClickListener;
    }

    public void setItemChildLongClickListener(int i) {
        View view = getView(i);
        if (view != null) {
            view.setOnLongClickListener(this);
        }
    }

    public void setOnRVItemChildTouchListener(CCROnRVItemChildTouchListener cCROnRVItemChildTouchListener) {
        this.mOnRVItemChildTouchListener = cCROnRVItemChildTouchListener;
    }

    public void setRVItemChildTouchListener(int i) {
        View view = getView(i);
        if (view != null) {
            view.setOnTouchListener(this);
        }
    }

    public void setOnItemChildCheckedChangeListener(CCROnItemChildCheckedChangeListener cCROnItemChildCheckedChangeListener) {
        this.mOnItemChildCheckedChangeListener = cCROnItemChildCheckedChangeListener;
    }

    public void setItemChildCheckedChangeListener(int i) {
        View view = getView(i);
        if (view == null || !(view instanceof CompoundButton)) {
            return;
        }
        ((CompoundButton) view).setOnCheckedChangeListener(this);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        CCROnRVItemChildTouchListener cCROnRVItemChildTouchListener = this.mOnRVItemChildTouchListener;
        if (cCROnRVItemChildTouchListener == null || this.mRecyclerView == null) {
            return false;
        }
        return cCROnRVItemChildTouchListener.onRvItemChildTouch(this.mRecyclerViewHolder, view, motionEvent);
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        CCROnItemChildLongClickListener cCROnItemChildLongClickListener = this.mOnItemChildLongClickListener;
        if (cCROnItemChildLongClickListener != null) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                return cCROnItemChildLongClickListener.onItemChildLongClick(recyclerView, view, getPosition());
            }
            AdapterView adapterView = this.mAdapterView;
            if (adapterView != null) {
                return cCROnItemChildLongClickListener.onItemChildLongClick(adapterView, view, getPosition());
            }
            return false;
        }
        return false;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        CCRRecyclerViewAdapter cCRRecyclerViewAdapter;
        if (this.mOnItemChildCheckedChangeListener != null) {
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof CCRHeaderAndFooterAdapter) {
                    cCRRecyclerViewAdapter = (CCRRecyclerViewAdapter) ((CCRHeaderAndFooterAdapter) adapter).getInnerAdapter();
                } else {
                    cCRRecyclerViewAdapter = (CCRRecyclerViewAdapter) adapter;
                }
                if (cCRRecyclerViewAdapter.isIgnoreCheckedChanged()) {
                    return;
                }
                this.mOnItemChildCheckedChangeListener.onItemChildCheckedChanged(this.mRecyclerView, compoundButton, getPosition(), z);
                return;
            }
            AdapterView adapterView = this.mAdapterView;
            if (adapterView == null || ((CCRAdapterViewAdapter) adapterView.getAdapter()).isIgnoreCheckedChanged()) {
                return;
            }
            this.mOnItemChildCheckedChangeListener.onItemChildCheckedChanged(this.mAdapterView, compoundButton, getPosition(), z);
        }
    }

    public <T extends View> T getView(int i) {
        T t = (T) this.mViews.get(i);
        if (t == null) {
            T t2 = (T) this.mConvertView.findViewById(i);
            this.mViews.put(i, t2);
            return t2;
        }
        return t;
    }

    public ImageView getImageView(int i) {
        return (ImageView) getView(i);
    }

    public TextView getTextView(int i) {
        return (TextView) getView(i);
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public void setObj(Object obj) {
        this.mObj = obj;
    }

    public Object getObj() {
        return this.mObj;
    }

    public CCRViewHolderHelper setText(int i, CharSequence charSequence) {
        if (charSequence == null) {
            charSequence = "";
        }
        getTextView(i).setText(charSequence);
        return this;
    }

    public CCRViewHolderHelper setText(int i, int i2) {
        getTextView(i).setText(i2);
        return this;
    }

    public CCRViewHolderHelper setTextSizeSp(int i, float f) {
        getTextView(i).setTextSize(2, f);
        return this;
    }

    public CCRViewHolderHelper setIsBold(int i, boolean z) {
        getTextView(i).getPaint().setTypeface(z ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        return this;
    }

    public CCRViewHolderHelper setHtml(int i, String str) {
        if (str == null) {
            str = "";
        }
        getTextView(i).setText(Html.fromHtml(str));
        return this;
    }

    public CCRViewHolderHelper setChecked(int i, boolean z) {
        ((Checkable) getView(i)).setChecked(z);
        return this;
    }

    public CCRViewHolderHelper setTag(int i, Object obj) {
        getView(i).setTag(obj);
        return this;
    }

    public CCRViewHolderHelper setTag(int i, int i2, Object obj) {
        getView(i).setTag(i2, obj);
        return this;
    }

    public CCRViewHolderHelper setVisibility(int i, int i2) {
        getView(i).setVisibility(i2);
        return this;
    }

    public CCRViewHolderHelper setImageBitmap(int i, Bitmap bitmap) {
        ((ImageView) getView(i)).setImageBitmap(bitmap);
        return this;
    }

    public CCRViewHolderHelper setImageDrawable(int i, Drawable drawable) {
        ((ImageView) getView(i)).setImageDrawable(drawable);
        return this;
    }

    public CCRViewHolderHelper setTextColorRes(int i, int i2) {
        getTextView(i).setTextColor(this.mContext.getResources().getColor(i2));
        return this;
    }

    public CCRViewHolderHelper setTextColor(int i, int i2) {
        getTextView(i).setTextColor(i2);
        return this;
    }

    public CCRViewHolderHelper setBackgroundRes(int i, int i2) {
        getView(i).setBackgroundResource(i2);
        return this;
    }

    public CCRViewHolderHelper setBackgroundColor(int i, int i2) {
        getView(i).setBackgroundColor(i2);
        return this;
    }

    public CCRViewHolderHelper setBackgroundColorRes(int i, int i2) {
        getView(i).setBackgroundColor(this.mContext.getResources().getColor(i2));
        return this;
    }

    public CCRViewHolderHelper setImageResource(int i, int i2) {
        ((ImageView) getView(i)).setImageResource(i2);
        return this;
    }

    public CCRViewHolderHelper setBold(int i, boolean z) {
        getTextView(i).getPaint().setTypeface(z ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        return this;
    }
}
