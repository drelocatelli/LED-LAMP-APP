package com.home.fragment.service;

import android.view.View;
import android.widget.LinearLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class ServicesFragment_ViewBinding implements Unbinder {
    private ServicesFragment target;

    public ServicesFragment_ViewBinding(ServicesFragment servicesFragment, View view) {
        this.target = servicesFragment;
        servicesFragment.srlComment = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.srlComment, "field 'srlComment'", SwipeRefreshLayout.class);
        servicesFragment.ll_NoScene = (LinearLayout) Utils.findRequiredViewAsType(view, R.id.ll_NoScene, "field 'll_NoScene'", LinearLayout.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        ServicesFragment servicesFragment = this.target;
        if (servicesFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        servicesFragment.srlComment = null;
        servicesFragment.ll_NoScene = null;
    }
}
