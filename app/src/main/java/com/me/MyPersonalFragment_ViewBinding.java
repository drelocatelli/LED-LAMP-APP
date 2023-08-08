package com.me;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;
import com.video.CircleImageView;

/* loaded from: classes.dex */
public class MyPersonalFragment_ViewBinding implements Unbinder {
    private MyPersonalFragment target;

    public MyPersonalFragment_ViewBinding(MyPersonalFragment myPersonalFragment, View view) {
        this.target = myPersonalFragment;
        myPersonalFragment.tvUserName = (TextView) Utils.findRequiredViewAsType(view, R.id.tvUserName, "field 'tvUserName'", TextView.class);
        myPersonalFragment.tvEmail = (TextView) Utils.findRequiredViewAsType(view, R.id.tvEmail, "field 'tvEmail'", TextView.class);
        myPersonalFragment.btnUpdate = (Button) Utils.findRequiredViewAsType(view, R.id.btnUpdate, "field 'btnUpdate'", Button.class);
        myPersonalFragment.btnLogout = (Button) Utils.findRequiredViewAsType(view, R.id.btnLogout, "field 'btnLogout'", Button.class);
        myPersonalFragment.ivUserPic = (CircleImageView) Utils.findRequiredViewAsType(view, R.id.ivUserPic, "field 'ivUserPic'", CircleImageView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        MyPersonalFragment myPersonalFragment = this.target;
        if (myPersonalFragment == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        myPersonalFragment.tvUserName = null;
        myPersonalFragment.tvEmail = null;
        myPersonalFragment.btnUpdate = null;
        myPersonalFragment.btnLogout = null;
        myPersonalFragment.ivUserPic = null;
    }
}
