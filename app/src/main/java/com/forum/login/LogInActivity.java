package com.forum.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.FirstActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.uitl.StringUtils;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.other.PrivacyPolicyActivity;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.ledlamp.R;
import java.util.HashMap;

/* loaded from: classes.dex */
public class LogInActivity extends LedBleActivity implements View.OnClickListener {
    private static SharedPreferences sp;
    EditText accountet;
    ImageView back;
    private Button btnLogin;
    private boolean check = false;
    private CheckBox checkbox;
    private LinearLayout llCheckAgreement;
    EditText passwordet;
    private TextView tvRegister;
    private TextView tvRetrieve;
    private TextView tvSpan;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        setContentView(R.layout.activity_log_in);
        ImageView imageView = (ImageView) findViewById(R.id.back);
        this.back = imageView;
        imageView.setOnClickListener(this);
        this.accountet = (EditText) findViewById(R.id.et_account);
        this.passwordet = (EditText) findViewById(R.id.et_password);
        this.llCheckAgreement = (LinearLayout) findViewById(R.id.llCheckAgreement);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        this.checkbox = checkBox;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.forum.login.LogInActivity.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                LogInActivity.this.check = z;
            }
        });
        Button button = (Button) findViewById(R.id.btnLogin);
        this.btnLogin = button;
        button.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.tvRetrieve);
        this.tvRetrieve = textView;
        textView.setOnClickListener(this);
        TextView textView2 = (TextView) findViewById(R.id.tvRegister);
        this.tvRegister = textView2;
        textView2.setOnClickListener(this);
        TextView textView3 = (TextView) findViewById(R.id.tvSpan);
        this.tvSpan = textView3;
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
        this.tvSpan.setHighlightColor(getResources().getColor(17170445));
        this.tvSpan.setText(getBuilder());
    }

    private SpannableStringBuilder getBuilder() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.regist_agree_policy));
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: com.forum.login.LogInActivity.2
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this.getApplicationContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.ServiceAgreement);
                LogInActivity.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        String string = getResources().getString(R.string.about_us_and_help);
        if (string.contains("关于我们") || string.contains("關於我們")) {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.regist_agree_policy1).length(), getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.regist_agree_policy1).length(), getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + 1, 34);
        }
        ClickableSpan clickableSpan2 = new ClickableSpan() { // from class: com.forum.login.LogInActivity.3
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this.getApplicationContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.PricavyPolicy);
                LogInActivity.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        if (string.contains("关于我们") || string.contains("關於我們")) {
            spannableStringBuilder.setSpan(clickableSpan2, getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + getResources().getString(R.string.regist_agree_policy3).length(), getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + getResources().getString(R.string.regist_agree_policy3).length() + getResources().getString(R.string.regist_agree_policy4).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan2, getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + getResources().getString(R.string.regist_agree_policy3).length() + 2, getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + getResources().getString(R.string.regist_agree_policy3).length() + getResources().getString(R.string.regist_agree_policy4).length() + 3, 34);
        }
        return spannableStringBuilder;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back /* 2131296333 */:
                finish();
                return;
            case R.id.btnLogin /* 2131296390 */:
                if (!this.check) {
                    this.llCheckAgreement.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_scale));
                    return;
                }
                final String trim = this.accountet.getText().toString().trim();
                final String trim2 = this.passwordet.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    Toast.makeText(this, getString(R.string.input_account), 0).show();
                    return;
                } else if (TextUtils.isEmpty(trim2)) {
                    Toast.makeText(this, getString(R.string.input_password), 0).show();
                    return;
                } else {
                    String encrypt = new PasswordUtil().encrypt(trim2);
                    HashMap hashMap = new HashMap();
                    hashMap.put("userName", trim);
                    hashMap.put("password", encrypt);
                    HttpUtil.getInstance().getSourceData(true, this, Constant.loginByApp, hashMap, new HttpUtil.HttpCallBack() { // from class: com.forum.login.LogInActivity.4
                        @Override // com.home.http.HttpUtil.HttpCallBack
                        public void onSuccess(String str) {
                            ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<UserBean>>() { // from class: com.forum.login.LogInActivity.4.1
                            }, new Feature[0]);
                            if (responseBean != null) {
                                if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                                    UserBean userBean = (UserBean) responseBean.getContent();
                                    if (userBean != null && userBean.getToken() != null) {
                                        new LedBleApplication().setUserBean(userBean);
                                    }
                                    LogInActivity.putString(LogInActivity.this, "account", trim);
                                    LogInActivity.putString(LogInActivity.this, "password", trim2);
                                    LogInActivity.this.setResult(FirstActivity.RESULT444);
                                    LogInActivity.this.finish();
                                    return;
                                }
                                LogInActivity logInActivity = LogInActivity.this;
                                Toast.makeText(logInActivity, StringUtils.getReturnCodeString(logInActivity.getBaseContext(), responseBean.getReturnCode()), 0).show();
                            }
                        }

                        @Override // com.home.http.HttpUtil.HttpCallBack
                        public void onException(String str) {
                            LogInActivity logInActivity = LogInActivity.this;
                            Toast.makeText(logInActivity, logInActivity.getString(R.string.request_failed), 0).show();
                        }
                    });
                    return;
                }
            case R.id.tvRegister /* 2131297774 */:
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("name", getResources().getString(R.string.about_us_and_help));
                startActivity(intent);
                return;
            case R.id.tvRetrieve /* 2131297777 */:
                Intent intent2 = new Intent(getApplicationContext(), RetrieveActivity.class);
                intent2.putExtra("name", getResources().getString(R.string.about_us_and_help));
                startActivity(intent2);
                return;
            case R.id.tvService /* 2131297786 */:
                Intent intent3 = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                intent3.putExtra("scene", Constant.ServiceAgreement);
                startActivity(intent3);
                return;
            default:
                return;
        }
    }

    @Override // com.home.base.LedBleActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (getString(this, "account", "").equals("") || getString(this, "password", "").equals("")) {
            return;
        }
        this.accountet.setText(getString(this, "account", "0000"));
        this.passwordet.setText(getString(this, "password", "0000"));
    }

    public static void putString(Context context, String str, String str2) {
        SharedPreferences.Editor edit = getSp(context).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public static String getString(Context context, String str, String str2) {
        return getSp(context).getString(str, str2);
    }

    private static SharedPreferences getSp(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("SpUtil", 0);
        }
        return sp;
    }
}
