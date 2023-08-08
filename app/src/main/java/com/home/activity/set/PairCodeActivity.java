package com.home.activity.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.common.uitl.NumberHelper;
import com.common.uitl.SharePersistent;
import com.common.view.TextViewBorder;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleActivity;
import com.home.view.wheel.OnWheelChangedListener;
import com.home.view.wheel.WheelModelAdapter;
import com.home.view.wheel.WheelView;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class PairCodeActivity extends LedBleActivity {
    private WheelView listViewModel;
    private int model;
    private TextViewBorder textViewModel;
    private WheelModelAdapter wheelAdapterModel;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_pair_code);
        this.textViewModel = (TextViewBorder) findViewById(R.id.textViewModel);
        this.listViewModel = (WheelView) findViewById(R.id.listViewModel);
        String[] strArr = new String[256];
        for (int i = 0; i < 256; i++) {
            strArr[i] = NumberHelper.LeftPad_Tow_Zero(i);
        }
        WheelModelAdapter wheelModelAdapter = new WheelModelAdapter(this, strArr);
        this.wheelAdapterModel = wheelModelAdapter;
        this.listViewModel.setViewAdapter(wheelModelAdapter);
        this.listViewModel.setCurrentItem(SharePersistent.getInt(getApplicationContext(), "PAIR-CODE-BLE"));
        this.listViewModel.addChangingListener(new OnWheelChangedListener() { // from class: com.home.activity.set.PairCodeActivity.1
            @Override // com.home.view.wheel.OnWheelChangedListener
            public void onChanged(WheelView wheelView, int i2, int i3) {
                PairCodeActivity.this.model = i3;
                PairCodeActivity.this.textViewModel.setText(PairCodeActivity.this.getResources().getString(R.string.pair_code_set, Integer.valueOf(i3)));
                SharePersistent.savePerference(PairCodeActivity.this.getApplicationContext(), "PAIR-CODE-BLE", PairCodeActivity.this.model - 1);
            }
        });
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.home.activity.set.PairCodeActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.buttonCancell) {
                    PairCodeActivity.this.finish();
                } else if (id != R.id.textViewOKButton) {
                } else {
                    if (MainActivity_BLE.getMainActivity() != null) {
                        MainActivity_BLE.getMainActivity().setPairCode(PairCodeActivity.this.model);
                    }
                    PairCodeActivity.this.putDataback();
                }
            }
        };
        findViewById(R.id.buttonCancell).setOnClickListener(onClickListener);
        findViewById(R.id.textViewOKButton).setOnClickListener(onClickListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putDataback() {
        setResult(-1, new Intent());
        finish();
    }
}
