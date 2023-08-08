package com.home.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.common.uitl.ListUtiles;
import com.home.base.LedBleApplication;
import com.home.constant.CommonConstant;
import com.home.db.GroupDevice;
import com.home.fragment.service.ServicesFragment;
import com.ledlamp.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class BleSelectDeviceAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GroupDevice> groupDevices;
    private Set<BluetoothDevice> hashSet = new HashSet();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public BleSelectDeviceAdapter(Context context) {
        this.context = context;
    }

    public Set<BluetoothDevice> getSelectSet() {
        return this.hashSet;
    }

    public void select(BluetoothDevice bluetoothDevice) {
        if (this.hashSet.contains(bluetoothDevice)) {
            this.hashSet.remove(bluetoothDevice);
        } else {
            this.hashSet.add(bluetoothDevice);
        }
        notifyDataSetChanged();
    }

    public void setSelected(ArrayList<GroupDevice> arrayList) {
        this.groupDevices = arrayList;
        if (!ListUtiles.isEmpty(arrayList)) {
            ArrayList<BluetoothDevice> bleDevices = LedBleApplication.getApp().getBleDevices();
            Iterator<GroupDevice> it = arrayList.iterator();
            while (it.hasNext()) {
                GroupDevice next = it.next();
                Iterator<BluetoothDevice> it2 = bleDevices.iterator();
                while (it2.hasNext()) {
                    BluetoothDevice next2 = it2.next();
                    if (next.getAddress().equals(next2.getAddress())) {
                        this.hashSet.add(next2);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setSelected_Stage(ArrayList<GroupDevice> arrayList) {
        this.groupDevices = arrayList;
        if (!ListUtiles.isEmpty(arrayList)) {
            ArrayList<BluetoothDevice> bleDevices = LedBleApplication.getApp().getBleDevices();
            Iterator<GroupDevice> it = arrayList.iterator();
            while (it.hasNext()) {
                GroupDevice next = it.next();
                Iterator<BluetoothDevice> it2 = bleDevices.iterator();
                while (it2.hasNext()) {
                    BluetoothDevice next2 = it2.next();
                    if (next.getAddress().equals(next2.getAddress())) {
                        this.hashSet.add(next2);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<GroupDevice> getGroupDevices() {
        return this.groupDevices;
    }

    public void setGroupDevices(ArrayList<GroupDevice> arrayList) {
        this.groupDevices = arrayList;
    }

    public BluetoothDevice getDevice(int i) {
        return LedBleApplication.getApp().getBleDevices().get(i);
    }

    public void clear() {
        LedBleApplication.getApp().getBleDevices().clear();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return LedBleApplication.getApp().getBleDevices().size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return LedBleApplication.getApp().getBleDevices().get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        BluetoothDevice bluetoothDevice;
        if (view == null) {
            view = View.inflate(this.context, R.layout.layout_listitem_device_select, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
            viewHolder.imageViewHook = (ImageView) view.findViewById(R.id.imageViewHook);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (ServicesFragment.sceneBeanFragment != null) {
            if (ServicesFragment.sceneBeanFragment.equalsIgnoreCase(CommonConstant.LEDSTAGE)) {
                bluetoothDevice = LedBleApplication.getApp().getBleDevices().get(i);
            } else {
                bluetoothDevice = LedBleApplication.getApp().getBleDevices().get(i);
            }
            String name = bluetoothDevice.getName();
            if (name != null && name.length() > 0) {
                viewHolder.deviceName.setText(name);
            } else {
                viewHolder.deviceName.setText("unknown device");
            }
            viewHolder.deviceAddress.setText(bluetoothDevice.getAddress());
            if (this.hashSet.contains(bluetoothDevice)) {
                viewHolder.imageViewHook.setVisibility(0);
            } else {
                viewHolder.imageViewHook.setVisibility(4);
            }
        }
        return view;
    }

    /* loaded from: classes.dex */
    class ViewHolder {
        TextView deviceAddress;
        TextView deviceName;
        ImageView imageViewHook;

        ViewHolder() {
        }
    }
}
