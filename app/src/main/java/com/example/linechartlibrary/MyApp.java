package com.example.linechartlibrary;

import android.content.Context;

/* loaded from: classes.dex */
public class MyApp {
    private static Context applicationContext;

    public static void setInstance(Context context) {
        applicationContext = context;
    }

    public static Context getInstance() {
        return applicationContext;
    }
}
