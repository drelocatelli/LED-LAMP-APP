package me.imid.swipebacklayout.lib.app;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;

/* loaded from: classes.dex */
public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SwipeBackActivityHelper swipeBackActivityHelper = new SwipeBackActivityHelper(this);
        this.mHelper = swipeBackActivityHelper;
        swipeBackActivityHelper.onActivityCreate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity
    public void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        this.mHelper.onPostCreate();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity
    public View findViewById(int i) {
        SwipeBackActivityHelper swipeBackActivityHelper;
        View findViewById = super.findViewById(i);
        return (findViewById != null || (swipeBackActivityHelper = this.mHelper) == null) ? findViewById : swipeBackActivityHelper.findViewById(i);
    }

    @Override // me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
    public SwipeBackLayout getSwipeBackLayout() {
        return this.mHelper.getSwipeBackLayout();
    }

    @Override // me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
    public void setSwipeBackEnable(boolean z) {
        getSwipeBackLayout().setEnableGesture(z);
    }

    @Override // me.imid.swipebacklayout.lib.app.SwipeBackActivityBase
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
