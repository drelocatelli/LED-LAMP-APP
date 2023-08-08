package me.imid.swipebacklayout.lib.app;

import android.app.Activity;
import java.lang.ref.WeakReference;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;

/* loaded from: classes.dex */
public class SwipeBackListenerActivityAdapter implements SwipeBackLayout.SwipeListenerEx {
    private final WeakReference<Activity> mActivity;

    @Override // me.imid.swipebacklayout.lib.SwipeBackLayout.SwipeListener
    public void onScrollOverThreshold() {
    }

    @Override // me.imid.swipebacklayout.lib.SwipeBackLayout.SwipeListener
    public void onScrollStateChange(int i, float f) {
    }

    public SwipeBackListenerActivityAdapter(Activity activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    @Override // me.imid.swipebacklayout.lib.SwipeBackLayout.SwipeListener
    public void onEdgeTouch(int i) {
        Activity activity = this.mActivity.get();
        if (activity != null) {
            Utils.convertActivityToTranslucent(activity);
        }
    }

    @Override // me.imid.swipebacklayout.lib.SwipeBackLayout.SwipeListenerEx
    public void onContentViewSwipedBack() {
        Activity activity = this.mActivity.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }
}
