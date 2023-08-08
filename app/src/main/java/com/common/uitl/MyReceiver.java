package com.common.uitl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    boolean aa = true;
    public MyListener myListener;

    /* loaded from: classes.dex */
    public interface MyListener {
        void onListener(int i);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra("level", 0);
        if (intExtra >= 15 || !this.aa) {
            return;
        }
        this.aa = false;
        this.myListener.onListener(intExtra);
    }

    public void setMyListener(MyListener myListener) {
        this.myListener = myListener;
    }
}
