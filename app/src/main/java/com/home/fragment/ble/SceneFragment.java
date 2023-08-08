package com.home.fragment.ble;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.common.view.toast.bamtoast.BamToast;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleFragment;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class SceneFragment extends LedBleFragment {
    @BindView(R.id.ll_SceneAfternoontea)
    LinearLayout ll_SceneAfternoontea;
    @BindView(R.id.ll_SceneCandlelightdinner)
    LinearLayout ll_SceneCandlelightdinner;
    @BindView(R.id.ll_SceneColorful)
    LinearLayout ll_SceneColorful;
    @BindView(R.id.ll_SceneDrivemidge)
    LinearLayout ll_SceneDrivemidge;
    @BindView(R.id.ll_SceneParty)
    LinearLayout ll_SceneParty;
    @BindView(R.id.ll_SceneReading)
    LinearLayout ll_SceneReading;
    @BindView(R.id.ll_SceneSea)
    LinearLayout ll_SceneSea;
    @BindView(R.id.ll_SceneSunrise)
    LinearLayout ll_SceneSunrise;
    @BindView(R.id.ll_SceneSunset)
    LinearLayout ll_SceneSunset;
    @BindView(R.id.ll_SceneTropical)
    LinearLayout ll_SceneTropical;
    @BindView(R.id.ll_SceneYoga)
    LinearLayout ll_SceneYoga;
    private MainActivity_BLE mActivity;
    @BindView(R.id.relativeTab2)
    View relativeTab2;
    private int style = 0;

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_scene_smart, viewGroup, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
    }

    public static void showToast(Context context, String str) {
        BamToast.showText(context, str);
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        this.mActivity = (MainActivity_BLE) getActivity();
        this.ll_SceneSunrise.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegMode(1, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.sunrise_mode));
            }
        });
        this.ll_SceneSunset.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegMode(2, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.sunset_mode));
            }
        });
        this.ll_SceneColorful.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(3, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.lightning_mode));
            }
        });
        this.ll_SceneAfternoontea.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(4, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.thunderstorm_mode));
            }
        });
        this.ll_SceneDrivemidge.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(5, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.sunshine_mode));
            }
        });
        this.ll_SceneYoga.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(6, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.marine_mode));
            }
        });
        this.ll_SceneParty.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(7, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.coral_artistic_conception));
            }
        });
        this.ll_SceneTropical.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(8, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.aquatic_mode));
            }
        });
        this.ll_SceneSea.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(9, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.sleep_mode));
            }
        });
        this.ll_SceneReading.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(10, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.deep_sea_mode));
            }
        });
        this.ll_SceneCandlelightdinner.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.ble.SceneFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SceneFragment.this.startAnimation(view);
                SceneFragment.this.mActivity.setRegModeNoInterval(11, false);
                MainActivity_BLE mainActivity_BLE = SceneFragment.this.mActivity;
                SceneFragment.showToast(mainActivity_BLE, "" + SceneFragment.this.getString(R.string.colorful_mode));
            }
        });
    }
}
