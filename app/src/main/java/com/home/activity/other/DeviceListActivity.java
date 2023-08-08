package com.home.activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.home.adapter.BleSelectDeviceAdapter;
import com.home.base.LedBleActivity;
import com.home.base.LedBleApplication;
import com.home.db.GroupDevice;
import com.ledlamp.R;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class DeviceListActivity extends LedBleActivity {
    private BleSelectDeviceAdapter bleDeviceAdapter;
    private ArrayList<GroupDevice> groupDevices;
    private String groupName = "";
    private ListView listViewDevices;

    @Override // com.home.base.LedBleActivity, me.imid.swipebacklayout.lib.app.SwipeBackActivity, androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_device_list);
        this.groupDevices = (ArrayList) getIntent().getSerializableExtra("devices");
        this.listViewDevices = (ListView) findViewById(R.id.listViewDevices);
        this.bleDeviceAdapter = new BleSelectDeviceAdapter(this);
        if (getIntent().getStringExtra("group") != null && this.bleDeviceAdapter != null) {
            this.groupName = getIntent().getStringExtra("group");
            this.bleDeviceAdapter.setSelected(this.groupDevices);
            this.listViewDevices.setAdapter((ListAdapter) this.bleDeviceAdapter);
        } else if (getIntent().getStringExtra("groupstage") != null && this.bleDeviceAdapter != null) {
            this.groupName = getIntent().getStringExtra("groupstage");
            this.bleDeviceAdapter.setSelected_Stage(this.groupDevices);
            this.listViewDevices.setAdapter((ListAdapter) this.bleDeviceAdapter);
        }
        this.listViewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.home.activity.other.DeviceListActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                DeviceListActivity.this.bleDeviceAdapter.select(DeviceListActivity.this.bleDeviceAdapter.getDevice(i));
                DeviceListActivity.this.bleDeviceAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.textViewBack).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.DeviceListActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DeviceListActivity.this.finish();
            }
        });
        findViewById(R.id.buttonFireBind).setOnClickListener(new View.OnClickListener() { // from class: com.home.activity.other.DeviceListActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LedBleApplication.getApp().setTempDevices(DeviceListActivity.this.bleDeviceAdapter.getSelectSet());
                Intent intent = new Intent();
                if (DeviceListActivity.this.getIntent().getStringExtra("group") != null) {
                    intent.putExtra("group", DeviceListActivity.this.groupName);
                } else if (DeviceListActivity.this.getIntent().getStringExtra("groupstage") != null) {
                    intent.putExtra("groupstage", DeviceListActivity.this.groupName);
                }
                DeviceListActivity.this.setResult(-1, intent);
                DeviceListActivity.this.finish();
            }
        });
    }
}
