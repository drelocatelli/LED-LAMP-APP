package com.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.FirstActivity;
import com.finddreams.languagelib.MultiLanguageUtil;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class SetLanguageActivity extends LedBleActivity implements View.OnClickListener {
    private ImageView iv_english;
    private ImageView iv_followsystem;
    private ImageView iv_simplified_chinese;
    private ImageView iv_traditional_chinese;
    private RelativeLayout rl_english;
    private RelativeLayout rl_followsytem;
    private RelativeLayout rl_simplified_chinese;
    private RelativeLayout rl_traditional_chinese;
    private int savedLanguageType;
    private TextView textViewNavLeft;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        setContentView(R.layout.activity_set_language);
        initViews();
    }

    private void initViews() {
        TextView textView = (TextView) findViewById(R.id.textViewNavLeft);
        this.textViewNavLeft = textView;
        textView.setOnClickListener(this);
        this.rl_followsytem = (RelativeLayout) findViewById(R.id.rl_followsytem);
        this.rl_simplified_chinese = (RelativeLayout) findViewById(R.id.rl_simplified_chinese);
        this.rl_traditional_chinese = (RelativeLayout) findViewById(R.id.rl_traditional_chinese);
        this.rl_english = (RelativeLayout) findViewById(R.id.rl_english);
        this.iv_followsystem = (ImageView) findViewById(R.id.iv_followsystem);
        this.iv_english = (ImageView) findViewById(R.id.iv_english);
        this.iv_simplified_chinese = (ImageView) findViewById(R.id.iv_simplified_chinese);
        this.iv_traditional_chinese = (ImageView) findViewById(R.id.iv_traditional_chinese);
        this.rl_followsytem.setOnClickListener(this);
        this.rl_simplified_chinese.setOnClickListener(this);
        this.rl_traditional_chinese.setOnClickListener(this);
        this.rl_english.setOnClickListener(this);
        int languageType = MultiLanguageUtil.getInstance().getLanguageType();
        this.savedLanguageType = languageType;
        if (languageType == 0) {
            setFollowSytemVisible();
        } else if (languageType == 3) {
            setTraditionalVisible();
        } else if (languageType == 1) {
            setEnglishVisible();
        } else if (languageType == 2) {
            setSimplifiedVisible();
        } else {
            setSimplifiedVisible();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        int i = 0;
        if (id == R.id.textViewNavLeft) {
            setResult(0, getIntent());
            finish();
            return;
        }
        switch (id) {
            case R.id.rl_english /* 2131297325 */:
                setEnglishVisible();
                i = 1;
                break;
            case R.id.rl_followsytem /* 2131297326 */:
                setFollowSytemVisible();
                break;
            case R.id.rl_simplified_chinese /* 2131297340 */:
                setSimplifiedVisible();
                i = 2;
                break;
            case R.id.rl_traditional_chinese /* 2131297343 */:
                setTraditionalVisible();
                i = 3;
                break;
        }
        MultiLanguageUtil.getInstance().updateLanguage(i);
        Intent intent = new Intent(this, FirstActivity.class);
        intent.addFlags(67141632);
        startActivity(intent);
    }

    private void setSimplifiedVisible() {
        this.iv_followsystem.setVisibility(8);
        this.iv_english.setVisibility(8);
        this.iv_simplified_chinese.setVisibility(0);
        this.iv_traditional_chinese.setVisibility(8);
    }

    private void setEnglishVisible() {
        this.iv_followsystem.setVisibility(8);
        this.iv_english.setVisibility(0);
        this.iv_simplified_chinese.setVisibility(8);
        this.iv_traditional_chinese.setVisibility(8);
    }

    private void setTraditionalVisible() {
        this.iv_followsystem.setVisibility(8);
        this.iv_english.setVisibility(8);
        this.iv_simplified_chinese.setVisibility(8);
        this.iv_traditional_chinese.setVisibility(0);
    }

    private void setFollowSytemVisible() {
        this.iv_followsystem.setVisibility(0);
        this.iv_english.setVisibility(8);
        this.iv_simplified_chinese.setVisibility(8);
        this.iv_traditional_chinese.setVisibility(8);
    }
}
