package com.forum;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class HomePageFragment_ViewBinding implements Unbinder {
    private HomePageFragment target;

    public HomePageFragment_ViewBinding(HomePageFragment homePageFragment, View view) {
        this.target = homePageFragment;
        homePageFragment.srlComment = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.srlComment, "field 'srlComment'", SwipeRefreshLayout.class);
        homePageFragment.lvComment = (RecyclerView) Utils.findRequiredViewAsType(view, R.id.lvComment, "field 'lvComment'", RecyclerView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        HomePageFragment homePageFragment = this.target;
        if (homePageFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        homePageFragment.srlComment = null;
        homePageFragment.lvComment = null;
    }
}
