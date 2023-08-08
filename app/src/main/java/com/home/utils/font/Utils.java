package com.home.utils.font;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public final class Utils {
    private static Application sApplication;
    static WeakReference<Activity> sTopActivityWeakRef;
    static List<Activity> sActivityList = new LinkedList();
    private static Application.ActivityLifecycleCallbacks mCallbacks = new Application.ActivityLifecycleCallbacks() { // from class: com.home.utils.font.Utils.1
        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityPaused(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStopped(Activity activity) {
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityCreated(Activity activity, Bundle bundle) {
            Utils.sActivityList.add(activity);
            Utils.setTopActivityWeakRef(activity);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityStarted(Activity activity) {
            Utils.setTopActivityWeakRef(activity);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityResumed(Activity activity) {
            Utils.setTopActivityWeakRef(activity);
        }

        @Override // android.app.Application.ActivityLifecycleCallbacks
        public void onActivityDestroyed(Activity activity) {
            Utils.sActivityList.remove(activity);
        }
    };

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Application application) {
        sApplication = application;
        application.registerActivityLifecycleCallbacks(mCallbacks);
    }

    public static Application getApp() {
        Application application = sApplication;
        Objects.requireNonNull(application, "u should init first");
        return application;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setTopActivityWeakRef(Activity activity) {
        WeakReference<Activity> weakReference = sTopActivityWeakRef;
        if (weakReference == null || !activity.equals(weakReference.get())) {
            sTopActivityWeakRef = new WeakReference<>(activity);
        }
    }
}
