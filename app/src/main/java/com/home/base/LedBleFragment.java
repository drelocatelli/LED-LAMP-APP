package com.home.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;

/* loaded from: classes.dex */
public abstract class LedBleFragment extends Fragment {
    public LedBleActivity activity;
    public LedBleApplication baseApp;

    public abstract void initData();

    public abstract void initEvent();

    public abstract void initView();

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (LedBleActivity) activity;
        this.baseApp = LedBleActivity.getBaseApp();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ButterKnife.bind(this, view);
        initData();
        initView();
        initEvent();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
    }
}
