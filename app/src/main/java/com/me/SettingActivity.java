package com.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class SettingActivity extends LedBleActivity implements View.OnClickListener {
    public static final int REQUST_CODE = 1;
    private TextView textViewNavLeft;
    private TextView textViewNavRight;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_setting);
        this.textViewNavLeft = (TextView) findViewById(R.id.textViewNavLeft);
        this.textViewNavRight = (TextView) findViewById(R.id.textViewNavRight);
        this.textViewNavLeft.setOnClickListener(this);
        this.textViewNavRight.setOnClickListener(this);
        findViewById(R.id.llAccountSecurity).setOnClickListener(this);
        findViewById(R.id.llPassword).setOnClickListener(this);
        findViewById(R.id.layoutDownloadCenter).setOnClickListener(this);
        findViewById(R.id.rlSwitchLanguage).setOnClickListener(this);
        findViewById(R.id.layoutHelp).setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = getIntent();
        switch (id) {
            case R.id.layoutDownloadCenter /* 2131296848 */:
                intent.setClass(this, WebViewActivity.class);
                startActivityForResult(intent, 1);
                return;
            case R.id.layoutHelp /* 2131296849 */:
                intent.setClass(this, AboutusActivity.class);
                intent.putExtra("name", getResources().getString(R.string.about_us_and_help));
                startActivityForResult(intent, 1);
                return;
            case R.id.llAccountSecurity /* 2131296940 */:
                intent.setClass(this, AccountSecurityActivity.class);
                startActivityForResult(intent, 1);
                return;
            case R.id.llPassword /* 2131296982 */:
                intent.setClass(this, PasswordActivity.class);
                startActivityForResult(intent, 1);
                return;
            case R.id.rlSwitchLanguage /* 2131297302 */:
                intent.setClass(this, SetLanguageActivity.class);
                startActivityForResult(intent, 1);
                return;
            case R.id.textViewNavLeft /* 2131297580 */:
                setResult(0, intent);
                finish();
                return;
            case R.id.textViewNavRight /* 2131297581 */:
                setResult(-1, intent);
                finish();
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }
}
