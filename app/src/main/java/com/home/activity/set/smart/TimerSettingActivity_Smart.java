package com.home.activity.set.smart;

import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.common.uitl.NumberHelper;
import com.home.activity.set.smart.modeTime.TimeModle;
import com.home.base.LedBleActivity;
import com.home.fragment.ble.LineFragment;
import com.home.view.wheel.OnWheelChangedListener;
import com.home.view.wheel.WheelModelAdapter;
import com.home.view.wheel.WheelView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class TimerSettingActivity_Smart extends LedBleActivity {
    private int crystalValue;
    private int getHour;
    int getId;
    private int getMinite;
    private int greenValue;
    private int hour;
    int i;
    private int lightblueValue;
    private LinearLayout linearTime;
    private LinearLayout linearTimeSet;
    private WheelView listViewCrystal3;
    private WheelView listViewGreen3;
    private WheelView listViewH3;
    private WheelView listViewLightblue3;
    private WheelView listViewM3;
    private WheelView listViewPink3;
    private WheelView listViewRed3;
    private WheelView listViewWhite3;
    private int minite;
    private int model;
    private String modelText = "";
    private int pinkValue;
    private int redValue;
    private int tag;
    private TextView textViewID;
    TimeModle timeModle;
    private View viewColorBlue;
    private View viewColorCyan;
    private View viewColorGreen;
    private View viewColorPurple;
    private View viewColorRed;
    private View viewColorWhite;
    private WheelModelAdapter wheelAdapterH;
    private WheelModelAdapter wheelAdapterM;
    private WheelModelAdapter wheelAdapterModel;
    private int whiteValue;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_timer_setting_smart);
        this.getId = getIntent().getIntExtra("tag", 0);
        this.timeModle = (TimeModle) getIntent().getSerializableExtra("data");
        int intExtra = getIntent().getIntExtra("666", 123);
        this.i = intExtra;
        if (intExtra != 666) {
            Time time = new Time();
            time.setToNow();
            this.hour = time.hour;
            this.minite = time.minute;
        }
        this.listViewH3 = (WheelView) findViewById(R.id.listViewH3);
        this.listViewM3 = (WheelView) findViewById(R.id.listViewM3);
        this.listViewRed3 = (WheelView) findViewById(R.id.listViewModel31);
        this.listViewGreen3 = (WheelView) findViewById(R.id.listViewModel32);
        this.listViewLightblue3 = (WheelView) findViewById(R.id.listViewModel33);
        this.listViewWhite3 = (WheelView) findViewById(R.id.listViewModel34);
        this.listViewCrystal3 = (WheelView) findViewById(R.id.listViewModel35);
        this.listViewPink3 = (WheelView) findViewById(R.id.listViewModel36);
        this.linearTime = (LinearLayout) findViewById(R.id.linearTime);
        this.linearTimeSet = (LinearLayout) findViewById(R.id.linearTimeSet);
        this.viewColorRed = findViewById(R.id.viewColorRed);
        this.viewColorGreen = findViewById(R.id.viewColorGreen);
        this.viewColorBlue = findViewById(R.id.viewColorBlue);
        this.viewColorWhite = findViewById(R.id.viewColorWhite);
        this.viewColorCyan = findViewById(R.id.viewColorCyan);
        this.viewColorPurple = findViewById(R.id.viewColorPurple);
        if (this.getId == 0) {
            if (LineFragment.lineshow == 3) {
                this.linearTimeSet.setVisibility(4);
            } else {
                this.linearTimeSet.setVisibility(8);
            }
        }
        if (LineFragment.lineshow == 1) {
            this.listViewRed3.setVisibility(8);
            this.listViewGreen3.setVisibility(8);
            this.listViewLightblue3.setVisibility(8);
            this.listViewWhite3.setVisibility(0);
            this.viewColorRed.setVisibility(8);
            this.viewColorGreen.setVisibility(8);
            this.viewColorBlue.setVisibility(8);
            this.viewColorWhite.setVisibility(0);
        } else if (LineFragment.lineshow == 2) {
            this.listViewRed3.setVisibility(8);
            this.listViewGreen3.setVisibility(8);
            this.listViewLightblue3.setVisibility(0);
            this.listViewWhite3.setVisibility(0);
            this.viewColorRed.setVisibility(8);
            this.viewColorGreen.setVisibility(8);
            this.viewColorBlue.setVisibility(0);
            this.viewColorWhite.setVisibility(0);
        } else if (LineFragment.lineshow == 4 || LineFragment.lineshow == 5) {
            this.listViewWhite3.setVisibility(0);
            this.viewColorWhite.setVisibility(0);
        } else {
            this.listViewWhite3.setVisibility(8);
            this.viewColorWhite.setVisibility(8);
        }
        if (LineFragment.lineshow == 5) {
            this.listViewCrystal3.setVisibility(0);
            this.listViewPink3.setVisibility(0);
            this.viewColorCyan.setVisibility(0);
            this.viewColorPurple.setVisibility(0);
        } else {
            this.listViewCrystal3.setVisibility(8);
            this.listViewPink3.setVisibility(8);
            this.viewColorCyan.setVisibility(8);
            this.viewColorPurple.setVisibility(8);
        }
        String[] strArr = new String[24];
        for (int i = 0; i < 24; i++) {
            strArr[i] = NumberHelper.LeftPad_Tow_Zero(i);
        }
        WheelModelAdapter wheelModelAdapter = new WheelModelAdapter(this, strArr);
        this.wheelAdapterH = wheelModelAdapter;
        this.listViewH3.setViewAdapter(wheelModelAdapter);
        this.listViewH3.setCurrentItem(this.hour);
        this.listViewH3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.1
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i2, int i3) {
                TimerSettingActivity_Smart.this.hour = i3;
            }
        });
        String[] strArr2 = new String[60];
        for (int i2 = 0; i2 < 60; i2++) {
            strArr2[i2] = NumberHelper.LeftPad_Tow_Zero(i2);
        }
        WheelModelAdapter wheelModelAdapter2 = new WheelModelAdapter(this, strArr2);
        this.wheelAdapterM = wheelModelAdapter2;
        this.listViewM3.setViewAdapter(wheelModelAdapter2);
        this.listViewM3.setCurrentItem(this.minite);
        this.listViewM3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.2
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                TimerSettingActivity_Smart.this.minite = i4;
            }
        });
        String[] strArr3 = new String[101];
        for (int i3 = 0; i3 <= 100; i3++) {
            strArr3[i3] = NumberHelper.LeftPad_Tow_Zero(i3);
        }
        WheelModelAdapter wheelModelAdapter3 = new WheelModelAdapter(this, strArr3);
        this.wheelAdapterModel = wheelModelAdapter3;
        this.listViewWhite3.setViewAdapter(wheelModelAdapter3);
        this.listViewWhite3.setCurrentItem(0);
        this.listViewWhite3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.3
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i4, int i5) {
                TimerSettingActivity_Smart.this.whiteValue = i5;
            }
        });
        this.listViewLightblue3.setViewAdapter(this.wheelAdapterModel);
        this.listViewLightblue3.setCurrentItem(0);
        this.listViewLightblue3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.4
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i4, int i5) {
                TimerSettingActivity_Smart.this.lightblueValue = i5;
            }
        });
        this.listViewRed3.setViewAdapter(this.wheelAdapterModel);
        this.listViewRed3.setCurrentItem(0);
        this.listViewRed3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.5
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i4, int i5) {
                TimerSettingActivity_Smart.this.redValue = i5;
            }
        });
        this.listViewGreen3.setViewAdapter(this.wheelAdapterModel);
        this.listViewGreen3.setCurrentItem(0);
        this.listViewGreen3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.6
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i4, int i5) {
                TimerSettingActivity_Smart.this.greenValue = i5;
            }
        });
        this.listViewPink3.setViewAdapter(this.wheelAdapterModel);
        this.listViewPink3.setCurrentItem(0);
        this.listViewPink3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.7
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i4, int i5) {
                TimerSettingActivity_Smart.this.pinkValue = i5;
            }
        });
        this.listViewCrystal3.setViewAdapter(this.wheelAdapterModel);
        this.listViewCrystal3.setCurrentItem(0);
        this.listViewCrystal3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.8
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i4, int i5) {
                TimerSettingActivity_Smart.this.crystalValue = i5;
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.activity.set.smart.TimerSettingActivity_Smart.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.buttonCancell) {
                    TimerSettingActivity_Smart.this.finish();
                } else if (id != R.id.buttonSave) {
                } else {
                    TimerSettingActivity_Smart.this.save();
                }
            }
        };
        findViewById(R.id.buttonCancell).setOnClickListener(onClickListener);
        findViewById(R.id.buttonSave).setOnClickListener(onClickListener);
        TextView textView = (TextView) findViewById(R.id.textViewID);
        this.textViewID = textView;
        if (this.i == 666) {
            textView.setText("ID:" + String.valueOf(this.getId + 1));
            this.listViewH3.setCurrentItem(this.timeModle.getHour());
            this.listViewM3.setCurrentItem(this.timeModle.getMinute());
            this.listViewRed3.setCurrentItem(this.timeModle.getRed());
            this.listViewGreen3.setCurrentItem(this.timeModle.getGreen());
            this.listViewLightblue3.setCurrentItem(this.timeModle.getBlude());
            this.listViewWhite3.setCurrentItem(this.timeModle.getWhite());
            this.listViewCrystal3.setCurrentItem(this.timeModle.getCyan());
            this.listViewPink3.setCurrentItem(this.timeModle.getViolet());
            return;
        }
        this.listViewH3.setCurrentItem(this.hour);
        this.listViewM3.setCurrentItem(this.minite);
        this.listViewRed3.setCurrentItem(0);
        this.listViewGreen3.setCurrentItem(0);
        this.listViewLightblue3.setCurrentItem(0);
        this.listViewWhite3.setCurrentItem(0);
        this.listViewCrystal3.setCurrentItem(0);
        this.listViewPink3.setCurrentItem(0);
    }

    public void save() {
        boolean z;
        boolean z2 = false;
        if (LineFragment.listDate_line.size() != 0) {
            for (int i = 0; i < LineFragment.listDate_line.size(); i++) {
                if (i != this.getId && this.hour == LineFragment.listDate_line.get(i).getHour() && this.minite == LineFragment.listDate_line.get(i).getMinute()) {
                    z = false;
                    break;
                }
            }
        }
        z = true;
        if (z) {
            if (LineFragment.listDate_line.size() == 0 || this.getId == 0 || this.hour != 0 || this.minite != 0) {
                z2 = true;
            } else {
                Toast.makeText(this, (int) R.string.timeday, 1).show();
            }
            if (z2) {
                if (this.i == 666) {
                    if (LineFragment.lineshow == 1) {
                        LineFragment.listDate_line.set(this.getId, new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue, this.whiteValue));
                    } else if (LineFragment.lineshow == 2) {
                        LineFragment.listDate_line.set(this.getId, new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue, this.whiteValue));
                    } else if (LineFragment.lineshow == 3) {
                        LineFragment.listDate_line.set(this.getId, new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue));
                    } else if (LineFragment.lineshow == 4) {
                        LineFragment.listDate_line.set(this.getId, new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue, this.whiteValue));
                    } else if (LineFragment.lineshow == 5) {
                        LineFragment.listDate_line.set(this.getId, new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue, this.whiteValue, this.crystalValue, this.pinkValue));
                    }
                } else {
                    if (LineFragment.listDate_line.size() == 0 && this.hour != 0 && this.minite != 0) {
                        if (LineFragment.lineshow == 1) {
                            LineFragment.listDate_line.add(new TimeModle(0, 0, 0, 0, 0, 0));
                        } else if (LineFragment.lineshow == 2) {
                            LineFragment.listDate_line.add(new TimeModle(0, 0, 0, 0, 0, 0));
                        } else if (LineFragment.lineshow == 3) {
                            LineFragment.listDate_line.add(new TimeModle(0, 0, 0, 0, 0));
                        } else if (LineFragment.lineshow == 4) {
                            LineFragment.listDate_line.add(new TimeModle(0, 0, 0, 0, 0, 0));
                        } else if (LineFragment.lineshow == 5) {
                            LineFragment.listDate_line.add(new TimeModle(0, 0, 0, 0, 0, 0, 0, 0));
                        }
                    }
                    if (LineFragment.lineshow == 1) {
                        LineFragment.listDate_line.add(new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue, this.whiteValue));
                    } else if (LineFragment.lineshow == 2) {
                        LineFragment.listDate_line.add(new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue, this.whiteValue));
                    } else if (LineFragment.lineshow == 3) {
                        LineFragment.listDate_line.add(new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue));
                    } else if (LineFragment.lineshow == 4) {
                        LineFragment.listDate_line.add(new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue, this.whiteValue));
                    } else if (LineFragment.lineshow == 5) {
                        LineFragment.listDate_line.add(new TimeModle(this.hour, this.minite, this.redValue, this.greenValue, this.lightblueValue, this.whiteValue, this.crystalValue, this.pinkValue));
                    }
                }
                setResult(-1);
                finish();
                return;
            }
            return;
        }
        Toast.makeText(this, (int) R.string.timeday, 1).show();
    }
}
