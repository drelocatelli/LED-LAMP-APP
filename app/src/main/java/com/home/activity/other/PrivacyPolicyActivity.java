package com.home.activity.other;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class PrivacyPolicyActivity extends LedBleActivity {
    @BindView(R.id.webView)
    WebView mWebView;
    private String scene;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_privacypolicy);
        ButterKnife.bind(this);
        this.scene = (String) getIntent().getSerializableExtra("scene");
        WebSettings settings = this.mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (this.scene.equalsIgnoreCase(Constant.ServiceAgreement)) {
            if (getResources().getString(R.string.privacy_policy).contains("服务协议")) {
                this.mWebView.loadUrl("file:///android_asset/serviceagreement_zh_cn.html");
            } else if (getResources().getString(R.string.privacy_policy).contains("服務協議")) {
                this.mWebView.loadUrl("file:///android_asset/serviceagreement_zh_tw.html");
            } else {
                this.mWebView.loadUrl("file:///android_asset/serviceagreement_en.html");
            }
        } else if (getResources().getString(R.string.privacy_policy).contains("隐私政策")) {
            this.mWebView.loadUrl("file:///android_asset/privacypolicy_zh_cn.html");
        } else if (getResources().getString(R.string.privacy_policy).contains("隱私政策")) {
            this.mWebView.loadUrl("file:///android_asset/privacypolicy_zh_tw.html");
        } else {
            this.mWebView.loadUrl("file:///android_asset/privacypolicy_en.html");
        }
    }
}
