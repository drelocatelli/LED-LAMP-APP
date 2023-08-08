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
import com.home.constant.CommonConstant;
import com.home.fragment.sun.TimerFragment_sun;
import com.home.view.wheel.OnWheelChangedListener;
import com.home.view.wheel.WheelModelAdapter;
import com.home.view.wheel.WheelView;
import com.ledlamp.R;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class ChioceTimeActivity extends LedBleActivity implements View.OnClickListener {
    AlertDialog dialogmode;
    AlertDialog dialogweek;
    Map<Integer, String> hashMap = new HashMap();
    private int hour;
    String ledmode;
    String ledweek;
    WheelView listHour;
    WheelView listMinute;
    private int minute;
    private AlertDialog.Builder modeBuilder;
    public String[] modeble;
    ImageView no;
    RecordMode recordMode;
    RelativeLayout rl_mode;
    RelativeLayout rl_week;
    TextView tvmode;
    TextView tvweek;
    private AlertDialog.Builder weekBuilder;
    WheelModelAdapter wheelAdapterH;
    WheelModelAdapter wheelAdapterM;
    ImageView yes;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        setContentView(R.layout.activity_chioce_time);
        this.recordMode = (RecordMode) getIntent().getSerializableExtra("key");
        init();
    }

    public void init() {
        this.tvmode = (TextView) findViewById(R.id.modetv);
        this.tvweek = (TextView) findViewById(R.id.weektv);
        if (LedBleApplication.getApp().getSceneBean().contains("DMX02") || LedBleApplication.getApp().getSceneBean().contains("DMX03")) {
            this.modeble = new String[2];
            for (int i = 0; i < 2; i++) {
                this.modeble[i] = getResources().getStringArray(R.array.timer_model_dmx)[i];
            }
        }
        RecordMode recordMode = this.recordMode;
        if (recordMode != null) {
            if (recordMode.getMode() != null) {
                TextView textView = this.tvmode;
                textView.setText("" + TimeActivity.modeble[Integer.parseInt(this.recordMode.getMode())]);
            }
            if (this.recordMode.getWeek() != null) {
                String[] stringArray = getResources().getStringArray(R.array.week);
                String[] split = this.recordMode.getWeek().split(" ");
                StringBuffer stringBuffer = new StringBuffer(100);
                if (split.length > 0) {
                    for (int i2 = 0; i2 < split.length; i2++) {
                        stringBuffer.append(stringArray[Integer.parseInt(split[i2])] + " ");
                    }
                }
                this.tvweek.setText(stringBuffer.toString());
            }
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
        for (int i3 = 0; i3 < 24; i3++) {
            strArr[i3] = NumberHelper.LeftPad_Tow_Zero(i3);
        }
        WheelModelAdapter wheelModelAdapter = new WheelModelAdapter(this, strArr);
        this.wheelAdapterH = wheelModelAdapter;
        this.listHour.setViewAdapter(wheelModelAdapter);
        this.listHour.setCurrentItem(this.hour);
        this.listHour.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.timer.ChioceTimeActivity.1
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i4, int i5) {
                ChioceTimeActivity.this.hour = i5;
            }
        });
        String[] strArr2 = new String[60];
        for (int i4 = 0; i4 < 60; i4++) {
            strArr2[i4] = NumberHelper.LeftPad_Tow_Zero(i4);
        }
        WheelModelAdapter wheelModelAdapter2 = new WheelModelAdapter(this, strArr2);
        this.wheelAdapterM = wheelModelAdapter2;
        this.listMinute.setViewAdapter(wheelModelAdapter2);
        this.listMinute.setCurrentItem(this.minute);
        this.listMinute.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.timer.ChioceTimeActivity.2
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i5, int i6) {
                ChioceTimeActivity.this.minute = i6;
            }
        });
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
                    MyDataBase myDataBase = MyDataBase.getInstance(this);
                    String sceneBean = LedBleApplication.getApp().getSceneBean();
                    int i = this.hour;
                    int i2 = this.minute;
                    String str = this.ledmode;
                    String str2 = this.ledweek;
                    myDataBase.update(sceneBean, i, i2, str, str2, 1, "_id=?", new String[]{this.recordMode.getId() + ""});
                    finish();
                    return;
                } else if (LedBleApplication.getApp().getSceneBean().contains(CommonConstant.LEDSTAGE) || LedBleApplication.getApp().getSceneBean().contains(CommonConstant.LEDLIGHT)) {
                    if (this.ledmode != null) {
                        MyDataBase.getInstance(this).insert(LedBleApplication.getApp().getSceneBean(), this.hour, this.minute, this.ledmode, this.ledweek, 1);
                        finish();
                        return;
                    }
                    Toast.makeText(this, (int) R.string.enter, 1).show();
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
        if (this.modeBuilder == null) {
            this.modeBuilder = new AlertDialog.Builder(this);
        }
        if (LedBleApplication.getApp().getSceneBean().contains("DMX03") || LedBleApplication.getApp().getSceneBean().contains("DMX02")) {
            this.modeBuilder.setTitle(R.string.choosemode);
            this.modeBuilder.setSingleChoiceItems(this.modeble, getSharedPreferences("mode", 0).getInt("mode", 0), new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.ChioceTimeActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ChioceTimeActivity.this.dialogmode.dismiss();
                    TextView textView = ChioceTimeActivity.this.tvmode;
                    textView.setText("" + ChioceTimeActivity.this.modeble[i]);
                    ChioceTimeActivity chioceTimeActivity = ChioceTimeActivity.this;
                    chioceTimeActivity.ledmode = "" + i;
                }
            });
        } else if (LedBleApplication.getApp().getSceneBean().contains(CommonConstant.LEDSUN)) {
            this.modeBuilder.setTitle(R.string.choosemode);
            this.modeBuilder.setSingleChoiceItems(TimerFragment_sun.modeble, getSharedPreferences("mode", 0).getInt("mode", 0), new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.ChioceTimeActivity.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ChioceTimeActivity.this.dialogmode.dismiss();
                    ChioceTimeActivity.this.tvmode.setText(TimerFragment_sun.modeble[i]);
                    ChioceTimeActivity chioceTimeActivity = ChioceTimeActivity.this;
                    chioceTimeActivity.ledmode = "" + i;
                }
            });
        } else {
            this.modeBuilder.setTitle(R.string.choosemode);
            this.modeBuilder.setSingleChoiceItems(TimeActivity.modeble, getSharedPreferences("mode", 0).getInt("mode", 0), new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.ChioceTimeActivity.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    ChioceTimeActivity.this.dialogmode.dismiss();
                    ChioceTimeActivity.this.tvmode.setText(TimeActivity.modeble[i]);
                    ChioceTimeActivity chioceTimeActivity = ChioceTimeActivity.this;
                    chioceTimeActivity.ledmode = "" + i;
                }
            });
        }
        AlertDialog create = this.modeBuilder.create();
        this.dialogmode = create;
        create.show();
    }

    public void week() {
        if (this.weekBuilder == null) {
            this.weekBuilder = new AlertDialog.Builder(this);
        }
        this.weekBuilder.setTitle(R.string.choose);
        String[] stringArray = getResources().getStringArray(R.array.weekday);
        final String[] stringArray2 = getResources().getStringArray(R.array.week);
        this.weekBuilder.setMultiChoiceItems(stringArray, (boolean[]) null, new DialogInterface.OnMultiChoiceClickListener() { // from class: com.home.activity.set.timer.ChioceTimeActivity.6
            @Override // android.content.DialogInterface.OnMultiChoiceClickListener
            public void onClick(DialogInterface dialogInterface, int i, boolean z) {
                if (z) {
                    ChioceTimeActivity.this.hashMap.put(Integer.valueOf(i), stringArray2[i]);
                } else {
                    ChioceTimeActivity.this.hashMap.remove(Integer.valueOf(i));
                }
            }
        });
        this.weekBuilder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.ChioceTimeActivity.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuffer stringBuffer = new StringBuffer(100);
                StringBuffer stringBuffer2 = new StringBuffer(100);
                if (ChioceTimeActivity.this.hashMap.size() > 0) {
                    for (Map.Entry<Integer, String> entry : ChioceTimeActivity.this.hashMap.entrySet()) {
                        String value = entry.getValue();
                        Integer key = entry.getKey();
                        stringBuffer.append(((Object) value) + " ");
                        stringBuffer2.append(key + " ");
                    }
                    ChioceTimeActivity.this.tvweek.setText(stringBuffer);
                    ChioceTimeActivity.this.ledweek = stringBuffer2.toString();
                }
                ChioceTimeActivity.this.dialogweek.dismiss();
            }
        });
        this.weekBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.home.activity.set.timer.ChioceTimeActivity.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ChioceTimeActivity.this.dialogweek.dismiss();
            }
        });
        AlertDialog create = this.weekBuilder.create();
        this.dialogweek = create;
        create.show();
    }
}
