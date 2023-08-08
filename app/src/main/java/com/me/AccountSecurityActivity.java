package com.me;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.githang.statusbar.StatusBarCompat;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.widget.effects.Effectstype;
import com.home.widget.effects.NiftyDialogBuilder;
import com.ledlamp.R;
import java.util.HashMap;

/* loaded from: classes.dex */
public class AccountSecurityActivity extends LedBleActivity implements View.OnClickListener {
    private Effectstype effect;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        setContentView(R.layout.activity_account_security);
        findViewById(R.id.textViewNavLeft).setOnClickListener(this);
        findViewById(R.id.rlDeleteAccount).setOnClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rlDeleteAccount) {
            showPrivacyolicyDialog();
        } else if (id != R.id.textViewNavLeft) {
        } else {
            finish();
        }
    }

    private void showPrivacyolicyDialog() {
        if (isFinishing()) {
            return;
        }
        final NiftyDialogBuilder niftyDialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_user_agreement_and_privacy_policy);
        this.effect = Effectstype.SlideBottom;
        niftyDialogBuilder.setCancelable(false);
        niftyDialogBuilder.withTitle(getResources().getString(R.string.Are_you_sure_delete_this_account)).withTitleColor("#000000").withDividerColor("#11000000").isCancelableOnTouchOutside(false).withDuration(200).withEffect(this.effect).withButton1Text(getResources().getString(R.string.cancel)).withButton2Text(getResources().getString(R.string.confirm));
        niftyDialogBuilder.setButton1Click(new View.OnClickListener() { // from class: com.me.AccountSecurityActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (niftyDialogBuilder.isShowing()) {
                    niftyDialogBuilder.dismiss();
                }
            }
        }).setButton2Click(new View.OnClickListener() { // from class: com.me.AccountSecurityActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AccountSecurityActivity.this.DeleteThisAccount();
                if (niftyDialogBuilder.isShowing()) {
                    niftyDialogBuilder.dismiss();
                }
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void DeleteThisAccount() {
        HashMap hashMap = new HashMap();
        hashMap.put("userName", LedBleActivity.getBaseApp().getUserName());
        hashMap.put(SharedPreferencesKey.SP_KEY_TOKEN, LedBleActivity.getBaseApp().getUserToken());
        HttpUtil.getInstance().getSourceData(true, this, Constant.deleteAccount, hashMap, new HttpUtil.HttpCallBack() { // from class: com.me.AccountSecurityActivity.3
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                AccountSecurityActivity accountSecurityActivity = AccountSecurityActivity.this;
                Toast.makeText(accountSecurityActivity, accountSecurityActivity.getString(R.string.Delete_account_succeeded), 0).show();
                LedBleActivity.getBaseApp().setUserBean(null);
                AccountSecurityActivity.this.finish();
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                AccountSecurityActivity accountSecurityActivity = AccountSecurityActivity.this;
                Toast.makeText(accountSecurityActivity, accountSecurityActivity.getString(R.string.request_failed), 0).show();
            }
        });
    }
}
