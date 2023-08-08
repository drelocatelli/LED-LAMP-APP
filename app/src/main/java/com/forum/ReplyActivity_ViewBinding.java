package com.forum;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class ReplyActivity_ViewBinding implements Unbinder {
    private ReplyActivity target;

    public ReplyActivity_ViewBinding(ReplyActivity replyActivity) {
        this(replyActivity, replyActivity.getWindow().getDecorView());
    }

    public ReplyActivity_ViewBinding(ReplyActivity replyActivity, View view) {
        this.target = replyActivity;
        replyActivity.ivBack = (ImageView) Utils.findRequiredViewAsType(view, R.id.ivBack, "field 'ivBack'", ImageView.class);
        replyActivity.tvTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.tvTitle, "field 'tvTitle'", TextView.class);
        replyActivity.releseCharacter = (ImageView) Utils.findRequiredViewAsType(view, R.id.imageViewAdd, "field 'releseCharacter'", ImageView.class);
        replyActivity.lv_Reply = (ListView) Utils.findRequiredViewAsType(view, R.id.lv_Reply, "field 'lv_Reply'", ListView.class);
        replyActivity.btnReply = (Button) Utils.findRequiredViewAsType(view, R.id.btnReply, "field 'btnReply'", Button.class);
        replyActivity.srl_Comment = (SwipeRefreshLayout) Utils.findRequiredViewAsType(view, R.id.srl_Comment, "field 'srl_Comment'", SwipeRefreshLayout.class);
        replyActivity.tv_send111 = (TextView) Utils.findRequiredViewAsType(view, R.id.tv_send111, "field 'tv_send111'", TextView.class);
        replyActivity.et_context111 = (EditText) Utils.findRequiredViewAsType(view, R.id.et_context111, "field 'et_context111'", EditText.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        ReplyActivity replyActivity = this.target;
        if (replyActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        replyActivity.ivBack = null;
        replyActivity.tvTitle = null;
        replyActivity.releseCharacter = null;
        replyActivity.lv_Reply = null;
        replyActivity.btnReply = null;
        replyActivity.srl_Comment = null;
        replyActivity.tv_send111 = null;
        replyActivity.et_context111 = null;
    }
}
