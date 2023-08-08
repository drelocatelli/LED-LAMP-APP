package com.home.fragment.dmx03;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import butterknife.BindView;
import com.common.adapter.OnSeekBarChangeListenerAdapter;
import com.common.uitl.SharePersistent;
import com.common.uitl.Tool;
import com.common.view.SegmentedRadioGroup;
import com.home.activity.main.MainActivity_DMX03;
import com.home.base.LedBleApplication;
import com.home.base.LedBleFragment;
import com.home.constant.Constant;
import com.home.view.BlackWiteSelectView;
import com.home.view.ColorTextView;
import com.home.view.MyColorPicker;
import com.itheima.wheelpicker.WheelPicker;
import com.ledlamp.BuildConfig;
import com.ledlamp.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerView;

/* loaded from: classes.dex */
public class DMX03ModeFragment extends LedBleFragment {
    private static final int COLOR_DEFALUT = 0;
    private static final String TAG = "ModeFragment";
    private ColorTextView actionView;
    private ImageView backImage;
    private BlackWiteSelectView blackWiteSelectView2;
    private Button buttonPlay;
    private Button buttonSelectColorConfirm;
    private ArrayList<ColorTextView> colorTextViews;
    private int currentSelecColorFromPicker;
    private String diyViewTag;
    @BindView(R.id.imageViewOnOff)
    Button imageViewOnOff;
    private ColorPickerView imageViewPicker2;
    private ImageView iv_switch_select;
    private LinearLayout linearChouse_select;
    private LinearLayout llCoverMode;
    private LinearLayout llRing;
    @BindView(R.id.llmode)
    LinearLayout llmode;
    private MainActivity_DMX03 mActivity;
    private View mContentView;
    private PopupWindow mPopupWindow;
    private View menuView;
    private MyColorPicker myColor_select;
    @BindView(R.id.rlModeTopDIYCAR01BLE)
    RelativeLayout rlModeTopDIYCAR01BLE;
    @BindView(R.id.rlModeTopDIYCAR01DMX)
    RelativeLayout rlModeTopDIYCAR01DMX;
    private SeekBar seekBarBrightBarSC;
    @BindView(R.id.seekBarBrightNess)
    SeekBar seekBarBrightness;
    @BindView(R.id.seekBarMode)
    SeekBar seekBarMode;
    private SeekBar seekBarModeSC;
    @BindView(R.id.seekBarSpeed)
    SeekBar seekBarSpeedBar;
    private SeekBar seekBarSpeedBarSC;
    private SharedPreferences sharedPreferences;
    private SegmentedRadioGroup srgCover;
    private TextView textGreen_select;
    private TextView textRed_select;
    private TextView textViewBrightSC;
    @BindView(R.id.textViewBrightNess)
    TextView textViewBrightness;
    @BindView(R.id.textViewMode)
    TextView textViewMode;
    private TextView textViewModeSC;
    @BindView(R.id.textViewSpeed)
    TextView textViewSpeed;
    private TextView textViewSpeedSC;
    private TextView tvBlue_select;
    @BindView(R.id.wheelPicker)
    WheelPicker wheelPicker;
    private WheelPicker wheelPicker_tang;
    private Boolean isCAR01DMX = false;
    private int playBtnState = 0;
    private List<String> listName = new ArrayList();
    private List<String> listNubmer = new ArrayList();
    private int select_r = 255;
    private int select_g = 255;
    private int select_b = 255;

    @Override // com.home.base.LedBleFragment
    public void initEvent() {
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContentView = layoutInflater.inflate(R.layout.fragment_mode, viewGroup, false);
        this.menuView = layoutInflater.inflate(R.layout.activity_select_color, viewGroup, false);
        return this.mContentView;
    }

    @Override // com.home.base.LedBleFragment
    public void initData() {
        if (MainActivity_DMX03.getMainActivity() != null) {
            this.mActivity = MainActivity_DMX03.getMainActivity();
        }
        this.sharedPreferences = getActivity().getSharedPreferences(SharePersistent.getPerference(getContext(), Constant.CUSTOM_DIY_APPKEY), 0);
    }

