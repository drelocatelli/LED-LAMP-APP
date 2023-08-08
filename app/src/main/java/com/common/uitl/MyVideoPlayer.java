package com.common.uitl;

import android.content.Context;
import android.util.AttributeSet;
import cn.jzvd.JZVideoPlayerStandard;

/* loaded from: classes.dex */
public class MyVideoPlayer extends JZVideoPlayerStandard {
    public OnItemClickListener myFinishListerer;

    /* loaded from: classes.dex */
    public interface OnItemClickListener {
        void onItemClick();
    }

    public void setFinishListerer(OnItemClickListener onItemClickListener) {
        this.myFinishListerer = onItemClickListener;
    }

    public MyVideoPlayer(Context context) {
        super(context);
    }

    public MyVideoPlayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // cn.jzvd.JZVideoPlayerStandard, cn.jzvd.JZVideoPlayer
    public void onAutoCompletion() {
        super.onAutoCompletion();
        OnItemClickListener onItemClickListener = this.myFinishListerer;
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick();
        }
    }
}
