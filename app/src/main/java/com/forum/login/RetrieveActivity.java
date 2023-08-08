package com.forum.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
public class RetrieveActivity extends LedBleActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private Button btnRegister;
    private EditText clearEtCheckCode;
    private EditText clearEtCheckMode;
    private EditText clearEtName;
    private EditText clearEtPassWord;
    private ImageView ivBack;
    private ImageView releseCharacter;
    private TextView tvCheckcode;
    private TextView tvPassWord;
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
        this.tvPassWord = (TextView) findViewById(R.id.tvPassWord);
        this.clearEtName = (EditText) findViewById(R.id.clearEtName);
        this.clearEtPassWord = (EditText) findViewById(R.id.clearEtPassWord);
        this.clearEtCheckMode = (EditText) findViewById(R.id.clearEtCheckMode);
        this.clearEtCheckCode = (EditText) findViewById(R.id.clearEtCheckCode);
        this.tvCheckcode = (TextView) findViewById(R.id.tvCheckcode);
        this.btnRegister = (Button) findViewById(R.id.btnRegister);
        this.tvTitle.setText(getString(R.string.retrieve_password));
        this.btnRegister.setText(getString(R.string.reset_password));
        this.tvPassWord.setText(getString(R.string.new_password));
        this.ivBack.setOnClickListener(this);
        this.tvCheckcode.setOnClickListener(this);
        this.btnRegister.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.tvSpan);
        this.tvSpan = textView;
        textView.setVisibility(8);
    }

    private SpannableStringBuilder getBuilder() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.regist_agree_policy));
        ClickableSpan clickableSpan = new ClickableSpan() { // from class: com.forum.login.RetrieveActivity.1
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(RetrieveActivity.this.getApplicationContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.ServiceAgreement);
                RetrieveActivity.this.startActivity(intent);
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
        ClickableSpan clickableSpan2 = new ClickableSpan() { // from class: com.forum.login.RetrieveActivity.2
            @Override // android.text.style.ClickableSpan
            public void onClick(View view) {
                Intent intent = new Intent(RetrieveActivity.this.getApplicationContext(), PrivacyPolicyActivity.class);
                intent.putExtra("scene", Constant.PricavyPolicy);
                RetrieveActivity.this.startActivity(intent);
            }

            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.parseColor("#0099EE"));
                textPaint.setUnderlineText(false);
                textPaint.clearShadowLayer();
            }
        };
        if (stringExtra.contains("关于我们")) {
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
        int id = view.getId();
        if (id == R.id.btnRegister) {
            if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(obj2) || TextUtils.isEmpty(trim) || TextUtils.isEmpty(trim2)) {
                Toast.makeText(this, getString(R.string.input_not_null), 0).show();
                return;
            }
            if (obj.length() >= 6 && obj2.length() >= 6) {
                trim2.length();
            }
            String encrypt = new PasswordUtil().encrypt(obj2);
            HashMap hashMap = new HashMap();
            hashMap.put("userName", obj);
            hashMap.put("password", encrypt);
            hashMap.put("checkMode", trim);
            hashMap.put("verifyCode", trim2);
            HttpUtil.getInstance().getSourceData(true, this, Constant.resetPswdByApp, hashMap, new HttpUtil.HttpCallBack() { // from class: com.forum.login.RetrieveActivity.4
                @Override // com.home.http.HttpUtil.HttpCallBack
                public void onSuccess(String str) {
                    com.home.http.ResponseBean responseBean = (com.home.http.ResponseBean) JSON.parseObject(str, new TypeReference<com.home.http.ResponseBean<UserBean>>() { // from class: com.forum.login.RetrieveActivity.4.1
                    }, new Feature[0]);
                    if (responseBean != null) {
                        if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                            RetrieveActivity retrieveActivity = RetrieveActivity.this;
                            Toast.makeText(retrieveActivity, retrieveActivity.getString(R.string.reset_password_success), 0).show();
                            RetrieveActivity.this.finish();
                            return;
                        }
                        RetrieveActivity retrieveActivity2 = RetrieveActivity.this;
                        Toast.makeText(retrieveActivity2, StringUtils.getReturnCodeString(retrieveActivity2.getBaseContext(), responseBean.getReturnCode()), 0).show();
                    }
                }

                @Override // com.home.http.HttpUtil.HttpCallBack
                public void onException(String str) {
                    RetrieveActivity retrieveActivity = RetrieveActivity.this;
                    Toast.makeText(retrieveActivity, retrieveActivity.getString(R.string.request_failed), 0).show();
                }
            });
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
                hashMap2.put("isReset", String.valueOf(1));
                HttpUtil.getInstance().getSourceData(true, this, Constant.getVerifyCodeByApp, hashMap2, new HttpUtil.HttpCallBack() { // from class: com.forum.login.RetrieveActivity.3
                    @Override // com.home.http.HttpUtil.HttpCallBack
                    public void onSuccess(String str2) {
                        com.home.http.ResponseBean responseBean = (com.home.http.ResponseBean) JSON.parseObject(str2, new TypeReference<com.home.http.ResponseBean<UserBean>>() { // from class: com.forum.login.RetrieveActivity.3.1
                        }, new Feature[0]);
                        if (responseBean != null) {
                            if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                                RetrieveActivity.this.tvCheckcode.setBackgroundColor(RetrieveActivity.this.getResources().getColor(R.color.gray));
                                RetrieveActivity retrieveActivity = RetrieveActivity.this;
                                new TimeCountUtil(retrieveActivity, retrieveActivity.tvCheckcode, 60).countTime();
                                RetrieveActivity retrieveActivity2 = RetrieveActivity.this;
                                Toast.makeText(retrieveActivity2, retrieveActivity2.getString(R.string.send_checkcode), 0).show();
                                return;
                            }
                            RetrieveActivity retrieveActivity3 = RetrieveActivity.this;
                            Toast.makeText(retrieveActivity3, StringUtils.getReturnCodeString(retrieveActivity3.getBaseContext(), responseBean.getReturnCode()), 0).show();
                        }
                    }

                    @Override // com.home.http.HttpUtil.HttpCallBack
                    public void onException(String str2) {
                        RetrieveActivity retrieveActivity = RetrieveActivity.this;
                        Toast.makeText(retrieveActivity, retrieveActivity.getString(R.string.request_failed), 0).show();
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
