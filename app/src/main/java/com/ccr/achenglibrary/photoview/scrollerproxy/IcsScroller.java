package com.ccr.achenglibrary.photoview.scrollerproxy;

import android.content.Context;

/* loaded from: classes.dex */
public class IcsScroller extends GingerScroller {
    public IcsScroller(Context context) {
        super(context);
    }

    @Override // com.ccr.achenglibrary.photoview.scrollerproxy.GingerScroller, com.ccr.achenglibrary.photoview.scrollerproxy.ScrollerProxy
    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }
}
