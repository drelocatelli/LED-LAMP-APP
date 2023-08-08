package com;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.home.activity.other.PrivacyPolicyActivity;
import com.home.constant.Constant;
import com.home.widget.effects.Effectstype;
import com.home.widget.effects.NiftyDialogBuilder;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class LoadingActivity extends AppCompatActivity {
    private static SharedPreferences sp;
    private Effectstype effect;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_loading);
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.AgreeAPPPolicy, 0);
        sp = sharedPreferences;
        if (!Boolean.valueOf(sharedPreferences.getBoolean("AGREE", false)).booleanValue()) {
            showPrivacyolicyDialog();
            return;
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() { // from class: com.LoadingActivity.1
            @Override // java.lang.Runnable
            public void run() {
                LoadingActivity.this.startActivity(new Intent(LoadingActivity.this, FirstActivity.class));
                handler.removeCallbacks(this);
                LoadingActivity.this.finish();
            }
        }, 1L);
    }

    private void showPrivacyolicyDialog() {
        final NiftyDialogBuilder niftyDialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_user_agreement_and_privacy_policy);
        this.effect = Effectstype.SlideBottom;
        niftyDialogBuilder.withMessage(getResources().getString(R.string.privacy_policy)).withMessageColor("#FFFFFF").isCancelableOnTouchOutside(false).withDuration(200).withEffect(this.effect).withButton1Text(getResources().getString(R.string.not_used)).withButton2Text(getResources().getString(R.string.agree));
        niftyDialogBuilder.setCustomView(R.layout.activity_spannable, this);
        TextView textView = (TextView) niftyDialogBuilder.getContentView().findViewById(R.id.span_builder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(getResources().getColor(17170445));
        textView.setText(getBuilder());
        niftyDialogBuilder.setButton1Click(new View.OnClickListener() { // from class: com.LoadingActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LoadingActivity.this.exitAPP();
                LoadingActivity.sp.edit().putBoolean("AGREE", false).commit();
                if (niftyDialogBuilder.isShowing()) {
                    niftyDialogBuilder.dismiss();
                }
            }
        }).setButton2Click(new View.OnClickListener() { // from class: com.LoadingActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LoadingActivity.sp.edit().putBoolean("AGREE", true).commit();
                LoadingActivity.this.startActivity(new Intent(LoadingActivity.this, FirstActivity.class));
                LoadingActivity.this.finish();
                if (niftyDialogBuilder.isShowing()) {
                    niftyDialogBuilder.dismiss();
                }
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exitAPP() {
        for (ActivityManager.AppTask appTask : ((ActivityManager) getApplicationContext().getSystemService("activity")).getAppTasks()) {
            appTask.finishAndRemoveTask();
        }
        System.exit(0);
    }

    private SpannableStringBuilder getBuilder() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.privacy_policy_content));
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: com.LoadingActivity.4
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(LoadingActivity.this, PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.ServiceAgreement);
                LoadingActivity.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        if (getResources().getConfiguration().locale.getCountry().equals("CN") || getResources().getConfiguration().locale.getCountry().equals("TW")) {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.privacy_policy_content1).length(), getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.privacy_policy_content1).length(), getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + 1, 34);
        }
        ClickableSpan clickableSpan2 = new ClickableSpan() { // from class: com.LoadingActivity.5
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(LoadingActivity.this, PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.PricavyPolicy);
                LoadingActivity.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        if (getResources().getConfiguration().locale.getCountry().equals("CN") || getResources().getConfiguration().locale.getCountry().equals("TW")) {
            spannableStringBuilder.setSpan(clickableSpan2, getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + getResources().getString(R.string.privacy_policy_content3).length(), getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + getResources().getString(R.string.privacy_policy_content3).length() + getResources().getString(R.string.privacy_policy_content4).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan2, getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + getResources().getString(R.string.privacy_policy_content3).length() + 2, getResources().getString(R.string.privacy_policy_content1).length() + getResources().getString(R.string.privacy_policy_content2).length() + getResources().getString(R.string.privacy_policy_content3).length() + getResources().getString(R.string.privacy_policy_content4).length() + 3, 34);
        }
        return spannableStringBuilder;
    }
}
