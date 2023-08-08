package com.ccr.achenglibrary.photopicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes.dex */
public class CCRAdapterViewHolder {
    protected View mConvertView;
    protected CCRViewHolderHelper mViewHolderHelper;

    private CCRAdapterViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
        this.mConvertView = inflate;
        inflate.setTag(this);
        this.mViewHolderHelper = new CCRViewHolderHelper(viewGroup, this.mConvertView);
    }

    public static CCRAdapterViewHolder dequeueReusableAdapterViewHolder(View view, ViewGroup viewGroup, int i) {
        if (view == null) {
            return new CCRAdapterViewHolder(viewGroup, i);
        }
        return (CCRAdapterViewHolder) view.getTag();
    }

    public CCRViewHolderHelper getViewHolderHelper() {
        return this.mViewHolderHelper;
    }

    public View getConvertView() {
        return this.mConvertView;
    }
}
