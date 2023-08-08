package com.home.activity.other;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import androidx.fragment.app.FragmentTabHost;
import com.common.uitl.SharePersistent;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.home.constant.CommonConstant;
import com.home.fragment.help.HelpVideoFragment;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class HelpActivity extends LedBleActivity {
    private final Class[] fragments = {HelpVideoFragment.class};
    ImageView imbac;
    private FragmentTabHost mTabHost;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_help);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        SharePersistent.saveBoolean(getApplicationContext(), CommonConstant.GOTO_MAIN, true);
        this.imbac = (ImageView) findViewById(R.id.imbac);
        FragmentTabHost fragmentTabHost = (FragmentTabHost) findViewById(16908306);
        this.mTabHost = fragmentTabHost;
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.addFragment);
        int length = this.fragments.length;
        for (int i = 0; i < length; i++) {
            FragmentTabHost fragmentTabHost2 = this.mTabHost;
            TabHost.TabSpec newTabSpec = fragmentTabHost2.newTabSpec(i + "");
            this.mTabHost.addTab(newTabSpec.setIndicator(i + ""), this.fragments[i], null);
        }
        this.mTabHost.setCurrentTab(0);
        this.imbac.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.HelpActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                HelpActivity.this.finish();
            }
        });
    }
}
