package com.home.activity.set.smart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.uitl.SharePersistent;
import com.githang.statusbar.StatusBarCompat;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleActivity;
import com.home.constant.CommonConstant;
import com.itheima.wheelpicker.WheelPicker;
import com.ledlamp.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class ModeSelectActivity extends LedBleActivity {
    private ImageView backButton;
    private WheelPicker listViewModel;
    private int model;
    private String modelStr;
    private TextView tvTitle;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        StatusBarCompat.setStatusBarColor((Activity) this, getResources().getColor(R.color.blue), true);
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_rgb_sort);
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (MainActivity_BLE.getMainActivity() != null && MainActivity_BLE.getSceneBean().equalsIgnoreCase(CommonConstant.LEDSMART)) {
            this.tvTitle.setText(getResources().getString(R.string.mode_select));
        }
        ImageView imageView = (ImageView) findViewById(R.id.backcode);
        this.backButton = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.smart.ModeSelectActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ModeSelectActivity.this.finish();
            }
        });
        final String[] stringArray = getResources().getStringArray(R.array.select_mode);
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray) {
            arrayList.add(str.split(",")[0]);
        }
        WheelPicker wheelPicker = (WheelPicker) findViewById(R.id.listViewModel);
        this.listViewModel = wheelPicker;
        wheelPicker.setData(arrayList);
        this.listViewModel.setItemTextColor(getResources().getColor(R.color.black));
        this.listViewModel.setSelectedItemTextColor(getResources().getColor(R.color.black));
        this.listViewModel.setSelectedItemPosition(SharePersistent.getSmartModeInt(getApplicationContext(), CommonConstant.SELECT_MODE_SMART));
        this.model = SharePersistent.getSmartModeInt(getApplicationContext(), CommonConstant.SELECT_MODE_SMART);
        this.listViewModel.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.activity.set.smart.ModeSelectActivity.2
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker2, Object obj, int i) {
                if (i >= 0) {
                    String[] split = stringArray[i].split(",");
                    ModeSelectActivity.this.model = Integer.parseInt(split[1].replaceAll(" ", "").trim());
                    ModeSelectActivity.this.modelStr = split[0];
                    TextView textView = ModeSelectActivity.this.tvTitle;
                    textView.setText("" + split[0]);
                    MainActivity_BLE.getMainActivity().rgbFragment.setActive(ModeSelectActivity.this.modelStr);
                    MainActivity_BLE.getMainActivity().brightFragment.setActive(ModeSelectActivity.this.modelStr);
                    ModeSelectActivity modeSelectActivity = ModeSelectActivity.this;
                    SharePersistent.savePerference(modeSelectActivity, CommonConstant.SELECT_MODE_SMART, modeSelectActivity.model);
                }
            }
        });
        ((TextView) findViewById(R.id.textViewOKButton)).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.smart.ModeSelectActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (ModeSelectActivity.this.modelStr != null) {
                    ModeSelectActivity modeSelectActivity = ModeSelectActivity.this;
                    SharePersistent.savePerference(modeSelectActivity, CommonConstant.SELECT_MODE_SMART, modeSelectActivity.model);
                    ModeSelectActivity modeSelectActivity2 = ModeSelectActivity.this;
                    SharePersistent.savePerference(modeSelectActivity2, CommonConstant.SELECT_MODE_SMART_STRING, modeSelectActivity2.modelStr);
                    Intent intent = ModeSelectActivity.this.getIntent();
                    intent.putExtra(CommonConstant.SELECT_MODE_SMART_STRING, ModeSelectActivity.this.modelStr);
                    ModeSelectActivity.this.setResult(-1, intent);
                    ModeSelectActivity.this.finish();
                }
            }
        });
    }
}
