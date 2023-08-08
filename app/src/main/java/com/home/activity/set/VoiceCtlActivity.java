package com.home.activity.set;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.uitl.SharePersistent;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleActivity;
import com.itheima.wheelpicker.WheelPicker;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class VoiceCtlActivity extends LedBleActivity {
    private static final String TAG = "VoiceCtlActivity";
    private ImageView backVctl;
    private SeekBar seekBarSpeedBar;
    private TextView textViewSpeed;
    private WheelPicker wheelPicker;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_voicectl);
        ImageView imageView = (ImageView) findViewById(R.id.backVctl);
        this.backVctl = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.VoiceCtlActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                VoiceCtlActivity.this.finish();
            }
        });
        WheelPicker wheelPicker = (WheelPicker) findViewById(R.id.wheelPicker);
        this.wheelPicker = wheelPicker;
        wheelPicker.setData(buildBLEModel());
        this.wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.activity.set.VoiceCtlActivity.2
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker2, Object obj, int i) {
                if (i >= 0) {
                    MainActivity_BLE.getMainActivity().setVoiceCtlAndMusicMode(true, false, false, Integer.parseInt(((String) VoiceCtlActivity.this.buildBLEModel().get(i)).split(" ")[1].trim()));
                }
            }
        });
        this.seekBarSpeedBar = (SeekBar) findViewById(R.id.seekBarSpeed);
        this.textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        this.seekBarSpeedBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.activity.set.VoiceCtlActivity.3
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    if (i >= 1 && i <= 100) {
                        MainActivity_BLE.getMainActivity().setSensitivity(i, false, false);
                        VoiceCtlActivity.this.textViewSpeed.setText(VoiceCtlActivity.this.getResources().getString(R.string.sensitivity_set, Integer.valueOf(i)));
                    }
                    SharePersistent.saveInt(VoiceCtlActivity.this, "VoiceCtlActivitysensitivity", i);
                }
            }
        });
        int i = SharePersistent.getInt(this, "VoiceCtlActivitysensitivity");
        if (i > 0) {
            this.seekBarSpeedBar.setProgress(i);
            this.textViewSpeed.setText(getResources().getString(R.string.sensitivity_set, String.valueOf(i)));
            return;
        }
        this.textViewSpeed.setText(getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
        this.seekBarSpeedBar.setProgress(90);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> buildBLEModel() {
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= 255; i++) {
            arrayList.add("MODE " + i);
        }
        return arrayList;
    }
}
