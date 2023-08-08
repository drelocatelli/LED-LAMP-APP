package com.video;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class VideoReplyActivity_ViewBinding implements Unbinder {
    private VideoReplyActivity target;

    public VideoReplyActivity_ViewBinding(VideoReplyActivity videoReplyActivity) {
        this(videoReplyActivity, videoReplyActivity.getWindow().getDecorView());
    }

    public VideoReplyActivity_ViewBinding(VideoReplyActivity videoReplyActivity, View view) {
        this.target = videoReplyActivity;
        videoReplyActivity.ivBack7 = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivBack7, "field 'ivBack7'", ImageView.class);
        videoReplyActivity.srl_Comment7 = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.srl_Comment7, "field 'srl_Comment7'", SwipeRefreshLayout.class);
        videoReplyActivity.lv_Reply7 = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_Reply7, "field 'lv_Reply7'", ListView.class);
        videoReplyActivity.btnReply7 = (Button) Utils.findRequiredViewAsType(view, R.id.btnReply7, "field 'btnReply7'", Button.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        VideoReplyActivity videoReplyActivity = this.target;
        if (videoReplyActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        videoReplyActivity.ivBack7 = null;
        videoReplyActivity.srl_Comment7 = null;
        videoReplyActivity.lv_Reply7 = null;
        videoReplyActivity.btnReply7 = null;
    }
}
