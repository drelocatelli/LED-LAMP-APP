package com.video;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.example.linechartlibrary.SharedPreferencesKey;
import com.home.base.LedBleActivity;
import com.home.constant.Constant;
import com.home.http.HttpUtil;
import com.home.http.ResponseBean;
import com.home.utils.Utils;
import com.ledlamp.R;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_Publish75;
    EditText et_Content75;
    private Map<String, File> files = new HashMap();
    ImageView iv_Back75;
    private Map<String, String> params;
    String replyId;
    String replyType;
    String target;
    String token;
    TextView tv_Title75;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_comment2);
        this.btn_Publish75 = (Button) findViewById(R.id.btn_Publish75);
        this.et_Content75 = (EditText) findViewById(R.id.et_Content75);
        this.tv_Title75 = (TextView) findViewById(R.id.tv_Title75);
        this.iv_Back75 = (ImageView) findViewById(R.id.iv_Back75);
        this.btn_Publish75.setOnClickListener(this);
        this.iv_Back75.setOnClickListener(this);
        this.token = LedBleActivity.getBaseApp().getUserToken();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.replyId = extras.getString("replyId");
            this.replyType = extras.getString("replyType");
            this.target = extras.getString("target");
            if (this.replyType.equals("reply")) {
                this.tv_Title75.setText(R.string.reply);
            } else {
                this.tv_Title75.setText(R.string.comment);
            }
        }
        this.params = new HashMap();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btn_Publish75) {
            if (id != R.id.iv_Back75) {
                return;
            }
            finish();
        } else if (!this.token.equals("") && !TextUtils.isEmpty(this.replyId)) {
            replyContent();
        } else {
            setResult(119);
            finish();
        }
    }

    public void replyContent() {
        if (TextUtils.isEmpty(this.et_Content75.getText())) {
            Toast.makeText(this, (int) R.string.text, 0).show();
            return;
        }
        this.params.put(SharedPreferencesKey.SP_KEY_TOKEN, this.token);
        this.params.put("replyId", this.replyId);
        this.params.put("replyType", this.replyType);
        this.params.put("target", this.target);
        this.params.put(Utils.RESPONSE_CONTENT, this.et_Content75.getText().toString());
        uploading();
    }

    public void uploading() {
        HttpUtil.getInstance().getSourceData(true, this, Constant.publishShortVideoReply, this.params, new HttpUtil.HttpCallBack() { // from class: com.video.ReviewActivity.1
            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onSuccess(String str) {
                Log.e("--", "onSuccess: " + str);
                ResponseBean responseBean = (ResponseBean) JSON.parseObject(str, new TypeReference<ResponseBean<String>>() { // from class: com.video.ReviewActivity.1.1
                }, new Feature[0]);
                if (responseBean == null || !Constant.SUCCESS_CODE.equals(responseBean.getReturnCode())) {
                    return;
                }
                Toast.makeText(ReviewActivity.this.getApplicationContext(), (int) R.string.comment_publish_success, 0).show();
                ReviewActivity.this.et_Content75.setText("");
                if (ReviewActivity.this.replyType.equals("reply")) {
                    ReviewActivity.this.setResult(10);
                } else {
                    ReviewActivity.this.setResult(11);
                }
                ReviewActivity.this.finish();
            }

            @Override // com.home.http.HttpUtil.HttpCallBack
            public void onException(String str) {
                Log.e("---", "onException: " + str);
                Toast.makeText(ReviewActivity.this.getApplicationContext(), (int) R.string.request_failed, 0).show();
            }
        });
    }
}
