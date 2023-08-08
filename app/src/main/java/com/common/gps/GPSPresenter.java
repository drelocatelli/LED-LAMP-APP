package com.common.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;

/* loaded from: classes.dex */
public class GPSPresenter {
    private String GPS_ACTION = "android.location.PROVIDERS_CHANGED";
    private Context mContext;
    private GPS_Interface mInterface;
    private Receiver receiver;

    public GPSPresenter(Context context, GPS_Interface gPS_Interface) {
        this.mContext = context;
        this.mInterface = gPS_Interface;
        registerGPSReceiver();
    }

    private void registerGPSReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(this.GPS_ACTION);
        Receiver receiver = new Receiver();
        this.receiver = receiver;
        this.mContext.registerReceiver(receiver, intentFilter);
    }

    public void onDestroy() {
        Receiver receiver = this.receiver;
        if (receiver != null) {
            this.mContext.unregisterReceiver(receiver);
        }
        if (this.mContext != null) {
            this.mContext = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Receiver extends BroadcastReceiver {
        Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().matches(GPSPresenter.this.GPS_ACTION) || GPSPresenter.this.mInterface == null) {
                return;
            }
            GPSPresenter.this.mInterface.gpsSwitchState(GPSPresenter.this.gpsIsOpen(context));
        }
    }

    public boolean gpsIsOpen(Context context) {
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService("location");
        boolean isProviderEnabled = locationManager.isProviderEnabled("gps");
        locationManager.isProviderEnabled("network");
        return isProviderEnabled;
    }
}
