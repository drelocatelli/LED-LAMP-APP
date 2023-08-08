package com.home.activity.set;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.common.uitl.NumberHelper;
import com.common.uitl.SharePersistent;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleActivity;
import com.home.bean.SceneBean;
import com.home.view.wheel.OnWheelChangedListener;
import com.home.view.wheel.WheelModelAdapter;
import com.home.view.wheel.WheelView;
import com.ledlamp.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ChannelSetActivity extends LedBleActivity {
    public static SceneBean sceneBean;
    private ArrayList<Integer> arrayList;
    private ArrayList<Integer> arrayTempList;
    private WheelView listViewCrystal3;
    private WheelView listViewGreen3;
    private WheelView listViewLightblue3;
    private WheelView listViewPink3;
    private WheelView listViewRed3;
    private WheelView listViewWhite3;
    private int tempValue;
    private WheelModelAdapter wheelAdapterModel;
    private int rValue = 255;
    private int gValue = 255;
    private int bValue = 255;
    private int wValue = 255;
    private int yValue = 255;
    private int pValue = 255;
    private String modelText = "";
    private String RGB = "RGBDataBase";
    private String RGBW = "RGBWDataBase";
    private String RGBWC = "RGBWC";
    private String RGBWCP = "RGBWCPDataBase";

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_channel_set);
        this.arrayList = new ArrayList<>();
        this.arrayTempList = new ArrayList<>();
        this.listViewRed3 = (WheelView) findViewById(R.id.listViewModel31);
        this.listViewGreen3 = (WheelView) findViewById(R.id.listViewModel32);
        this.listViewLightblue3 = (WheelView) findViewById(R.id.listViewModel33);
        this.listViewWhite3 = (WheelView) findViewById(R.id.listViewModel34);
        this.listViewCrystal3 = (WheelView) findViewById(R.id.listViewModel35);
        this.listViewPink3 = (WheelView) findViewById(R.id.listViewModel36);
        String[] strArr = new String[24];
        for (int i = 0; i < 24; i++) {
            strArr[i] = NumberHelper.LeftPad_Tow_Zero(i);
        }
        final String[] stringArray = getApplication().getResources().getStringArray(R.array.channel_set_model);
        String[] strArr2 = new String[stringArray.length];
        for (int i2 = 0; i2 < stringArray.length; i2++) {
            strArr2[i2] = stringArray[i2].split(",")[0];
        }
        WheelModelAdapter wheelModelAdapter = new WheelModelAdapter(this, strArr2);
        this.wheelAdapterModel = wheelModelAdapter;
        this.listViewRed3.setViewAdapter(wheelModelAdapter);
        this.listViewRed3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.ChannelSetActivity.1
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                String[] split = stringArray[i4].split(",");
                ChannelSetActivity.this.rValue = Integer.parseInt(split[1].trim());
                ChannelSetActivity.this.arrayList.add(Integer.valueOf(ChannelSetActivity.this.rValue));
                if (ChannelSetActivity.this.rValue == ChannelSetActivity.this.tempValue) {
                    Toast.makeText(ChannelSetActivity.this.getApplication(), ChannelSetActivity.this.getString(R.string.Values_can_be_the_same), 0).show();
                }
                ChannelSetActivity channelSetActivity = ChannelSetActivity.this;
                channelSetActivity.tempValue = channelSetActivity.rValue;
            }
        });
        this.listViewGreen3.setViewAdapter(this.wheelAdapterModel);
        this.listViewGreen3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.ChannelSetActivity.2
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                String[] split = stringArray[i4].split(",");
                ChannelSetActivity.this.gValue = Integer.parseInt(split[1].trim());
                ChannelSetActivity.this.arrayList.add(Integer.valueOf(ChannelSetActivity.this.rValue));
                if (ChannelSetActivity.this.gValue == ChannelSetActivity.this.tempValue) {
                    Toast.makeText(ChannelSetActivity.this.getApplication(), ChannelSetActivity.this.getString(R.string.Values_can_be_the_same), 0).show();
                }
                ChannelSetActivity channelSetActivity = ChannelSetActivity.this;
                channelSetActivity.tempValue = channelSetActivity.gValue;
            }
        });
        this.listViewLightblue3.setViewAdapter(this.wheelAdapterModel);
        this.listViewLightblue3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.ChannelSetActivity.3
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                String[] split = stringArray[i4].split(",");
                ChannelSetActivity.this.bValue = Integer.parseInt(split[1].trim());
                ChannelSetActivity.this.arrayList.add(Integer.valueOf(ChannelSetActivity.this.rValue));
                if (ChannelSetActivity.this.bValue == ChannelSetActivity.this.tempValue) {
                    Toast.makeText(ChannelSetActivity.this.getApplication(), ChannelSetActivity.this.getString(R.string.Values_can_be_the_same), 0).show();
                }
                ChannelSetActivity channelSetActivity = ChannelSetActivity.this;
                channelSetActivity.tempValue = channelSetActivity.bValue;
            }
        });
        this.listViewWhite3.setViewAdapter(this.wheelAdapterModel);
        this.listViewWhite3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.ChannelSetActivity.4
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                String[] split = stringArray[i4].split(",");
                ChannelSetActivity.this.wValue = Integer.parseInt(split[1].trim());
                ChannelSetActivity.this.arrayList.add(Integer.valueOf(ChannelSetActivity.this.wValue));
                if (ChannelSetActivity.this.wValue == ChannelSetActivity.this.tempValue) {
                    Toast.makeText(ChannelSetActivity.this.getApplication(), ChannelSetActivity.this.getString(R.string.Values_can_be_the_same), 0).show();
                }
                ChannelSetActivity channelSetActivity = ChannelSetActivity.this;
                channelSetActivity.tempValue = channelSetActivity.wValue;
            }
        });
        this.listViewPink3.setViewAdapter(this.wheelAdapterModel);
        this.listViewPink3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.ChannelSetActivity.5
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                String[] split = stringArray[i4].split(",");
                ChannelSetActivity.this.pValue = Integer.parseInt(split[1].trim());
                ChannelSetActivity.this.arrayList.add(Integer.valueOf(ChannelSetActivity.this.pValue));
                if (ChannelSetActivity.this.wValue == ChannelSetActivity.this.tempValue) {
                    Toast.makeText(ChannelSetActivity.this.getApplication(), ChannelSetActivity.this.getString(R.string.Values_can_be_the_same), 0).show();
                }
                ChannelSetActivity channelSetActivity = ChannelSetActivity.this;
                channelSetActivity.tempValue = channelSetActivity.pValue;
            }
        });
        this.listViewCrystal3.setViewAdapter(this.wheelAdapterModel);
        this.listViewCrystal3.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.ChannelSetActivity.6
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i3, int i4) {
                String[] split = stringArray[i4].split(",");
                ChannelSetActivity.this.yValue = Integer.parseInt(split[1].trim());
                ChannelSetActivity.this.arrayList.add(Integer.valueOf(ChannelSetActivity.this.yValue));
                if (ChannelSetActivity.this.wValue == ChannelSetActivity.this.tempValue) {
                    Toast.makeText(ChannelSetActivity.this.getApplication(), ChannelSetActivity.this.getString(R.string.Values_can_be_the_same), 0).show();
                }
                ChannelSetActivity channelSetActivity = ChannelSetActivity.this;
                channelSetActivity.tempValue = channelSetActivity.yValue;
            }
        });
        if (MainActivity_BLE.getMainActivity() != null) {
            int i3 = SharePersistent.getInt(getApplicationContext(), "CH_R_STAGE");
            if (i3 > 0 && i3 != 255) {
                this.listViewRed3.setCurrentItem(i3);
            }
            int i4 = SharePersistent.getInt(getApplicationContext(), "CH_G_STAGE");
            if (i4 > 0 && i4 != 255) {
                this.listViewGreen3.setCurrentItem(i4);
            }
            int i5 = SharePersistent.getInt(getApplicationContext(), "CH_B_STAGE");
            if (i5 > 0 && i5 != 255) {
                this.listViewLightblue3.setCurrentItem(i5);
            }
            int i6 = SharePersistent.getInt(getApplicationContext(), "CH_W_STAGE");
            if (i6 > 0 && i6 != 255) {
                this.listViewWhite3.setCurrentItem(i6);
            }
            int i7 = SharePersistent.getInt(getApplicationContext(), "CH_P_STAGE");
            if (i7 > 0 && i7 != 255) {
                this.listViewPink3.setCurrentItem(i7);
            }
            int i8 = SharePersistent.getInt(getApplicationContext(), "CH_Y_STAGE");
            if (i8 > 0 && i8 != 255) {
                this.listViewCrystal3.setCurrentItem(i8);
            }
        }
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.activity.set.ChannelSetActivity.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.buttonCancell) {
                    ChannelSetActivity.this.finish();
                } else if (id != R.id.buttonSave) {
                } else {
                    if (MainActivity_BLE.getMainActivity() != null) {
                        MainActivity_BLE.getMainActivity().SetCHN(ChannelSetActivity.this.rValue, ChannelSetActivity.this.gValue, ChannelSetActivity.this.bValue, ChannelSetActivity.this.wValue, ChannelSetActivity.this.yValue, ChannelSetActivity.this.pValue);
                        SharePersistent.savePerference(MainActivity_BLE.getMainActivity(), "CH_R_STAGE", ChannelSetActivity.this.rValue);
                        SharePersistent.savePerference(MainActivity_BLE.getMainActivity(), "CH_G_STAGE", ChannelSetActivity.this.gValue);
                        SharePersistent.savePerference(MainActivity_BLE.getMainActivity(), "CH_B_STAGE", ChannelSetActivity.this.bValue);
                        SharePersistent.savePerference(MainActivity_BLE.getMainActivity(), "CH_W_STAGE", ChannelSetActivity.this.wValue);
                        SharePersistent.savePerference(MainActivity_BLE.getMainActivity(), "CH_P_STAGE", ChannelSetActivity.this.pValue);
                        SharePersistent.savePerference(MainActivity_BLE.getMainActivity(), "CH_Y_STAGE", ChannelSetActivity.this.yValue);
                    }
                    ChannelSetActivity.this.finish();
                }
            }
        };
        findViewById(R.id.buttonCancell).setOnClickListener(onClickListener);
        findViewById(R.id.buttonSave).setOnClickListener(onClickListener);
    }
}
