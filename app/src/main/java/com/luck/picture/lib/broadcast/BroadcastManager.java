package com.luck.picture.lib.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class BroadcastManager {
    private static final String TAG = "BroadcastManager";
    private String action;
    private Intent intent;
    private LocalBroadcastManager localBroadcastManager;

    public static BroadcastManager getInstance(Context context) {
        BroadcastManager broadcastManager = new BroadcastManager();
        broadcastManager.localBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        return broadcastManager;
    }

    public BroadcastManager intent(Intent intent) {
        this.intent = intent;
        return this;
    }

    public BroadcastManager action(String str) {
        this.action = str;
        return this;
    }

    public BroadcastManager extras(Bundle bundle) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtras(bundle);
        return this;
    }

    public BroadcastManager put(String str, ArrayList<? extends Parcelable> arrayList) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, arrayList);
        return this;
    }

    public BroadcastManager put(String str, Parcelable[] parcelableArr) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, parcelableArr);
        return this;
    }

    public BroadcastManager put(String str, Parcelable parcelable) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, parcelable);
        return this;
    }

    public BroadcastManager put(String str, float f) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, f);
        return this;
    }

    public BroadcastManager put(String str, double d) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, d);
        return this;
    }

    public BroadcastManager put(String str, long j) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, j);
        return this;
    }

    public BroadcastManager put(String str, boolean z) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, z);
        return this;
    }

    public BroadcastManager put(String str, int i) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, i);
        return this;
    }

    public BroadcastManager put(String str, String str2) {
        createIntent();
        Intent intent = this.intent;
        if (intent == null) {
            Log.e(TAG, "intent create failed");
            return this;
        }
        intent.putExtra(str, str2);
        return this;
    }

    private void createIntent() {
        if (this.intent == null) {
            Log.d(TAG, "intent is not created");
        }
        if (this.intent == null) {
            if (!TextUtils.isEmpty(this.action)) {
                this.intent = new Intent(this.action);
            }
            Log.d(TAG, "intent created with action");
        }
    }

    public void broadcast() {
        String str;
        createIntent();
        Intent intent = this.intent;
        if (intent == null || (str = this.action) == null) {
            return;
        }
        intent.setAction(str);
        LocalBroadcastManager localBroadcastManager = this.localBroadcastManager;
        if (localBroadcastManager != null) {
            localBroadcastManager.sendBroadcast(this.intent);
        }
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver, List<String> list) {
        if (broadcastReceiver == null || list == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        if (list != null) {
            for (String str : list) {
                intentFilter.addAction(str);
            }
        }
        LocalBroadcastManager localBroadcastManager = this.localBroadcastManager;
        if (localBroadcastManager != null) {
            localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver, String... strArr) {
        if (strArr == null || strArr.length <= 0) {
            return;
        }
        registerReceiver(broadcastReceiver, Arrays.asList(strArr));
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver == null) {
            return;
        }
        try {
            this.localBroadcastManager.unregisterReceiver(broadcastReceiver);
        } catch (Exception unused) {
        }
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver, String... strArr) {
        unregisterReceiver(broadcastReceiver);
    }
}
