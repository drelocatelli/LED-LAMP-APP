package com.home.activity.other;

import android.view.View;
import android.webkit.WebView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class PrivacyPolicyActivity_ViewBinding implements Unbinder {
    private PrivacyPolicyActivity target;

    public PrivacyPolicyActivity_ViewBinding(PrivacyPolicyActivity privacyPolicyActivity) {
        this(privacyPolicyActivity, privacyPolicyActivity.getWindow().getDecorView());
    }

    public PrivacyPolicyActivity_ViewBinding(PrivacyPolicyActivity privacyPolicyActivity, View view) {
        this.target = privacyPolicyActivity;
        privacyPolicyActivity.mWebView = (WebView) Utils.findRequiredViewAsType(view, R.id.webView, "field 'mWebView'", WebView.class);
    }

    @Override // butterknife.Unbinder
    public void unbind() {
        PrivacyPolicyActivity privacyPolicyActivity = this.target;
        if (privacyPolicyActivity == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        privacyPolicyActivity.mWebView = null;
    }
}
