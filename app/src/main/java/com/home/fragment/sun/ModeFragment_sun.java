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
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import com.common.uitl.SharePersistent;
import com.common.view.SegmentedRadioGroup;
import com.common.view.toast.bamtoast.BamToast;
import com.home.activity.main.MainActivity_BLE;
import com.home.base.LedBleFragment;
import com.ledlamp.R;

/* loaded from: classes.dex */
public class ModeFragment_sun extends LedBleFragment {
    private static final String TAG = "ModeFragment_sun";
    private Handler handler;
    @BindView(R.id.linearLayoutTab1)
    View linearLayoutTab1;
    @BindView(R.id.linearLayoutTab2)
    View linearLayoutTab2;
    @BindView(R.id.ll_breath)
    LinearLayout ll_breath;
    @BindView(R.id.ll_flash)
    LinearLayout ll_flash;
    @BindView(R.id.ll_jump)
    LinearLayout ll_jump;
    @BindView(R.id.ll_learn)
    LinearLayout ll_learn;
    @BindView(R.id.ll_leisure)
    LinearLayout ll_leisure;
    @BindView(R.id.ll_natural)
    LinearLayout ll_natural;
    @BindView(R.id.ll_romantic)
    LinearLayout ll_romantic;
    @BindView(R.id.ll_rread)
    LinearLayout ll_rread;
    @BindView(R.id.ll_sleep)
    LinearLayout ll_sleep;
    @BindView(R.id.ll_soft)
    LinearLayout ll_soft;
    @BindView(R.id.ll_together)
    LinearLayout ll_together;
    MainActivity_BLE mainActivity;
    private Runnable runnable;
    @BindView(R.id.seekBarBrightNess_sun)
    SeekBar seekBarBrightNess_sun;
    @BindView(R.id.seekBarSensitivity_sun)
    SeekBar seekBarSensitivity_sun;
    @BindView(R.id.seekBarSpeed_sun)
    SeekBar seekBarSpeed_sun;
    private SegmentedRadioGroup segmentedRadioGroup;
    @BindView(R.id.textViewBrightNess_sun)
    TextView textViewBrightNess_sun;
    @BindView(R.id.textViewSensitivity_sun)
    TextView textViewSensitivity_sun;
    @BindView(R.id.textViewSpeed_sun)
    TextView textViewSpeed_sun;
    private boolean canSend = true;
    private boolean valueIs100 = false;

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_mode_fragment_sun, viewGroup, false);
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        MainActivity_BLE mainActivity_BLE = (MainActivity_BLE) getActivity();
        this.mainActivity = mainActivity_BLE;
        SegmentedRadioGroup segmentModeSun = mainActivity_BLE.getSegmentModeSun();
        this.segmentedRadioGroup = segmentModeSun;
        segmentModeSun.check(R.id.rbModeSun);
        SegmentedRadioGroup segmentedRadioGroup = this.segmentedRadioGroup;
        if (segmentedRadioGroup != null) {
            segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.sun.ModeFragment_sun.1
                @Override // android.widget.RadioGroup.OnCheckedChangeListener
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (R.id.rbModeSun == i) {
                        ModeFragment_sun.this.linearLayoutTab1.setVisibility(0);
                        ModeFragment_sun.this.linearLayoutTab2.setVisibility(8);
                    } else if (R.id.rbVcSun == i) {
                        ModeFragment_sun.this.linearLayoutTab1.setVisibility(8);
                        ModeFragment_sun.this.linearLayoutTab2.setVisibility(0);
                    }
                }
            });
        }
        this.seekBarBrightNess_sun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.fragment.sun.ModeFragment_sun.2
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
                        ModeFragment_sun.this.valueIs100 = true;
                    } else {
                        ModeFragment_sun.this.valueIs100 = false;
                    }
                    String string = ModeFragment_sun.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(i));
                    ModeFragment_sun.this.textViewBrightNess_sun.setText(string);
                    SharePersistent.saveInt(ModeFragment_sun.this.getContext(), "ModeFragment_sunbright", i);
                    if (ModeFragment_sun.this.handler == null) {
                        ModeFragment_sun.this.handler = new Handler();
                    }
                    if (!ModeFragment_sun.this.canSend) {
                        if (ModeFragment_sun.this.valueIs100) {
                            ModeFragment_sun.this.mainActivity.setBrightNess(100, false, false, false);
                            return;
                        }
                        return;
                    }
                    Log.e(ModeFragment_sun.TAG, "brightNess: " + string);
                    if (ModeFragment_sun.this.mainActivity != null) {
                        if (ModeFragment_sun.this.valueIs100) {
                            ModeFragment_sun.this.mainActivity.setBrightNess(100, false, false, false);
                        } else {
                            ModeFragment_sun.this.mainActivity.setBrightNess(i, false, false, false);
                        }
                    }
                    ModeFragment_sun.this.canSend = false;
                    ModeFragment_sun.this.runnable = new Runnable() { // from class: com.home.fragment.sun.ModeFragment_sun.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ModeFragment_sun.this.canSend = true;
                            if (ModeFragment_sun.this.valueIs100) {
                                ModeFragment_sun.this.mainActivity.setBrightNess(100, false, false, false);
                            }
                        }
                    };
                    ModeFragment_sun.this.handler.postDelayed(ModeFragment_sun.this.runnable, 200L);
                }
            }
        });
        int i = SharePersistent.getInt(getContext(), "ModeFragment_sunbright");
        if (i > 0) {
            this.seekBarBrightNess_sun.setProgress(i);
            this.textViewBrightNess_sun.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i)));
        } else {
            this.seekBarBrightNess_sun.setProgress(100);
            this.textViewBrightNess_sun.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
        }
        this.seekBarSpeed_sun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.fragment.sun.ModeFragment_sun.3
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z) {
                    if (i2 == 0) {
                        i2 = 1;
                    }
                    if (i2 == 100) {
                        ModeFragment_sun.this.valueIs100 = true;
                    } else {
                        ModeFragment_sun.this.valueIs100 = false;
                    }
                    ModeFragment_sun.this.textViewSpeed_sun.setText(ModeFragment_sun.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i2)));
                    SharePersistent.saveInt(ModeFragment_sun.this.getContext(), "ModeFragment_sunspeed", i2);
                    if (ModeFragment_sun.this.handler == null) {
                        ModeFragment_sun.this.handler = new Handler();
                    }
                    if (!ModeFragment_sun.this.canSend) {
                        if (ModeFragment_sun.this.valueIs100) {
                            ModeFragment_sun.this.mainActivity.setSpeed(100, false, false);
                            return;
                        }
                        return;
                    }
                    Log.e(ModeFragment_sun.TAG, "setSpeed: " + i2);
                    if (ModeFragment_sun.this.mainActivity != null) {
                        if (ModeFragment_sun.this.valueIs100) {
                            ModeFragment_sun.this.mainActivity.setSpeed(100, false, false);
                        } else {
                            ModeFragment_sun.this.mainActivity.setSpeed(i2, false, false);
                        }
                    }
                    ModeFragment_sun.this.canSend = false;
                    ModeFragment_sun.this.runnable = new Runnable() { // from class: com.home.fragment.sun.ModeFragment_sun.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ModeFragment_sun.this.canSend = true;
                            if (ModeFragment_sun.this.valueIs100) {
                                ModeFragment_sun.this.mainActivity.setSpeed(100, false, false);
                            }
                        }
                    };
                    ModeFragment_sun.this.handler.postDelayed(ModeFragment_sun.this.runnable, 200L);
                }
            }
        });
        int i2 = SharePersistent.getInt(getContext(), "ModeFragment_sunspeed");
        if (i2 > 0) {
            this.seekBarSpeed_sun.setProgress(i2);
            this.textViewSpeed_sun.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(i2)));
        } else {
            this.seekBarSpeed_sun.setProgress(80);
            this.textViewSpeed_sun.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(80)));
        }
        this.ll_rread.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setRegMode(1, false);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.sun_a));
            }
        });
        this.ll_learn.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setRegMode(2, false);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.sun_b));
            }
        });
        this.ll_together.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setRegMode(3, false);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.sun_c));
            }
        });
        this.ll_leisure.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setRegMode(4, false);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.sun_d));
            }
        });
        this.ll_romantic.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setRegMode(5, false);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.sun_e));
            }
        });
        this.ll_soft.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setRegMode(6, false);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.sun_f));
            }
        });
        this.ll_sleep.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setRegMode(7, false);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.sun_g));
            }
        });
        this.ll_natural.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setRegMode(8, false);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.sun_h));
            }
        });
        this.ll_jump.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.12
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setSunVcMode(0);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.jump));
            }
        });
        this.ll_flash.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setSunVcMode(1);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.flash));
            }
        });
        this.ll_breath.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.sun.ModeFragment_sun.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(ModeFragment_sun.this.getActivity(), R.anim.layout_scale));
                ModeFragment_sun.this.mainActivity.setSunVcMode(2);
                MainActivity_BLE mainActivity_BLE2 = ModeFragment_sun.this.mainActivity;
                ModeFragment_sun.showToast(mainActivity_BLE2, "" + ModeFragment_sun.this.getString(R.string.breathe));
            }
        });
        this.seekBarSensitivity_sun.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.home.fragment.sun.ModeFragment_sun.15
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    if (i3 == 0) {
                        i3 = 1;
                    }
                    if (i3 == 100) {
                        ModeFragment_sun.this.valueIs100 = true;
                    } else {
                        ModeFragment_sun.this.valueIs100 = false;
                    }
                    ModeFragment_sun.this.textViewSensitivity_sun.setText(ModeFragment_sun.this.getActivity().getResources().getString(R.string.sensitivity_set, String.valueOf(i3)));
                    SharePersistent.saveInt(ModeFragment_sun.this.getContext(), "ModeFragment_sunsensitivity", i3);
                    if (ModeFragment_sun.this.handler == null) {
                        ModeFragment_sun.this.handler = new Handler();
                    }
                    if (!ModeFragment_sun.this.canSend) {
                        if (ModeFragment_sun.this.valueIs100) {
                            ModeFragment_sun.this.mainActivity.setSunSensitivity(100);
                            return;
                        }
                        return;
                    }
                    Log.e(ModeFragment_sun.TAG, "setSunSensitivity: " + i3);
                    if (ModeFragment_sun.this.mainActivity != null) {
                        if (ModeFragment_sun.this.valueIs100) {
                            ModeFragment_sun.this.mainActivity.setSunSensitivity(100);
                        } else {
                            ModeFragment_sun.this.mainActivity.setSunSensitivity(i3);
                        }
                    }
                    ModeFragment_sun.this.canSend = false;
                    ModeFragment_sun.this.runnable = new Runnable() { // from class: com.home.fragment.sun.ModeFragment_sun.15.1
                        @Override // java.lang.Runnable
                        public void run() {
                            ModeFragment_sun.this.canSend = true;
                            if (ModeFragment_sun.this.valueIs100) {
                                ModeFragment_sun.this.mainActivity.setSunSensitivity(100);
                            }
                        }
                    };
                    ModeFragment_sun.this.handler.postDelayed(ModeFragment_sun.this.runnable, 200L);
                }
            }
        });
        int i3 = SharePersistent.getInt(getContext(), "ModeFragment_sunsensitivity");
        if (i3 > 0) {
            this.seekBarSensitivity_sun.setProgress(i3);
            this.textViewSensitivity_sun.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(i3)));
            return;
        }
        this.seekBarSensitivity_sun.setProgress(90);
        this.textViewSensitivity_sun.setText(getContext().getResources().getString(R.string.sensitivity_set, String.valueOf(90)));
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
