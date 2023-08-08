package com.home.activity.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface;
import com.home.activity.main.MainActivity_BLE;
import com.home.http.AdvertBean;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class AdvertActivity extends Activity {
    private AdvertBean advertBean;
    private ImageView ivAdvertImage;
    private CountDownTimer timer;
    private TextView tvText;
    private String TAG = "AdvertActivity";
    private String ADVERT_KEY = "advertKey";

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_advert);
        this.ivAdvertImage = (ImageView) findViewById(R.id.ivAdvertImage);
        this.tvText = (TextView) findViewById(R.id.tvText);
        AdvertBean advertBean = (AdvertBean) getIntent().getSerializableExtra("advertBean");
        this.advertBean = advertBean;
        if (advertBean != null) {
            SharedPreferences sharedPreferences = getSharedPreferences("Advert", 0);
            int i = sharedPreferences.getInt(this.ADVERT_KEY, 0);
            int advertId = this.advertBean.getAdvertId();
            String str = this.TAG;
            Log.d(str, "advertKey:" + i);
            String str2 = this.TAG;
            Log.d(str2, "advertId:" + advertId);
            if (advertId > i) {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putInt(this.ADVERT_KEY, advertId);
                edit.commit();
            } else {
                gotoMainPage();
            }
        } else {
            gotoMainPage();
        }
        this.timer = new CountDownTimer(6000L, 1000L) { // from class: com.home.activity.other.AdvertActivity.1
            @Override // android.os.CountDownTimer
            public void onTick(long j) {
                String str3 = AdvertActivity.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onTick  ");
                long j2 = j / 1000;
                sb.append(j2);
                Log.d(str3, sb.toString());
                TextView textView = AdvertActivity.this.tvText;
                textView.setText(j2 + ExifInterface.LATITUDE_SOUTH);
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                AdvertActivity.this.gotoMainPage();
            }
        };
        findViewById(R.id.llSkip).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.AdvertActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                AdvertActivity.this.gotoMainPage();
            }
        });
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        Log.d(this.TAG, "onResume");
        CountDownTimer countDownTimer = this.timer;
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        CountDownTimer countDownTimer = this.timer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void gotoMainPage() {
        startActivity(new Intent(this, MainActivity_BLE.class));
        finish();
    }
}
