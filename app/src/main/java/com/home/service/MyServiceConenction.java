package com.home.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.home.service.BluetoothLeServiceSingle;

/* loaded from: classes.dex */
public class MyServiceConenction implements ServiceConnection {
    private BluetoothLeServiceSingle bluetoothLeService;
    private ServiceConnectListener serviceConnectListener;

    /* loaded from: classes.dex */
    public interface ServiceConnectListener {
        void onConnect(ComponentName componentName, IBinder iBinder, BluetoothLeServiceSingle bluetoothLeServiceSingle);

        void onDisConnect(ComponentName componentName);
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        BluetoothLeServiceSingle service = ((BluetoothLeServiceSingle.LocalBinder) iBinder).getService();
        this.bluetoothLeService = service;
        ServiceConnectListener serviceConnectListener = this.serviceConnectListener;
        if (serviceConnectListener != null) {
            serviceConnectListener.onConnect(componentName, iBinder, service);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.bluetoothLeService = null;
        ServiceConnectListener serviceConnectListener = this.serviceConnectListener;
        if (serviceConnectListener != null) {
            serviceConnectListener.onDisConnect(componentName);
        }
    }

    public BluetoothLeServiceSingle getBluetoothLeService() {
        return this.bluetoothLeService;
    }

    public void setBluetoothLeService(BluetoothLeServiceSingle bluetoothLeServiceSingle) {
        this.bluetoothLeService = bluetoothLeServiceSingle;
    }

    public ServiceConnectListener getServiceConnectListener() {
        return this.serviceConnectListener;
    }

    public void setServiceConnectListener(ServiceConnectListener serviceConnectListener) {
        this.serviceConnectListener = serviceConnectListener;
    }
}
