package com.forum.im.widget.pulltorefresh.base;

import android.content.Context;
import android.view.View;
import com.forum.im.widget.pulltorefresh.PullToRefreshListView;
import com.forum.im.widget.pulltorefresh.PullToRefreshRecyclerView;

/* loaded from: classes.dex */
public class PullToRefreshView extends View {
    public static final int LISTVIEW = 0;
    public static final int RECYCLERVIEW = 1;

    public PullToRefreshView(Context context) {
        super(context);
    }

    public View getSlideView(int i) {
        if (i != 0) {
            if (i != 1) {
                return null;
            }
            return new PullToRefreshRecyclerView(getContext());
        }
        return new PullToRefreshListView(getContext());
    }
}
