package com.home.adapter;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public class BluetoothDataModel {
    private BluetoothDevice device;
    private boolean isSeleted = false;

    public void setDevice(BluetoothDevice bluetoothDevice) {
        this.device = bluetoothDevice;
    }

    public void setSeleted(boolean z) {
        this.isSeleted = z;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public boolean isSeleted() {
        return this.isSeleted;
    }
}
