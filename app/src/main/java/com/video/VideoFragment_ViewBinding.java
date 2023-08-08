package com.video;

import android.view.View;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

/* loaded from: classes.dex */
public class VideoFragment_ViewBinding implements Unbinder {
    private VideoFragment target;

    public VideoFragment_ViewBinding(VideoFragment videoFragment, View view) {
        this.target = videoFragment;
        videoFragment.srl_Comment11 = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.srl_Comment11, "field 'srl_Comment11'", SwipeRefreshLayout.class);
        videoFragment.gv_video = (GridViewWithHeaderAndFooter) Utils.findRequiredViewAsType(view, R.id.gv_video, "field 'gv_video'", GridViewWithHeaderAndFooter.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        VideoFragment videoFragment = this.target;
        if (videoFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        videoFragment.srl_Comment11 = null;
        videoFragment.gv_video = null;
    }
}
