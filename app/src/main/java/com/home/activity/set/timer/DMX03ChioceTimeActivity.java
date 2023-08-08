package com.home.activity.set.timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.common.uitl.NumberHelper;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.set.timer.sql.MyDataBase;
import com.home.activity.set.timer.sql.RecordMode;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.view.wheel.OnWheelChangedListener;
import com.home.view.wheel.WheelModelAdapter;
import com.home.view.wheel.WheelView;
import com.ledlamp.R;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class DMX03ChioceTimeActivity extends LedBleActivity implements View.OnClickListener {
    AlertDialog dialogmode;
    AlertDialog dialogweek;
    Map<Integer, String> hashMap = new HashMap();
    private int hour;
    String ledmode;
    String ledweek;
    WheelView listHour;
    WheelView listMinute;
    private int minute;
    public String[] modeble;
    ImageView no;
    RecordMode recordMode;
    RelativeLayout rl_mode;
    RelativeLayout rl_week;
    TextView tvmode;
    TextView tvweek;
    WheelModelAdapter wheelAdapterH;
    WheelModelAdapter wheelAdapterM;
    ImageView yes;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        setContentView(R.layout.dmx03_activity_chioce_time);
        this.recordMode = (RecordMode) getIntent().getSerializableExtra("key");
        init();
    }

    public void init() {
        this.tvmode = (TextView) findViewById(R.id.modetv);
        this.tvweek = (TextView) findViewById(R.id.weektv);
        RecordMode recordMode = this.recordMode;
        if (recordMode != null) {
            this.tvmode.setText(recordMode.getMode());
            this.tvweek.setText(this.recordMode.getWeek());
        }
        this.yes = (ImageView) findViewById(R.id.yes);
        this.no = (ImageView) findViewById(R.id.no);
        this.rl_mode = (RelativeLayout) findViewById(R.id.rl_mode);
        this.rl_week = (RelativeLayout) findViewById(R.id.rl_week);
        this.yes.setOnClickListener(this);
        this.no.setOnClickListener(this);
        this.rl_week.setOnClickListener(this);
        this.rl_mode.setOnClickListener(this);
        if (LedBleApplication.getApp().getSceneBean().contains("LIGHT") || LedBleApplication.getApp().getSceneBean().contains("STAGE")) {
            this.rl_week.setVisibility(8);
        }
        RecordMode recordMode2 = this.recordMode;
        if (recordMode2 != null) {
            this.hour = recordMode2.getHour();
            this.minute = this.recordMode.getMinute();
        } else {
            Time time = new Time();
            time.setToNow();
            this.hour = time.hour;
            this.minute = time.minute;
        }
        this.listHour = (WheelView) findViewById(R.id.listHour);
        this.listMinute = (WheelView) findViewById(R.id.listMinute);
        String[] strArr = new String[24];
        for (int i = 0; i < 24; i++) {
            strArr[i] = NumberHelper.LeftPad_Tow_Zero(i);
        }
        WheelModelAdapter wheelModelAdapter = new WheelModelAdapter(this, strArr);
        this.wheelAdapterH = wheelModelAdapter;
        this.listHour.setViewAdapter(wheelModelAdapter);
        this.listHour.setCurrentItem(this.hour);
        this.listHour.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.timer.DMX03ChioceTimeActivity.1
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i2, int i3) {
                DMX03ChioceTimeActivity.this.hour = i3;
            }
        });
        String[] strArr2 = new String[60];
        for (int i2 = 0; i2 < 60; i2++) {
            strArr2[i2] = NumberHelper.LeftPad_Tow_Zero(i2);
        }
        WheelModelAdapter wheelModelAdapter2 = new WheelModelAdapter(this, strArr2);
        this.wheelAdapterM = wheelModelAdapter2;
        this.listMinute.setViewAdapter(wheelModelAdapter2);
        this.listMinute.setCurrentItem(this.minute);
        this.listMinute.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.timer.DMX03ChioceTimeActivity.2
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                DMX03ChioceTimeActivity.this.minute = i4;
            }
        });
        if (LedBleApplication.getApp().getSceneBean().contains("DMX02") || LedBleApplication.getApp().getSceneBean().contains("DMX03")) {
            this.modeble = new String[2];
            for (int i3 = 0; i3 < 2; i3++) {
                this.modeble[i3] = getResources().getStringArray(R.array.timer_model_dmx)[i3];
            }
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.no /* 2131297098 */:
                finish();
                return;
            case R.id.rl_mode /* 2131297335 */:
                mode();
                return;
            case R.id.rl_week /* 2131297345 */:
                this.hashMap.clear();
                week();
                return;
            case R.id.yes /* 2131298036 */:
                if (this.recordMode != null) {
                    MyDataBase.getInstance(this).update(LedBleApplication.getApp().getSceneBean(), this.hour, this.minute, (String) this.tvmode.getText(), (String) this.tvweek.getText(), 1, "_id=?", new String[]{this.recordMode.getId() + ""});
                    finish();
                    return;
                } else if (this.ledmode != null && this.ledweek != null) {
                    MyDataBase.getInstance(this).insert(LedBleApplication.getApp().getSceneBean(), this.hour, this.minute, this.ledmode, this.ledweek, 1);
                    finish();
                    return;
                } else {
                    Toast.makeText(this, (int) R.string.enter, 1).show();
                    return;
                }
            default:
                return;
        }
    }

    public void mode() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (LedBleApplication.getApp().getSceneBean().contains("DMX03") || LedBleApplication.getApp().getSceneBean().contains("DMX02")) {
            builder.setTitle(R.string.choosemode);
            builder.setSingleChoiceItems(this.modeble, getSharedPreferences("mode", 0).getInt("mode", 0), new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.DMX03ChioceTimeActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    DMX03ChioceTimeActivity.this.dialogmode.dismiss();
                    TextView textView = DMX03ChioceTimeActivity.this.tvmode;
                    textView.setText("" + DMX03ChioceTimeActivity.this.modeble[i]);
                    DMX03ChioceTimeActivity dMX03ChioceTimeActivity = DMX03ChioceTimeActivity.this;
                    dMX03ChioceTimeActivity.ledmode = dMX03ChioceTimeActivity.modeble[i];
                }
            });
        } else {
            builder.setTitle(R.string.choosemode);
            builder.setSingleChoiceItems(TimeActivity.modeble, getSharedPreferences("mode", 0).getInt("mode", 0), new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.DMX03ChioceTimeActivity.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    DMX03ChioceTimeActivity.this.dialogmode.dismiss();
                    DMX03ChioceTimeActivity.this.tvmode.setText(TimeActivity.modeble[i]);
                    DMX03ChioceTimeActivity.this.ledmode = TimeActivity.modeble[i];
                }
            });
        }
        AlertDialog create = builder.create();
        this.dialogmode = create;
        create.show();
    }

    public void week() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.choose);
        String[] stringArray = getResources().getStringArray(R.array.weekday);
        final String[] stringArray2 = getResources().getStringArray(R.array.week);
        builder.setMultiChoiceItems(stringArray, (boolean[]) null, new DialogInterface.OnMultiChoiceClickListener() { // from class: com.home.activity.set.timer.DMX03ChioceTimeActivity.5
            @Override // android.content.DialogInterface.OnMultiChoiceClickListener
            public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                if (z) {
                    DMX03ChioceTimeActivity.this.hashMap.put(Integer.valueOf(i), stringArray2[i]);
                } else {
                    DMX03ChioceTimeActivity.this.hashMap.remove(Integer.valueOf(i));
                }
            }
        });
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.DMX03ChioceTimeActivity.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuffer stringBuffer = new StringBuffer(100);
                if (DMX03ChioceTimeActivity.this.hashMap.size() > 0) {
                    for (Map.Entry<Integer, String> entry : DMX03ChioceTimeActivity.this.hashMap.entrySet()) {
                        String value = entry.getValue();
                        stringBuffer.append(((Object) value) + " ");
                    }
                    DMX03ChioceTimeActivity.this.tvweek.setText(stringBuffer);
                    DMX03ChioceTimeActivity.this.ledweek = stringBuffer.toString();
                }
                DMX03ChioceTimeActivity.this.dialogweek.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.DMX03ChioceTimeActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                DMX03ChioceTimeActivity.this.dialogweek.dismiss();
            }
        });
        AlertDialog create = builder.create();
        this.dialogweek = create;
        create.show();
    }
}
