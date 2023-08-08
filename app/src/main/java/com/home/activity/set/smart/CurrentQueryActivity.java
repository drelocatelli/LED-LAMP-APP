package com.home.activity.set.smart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.exifinterface.media.ExifInterface;
import com.common.uitl.NumberHelper;
import com.common.uitl.SharePersistent;
import com.home.base.LedBleActivity;
import com.home.constant.CommonConstant;
import com.home.constant.Constant;
import com.home.net.NetConnectBle;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class CurrentQueryActivity extends LedBleActivity {
    private LinearLayout linearLayoutTimer;
    private TextView tvBlue;
    private TextView tvCyan;
    private TextView tvGreen;
    private TextView tvPurple;
    private TextView tvRed;
    private TextView tvTemperature;
    private TextView tvTime;
    private TextView tvWhite;
    private RelativeLayout viewBlue;
    private RelativeLayout viewCyan;
    private RelativeLayout viewGreen;
    private RelativeLayout viewPurple;
    private RelativeLayout viewRed;
    private RelativeLayout viewWhite;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
        setActive();
    }

    public void initView() {
        setContentView(R.layout.activity_current_query);
        this.linearLayoutTimer = (LinearLayout) findViewById(R.id.linearLayoutTimer);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.activity.set.smart.CurrentQueryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.buttonCancell) {
                    CurrentQueryActivity.this.finish();
                } else if (id != R.id.tvQuery) {
                } else {
                    Toast.makeText(CurrentQueryActivity.this.getApplicationContext(), CurrentQueryActivity.this.getString(R.string.currentquery), 0).show();
                    CurrentQueryActivity.this.currentQuery();
                }
            }
        };
        findViewById(R.id.buttonCancell).setOnClickListener(onClickListener);
        findViewById(R.id.tvQuery).setOnClickListener(onClickListener);
        this.viewRed = (RelativeLayout) findViewById(R.id.rl_Red);
        this.viewGreen = (RelativeLayout) findViewById(R.id.rl_Green);
        this.viewBlue = (RelativeLayout) findViewById(R.id.rl_Blue);
        this.viewWhite = (RelativeLayout) findViewById(R.id.rl_White);
        this.viewCyan = (RelativeLayout) findViewById(R.id.rl_Cyan);
        this.viewPurple = (RelativeLayout) findViewById(R.id.rl_Purple);
        this.tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        this.tvTime = (TextView) findViewById(R.id.tvTime);
        this.tvRed = (TextView) findViewById(R.id.tvRed);
        this.tvGreen = (TextView) findViewById(R.id.tvGreen);
        this.tvBlue = (TextView) findViewById(R.id.tvBlue);
        this.tvWhite = (TextView) findViewById(R.id.tvWhite);
        this.tvCyan = (TextView) findViewById(R.id.tvCycan);
        this.tvPurple = (TextView) findViewById(R.id.tvPurple);
    }

    public void setActive() {
        String smartModeString = SharePersistent.getSmartModeString(getApplicationContext(), CommonConstant.SELECT_MODE_SMART_STRING);
        if (smartModeString.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST) || smartModeString.equalsIgnoreCase("BW")) {
            this.viewRed.setVisibility(8);
            this.viewGreen.setVisibility(8);
            this.viewBlue.setVisibility(8);
            this.viewCyan.setVisibility(8);
            this.viewPurple.setVisibility(8);
            this.tvRed.setVisibility(8);
            this.tvGreen.setVisibility(8);
            this.tvBlue.setVisibility(8);
            this.tvCyan.setVisibility(8);
            this.tvPurple.setVisibility(8);
            if (smartModeString.equalsIgnoreCase(ExifInterface.LONGITUDE_WEST)) {
                this.viewWhite.setVisibility(0);
                this.tvWhite.setVisibility(0);
            }
            if (smartModeString.equalsIgnoreCase("BW")) {
                this.viewBlue.setVisibility(0);
                this.viewWhite.setVisibility(0);
                this.tvBlue.setVisibility(0);
                this.tvWhite.setVisibility(0);
            }
        } else if (smartModeString.equalsIgnoreCase("RGB")) {
            this.viewWhite.setVisibility(8);
            this.viewCyan.setVisibility(8);
            this.viewPurple.setVisibility(8);
            this.tvWhite.setVisibility(8);
            this.tvCyan.setVisibility(8);
            this.tvPurple.setVisibility(8);
        } else if (smartModeString.equalsIgnoreCase("RGBW")) {
            this.viewCyan.setVisibility(8);
            this.viewPurple.setVisibility(8);
            this.tvCyan.setVisibility(8);
            this.tvPurple.setVisibility(8);
        } else {
            smartModeString.equalsIgnoreCase("RGBWCP");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void currentQuery() {
        NetConnectBle.getInstance().setSmartCheck(2);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() { // from class: com.home.activity.set.smart.CurrentQueryActivity.2
            @Override // java.lang.Runnable
            public void run() {
                String perference = SharePersistent.getPerference(CurrentQueryActivity.this.getApplicationContext(), Constant.CurrentQueryActivity);
                Log.e("---AAA--", "run: " + perference);
                CurrentQueryActivity.this.getResources().getStringArray(R.array.select_mode);
                if (perference.length() > 0) {
                    String[] split = perference.split("[ ]");
                    for (int i = 0; i < split.length; i++) {
                        if (i == 2) {
                            CurrentQueryActivity.this.tvRed.setText(String.valueOf(Integer.parseInt(split[i], 16)));
                        } else if (i == 3) {
                            CurrentQueryActivity.this.tvGreen.setText(String.valueOf(Integer.parseInt(split[i], 16)));
                        } else if (i == 4) {
                            CurrentQueryActivity.this.tvBlue.setText(String.valueOf(Integer.parseInt(split[i], 16)));
                        } else if (i == 5) {
                            CurrentQueryActivity.this.tvWhite.setText(String.valueOf(Integer.parseInt(split[i], 16)));
                        } else if (i == 6) {
                            CurrentQueryActivity.this.tvCyan.setText(String.valueOf(Integer.parseInt(split[i], 16)));
                        } else if (i == 7) {
                            CurrentQueryActivity.this.tvPurple.setText(String.valueOf(Integer.parseInt(split[i], 16)));
                        } else if (i == 11 || i == 12) {
                            TextView textView = CurrentQueryActivity.this.tvTime;
                            textView.setText(NumberHelper.LeftPad_Tow_Zero(Integer.parseInt(split[11], 16)) + ":" + NumberHelper.LeftPad_Tow_Zero(Integer.parseInt(split[12], 16)));
                        } else if (i == 14) {
                            CurrentQueryActivity.this.tvTemperature.setText(String.valueOf(Integer.parseInt(split[i], 16)));
                        }
                    }
                }
                SharePersistent.savePerference(CurrentQueryActivity.this.getApplicationContext(), Constant.CurrentQueryActivity, "");
                handler.removeCallbacks(this);
            }
        }, 1000L);
    }

    private void putDataback() {
        setResult(-1, new Intent());
        finish();
    }
}
