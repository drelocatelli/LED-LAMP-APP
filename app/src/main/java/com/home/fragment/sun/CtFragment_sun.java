package com.home.fragment.sun;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;
import com.common.uitl.SharePersistent;
import com.common.view.TempControlView;
import com.common.view.toast.bamtoast.BamToast;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleFragment;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class CtFragment_sun extends LedBleFragment {
    private static final String TAG = "CtFragment_sun";
    private Handler handler;
    @BindView(R.id.ll_naturallight)
    LinearLayout ll_naturallight;
    @BindView(R.id.ll_warmlight)
    LinearLayout ll_warmlight;
    @BindView(R.id.ll_whitelight)
    LinearLayout ll_whitelight;
    private MainActivity_BLE mActivity;
    private View mContentView;
    private Runnable runnable;
    @BindView(R.id.seekBarBrightNess_sun)
    SeekBar seekBarBrightNess_sun;
    @BindView(R.id.temp_control)
    TempControlView tempControl;
    @BindView(R.id.textViewBrightNess_sun)
    TextView textViewBrightNess_sun;
    private boolean canSend = true;
    private boolean valueIs100 = false;

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_ct, viewGroup, false);
        this.mContentView = inflate;
        return inflate;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        this.mActivity = (MainActivity_BLE) getActivity();
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        this.tempControl.setAngleRate(1);
        this.tempControl.setTemp(0, 100, 0);
        this.tempControl.setCanRotate(true);
        this.tempControl.setOnTempChangeListener(new TempControlView.OnTempChangeListener() { // from class: com.home.fragment.sun.CtFragment_sun.1
            @Override // com.common.view.TempControlView.OnTempChangeListener
            public void change(int i) {
                if (CtFragment_sun.this.handler == null) {
                    CtFragment_sun.this.handler = new Handler();
                }
                if (CtFragment_sun.this.canSend) {
                    Log.e(CtFragment_sun.TAG, "CtFragment_sun: " + i);
                    CtFragment_sun.this.mActivity.setCT(i, 100 - i);
                    CtFragment_sun.this.canSend = false;
                    CtFragment_sun.this.runnable = new Runnable() { // from class: com.home.fragment.sun.CtFragment_sun.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CtFragment_sun.this.canSend = true;
                        }
                    };
                    CtFragment_sun.this.handler.postDelayed(CtFragment_sun.this.runnable, 100L);
                }
            }
        });
        this.tempControl.setOnClickListener(new TempControlView.OnClickListener() { // from class: com.home.fragment.sun.CtFragment_sun.2
            @Override // com.common.view.TempControlView.OnClickListener
            public void onClick(int i) {
            }
        });
        this.ll_whitelight.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.CtFragment_sun.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CtFragment_sun ctFragment_sun = CtFragment_sun.this;
                ctFragment_sun.sendCMD(view, ctFragment_sun.getString(R.string.white_light), 0);
            }
        });
        this.ll_naturallight.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.CtFragment_sun.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CtFragment_sun ctFragment_sun = CtFragment_sun.this;
                ctFragment_sun.sendCMD(view, ctFragment_sun.getString(R.string.natural_light), 50);
            }
        });
        this.ll_warmlight.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.CtFragment_sun.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CtFragment_sun ctFragment_sun = CtFragment_sun.this;
                ctFragment_sun.sendCMD(view, ctFragment_sun.getString(R.string.warm_light), 100);
            }
        });
        this.seekBarBrightNess_sun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.fragment.sun.CtFragment_sun.6
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    if (i == 0) {
                        i = 1;
                    }
                    if (i == 100) {
                        CtFragment_sun.this.valueIs100 = true;
                    } else {
                        CtFragment_sun.this.valueIs100 = false;
                    }
                    CtFragment_sun.this.textViewBrightNess_sun.setText(CtFragment_sun.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(i)));
                    SharePersistent.saveInt(CtFragment_sun.this.getContext(), "CtFragment_sunbright", i);
                    if (CtFragment_sun.this.handler == null) {
                        CtFragment_sun.this.handler = new Handler();
                    }
                    if (CtFragment_sun.this.canSend) {
                        if (CtFragment_sun.this.mActivity != null) {
                            if (CtFragment_sun.this.valueIs100) {
                                CtFragment_sun.this.mActivity.setBrightNess(100, false, false, false);
                            } else {
                                CtFragment_sun.this.mActivity.setBrightNess(i, false, false, false);
                            }
                        }
                        CtFragment_sun.this.canSend = false;
                        CtFragment_sun.this.runnable = new Runnable() { // from class: com.home.fragment.sun.CtFragment_sun.6.1
                            @Override // java.lang.Runnable
                            public void run() {
                                CtFragment_sun.this.canSend = true;
                                if (CtFragment_sun.this.valueIs100) {
                                    CtFragment_sun.this.mActivity.setBrightNess(100, false, false, false);
                                }
                            }
                        };
                        CtFragment_sun.this.handler.postDelayed(CtFragment_sun.this.runnable, 200L);
                    }
                }
            }
        });
        int i = SharePersistent.getInt(getContext(), "CtFragment_sunbright");
        if (i > 0) {
            this.seekBarBrightNess_sun.setProgress(i);
            this.textViewBrightNess_sun.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
            return;
        }
        this.seekBarBrightNess_sun.setProgress(100);
        this.textViewBrightNess_sun.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendCMD(View view, String str, int i) {
        this.mActivity.setCT(i, 100 - i);
        view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
        FragmentActivity activity = getActivity();
        showToast(activity, "" + str);
    }

    public static void showToast(Context context, String str) {
        BamToast.showText(context, str);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacks(this.runnable);
        }
    }
}
