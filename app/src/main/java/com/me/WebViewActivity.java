package com.me;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class WebViewActivity extends LedBleActivity implements View.OnClickListener {
    private ImageButton imageViewNavRight;
    private TextView textViewNavLeft;
    private WebView webView;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_webview);
        this.textViewNavLeft = (TextView) findViewById(R.id.textViewNavLeft);
        this.imageViewNavRight = (ImageButton) findViewById(R.id.imageViewNavRight);
        this.textViewNavLeft.setOnClickListener(this);
        this.imageViewNavRight.setOnClickListener(this);
        WebView webView = (WebView) findViewById(R.id.wv_oauth);
        this.webView = webView;
        webView.loadUrl(Constant.queryFileDownload);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setDownloadListener(new DownloadListener() { // from class: com.me.WebViewActivity.1
            @Override // android.webkit.DownloadListener
            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(str));
                WebViewActivity.this.startActivity(intent);
            }
        });
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = getIntent();
        if (id == R.id.imageViewNavRight) {
            this.webView.reload();
        } else if (id != R.id.textViewNavLeft) {
        } else {
            setResult(0, intent);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }
}
