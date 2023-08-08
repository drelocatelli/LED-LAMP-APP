package com.home.activity.set;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.uitl.SharePersistent;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleActivity;
import com.ledlamp.R;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemScrollListener;
import com.weigan.loopview.OnItemSelectedListener;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RgbSortActivity extends LedBleActivity {
    private ImageView backButton;
    private LoopView listViewModel;
    private int model;
    private TextView tvTitle;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_rgb_sort);
        ImageView imageView = (ImageView) findViewById(R.id.backcode);
        this.backButton = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.RgbSortActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                RgbSortActivity.this.finish();
            }
        });
        this.tvTitle = (TextView) findViewById(R.id.tvTitle);
        findViewById(R.id.textViewOKButton).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.set.RgbSortActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (view.getId() != R.id.textViewOKButton) {
                    return;
                }
                if (MainActivity_BLE.getMainActivity() != null) {
                    MainActivity_BLE.getMainActivity().setRgbSort(RgbSortActivity.this.model);
                }
                RgbSortActivity.this.putDataback();
            }
        });
        final String[] stringArray = getResources().getStringArray(R.array.rgb_sort_ble);
        this.tvTitle.setText(getResources().getString(R.string.rgb_sort_set, new String[stringArray.length][SharePersistent.getInt(getApplicationContext(), "RGBDataBase-SORT-BLE")]));
        ArrayList arrayList = new ArrayList();
        for (String str : stringArray) {
            arrayList.add(str.split(",")[0]);
        }
        LoopView loopView = (LoopView) findViewById(R.id.listViewModel);
        this.listViewModel = loopView;
        loopView.setListener(new OnItemSelectedListener() { // from class: com.home.activity.set.RgbSortActivity.3
            @Override // com.weigan.loopview.OnItemSelectedListener
            public void onItemSelected(int i) {
                String[] split = stringArray[i].split(",");
                RgbSortActivity.this.model = Integer.parseInt(split[1].replaceAll(" ", "").trim());
                RgbSortActivity.this.tvTitle.setText(RgbSortActivity.this.getResources().getString(R.string.rgb_sort_set, split[0]));
                SharePersistent.savePerference(RgbSortActivity.this.getApplicationContext(), "RGBDataBase-SORT-BLE", RgbSortActivity.this.model - 1);
            }
        });
        this.listViewModel.setOnItemScrollListener(new OnItemScrollListener() { // from class: com.home.activity.set.RgbSortActivity.4
            @Override // com.weigan.loopview.OnItemScrollListener
            public void onItemScrollStateChanged(LoopView loopView2, int i, int i2, int i3, int i4) {
                Log.i("gy", String.format("onItemScrollStateChanged currentPassItem %d  oldScrollState %d  scrollState %d  totalScrollY %d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)));
            }

            @Override // com.weigan.loopview.OnItemScrollListener
            public void onItemScrolling(LoopView loopView2, int i, int i2, int i3) {
                Log.i("gy", String.format("onItemScrolling currentPassItem %d  scrollState %d  totalScrollY %d", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)));
            }
        });
        this.listViewModel.setItems(arrayList);
        this.listViewModel.setInitPosition(SharePersistent.getInt(getApplicationContext(), "RGBDataBase-SORT-BLE"));
        this.model = SharePersistent.getInt(getApplicationContext(), "RGBDataBase-SORT-BLE");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void putDataback() {
        setResult(-1, new Intent());
        finish();
    }
}