    @Override // com.home.base.LedBleFragment
    public void initView() {
        if (this.mActivity != null) {
            this.wheelPicker.setData(dmxModel());
            this.seekBarMode.setMax(210);
        }
        this.wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.1
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker, Object obj, int i) {
                if (i >= 0) {
                    Log.e(DMX03ModeFragment.TAG, "onItemSelected: " + i);
                    if (DMX03ModeFragment.this.seekBarMode != null) {
                        DMX03ModeFragment.this.seekBarMode.setProgress(i);
                        List data = wheelPicker.getData();
                        Log.e(DMX03ModeFragment.TAG, "data: " + data.get(i));
                        TextView textView = DMX03ModeFragment.this.textViewMode;
                        textView.setText("" + data.get(i));
                    }
                    if (DMX03ModeFragment.this.mActivity != null) {
                        DMX03ModeFragment.this.mActivity.setRegModeNoInterval(Integer.parseInt(((String) DMX03ModeFragment.this.listNubmer.get(i)).trim()), DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                    }
                    Context context = DMX03ModeFragment.this.getContext();
                    SharePersistent.saveInt(context, LedBleApplication.getApp().getSceneBean() + DMX03ModeFragment.TAG + "mode-mode", i);
                }
            }
        });
        if (this.mActivity != null) {
            Button btnModePlay = MainActivity_DMX03.getMainActivity().getBtnModePlay();
            this.buttonPlay = btnModePlay;
            btnModePlay.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    DMX03ModeFragment.this.startAnimation(view);
                    if (DMX03ModeFragment.this.playBtnState == 0) {
                        DMX03ModeFragment.this.buttonPlay.setBackgroundResource(R.drawable.mode_play);
                        DMX03ModeFragment.this.playBtnState = 1;
                        if (DMX03ModeFragment.this.mActivity != null) {
                            DMX03ModeFragment.this.mActivity.setSPIPause(0);
                            return;
                        }
                        return;
                    }
                    DMX03ModeFragment.this.buttonPlay.setBackgroundResource(R.drawable.mode_stop);
                    DMX03ModeFragment.this.playBtnState = 0;
                    if (DMX03ModeFragment.this.mActivity != null) {
                        DMX03ModeFragment.this.mActivity.setSPIPause(1);
                    }
                }
            });
        }
        this.seekBarMode.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.3
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    List data = DMX03ModeFragment.this.wheelPicker.getData();
                    if (i < data.size()) {
                        TextView textView = DMX03ModeFragment.this.textViewMode;
                        textView.setText("" + data.get(i));
                        if (DMX03ModeFragment.this.mActivity != null && i >= 0 && i <= 210) {
                            DMX03ModeFragment.this.wheelPicker.setSelectedItemPosition(i);
                            DMX03ModeFragment.this.mActivity.setSPIModel(Integer.parseInt(((String) DMX03ModeFragment.this.listNubmer.get(i)).trim()));
                        }
                        Context context = DMX03ModeFragment.this.getContext();
                        SharePersistent.saveInt(context, LedBleApplication.getApp().getSceneBean() + DMX03ModeFragment.TAG + "mode-mode", i);
                    }
                }
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context = getContext();
            int i = SharePersistent.getInt(context, LedBleApplication.getApp().getSceneBean() + TAG + "mode-mode");
            this.seekBarMode.setProgress(i);
            this.wheelPicker.setSelectedItemPosition(i);
            List data = this.wheelPicker.getData();
            if (i < data.size()) {
                TextView textView = this.textViewMode;
                textView.setText("" + data.get(i));
            }
        }
        this.seekBarSpeedBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.4
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX03ModeFragment.this.mActivity == null || DMX03ModeFragment.this.textViewSpeed.getTag() == null) {
                    return;
                }
                if (DMX03ModeFragment.this.textViewSpeed.getTag().equals(100)) {
                    DMX03ModeFragment.this.mActivity.setSpeed(100, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                } else if (DMX03ModeFragment.this.textViewSpeed.getTag().equals(1)) {
                    DMX03ModeFragment.this.mActivity.setSpeed(1, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
                if (z) {
                    if (i2 != 0) {
                        DMX03ModeFragment.this.mActivity.setSpeed(i2, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                        DMX03ModeFragment.this.textViewSpeed.setText(DMX03ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i2)));
                        DMX03ModeFragment.this.textViewSpeed.setTag(Integer.valueOf(i2));
                        Context context2 = DMX03ModeFragment.this.getContext();
                        SharePersistent.saveInt(context2, LedBleApplication.getApp().getSceneBean() + DMX03ModeFragment.TAG + "speed", i2);
                        return;
                    }
                    DMX03ModeFragment.this.textViewSpeed.setText(DMX03ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, String.valueOf(1)));
                    if (DMX03ModeFragment.this.mActivity != null) {
                        DMX03ModeFragment.this.mActivity.setSpeed(1, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                    }
                    DMX03ModeFragment.this.textViewSpeed.setTag(1);
                }
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context2 = getContext();
            int i2 = SharePersistent.getInt(context2, LedBleApplication.getApp().getSceneBean() + TAG + "speed");
            if (i2 > 0) {
                this.seekBarSpeedBar.setProgress(i2);
                this.textViewSpeed.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(i2)));
            } else {
                this.textViewSpeed.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(80)));
                this.seekBarSpeedBar.setProgress(80);
            }
        }
        this.seekBarBrightness.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.5
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                super.onStopTrackingTouch(seekBar);
                if (DMX03ModeFragment.this.textViewBrightness == null || DMX03ModeFragment.this.textViewBrightness.getTag() == null) {
                    return;
                }
                if (DMX03ModeFragment.this.textViewBrightness.getTag().equals(100)) {
                    if (DMX03ModeFragment.this.mActivity != null) {
                        DMX03ModeFragment.this.mActivity.setBrightNess(100, false, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                    }
                } else if (!DMX03ModeFragment.this.textViewBrightness.getTag().equals(1) || DMX03ModeFragment.this.mActivity == null) {
                } else {
                    DMX03ModeFragment.this.mActivity.setBrightNess(1, false, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                }
            }

            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i3, boolean z) {
                if (z) {
                    if (i3 == 0) {
                        DMX03ModeFragment.this.textViewBrightness.setText(DMX03ModeFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(1)));
                        DMX03ModeFragment.this.textViewBrightness.setTag(1);
                        if (DMX03ModeFragment.this.mActivity != null) {
                            DMX03ModeFragment.this.mActivity.setBrightNess(1, false, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                            return;
                        }
                        return;
                    }
                    DMX03ModeFragment.this.textViewBrightness.setText(DMX03ModeFragment.this.getActivity().getResources().getString(R.string.brightness_set, String.valueOf(i3)));
                    DMX03ModeFragment.this.textViewBrightness.setTag(Integer.valueOf(i3));
                    if (DMX03ModeFragment.this.mActivity != null) {
                        DMX03ModeFragment.this.mActivity.setBrightNess(i3, false, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                    }
                    Context context3 = DMX03ModeFragment.this.getContext();
                    SharePersistent.saveInt(context3, LedBleApplication.getApp().getSceneBean() + DMX03ModeFragment.TAG + "bright", i3);
                }
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context3 = getContext();
            int i3 = SharePersistent.getInt(context3, LedBleApplication.getApp().getSceneBean() + TAG + "bright");
            if (i3 > 0) {
                this.seekBarBrightness.setProgress(i3);
                this.textViewBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i3)));
            } else {
                this.textViewBrightness.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
                this.seekBarBrightness.setProgress(100);
            }
        }
        this.myColor_select = (MyColorPicker) this.menuView.findViewById(R.id.myColor_select);
        this.linearChouse_select = (LinearLayout) this.menuView.findViewById(R.id.linearChouse_select);
        this.textRed_select = (TextView) this.menuView.findViewById(R.id.textRed_select);
        this.textGreen_select = (TextView) this.menuView.findViewById(R.id.textGreen_select);
        this.tvBlue_select = (TextView) this.menuView.findViewById(R.id.tvBlue_select);
        this.iv_switch_select = (ImageView) this.menuView.findViewById(R.id.iv_switch_select);
        SegmentedRadioGroup segmentedRadioGroup = (SegmentedRadioGroup) this.menuView.findViewById(R.id.srgCover);
        this.srgCover = segmentedRadioGroup;
        segmentedRadioGroup.check(R.id.rbRing);
        this.srgCover.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.6
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i4) {
                if (R.id.rbRing == i4) {
                    DMX03ModeFragment.this.llRing.setVisibility(0);
                    DMX03ModeFragment.this.llCoverMode.setVisibility(8);
                } else if (R.id.rbModle == i4) {
                    DMX03ModeFragment.this.llCoverMode.setVisibility(0);
                    DMX03ModeFragment.this.llRing.setVisibility(8);
                }
            }
        });
        this.wheelPicker_tang = (WheelPicker) this.menuView.findViewById(R.id.wheelPicker_tang);
        this.seekBarModeSC = (SeekBar) this.menuView.findViewById(R.id.seekBarModeSC);
        this.textViewModeSC = (TextView) this.menuView.findViewById(R.id.textViewModeSC);
        this.wheelPicker_tang.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.7
            @Override // com.itheima.wheelpicker.WheelPicker.OnItemSelectedListener
            public void onItemSelected(WheelPicker wheelPicker, Object obj, int i4) {
                if (i4 >= 0) {
                    MainActivity_DMX03 unused = DMX03ModeFragment.this.mActivity;
                    Log.e(DMX03ModeFragment.TAG, "wheelPicker_tang: " + i4);
                    if (DMX03ModeFragment.this.mActivity != null) {
                        DMX03ModeFragment dMX03ModeFragment = DMX03ModeFragment.this;
                        dMX03ModeFragment.currentSelecColorFromPicker = Integer.parseInt(((String) dMX03ModeFragment.dmxNoAutoListNubmer().get(i4)).trim());
                        DMX03ModeFragment.this.mActivity.setRegMode(Integer.parseInt(((String) DMX03ModeFragment.this.dmxNoAutoListNubmer().get(i4)).trim()), DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                    }
                    if (DMX03ModeFragment.this.seekBarModeSC != null) {
                        DMX03ModeFragment.this.seekBarModeSC.setProgress(i4);
                        List data2 = DMX03ModeFragment.this.wheelPicker_tang.getData();
                        TextView textView2 = DMX03ModeFragment.this.textViewModeSC;
                        textView2.setText("" + data2.get(i4));
                    }
                    SeekBar seekBar = DMX03ModeFragment.this.seekBarModeSC;
                    seekBar.setTag("" + DMX03ModeFragment.this.currentSelecColorFromPicker);
                }
            }
        });
        this.seekBarModeSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.8
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i4, boolean z) {
                if (z) {
                    List data2 = DMX03ModeFragment.this.wheelPicker_tang.getData();
                    if (DMX03ModeFragment.this.mActivity != null && i4 >= 0 && i4 <= 210) {
                        String trim = ((String) DMX03ModeFragment.this.listNubmer.get(i4)).trim();
                        Log.e(DMX03ModeFragment.TAG, "mode = " + trim);
                        Log.e(DMX03ModeFragment.TAG, "listNubmer.size() = " + DMX03ModeFragment.this.listNubmer.size());
                        DMX03ModeFragment.this.wheelPicker_tang.setSelectedItemPosition(i4);
                        DMX03ModeFragment dMX03ModeFragment = DMX03ModeFragment.this;
                        dMX03ModeFragment.currentSelecColorFromPicker = Integer.parseInt(((String) dMX03ModeFragment.dmxNoAutoListNubmer().get(i4)).trim());
                        DMX03ModeFragment.this.mActivity.setSPIModel(Integer.parseInt(((String) DMX03ModeFragment.this.dmxNoAutoListNubmer().get(i4)).trim()));
                        TextView textView2 = DMX03ModeFragment.this.textViewModeSC;
                        textView2.setText("" + data2.get(i4));
                    }
                    Context context4 = DMX03ModeFragment.this.getContext();
                    SharePersistent.saveInt(context4, LedBleApplication.getApp().getSceneBean() + DMX03ModeFragment.TAG + "modediymode", i4);
                }
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context4 = getContext();
            int i4 = SharePersistent.getInt(context4, LedBleApplication.getApp().getSceneBean() + TAG + "modediymode");
            if (i4 > 0) {
                this.seekBarModeSC.setProgress(i4);
                this.wheelPicker_tang.setSelectedItemPosition(i4);
                List data2 = this.wheelPicker_tang.getData();
                if (data2.size() > i4) {
                    TextView textView2 = this.textViewModeSC;
                    textView2.setText("" + data2.get(i4));
                }
            }
        }
        this.seekBarBrightBarSC = (SeekBar) this.menuView.findViewById(R.id.seekBarBrightNess);
        this.textViewBrightSC = (TextView) this.menuView.findViewById(R.id.textViewBrightNess);
        this.seekBarBrightBarSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.9
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i5, boolean z) {
                if (z) {
                    if (i5 == 0) {
                        if (DMX03ModeFragment.this.mActivity != null) {
                            DMX03ModeFragment.this.mActivity.setBrightNess(1, false, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                        }
                        DMX03ModeFragment.this.textViewBrightSC.setText(DMX03ModeFragment.this.getActivity().getResources().getString(R.string.brightness_set, 1));
                        return;
                    }
                    if (DMX03ModeFragment.this.mActivity != null) {
                        DMX03ModeFragment.this.mActivity.setBrightNess(i5, false, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                    }
                    DMX03ModeFragment.this.textViewBrightSC.setText(DMX03ModeFragment.this.getActivity().getResources().getString(R.string.brightness_set, Integer.valueOf(i5)));
                    Context context5 = DMX03ModeFragment.this.getContext();
                    SharePersistent.saveInt(context5, LedBleApplication.getApp().getSceneBean() + DMX03ModeFragment.TAG + "modediybright", i5);
                }
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context5 = getContext();
            int i5 = SharePersistent.getInt(context5, LedBleApplication.getApp().getSceneBean() + TAG + "modediybright");
            if (i5 > 0) {
                this.seekBarBrightBarSC.setProgress(i5);
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(i5)));
            } else {
                this.seekBarBrightBarSC.setProgress(100);
                this.textViewBrightSC.setText(getContext().getResources().getString(R.string.brightness_set, String.valueOf(100)));
            }
        }
        this.seekBarSpeedBarSC = (SeekBar) this.menuView.findViewById(R.id.seekBarSpeedSC);
        this.textViewSpeedSC = (TextView) this.menuView.findViewById(R.id.textViewSpeedSC);
        this.seekBarSpeedBarSC.setOnSeekBarChangeListener(new OnSeekBarChangeListenerAdapter() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.10
            @Override // com.common.adapter.OnSeekBarChangeListenerAdapter, android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i6, boolean z) {
                if (z) {
                    if (i6 == 0) {
                        if (DMX03ModeFragment.this.mActivity != null) {
                            DMX03ModeFragment.this.mActivity.setSpeed(1, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                        }
                        DMX03ModeFragment.this.textViewSpeedSC.setText(DMX03ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, 1));
                        return;
                    }
                    if (DMX03ModeFragment.this.mActivity != null) {
                        DMX03ModeFragment.this.mActivity.setSpeed(i6, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                    }
                    DMX03ModeFragment.this.textViewSpeedSC.setText(DMX03ModeFragment.this.getActivity().getResources().getString(R.string.speed_set, Integer.valueOf(i6)));
                    Context context6 = DMX03ModeFragment.this.getContext();
                    SharePersistent.saveInt(context6, LedBleApplication.getApp().getSceneBean() + DMX03ModeFragment.TAG + "modediyspeed", i6);
                }
            }
        });
        if (LedBleApplication.getApp().getSceneBean() != null) {
            Context context6 = getContext();
            int i6 = SharePersistent.getInt(context6, LedBleApplication.getApp().getSceneBean() + TAG + "modediyspeed");
            if (i6 > 0) {
                this.seekBarSpeedBarSC.setProgress(i6);
                this.textViewSpeedSC.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(i6)));
            } else {
                this.seekBarSpeedBarSC.setProgress(80);
                this.textViewSpeedSC.setText(getContext().getResources().getString(R.string.speed_set, String.valueOf(80)));
            }
        }
        if (this.mActivity != null) {
            this.buttonSelectColorConfirm = (Button) this.menuView.findViewById(R.id.buttonSelectColorConfirm);
            this.srgCover = (SegmentedRadioGroup) this.menuView.findViewById(R.id.srgCover);
            this.llRing = (LinearLayout) this.menuView.findViewById(R.id.llRing);
            this.llCoverMode = (LinearLayout) this.menuView.findViewById(R.id.llCoverMode);
            this.imageViewPicker2 = (ColorPickerView) this.menuView.findViewById(R.id.imageViewPicker2);
            this.blackWiteSelectView2 = (BlackWiteSelectView) this.menuView.findViewById(R.id.blackWiteSelectView2);
            initColorSelecterView();
        }
        ImageView imageView = (ImageView) this.menuView.findViewById(R.id.backImage);
        this.backImage = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DMX03ModeFragment.this.hideColorCover();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAnimation(View view) {
        if (getActivity() != null) {
            view.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.layout_scale));
        }
    }

    public void showColorCover(ColorTextView colorTextView, boolean z) {
        this.actionView = colorTextView;
        this.currentSelecColorFromPicker = 0;
        this.srgCover.check(R.id.rbRing);
        if (z) {
            this.srgCover.setVisibility(8);
            this.llRing.setVisibility(8);
            this.llCoverMode.setVisibility(0);
            this.blackWiteSelectView2.setVisibility(8);
        } else {
            this.srgCover.setVisibility(4);
            this.llRing.setVisibility(0);
            this.llCoverMode.setVisibility(8);
            this.blackWiteSelectView2.setVisibility(8);
        }
        if (this.mActivity != null) {
            this.wheelPicker_tang.setData(dmxNoAutoListName());
            this.seekBarModeSC.setMax(209);
        }
        PopupWindow popupWindow = new PopupWindow(this.menuView, -1, -1, true);
        this.mPopupWindow = popupWindow;
        popupWindow.showAtLocation(this.mContentView, 80, 0, 0);
    }

    private void initColorSelecterView() {
        this.imageViewPicker2.subscribe(new ColorObserver() { // from class: com.home.fragment.dmx03.DMX03ModeFragment$$ExternalSyntheticLambda0
            @Override // top.defaults.colorpicker.ColorObserver
            public final void onColor(int i, boolean z, boolean z2) {
                DMX03ModeFragment.this.m25xeb78ea9b(i, z, z2);
            }
        });
        this.myColor_select.setOnColorChangedListener(new MyColorPicker.OnColorChangedListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.12
            @Override // com.home.view.MyColorPicker.OnColorChangedListener
            public void onColorChanged(int i) {
                int[] rgb = Tool.getRGB(i);
                DMX03ModeFragment.this.blackWiteSelectView2.setStartColor(i);
                DMX03ModeFragment.this.currentSelecColorFromPicker = i;
                DMX03ModeFragment.this.updateRgbText(rgb, false);
                DMX03ModeFragment.this.select_r = rgb[0];
                DMX03ModeFragment.this.select_g = rgb[1];
                DMX03ModeFragment.this.select_b = rgb[2];
                DMX03ModeFragment.this.textRed_select.setText(DMX03ModeFragment.this.mActivity.getString(R.string.red, new Object[]{Integer.valueOf(DMX03ModeFragment.this.select_r)}));
                DMX03ModeFragment.this.textRed_select.setBackgroundColor(Color.rgb(DMX03ModeFragment.this.select_r, 0, 0));
                DMX03ModeFragment.this.textGreen_select.setText(DMX03ModeFragment.this.mActivity.getString(R.string.green, new Object[]{Integer.valueOf(DMX03ModeFragment.this.select_g)}));
                DMX03ModeFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, DMX03ModeFragment.this.select_g, 0));
                DMX03ModeFragment.this.tvBlue_select.setText(DMX03ModeFragment.this.mActivity.getString(R.string.blue, new Object[]{Integer.valueOf(DMX03ModeFragment.this.select_b)}));
                DMX03ModeFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, DMX03ModeFragment.this.select_b));
                SharePersistent.saveBrightData(DMX03ModeFragment.this.getActivity(), DMX03ModeFragment.this.diyViewTag + "bright", DMX03ModeFragment.this.diyViewTag + "bright", 32);
            }
        });
        this.iv_switch_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DMX03ModeFragment.this.imageViewPicker2.getVisibility() == 0) {
                    DMX03ModeFragment.this.iv_switch_select.setImageResource(R.drawable.bg_collor_picker);
                    DMX03ModeFragment.this.imageViewPicker2.setVisibility(8);
                    DMX03ModeFragment.this.myColor_select.setVisibility(0);
                    return;
                }
                DMX03ModeFragment.this.iv_switch_select.setImageResource(R.drawable.collor_picker);
                DMX03ModeFragment.this.myColor_select.setVisibility(8);
                DMX03ModeFragment.this.imageViewPicker2.setVisibility(0);
            }
        });
        final ColorPicker colorPicker = new ColorPicker(this.mActivity, this.select_r, this.select_g, this.select_b);
        colorPicker.setCallback(new ColorPickerCallback() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.14
            @Override // com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback
            public void onColorChosen(int i) {
                int[] rgb = Tool.getRGB(i);
                DMX03ModeFragment.this.currentSelecColorFromPicker = i;
                DMX03ModeFragment.this.blackWiteSelectView2.setStartColor(i);
                DMX03ModeFragment.this.updateRgbText(rgb, false);
                DMX03ModeFragment.this.select_r = Color.red(i);
                DMX03ModeFragment.this.select_g = Color.green(i);
                DMX03ModeFragment.this.select_b = Color.blue(i);
                DMX03ModeFragment.this.textRed_select.setBackgroundColor(Color.rgb(DMX03ModeFragment.this.select_r, 0, 0));
                DMX03ModeFragment.this.textGreen_select.setBackgroundColor(Color.rgb(0, DMX03ModeFragment.this.select_g, 0));
                DMX03ModeFragment.this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, DMX03ModeFragment.this.select_b));
                DMX03ModeFragment.this.textRed_select.setText(DMX03ModeFragment.this.mActivity.getString(R.string.red, new Object[]{Integer.valueOf(DMX03ModeFragment.this.select_r)}));
                DMX03ModeFragment.this.textGreen_select.setText(DMX03ModeFragment.this.mActivity.getString(R.string.green, new Object[]{Integer.valueOf(DMX03ModeFragment.this.select_g)}));
                DMX03ModeFragment.this.tvBlue_select.setText(DMX03ModeFragment.this.mActivity.getString(R.string.blue, new Object[]{Integer.valueOf(DMX03ModeFragment.this.select_b)}));
                DMX03ModeFragment.this.mActivity.setRgb(DMX03ModeFragment.this.select_r, DMX03ModeFragment.this.select_g, DMX03ModeFragment.this.select_b, true, DMX03ModeFragment.this.isCAR01DMX.booleanValue(), false, false);
                SharePersistent.saveBrightData(DMX03ModeFragment.this.getActivity(), DMX03ModeFragment.this.diyViewTag + "bright", DMX03ModeFragment.this.diyViewTag + "bright", 32);
            }
        });
        colorPicker.enableAutoClose();
        this.linearChouse_select.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.15
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                colorPicker.setColors(DMX03ModeFragment.this.select_r, DMX03ModeFragment.this.select_g, DMX03ModeFragment.this.select_b);
                colorPicker.show();
            }
        });
        this.blackWiteSelectView2.setOnSelectColor(new BlackWiteSelectView.OnSelectColor() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.16
            @Override // com.home.view.BlackWiteSelectView.OnSelectColor
            public void onColorSelect(int i, int i2) {
                if (i2 <= 0) {
                    i2 = 0;
                } else if (i2 >= 100) {
                    i2 = 100;
                }
                SharePersistent.saveBrightData(DMX03ModeFragment.this.getActivity(), DMX03ModeFragment.this.diyViewTag + "bright", DMX03ModeFragment.this.diyViewTag + "bright", i2);
                if (DMX03ModeFragment.this.mActivity != null) {
                    DMX03ModeFragment.this.mActivity.setBrightNess(i2, false, false, DMX03ModeFragment.this.isCAR01DMX.booleanValue());
                }
            }
        });
        View findViewById = this.menuView.findViewById(R.id.viewColors);
        ArrayList arrayList = new ArrayList();
        int[] iArr = {SupportMenu.CATEGORY_MASK, -16711936, -16776961, -1, InputDeviceCompat.SOURCE_ANY, -65281};
        HashMap hashMap = new HashMap();
        hashMap.put(Integer.valueOf(iArr[0]), Double.valueOf(0.0d));
        hashMap.put(Integer.valueOf(iArr[1]), Double.valueOf(1.0471975511965976d));
        hashMap.put(Integer.valueOf(iArr[2]), Double.valueOf(2.0943951023931953d));
        hashMap.put(Integer.valueOf(iArr[3]), Double.valueOf(3.141592653589793d));
        hashMap.put(Integer.valueOf(iArr[4]), Double.valueOf(4.1887902047863905d));
        hashMap.put(Integer.valueOf(iArr[5]), Double.valueOf(5.235987755982989d));
        for (int i = 1; i <= 6; i++) {
            View findViewWithTag = findViewById.findViewWithTag("viewColor" + i);
            findViewWithTag.setTag(Integer.valueOf(iArr[i + (-1)]));
            findViewWithTag.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.17
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int intValue = ((Integer) view.getTag()).intValue();
                    DMX03ModeFragment.this.currentSelecColorFromPicker = intValue;
                    DMX03ModeFragment.this.blackWiteSelectView2.setStartColor(intValue);
                    DMX03ModeFragment.this.imageViewPicker2.setInitialColor(intValue);
                    DMX03ModeFragment.this.updateRgbText(Tool.getRGB(intValue), true);
                    Tool.getRGB(intValue);
                }
            });
            arrayList.add(findViewWithTag);
        }
        this.buttonSelectColorConfirm.setOnClickListener(new View.OnClickListener() { // from class: com.home.fragment.dmx03.DMX03ModeFragment.18
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (DMX03ModeFragment.this.currentSelecColorFromPicker != 0) {
                    DMX03ModeFragment.this.actionView.setColor(DMX03ModeFragment.this.currentSelecColorFromPicker);
                    DMX03ModeFragment.this.sharedPreferences.edit().putInt(DMX03ModeFragment.this.actionView.getTag() + SharePersistent.getPerference(DMX03ModeFragment.this.getContext(), Constant.CUSTOM_DIY_APPKEY), DMX03ModeFragment.this.currentSelecColorFromPicker).commit();
                    if (DMX03ModeFragment.this.currentSelecColorFromPicker == 255) {
                        DMX03ModeFragment.this.actionView.setText("Auto");
                    } else {
                        ColorTextView colorTextView = DMX03ModeFragment.this.actionView;
                        colorTextView.setText("" + DMX03ModeFragment.this.currentSelecColorFromPicker);
                    }
                }
                DMX03ModeFragment.this.hideColorCover();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$initColorSelecterView$0$com-home-fragment-dmx03-DMX03ModeFragment  reason: not valid java name */
    public /* synthetic */ void m25xeb78ea9b(int i, boolean z, boolean z2) {
        if (z) {
            this.blackWiteSelectView2.setStartColor(i);
            this.currentSelecColorFromPicker = i;
            int[] rgb = Tool.getRGB(i);
            updateRgbText(rgb, false);
            int i2 = rgb[0];
            this.select_r = i2;
            this.select_g = rgb[1];
            this.select_b = rgb[2];
            this.textRed_select.setText(this.mActivity.getString(R.string.red, new Object[]{Integer.valueOf(i2)}));
            this.textRed_select.setBackgroundColor(Color.rgb(this.select_r, 0, 0));
            this.textGreen_select.setText(this.mActivity.getString(R.string.green, new Object[]{Integer.valueOf(this.select_g)}));
            this.textGreen_select.setBackgroundColor(Color.rgb(0, this.select_g, 0));
            this.tvBlue_select.setText(this.mActivity.getString(R.string.blue, new Object[]{Integer.valueOf(this.select_b)}));
            this.tvBlue_select.setBackgroundColor(Color.rgb(0, 0, this.select_b));
            SharePersistent.saveBrightData(getActivity(), this.diyViewTag + "bright", this.diyViewTag + "bright", 32);
        }
    }

    public Drawable getImage(String str) {
        Resources resources = getActivity().getResources();
        return getActivity().getResources().getDrawable(resources.getIdentifier("img_" + str, "drawable", BuildConfig.APPLICATION_ID));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideColorCover() {
        this.mPopupWindow.dismiss();
    }

    public void updateRgbText(int[] iArr, boolean z) {
        try {
            MainActivity_DMX03 mainActivity_DMX03 = this.mActivity;
            if (mainActivity_DMX03 != null) {
                mainActivity_DMX03.setRgb(iArr[0], iArr[1], iArr[2], !z, this.isCAR01DMX.booleanValue(), false, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> dmxModel() {
        String[] stringArray;
        this.listName.clear();
        this.listNubmer.clear();
        for (String str : getActivity().getResources().getStringArray(R.array.dmx03_model)) {
            this.listName.add(str.substring(0, str.lastIndexOf(",")));
            this.listNubmer.add(str.substring(str.lastIndexOf(",") + 1).trim());
        }
        return this.listName;
    }

    private List<String> dmxNoAutoListName() {
        ArrayList arrayList = new ArrayList();
        ArrayList<String> arrayList2 = new ArrayList(Arrays.asList(getActivity().getResources().getStringArray(R.array.dmx03_model)));
        arrayList2.remove(0);
        for (String str : arrayList2) {
            arrayList.add(str.substring(0, str.lastIndexOf(",")));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<String> dmxNoAutoListNubmer() {
        ArrayList arrayList = new ArrayList();
        ArrayList<String> arrayList2 = new ArrayList(Arrays.asList(getActivity().getResources().getStringArray(R.array.dmx03_model)));
        arrayList2.remove(0);
        for (String str : arrayList2) {
            arrayList.add(str.substring(str.lastIndexOf(",") + 1).trim());
        }
        return arrayList;
    }
}
