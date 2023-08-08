package com.home.activity.set.smart;

import android.os.Bundle;
import android.view.View;
import androidx.exifinterface.media.ExifInterface;
import com.common.uitl.NumberHelper;
import com.home.base.LedBleActivity;
import com.home.net.NetConnectBle;
import com.home.view.wheel.OnWheelChangedListener;
import com.home.view.wheel.WheelModelAdapter;
import com.home.view.wheel.WheelView;
import com.ledlamp.R;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/* loaded from: classes.dex */
public class OtherSettingActivity extends LedBleActivity {
    private int day;
    private int hour;
    private WheelView listViewHour;
    private WheelView listViewMinute;
    private WheelView listViewSecond;
    private int minute;
    private int second;
    private WheelModelAdapter wheelAdapterH;
    private WheelModelAdapter wheelAdapterM;
    private WheelModelAdapter wheelAdapterS;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_other_setting);
        Date date = new Date(System.currentTimeMillis());
        List asList = Arrays.asList(getResources().getStringArray(R.array.week));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("mm");
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("ss");
        String format = new SimpleDateFormat(ExifInterface.LONGITUDE_EAST).format(date);
        this.hour = Integer.parseInt(simpleDateFormat.format(date).trim());
        this.minute = Integer.parseInt(simpleDateFormat2.format(date).trim());
        this.second = Integer.parseInt(simpleDateFormat3.format(date).trim());
        this.day = 0;
        if (format.equals(asList.get(0))) {
            this.day = 1;
        } else if (format.equals(asList.get(1))) {
            this.day = 2;
        } else if (format.equals(asList.get(2))) {
            this.day = 3;
        } else if (format.equals(asList.get(3))) {
            this.day = 4;
        } else if (format.equals(asList.get(4))) {
            this.day = 5;
        } else if (format.equals(asList.get(5))) {
            this.day = 6;
        } else if (format.equals(asList.get(6))) {
            this.day = 7;
        }
        this.listViewSecond = (WheelView) findViewById(R.id.listViewSecond);
        String[] strArr = new String[24];
        for (int i = 0; i < 24; i++) {
            strArr[i] = NumberHelper.LeftPad_Tow_Zero(i);
        }
        this.wheelAdapterH = new WheelModelAdapter(this, strArr);
        WheelView wheelView = (WheelView) findViewById(R.id.listViewHour);
        this.listViewHour = wheelView;
        wheelView.setViewAdapter(this.wheelAdapterH);
        this.listViewHour.setCurrentItem(this.hour);
        this.listViewHour.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.OtherSettingActivity.1
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView2, int i2, int i3) {
                OtherSettingActivity.this.hour = i3;
            }
        });
        String[] strArr2 = new String[60];
        for (int i2 = 0; i2 < 60; i2++) {
            strArr2[i2] = NumberHelper.LeftPad_Tow_Zero(i2);
        }
        this.wheelAdapterM = new WheelModelAdapter(this, strArr2);
        WheelView wheelView2 = (WheelView) findViewById(R.id.listViewMinute);
        this.listViewMinute = wheelView2;
        wheelView2.setViewAdapter(this.wheelAdapterM);
        this.listViewMinute.setCurrentItem(this.minute);
        this.listViewMinute.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.OtherSettingActivity.2
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView3, int i3, int i4) {
                OtherSettingActivity.this.minute = i4;
            }
        });
        String[] strArr3 = new String[60];
        for (int i3 = 0; i3 < 60; i3++) {
            strArr3[i3] = NumberHelper.LeftPad_Tow_Zero(i3);
        }
        this.wheelAdapterS = new WheelModelAdapter(this, strArr3);
        WheelView wheelView3 = (WheelView) findViewById(R.id.listViewSecond);
        this.listViewSecond = wheelView3;
        wheelView3.setViewAdapter(this.wheelAdapterS);
        this.listViewSecond.setCurrentItem(this.second);
        this.listViewSecond.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.OtherSettingActivity.3
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView4, int i4, int i5) {
                OtherSettingActivity.this.second = i5;
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.activity.set.smart.OtherSettingActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.buttonCancell) {
                    OtherSettingActivity.this.finish();
                } else if (id != R.id.textViewOKButton) {
                } else {
                    NetConnectBle.getInstanceByGroup("").setSmartTimeSet(OtherSettingActivity.this.hour, OtherSettingActivity.this.minute, OtherSettingActivity.this.second, OtherSettingActivity.this.day);
                    OtherSettingActivity.this.finish();
                }
            }
        };
        findViewById(R.id.buttonCancell).setOnClickListener(onClickListener);
        findViewById(R.id.textViewOKButton).setOnClickListener(onClickListener);
    }
}
