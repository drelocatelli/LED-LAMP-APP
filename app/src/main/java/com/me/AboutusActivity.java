package com.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.other.PrivacyPolicyActivity;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class AboutusActivity extends LedBleActivity implements View.OnClickListener {
    private ImageView back;
    private TextView tvSpan;
    private TextView tvVersion;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_aboutus);
        ImageView imageView = (ImageView) findViewById(R.id.backAB);
        this.back = imageView;
        imageView.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.tvSpanAB);
        this.tvSpan = textView;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        this.tvSpan.setHighlightColor(getResources().getColor(17170445));
        this.tvSpan.setText(getBuilder());
        TextView textView2 = (TextView) findViewById(R.id.tvVersion);
        this.tvVersion = textView2;
        textView2.setText(ExifInterface.GPS_MEASUREMENT_INTERRUPTED + getVerName(getApplicationContext()));
    }

    private SpannableStringBuilder getBuilder() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.regist_agree_policy));
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: com.me.AboutusActivity.1
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(AboutusActivity.this.getApplicationContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.ServiceAgreement);
                AboutusActivity.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        String stringExtra = getIntent().getStringExtra("name");
        if (stringExtra.contains("关于我们")) {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.regist_agree_policy1).length(), getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.regist_agree_policy1).length(), getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + 1, 34);
        }
        ClickableSpan clickableSpan2 = new ClickableSpan() { // from class: com.me.AboutusActivity.2
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(AboutusActivity.this.getApplicationContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.PricavyPolicy);
                AboutusActivity.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        if (stringExtra.contains("关于我们") || stringExtra.contains("關於我們")) {
            spannableStringBuilder.setSpan(clickableSpan2, getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + getResources().getString(R.string.regist_agree_policy3).length(), getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + getResources().getString(R.string.regist_agree_policy3).length() + getResources().getString(R.string.regist_agree_policy4).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan2, getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + getResources().getString(R.string.regist_agree_policy3).length() + 2, getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + getResources().getString(R.string.regist_agree_policy3).length() + getResources().getString(R.string.regist_agree_policy4).length() + 3, 34);
        }
        return spannableStringBuilder;
    }

    public static String getVerName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != R.id.backAB) {
            return;
        }
        finish();
    }
}
