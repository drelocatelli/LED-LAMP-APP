package com.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.forum.login.PasswordUtil;
import com.forum.login.UserBean;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.ledlamp.R;
import java.util.HashMap;

/* loaded from: classes.dex */
public class UpdatePasswordActivity extends LedBleActivity implements View.OnClickListener {
    private Button btnUpdate;
    private EditText clearEtNew;
    private EditText clearEtOld;
    private ImageView ivBack;
    private ImageView releseCharacter;
    private TextView tvTitle;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_update_password);
        ImageView imageView = (ImageView) findViewById(R.id.ivBack);
        this.ivBack = imageView;
        imageView.setOnClickListener(this);
        TextView textView = (TextView) findViewById(R.id.tvTitle);
        this.tvTitle = textView;
        textView.setText(getString(R.string.update_password));
        ImageView imageView2 = (ImageView) findViewById(R.id.imageViewAdd);
        this.releseCharacter = imageView2;
        imageView2.setVisibility(8);
        Button button = (Button) findViewById(R.id.btnUpdate);
        this.btnUpdate = button;
        button.setOnClickListener(this);
        this.clearEtOld = (EditText) findViewById(R.id.clearEtOld);
        this.clearEtNew = (EditText) findViewById(R.id.clearEtNew);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btnUpdate) {
            if (id != R.id.ivBack) {
                return;
            }
            finish();
            return;
        }
        String obj = this.clearEtOld.getText().toString();
        String obj2 = this.clearEtNew.getText().toString();
        if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(obj2)) {
            Toast.makeText(this, getString(R.string.input_password), 0).show();
        } else if (obj.length() < 6 || obj2.length() < 6) {
            Toast.makeText(this, getString(R.string.input_length_error), 0).show();
        } else {
            String encrypt = new PasswordUtil().encrypt(obj);
            String encrypt2 = new PasswordUtil().encrypt(obj2);
            HashMap hashMap = new HashMap();
            hashMap.put(SharedPreferencesKey.SP_KEY_TOKEN, getBaseApp().getUserToken());
            hashMap.put("oldPassword", encrypt);
            hashMap.put("password", encrypt2);
            HttpUtil.getInstance().getSourceData(true, this, Constant.updatePswdByApp, hashMap, new HttpUtil.HttpCallBack() { // from class: com.me.UpdatePasswordActivity.1
                @Override // com.home.http.HttpUtil.HttpCallBack
                public void onSuccess(String str) {
                    ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<UserBean>>() { // from class: com.me.UpdatePasswordActivity.1.1
                    }, new Feature[0]);
                    if (responseBean != null) {
                        if (Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                            UpdatePasswordActivity updatePasswordActivity = UpdatePasswordActivity.this;
                            Toast.makeText(updatePasswordActivity, updatePasswordActivity.getString(R.string.update_password_success), 0).show();
                            UpdatePasswordActivity.this.finish();
                            return;
                        }
                        Toast.makeText(UpdatePasswordActivity.this, responseBean.getReturnDesc(), 0).show();
                    }
                }

                @Override // com.home.http.HttpUtil.HttpCallBack
                public void onException(String str) {
                    UpdatePasswordActivity updatePasswordActivity = UpdatePasswordActivity.this;
                    Toast.makeText(updatePasswordActivity, updatePasswordActivity.getString(R.string.request_failed), 0).show();
                }
            });
        }
    }
}
