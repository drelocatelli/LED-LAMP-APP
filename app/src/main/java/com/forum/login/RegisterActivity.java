package com.forum.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
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
import androidx.core.app.NotificationCompat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.common.uitl.StringUtils;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.other.PrivacyPolicyActivity;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.utils.Utils;
import com.ledlamp.R;
import java.util.HashMap;

/* loaded from: classes.dex */
public class RegisterActivity extends LedBleActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private Button btnRegister;
    private boolean check = false;
    private CheckBox checkbox;
    private EditText clearEtCheckCode;
    private EditText clearEtCheckMode;
    private EditText clearEtName;
    private EditText clearEtPassWord;
    private ImageView ivBack;
    private LinearLayout llCheckAgreement;
    private ImageView releseCharacter;
    private TextView tvCheckcode;
    private TextView tvSpan;
    private TextView tvTitle;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_register);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewAdd);
        this.releseCharacter = imageView;
        imageView.setVisibility(8);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        this.clearEtName = (EditText) findViewById(R.id.clearEtName);
        this.clearEtPassWord = (EditText) findViewById(R.id.clearEtPassWord);
        this.clearEtCheckMode = (EditText) findViewById(R.id.clearEtCheckMode);
        this.clearEtCheckCode = (EditText) findViewById(R.id.clearEtCheckCode);
        this.tvCheckcode = (TextView) findViewById(R.id.tvCheckcode);
        this.btnRegister = (Button) findViewById(R.id.btnRegister);
        this.tvTitle.setText(getString(R.string.register));
        this.ivBack.setOnClickListener(this);
        this.tvCheckcode.setOnClickListener(this);
        this.btnRegister.setOnClickListener(this);
        this.llCheckAgreement = (LinearLayout) findViewById(R.id.llCheckAgreement);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        this.checkbox = checkBox;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.forum.login.RegisterActivity.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                RegisterActivity.this.check = z;
            }
        });
        TextView textView = (TextView) findViewById(R.id.tvSpan);
        this.tvSpan = textView;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        this.tvSpan.setHighlightColor(getResources().getColor(17170445));
        this.tvSpan.setText(getBuilder());
    }

    private SpannableStringBuilder getBuilder() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.regist_agree_policy));
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: com.forum.login.RegisterActivity.2
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this.getApplicationContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.ServiceAgreement);
                RegisterActivity.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        String stringExtra = getIntent().getStringExtra("name");
        if (stringExtra.contains("关于我们") || stringExtra.contains("關於我們")) {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.regist_agree_policy1).length(), getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length(), 34);
        } else {
            spannableStringBuilder.setSpan(clickableSpan, getResources().getString(R.string.regist_agree_policy1).length(), getResources().getString(R.string.regist_agree_policy1).length() + getResources().getString(R.string.regist_agree_policy2).length() + 1, 34);
        }
        ClickableSpan clickableSpan2 = new ClickableSpan() { // from class: com.forum.login.RegisterActivity.3
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this.getApplicationContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.PricavyPolicy);
                RegisterActivity.this.startActivity(intent);
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

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        String obj = this.clearEtName.getText().toString();
        String obj2 = this.clearEtPassWord.getText().toString();
        String trim = this.clearEtCheckMode.getText().toString().trim();
        String trim2 = this.clearEtCheckCode.getText().toString().trim();
        Log.e(TAG, "onClick: Name :" + obj + "word:" + obj2 + "checkMode:" + trim + "chcode:" + trim2);
        int id = view.getId();
        if (id == R.id.btnRegister) {
            if (!this.check) {
                this.llCheckAgreement.startAnimation(AnimationUtils.loadAnimation(this, R.anim.layout_scale));
            } else if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(obj2) || TextUtils.isEmpty(trim) || TextUtils.isEmpty(trim2)) {
                Toast.makeText(this, getString(R.string.input_not_null), 0).show();
            } else {
                if (obj.length() >= 6 && obj2.length() >= 6) {
                    trim2.length();
                }
                String encrypt = new PasswordUtil().encrypt(obj2);
                HashMap hashMap = new HashMap();
                hashMap.put("userName", obj);
                hashMap.put("password", encrypt);
                hashMap.put("checkMode", trim);
                hashMap.put("verifyCode", trim2);
                HttpUtil.getInstance().getSourceData(true, this, Constant.registerByApp, hashMap, new HttpUtil.HttpCallBack() { // from class: com.forum.login.RegisterActivity.5
                    @Override // com.home.http.HttpUtil.HttpCallBack
                    public void onSuccess(String str) {
                        com.home.http.ResponseBean responseBean = (com.home.http.ResponseBean) JSON.parseObject(str, new TypeReference<com.home.http.ResponseBean<UserBean>>() { // from class: com.forum.login.RegisterActivity.5.1
                        }, new Feature[0]);
                        if (responseBean != null) {
                            if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                                RegisterActivity registerActivity = RegisterActivity.this;
                                Toast.makeText(registerActivity, registerActivity.getString(R.string.register_success), 0).show();
                                RegisterActivity.this.finish();
                                return;
                            }
                            RegisterActivity registerActivity2 = RegisterActivity.this;
                            Toast.makeText(registerActivity2, StringUtils.getReturnCodeString(registerActivity2.getBaseContext(), responseBean.getReturnCode()), 0).show();
                        }
                    }

                    @Override // com.home.http.HttpUtil.HttpCallBack
                    public void onException(String str) {
                        RegisterActivity registerActivity = RegisterActivity.this;
                        Toast.makeText(registerActivity, registerActivity.getString(R.string.request_failed), 0).show();
                    }
                });
            }
        } else if (id == R.id.ivBack) {
            finish();
        } else if (id != R.id.tvCheckcode) {
        } else {
            if (TextUtils.isEmpty(obj)) {
                Toast.makeText(this, getString(R.string.input_account), 0).show();
            } else if (!Utils.isEmail(trim) && !Utils.isMobileNo(trim)) {
                Toast.makeText(this, getString(R.string.account_format_error), 0).show();
            } else {
                HashMap hashMap2 = new HashMap();
                String encrypt2 = new PasswordUtil().encrypt(trim);
                String str = Utils.isEmail(trim) ? NotificationCompat.CATEGORY_EMAIL : "phone";
                hashMap2.put("userName", obj);
                hashMap2.put("checkMode", trim);
                hashMap2.put("safetyCode", encrypt2);
                hashMap2.put("type", str);
                HttpUtil.getInstance().getSourceData(true, this, Constant.getVerifyCodeByApp, hashMap2, new HttpUtil.HttpCallBack() { // from class: com.forum.login.RegisterActivity.4
                    @Override // com.home.http.HttpUtil.HttpCallBack
                    public void onSuccess(String str2) {
                        com.home.http.ResponseBean responseBean = (com.home.http.ResponseBean) JSON.parseObject(str2, new TypeReference<com.home.http.ResponseBean<UserBean>>() { // from class: com.forum.login.RegisterActivity.4.1
                        }, new Feature[0]);
                        if (responseBean != null) {
                            if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                                RegisterActivity.this.tvCheckcode.setBackgroundColor(RegisterActivity.this.getResources().getColor(R.color.gray));
                                RegisterActivity registerActivity = RegisterActivity.this;
                                new TimeCountUtil(registerActivity, registerActivity.tvCheckcode, 60).countTime();
                                RegisterActivity registerActivity2 = RegisterActivity.this;
                                Toast.makeText(registerActivity2, registerActivity2.getString(R.string.send_checkcode), 0).show();
                                return;
                            }
                            RegisterActivity registerActivity3 = RegisterActivity.this;
                            Toast.makeText(registerActivity3, StringUtils.getReturnCodeString(registerActivity3.getBaseContext(), responseBean.getReturnCode()), 0).show();
                        }
                    }

                    @Override // com.home.http.HttpUtil.HttpCallBack
                    public void onException(String str2) {
                        RegisterActivity registerActivity = RegisterActivity.this;
                        Toast.makeText(registerActivity, registerActivity.getString(R.string.request_failed), 0).show();
                    }
                });
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finish();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
